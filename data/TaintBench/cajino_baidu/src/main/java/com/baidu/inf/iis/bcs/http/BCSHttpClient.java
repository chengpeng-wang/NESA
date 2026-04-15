package com.baidu.inf.iis.bcs.http;

import com.baidu.inf.iis.bcs.handler.ErrorResponseHandler;
import com.baidu.inf.iis.bcs.handler.HttpResponseHandler;
import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.model.BCSServiceException;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;

public class BCSHttpClient {
    private static final int MAX_BACKOFF_IN_MILLISECONDS = 20000;
    private static final Log log = LogFactory.getLog(BCSHttpClient.class);
    private final ClientConfiguration config;
    private ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();
    private HttpClient httpClient;
    private HttpClientFactory httpClientFactory = new HttpClientFactory();
    private HttpRequestFactory httpRequestFactory = new HttpRequestFactory();

    public BCSHttpClient(ClientConfiguration clientConfiguration) {
        this.config = clientConfiguration;
        this.httpClient = this.httpClientFactory.createHttpClient(this.config);
    }

    private BCSHttpResponse createBCSHttpResponse(HttpResponse httpResponse) throws IllegalStateException, IOException {
        BCSHttpResponse bCSHttpResponse = new BCSHttpResponse();
        if (httpResponse.getEntity() != null) {
            bCSHttpResponse.setContent(httpResponse.getEntity().getContent());
        }
        bCSHttpResponse.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        bCSHttpResponse.setStatusText(httpResponse.getStatusLine().getReasonPhrase());
        for (Header header : httpResponse.getAllHeaders()) {
            bCSHttpResponse.addHeader(header.getName(), header.getValue());
        }
        return bCSHttpResponse;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00e2  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00a2 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0117 A:{SYNTHETIC} */
    public <T> com.baidu.inf.iis.bcs.response.BaiduBCSResponse<T> execute(com.baidu.inf.iis.bcs.http.BCSHttpRequest r10, com.baidu.inf.iis.bcs.handler.HttpResponseHandler<T> r11) {
        /*
        r9 = this;
        r0 = 0;
        r1 = r9.httpRequestFactory;
        r2 = r9.config;
        r4 = r1.createHttpRequestBase(r2, r10);
        r1 = r9.httpClient;
        r1 = r1.getConnectionManager();
        r2 = 30;
        r5 = java.util.concurrent.TimeUnit.SECONDS;
        r1.closeIdleConnections(r2, r5);
        r1 = r0;
    L_0x0017:
        r3 = 0;
        r9.pauseExponentially(r0);	 Catch:{ ClientProtocolException -> 0x014d, IOException -> 0x00f4 }
        r2 = r0 + 1;
        r0 = r9.httpClient;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r3 = r0.execute(r4);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = log;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5 = new java.lang.StringBuilder;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5.<init>();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r6 = "Send Request Finish: ";
        r5 = r5.append(r6);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r6 = r3.getStatusLine();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5 = r5.append(r6);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r6 = ", ";
        r5 = r5.append(r6);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r6 = r4.getURI();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5 = r5.append(r6);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5 = r5.toString();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0.info(r5);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = r9.isRequestSuccessful(r3);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        if (r0 == 0) goto L_0x0069;
    L_0x0053:
        r1 = r11.isNeedsConnectionLeftOpen();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = r9.handleHttpResponse(r10, r3, r11);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        if (r1 != 0) goto L_0x0068;
    L_0x005d:
        r1 = r3.getEntity();	 Catch:{ Throwable -> 0x0150 }
        r1 = r1.getContent();	 Catch:{ Throwable -> 0x0150 }
        r1.close();	 Catch:{ Throwable -> 0x0150 }
    L_0x0068:
        return r0;
    L_0x0069:
        r0 = r9.errorResponseHandler;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = r9.handleErrorHttpResponse(r10, r3, r0);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = r0.getResult();	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r0 = (com.baidu.inf.iis.bcs.model.BCSServiceException) r0;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        r5 = r9.shouldRetry(r0, r2);	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
        if (r5 != 0) goto L_0x00ce;
    L_0x007b:
        throw r0;	 Catch:{ ClientProtocolException -> 0x007c, IOException -> 0x0148 }
    L_0x007c:
        r0 = move-exception;
        r8 = r0;
        r0 = r2;
        r2 = r8;
    L_0x0080:
        r5 = log;	 Catch:{ all -> 0x00bf }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00bf }
        r6.<init>();	 Catch:{ all -> 0x00bf }
        r7 = "Unable to execute HTTP request: ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x00bf }
        r7 = r2.getMessage();	 Catch:{ all -> 0x00bf }
        r6 = r6.append(r7);	 Catch:{ all -> 0x00bf }
        r6 = r6.toString();	 Catch:{ all -> 0x00bf }
        r5.warn(r6);	 Catch:{ all -> 0x00bf }
        r5 = r9.shouldRetry(r2, r0);	 Catch:{ all -> 0x00bf }
        if (r5 != 0) goto L_0x00e2;
    L_0x00a2:
        r0 = new com.baidu.inf.iis.bcs.model.BCSClientException;	 Catch:{ all -> 0x00bf }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00bf }
        r4.<init>();	 Catch:{ all -> 0x00bf }
        r5 = "Send to server failed: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x00bf }
        r5 = r2.getMessage();	 Catch:{ all -> 0x00bf }
        r4 = r4.append(r5);	 Catch:{ all -> 0x00bf }
        r4 = r4.toString();	 Catch:{ all -> 0x00bf }
        r0.m760init(r4, r2);	 Catch:{ all -> 0x00bf }
        throw r0;	 Catch:{ all -> 0x00bf }
    L_0x00bf:
        r0 = move-exception;
        if (r1 != 0) goto L_0x00cd;
    L_0x00c2:
        r1 = r3.getEntity();	 Catch:{ Throwable -> 0x0146 }
        r1 = r1.getContent();	 Catch:{ Throwable -> 0x0146 }
        r1.close();	 Catch:{ Throwable -> 0x0146 }
    L_0x00cd:
        throw r0;
    L_0x00ce:
        if (r1 != 0) goto L_0x0153;
    L_0x00d0:
        r0 = r3.getEntity();	 Catch:{ Throwable -> 0x00de }
        r0 = r0.getContent();	 Catch:{ Throwable -> 0x00de }
        r0.close();	 Catch:{ Throwable -> 0x00de }
        r0 = r2;
        goto L_0x0017;
    L_0x00de:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0017;
    L_0x00e2:
        if (r1 != 0) goto L_0x0017;
    L_0x00e4:
        r2 = r3.getEntity();	 Catch:{ Throwable -> 0x00f1 }
        r2 = r2.getContent();	 Catch:{ Throwable -> 0x00f1 }
        r2.close();	 Catch:{ Throwable -> 0x00f1 }
        goto L_0x0017;
    L_0x00f1:
        r2 = move-exception;
        goto L_0x0017;
    L_0x00f4:
        r2 = move-exception;
    L_0x00f5:
        r5 = log;	 Catch:{ all -> 0x00bf }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00bf }
        r6.<init>();	 Catch:{ all -> 0x00bf }
        r7 = "Unable to execute HTTP request: ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x00bf }
        r7 = r2.getMessage();	 Catch:{ all -> 0x00bf }
        r6 = r6.append(r7);	 Catch:{ all -> 0x00bf }
        r6 = r6.toString();	 Catch:{ all -> 0x00bf }
        r5.warn(r6);	 Catch:{ all -> 0x00bf }
        r5 = r9.shouldRetry(r2, r0);	 Catch:{ all -> 0x00bf }
        if (r5 != 0) goto L_0x0134;
    L_0x0117:
        r0 = new com.baidu.inf.iis.bcs.model.BCSClientException;	 Catch:{ all -> 0x00bf }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00bf }
        r4.<init>();	 Catch:{ all -> 0x00bf }
        r5 = "Send to server failed: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x00bf }
        r5 = r2.getMessage();	 Catch:{ all -> 0x00bf }
        r4 = r4.append(r5);	 Catch:{ all -> 0x00bf }
        r4 = r4.toString();	 Catch:{ all -> 0x00bf }
        r0.m760init(r4, r2);	 Catch:{ all -> 0x00bf }
        throw r0;	 Catch:{ all -> 0x00bf }
    L_0x0134:
        if (r1 != 0) goto L_0x0017;
    L_0x0136:
        r2 = r3.getEntity();	 Catch:{ Throwable -> 0x0143 }
        r2 = r2.getContent();	 Catch:{ Throwable -> 0x0143 }
        r2.close();	 Catch:{ Throwable -> 0x0143 }
        goto L_0x0017;
    L_0x0143:
        r2 = move-exception;
        goto L_0x0017;
    L_0x0146:
        r1 = move-exception;
        goto L_0x00cd;
    L_0x0148:
        r0 = move-exception;
        r8 = r0;
        r0 = r2;
        r2 = r8;
        goto L_0x00f5;
    L_0x014d:
        r2 = move-exception;
        goto L_0x0080;
    L_0x0150:
        r1 = move-exception;
        goto L_0x0068;
    L_0x0153:
        r0 = r2;
        goto L_0x0017;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.inf.iis.bcs.http.BCSHttpClient.execute(com.baidu.inf.iis.bcs.http.BCSHttpRequest, com.baidu.inf.iis.bcs.handler.HttpResponseHandler):com.baidu.inf.iis.bcs.response.BaiduBCSResponse");
    }

    private BaiduBCSResponse<BCSServiceException> handleErrorHttpResponse(BCSHttpRequest bCSHttpRequest, HttpResponse httpResponse, HttpResponseHandler<BCSServiceException> httpResponseHandler) throws IllegalStateException, IOException {
        BCSHttpResponse createBCSHttpResponse = createBCSHttpResponse(httpResponse);
        createBCSHttpResponse.setRequest(bCSHttpRequest);
        return httpResponseHandler.handle(createBCSHttpResponse);
    }

    private <T> BaiduBCSResponse<T> handleHttpResponse(BCSHttpRequest bCSHttpRequest, HttpResponse httpResponse, HttpResponseHandler<T> httpResponseHandler) throws IllegalStateException, IOException {
        BCSHttpResponse createBCSHttpResponse = createBCSHttpResponse(httpResponse);
        createBCSHttpResponse.setRequest(bCSHttpRequest);
        return httpResponseHandler.handle(createBCSHttpResponse);
    }

    private boolean isRequestSuccessful(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode() / 100 == 2;
    }

    public HttpRequestFactory getHttpRequestFactory() {
        return this.httpRequestFactory;
    }

    public void setHttpRequestFactory(HttpRequestFactory httpRequestFactory) {
        this.httpRequestFactory = httpRequestFactory;
    }

    public ClientConfiguration getConfig() {
        return this.config;
    }

    public boolean shouldRetry(Exception exception, int i) {
        if (i > this.config.getMaxErrorRetry()) {
            log.warn("Max error retry is[" + this.config.getMaxErrorRetry() + "]. Stop retry.");
            return false;
        } else if ((exception instanceof NoHttpResponseException) || (exception instanceof SocketException) || (exception instanceof SocketTimeoutException)) {
            log.debug("Retrying on " + exception.getClass().getName() + ": " + exception.getMessage());
            return true;
        } else {
            if (exception instanceof BCSServiceException) {
                BCSServiceException bCSServiceException = (BCSServiceException) exception;
                if (bCSServiceException.getBcsErrorCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR || bCSServiceException.getBcsErrorCode() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                    log.debug("Retrying on server response[" + bCSServiceException.getBcsErrorCode() + "]");
                    return true;
                }
            }
            log.warn("Should not retry.");
            return false;
        }
    }

    private void pauseExponentially(int i) {
        if (i != 0) {
            long min = Math.min((long) (((double) 300) * Math.pow(2.0d, (double) i)), 20000);
            log.debug("Retriable error detected, will retry in " + min + "ms, attempt number: " + i);
            try {
                Thread.sleep(min);
            } catch (InterruptedException e) {
                throw new BCSClientException(e.getMessage(), e);
            }
        }
    }

    public void shutdown() {
        this.httpClient.getConnectionManager().shutdown();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        shutdown();
        super.finalize();
    }
}
