package com.example.stonksapp.UI.Components;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WorkDoneListener {

    private static ArrayList<OnCompleteListener> listenerList = new ArrayList<>();

    public static synchronized void setNewListener(@NonNull OnCompleteListener newListener) {
        if (newListener.getTag() == null) {
            Log.e("WorkDoneListener", "Set listener with no tag");
            return;
        }

        listenerList.add(newListener);
    }

    private static int getPosition(final String tag) {
        for (int i = 0; i < listenerList.size(); i++) {
            if (listenerList.get(i).getTag().equals(tag))
                return i;
        }

        return -1;
    }

    public static void complete(@NonNull final String tag, OnCompleteListener.Result result) throws NullPointerException {
        if (result == OnCompleteListener.Result.SUCCESS) {
            Log.i("WorkDoneListener", "Previous work complete");
        } else {
            Log.i("WorkDoneListener", "Previous work incomplete");
        }

        int i = getPosition(tag);
        if (i != -1) {
            listenerList.get(i).doWork();
            listenerList.remove(i);
            Log.i("WorkDoneListener", "Work Done");
            return;
        }

        Log.e("WorkDoneListener", "Want to do work in non-existing listener");
    }

    public static boolean isListenerSet(@NonNull final String tag) {
        return getPosition(tag) != -1;
    }

    public static void setListenerWaiting(@NonNull final String tag) {
        int i = getPosition(tag);
        if (i != -1) {
            listenerList.get(i).setWaiting();
        } else
            Log.w("WorkDoneListener", "Set non-existing listener waiting");
    }

    public static boolean isListenerWaiting(@NonNull final String tag) throws ArrayIndexOutOfBoundsException {
        return listenerList.get(getPosition(tag)).isWaiting();
    }
}
