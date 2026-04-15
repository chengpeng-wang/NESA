package com.mopub.common;

import android.support.annotation.NonNull;
import com.moceanmobile.mast.Defaults;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.Networking;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

public abstract class MoPubHttpUrlConnection extends HttpURLConnection {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private MoPubHttpUrlConnection(URL url) {
        super(url);
    }

    public static HttpURLConnection getHttpUrlConnection(@NonNull String url) throws IOException {
        Preconditions.checkNotNull(url);
        if (isUrlImproperlyEncoded(url)) {
            throw new IllegalArgumentException("URL is improperly encoded: " + url);
        }
        try {
            url = urlEncode(url);
        } catch (Exception e) {
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", Networking.getCachedUserAgent());
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        return httpURLConnection;
    }

    @NonNull
    public static String urlEncode(@NonNull String url) throws Exception {
        Preconditions.checkNotNull(url);
        if (isUrlImproperlyEncoded(url)) {
            throw new UnsupportedEncodingException("URL is improperly encoded: " + url);
        }
        URI encodeUrl;
        if (isUrlUnencoded(url)) {
            encodeUrl = encodeUrl(url);
        } else {
            encodeUrl = new URI(url);
        }
        return encodeUrl.toURL().toString();
    }

    static boolean isUrlImproperlyEncoded(@NonNull String url) {
        try {
            URLDecoder.decode(url, Defaults.ENCODING_UTF_8);
            return false;
        } catch (UnsupportedEncodingException e) {
            MoPubLog.w("Url is improperly encoded: " + url);
            return true;
        }
    }

    static boolean isUrlUnencoded(@NonNull String url) {
        try {
            URI uri = new URI(url);
            return false;
        } catch (URISyntaxException e) {
            return true;
        }
    }

    @NonNull
    static URI encodeUrl(@NonNull String urlString) throws Exception {
        try {
            URL url = new URL(urlString);
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (Exception e) {
            MoPubLog.w("Failed to encode url: " + urlString);
            throw e;
        }
    }
}
