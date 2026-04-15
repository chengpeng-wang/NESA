package com.mopub.network;

import android.content.Context;
import com.mopub.volley.toolbox.HurlStack.UrlRewriter;

public class PlayServicesUrlRewriter implements UrlRewriter {
    public static final String DO_NOT_TRACK_TEMPLATE = "mp_tmpl_do_not_track";
    private static final String IFA_PREFIX = "ifa:";
    public static final String UDID_TEMPLATE = "mp_tmpl_advertising_id";
    private final Context applicationContext;
    private final String deviceIdentifier;

    public PlayServicesUrlRewriter(String deviceId, Context context) {
        this.deviceIdentifier = deviceId;
        this.applicationContext = context.getApplicationContext();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0050  */
    public java.lang.String rewriteUrl(java.lang.String r5) {
        /*
        r4 = this;
        r0 = "mp_tmpl_advertising_id";
        r0 = r5.contains(r0);
        if (r0 != 0) goto L_0x0011;
    L_0x0008:
        r0 = "mp_tmpl_do_not_track";
        r0 = r5.contains(r0);
        if (r0 != 0) goto L_0x0011;
    L_0x0010:
        return r5;
    L_0x0011:
        r2 = "";
        r1 = new com.mopub.common.GpsHelper$AdvertisingInfo;
        r0 = r4.deviceIdentifier;
        r3 = 0;
        r1.m1854init(r0, r3);
        r0 = r4.applicationContext;
        r0 = com.mopub.common.GpsHelper.isPlayServicesAvailable(r0);
        if (r0 == 0) goto L_0x005a;
    L_0x0023:
        r0 = r4.applicationContext;
        r0 = com.mopub.common.GpsHelper.fetchAdvertisingInfoSync(r0);
        if (r0 == 0) goto L_0x005a;
    L_0x002b:
        r1 = "ifa:";
    L_0x002d:
        r2 = "mp_tmpl_advertising_id";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = r3.append(r1);
        r3 = r0.advertisingId;
        r1 = r1.append(r3);
        r1 = r1.toString();
        r1 = android.net.Uri.encode(r1);
        r1 = r5.replace(r2, r1);
        r2 = "mp_tmpl_do_not_track";
        r0 = r0.limitAdTracking;
        if (r0 == 0) goto L_0x0057;
    L_0x0050:
        r0 = "1";
    L_0x0052:
        r5 = r1.replace(r2, r0);
        goto L_0x0010;
    L_0x0057:
        r0 = "0";
        goto L_0x0052;
    L_0x005a:
        r0 = r1;
        r1 = r2;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.network.PlayServicesUrlRewriter.rewriteUrl(java.lang.String):java.lang.String");
    }
}
