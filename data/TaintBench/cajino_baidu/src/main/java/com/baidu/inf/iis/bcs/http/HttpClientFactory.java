package com.baidu.inf.iis.bcs.http;

import com.baidu.inf.iis.bcs.model.BCSClientException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientFactory {

    private static class TrustingX509TrustManager implements X509TrustManager {
        private static final X509Certificate[] X509_CERTIFICATES = new X509Certificate[0];

        private TrustingX509TrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return X509_CERTIFICATES;
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }
    }

    private static class TrustingSocketFactory implements SchemeSocketFactory, LayeredSchemeSocketFactory {
        private SSLContext sslcontext = null;

        private TrustingSocketFactory() {
        }

        private static SSLContext createSSLContext() throws IOException {
            try {
                SSLContext instance = SSLContext.getInstance(SSLSocketFactory.TLS);
                instance.init(null, new TrustManager[]{new TrustingX509TrustManager()}, null);
                return instance;
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }

        private SSLContext getSSLContext() throws IOException {
            if (this.sslcontext == null) {
                this.sslcontext = createSSLContext();
            }
            return this.sslcontext;
        }

        public Socket createSocket(HttpParams httpParams) throws IOException {
            return getSSLContext().getSocketFactory().createSocket();
        }

        public Socket connectSocket(Socket socket, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
            int connectionTimeout = HttpConnectionParams.getConnectionTimeout(httpParams);
            int soTimeout = HttpConnectionParams.getSoTimeout(httpParams);
            SSLSocket sSLSocket = (SSLSocket) (socket != null ? socket : createSocket(httpParams));
            if (inetSocketAddress2 != null) {
                sSLSocket.bind(inetSocketAddress2);
            }
            sSLSocket.connect(inetSocketAddress, connectionTimeout);
            sSLSocket.setSoTimeout(soTimeout);
            return sSLSocket;
        }

        public boolean isSecure(Socket socket) throws IllegalArgumentException {
            return true;
        }

        public Socket createLayeredSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket();
        }
    }

    private class TrustAllSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);

        public TrustAllSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(keyStore);
            TrustingX509TrustManager trustingX509TrustManager = new TrustingX509TrustManager();
            this.sslContext.init(null, new TrustManager[]{trustingX509TrustManager}, null);
        }

        public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(socket, str, i, z);
        }

        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }

    public HttpClient createHttpClient(ClientConfiguration clientConfiguration) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(basicHttpParams, clientConfiguration.getUserAgent());
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, clientConfiguration.getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(basicHttpParams, clientConfiguration.getSocketTimeout());
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, true);
        HttpConnectionParams.setTcpNoDelay(basicHttpParams, true);
        int i = clientConfiguration.getSocketBufferSizeHints()[0];
        int i2 = clientConfiguration.getSocketBufferSizeHints()[1];
        if (i > 0 || i2 > 0) {
            HttpConnectionParams.setSocketBufferSize(basicHttpParams, Math.max(i, i2));
        }
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, null);
            schemeRegistry.register(new Scheme("https", new TrustAllSSLSocketFactory(instance), 443));
            ConnManagerParams.setMaxTotalConnections(basicHttpParams, clientConfiguration.getMaxConnections());
            ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(clientConfiguration.getMaxConnectionsPerRoute()));
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
            String proxyHost = clientConfiguration.getProxyHost();
            i2 = clientConfiguration.getProxyPort();
            if (proxyHost != null && i2 > 0) {
                defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, i2));
                String proxyUsername = clientConfiguration.getProxyUsername();
                String proxyPassword = clientConfiguration.getProxyPassword();
                String proxyDomain = clientConfiguration.getProxyDomain();
                String proxyWorkstation = clientConfiguration.getProxyWorkstation();
                if (!(proxyUsername == null || proxyPassword == null)) {
                    defaultHttpClient.getCredentialsProvider().setCredentials(new AuthScope(proxyHost, i2), new NTCredentials(proxyUsername, proxyPassword, proxyWorkstation, proxyDomain));
                }
            }
            return defaultHttpClient;
        } catch (Exception e) {
            throw new BCSClientException("Can not enable ssl.", e);
        }
    }
}
