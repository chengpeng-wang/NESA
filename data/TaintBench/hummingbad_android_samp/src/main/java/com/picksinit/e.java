package com.picksinit;

import android.util.Log;
import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.data.MarketResponse;
import com.cleanmaster.ui.app.market.loader.a;
import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient.MarketRequestBuilder;
import java.util.ArrayList;

/* compiled from: PicksMobBase */
class e extends a {
    final /* synthetic */ ICallBack h;
    final /* synthetic */ int i;
    final /* synthetic */ int j;
    final /* synthetic */ int k;
    final /* synthetic */ int l;
    final /* synthetic */ int m;
    final /* synthetic */ int n;
    final /* synthetic */ c o;

    e(c cVar, int i, int i2, String str, ICallBack iCallBack, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.o = cVar;
        this.h = iCallBack;
        this.i = i3;
        this.j = i4;
        this.k = i5;
        this.l = i6;
        this.m = i7;
        this.n = i8;
        super(i, i2, str);
    }

    public void d(MarketResponse marketResponse) {
        if (this.h == null) {
            return;
        }
        if (marketResponse == null) {
            this.h.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_NO_FILL, "ad is no filled", this.i));
        } else {
            this.h.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL, "ad data is null, but request status is right", this.i));
        }
    }

    public void e(MarketResponse marketResponse) {
        super.e(marketResponse);
        if (marketResponse == null) {
            this.h.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_NO_FILL, "ad is no filled", this.i));
            return;
        }
        ArrayList<Ad> arrayList = new ArrayList();
        if (marketResponse.ads() != null && marketResponse.ads().size() > 0) {
            arrayList.addAll(marketResponse.ads());
            Object obj = arrayList.size() > 0 ? 1 : null;
            this.o.filterShowed(arrayList);
            if (obj == null || arrayList.size() != 0) {
                this.o.filter(arrayList);
                if (c.getInstance().isDebug()) {
                    if (arrayList.size() <= 0) {
                        Log.e(c.TAG, "ad size is zero");
                    }
                    for (Ad toJson : arrayList) {
                        Log.e(c.TAG, toJson.toJson().toString());
                    }
                }
                if (this.h == null) {
                    return;
                }
                if (arrayList.size() > 0) {
                    this.h.onLoadSuccess(arrayList);
                    return;
                } else {
                    this.h.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL, "ad data is filtered, please use setConfig to warn", this.i));
                    return;
                }
            }
            this.o.loadFromNet(this.j, this.i, this.h, this.k, this.l, this.m, this.n);
        } else if (this.h != null) {
            this.h.onLoadError(new ErrorInfo(ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL, "ad data is zero, but request status is right", this.i));
        }
    }

    /* access modifiers changed from: protected */
    public void d() {
        super.d();
        if (this.h != null) {
            this.h.onPreExecute();
        }
    }

    /* access modifiers changed from: protected */
    public MarketRequestBuilder h() {
        MarketRequestBuilder h = super.h();
        if (this.m > 0 && this.n > 0) {
            h.w(this.m).h(this.n);
        }
        return h;
    }
}
