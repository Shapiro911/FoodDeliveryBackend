package com.project.foodDelivery.service;

import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.repository.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RestaurantsService {
    @Autowired
    private RestaurantsRepository restaurantsRepository;
//    public void getRestaurants(int pageMax, int page) {
//        System.out.println("Restaurants found with findAll():");
//        System.out.println("-------------------------------");
//        AtomicInteger counter = new AtomicInteger();
//        for (Restaurant restaurant : restaurantsRepository.findAll()) {
//            if (counter.get() == 20) {
//                break;
//            }
//            System.out.println(restaurant);
//            counter.getAndIncrement();
//        }
//    }

    public List<Restaurant> getRestaurants() {
        AtomicInteger counter = new AtomicInteger();
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        
        for (Restaurant restaurant : restaurantsRepository.findAll()) {
            if (counter.get() == 20) {
                break;
            }

            restaurantList.add(restaurant);
            counter.getAndIncrement();
        }
        
        return restaurantList;
    }
}
