package com.androidquery.auth;

import android.content.Context;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.apache.http.HttpRequest;

public abstract class AccountHandle {
    private LinkedHashSet<AbstractAjaxCallback<?, ?>> callbacks;

    public abstract void auth();

    public abstract boolean authenticated();

    public abstract boolean expired(AbstractAjaxCallback<?, ?> abstractAjaxCallback, AjaxStatus ajaxStatus);

    public abstract boolean reauth(AbstractAjaxCallback<?, ?> abstractAjaxCallback);

    public synchronized void auth(AbstractAjaxCallback<?, ?> cb) {
        if (this.callbacks == null) {
            this.callbacks = new LinkedHashSet();
            this.callbacks.add(cb);
            auth();
        } else {
            this.callbacks.add(cb);
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void success(Context context) {
        if (this.callbacks != null) {
            Iterator it = this.callbacks.iterator();
            while (it.hasNext()) {
                ((AbstractAjaxCallback) it.next()).async(context);
            }
            this.callbacks = null;
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void failure(Context context, int code, String message) {
        if (this.callbacks != null) {
            Iterator it = this.callbacks.iterator();
            while (it.hasNext()) {
                ((AbstractAjaxCallback) it.next()).failure(code, message);
            }
            this.callbacks = null;
        }
    }

    public void applyToken(AbstractAjaxCallback<?, ?> abstractAjaxCallback, HttpRequest request) {
    }

    public void applyToken(AbstractAjaxCallback<?, ?> abstractAjaxCallback, HttpURLConnection conn) {
    }

    public String getNetworkUrl(String url) {
        return url;
    }

    public String getCacheUrl(String url) {
        return url;
    }

    public void unauth() {
    }
}
