package com.example.stonksapp.financial.Background;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.Constants;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import org.json.JSONObject;
import android.util.Log;

import java.lang.Thread;
import java.util.ArrayList;

public class ChangeCurrentPricesService extends JobService{

    public ChangeCurrentPricesService() { }
    private static String[] list;

    private static void lastPricesUpdate(final String json) {
        Log.d("message", "superMessage");
        for (final String symbol: list) {
            Log.d("symbol in last price", symbol);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BackgroundTaskHandler.getClient().sendTextViaSocket(
                            String.format(
                                    json, symbol));
                }
            }).start();
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int taskId = params.getExtras().getInt("taskId");
        list = params.getExtras().getStringArray(Constants.CHOSEN_SYMBOLS);

        if (taskId == Constants.SUBSCRIBE_LAST_PRICE_UPDATES_ID)
            lastPricesUpdate(Constants.SUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE);
        else
            lastPricesUpdate(Constants.UNSUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
