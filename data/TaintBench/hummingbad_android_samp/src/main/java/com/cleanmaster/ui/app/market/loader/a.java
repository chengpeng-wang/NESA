package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import java.net.URI;

/* compiled from: AppLoader */
public class a extends m {
    int a = -1;
    private long h = 0;

    public a(int i, int i2, String str) {
        super(i, i2, str);
        a("BaseAppLoader/" + str);
    }

    /* access modifiers changed from: protected */
    public URI a(MarketRequestBuilder marketRequestBuilder) {
        if (this.a > 0) {
            marketRequestBuilder.g_pg(this.a);
        }
        return super.a(marketRequestBuilder);
    }

    /* access modifiers changed from: protected */
    public void a() {
        super.a();
    }

    public boolean a(MarketResponse marketResponse) {
        return super.a(marketResponse);
    }

    /* access modifiers changed from: protected */
    public long b() {
        if (0 != this.h) {
            return this.h;
        }
        return super.b();
    }
}
