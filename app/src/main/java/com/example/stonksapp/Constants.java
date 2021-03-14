package com.example.stonksapp;

public class Constants {
    public static final String WATCH_STONKS_TAG = "watchStonksTag";
    public static final String MANAGE_YOUR_FAVOURITES_TAG = "manageFavouriteStonksTag";
    public static final String MAIN_API_URI = "wss://ws.finnhub.io?token=";
    public static final String API_TOKEN = "c12ht2f48v6oi252p5ag";
    public static final String SUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE =
            "{\"type\": \"subscribe\", \"symbol\": \"%s\"}";

    public static final String UNSUBSCRIBE_LAST_PRICE_UPDATES_JSON_TEMPLATE =
            "{\"type\": \"unsubscribe\", \"symbol\": \"%s\"}";

    public static final String GET_STOCK_SYMBOLS_TEMPLATE =
            "https://finnhub.io/api/v1/stock/symbol?exchange=%s&token=%s";

    public static final String GET_SYMBOL_LOOKUP_TEMPLATE =
            "https://finnhub.io/api/v1/search?q=%s&token=%s";

    public static final byte SUBSCRIBE_LAST_PRICE_UPDATES_ID = 0;
    public static final byte UNSUBSCRIBE_LAST_PRICE_UPDATES_ID = 1;
    public static final String CHOSEN_SYMBOLS = "chosenSymbols";

    public static final int SUCCESS = 116;
    public static final int FAILURE = 777;
    public static final String PING_MESSAGE = "{\"type\":\"ping\"}";

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

    public static String[] toStringArray(java.util.List<String> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < result.length; i++)
                result[i] = list.get(i);

        return result;
    }
}