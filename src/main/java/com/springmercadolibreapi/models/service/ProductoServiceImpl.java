package com.springmercadolibreapi.models.service;

import com.springmercadolibreapi.models.dao.ICategorieDao;
import com.springmercadolibreapi.models.dao.IProductDao;
import com.springmercadolibreapi.models.documents.Category;
import com.springmercadolibreapi.models.documents.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private IProductDao daoProduct;

    @Autowired
    private ICategorieDao daoCategorie;


    @Override
    public Flux<Product> findAll() {
        return daoProduct.findAll();
    }

    @Override
    public Flux<Category> findAllCategory() {
        return daoCategorie.findAll();
    }

    @Override
    public Mono<Category> findCategorieById(String id) {
        return daoCategorie.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category categorie) {
        return daoCategorie.save(categorie);
    }

    @Override
    public Mono<Product> findById(String id) {
        return daoProduct.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return daoProduct.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return daoProduct.delete(product);
    }


    @Override
    public Mono<Product> findByName(String name) {
        return daoProduct.findByName(name);
    }
}
