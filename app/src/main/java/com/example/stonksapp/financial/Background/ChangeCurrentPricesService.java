package com.example.stonksapp.financial.Background;
import com.example.stonksapp.Constants;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;

import java.lang.Thread;

@SuppressLint("SpecifyJobSchedulerIdRange")
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
