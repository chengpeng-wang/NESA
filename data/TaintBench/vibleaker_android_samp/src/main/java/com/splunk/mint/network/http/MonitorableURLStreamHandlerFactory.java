package com.splunk.mint.network.http;

import com.splunk.mint.network.MonitorRegistry;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

public class MonitorableURLStreamHandlerFactory implements URLStreamHandlerFactory {
    private Map<String, URLStreamHandler> handlers = new HashMap();

    public MonitorableURLStreamHandlerFactory(MonitorRegistry registry) throws ClassNotFoundException {
        URLStreamHandlerBase h = new HTTPURLStreamHandler(registry);
        this.handlers.put(h.getProtocol(), h);
        h = new HTTPSURLStreamHandler(registry);
        this.handlers.put(h.getProtocol(), h);
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        return (URLStreamHandler) this.handlers.get(protocol);
    }
}
