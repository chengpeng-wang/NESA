package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator {
    protected final SchemeRegistry schemeRegistry;

    public DefaultClientConnectionOperator(SchemeRegistry schemes) {
        if (schemes == null) {
            throw new IllegalArgumentException("Scheme registry must not be null.");
        }
        this.schemeRegistry = schemes;
    }

    public OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }

    public void openConnection(OperatedClientConnection conn, HttpHost target, InetAddress local, HttpContext context, HttpParams params) throws IOException {
        if (conn == null) {
            throw new IllegalArgumentException("Connection must not be null.");
        } else if (target == null) {
            throw new IllegalArgumentException("Target host must not be null.");
        } else if (params == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        } else if (conn.isOpen()) {
            throw new IllegalArgumentException("Connection must not be open.");
        } else {
            LayeredSocketFactory layeredsf = null;
            Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
            SocketFactory sf = schm.getSocketFactory();
            if (sf instanceof LayeredSocketFactory) {
                layeredsf = (LayeredSocketFactory) sf;
                sf = PlainSocketFactory.getSocketFactory();
            }
            InetAddress[] addresses = InetAddress.getAllByName(target.getHostName());
            int i = 0;
            while (i < addresses.length) {
                InetAddress address = addresses[i];
                boolean last = i == addresses.length + -1;
                Socket sock = sf.createSocket();
                conn.opening(sock, target);
                try {
                    Socket connsock = sf.connectSocket(sock, address.getHostAddress(), schm.resolvePort(target.getPort()), local, 0, params);
                    if (sock != connsock) {
                        sock = connsock;
                        conn.opening(sock, target);
                    }
                    if (layeredsf != null) {
                        connsock = layeredsf.createSocket(sock, target.getHostName(), schm.resolvePort(target.getPort()), true);
                        if (sock != connsock) {
                            sock = connsock;
                            conn.opening(sock, target);
                        }
                        sf = layeredsf;
                    }
                    prepareSocket(sock, context, params);
                    conn.openCompleted(sf.isSecure(sock), params);
                    return;
                } catch (ConnectException ex) {
                    if (last) {
                        throw new HttpHostConnectException(target, ex);
                    }
                    i++;
                } catch (ConnectTimeoutException ex2) {
                    if (last) {
                        throw ex2;
                    }
                    i++;
                }
            }
        }
    }

    public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
        if (conn == null) {
            throw new IllegalArgumentException("Connection must not be null.");
        } else if (target == null) {
            throw new IllegalArgumentException("Target host must not be null.");
        } else if (params == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        } else if (conn.isOpen()) {
            Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
            if (schm.getSocketFactory() instanceof LayeredSocketFactory) {
                LayeredSocketFactory lsf = (LayeredSocketFactory) schm.getSocketFactory();
                try {
                    Socket sock = lsf.createSocket(conn.getSocket(), target.getHostName(), target.getPort(), true);
                    prepareSocket(sock, context, params);
                    conn.update(sock, target, lsf.isSecure(sock), params);
                    return;
                } catch (ConnectException ex) {
                    throw new HttpHostConnectException(target, ex);
                }
            }
            throw new IllegalArgumentException("Target scheme (" + schm.getName() + ") must have layered socket factory.");
        } else {
            throw new IllegalArgumentException("Connection must be open.");
        }
    }

    /* access modifiers changed from: protected */
    public void prepareSocket(Socket sock, HttpContext context, HttpParams params) throws IOException {
        sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
        sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
        int linger = HttpConnectionParams.getLinger(params);
        if (linger >= 0) {
            sock.setSoLinger(linger > 0, linger);
        }
    }
}
