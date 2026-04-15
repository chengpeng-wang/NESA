package com.baidu.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

class e {
    /* renamed from: byte */
    private static final int f116byte = 15;
    /* access modifiers changed from: private|static */
    /* renamed from: try */
    public static String f117try = f.v;
    private final long a = 5000;
    private long b = 0;
    private a c = null;
    /* renamed from: case */
    private c f118case = null;
    /* renamed from: char */
    private b f119char = null;
    /* access modifiers changed from: private */
    public boolean d = false;
    /* access modifiers changed from: private */
    /* renamed from: do */
    public boolean f120do = false;
    private Method e = null;
    /* renamed from: else */
    private boolean f121else = false;
    private final long f = 3000;
    /* renamed from: for */
    private boolean f122for = true;
    private Object g = null;
    /* renamed from: goto */
    private Context f123goto;
    /* access modifiers changed from: private */
    /* renamed from: if */
    public Handler f124if = null;
    /* renamed from: int */
    private boolean f125int = false;
    /* renamed from: long */
    private long f126long = 0;
    /* renamed from: new */
    private final long f127new = 3000;
    /* renamed from: void */
    private WifiManager f128void = null;

    private class a extends BroadcastReceiver {
        private a() {
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && e.this.f124if != null) {
                e.this.m840goto();
            }
        }
    }

    private class b extends BroadcastReceiver {
        private b() {
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && e.this.f124if != null) {
                e.this.m842if();
                e.this.f124if.obtainMessage(41).sendToTarget();
                j.a(e.f117try, "wifi manager receive new wifi...");
            }
        }
    }

    protected class c {
        /* renamed from: do */
        public List f113do = null;
        /* renamed from: for */
        private long f114for = 0;
        /* renamed from: if */
        private long f115if = 0;

        public c(List list, long j) {
            this.f115if = j;
            this.f113do = list;
            this.f114for = System.currentTimeMillis();
            a();
            j.m976if(e.f117try, m826for());
        }

        private void a() {
            if (m831new() >= 1) {
                Object obj = 1;
                for (int size = this.f113do.size() - 1; size >= 1 && obj != null; size--) {
                    int i = 0;
                    obj = null;
                    while (i < size) {
                        Object obj2;
                        if (((ScanResult) this.f113do.get(i)).level < ((ScanResult) this.f113do.get(i + 1)).level) {
                            ScanResult scanResult = (ScanResult) this.f113do.get(i + 1);
                            this.f113do.set(i + 1, this.f113do.get(i));
                            this.f113do.set(i, scanResult);
                            obj2 = 1;
                        } else {
                            obj2 = obj;
                        }
                        i++;
                        obj = obj2;
                    }
                }
            }
        }

        public String a(int i) {
            if (m831new() < 1) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer(512);
            String str = e.this.m845char();
            int size = this.f113do.size();
            if (size <= i) {
                i = size;
            }
            int i2 = 0;
            int i3 = 1;
            int i4 = 0;
            int i5 = 0;
            while (i2 < i) {
                String replace;
                if (((ScanResult) this.f113do.get(i2)).level == 0) {
                    size = i3;
                    i3 = i5;
                } else if (i3 != 0) {
                    stringBuffer.append("&wf=");
                    replace = ((ScanResult) this.f113do.get(i2)).BSSID.replace(":", "");
                    stringBuffer.append(replace);
                    size = ((ScanResult) this.f113do.get(i2)).level;
                    if (size < 0) {
                        size = -size;
                    }
                    stringBuffer.append(String.format(";%d;", new Object[]{Integer.valueOf(size)}));
                    i3 = i5 + 1;
                    size = (str == null || !str.equals(replace)) ? i4 : i3;
                    i4 = size;
                    size = 0;
                } else {
                    stringBuffer.append("|");
                    replace = ((ScanResult) this.f113do.get(i2)).BSSID.replace(":", "");
                    stringBuffer.append(replace);
                    size = ((ScanResult) this.f113do.get(i2)).level;
                    if (size < 0) {
                        size = -size;
                    }
                    stringBuffer.append(String.format(";%d;", new Object[]{Integer.valueOf(size)}));
                    size = i5 + 1;
                    int i6;
                    if (str == null || !str.equals(replace)) {
                        i6 = i3;
                        i3 = size;
                        size = i6;
                    } else {
                        i4 = size;
                        i6 = i3;
                        i3 = size;
                        size = i6;
                    }
                }
                i2++;
                i5 = i3;
                i3 = size;
            }
            if (i3 != 0) {
                return null;
            }
            j.a(e.f117try, str + i4);
            stringBuffer.append("&wf_n=" + i4);
            stringBuffer.append("&wf_st=");
            stringBuffer.append(this.f115if);
            stringBuffer.append("&wf_et=");
            stringBuffer.append(this.f114for);
            return stringBuffer.toString();
        }

        public boolean a(c cVar) {
            return a(cVar, this, j.D);
        }

        public boolean a(c cVar, c cVar2, float f) {
            if (cVar == null || cVar2 == null) {
                return false;
            }
            List list = cVar.f113do;
            List list2 = cVar2.f113do;
            if (list == list2) {
                return true;
            }
            if (list == null || list2 == null) {
                return false;
            }
            int size = list.size();
            int size2 = list2.size();
            float f2 = (float) (size + size2);
            if (size == 0 && size2 == 0) {
                return true;
            }
            if (size == 0 || size2 == 0) {
                return false;
            }
            int i = 0;
            int i2 = 0;
            while (i < size) {
                int i3;
                String str = ((ScanResult) list.get(i)).BSSID;
                if (str == null) {
                    i3 = i2;
                } else {
                    for (int i4 = 0; i4 < size2; i4++) {
                        if (str.equals(((ScanResult) list2.get(i4)).BSSID)) {
                            i3 = i2 + 1;
                            break;
                        }
                    }
                    i3 = i2;
                }
                i++;
                i2 = i3;
            }
            j.a(e.f117try, String.format("same %d,total %f,rate %f...", new Object[]{Integer.valueOf(i2), Float.valueOf(f2), Float.valueOf(f)}));
            return ((float) (i2 * 2)) >= f2 * f;
        }

        /* renamed from: byte */
        public String m822byte() {
            try {
                return a(j.F);
            } catch (Exception e) {
                return null;
            }
        }

        /* renamed from: case */
        public String m823case() {
            StringBuffer stringBuffer = new StringBuffer(512);
            stringBuffer.append("wifi info:");
            if (m831new() < 1) {
                return stringBuffer.toString();
            }
            int size = this.f113do.size();
            if (size > 10) {
                size = 10;
            }
            int i = 0;
            int i2 = 1;
            while (i < size) {
                int i3;
                if (((ScanResult) this.f113do.get(i)).level == 0) {
                    i3 = i2;
                } else if (i2 != 0) {
                    stringBuffer.append("wifi=");
                    stringBuffer.append(((ScanResult) this.f113do.get(i)).BSSID.replace(":", ""));
                    stringBuffer.append(String.format(";%d;", new Object[]{Integer.valueOf(((ScanResult) this.f113do.get(i)).level)}));
                    i3 = 0;
                } else {
                    stringBuffer.append(";");
                    stringBuffer.append(((ScanResult) this.f113do.get(i)).BSSID.replace(":", ""));
                    stringBuffer.append(String.format(",%d;", new Object[]{Integer.valueOf(((ScanResult) this.f113do.get(i)).level)}));
                    i3 = i2;
                }
                i++;
                i2 = i3;
            }
            return stringBuffer.toString();
        }

        /* renamed from: do */
        public boolean m824do() {
            return System.currentTimeMillis() - this.f114for < 3000;
        }

        /* renamed from: do */
        public boolean m825do(c cVar) {
            if (this.f113do == null || cVar == null || cVar.f113do == null) {
                return false;
            }
            int size = this.f113do.size() < cVar.f113do.size() ? this.f113do.size() : cVar.f113do.size();
            for (int i = 0; i < size; i++) {
                String str = ((ScanResult) this.f113do.get(i)).BSSID;
                int i2 = ((ScanResult) this.f113do.get(i)).level;
                String str2 = ((ScanResult) cVar.f113do.get(i)).BSSID;
                int i3 = ((ScanResult) cVar.f113do.get(i)).level;
                if (!str.equals(str2) || i2 != i3) {
                    return false;
                }
            }
            return true;
        }

        /* renamed from: for */
        public String m826for() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("wifi=");
            if (this.f113do == null) {
                return stringBuilder.toString();
            }
            for (int i = 0; i < this.f113do.size(); i++) {
                int i2 = ((ScanResult) this.f113do.get(i)).level;
                stringBuilder.append(((ScanResult) this.f113do.get(i)).BSSID.replace(":", ""));
                stringBuilder.append(String.format(",%d;", new Object[]{Integer.valueOf(i2)}));
            }
            return stringBuilder.toString();
        }

        /* renamed from: if */
        public String m827if(int i) {
            int i2 = 0;
            if (i == 0 || m831new() < 1) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer(256);
            int i3 = 0;
            int i4 = 1;
            while (true) {
                int i5 = i2;
                if (i5 >= j.F) {
                    return stringBuffer.toString();
                }
                if ((i4 & i) != 0) {
                    if (i3 == 0) {
                        stringBuffer.append("&ssid=");
                    } else {
                        stringBuffer.append("|");
                    }
                    stringBuffer.append(((ScanResult) this.f113do.get(i5)).BSSID);
                    stringBuffer.append(";");
                    stringBuffer.append(((ScanResult) this.f113do.get(i5)).SSID);
                    i3++;
                }
                i4 <<= 1;
                i2 = i5 + 1;
            }
        }

        /* renamed from: if */
        public boolean m828if() {
            return System.currentTimeMillis() - this.f115if < 3000;
        }

        /* renamed from: if */
        public boolean m829if(c cVar) {
            if (this.f113do == null || cVar == null || cVar.f113do == null) {
                return false;
            }
            int size = this.f113do.size() < cVar.f113do.size() ? this.f113do.size() : cVar.f113do.size();
            for (int i = 0; i < size; i++) {
                if (!((ScanResult) this.f113do.get(i)).BSSID.equals(((ScanResult) cVar.f113do.get(i)).BSSID)) {
                    return false;
                }
            }
            return true;
        }

        /* renamed from: int */
        public boolean m830int() {
            return System.currentTimeMillis() - this.f114for < 5000;
        }

        /* renamed from: new */
        public int m831new() {
            return this.f113do == null ? 0 : this.f113do.size();
        }

        /* renamed from: try */
        public String m832try() {
            try {
                return a(15);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private class d implements Runnable {
        private d() {
        }

        public void run() {
            if (e.this.d && j.J) {
                e.this.f124if.obtainMessage(91).sendToTarget();
                e.this.f124if.postDelayed(this, (long) j.h);
                e.this.f120do = true;
                return;
            }
            e.this.f120do = false;
        }
    }

    public e(Context context, Handler handler) {
        this.f123goto = context;
        this.f124if = handler;
    }

    /* access modifiers changed from: private */
    /* renamed from: goto */
    public void m840goto() {
        State state;
        State state2 = State.UNKNOWN;
        try {
            state = ((ConnectivityManager) this.f123goto.getSystemService("connectivity")).getNetworkInfo(1).getState();
        } catch (Exception e) {
            state = state2;
        }
        if (State.CONNECTED != state) {
            this.d = false;
        } else if (!this.d) {
            this.d = true;
            this.f124if.postDelayed(new d(), (long) j.h);
            this.f120do = true;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: if */
    public void m842if() {
        if (this.f128void != null) {
            try {
                c cVar = new c(this.f128void.getScanResults(), this.b);
                this.b = 0;
                if (this.f118case == null || !cVar.m829if(this.f118case)) {
                    this.f118case = cVar;
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean a() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f126long <= 10000) {
            return false;
        }
        this.f126long = currentTimeMillis;
        return m849new();
    }

    /* renamed from: byte */
    public c m843byte() {
        if ((this.f118case != null && this.f118case.m830int()) || this.f128void == null) {
            return this.f118case;
        }
        try {
            return new c(this.f128void.getScanResults(), 0);
        } catch (Exception e) {
            return new c(null, 0);
        }
    }

    /* renamed from: case */
    public void m844case() {
        if (this.c == null) {
            this.c = new a();
        }
        try {
            if (j.f209try && !this.f121else) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                this.f123goto.registerReceiver(this.c, intentFilter);
                m840goto();
                this.f121else = true;
            }
        } catch (Exception e) {
        }
    }

    /* renamed from: char */
    public String m845char() {
        WifiInfo connectionInfo = this.f128void.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        try {
            String bssid = connectionInfo.getBSSID();
            return bssid != null ? bssid.replace(":", "") : null;
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: else */
    public void m846else() {
        if (this.f125int) {
            try {
                this.f123goto.unregisterReceiver(this.f119char);
                if (j.f209try) {
                    this.f123goto.unregisterReceiver(this.c);
                }
            } catch (Exception e) {
            }
            this.f119char = null;
            this.f128void = null;
            this.c = null;
            this.f125int = false;
            j.a(f117try, "wifimanager stop ...");
        }
    }

    /* renamed from: for */
    public void m847for() {
        if (!this.f120do && j.f209try && j.J) {
            this.f124if.postDelayed(new d(), (long) j.h);
            this.f120do = true;
        }
    }

    /* renamed from: int */
    public c m848int() {
        if ((this.f118case != null && this.f118case.m824do()) || this.f128void == null) {
            return this.f118case;
        }
        try {
            return new c(this.f128void.getScanResults(), 0);
        } catch (Exception e) {
            return new c(null, 0);
        }
    }

    /* renamed from: new */
    public boolean m849new() {
        if (this.f128void == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.b <= 3000) {
            return false;
        }
        try {
            if (this.f128void.isWifiEnabled()) {
                if (this.e == null || this.g == null) {
                    this.f128void.startScan();
                } else {
                    try {
                        this.e.invoke(this.g, new Object[]{Boolean.valueOf(this.f122for)});
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.f128void.startScan();
                    }
                }
                this.b = currentTimeMillis;
                j.a(f117try, "wifimanager start scan ...");
                return true;
            }
            this.b = 0;
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    /* renamed from: try */
    public void m850try() {
        if (!this.f125int) {
            this.f128void = (WifiManager) this.f123goto.getSystemService("wifi");
            this.f119char = new b();
            try {
                this.f123goto.registerReceiver(this.f119char, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
                this.c = new a();
                if (j.f209try) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    this.f123goto.registerReceiver(this.c, intentFilter);
                    this.f121else = true;
                    m840goto();
                }
            } catch (Exception e) {
            }
            this.f125int = true;
            j.a(f117try, "wifimanager start ...");
            try {
                Field declaredField = Class.forName("android.net.wifi.WifiManager").getDeclaredField("mService");
                if (declaredField == null) {
                    j.a(f117try, "android.net.wifi.WifiManager.mService  NOT  found ...");
                    return;
                }
                declaredField.setAccessible(true);
                this.g = declaredField.get(this.f128void);
                Class cls = this.g.getClass();
                j.a(f117try, "mserviceClass : " + cls.getName());
                this.e = cls.getDeclaredMethod("startScan", new Class[]{Boolean.TYPE});
                if (this.e == null) {
                    j.a(f117try, "mService.startScan NOT  found ...");
                } else {
                    this.e.setAccessible(true);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
