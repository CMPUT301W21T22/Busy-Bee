package com.example.spearmint;

public class NonCount {
    private String noncountDescription;
    private String noncountResult;

    NonCount(String noncountDescription, String noncountResult) {
        this.noncountDescription = noncountDescription;
        this.noncountResult = noncountResult;
    }

    public String getNoncountDescription() {
        return this.noncountDescription;
    }

    public String getNoncountResult() {
        return this.noncountResult;
    }
}