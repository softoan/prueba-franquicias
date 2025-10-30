package com.pbfranquicias.repository;

import com.pbfranquicias.entity.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {
    Mono<Boolean> existsByNameIgnoreCase(String name);
}