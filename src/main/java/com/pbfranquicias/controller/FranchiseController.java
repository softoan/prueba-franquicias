package com.pbfranquicias.controller;

import com.pbfranquicias.dto.*;
import com.pbfranquicias.service.interfaces.FranchiseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
@Validated
public class FranchiseController {

    private final FranchiseService service;

    // Crear franquicia
    @PostMapping
    public Mono<ResponseEntity<Map<String, String>>> createFranchise(
            @Valid @RequestBody FranchiseCreateDTO dto) {

        return service.createFranchise(dto)
                .map(id -> ResponseEntity
                        .created(URI.create("/api/franchises/" + id))
                        .body(Map.of("id", id)));
    }

    // Agregar sucursal
    @PostMapping("/{id}/branches")
    public Mono<ResponseEntity<Map<String, String>>> addBranch(
            @PathVariable("id") String id,
            @Valid @RequestBody BranchCreateDTO dto) {

        return service.addBranch(id, dto)
                .map(branchId -> ResponseEntity
                        .created(URI.create("/api/franchises/" + id + "/branches/" + branchId))
                        .body(Map.of("id", branchId)));
    }

    // Agregar producto
    @PostMapping("/{id}/branches/{branchId}/products")
    public Mono<ResponseEntity<Map<String, String>>> addProduct(
            @PathVariable("id") String id,
            @PathVariable("branchId") String branchId,
            @Valid @RequestBody ProductCreateDTO dto) {
        return service.addProduct(id, branchId, dto)
                .map(productId -> ResponseEntity
                        .created(URI.create("/api/franchises/" + id + "/branches/" + branchId + "/products/" + productId))
                        .body(Map.of("id", productId)));
    }

    // Eliminar producto
    @DeleteMapping("/{id}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<Void>> deleteProduct(
            @PathVariable("id") String id,
            @PathVariable("branchId") String branchId,
            @PathVariable("productId") String productId) {

        return service.deleteProduct(id, branchId, productId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // Actualizar stock
    @PatchMapping("/{id}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<Void>> updateStock(
            @PathVariable("id") String id,
            @PathVariable("branchId") String branchId,
            @PathVariable("productId") String productId,
            @Valid @RequestBody StockUpdateDTO dto) {

            return service.updateStock(id, branchId, productId, dto)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // Obtener productos con stock máximo
    @GetMapping("/{id}/max-stock")
    public Flux<?> getMaxStock(@PathVariable String id) {
        return service.getMaxStockByBranch(id);
    }

    // Actualizar nombre de la franquicia
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateFranchiseName(
            @PathVariable String id,
            @Valid @RequestBody FranchiseCreateDTO dto) {

        return service.updateFranchiseName(id, dto)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // Actualizar nombre de sucursal
    @PatchMapping("/{id}/branches/{branchId}")
    public Mono<ResponseEntity<Void>> updateBranchName(
            @PathVariable String id,
            @PathVariable String branchId,
            @Valid @RequestBody BranchCreateDTO dto) {

        return service.updateBranchName(id, branchId, dto)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // Actualizar nombre de producto
    @PatchMapping("/{id}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<Void>> updateProductName(
            @PathVariable String id,
            @PathVariable String branchId,
            @PathVariable String productId,
            @Valid @RequestBody ProductCreateDTO dto) {

        return service.updateProductName(id, branchId, productId, dto)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
