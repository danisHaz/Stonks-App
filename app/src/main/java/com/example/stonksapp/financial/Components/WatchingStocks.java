package com.example.stonksapp.financial.Components;

import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WatchingStocks implements FavouriteObject {
    public static List<Stock> watchingStocks = new ArrayList<>();
    private static MainActivity activity;

    @Override
    public void setFavourite() { }

    public static void define(MainActivity mActivity) {
        activity = mActivity;
        SharedPreferences prefs = mActivity.getSharedPreferences(Constants.WATCH_STONKS_TAG, Context.MODE_PRIVATE);

        if (!prefs.getBoolean("isFirstBoot", true))
            BackgroundTaskHandler.myDb.getAll(WatchingStocks.class);
        else {

            insert(new Stock("GOOGL", "GOOGLE INC",  "US", "N/A"));
            insert(new Stock("AAPL", "APPLE INC", "US", "N/A"));
            insert(new Stock("AMZN", "AMAZON INC", "US", "N/A"));
            insert(new Stock("NFLX", "NETFLIX INC", "US", "N/A"));
            insert(new Stock("TSLA", "TESLA INC", "US", "N/A"));

            SharedPreferences.Editor pprefs =  prefs.edit();
            pprefs.putBoolean("isFirstBoot", false);
            pprefs.apply();
        }

    }

    public static ArrayList<String> getSymbols() {
        ArrayList<String> symbolsList = new ArrayList<>();

        for (Stock stock: watchingStocks) {
            symbolsList.add(stock.symbol);
        }

        return symbolsList;
    }

    public static void update(Stock stock) {
        for (int i = 0; i < watchingStocks.size(); i++) {
            if (watchingStocks.get(i).symbol.equals(stock.symbol)) {
                Stock stck = watchingStocks.get(i);
                stck.mergeNonNull(stock);
                watchingStocks.set(i, stck);
                BackgroundTaskHandler.myDb.updateCurrent(stck);
                return;
            }
        }
    }

    public static void insert(Stock stock) {
        for (int i = 0; i < watchingStocks.size(); i++) {
            if (watchingStocks.get(i).symbol.equals(stock.symbol)) {
                Log.d("Warn", "Provided stock already in current");
                return;
            }
        }

        watchingStocks.add(stock);
        BackgroundTaskHandler.myDb.insertCurrent(stock);
    }

}
