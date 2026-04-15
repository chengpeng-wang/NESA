package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.UserInfoBean;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import java.util.Date;
import java.util.List;

/* compiled from: BUGLY */
public class w {
    private static w a = null;
    private final y b;
    private final a c;
    private final q d;
    private final t e;
    /* access modifiers changed from: private|final */
    public final Context f;
    private long[] g = new long[]{-1, -1, -1};

    protected w(Context context, y yVar, a aVar, t tVar, q qVar) {
        this.f = context;
        this.b = yVar;
        this.c = aVar;
        this.d = qVar;
        this.e = tVar;
        v.a(context);
    }

    public static synchronized w a(Context context, y yVar, a aVar, q qVar) {
        w wVar;
        synchronized (w.class) {
            if (a == null) {
                a = new w(context, yVar, aVar, new t(context), qVar);
            }
            wVar = a;
        }
        return wVar;
    }

    public static synchronized w a() {
        w wVar;
        synchronized (w.class) {
            wVar = a;
        }
        return wVar;
    }

    /* access modifiers changed from: protected */
    public long b() {
        long j;
        long j2 = 0;
        long b = ag.b();
        List a = this.d.a(3);
        if (a == null || a.size() <= 0) {
            j = 0;
        } else {
            try {
                com.tencent.bugly.crashreport.common.strategy.a aVar = (com.tencent.bugly.crashreport.common.strategy.a) a.get(0);
                if (aVar.e >= b) {
                    j2 = ag.b(aVar.f);
                    a.remove(aVar);
                }
                j = j2;
            } catch (Throwable th) {
                j = 0;
                z.e("error local type %d", Integer.valueOf(3));
            }
            if (a.size() > 0) {
                this.d.g(a);
            }
        }
        z.c("consume getted %d", Long.valueOf(j));
        return j;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void a(long j) {
        com.tencent.bugly.crashreport.common.strategy.a aVar = new com.tencent.bugly.crashreport.common.strategy.a();
        aVar.b = 3;
        aVar.e = ag.b();
        aVar.c = "";
        aVar.d = "";
        aVar.f = ag.b(j);
        this.d.b(3);
        this.d.b(aVar);
        z.c("consume update %d", Long.valueOf(j));
    }

    /* access modifiers changed from: protected */
    public void a(ay ayVar, c cVar, int i) {
        final int i2 = i;
        this.b.b(new x(this.f, ayVar, this.c, cVar, this.e, this) {
            /* access modifiers changed from: protected */
            public void a() {
                super.a();
                w.this.a(i2, new Date().getTime());
            }
        });
    }

    public void a(List<UserInfoBean> list, c cVar, int i) {
        StrategyBean d = cVar.d();
        if (!d.e && !d.f) {
            z.d("remote uin&query is diable!", new Object[0]);
        } else if (list != null && list.size() != 0) {
            try {
                m a = s.a((List) list, this.c, i);
                if (a == null) {
                    z.d("create uPkg fail!", new Object[0]);
                    return;
                }
                byte[] a2 = s.a(a);
                if (a2 == null) {
                    z.d("send encode fail!", new Object[0]);
                    return;
                }
                ay a3 = s.a(this.f, 640, a2, this.c, d);
                final List<UserInfoBean> list2 = list;
                this.b.b(new x(this.f, a3, this.c, cVar, this.e, this) {
                    /* access modifiers changed from: protected */
                    public void a() {
                        super.a();
                        w.this.a(2, new Date().getTime());
                    }

                    /* access modifiers changed from: protected */
                    public void a(boolean z, int i, String str) {
                        super.a(z, i, str);
                        if (list2 != null && list2.size() > 0 && z) {
                            z.c("up success do final", new Object[0]);
                            long time = new Date().getTime();
                            for (UserInfoBean userInfoBean : list2) {
                                userInfoBean.f = time;
                            }
                            q.a(w.this.f).e(list2);
                        }
                    }
                });
            } catch (Throwable th) {
                z.e("req ur error %s", th.toString());
                if (!z.b(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void a(List<CrashDetailBean> list, c cVar) {
        StrategyBean d = cVar.d();
        if (!d.d) {
            z.d("remote report is disable!", new Object[0]);
            z.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
        } else if (list != null && list.size() != 0) {
            try {
                m a = s.a(this.f, (List) list, this.c);
                if (a == null) {
                    z.d("create eupPkg fail!", new Object[0]);
                    return;
                }
                byte[] a2 = s.a(a);
                if (a2 == null) {
                    z.d("send encode fail!", new Object[0]);
                    return;
                }
                ay a3 = s.a(this.f, 630, a2, this.c, d);
                final List<CrashDetailBean> list2 = list;
                this.b.b(new x(this.f, a3, this.c, cVar, this.e, this) {
                    /* access modifiers changed from: protected */
                    public void a() {
                        super.a();
                        w.this.a(0, new Date().getTime());
                    }

                    /* access modifiers changed from: protected */
                    public void a(boolean z, int i, String str) {
                        super.a(z, i, str);
                        if (list2 != null && list2.size() > 0) {
                            z.a("up finish update state %b", Boolean.valueOf(z));
                            for (CrashDetailBean crashDetailBean : list2) {
                                z.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                                crashDetailBean.l++;
                                crashDetailBean.d = z;
                                z.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                            }
                            q.a(w.this.f).a(list2);
                            z.a("update state size %d", Integer.valueOf(list2.size()));
                        }
                        if (z) {
                            z.b("[crash] upload success.", new Object[0]);
                        } else {
                            z.b("[crash] upload fail.", new Object[0]);
                        }
                    }
                });
            } catch (Throwable th) {
                z.e("req cr error %s", th.toString());
                if (!z.b(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void a(List<CrashDetailBean> list, c cVar, long j) {
        StrategyBean d = cVar.d();
        if (cVar.b() && !d.d) {
            z.e("crash report was closed by remote , will not upload to Bugly!", new Object[0]);
            z.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
            cVar.a("remoteClose", false);
        } else if (list != null && list.size() != 0) {
            try {
                m a = s.a(this.f, (List) list, this.c);
                if (a == null) {
                    z.d("create eupPkg fail!", new Object[0]);
                    cVar.a("packageFail", false);
                    return;
                }
                byte[] a2 = s.a(a);
                if (a2 == null) {
                    z.d("send encode fail!", new Object[0]);
                    return;
                }
                z.a("wait for crash report! " + list.size(), new Object[0]);
                ay a3 = s.a(this.f, 630, a2, this.c, d);
                final List<CrashDetailBean> list2 = list;
                Thread thread = new Thread(new x(this.f, a3, this.c, cVar, this.e, this) {
                    /* access modifiers changed from: protected */
                    public void a() {
                        super.a();
                        w.this.a(0, new Date().getTime());
                    }

                    /* access modifiers changed from: protected */
                    public void a(boolean z, int i, String str) {
                        super.a(z, i, str);
                        if (list2 != null && list2.size() > 0) {
                            z.a("up finish update state %b", Boolean.valueOf(z));
                            for (CrashDetailBean crashDetailBean : list2) {
                                z.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                                crashDetailBean.l++;
                                crashDetailBean.d = z;
                                z.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                            }
                            q.a(w.this.f).a(list2);
                            z.a("update state size %d", Integer.valueOf(list2.size()));
                        }
                        if (z) {
                            z.b("[crash] upload success.", new Object[0]);
                        } else {
                            z.b("[crash] upload fail.", new Object[0]);
                        }
                    }
                });
                thread.setName("CrashUploadThread");
                thread.start();
                thread.join(j);
                z.a("wake up! ", new Object[0]);
            } catch (Throwable th) {
                z.e("req cr error %s", th.toString());
                if (!z.b(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void a(c cVar) {
        StrategyBean d = cVar.d();
        if (d.f) {
            try {
                a(s.a(this.f, 510, null, this.c, d), cVar, 1);
                return;
            } catch (Throwable th) {
                z.e("req error %s", th.toString());
                if (!z.b(th)) {
                    th.printStackTrace();
                    return;
                }
                return;
            }
        }
        z.d("remote query is disable!", new Object[0]);
    }

    public synchronized void a(int i, long j) {
        if (i >= 0) {
            if (i < this.g.length) {
                this.g[i] = j;
                z.c("up %d %d", Integer.valueOf(i), Long.valueOf(j));
            }
        }
        z.e("unknown up %d", Integer.valueOf(i));
    }

    public synchronized long a(int i) {
        long j;
        if (i >= 0) {
            if (i < this.g.length) {
                j = this.g[i];
            }
        }
        z.e("unknown up %d", Integer.valueOf(i));
        j = -2;
        return j;
    }
}
