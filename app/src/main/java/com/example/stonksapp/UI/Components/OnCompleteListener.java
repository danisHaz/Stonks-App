package com.example.stonksapp.UI.Components;

public abstract class OnCompleteListener {
    private String tag;

    public enum Result {
        SUCCESS,
        FAILURE
    }

    public abstract void doWork();

    public OnCompleteListener setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() {
        return tag;
    }
}