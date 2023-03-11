package com.project.foodDelivery;

import com.project.foodDelivery.model.DirectionResult;
import com.project.foodDelivery.model.Restaurant;
import com.project.foodDelivery.repository.RestaurantsRepository;
import com.project.foodDelivery.service.RestaurantsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RestaurantsTest {
    @Autowired
    RestaurantsService restaurantsService;
    @Autowired
    RestaurantsRepository restaurantsRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void itShouldFindDirection() {
        String origin = "Queens";
        String destination = "40.730610, -73.935242";
        String mode = "bicycle";
        DirectionResult directionResultExpect = new DirectionResult(1339L, 15456L);
        DirectionResult directionResult = restaurantsService.findDirection(origin, destination, mode);

        assertEquals(directionResultExpect.getDuration(), directionResult.getDuration());
        assertEquals(directionResultExpect.getDistance(), directionResult.getDistance());
    }

    @Test
    public void itShouldGetBoroughs() throws Exception {
        String address = "40.730610, -73.935242";
        List<String> nearbyBoroughs = restaurantsService.getNearbyBoroughs(address);

        assertEquals(2, nearbyBoroughs.size());
    }

    @Test
    public void itShouldReturnRestaurantList() throws Exception {
        List<Double> coordinates = Arrays.asList(40.730610, -73.935242);
        List<Integer> priceRange = new ArrayList<>();
        SortValues sortValues = new SortValues("popular", priceRange, 0);
        Integer page = 1;
        Integer pageSize = 21;

        List<Restaurant> restaurantList = restaurantsService.getRestaurants(coordinates, sortValues, page, pageSize);
        System.out.println(restaurantList);
        assertEquals(pageSize, restaurantList.size());

        for (int i = 0; i < pageSize; i++) {
            Restaurant restaurant = restaurantList.get(i);
            assertEquals("String", restaurant.getId().getClass().getSimpleName());
            assertFalse(restaurant.getId().isEmpty());
            assertEquals("String", restaurant.getImg().getClass().getSimpleName());
            assertFalse(restaurant.getImg().isEmpty());
            assertEquals("String", restaurant.getCuisine().getClass().getSimpleName());
            assertFalse(restaurant.getCuisine().isEmpty());
            assertEquals("String", restaurant.getBorough().getClass().getSimpleName());
            assertFalse(restaurant.getBorough().isEmpty());
            assertEquals("ArrayList", restaurant.getAddress().getCoord().getClass().getSimpleName());
            assertFalse(restaurant.getAddress().getCoord().isEmpty());
            assertEquals("String", restaurant.getName().getClass().getSimpleName());
            assertFalse(restaurant.getName().isEmpty());
            assertEquals("Double", restaurant.getFee().getClass().getSimpleName());
            assertFalse(restaurant.getFee().isNaN());
            assertEquals("String", restaurant.getDuration().getClass().getSimpleName());
            assertFalse(restaurant.getDuration().isEmpty());
            assertEquals("ArrayList", restaurant.getDishes().getClass().getSimpleName());
            assertFalse(restaurant.getDishes().isEmpty());
            assertEquals("Integer", restaurant.getPrice().getClass().getSimpleName());
        }
    }
}