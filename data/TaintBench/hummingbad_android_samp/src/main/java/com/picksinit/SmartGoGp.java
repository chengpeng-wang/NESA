package com.picksinit;

import android.content.Context;
import com.cleanmaster.ui.app.market.c;
import com.cleanmaster.ui.app.utils.MarketContext;

public class SmartGoGp implements goGp {
    private String mUrl = "";

    public SmartGoGp(String url) {
        this.mUrl = url;
    }

    public void goGp(Context context1) {
        c.a(new MarketContext(context1), this.mUrl);
    }
}
