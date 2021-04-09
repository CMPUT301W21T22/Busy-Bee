package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ExperimentTest {
    private Experiment experiment;
    private ArrayList<String> owner = new ArrayList<String>(){
        {
            add("Daniel");
        }
    };

    private Experiment mockExperiment(){
        experiment = new Experiment(
                "Count Cars",
                "Edmonton",
                "5",
                owner,
                "Allard",
                "Count");

        return experiment;
    }

    @Test
    public void getExperimentDescriptionTest(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getExperimentDescription().contains("Count Cars"));
    }

    @Test
    public void getExperimentRegionTest(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getExperimentRegion().contains("Edmonton"));
    }

    @Test
    public void getExperimentCountTest(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getExperimentCount().contains("5"));

    }

    @Test
    public void getExperimentOwner(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getExperimentOwner().contains("Daniel"));
    }

    @Test
    public void getGeoLocation(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getGeoLocation().contains("Allard"));
    }

    @Test
    public void getTrialTypeTest(){
        Experiment experiment = mockExperiment();
        Assert.assertTrue(experiment.getTrialType().contains("Count"));
    }


}
