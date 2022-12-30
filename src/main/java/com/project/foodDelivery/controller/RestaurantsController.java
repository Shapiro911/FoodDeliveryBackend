package com.project.foodDelivery.controller;

import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.service.RestaurantsService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping
//    public void getRestaurants(@RequestParam(value = "pageMax") @NotNull @Min(1) int pageMax, @RequestParam(value = "page") @NotNull @Min(1) int page) {
//        restaurantsService.getRestaurants(pageMax, page);
//    }

    @GetMapping
    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = restaurantsService.getRestaurants();
        return restaurants;
    }

    @GetMapping("/test")
        public int test() {
            return 1;
    }
}
