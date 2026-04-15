package com.mvlove.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mvlove.App;
import com.mvlove.http.exception.HttpResponseException;
import com.mvlove.util.LogUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

public class HttpWrapper {
    private static final String CTWAP_PROXY_SERVER = "10.0.0.200";
    private static final String UNIWAP_PROXY_SERVER = "10.0.0.172";
    private static HttpWrapper wrapper;
    private String apnName = "";
    private boolean isWifi = false;
    private BroadcastReceiver receiver = new NetWorkStatusReceiver();

    class NetWorkStatusReceiver extends BroadcastReceiver {
        NetWorkStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            HttpWrapper.this.setNetWorkInfo(context);
        }
    }

    public static synchronized HttpWrapper getInstance() {
        HttpWrapper httpWrapper;
        synchronized (HttpWrapper.class) {
            if (wrapper == null) {
                wrapper = new HttpWrapper();
            }
            httpWrapper = wrapper;
        }
        return httpWrapper;
    }

    private HttpWrapper() {
        Context context = App.getAppContext();
        context.registerReceiver(this.receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        setNetWorkInfo(context);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            App.getAppContext().unregisterReceiver(this.receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finalize();
    }

    /* access modifiers changed from: private */
    public void setNetWorkInfo(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        LogUtil.debug("############当前网络状态改变##############");
        if (networkInfo == null) {
            setApnName("");
            setWifi(false);
            return;
        }
        LogUtil.debug("******当前活动网络为:" + networkInfo.getTypeName());
        if (1 == networkInfo.getType()) {
            setApnName("");
            setWifi(true);
        } else if (networkInfo.getType() == 0) {
            setApnName(ApnUtil.getApnType(context));
            setWifi(false);
        }
    }

    public void supportGzip(HttpRequest request) {
        request.addHeader("Accept-Encoding", "gzip");
    }

    public boolean isSupportGzip(HttpResponse response) {
        Header contentEncoding = response.getFirstHeader("Content-Encoding");
        return contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip");
    }

    public boolean checkNeedProxy() {
        if (ApnNet.CMWAP.equals(this.apnName) || ApnNet.UNIWAP.equals(this.apnName) || ApnNet.GWAP_3.equals(this.apnName) || ApnNet.CTWAP.equals(this.apnName)) {
            return true;
        }
        return false;
    }

    public void checkProxy(HttpRequest request) {
        if (!this.isWifi) {
            if (ApnNet.CMWAP.equals(this.apnName) || ApnNet.UNIWAP.equals(this.apnName) || ApnNet.GWAP_3.equals(this.apnName)) {
                ConnRouteParams.setDefaultProxy(request.getParams(), new HttpHost(UNIWAP_PROXY_SERVER, 80));
            } else if (ApnNet.CTWAP.equals(this.apnName)) {
                ConnRouteParams.setDefaultProxy(request.getParams(), new HttpHost(CTWAP_PROXY_SERVER, 80));
            }
        }
    }

    public <T> T getJSON(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return getJSON(null, url, data, clazz);
    }

    public <T> T getJSON(HttpGet request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        if (!(data == null || data.isEmpty())) {
            String paramStr = "";
            for (Entry<String, String> entry : data.entrySet()) {
                paramStr = new StringBuilder(String.valueOf(paramStr)).append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue()).toString();
            }
            if (url.indexOf("?") == -1) {
                url = new StringBuilder(String.valueOf(url)).append(paramStr.replaceFirst("&", "?")).toString();
            } else {
                url = new StringBuilder(String.valueOf(url)).append(paramStr).toString();
            }
        }
        if (request == null) {
            request = new HttpGet();
        }
        request.setURI(URI.create(url));
        LogUtil.debug("url :" + url);
        supportGzip(request);
        checkProxy(request);
        try {
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (NullPointerException e) {
            throw new IOException(e.getMessage());
        }
    }

    public <T> List<T> getJSONArray(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return getJSONArray(null, url, data, clazz);
    }

    public <T> List<T> getJSONArray(HttpGet request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        if (!(data == null || data.isEmpty())) {
            String paramStr = "";
            for (Entry<String, String> entry : data.entrySet()) {
                paramStr = new StringBuilder(String.valueOf(paramStr)).append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue()).toString();
            }
            if (url.indexOf("?") == -1) {
                url = new StringBuilder(String.valueOf(url)).append(paramStr.replaceFirst("&", "?")).toString();
            } else {
                url = new StringBuilder(String.valueOf(url)).append(paramStr).toString();
            }
        }
        if (request == null) {
            request = new HttpGet();
        }
        request.setURI(URI.create(url));
        LogUtil.debug("url :" + url);
        supportGzip(request);
        checkProxy(request);
        try {
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                Reader reader;
                try {
                    String result;
                    HttpEntity entity = response.getEntity();
                    if (isSupportGzip(response)) {
                        reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                        CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                        char[] tmp = new char[1024];
                        while (true) {
                            int l = reader.read(tmp);
                            if (l == -1) {
                                break;
                            }
                            buffer.append(tmp, 0, l);
                        }
                        reader.close();
                        result = buffer.toString();
                    } else {
                        result = EntityUtils.toString(entity);
                    }
                    entity.consumeContent();
                    Type listType = new TypeToken<List<T>>() {
                    }.getType();
                    GsonBuilder gBuilder = new GsonBuilder();
                    gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
                    return (List) gBuilder.create().fromJson(result, listType);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    return null;
                } catch (Throwable th) {
                    reader.close();
                }
            }
            throw new HttpResponseException(statusCode);
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T postJSON(String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSON(null, url, value, (Class) clazz);
    }

    public <T> T postJSON(HttpPost request, String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("url :" + url);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(new Gson().toJson(value), "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> List<T> postJSON(String url, Map<String, String> data, Class<T> clazz) throws ClientProtocolException, IOException {
        return postJSON(null, url, (Map) data, (Class) clazz);
    }

    public <T> List<T> postJSON(HttpPost request, String url, Map<String, String> data, Class<T> clazz) throws ClientProtocolException, IOException {
        LogUtil.debug("url :" + url);
        LogUtil.debug("data :" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        List<NameValuePair> parameters = new LinkedList();
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                parameters.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        Reader reader;
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            String result;
            HttpEntity entity = response.getEntity();
            if (isSupportGzip(response)) {
                reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                char[] tmp = new char[1024];
                while (true) {
                    int l = reader.read(tmp);
                    if (l == -1) {
                        break;
                    }
                    buffer.append(tmp, 0, l);
                }
                reader.close();
                result = buffer.toString();
            } else {
                result = EntityUtils.toString(entity);
            }
            entity.consumeContent();
            LogUtil.debug(result);
            GsonBuilder gBuilder = new GsonBuilder();
            Type listType = new TypeToken<List<T>>() {
            }.getType();
            gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
            return (List) gBuilder.create().fromJson(result, listType);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        } catch (Throwable th) {
            reader.close();
        }
    }

    public <T> T postJSON(String url, String json, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSON(null, url, json, (Class) clazz);
    }

    public <T> T postJSON(HttpPost request, String url, String json, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug(url);
        LogUtil.debug("data = " + json);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> List<T> postJSONArray(String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return postJSONArray(null, url, value, clazz);
    }

    public <T> List<T> postJSONArray(HttpPost request, String url, Object value, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("url = " + url);
        LogUtil.debug("data = " + value);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        try {
            StringEntity entity = new StringEntity(new Gson().toJson(value), "UTF-8");
            entity.setContentType(new BasicHeader(MIME.CONTENT_TYPE, "application/json"));
            request.setEntity(entity);
            HttpResponse response = HttpClientManager.getHttpClient().execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String result;
                entity = response.getEntity();
                if (isSupportGzip(response)) {
                    Reader reader = new InputStreamReader(new GZIPInputStream(entity.getContent()), EntityUtils.getContentCharSet(entity));
                    CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                    try {
                        char[] tmp = new char[1024];
                        while (true) {
                            int l = reader.read(tmp);
                            if (l == -1) {
                                break;
                            }
                            buffer.append(tmp, 0, l);
                        }
                        result = buffer.toString();
                    } finally {
                        reader.close();
                    }
                } else {
                    result = EntityUtils.toString(entity);
                }
                entity.consumeContent();
                GsonBuilder gBuilder = new GsonBuilder();
                Type listType = new TypeToken<List<T>>() {
                }.getType();
                gBuilder.registerTypeAdapter(listType, new ListTypeAdapter(clazz));
                return (List) gBuilder.create().fromJson(result, listType);
            }
            throw new HttpResponseException(statusCode);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T post(String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        return post(null, url, data, clazz);
    }

    public <T> T post(HttpPost request, String url, Map<String, String> data, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug("post url:" + url);
        LogUtil.debug("post data:" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        List<NameValuePair> parameters = new LinkedList();
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                parameters.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage());
        } catch (NullPointerException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    public <T> T uploadFile(String url, Map<String, String> data, Map<String, File> files, Class<T> clazz, FileTransferListener fileTransferListener) throws ParseException, IOException, HttpResponseException {
        return uploadFile(null, url, data, files, clazz, fileTransferListener);
    }

    public <T> T uploadFile(HttpPost request, String url, Map<String, String> data, Map<String, File> files, Class<T> clazz, FileTransferListener fileTransferListener) throws ParseException, IOException, HttpResponseException {
        LogUtil.debug(url);
        LogUtil.debug("data :" + data);
        if (request == null) {
            request = new HttpPost();
        }
        request.setURI(URI.create(url));
        supportGzip(request);
        checkProxy(request);
        Charset charset = Charset.forName("UTF-8");
        EIMultipartEntity entity = new EIMultipartEntity(fileTransferListener);
        if (!(data == null || data.isEmpty())) {
            for (Entry<String, String> entry : data.entrySet()) {
                entity.addPart((String) entry.getKey(), new StringBody((String) entry.getValue(), charset));
            }
        }
        if (!(files == null || files.isEmpty())) {
            for (Entry<String, File> entry2 : files.entrySet()) {
                entity.addPart((String) entry2.getKey(), new FileBody((File) entry2.getValue()));
            }
        }
        try {
            request.setEntity(entity);
            return processResponse(HttpClientManager.getHttpClient().execute(request), clazz);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private <T> T processResponse(HttpResponse response, Class<T> clazz) throws ParseException, IOException, HttpResponseException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String result;
            HttpEntity entity = response.getEntity();
            if (isSupportGzip(response)) {
                InputStream is = new GZIPInputStream(entity.getContent());
                String charset = EntityUtils.getContentCharSet(entity);
                if (TextUtils.isEmpty(charset)) {
                    charset = "UTF-8";
                }
                Reader reader = new InputStreamReader(is, charset);
                CharArrayBuffer buffer = new CharArrayBuffer((int) entity.getContentLength());
                try {
                    char[] tmp = new char[1024];
                    while (true) {
                        int l = reader.read(tmp);
                        if (l == -1) {
                            break;
                        }
                        buffer.append(tmp, 0, l);
                    }
                    result = buffer.toString();
                    LogUtil.debug(result);
                } finally {
                    reader.close();
                }
            } else {
                result = EntityUtils.toString(entity);
            }
            entity.consumeContent();
            if (TextUtils.isEmpty(result)) {
                return null;
            }
            LogUtil.debug("processResponse" + result);
            GsonBuilder gsonb = new GsonBuilder();
            gsonb.serializeNulls();
            Gson gson = gsonb.create();
            if (clazz.equals(Map.class)) {
                return gson.fromJson(result, new TypeToken<Map<String, String>>() {
                }.getType());
            }
            return !clazz.equals(String.class) ? gson.fromJson(result, (Class) clazz) : result;
        } else {
            throw new HttpResponseException(statusCode);
        }
    }

    public boolean isWifi() {
        return this.isWifi;
    }

    public void setWifi(boolean isWifi) {
        this.isWifi = isWifi;
    }

    public String getApnName() {
        return this.apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }
}
