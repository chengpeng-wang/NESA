package org.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;

@NotThreadSafe
public class DefaultRequestDirector implements RequestDirector {
    protected final ClientConnectionManager connManager;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    protected ManagedClientConnection managedConn;
    private int maxRedirects;
    protected final HttpParams params;
    protected final AuthenticationHandler proxyAuthHandler;
    protected final AuthState proxyAuthState;
    private int redirectCount;
    protected final RedirectHandler redirectHandler;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    protected final AuthenticationHandler targetAuthHandler;
    protected final AuthState targetAuthState;
    protected final UserTokenHandler userTokenHandler;
    private HttpHost virtualHost;

    DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
        if (log == null) {
            throw new IllegalArgumentException("Log may not be null.");
        } else if (requestExec == null) {
            throw new IllegalArgumentException("Request executor may not be null.");
        } else if (conman == null) {
            throw new IllegalArgumentException("Client connection manager may not be null.");
        } else if (reustrat == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null.");
        } else if (kastrat == null) {
            throw new IllegalArgumentException("Connection keep alive strategy may not be null.");
        } else if (rouplan == null) {
            throw new IllegalArgumentException("Route planner may not be null.");
        } else if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP protocol processor may not be null.");
        } else if (retryHandler == null) {
            throw new IllegalArgumentException("HTTP request retry handler may not be null.");
        } else if (redirectHandler == null) {
            throw new IllegalArgumentException("Redirect handler may not be null.");
        } else if (targetAuthHandler == null) {
            throw new IllegalArgumentException("Target authentication handler may not be null.");
        } else if (proxyAuthHandler == null) {
            throw new IllegalArgumentException("Proxy authentication handler may not be null.");
        } else if (userTokenHandler == null) {
            throw new IllegalArgumentException("User token handler may not be null.");
        } else if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.log = log;
            this.requestExec = requestExec;
            this.connManager = conman;
            this.reuseStrategy = reustrat;
            this.keepAliveStrategy = kastrat;
            this.routePlanner = rouplan;
            this.httpProcessor = httpProcessor;
            this.retryHandler = retryHandler;
            this.redirectHandler = redirectHandler;
            this.targetAuthHandler = targetAuthHandler;
            this.proxyAuthHandler = proxyAuthHandler;
            this.userTokenHandler = userTokenHandler;
            this.params = params;
            this.managedConn = null;
            this.redirectCount = 0;
            this.maxRedirects = this.params.getIntParameter(ClientPNames.MAX_REDIRECTS, 100);
            this.targetAuthState = new AuthState();
            this.proxyAuthState = new AuthState();
        }
    }

    public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
        this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectHandler, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
    }

    private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
        if (request instanceof HttpEntityEnclosingRequest) {
            return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request);
        }
        return new RequestWrapper(request);
    }

    /* access modifiers changed from: protected */
    public void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (route.getProxyHost() == null || route.isTunnelled()) {
                if (uri.isAbsolute()) {
                    request.setURI(URIUtils.rewriteURI(uri, null));
                }
            } else if (!uri.isAbsolute()) {
                request.setURI(URIUtils.rewriteURI(uri, route.getTargetHost()));
            }
        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x01c7 A:{Splitter:B:2:0x0050, ExcHandler: HttpException (r10_2 'ex' org.apache.http.HttpException)} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01f0 A:{Splitter:B:2:0x0050, ExcHandler: RuntimeException (r10_0 'ex' java.lang.RuntimeException)} */
    /* JADX WARNING: Missing block: B:38:0x01c7, code skipped:
            r10 = move-exception;
     */
    /* JADX WARNING: Missing block: B:39:0x01c8, code skipped:
            abortConnection();
     */
    /* JADX WARNING: Missing block: B:40:0x01cb, code skipped:
            throw r10;
     */
    /* JADX WARNING: Missing block: B:50:0x01f0, code skipped:
            r10 = move-exception;
     */
    /* JADX WARNING: Missing block: B:51:0x01f1, code skipped:
            abortConnection();
     */
    /* JADX WARNING: Missing block: B:52:0x01f4, code skipped:
            throw r10;
     */
    public org.apache.http.HttpResponse execute(org.apache.http.HttpHost r33, org.apache.http.HttpRequest r34, org.apache.http.protocol.HttpContext r35) throws org.apache.http.HttpException, java.io.IOException {
        /*
        r32 = this;
        r15 = r34;
        r0 = r32;
        r17 = r0.wrapRequest(r15);
        r0 = r32;
        r0 = r0.params;
        r29 = r0;
        r0 = r17;
        r1 = r29;
        r0.setParams(r1);
        r0 = r32;
        r1 = r33;
        r2 = r17;
        r3 = r35;
        r16 = r0.determineRoute(r1, r2, r3);
        r29 = r15.getParams();
        r30 = "http.virtual-host";
        r29 = r29.getParameter(r30);
        r29 = (org.apache.http.HttpHost) r29;
        r0 = r29;
        r1 = r32;
        r1.virtualHost = r0;
        r23 = new org.apache.http.impl.client.RoutedRequest;
        r0 = r23;
        r1 = r17;
        r2 = r16;
        r0.m233init(r1, r2);
        r0 = r32;
        r0 = r0.params;
        r29 = r0;
        r25 = org.apache.http.conn.params.ConnManagerParams.getTimeout(r29);
        r11 = 0;
        r22 = 0;
        r5 = 0;
        r19 = 0;
    L_0x004e:
        if (r5 != 0) goto L_0x0213;
    L_0x0050:
        r28 = r23.getRequest();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r24 = r23.getRoute();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r19 = 0;
        r29 = "http.user-token";
        r0 = r35;
        r1 = r29;
        r27 = r0.getAttribute(r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        if (r29 != 0) goto L_0x00df;
    L_0x006c:
        r0 = r32;
        r0 = r0.connManager;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r24;
        r2 = r27;
        r4 = r0.requestConnection(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r15 instanceof org.apache.http.client.methods.AbortableHttpRequest;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        if (r29 == 0) goto L_0x008c;
    L_0x0082:
        r0 = r15;
        r0 = (org.apache.http.client.methods.AbortableHttpRequest) r0;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r0.setConnectionRequest(r4);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x008c:
        r29 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x01cc }
        r0 = r25;
        r2 = r29;
        r29 = r4.getConnection(r0, r2);	 Catch:{ InterruptedException -> 0x01cc }
        r0 = r29;
        r1 = r32;
        r1.managedConn = r0;	 Catch:{ InterruptedException -> 0x01cc }
        r0 = r32;
        r0 = r0.params;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = org.apache.http.params.HttpConnectionParams.isStaleCheckingEnabled(r29);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x00df;
    L_0x00a8:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isOpen();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x00df;
    L_0x00b4:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Stale connection check";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isStale();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x00df;
    L_0x00cb:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Stale connection detected";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29.close();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x00df:
        r0 = r15 instanceof org.apache.http.client.methods.AbortableHttpRequest;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        if (r29 == 0) goto L_0x00f3;
    L_0x00e5:
        r0 = r15;
        r0 = (org.apache.http.client.methods.AbortableHttpRequest) r0;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r29.setReleaseTrigger(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x00f3:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isOpen();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 != 0) goto L_0x01db;
    L_0x00ff:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.params;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r29;
        r1 = r24;
        r2 = r35;
        r3 = r30;
        r0.open(r1, r2, r3);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0116:
        r0 = r32;
        r1 = r24;
        r2 = r35;
        r0.establishRoute(r1, r2);	 Catch:{ TunnelRefusedException -> 0x01f5 }
        r28.resetHeaders();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r1 = r28;
        r2 = r24;
        r0.rewriteRequestURI(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.virtualHost;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r33 = r0;
        if (r33 != 0) goto L_0x0137;
    L_0x0133:
        r33 = r24.getTargetHost();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0137:
        r18 = r24.getProxyHost();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.target_host";
        r0 = r35;
        r1 = r29;
        r2 = r33;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.proxy_host";
        r0 = r35;
        r1 = r29;
        r2 = r18;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.connection";
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r35;
        r1 = r29;
        r2 = r30;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.auth.target-scope";
        r0 = r32;
        r0 = r0.targetAuthState;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r35;
        r1 = r29;
        r2 = r30;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.auth.proxy-scope";
        r0 = r32;
        r0 = r0.proxyAuthState;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r35;
        r1 = r29;
        r2 = r30;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.requestExec;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.httpProcessor;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r29;
        r1 = r28;
        r2 = r30;
        r3 = r35;
        r0.preProcess(r1, r2, r3);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r21 = 1;
        r20 = 0;
    L_0x019f:
        if (r21 == 0) goto L_0x0355;
    L_0x01a1:
        r11 = r11 + 1;
        r28.incrementExecCount();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r28.isRepeatable();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 != 0) goto L_0x023c;
    L_0x01ac:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Cannot retry non-repeatable request";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r20 == 0) goto L_0x0234;
    L_0x01b9:
        r29 = new org.apache.http.client.NonRepeatableRequestException;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = "Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.";
        r0 = r29;
        r1 = r30;
        r2 = r20;
        r0.m481init(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        throw r29;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x01c7:
        r10 = move-exception;
        r32.abortConnection();
        throw r10;
    L_0x01cc:
        r13 = move-exception;
        r14 = new java.io.InterruptedIOException;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r14.<init>();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r14.initCause(r13);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        throw r14;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x01d6:
        r10 = move-exception;
        r32.abortConnection();
        throw r10;
    L_0x01db:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.params;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r30 = org.apache.http.params.HttpConnectionParams.getSoTimeout(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29.setSocketTimeout(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x0116;
    L_0x01f0:
        r10 = move-exception;
        r32.abortConnection();
        throw r10;
    L_0x01f5:
        r10 = move-exception;
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isDebugEnabled();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x020f;
    L_0x0202:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = r10.getMessage();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x020f:
        r19 = r10.getResponse();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0213:
        if (r19 == 0) goto L_0x0225;
    L_0x0215:
        r29 = r19.getEntity();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x0225;
    L_0x021b:
        r29 = r19.getEntity();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r29.isStreaming();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 != 0) goto L_0x0463;
    L_0x0225:
        if (r22 == 0) goto L_0x0230;
    L_0x0227:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29.markReusable();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0230:
        r32.releaseConnection();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0233:
        return r19;
    L_0x0234:
        r29 = new org.apache.http.client.NonRepeatableRequestException;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = "Cannot retry request with a non-repeatable request entity.";
        r29.m480init(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        throw r29;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x023c:
        r0 = r32;
        r0 = r0.log;	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isDebugEnabled();	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x026c;
    L_0x0248:
        r0 = r32;
        r0 = r0.log;	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r30.<init>();	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r31 = "Attempt ";
        r30 = r30.append(r31);	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r0 = r30;
        r30 = r0.append(r11);	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r31 = " to execute request";
        r30 = r30.append(r31);	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r30 = r30.toString();	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r29.debug(r30);	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
    L_0x026c:
        r0 = r32;
        r0 = r0.requestExec;	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r29;
        r1 = r28;
        r2 = r30;
        r3 = r35;
        r19 = r0.execute(r1, r2, r3);	 Catch:{ IOException -> 0x0288, HttpException -> 0x01c7, RuntimeException -> 0x01f0 }
        r21 = 0;
        goto L_0x019f;
    L_0x0288:
        r10 = move-exception;
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Closing the connection.";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29.close();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.retryHandler;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = r28.getExecCount();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r29;
        r1 = r30;
        r2 = r35;
        r29 = r0.retryRequest(r10, r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x0345;
    L_0x02b3:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isInfoEnabled();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x02f1;
    L_0x02bf:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = new java.lang.StringBuilder;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30.<init>();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = "I/O exception (";
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = r10.getClass();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = r31.getName();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = ") caught when processing request: ";
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = r10.getMessage();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r30.toString();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29.info(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x02f1:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isDebugEnabled();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x030e;
    L_0x02fd:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = r10.getMessage();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r29;
        r1 = r30;
        r0.debug(r1, r10);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x030e:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Retrying request";
        r29.info(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r20 = r10;
        r29 = r24.isTunnelled();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 != 0) goto L_0x0346;
    L_0x0321:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Reopening the direct connection.";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.params;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r29;
        r1 = r24;
        r2 = r35;
        r3 = r30;
        r0.open(r1, r2, r3);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x019f;
    L_0x0345:
        throw r10;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0346:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Proxied connection. Need to start over.";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r21 = 0;
        goto L_0x019f;
    L_0x0355:
        if (r19 == 0) goto L_0x004e;
    L_0x0357:
        r0 = r32;
        r0 = r0.params;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r19;
        r1 = r29;
        r0.setParams(r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.requestExec;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r32;
        r0 = r0.httpProcessor;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r0;
        r0 = r29;
        r1 = r19;
        r2 = r30;
        r3 = r35;
        r0.postProcess(r1, r2, r3);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.reuseStrategy;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r19;
        r2 = r35;
        r22 = r0.keepAlive(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r22 == 0) goto L_0x03e2;
    L_0x038d:
        r0 = r32;
        r0 = r0.keepAliveStrategy;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r19;
        r2 = r35;
        r6 = r0.getKeepAliveDuration(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r29;
        r1 = r30;
        r0.setIdleDuration(r6, r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29 = r29.isDebugEnabled();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 == 0) goto L_0x03e2;
    L_0x03b8:
        r29 = 0;
        r29 = (r6 > r29 ? 1 : (r6 == r29 ? 0 : -1));
        if (r29 < 0) goto L_0x0425;
    L_0x03be:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = new java.lang.StringBuilder;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30.<init>();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = "Connection can be kept alive for ";
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r30;
        r30 = r0.append(r6);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r31 = " ms";
        r30 = r30.append(r31);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r30.toString();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x03e2:
        r0 = r32;
        r1 = r23;
        r2 = r19;
        r3 = r35;
        r12 = r0.handleResponse(r1, r2, r3);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r12 != 0) goto L_0x0431;
    L_0x03f0:
        r5 = 1;
    L_0x03f1:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        if (r29 == 0) goto L_0x004e;
    L_0x03f9:
        if (r27 != 0) goto L_0x004e;
    L_0x03fb:
        r0 = r32;
        r0 = r0.userTokenHandler;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r35;
        r27 = r0.getUserToken(r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = "http.user-token";
        r0 = r35;
        r1 = r29;
        r2 = r27;
        r0.setAttribute(r1, r2);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r27 == 0) goto L_0x004e;
    L_0x0416:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r27;
        r0.setState(r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x004e;
    L_0x0425:
        r0 = r32;
        r0 = r0.log;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r30 = "Connection can be kept alive indefinitely";
        r29.debug(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x03e2;
    L_0x0431:
        if (r22 == 0) goto L_0x0459;
    L_0x0433:
        r8 = r19.getEntity();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r8 == 0) goto L_0x043c;
    L_0x0439:
        r8.consumeContent();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x043c:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29.markReusable();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0445:
        r29 = r12.getRoute();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r30 = r23.getRoute();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r29.equals(r30);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        if (r29 != 0) goto L_0x0456;
    L_0x0453:
        r32.releaseConnection();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
    L_0x0456:
        r23 = r12;
        goto L_0x03f1;
    L_0x0459:
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r29.close();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x0445;
    L_0x0463:
        r8 = r19.getEntity();	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r9 = new org.apache.http.conn.BasicManagedEntity;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r32;
        r0 = r0.managedConn;	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r29 = r0;
        r0 = r29;
        r1 = r22;
        r9.m485init(r8, r0, r1);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        r0 = r19;
        r0.setEntity(r9);	 Catch:{ HttpException -> 0x01c7, IOException -> 0x01d6, RuntimeException -> 0x01f0 }
        goto L_0x0233;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.client.DefaultRequestDirector.execute(org.apache.http.HttpHost, org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext):org.apache.http.HttpResponse");
    }

    /* access modifiers changed from: protected */
    public void releaseConnection() {
        try {
            this.managedConn.releaseConnection();
        } catch (IOException ignored) {
            this.log.debug("IOException releasing connection", ignored);
        }
        this.managedConn = null;
    }

    /* access modifiers changed from: protected */
    public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        if (target == null) {
            target = (HttpHost) request.getParams().getParameter(ClientPNames.DEFAULT_HOST);
        }
        if (target != null) {
            return this.routePlanner.determineRoute(target, request, context);
        }
        throw new IllegalStateException("Target host must not be null, or set in parameters.");
    }

    /* access modifiers changed from: protected */
    public void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
        HttpRouteDirector rowdy = new BasicRouteDirector();
        int step;
        do {
            HttpRoute fact = this.managedConn.getRoute();
            step = rowdy.nextStep(route, fact);
            boolean secure;
            switch (step) {
                case -1:
                    throw new IllegalStateException("Unable to establish route.\nplanned = " + route + "\ncurrent = " + fact);
                case 0:
                    break;
                case 1:
                case 2:
                    this.managedConn.open(route, context, this.params);
                    continue;
                case 3:
                    secure = createTunnelToTarget(route, context);
                    this.log.debug("Tunnel to target created.");
                    this.managedConn.tunnelTarget(secure, this.params);
                    continue;
                case 4:
                    int hop = fact.getHopCount() - 1;
                    secure = createTunnelToProxy(route, hop, context);
                    this.log.debug("Tunnel to proxy created.");
                    this.managedConn.tunnelProxy(route.getHopTarget(hop), secure, this.params);
                    continue;
                case 5:
                    this.managedConn.layerProtocol(context, this.params);
                    continue;
                default:
                    throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
            }
        } while (step > 0);
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
        HttpEntity entity;
        HttpHost proxy = route.getProxyHost();
        HttpHost target = route.getTargetHost();
        HttpResponse response = null;
        boolean done = false;
        while (!done) {
            done = true;
            if (!this.managedConn.isOpen()) {
                this.managedConn.open(route, context, this.params);
            }
            HttpRequest connect = createConnectRequest(route, context);
            connect.setParams(this.params);
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
            context.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxy);
            context.setAttribute(ExecutionContext.HTTP_CONNECTION, this.managedConn);
            context.setAttribute(ClientContext.TARGET_AUTH_STATE, this.targetAuthState);
            context.setAttribute(ClientContext.PROXY_AUTH_STATE, this.proxyAuthState);
            context.setAttribute(ExecutionContext.HTTP_REQUEST, connect);
            this.requestExec.preProcess(connect, this.httpProcessor, context);
            response = this.requestExec.execute(connect, this.managedConn, context);
            response.setParams(this.params);
            this.requestExec.postProcess(response, this.httpProcessor, context);
            if (response.getStatusLine().getStatusCode() < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
            }
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credsProvider != null && HttpClientParams.isAuthenticating(this.params)) {
                if (this.proxyAuthHandler.isAuthenticationRequested(response, context)) {
                    this.log.debug("Proxy requested authentication");
                    try {
                        processChallenges(this.proxyAuthHandler.getChallenges(response, context), this.proxyAuthState, this.proxyAuthHandler, response, context);
                    } catch (AuthenticationException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex.getMessage());
                            break;
                        }
                    }
                    updateAuthState(this.proxyAuthState, proxy, credsProvider);
                    if (this.proxyAuthState.getCredentials() != null) {
                        done = false;
                        if (this.reuseStrategy.keepAlive(response, context)) {
                            this.log.debug("Connection kept alive");
                            entity = response.getEntity();
                            if (entity != null) {
                                entity.consumeContent();
                            }
                        } else {
                            this.managedConn.close();
                        }
                    }
                } else {
                    this.proxyAuthState.setAuthScope(null);
                }
            }
        }
        if (response.getStatusLine().getStatusCode() > 299) {
            entity = response.getEntity();
            if (entity != null) {
                response.setEntity(new BufferedHttpEntity(entity));
            }
            this.managedConn.close();
            throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
        }
        this.managedConn.markReusable();
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
        throw new UnsupportedOperationException("Proxy chains are not supported.");
    }

    /* access modifiers changed from: protected */
    public HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
        HttpHost target = route.getTargetHost();
        String host = target.getHostName();
        int port = target.getPort();
        if (port < 0) {
            port = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName()).getDefaultPort();
        }
        StringBuilder buffer = new StringBuilder(host.length() + 6);
        buffer.append(host);
        buffer.append(':');
        buffer.append(Integer.toString(port));
        return new BasicHttpRequest("CONNECT", buffer.toString(), HttpProtocolParams.getVersion(this.params));
    }

    /* access modifiers changed from: protected */
    public RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
        HttpRoute route = roureq.getRoute();
        RequestWrapper request = roureq.getRequest();
        HttpParams params = request.getParams();
        if (!HttpClientParams.isRedirecting(params) || !this.redirectHandler.isRedirectRequested(response, context)) {
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credsProvider != null && HttpClientParams.isAuthenticating(params)) {
                if (this.targetAuthHandler.isAuthenticationRequested(response, context)) {
                    HttpHost target = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                    if (target == null) {
                        target = route.getTargetHost();
                    }
                    this.log.debug("Target requested authentication");
                    try {
                        processChallenges(this.targetAuthHandler.getChallenges(response, context), this.targetAuthState, this.targetAuthHandler, response, context);
                    } catch (AuthenticationException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex.getMessage());
                            return null;
                        }
                    }
                    updateAuthState(this.targetAuthState, target, credsProvider);
                    if (this.targetAuthState.getCredentials() == null) {
                        return null;
                    }
                    return roureq;
                }
                this.targetAuthState.setAuthScope(null);
                if (this.proxyAuthHandler.isAuthenticationRequested(response, context)) {
                    HttpHost proxy = route.getProxyHost();
                    this.log.debug("Proxy requested authentication");
                    try {
                        processChallenges(this.proxyAuthHandler.getChallenges(response, context), this.proxyAuthState, this.proxyAuthHandler, response, context);
                    } catch (AuthenticationException ex2) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex2.getMessage());
                            return null;
                        }
                    }
                    updateAuthState(this.proxyAuthState, proxy, credsProvider);
                    if (this.proxyAuthState.getCredentials() == null) {
                        return null;
                    }
                    return roureq;
                }
                this.proxyAuthState.setAuthScope(null);
            }
            return null;
        } else if (this.redirectCount >= this.maxRedirects) {
            throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
        } else {
            this.redirectCount++;
            this.virtualHost = null;
            URI uri = this.redirectHandler.getLocationURI(response, context);
            HttpHost newTarget = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            this.targetAuthState.setAuthScope(null);
            this.proxyAuthState.setAuthScope(null);
            if (!route.getTargetHost().equals(newTarget)) {
                this.targetAuthState.invalidate();
                AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
                if (authScheme != null && authScheme.isConnectionBased()) {
                    this.proxyAuthState.invalidate();
                }
            }
            HttpRedirect httpRedirect = new HttpRedirect(request.getMethod(), uri);
            httpRedirect.setHeaders(request.getOriginal().getAllHeaders());
            RequestWrapper requestWrapper = new RequestWrapper(httpRedirect);
            requestWrapper.setParams(params);
            HttpRoute newRoute = determineRoute(newTarget, requestWrapper, context);
            RoutedRequest newRequest = new RoutedRequest(requestWrapper, newRoute);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Redirecting to '" + uri + "' via " + newRoute);
            }
            return newRequest;
        }
    }

    private void abortConnection() {
        ManagedClientConnection mcc = this.managedConn;
        if (mcc != null) {
            this.managedConn = null;
            try {
                mcc.abortConnection();
            } catch (IOException ex) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(ex.getMessage(), ex);
                }
            }
            try {
                mcc.releaseConnection();
            } catch (IOException ignored) {
                this.log.debug("Error releasing connection", ignored);
            }
        }
    }

    private void processChallenges(Map<String, Header> challenges, AuthState authState, AuthenticationHandler authHandler, HttpResponse response, HttpContext context) throws MalformedChallengeException, AuthenticationException {
        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null) {
            authScheme = authHandler.selectScheme(challenges, response, context);
            authState.setAuthScheme(authScheme);
        }
        String id = authScheme.getSchemeName();
        Header challenge = (Header) challenges.get(id.toLowerCase(Locale.ENGLISH));
        if (challenge == null) {
            throw new AuthenticationException(id + " authorization challenge expected, but not found");
        }
        authScheme.processChallenge(challenge);
        this.log.debug("Authorization challenge processed");
    }

    private void updateAuthState(AuthState authState, HttpHost host, CredentialsProvider credsProvider) {
        if (authState.isValid()) {
            String hostname = host.getHostName();
            int port = host.getPort();
            if (port < 0) {
                port = this.connManager.getSchemeRegistry().getScheme(host).getDefaultPort();
            }
            AuthScheme authScheme = authState.getAuthScheme();
            AuthScope authScope = new AuthScope(hostname, port, authScheme.getRealm(), authScheme.getSchemeName());
            if (this.log.isDebugEnabled()) {
                this.log.debug("Authentication scope: " + authScope);
            }
            Credentials creds = authState.getCredentials();
            if (creds == null) {
                creds = credsProvider.getCredentials(authScope);
                if (this.log.isDebugEnabled()) {
                    if (creds != null) {
                        this.log.debug("Found credentials");
                    } else {
                        this.log.debug("Credentials not found");
                    }
                }
            } else if (authScheme.isComplete()) {
                this.log.debug("Authentication failed");
                creds = null;
            }
            authState.setAuthScope(authScope);
            authState.setCredentials(creds);
        }
    }
}
