package com.project.foodDelivery.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RestaurantSearch {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SortValues {
        @NotBlank(message = "Invalid sort: empty sort")
        @NotNull(message = "Invalid sort: sort is null")
        @Pattern(regexp = "^[a-zA-Z]*$", message = "Invalid sort: must correspond to ^[a-zA-Z]*$ pattern")
        private String sortBy;
        private List<Integer> priceRange;
        @NotNull(message = "Invalid fee: fee is null")
        @Min(value = 0, message = "Invalid fee: must be more than -1")
        @Max(value = 8, message = "Invalid fee: must be less than 9")
        private Integer fee;
    }
    @NonNull
    @Valid
    private SortValues sortValues;
    private String searchText;
}
