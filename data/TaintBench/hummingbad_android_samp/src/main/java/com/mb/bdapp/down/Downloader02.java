package com.mb.bdapp.down;

import android.os.SystemClock;
import com.mb.bdapp.down.callback.FileDownloadHandler;
import com.mb.bdapp.down.callback.RequestCallBack;
import com.mb.bdapp.down.callback.RequestCallBackHandler;
import com.mb.bdapp.entity.GZipDecompressingEntity;
import com.mopub.common.Constants;
import com.picksinit.ErrorInfo;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Downloader02 implements Runnable, RequestCallBackHandler {
    private static final int DEFAULT_CONN_TIMEOUT = 30000;
    private static final int DEFAULT_RETRY_TIMES = 3;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String TAG = Downloader02.class.getSimpleName();
    private static final int UPDATE_FAILURE = 3;
    private static final int UPDATE_LOADING = 2;
    private static final int UPDATE_START = 1;
    private static final int UPDATE_SUCCESS = 4;
    private String downUrl;
    private String fileSavePath;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext = new BasicHttpContext();
    private long lastUpdateTime;
    private RequestCallBack mRequestCallBack;
    private HttpRequestBase request;
    private int retriedCount = 0;
    private State state = State.WAITING;

    public enum State {
        WAITING(0),
        STARTED(1),
        LOADING(2),
        FAILURE(3),
        CANCELLED(4),
        SUCCESS(5);
        
        private int value;

        private State(int value) {
            this.value = 0;
            this.value = value;
        }

        public static State valueOf(int value) {
            switch (value) {
                case 0:
                    return WAITING;
                case 1:
                    return STARTED;
                case 2:
                    return LOADING;
                case 3:
                    return FAILURE;
                case 4:
                    return CANCELLED;
                case 5:
                    return SUCCESS;
                default:
                    return FAILURE;
            }
        }

        public int value() {
            return this.value;
        }
    }

    public Downloader02(String url, String path, RequestCallBack requestCallBack) {
        this.mRequestCallBack = requestCallBack;
        if (url == null || path == null) {
            throw new IllegalArgumentException("downUrl error!downUrl=" + url + ",path=" + path);
        }
        this.downUrl = url;
        this.fileSavePath = path;
        this.httpClient = createHttpClient();
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public State getState() {
        return this.state;
    }

    /* access modifiers changed from: protected */
    public DefaultHttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 30000);
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpProtocolParams.setUserAgent(params, OtherUtils.getUserAgent(null));
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(params, 10);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(Constants.HTTP, PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme(Constants.HTTPS, DefaultSSLSocketFactory.getSocketFactory(), 443));
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
        httpClient.setHttpRequestRetryHandler(new RetryHandler(3));
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                if (!httpRequest.containsHeader(Downloader02.HEADER_ACCEPT_ENCODING)) {
                    httpRequest.addHeader(Downloader02.HEADER_ACCEPT_ENCODING, Downloader02.ENCODING_GZIP);
                }
            }
        });
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header encoding = entity.getContentEncoding();
                    if (encoding != null) {
                        for (HeaderElement element : encoding.getElements()) {
                            if (element.getName().equalsIgnoreCase(Downloader02.ENCODING_GZIP)) {
                                response.setEntity(new GZipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }
            }
        });
        return httpClient;
    }

    public void run() {
        try {
            onProgressUpdate(Integer.valueOf(1));
            this.lastUpdateTime = SystemClock.uptimeMillis();
            this.request = new HttpGet(this.downUrl);
            if (sendRequest(this.request) != null) {
                onProgressUpdate(Integer.valueOf(4), sendRequest(this.request));
            }
        } catch (DownloadException e) {
            onProgressUpdate(Integer.valueOf(3), e, e.getMessage());
        }
    }

    private File sendRequest(HttpRequestBase request) throws DownloadException {
        Throwable exception;
        int i;
        HttpRequestRetryHandler retryHandler = this.httpClient.getHttpRequestRetryHandler();
        boolean retry;
        do {
            File downloadFile = new File(this.fileSavePath);
            long fileLen = 0;
            if (downloadFile.isFile() && downloadFile.exists()) {
                fileLen = downloadFile.length();
            }
            if (fileLen >= 0) {
                request.setHeader("RANGE", "bytes=" + fileLen + "-");
            }
            request.setHeader(HEADER_ACCEPT_ENCODING, "identity");
            try {
                File resultFile = handleResponse(this.httpClient.execute(request, this.httpContext));
                if (resultFile != null) {
                    return resultFile;
                }
                throw new NullPointerException("resultFile = null");
            } catch (UnknownHostException e) {
                exception = e;
                i = this.retriedCount + 1;
                this.retriedCount = i;
                retry = retryHandler.retryRequest(exception, i, this.httpContext);
                continue;
            } catch (IOException e2) {
                exception = e2;
                i = this.retriedCount + 1;
                this.retriedCount = i;
                retry = retryHandler.retryRequest(exception, i, this.httpContext);
                continue;
            } catch (NullPointerException e3) {
                exception = new IOException(e3.getMessage());
                exception.initCause(e3);
                i = this.retriedCount + 1;
                this.retriedCount = i;
                retry = retryHandler.retryRequest(exception, i, this.httpContext);
                continue;
            } catch (DownloadException e4) {
                throw e4;
            } catch (Throwable e22) {
                exception = new IOException(e22.getMessage());
                exception.initCause(e22);
                i = this.retriedCount + 1;
                this.retriedCount = i;
                retry = retryHandler.retryRequest(exception, i, this.httpContext);
                continue;
            }
        } while (retry);
        throw new DownloadException(exception);
    }

    private File handleResponse(HttpResponse response) throws DownloadException, IOException {
        if (response == null) {
            throw new DownloadException("response is null");
        }
        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode < ErrorInfo.ERROR_CODE_NO_NETWORK) {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            boolean autoResume = OtherUtils.isSupportRange(response) && OtherUtils.isSupportRange(response);
            return new FileDownloadHandler().handleEntity(entity, this, this.fileSavePath, autoResume, null);
        } else if (statusCode == 301 || statusCode == 302) {
            HttpRequestBase request = getDirectRequest(response);
            if (request != null) {
                return sendRequest(request);
            }
            return null;
        } else if (statusCode == 416) {
            throw new DownloadException(statusCode, "maybe the file has downloaded completely");
        } else {
            throw new DownloadException(statusCode, status.getReasonPhrase());
        }
    }

    public HttpRequestBase getDirectRequest(HttpResponse response) {
        if (!response.containsHeader("Location")) {
            return null;
        }
        HttpGet request = new HttpGet(response.getFirstHeader("Location").getValue());
        if (!response.containsHeader("Set-Cookie")) {
            return request;
        }
        request.addHeader("Cookie", response.getFirstHeader("Set-Cookie").getValue());
        return request;
    }

    public boolean updateProgress(long total, long current, boolean forceUpdateUI) {
        if (!(this.mRequestCallBack == null || this.state == State.CANCELLED)) {
            if (forceUpdateUI) {
                onProgressUpdate(Integer.valueOf(2), Long.valueOf(total), Long.valueOf(current));
            } else {
                long currTime = SystemClock.uptimeMillis();
                if (currTime - this.lastUpdateTime >= ((long) this.mRequestCallBack.getRate())) {
                    this.lastUpdateTime = currTime;
                    onProgressUpdate(Integer.valueOf(2), Long.valueOf(total), Long.valueOf(current));
                }
            }
        }
        if (this.state != State.CANCELLED) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected|varargs */
    public void onProgressUpdate(Object... values) {
        if (this.state != State.CANCELLED && values != null && values.length != 0 && this.mRequestCallBack != null) {
            switch (((Integer) values[0]).intValue()) {
                case 1:
                    this.state = State.STARTED;
                    this.mRequestCallBack.onStart();
                    return;
                case 2:
                    if (values.length == 3) {
                        this.state = State.LOADING;
                        this.mRequestCallBack.onLoading(Long.valueOf(String.valueOf(values[1])).longValue(), Long.valueOf(String.valueOf(values[2])).longValue());
                        return;
                    }
                    return;
                case 3:
                    if (values.length == 3) {
                        this.state = State.FAILURE;
                        this.mRequestCallBack.onFailure((DownloadException) values[1], (String) values[2]);
                        return;
                    }
                    return;
                case 4:
                    if (values.length == 2) {
                        this.state = State.SUCCESS;
                        this.mRequestCallBack.onSuccess((File) values[1]);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void cancel() {
        this.state = State.CANCELLED;
        if (!(this.request == null || this.request.isAborted())) {
            try {
                this.request.abort();
            } catch (Throwable th) {
            }
        }
        if (this.mRequestCallBack != null) {
            this.mRequestCallBack.onCancelled();
        }
    }
}
