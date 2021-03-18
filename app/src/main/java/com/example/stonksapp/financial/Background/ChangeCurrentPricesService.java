package com.example.stonksapp.financial.Background;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.WatchingStocks;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import org.json.JSONObject;
import android.util.Log;

import java.lang.Thread;
import java.util.ArrayList;

public class ChangeCurrentPricesService extends JobService{

    public ChangeCurrentPricesService() { }
    private static String[] list1;

    private static void lastPricesUpdate(final String json) {

        for (final String symbol: list1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BackgroundTaskHandler.getClient().sendTextViaSocket(
                            String.format(json, symbol)
                    );
                }
            }).start();
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int taskId = params.getExtras().getInt("taskId");
        list1 = params.getExtras().getStringArray("arr");

        Log.d("List sizeof", String.valueOf(list1.length));

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
