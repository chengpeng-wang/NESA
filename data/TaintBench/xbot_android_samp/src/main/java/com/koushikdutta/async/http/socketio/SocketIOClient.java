package com.koushikdutta.async.http.socketio;

import android.os.Handler;
import android.text.TextUtils;
import com.codebutler.android_websockets.WebSocketClient;
import com.koushikdutta.http.AsyncHttpClient;
import com.koushikdutta.http.AsyncHttpClient.SocketIORequest;
import org.json.JSONArray;
import org.json.JSONObject;

public class SocketIOClient extends EventEmitter {
    ConnectCallback connectCallback;
    boolean connected;
    SocketIOConnection connection;
    DisconnectCallback disconnectCallback;
    boolean disconnected;
    String endpoint;
    ErrorCallback errorCallback;
    Handler handler;
    JSONCallback jsonCallback;
    ReconnectCallback reconnectCallback;
    StringCallback stringCallback;

    private void emitRaw(int type, String message, Acknowledge acknowledge) {
        this.connection.emitRaw(type, this, message, acknowledge);
    }

    public void emit(String name, JSONArray args) {
        emit(name, args, null);
    }

    public void emit(String message) {
        emit(message, (Acknowledge) null);
    }

    public void emit(JSONObject jsonMessage) {
        emit(jsonMessage, null);
    }

    public void emit(String name, JSONArray args, Acknowledge acknowledge) {
        JSONObject event = new JSONObject();
        try {
            event.put("name", name);
            event.put("args", args);
            emitRaw(5, event.toString(), acknowledge);
        } catch (Exception e) {
        }
    }

    public void emit(String message, Acknowledge acknowledge) {
        emitRaw(3, message, acknowledge);
    }

    public void emit(JSONObject jsonMessage, Acknowledge acknowledge) {
        emitRaw(4, jsonMessage.toString(), acknowledge);
    }

    public static void connect(String uri, ConnectCallback callback, Handler handler) {
        connect(new SocketIORequest(uri), callback, handler);
    }

    public static void connect(final SocketIORequest request, final ConnectCallback callback, final Handler handler) {
        final SocketIOConnection connection = new SocketIOConnection(handler, new AsyncHttpClient(), request);
        connection.clients.add(new SocketIOClient(connection, "", new ConnectCallback() {
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                if (ex != null || TextUtils.isEmpty(request.getEndpoint())) {
                    client.handler = handler;
                    if (callback != null) {
                        callback.onConnectCompleted(ex, client);
                        return;
                    }
                    return;
                }
                connection.clients.remove(client);
                client.of(request.getEndpoint(), new ConnectCallback() {
                    public void onConnectCompleted(Exception ex, SocketIOClient client) {
                        if (callback != null) {
                            callback.onConnectCompleted(ex, client);
                        }
                    }
                });
            }
        }));
        connection.reconnect();
    }

    public void setErrorCallback(ErrorCallback callback) {
        this.errorCallback = callback;
    }

    public ErrorCallback getErrorCallback() {
        return this.errorCallback;
    }

    public void setDisconnectCallback(DisconnectCallback callback) {
        this.disconnectCallback = callback;
    }

    public DisconnectCallback getDisconnectCallback() {
        return this.disconnectCallback;
    }

    public void setReconnectCallback(ReconnectCallback callback) {
        this.reconnectCallback = callback;
    }

    public ReconnectCallback getReconnectCallback() {
        return this.reconnectCallback;
    }

    public void setJSONCallback(JSONCallback callback) {
        this.jsonCallback = callback;
    }

    public JSONCallback getJSONCallback() {
        return this.jsonCallback;
    }

    public void setStringCallback(StringCallback callback) {
        this.stringCallback = callback;
    }

    public StringCallback getStringCallback() {
        return this.stringCallback;
    }

    private SocketIOClient(SocketIOConnection connection, String endpoint, ConnectCallback callback) {
        this.endpoint = endpoint;
        this.connection = connection;
        this.connectCallback = callback;
    }

    public boolean isConnected() {
        return this.connected && !this.disconnected && this.connection.isConnected();
    }

    public void disconnect() {
        this.connection.disconnect(this);
        final DisconnectCallback disconnectCallback = this.disconnectCallback;
        if (disconnectCallback != null) {
            this.handler.post(new Runnable() {
                public void run() {
                    disconnectCallback.onDisconnect(null);
                }
            });
        }
    }

    public void of(String endpoint, ConnectCallback connectCallback) {
        this.connection.connect(new SocketIOClient(this.connection, endpoint, connectCallback));
    }

    public WebSocketClient getWebSocket() {
        return this.connection.webSocketClient;
    }
}
