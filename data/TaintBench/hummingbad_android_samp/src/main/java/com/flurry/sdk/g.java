package com.flurry.sdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import java.util.Collections;

public class g {
    /* access modifiers changed from: private|static|final */
    public static final String b = g.class.getSimpleName();
    int a = 10;
    private gy c = new gy();

    private void a(Context context, String str, af afVar, boolean z, boolean z2) {
        if (context == null) {
            iw.a(5, b, "Unable to launch url, null context");
        } else {
            hz.a.b(new h(this, str, context, z, afVar, z2));
        }
    }

    private static boolean a(a aVar) {
        boolean z = false;
        String a = aVar.a("sendYCookies");
        if (TextUtils.isEmpty(a)) {
            return z;
        }
        try {
            return Boolean.parseBoolean(a);
        } catch (Exception e) {
            iw.a(6, b, "caught Exception with sendYCookies parameter in onProcessRedirect:" + a);
            return z;
        }
    }

    private static boolean a(String str) {
        Intent launchIntentForPackage = hz.a.b.getPackageManager().getLaunchIntentForPackage(str);
        return launchIntentForPackage != null && lt.a(launchIntentForPackage);
    }

    private static void b(a aVar) {
        String a = aVar.a("idHash");
        if (!TextUtils.isEmpty(a)) {
            for (dj djVar : n.a().f.a(a)) {
                djVar.i++;
                djVar.j = System.currentTimeMillis();
                iw.a(4, b, "updateViewCount:capType=" + djVar.a + ",id=" + djVar.b + ",capRemaining=" + djVar.f + ",totalCap=" + djVar.g + ",views=" + djVar.i);
                if (djVar.i >= djVar.f) {
                    String str = aVar.c.e.a.b;
                    if (djVar.i > djVar.f) {
                        iw.a(6, b, "FlurryAdAction: !! rendering a capped object for id: " + djVar.b + " for adspace: " + str);
                    } else {
                        iw.a(4, b, "FlurryAdAction: hit cap for id: " + djVar.b + " for adspace: " + str);
                    }
                    di diVar = new di();
                    diVar.a = djVar;
                    diVar.a();
                }
            }
        }
    }

