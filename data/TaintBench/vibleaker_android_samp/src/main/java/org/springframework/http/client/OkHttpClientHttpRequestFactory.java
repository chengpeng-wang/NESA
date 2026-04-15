package org.springframework.http.client;

import com.squareup.okhttp.OkHttpClient;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

public class OkHttpClientHttpRequestFactory implements ClientHttpRequestFactory, DisposableBean {
    private final OkHttpClient client;
    private final boolean defaultClient;

    public OkHttpClientHttpRequestFactory() {
        this.client = new OkHttpClient();
        this.defaultClient = true;
    }

    public OkHttpClientHttpRequestFactory(OkHttpClient client) {
        Assert.notNull(client, "'client' must not be null");
        this.client = client;
        this.defaultClient = false;
    }

    public void setReadTimeout(int readTimeout) {
        this.client.setReadTimeout((long) readTimeout, TimeUnit.MILLISECONDS);
    }

    public void setWriteTimeout(int writeTimeout) {
        this.client.setWriteTimeout((long) writeTimeout, TimeUnit.MILLISECONDS);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.client.setConnectTimeout((long) connectTimeout, TimeUnit.MILLISECONDS);
    }

    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) {
        return createRequestInternal(uri, httpMethod);
    }

    private OkHttpClientHttpRequest createRequestInternal(URI uri, HttpMethod httpMethod) {
        return new OkHttpClientHttpRequest(this.client, uri, httpMethod);
    }

    public void destroy() throws Exception {
        if (this.defaultClient) {
            if (this.client.getCache() != null) {
                this.client.getCache().close();
            }
            this.client.getDispatcher().getExecutorService().shutdown();
        }
    }
}
