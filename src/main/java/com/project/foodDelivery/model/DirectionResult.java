package com.project.foodDelivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectionResult {
    private Long duration;
    private Long distance;

    public DirectionResult(Long duration, Long distance) {
        this.duration = duration;
        this.distance = distance;
    }
}
