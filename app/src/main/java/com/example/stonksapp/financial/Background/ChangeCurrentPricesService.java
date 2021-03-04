package com.example.stonksapp.financial.Background;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.Constants;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import java.lang.Thread;

public class ChangeCurrentPricesService extends JobService{

    public ChangeCurrentPricesService() { }

    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BackgroundTaskHandler.getClient().sendTextViaSocket(
                        String.format(Constants.LAST_PRICE_UPDATES_JSON_TEMPLATE, "AAPL"));
            }
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
