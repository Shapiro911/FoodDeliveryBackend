package com.project.foodDelivery.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("Restaurants")
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
    private Long duration;
    private Double rating;
    private Address address;
    private String cuisine;
    private String borough;
    private Double fee;

    public Restaurant(String name, String img, Long duration, Double rating, String cuisine, Address address, String borough, Double fee) {
        this.name = name;
        this.img = img;
        this.duration = duration;
        this.rating = rating;
        this.cuisine = cuisine;
        this.address = address;
        this.borough = borough;
        this.fee = fee;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getCuisine() {
        return cuisine;
    }
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}
