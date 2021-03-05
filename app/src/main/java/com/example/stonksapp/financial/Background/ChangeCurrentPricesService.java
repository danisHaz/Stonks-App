package com.example.stonksapp.financial.Background;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.Constants;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import org.json.JSONObject;
import android.util.Log;

import java.lang.Thread;

public class ChangeCurrentPricesService extends JobService{

    public ChangeCurrentPricesService() { }

    private static void subscribeOnLastPrices() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundTaskHandler.getClient().sendTextViaSocket(
                        String.format(
                                Constants.SUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE, "AAPL"));
            }
        }).start();
    }

    private static void unsubscribeFromLastPrices() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundTaskHandler.getClient().sendTextViaSocket(
                        String.format(
                                Constants.UNSUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE, "AAPL"));
            }
        }).start();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int taskId = params.getExtras().getInt("taskId");
        if (taskId == Constants.SUBSCRIBE_LAST_PRICE_UPDATES_ID)
            subscribeOnLastPrices();
        else
            unsubscribeFromLastPrices();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
