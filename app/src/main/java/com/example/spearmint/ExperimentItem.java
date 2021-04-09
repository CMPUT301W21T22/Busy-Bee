package com.example.spearmint;

import java.util.ArrayList;

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