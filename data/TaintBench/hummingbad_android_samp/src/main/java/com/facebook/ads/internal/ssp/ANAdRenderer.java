package com.facebook.ads.internal.ssp;

import android.content.Context;
import android.view.View;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.adapters.k;
import com.facebook.ads.internal.d;
import com.facebook.ads.internal.e;
import com.facebook.ads.internal.view.c;
import org.json.JSONObject;

public class ANAdRenderer {

    public interface Listener {
        void onAdClick();

        void onAdClose();

        void onAdError(Throwable th);

        void onAdImpression();
    }

    public static String getSupportedCapabilities() {
        return d.c();
    }

    public static int getTemplateID(int i, int i2) {
        AdSize fromWidthAndHeight = AdSize.fromWidthAndHeight(i, i2);
        if (fromWidthAndHeight == null) {
            return e.UNKNOWN.a();
        }
        switch (fromWidthAndHeight) {
            case INTERSTITIAL:
                return e.WEBVIEW_INTERSTITIAL_UNKNOWN.a();
            case RECTANGLE_HEIGHT_250:
                return e.WEBVIEW_BANNER_250.a();
            case BANNER_HEIGHT_90:
                return e.WEBVIEW_BANNER_90.a();
            case BANNER_HEIGHT_50:
                return e.WEBVIEW_BANNER_50.a();
            default:
                return e.WEBVIEW_BANNER_LEGACY.a();
        }
    }

    public static View renderAd(Context context, JSONObject jSONObject, int i, int i2, int i3, Listener listener) {
        try {
            return new c(context, k.a(jSONObject), i3, listener);
        } catch (Throwable th) {
            listener.onAdError(th);
            return null;
        }
    }
}
