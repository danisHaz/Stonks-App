package com.example.stonksapp.UI.Components;

public interface OnCompleteListener {
    enum Result {
        SUCCESS,
        FAILURE
    }

    void doWork();
}