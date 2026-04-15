package com.baidu.platform.comapi.b;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import com.baidu.platform.comapi.basestruct.b;
import com.baidu.platform.comapi.basestruct.c;
import com.baidu.platform.comjni.map.search.a;
import java.util.Map;
import org.jivesoftware.smackx.GroupChatInvitation;

public class e {
    private static e b = null;
    private a a = null;
    /* access modifiers changed from: private */
    public d c = null;
    private Handler d = null;
    private int e = 10;
    private Bundle f = null;

    private e() {
    }

    private Bundle a(b bVar) {
        if (bVar == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", bVar.a);
        bundle.putString("uid", bVar.d);
        if (bVar.b != null) {
            bundle.putInt(GroupChatInvitation.ELEMENT_NAME, bVar.b.a());
            bundle.putInt("y", bVar.b.b());
        }
        bundle.putString("keyword", bVar.c);
        return bundle;
    }

    public static e a() {
        if (b == null) {
            b = new e();
            if (!b.d()) {
                b = null;
                return null;
            }
        }
        return b;
    }

    public static void b() {
        if (b != null) {
            if (b.a != null) {
                if (b.d != null) {
                    com.baidu.platform.comjni.engine.a.b(2000, b.d);
                    b.d = null;
                }
                b.a.c();
                b.a = null;
                b.f = null;
                b.c.a();
                b.c = null;
            }
            b = null;
        }
    }

    @SuppressLint({"HandlerLeak"})
    private boolean d() {
        if (this.a != null) {
            return true;
        }
        this.a = new a();
        if (this.a.a() == 0) {
            this.a = null;
            return false;
        }
        this.c = new d();
        this.d = new f(this);
        com.baidu.platform.comjni.engine.a.a(2000, this.d);
        this.c.a(this);
        return true;
    }

    private Bundle e() {
        if (this.f == null) {
            this.f = new Bundle();
        } else {
            this.f.clear();
        }
        return this.f;
    }

    public void a(int i) {
        if (i > 0 && i <= 50) {
            this.e = i;
        }
    }

    public void a(c cVar) {
        this.c.a(cVar);
    }

    public boolean a(b bVar, b bVar2, String str, b bVar3, int i, int i2, Map<String, Object> map) {
        if (str == null || str.equals("")) {
            return false;
        }
        Bundle e = e();
        Bundle a = a(bVar);
        Bundle a2 = a(bVar2);
        if (a == null || a2 == null) {
            return false;
        }
        e.putBundle("start", a);
        e.putBundle("end", a2);
        if (i2 < 3 || i2 > 6) {
            return false;
        }
        e.putInt("strategy", i2);
        e.putString("cityid", str);
        if (!(bVar3 == null || bVar3.a == null || bVar3.b == null)) {
            Bundle bundle = new Bundle();
            bundle.putInt("level", i);
            bundle.putInt("ll_x", bVar3.a.a());
            bundle.putInt("ll_y", bVar3.a.b());
            bundle.putInt("ru_x", bVar3.b.a());
            bundle.putInt("ru_y", bVar3.b.b());
            e.putBundle("mapbound", bundle);
        }
        if (map != null) {
            a = new Bundle();
            for (String str2 : map.keySet()) {
                a.putString(str2.toString(), map.get(str2).toString());
            }
            e.putBundle("extparams", a);
        }
        return this.a.d(e);
    }

    /* JADX WARNING: Missing block: B:13:0x0024, code skipped:
            if (r16.equals("") != false) goto L_0x0026;
     */
    public boolean a(com.baidu.platform.comapi.b.b r12, com.baidu.platform.comapi.b.b r13, java.lang.String r14, java.lang.String r15, java.lang.String r16, com.baidu.platform.comapi.basestruct.b r17, int r18, int r19, int r20, java.util.ArrayList<com.baidu.platform.comapi.b.g> r21, java.util.Map<java.lang.String, java.lang.Object> r22) {
        /*
        r11 = this;
        if (r12 == 0) goto L_0x0004;
    L_0x0002:
        if (r13 != 0) goto L_0x0006;
    L_0x0004:
        r1 = 0;
    L_0x0005:
        return r1;
    L_0x0006:
        r1 = r12.b;
        if (r1 != 0) goto L_0x0016;
    L_0x000a:
        if (r15 == 0) goto L_0x0014;
    L_0x000c:
        r1 = "";
        r1 = r15.equals(r1);
        if (r1 == 0) goto L_0x0016;
    L_0x0014:
        r1 = 0;
        goto L_0x0005;
    L_0x0016:
        r1 = r13.b;
        if (r1 != 0) goto L_0x0028;
    L_0x001a:
        if (r16 == 0) goto L_0x0026;
    L_0x001c:
        r1 = "";
        r0 = r16;
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0028;
    L_0x0026:
        r1 = 0;
        goto L_0x0005;
    L_0x0028:
        r6 = r11.e();
        r1 = "starttype";
        r2 = r12.a;
        r6.putInt(r1, r2);
        r1 = r12.b;
        if (r1 == 0) goto L_0x004d;
    L_0x0037:
        r1 = "startptx";
        r2 = r12.b;
        r2 = r2.a();
        r6.putInt(r1, r2);
        r1 = "startpty";
        r2 = r12.b;
        r2 = r2.b();
        r6.putInt(r1, r2);
    L_0x004d:
        r1 = "startkeyword";
        r2 = r12.c;
        r6.putString(r1, r2);
        r1 = "startuid";
        r2 = r12.d;
        r6.putString(r1, r2);
        r1 = "endtype";
        r2 = r13.a;
        r6.putInt(r1, r2);
        r1 = r13.b;
        if (r1 == 0) goto L_0x007c;
    L_0x0066:
        r1 = "endptx";
        r2 = r13.b;
        r2 = r2.a();
        r6.putInt(r1, r2);
        r1 = "endpty";
        r2 = r13.b;
        r2 = r2.b();
        r6.putInt(r1, r2);
    L_0x007c:
        r1 = "endkeyword";
        r2 = r13.c;
        r6.putString(r1, r2);
        r1 = "enduid";
        r2 = r13.d;
        r6.putString(r1, r2);
        r1 = "level";
        r0 = r18;
        r6.putInt(r1, r0);
        if (r17 == 0) goto L_0x00d3;
    L_0x0093:
        r0 = r17;
        r1 = r0.a;
        if (r1 == 0) goto L_0x00d3;
    L_0x0099:
        r0 = r17;
        r1 = r0.b;
        if (r1 == 0) goto L_0x00d3;
    L_0x009f:
        r1 = "ll_x";
        r0 = r17;
        r2 = r0.a;
        r2 = r2.a();
        r6.putInt(r1, r2);
        r1 = "ll_y";
        r0 = r17;
        r2 = r0.a;
        r2 = r2.b();
        r6.putInt(r1, r2);
        r1 = "ru_x";
        r0 = r17;
        r2 = r0.b;
        r2 = r2.a();
        r6.putInt(r1, r2);
        r1 = "ru_y";
        r0 = r17;
        r2 = r0.b;
        r2 = r2.b();
        r6.putInt(r1, r2);
    L_0x00d3:
        r1 = "strategy";
        r0 = r19;
        r6.putInt(r1, r0);
        r1 = "cityid";
        r6.putString(r1, r14);
        r1 = "st_cityid";
        r6.putString(r1, r15);
        r1 = "en_cityid";
        r0 = r16;
        r6.putString(r1, r0);
        r1 = "traffic";
        r0 = r20;
        r6.putInt(r1, r0);
        if (r21 == 0) goto L_0x01e8;
    L_0x00f4:
        r7 = r21.size();
        r3 = 0;
        r4 = "";
        r2 = "";
        r1 = 0;
        r5 = r1;
    L_0x00ff:
        if (r5 >= r7) goto L_0x01de;
    L_0x0101:
        r8 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x01da }
        r8.<init>();	 Catch:{ JSONException -> 0x01da }
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.a;	 Catch:{ JSONException -> 0x01da }
        if (r1 == 0) goto L_0x01d2;
    L_0x0112:
        r1 = "type";
        r9 = 1;
        r8.put(r1, r9);	 Catch:{ JSONException -> 0x01da }
    L_0x0118:
        r9 = "keyword";
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.b;	 Catch:{ JSONException -> 0x01da }
        r8.put(r9, r1);	 Catch:{ JSONException -> 0x01da }
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.a;	 Catch:{ JSONException -> 0x01da }
        if (r1 == 0) goto L_0x016f;
    L_0x0133:
        r9 = "xy";
        r10 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x01da }
        r10.<init>();	 Catch:{ JSONException -> 0x01da }
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.a;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.a;	 Catch:{ JSONException -> 0x01da }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ JSONException -> 0x01da }
        r1 = r10.append(r1);	 Catch:{ JSONException -> 0x01da }
        r10 = ",";
        r10 = r1.append(r10);	 Catch:{ JSONException -> 0x01da }
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.a;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.b;	 Catch:{ JSONException -> 0x01da }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ JSONException -> 0x01da }
        r1 = r10.append(r1);	 Catch:{ JSONException -> 0x01da }
        r1 = r1.toString();	 Catch:{ JSONException -> 0x01da }
        r8.put(r9, r1);	 Catch:{ JSONException -> 0x01da }
    L_0x016f:
        r1 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x01da }
        r1.<init>();	 Catch:{ JSONException -> 0x01da }
        r9 = r1.append(r2);	 Catch:{ JSONException -> 0x01da }
        r0 = r21;
        r1 = r0.get(r5);	 Catch:{ JSONException -> 0x01da }
        r1 = (com.baidu.platform.comapi.b.g) r1;	 Catch:{ JSONException -> 0x01da }
        r1 = r1.c;	 Catch:{ JSONException -> 0x01da }
        r1 = r9.append(r1);	 Catch:{ JSONException -> 0x01da }
        r1 = r1.toString();	 Catch:{ JSONException -> 0x01da }
        r2 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0224 }
        r2.<init>();	 Catch:{ JSONException -> 0x0224 }
        r2 = r2.append(r4);	 Catch:{ JSONException -> 0x0224 }
        r8 = r8.toString();	 Catch:{ JSONException -> 0x0224 }
        r2 = r2.append(r8);	 Catch:{ JSONException -> 0x0224 }
        r2 = r2.toString();	 Catch:{ JSONException -> 0x0224 }
        r4 = r7 + -1;
        if (r3 == r4) goto L_0x01c9;
    L_0x01a3:
        r4 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0222 }
        r4.<init>();	 Catch:{ JSONException -> 0x0222 }
        r4 = r4.append(r2);	 Catch:{ JSONException -> 0x0222 }
        r8 = "|";
        r4 = r4.append(r8);	 Catch:{ JSONException -> 0x0222 }
        r2 = r4.toString();	 Catch:{ JSONException -> 0x0222 }
        r4 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0222 }
        r4.<init>();	 Catch:{ JSONException -> 0x0222 }
        r4 = r4.append(r1);	 Catch:{ JSONException -> 0x0222 }
        r8 = "|";
        r4 = r4.append(r8);	 Catch:{ JSONException -> 0x0222 }
        r1 = r4.toString();	 Catch:{ JSONException -> 0x0222 }
    L_0x01c9:
        r3 = r3 + 1;
    L_0x01cb:
        r4 = r5 + 1;
        r5 = r4;
        r4 = r2;
        r2 = r1;
        goto L_0x00ff;
    L_0x01d2:
        r1 = "type";
        r9 = 2;
        r8.put(r1, r9);	 Catch:{ JSONException -> 0x01da }
        goto L_0x0118;
    L_0x01da:
        r1 = move-exception;
        r1 = r2;
        r2 = r4;
        goto L_0x01cb;
    L_0x01de:
        r1 = "wp";
        r6.putString(r1, r4);
        r1 = "wpc";
        r6.putString(r1, r2);
    L_0x01e8:
        if (r22 == 0) goto L_0x021a;
    L_0x01ea:
        r2 = new android.os.Bundle;
        r2.<init>();
        r1 = r22.keySet();
        r3 = r1.iterator();
    L_0x01f7:
        r1 = r3.hasNext();
        if (r1 == 0) goto L_0x0215;
    L_0x01fd:
        r1 = r3.next();
        r1 = (java.lang.String) r1;
        r0 = r22;
        r4 = r0.get(r1);
        r1 = r1.toString();
        r4 = r4.toString();
        r2.putString(r1, r4);
        goto L_0x01f7;
    L_0x0215:
        r1 = "extparams";
        r6.putBundle(r1, r2);
    L_0x021a:
        r1 = r11.a;
        r1 = r1.e(r6);
        goto L_0x0005;
    L_0x0222:
        r4 = move-exception;
        goto L_0x01cb;
    L_0x0224:
        r2 = move-exception;
        r2 = r4;
        goto L_0x01cb;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.b.e.a(com.baidu.platform.comapi.b.b, com.baidu.platform.comapi.b.b, java.lang.String, java.lang.String, java.lang.String, com.baidu.platform.comapi.basestruct.b, int, int, int, java.util.ArrayList, java.util.Map):boolean");
    }

    public boolean a(b bVar, b bVar2, String str, String str2, String str3, b bVar3, int i, Map<String, Object> map) {
        if (bVar == null || bVar2 == null) {
            return false;
        }
        if (bVar.b == null && (str2 == null || str2.equals(""))) {
            return false;
        }
        if (bVar2.b == null && (str3 == null || str3.equals(""))) {
            return false;
        }
        Bundle e = e();
        e.putInt("starttype", bVar.a);
        if (bVar.b != null) {
            e.putInt("startptx", bVar.b.a());
            e.putInt("startpty", bVar.b.b());
        }
        e.putString("startkeyword", bVar.c);
        e.putString("startuid", bVar.d);
        e.putInt("endtype", bVar2.a);
        if (bVar2.b != null) {
            e.putInt("endptx", bVar2.b.a());
            e.putInt("endpty", bVar2.b.b());
        }
        e.putString("endkeyword", bVar2.c);
        e.putString("enduid", bVar2.d);
        e.putInt("level", i);
        if (!(bVar3 == null || bVar3.a == null || bVar3.b == null)) {
            e.putInt("ll_x", bVar3.a.a());
            e.putInt("ll_y", bVar3.a.b());
            e.putInt("ru_x", bVar3.b.a());
            e.putInt("ru_y", bVar3.b.b());
        }
        e.putString("cityid", str);
        e.putString("st_cityid", str2);
        e.putString("en_cityid", str3);
        if (map != null) {
            Bundle bundle = new Bundle();
            for (String str4 : map.keySet()) {
                bundle.putString(str4.toString(), map.get(str4).toString());
            }
            e.putBundle("extparams", bundle);
        }
        return this.a.f(e);
    }

    public boolean a(c cVar) {
        if (cVar == null) {
            return false;
        }
        int b = cVar.b();
        return this.a.a(cVar.a(), b);
    }

    public boolean a(c cVar, String str, String str2) {
        return (cVar == null || str == null || str2 == null) ? false : this.a.a(cVar.a(), cVar.b(), str, str2);
    }

    public boolean a(String str) {
        if (str == null) {
            return false;
        }
        String trim = str.trim();
        return (trim.length() == 0 || trim.length() > 99) ? false : this.a.a(trim);
    }

    public boolean a(String str, int i, int i2, int i3, b bVar, b bVar2, Map<String, Object> map) {
        if (str == null) {
            return false;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return false;
        }
        Bundle e = e();
        e.putString("keyword", trim);
        e.putInt("pagenum", i2);
        e.putInt("count", this.e);
        e.putInt("cityid", i);
        e.putInt("level", i3);
        if (bVar2 != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("ll_x", bVar2.a.a());
            bundle.putInt("ll_y", bVar2.a.b());
            bundle.putInt("ru_x", bVar2.b.a());
            bundle.putInt("ru_y", bVar2.b.b());
            e.putBundle("mapbound", bundle);
        }
        if (bVar != null) {
            e.putInt("ll_x", bVar.a.a());
            e.putInt("ll_y", bVar.a.b());
            e.putInt("ru_x", bVar.b.a());
            e.putInt("ru_y", bVar.b.b());
            e.putInt("loc_x", (bVar.a.a() + bVar.b.a()) / 2);
            e.putInt("loc_y", (bVar.a.b() + bVar.b.b()) / 2);
        }
        if (map != null) {
            Bundle bundle2 = new Bundle();
            for (String str2 : map.keySet()) {
                bundle2.putString(str2.toString(), map.get(str2).toString());
            }
            e.putBundle("extparams", bundle2);
        }
        return this.a.b(e);
    }

    public boolean a(String str, int i, int i2, b bVar, int i3, c cVar, Map<String, Object> map) {
        if (bVar == null || str == null) {
            return false;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return false;
        }
        Bundle e = e();
        e.putString("keyword", trim);
        e.putInt("pagenum", i2);
        e.putInt("count", this.e);
        e.putString("cityid", String.valueOf(i));
        e.putInt("level", i3);
        if (bVar != null) {
            e.putInt("ll_x", bVar.a.a());
            e.putInt("ll_y", bVar.a.b());
            e.putInt("ru_x", bVar.b.a());
            e.putInt("ru_y", bVar.b.b());
        }
        if (cVar != null) {
            e.putInt("loc_x", cVar.a);
            e.putInt("loc_y", cVar.b);
        }
        if (map != null) {
            Bundle bundle = new Bundle();
            for (String str2 : map.keySet()) {
                bundle.putString(str2.toString(), map.get(str2).toString());
            }
            e.putBundle("extparams", bundle);
        }
        return this.a.h(e);
    }

    public boolean a(String str, int i, String str2, b bVar, int i2, c cVar) {
        if (str == null) {
            return false;
        }
        if (i != 0 && i != 2) {
            return false;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return false;
        }
        Bundle e = e();
        e.putString("keyword", str);
        e.putInt("type", i);
        e.putString("cityid", str2);
        Bundle bundle = new Bundle();
        bundle.putInt("level", i2);
        e.putBundle("mapbound", bundle);
        if (cVar != null) {
            e.putInt("loc_x", cVar.a);
            e.putInt("loc_y", cVar.b);
        }
        return this.a.g(e);
    }

    public boolean a(String str, String str2) {
        if (str2 == null || str == null || str.equals("")) {
            return false;
        }
        String trim = str2.trim();
        return (trim.length() == 0 || trim.length() > 99) ? false : this.a.a(str, trim);
    }

    public boolean a(String str, String str2, int i, b bVar, int i2, Map<String, Object> map) {
        if (str == null) {
            return false;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return false;
        }
        Bundle e = e();
        e.putString("keyword", trim);
        e.putInt("pagenum", i);
        e.putInt("count", this.e);
        e.putString("cityid", str2);
        e.putInt("level", i2);
        if (bVar != null) {
            e.putInt("ll_x", bVar.a.a());
            e.putInt("ll_y", bVar.a.b());
            e.putInt("ru_x", bVar.b.a());
            e.putInt("ru_y", bVar.b.b());
        }
        if (map != null) {
            Bundle bundle = new Bundle();
            for (String str3 : map.keySet()) {
                bundle.putString(str3.toString(), map.get(str3).toString());
            }
            e.putBundle("extparams", bundle);
        }
        return this.a.a(e);
    }

    public boolean a(String[] strArr, int i, int i2, int i3, int i4, b bVar, b bVar2, Map<String, Object> map) {
        if (strArr == null || strArr.length < 2 || strArr.length > 10) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i5 = 0;
        while (i5 < strArr.length) {
            if (strArr[i5] == null) {
                return false;
            }
            String trim = strArr[i5].trim();
            if (trim.length() == 0 || trim.length() > 99 || strArr[i5].contains("$$")) {
                return false;
            }
            stringBuilder.append(trim);
            if (i5 != strArr.length - 1) {
                stringBuilder.append("$$");
            }
            i5++;
        }
        if (stringBuilder.toString().length() > 99) {
            return false;
        }
        Bundle e = e();
        e.putString("keyword", stringBuilder.toString());
        e.putInt("pagenum", i2);
        e.putInt("count", this.e);
        e.putInt("cityid", i);
        e.putInt("level", i3);
        e.putInt("radius", i4);
        if (bVar2 != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("ll_x", bVar2.a.a());
            bundle.putInt("ll_y", bVar2.a.b());
            bundle.putInt("ru_x", bVar2.b.a());
            bundle.putInt("ru_y", bVar2.b.b());
            e.putBundle("mapbound", bundle);
        }
        if (bVar != null) {
            e.putInt("ll_x", bVar.a.a());
            e.putInt("ll_y", bVar.a.b());
            e.putInt("ru_x", bVar.b.a());
            e.putInt("ru_y", bVar.b.b());
            e.putInt("loc_x", (bVar.a.a() + bVar.b.a()) / 2);
            e.putInt("loc_y", (bVar.a.b() + bVar.b.b()) / 2);
        }
        if (map != null) {
            Bundle bundle2 = new Bundle();
            for (String str : map.keySet()) {
                bundle2.putString(str.toString(), map.get(str).toString());
            }
            e.putBundle("extparams", bundle2);
        }
        return this.a.c(e);
    }

    /* access modifiers changed from: 0000 */
    public String b(int i) {
        String a = this.a.a(i);
        return (a == null || a.trim().length() > 0) ? a : null;
    }

    public boolean b(String str) {
        return str == null ? false : this.a.b(str);
    }

    public boolean b(String str, String str2) {
        return this.a.b(str, str2);
    }

    public int c() {
        return this.e;
    }
}
