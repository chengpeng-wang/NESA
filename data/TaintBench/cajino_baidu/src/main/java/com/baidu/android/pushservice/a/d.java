package com.baidu.android.pushservice.a;

import android.content.Context;

public class d extends c {
    protected boolean e = false;

    public d(l lVar, Context context) {
        super(lVar, context);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x011d A:{LOOP_END, LOOP:0: B:19:0x0117->B:21:0x011d} */
    public java.lang.String b(java.lang.String r7) {
        /*
        r6 = this;
        r0 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0096 }
        r0.<init>(r7);	 Catch:{ JSONException -> 0x0096 }
        r1 = "response_params";
        r1 = r0.getJSONObject(r1);	 Catch:{ JSONException -> 0x0096 }
        r2 = "user_id";
        r2 = r1.getString(r2);	 Catch:{ JSONException -> 0x0096 }
        r3 = "appid";
        r3 = r1.getString(r3);	 Catch:{ JSONException -> 0x0096 }
        r4 = "channel_id";
        r5 = com.baidu.android.pushservice.y.a();	 Catch:{ JSONException -> 0x0096 }
        r5 = r5.c();	 Catch:{ JSONException -> 0x0096 }
        r1.put(r4, r5);	 Catch:{ JSONException -> 0x0096 }
        r1 = r6.b;	 Catch:{ JSONException -> 0x0096 }
        r1.g = r2;	 Catch:{ JSONException -> 0x0096 }
        r1 = r6.b;	 Catch:{ JSONException -> 0x0096 }
        r1.f = r3;	 Catch:{ JSONException -> 0x0096 }
        r0 = r0.toString();	 Catch:{ JSONException -> 0x0096 }
        r1 = com.baidu.android.pushservice.b.a();	 Catch:{ JSONException -> 0x013f }
        if (r1 == 0) goto L_0x007e;
    L_0x0036:
        r1 = "BaseRegisterProcessor";
        r4 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x013f }
        r4.<init>();	 Catch:{ JSONException -> 0x013f }
        r5 = "RegisterThread userId :  ";
        r4 = r4.append(r5);	 Catch:{ JSONException -> 0x013f }
        r2 = r4.append(r2);	 Catch:{ JSONException -> 0x013f }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x013f }
        com.baidu.android.common.logging.Log.i(r1, r2);	 Catch:{ JSONException -> 0x013f }
        r1 = "BaseRegisterProcessor";
        r2 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x013f }
        r2.<init>();	 Catch:{ JSONException -> 0x013f }
        r4 = "RegisterThread appId :  ";
        r2 = r2.append(r4);	 Catch:{ JSONException -> 0x013f }
        r2 = r2.append(r3);	 Catch:{ JSONException -> 0x013f }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x013f }
        com.baidu.android.common.logging.Log.i(r1, r2);	 Catch:{ JSONException -> 0x013f }
        r1 = "BaseRegisterProcessor";
        r2 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x013f }
        r2.<init>();	 Catch:{ JSONException -> 0x013f }
        r3 = "RegisterThread content :  ";
        r2 = r2.append(r3);	 Catch:{ JSONException -> 0x013f }
        r2 = r2.append(r7);	 Catch:{ JSONException -> 0x013f }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x013f }
        com.baidu.android.common.logging.Log.i(r1, r2);	 Catch:{ JSONException -> 0x013f }
    L_0x007e:
        r1 = r0;
    L_0x007f:
        r0 = r6.b;
        r0 = r0.b;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x00b8;
    L_0x0089:
        r0 = r6.b;
        r0 = r0.b;
        r2 = "internal";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x00b8;
    L_0x0095:
        return r1;
    L_0x0096:
        r0 = move-exception;
        r0 = r7;
    L_0x0098:
        r1 = com.baidu.android.pushservice.b.a();
        if (r1 == 0) goto L_0x00b6;
    L_0x009e:
        r1 = "BaseRegisterProcessor";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Appid or user_id not found @: \r\n";
        r2 = r2.append(r3);
        r2 = r2.append(r7);
        r2 = r2.toString();
        com.baidu.android.common.logging.Log.d(r1, r2);
    L_0x00b6:
        r1 = r0;
        goto L_0x007f;
    L_0x00b8:
        r0 = new com.baidu.android.pushservice.d;
        r0.m655init();
        r2 = r6.b;
        r2 = r2.e;
        r0.a = r2;
        r2 = r6.b;
        r2 = r2.f;
        r0.b = r2;
        r2 = r6.b;
        r2 = r2.g;
        r0.c = r2;
        r2 = new android.content.Intent;
        r3 = "com.baidu.android.pushservice.action.BIND_SYNC";
        r2.<init>(r3);
        r3 = r6.a;
        r3 = com.baidu.android.pushservice.a.a(r3);
        r4 = r6.e;
        r3 = r3.a(r0, r4);
        r4 = "r_sync_rdata";
        r2.putExtra(r4, r3);
        r3 = r6.a;
        r3 = com.baidu.android.pushservice.a.a(r3);
        r4 = r6.e;
        r0 = r3.b(r0, r4);
        r3 = "r_sync_rdata_v2";
        r2.putExtra(r3, r0);
        r0 = "r_sync_from";
        r3 = r6.a;
        r3 = r3.getPackageName();
        r2.putExtra(r0, r3);
        r0 = 32;
        r2.setFlags(r0);
        r0 = r6.a;
        r0.sendBroadcast(r2);
        r0 = r6.a;
        r0 = com.baidu.android.pushservice.util.m.q(r0);
        r2 = r0.iterator();
    L_0x0117:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x0095;
    L_0x011d:
        r0 = r2.next();
        r0 = (java.lang.String) r0;
        r3 = r6.a;
        r3 = com.baidu.android.pushservice.PushConstants.createMethodIntent(r3);
        r4 = "method";
        r5 = "pushservice_restart";
        r3.putExtra(r4, r5);
        r3.setPackage(r0);
        r0 = r6.a;
        r0.sendBroadcast(r3);
        r0 = r6.a;
        r3 = 0;
        com.baidu.android.pushservice.b.a(r0, r3);
        goto L_0x0117;
    L_0x013f:
        r1 = move-exception;
        goto L_0x0098;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.d.b(java.lang.String):java.lang.String");
    }
}
