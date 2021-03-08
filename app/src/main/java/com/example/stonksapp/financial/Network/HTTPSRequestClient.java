package com.example.stonksapp.financial.Network;

import com.example.stonksapp.financial.StockSymbolsArray;
import com.example.stonksapp.financial.StockSymbol;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.lang.IllegalStateException;

import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class HTTPSRequestClient {

    public StockSymbol[] GET(final String url) {
        final StockSymbolsArray[] finalArr = new StockSymbolsArray[1];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL obj = new URL(url);
                    HttpsURLConnection connection =
                            (HttpsURLConnection) obj.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    connection.setRequestProperty("Content-Type", "application/json");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    String jsonMessage = "{\"type\": ";
                    String str;

                    while ((str = reader.readLine()) != null) {
                        jsonMessage += str;
                    }
                    reader.close();

                    jsonMessage += "}";

                    Gson gson = (new GsonBuilder()).create();

                    finalArr[0] =
                            gson.fromJson(jsonMessage, StockSymbolsArray.class);

                } catch (MalformedURLException e) {
                    Log.d("Err", "Provided wrong URL in GET");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("Err", "IOException occurred in GET");
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    Log.d("Err", "json format is wrong");
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return finalArr[0].arr;
    }

}
