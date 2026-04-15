package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.c;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public class x implements u, Runnable {
    private final Context a;
    private final ay b;
    protected int c = 0;
    protected long d = 0;
    protected long e = 0;
    private final a f;
    private final c g;
    private final t h;
    private final w i;

    public x(Context context, ay ayVar, a aVar, c cVar, t tVar, w wVar) {
        this.a = context;
        this.f = aVar;
        this.b = ayVar;
        this.g = cVar;
        this.h = tVar;
        this.i = wVar;
    }

    /* access modifiers changed from: protected */
    public void a() {
        this.c = 0;
        this.d = 0;
        this.e = 0;
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str) {
        z.e("try upload fail! %d %s", Integer.valueOf(this.b.g), str);
    }

    /* access modifiers changed from: protected */
    public void a(boolean z, int i, String str) {
        if (z) {
            z.a("[upload] success! %d", Integer.valueOf(this.b.g));
        } else {
            z.e("[upload] fail! %d %d %s", Integer.valueOf(this.b.g), Integer.valueOf(i), str);
            c.a().b(this.b.g + " end " + i + " " + str);
        }
        if (this.d + this.e > 0) {
            this.i.a((this.i.b() + this.d) + this.e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(az azVar, a aVar, c cVar) {
        if (azVar == null) {
            z.d("resp == null!", new Object[0]);
            return false;
        } else if (azVar.a != (byte) 0) {
            z.e("resp result error %d", Byte.valueOf(azVar.a));
            return false;
        } else {
            z.c("sync remote server time %s , %d", azVar.d, Long.valueOf(aVar.o()));
            aVar.f(azVar.d);
            aVar.a(aVar.o());
            if (azVar.b == 510) {
                if (azVar.c == null) {
                    z.e("remote data is miss! %d", Integer.valueOf(azVar.b));
                    return false;
                }
                bb a = s.a(azVar.c);
                if (a == null) {
                    z.e("remote data is error! %d", Integer.valueOf(azVar.b));
                    return false;
                }
                String str = "en:%b qu:%b uin:%b vm:%d";
                Object[] objArr = new Object[4];
                objArr[0] = Boolean.valueOf(a.a);
                objArr[1] = Boolean.valueOf(a.c);
                objArr[2] = Boolean.valueOf(a.b);
                objArr[3] = Integer.valueOf(a.g == null ? -1 : a.g.size());
                z.c(str, objArr);
                cVar.a(a);
            }
            return true;
        }
    }

    public void run() {
        Integer num = null;
        int i = 0;
        if (this.i.b() >= 2097152) {
            z.e("up too much wait to next time ! %d %d ", Long.valueOf(this.i.b()), Long.valueOf(2097152));
            return;
        }
        try {
            String str = "do up task %d";
            Object[] objArr = new Object[1];
            if (this.b != null) {
                num = Integer.valueOf(this.b.g);
            }
            objArr[0] = num;
            z.c(str, objArr);
            a();
            if (this.a == null || this.b == null || this.f == null || this.g == null || this.h == null) {
                z.e("up task args error! " + this.a + "|" + this.b + "|" + this.f + "|" + this.g + "|" + this.h, new Object[0]);
                a(false, 0, "upload fail, illegal access error! ");
                return;
            }
            StrategyBean d = this.g.d();
            byte[] a = s.a(this.b);
            if (a != null) {
                Map hashMap = new HashMap();
                hashMap.put("pid", URLEncoder.encode(this.f.d(), "utf-8"));
                hashMap.put("bid", URLEncoder.encode(this.f.f(), "utf-8"));
                hashMap.put("pver", URLEncoder.encode(this.f.e(), "utf-8"));
                int i2 = 0;
                int i3 = 0;
                while (i3 < 5) {
                    if (i3 != 0) {
                        ag.a((long) BuglyBroadcastRecevier.UPLOADLIMITED);
                    }
                    i2 = i3 + 1;
                    if (b.e(this.a) == null) {
                        a(0, "Network is not availiable!");
                        i3 = i2;
                        i2 = 0;
                    } else {
                        z.c("send %d", Integer.valueOf(a.length));
                        byte[] a2 = this.h.a(d.o, a, (u) this, hashMap);
                        if (a2 == null) {
                            a(1, "upload fail, no response! ");
                            i3 = i2;
                            i2 = 1;
                        } else {
                            z.c("recv %d", Integer.valueOf(a2.length));
                            az a3 = s.a(a2, d);
                            if (a3 == null) {
                                a(false, 1, "upload fail, resp null! ");
                                return;
                            }
                            v.a(a3);
                            String str2 = "response %d %d";
                            Object[] objArr2 = new Object[2];
                            objArr2[0] = Integer.valueOf(a3.b);
                            if (a3.c != null) {
                                i = a3.c.length;
                            }
                            objArr2[1] = Integer.valueOf(i);
                            z.c(str2, objArr2);
                            if (a(a3, this.f, this.g)) {
                                a(true, 2, null);
                                return;
                            } else {
                                a(false, 2, a3.f);
                                return;
                            }
                        }
                    }
                }
                a(false, i2, "try OT Fail! ");
            }
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            c.a().b(this.b.g + " throw " + (th.getClass().getName() + ":" + th.getMessage()));
        }
    }

    public void a(String str, long j, String str2) {
        this.c++;
        this.d += j;
    }

    public void a(long j) {
        this.e += j;
    }
}
