package com.baidu.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import java.util.ArrayList;
import java.util.Iterator;

public class i {
    /* renamed from: new */
    public static final String f181new = "android.com.baidu.location.TIMER.NOTIFY";
    private int a = 0;
    private long b = 0;
    /* access modifiers changed from: private */
    /* renamed from: byte */
    public ArrayList f182byte = null;
    private boolean c = false;
    /* renamed from: case */
    private BDLocation f183case = null;
    /* renamed from: char */
    private long f184char = 0;
    /* access modifiers changed from: private */
    public LocationClient d = null;
    /* access modifiers changed from: private */
    /* renamed from: do */
    public String f185do = f.v;
    /* renamed from: else */
    private b f186else = null;
    /* renamed from: for */
    private AlarmManager f187for = null;
    /* renamed from: goto */
    private float f188goto = Float.MAX_VALUE;
    /* renamed from: if */
    private Context f189if = null;
    /* renamed from: int */
    private a f190int = new a();
    /* renamed from: long */
    private boolean f191long = false;
    /* renamed from: try */
    private PendingIntent f192try = null;
    /* renamed from: void */
    private boolean f193void = false;

    public class b extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            j.a(i.this.f185do, "timer expire,request location...");
            if (i.this.f182byte != null && !i.this.f182byte.isEmpty()) {
                i.this.d.requestNotifyLocation();
            }
        }
    }

    public class a implements BDLocationListener {
        public void onReceiveLocation(BDLocation bDLocation) {
            i.this.a(bDLocation);
        }

        public void onReceivePoi(BDLocation bDLocation) {
        }
    }

    public i(Context context, LocationClient locationClient) {
        this.f189if = context;
        this.d = locationClient;
        this.d.registerNotifyLocationListener(this.f190int);
        this.f187for = (AlarmManager) this.f189if.getSystemService("alarm");
        this.f186else = new b();
        this.c = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0034  */
    private void a() {
        /*
        r8 = this;
        r1 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r2 = 0;
        r3 = 1;
        r0 = r8.m956do();
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r0 = r8.f188goto;
        r4 = 1167867904; // 0x459c4000 float:5000.0 double:5.7700341E-315;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 <= 0) goto L_0x0043;
    L_0x0014:
        r0 = 600000; // 0x927c0 float:8.40779E-40 double:2.964394E-318;
    L_0x0017:
        r4 = r8.f191long;
        if (r4 == 0) goto L_0x005f;
    L_0x001b:
        r8.f191long = r2;
    L_0x001d:
        r0 = r8.a;
        if (r0 == 0) goto L_0x005d;
    L_0x0021:
        r4 = r8.f184char;
        r0 = r8.a;
        r6 = (long) r0;
        r4 = r4 + r6;
        r6 = java.lang.System.currentTimeMillis();
        r4 = r4 - r6;
        r6 = (long) r1;
        r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r0 <= 0) goto L_0x005d;
    L_0x0031:
        r0 = r2;
    L_0x0032:
        if (r0 == 0) goto L_0x000a;
    L_0x0034:
        r8.a = r1;
        r0 = java.lang.System.currentTimeMillis();
        r8.f184char = r0;
        r0 = r8.a;
        r0 = (long) r0;
        r8.a(r0);
        goto L_0x000a;
    L_0x0043:
        r0 = r8.f188goto;
        r4 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 <= 0) goto L_0x004f;
    L_0x004b:
        r0 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        goto L_0x0017;
    L_0x004f:
        r0 = r8.f188goto;
        r4 = 1140457472; // 0x43fa0000 float:500.0 double:5.634608575E-315;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 <= 0) goto L_0x005b;
    L_0x0057:
        r0 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        goto L_0x0017;
    L_0x005b:
        r0 = r1;
        goto L_0x0017;
    L_0x005d:
        r0 = r3;
        goto L_0x0032;
    L_0x005f:
        r1 = r0;
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.i.a():void");
    }

    private void a(long j) {
        if (this.f193void) {
            this.f187for.cancel(this.f192try);
        }
        this.f192try = PendingIntent.getBroadcast(this.f189if, 0, new Intent(f181new), 134217728);
        this.f187for.set(0, System.currentTimeMillis() + j, this.f192try);
        j.a(this.f185do, "timer start:" + j);
    }

    /* access modifiers changed from: private */
    public void a(BDLocation bDLocation) {
        j.a(this.f185do, "notify new loation");
        this.f193void = false;
        if (bDLocation.getLocType() != 61 && bDLocation.getLocType() != BDLocation.TypeNetWorkLocation && bDLocation.getLocType() != 65) {
            a(120000);
        } else if (System.currentTimeMillis() - this.b >= 5000 && this.f182byte != null) {
            float f;
            this.f183case = bDLocation;
            this.b = System.currentTimeMillis();
            float[] fArr = new float[1];
            float f2 = Float.MAX_VALUE;
            Iterator it = this.f182byte.iterator();
            while (true) {
                f = f2;
                if (!it.hasNext()) {
                    break;
                }
                BDNotifyListener bDNotifyListener = (BDNotifyListener) it.next();
                Location.distanceBetween(bDLocation.getLatitude(), bDLocation.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                f2 = (fArr[0] - bDNotifyListener.mRadius) - bDLocation.getRadius();
                j.a(this.f185do, "distance:" + f2);
                if (f2 > 0.0f) {
                    if (f2 < f) {
                    }
                    f2 = f;
                } else {
                    if (bDNotifyListener.Notified < 3) {
                        bDNotifyListener.Notified++;
                        bDNotifyListener.onNotify(bDLocation, fArr[0]);
                        if (bDNotifyListener.Notified < 3) {
                            this.f191long = true;
                        }
                    }
                    f2 = f;
                }
            }
            if (f < this.f188goto) {
                this.f188goto = f;
            }
            this.a = 0;
            a();
        }
    }

    /* renamed from: do */
    private boolean m956do() {
        boolean z = false;
        if (this.f182byte == null || this.f182byte.isEmpty()) {
            return false;
        }
        Iterator it = this.f182byte.iterator();
        while (true) {
            boolean z2 = z;
            if (!it.hasNext()) {
                return z2;
            }
            z = ((BDNotifyListener) it.next()).Notified < 3 ? true : z2;
        }
    }

    public void a(BDNotifyListener bDNotifyListener) {
        j.a(this.f185do, bDNotifyListener.mCoorType + "2gcj");
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals("gcj02")) {
                double[] dArr = Jni.m674if(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = dArr[0];
                bDNotifyListener.mLatitudeC = dArr[1];
                j.a(this.f185do, bDNotifyListener.mCoorType + "2gcj");
                j.a(this.f185do, "coor:" + bDNotifyListener.mLongitude + "," + bDNotifyListener.mLatitude + ":" + bDNotifyListener.mLongitudeC + "," + bDNotifyListener.mLatitudeC);
            }
            if (this.f183case == null || System.currentTimeMillis() - this.b > 300000) {
                this.d.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.f183case.getLatitude(), this.f183case.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.f183case.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.f188goto) {
                        this.f188goto = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.f183case, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.f191long = true;
                    }
                }
            }
            a();
        }
    }

    /* renamed from: do */
    public int m958do(BDNotifyListener bDNotifyListener) {
        if (this.f182byte == null) {
            return 0;
        }
        if (this.f182byte.contains(bDNotifyListener)) {
            this.f182byte.remove(bDNotifyListener);
        }
        if (this.f182byte.size() == 0 && this.f193void) {
            this.f187for.cancel(this.f192try);
        }
        return 1;
    }

    /* renamed from: if */
    public int m959if(BDNotifyListener bDNotifyListener) {
        if (this.f182byte == null) {
            this.f182byte = new ArrayList();
        }
        this.f182byte.add(bDNotifyListener);
        bDNotifyListener.isAdded = true;
        bDNotifyListener.mNotifyCache = this;
        if (!this.c) {
            this.f189if.registerReceiver(this.f186else, new IntentFilter(f181new));
            this.c = true;
        }
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals("gcj02")) {
                double[] dArr = Jni.m674if(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = dArr[0];
                bDNotifyListener.mLatitudeC = dArr[1];
                j.a(this.f185do, bDNotifyListener.mCoorType + "2gcj");
                j.a(this.f185do, "coor:" + bDNotifyListener.mLongitude + "," + bDNotifyListener.mLatitude + ":" + bDNotifyListener.mLongitudeC + "," + bDNotifyListener.mLatitudeC);
            }
            if (this.f183case == null || System.currentTimeMillis() - this.b > 30000) {
                this.d.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.f183case.getLatitude(), this.f183case.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.f183case.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.f188goto) {
                        this.f188goto = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.f183case, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.f191long = true;
                    }
                }
            }
            a();
        }
        return 1;
    }

    /* renamed from: if */
    public void m960if() {
        if (this.f193void) {
            this.f187for.cancel(this.f192try);
        }
        this.f183case = null;
        this.b = 0;
        if (this.c) {
            j.a(this.f185do, "unregister...");
            this.f189if.unregisterReceiver(this.f186else);
        }
        this.c = false;
    }
}
