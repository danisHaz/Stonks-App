package com.example.stonksapp.financial.Components;

import com.example.stonksapp.financial.Network.WebSocketClient;
import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchingStocks {
    public static List<Stock> watchingStocks = new ArrayList<>();
    public static WebSocketClient client;
    private static MainActivity activity;

    private static void createSocketConnection() {
        client = new WebSocketClient(Constants.MAIN_API_URI + Constants.API_TOKEN,
                activity);
        client.connect();
    }

    private static void destroySocketConnection() {
        client.disconnect();
    }

    public static void destroy() {
        destroySocketConnection();
        client = null;
    }

    public static WebSocketClient getClient() { return client; }

    public static void define(MainActivity mActivity) {
        activity = mActivity;
        createSocketConnection();
//        HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
//        StockSymbol[] curArray = client.StockSymbols(String.format(
//                Constants.GET_STOCK_SYMBOLS_TEMPLATE, "US", Constants.API_TOKEN));
//
//        for (int pos = 0; pos < 10; pos++) {
//            watchingStocks.add(new Stock(curArray[pos].symbol, "US"));
//        }

        watchingStocks.add(new Stock("GOOGL", "US"));
        watchingStocks.add(new Stock("AMZN", "US"));
        watchingStocks.add(new Stock("AAPL", "US"));
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
                watchingStocks.set(i, stock);
                return;
            }
        }
    }

}
