package com.example.stonksapp.financial.Background;
import com.example.stonksapp.Constants;

import android.util.Log;
import android.content.ComponentName;
import android.app.job.JobScheduler;
import android.app.job.JobInfo;
import android.content.Context;
import android.os.PersistableBundle;

import com.example.stonksapp.UI.Activities.MainActivity;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Network.WebSocketClient;

public class BackgroundTaskHandler {
    private static int currJobNum = 0;

    private static void scheduleJob(Class<? extends android.app.job.JobService> cls,
                                    Context context,
                                    byte taskId, int currentClass) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("taskId", taskId);
        bundle.putInt("currentClass", currentClass);

        ComponentName componentName = new ComponentName(context, cls);
        JobInfo.Builder builder = new JobInfo.Builder(currJobNum++, componentName)
                .setExtras(bundle)
                .setRequiresCharging(false);

        JobScheduler scheduler =
                (JobScheduler) context.getSystemService(JobScheduler.class);

        int res = scheduler.schedule(builder.build());

        if (res == JobScheduler.RESULT_SUCCESS) {
            Log.d("D", String.format("Job %d result success", currJobNum - 1));
        } else {
            Log.d("D", String.format("Job %d result failure", currJobNum - 1));
        }
    }

    public static void subscribeOnLastPriceUpdates(
            MainActivity activity, int currentClass) {
        // some additional definitions

        scheduleJob(ChangeCurrentPricesService.class, activity,
                Constants.SUBSCRIBE_LAST_PRICE_UPDATES_ID, currentClass);
    }

    public static void unsubscribeFromLastPriceUpdates(MainActivity activity,
                                                       int currentClass) {
        scheduleJob(ChangeCurrentPricesService.class, activity,
                Constants.UNSUBSCRIBE_LAST_PRICE_UPDATES_ID, currentClass);
    }

}
