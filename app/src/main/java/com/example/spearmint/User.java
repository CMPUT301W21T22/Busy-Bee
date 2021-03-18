package com.example.spearmint;
import com.example.spearmint.Experiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a unique user with their own attributes
 *
 * */

public class User implements Serializable {
    private String UUID;
    private String username;
    private String email;
    private String number;
    private ArrayList<Experiment> ownedExperiments;
    private ArrayList<Experiment> subscribedExperiments;

    public User() {}

    public User(String UUID, String username, String email, String number,
                ArrayList<Experiment> ownedExperiments,
                ArrayList<Experiment> subscribedExperiments) {
        this.UUID = UUID;
        this.username = username;
        this.email = email;
        this.number = number;
        this.ownedExperiments = ownedExperiments;
        this.subscribedExperiments = subscribedExperiments;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<Experiment> getOwnedExperiments() {
        return ownedExperiments;
    }

    public void setOwnedExperiments(ArrayList<Experiment> ownedExperiments) {
        this.ownedExperiments = ownedExperiments;
    }

    public ArrayList<Experiment> getSubscribedExperiments() {
        return subscribedExperiments;
    }

    public void setSubscribedExperiments(ArrayList<Experiment> subscribedExperiments) {
        this.subscribedExperiments = subscribedExperiments;
    }
}
