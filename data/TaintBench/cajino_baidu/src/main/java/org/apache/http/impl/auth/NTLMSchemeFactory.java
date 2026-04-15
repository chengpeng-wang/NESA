package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

@Immutable
public class NTLMSchemeFactory implements AuthSchemeFactory {
    public AuthScheme newInstance(HttpParams params) {
        return new NTLMScheme(new NTLMEngineImpl());
    }
}
