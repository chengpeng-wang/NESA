package com.splunk.mint.network.http;

import com.splunk.mint.network.MonitorRegistry;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public final class HTTPURLStreamHandler extends URLStreamHandlerBase {
    private static final int PORT = 80;
    private static final String PROTOCOL = "http";
    private static final String[] SYSTEM_CLASSES = new String[]{"libcore.net.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnection"};
    private final MonitorRegistry registry;

    public HTTPURLStreamHandler(MonitorRegistry registry) throws ClassNotFoundException {
        super(SYSTEM_CLASSES);
        this.registry = registry;
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        return new MonitorableHttpURLConnection(this.registry, super.openConnection(url));
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new MonitorableHttpURLConnection(this.registry, super.openConnection(url, proxy));
    }

    public int getDefaultPort() {
        return 80;
    }

    public String getProtocol() {
        return PROTOCOL;
    }
}
