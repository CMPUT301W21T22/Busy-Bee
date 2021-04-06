package com.example.spearmint;

public class Trial {

    private String trialDescription;
    private String trialResult;

    Trial(String trialDescription, String trialResult) {
        this.trialDescription = trialDescription;
        this.trialResult = trialResult;
    }

    public String getTrialDescription() {
        return this.trialDescription;
    }

    public String getTrialResult() {
        return this.trialResult;
    }
}
