package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
import android.webkit.WebView;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.InterstitialAdActivity.Type;
import com.facebook.ads.internal.util.f;
import com.facebook.ads.internal.util.h;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class i extends InterstitialAdapter {
    private final String a = UUID.randomUUID().toString();
    private Context b;
    private c c;
    private InterstitialAdapterListener d;
    private boolean e = false;
    private k f;
    private a g;
    private WebView h;

    public enum a {
        VERTICAL,
        HORIZONTAL;

        public static a a(int i) {
            return i == 2 ? HORIZONTAL : VERTICAL;
        }
    }

    private int a() {
        int rotation = ((WindowManager) this.b.getSystemService("window")).getDefaultDisplay().getRotation();
        if (this.g == a.HORIZONTAL) {
            switch (rotation) {
                case 2:
                case 3:
                    return 8;
                default:
                    return 0;
            }
        }
        switch (rotation) {
            case 2:
                return 9;
            default:
                return 1;
        }
    }

    public void loadInterstitialAd(Context context, InterstitialAdapterListener interstitialAdapterListener, Map<String, Object> map) {
        this.b = context;
        this.d = interstitialAdapterListener;
        this.f = k.a((JSONObject) map.get("data"));
        if (f.a(context, this.f)) {
            interstitialAdapterListener.onInterstitialError(this, AdError.NO_FILL);
            return;
        }
        this.c = new c(context, this.a, this, this.d);
        this.c.a();
        Map h = this.f.h();
        if (h.containsKey("orientation")) {
            this.g = a.a(Integer.parseInt((String) h.get("orientation")));
        }
        this.e = true;
        if (this.d != null) {
            this.d.onInterstitialAdLoaded(this);
        }
    }

    public void onDestroy() {
        if (this.c != null) {
            this.c.b();
        }
        if (this.h != null) {
            h.a(this.h);
            this.h.destroy();
            this.h = null;
        }
    }

    public boolean show() {
        if (this.e) {
            Intent intent = new Intent(this.b, InterstitialAdActivity.class);
            this.f.a(intent);
            intent.putExtra(InterstitialAdActivity.PREDEFINED_ORIENTATION_KEY, a());
            intent.putExtra("adInterstitialUniqueId", this.a);
            intent.putExtra(InterstitialAdActivity.VIEW_TYPE, Type.DISPLAY);
            intent.addFlags(268435456);
            this.b.startActivity(intent);
            return true;
        }
        if (this.d != null) {
            this.d.onInterstitialError(this, AdError.INTERNAL_ERROR);
        }
        return false;
    }
}
