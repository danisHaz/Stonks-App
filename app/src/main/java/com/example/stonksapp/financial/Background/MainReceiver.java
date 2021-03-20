package com.example.stonksapp.financial.Background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.WorkRequest;
import androidx.work.WorkManager;
import androidx.work.PeriodicWorkRequest;


import com.example.stonksapp.Constants;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainReceiver extends BroadcastReceiver {
    public static final int repeatIntervalInDays = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals(Constants.ACTION_QUOTE)) {
            enableAlarm(context);
        }
    }

    public static void enableAlarm(Context context) {
        WorkManager manager = WorkManager.getInstance(context);

        Constraints constrs = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .build();

        WorkRequest request = new PeriodicWorkRequest.Builder(SendDailyQouteWork.class,
                repeatIntervalInDays,
                TimeUnit.DAYS)
                .setConstraints(constrs)
                .build();

        manager.enqueue(request);
    }
}
