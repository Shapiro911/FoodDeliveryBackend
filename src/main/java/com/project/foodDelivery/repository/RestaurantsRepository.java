package com.project.foodDelivery.repository;


import com.project.foodDelivery.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantsRepository extends MongoRepository<Restaurant, String> {
//    @Query
//    List<Restaurant> findAll();
}
