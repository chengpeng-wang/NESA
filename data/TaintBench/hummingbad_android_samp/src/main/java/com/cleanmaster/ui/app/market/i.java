package com.cleanmaster.ui.app.market;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.RedirectHandler;
import org.apache.http.protocol.HttpContext;

/* compiled from: ParseUrlUtils */
class i implements RedirectHandler {
    final /* synthetic */ h a;

    i(h hVar) {
        this.a = hVar;
    }

    public URI getLocationURI(HttpResponse response, HttpContext context) {
        return null;
    }

    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
        return false;
    }
}
