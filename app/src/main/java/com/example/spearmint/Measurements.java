package com.example.spearmint;

public class Measurements {
    private String measurementDescription;
    private String measurementResult;
    private String measurementUnit;

    Measurements(String measurementDescription, String measurementResult, String measurementUnit) {
        this.measurementDescription = measurementDescription;
        this.measurementResult = measurementResult;
        this.measurementUnit = measurementUnit;
    }

    public String getMeasurementDescription() {
        return this.measurementDescription;
    }

    public String getMeasurementResult() {
        return this.measurementResult;
    }

    public String getMeasurementUnit() {
        return this.measurementUnit;
    }
}