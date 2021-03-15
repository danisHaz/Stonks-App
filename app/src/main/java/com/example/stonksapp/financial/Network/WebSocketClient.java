package com.example.stonksapp.financial.Network;

import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Components.WatchingStocks;
import com.example.stonksapp.financial.TradesPrices;
import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.FavouriteStock;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import android.util.Log;

public class WebSocketClient {
    private WebSocket socket;
    private String socketUri;
    public final boolean[] isConnected = new boolean[1];
    private MainActivity activity;

    public WebSocketClient(String uri, MainActivity activity) {
        socketUri = uri;
        isConnected[0] = false;
        this.activity = activity;
    }

    private void reconnect() {
        try {
            socket.recreate().connect();
        } catch (java.io.IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        // TODO: rewrite to RxJava
        Thread thready = new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    reconnect();
                } else {
                    try {

                        socket = new WebSocketFactory()
                                .setConnectionTimeout(5000)
                                .createSocket(socketUri);
                        socket.addListener(new SocketListener());
                        socket.connect();

                        isConnected[0] = true;
                        Log.d("Socket", "Socket connection done");

                    } catch (IOException | WebSocketException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thready.start();

        try {
            thready.join();
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        socket.disconnect();
        isConnected[0] = false;
    }

    public WebSocket getSocket() {
        return socket;
    }

    public void sendTextViaSocket(String someText) {
        socket.sendText(someText);
        Log.d("Socket", "Message sent");
    }

    // class to listen to WebSocket actions
    public class SocketListener extends WebSocketAdapter {
        private String listenerTag = "SocketListenerTag";
        private int limit = 0;

        // todo: all updates should work async
        private void update(String message) {

            Gson gson = (new GsonBuilder()).create();

            try {
                Stock stock = Stock.from(gson.fromJson(message, TradesPrices.class));

                int resultId;
                if (MainActivity.attachedFragmentTag.equals(Constants.WATCH_STONKS_TAG)) {
                    WatchingStocks.update(stock);
                    resultId = activity.getWatchStonksFragment().updateAndRefresh();
                } else {
                    FavouriteStock.updateFavourite(stock);
                    resultId = activity.getManageFavouriteFragment().updateAndRefresh();
                    Log.d("Tag", "After");
                }

                if (resultId == Constants.SUCCESS) {
                    Log.d("Socket Updater", "update success");
                } else {
                    Log.d("Socket Updater", "update failure");
                }
            } catch (JsonSyntaxException e) {
                Log.d("Err", "Provided class for JSON is not valid");
                e.printStackTrace();
            }
        }

        @Override
        public void onConnected(WebSocket ws, Map<String, List<String>> headers) throws Exception {
            super.onConnected(ws, headers);
        }

        @Override
        public void onTextMessage(WebSocket ws, String message) {
//            Log.i("MessageReceived", message);

            if (message.equals(Constants.PING_MESSAGE))
                return;

            // todo: set some frequency limits

            update(message);
        }

        @Override
        public void onError(WebSocket ws, WebSocketException e) {
            try {
                Log.d(listenerTag, e.getMessage());
            } catch (java.lang.NullPointerException err) {
                err.printStackTrace();
            }

            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket ws, WebSocketFrame serverFrame,
                                   WebSocketFrame clientFrame, boolean isClosedByServer) {
            Log.d(listenerTag, "onDisconnected");
            if (isClosedByServer)
                reconnect();
        }

        @Override
        public void onUnexpectedError(WebSocket ws, WebSocketException err) {
            try {
                Log.d(listenerTag, err.getMessage());
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }

            reconnect();
        }

        @Override
        public void onPongFrame(WebSocket ws, WebSocketFrame frame) throws Exception {
            super.onPongFrame(ws, frame);
            Log.d("PingPong", frame.toString());
            ws.sendPing("Ping");
        }
    }
}