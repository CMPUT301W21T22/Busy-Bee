package com.example.spearmint;

import java.util.ArrayList;

/**
 * Base class defining the ExperimentItem object with fields of type String
 * Has getter methods so other classes can retrieve the information of an experiment
 * Has fields "aTitle", "userInfo"
 */

public class ExperimentItem {
    private String aTitle;
    private String userInfo;

    public ExperimentItem(String title, String info){
        aTitle = title;
        userInfo = info;
    }
    public String getaTitle(){
        return aTitle;
    }

    public String getUserInfo() {
        return userInfo;
    }
}