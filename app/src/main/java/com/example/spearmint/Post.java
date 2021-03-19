package com.example.spearmint;

public class Post {

    private String experimentTitle;
    private String text;

    Post(String experimentTitle, String text){
        this.experimentTitle = experimentTitle;
        this.text = text;
    }

    public String getExperimentTitle() {
        return this.experimentTitle;
    }

    public String getText() {
        return this.text;
    }

}
