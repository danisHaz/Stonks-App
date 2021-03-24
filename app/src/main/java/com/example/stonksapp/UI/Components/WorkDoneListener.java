package com.example.stonksapp.UI.Components;

import android.util.Log;

import java.util.ArrayList;
import java.lang.Exception;

public class WorkDoneListener {

    private static ArrayList<OnCompleteListener> listenerList = new ArrayList<>();

    public static synchronized void setNewListener(OnCompleteListener newListener) {
        if (newListener.getTag() == null) {
            Log.e("WorkDoneListener", "Set listener with no tag");
            return;
        }

        listenerList.add(newListener);
    }

    public static void complete(String tag, OnCompleteListener.Result result) throws NullPointerException {
        if (result == OnCompleteListener.Result.SUCCESS) {
            Log.i("WorkDoneListener", "Previous work complete");
        } else {
            Log.i("WorkDoneListener", "Previous work incomplete");
        }

        for (int i = 0; i < listenerList.size(); i++) {
            if (listenerList.get(i).getTag().equals(tag)) {
                listenerList.get(i).doWork();
                listenerList.remove(i);
                Log.i("WorkDoneListener", "Work Done");
                return;
            }
        }

        Log.e("WorkDoneListener", "Want to do work in non-existing listener");
    }
}
