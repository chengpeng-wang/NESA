package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.internal.util.b.a;
import com.facebook.ads.internal.util.g;
import com.moceanmobile.mast.MASTNativeAdConstants;

public class d extends a {
    private static final String a = d.class.getSimpleName();
    private final Context b;
    private final Uri c;

    public d(Context context, Uri uri) {
        this.b = context;
        this.c = uri;
    }

    public a a() {
        return a.OPEN_LINK;
    }

    public void b() {
        a(this.b, this.c);
        try {
            g.a(this.b, Uri.parse(this.c.getQueryParameter(MASTNativeAdConstants.RESPONSE_LINK)));
        } catch (Exception e) {
            Log.d(a, "Failed to open link url: " + this.c.toString(), e);
        }
    }
}
