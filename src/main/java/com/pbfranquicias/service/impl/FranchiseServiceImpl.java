package com.pbfranquicias.service.impl;

import com.pbfranquicias.dto.*;
import com.pbfranquicias.entity.*;
import com.pbfranquicias.repository.FranchiseRepository;
import com.pbfranquicias.service.interfaces.FranchiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<String> createFranchise(FranchiseCreateDTO dto) {
        return franchiseRepository.existsByNameIgnoreCase(dto.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("La franquicia ya existe."));
                    }
                    Franchise f = Franchise.builder()
                            .id(UUID.randomUUID().toString())
                            .name(dto.getName())
                            .branches(new ArrayList<>())
                            .build();
                    return franchiseRepository.save(f).map(Franchise::getId);
                });
    }

    @Override
    public Mono<String> addBranch(String franchiseId, BranchCreateDTO dto) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    Branch b = Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(dto.getName())
                            .products(new ArrayList<>())
                            .build();
                    f.getBranches().add(b);
                    return franchiseRepository.save(f).thenReturn(b.getId());
                });
    }

    @Override
    public Mono<String> addProduct(String franchiseId, String branchId, ProductCreateDTO dto) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> Mono.justOrEmpty(
                                f.getBranches().stream()
                                        .filter(b -> Objects.equals(b.getId(), branchId))
                                        .findFirst()
                        )
                        .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Sucursal no encontrada")))
                        .flatMap(branch -> {
                            Product p = Product.builder()
                                    .id(UUID.randomUUID().toString())
                                    .name(dto.getName())
                                    .stock(dto.getStock())
                                    .build();

                            branch.getProducts().add(p);
                            return franchiseRepository.save(f).thenReturn(p.getId());
                        }));
    }

    @Override
    public Mono<Void> deleteProduct(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    Branch branch = f.getBranches().stream()
                            .filter(b -> Objects.equals(b.getId(), branchId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Sucursal no encontrada"));

                    int before = branch.getProducts().size();
                    branch.setProducts(branch.getProducts().stream()
                            .filter(p -> !Objects.equals(p.getId(), productId))
                            .collect(Collectors.toList()));

                    if (branch.getProducts().size() == before) {
                        return Mono.error(new com.pbfranquicias.exception.NotFoundException("Producto no encontrado"));
                    }

                    return franchiseRepository.save(f).then();
                });
    }

    @Override
    public Mono<Void> updateStock(String franchiseId, String branchId, String productId, StockUpdateDTO dto) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    Branch b = f.getBranches().stream()
                            .filter(br -> Objects.equals(br.getId(), branchId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Sucursal no encontrada"));

                    Product p = b.getProducts().stream()
                            .filter(pr -> Objects.equals(pr.getId(), productId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Producto no encontrado"));

                    p.setStock(dto.getStock());
                    return franchiseRepository.save(f).then();
                });
    }

    @Override
    public Flux<Map<String, Object>> getMaxStockByBranch(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMapMany(f -> Flux.fromIterable(f.getBranches()))
                .filter(branch -> branch.getProducts() != null && !branch.getProducts().isEmpty())
                .map(branch -> {
                    Product max = branch.getProducts().stream()
                            .max(Comparator.comparingInt(pr -> pr.getStock() == null ? 0 : pr.getStock()))
                            .orElse(null);

                    Map<String, Object> m = new HashMap<>();
                    m.put("branchId", branch.getId());
                    m.put("branchName", branch.getName());
                    if (max != null) {
                        m.put("productId", max.getId());
                        m.put("productName", max.getName());
                        m.put("stock", max.getStock());
                    }
                    return m;
                });
    }

    @Override
    public Mono<Void> updateFranchiseName(String franchiseId, FranchiseCreateDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            return Mono.error(new IllegalArgumentException("Nombre requerido"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    f.setName(dto.getName());
                    return franchiseRepository.save(f).then();
                });
    }

    @Override
    public Mono<Void> updateBranchName(String franchiseId, String branchId, BranchCreateDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            return Mono.error(new IllegalArgumentException("Nombre requerido"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    Branch b = f.getBranches().stream()
                            .filter(br -> Objects.equals(br.getId(), branchId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Sucursal no encontrada"));
                    b.setName(dto.getName());
                    return franchiseRepository.save(f).then();
                });
    }

    @Override
    public Mono<Void> updateProductName(String franchiseId, String branchId, String productId, ProductCreateDTO dto) {
        if (!StringUtils.hasText(dto.getName())) {
            return Mono.error(new IllegalArgumentException("Nombre requerido"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new com.pbfranquicias.exception.NotFoundException("Franquicia no encontrada")))
                .flatMap(f -> {
                    Branch b = f.getBranches().stream()
                            .filter(br -> Objects.equals(br.getId(), branchId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Sucursal no encontrada"));

                    Product p = b.getProducts().stream()
                            .filter(pr -> Objects.equals(pr.getId(), productId))
                            .findFirst()
                            .orElseThrow(() -> new com.pbfranquicias.exception.NotFoundException("Product not found"));

                    p.setName(dto.getName());
                    return franchiseRepository.save(f).then();
                });
    }
}