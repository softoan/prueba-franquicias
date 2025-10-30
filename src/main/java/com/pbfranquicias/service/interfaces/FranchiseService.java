package com.pbfranquicias.service.interfaces;

import com.pbfranquicias.dto.BranchCreateDTO;
import com.pbfranquicias.dto.FranchiseCreateDTO;
import com.pbfranquicias.dto.ProductCreateDTO;
import com.pbfranquicias.dto.StockUpdateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseService {

    Mono<String> createFranchise(FranchiseCreateDTO dto);

    Mono<String> addBranch(String franchiseId, BranchCreateDTO dto);

    Mono<String> addProduct(String franchiseId, String branchId, ProductCreateDTO dto);

    Mono<Void> deleteProduct(String franchiseId, String branchId, String productId);

    Mono<Void> updateStock(String franchiseId, String branchId, String productId, StockUpdateDTO dto);

    Flux<?> getMaxStockByBranch(String franchiseId);

    Mono<Void> updateFranchiseName(String franchiseId, FranchiseCreateDTO dto);

    Mono<Void> updateBranchName(String franchiseId, String branchId, BranchCreateDTO dto);

    Mono<Void> updateProductName(String franchiseId, String branchId, String productId, ProductCreateDTO dto);
}
