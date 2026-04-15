package com.baidu.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.baidu.mapapi.search.MKSearch;
import java.util.ArrayList;
import java.util.Iterator;

public final class LocationClient {
    private static final int B = 4;
    private static final int b = 8;
    private static final int f = 9;
    /* renamed from: for */
    private static final String f28for = "baidu_location_Client";
    /* renamed from: goto */
    private static final int f29goto = 1000;
    private static final int h = 7;
    /* renamed from: if */
    private static final int f30if = 10;
    private static final int m = 5;
    private static final int n = 12;
    private static final int o = 6;
    private static final int p = 2;
    private static final int s = 6000;
    /* renamed from: try */
    private static final int f31try = 1;
    /* renamed from: void */
    private static final int f32void = 3;
    private static final int y = 11;
    private String A = null;
    /* access modifiers changed from: private */
    public ArrayList C = null;
    /* access modifiers changed from: private */
    public boolean a = false;
    /* renamed from: byte */
    private b f33byte = null;
    private BDLocationListener c = null;
    /* renamed from: case */
    private Boolean f34case = Boolean.valueOf(false);
    /* renamed from: char */
    private long f35char = 0;
    private boolean d = false;
    /* access modifiers changed from: private */
    /* renamed from: do */
    public a f36do = new a(this, null);
    private String e = "3.1";
    /* renamed from: else */
    private i f37else = null;
    /* access modifiers changed from: private */
    public boolean g = false;
    /* access modifiers changed from: private|final */
    public final Messenger i = new Messenger(this.f36do);
    /* access modifiers changed from: private */
    /* renamed from: int */
    public boolean f38int = false;
    private Context j = null;
    /* access modifiers changed from: private */
    public Messenger k = null;
    /* access modifiers changed from: private */
    public LocationClientOption l = new LocationClientOption();
    /* renamed from: long */
    private Boolean f39long = Boolean.valueOf(false);
    /* access modifiers changed from: private|final */
    /* renamed from: new */
    public final Object f40new = new Object();
    /* access modifiers changed from: private */
    public boolean q = false;
    private long r = 0;
    private ServiceConnection t = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocationClient.this.k = new Messenger(iBinder);
            if (LocationClient.this.k == null) {
                j.a(LocationClient.f28for, "server not connected");
                return;
            }
            LocationClient.this.q = true;
            Log.d("baidu_location_client", "baidu location connected ...");
            try {
                Message obtain = Message.obtain(null, 11);
                obtain.replyTo = LocationClient.this.i;
                obtain.setData(LocationClient.this.m698if());
                LocationClient.this.k.send(obtain);
                LocationClient.this.q = true;
                LocationClient.this.f38int = true;
                j.a(LocationClient.f28for, "bindService ...");
                if (LocationClient.this.l != null) {
                    LocationClient.this.f36do.obtainMessage(4).sendToTarget();
                }
            } catch (RemoteException e) {
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            LocationClient.this.k = null;
            LocationClient.this.q = false;
            j.a(LocationClient.f28for, "unbindservice...");
        }
    };
    private String u = "http://loc.map.baidu.com/sdk.php";
    private boolean v = false;
    private boolean w = false;
    private BDLocation x = null;
    private String z = null;

    private class a extends Handler {
        private a() {
        }

        /* synthetic */ a(LocationClient locationClient, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    LocationClient.this.m693for();
                    return;
                case 2:
                    LocationClient.this.m703int();
                    return;
                case 3:
                    LocationClient.this.m699if(message);
                    return;
                case 4:
                    LocationClient.this.m682byte();
                    return;
                case 5:
                    LocationClient.this.m694for(message);
                    return;
                case 6:
                    LocationClient.this.a(message);
                    return;
                case 7:
                    LocationClient.this.m687do();
                    return;
                case 8:
                    LocationClient.this.onRegisterNotifyLocationListener(message);
                    return;
                case 9:
                    LocationClient.this.onRegisterNotify(message);
                    return;
                case 10:
                    LocationClient.this.onRemoveNotifyEvent(message);
                    return;
                case 11:
                    LocationClient.this.m707new();
                    return;
                case LocationClient.n /*12*/:
                    LocationClient.this.m709try();
                    return;
                case MKSearch.TYPE_AREA_POI_LIST /*21*/:
                    LocationClient.this.a(message, 21);
                    return;
                case 26:
                    LocationClient.this.a(message, 26);
                    return;
                case 27:
                    LocationClient.this.m688do(message);
                    return;
                case 54:
                    if (LocationClient.this.l.f53void) {
                        LocationClient.this.a = true;
                        return;
                    }
                    return;
                case 55:
                    if (LocationClient.this.l.f53void) {
                        LocationClient.this.a = false;
                        return;
                    }
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    private class b implements Runnable {
        private b() {
        }

        /* synthetic */ b(LocationClient locationClient, AnonymousClass1 anonymousClass1) {
            this();
        }

        /* JADX WARNING: Missing block: B:20:?, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:21:?, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = com.baidu.location.LocationClient.this;
            r1 = r0.f40new;
            monitor-enter(r1);
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r2 = 0;
            r0.g = r2;	 Catch:{ all -> 0x0036 }
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.k;	 Catch:{ all -> 0x0036 }
            if (r0 == 0) goto L_0x001d;
        L_0x0015:
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.i;	 Catch:{ all -> 0x0036 }
            if (r0 != 0) goto L_0x001f;
        L_0x001d:
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
        L_0x001e:
            return;
        L_0x001f:
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.C;	 Catch:{ all -> 0x0036 }
            if (r0 == 0) goto L_0x0034;
        L_0x0027:
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.C;	 Catch:{ all -> 0x0036 }
            r0 = r0.size();	 Catch:{ all -> 0x0036 }
            r2 = 1;
            if (r0 >= r2) goto L_0x0039;
        L_0x0034:
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            goto L_0x001e;
        L_0x0036:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            throw r0;
        L_0x0039:
            r0 = "baidu_location_Client";
            r2 = "request location ...";
            com.baidu.location.j.a(r0, r2);	 Catch:{ all -> 0x0036 }
            r0 = com.baidu.location.LocationClient.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.f36do;	 Catch:{ all -> 0x0036 }
            r2 = 4;
            r0 = r0.obtainMessage(r2);	 Catch:{ all -> 0x0036 }
            r0.sendToTarget();	 Catch:{ all -> 0x0036 }
            monitor-exit(r1);	 Catch:{ all -> 0x0036 }
            goto L_0x001e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.LocationClient$b.run():void");
        }
    }

    public LocationClient(Context context) {
        this.j = context;
        this.l = new LocationClientOption();
        this.f37else = new i(this.j, this);
    }

    public LocationClient(Context context, LocationClientOption locationClientOption) {
        this.j = context;
        this.l = locationClientOption;
        this.f37else = new i(this.j, this);
    }

    private Bundle a() {
        if (this.l == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("num", this.l.a);
        bundle.putFloat("distance", this.l.f44do);
        bundle.putBoolean("extraInfo", this.l.f48if);
        return bundle;
    }

    private void a(int i) {
        Iterator it;
        if (i == 26 && this.v) {
            it = this.C.iterator();
            while (it.hasNext()) {
                ((BDLocationListener) it.next()).onReceivePoi(this.x);
            }
            this.v = false;
        }
        if (!this.d && ((!this.l.f53void || this.x.getLocType() != 61) && this.x.getLocType() != 66 && this.x.getLocType() != 67)) {
            return;
        }
        if (this.l == null || !this.l.isDisableCache() || this.x.getLocType() != 65) {
            it = this.C.iterator();
            while (it.hasNext()) {
                ((BDLocationListener) it.next()).onReceiveLocation(this.x);
            }
            if (this.f38int && j.f209try && this.x.getLocType() == 65) {
                this.f38int = false;
            } else if (this.x.getLocType() != 66 && this.x.getLocType() != 67) {
                this.d = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Message message) {
        if (message != null && message.obj != null) {
            BDLocationListener bDLocationListener = (BDLocationListener) message.obj;
            if (this.C != null && this.C.contains(bDLocationListener)) {
                this.C.remove(bDLocationListener);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Message message, int i) {
        String string = message.getData().getString("locStr");
        j.a(f28for, "on receive new location : " + string);
        j.m976if(f28for, "on receive new location : " + string);
        this.x = new BDLocation(string);
        a(i);
    }

    /* access modifiers changed from: private */
    /* renamed from: byte */
    public void m682byte() {
        if (this.k == null) {
            j.a(f28for, "server not connected");
            return;
        }
        if (!(this.a && this.l.f53void)) {
            Message obtain = Message.obtain(null, 22);
            try {
                obtain.replyTo = this.i;
                this.k.send(obtain);
                this.r = System.currentTimeMillis();
                this.d = true;
                j.a(f28for, "send request to server...");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        synchronized (this.f40new) {
            if (!(this.l == null || this.l.f49int < 1000 || this.g)) {
                if (this.f33byte == null) {
                    this.f33byte = new b(this, null);
                }
                this.f36do.postDelayed(this.f33byte, (long) this.l.f49int);
                this.g = true;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: do */
    public void m687do() {
        if (this.k != null) {
            Message obtain = Message.obtain(null, 25);
            try {
                obtain.replyTo = this.i;
                obtain.setData(a());
                this.k.send(obtain);
                this.f35char = System.currentTimeMillis();
                this.v = true;
                j.a(f28for, "send poi request to server...");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: do */
    public void m688do(Message message) {
        BDLocation bDLocation = new BDLocation(message.getData().getString("locStr"));
        if (this.c == null) {
            return;
        }
        if (this.l == null || !this.l.isDisableCache() || bDLocation.getLocType() != 65) {
            this.c.onReceiveLocation(bDLocation);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: for */
    public void m693for() {
        if (!this.q) {
            j.m972for();
            this.A = this.j.getPackageName();
            this.z = this.A + "_bdls_v2.9";
            j.a(f28for, this.z);
            Intent intent = new Intent(this.j, f.class);
            if (this.l == null) {
                this.l = new LocationClientOption();
            }
            try {
                this.j.bindService(intent, this.t, 1);
            } catch (Exception e) {
                e.printStackTrace();
                this.q = false;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: for */
    public void m694for(Message message) {
        if (message != null && message.obj != null) {
            BDLocationListener bDLocationListener = (BDLocationListener) message.obj;
            if (this.C == null) {
                this.C = new ArrayList();
            }
            this.C.add(bDLocationListener);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: if */
    public Bundle m698if() {
        if (this.l == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("packName", this.A);
        bundle.putString("prodName", this.l.f51new);
        bundle.putString("coorType", this.l.f52try);
        bundle.putString("addrType", this.l.f43char);
        bundle.putString("Url", this.u);
        bundle.putBoolean("openGPS", this.l.f42case);
        bundle.putBoolean("location_change_notify", this.l.f53void);
        bundle.putInt("scanSpan", this.l.f49int);
        bundle.putInt("timeOut", this.l.f50long);
        bundle.putInt("priority", this.l.f47goto);
        bundle.putBoolean("map", this.f39long.booleanValue());
        bundle.putBoolean("import", this.f34case.booleanValue());
        return bundle;
    }

    /* access modifiers changed from: private */
    /* renamed from: if */
    public void m699if(Message message) {
        j.a(f28for, "onSetOption...");
        if (message == null || message.obj == null) {
            j.a(f28for, "setOption, but msg.obj is null");
            return;
        }
        LocationClientOption locationClientOption = (LocationClientOption) message.obj;
        if (!this.l.equals(locationClientOption)) {
            if (this.l.f49int != locationClientOption.f49int) {
                try {
                    synchronized (this.f40new) {
                        if (this.g) {
                            this.f36do.removeCallbacks(this.f33byte);
                            this.g = false;
                        }
                        if (locationClientOption.f49int >= 1000 && !this.g) {
                            if (this.f33byte == null) {
                                this.f33byte = new b(this, null);
                            }
                            this.f36do.postDelayed(this.f33byte, (long) locationClientOption.f49int);
                            this.g = true;
                        }
                    }
                } catch (Exception e) {
                    j.a(f28for, "set location excpetion...");
                }
            }
            this.l = new LocationClientOption(locationClientOption);
            if (this.k == null) {
                j.a(f28for, "server not connected");
                return;
            }
            try {
                Message obtain = Message.obtain(null, 15);
                obtain.replyTo = this.i;
                obtain.setData(m698if());
                this.k.send(obtain);
                j.a(f28for, "change option ...");
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: int */
    public void m703int() {
        if (this.q && this.k != null) {
            Message obtain = Message.obtain(null, n);
            obtain.replyTo = this.i;
            try {
                this.k.send(obtain);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            this.j.unbindService(this.t);
            synchronized (this.f40new) {
                try {
                    if (this.g) {
                        this.f36do.removeCallbacks(this.f33byte);
                        this.g = false;
                    }
                } catch (Exception e2) {
                }
            }
            this.f37else.m960if();
            this.k = null;
            j.m977int();
            this.q = false;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: new */
    public void m707new() {
        if (this.k == null) {
            j.a(f28for, "server not connected");
            return;
        }
        Message obtain = Message.obtain(null, 22);
        try {
            obtain.replyTo = this.i;
            this.k.send(obtain);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: try */
    public void m709try() {
        Message obtain = Message.obtain(null, 28);
        try {
            obtain.replyTo = this.i;
            this.k.send(obtain);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public BDLocation getLastKnownLocation() {
        return this.x;
    }

    public LocationClientOption getLocOption() {
        return this.l;
    }

    public String getVersion() {
        return this.e;
    }

    public boolean isStarted() {
        return this.q;
    }

    public void onRegisterNotify(Message message) {
        if (message != null && message.obj != null) {
            this.f37else.m959if((BDNotifyListener) message.obj);
        }
    }

    public void onRegisterNotifyLocationListener(Message message) {
        if (message != null && message.obj != null) {
            this.c = (BDLocationListener) message.obj;
        }
    }

    public void onRemoveNotifyEvent(Message message) {
        if (message != null && message.obj != null) {
            this.f37else.m958do((BDNotifyListener) message.obj);
        }
    }

    public void registerLocationListener(BDLocationListener bDLocationListener) {
        Message obtainMessage = this.f36do.obtainMessage(5);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public void registerNotify(BDNotifyListener bDNotifyListener) {
        Message obtainMessage = this.f36do.obtainMessage(9);
        obtainMessage.obj = bDNotifyListener;
        obtainMessage.sendToTarget();
    }

    public void registerNotifyLocationListener(BDLocationListener bDLocationListener) {
        Message obtainMessage = this.f36do.obtainMessage(8);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public void removeNotifyEvent(BDNotifyListener bDNotifyListener) {
        Message obtainMessage = this.f36do.obtainMessage(10);
        obtainMessage.obj = bDNotifyListener;
        obtainMessage.sendToTarget();
    }

    public int requestLocation() {
        if (this.k == null || this.i == null) {
            return 1;
        }
        if (this.C == null || this.C.size() < 1) {
            return 2;
        }
        if (System.currentTimeMillis() - this.r < 1000) {
            return 6;
        }
        j.a(f28for, "request location ...");
        this.f36do.obtainMessage(4).sendToTarget();
        return 0;
    }

    public void requestNotifyLocation() {
        this.f36do.obtainMessage(11).sendToTarget();
    }

    public int requestOfflineLocation() {
        if (this.k == null || this.i == null) {
            return 1;
        }
        if (this.C == null || this.C.size() < 1) {
            return 2;
        }
        this.f36do.obtainMessage(n).sendToTarget();
        return 0;
    }

    public int requestPoi() {
        if (this.k == null || this.i == null) {
            return 1;
        }
        if (this.C == null || this.C.size() < 1) {
            return 2;
        }
        if (System.currentTimeMillis() - this.f35char < 6000) {
            return 6;
        }
        if (this.l.a < 1) {
            return 7;
        }
        j.a(f28for, "request location ...");
        this.f36do.obtainMessage(7).sendToTarget();
        return 0;
    }

    public void setForBaiduMap(boolean z) {
        this.f39long = Boolean.valueOf(z);
        j.f209try = z;
    }

    public void setForPreImport(boolean z) {
        this.f34case = Boolean.valueOf(z);
        j.v = z;
    }

    public void setLocOption(LocationClientOption locationClientOption) {
        Object locationClientOption2;
        if (locationClientOption2 == null) {
            locationClientOption2 = new LocationClientOption();
        }
        Message obtainMessage = this.f36do.obtainMessage(3);
        obtainMessage.obj = locationClientOption2;
        obtainMessage.sendToTarget();
    }

    public void setTestUrl(String str) {
        if (str == null) {
            this.u = "http://220.181.3.9:8091/loc_addr_all.php";
        } else {
            this.u = str;
        }
    }

    public void start() {
        this.f36do.obtainMessage(1).sendToTarget();
    }

    public void stop() {
        this.f36do.obtainMessage(2).sendToTarget();
    }

    public void unRegisterLocationListener(BDLocationListener bDLocationListener) {
        Message obtainMessage = this.f36do.obtainMessage(6);
        obtainMessage.obj = bDLocationListener;
        obtainMessage.sendToTarget();
    }

    public boolean updateLocation(Location location) {
        if (this.k == null || this.i == null || location == null) {
            return false;
        }
        try {
            Message obtain = Message.obtain(null, 57);
            obtain.obj = location;
            this.k.send(obtain);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }
}
