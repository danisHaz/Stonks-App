package com.example.stonksapp.financial.Background;
import com.example.stonksapp.financial.Background.ChangeCurrentPricesService;

import android.util.Log;
import android.content.ComponentName;
import android.app.job.JobScheduler;
import android.app.job.JobInfo;
import android.content.Context;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Network.WebSocketClient;

public class BackgroundTaskHandler {
    private static WebSocketClient client;
    private static int currJobNum = 0;

    public static WebSocketClient getClient() {
        return client;
    }

    private static void createSocketConnection() {
        client = new WebSocketClient(Constants.MAIN_API_URI + Constants.API_TOKEN);
        client.connect();
    }

    private static void scheduleJob(Class<? extends android.app.job.JobService> cls, Context context) {
        ComponentName componentName = new ComponentName(context, cls);
        JobInfo.Builder builder = new JobInfo.Builder(currJobNum++, componentName)
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

    public static void subscribeOnLastPriceUpdates(Context context) {
        if (client == null)
            createSocketConnection();

        scheduleJob(ChangeCurrentPricesService.class, context);
    }

    public static void unsubscribeFromLastPriceUpdates() {
        if (client == null)
            Log.d("D", "Want to unsubscribe from updates when connection does not exist");
        else
            client.disconnect();
    }
}
