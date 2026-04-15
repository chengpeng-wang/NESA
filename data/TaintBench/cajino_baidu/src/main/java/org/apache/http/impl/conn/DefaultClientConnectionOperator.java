package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator {
    private final Log log = LogFactory.getLog(getClass());
    protected final SchemeRegistry schemeRegistry;

    public DefaultClientConnectionOperator(SchemeRegistry schemes) {
        if (schemes == null) {
            throw new IllegalArgumentException("Scheme registry amy not be null");
        }
        this.schemeRegistry = schemes;
    }

    public OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0105 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00dd  */
    public void openConnection(org.apache.http.conn.OperatedClientConnection r19, org.apache.http.HttpHost r20, java.net.InetAddress r21, org.apache.http.protocol.HttpContext r22, org.apache.http.params.HttpParams r23) throws java.io.IOException {
        /*
        r18 = this;
        if (r19 != 0) goto L_0x000a;
    L_0x0002:
        r15 = new java.lang.IllegalArgumentException;
        r16 = "Connection may not be null";
        r15.<init>(r16);
        throw r15;
    L_0x000a:
        if (r20 != 0) goto L_0x0014;
    L_0x000c:
        r15 = new java.lang.IllegalArgumentException;
        r16 = "Target host may not be null";
        r15.<init>(r16);
        throw r15;
    L_0x0014:
        if (r23 != 0) goto L_0x001e;
    L_0x0016:
        r15 = new java.lang.IllegalArgumentException;
        r16 = "Parameters may not be null";
        r15.<init>(r16);
        throw r15;
    L_0x001e:
        r15 = r19.isOpen();
        if (r15 == 0) goto L_0x002c;
    L_0x0024:
        r15 = new java.lang.IllegalStateException;
        r16 = "Connection must not be open";
        r15.<init>(r16);
        throw r15;
    L_0x002c:
        r0 = r18;
        r15 = r0.schemeRegistry;
        r16 = r20.getSchemeName();
        r12 = r15.getScheme(r16);
        r13 = r12.getSchemeSocketFactory();
        r15 = r20.getHostName();
        r0 = r18;
        r4 = r0.resolveHostname(r15);
        r15 = r20.getPort();
        r10 = r12.resolvePort(r15);
        r7 = 0;
    L_0x004f:
        r15 = r4.length;
        if (r7 >= r15) goto L_0x00c1;
    L_0x0052:
        r3 = r4[r7];
        r15 = r4.length;
        r15 = r15 + -1;
        if (r7 != r15) goto L_0x00c2;
    L_0x0059:
        r8 = 1;
    L_0x005a:
        r0 = r23;
        r14 = r13.createSocket(r0);
        r0 = r19;
        r1 = r20;
        r0.opening(r14, r1);
        r11 = new java.net.InetSocketAddress;
        r11.<init>(r3, r10);
        r9 = 0;
        if (r21 == 0) goto L_0x0077;
    L_0x006f:
        r9 = new java.net.InetSocketAddress;
        r15 = 0;
        r0 = r21;
        r9.<init>(r0, r15);
    L_0x0077:
        r0 = r18;
        r15 = r0.log;
        r15 = r15.isDebugEnabled();
        if (r15 == 0) goto L_0x009d;
    L_0x0081:
        r0 = r18;
        r15 = r0.log;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "Connecting to ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r11);
        r16 = r16.toString();
        r15.debug(r16);
    L_0x009d:
        r0 = r23;
        r5 = r13.connectSocket(r14, r11, r9, r0);	 Catch:{ ConnectException -> 0x00c4, ConnectTimeoutException -> 0x00cf }
        if (r14 == r5) goto L_0x00ad;
    L_0x00a5:
        r14 = r5;
        r0 = r19;
        r1 = r20;
        r0.opening(r14, r1);	 Catch:{ ConnectException -> 0x00c4, ConnectTimeoutException -> 0x00cf }
    L_0x00ad:
        r0 = r18;
        r1 = r22;
        r2 = r23;
        r0.prepareSocket(r14, r1, r2);	 Catch:{ ConnectException -> 0x00c4, ConnectTimeoutException -> 0x00cf }
        r15 = r13.isSecure(r14);	 Catch:{ ConnectException -> 0x00c4, ConnectTimeoutException -> 0x00cf }
        r0 = r19;
        r1 = r23;
        r0.openCompleted(r15, r1);	 Catch:{ ConnectException -> 0x00c4, ConnectTimeoutException -> 0x00cf }
    L_0x00c1:
        return;
    L_0x00c2:
        r8 = 0;
        goto L_0x005a;
    L_0x00c4:
        r6 = move-exception;
        if (r8 == 0) goto L_0x00d3;
    L_0x00c7:
        r15 = new org.apache.http.conn.HttpHostConnectException;
        r0 = r20;
        r15.m907init(r0, r6);
        throw r15;
    L_0x00cf:
        r6 = move-exception;
        if (r8 == 0) goto L_0x00d3;
    L_0x00d2:
        throw r6;
    L_0x00d3:
        r0 = r18;
        r15 = r0.log;
        r15 = r15.isDebugEnabled();
        if (r15 == 0) goto L_0x0105;
    L_0x00dd:
        r0 = r18;
        r15 = r0.log;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "Connect to ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r11);
        r17 = " timed out. ";
        r16 = r16.append(r17);
        r17 = "Connection will be retried using another IP address";
        r16 = r16.append(r17);
        r16 = r16.toString();
        r15.debug(r16);
    L_0x0105:
        r7 = r7 + 1;
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.DefaultClientConnectionOperator.openConnection(org.apache.http.conn.OperatedClientConnection, org.apache.http.HttpHost, java.net.InetAddress, org.apache.http.protocol.HttpContext, org.apache.http.params.HttpParams):void");
    }

    public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
        if (conn == null) {
            throw new IllegalArgumentException("Connection may not be null");
        } else if (target == null) {
            throw new IllegalArgumentException("Target host may not be null");
        } else if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        } else if (conn.isOpen()) {
            Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
            if (schm.getSchemeSocketFactory() instanceof LayeredSchemeSocketFactory) {
                LayeredSchemeSocketFactory lsf = (LayeredSchemeSocketFactory) schm.getSchemeSocketFactory();
                try {
                    Socket sock = lsf.createLayeredSocket(conn.getSocket(), target.getHostName(), target.getPort(), true);
                    prepareSocket(sock, context, params);
                    conn.update(sock, target, lsf.isSecure(sock), params);
                    return;
                } catch (ConnectException ex) {
                    throw new HttpHostConnectException(target, ex);
                }
            }
            throw new IllegalArgumentException("Target scheme (" + schm.getName() + ") must have layered socket factory.");
        } else {
            throw new IllegalStateException("Connection must be open");
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

    /* access modifiers changed from: protected */
    public InetAddress[] resolveHostname(String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }
}
