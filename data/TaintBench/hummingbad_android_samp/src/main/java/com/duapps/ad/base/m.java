package com.duapps.ad.base;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.duapps.ad.AdError;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: ToolCacheManager */
final class m implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ x f;
    final /* synthetic */ j g;
    private /* synthetic */ DisplayMetrics h;
    private /* synthetic */ int i;
    private /* synthetic */ int j;

    m(j jVar, String str, DisplayMetrics displayMetrics, int i, int i2, String str2, int i3, String str3, String str4, x xVar) {
        this.g = jVar;
        this.a = str;
        this.h = displayMetrics;
        this.i = i;
        this.b = i2;
        this.c = str2;
        this.j = i3;
        this.d = str3;
        this.e = str4;
        this.f = xVar;
    }

    public final void run() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            List a = e.a(this.g.d, this.a);
            a.add(new BasicNameValuePair("play", d.a(this.g.d, "com.android.vending") ? "1" : "0"));
            a.add(new BasicNameValuePair("res", String.valueOf(this.h.heightPixels) + "*" + String.valueOf(this.h.widthPixels)));
            a.add(new BasicNameValuePair("ps", String.valueOf(20)));
            a.add(new BasicNameValuePair("pn", String.valueOf(this.i)));
            a.add(new BasicNameValuePair("sid", String.valueOf(this.b)));
            a.add(new BasicNameValuePair("sType", this.c));
            a.add(new BasicNameValuePair("aSize", String.valueOf(this.j)));
            a.add(new BasicNameValuePair(MASTNativeAdConstants.REQUESTPARAM_UA, g.b));
            URL url = new URL(this.d + URLEncodedUtils.format(a, Defaults.ENCODING_UTF_8));
            f.c("ToolboxCacheManager", "getOnlineWall sType :" + this.c + ", Url ->" + url.toString());
            w.a(url, new n(this, elapsedRealtime), i.b(this.g.d, this.b));
        } catch (MalformedURLException e) {
            f.a("ToolboxCacheManager", "getWall sType :" + this.c + ", parse exception.", e);
            this.f.a(AdError.UNKNOW_ERROR.getErrorMessage());
            d.c(this.g.d, this.b, -102, SystemClock.elapsedRealtime() - elapsedRealtime);
        }
    }
}
