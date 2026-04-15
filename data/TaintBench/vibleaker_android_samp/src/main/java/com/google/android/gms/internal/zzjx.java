package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;

@TargetApi(14)
@zzhb
public final class zzjx extends zzjv {
    public zzjx(zzjp zzjp) {
        super(zzjp);
    }

    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback customViewCallback) {
        zza(view, requestedOrientation, customViewCallback);
    }
}
