package com.project.foodDelivery.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@Document("Products")
public class Product {
    @Id
    private String id;
    private String name;
    private String category;
    private Double price;
    private String img;

    public Product(String name, String category, Double price, String img) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.img = img;
    }
}

