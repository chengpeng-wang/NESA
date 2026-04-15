package com.mvlove.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientManager {
    public static final int CONNECT_TIMEOUT = 40000;
    public static final int MAX_ROUTE_CONNECTIONS = 400;
    public static final int MAX_TOTAL_CONNECTIONS = 400;
    public static final int READ_TIMEOUT = 60000;
    public static final int WAIT_TIMEOUT = 20000;
    private static ClientConnectionManager connectionManager;
    private static HttpParams params = new BasicHttpParams();

    static {
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        params.setParameter("http.protocol.cookie-policy", "rfc2965");
        ConnManagerParams.setMaxTotalConnections(params, 400);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(400));
        ConnManagerParams.setTimeout(params, 20000);
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        connectionManager = new ThreadSafeClientConnManager(params, schReg);
    }

    public static HttpClient getHttpClient() {
        return new DefaultHttpClient(connectionManager, params);
    }
}
