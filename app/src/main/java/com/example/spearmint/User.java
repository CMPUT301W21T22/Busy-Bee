package com.example.spearmint;

/**
 * This class models a user with their own attributes.
 * A user has a unique ID, username, email, phone number, their own experiments and
 * their subscribed experiments
 * @author Gavriel and Michael
 *
 * Ruiqin Pi, "Lab 3", 2021-02-03, Public Domain,
 * https://drive.google.com/file/d/1ePP0TvI6-ZWnq6rBCvGgaRn6UPNI-KgS/view?usp=sharing
 * */

import java.io.Serializable;
import java.util.ArrayList;

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