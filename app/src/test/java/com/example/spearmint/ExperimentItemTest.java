package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ExperimentItemTest {

    private ExperimentItem experimentItem;
    private ExperimentItem mockExperimentItem(){
        experimentItem = new ExperimentItem("ExperimentItemTest");
        return experimentItem;
    }

    @Test
    public void ExperimentItemTest(){
        ExperimentItem experimentItem = mockExperimentItem();
        Assert.assertEquals("ExperimentItemTest", mockExperimentItem().getaTitle());
        Assert.assertTrue(mockExperimentItem().getaTitle().contains("ExperimentItemTest"));
    }


}
