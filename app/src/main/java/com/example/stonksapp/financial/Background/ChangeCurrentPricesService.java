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
    private static String[] list;

    private static void lastPricesUpdate(final String json, final int currentClass) {
        for (final String symbol: list) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (currentClass == Constants.CURRENT_CLASS_IS_FAVOURITE)
                        FavouriteStock.getClient().sendTextViaSocket(
                                String.format(
                                        json, symbol));
                    else if (currentClass == Constants.CURRENT_CLASS_IS_WATCHING)
                        WatchingStocks.getClient().sendTextViaSocket(
                                String.format(
                                        json, symbol));
                }
            }).start();
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int taskId = params.getExtras().getInt("taskId");
        int currentClass = params.getExtras().getInt("currentClass");
        list = currentClass == Constants.CURRENT_CLASS_IS_WATCHING ?
                Constants.toStringArray(WatchingStocks.getSymbols())
                : Constants.toStringArray(FavouriteStock.getSymbols());

        if (taskId == Constants.SUBSCRIBE_LAST_PRICE_UPDATES_ID)
            lastPricesUpdate(Constants.SUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE, currentClass);
        else
            lastPricesUpdate(Constants.UNSUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE, currentClass);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
