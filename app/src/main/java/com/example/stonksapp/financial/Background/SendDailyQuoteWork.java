package com.example.stonksapp.financial.Background;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.WatchingStocks;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Quote;

public class SendDailyQuoteWork extends Worker {
    SendDailyQuoteWork(@NonNull Context context,
                       @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    @NonNull
    public ListenableWorker.Result doWork() {
        if (BackgroundTaskHandler.myDb == null) {
            BackgroundTaskHandler.defineDB(getApplicationContext());
            WatchingStocks.define(getApplicationContext());
            FavouriteStock.define();
        }

        HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();

        for (Stock stock: WatchingStocks.watchingStocks) {
            stock.countPercentage(getter);
        }

        for (Stock stock: FavouriteStock.currentFavourites) {
            stock.countPercentage(getter);
        }

        return ListenableWorker.Result.success();
    }

}
