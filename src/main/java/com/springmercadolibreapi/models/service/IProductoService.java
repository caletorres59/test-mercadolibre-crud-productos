package com.springmercadolibreapi.models.service;

import com.springmercadolibreapi.models.documents.Category;
import com.springmercadolibreapi.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductoService {

    public Flux<Product> findAll();
    public Mono<Product> findById(String id);
    public Mono<Product> save(Product product);
    public Mono<Void> delete(Product product);

    public Mono<Product> findByName(String name);

    //Categorie
    public Flux<Category> findAllCategory();

    public Mono<Category> findCategorieById(String id);

    public Mono<Category> saveCategory(Category categorie);
}
