package com.example.stonksapp.financial.Background;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import android.content.Context;
import android.util.Log;

import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.UI.Components.WorkDoneListener;
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

        Log.d("i am", "here");

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork() {
                HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();

                Log.d("SendDailyQuoteWork", "Main work is running");

                for (Stock stock: WatchingStocks.watchingStocks) {
                    stock.countPercentage(getter, false);
                }

                for (Stock stock: FavouriteStock.currentFavourites) {
                    stock.countPercentage(getter, true);
                }
            }
        }.setTag(Constants.DO_DAILY_WORK));

        if (BackgroundTaskHandler.myDb == null) {
            BackgroundTaskHandler.defineDB(getApplicationContext());
            WatchingStocks.define(getApplicationContext());
            FavouriteStock.define(getApplicationContext());
        }

        return ListenableWorker.Result.success();
    }

}
