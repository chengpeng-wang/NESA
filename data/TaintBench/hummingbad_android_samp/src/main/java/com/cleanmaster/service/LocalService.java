package com.cleanmaster.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.cleanmaster.ui.app.market.storage.MarketStorage;

public class LocalService extends IntentService {
    public static boolean a = true;
    public static long b = 15000;
    public static boolean d = false;
    PackageManager c = null;
    int e = 0;

    public LocalService() {
        super("LocalService");
    }

    public void onCreate() {
        super.onCreate();
    }

    public static void a(Context context, String str) {
        Intent intent = new Intent();
        intent.setClass(context, LocalService.class);
        intent.setAction("com.cleanmaster.service.ACTION_MARKET_SAVE_PENDDING_DOWNLOAD");
        intent.putExtra(":package-name", str);
        context.startService(intent);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        if (intent != null) {
            if ("com.cleanmaster.service.ACTION_MARKET_CLEAN_CACHE".equals(intent.getAction())) {
                a(intent);
            }
        }
    }

    private void a(Intent intent) {
        MarketStorage.a().a(intent.getLongExtra(":recent", a.a(1)));
    }
}
