package com.example.spearmint;

public class Trial {

    private String trialDescription;
    private String trialResult;
    private String trialLocation;

    Trial(String trialDescription, String trialResult, String trialLocation) {
        this.trialDescription = trialDescription;
        this.trialResult = trialResult;
        this.trialLocation = trialLocation;
    }

    public String getTrialDescription() {
        return this.trialDescription;
    }

    public String getTrialResult() {
        return this.trialResult;
    }

    public String getTrialLocation() {
        return this.trialLocation;
    }
}
