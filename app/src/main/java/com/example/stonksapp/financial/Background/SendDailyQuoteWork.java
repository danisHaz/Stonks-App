package com.example.stonksapp.financial.Background;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.util.Log;

import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.WatchingStocks;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;

public class SendDailyQuoteWork extends Worker {
    SendDailyQuoteWork(@NonNull Context context,
                       @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        if (!MainActivity.ifNetworkProvided) {
            Log.w("SendDailyQuoteWork", "Trying to update when network is not provided");
            return ListenableWorker.Result.failure();
        }

        if (BackgroundTaskHandler.myDb == null) {
            BackgroundTaskHandler.defineDB(getApplicationContext());
            WatchingStocks.define(getApplicationContext());
            FavouriteStock.define();
        }

        HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();

        for (Stock stock: WatchingStocks.watchingStocks) {
            stock.countPercentage(getter, false);
        }

        for (Stock stock: FavouriteStock.currentFavourites) {
            stock.countPercentage(getter, true);
        }

        return ListenableWorker.Result.success();
    }

}
