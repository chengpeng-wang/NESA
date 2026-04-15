package com.mb.bdapp.net;

import com.mb.bdapp.down.OtherUtils;
import com.mb.bdapp.util.LogUtil;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.Constants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpManager {
    public static final String HTTPMETHOD_GET = "GET";
    public static final String HTTPMETHOD_POST = "POST";
    private static final int SET_CONNECTION_TIMEOUT = 15000;
    private static final int SET_SOCKET_TIMEOUT = 30000;
    private static final String TAG = HttpManager.class.getSimpleName();

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[]{tm}, null);
        }

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }

    public static String openUrl(String url, String method, HttpParameters params) throws Exception {
        String result = "";
        try {
            HttpClient client = getNewHttpClient();
            HttpUriRequest request = null;
            client.getParams().setParameter("http.route.default-proxy", NetStateManager.getAPN());
            if (method.equals("GET")) {
                if (params != null) {
                    url = new StringBuilder(String.valueOf(url)).append(MASTNativeAdConstants.QUESTIONMARK).append(Utility.encodeUrl(params)).toString();
                }
                LogUtil.i(TAG, url);
                request = new HttpGet(url);
            } else {
                if (method.equals("POST")) {
                    HttpPost post = new HttpPost(url);
                    Object request2 = post;
                    String _contentType = params.getValue("content-type");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (_contentType != null) {
                        params.remove("content-type");
                        post.setHeader(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, _contentType);
                    } else {
                        post.setHeader(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded");
                    }
                    bos.write(Utility.encodeParameters(params).getBytes(Defaults.ENCODING_UTF_8));
                    byte[] data = bos.toByteArray();
                    bos.close();
                    post.setEntity(new ByteArrayEntity(data));
                } else {
                    if (method.equals("DELETE")) {
                        request2 = new HttpDelete(url);
                    }
                }
            }
            HttpResponse response = client.execute(request2);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == CtaButton.WIDTH_DIPS) {
                return readHttpResponse(response);
            }
            throw new MyHttpException(readHttpResponse(response), statusCode);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static ByteArrayOutputStream openUrl4Binary(String url, String method, HttpParameters params) throws Exception {
        try {
            HttpClient client = getNewHttpClient();
            HttpUriRequest request = null;
            client.getParams().setParameter("http.route.default-proxy", NetStateManager.getAPN());
            if (method.equals("GET")) {
                request = new HttpGet(new StringBuilder(String.valueOf(url)).append(MASTNativeAdConstants.QUESTIONMARK).append(Utility.encodeUrl(params)).toString());
            } else if (method.equals("POST")) {
                HttpPost post = new HttpPost(url);
                Object request2 = post;
                String _contentType = params.getValue("content-type");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                if (_contentType != null) {
                    params.remove("content-type");
                    post.setHeader(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, _contentType);
                } else {
                    post.setHeader(MASTNativeAdConstants.REQUEST_HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded");
                }
                bos.write(Utility.encodeParameters(params).getBytes(Defaults.ENCODING_UTF_8));
                byte[] data = bos.toByteArray();
                bos.close();
                post.setEntity(new ByteArrayEntity(data));
            } else if (method.equals("DELETE")) {
                request2 = new HttpDelete(url);
            }
            HttpResponse response = client.execute(request2);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return readBytesFromHttpResponse(response);
            }
            throw new MyHttpException(readHttpResponse(response), statusCode);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private static ByteArrayOutputStream readBytesFromHttpResponse(HttpResponse response) {
        try {
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }
            byte[] sBuffer = new byte[512];
            while (true) {
                int readBytes = inputStream.read(sBuffer);
                if (readBytes == -1) {
                    return content;
                }
                content.write(sBuffer, 0, readBytes);
            }
        } catch (IOException | IllegalStateException e) {
            return null;
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setUserAgent(params, OtherUtils.getUserAgent(null));
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, Defaults.ENCODING_UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(Constants.HTTP, PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme(Constants.HTTPS, sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, 30000);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static String readHttpResponse(HttpResponse response) {
        try {
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }
            byte[] sBuffer = new byte[512];
            while (true) {
                int readBytes = inputStream.read(sBuffer);
                if (readBytes == -1) {
                    String result = new String(content.toByteArray(), Defaults.ENCODING_UTF_8);
                    return result;
                }
                content.write(sBuffer, 0, readBytes);
            }
        } catch (IOException | IllegalStateException e) {
            return "";
        }
    }
}
