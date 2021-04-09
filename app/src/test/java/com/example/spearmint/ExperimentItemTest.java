package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ExperimentItemTest {

    private ExperimentItem experimentItem;
    private ExperimentItem mockExperimentItem(){
        experimentItem = new ExperimentItem("ExperimentItemTest", "Sandi");
        return experimentItem;
    }

    @Test
    public void getExperimentItemTest(){
        ExperimentItem experimentItem = mockExperimentItem();
        Assert.assertEquals("ExperimentItemTest", mockExperimentItem().getaTitle());
        Assert.assertTrue(mockExperimentItem().getaTitle().contains("ExperimentItemTest"));
    }

    @Test
    public void getUserInfoTest(){
        ExperimentItem experimentItem = mockExperimentItem();
        Assert.assertEquals("Sandi", mockExperimentItem().getUserInfo());
        Assert.assertTrue(mockExperimentItem().getUserInfo().contains("Sandi"));
    }


}
