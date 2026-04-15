package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RequestTargetAuthentication implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else if (!request.getRequestLine().getMethod().equalsIgnoreCase("CONNECT") && !request.containsHeader("Authorization")) {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (authState == null) {
                this.log.debug("Target auth state not set in the context");
                return;
            }
            AuthScheme authScheme = authState.getAuthScheme();
            if (authScheme != null) {
                Credentials creds = authState.getCredentials();
                if (creds == null) {
                    this.log.debug("User credentials not available");
                } else if (authState.getAuthScope() != null || !authScheme.isConnectionBased()) {
                    try {
                        Header header;
                        if (authScheme instanceof ContextAwareAuthScheme) {
                            header = ((ContextAwareAuthScheme) authScheme).authenticate(creds, request, context);
                        } else {
                            header = authScheme.authenticate(creds, request);
                        }
                        request.addHeader(header);
                    } catch (AuthenticationException ex) {
                        if (this.log.isErrorEnabled()) {
                            this.log.error("Authentication error: " + ex.getMessage());
                        }
                    }
                }
            }
        }
    }
}
