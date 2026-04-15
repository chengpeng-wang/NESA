package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpDeleteHC4;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpHeadHC4;
import org.apache.http.client.methods.HttpOptionsHC4;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.client.methods.HttpPutHC4;
import org.apache.http.client.methods.HttpTraceHC4;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

public class HttpComponentsClientHttpRequestFactory implements ClientHttpRequestFactory, DisposableBean {
    private boolean bufferRequestBody;
    private int connectTimeout;
    private CloseableHttpClient httpClient;
    private int socketTimeout;

    public HttpComponentsClientHttpRequestFactory() {
        this(HttpClients.createSystem());
    }

    public HttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        this.bufferRequestBody = true;
        Assert.notNull(httpClient, "'httpClient' must not be null");
        Assert.isInstanceOf(CloseableHttpClient.class, httpClient, "'httpClient' is not of type CloseableHttpClient");
        this.httpClient = (CloseableHttpClient) httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        Assert.isInstanceOf(CloseableHttpClient.class, httpClient, "'httpClient' is not of type CloseableHttpClient");
        this.httpClient = (CloseableHttpClient) httpClient;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public void setConnectTimeout(int timeout) {
        Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
        this.connectTimeout = timeout;
        setLegacyConnectionTimeout(getHttpClient(), timeout);
    }

    private void setLegacyConnectionTimeout(HttpClient client, int timeout) {
        if (AbstractHttpClient.class.isInstance(client)) {
            client.getParams().setIntParameter("http.connection.timeout", timeout);
        }
    }

    public void setReadTimeout(int timeout) {
        Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
        this.socketTimeout = timeout;
        setLegacySocketTimeout(getHttpClient(), timeout);
    }

    private void setLegacySocketTimeout(HttpClient client, int timeout) {
        if (AbstractHttpClient.class.isInstance(client)) {
            client.getParams().setIntParameter("http.socket.timeout", timeout);
        }
    }

    public void setBufferRequestBody(boolean bufferRequestBody) {
        this.bufferRequestBody = bufferRequestBody;
    }

    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        CloseableHttpClient client = (CloseableHttpClient) getHttpClient();
        Assert.state(client != null, "Synchronous execution requires an HttpClient to be set");
        HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
        postProcessHttpRequest(httpRequest);
        HttpContext context = createHttpContext(httpMethod, uri);
        if (context == null) {
            context = HttpClientContext.create();
        }
        if (context.getAttribute("http.request-config") == null) {
            RequestConfig config = null;
            if (httpRequest instanceof Configurable) {
                config = ((Configurable) httpRequest).getConfig();
            }
            if (config == null) {
                if (this.socketTimeout > 0 || this.connectTimeout > 0) {
                    config = RequestConfig.custom().setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
                } else {
                    config = RequestConfig.DEFAULT;
                }
            }
            context.setAttribute("http.request-config", config);
        }
        if (this.bufferRequestBody) {
            return new HttpComponentsClientHttpRequest(client, httpRequest, context);
        }
        return new HttpComponentsStreamingClientHttpRequest(client, httpRequest, context);
    }

    /* access modifiers changed from: protected */
    public HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
        switch (httpMethod) {
            case GET:
                return new HttpGetHC4(uri);
            case DELETE:
                return new HttpDeleteHC4(uri);
            case HEAD:
                return new HttpHeadHC4(uri);
            case OPTIONS:
                return new HttpOptionsHC4(uri);
            case POST:
                return new HttpPostHC4(uri);
            case PUT:
                return new HttpPutHC4(uri);
            case TRACE:
                return new HttpTraceHC4(uri);
            case PATCH:
                return new HttpPatch(uri);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
        }
    }

    /* access modifiers changed from: protected */
    public void postProcessHttpRequest(HttpUriRequest request) {
    }

    /* access modifiers changed from: protected */
    public HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
        return null;
    }

    public void destroy() throws Exception {
        this.httpClient.close();
    }
}
