package com.baidu.android.pushservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.security.AESUtil;
import com.baidu.android.common.security.Base64;
import com.baidu.android.pushservice.a.g;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class a {
    private static volatile a c;
    public ArrayList a = new ArrayList();
    public ArrayList b = new ArrayList();
    private Context d;
    private HashMap e = new HashMap();

    private a(Context context) {
        ArrayList d;
        this.d = context.getApplicationContext();
        c(this.d);
        d(this.d);
        SharedPreferences sharedPreferences = this.d.getSharedPreferences(this.d.getPackageName() + ".push_sync", 1);
        String string = sharedPreferences.getString("r", "");
        if (!TextUtils.isEmpty(string)) {
            try {
                String str = new String(AESUtil.decrypt("2011121211143000", "1234567890123456", Base64.decode(string.getBytes())));
                if (b.a()) {
                    Log.i("ClientManager", "ClientManager init strApps : " + str);
                }
                d = d(str);
                if (d != null) {
                    Iterator it = d.iterator();
                    while (it.hasNext()) {
                        this.a.add((d) it.next());
                    }
                }
            } catch (Exception e) {
                Log.e("ClientManager", e);
            }
        } else if (b.a()) {
            Log.i("ClientManager", "ClientManager init strApps empty.");
        }
        string = sharedPreferences.getString("r_v2", "");
        if (!TextUtils.isEmpty(string)) {
            try {
                String str2 = new String(AESUtil.decrypt("2011121211143000", "1234567890123456", Base64.decode(string.getBytes())));
                if (b.a()) {
                    Log.i("ClientManager", "ClientManager init strAppsV2 : " + str2);
                }
                d = d(str2);
                if (d != null) {
                    Iterator it2 = d.iterator();
                    while (it2.hasNext()) {
                        this.b.add((d) it2.next());
                    }
                }
            } catch (Exception e2) {
                Log.e("ClientManager", e2);
            }
        } else if (b.a()) {
            Log.i("ClientManager", "ClientManager init strAppsV2 empty.");
        }
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (c == null) {
                c = new a(context);
            }
            aVar = c;
        }
        return aVar;
    }

    static String a(List list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                return stringBuffer.toString();
            }
            d dVar = (d) list.get(i2);
            stringBuffer.append(dVar.a);
            stringBuffer.append(",");
            stringBuffer.append(dVar.b);
            stringBuffer.append(",");
            stringBuffer.append(dVar.c);
            if (i2 != list.size() - 1) {
                stringBuffer.append(";");
            }
            i = i2 + 1;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x010c A:{Catch:{ Exception -> 0x00f5 }} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a4 A:{SYNTHETIC, Splitter:B:25:0x00a4} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00a4 A:{SYNTHETIC, Splitter:B:25:0x00a4} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x010c A:{Catch:{ Exception -> 0x00f5 }} */
    public static synchronized void b(android.content.Context r7) {
        /*
        r3 = com.baidu.android.pushservice.a.class;
        monitor-enter(r3);
        r0 = c;	 Catch:{ all -> 0x011a }
        if (r0 == 0) goto L_0x00fb;
    L_0x0007:
        r0 = c;	 Catch:{ all -> 0x011a }
        r0 = r0.a;	 Catch:{ all -> 0x011a }
        r0.clear();	 Catch:{ all -> 0x011a }
        r0 = c;	 Catch:{ all -> 0x011a }
        r0 = r0.b;	 Catch:{ all -> 0x011a }
        r0.clear();	 Catch:{ all -> 0x011a }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x011a }
        r0.<init>();	 Catch:{ all -> 0x011a }
        r1 = r7.getPackageName();	 Catch:{ all -> 0x011a }
        r0 = r0.append(r1);	 Catch:{ all -> 0x011a }
        r1 = ".push_sync";
        r0 = r0.append(r1);	 Catch:{ all -> 0x011a }
        r0 = r0.toString();	 Catch:{ all -> 0x011a }
        r1 = 1;
        r4 = r7.getSharedPreferences(r0, r1);	 Catch:{ all -> 0x011a }
        r0 = "r";
        r1 = "";
        r2 = r4.getString(r0, r1);	 Catch:{ all -> 0x011a }
        r0 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x011a }
        if (r0 != 0) goto L_0x00fd;
    L_0x003f:
        r0 = r2.getBytes();	 Catch:{ Exception -> 0x011d }
        r0 = com.baidu.android.common.security.Base64.decode(r0);	 Catch:{ Exception -> 0x011d }
        r1 = new java.lang.String;	 Catch:{ Exception -> 0x011d }
        r5 = "2011121211143000";
        r6 = "1234567890123456";
        r0 = com.baidu.android.common.security.AESUtil.decrypt(r5, r6, r0);	 Catch:{ Exception -> 0x011d }
        r1.<init>(r0);	 Catch:{ Exception -> 0x011d }
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ Exception -> 0x0090 }
        if (r0 == 0) goto L_0x0072;
    L_0x005a:
        r0 = "ClientManager";
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0090 }
        r2.<init>();	 Catch:{ Exception -> 0x0090 }
        r5 = "ClientManager init strApps : ";
        r2 = r2.append(r5);	 Catch:{ Exception -> 0x0090 }
        r2 = r2.append(r1);	 Catch:{ Exception -> 0x0090 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0090 }
        com.baidu.android.common.logging.Log.i(r0, r2);	 Catch:{ Exception -> 0x0090 }
    L_0x0072:
        r0 = d(r1);	 Catch:{ Exception -> 0x0090 }
        if (r0 == 0) goto L_0x0096;
    L_0x0078:
        r2 = r0.iterator();	 Catch:{ Exception -> 0x0090 }
    L_0x007c:
        r0 = r2.hasNext();	 Catch:{ Exception -> 0x0090 }
        if (r0 == 0) goto L_0x0096;
    L_0x0082:
        r0 = r2.next();	 Catch:{ Exception -> 0x0090 }
        r0 = (com.baidu.android.pushservice.d) r0;	 Catch:{ Exception -> 0x0090 }
        r5 = c;	 Catch:{ Exception -> 0x0090 }
        r5 = r5.a;	 Catch:{ Exception -> 0x0090 }
        r5.add(r0);	 Catch:{ Exception -> 0x0090 }
        goto L_0x007c;
    L_0x0090:
        r0 = move-exception;
    L_0x0091:
        r2 = "ClientManager";
        com.baidu.android.common.logging.Log.e(r2, r0);	 Catch:{ all -> 0x011a }
    L_0x0096:
        r0 = "r_v2";
        r2 = "";
        r0 = r4.getString(r0, r2);	 Catch:{ all -> 0x011a }
        r2 = android.text.TextUtils.isEmpty(r0);	 Catch:{ all -> 0x011a }
        if (r2 != 0) goto L_0x010c;
    L_0x00a4:
        r0 = r0.getBytes();	 Catch:{ Exception -> 0x00f5 }
        r0 = com.baidu.android.common.security.Base64.decode(r0);	 Catch:{ Exception -> 0x00f5 }
        r2 = new java.lang.String;	 Catch:{ Exception -> 0x00f5 }
        r4 = "2011121211143000";
        r5 = "1234567890123456";
        r0 = com.baidu.android.common.security.AESUtil.decrypt(r4, r5, r0);	 Catch:{ Exception -> 0x00f5 }
        r2.<init>(r0);	 Catch:{ Exception -> 0x00f5 }
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ Exception -> 0x00f5 }
        if (r0 == 0) goto L_0x00d7;
    L_0x00bf:
        r0 = "ClientManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00f5 }
        r4.<init>();	 Catch:{ Exception -> 0x00f5 }
        r5 = "ClientManager init strApps : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x00f5 }
        r1 = r4.append(r1);	 Catch:{ Exception -> 0x00f5 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00f5 }
        com.baidu.android.common.logging.Log.i(r0, r1);	 Catch:{ Exception -> 0x00f5 }
    L_0x00d7:
        r0 = d(r2);	 Catch:{ Exception -> 0x00f5 }
        if (r0 == 0) goto L_0x00fb;
    L_0x00dd:
        r1 = r0.iterator();	 Catch:{ Exception -> 0x00f5 }
    L_0x00e1:
        r0 = r1.hasNext();	 Catch:{ Exception -> 0x00f5 }
        if (r0 == 0) goto L_0x00fb;
    L_0x00e7:
        r0 = r1.next();	 Catch:{ Exception -> 0x00f5 }
        r0 = (com.baidu.android.pushservice.d) r0;	 Catch:{ Exception -> 0x00f5 }
        r2 = c;	 Catch:{ Exception -> 0x00f5 }
        r2 = r2.b;	 Catch:{ Exception -> 0x00f5 }
        r2.add(r0);	 Catch:{ Exception -> 0x00f5 }
        goto L_0x00e1;
    L_0x00f5:
        r0 = move-exception;
        r1 = "ClientManager";
        com.baidu.android.common.logging.Log.e(r1, r0);	 Catch:{ all -> 0x011a }
    L_0x00fb:
        monitor-exit(r3);
        return;
    L_0x00fd:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x011a }
        if (r0 == 0) goto L_0x010a;
    L_0x0103:
        r0 = "ClientManager";
        r1 = "ClientManager init strApps empty.";
        com.baidu.android.common.logging.Log.i(r0, r1);	 Catch:{ all -> 0x011a }
    L_0x010a:
        r1 = r2;
        goto L_0x0096;
    L_0x010c:
        r0 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x011a }
        if (r0 == 0) goto L_0x00fb;
    L_0x0112:
        r0 = "ClientManager";
        r1 = "ClientManager init strAppsV2 empty.";
        com.baidu.android.common.logging.Log.i(r0, r1);	 Catch:{ all -> 0x011a }
        goto L_0x00fb;
    L_0x011a:
        r0 = move-exception;
        monitor-exit(r3);
        throw r0;
    L_0x011d:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0091;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.b(android.content.Context):void");
    }

    private static String c(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1);
        if (!"nodata".equals(sharedPreferences.getString("r", "nodata"))) {
            return null;
        }
        for (ResolveInfo resolveInfo : context.getPackageManager().queryBroadcastReceivers(new Intent("com.baidu.android.pushservice.action.BIND_SYNC"), 0)) {
            d dVar = new d();
            dVar.a = resolveInfo.activityInfo.packageName;
            try {
                String string = context.createPackageContext(dVar.a, 2).getSharedPreferences(dVar.a + ".push_sync", 1).getString("r", "nodata");
                if (!"nodata".equals(string)) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("r", string);
                    edit.commit();
                    return dVar.a;
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String d(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + ".push_sync", 1);
        if (!"nodata".equals(sharedPreferences.getString("r_v2", "nodata"))) {
            return null;
        }
        for (ResolveInfo resolveInfo : context.getPackageManager().queryBroadcastReceivers(new Intent("com.baidu.android.pushservice.action.BIND_SYNC"), 0)) {
            d dVar = new d();
            dVar.a = resolveInfo.activityInfo.packageName;
            try {
                String string = context.createPackageContext(dVar.a, 2).getSharedPreferences(dVar.a + ".push_sync", 1).getString("r_v2", "nodata");
                if (!"nodata".equals(string)) {
                    Editor edit = sharedPreferences.edit();
                    edit.putString("r_v2", string);
                    edit.commit();
                    return dVar.a;
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static ArrayList d(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String trim : str.trim().split(";")) {
            String[] split = trim.trim().split(",");
            if (split.length >= 3) {
                d dVar = new d();
                dVar.a = split[0].trim();
                dVar.b = split[1].trim();
                dVar.c = split[2].trim();
                arrayList.add(dVar);
            }
        }
        return arrayList;
    }

    public d a(String str) {
        d dVar;
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            dVar = (d) it.next();
            if (dVar.a.equals(str)) {
                return dVar;
            }
        }
        it = this.a.iterator();
        while (it.hasNext()) {
            dVar = (d) it.next();
            if (dVar.a.equals(str)) {
                return dVar;
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00f9 A:{Catch:{ Exception -> 0x0131 }} */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public java.lang.String a(com.baidu.android.pushservice.d r9, boolean r10) {
        /*
        r8 = this;
        r1 = 1;
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x0029;
    L_0x0007:
        r0 = "ClientManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "sync addOrRemove:";
        r2 = r2.append(r3);
        r2 = r2.append(r10);
        r3 = ", ";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r2 = r2.toString();
        com.baidu.android.common.logging.Log.d(r0, r2);
    L_0x0029:
        r3 = r8.a;
        monitor-enter(r3);
        r2 = 0;
        r0 = "ClientManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r4.<init>();	 Catch:{ all -> 0x013b }
        r5 = "client.packageName=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.a;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = " client.appId=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.b;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = " client.userId=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.c;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r4 = r4.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.d(r0, r4);	 Catch:{ all -> 0x013b }
        r0 = r8.a;	 Catch:{ all -> 0x013b }
        r4 = r0.iterator();	 Catch:{ all -> 0x013b }
    L_0x0065:
        r0 = r4.hasNext();	 Catch:{ all -> 0x013b }
        if (r0 == 0) goto L_0x013e;
    L_0x006b:
        r0 = r4.next();	 Catch:{ all -> 0x013b }
        r0 = (com.baidu.android.pushservice.d) r0;	 Catch:{ all -> 0x013b }
        r5 = "ClientManager";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r6.<init>();	 Catch:{ all -> 0x013b }
        r7 = "c.packageName=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.a;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = " c.appId=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.b;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = " c.userId=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.c;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r6 = r6.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.d(r5, r6);	 Catch:{ all -> 0x013b }
        r5 = r0.a;	 Catch:{ all -> 0x013b }
        r6 = r9.a;	 Catch:{ all -> 0x013b }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x013b }
        if (r5 != 0) goto L_0x00b7;
    L_0x00ad:
        r5 = r0.b;	 Catch:{ all -> 0x013b }
        r6 = r9.b;	 Catch:{ all -> 0x013b }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x013b }
        if (r5 == 0) goto L_0x0065;
    L_0x00b7:
        r2 = r8.a;	 Catch:{ all -> 0x013b }
        r2.remove(r0);	 Catch:{ all -> 0x013b }
        if (r10 == 0) goto L_0x00c3;
    L_0x00be:
        r0 = r8.a;	 Catch:{ all -> 0x013b }
        r0.add(r9);	 Catch:{ all -> 0x013b }
    L_0x00c3:
        r0 = r1;
    L_0x00c4:
        if (r0 != 0) goto L_0x00cd;
    L_0x00c6:
        if (r10 == 0) goto L_0x00cd;
    L_0x00c8:
        r0 = r8.a;	 Catch:{ all -> 0x013b }
        r0.add(r9);	 Catch:{ all -> 0x013b }
    L_0x00cd:
        r0 = r8.d;	 Catch:{ all -> 0x013b }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r1.<init>();	 Catch:{ all -> 0x013b }
        r2 = r8.d;	 Catch:{ all -> 0x013b }
        r2 = r2.getPackageName();	 Catch:{ all -> 0x013b }
        r1 = r1.append(r2);	 Catch:{ all -> 0x013b }
        r2 = ".push_sync";
        r1 = r1.append(r2);	 Catch:{ all -> 0x013b }
        r1 = r1.toString();	 Catch:{ all -> 0x013b }
        r2 = 1;
        r1 = r0.getSharedPreferences(r1, r2);	 Catch:{ all -> 0x013b }
        r0 = r8.a;	 Catch:{ all -> 0x013b }
        r0 = a(r0);	 Catch:{ all -> 0x013b }
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x013b }
        if (r2 == 0) goto L_0x0111;
    L_0x00f9:
        r2 = "ClientManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r4.<init>();	 Catch:{ all -> 0x013b }
        r5 = "sync  strApps: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r4 = r4.append(r0);	 Catch:{ all -> 0x013b }
        r4 = r4.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.i(r2, r4);	 Catch:{ all -> 0x013b }
    L_0x0111:
        r2 = "2011121211143000";
        r4 = "1234567890123456";
        r0 = r0.getBytes();	 Catch:{ Exception -> 0x0131 }
        r0 = com.baidu.android.common.security.AESUtil.encrypt(r2, r4, r0);	 Catch:{ Exception -> 0x0131 }
        r2 = "utf-8";
        r0 = com.baidu.android.common.security.Base64.encode(r0, r2);	 Catch:{ Exception -> 0x0131 }
        r1 = r1.edit();	 Catch:{ Exception -> 0x0131 }
        r2 = "r";
        r1.putString(r2, r0);	 Catch:{ Exception -> 0x0131 }
        r1.commit();	 Catch:{ Exception -> 0x0131 }
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
    L_0x0130:
        return r0;
    L_0x0131:
        r0 = move-exception;
        r1 = "ClientManager";
        com.baidu.android.common.logging.Log.e(r1, r0);	 Catch:{ all -> 0x013b }
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
        r0 = "";
        goto L_0x0130;
    L_0x013b:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
        throw r0;
    L_0x013e:
        r0 = r2;
        goto L_0x00c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.a(com.baidu.android.pushservice.d, boolean):java.lang.String");
    }

    public void a(String str, g gVar) {
        this.e.put(str, gVar);
    }

    public boolean a(String str, String str2) {
        return (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) ? false : this.e.containsKey(str) && str2.equals(((g) this.e.get(str)).a());
    }

    public d b(String str) {
        d dVar;
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            dVar = (d) it.next();
            if (dVar.b.equals(str)) {
                return dVar;
            }
        }
        it = this.a.iterator();
        while (it.hasNext()) {
            dVar = (d) it.next();
            if (dVar.b.equals(str)) {
                return dVar;
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00f9 A:{Catch:{ Exception -> 0x0131 }} */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public java.lang.String b(com.baidu.android.pushservice.d r9, boolean r10) {
        /*
        r8 = this;
        r1 = 1;
        r0 = com.baidu.android.pushservice.b.a();
        if (r0 == 0) goto L_0x0029;
    L_0x0007:
        r0 = "ClientManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "syncV2 addOrRemove:";
        r2 = r2.append(r3);
        r2 = r2.append(r10);
        r3 = ", ";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r2 = r2.toString();
        com.baidu.android.common.logging.Log.d(r0, r2);
    L_0x0029:
        r3 = r8.b;
        monitor-enter(r3);
        r2 = 0;
        r0 = "ClientManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r4.<init>();	 Catch:{ all -> 0x013b }
        r5 = "client.packageName=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.a;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = " client.appId=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.b;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = " client.userId=";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r5 = r9.c;	 Catch:{ all -> 0x013b }
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r4 = r4.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.d(r0, r4);	 Catch:{ all -> 0x013b }
        r0 = r8.b;	 Catch:{ all -> 0x013b }
        r4 = r0.iterator();	 Catch:{ all -> 0x013b }
    L_0x0065:
        r0 = r4.hasNext();	 Catch:{ all -> 0x013b }
        if (r0 == 0) goto L_0x013e;
    L_0x006b:
        r0 = r4.next();	 Catch:{ all -> 0x013b }
        r0 = (com.baidu.android.pushservice.d) r0;	 Catch:{ all -> 0x013b }
        r5 = "ClientManager";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r6.<init>();	 Catch:{ all -> 0x013b }
        r7 = "c.packageName=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.a;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = " c.appId=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.b;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = " c.userId=";
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r7 = r0.c;	 Catch:{ all -> 0x013b }
        r6 = r6.append(r7);	 Catch:{ all -> 0x013b }
        r6 = r6.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.d(r5, r6);	 Catch:{ all -> 0x013b }
        r5 = r0.a;	 Catch:{ all -> 0x013b }
        r6 = r9.a;	 Catch:{ all -> 0x013b }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x013b }
        if (r5 != 0) goto L_0x00b7;
    L_0x00ad:
        r5 = r0.b;	 Catch:{ all -> 0x013b }
        r6 = r9.b;	 Catch:{ all -> 0x013b }
        r5 = r5.equals(r6);	 Catch:{ all -> 0x013b }
        if (r5 == 0) goto L_0x0065;
    L_0x00b7:
        r2 = r8.b;	 Catch:{ all -> 0x013b }
        r2.remove(r0);	 Catch:{ all -> 0x013b }
        if (r10 == 0) goto L_0x00c3;
    L_0x00be:
        r0 = r8.b;	 Catch:{ all -> 0x013b }
        r0.add(r9);	 Catch:{ all -> 0x013b }
    L_0x00c3:
        r0 = r1;
    L_0x00c4:
        if (r0 != 0) goto L_0x00cd;
    L_0x00c6:
        if (r10 == 0) goto L_0x00cd;
    L_0x00c8:
        r0 = r8.b;	 Catch:{ all -> 0x013b }
        r0.add(r9);	 Catch:{ all -> 0x013b }
    L_0x00cd:
        r0 = r8.d;	 Catch:{ all -> 0x013b }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r1.<init>();	 Catch:{ all -> 0x013b }
        r2 = r8.d;	 Catch:{ all -> 0x013b }
        r2 = r2.getPackageName();	 Catch:{ all -> 0x013b }
        r1 = r1.append(r2);	 Catch:{ all -> 0x013b }
        r2 = ".push_sync";
        r1 = r1.append(r2);	 Catch:{ all -> 0x013b }
        r1 = r1.toString();	 Catch:{ all -> 0x013b }
        r2 = 1;
        r1 = r0.getSharedPreferences(r1, r2);	 Catch:{ all -> 0x013b }
        r0 = r8.b;	 Catch:{ all -> 0x013b }
        r0 = a(r0);	 Catch:{ all -> 0x013b }
        r2 = com.baidu.android.pushservice.b.a();	 Catch:{ all -> 0x013b }
        if (r2 == 0) goto L_0x0111;
    L_0x00f9:
        r2 = "ClientManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013b }
        r4.<init>();	 Catch:{ all -> 0x013b }
        r5 = "syncV2  strApps: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x013b }
        r4 = r4.append(r0);	 Catch:{ all -> 0x013b }
        r4 = r4.toString();	 Catch:{ all -> 0x013b }
        com.baidu.android.common.logging.Log.i(r2, r4);	 Catch:{ all -> 0x013b }
    L_0x0111:
        r2 = "2011121211143000";
        r4 = "1234567890123456";
        r0 = r0.getBytes();	 Catch:{ Exception -> 0x0131 }
        r0 = com.baidu.android.common.security.AESUtil.encrypt(r2, r4, r0);	 Catch:{ Exception -> 0x0131 }
        r2 = "utf-8";
        r0 = com.baidu.android.common.security.Base64.encode(r0, r2);	 Catch:{ Exception -> 0x0131 }
        r1 = r1.edit();	 Catch:{ Exception -> 0x0131 }
        r2 = "r_v2";
        r1.putString(r2, r0);	 Catch:{ Exception -> 0x0131 }
        r1.commit();	 Catch:{ Exception -> 0x0131 }
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
    L_0x0130:
        return r0;
    L_0x0131:
        r0 = move-exception;
        r1 = "ClientManager";
        com.baidu.android.common.logging.Log.e(r1, r0);	 Catch:{ all -> 0x013b }
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
        r0 = "";
        goto L_0x0130;
    L_0x013b:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x013b }
        throw r0;
    L_0x013e:
        r0 = r2;
        goto L_0x00c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.pushservice.a.b(com.baidu.android.pushservice.d, boolean):java.lang.String");
    }

    public boolean c(String str) {
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            if (((d) it.next()).b.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String e(String str) {
        return this.e.get(str) != null ? ((g) this.e.get(str)).b() : "";
    }

    public void f(String str) {
        if (this.e.containsKey(str)) {
            this.e.remove(str);
        }
    }
}
