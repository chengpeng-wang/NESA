package com.cleanmaster.ui.app.market.loader;

import android.os.Process;
import com.cleanmaster.ui.app.market.b;
import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.ui.app.market.data.filter.a;
import com.cleanmaster.ui.app.market.storage.MarketStorage;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import com.cleanmaster.util.v;
import com.picksinit.c;
import java.net.URI;
import java.util.List;
import org.xbill.DNS.TTL;

/* compiled from: BaseMarketLoader */
public abstract class i extends AsyncTaskEx {
    private boolean a = false;
    protected MarketRequestBuilder b;
    protected String c;
    protected String d = "BaseMarketLoader";
    private boolean f = false;
    private int g = -1;
    private long h;
    private boolean i = false;

    /* access modifiers changed from: protected */
    public void a(String str) {
        this.d = str;
    }

    public void a(int i) {
        this.g = i;
        b.a(Long.valueOf(this.c).longValue(), b() / 1000);
    }

    /* access modifiers changed from: protected */
    public String g() {
        return this.c;
    }

    public i(String str) {
        b(str);
    }

    /* access modifiers changed from: protected */
    public MarketRequestBuilder h() {
        this.b = MarketRequestBuilder.CREATOR();
        this.b.posid(this.c);
        return this.b;
    }

    public void b(String str) {
        this.c = str;
    }

    /* access modifiers changed from: protected */
    public URI a(MarketRequestBuilder marketRequestBuilder) {
        return marketRequestBuilder.toURI();
    }

    /* access modifiers changed from: protected */
    public void d() {
    }

    /* access modifiers changed from: protected|varargs */
    public MarketResponse a(Void... voidArr) {
        h();
        if (this.f) {
            Process.setThreadPriority(10);
        }
        if (!i()) {
            return null;
        }
        if (l()) {
            c("  请求网络.........");
            long currentTimeMillis = System.currentTimeMillis();
            a();
            MarketResponse a = a(a(this.b));
            currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
            if (currentTimeMillis > TTL.MAX_VALUE || currentTimeMillis < 0) {
                currentTimeMillis = 0;
            }
            b.c(currentTimeMillis);
            v.b(new j(this));
            if (a != null) {
                c("  保存到本地..........");
                if (a(a)) {
                    com.cleanmaster.util.i.a().a(g(), a);
                    b(a);
                    this.a = false;
                }
            }
            this.i = false;
            return a;
        }
        MarketResponse r = r();
        this.a = true;
        c("  从缓存中加载");
        return r;
    }

    /* access modifiers changed from: protected */
    public boolean i() {
        return true;
    }

    public boolean j() {
        return this.i;
    }

    public void k() {
        this.i = true;
    }

    /* access modifiers changed from: protected */
    public void c(String str) {
    }

    /* access modifiers changed from: protected */
    public void a() {
        m();
    }

    /* access modifiers changed from: protected */
    public boolean l() {
        return q() || j();
    }

    /* access modifiers changed from: protected */
    public void b(MarketResponse marketResponse) {
        o();
    }

    /* access modifiers changed from: protected */
    public MarketResponse a(URI uri) {
        return CmMarketHttpClient.a().a(this.b.mPosId, uri);
    }

    /* access modifiers changed from: protected */
    public boolean m() {
        if (!n() || MarketStorage.a().c(g()) <= 0) {
            return false;
        }
        com.cleanmaster.util.i.a().b(g());
        new Thread(new k(this)).start();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean n() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void o() {
        this.h = System.currentTimeMillis();
        c.getInstance().getContext().getSharedPreferences("market_config", 0).edit().putLong(g() + "_cache_time", this.h).commit();
    }

    /* access modifiers changed from: protected */
    public long p() {
        if (this.h <= 0) {
            this.h = c.getInstance().getContext().getSharedPreferences("market_config", 0).getLong(g() + "_cache_time", 0);
        }
        return this.h;
    }

    /* access modifiers changed from: protected */
    public boolean q() {
        long a = b.a(Long.valueOf(this.c));
        if (a == 0) {
            a = b.b(this.c);
            if (a == 0) {
                a = b();
            }
        }
        a = System.currentTimeMillis() - (a + p());
        c("  now=" + System.currentTimeMillis() + " last=" + p() + " expire=" + a);
        return a > 0;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public long b() {
        if (this.g < 0) {
            this.g = b.a();
        }
        return (long) this.g;
    }

    /* access modifiers changed from: protected */
    public MarketResponse r() {
        if (q()) {
            if (p() > 0) {
                m();
            }
            return null;
        }
        MarketResponse a = com.cleanmaster.util.i.a().a(g());
        if (a != null) {
            return a;
        }
        a = new MarketResponse();
        List a2 = MarketStorage.a().a(g(), null, null, null);
        if (a2.isEmpty()) {
            return null;
        }
        a.getHeader().a = 0;
        a.addAds(a2);
        a.getHeader().c = a2.size();
        a.getHeader().f = a2.size();
        com.cleanmaster.util.i.a().a(g(), a);
        return a;
    }

    public boolean a(MarketResponse marketResponse) {
        return MarketStorage.a().a(g(), marketResponse.ads()) == marketResponse.ads().size() && MarketStorage.a().a(g(), marketResponse) >= 0;
    }

    /* access modifiers changed from: protected|final */
    /* renamed from: c */
    public final void a(MarketResponse marketResponse) {
        v.b(new l(this, marketResponse));
    }

    /* access modifiers changed from: private */
    public MarketResponse f(MarketResponse marketResponse) {
        marketResponse.filter(a.a().b());
        return marketResponse;
    }

    public void d(MarketResponse marketResponse) {
    }

    public void e(MarketResponse marketResponse) {
    }
}
