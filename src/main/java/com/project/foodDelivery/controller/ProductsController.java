package com.project.foodDelivery.controller;

import com.project.foodDelivery.model.ProductList;
import com.project.foodDelivery.service.ProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@CrossOrigin
@RequestMapping(value = "/products",
        produces = "application/json")
public class ProductsController {
    @Autowired
    ProductsService productsService;

    private static final Logger log = LoggerFactory.getLogger(ProductsController.class);

    @GetMapping
    public List<ProductList> getProducts(@RequestParam(value = "restaurantId") String restaurantId) {
        List<ProductList> productList = productsService.getProducts(restaurantId);
        log.info("got product list from DB");
        return productList;
    }
}
