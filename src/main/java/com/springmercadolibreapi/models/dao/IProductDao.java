package com.springmercadolibreapi.models.dao;

import com.springmercadolibreapi.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IProductDao extends ReactiveMongoRepository<Product, String> {

    public Mono<Product> findByName(String name);

}
