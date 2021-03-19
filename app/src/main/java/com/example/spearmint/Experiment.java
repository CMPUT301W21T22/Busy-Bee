package com.example.spearmint;

/**
 * Base class defining the Experiment object with fields of type String
 * Has getter methods so other classes can retrieve the information of an experiment
 * Has fields "description", "region", "count"
 * @author Daniel and Andrew
 */
public class Experiment {

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
