package fm.xtube;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import fm.xtube.core.CallBack;
import fm.xtube.core.GodHelpMe;
import fm.xtube.core.MainManager;
import fm.xtube.core.Server;
import sx.jolly.core.JollyService;
import sx.jolly.utils.Utils;

public class CheckAgeActivity extends GodHelpMe {
    private Button checkAgeButton;
    private CheckBox checkBoxAge;
    private boolean firstRun;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setPartner(this);
        startService(new Intent(getBaseContext(), JollyService.class));
        setContentView(R.layout.welcome);
        this.firstRun = getSharedPreferences("PREFERENCE", 0).getBoolean("firstRun", true);
        Server.setDeviceId(((TelephonyManager) this.self.getSystemService("phone")).getDeviceId());
        if (!isOnline()) {
            setContentView(R.layout.noconnection);
        }
        this.checkAgeButton = (Button) findViewById(R.id.btn18);
        this.checkBoxAge = (CheckBox) findViewById(R.id.checkboxAgreement);
        if (this.firstRun) {
            getSharedPreferences("PREFERENCE", 0).edit().putBoolean("firstRun", false).commit();
        } else {
            this.checkBoxAge.setVisibility(8);
        }
        this.checkAgeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CheckAgeActivity.this.isOnline()) {
                    CheckAgeActivity.this.goToMain();
                } else if (CheckAgeActivity.this.isOnline()) {
                    CheckAgeActivity.this.goToMain();
                }
            }
        });
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void goToMain() {
        if (this.checkBoxAge.isChecked()) {
            this.self.hdProgress();
            MainManager.checkPaiment(new CallBack() {
                public void onFinished(Object result) {
                    if (((Boolean) result).booleanValue()) {
                        CheckAgeActivity.this.self.hideProgress();
                        CheckAgeActivity.this.startActivity(new Intent(CheckAgeActivity.this.self, HdActivity.class));
                        return;
                    }
                    CheckAgeActivity.this.self.hideProgress();
                    CheckAgeActivity.this.startActivity(new Intent(CheckAgeActivity.this.self, MainActivity.class));
                }

                public void onFail(String message) {
                }
            });
            return;
        }
        Toast toast = Toast.makeText(getApplicationContext(), this.self.getResources().getString(R.string.check_false), 0);
        toast.setGravity(49, 0, 350);
        toast.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setFlags(268435456);
        startActivity(intent);
    }
}
package com.loopj.android.http;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
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
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

