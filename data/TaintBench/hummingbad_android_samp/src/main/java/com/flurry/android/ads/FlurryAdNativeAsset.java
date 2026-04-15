package com.flurry.android.ads;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.flurry.sdk.eh;
import com.flurry.sdk.iw;
import com.flurry.sdk.n;
import com.flurry.sdk.v;
import com.flurry.sdk.z;
import java.util.Map;

public final class FlurryAdNativeAsset {
    private static final String kLogTag = FlurryAdNativeAsset.class.getSimpleName();
    private int fAdId;
    private eh fAsset;

    FlurryAdNativeAsset(eh ehVar, int i) {
        if (ehVar == null) {
            throw new IllegalArgumentException("asset cannot be null");
        }
        this.fAsset = ehVar;
        this.fAdId = i;
    }

    private boolean canReturnImage() {
        Map map = this.fAsset.f;
        return ((this.fAsset.a.equals("secOrigImg") || this.fAsset.a.equals("secHqImage") || this.fAsset.a.equals("secImage")) && map.containsKey("internalOnly")) ? !Boolean.parseBoolean((String) map.get("internalOnly")) : true;
    }

    private String getUrlForImageAsset() {
        if (canReturnImage()) {
            v vVar = n.a().e;
            return v.a(this.fAsset, this.fAdId);
        }
        iw.a(kLogTag, "Cannot call getValue() this is video ad. Please look for video asset.");
        return null;
    }

    public final View getAssetView(Context context) {
        View view = null;
        v vVar = n.a().e;
        eh ehVar = this.fAsset;
        int i = this.fAdId;
        if (!(context == null || ehVar == null)) {
            switch (z.a[ehVar.b.ordinal()]) {
                case 1:
                    if (!ehVar.a.equals("callToAction")) {
                        view = new TextView(context);
                        break;
                    }
                    view = new Button(context);
                    break;
                case 2:
                    view = new ImageView(context);
                    break;
            }
            vVar.a(ehVar, view, i);
        }
        return view;
    }

    public final String getName() {
        return this.fAsset.a;
    }

    public final String getValue() {
        switch (this.fAsset.b) {
            case STRING:
                return this.fAsset.c;
            case IMAGE:
                return getUrlForImageAsset();
            case VIDEO:
                iw.a(kLogTag, "Cannot call getValue() on video type.");
                return null;
            default:
                return null;
        }
    }

    public final void loadAssetIntoView(View view) {
        n.a().e.a(this.fAsset, view, this.fAdId);
    }
}
