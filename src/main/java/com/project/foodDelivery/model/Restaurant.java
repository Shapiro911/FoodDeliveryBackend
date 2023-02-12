package com.project.foodDelivery.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Restaurants")
@Getter
@Setter
public class Restaurant {
    public class Address {
        private List<Double> coord;

        public List<Double> getCoord() {
            return coord;
        }

        public void setCoord(List<Double> coord) {
            this.coord = coord;
        }

        public Address(List<Double> coord) {
            this.coord = coord;
        }
    }
    @Id
    private String id;
    private String name;
    private String img;
    private String duration;
    private Double rating;
    private Address address;
    private String cuisine;
    private String borough;
    private Double fee;
    private List<String> dishes;
    private Integer price;

    public Restaurant(String name, String img, String duration, Double rating, String cuisine, Address address, String borough, Double fee, List<String> dishes, Integer price) {
        this.name = name;
        this.img = img;
        this.duration = duration;
        this.rating = rating;
        this.cuisine = cuisine;
        this.address = address;
        this.borough = borough;
        this.fee = fee;
        this.dishes = dishes;
        this.price = price;
    }
}
