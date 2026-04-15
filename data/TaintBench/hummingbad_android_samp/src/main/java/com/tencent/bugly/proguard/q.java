package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.UserInfoBean;
import com.tencent.bugly.crashreport.common.strategy.a;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import java.util.List;

/* compiled from: BUGLY */
public class q {
    private static q a = null;
    private static r b = null;

    private q(Context context) {
        b = new r(context);
    }

    public static synchronized q a(Context context) {
        q qVar;
        synchronized (q.class) {
            if (a == null) {
                a = new q(context);
            }
            qVar = a;
        }
        return qVar;
    }

    public void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            a aVar = new a();
            aVar.b = 2;
            aVar.a = strategyBean.b;
            aVar.c = null;
            aVar.d = null;
            aVar.e = strategyBean.c;
            aVar.f = ad.a((Parcelable) strategyBean);
            b(aVar);
        }
    }

    public StrategyBean a() {
        List a = a(2);
        if (a != null && a.size() > 0) {
            a aVar = (a) a.get(0);
            if (aVar.f != null) {
                return (StrategyBean) ad.a(aVar.f, StrategyBean.CREATOR);
            }
        }
        return null;
    }

    public void b() {
        b(2);
    }

    /* access modifiers changed from: protected */
    public ContentValues a(CrashDetailBean crashDetailBean) {
        int i = 1;
        if (crashDetailBean == null) {
            return null;
        }
        try {
            int i2;
            ContentValues contentValues = new ContentValues();
            if (crashDetailBean.a > 0) {
                contentValues.put("_id", Long.valueOf(crashDetailBean.a));
            }
            contentValues.put("_tm", Long.valueOf(crashDetailBean.r));
            contentValues.put("_s1", crashDetailBean.u);
            String str = "_up";
            if (crashDetailBean.d) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            contentValues.put(str, Integer.valueOf(i2));
            String str2 = "_me";
            if (!crashDetailBean.j) {
                i = 0;
            }
            contentValues.put(str2, Integer.valueOf(i));
            contentValues.put("_uc", Integer.valueOf(crashDetailBean.l));
            contentValues.put("_dt", ad.a((Parcelable) crashDetailBean));
            return contentValues;
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public CrashDetailBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            CrashDetailBean crashDetailBean = (CrashDetailBean) ad.a(blob, CrashDetailBean.CREATOR);
            if (crashDetailBean == null) {
                return crashDetailBean;
            }
            crashDetailBean.a = j;
            return crashDetailBean;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public void b(CrashDetailBean crashDetailBean) {
        if (crashDetailBean != null) {
            try {
                SQLiteDatabase writableDatabase = b.getWritableDatabase();
                if (writableDatabase != null) {
                    ContentValues a = a(crashDetailBean);
                    if (a != null) {
                        long replace = writableDatabase.replace("t_cr", "_id", a);
                        if (replace >= 0) {
                            z.c("insert %s success!", "t_cr");
                            crashDetailBean.a = replace;
                            return;
                        }
                        return;
                    }
                    return;
                }
                c.a().a("save crash fail db null", true);
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
                c.a().a("save crash fail error " + th.getClass().getName() + ":" + th.getMessage(), false);
            }
        }
    }

    public void a(List<CrashDetailBean> list) {
        if (list != null && list.size() != 0) {
            try {
                SQLiteDatabase writableDatabase = b.getWritableDatabase();
                if (writableDatabase != null) {
                    for (CrashDetailBean crashDetailBean : list) {
                        ContentValues a = a(crashDetailBean);
                        if (a != null) {
                            long replace = writableDatabase.replace("t_cr", "_id", a);
                            if (replace >= 0) {
                                z.c("insert %s success!", "t_cr");
                                crashDetailBean.a = replace;
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x00ca A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:23:0x0072} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:31:0x0087, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:32:0x0088, code skipped:
            r1 = r2;
     */
    /* JADX WARNING: Missing block: B:35:0x008d, code skipped:
            if (com.tencent.bugly.proguard.z.a(r0) == false) goto L_0x008f;
     */
    /* JADX WARNING: Missing block: B:36:0x008f, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:40:0x009a, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:49:0x00ca, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:66:0x0117, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:67:0x0118, code skipped:
            r2 = r1;
     */
    public java.util.List<com.tencent.bugly.crashreport.crash.CrashDetailBean> b(java.util.List<com.tencent.bugly.crashreport.crash.a> r11) {
        /*
        r10 = this;
        r6 = 0;
        r8 = 0;
        if (r11 == 0) goto L_0x000a;
    L_0x0004:
        r0 = r11.size();
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        r0 = r8;
    L_0x000b:
        return r0;
    L_0x000c:
        r0 = b;
        r0 = r0.getWritableDatabase();
        if (r0 == 0) goto L_0x009d;
    L_0x0014:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r2 = r11.iterator();
    L_0x001d:
        r1 = r2.hasNext();
        if (r1 == 0) goto L_0x0041;
    L_0x0023:
        r1 = r2.next();
        r1 = (com.tencent.bugly.crashreport.crash.a) r1;
        r3 = " or ";
        r3 = r9.append(r3);
        r4 = "_id";
        r3 = r3.append(r4);
        r4 = " = ";
        r3 = r3.append(r4);
        r4 = r1.a;
        r3.append(r4);
        goto L_0x001d;
    L_0x0041:
        r3 = r9.toString();
        r1 = r3.length();
        if (r1 <= 0) goto L_0x0055;
    L_0x004b:
        r1 = " or ";
        r1 = r1.length();
        r3 = r3.substring(r1);
    L_0x0055:
        r9.setLength(r6);
        r1 = "t_cr";
        r2 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Throwable -> 0x011a, all -> 0x0114 }
        if (r2 != 0) goto L_0x0072;
    L_0x0065:
        if (r2 == 0) goto L_0x0070;
    L_0x0067:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x0070;
    L_0x006d:
        r2.close();
    L_0x0070:
        r0 = r8;
        goto L_0x000b;
    L_0x0072:
        r1 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r1.<init>();	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
    L_0x0077:
        r3 = r2.moveToNext();	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        if (r3 == 0) goto L_0x00d7;
    L_0x007d:
        r3 = r10.a(r2);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        if (r3 == 0) goto L_0x00a0;
    L_0x0083:
        r1.add(r3);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        goto L_0x0077;
    L_0x0087:
        r0 = move-exception;
        r1 = r2;
    L_0x0089:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x0117 }
        if (r2 != 0) goto L_0x0092;
    L_0x008f:
        r0.printStackTrace();	 Catch:{ all -> 0x0117 }
    L_0x0092:
        if (r1 == 0) goto L_0x009d;
    L_0x0094:
        r0 = r1.isClosed();
        if (r0 != 0) goto L_0x009d;
    L_0x009a:
        r1.close();
    L_0x009d:
        r0 = r8;
        goto L_0x000b;
    L_0x00a0:
        r3 = "_id";
        r3 = r2.getColumnIndex(r3);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        r3 = r2.getLong(r3);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        r5 = " or ";
        r5 = r9.append(r5);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        r6 = "_id";
        r5 = r5.append(r6);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        r6 = " = ";
        r5 = r5.append(r6);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        r5.append(r3);	 Catch:{ Throwable -> 0x00c0, all -> 0x00ca }
        goto L_0x0077;
    L_0x00c0:
        r3 = move-exception;
        r3 = "unknown id!";
        r4 = 0;
        r4 = new java.lang.Object[r4];	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        com.tencent.bugly.proguard.z.d(r3, r4);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        goto L_0x0077;
    L_0x00ca:
        r0 = move-exception;
    L_0x00cb:
        if (r2 == 0) goto L_0x00d6;
    L_0x00cd:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x00d6;
    L_0x00d3:
        r2.close();
    L_0x00d6:
        throw r0;
    L_0x00d7:
        r3 = r9.toString();	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r4 = r3.length();	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        if (r4 <= 0) goto L_0x0106;
    L_0x00e1:
        r4 = " or ";
        r4 = r4.length();	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r3 = r3.substring(r4);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r4 = "t_cr";
        r5 = 0;
        r0 = r0.delete(r4, r3, r5);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r3 = "deleted %s illegle data %d";
        r4 = 2;
        r4 = new java.lang.Object[r4];	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r5 = 0;
        r6 = "t_cr";
        r4[r5] = r6;	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r5 = 1;
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        r4[r5] = r0;	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
        com.tencent.bugly.proguard.z.d(r3, r4);	 Catch:{ Throwable -> 0x0087, all -> 0x00ca }
    L_0x0106:
        if (r2 == 0) goto L_0x0111;
    L_0x0108:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x0111;
    L_0x010e:
        r2.close();
    L_0x0111:
        r0 = r1;
        goto L_0x000b;
    L_0x0114:
        r0 = move-exception;
        r2 = r8;
        goto L_0x00cb;
    L_0x0117:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00cb;
    L_0x011a:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0089;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.q.b(java.util.List):java.util.List");
    }

    /* access modifiers changed from: protected */
    public com.tencent.bugly.crashreport.crash.a b(Cursor cursor) {
        boolean z = true;
        if (cursor == null) {
            return null;
        }
        try {
            com.tencent.bugly.crashreport.crash.a aVar = new com.tencent.bugly.crashreport.crash.a();
            aVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            aVar.b = cursor.getLong(cursor.getColumnIndex("_tm"));
            aVar.c = cursor.getString(cursor.getColumnIndex("_s1"));
            aVar.d = cursor.getInt(cursor.getColumnIndex("_up")) == 1;
            if (cursor.getInt(cursor.getColumnIndex("_me")) != 1) {
                z = false;
            }
            aVar.e = z;
            aVar.f = cursor.getInt(cursor.getColumnIndex("_uc"));
            return aVar;
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a0 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:11:0x0044} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:19:0x005e, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:20:0x005f, code skipped:
            r1 = r2;
     */
    /* JADX WARNING: Missing block: B:23:0x0064, code skipped:
            if (com.tencent.bugly.proguard.z.a(r0) == false) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:24:0x0066, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:28:0x0071, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:37:0x00a0, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:54:0x00ed, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:55:0x00ee, code skipped:
            r2 = r1;
     */
    public java.util.List<com.tencent.bugly.crashreport.crash.a> c() {
        /*
        r9 = this;
        r8 = 0;
        r0 = b;
        r0 = r0.getWritableDatabase();
        if (r0 == 0) goto L_0x0074;
    L_0x0009:
        r1 = 6;
        r2 = new java.lang.String[r1];	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 0;
        r3 = "_id";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 1;
        r3 = "_tm";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 2;
        r3 = "_s1";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 3;
        r3 = "_up";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 4;
        r3 = "_me";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = 5;
        r3 = "_uc";
        r2[r1] = r3;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = "t_cr";
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        if (r2 != 0) goto L_0x0044;
    L_0x0037:
        if (r2 == 0) goto L_0x0042;
    L_0x0039:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x0042;
    L_0x003f:
        r2.close();
    L_0x0042:
        r0 = r8;
    L_0x0043:
        return r0;
    L_0x0044:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3.<init>();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r1 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r1.<init>();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
    L_0x004e:
        r4 = r2.moveToNext();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 == 0) goto L_0x00ad;
    L_0x0054:
        r4 = r9.b(r2);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 == 0) goto L_0x0076;
    L_0x005a:
        r1.add(r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        goto L_0x004e;
    L_0x005e:
        r0 = move-exception;
        r1 = r2;
    L_0x0060:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00ed }
        if (r2 != 0) goto L_0x0069;
    L_0x0066:
        r0.printStackTrace();	 Catch:{ all -> 0x00ed }
    L_0x0069:
        if (r1 == 0) goto L_0x0074;
    L_0x006b:
        r0 = r1.isClosed();
        if (r0 != 0) goto L_0x0074;
    L_0x0071:
        r1.close();
    L_0x0074:
        r0 = r8;
        goto L_0x0043;
    L_0x0076:
        r4 = "_id";
        r4 = r2.getColumnIndex(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r4 = r2.getLong(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r6 = " or ";
        r6 = r3.append(r6);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r7 = "_id";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r7 = " = ";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r6.append(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        goto L_0x004e;
    L_0x0096:
        r4 = move-exception;
        r4 = "unknown id!";
        r5 = 0;
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        com.tencent.bugly.proguard.z.d(r4, r5);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        goto L_0x004e;
    L_0x00a0:
        r0 = move-exception;
    L_0x00a1:
        if (r2 == 0) goto L_0x00ac;
    L_0x00a3:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x00ac;
    L_0x00a9:
        r2.close();
    L_0x00ac:
        throw r0;
    L_0x00ad:
        r3 = r3.toString();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4 = r3.length();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 <= 0) goto L_0x00dc;
    L_0x00b7:
        r4 = " or ";
        r4 = r4.length();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3 = r3.substring(r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4 = "t_cr";
        r5 = 0;
        r0 = r0.delete(r4, r3, r5);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3 = "deleted %s illegle data %d";
        r4 = 2;
        r4 = new java.lang.Object[r4];	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r5 = 0;
        r6 = "t_cr";
        r4[r5] = r6;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r5 = 1;
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4[r5] = r0;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        com.tencent.bugly.proguard.z.d(r3, r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
    L_0x00dc:
        if (r2 == 0) goto L_0x00e7;
    L_0x00de:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x00e7;
    L_0x00e4:
        r2.close();
    L_0x00e7:
        r0 = r1;
        goto L_0x0043;
    L_0x00ea:
        r0 = move-exception;
        r2 = r8;
        goto L_0x00a1;
    L_0x00ed:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00a1;
    L_0x00f0:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.q.c():java.util.List");
    }

    public void c(List<com.tencent.bugly.crashreport.crash.a> list) {
        if (list != null && list.size() != 0) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (com.tencent.bugly.crashreport.crash.a aVar : list) {
                    stringBuilder.append(" or ").append("_id").append(" = ").append(aVar.a);
                }
                String stringBuilder2 = stringBuilder.toString();
                if (stringBuilder2.length() > 0) {
                    stringBuilder2 = stringBuilder2.substring(" or ".length());
                }
                stringBuilder.setLength(0);
                try {
                    int delete = writableDatabase.delete("t_cr", stringBuilder2, null);
                    z.c("deleted %s data %d", "t_cr", Integer.valueOf(delete));
                } catch (Throwable th) {
                    if (!z.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    public void d(List<CrashDetailBean> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    if (writableDatabase != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (CrashDetailBean crashDetailBean : list) {
                            stringBuilder.append(" or ").append("_id").append(" = ").append(crashDetailBean.a);
                        }
                        String stringBuilder2 = stringBuilder.toString();
                        if (stringBuilder2.length() > 0) {
                            stringBuilder2 = stringBuilder2.substring(" or ".length());
                        }
                        stringBuilder.setLength(0);
                        int delete = writableDatabase.delete("t_cr", stringBuilder2, null);
                        z.c("deleted %s data %d", "t_cr", Integer.valueOf(delete));
                    }
                }
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.a > 0) {
                contentValues.put("_id", Long.valueOf(userInfoBean.a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            contentValues.put("_dt", ad.a((Parcelable) userInfoBean));
            return contentValues;
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public void b(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                ContentValues a = a(userInfoBean);
                if (a != null) {
                    long replace = writableDatabase.replace("t_ui", "_id", a);
                    if (replace >= 0) {
                        z.c("insert %s success! %d", "t_ui", Long.valueOf(replace));
                        userInfoBean.a = replace;
                        return;
                    }
                    return;
                }
                return;
            }
            z.d("db error delay error record 1min", new Object[0]);
            c.a().a("save ui fail db null", true);
        }
    }

    public void e(List<UserInfoBean> list) {
        if (list != null && list.size() != 0) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                for (UserInfoBean userInfoBean : list) {
                    ContentValues a = a(userInfoBean);
                    if (a != null) {
                        long replace = writableDatabase.replace("t_ui", "_id", a);
                        if (replace >= 0) {
                            z.c("insert %s success!", "t_ui");
                            userInfoBean.a = replace;
                        }
                    }
                }
                return;
            }
            c.a().a("save ui list fail db null", true);
        }
    }

    /* access modifiers changed from: protected */
    public UserInfoBean c(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            UserInfoBean userInfoBean = (UserInfoBean) ad.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean == null) {
                return userInfoBean;
            }
            userInfoBean.a = j;
            return userInfoBean;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a0 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:15:0x0044} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:23:0x005e, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:24:0x005f, code skipped:
            r1 = r2;
     */
    /* JADX WARNING: Missing block: B:27:0x0064, code skipped:
            if (com.tencent.bugly.proguard.z.a(r0) == false) goto L_0x0066;
     */
    /* JADX WARNING: Missing block: B:28:0x0066, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:32:0x0071, code skipped:
            r1.close();
     */
    /* JADX WARNING: Missing block: B:41:0x00a0, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:58:0x00ed, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:59:0x00ee, code skipped:
            r2 = r1;
     */
    public java.util.List<com.tencent.bugly.crashreport.common.strategy.UserInfoBean> a(java.lang.String r10) {
        /*
        r9 = this;
        r8 = 0;
        r0 = b;
        r0 = r0.getWritableDatabase();
        if (r0 == 0) goto L_0x0074;
    L_0x0009:
        r1 = com.tencent.bugly.proguard.ag.b(r10);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        if (r1 == 0) goto L_0x002a;
    L_0x000f:
        r3 = r8;
    L_0x0010:
        r1 = "t_ui";
        r2 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        if (r2 != 0) goto L_0x0044;
    L_0x001d:
        if (r2 == 0) goto L_0x0028;
    L_0x001f:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x0028;
    L_0x0025:
        r2.close();
    L_0x0028:
        r0 = r8;
    L_0x0029:
        return r0;
    L_0x002a:
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1.<init>();	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r2 = "_pc = '";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r2 = "'";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        r3 = r1.toString();	 Catch:{ Throwable -> 0x00f0, all -> 0x00ea }
        goto L_0x0010;
    L_0x0044:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3.<init>();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r1 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r1.<init>();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
    L_0x004e:
        r4 = r2.moveToNext();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 == 0) goto L_0x00ad;
    L_0x0054:
        r4 = r9.c(r2);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 == 0) goto L_0x0076;
    L_0x005a:
        r1.add(r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        goto L_0x004e;
    L_0x005e:
        r0 = move-exception;
        r1 = r2;
    L_0x0060:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00ed }
        if (r2 != 0) goto L_0x0069;
    L_0x0066:
        r0.printStackTrace();	 Catch:{ all -> 0x00ed }
    L_0x0069:
        if (r1 == 0) goto L_0x0074;
    L_0x006b:
        r0 = r1.isClosed();
        if (r0 != 0) goto L_0x0074;
    L_0x0071:
        r1.close();
    L_0x0074:
        r0 = r8;
        goto L_0x0029;
    L_0x0076:
        r4 = "_id";
        r4 = r2.getColumnIndex(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r4 = r2.getLong(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r6 = " or ";
        r6 = r3.append(r6);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r7 = "_id";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r7 = " = ";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        r6.append(r4);	 Catch:{ Throwable -> 0x0096, all -> 0x00a0 }
        goto L_0x004e;
    L_0x0096:
        r4 = move-exception;
        r4 = "unknown id!";
        r5 = 0;
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        com.tencent.bugly.proguard.z.d(r4, r5);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        goto L_0x004e;
    L_0x00a0:
        r0 = move-exception;
    L_0x00a1:
        if (r2 == 0) goto L_0x00ac;
    L_0x00a3:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x00ac;
    L_0x00a9:
        r2.close();
    L_0x00ac:
        throw r0;
    L_0x00ad:
        r3 = r3.toString();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4 = r3.length();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        if (r4 <= 0) goto L_0x00dc;
    L_0x00b7:
        r4 = " or ";
        r4 = r4.length();	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3 = r3.substring(r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4 = "t_ui";
        r5 = 0;
        r0 = r0.delete(r4, r3, r5);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r3 = "deleted %s illegle data %d";
        r4 = 2;
        r4 = new java.lang.Object[r4];	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r5 = 0;
        r6 = "t_ui";
        r4[r5] = r6;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r5 = 1;
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        r4[r5] = r0;	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
        com.tencent.bugly.proguard.z.d(r3, r4);	 Catch:{ Throwable -> 0x005e, all -> 0x00a0 }
    L_0x00dc:
        if (r2 == 0) goto L_0x00e7;
    L_0x00de:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x00e7;
    L_0x00e4:
        r2.close();
    L_0x00e7:
        r0 = r1;
        goto L_0x0029;
    L_0x00ea:
        r0 = move-exception;
        r2 = r8;
        goto L_0x00a1;
    L_0x00ed:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00a1;
    L_0x00f0:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.q.a(java.lang.String):java.util.List");
    }

    public void f(List<UserInfoBean> list) {
        if (list != null && list.size() != 0) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (UserInfoBean userInfoBean : list) {
                    stringBuilder.append(" or ").append("_id").append(" = ").append(userInfoBean.a);
                }
                String stringBuilder2 = stringBuilder.toString();
                if (stringBuilder2.length() > 0) {
                    stringBuilder2 = stringBuilder2.substring(" or ".length());
                }
                stringBuilder.setLength(0);
                try {
                    int delete = writableDatabase.delete("t_ui", stringBuilder2, null);
                    z.c("deleted %s data %d", "t_ui", Integer.valueOf(delete));
                } catch (Throwable th) {
                    if (!z.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public ContentValues a(a aVar) {
        if (aVar == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (aVar.a > 0) {
                contentValues.put("_id", Long.valueOf(aVar.a));
            }
            contentValues.put("_tp", Integer.valueOf(aVar.b));
            contentValues.put("_pc", aVar.c);
            contentValues.put("_th", aVar.d);
            contentValues.put("_tm", Long.valueOf(aVar.e));
            if (aVar.f != null) {
                contentValues.put("_dt", aVar.f);
            }
            return contentValues;
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public boolean b(a aVar) {
        if (aVar == null) {
            return false;
        }
        SQLiteDatabase writableDatabase = b.getWritableDatabase();
        if (writableDatabase == null) {
            return false;
        }
        ContentValues a = a(aVar);
        if (a == null) {
            return false;
        }
        long replace = writableDatabase.replace("t_lr", "_id", a);
        if (replace < 0) {
            return false;
        }
        z.c("insert %s success!", "t_lr");
        aVar.a = replace;
        return true;
    }

    /* access modifiers changed from: protected */
    public a d(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            a aVar = new a();
            aVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            aVar.c = cursor.getString(cursor.getColumnIndex("_pc"));
            aVar.d = cursor.getString(cursor.getColumnIndex("_th"));
            aVar.e = cursor.getLong(cursor.getColumnIndex("_tm"));
            aVar.f = cursor.getBlob(cursor.getColumnIndex("_dt"));
            return aVar;
        } catch (Throwable th) {
            if (z.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0096 A:{ExcHandler: all (th java.lang.Throwable), Splitter:B:13:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x005c A:{Catch:{ all -> 0x00e3 }} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:21:0x0054, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:22:0x0055, code skipped:
            r1 = r2;
     */
    /* JADX WARNING: Missing block: B:26:0x005c, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:39:0x0096, code skipped:
            r0 = th;
     */
    public java.util.List<com.tencent.bugly.crashreport.common.strategy.a> a(int r10) {
        /*
        r9 = this;
        r8 = 0;
        r0 = b;
        r0 = r0.getWritableDatabase();
        if (r0 == 0) goto L_0x006a;
    L_0x0009:
        if (r10 < 0) goto L_0x0038;
    L_0x000b:
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
        r1.<init>();	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
        r2 = "_tp = ";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
        r3 = r1.toString();	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
    L_0x001e:
        r1 = "t_lr";
        r2 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r2 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Throwable -> 0x00e6, all -> 0x00e0 }
        if (r2 != 0) goto L_0x003a;
    L_0x002b:
        if (r2 == 0) goto L_0x0036;
    L_0x002d:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x0036;
    L_0x0033:
        r2.close();
    L_0x0036:
        r0 = r8;
    L_0x0037:
        return r0;
    L_0x0038:
        r3 = r8;
        goto L_0x001e;
    L_0x003a:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r3.<init>();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r1 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r1.<init>();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
    L_0x0044:
        r4 = r2.moveToNext();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        if (r4 == 0) goto L_0x00a3;
    L_0x004a:
        r4 = r9.d(r2);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        if (r4 == 0) goto L_0x006c;
    L_0x0050:
        r1.add(r4);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        goto L_0x0044;
    L_0x0054:
        r0 = move-exception;
        r1 = r2;
    L_0x0056:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00e3 }
        if (r2 != 0) goto L_0x005f;
    L_0x005c:
        r0.printStackTrace();	 Catch:{ all -> 0x00e3 }
    L_0x005f:
        if (r1 == 0) goto L_0x006a;
    L_0x0061:
        r0 = r1.isClosed();
        if (r0 != 0) goto L_0x006a;
    L_0x0067:
        r1.close();
    L_0x006a:
        r0 = r8;
        goto L_0x0037;
    L_0x006c:
        r4 = "_id";
        r4 = r2.getColumnIndex(r4);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        r4 = r2.getLong(r4);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        r6 = " or ";
        r6 = r3.append(r6);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        r7 = "_id";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        r7 = " = ";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        r6.append(r4);	 Catch:{ Throwable -> 0x008c, all -> 0x0096 }
        goto L_0x0044;
    L_0x008c:
        r4 = move-exception;
        r4 = "unknown id!";
        r5 = 0;
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        com.tencent.bugly.proguard.z.d(r4, r5);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        goto L_0x0044;
    L_0x0096:
        r0 = move-exception;
    L_0x0097:
        if (r2 == 0) goto L_0x00a2;
    L_0x0099:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x00a2;
    L_0x009f:
        r2.close();
    L_0x00a2:
        throw r0;
    L_0x00a3:
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r4 = r3.length();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        if (r4 <= 0) goto L_0x00d2;
    L_0x00ad:
        r4 = " or ";
        r4 = r4.length();	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r3 = r3.substring(r4);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r4 = "t_lr";
        r5 = 0;
        r0 = r0.delete(r4, r3, r5);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r3 = "deleted %s illegle data %d";
        r4 = 2;
        r4 = new java.lang.Object[r4];	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r5 = 0;
        r6 = "t_lr";
        r4[r5] = r6;	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r5 = 1;
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        r4[r5] = r0;	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
        com.tencent.bugly.proguard.z.d(r3, r4);	 Catch:{ Throwable -> 0x0054, all -> 0x0096 }
    L_0x00d2:
        if (r2 == 0) goto L_0x00dd;
    L_0x00d4:
        r0 = r2.isClosed();
        if (r0 != 0) goto L_0x00dd;
    L_0x00da:
        r2.close();
    L_0x00dd:
        r0 = r1;
        goto L_0x0037;
    L_0x00e0:
        r0 = move-exception;
        r2 = r8;
        goto L_0x0097;
    L_0x00e3:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0097;
    L_0x00e6:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.q.a(int):java.util.List");
    }

    public void g(List<a> list) {
        if (list != null && list.size() != 0) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (a aVar : list) {
                    stringBuilder.append(" or ").append("_id").append(" = ").append(aVar.a);
                }
                String stringBuilder2 = stringBuilder.toString();
                if (stringBuilder2.length() > 0) {
                    stringBuilder2 = stringBuilder2.substring(" or ".length());
                }
                stringBuilder.setLength(0);
                try {
                    int delete = writableDatabase.delete("t_lr", stringBuilder2, null);
                    z.c("deleted %s data %d", "t_lr", Integer.valueOf(delete));
                } catch (Throwable th) {
                    if (!z.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    public void b(int i) {
        String str = null;
        SQLiteDatabase writableDatabase = b.getWritableDatabase();
        if (writableDatabase != null) {
            if (i >= 0) {
                try {
                    str = "_tp = " + i;
                } catch (Throwable th) {
                    if (!z.a(th)) {
                        th.printStackTrace();
                        return;
                    }
                    return;
                }
            }
            int delete = writableDatabase.delete("t_lr", str, null);
            z.c("deleted %s data %d", "t_lr", Integer.valueOf(delete));
        }
    }
}
