package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import com.picksinit.c;
import java.net.URI;

/* compiled from: PageLoader */
public class m extends i {
    protected int f = 0;
    protected int g = 10;

    public m(int i, int i2, String str) {
        super(str);
        this.f = i;
        this.g = i2;
    }

    /* access modifiers changed from: protected */
    public void d() {
        c("开始加载  start=" + this.f + " mCount=" + this.g + " mOffset=" + s());
    }

    /* access modifiers changed from: protected */
    public boolean l() {
        return q() || this.f != 0 || j();
    }

    /* access modifiers changed from: protected */
    public boolean n() {
        return this.f == 0;
    }

    /* access modifiers changed from: protected */
    public void b(MarketResponse marketResponse) {
        if (this.f == 0) {
            o();
        }
    }

    /* access modifiers changed from: protected */
    public URI a(MarketRequestBuilder marketRequestBuilder) {
        marketRequestBuilder.pg(this.f);
        if (this.f == 0) {
            marketRequestBuilder.offset(0);
        } else {
            marketRequestBuilder.offset(s());
        }
        marketRequestBuilder.adn(this.g);
        return super.a(marketRequestBuilder);
    }

    public void e(MarketResponse marketResponse) {
        b(marketResponse.offset());
    }

    /* access modifiers changed from: protected|final */
    public final void b(int i) {
        c.getInstance().getContext().getSharedPreferences("market_config", 0).edit().putInt(g() + "_pageloader_offset", i).commit();
    }

    /* access modifiers changed from: protected|final */
    public final int s() {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getInt(g() + "_pageloader_offset", 0);
    }
}
