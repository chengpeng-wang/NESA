package com.codebutler.android_websockets;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.codebutler.android_websockets.HybiParser.HappyDataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.java_websocket.WebSocket;

public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    private static TrustManager[] sTrustManagers;
    /* access modifiers changed from: private */
    public boolean mConnected;
    /* access modifiers changed from: private */
    public List<BasicNameValuePair> mExtraHeaders;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    /* access modifiers changed from: private */
    public Listener mListener;
    /* access modifiers changed from: private */
    public HybiParser mParser;
    /* access modifiers changed from: private|final */
    public final Object mSendLock = new Object();
    /* access modifiers changed from: private */
    public Socket mSocket;
    private Thread mThread;
    /* access modifiers changed from: private */
    public URI mURI;

    public interface Listener {
        void onConnect();

        void onDisconnect(int i, String str);

        void onError(Exception exception);

        void onMessage(String str);

        void onMessage(byte[] bArr);
    }

    public static void setTrustManagers(TrustManager[] tm) {
        sTrustManagers = tm;
    }

    public WebSocketClient(URI uri, Listener listener, List<BasicNameValuePair> extraHeaders) {
        this.mURI = uri;
        this.mListener = listener;
        this.mExtraHeaders = extraHeaders;
        this.mConnected = false;
        this.mParser = new HybiParser(this);
        this.mHandlerThread = new HandlerThread("websocket-thread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
    }

    public Listener getListener() {
        return this.mListener;
    }

    public void connect() {
        if (this.mThread == null || !this.mThread.isAlive()) {
            this.mThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        int port = WebSocketClient.this.mURI.getPort() != -1 ? WebSocketClient.this.mURI.getPort() : (WebSocketClient.this.mURI.getScheme().equals("wss") || WebSocketClient.this.mURI.getScheme().equals("https")) ? WebSocket.DEFAULT_WSS_PORT : 80;
                        String path = TextUtils.isEmpty(WebSocketClient.this.mURI.getPath()) ? "/" : WebSocketClient.this.mURI.getPath();
                        if (!TextUtils.isEmpty(WebSocketClient.this.mURI.getQuery())) {
                            path = path + "?" + WebSocketClient.this.mURI.getQuery();
                        }
                        URI origin = new URI(WebSocketClient.this.mURI.getScheme().equals("wss") ? "https" : "http", "//" + WebSocketClient.this.mURI.getHost(), null);
                        SocketFactory factory = (WebSocketClient.this.mURI.getScheme().equals("wss") || WebSocketClient.this.mURI.getScheme().equals("https")) ? WebSocketClient.this.getSSLSocketFactory() : SocketFactory.getDefault();
                        WebSocketClient.this.mSocket = factory.createSocket(WebSocketClient.this.mURI.getHost(), port);
                        WebSocketClient.this.mSocket.setSoTimeout(180000);
                        WebSocketClient.this.mSocket.setKeepAlive(true);
                        PrintWriter out = new PrintWriter(WebSocketClient.this.mSocket.getOutputStream());
                        String secretKey = WebSocketClient.this.createSecret();
                        out.print("GET " + path + " HTTP/1.1\r\n");
                        out.print("Upgrade: websocket\r\n");
                        out.print("Connection: Upgrade\r\n");
                        out.print("Host: " + WebSocketClient.this.mURI.getHost() + "\r\n");
                        out.print("Origin: " + origin.toString() + "\r\n");
                        out.print("Sec-WebSocket-Key: " + secretKey + "\r\n");
                        out.print("Sec-WebSocket-Version: 13\r\n");
                        if (WebSocketClient.this.mExtraHeaders != null) {
                            for (NameValuePair pair : WebSocketClient.this.mExtraHeaders) {
                                out.print(String.format("%s: %s\r\n", new Object[]{pair.getName(), pair.getValue()}));
                            }
                        }
                        out.print("\r\n");
                        out.flush();
                        HappyDataInputStream stream = new HappyDataInputStream(WebSocketClient.this.mSocket.getInputStream());
                        StatusLine statusLine = WebSocketClient.this.parseStatusLine(WebSocketClient.this.readLine(stream));
                        if (statusLine == null) {
                            throw new HttpException("Received no reply from server.");
                        } else if (statusLine.getStatusCode() != 101) {
                            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                        } else {
                            while (true) {
                                String line = WebSocketClient.this.readLine(stream);
                                if (TextUtils.isEmpty(line)) {
                                    WebSocketClient.this.mListener.onConnect();
                                    WebSocketClient.this.mConnected = true;
                                    WebSocketClient.this.mParser.start(stream);
                                    return;
                                }
                                Header header = WebSocketClient.this.parseHeader(line);
                                if (header.getName().equals("Sec-WebSocket-Accept")) {
                                    String expected = WebSocketClient.this.expectedKey(secretKey);
                                    if (expected == null) {
                                        throw new Exception("SHA-1 algorithm not found");
                                    } else if (!expected.equals(header.getValue())) {
                                        throw new Exception("Invalid Sec-WebSocket-Accept, expected: " + expected + ", got: " + header.getValue());
                                    }
                                }
                            }
                        }
                    } catch (EOFException ex) {
                        Log.d(WebSocketClient.TAG, "WebSocket EOF!", ex);
                        WebSocketClient.this.mListener.onDisconnect(0, "EOF");
                        WebSocketClient.this.mConnected = false;
                    } catch (SSLException ex2) {
                        Log.d(WebSocketClient.TAG, "Websocket SSL error!", ex2);
                        WebSocketClient.this.mListener.onDisconnect(0, "SSL");
                        WebSocketClient.this.mConnected = false;
                    } catch (Exception ex3) {
                        WebSocketClient.this.mListener.onError(ex3);
                    }
                }
            });
            this.mThread.start();
        }
    }

    public void disconnect() {
        if (this.mSocket != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (WebSocketClient.this.mSocket != null) {
                        try {
                            WebSocketClient.this.mSocket.close();
                        } catch (IOException ex) {
                            Log.d(WebSocketClient.TAG, "Error while disconnecting", ex);
                            WebSocketClient.this.mListener.onError(ex);
                        }
                        WebSocketClient.this.mSocket = null;
                    }
                    WebSocketClient.this.mConnected = false;
                }
            });
        }
    }

    public void send(String data) {
        sendFrame(this.mParser.frame(data));
    }

    public void send(byte[] data) {
        sendFrame(this.mParser.frame(data));
    }

    public boolean isConnected() {
        return this.mConnected;
    }

    /* access modifiers changed from: private */
    public StatusLine parseStatusLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }
        return BasicLineParser.parseStatusLine(line, new BasicLineParser());
    }

    /* access modifiers changed from: private */
    public Header parseHeader(String line) {
        return BasicLineParser.parseHeader(line, new BasicLineParser());
    }

    /* access modifiers changed from: private */
    public String readLine(HappyDataInputStream reader) throws IOException {
        int readChar = reader.read();
        if (readChar == -1) {
            return null;
        }
        StringBuilder string = new StringBuilder("");
        while (readChar != 10) {
            if (readChar != 13) {
                string.append((char) readChar);
            }
            readChar = reader.read();
            if (readChar == -1) {
                return null;
            }
        }
        return string.toString();
    }

    /* access modifiers changed from: private */
    public String expectedKey(String secret) {
        try {
            String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            return Base64.encodeToString(MessageDigest.getInstance("SHA-1").digest((secret + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()), 0).trim();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public String createSecret() {
        byte[] nonce = new byte[16];
        for (int i = 0; i < 16; i++) {
            nonce[i] = (byte) ((int) (Math.random() * 256.0d));
        }
        return Base64.encodeToString(nonce, 0).trim();
    }

    /* access modifiers changed from: 0000 */
    public void sendFrame(final byte[] frame) {
        this.mHandler.post(new Runnable() {
            public void run() {
                try {
                    synchronized (WebSocketClient.this.mSendLock) {
                        OutputStream outputStream = WebSocketClient.this.mSocket.getOutputStream();
                        outputStream.write(frame);
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    WebSocketClient.this.mListener.onError(e);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, sTrustManagers, null);
        return context.getSocketFactory();
    }
}
