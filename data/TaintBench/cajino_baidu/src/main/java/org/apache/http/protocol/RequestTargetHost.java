package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;

public class RequestTargetHost implements HttpRequestInterceptor {
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            if ((!request.getRequestLine().getMethod().equalsIgnoreCase("CONNECT") || !ver.lessEquals(HttpVersion.HTTP_1_0)) && !request.containsHeader("Host")) {
                HttpHost targethost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (targethost == null) {
                    HttpConnection conn = (HttpConnection) context.getAttribute(ExecutionContext.HTTP_CONNECTION);
                    if (conn instanceof HttpInetConnection) {
                        InetAddress address = ((HttpInetConnection) conn).getRemoteAddress();
                        int port = ((HttpInetConnection) conn).getRemotePort();
                        if (address != null) {
                            targethost = new HttpHost(address.getHostName(), port);
                        }
                    }
                    if (targethost == null) {
                        if (!ver.lessEquals(HttpVersion.HTTP_1_0)) {
                            throw new ProtocolException("Target host missing");
                        }
                        return;
                    }
                }
                request.addHeader("Host", targethost.toHostString());
            }
        }
    }
}
