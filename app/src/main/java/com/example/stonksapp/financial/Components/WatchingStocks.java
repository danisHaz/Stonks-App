package com.example.stonksapp.financial.Components;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Background.MainReceiver;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.StockSymbol;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WatchingStocks implements FavouriteObject {
    public static List<Stock> watchingStocks = new ArrayList<>();
    private static HTTPSRequestClient.GET getter;

    @Override
    public void setFavourite() { }

    public static void define(Context mActivity) {
        SharedPreferences prefs = mActivity.getSharedPreferences(Constants.WATCH_STONKS_TAG, Context.MODE_PRIVATE);
        getter = new HTTPSRequestClient.GET();

        if (!prefs.getBoolean("isFirstBoot", true))
            BackgroundTaskHandler.myDb.getAll(WatchingStocks.class);
        else {
            HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();
            StockSymbol[] a = getter.StockSymbols(String.format(
                    Constants.GET_STOCK_SYMBOLS_TEMPLATE, "US", "XNGS", Constants.API_TOKEN));

            for (int i = 0; i < java.lang.Math.min(a.length, 10); i++) {
                insert(new Stock(a[i].symbol, a[i].description, a[i].currency, null));
            }

            if (a.length == 0) {
                insert(new Stock("GOOGL", "GOOGLE INC", "US", "N/A"));
                insert(new Stock("AAPL", "APPLE INC", "US", "N/A"));
                insert(new Stock("AMZN", "AMAZON INC", "US", "N/A"));
                insert(new Stock("NFLX", "NETFLIX INC", "US", "N/A"));
                insert(new Stock("TSLA", "TESLA INC", "US", "N/A"));

                Toast.makeText(mActivity, "XNGS not working", Toast.LENGTH_LONG).show();
            }

            MainReceiver.enableAlarm(mActivity);

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

        stock.countPercentage(getter);
        watchingStocks.add(stock);
        BackgroundTaskHandler.myDb.insertCurrent(stock);
    }

}
