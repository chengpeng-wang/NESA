package com.duapps.ad.base;

import android.os.SystemClock;
import com.duapps.ad.AdError;
import com.moceanmobile.mast.Defaults;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: ToolCacheManager */
final class k implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ x g;
    final /* synthetic */ j h;
    private /* synthetic */ int i;
    private /* synthetic */ int j;

    k(j jVar, String str, int i, int i2, int i3, String str2, String str3, String str4, String str5, x xVar) {
        this.h = jVar;
        this.a = str;
        this.i = i;
        this.j = i2;
        this.b = i3;
        this.c = str2;
        this.d = str3;
        this.e = str4;
        this.f = str5;
        this.g = xVar;
    }

    public final void run() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            List a = e.a(this.h.d, this.a);
            a.add(new BasicNameValuePair("play", d.a(this.h.d, "com.android.vending") ? "1" : "0"));
            a.add(new BasicNameValuePair("res", "1080*460,244*244,170*170,108*108"));
            a.add(new BasicNameValuePair("ps", String.valueOf(this.i)));
            a.add(new BasicNameValuePair("pn", String.valueOf(this.j)));
            a.add(new BasicNameValuePair("sid", String.valueOf(this.b)));
            a.add(new BasicNameValuePair("sType", this.c));
            a.add(new BasicNameValuePair("dllv", this.d));
            URL url = new URL(this.e + URLEncodedUtils.format(a, Defaults.ENCODING_UTF_8));
            f.c("ToolboxCacheManager", "getWall sType :" + this.d + "," + this.c + ", Url ->" + url.toString());
            w.a(url, new l(this, elapsedRealtime), i.b(this.h.d, this.b));
        } catch (MalformedURLException e) {
            f.a("ToolboxCacheManager", "getWall sType :" + this.c + ", parse exception.", e);
            this.g.a(AdError.UNKNOW_ERROR.getErrorMessage());
            d.a(this.h.d, this.d, this.b, -102, SystemClock.elapsedRealtime() - elapsedRealtime);
        }
    }
}
