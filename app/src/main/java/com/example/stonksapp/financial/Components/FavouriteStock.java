package com.example.stonksapp.financial.Components;

import android.util.Log;

import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.UI.Components.WorkDoneListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FavouriteStock implements FavouriteObject {
    public static volatile List<Stock> currentFavourites = new ArrayList<>();
    private static TreeSet<Stock> delayedDelete = new TreeSet<>();
    private static HTTPSRequestClient.GET getter;

    @Override
    public void setFavourite() {
        // add favourite stock
    }

    public static void define() {
        if (getter == null)
            getter = new HTTPSRequestClient.GET();
        BackgroundTaskHandler.myDb.getAll(FavouriteStock.class);

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public void doWork() {
                MainActivity.definitionWorksListener();
            }
        }.setTag(Constants.DO_FAVOURITE_DEFINITION_WORK));
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

    public static boolean isInDelayedDeletion(Stock stock) {
        for (Stock s: delayedDelete) {
            if (s.symbol.equals(stock.symbol))
                return true;
        }

        return false;
    }

    public static boolean isInDelayedDeletion(int pos) {
        if (pos >= currentFavourites.size()) {
            Log.e("FavouriteStock", "Chosen Stock in favourites has incorrect position");
            return false;
        }
        for (Stock s: delayedDelete) {
            if (s.symbol.equals(currentFavourites.get(pos).symbol))
                return true;
        }

        return false;
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
            Log.i("FavouriteStock", "Provided stock already in favourites");
            return;
        }

        stock.countPercentage(getter);
        currentFavourites.add(stock);
        BackgroundTaskHandler.myDb.insertFavourite(stock);
    }

    public static void updateFavourite(Stock stock) {
        for (int i = 0; i < currentFavourites.size(); i++) {
            if (currentFavourites.get(i).symbol.equals(stock.symbol)) {
                Stock stck = currentFavourites.get(i);
                stck.mergeNonNull(stock);
                currentFavourites.set(i, stck);
                BackgroundTaskHandler.myDb.updateFavourite(stck);
                return;
            }
        }

    }

    public static void saveToDataBase() {
        for (Stock stock: currentFavourites) {
            BackgroundTaskHandler.myDb.updateFavourite(stock);
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

        Log.e("FavouriteStock", "Attempt to delete element from favourites that not exist");
    }

}
