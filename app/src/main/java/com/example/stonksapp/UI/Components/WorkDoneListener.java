package com.example.stonksapp.UI.Components;

import android.util.Log;

public class WorkDoneListener {

    private static OnCompleteListener listener;

    public static synchronized void setNewListener(OnCompleteListener newListener) {
        if (listener == null)
            listener = newListener;
        else {
            Log.i("WorkDoneListener", "Listener is already set");
        }
    }

    public static void complete(OnCompleteListener.Result result) throws NullPointerException {
        if (result == OnCompleteListener.Result.SUCCESS) {
            Log.i("WorkDoneListener", "Previous work complete");
        } else {
            Log.i("WorkDoneListener", "Previous work incomplete");
        }

        listener.doWork();
        listener = null;
        Log.i("WorkDoneListener", "Work Done");
    }
}
