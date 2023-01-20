package com.project.foodDelivery.model;

public class DirectionResult {
    private Long duration;
    private Long distance;

    public DirectionResult(Long duration, Long distance) {
        this.duration = duration;
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }
}
