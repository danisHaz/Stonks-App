package com.example.stonksapp.financial.Network;

import com.example.stonksapp.Constants;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.Map;
import java.util.List;

import android.util.Log;

public class WebSocketClient {
    private WebSocket socket;

    WebSocketClient() {

    }

    public void reconnect() {
        try {
            socket.recreate().connect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        // TODO: rewrite to RxJava
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    reconnect();
                } else {
                    try {
                        socket = new WebSocketFactory().createSocket(
                                Constants.MAIN_API_URI + Constants.API_TOKEN);

                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public class SocketListener extends WebSocketAdapter {
        private String listenerTag = "SocketListenerTag";

        @Override
        public void onConnected(WebSocket ws, Map<String, List<String>> headers) throws Exception {
            super.onConnected(ws, headers);
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
    }
}
