package com.project.foodDelivery.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.foodDelivery.model.DirectionResult;
import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.repository.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RestaurantsService {
    @Autowired
    private RestaurantsRepository restaurantsRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
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

    public List<Restaurant> getRestaurants(List<Double> coordinates) throws IOException {
        AtomicInteger counter = new AtomicInteger();
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        String address = coordinates.get(0).toString() + " " + coordinates.get(1);
        List<String> nearbyBoroughs = getNearbyBoroughs(address);

        Point coord = new Point(coordinates.get(1), coordinates.get(0));
        Query query = new Query();
        query.addCriteria(Criteria.where("address.coord").nearSphere(coord)).addCriteria(Criteria.where("borough").in(nearbyBoroughs)).limit(30);
        
        for (Restaurant restaurant : mongoTemplate.find(query, Restaurant.class)) {
            if (counter.get() == 21) {
                break;
            }

            String coordinatesRestaurant = restaurant.getAddress().getCoord().get(1).toString() + " " + restaurant.getAddress().getCoord().get(0).toString();

            Long duration = findDirection(address, coordinatesRestaurant, "bicycling").getDuration();
            if (duration != 0) {
                restaurant.setDuration(duration);
                restaurantList.add(restaurant);
                counter.getAndIncrement();
            }
        }

        return restaurantList;
    }

    public DirectionResult findDirection(String origin, String destination, String mode) throws IOException {
        String url = String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=AIzaSyCxyo5J8uJ1O5hzhV4fx2-hGiySpsmMQk4&mode=%s", origin, destination, mode);
        RestTemplate restTemplate = new RestTemplate();

        JsonObject direction = new Gson().fromJson(restTemplate.getForObject(url, String.class), JsonObject.class);

        if(Objects.equals(direction.get("status").toString(), "\"OK\"")) {
            JsonObject legs = direction.get("routes").getAsJsonArray().get(0).getAsJsonObject().get("legs").getAsJsonArray().get(0).getAsJsonObject();
            Long distance = legs.get("distance").getAsJsonObject().get("value").getAsLong();
            Long duration = legs.get("duration").getAsJsonObject().get("value").getAsLong();
            DirectionResult directionResult = new DirectionResult(duration, distance);
            return directionResult;
        } else {
              DirectionResult directionResult = new DirectionResult(0L, 0L);
              return directionResult;
        }
    }

    public List<String> getNearbyBoroughs(String address) throws IOException {
        List<String> boroughsNY = Arrays.asList("Queens", "Bronx", "Manhattan", "Brooklyn", "Staten Island");
        List<String> nearbyBoroughs = new ArrayList<String>();

        for(String borough : boroughsNY) {
            Long distance = findDirection(borough, address, "walking").getDistance();
            if (distance <= 10000) {
                nearbyBoroughs.add(borough);
            }
        }
        System.out.println(nearbyBoroughs);
        return nearbyBoroughs;
    }
}