    private static void b(a aVar, int i) {
        Context context = aVar.c.c;
        String a = aVar.a("idHash");
        if (!TextUtils.isEmpty(a)) {
            dn dnVar = n.a().f;
            for (dj djVar : dnVar.a(a)) {
                dj djVar2;
                dg dgVar;
                dg dgVar2 = dg.EV_CAP_NOT_EXHAUSTED;
                if (djVar2 != null && dn.a(djVar2.d)) {
                    iw.a(4, b, "Discarding expired frequency cap info for id=" + a);
                    dnVar.a(djVar2.a, a);
                    djVar2 = null;
                }
                if (djVar2 == null || djVar2.i < djVar2.f) {
                    dgVar = dgVar2;
                } else {
                    iw.a(4, b, "Frequency cap exhausted for id=" + a);
                    dgVar = dg.EV_CAP_EXHAUSTED;
                }
                f.a().a(dgVar.J);
                gx.a(dgVar, Collections.emptyMap(), context, aVar.c.d, aVar.c.e, i + 1);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0151  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x02e9  */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0300  */
    /* JADX WARNING: Missing block: B:169:0x05a6, code skipped:
            if (r5.equals("next") != false) goto L_0x04fd;
     */
    public final void a(com.flurry.sdk.a r13, int r14) {
        /*
        r12 = this;
        r11 = 5;
        r9 = 6;
        r4 = 1;
        r2 = 0;
        r10 = 3;
        r0 = 0;
        r1 = r13.c;
        if (r1 == 0) goto L_0x000e;
    L_0x000a:
        r0 = r13.c;
        r0 = r0.a;
    L_0x000e:
        r1 = b;
        r3 = new java.lang.StringBuilder;
        r5 = "performAction:action=";
        r3.<init>(r5);
        r5 = r13.toString();
        r3 = r3.append(r5);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r10, r1, r3);
        r1 = r12.a;
        if (r14 <= r1) goto L_0x0043;
    L_0x002a:
        r0 = b;
        r1 = new java.lang.StringBuilder;
        r2 = "Maximum depth for event/action loop exceeded when performing action:";
        r1.<init>(r2);
        r2 = r13.toString();
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r11, r0, r1);
    L_0x0042:
        return;
    L_0x0043:
        r1 = com.flurry.sdk.i.a;
        r3 = r13.a;
        r3 = r3.ordinal();
        r1 = r1[r3];
        switch(r1) {
            case 1: goto L_0x009a;
            case 2: goto L_0x0112;
            case 3: goto L_0x0127;
            case 4: goto L_0x017e;
            case 5: goto L_0x01b5;
            case 6: goto L_0x0224;
            case 7: goto L_0x027c;
            case 8: goto L_0x03f4;
            case 9: goto L_0x0403;
            case 10: goto L_0x04e4;
            case 11: goto L_0x0606;
            case 12: goto L_0x0636;
            case 13: goto L_0x063b;
            case 14: goto L_0x0640;
            case 15: goto L_0x0651;
            case 16: goto L_0x0662;
            default: goto L_0x0050;
        };
    L_0x0050:
        r1 = b;
        r2 = new java.lang.StringBuilder;
        r3 = "Unknown action:";
        r2.<init>(r3);
        r3 = r13.a;
        r2 = r2.append(r3);
        r3 = ",triggered by:";
        r2 = r2.append(r3);
        r0 = r2.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r11, r1, r0);
    L_0x0070:
        r0 = r13.c;
        r0 = r0.b;
        r1 = "requiresCallComplete";
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 != 0) goto L_0x0042;
    L_0x0082:
        r1 = "true";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0042;
    L_0x008a:
        r0 = b;
        r1 = "Fire call complete";
        com.flurry.sdk.iw.a(r10, r0, r1);
        r0 = new com.flurry.sdk.hf;
        r0.m3431init();
        r0.a();
        goto L_0x0042;
    L_0x009a:
        r0 = r13.c;
        r1 = r0.c;
        r0 = r13.c;
        r3 = r0.d;
        r5 = a(r13);
        r0 = "url";
        r0 = r13.a(r0);
        r6 = android.text.TextUtils.isEmpty(r0);
        if (r6 != 0) goto L_0x00f8;
    L_0x00b2:
        r6 = com.flurry.sdk.mb.c(r0);
        if (r6 == 0) goto L_0x00bc;
    L_0x00b8:
        com.flurry.sdk.hd.a(r1, r0);
        goto L_0x0070;
    L_0x00bc:
        r6 = "true";
        r7 = "native";
        r7 = r13.a(r7);
        r6 = r6.equals(r7);
        r7 = "true";
        r8 = "is_privacy";
        r8 = r13.a(r8);
        r7 = r7.equals(r8);
        if (r7 != 0) goto L_0x00ea;
    L_0x00d6:
        if (r6 == 0) goto L_0x00ec;
    L_0x00d8:
        r2 = 2;
        r3 = b;
        r4 = "Explictly instructed to use native browser";
        com.flurry.sdk.iw.a(r2, r3, r4);
        r2 = r12.c;
        r0 = r2.a(r13, r0);
        com.flurry.sdk.hd.c(r1, r0);
        goto L_0x0070;
    L_0x00ea:
        r4 = r2;
        goto L_0x00d6;
    L_0x00ec:
        r2 = r12.c;
        r2 = r2.a(r13, r0);
        r0 = r12;
        r0.a(r1, r2, r3, r4, r5);
        goto L_0x0070;
    L_0x00f8:
        r0 = b;
        r1 = new java.lang.StringBuilder;
        r2 = "failed to perform directOpen action: no url in ";
        r1.<init>(r2);
        r2 = r13.c;
        r2 = r2.a;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r9, r0, r1);
        goto L_0x0070;
    L_0x0112:
        r0 = r13.c;
        r0 = r0.d;
        r1 = "groupId";
        r1 = r13.a(r1);
        r2 = android.text.TextUtils.isEmpty(r1);
        if (r2 != 0) goto L_0x0070;
    L_0x0122:
        r0.a(r1);
        goto L_0x0070;
    L_0x0127:
        r0 = r13.c;
        r1 = r0.c;
        r0 = r13.c;
        r3 = r0.d;
        r0 = "url";
        r0 = r13.a(r0);
        r5 = android.text.TextUtils.isEmpty(r0);
        if (r5 != 0) goto L_0x0070;
    L_0x013b:
        r5 = "sendYCookies";
        r5 = r13.a(r5);
        r6 = android.text.TextUtils.isEmpty(r5);
        if (r6 != 0) goto L_0x017c;
    L_0x0147:
        r5 = java.lang.Boolean.parseBoolean(r5);	 Catch:{ Exception -> 0x0167 }
    L_0x014b:
        r2 = android.text.TextUtils.isEmpty(r0);
        if (r2 != 0) goto L_0x0070;
    L_0x0151:
        r2 = r12.c;
        r0 = r2.a(r13, r0);
        r2 = com.flurry.sdk.mb.a(r0);
        r0 = android.text.TextUtils.isEmpty(r2);
        if (r0 != 0) goto L_0x0070;
    L_0x0161:
        r0 = r12;
        r0.a(r1, r2, r3, r4, r5);
        goto L_0x0070;
    L_0x0167:
        r6 = move-exception;
        r6 = b;
        r7 = new java.lang.StringBuilder;
        r8 = "caught Exception with sendYCookies parameter in onProcessRedirect:";
        r7.<init>(r8);
        r5 = r7.append(r5);
        r5 = r5.toString();
        com.flurry.sdk.iw.a(r9, r6, r5);
    L_0x017c:
        r5 = r2;
        goto L_0x014b;
    L_0x017e:
        r0 = r13.c;
        r2 = r0.c;
        r0 = r13.c;
        r3 = r0.d;
        r0 = r13.c;
        r4 = r0.e;
        r0 = "url";
        r0 = r13.a(r0);
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 != 0) goto L_0x0070;
    L_0x0196:
        r0 = a(r0);
        if (r0 == 0) goto L_0x01b2;
    L_0x019c:
        r0 = com.flurry.sdk.dg.EV_URL_VERIFIED;
    L_0x019e:
        r1 = com.flurry.sdk.f.a();
        r5 = r0.J;
        r1.a(r5);
        r1 = java.util.Collections.emptyMap();
        r5 = r14 + 1;
        com.flurry.sdk.gx.a(r0, r1, r2, r3, r4, r5);
        goto L_0x0070;
    L_0x01b2:
        r0 = com.flurry.sdk.dg.EV_URL_NOT_VERIFIED;
        goto L_0x019e;
    L_0x01b5:
        r0 = r13.c;
        r2 = r0.c;
        r0 = r13.c;
        r3 = r0.d;
        r0 = r13.c;
        r4 = r0.e;
        r0 = "url";
        r5 = r13.a(r0);
        r0 = android.text.TextUtils.isEmpty(r5);
        if (r0 != 0) goto L_0x0070;
    L_0x01cd:
        r0 = a(r5);
        if (r0 == 0) goto L_0x0221;
    L_0x01d3:
        r0 = com.flurry.sdk.dg.EV_PACKAGE_VERIFIED;
    L_0x01d5:
        r1 = com.flurry.sdk.f.a();
        r6 = r0.J;
        r1.a(r6);
        r1 = new java.util.HashMap;
        r1.<init>();
        r6 = r13.c;
        r6 = r6.a;
        r7 = com.flurry.sdk.dg.EV_FILLED;
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x01f8;
    L_0x01ef:
        r6 = "origin";
        r7 = com.flurry.sdk.dg.EV_FILLED;
        r7 = r7.J;
        r1.put(r6, r7);
    L_0x01f8:
        r6 = "VerifyPackageLog";
        r7 = new java.lang.StringBuilder;
        r8 = "onVerifyPackage() called for pkg: ";
        r7.<init>(r8);
        r7 = r7.append(r5);
        r8 = " packageInstalled: ";
        r7 = r7.append(r8);
        r5 = a(r5);
        r5 = r7.append(r5);
        r5 = r5.toString();
        com.flurry.sdk.iw.a(r10, r6, r5);
        r5 = r14 + 1;
        com.flurry.sdk.gx.a(r0, r1, r2, r3, r4, r5);
        goto L_0x0070;
    L_0x0221:
        r0 = com.flurry.sdk.dg.EV_PACKAGE_NOT_VERIFIED;
        goto L_0x01d5;
    L_0x0224:
        r0 = r13.c;
        r1 = r0.c;
        a(r13);
        r0 = "package";
        r0 = r13.a(r0);
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 != 0) goto L_0x0070;
    L_0x0237:
        r3 = com.flurry.sdk.hz.a;
        r3 = r3.b;
        r3 = r3.getPackageManager();
        r3 = r3.getLaunchIntentForPackage(r0);
        if (r3 == 0) goto L_0x0250;
    L_0x0245:
        r4 = com.flurry.sdk.lt.a(r3);
        if (r4 == 0) goto L_0x0250;
    L_0x024b:
        com.flurry.sdk.hd.a(r1, r3);
        goto L_0x0070;
    L_0x0250:
        r3 = new java.lang.StringBuilder;
        r4 = "https://play.google.com/store/apps/details?id=";
        r3.<init>(r4);
        r0 = r3.append(r0);
        r3 = r0.toString();
        r0 = com.flurry.sdk.mb.c(r3);
        if (r0 == 0) goto L_0x0673;
    L_0x0265:
        r0 = com.flurry.sdk.hd.a(r1, r3);
    L_0x0269:
        if (r0 != 0) goto L_0x0275;
    L_0x026b:
        r2 = com.flurry.sdk.mb.d(r3);
        if (r2 == 0) goto L_0x0275;
    L_0x0271:
        r0 = com.flurry.sdk.hd.b(r1, r3);
    L_0x0275:
        if (r0 != 0) goto L_0x0070;
    L_0x0277:
        com.flurry.sdk.hd.c(r1, r3);
        goto L_0x0070;
    L_0x027c:
        r0 = "url";
        r1 = r13.a(r0);
        r0 = android.text.TextUtils.isEmpty(r1);
        if (r0 != 0) goto L_0x0070;
    L_0x0288:
        r3 = java.lang.System.currentTimeMillis();
        r5 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        r4 = r3 + r5;
        r0 = "expirationTimeEpochSeconds";
        r0 = r13.a(r0);
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 != 0) goto L_0x02a5;
    L_0x029d:
        r3 = java.lang.Long.parseLong(r0);	 Catch:{ Exception -> 0x03bb }
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = r3 * r5;
    L_0x02a5:
        r7 = 2;
        r0 = "maxRetries";
        r0 = r13.a(r0);
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 != 0) goto L_0x02b6;
    L_0x02b2:
        r7 = java.lang.Integer.parseInt(r0);	 Catch:{ Exception -> 0x03d2 }
    L_0x02b6:
        r0 = "sendYCookies";
        r0 = r13.a(r0);
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 != 0) goto L_0x03f1;
    L_0x02c2:
        r2 = java.lang.Boolean.parseBoolean(r0);	 Catch:{ Exception -> 0x03dc }
        r6 = r2;
    L_0x02c7:
        r0 = r13.c;
        r2 = r0.d;
        r0 = r13.c;
        r0 = r0.a;
        r3 = com.flurry.sdk.dg.EV_VIDEO_START;
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x032c;
    L_0x02d7:
        r0 = r13.c;
        r0 = r0.b;
        if (r0 == 0) goto L_0x032c;
    L_0x02dd:
        r0 = r13.c;
        r3 = r0.b;
        r0 = "va";
        r0 = r3.containsKey(r0);
        if (r0 == 0) goto L_0x02f8;
    L_0x02e9:
        r8 = "$(V_AUTOPLAYED)";
        r0 = "va";
        r0 = r3.get(r0);
        r0 = (java.lang.CharSequence) r0;
        r0 = r1.replace(r8, r0);
        r1 = r0;
    L_0x02f8:
        r0 = "vm";
        r0 = r3.containsKey(r0);
        if (r0 == 0) goto L_0x030e;
    L_0x0300:
        r8 = "$(V_MUTED)";
        r0 = "vm";
        r0 = r3.get(r0);
        r0 = (java.lang.CharSequence) r0;
        r1 = r1.replace(r8, r0);
    L_0x030e:
        r0 = b;
        r3 = new java.lang.StringBuilder;
        r8 = "sendUrlAsync: New Url: ";
        r3.<init>(r8);
        r3 = r3.append(r1);
        r8 = " adObj: ";
        r3 = r3.append(r8);
        r3 = r3.append(r2);
        r3 = r3.toString();
        android.util.Log.e(r0, r3);
    L_0x032c:
        r3 = r1;
        r0 = r13.c;
        r0 = r0.a;
        r1 = com.flurry.sdk.dg.EV_VIDEO_START;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0369;
    L_0x0339:
        r0 = r13.c;
        r0 = r0.a;
        r1 = com.flurry.sdk.dg.EV_VIDEO_VIEWED;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0369;
    L_0x0345:
        r0 = r13.c;
        r0 = r0.a;
        r1 = com.flurry.sdk.dg.EV_VIDEO_FIRST_QUARTILE;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0369;
    L_0x0351:
        r0 = r13.c;
        r0 = r0.a;
        r1 = com.flurry.sdk.dg.EV_VIDEO_MIDPOINT;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0369;
    L_0x035d:
        r0 = r13.c;
        r0 = r0.a;
        r1 = com.flurry.sdk.dg.EV_VIDEO_COMPLETED;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0397;
    L_0x0369:
        r0 = b;
        r1 = new java.lang.StringBuilder;
        r8 = "BeaconTest: event name: ";
        r1.<init>(r8);
        r8 = r13.c;
        r8 = r8.a;
        r8 = r8.J;
        r1 = r1.append(r8);
        r8 = " beacon Url: ";
        r1 = r1.append(r8);
        r1 = r1.append(r3);
        r8 = " adObj: ";
        r1 = r1.append(r8);
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r11, r0, r1);
    L_0x0397:
        r0 = new com.flurry.sdk.eq;
        r1 = r13.c;
        r1 = r1.a;
        r1 = r1.J;
        r2 = r13.c;
        r2 = r2.a();
        r2 = r2.f;
        r8 = r12.c;
        r3 = r8.a(r13, r3);
        r0.m3378init(r1, r2, r3, r4, r6, r7);
        r1 = com.flurry.sdk.n.a();
        r1 = r1.d;
        r1.b(r0);
        goto L_0x0070;
    L_0x03bb:
        r3 = move-exception;
        r3 = b;
        r6 = new java.lang.StringBuilder;
        r7 = "caught Exception with expirationTime parameter in onSendUrlAsync:";
        r6.<init>(r7);
        r0 = r6.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r9, r3, r0);
        goto L_0x02a5;
    L_0x03d2:
        r0 = move-exception;
        r0 = b;
        r3 = "caught Exception with maxRetries parameter in onSendUrlAsync:2";
        com.flurry.sdk.iw.a(r9, r0, r3);
        goto L_0x02b6;
    L_0x03dc:
        r3 = move-exception;
        r3 = b;
        r6 = new java.lang.StringBuilder;
        r8 = "caught Exception with sendYCookies parameter in onSendUrlAsync:";
        r6.<init>(r8);
        r0 = r6.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r9, r3, r0);
    L_0x03f1:
        r6 = r2;
        goto L_0x02c7;
    L_0x03f4:
        r0 = com.flurry.sdk.n.a();
        r1 = r0.i;
        if (r1 == 0) goto L_0x0070;
    L_0x03fc:
        r0 = r0.i;
        r0.a();
        goto L_0x0070;
    L_0x0403:
        r0 = r13.b;
        r1 = "__sendToServer";
        r0 = r0.containsKey(r1);
        if (r0 == 0) goto L_0x04c0;
    L_0x040d:
        r0 = "__sendToServer";
        r0 = r13.a(r0);
        r1 = "true";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x04c0;
    L_0x041b:
        r1 = r4;
    L_0x041c:
        r0 = r13.b;
        if (r0 == 0) goto L_0x0428;
    L_0x0420:
        r0 = "__sendToServer";
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x04c3;
    L_0x0428:
        r0 = r13.c;
        r0 = r0.e;
        r3 = r0.a();
        r0 = r13.c;
        r5 = r0.a;
        r6 = r13.b;
        r0 = r13.c;
        r7 = r0.e;
        r0 = r13.c;
        r0 = r0.a;
        r8 = r0.J;
        r0 = r7.b;
        r9 = r7.d;
        r0 = r0.get(r9);
        r0 = (com.flurry.sdk.cu) r0;
        r9 = android.text.TextUtils.isEmpty(r8);
        if (r9 != 0) goto L_0x04cc;
    L_0x0450:
        r9 = r0.b;
        r9 = r9.contains(r8);
        if (r9 == 0) goto L_0x0460;
    L_0x0458:
        r0 = r0.d;
        r0 = r0.contains(r8);
        if (r0 != 0) goto L_0x04cc;
    L_0x0460:
        if (r4 == 0) goto L_0x04ce;
    L_0x0462:
        r0 = b;
        r2 = new java.lang.StringBuilder;
        r4 = "onLogEvent(";
        r2.<init>(r4);
        r2 = r2.append(r3);
        r4 = ", ";
        r2 = r2.append(r4);
        r2 = r2.append(r5);
        r4 = ", ";
        r2 = r2.append(r4);
        r2 = r2.append(r1);
        r4 = ", ";
        r2 = r2.append(r4);
        r2 = r2.append(r6);
        r4 = ")";
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r10, r0, r2);
        r0 = com.flurry.sdk.n.a();
        r0.a(r3, r5, r1, r6);
        r0 = r7.b;
        r1 = r7.d;
        r0 = r0.get(r1);
        r0 = (com.flurry.sdk.cu) r0;
        r1 = android.text.TextUtils.isEmpty(r8);
        if (r1 != 0) goto L_0x0070;
    L_0x04b1:
        r1 = r0.b;
        r1 = r1.contains(r8);
        if (r1 == 0) goto L_0x0070;
    L_0x04b9:
        r0 = r0.d;
        r0.add(r8);
        goto L_0x0070;
    L_0x04c0:
        r1 = r2;
        goto L_0x041c;
    L_0x04c3:
        r0 = r13.b;
        r3 = "__sendToServer";
        r0.remove(r3);
        goto L_0x0428;
    L_0x04cc:
        r4 = r2;
        goto L_0x0460;
    L_0x04ce:
        r0 = b;
        r1 = new java.lang.StringBuilder;
        r2 = "Event already logged for ";
        r1.<init>(r2);
        r1 = r1.append(r8);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r10, r0, r1);
        goto L_0x0070;
    L_0x04e4:
        r0 = r13.c;
        r3 = r0.e;
        r0 = r3.d;
        r0 = r0 + 1;
        r1 = "offset";
        r5 = r13.a(r1);
        if (r5 == 0) goto L_0x05d5;
    L_0x04f4:
        r1 = -1;
        r6 = r5.hashCode();
        switch(r6) {
            case 3377907: goto L_0x05a0;
            case 1126940025: goto L_0x05aa;
            default: goto L_0x04fc;
        };
    L_0x04fc:
        r2 = r1;
    L_0x04fd:
        switch(r2) {
            case 0: goto L_0x05b5;
            case 1: goto L_0x0070;
            default: goto L_0x0500;
        };
    L_0x0500:
        r0 = java.lang.Integer.parseInt(r5);	 Catch:{ Exception -> 0x05bc }
        r1 = r0;
    L_0x0505:
        r0 = r13.c;
        r2 = r0.e;
        a(r13);
        r0 = b;
        r3 = new java.lang.StringBuilder;
        r4 = "goToFrame: triggering event = ";
        r3.<init>(r4);
        r4 = r13.c;
        r4 = r4.c;
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r10, r0, r3);
        r0 = r2.d;
        if (r1 == r0) goto L_0x0070;
    L_0x0528:
        r0 = r2.a;
        r0 = r0.d;
        r0 = r0.size();
        if (r1 >= r0) goto L_0x0070;
    L_0x0532:
        r0 = b;
        r3 = new java.lang.StringBuilder;
        r4 = "goToFrame: currentIndex = ";
        r3.<init>(r4);
        r4 = r2.d;
        r3 = r3.append(r4);
        r4 = " and go to index: ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r10, r0, r3);
        r0 = r2.a;
        r0 = r0.d;
        r0 = r0.get(r1);
        r0 = (com.flurry.sdk.dq) r0;
        r3 = r2.c();
        r0 = r0.d;
        r0 = r0.d;
        r4 = r3.toString();
        r4 = r0.equalsIgnoreCase(r4);
        if (r4 != 0) goto L_0x05d8;
    L_0x056e:
        r4 = b;
        r5 = new java.lang.StringBuilder;
        r6 = "goToFrame: Moving now from ";
        r5.<init>(r6);
        r3 = r3.toString();
        r3 = r5.append(r3);
        r5 = " to format ";
        r3 = r3.append(r5);
        r3 = r3.append(r0);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r10, r4, r3);
        r3 = com.flurry.sdk.dh.TAKEOVER;
        r3 = r3.toString();
        r0 = r0.equalsIgnoreCase(r3);
        if (r0 == 0) goto L_0x0070;
    L_0x059c:
        r2.d = r1;
        goto L_0x0070;
    L_0x05a0:
        r4 = "next";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x04fc;
    L_0x05a8:
        goto L_0x04fd;
    L_0x05aa:
        r2 = "current";
        r2 = r5.equals(r2);
        if (r2 == 0) goto L_0x04fc;
    L_0x05b2:
        r2 = r4;
        goto L_0x04fd;
    L_0x05b5:
        r0 = r3.d;
        r0 = r0 + 1;
        r1 = r0;
        goto L_0x0505;
    L_0x05bc:
        r1 = move-exception;
        r2 = b;
        r3 = new java.lang.StringBuilder;
        r4 = "caught: ";
        r3.<init>(r4);
        r1 = r1.getMessage();
        r1 = r3.append(r1);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r9, r2, r1);
    L_0x05d5:
        r1 = r0;
        goto L_0x0505;
    L_0x05d8:
        r4 = b;
        r5 = new java.lang.StringBuilder;
        r6 = "goToFrame: Already a takeover Ad, just move to next frame. ";
        r5.<init>(r6);
        r3 = r3.toString();
        r3 = r5.append(r3);
        r5 = " to format ";
        r3 = r3.append(r5);
        r0 = r3.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r10, r4, r0);
        r2.d = r1;
        r0 = new com.flurry.sdk.he;
        r0.m3430init();
        r0.a();
        goto L_0x0070;
    L_0x0606:
        r0 = r13.c;
        r0 = r0.d;
        r1 = r13.c;
        r1 = r1.a;
        r3 = com.flurry.sdk.dg.EV_PACKAGE_VERIFIED;
        r1 = r1.equals(r3);
        if (r1 != 0) goto L_0x0622;
    L_0x0616:
        r1 = r13.c;
        r1 = r1.a;
        r3 = com.flurry.sdk.dg.EV_PACKAGE_NOT_VERIFIED;
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0631;
    L_0x0622:
        r1 = r13.c;
        r1 = r1.b;
        r3 = com.flurry.sdk.dg.EV_FILLED;
        r3 = r3.J;
        r1 = r1.containsValue(r3);
        if (r1 == 0) goto L_0x0631;
    L_0x0630:
        r4 = r2;
    L_0x0631:
        r0.a(r4);
        goto L_0x0070;
    L_0x0636:
        b(r13, r14);
        goto L_0x0070;
    L_0x063b:
        b(r13);
        goto L_0x0070;
    L_0x0640:
        r0 = b;
        r1 = "closing ad";
        com.flurry.sdk.iw.a(r10, r0, r1);
        r0 = new com.flurry.sdk.hf;
        r0.m3431init();
        r0.a();
        goto L_0x0070;
    L_0x0651:
        r0 = b;
        r1 = "notify user";
        com.flurry.sdk.iw.a(r10, r0, r1);
        r0 = new com.flurry.sdk.hf;
        r0.m3431init();
        r0.a();
        goto L_0x0070;
    L_0x0662:
        r0 = b;
        r1 = "expanding ad";
        com.flurry.sdk.iw.a(r10, r0, r1);
        r0 = new com.flurry.sdk.hf;
        r0.m3431init();
        r0.a();
        goto L_0x0070;
    L_0x0673:
        r0 = r2;
        goto L_0x0269;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.g.a(com.flurry.sdk.a, int):void");
    }
}
