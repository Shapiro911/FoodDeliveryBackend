package com.project.foodDelivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SortValues {
    @NotBlank(message = "Invalid sort: empty sort")
    @NotNull(message = "Invalid sort: sort is null")
    private String sortBy;
    private List<Integer> priceRange;
    @NotNull(message = "Invalid fee: fee is null")
    private Integer fee;

    public SortValues(String sortBy, List<Integer> priceRange, Integer fee) {
        this.sortBy = sortBy;
        this.priceRange = priceRange;
        this.fee = fee;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<Integer> getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(List<Integer> priceRange) {
        this.priceRange = priceRange;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }
}
