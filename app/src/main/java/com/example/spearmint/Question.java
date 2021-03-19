package com.example.spearmint;

/**
 * Base class for the Question object with fields of type String
 * has getter methods so other classes can access the information held by a Question
 * @author Andrew
 */

public class Question {

    private String question;
    private String answer;

    Question (String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }
}

