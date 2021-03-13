package com.example.stonksapp.financial.Components;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.StockSymbol;

import java.util.ArrayList;

public class WatchingStocks {
    public static ArrayList<Stock> watchingStocks = new ArrayList<>();

    public static void define() {
        HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
        StockSymbol[] curArray = client.StockSymbols(String.format(
                Constants.GET_STOCK_SYMBOLS_TEMPLATE, "US", Constants.API_TOKEN));

        for (int pos = 0; pos < 10; pos++) {
            watchingStocks.add(new Stock(curArray[pos].symbol, "US"));
        }
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
            }
        }
    }

}
