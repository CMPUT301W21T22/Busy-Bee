package com.example.spearmint;

public class Experiment extends Object {

    private String experimentDescription;
    private String experimentRegion;
    private String experimentCount;

    Experiment(String experimentDescription, String experimentRegion, String experimentCount) {
        this.experimentDescription = experimentDescription;
        this.experimentRegion = experimentRegion;
        this.experimentCount = experimentCount;
    }

    public String getExperimentDescription() {
        return this.experimentDescription;
    }

    public String getExperimentRegion() {
        return this.experimentRegion;
    }

    public String getExperimentCount() {
        return this.experimentCount;
    }
}
