package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RequestAuthCache implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            AuthCache authCache = (AuthCache) context.getAttribute(ClientContext.AUTH_CACHE);
            if (authCache == null) {
                this.log.debug("Auth cache not set in the context");
                return;
            }
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credsProvider == null) {
                this.log.debug("Credentials provider not set in the context");
                return;
            }
            AuthScheme authScheme;
            HttpHost target = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            AuthState targetState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (!(target == null || targetState == null || targetState.getAuthScheme() != null)) {
                authScheme = authCache.get(target);
                if (authScheme != null) {
                    doPreemptiveAuth(target, authScheme, targetState, credsProvider);
                }
            }
            HttpHost proxy = (HttpHost) context.getAttribute(ExecutionContext.HTTP_PROXY_HOST);
            AuthState proxyState = (AuthState) context.getAttribute(ClientContext.PROXY_AUTH_STATE);
            if (proxy != null && proxyState != null && proxyState.getAuthScheme() == null) {
                authScheme = authCache.get(proxy);
                if (authScheme != null) {
                    doPreemptiveAuth(proxy, authScheme, proxyState, credsProvider);
                }
            }
        }
    }

    private void doPreemptiveAuth(HttpHost host, AuthScheme authScheme, AuthState authState, CredentialsProvider credsProvider) {
        String schemeName = authScheme.getSchemeName();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Re-using cached '" + schemeName + "' auth scheme for " + host);
        }
        Credentials creds = credsProvider.getCredentials(new AuthScope(host.getHostName(), host.getPort(), AuthScope.ANY_REALM, schemeName));
        if (creds != null) {
            authState.setAuthScheme(authScheme);
            authState.setCredentials(creds);
            return;
        }
        this.log.debug("No credentials for preemptive authentication");
    }
}
