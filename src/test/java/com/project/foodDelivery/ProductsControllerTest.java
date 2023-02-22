package com.project.foodDelivery;

import com.project.foodDelivery.controller.ProductsController;
import com.project.foodDelivery.service.ProductsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductsController.class)
@AutoConfigureDataMongo
public class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MongoTemplate mongoTemplate;

    @MockBean
    ProductsService productsService;

    @Test
    public void testGetRestaurants() throws Exception {
        String restaurantId = "5eb3d668b31de5d588f42948";

        mockMvc.perform(get("/products").param("restaurantId", restaurantId)).andDo(print()).andExpect(status().isOk()).andReturn();
        verify(productsService, times(1)).getProducts(restaurantId);
    }

}
