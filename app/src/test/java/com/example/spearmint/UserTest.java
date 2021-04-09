package com.example.spearmint;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class UserTest {

    private final ArrayList<String> owner = new ArrayList<String>(){
        {
            add("Daniel");
        }
    };

    Experiment ownedExperiment = new Experiment("Count Cars",
            "Edmonton",
            "5",
            owner,
            "Allard",
            "Count");

    Experiment subscribedExperiment = new Experiment("Count Cars",
            "Edmonton",
            "5",
            owner,
            "Allard",
            "Count");

    private User user;
    private ArrayList<Experiment> myExperiment = new ArrayList<Experiment>(){
        {
            add(ownedExperiment);
        }
    };
    private ArrayList<Experiment> otherExperiment = new ArrayList<Experiment>(){
        {
            add(subscribedExperiment);
        }
    };
    private User mockUser(){
        user = new User();

        return user;
    }

    @Test
    public void setUUIDTest(){
        User user = mockUser();
        user.setUUID("usernameIDTest");
        Assert.assertTrue(user.getUUID().contains("usernameIDTest"));
    }

    @Test
    public void getUUIDTest() {
        User user = mockUser();

        user.setUUID("usernameIDTestget");
        Assert.assertTrue(user.getUUID().contains("usernameIDTestget"));
    }

    @Test
    public void setUserTest(){
        User user = mockUser();

        user.setUsername("gavethegreat");
        Assert.assertTrue(user.getUsername().contains("gavethegreat"));
    }

    @Test
    public void getUserTest(){
        User user = mockUser();

        user.setUsername("gavethegreatget");
        Assert.assertTrue(user.getUsername().contains("gavethegreatget"));
    }

    @Test
    public void setEmailTest(){
        User user = mockUser();

        user.setEmail("gavethegreat@gmail.com");
        Assert.assertTrue(user.getEmail().contains("gavethegreat@gmail.com"));
    }

    @Test
    public void getEmailTest(){
        User user = mockUser();

        user.setEmail("gavethegreatget@gmail.com");
        Assert.assertTrue(user.getEmail().contains("gavethegreatget@gmail.com"));
    }

    @Test
    public void setNumber(){
        User user = mockUser();

        user.setNumber("7802223333");
        Assert.assertTrue(user.getNumber().contains("7802223333"));
    }

    @Test
    public void getNumber(){
        User user = mockUser();

        user.setNumber("7802224444");
        Assert.assertTrue(user.getNumber().contains("7802224444"));
    }

    @Test
    public void setOwnedExperimentsTest(){
        User user = mockUser();
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();

        user.setOwnedExperiments(myExperiment);
        Assert.assertTrue(user.getOwnedExperiments().contains(ownedExperiment));
    }

    @Test
    public void setSubscribedExperimentTest(){
        User user = mockUser();
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();

        user.setSubscribedExperiments(otherExperiment);
        Assert.assertTrue(user.getSubscribedExperiments().contains(subscribedExperiment));
    }


}
