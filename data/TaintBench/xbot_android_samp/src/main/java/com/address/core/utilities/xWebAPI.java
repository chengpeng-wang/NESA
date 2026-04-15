package com.address.core.utilities;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.widget.Toast;

public class xWebAPI {
    private Activity _activity = null;
    private Context _ctx = null;
    private WebView _web = null;

    public xWebAPI(Context ctx, WebView web, Activity activity) {
        this._ctx = ctx;
        this._web = web;
        this._activity = activity;
    }

    public void showToast(String msg) {
        Toast.makeText(this._ctx, msg, 1).show();
    }

    public void setTitle(String title) {
        this._activity.setTitle(title);
    }

    public void finish() {
        this._activity.finish();
    }
}
