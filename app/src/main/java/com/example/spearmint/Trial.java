package com.example.spearmint;

import android.os.Parcel;
import android.os.Parcelable;

public class Trial implements Parcelable {

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

    protected Trial(Parcel in) {
        trialDescription = in.readString();
        trialResult = in.readString();
        trialLocation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trialDescription);
        dest.writeString(trialResult);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trial> CREATOR = new Creator<Trial>() {
        @Override
        public Trial createFromParcel(Parcel in) {
            return new Trial(in);
        }

        @Override
        public Trial[] newArray(int size) {
            return new Trial[size];
        }
    };

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
