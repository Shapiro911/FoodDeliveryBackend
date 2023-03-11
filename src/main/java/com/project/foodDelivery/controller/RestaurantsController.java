package com.project.foodDelivery.controller;

import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.model.RestaurantSearch;
import com.project.foodDelivery.service.RestaurantsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurants",
        produces = "application/json"
)
@Validated
@CrossOrigin
public class RestaurantsController {
    @Autowired
    RestaurantsService restaurantsService;
    private static final Logger log = LoggerFactory.getLogger(RestaurantsController.class);

    @PostMapping
    public List<Restaurant> getRestaurants(@RequestParam(value = "coordinates") List<Double> coordinates, @Valid @RequestBody @NotNull RestaurantSearch restaurantSearch, @RequestParam(value = "page") @Min(1) Integer page, @RequestParam(value = "pageSize") @Min(1) Integer pageSize) throws IOException {
        List<Restaurant> restaurants = restaurantsService.getRestaurants(coordinates, restaurantSearch, page, pageSize);
        log.info("Got restaurant list from DB");
        return restaurants;
    }
}
