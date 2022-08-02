package com.springmercadolibreapi.models.dao;

import com.springmercadolibreapi.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICategorieDao extends ReactiveMongoRepository<Category, String> {

}
