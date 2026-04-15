package com.androidquery.callback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Xml;
import android.view.View;
import com.androidquery.auth.AccountHandle;
import com.androidquery.auth.GoogleHandle;
import com.androidquery.util.AQUtility;
import com.androidquery.util.Common;
import com.androidquery.util.Constants;
import com.androidquery.util.Progress;
import com.androidquery.util.XmlDom;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.acra.ACRAConstants;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractAjaxCallback<T, K> implements Runnable {
    private static String AGENT = null;
    private static final Class<?>[] DEFAULT_SIG = new Class[]{String.class, Object.class, AjaxStatus.class};
    private static boolean GZIP = true;
    private static int NETWORK_POOL = 4;
    private static int NET_TIMEOUT = 30000;
    private static boolean REUSE_CLIENT = true;
    private static final String boundary = "*****";
    private static DefaultHttpClient client = null;
    private static ExecutorService fetchExe = null;
    private static int lastStatus = 200;
    private static final String lineEnd = "\r\n";
    private static SocketFactory ssf = null;
    private static Transformer st = null;
    private static final String twoHyphens = "--";
    private boolean abort;
    private WeakReference<Activity> act;
    private AccountHandle ah;
    private boolean blocked;
    private File cacheDir;
    private String callback;
    private boolean completed;
    private Map<String, String> cookies;
    private String encoding = "UTF-8";
    private long expire;
    protected boolean fileCache;
    private Object handler;
    private Map<String, String> headers;
    protected boolean memCache;
    private int method = 4;
    private String networkUrl;
    private Map<String, Object> params;
    private int policy = 0;
    private WeakReference<Object> progress;
    private HttpHost proxy;
    private boolean reauth;
    private boolean refresh;
    private HttpUriRequest request;
    protected T result;
    protected AjaxStatus status;
    private File targetFile;
    private int timeout = 0;
    private Transformer transformer;
    private Class<T> type;
    private boolean uiCallback = true;
    /* access modifiers changed from: private */
    public String url;
    private Reference<Object> whandler;

    private K self() {
        return this;
    }

    private void clear() {
        this.whandler = null;
        this.handler = null;
        this.progress = null;
        this.request = null;
        this.transformer = null;
        this.ah = null;
        this.act = null;
    }

    public static void setTimeout(int timeout) {
        NET_TIMEOUT = timeout;
    }

    public static void setAgent(String agent) {
        AGENT = agent;
    }

    public static void setGZip(boolean gzip) {
        GZIP = gzip;
    }

    public static void setTransformer(Transformer transformer) {
        st = transformer;
    }

    public Class<T> getType() {
        return this.type;
    }

    public K weakHandler(Object handler, String callback) {
        this.whandler = new WeakReference(handler);
        this.callback = callback;
        this.handler = null;
        return self();
    }

    public K handler(Object handler, String callback) {
        this.handler = handler;
        this.callback = callback;
        this.whandler = null;
        return self();
    }

    public K url(String url) {
        this.url = url;
        return self();
    }

    public K networkUrl(String url) {
        this.networkUrl = url;
        return self();
    }

    public K type(Class<T> type) {
        this.type = type;
        return self();
    }

    public K method(int method) {
        this.method = method;
        return self();
    }

    public K timeout(int timeout) {
        this.timeout = timeout;
        return self();
    }

    public K transformer(Transformer transformer) {
        this.transformer = transformer;
        return self();
    }

    public K fileCache(boolean cache) {
        this.fileCache = cache;
        return self();
    }

    public K memCache(boolean cache) {
        this.memCache = cache;
        return self();
    }

    public K policy(int policy) {
        this.policy = policy;
        return self();
    }

    public K refresh(boolean refresh) {
        this.refresh = refresh;
        return self();
    }

    public K uiCallback(boolean uiCallback) {
        this.uiCallback = uiCallback;
        return self();
    }

    public K expire(long expire) {
        this.expire = expire;
        return self();
    }

    public K header(String name, String value) {
        if (this.headers == null) {
            this.headers = new HashMap();
        }
        this.headers.put(name, value);
        return self();
    }

    public K cookie(String name, String value) {
        if (this.cookies == null) {
            this.cookies = new HashMap();
        }
        this.cookies.put(name, value);
        return self();
    }

    public K encoding(String encoding) {
        this.encoding = encoding;
        return self();
    }

    public K proxy(String host, int port) {
        this.proxy = new HttpHost(host, port);
        return self();
    }

    public K targetFile(File file) {
        this.targetFile = file;
        return self();
    }

    public K param(String name, Object value) {
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(name, value);
        return self();
    }

    public K params(Map<String, ?> params) {
        this.params = params;
        return self();
    }

    public K progress(View view) {
        return progress((Object) view);
    }

    public K progress(Dialog dialog) {
        return progress((Object) dialog);
    }

    public K progress(Object progress) {
        if (progress != null) {
            this.progress = new WeakReference(progress);
        }
        return self();
    }

    /* access modifiers changed from: 0000 */
    public void callback() {
        showProgress(false);
        this.completed = true;
        if (!isActive()) {
            skip(this.url, this.result, this.status);
        } else if (this.callback != null) {
            Class[] AJAX_SIG = new Class[]{String.class, this.type, AjaxStatus.class};
            AQUtility.invokeHandler(getHandler(), this.callback, true, true, AJAX_SIG, DEFAULT_SIG, this.url, this.result, this.status);
        } else {
            try {
                callback(this.url, this.result, this.status);
            } catch (Exception e) {
                AQUtility.report(e);
            }
        }
        filePut();
        if (!this.blocked) {
            this.status.close();
        }
        wake();
        AQUtility.debugNotify();
    }

    private void wake() {
        if (this.blocked) {
            synchronized (this) {
                try {
                    notifyAll();
                } catch (Exception e) {
                }
            }
        }
    }

    public void block() {
        if (AQUtility.isUIThread()) {
            throw new IllegalStateException("Cannot block UI thread.");
        } else if (!this.completed) {
            try {
                synchronized (this) {
                    this.blocked = true;
                    wait((long) (NET_TIMEOUT + ACRAConstants.DEFAULT_SOCKET_TIMEOUT));
                }
            } catch (Exception e) {
            }
        }
    }

    public void callback(String url, T t, AjaxStatus status) {
    }

    /* access modifiers changed from: protected */
    public void skip(String url, T t, AjaxStatus status) {
    }

    /* access modifiers changed from: protected */
    public T fileGet(String url, File file, AjaxStatus status) {
        byte[] data = null;
        try {
            if (isStreamingContent()) {
                status.file(file);
            } else {
                data = AQUtility.toBytes(new FileInputStream(file));
            }
            return transform(url, data, status);
        } catch (Exception e) {
            AQUtility.debug(e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public T datastoreGet(String url) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void showProgress(final boolean show) {
        final Object p = this.progress == null ? null : this.progress.get();
        if (p == null) {
            return;
        }
        if (AQUtility.isUIThread()) {
            Common.showProgress(p, this.url, show);
        } else {
            AQUtility.post(new Runnable() {
                public void run() {
                    Common.showProgress(p, AbstractAjaxCallback.this.url, show);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public T transform(String url, byte[] data, AjaxStatus status) {
        T result;
        Throwable e;
        Exception e2;
        if (this.type == null) {
            return null;
        }
        T file = status.getFile();
        if (data != null) {
            if (this.type.equals(Bitmap.class)) {
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            } else if (this.type.equals(JSONObject.class)) {
                result = null;
                Object str = null;
                try {
                    String str2 = new String(data, this.encoding);
                    String str3;
                    try {
                        str3 = str2;
                        return (JSONObject) new JSONTokener(str2).nextValue();
                    } catch (Exception e3) {
                        e = e3;
                        str3 = str2;
                        AQUtility.debug(e);
                        AQUtility.debug(str);
                        return result;
                    }
                } catch (Exception e4) {
                    e = e4;
                    AQUtility.debug(e);
                    AQUtility.debug(str);
                    return result;
                }
            } else if (this.type.equals(JSONArray.class)) {
                result = null;
                try {
                    return (JSONArray) new JSONTokener(new String(data, this.encoding)).nextValue();
                } catch (Exception e5) {
                    AQUtility.debug(e5);
                    return result;
                }
            } else if (this.type.equals(String.class)) {
                if (status.getSource() == 1) {
                    AQUtility.debug((Object) "network");
                    return correctEncoding(data, this.encoding, status);
                }
                AQUtility.debug((Object) "file");
                try {
                    return new String(data, this.encoding);
                } catch (Exception e52) {
                    AQUtility.debug(e52);
                    return null;
                }
            } else if (this.type.equals(byte[].class)) {
                return data;
            } else {
                if (this.transformer != null) {
                    return this.transformer.transform(url, this.type, this.encoding, data, status);
                } else if (st != null) {
                    return st.transform(url, this.type, this.encoding, data, status);
                }
            }
        } else if (file != null) {
            if (this.type.equals(File.class)) {
                return file;
            }
            if (this.type.equals(XmlDom.class)) {
                try {
                    InputStream fis = new FileInputStream(file);
                    T result2 = new XmlDom(fis);
                    try {
                        status.closeLater(fis);
                        return result2;
                    } catch (Exception e6) {
                        e2 = e6;
                        AQUtility.report(e2);
                        return null;
                    }
                } catch (Exception e7) {
                    e2 = e7;
                    AQUtility.report(e2);
                    return null;
                }
            } else if (this.type.equals(XmlPullParser.class)) {
                T parser = Xml.newPullParser();
                try {
                    FileInputStream fis2 = new FileInputStream(file);
                    parser.setInput(fis2, this.encoding);
                    status.closeLater(fis2);
                    return parser;
                } catch (Exception e22) {
                    AQUtility.report(e22);
                    return null;
                }
            } else if (this.type.equals(InputStream.class)) {
                try {
                    T fis3 = new FileInputStream(file);
                    status.closeLater(fis3);
                    return fis3;
                } catch (Exception e222) {
                    AQUtility.report(e222);
                    return null;
                }
            }
        }
        return null;
    }

    private String getCharset(String html) {
        Matcher m = Pattern.compile("<meta [^>]*http-equiv[^>]*\"Content-Type\"[^>]*>", 2).matcher(html);
        if (m.find()) {
            return parseCharset(m.group());
        }
        return null;
    }

    private String parseCharset(String tag) {
        if (tag == null) {
            return null;
        }
        int i = tag.indexOf("charset");
        if (i != -1) {
            return tag.substring(i + 7).replaceAll("[^\\w-]", "");
        }
        return null;
    }

    private String correctEncoding(byte[] data, String target, AjaxStatus status) {
        Exception e;
        String result = null;
        try {
            if (!"utf-8".equalsIgnoreCase(target)) {
                return new String(data, target);
            }
            String header = parseCharset(status.getHeader("Content-Type"));
            AQUtility.debug("parsing header", header);
            if (header != null) {
                return new String(data, header);
            }
            String result2 = new String(data, "utf-8");
            try {
                String charset = getCharset(result2);
                AQUtility.debug("parsing needed", charset);
                if (charset == null || "utf-8".equalsIgnoreCase(charset)) {
                    result = result2;
                    return result;
                }
                AQUtility.debug("correction needed", charset);
                result = new String(data, charset);
                status.data(result.getBytes("utf-8"));
                return result;
            } catch (Exception e2) {
                e = e2;
                result = result2;
                AQUtility.report(e);
                return result;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* access modifiers changed from: protected */
    public T memGet(String url) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void memPut(String url, T t) {
    }

    /* access modifiers changed from: protected */
    public void filePut(String url, T t, File file, byte[] data) {
        if (file != null && data != null) {
            AQUtility.storeAsync(file, data, 0);
        }
    }

    /* access modifiers changed from: protected */
    public File accessFile(File cacheDir, String url) {
        if (this.expire < 0) {
            return null;
        }
        File file = AQUtility.getExistedCacheByUrl(cacheDir, url);
        if (file == null || this.expire == 0 || System.currentTimeMillis() - file.lastModified() <= this.expire) {
            return file;
        }
        return null;
    }

    public void async(Activity act) {
        if (act.isFinishing()) {
            AQUtility.warn("Warning", "Possible memory leak. Calling ajax with a terminated activity.");
        }
        if (this.type == null) {
            AQUtility.warn("Warning", "type() is not called with response type.");
            return;
        }
        this.act = new WeakReference(act);
        async((Context) act);
    }

    public void async(Context context) {
        if (this.status == null) {
            this.status = new AjaxStatus();
            this.status.redirect(this.url).refresh(this.refresh);
        } else if (this.status.getDone()) {
            this.status.reset();
            this.result = null;
        }
        showProgress(true);
        if (this.ah == null || this.ah.authenticated()) {
            work(context);
            return;
        }
        AQUtility.debug("auth needed", this.url);
        this.ah.auth(this);
    }

    private boolean isActive() {
        if (this.act == null) {
            return true;
        }
        Activity a = (Activity) this.act.get();
        if (a == null || a.isFinishing()) {
            return false;
        }
        return true;
    }

    public void failure(int code, String message) {
        if (this.status != null) {
            this.status.code(code).message(message);
            callback();
        }
    }

    private void work(Context context) {
        T object = memGet(this.url);
        if (object != null) {
            this.result = object;
            this.status.source(4).done();
            callback();
            return;
        }
        this.cacheDir = AQUtility.getCacheDir(context, this.policy);
        execute(this);
    }

    /* access modifiers changed from: protected */
    public boolean cacheAvailable(Context context) {
        return this.fileCache && AQUtility.getExistedCacheByUrl(AQUtility.getCacheDir(context, this.policy), this.url) != null;
    }

    public void run() {
        if (this.status.getDone()) {
            afterWork();
            return;
        }
        try {
            backgroundWork();
        } catch (Throwable e) {
            AQUtility.debug(e);
            this.status.code(AjaxStatus.NETWORK_ERROR).done();
        }
        if (!this.status.getReauth()) {
            if (this.uiCallback) {
                AQUtility.post(this);
            } else {
                afterWork();
            }
        }
    }

    private void backgroundWork() {
        if (!this.refresh && this.fileCache) {
            fileWork();
        }
        if (this.result == null) {
            datastoreWork();
        }
        if (this.result == null) {
            networkWork();
        }
    }

    private String getCacheUrl() {
        if (this.ah != null) {
            return this.ah.getCacheUrl(this.url);
        }
        return this.url;
    }

    private String getNetworkUrl(String url) {
        String result = url;
        if (this.networkUrl != null) {
            result = this.networkUrl;
        }
        if (this.ah != null) {
            return this.ah.getNetworkUrl(result);
        }
        return result;
    }

    private void fileWork() {
        File file = accessFile(this.cacheDir, getCacheUrl());
        if (file != null) {
            this.status.source(3);
            this.result = fileGet(this.url, file, this.status);
            if (this.result != null) {
                this.status.time(new Date(file.lastModified())).done();
            }
        }
    }

    private void datastoreWork() {
        this.result = datastoreGet(this.url);
        if (this.result != null) {
            this.status.source(2).done();
        }
    }

    private void networkWork() {
        if (this.url == null) {
            this.status.code(AjaxStatus.NETWORK_ERROR).done();
            return;
        }
        byte[] data = null;
        try {
            network();
            if (!(this.ah == null || !this.ah.expired(this, this.status) || this.reauth)) {
                AQUtility.debug("reauth needed", this.status.getMessage());
                this.reauth = true;
                if (this.ah.reauth(this)) {
                    network();
                } else {
                    this.status.reauth(true);
                    return;
                }
            }
            data = this.status.getData();
        } catch (Exception e) {
            AQUtility.debug(e);
            this.status.code(AjaxStatus.NETWORK_ERROR).message("network error");
        }
        try {
            this.result = transform(this.url, data, this.status);
        } catch (Exception e2) {
            AQUtility.debug(e2);
        }
        if (this.result == null && data != null) {
            this.status.code(AjaxStatus.TRANSFORM_ERROR).message("transform error");
        }
        lastStatus = this.status.getCode();
        this.status.done();
    }

    /* access modifiers changed from: protected */
    public File getCacheFile() {
        return AQUtility.getCacheFile(this.cacheDir, getCacheUrl());
    }

    /* access modifiers changed from: protected */
    public boolean isStreamingContent() {
        return File.class.equals(this.type) || XmlPullParser.class.equals(this.type) || InputStream.class.equals(this.type) || XmlDom.class.equals(this.type);
    }

    private File getPreFile() {
        File result = null;
        if (isStreamingContent()) {
            if (this.targetFile != null) {
                result = this.targetFile;
            } else if (this.fileCache) {
                result = getCacheFile();
            } else {
                File dir = AQUtility.getTempDir();
                if (dir == null) {
                    dir = this.cacheDir;
                }
                result = AQUtility.getCacheFile(dir, this.url);
            }
        }
        if (result == null || result.exists()) {
            return result;
        }
        try {
            result.getParentFile().mkdirs();
            result.createNewFile();
            return result;
        } catch (Exception e) {
            AQUtility.report(e);
            return null;
        }
    }

    private void filePut() {
        if (this.result != null && this.fileCache) {
            byte[] data = this.status.getData();
            if (data != null) {
                try {
                    if (this.status.getSource() == 1) {
                        File file = getCacheFile();
                        if (!this.status.getInvalid()) {
                            filePut(this.url, this.result, file, data);
                        } else if (file.exists()) {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    AQUtility.debug(e);
                }
            }
            this.status.data(null);
        }
    }

    private static String extractUrl(Uri uri) {
        String result = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
        String fragment = uri.getFragment();
        if (fragment != null) {
            return new StringBuilder(String.valueOf(result)).append("#").append(fragment).toString();
        }
        return result;
    }

    private static Map<String, Object> extractParams(Uri uri) {
        Map<String, Object> params = new HashMap();
        for (String pair : uri.getQuery().split("&")) {
            String[] split = pair.split("=");
            if (split.length >= 2) {
                params.put(split[0], split[1]);
            } else if (split.length == 1) {
                params.put(split[0], "");
            }
        }
        return params;
    }

    private void network() throws IOException {
        String url = this.url;
        Map<String, Object> params = this.params;
        if (params == null && url.length() > 2000) {
            Uri uri = Uri.parse(url);
            url = extractUrl(uri);
            params = extractParams(uri);
        }
        url = getNetworkUrl(url);
        if (2 == this.method) {
            httpDelete(url, this.headers, this.status);
        } else if (3 == this.method) {
            httpPut(url, this.headers, params, this.status);
        } else {
            if (1 == this.method && params == null) {
                params = new HashMap();
            }
            if (params == null) {
                httpGet(url, this.headers, this.status);
            } else if (isMultiPart(params)) {
                httpMulti(url, this.headers, params, this.status);
            } else {
                httpPost(url, this.headers, params, this.status);
            }
        }
    }

    private void afterWork() {
        if (this.url != null && this.memCache) {
            memPut(this.url, this.result);
        }
        callback();
        clear();
    }

    public static void execute(Runnable job) {
        if (fetchExe == null) {
            fetchExe = Executors.newFixedThreadPool(NETWORK_POOL);
        }
        fetchExe.execute(job);
    }

    public static void setNetworkLimit(int limit) {
        NETWORK_POOL = Math.max(1, Math.min(25, limit));
        fetchExe = null;
        AQUtility.debug("setting network limit", Integer.valueOf(NETWORK_POOL));
    }

    public static void cancel() {
        if (fetchExe != null) {
            fetchExe.shutdownNow();
            fetchExe = null;
        }
        BitmapAjaxCallback.clearTasks();
    }

    private static String patchUrl(String url) {
        return url.replaceAll(" ", "%20").replaceAll("\\|", "%7C");
    }

    private void httpGet(String url, Map<String, String> headers, AjaxStatus status) throws IOException {
        AQUtility.debug("get", url);
        url = patchUrl(url);
        httpDo(new HttpGet(url), url, headers, status);
    }

    private void httpDelete(String url, Map<String, String> headers, AjaxStatus status) throws IOException {
        AQUtility.debug("get", url);
        url = patchUrl(url);
        httpDo(new HttpDelete(url), url, headers, status);
    }

    private void httpPost(String url, Map<String, String> headers, Map<String, Object> params, AjaxStatus status) throws ClientProtocolException, IOException {
        AQUtility.debug("post", url);
        httpEntity(url, new HttpPost(url), headers, params, status);
    }

    private void httpPut(String url, Map<String, String> headers, Map<String, Object> params, AjaxStatus status) throws ClientProtocolException, IOException {
        AQUtility.debug("put", url);
        httpEntity(url, new HttpPut(url), headers, params, status);
    }

    private void httpEntity(String url, HttpEntityEnclosingRequestBase req, Map<String, String> headers, Map<String, Object> params, AjaxStatus status) throws ClientProtocolException, IOException {
        HttpEntity entity;
        req.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        HttpEntity value = params.get(Constants.POST_ENTITY);
        if (value instanceof HttpEntity) {
            entity = value;
        } else {
            List<NameValuePair> pairs = new ArrayList();
            for (Entry<String, Object> e : params.entrySet()) {
                Object value2 = e.getValue();
                if (value2 != null) {
                    pairs.add(new BasicNameValuePair((String) e.getKey(), value2.toString()));
                }
            }
            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
        }
        if (!(headers == null || headers.containsKey("Content-Type"))) {
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        req.setEntity(entity);
        httpDo(req, url, headers, status);
    }

    public static void setSSF(SocketFactory sf) {
        ssf = sf;
        client = null;
    }

    public static void setReuseHttpClient(boolean reuse) {
        REUSE_CLIENT = reuse;
        client = null;
    }

    private static DefaultHttpClient getClient() {
        if (client == null || !REUSE_CLIENT) {
            AQUtility.debug((Object) "creating http client");
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, NET_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, NET_TIMEOUT);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(25));
            HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", ssf == null ? SSLSocketFactory.getSocketFactory() : ssf, 443));
            client = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
        }
        return client;
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0167  */
    private void httpDo(org.apache.http.client.methods.HttpUriRequest r33, java.lang.String r34, java.util.Map<java.lang.String, java.lang.String> r35, com.androidquery.callback.AjaxStatus r36) throws org.apache.http.client.ClientProtocolException, java.io.IOException {
        /*
        r32 = this;
        r28 = AGENT;
        if (r28 == 0) goto L_0x0011;
    L_0x0004:
        r28 = "User-Agent";
        r29 = AGENT;
        r0 = r33;
        r1 = r28;
        r2 = r29;
        r0.addHeader(r1, r2);
    L_0x0011:
        if (r35 == 0) goto L_0x0021;
    L_0x0013:
        r28 = r35.keySet();
        r29 = r28.iterator();
    L_0x001b:
        r28 = r29.hasNext();
        if (r28 != 0) goto L_0x00e0;
    L_0x0021:
        r28 = GZIP;
        if (r28 == 0) goto L_0x0040;
    L_0x0025:
        if (r35 == 0) goto L_0x0033;
    L_0x0027:
        r28 = "Accept-Encoding";
        r0 = r35;
        r1 = r28;
        r28 = r0.containsKey(r1);
        if (r28 != 0) goto L_0x0040;
    L_0x0033:
        r28 = "Accept-Encoding";
        r29 = "gzip";
        r0 = r33;
        r1 = r28;
        r2 = r29;
        r0.addHeader(r1, r2);
    L_0x0040:
        r8 = r32.makeCookie();
        if (r8 == 0) goto L_0x004f;
    L_0x0046:
        r28 = "Cookie";
        r0 = r33;
        r1 = r28;
        r0.addHeader(r1, r8);
    L_0x004f:
        r0 = r32;
        r0 = r0.ah;
        r28 = r0;
        if (r28 == 0) goto L_0x0066;
    L_0x0057:
        r0 = r32;
        r0 = r0.ah;
        r28 = r0;
        r0 = r28;
        r1 = r32;
        r2 = r33;
        r0.applyToken(r1, r2);
    L_0x0066:
        r5 = getClient();
        r18 = r33.getParams();
        r0 = r32;
        r0 = r0.proxy;
        r28 = r0;
        if (r28 == 0) goto L_0x0087;
    L_0x0076:
        r28 = "http.route.default-proxy";
        r0 = r32;
        r0 = r0.proxy;
        r29 = r0;
        r0 = r18;
        r1 = r28;
        r2 = r29;
        r0.setParameter(r1, r2);
    L_0x0087:
        r0 = r32;
        r0 = r0.timeout;
        r28 = r0;
        if (r28 <= 0) goto L_0x00b9;
    L_0x008f:
        r28 = "http.connection.timeout";
        r0 = r32;
        r0 = r0.timeout;
        r29 = r0;
        r29 = java.lang.Integer.valueOf(r29);
        r0 = r18;
        r1 = r28;
        r2 = r29;
        r0.setParameter(r1, r2);
        r28 = "http.socket.timeout";
        r0 = r32;
        r0 = r0.timeout;
        r29 = r0;
        r29 = java.lang.Integer.valueOf(r29);
        r0 = r18;
        r1 = r28;
        r2 = r29;
        r0.setParameter(r1, r2);
    L_0x00b9:
        r7 = new org.apache.http.protocol.BasicHttpContext;
        r7.<init>();
        r9 = new org.apache.http.impl.client.BasicCookieStore;
        r9.<init>();
        r28 = "http.cookie-store";
        r0 = r28;
        r7.setAttribute(r0, r9);
        r0 = r33;
        r1 = r32;
        r1.request = r0;
        r0 = r32;
        r0 = r0.abort;
        r28 = r0;
        if (r28 == 0) goto L_0x00fb;
    L_0x00d8:
        r28 = new java.io.IOException;
        r29 = "Aborted";
        r28.<init>(r29);
        throw r28;
    L_0x00e0:
        r21 = r29.next();
        r21 = (java.lang.String) r21;
        r0 = r35;
        r1 = r21;
        r28 = r0.get(r1);
        r28 = (java.lang.String) r28;
        r0 = r33;
        r1 = r21;
        r2 = r28;
        r0.addHeader(r1, r2);
        goto L_0x001b;
    L_0x00fb:
        r25 = 0;
        r0 = r33;
        r25 = r5.execute(r0, r7);	 Catch:{ HttpHostConnectException -> 0x01bc }
    L_0x0103:
        r12 = 0;
        r24 = r34;
        r28 = r25.getStatusLine();
        r6 = r28.getStatusCode();
        r28 = r25.getStatusLine();
        r20 = r28.getReasonPhrase();
        r15 = 0;
        r14 = r25.getEntity();
        r17 = 0;
        r28 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r28;
        if (r6 < r0) goto L_0x0129;
    L_0x0123:
        r28 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r0 = r28;
        if (r6 < r0) goto L_0x01ee;
    L_0x0129:
        r19 = 0;
        if (r14 == 0) goto L_0x0159;
    L_0x012d:
        r19 = r14.getContent();	 Catch:{ Exception -> 0x01e0 }
        r0 = r32;
        r28 = r0.getEncoding(r14);	 Catch:{ Exception -> 0x01e0 }
        r0 = r32;
        r1 = r28;
        r2 = r19;
        r26 = r0.toData(r1, r2);	 Catch:{ Exception -> 0x01e0 }
        r16 = new java.lang.String;	 Catch:{ Exception -> 0x01e0 }
        r28 = "UTF-8";
        r0 = r16;
        r1 = r26;
        r2 = r28;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x01e0 }
        r28 = "error";
        r0 = r28;
        r1 = r16;
        com.androidquery.util.AQUtility.debug(r0, r1);	 Catch:{ Exception -> 0x02b4, all -> 0x02af }
        r15 = r16;
    L_0x0159:
        com.androidquery.util.AQUtility.close(r19);
    L_0x015c:
        r28 = "response";
        r29 = java.lang.Integer.valueOf(r6);
        com.androidquery.util.AQUtility.debug(r28, r29);
        if (r12 == 0) goto L_0x0175;
    L_0x0167:
        r0 = r12.length;
        r28 = r0;
        r28 = java.lang.Integer.valueOf(r28);
        r0 = r28;
        r1 = r34;
        com.androidquery.util.AQUtility.debug(r0, r1);
    L_0x0175:
        r0 = r36;
        r28 = r0.code(r6);
        r0 = r28;
        r1 = r20;
        r28 = r0.message(r1);
        r0 = r28;
        r28 = r0.error(r15);
        r0 = r28;
        r1 = r24;
        r28 = r0.redirect(r1);
        r29 = new java.util.Date;
        r29.<init>();
        r28 = r28.time(r29);
        r0 = r28;
        r28 = r0.data(r12);
        r0 = r28;
        r1 = r17;
        r28 = r0.file(r1);
        r0 = r28;
        r28 = r0.client(r5);
        r0 = r28;
        r28 = r0.context(r7);
        r29 = r25.getAllHeaders();
        r28.headers(r29);
        return;
    L_0x01bc:
        r13 = move-exception;
        r0 = r32;
        r0 = r0.proxy;
        r28 = r0;
        if (r28 == 0) goto L_0x01df;
    L_0x01c5:
        r28 = "proxy failed, retrying without proxy";
        com.androidquery.util.AQUtility.debug(r28);
        r28 = "http.route.default-proxy";
        r29 = 0;
        r0 = r18;
        r1 = r28;
        r2 = r29;
        r0.setParameter(r1, r2);
        r0 = r33;
        r25 = r5.execute(r0, r7);
        goto L_0x0103;
    L_0x01df:
        throw r13;
    L_0x01e0:
        r13 = move-exception;
    L_0x01e1:
        com.androidquery.util.AQUtility.debug(r13);	 Catch:{ all -> 0x01e9 }
        com.androidquery.util.AQUtility.close(r19);
        goto L_0x015c;
    L_0x01e9:
        r28 = move-exception;
    L_0x01ea:
        com.androidquery.util.AQUtility.close(r19);
        throw r28;
    L_0x01ee:
        r28 = "http.target_host";
        r0 = r28;
        r10 = r7.getAttribute(r0);
        r10 = (org.apache.http.HttpHost) r10;
        r28 = "http.request";
        r0 = r28;
        r11 = r7.getAttribute(r0);
        r11 = (org.apache.http.client.methods.HttpUriRequest) r11;
        r28 = new java.lang.StringBuilder;
        r29 = r10.toURI();
        r29 = java.lang.String.valueOf(r29);
        r28.<init>(r29);
        r29 = r11.getURI();
        r28 = r28.append(r29);
        r24 = r28.toString();
        r28 = 32;
        r29 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r30 = r14.getContentLength();
        r0 = r30;
        r0 = (int) r0;
        r30 = r0;
        r29 = java.lang.Math.min(r29, r30);
        r27 = java.lang.Math.max(r28, r29);
        r22 = 0;
        r19 = 0;
        r17 = r32.getPreFile();	 Catch:{ all -> 0x02a7 }
        if (r17 != 0) goto L_0x027c;
    L_0x023a:
        r23 = new com.androidquery.util.PredefinedBAOS;	 Catch:{ all -> 0x02a7 }
        r0 = r23;
        r1 = r27;
        r0.m233init(r1);	 Catch:{ all -> 0x02a7 }
        r22 = r23;
    L_0x0245:
        r28 = r14.getContent();	 Catch:{ all -> 0x02a7 }
        r0 = r32;
        r29 = r0.getEncoding(r14);	 Catch:{ all -> 0x02a7 }
        r30 = r14.getContentLength();	 Catch:{ all -> 0x02a7 }
        r0 = r30;
        r0 = (int) r0;	 Catch:{ all -> 0x02a7 }
        r30 = r0;
        r0 = r32;
        r1 = r28;
        r2 = r22;
        r3 = r29;
        r4 = r30;
        r0.copy(r1, r2, r3, r4);	 Catch:{ all -> 0x02a7 }
        r22.flush();	 Catch:{ all -> 0x02a7 }
        if (r17 != 0) goto L_0x0294;
    L_0x026a:
        r0 = r22;
        r0 = (com.androidquery.util.PredefinedBAOS) r0;	 Catch:{ all -> 0x02a7 }
        r28 = r0;
        r12 = r28.toByteArray();	 Catch:{ all -> 0x02a7 }
    L_0x0274:
        com.androidquery.util.AQUtility.close(r19);
        com.androidquery.util.AQUtility.close(r22);
        goto L_0x015c;
    L_0x027c:
        r17.createNewFile();	 Catch:{ all -> 0x02a7 }
        r23 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x02a7 }
        r28 = new java.io.FileOutputStream;	 Catch:{ all -> 0x02a7 }
        r0 = r28;
        r1 = r17;
        r0.<init>(r1);	 Catch:{ all -> 0x02a7 }
        r0 = r23;
        r1 = r28;
        r0.<init>(r1);	 Catch:{ all -> 0x02a7 }
        r22 = r23;
        goto L_0x0245;
    L_0x0294:
        r28 = r17.exists();	 Catch:{ all -> 0x02a7 }
        if (r28 == 0) goto L_0x02a4;
    L_0x029a:
        r28 = r17.length();	 Catch:{ all -> 0x02a7 }
        r30 = 0;
        r28 = (r28 > r30 ? 1 : (r28 == r30 ? 0 : -1));
        if (r28 != 0) goto L_0x0274;
    L_0x02a4:
        r17 = 0;
        goto L_0x0274;
    L_0x02a7:
        r28 = move-exception;
        com.androidquery.util.AQUtility.close(r19);
        com.androidquery.util.AQUtility.close(r22);
        throw r28;
    L_0x02af:
        r28 = move-exception;
        r15 = r16;
        goto L_0x01ea;
    L_0x02b4:
        r13 = move-exception;
        r15 = r16;
        goto L_0x01e1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.androidquery.callback.AbstractAjaxCallback.httpDo(org.apache.http.client.methods.HttpUriRequest, java.lang.String, java.util.Map, com.androidquery.callback.AjaxStatus):void");
    }

    private String getEncoding(HttpEntity entity) {
        if (entity == null) {
            return null;
        }
        Header eheader = entity.getContentEncoding();
        if (eheader != null) {
            return eheader.getValue();
        }
        return null;
    }

    private void copy(InputStream is, OutputStream os, String encoding, int max) throws IOException {
        if ("gzip".equalsIgnoreCase(encoding)) {
            is = new GZIPInputStream(is);
        }
        Object o = null;
        if (this.progress != null) {
            o = this.progress.get();
        }
        Progress p = null;
        if (o != null) {
            p = new Progress(o);
        }
        AQUtility.copy(is, os, max, p);
    }

    public K auth(Activity act, String type, String account) {
        if (VERSION.SDK_INT >= 5 && type.startsWith("g.")) {
            this.ah = new GoogleHandle(act, type, account);
        }
        return self();
    }

    public K auth(AccountHandle handle) {
        this.ah = handle;
        return self();
    }

    public String getUrl() {
        return this.url;
    }

    public Object getHandler() {
        if (this.handler != null) {
            return this.handler;
        }
        if (this.whandler == null) {
            return null;
        }
        return this.whandler.get();
    }

    public String getCallback() {
        return this.callback;
    }

    protected static int getLastStatus() {
        return lastStatus;
    }

    public T getResult() {
        return this.result;
    }

    public AjaxStatus getStatus() {
        return this.status;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void abort() {
        this.abort = true;
        if (this.request != null && !this.request.isAborted()) {
            this.request.abort();
        }
    }

    private static boolean isMultiPart(Map<String, Object> params) {
        for (Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            AQUtility.debug(entry.getKey(), value);
            if ((value instanceof File) || (value instanceof byte[])) {
                return true;
            }
            if (value instanceof InputStream) {
                return true;
            }
        }
        return false;
    }

    private void httpMulti(String url, Map<String, String> headers, Map<String, Object> params, AjaxStatus status) throws IOException {
        AQUtility.debug("multipart", url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(NET_TIMEOUT * 4);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=*****");
        if (headers != null) {
            for (String name : headers.keySet()) {
                conn.setRequestProperty(name, (String) headers.get(name));
            }
        }
        String cookie = makeCookie();
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        if (this.ah != null) {
            this.ah.applyToken(this, conn);
        }
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        for (Entry<String, Object> entry : params.entrySet()) {
            writeObject(dos, (String) entry.getKey(), entry.getValue());
        }
        dos.writeBytes("--*****--\r\n");
        dos.flush();
        dos.close();
        conn.connect();
        int code = conn.getResponseCode();
        String message = conn.getResponseMessage();
        byte[] data = null;
        String encoding = conn.getContentEncoding();
        String error = null;
        if (code < 200 || code >= 300) {
            error = new String(toData(encoding, conn.getErrorStream()), "UTF-8");
            AQUtility.debug("error", error);
        } else {
            data = toData(encoding, conn.getInputStream());
        }
        AQUtility.debug("response", Integer.valueOf(code));
        if (data != null) {
            AQUtility.debug(Integer.valueOf(data.length), url);
        }
        status.code(code).message(message).redirect(url).time(new Date()).data(data).error(error).client(null);
    }

    private byte[] toData(String encoding, InputStream is) throws IOException {
        if ("gzip".equalsIgnoreCase(encoding)) {
            is = new GZIPInputStream(is);
        }
        return AQUtility.toBytes(is);
    }

    private static void writeObject(DataOutputStream dos, String name, Object obj) throws IOException {
        if (obj != null) {
            if (obj instanceof File) {
                File file = (File) obj;
                writeData(dos, name, file.getName(), new FileInputStream(file));
            } else if (obj instanceof byte[]) {
                writeData(dos, name, name, new ByteArrayInputStream((byte[]) obj));
            } else if (obj instanceof InputStream) {
                writeData(dos, name, name, (InputStream) obj);
            } else {
                writeField(dos, name, obj.toString());
            }
        }
    }

    private static void writeData(DataOutputStream dos, String name, String filename, InputStream is) throws IOException {
        dos.writeBytes("--*****\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\";" + " filename=\"" + filename + "\"" + lineEnd);
        dos.writeBytes(lineEnd);
        AQUtility.copy(is, dos);
        dos.writeBytes(lineEnd);
    }

    private static void writeField(DataOutputStream dos, String name, String value) throws IOException {
        dos.writeBytes("--*****\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"");
        dos.writeBytes(lineEnd);
        dos.writeBytes(lineEnd);
        dos.write(value.getBytes("UTF-8"));
        dos.writeBytes(lineEnd);
    }

    private String makeCookie() {
        if (this.cookies == null || this.cookies.size() == 0) {
            return null;
        }
        Iterator<String> iter = this.cookies.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) this.cookies.get(key);
            sb.append(key);
            sb.append("=");
            sb.append(value);
            if (iter.hasNext()) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }
}
