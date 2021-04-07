package com.example.spearmint;

import java.util.ArrayList;

/**
 * Base class defining the Experiment object with fields of type String
 * Has getter methods so other classes can retrieve the information of an experiment
 * Has fields "description", "region", "count"
 * Abram Hindle, "Lab 3 instructions - CustomList", Public Domain, https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
 * @author Daniel and Andrew
 */

public class Experiment {

    private String experimentDescription;
    private String experimentRegion;
    private String experimentCount;
    private ArrayList<String> experimentOwner;
    private String geoLocation;
    private String trialType;


    Experiment(String experimentDescription, String experimentRegion, String experimentCount, ArrayList<String> experimentOwner, String geoLocation, String trialType) {
        this.experimentDescription = experimentDescription;
        this.experimentRegion = experimentRegion;
        this.experimentCount = experimentCount;
        this.experimentOwner = experimentOwner;
        this.geoLocation = geoLocation;
        this.trialType = trialType;
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
  
    public ArrayList<String> getExperimentOwner() {
        return this.experimentOwner; }
  
    public String getGeoLocation() {
        return this.geoLocation;
    }

    public String getTrialType() {
        return this.trialType;
    }
}