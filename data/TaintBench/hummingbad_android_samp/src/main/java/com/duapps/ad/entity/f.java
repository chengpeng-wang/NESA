package com.duapps.ad.entity;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.duapps.ad.base.d;
import com.duapps.ad.base.i;
import com.duapps.ad.base.t;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.entity.strategy.a;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* compiled from: FacebookCacheManager */
public final class f extends a<NativeAd> implements Callback {
    private List<String> h = new ArrayList();
    private int i;
    private int j = 0;
    /* access modifiers changed from: private|final */
    public final List<l> k = new LinkedList();
    /* access modifiers changed from: private */
    public Handler l;
    /* access modifiers changed from: private */
    public Context m = null;

    private String e() {
        String str;
        synchronized (this.h) {
            if (this.h.size() <= 0) {
                str = null;
            } else if (this.j >= this.h.size()) {
                str = (String) this.h.get(0);
            } else {
                str = (String) this.h.get(this.j);
                this.j = (this.j + 1) % this.h.size();
            }
        }
        return str;
    }

    public f(Context context, int i, long j, int i2) {
        super(context, i, j);
        this.m = context.getApplicationContext();
        d.a(this.f);
        b(t.a(this.m).a(i));
        HandlerThread handlerThread = new HandlerThread("fbnative", 10);
        handlerThread.start();
        this.l = new Handler(handlerThread.getLooper(), this);
        if (this.h.size() <= 0) {
            com.duapps.ad.base.f.c("FbCache", "Refresh request failed: no available Placement Id");
            Log.e("DuNativeAd", "Please setup fbids in DuAdNetwork init method");
        }
        if (i2 <= 0 || i2 > 5) {
            i2 = 1;
        }
        this.i = i2;
        this.l.sendEmptyMessageDelayed(1, 3300000);
    }

    /* access modifiers changed from: private */
    /* renamed from: f */
    public NativeAd d() {
        l lVar;
        l lVar2 = null;
        synchronized (this.k) {
            while (this.k.size() > 0) {
                lVar2 = (l) this.k.remove(0);
                if (lVar2 != null) {
                    if (lVar2.isValid()) {
                        break;
                    }
                    lVar2.destroy();
                }
            }
            lVar = lVar2;
        }
        d.a(this.m, lVar == null ? "FAIL" : "OK", this.f);
        return lVar;
    }

    public final int b() {
        int i;
        synchronized (this.k) {
            Iterator it = this.k.iterator();
            i = 0;
            while (it.hasNext()) {
                l lVar = (l) it.next();
                if (lVar == null) {
                    it.remove();
                } else if (lVar.isValid()) {
                    i = (i + 1) + 1;
                } else {
                    it.remove();
                    lVar.destroy();
                }
            }
        }
        return i;
    }

    public final void a() {
        if (d.b(this.m)) {
            com.duapps.ad.base.f.c("FbCache", "Refresh request...");
            if (this.i <= 0) {
                this.a = true;
                com.duapps.ad.base.f.c("FbCache", "Refresh request failed: no available Placement Id");
                return;
            }
            this.a = false;
            this.l.obtainMessage(0).sendToTarget();
            return;
        }
        com.duapps.ad.base.f.c("FbCache", "network error && sid = " + this.f);
    }

    public final boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            this.l.removeMessages(0);
            if (this.b) {
                com.duapps.ad.base.f.c("FbCache", "Refresh request failed: already refreshing");
                return true;
            }
            int i2;
            this.b = true;
            this.c = true;
            synchronized (this.k) {
                Iterator it = this.k.iterator();
                i2 = 0;
                while (it.hasNext()) {
                    l lVar = (l) it.next();
                    if (lVar == null) {
                        it.remove();
                    } else if (lVar.isValid()) {
                        i2 = (i2 + 1) + 1;
                    } else {
                        it.remove();
                        lVar.destroy();
                    }
                }
            }
            if (i2 < this.i) {
                i = this.i - i2;
                if (com.duapps.ad.base.f.a()) {
                    com.duapps.ad.base.f.c("FbCache", "Refresh request send: green = " + i2 + " ,need = " + i);
                }
                this.l.obtainMessage(2, i, 0).sendToTarget();
            } else {
                com.duapps.ad.base.f.c("FbCache", "Refresh request OK: green is full");
                this.b = false;
            }
            return true;
        } else if (i != 2) {
            return false;
        } else {
            i = message.arg1;
            if (i > 0) {
                a(i);
            } else {
                this.b = false;
                com.duapps.ad.base.f.c("FbCache", "Refresh result: DONE for geeen count");
            }
            return true;
        }
    }

    private void a(int i) {
        String e = e();
        if (com.duapps.ad.base.f.a()) {
            synchronized (this.h) {
                int size = this.h.size();
                StringBuffer stringBuffer = new StringBuffer();
                for (int i2 = 0; i2 < size; i2++) {
                    stringBuffer.append(((String) this.h.get(i2)) + ",");
                }
                com.duapps.ad.base.f.c("FbCache", stringBuffer.toString());
            }
        }
        com.duapps.ad.base.f.c("FbCache", "refresh FB -> id = " + e);
        if (e == null) {
            com.duapps.ad.base.f.d("DuNativeAd", "No Available Placement ID");
            this.b = false;
            this.c = false;
            return;
        }
        l lVar = new l(this.m, e, this.f);
        lVar.a(new g(this, e, lVar, SystemClock.elapsedRealtime(), i));
        lVar.a();
    }

    public final void c() {
        synchronized (this.k) {
            this.k.clear();
        }
    }

    public final void a(List<String> list) {
        if (list != null && list.size() != 0) {
            i.a(this.m, (List) list, this.f);
            b((List) list);
        }
    }

    private void b(List<String> list) {
        if (list != null && list.size() > 0) {
            synchronized (this.h) {
                this.h.clear();
                this.h.addAll(list);
            }
        }
    }
}
