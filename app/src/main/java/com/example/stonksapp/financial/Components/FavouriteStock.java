package com.example.stonksapp.financial.Components;

import android.content.Context;

import com.example.stonksapp.financial.Components.StockDataBase;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.FavouriteObject;

import java.util.ArrayList;

public class FavouriteStock implements FavouriteObject {
    private static StockDataBase myDB;
    public static boolean isDefined = false;
    public static ArrayList<Stock> currentFavourites;

    public static void defineDB(Context context) {
        myDB = StockDataBase.createInstance(context);
        myDB.getAll(FavouriteStock.class);

        isDefined = true;
    }
}
