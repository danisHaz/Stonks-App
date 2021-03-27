package com.example.stonksapp.financial.Network;

import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.financial.StockSymbolsArray;
import com.example.stonksapp.financial.StockSymbol;
import com.example.stonksapp.financial.Quote;

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

import androidx.annotation.Nullable;

import javax.net.ssl.HttpsURLConnection;

public class HTTPSRequestClient {

    public static class GET {
        private BufferedReader getJSONinString(String url, @Nullable HttpsURLConnection connection) {
            if (!MainActivity.ifNetworkProvided)
                return null;

            BufferedReader reader = null;
            try {
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setRequestProperty("Content-Type", "application/json");

                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("HTTPSRequestClient", "Connection is null");
            }

            if (reader == null)
                Log.e("HTTPSRequestClient", "Connection failed");

            return reader;
        }

        public StockSymbol[] StockSymbols(final String url) {
            final StockSymbolsArray[] finalArr = new StockSymbolsArray[1];

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection connection = null;
                    try {
                        URL obj = new URL(url);
                        connection = (HttpsURLConnection) obj.openConnection();
                        BufferedReader reader = getJSONinString(url, connection);
                        if (reader == null)
                            return;

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
                        Log.d("HTTPSRequestClient", "Provided wrong URL in stock symbols");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d("HTTPSRequestClient", "IOException occurred in stock symbols");
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        Log.d("HTTPSRequestClient", "json format is wrong");
                        e.printStackTrace();
                    }

                    if (connection != null)
                        connection.disconnect();;
                }
            });

            thread.setDaemon(true);

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (finalArr[0] == null)
                return null;

            return finalArr[0].arr;
        }

        public SymbolQuery symbolLookup(final String url) {
            final SymbolQuery[] res = new SymbolQuery[1];
            HttpsURLConnection connection = null;
            try {
                URL obj = new URL(url);
                connection = (HttpsURLConnection) obj.openConnection();
                BufferedReader reader = getJSONinString(url, connection);

                String jsonMessage = "";
                String str;

                while ((str = reader.readLine()) != null) {
                    jsonMessage += str;
                }
                reader.close();

                Gson gson = (new GsonBuilder()).create();

                res[0] = gson.fromJson(jsonMessage, SymbolQuery.class);

            } catch (MalformedURLException e) {
                Log.d("HTTPSRequestClient", "Provided wrong URL in symbol lookup");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("HTTPSRequestClient", "IOException occurred in symbol lookup");
                e.printStackTrace();
            } catch (IllegalStateException e) {
                Log.d("HTTPSRequestClient", "json format is wrong");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.d("HTTPSRequestClient", "Connection is not stable or provided");
            }
            if (connection != null)
                connection.disconnect();

            return res[0];
        }

        public Quote quote(final String url) {
            final Quote[] res = new Quote[1];

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection connection = null;
                    try {
                        URL obj = new URL(url);
                        connection = (HttpsURLConnection) obj.openConnection();
                        BufferedReader reader = getJSONinString(url, connection);

                        if (reader == null)
                            return;

                        String jsonMessage = "";
                        String str;

                        while ((str = reader.readLine()) != null) {
                            jsonMessage += str;
                        }
                        reader.close();

                        Gson gson = (new GsonBuilder()).create();

                        res[0] = gson.fromJson(jsonMessage, Quote.class);

                    } catch (MalformedURLException e) {
                        Log.d("HTTPSRequestClient", "Provided wrong URL in quote");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d("HTTPSRequestClient", "IOException occurred in quote");
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        Log.d("HTTPSRequestClient", "json format is wrong");
                        e.printStackTrace();
                    }

                    if (connection != null)
                        connection.disconnect();;
                }
            });

            thread.setDaemon(true);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return res[0];
        }
    }

}
