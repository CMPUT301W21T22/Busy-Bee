package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class PostTest {

    private Post post;
    private Post mockPost(){
        post = new Post("Temperature Collection", "Temperature description test");
        return post;
    }

    @Test
    public void getExperimentTest(){
        Assert.assertTrue(mockPost().getExperimentTitle().contains("Temperature"));
        Assert.assertTrue(mockPost().getExperimentTitle().contains("Collection"));
    }

    @Test
    public void getTestTest(){
        Assert.assertTrue(mockPost().getText().contains("Temperature"));
        Assert.assertTrue(mockPost().getText().contains("description"));
        Assert.assertTrue(mockPost().getText().contains("test"));
    }


}
