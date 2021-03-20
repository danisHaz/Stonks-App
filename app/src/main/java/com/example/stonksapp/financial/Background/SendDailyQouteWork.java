package com.example.stonksapp.financial.Background;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;

public class SendDailyQouteWork extends Worker {
    SendDailyQouteWork(@NonNull Context context,
                       @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        // todo: get quotes and update Db

        return ListenableWorker.Result.success();
    }

}
