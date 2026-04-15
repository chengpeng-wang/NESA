package com.mb.bdapp.down;

import android.os.SystemClock;
import com.mb.bdapp.util.LogUtil;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;

public class RetryHandler implements HttpRequestRetryHandler {
    private static final int RETRY_SLEEP_INTERVAL = 1000;
    private static final String TAG = RetryHandler.class.getSimpleName();
    private static HashSet<Class<?>> exceptionBlackList = new HashSet();
    private static HashSet<Class<?>> exceptionWhiteList = new HashSet();
    private final int maxRetries;

    static {
        exceptionWhiteList.add(NoHttpResponseException.class);
        exceptionWhiteList.add(UnknownHostException.class);
        exceptionWhiteList.add(SocketException.class);
        exceptionBlackList.add(InterruptedIOException.class);
        exceptionBlackList.add(SSLHandshakeException.class);
    }

    public RetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean retryRequest(IOException exception, int retriedTimes, HttpContext context) {
        boolean sent = false;
        boolean retry = true;
        if (exception == null || context == null) {
            return false;
        }
        Object isReqSent = context.getAttribute("http.request_sent");
        if (isReqSent != null) {
            sent = ((Boolean) isReqSent).booleanValue();
        }
        LogUtil.d(TAG, "retriedTimes=" + retriedTimes);
        if (retriedTimes > this.maxRetries) {
            retry = false;
        } else if (exceptionBlackList.contains(exception.getClass())) {
            retry = false;
        } else if (exceptionWhiteList.contains(exception.getClass())) {
            retry = true;
        } else if (!sent) {
            retry = true;
        }
        if (retry) {
            try {
                Object currRequest = context.getAttribute("http.request");
                if (currRequest == null) {
                    retry = false;
                    LogUtil.d(TAG, "retry error, curr request is null");
                } else if (currRequest instanceof HttpRequestBase) {
                    retry = "GET".equals(((HttpRequestBase) currRequest).getMethod());
                } else if (currRequest instanceof RequestWrapper) {
                    retry = "GET".equals(((RequestWrapper) currRequest).getMethod());
                }
            } catch (Throwable th) {
                retry = false;
                LogUtil.e(TAG, "retry error");
            }
        }
        if (retry) {
            SystemClock.sleep(1000);
        }
        return retry;
    }

    public static boolean retryRequest(IOException exception) {
        if (exceptionBlackList.contains(exception.getClass())) {
            return false;
        }
        if (exceptionWhiteList.contains(exception.getClass())) {
            return true;
        }
        return true;
    }
}
