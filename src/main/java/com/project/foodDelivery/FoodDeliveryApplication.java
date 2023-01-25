package com.project.foodDelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableMongoRepositories
public class FoodDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryApplication.class, args);
	}
		}





