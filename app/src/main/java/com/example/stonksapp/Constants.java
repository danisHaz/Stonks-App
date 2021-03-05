package com.example.stonksapp;

public class Constants {
    public static String WATCH_STONKS_TAG = "watchStonksTag";
    public static String MANAGE_YOUR_FAVOURITES_TAG = "manageFavouriteStonksTag";
    public static String MAIN_API_URI = "wss://ws.finnhub.io?token=";
    public static String API_TOKEN = "c0v7fl748v6pr2p77re0";
    public static String LAST_PRICE_UPDATES_JSON_TEMPLATE = "{\"type\": \"subscribe\", \"symbol\": \"%s\"}";

    public static boolean isNetworkConnectionProvided(android.content.Context context) {
        android.net.ConnectivityManager manager =
                (android.net.ConnectivityManager) context.getSystemService(
                        android.content.Context.CONNECTIVITY_SERVICE);

        for(android.net.Network network: manager.getAllNetworks()) {
            if (manager.getNetworkInfo(network).isConnected())
                return true;
        }

        return false;
    }
}