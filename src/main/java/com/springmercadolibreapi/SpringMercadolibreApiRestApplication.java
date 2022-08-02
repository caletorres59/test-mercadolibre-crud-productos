package com.springmercadolibreapi;

import com.springmercadolibreapi.models.dao.ICategorieDao;
import com.springmercadolibreapi.models.documents.Category;
import com.springmercadolibreapi.models.documents.Product;
import com.springmercadolibreapi.models.service.IProductoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;


@SpringBootApplication
public class SpringMercadolibreApiRestApplication implements CommandLineRunner {


	@Autowired
	private IProductoService serviceProduct;

	@Autowired
	private ICategorieDao daoCateogorie;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	private static final Logger log = LoggerFactory.getLogger(SpringMercadolibreApiRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringMercadolibreApiRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("products").subscribe();
		mongoTemplate.dropCollection("categories").subscribe();
		Category electronico = new Category("electronico");
		Category hogar = new Category("hogar");
		Category entretenimiento = new Category("entretenimiento");
		Flux.just(electronico, hogar, entretenimiento)
				.flatMap(serviceProduct::saveCategory)
				.doOnNext(c -> {
					log.info("insert categorie: " + c);
				}).thenMany(
						Flux.just( new Product("televisor",5.200, electronico),
										new Product("camara",5.900,hogar ),
										new Product("computador",2.200,entretenimiento)
								)
								.flatMap(product -> {
									product.setCreateAt(new Date());
									return serviceProduct.save(product);
								})
				)
				.subscribe(product -> log.info("insert: " +  product.getId()));
	}
}
