package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TrialTest {

    private Trial trial;
    private Trial mockTrial(){
        trial = new Trial("Calculating coin flip probability",
                "50%",
                "gave",
                "Edmonton");
        return trial;
    }

    @Test
    public void getTrialDescriptionTest(){
        Assert.assertTrue(mockTrial().getTrialDescription().contains("Calculating"));
        Assert.assertTrue(mockTrial().getTrialDescription().contains("coin"));
        Assert.assertTrue(mockTrial().getTrialDescription().contains("flip"));
        Assert.assertTrue(mockTrial().getTrialDescription().contains("probability"));
    }

    @Test
    public void getTrialResult(){
        Assert.assertTrue(mockTrial().getTrialResult().contains("50%"));
    }

    @Test
    public void getExperimenterTest(){
        Assert.assertTrue(mockTrial().getExperimenter().contains("gave"));

    }

}
