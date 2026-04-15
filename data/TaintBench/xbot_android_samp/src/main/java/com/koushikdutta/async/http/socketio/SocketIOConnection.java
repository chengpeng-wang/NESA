package com.koushikdutta.async.http.socketio;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import com.codebutler.android_websockets.WebSocketClient;
import com.codebutler.android_websockets.WebSocketClient.Listener;
import com.koushikdutta.http.AsyncHttpClient;
import com.koushikdutta.http.AsyncHttpClient.SocketIORequest;
import com.koushikdutta.http.AsyncHttpClient.StringCallback;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.java_websocket.framing.CloseFrame;
import org.json.JSONArray;
import org.json.JSONObject;

class SocketIOConnection {
    int ackCount;
    Hashtable<String, Acknowledge> acknowledges = new Hashtable();
    ArrayList<SocketIOClient> clients = new ArrayList();
    int heartbeat;
    AsyncHttpClient httpClient;
    /* access modifiers changed from: private */
    public Handler mHandler;
    long reconnectDelay = 1000;
    SocketIORequest request;
    WebSocketClient webSocketClient;

    private interface SelectCallback {
        void onSelect(SocketIOClient socketIOClient);
    }

    public SocketIOConnection(Handler handler, AsyncHttpClient httpClient, SocketIORequest request) {
        this.mHandler = handler;
        this.httpClient = httpClient;
        this.request = request;
    }

    public boolean isConnected() {
        return this.webSocketClient != null && this.webSocketClient.isConnected();
    }

    public void emitRaw(int type, SocketIOClient client, String message, Acknowledge acknowledge) {
        String ack = "";
        if (acknowledge != null) {
            StringBuilder append = new StringBuilder().append("");
            int i = this.ackCount;
            this.ackCount = i + 1;
            String id = append.append(i).toString();
            ack = id + "+";
            this.acknowledges.put(id, acknowledge);
        }
        this.webSocketClient.send(String.format("%d:%s:%s:%s", new Object[]{Integer.valueOf(type), ack, client.endpoint, message}));
    }

    public void connect(SocketIOClient client) {
        this.clients.add(client);
        this.webSocketClient.send(String.format("1::%s", new Object[]{client.endpoint}));
    }

