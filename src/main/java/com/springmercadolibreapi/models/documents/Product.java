package com.springmercadolibreapi.models.documents;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotEmpty
    private String name;
    @NotNull
    private Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    @Valid
    @NotNull
    private Category cateogrie;


    public Product(String name, Double price) {
        this.name = name;
        this.price = price;

    }

    public Product(String name, Double price, Category cateogrie) {
        this(name,price);
        this.cateogrie = cateogrie;

    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Category getCateogrie() {
        return cateogrie;
    }

    public void setCateogrie(Category cateogrie) {
        this.cateogrie = cateogrie;
    }

}
