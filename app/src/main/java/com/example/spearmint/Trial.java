package com.example.spearmint;

public class Trial {

    private String trialDescription;
    private String trialResult;
    private String experimenter;
    private String trialLocation;

    Trial(String trialDescription, String trialResult, String experimenter, String trialLocation) {
        this.trialDescription = trialDescription;
        this.trialResult = trialResult;
        this.experimenter = experimenter;
        this.trialLocation = trialLocation;
    }

    public String getTrialDescription() {
        return this.trialDescription;
    }

    public String getTrialResult() {
        return this.trialResult;
    }

    public String getExperimenter() {
        return this.experimenter;
    }

    public String getTrialLocation() {
        return this.trialLocation;
    }
}
