package org.apache.http.protocol;

public interface HttpRequestHandlerResolver {
    HttpRequestHandler lookup(String str);
}
