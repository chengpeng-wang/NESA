package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;
import com.mopub.mobileads.BaseVideoPlayerActivity;

public class b {
    public static a a(Context context, Uri uri) {
        String authority = uri.getAuthority();
        return "store".equals(authority) ? uri.getQueryParameter(BaseVideoPlayerActivity.VIDEO_URL) != null ? null : new c(context, uri) : "open_link".equals(authority) ? new d(context, uri) : null;
    }
}
