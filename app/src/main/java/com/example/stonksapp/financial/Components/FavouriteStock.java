package com.example.stonksapp.financial.Components;

import android.util.Log;

import com.example.stonksapp.financial.Background.BackgroundTaskHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FavouriteStock implements FavouriteObject {
    public static List<Stock> currentFavourites = new ArrayList<>();
    private static TreeSet<Stock> delayedDelete = new TreeSet<>();

    @Override
    public void setFavourite() {
        // add favourite stock
    }

    public static void define() {
        BackgroundTaskHandler.myDb.getAll(FavouriteStock.class);
    }

    public static ArrayList<String> getSymbols() {
        ArrayList<String> symbolsList = new ArrayList<>();

        for (Stock stock: currentFavourites) {
            symbolsList.add(stock.symbol);
        }

        return symbolsList;
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
        if (isInFavourites(stock) != -1) {
            Log.d("Warn", "Provided stock already in favourites");
            return;
        }

        currentFavourites.add(stock);
        BackgroundTaskHandler.myDb.insertFavourite(stock);
    }

    public static void updateFavourite(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            if (currentFavourites.get(i).symbol.equals(stock.symbol)) {
                currentFavourites.set(i, stock);
                BackgroundTaskHandler.myDb.updateFavourite(stock);
                return;
            }
        }

    }

    public static void deleteFromFavourites(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            Stock currStock  = currentFavourites.get(i);
            if (currStock.symbol.equals(stock.symbol)) {
                currentFavourites.remove(i);
                BackgroundTaskHandler.myDb.deleteFromFavourites(currStock);
                return;
            }
        }

        Log.e("Err", "Attempt to delete element from favourites that not exist");
    }

}
