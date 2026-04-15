package com.duapps.ad.list.cache;

import android.content.Context;
import com.duapps.ad.list.AdListArrivalListener;

/* compiled from: NativeListRequestManager */
public final class a implements INativeListRequest {
    private INativeListRequest a;

    public a(Context context, int i, int i2) {
        this.a = new b(context, i, i2);
    }

    public final void loadList() {
        this.a.loadList();
    }

    public final void fillList() {
        this.a.fillList();
    }

    public final void setListener(AdListArrivalListener adListArrivalListener) {
        this.a.setListener(adListArrivalListener);
    }

    public final void destroy() {
        this.a.destroy();
    }

    public final void clearCache() {
        this.a.clearCache();
    }
}
