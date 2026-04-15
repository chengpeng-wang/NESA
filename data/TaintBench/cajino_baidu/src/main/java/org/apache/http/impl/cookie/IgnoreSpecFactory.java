package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.params.HttpParams;

@Immutable
public class IgnoreSpecFactory implements CookieSpecFactory {
    public CookieSpec newInstance(HttpParams params) {
        return new IgnoreSpec();
    }
}
