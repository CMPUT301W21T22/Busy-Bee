package com.example.spearmint;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    private Question question;
    private Question mockQuestion(){
        question = new Question("How cold was Edmonton in 1954?", "-50 degree celsius");
        return question;
    }

    @Test
    public void getQuestionTest(){
        Assert.assertTrue(mockQuestion().getQuestion().contains("How"));
        Assert.assertTrue(mockQuestion().getQuestion().contains("cold"));
        Assert.assertTrue(mockQuestion().getQuestion().contains("was"));
        Assert.assertTrue(mockQuestion().getQuestion().contains("Edmonton"));
        Assert.assertTrue(mockQuestion().getQuestion().contains("in"));
        Assert.assertTrue(mockQuestion().getQuestion().contains("1954?"));
    }

    @Test
    public void getAnswerTest(){
        Assert.assertTrue(mockQuestion().getAnswer().contains("-50"));
        Assert.assertTrue(mockQuestion().getAnswer().contains("degree"));
        Assert.assertTrue(mockQuestion().getAnswer().contains("celsius"));
    }

}
