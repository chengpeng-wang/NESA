package com.koushikdutta.http;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import com.codebutler.android_websockets.WebSocketClient;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.objectweb.asm.Opcodes;

public class AsyncHttpClient {

    public static class SocketIORequest {
        private String mEndpoint;
        private List<BasicNameValuePair> mHeaders;
        private String mUri;

        public SocketIORequest(String uri) {
            this(uri, null);
        }

        public SocketIORequest(String uri, String endpoint) {
            this(uri, endpoint, null);
        }

        public SocketIORequest(String uri, String endpoint, List<BasicNameValuePair> headers) {
            this.mUri = Uri.parse(uri).buildUpon().encodedPath("/socket.io/1/").build().toString();
            this.mEndpoint = endpoint;
            this.mHeaders = headers;
        }

        public String getUri() {
            return this.mUri;
        }

        public String getEndpoint() {
            return this.mEndpoint;
        }

        public List<BasicNameValuePair> getHeaders() {
            return this.mHeaders;
        }
    }

    public interface StringCallback {
        void onCompleted(Exception exception, String str);
    }

    public interface WebSocketConnectCallback {
        void onCompleted(Exception exception, WebSocketClient webSocketClient);
    }

    public void executeString(final SocketIORequest socketIORequest, final StringCallback stringCallback) {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected|varargs */
            public Void doInBackground(Void... params) {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance("android-websockets-2.0");
                HttpPost post = new HttpPost(socketIORequest.getUri());
                addHeadersToRequest(post, socketIORequest.getHeaders());
                try {
                    String responseString = AsyncHttpClient.this.readToEnd(httpClient.execute(post).getEntity().getContent());
                    if (stringCallback != null) {
                        stringCallback.onCompleted(null, responseString);
                    }
                    httpClient.close();
                } catch (IOException e) {
                    if (stringCallback != null) {
                        stringCallback.onCompleted(e, null);
                    }
                    httpClient.close();
                } catch (Throwable th) {
                    httpClient.close();
                }
                return null;
            }

            private void addHeadersToRequest(HttpRequest request, List<BasicNameValuePair> headers) {
                if (headers != null) {
                    for (BasicNameValuePair header : headers) {
                        request.addHeader(header.getName(), header.getValue());
                    }
                }
            }
        }.execute(new Void[0]);
    }

    private byte[] readToEndAsArray(InputStream input) throws IOException {
        DataInputStream dis = new DataInputStream(input);
        byte[] stuff = new byte[Opcodes.ACC_ABSTRACT];
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        while (true) {
            int read = dis.read(stuff);
            if (read == -1) {
                return buff.toByteArray();
            }
            buff.write(stuff, 0, read);
        }
    }

    /* access modifiers changed from: private */
    public String readToEnd(InputStream input) throws IOException {
        return new String(readToEndAsArray(input));
    }
}
