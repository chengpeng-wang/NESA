package com.google.android.apps.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.HashMap;

public class AnalyticsReceiver extends BroadcastReceiver {
    private static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";

    static String formatReferrer(String str) {
        String replace;
        String str2 = "=";
        String str3 = "%20";
        String[] split = str.split("&");
        HashMap hashMap = new HashMap();
        for (String replace2 : split) {
            String str4 = "=";
            String[] split2 = replace2.split(str2);
            if (split2.length != 2) {
                break;
            }
            hashMap.put(split2[0], split2[1]);
        }
        int i = hashMap.get("utm_campaign") != null ? 1 : 0;
        int i2 = hashMap.get("utm_medium") != null ? 1 : 0;
        int i3 = hashMap.get("utm_source") != null ? 1 : 0;
        if (i == 0 || i2 == 0 || i3 == 0) {
            Log.w(GoogleAnalyticsTracker.TRACKER_TAG, "Badly formatted referrer missing campaign, name or source");
            return null;
        }
        r0 = new String[7][];
        r0[0] = new String[]{"utmcid", (String) hashMap.get("utm_id")};
        r0[1] = new String[]{"utmcsr", (String) hashMap.get("utm_source")};
        r0[2] = new String[]{"utmgclid", (String) hashMap.get("gclid")};
        r0[3] = new String[]{"utmccn", (String) hashMap.get("utm_campaign")};
        r0[4] = new String[]{"utmcmd", (String) hashMap.get("utm_medium")};
        r0[5] = new String[]{"utmctr", (String) hashMap.get("utm_term")};
        r0[6] = new String[]{"utmcct", (String) hashMap.get("utm_content")};
        StringBuilder stringBuilder = new StringBuilder();
        i3 = 1;
        for (i2 = 0; i2 < r0.length; i2++) {
            if (r0[i2][1] != null) {
                String str5 = "%20";
                str5 = "%20";
                replace2 = r0[i2][1].replace("+", str3).replace(" ", str3);
                if (i3 != 0) {
                    i3 = 0;
                } else {
                    stringBuilder.append("|");
                }
                str5 = "=";
                stringBuilder.append(r0[i2][0]).append(str2).append(replace2);
            }
        }
        return stringBuilder.toString();
    }

    public void onReceive(Context context, Intent intent) {
        String str = GoogleAnalyticsTracker.TRACKER_TAG;
        String stringExtra = intent.getStringExtra("referrer");
        if (INSTALL_ACTION.equals(intent.getAction()) && stringExtra != null) {
            stringExtra = formatReferrer(stringExtra);
            if (stringExtra != null) {
                new PersistentEventStore(context).setReferrer(stringExtra);
                String str2 = GoogleAnalyticsTracker.TRACKER_TAG;
                Log.d(str, "Stored referrer:" + stringExtra);
                return;
            }
            stringExtra = GoogleAnalyticsTracker.TRACKER_TAG;
            Log.w(str, "Badly formatted referrer, ignored");
        }
    }
}
