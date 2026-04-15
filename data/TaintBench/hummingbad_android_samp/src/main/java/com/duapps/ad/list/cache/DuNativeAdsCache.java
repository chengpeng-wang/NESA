package com.duapps.ad.list.cache;

import android.content.Context;
import java.util.HashMap;
import java.util.Iterator;

public class DuNativeAdsCache {
    private static DuNativeAdsCache a;
    private Context b;
    private HashMap<Integer, INativeListRequest> c = new HashMap();

    private DuNativeAdsCache(Context context) {
        this.b = context;
    }

    public static DuNativeAdsCache getInstance(Context context) {
        if (a == null) {
            synchronized (DuNativeAdsCache.class) {
                if (a == null) {
                    a = new DuNativeAdsCache(context);
                }
            }
        }
        return a;
    }

    public INativeListRequest getCachePool(int i, int i2) {
        Integer valueOf = Integer.valueOf(i);
        if (this.c.containsKey(valueOf)) {
            return (INativeListRequest) this.c.get(valueOf);
        }
        a aVar = new a(this.b, i, i2);
        synchronized (this.c) {
            this.c.put(valueOf, aVar);
        }
        return aVar;
    }

    public void destroy() {
        synchronized (this.c) {
            Iterator it = this.c.keySet().iterator();
            while (it.hasNext()) {
                ((INativeListRequest) this.c.remove((Integer) it.next())).destroy();
                it.remove();
            }
            this.c.clear();
        }
    }

    public void destroy(int i) {
        if (this.c.containsKey(Integer.valueOf(i))) {
            INativeListRequest iNativeListRequest;
            synchronized (this.c) {
                iNativeListRequest = (INativeListRequest) this.c.remove(Integer.valueOf(i));
            }
            iNativeListRequest.destroy();
            iNativeListRequest.clearCache();
        }
    }
}