public class AsyncHttpClient {
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String VERSION = "1.4.0";
    private static int maxConnections = 10;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;
    /* access modifiers changed from: private|final */
    public final Map<String, String> clientHeaderMap;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext = new SyncBasicHttpContext(new BasicHttpContext());
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private ThreadPoolExecutor threadPool;

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity httpEntity) {
            super(httpEntity);
        }

        public InputStream getContent() throws IOException {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }

    public AsyncHttpClient() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(basicHttpParams, (long) socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 10);
        HttpConnectionParams.setSoTimeout(basicHttpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(basicHttpParams, true);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(basicHttpParams, String.format("android-async-http/%s (http://loopj.com/android-async-http)", new Object[]{VERSION}));
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
        this.httpClient = new DefaultHttpClient(threadSafeClientConnManager, basicHttpParams);
        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest httpRequest, HttpContext httpContext) {
                if (!httpRequest.containsHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING)) {
                    httpRequest.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
                }
                for (String str : AsyncHttpClient.this.clientHeaderMap.keySet()) {
                    httpRequest.addHeader(str, (String) AsyncHttpClient.this.clientHeaderMap.get(str));
                }
            }
        });
        this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse httpResponse, HttpContext httpContext) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    Header contentEncoding = entity.getContentEncoding();
                    if (contentEncoding != null) {
                        for (HeaderElement name : contentEncoding.getElements()) {
                            if (name.getName().equalsIgnoreCase(AsyncHttpClient.ENCODING_GZIP)) {
                                httpResponse.setEntity(new InflatingEntity(httpResponse.getEntity()));
                                return;
                            }
                        }
                    }
                }
            }
        });
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(5));
        this.threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.requestMap = new WeakHashMap();
        this.clientHeaderMap = new HashMap();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    public void setThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    public void setUserAgent(String str) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), str);
    }

    public void setTimeout(int i) {
        HttpParams params = this.httpClient.getParams();
        ConnManagerParams.setTimeout(params, (long) i);
        HttpConnectionParams.setSoTimeout(params, i);
        HttpConnectionParams.setConnectionTimeout(params, i);
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sSLSocketFactory, 443));
    }

    public void addHeader(String str, String str2) {
        this.clientHeaderMap.put(str, str2);
    }

    public void setBasicAuth(String str, String str2) {
        setBasicAuth(str, str2, AuthScope.ANY);
    }

    public void setBasicAuth(String str, String str2, AuthScope authScope) {
        this.httpClient.getCredentialsProvider().setCredentials(authScope, new UsernamePasswordCredentials(str, str2));
    }

    public void cancelRequests(Context context, boolean z) {
        List<WeakReference> list = (List) this.requestMap.get(context);
        if (list != null) {
            for (WeakReference weakReference : list) {
                Future future = (Future) weakReference.get();
                if (future != null) {
                    future.cancel(z);
                }
            }
        }
        this.requestMap.remove(context);
    }

    public void get(String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        get(null, str, null, asyncHttpResponseHandler);
    }

    public void get(String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        get(null, str, requestParams, asyncHttpResponseHandler);
    }

    public void get(Context context, String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        get(context, str, null, asyncHttpResponseHandler);
    }

    public void get(Context context, String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        sendRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(str, requestParams)), null, asyncHttpResponseHandler, context);
    }

    public void get(Context context, String str, Header[] headerArr, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpGet httpGet = new HttpGet(getUrlWithQueryString(str, requestParams));
        if (headerArr != null) {
            httpGet.setHeaders(headerArr);
        }
        sendRequest(this.httpClient, this.httpContext, httpGet, null, asyncHttpResponseHandler, context);
    }

    public void post(String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        post(null, str, null, asyncHttpResponseHandler);
    }

    public void post(String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        post(null, str, requestParams, asyncHttpResponseHandler);
    }

    public void post(Context context, String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        post(context, str, paramsToEntity(requestParams), null, asyncHttpResponseHandler);
    }

    public void post(Context context, String str, HttpEntity httpEntity, String str2, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(str), httpEntity), str2, asyncHttpResponseHandler, context);
    }

    public void post(Context context, String str, Header[] headerArr, RequestParams requestParams, String str2, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpPost httpPost = new HttpPost(str);
        if (requestParams != null) {
            httpPost.setEntity(paramsToEntity(requestParams));
        }
        if (headerArr != null) {
            httpPost.setHeaders(headerArr);
        }
        sendRequest(this.httpClient, this.httpContext, httpPost, str2, asyncHttpResponseHandler, context);
    }

    public void put(String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        put(null, str, null, asyncHttpResponseHandler);
    }

    public void put(String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        put(null, str, requestParams, asyncHttpResponseHandler);
    }

    public void put(Context context, String str, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        put(context, str, paramsToEntity(requestParams), null, asyncHttpResponseHandler);
    }

    public void put(Context context, String str, HttpEntity httpEntity, String str2, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPut(str), httpEntity), str2, asyncHttpResponseHandler, context);
    }

    public void put(Context context, String str, Header[] headerArr, HttpEntity httpEntity, String str2, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpEntityEnclosingRequestBase addEntityToRequestBase = addEntityToRequestBase(new HttpPut(str), httpEntity);
        if (headerArr != null) {
            addEntityToRequestBase.setHeaders(headerArr);
        }
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase, str2, asyncHttpResponseHandler, context);
    }

    public void delete(String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        delete(null, str, asyncHttpResponseHandler);
    }

    public void delete(Context context, String str, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        sendRequest(this.httpClient, this.httpContext, new HttpDelete(str), null, asyncHttpResponseHandler, context);
    }

    public void delete(Context context, String str, Header[] headerArr, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        HttpDelete httpDelete = new HttpDelete(str);
        if (headerArr != null) {
            httpDelete.setHeaders(headerArr);
        }
        sendRequest(this.httpClient, this.httpContext, httpDelete, null, asyncHttpResponseHandler, context);
    }

    private void sendRequest(DefaultHttpClient defaultHttpClient, HttpContext httpContext, HttpUriRequest httpUriRequest, String str, AsyncHttpResponseHandler asyncHttpResponseHandler, Context context) {
        if (str != null) {
            httpUriRequest.addHeader("Content-Type", str);
        }
        Future submit = this.threadPool.submit(new AsyncHttpRequest(defaultHttpClient, httpContext, httpUriRequest, asyncHttpResponseHandler));
        if (context != null) {
            List list = (List) this.requestMap.get(context);
            if (list == null) {
                list = new LinkedList();
                this.requestMap.put(context, list);
            }
            list.add(new WeakReference(submit));
        }
    }

    private String getUrlWithQueryString(String str, RequestParams requestParams) {
        if (requestParams == null) {
            return str;
        }
        return str + "?" + requestParams.getParamString();
    }

    private HttpEntity paramsToEntity(RequestParams requestParams) {
        if (requestParams != null) {
            return requestParams.getEntity();
        }
        return null;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, HttpEntity httpEntity) {
        if (httpEntity != null) {
            httpEntityEnclosingRequestBase.setEntity(httpEntity);
        }
        return httpEntityEnclosingRequestBase;
    }
}
package fm.xtube.core;

