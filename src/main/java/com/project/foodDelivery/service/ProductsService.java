package com.project.foodDelivery.service;

import com.project.foodDelivery.model.Product;
import com.project.foodDelivery.model.ProductList;
import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.repository.ProductsRepository;
import com.project.foodDelivery.repository.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductsService {
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    RestaurantsRepository restaurantsRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public List<ProductList> getProducts(String restaurantId) {
        List<ProductList> productListByCategory = new ArrayList<ProductList>();
        String category = "";
        ProductList productList = new ProductList();
        List<Product> productListTemp = new ArrayList<>();

        Optional<Restaurant> restaurant = restaurantsRepository.findById(restaurantId);
        List<String> productIds = restaurant.get().getDishes();

        for(Product product : productsRepository.findAllById(productIds)) {
            if (category.equals("")) {
                category = product.getCategory();
                productListTemp.add(product);
            } else if(Objects.equals(category, product.getCategory())) {
                productListTemp.add(product);
            } else {
                List<Product> productListTempCopy = new ArrayList<>();
                productListTempCopy.addAll(productListTemp);
                productList.setCategory(category);
                productList.setProducts(productListTempCopy);
                ProductList productListCopy = new ProductList(productList);
                productListByCategory.add(productListCopy);

                category = product.getCategory();
                productListTemp.clear();
                productListTemp.add(product);
            }
        }
        productList.setCategory(category);
        productList.setProducts(productListTemp);
        productListByCategory.add(productList);

        return productListByCategory;
    }
}
