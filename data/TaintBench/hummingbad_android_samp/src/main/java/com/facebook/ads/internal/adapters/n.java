package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.extra.AdExtras;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.Map;

public abstract class n implements AdAdapter {
    public abstract void a(int i);

    public abstract void a(Context context, o oVar, Map<String, Object> map);

    public abstract void a(Map<String, Object> map);

    public abstract void b(Map<String, Object> map);

    public abstract boolean d();

    public abstract boolean e();

    public abstract boolean f();

    public abstract boolean g();

    public final AdPlacementType getPlacementType() {
        return AdPlacementType.NATIVE;
    }

    public abstract int h();

    public abstract int i();

    public abstract int j();

    public abstract Image k();

    public abstract Image l();

    public abstract NativeAdViewAttributes m();

    public abstract String n();

    public abstract String o();

    public abstract String p();

    public abstract String q();

    public abstract String r();

    public abstract Rating s();

    public abstract Image t();

    public abstract String u();

    public abstract String v();

    public abstract String w();

    public abstract String x();

    public abstract AdExtras y();

    public abstract boolean z();
}
