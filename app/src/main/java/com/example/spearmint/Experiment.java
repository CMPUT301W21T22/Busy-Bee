package com.example.spearmint;

public class Experiment {

    private String experimentDescription;
    private String experimentRegion;

    Experiment(String experimentDescription, String experimentRegion) {
        this.experimentDescription = experimentDescription;
        this.experimentRegion = experimentRegion;
    }

    public String getExperimentDescription() {
        return this.experimentDescription;
    }

    public String getExperimentRegion() {
        return this.experimentRegion;
    }
}
