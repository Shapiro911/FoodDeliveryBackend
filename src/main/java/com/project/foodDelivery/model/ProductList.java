package com.project.foodDelivery.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
public class ProductList {
    @Id
    private String category;
    private List<Product> products;

    public ProductList(String category, List<Product> products) {
        this.category = category;
        this.products = products;
    }

    public ProductList() {
    }

    public ProductList(@NotNull ProductList productList) {
        this.category = productList.category;
        this.products = productList.products;
    }
}
