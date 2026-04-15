package com.cleanmaster.util;

import android.text.TextUtils;
import com.cleanmaster.ui.app.market.data.MarketResponse;
import java.util.Hashtable;
import java.util.Map;

/* compiled from: GlobalCache */
public class i {
    private static i b;
    private Map a;

    public static i a() {
        if (b == null) {
            synchronized (i.class) {
                if (b == null) {
                    b = new i();
                }
            }
        }
        return b;
    }

    public synchronized void a(String str, MarketResponse marketResponse) {
        if (this.a == null) {
            this.a = new Hashtable();
        }
        if (!(TextUtils.isEmpty(str) || marketResponse == null)) {
            this.a.put(str, marketResponse);
        }
    }

    public synchronized MarketResponse a(String str) {
        MarketResponse marketResponse;
        if (this.a != null) {
            marketResponse = (MarketResponse) this.a.get(str);
        } else {
            marketResponse = null;
        }
        return marketResponse;
    }

    public synchronized void b(String str) {
        if (!(this.a == null || ((MarketResponse) this.a.get(str)) == null)) {
            this.a.remove(str);
        }
    }
}
