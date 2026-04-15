package com.sun.mail.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SocketFetcher {
    private SocketFetcher() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x01bb  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x010e  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0120 A:{SYNTHETIC, Splitter:B:38:0x0120} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x015f  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x015c  */
    public static java.net.Socket getSocket(java.lang.String r17, int r18, java.util.Properties r19, java.lang.String r20, boolean r21) throws java.io.IOException {
        /*
        if (r20 != 0) goto L_0x0004;
    L_0x0002:
        r20 = "socket";
    L_0x0004:
        if (r19 != 0) goto L_0x000b;
    L_0x0006:
        r19 = new java.util.Properties;
        r19.<init>();
    L_0x000b:
        r3 = new java.lang.StringBuilder;
        r4 = java.lang.String.valueOf(r20);
        r3.<init>(r4);
        r4 = ".connectiontimeout";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r4 = 0;
        r0 = r19;
        r1 = r3;
        r2 = r4;
        r3 = r0.getProperty(r1, r2);
        r7 = -1;
        if (r3 == 0) goto L_0x002e;
    L_0x002a:
        r7 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x01af }
    L_0x002e:
        r12 = 0;
        r3 = new java.lang.StringBuilder;
        r4 = java.lang.String.valueOf(r20);
        r3.<init>(r4);
        r4 = ".timeout";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r4 = 0;
        r0 = r19;
        r1 = r3;
        r2 = r4;
        r16 = r0.getProperty(r1, r2);
        r3 = new java.lang.StringBuilder;
        r4 = java.lang.String.valueOf(r20);
        r3.<init>(r4);
        r4 = ".localaddress";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r4 = 0;
        r0 = r19;
        r1 = r3;
        r2 = r4;
        r4 = r0.getProperty(r1, r2);
        r3 = 0;
        if (r4 == 0) goto L_0x006e;
    L_0x006a:
        r3 = java.net.InetAddress.getByName(r4);
    L_0x006e:
        r4 = new java.lang.StringBuilder;
        r5 = java.lang.String.valueOf(r20);
        r4.<init>(r5);
        r5 = ".localport";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r5 = 0;
        r0 = r19;
        r1 = r4;
        r2 = r5;
        r5 = r0.getProperty(r1, r2);
        r4 = 0;
        if (r5 == 0) goto L_0x0091;
    L_0x008d:
        r4 = java.lang.Integer.parseInt(r5);	 Catch:{ NumberFormatException -> 0x01b2 }
    L_0x0091:
        r5 = 0;
        r5 = new java.lang.StringBuilder;
        r6 = java.lang.String.valueOf(r20);
        r5.<init>(r6);
        r6 = ".socketFactory.fallback";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r6 = 0;
        r0 = r19;
        r1 = r5;
        r2 = r6;
        r5 = r0.getProperty(r1, r2);
        if (r5 == 0) goto L_0x0133;
    L_0x00b0:
        r6 = "false";
        r5 = r5.equalsIgnoreCase(r6);
        if (r5 == 0) goto L_0x0133;
    L_0x00b8:
        r5 = 0;
        r10 = r5;
    L_0x00ba:
        r5 = new java.lang.StringBuilder;
        r6 = java.lang.String.valueOf(r20);
        r5.<init>(r6);
        r6 = ".socketFactory.class";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r6 = 0;
        r0 = r19;
        r1 = r5;
        r2 = r6;
        r11 = r0.getProperty(r1, r2);
        r6 = -1;
        r8 = getSocketFactory(r11);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        if (r8 == 0) goto L_0x01c2;
    L_0x00dd:
        r5 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        r9 = java.lang.String.valueOf(r20);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        r5.<init>(r9);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        r9 = ".socketFactory.port";
        r5 = r5.append(r9);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        r5 = r5.toString();	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        r9 = 0;
        r0 = r19;
        r1 = r5;
        r2 = r9;
        r5 = r0.getProperty(r1, r2);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
        if (r5 == 0) goto L_0x00ff;
    L_0x00fb:
        r6 = java.lang.Integer.parseInt(r5);	 Catch:{ NumberFormatException -> 0x01b5 }
    L_0x00ff:
        r5 = -1;
        if (r6 != r5) goto L_0x0104;
    L_0x0102:
        r6 = r18;
    L_0x0104:
        r5 = r17;
        r9 = r21;
        r5 = createSocket(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ SocketTimeoutException -> 0x0136, Exception -> 0x0138 }
    L_0x010c:
        if (r5 != 0) goto L_0x01bb;
    L_0x010e:
        r14 = 0;
        r9 = r3;
        r10 = r4;
        r11 = r17;
        r12 = r18;
        r13 = r7;
        r15 = r21;
        r17 = createSocket(r9, r10, r11, r12, r13, r14, r15);
    L_0x011c:
        r18 = -1;
        if (r16 == 0) goto L_0x0124;
    L_0x0120:
        r18 = java.lang.Integer.parseInt(r16);	 Catch:{ NumberFormatException -> 0x01b8 }
    L_0x0124:
        if (r18 < 0) goto L_0x0129;
    L_0x0126:
        r17.setSoTimeout(r18);
    L_0x0129:
        r0 = r17;
        r1 = r19;
        r2 = r20;
        configureSSLSocket(r0, r1, r2);
        return r17;
    L_0x0133:
        r5 = 1;
        r10 = r5;
        goto L_0x00ba;
    L_0x0136:
        r17 = move-exception;
        throw r17;
    L_0x0138:
        r5 = move-exception;
        if (r10 != 0) goto L_0x01c2;
    L_0x013b:
        r0 = r5;
        r0 = r0 instanceof java.lang.reflect.InvocationTargetException;
        r18 = r0;
        if (r18 == 0) goto L_0x01bf;
    L_0x0142:
        r0 = r5;
        r0 = (java.lang.reflect.InvocationTargetException) r0;
        r7 = r0;
        r18 = r7.getTargetException();
        r0 = r18;
        r0 = r0 instanceof java.lang.Exception;
        r19 = r0;
        if (r19 == 0) goto L_0x01bf;
    L_0x0152:
        r18 = (java.lang.Exception) r18;
    L_0x0154:
        r0 = r18;
        r0 = r0 instanceof java.io.IOException;
        r19 = r0;
        if (r19 == 0) goto L_0x015f;
    L_0x015c:
        r18 = (java.io.IOException) r18;
        throw r18;
    L_0x015f:
        r19 = new java.io.IOException;
        r20 = new java.lang.StringBuilder;
        r21 = "Couldn't connect using \"";
        r20.<init>(r21);
        r0 = r20;
        r1 = r11;
        r20 = r0.append(r1);
        r21 = "\" socket factory to host, port: ";
        r20 = r20.append(r21);
        r0 = r20;
        r1 = r17;
        r17 = r0.append(r1);
        r20 = ", ";
        r0 = r17;
        r1 = r20;
        r17 = r0.append(r1);
        r0 = r17;
        r1 = r6;
        r17 = r0.append(r1);
        r20 = "; Exception: ";
        r0 = r17;
        r1 = r20;
        r17 = r0.append(r1);
        r17 = r17.append(r18);
        r17 = r17.toString();
        r0 = r19;
        r1 = r17;
        r0.<init>(r1);
        r0 = r19;
        r1 = r18;
        r0.initCause(r1);
        throw r19;
    L_0x01af:
        r3 = move-exception;
        goto L_0x002e;
    L_0x01b2:
        r5 = move-exception;
        goto L_0x0091;
    L_0x01b5:
        r5 = move-exception;
        goto L_0x00ff;
    L_0x01b8:
        r21 = move-exception;
        goto L_0x0124;
    L_0x01bb:
        r17 = r5;
        goto L_0x011c;
    L_0x01bf:
        r18 = r5;
        goto L_0x0154;
    L_0x01c2:
        r5 = r12;
        goto L_0x010c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.SocketFetcher.getSocket(java.lang.String, int, java.util.Properties, java.lang.String, boolean):java.net.Socket");
    }

    public static Socket getSocket(String host, int port, Properties props, String prefix) throws IOException {
        return getSocket(host, port, props, prefix, false);
    }

    private static Socket createSocket(InetAddress localaddr, int localport, String host, int port, int cto, SocketFactory sf, boolean useSSL) throws IOException {
        Socket socket;
        if (sf != null) {
            socket = sf.createSocket();
        } else if (useSSL) {
            socket = SSLSocketFactory.getDefault().createSocket();
        } else {
            socket = new Socket();
        }
        if (localaddr != null) {
            socket.bind(new InetSocketAddress(localaddr, localport));
        }
        if (cto >= 0) {
            socket.connect(new InetSocketAddress(host, port), cto);
        } else {
            socket.connect(new InetSocketAddress(host, port));
        }
        return socket;
    }

    private static SocketFactory getSocketFactory(String sfClass) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (sfClass == null || sfClass.length() == 0) {
            return null;
        }
        ClassLoader cl = getContextClassLoader();
        Class clsSockFact = null;
        if (cl != null) {
            try {
                clsSockFact = cl.loadClass(sfClass);
            } catch (ClassNotFoundException e) {
            }
        }
        if (clsSockFact == null) {
            clsSockFact = Class.forName(sfClass);
        }
        return (SocketFactory) clsSockFact.getMethod("getDefault", new Class[0]).invoke(new Object(), new Object[0]);
    }

    public static Socket startTLS(Socket socket) throws IOException {
        return startTLS(socket, new Properties(), "socket");
    }

    public static Socket startTLS(Socket socket, Properties props, String prefix) throws IOException {
        String host = socket.getInetAddress().getHostName();
        int port = socket.getPort();
        try {
            SSLSocketFactory ssf;
            SocketFactory sf = getSocketFactory(props.getProperty(new StringBuilder(String.valueOf(prefix)).append(".socketFactory.class").toString(), null));
            if (sf == null || !(sf instanceof SSLSocketFactory)) {
                ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            } else {
                ssf = (SSLSocketFactory) sf;
            }
            socket = ssf.createSocket(socket, host, port, true);
            configureSSLSocket(socket, props, prefix);
            return socket;
        } catch (Exception e) {
            Exception ex = e;
            if (ex instanceof InvocationTargetException) {
                Throwable t = ((InvocationTargetException) ex).getTargetException();
                if (t instanceof Exception) {
                    ex = (Exception) t;
                }
            }
            if (ex instanceof IOException) {
                throw ((IOException) ex);
            }
            IOException ioex = new IOException("Exception in startTLS: host " + host + ", port " + port + "; Exception: " + ex);
            ioex.initCause(ex);
            throw ioex;
        }
    }

    private static void configureSSLSocket(Socket socket, Properties props, String prefix) {
        if (socket instanceof SSLSocket) {
            SSLSocket sslsocket = (SSLSocket) socket;
            String protocols = props.getProperty(new StringBuilder(String.valueOf(prefix)).append(".ssl.protocols").toString(), null);
            if (protocols != null) {
                sslsocket.setEnabledProtocols(stringArray(protocols));
            } else {
                sslsocket.setEnabledProtocols(new String[]{"TLSv1"});
            }
            String ciphers = props.getProperty(new StringBuilder(String.valueOf(prefix)).append(".ssl.ciphersuites").toString(), null);
            if (ciphers != null) {
                sslsocket.setEnabledCipherSuites(stringArray(ciphers));
            }
        }
    }

    private static String[] stringArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                ClassLoader cl = null;
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                    return cl;
                }
            }
        });
    }
}
