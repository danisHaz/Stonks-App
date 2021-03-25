package com.example.stonksapp.UI.Components;

public abstract class OnCompleteListener {
    private String tag;
    private boolean isDelayed = false;

    public enum Result {
        SUCCESS,
        FAILURE
    }

    public abstract void doWork();

    public OnCompleteListener setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() { return tag; }

    public void setWaiting() { isDelayed = true; }

    public boolean isWaiting() { return isDelayed; }
}