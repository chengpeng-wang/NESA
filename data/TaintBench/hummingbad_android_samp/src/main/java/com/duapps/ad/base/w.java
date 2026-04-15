package com.duapps.ad.base;

import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.Defaults;
import com.mopub.common.Constants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONObject;

/* compiled from: ToolboxRequestHelper */
public final class w {
    private static final Header a = new BasicHeader("Content-Encoding", "gzip");
    private static final Header b = new BasicHeader("Accept-Encoding", "gzip");
    private static DefaultHttpClient c;

    /* compiled from: ToolboxRequestHelper */
    public static class a {
        public JSONObject a;
        public JSONObject b;
        public long c;
    }

    static {
        w wVar = new w();
    }

    private w() {
    }

    public static void a(URL url, b bVar, long j) {
        try {
            Object aVar = new a();
            ArrayList arrayList = new ArrayList();
            Date date = new Date(j);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            arrayList.add(new BasicHeader("If-Modified-Since", simpleDateFormat.format(date)));
            int a = a(url, arrayList, aVar, 1);
            if (CtaButton.WIDTH_DIPS == a || 304 == a) {
                bVar.a(a, aVar);
            } else {
                bVar.a(a, aVar.b == null ? "NETWORK_FAIL" : aVar.b.optString("msg"));
            }
        } catch (Exception e) {
            bVar.a(e);
            f.a("ToolboxRequestHelper", "failed to get project", e);
        }
    }

    private static int a(URL url, List<Header> list, a aVar, int i) {
        int i2 = 1024;
        HttpResponse a = a(url, (List) list, true);
        try {
            int statusCode = a.getStatusLine().getStatusCode();
            if (statusCode != CtaButton.WIDTH_DIPS && statusCode != 304) {
                return statusCode;
            }
            if (statusCode == CtaButton.WIDTH_DIPS) {
                InputStream inputStream;
                HttpEntity entity = a.getEntity();
                InputStream content = entity.getContent();
                statusCode = (int) entity.getContentLength();
                if (statusCode >= 0) {
                    i2 = statusCode;
                }
                Header contentEncoding = entity.getContentEncoding();
                if (contentEncoding == null || contentEncoding.getValue().indexOf("gzip") == -1) {
                    inputStream = content;
                } else {
                    inputStream = new GZIPInputStream(content);
                }
                Closeable inputStreamReader = new InputStreamReader(inputStream, Defaults.ENCODING_UTF_8);
                CharArrayBuffer charArrayBuffer = new CharArrayBuffer(i2);
                char[] cArr = new char[1024];
                while (true) {
                    int read = inputStreamReader.read(cArr);
                    if (read == -1) {
                        break;
                    }
                    charArrayBuffer.append(cArr, 0, read);
                }
                d.a(inputStreamReader);
                JSONObject jSONObject = new JSONObject(charArrayBuffer.toString());
                JSONObject jSONObject2 = jSONObject.getJSONObject("responseHeader");
                aVar.b = jSONObject2;
                statusCode = jSONObject2.getInt(DuAd.DB_STATUS);
                if (statusCode == CtaButton.WIDTH_DIPS) {
                    long time;
                    aVar.a = jSONObject.getJSONObject("response");
                    Header firstHeader = a.getFirstHeader("Last-Modified");
                    if (firstHeader != null) {
                        String value = firstHeader.getValue();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                        time = simpleDateFormat.parse(value).getTime();
                    } else {
                        time = 0;
                    }
                    aVar.c = time;
                } else {
                    a(a);
                    return statusCode;
                }
            }
            a(a);
            return statusCode;
        } finally {
            a(a);
        }
    }

    public static void a(HttpResponse httpResponse) {
        if (httpResponse != null && httpResponse.getEntity() != null) {
            try {
                httpResponse.getEntity().consumeContent();
            } catch (Exception e) {
                f.a("ToolboxRequestHelper", "failed to cosume entity", e);
            }
        }
    }

    public static HttpResponse a(URI uri, String str, List<Header> list) {
        byte[] bArr;
        HttpPost httpPost = new HttpPost(uri.toString());
        httpPost.addHeader(a);
        httpPost.addHeader(b);
        if (str == null || str.length() == 0) {
            bArr = new byte[0];
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes(Defaults.ENCODING_UTF_8));
            gZIPOutputStream.close();
            bArr = byteArrayOutputStream.toByteArray();
        }
        httpPost.setEntity(new ByteArrayEntity(bArr));
        if (f.a()) {
            f.c("ToolboxRequestHelper", "request uri:" + httpPost.getURI() + ",body:" + str + ",headers:" + Arrays.asList(httpPost.getAllHeaders()));
        }
        httpPost.getParams().setParameter("http.socket.timeout", Integer.valueOf(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT));
        try {
            return a().execute(httpPost);
        } catch (IOException e) {
            httpPost.abort();
            throw e;
        }
    }

    public static HttpResponse a(URL url, List<Header> list, boolean z) {
        HttpGet httpGet = new HttpGet(url.toString());
        if (list != null) {
            for (Header addHeader : list) {
                httpGet.addHeader(addHeader);
            }
        }
        httpGet.addHeader(b);
        if (f.a()) {
            f.c("ToolboxRequestHelper", "request uri: " + httpGet.getURI() + ", headers: " + Arrays.asList(httpGet.getAllHeaders()));
        }
        httpGet.getParams().setParameter("http.socket.timeout", Integer.valueOf(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT));
        try {
            return a().execute(httpGet);
        } catch (IOException e) {
            httpGet.abort();
            throw e;
        }
    }

    private static synchronized DefaultHttpClient a() {
        DefaultHttpClient defaultHttpClient;
        synchronized (w.class) {
            if (c != null) {
                defaultHttpClient = c;
            } else {
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme(Constants.HTTP, PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme(Constants.HTTPS, SSLSocketFactory.getSocketFactory(), 443));
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
                c = defaultHttpClient;
                HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), 30000);
                HttpConnectionParams.setConnectionTimeout(c.getParams(), 30000);
                c.getParams().setIntParameter("http.protocol.max-redirects", 10);
                HttpClientParams.setCookiePolicy(c.getParams(), "compatibility");
                HttpProtocolParams.setUserAgent(c.getParams(), "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.18) Gecko/20110628 Ubuntu/10.04 (lucid) Firefox/3.6.18");
                c.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
                defaultHttpClient = c;
            }
        }
        return defaultHttpClient;
    }
}
