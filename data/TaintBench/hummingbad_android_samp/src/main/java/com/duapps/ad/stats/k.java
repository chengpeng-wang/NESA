package com.duapps.ad.stats;

import android.content.Context;
import android.os.SystemClock;
import com.duapps.ad.base.d;
import com.duapps.ad.base.e;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.duapps.ad.base.t;
import com.duapps.ad.base.w;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/* compiled from: ToolStatsCore */
final class k implements Runnable {
    final /* synthetic */ ToolStatsCore a;

    k(ToolStatsCore toolStatsCore) {
        this.a = toolStatsCore;
    }

    public final void run() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            Context a = this.a.f;
            this.a.f;
            List a2 = e.a(a, t.a());
            HashSet b = t.a(this.a.f).b();
            StringBuilder stringBuilder = new StringBuilder();
            Iterator it = b.iterator();
            while (it.hasNext()) {
                stringBuilder.append(it.next());
                stringBuilder.append(",");
            }
            String stringBuilder2 = stringBuilder.toString();
            if (stringBuilder2.length() > 1) {
                stringBuilder2 = stringBuilder2.substring(0, stringBuilder2.length() - 1);
            }
            a2.add(new BasicNameValuePair("sid", stringBuilder2));
            a2.add(new BasicNameValuePair("res", "1080*460,244*244,170*170,108*108"));
            URL url = new URL(ToolStatsCore.c + URLEncodedUtils.format(a2, Defaults.ENCODING_UTF_8));
            f.c(MASTNativeAdConstants.REQUESTPARAM_TEST, "get src priority url: " + url);
            w.a(url, new l(this, elapsedRealtime), i.e(this.a.f));
        } catch (MalformedURLException e) {
            d.a(this.a.f, -102, SystemClock.elapsedRealtime() - elapsedRealtime);
        }
    }
}
