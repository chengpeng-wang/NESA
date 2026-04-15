package org.springframework.http.client.support;

import android.os.Build.VERSION;
import android.util.Log;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAndroidClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class HttpAccessor {
    private static final String TAG = HttpAccessor.class.getSimpleName();
    private static final boolean httpClient43Present = ClassUtils.isPresent("org.apache.http.impl.client.CloseableHttpClient", HttpAccessor.class.getClassLoader());
    private static final boolean okHttpPresent = ClassUtils.isPresent("com.squareup.okhttp.OkHttpClient", HttpAccessor.class.getClassLoader());
    private ClientHttpRequestFactory requestFactory;

    protected HttpAccessor() {
        if (httpClient43Present) {
            this.requestFactory = new HttpComponentsClientHttpRequestFactory();
        } else if (okHttpPresent) {
            this.requestFactory = new OkHttpClientHttpRequestFactory();
        } else if (VERSION.SDK_INT >= 9) {
            this.requestFactory = new SimpleClientHttpRequestFactory();
        } else {
            this.requestFactory = new HttpComponentsAndroidClientHttpRequestFactory();
        }
    }

    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "'requestFactory' must not be null");
        this.requestFactory = requestFactory;
    }

    public ClientHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    /* access modifiers changed from: protected */
    public ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = getRequestFactory().createRequest(url, method);
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Created " + method.name() + " request for \"" + url + "\"");
        }
        return request;
    }
}
