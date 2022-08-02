package com.springmercadolibreapi;

import com.springmercadolibreapi.models.documents.Product;
import com.springmercadolibreapi.models.service.IProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.util.Collections;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringMercadolibreApiRestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private IProductoService productService;

	@Test
	void listTest() {
		client.get().uri("/api/products")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
	}

	@Test
	void getByName() {
		Mono<Product>   product = productService.findByName("televisor");
		client.get().uri("/api/products/{id}", Collections.singletonMap("id", product.block().getId()))
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
	}
	

}
