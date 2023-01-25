package com.project.foodDelivery.repository;


import com.project.foodDelivery.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantsRepository extends MongoRepository<Restaurant, String> {
}
