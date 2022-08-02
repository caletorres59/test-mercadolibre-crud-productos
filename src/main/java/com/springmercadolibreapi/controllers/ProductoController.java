package com.springmercadolibreapi.controllers;


import com.springmercadolibreapi.models.documents.Product;
import com.springmercadolibreapi.models.service.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")

public class ProductoController {

    @Autowired
    private IProductoService productService;

    //List Products
    @Operation(summary = "Listado de productos")
    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> index() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(productService.findAll())
        );
    }

    //Search by id
    @Operation(summary = "Buscar producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)})

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> searchById(@PathVariable String id) {
        return productService.findById(id)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(p)

                )
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Guardar producto en base de datos")
    @Parameter(description = "Producto")
    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> saveProduct(@Valid @RequestBody Mono<Product> monoProduct) {

        Map<String, Object> response = new HashMap<String, Object>();
        return monoProduct.flatMap(product -> {
            //Valido la fecha
            if (product.getCreateAt() == null) {
                product.setCreateAt(new Date());
            }
            return productService.save(product)
                    .map(pro -> {
                                response.put("product", pro);
                                response.put("messagge", "Producto creado con exito");
                                response.put("timestap", new Date());
                                return ResponseEntity.created(URI.create("/api/products/".concat(pro.getId())))
                                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                                        .body(response);
                            }
                    );
        }).onErrorResume(t -> {
            return Mono.just(t)
                    .cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        response.put("errors", list);
                        response.put("timestap", new Date());
                        response.put("status", HttpStatus.BAD_REQUEST.value());
                        return Mono.just(ResponseEntity.badRequest().body(response));
                    });

        });
    }

    @Operation(summary = "Actualiza un producto")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product product, @PathVariable String id) {
        return productService.findById(id)
                .flatMap(pro -> {
                    pro.setName(product.getName());
                    pro.setPrice(product.getPrice());
                    pro.setCateogrie(product.getCateogrie());
                    return productService.save(pro);
                })
                .map(prUpdate -> ResponseEntity.created(URI.create("/api/products/".concat(prUpdate.getId()))

                                )
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .body(prUpdate)

                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Elimina un registro de base de datos")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {

        return productService.findById(id)
                .flatMap(prod -> {
                    return productService.delete(prod).then(Mono.just((new ResponseEntity<Void>(HttpStatus.NO_CONTENT))));
                }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
