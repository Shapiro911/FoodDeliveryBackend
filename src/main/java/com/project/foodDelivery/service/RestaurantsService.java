package com.project.foodDelivery.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.foodDelivery.model.DirectionResult;
import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.model.SortValues;
import com.project.foodDelivery.repository.RestaurantsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantsService {
    @Autowired
    private RestaurantsRepository restaurantsRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Restaurant> getRestaurants(@NotNull List<Double> coordinates, @NotNull SortValues sortValues, Integer page, Integer pageSize) throws IOException {
        List<Restaurant> restaurantList = new ArrayList<Restaurant>();
        String address = coordinates.get(0).toString() + " " + coordinates.get(1);
        List<String> nearbyBoroughs = getNearbyBoroughs(address);
        Point coord = new Point(coordinates.get(1), coordinates.get(0));
        Pageable pageable = PageRequest.of(page, pageSize);

        Query query = new Query();

        if (!sortValues.getPriceRange().isEmpty()) {
            query.addCriteria(Criteria.where("price").in(sortValues.getPriceRange()));
        }
        if (sortValues.getFee() > 0 && sortValues.getFee() < 8) {
            query.addCriteria(Criteria.where("fee").lte(sortValues.getFee()));
        }
        if (Objects.equals(sortValues.getSortBy(), "popular")) {
            query.addCriteria(Criteria.where("rating").gte(4));
        }
        if (Objects.equals(sortValues.getSortBy(), "rating")) {
            query.with(Sort.by(Sort.Direction.DESC, "rating"));
        }

        query.addCriteria(Criteria.where("address.coord").nearSphere(coord).maxDistance(8000)).addCriteria(Criteria.where("borough").in(nearbyBoroughs)).with(pageable);

        for (Restaurant restaurant : mongoTemplate.find(query, Restaurant.class)) {
            String coordinatesRestaurant = restaurant.getAddress().getCoord().get(1).toString() + " " + restaurant.getAddress().getCoord().get(0).toString();
            Long duration = findDirection(address, coordinatesRestaurant, "bicycling").getDuration();

            if (duration != 0) {
                int durationRounded = Math.round((duration / 60 + 5) / 10) * 10;
                if (durationRounded < 10) {
                    restaurant.setDuration("10-25");
                } else if (durationRounded > (duration / 60)) {
                    restaurant.setDuration(durationRounded + "-" + (durationRounded + 15));
                } else {
                    restaurant.setDuration(durationRounded + 5 + "-" + (durationRounded + 15));
                }
                restaurantList.add(restaurant);
            }
        }
        return restaurantList;
    }

    public DirectionResult findDirection(String origin, String destination, String mode) {
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
            if (distance <= 10000 && distance > 0) {
                nearbyBoroughs.add(borough);
            }
        }
        return nearbyBoroughs;
    }
}

