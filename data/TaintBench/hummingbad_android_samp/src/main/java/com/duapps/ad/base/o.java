package com.duapps.ad.base;

import android.os.SystemClock;
import android.text.TextUtils;
import com.duapps.ad.AdError;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: ToolCacheManager */
final class o implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ x c;
    final /* synthetic */ j d;
    private /* synthetic */ String e;
    private /* synthetic */ String f;

    o(j jVar, String str, int i, String str2, String str3, x xVar) {
        this.d = jVar;
        this.a = str;
        this.b = i;
        this.e = str2;
        this.f = str3;
        this.c = xVar;
    }

    public final void run() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            List a = e.a(this.d.d, this.a);
            a.add(new BasicNameValuePair("play", d.a(this.d.d, "com.android.vending") ? "1" : "0"));
            a.add(new BasicNameValuePair("res", "1080*460,244*244,170*170,108*108"));
            a.add(new BasicNameValuePair("ps", String.valueOf(20)));
            a.add(new BasicNameValuePair("pn", String.valueOf(1)));
            a.add(new BasicNameValuePair("sid", String.valueOf(this.b)));
            a.add(new BasicNameValuePair("sType", MASTNativeAdConstants.RESPONSE_NATIVE_STRING));
            a.add(new BasicNameValuePair("or", String.valueOf(a.j(this.d.d))));
            a.add(new BasicNameValuePair("siteId", this.e));
            a.add(new BasicNameValuePair("aSize", this.f));
            String property = System.getProperty("http.agent");
            if (TextUtils.isEmpty(property)) {
                property = "dianxinosdxbs/3.2 (Linux; Android; Tapas OTA) Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.18) Gecko/20110628 Ubuntu/10.04 (lucid) Firefox/3.6.18";
            }
            a.add(new BasicNameValuePair(MASTNativeAdConstants.REQUESTPARAM_UA, property));
            URL url = new URL(j.b + URLEncodedUtils.format(a, Defaults.ENCODING_UTF_8));
            f.c("ToolboxCacheManager", "getInmobiNativeAds sType :native, Url ->" + url.toString());
            w.a(url, new p(this, elapsedRealtime), i.b(this.d.d, this.b));
            i.b(j.b + this.b, this.d.d);
        } catch (MalformedURLException e) {
            f.a("ToolboxCacheManager", "getInmobiNativeAds sType :native, parse exception.", e);
            this.c.a(AdError.UNKNOW_ERROR.getErrorMessage());
            d.b(this.d.d, this.b, -102, SystemClock.elapsedRealtime() - elapsedRealtime);
        }
    }
}
