package com.example.spearmint;

/**
 * Base class for the Post object with fields of type String
 * has getter methods so other classes can access the information held by a Post
 * @author Daniel
 */

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
