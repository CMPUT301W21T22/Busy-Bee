package com.example.spearmint;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Base class defining the Experiment object with fields of type String
 * Has getter methods so other classes can retrieve the information of an experiment
 * Has fields "description", "region", "count,", "owner", "geolocation", "trialtype", "status"
 * Abram Hindle, "Lab 3 instructions - CustomList", Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
 * @author Daniel and Andrew
 */

public class Experiment implements Parcelable {

    private String experimentDescription;
    private String experimentRegion;
    private String experimentCount;
    private ArrayList<String> experimentOwner;
    private String geoLocation;
    private String trialType;
    private String status;

    Experiment(String experimentDescription, String experimentRegion, String experimentCount, ArrayList<String> experimentOwner, String geoLocation, String trialType, String status) {
        this.experimentDescription = experimentDescription;
        this.experimentRegion = experimentRegion;
        this.experimentCount = experimentCount;
        this.experimentOwner = experimentOwner;
        this.geoLocation = geoLocation;
        this.trialType = trialType;
        this.status = status;
    }

    protected Experiment(Parcel in) {
        experimentDescription = in.readString();
        experimentRegion = in.readString();
        experimentCount = in.readString();
        experimentOwner = in.createStringArrayList();
        geoLocation = in.readString();
        trialType = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(experimentDescription);
        dest.writeString(experimentRegion);
        dest.writeString(experimentCount);
        dest.writeStringList(experimentOwner);
        dest.writeString(geoLocation);
        dest.writeString(trialType);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Experiment> CREATOR = new Creator<Experiment>() {
        @Override
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        @Override
        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };

    public String getExperimentDescription() {
        return this.experimentDescription;
    }

    public String getExperimentRegion() {
        return this.experimentRegion;
    }

    public String getExperimentCount() {
        return this.experimentCount;
    }
  
    public ArrayList<String> getExperimentOwner() {
        return this.experimentOwner;
    }
  
    public String getGeoLocation() {
        return this.geoLocation;
    }

    public String getTrialType() {
        return this.trialType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}