    public void disconnect(SocketIOClient client) {
        this.clients.remove(client);
        boolean needsEndpointDisconnect = true;
        Iterator it = this.clients.iterator();
        while (it.hasNext()) {
            if (!TextUtils.equals(((SocketIOClient) it.next()).endpoint, client.endpoint)) {
                if (TextUtils.isEmpty(client.endpoint)) {
                }
            }
            needsEndpointDisconnect = false;
        }
        if (needsEndpointDisconnect) {
            this.webSocketClient.send(String.format("0::%s", new Object[]{client.endpoint}));
        }
        if (this.clients.size() <= 0) {
            this.webSocketClient.disconnect();
            this.webSocketClient = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void reconnect() {
        if (!isConnected()) {
            this.httpClient.executeString(this.request, new StringCallback() {
                public void onCompleted(Exception e, String result) {
                    if (e != null) {
                        SocketIOConnection.this.reportDisconnect(e);
                        return;
                    }
                    try {
                        String[] parts = result.split(":");
                        String session = parts[0];
                        if ("".equals(parts[1])) {
                            SocketIOConnection.this.heartbeat = 0;
                        } else {
                            SocketIOConnection.this.heartbeat = (Integer.parseInt(parts[1]) / 2) * CloseFrame.NORMAL;
                        }
                        if (new HashSet(Arrays.asList(parts[3].split(","))).contains("websocket")) {
                            String sessionUrl = Uri.parse(SocketIOConnection.this.request.getUri()).buildUpon().appendPath("websocket").appendPath(session).build().toString();
                            SocketIOConnection.this.webSocketClient = new WebSocketClient(URI.create(sessionUrl), new Listener() {
                                public void onMessage(byte[] data) {
                                }

                                public void onMessage(String message) {
                                    try {
                                        String[] parts = message.split(":", 4);
                                        switch (Integer.parseInt(parts[0])) {
                                            case 0:
                                                SocketIOConnection.this.webSocketClient.disconnect();
                                                SocketIOConnection.this.reportDisconnect(null);
                                                return;
                                            case 1:
                                                SocketIOConnection.this.reportConnect(parts[2]);
                                                return;
                                            case 2:
                                                SocketIOConnection.this.webSocketClient.send("2::");
                                                return;
                                            case 3:
                                                SocketIOConnection.this.reportString(parts[2], parts[3], SocketIOConnection.this.acknowledge(parts[1]));
                                                return;
                                            case 4:
                                                SocketIOConnection.this.reportJson(parts[2], new JSONObject(parts[3]), SocketIOConnection.this.acknowledge(parts[1]));
                                                return;
                                            case 5:
                                                JSONObject data = new JSONObject(parts[3]);
                                                SocketIOConnection.this.reportEvent(parts[2], data.getString("name"), data.optJSONArray("args"), SocketIOConnection.this.acknowledge(parts[1]));
                                                return;
                                            case 6:
                                                String[] ackParts = parts[3].split("\\+", 2);
                                                Acknowledge ack = (Acknowledge) SocketIOConnection.this.acknowledges.remove(ackParts[0]);
                                                if (ack != null) {
                                                    JSONArray arguments = null;
                                                    if (ackParts.length == 2) {
                                                        arguments = new JSONArray(ackParts[1]);
                                                    }
                                                    ack.acknowledge(arguments);
                                                    return;
                                                }
                                                return;
                                            case 7:
                                                SocketIOConnection.this.reportError(parts[2], parts[3]);
                                                return;
                                            case 8:
                                                return;
                                            default:
                                                throw new Exception("unknown code");
                                        }
                                    } catch (Exception ex) {
                                        SocketIOConnection.this.webSocketClient.disconnect();
                                        SocketIOConnection.this.webSocketClient = null;
                                        SocketIOConnection.this.reportDisconnect(ex);
                                    }
                                    SocketIOConnection.this.webSocketClient.disconnect();
                                    SocketIOConnection.this.webSocketClient = null;
                                    SocketIOConnection.this.reportDisconnect(ex);
                                }

                                public void onError(Exception error) {
                                    SocketIOConnection.this.reportDisconnect(error);
                                }

                                public void onDisconnect(int code, String reason) {
                                    SocketIOConnection.this.reportDisconnect(new IOException(String.format("Disconnected code %d for reason %s", new Object[]{Integer.valueOf(code), reason})));
                                }

                                public void onConnect() {
                                    SocketIOConnection.this.reconnectDelay = 1000;
                                    SocketIOConnection.this.setupHeartbeat();
                                }
                            }, null);
                            SocketIOConnection.this.webSocketClient.connect();
                            return;
                        }
                        throw new Exception("websocket not supported");
                    } catch (Exception ex) {
                        SocketIOConnection.this.reportDisconnect(ex);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void setupHeartbeat() {
        final WebSocketClient ws = this.webSocketClient;
        new Runnable() {
            public void run() {
                if (SocketIOConnection.this.heartbeat > 0 && ws == SocketIOConnection.this.webSocketClient && ws != null && ws.isConnected()) {
                    SocketIOConnection.this.webSocketClient.send("2:::");
                    SocketIOConnection.this.mHandler.postDelayed(this, (long) SocketIOConnection.this.heartbeat);
                }
            }
        }.run();
    }

    private void select(String endpoint, SelectCallback callback) {
        Iterator it = this.clients.iterator();
        while (it.hasNext()) {
            SocketIOClient client = (SocketIOClient) it.next();
            if (endpoint == null || TextUtils.equals(client.endpoint, endpoint)) {
                callback.onSelect(client);
            }
        }
    }

    private void delayReconnect() {
        if (this.webSocketClient == null && this.clients.size() != 0) {
            boolean disconnected = false;
            Iterator it = this.clients.iterator();
            while (it.hasNext()) {
                if (((SocketIOClient) it.next()).disconnected) {
                    disconnected = true;
                    break;
                }
            }
            if (disconnected) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SocketIOConnection.this.reconnect();
                    }
                }, this.reconnectDelay);
                this.reconnectDelay *= 2;
            }
        }
    }

    /* access modifiers changed from: private */
    public void reportDisconnect(final Exception ex) {
        select(null, new SelectCallback() {
            public void onSelect(final SocketIOClient client) {
                if (client.connected) {
                    client.disconnected = true;
                    final DisconnectCallback closed = client.getDisconnectCallback();
                    if (closed != null) {
                        SocketIOConnection.this.mHandler.post(new Runnable() {
                            public void run() {
                                closed.onDisconnect(ex);
                            }
                        });
                        return;
                    }
                    return;
                }
                final ConnectCallback callback = client.connectCallback;
                if (callback != null) {
                    SocketIOConnection.this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onConnectCompleted(ex, client);
                        }
                    });
                }
            }
        });
        delayReconnect();
    }

    /* access modifiers changed from: private */
    public void reportConnect(String endpoint) {
        select(endpoint, new SelectCallback() {
            public void onSelect(SocketIOClient client) {
                if (!client.isConnected()) {
                    if (!client.connected) {
                        client.connected = true;
                        ConnectCallback callback = client.connectCallback;
                        if (callback != null) {
                            callback.onConnectCompleted(null, client);
                        }
                    } else if (client.disconnected) {
                        client.disconnected = false;
                        ReconnectCallback callback2 = client.reconnectCallback;
                        if (callback2 != null) {
                            callback2.onReconnect();
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void reportJson(String endpoint, final JSONObject jsonMessage, final Acknowledge acknowledge) {
        select(endpoint, new SelectCallback() {
            public void onSelect(SocketIOClient client) {
                final JSONCallback callback = client.jsonCallback;
                if (callback != null) {
                    SocketIOConnection.this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onJSON(jsonMessage, acknowledge);
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void reportString(String endpoint, final String string, final Acknowledge acknowledge) {
        select(endpoint, new SelectCallback() {
            public void onSelect(SocketIOClient client) {
                final StringCallback callback = client.stringCallback;
                if (callback != null) {
                    SocketIOConnection.this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onString(string, acknowledge);
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void reportEvent(String endpoint, final String event, final JSONArray arguments, final Acknowledge acknowledge) {
        select(endpoint, new SelectCallback() {
            public void onSelect(final SocketIOClient client) {
                SocketIOConnection.this.mHandler.post(new Runnable() {
                    public void run() {
                        client.onEvent(event, arguments, acknowledge);
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void reportError(String endpoint, final String error) {
        select(endpoint, new SelectCallback() {
            public void onSelect(SocketIOClient client) {
                final ErrorCallback callback = client.errorCallback;
                if (callback != null) {
                    SocketIOConnection.this.mHandler.post(new Runnable() {
                        public void run() {
                            callback.onError(error);
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public Acknowledge acknowledge(final String messageId) {
        if (TextUtils.isEmpty(messageId)) {
            return null;
        }
        return new Acknowledge() {
            public void acknowledge(JSONArray arguments) {
                String data = "";
                if (arguments != null) {
                    data = data + "+" + arguments.toString();
                }
                SocketIOConnection.this.webSocketClient.send(String.format("6:::%s%s", new Object[]{messageId, data}));
            }
        };
    }
}
