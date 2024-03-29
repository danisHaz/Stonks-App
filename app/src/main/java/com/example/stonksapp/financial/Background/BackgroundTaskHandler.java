package com.example.stonksapp.financial.Background;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.StockDataBase;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Network.WebSocketClient;

import android.util.Log;
import android.content.ComponentName;
import android.app.job.JobScheduler;
import android.app.job.JobInfo;
import android.content.Context;
import android.os.PersistableBundle;

public class BackgroundTaskHandler {
    private static int currJobNum = 0;
    private static WebSocketClient client;
    public static StockDataBase myDb;

    public static WebSocketClient getClient() { return client; }

    public static void defineDB(Context mActivity) {
        if (myDb != null)
            return;
        myDb = StockDataBase.createInstance(mActivity);
    }

    public static void createConnection(MainActivity activity) {
        if (client != null) {
            Log.e("Err", "connection already exists");
            return;
        }

        client = new WebSocketClient(Constants.MAIN_API_URI + Constants.API_TOKEN,
                activity);
        client.connect();
    }

    public static void destroyConnection() {
        if (client != null)
            client.disconnect();
        client = null;
    }

    private static void scheduleJob(Class<? extends android.app.job.JobService> cls,
                                    Context context,
                                    byte taskId, String[] arr) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("taskId", taskId);
        bundle.putStringArray("arr", arr);

        ComponentName componentName = new ComponentName(context, cls);
        JobInfo.Builder builder = new JobInfo.Builder(currJobNum++, componentName)
                .setExtras(bundle)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        JobScheduler scheduler =
                (JobScheduler) context.getSystemService(JobScheduler.class);

        int res = scheduler.schedule(builder.build());

        if (res == JobScheduler.RESULT_SUCCESS) {
            Log.d("D", String.format("Job %d result success", currJobNum - 1));
        } else {
            Log.d("D", String.format("Job %d result failure", currJobNum - 1));
        }
    }

    public static void subscribeOnLastPriceUpdates(String[] arr, Context context) {

        scheduleJob(ChangeCurrentPricesService.class, context,
                Constants.SUBSCRIBE_LAST_PRICE_UPDATES_ID, arr);
    }


    public static void unsubscribeFromLastPriceUpdates(String[] arr, Context context) {
        if (client == null) {
            Log.i("BackgroundTaskHandler", "Connection was not provided");
            return;
        }

        scheduleJob(ChangeCurrentPricesService.class, context,
                Constants.UNSUBSCRIBE_LAST_PRICE_UPDATES_ID, arr);
    }

}
