package com.cleanmaster.ui.app.market.transport;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

/* compiled from: MarketHttpConfig */
final class c implements LayeredSocketFactory {
    SSLSocketFactory a = SSLSocketFactory.getSocketFactory();

    c() {
    }

    public final Socket createSocket() {
        return this.a.createSocket();
    }

    public final Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) {
        return this.a.connectSocket(sock, host, port, localAddress, localPort, params);
    }

    public final boolean isSecure(Socket sock) {
        return this.a.isSecure(sock);
    }

    public final Socket createSocket(Socket socket, String host, int port, boolean autoClose) {
        a(socket, host);
        return this.a.createSocket(socket, host, port, autoClose);
    }

    private void a(Socket socket, String str) {
        try {
            Field declaredField = InetAddress.class.getDeclaredField("hostName");
            declaredField.setAccessible(true);
            declaredField.set(socket.getInetAddress(), str);
        } catch (Exception e) {
        }
    }
}
