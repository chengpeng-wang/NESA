package org.apache.http.protocol;

import java.util.Map;

public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver {
    private final UriPatternMatcher matcher = new UriPatternMatcher();

    public void register(String pattern, HttpRequestHandler handler) {
        if (pattern == null) {
            throw new IllegalArgumentException("URI request pattern may not be null");
        } else if (handler == null) {
            throw new IllegalArgumentException("Request handler may not be null");
        } else {
            this.matcher.register(pattern, handler);
        }
    }

    public void unregister(String pattern) {
        this.matcher.unregister(pattern);
    }

    public void setHandlers(Map map) {
        this.matcher.setObjects(map);
    }

    public HttpRequestHandler lookup(String requestURI) {
        return (HttpRequestHandler) this.matcher.lookup(requestURI);
    }

    /* access modifiers changed from: protected */
    public boolean matchUriRequestPattern(String pattern, String requestUri) {
        return this.matcher.matchUriRequestPattern(pattern, requestUri);
    }
}
