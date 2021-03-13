package com.example.stonksapp.financial.Components;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

public class FavouriteStock implements FavouriteObject {
    private static StockDataBase myDB;
    public static boolean isDefined = false;
    public static List<Stock> currentFavourites;
    private static TreeSet<Stock> delayedDelete = new TreeSet<>();

    @Override
    public void setFavourite() {
        // add favourite stock
    }

    public static void setFavouriteDelayDeleted(int pos) {
        delayedDelete.add(currentFavourites.get(pos));
    }

    public static void deleteAllDelayed() {
        for (Stock stock: delayedDelete) {
            deleteFromFavourites(stock);
        }
        delayedDelete.clear();
    }

    public static void cancelDeletion(int pos) {
        delayedDelete.remove(currentFavourites.get(pos));
    }

    public static int isInFavourites(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            if (currentFavourites.get(i).symbol.equals(stock.symbol)) {
                return i;
            }
        }
        return -1;
    }

    public static void addToFavourites(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            if (currentFavourites.get(i).symbol.equals(stock.symbol)) {
                Log.d("Warn", "Provided stock already in favourites");
                return;
            }
        }

        currentFavourites.add(stock);
        myDB.insertFavourite(stock);
    }

    public static void updateFavourite(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            if (currentFavourites.get(i).symbol.equals(stock.symbol)) {
                currentFavourites.set(i, stock);
                myDB.updateFavourite(stock);
            }
        }

        Log.e("Err", "Unable to find and update stock");
    }

    public static void deleteFromFavourites(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            Stock currStock  = currentFavourites.get(i);
            if (currStock.symbol.equals(stock.symbol)) {
                currentFavourites.remove(i);
                myDB.deleteFromFavourites(currStock);
                return;
            }
        }

        Log.e("Err", "Attempt to delete element from favourites that not exist");
    }

    public static void defineDB(Context context) {
        myDB = StockDataBase.createInstance(context);
        myDB.getAll(FavouriteStock.class);

        isDefined = true;
    }
}
