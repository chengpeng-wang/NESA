package com.mopub.network;

import android.net.SSLCertificateSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class CustomSSLSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory mCertificateSocketFactory;

    private CustomSSLSocketFactory() {
    }

    public static CustomSSLSocketFactory getDefault(int handshakeTimeoutMillis) {
        CustomSSLSocketFactory customSSLSocketFactory = new CustomSSLSocketFactory();
        customSSLSocketFactory.mCertificateSocketFactory = SSLCertificateSocketFactory.getDefault(handshakeTimeoutMillis, null);
        return customSSLSocketFactory;
    }

    public Socket createSocket() throws IOException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket();
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    public Socket createSocket(String host, int i) throws IOException, UnknownHostException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket(host, i);
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    public Socket createSocket(String host, int port, InetAddress localhost, int localPort) throws IOException, UnknownHostException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket(host, port, localhost, localPort);
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    public Socket createSocket(InetAddress address, int port) throws IOException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket(address, port);
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localhost, int localPort) throws IOException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket(address, port, localhost, localPort);
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    public String[] getDefaultCipherSuites() {
        return this.mCertificateSocketFactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.mCertificateSocketFactory.getSupportedCipherSuites();
    }

    public Socket createSocket(Socket socketParam, String host, int port, boolean autoClose) throws IOException {
        Socket createSocket = this.mCertificateSocketFactory.createSocket(socketParam, host, port, autoClose);
        enableTlsIfAvailable(createSocket);
        return createSocket;
    }

    private void enableTlsIfAvailable(Socket socket) {
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            sSLSocket.setEnabledProtocols(sSLSocket.getSupportedProtocols());
        }
    }
}
