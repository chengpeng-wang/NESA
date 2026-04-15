package org.springframework.http.client;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpMethod;

public class InterceptingClientHttpRequestFactory extends AbstractClientHttpRequestFactoryWrapper {
    private final List<ClientHttpRequestInterceptor> interceptors;

    public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors) {
        super(requestFactory);
        if (interceptors == null) {
            interceptors = Collections.emptyList();
        }
        this.interceptors = interceptors;
    }

    /* access modifiers changed from: protected */
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
        return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
    }
}
