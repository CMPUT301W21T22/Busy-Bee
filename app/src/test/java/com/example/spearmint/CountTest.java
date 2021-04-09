package com.example.spearmint;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CountTest {

    private Count count;
    private Experiment experiment;
    private ArrayList<String> owner = new ArrayList<String>(){
        {add("Jiho");}
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
    public void getCountDescription(){
        count = new Count("TestDescription", "TestResult");
        Assert.assertEquals("TestDescription", count.getCountDescription());

        Assert.assertTrue(count.getCountDescription().contains("TestDescription"));

    }

    @Test
    public void getCountResult(){
        count = new Count("TestDescription", "TestResult");
        Assert.assertEquals("TestResult", count.getCountResult());

        Assert.assertTrue(count.getCountResult().contains("TestResult"));
    }


}