import com.google.analytics.tracking.android.ModelFields;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainManager {
    public static void loadAllCategories(final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadcategories", new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Category> categories = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String name = json_data.getString("tag");
                        String url = json_data.getString("thumb");
                        String id = json_data.getString("category");
                        Category category = new Category();
                        category.setName(name);
                        category.setUrl(url);
                        category.setId(id);
                        categories.add(category);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(categories);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void loadMovies(String id, final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadbycategory/category/" + id, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Movie> movies = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String pictureUrl = json_data.getString("thumb");
                        String movieUrl = json_data.getString("videofile");
                        String id = json_data.getString("id");
                        String description = json_data.getString(ModelFields.TITLE);
                        Movie movie = new Movie();
                        movie.setDescription(description);
                        movie.setMovieUrl(movieUrl);
                        movie.setPictureUrl(pictureUrl);
                        movie.setId(id);
                        movies.add(movie);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(movies);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void loadHdMovies(String id, final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadhdbycategory/category/" + id, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Movie> movies = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String pictureUrl = json_data.getString("thumb");
                        String movieUrl = json_data.getString("videofile");
                        String id = json_data.getString("id");
                        String description = json_data.getString(ModelFields.TITLE);
                        Movie movie = new Movie();
                        movie.setDescription(description);
                        movie.setMovieUrl(movieUrl);
                        movie.setPictureUrl(pictureUrl);
                        movie.setId(id);
                        movies.add(movie);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(movies);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void checkPaiment(final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/checkpayment/uid/" + Server.getDeviceId(), new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                if (response.indexOf("EXPIRES IN:") >= 0) {
                    Server.setRiggedTime("HD (" + response.replace("EXPIRES IN:", "") + " days left)");
                    callBack.onFinished(Boolean.valueOf(true));
                    return;
                }
                callBack.onFinished(Boolean.valueOf(false));
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(Boolean.valueOf(false));
            }
        });
    }
}
package fm.xtube.core;

public class Server {
    public static String DEVICE_ID;
    public static String RIGGED_TIME;

    public static void setDeviceId(String deviceId) {
        DEVICE_ID = deviceId;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static void setRiggedTime(String time) {
        RIGGED_TIME = time;
    }

    public static String getRiggedTime() {
        return RIGGED_TIME;
    }
}
