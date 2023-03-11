package com.project.foodDelivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foodDelivery.controller.RestaurantsController;
import com.project.foodDelivery.model.RestaurantSearch;
import com.project.foodDelivery.service.RestaurantsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestaurantsController.class)
@AutoConfigureDataMongo
public class RestaurantsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MongoTemplate mongoTemplate;

    @MockBean
    RestaurantsService restaurantsService;

    @Test
    public void testGetRestaurants() throws Exception {
        List<Double> coordinates = Arrays.asList(40.730610, -73.935242);
        List<Integer> priceRange = new ArrayList<>();
        RestaurantSearch restaurantSearch = new RestaurantSearch(new RestaurantSearch.SortValues("popular", priceRange, 0), "Burger");
        Integer page = 1;
        Integer pageSize = 21;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("coordinates", coordinates.toString().substring(1, coordinates.toString().length() - 1));
        params.add("page", String.valueOf(page));
        params.add("pageSize", String.valueOf(pageSize));
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(restaurantSearch);

        mockMvc.perform(post("/restaurants").params(params).contentType(MediaType.APPLICATION_JSON).content(body)).andDo(print()).andExpect(status().isOk()).andReturn();
//        verify(restaurantsService, times(1)).getRestaurants(coordinates, restaurantSearch, page, pageSize);
    }
}
