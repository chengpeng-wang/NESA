package com.google.android.apps.analytics;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.BasicHttpParams;

class PipelinedRequester {
    Callbacks callbacks;
    boolean canPipeline;
    DefaultHttpClientConnection connection;
    HttpHost host;
    int lastStatusCode;
    SocketFactory socketFactory;

    interface Callbacks {
        void pipelineModeChanged(boolean z);

        void requestSent();

        void serverError(int i);
    }

    public PipelinedRequester(HttpHost httpHost) {
        this(httpHost, new PlainSocketFactory());
    }

    public PipelinedRequester(HttpHost httpHost, SocketFactory socketFactory) {
        this.connection = new DefaultHttpClientConnection();
        this.canPipeline = true;
        this.host = httpHost;
        this.socketFactory = socketFactory;
    }

    private void closeConnection() {
        if (this.connection != null && this.connection.isOpen()) {
            try {
                this.connection.close();
            } catch (IOException e) {
            }
        }
    }

    private void maybeOpenConnection() throws IOException {
        if (this.connection == null || !this.connection.isOpen()) {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            this.connection.bind(this.socketFactory.connectSocket(this.socketFactory.createSocket(), this.host.getHostName(), this.host.getPort(), null, 0, basicHttpParams), basicHttpParams);
        }
    }

    public void addRequest(HttpRequest httpRequest) throws HttpException, IOException {
        maybeOpenConnection();
        this.connection.sendRequestHeader(httpRequest);
    }

    public void finishedCurrentRequests() {
        closeConnection();
    }

    public void installCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void sendRequests() throws IOException, HttpException {
        this.connection.flush();
        HttpConnectionMetrics metrics = this.connection.getMetrics();
        while (metrics.getResponseCount() < metrics.getRequestCount()) {
            HttpResponse receiveResponseHeader = this.connection.receiveResponseHeader();
            if (!receiveResponseHeader.getStatusLine().getProtocolVersion().greaterEquals(HttpVersion.HTTP_1_1)) {
                this.callbacks.pipelineModeChanged(false);
                this.canPipeline = false;
            }
            Header[] headers = receiveResponseHeader.getHeaders("Connection");
            if (headers != null) {
                for (Header value : headers) {
                    if ("close".equalsIgnoreCase(value.getValue())) {
                        this.callbacks.pipelineModeChanged(false);
                        this.canPipeline = false;
                    }
                }
            }
            this.lastStatusCode = receiveResponseHeader.getStatusLine().getStatusCode();
            if (this.lastStatusCode != 200) {
                this.callbacks.serverError(this.lastStatusCode);
                closeConnection();
                return;
            }
            this.connection.receiveResponseEntity(receiveResponseHeader);
            receiveResponseHeader.getEntity().consumeContent();
            this.callbacks.requestSent();
            if (!this.canPipeline) {
                closeConnection();
                return;
            }
        }
    }
}
