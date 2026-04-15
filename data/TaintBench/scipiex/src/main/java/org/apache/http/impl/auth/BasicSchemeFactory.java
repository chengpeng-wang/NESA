package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

@Immutable
public class BasicSchemeFactory implements AuthSchemeFactory {
    public AuthScheme newInstance(HttpParams params) {
        return new BasicScheme();
    }
}
