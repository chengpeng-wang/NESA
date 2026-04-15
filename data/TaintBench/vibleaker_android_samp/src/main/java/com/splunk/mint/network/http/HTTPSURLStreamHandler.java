package com.splunk.mint.network.http;

import com.splunk.mint.network.MonitorRegistry;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class HTTPSURLStreamHandler extends URLStreamHandlerBase {
    private static final int PORT = 443;
    private static final String PROTOCOL = "https";
    private static final String[] SYSTEM_CLASSES = new String[]{"libcore.net.http.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpsURLConnectionImpl", "org.apache.harmony.luni.internal.net.www.protocol.http.HttpsURLConnection"};
    private final MonitorRegistry registry;

    public HTTPSURLStreamHandler(MonitorRegistry registry) throws ClassNotFoundException {
        super(SYSTEM_CLASSES);
        this.registry = registry;
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        return new MonitorableHttpsURLConnection(this.registry, super.openConnection(url));
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new MonitorableHttpsURLConnection(this.registry, super.openConnection(url, proxy));
    }

    public int getDefaultPort() {
        return PORT;
    }

    public String getProtocol() {
        return PROTOCOL;
    }
}
