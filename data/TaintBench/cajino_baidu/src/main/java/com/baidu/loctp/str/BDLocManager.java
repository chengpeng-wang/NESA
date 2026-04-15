package com.baidu.loctp.str;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.view.MotionEventCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BDLocManager {
    private static Method e = null;
    private static Method f = null;
    private static Method g = null;
    private static Class<?> h = null;
    private static char[] r = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.".toCharArray();
    private final long a = 5000;
    private Context b = null;
    private TelephonyManager c = null;
    private a d = new a();
    private WifiManager i = null;
    private WifiList j = null;
    private Object k = null;
    private Method l = null;
    private boolean m = true;
    private long n = 0;
    /* access modifiers changed from: private */
    public String o = null;
    /* access modifiers changed from: private */
    public int p = 0;
    private String q = null;

    protected class WifiList {
        public List<ScanResult> _WifiList = null;
        private long b = 0;

        public WifiList(List<ScanResult> list) {
            this._WifiList = list;
            this.b = System.currentTimeMillis();
            a();
        }

        private void a() {
            if (size() >= 1) {
                Object obj = 1;
                for (int size = this._WifiList.size() - 1; size >= 1 && obj != null; size--) {
                    int i = 0;
                    obj = null;
                    while (i < size) {
                        Object obj2;
                        if (((ScanResult) this._WifiList.get(i)).level < ((ScanResult) this._WifiList.get(i + 1)).level) {
                            ScanResult scanResult = (ScanResult) this._WifiList.get(i + 1);
                            this._WifiList.set(i + 1, this._WifiList.get(i));
                            this._WifiList.set(i, scanResult);
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

        /* access modifiers changed from: private */
        public boolean b() {
            long currentTimeMillis = System.currentTimeMillis() - this.b;
            return currentTimeMillis < 0 || currentTimeMillis > 500;
        }

        public int size() {
            return this._WifiList == null ? 0 : this._WifiList.size();
        }

        public String toString(int i) {
            if (size() < 1) {
                return null;
            }
            Object obj;
            int obj2;
            Object obj3;
            boolean a = BDLocManager.this.a();
            if (a) {
                i--;
                obj2 = null;
            } else {
                obj2 = 1;
            }
            StringBuffer stringBuffer = new StringBuffer(512);
            int size = this._WifiList.size();
            int i2 = 0;
            int i3 = 0;
            Object obj4 = 1;
            Object obj5 = obj2;
            while (i2 < size) {
                if (((ScanResult) this._WifiList.get(i2)).level == 0) {
                    obj2 = i3;
                    obj3 = obj4;
                    obj4 = obj5;
                } else {
                    String str = ((ScanResult) this._WifiList.get(i2)).BSSID;
                    obj2 = ((ScanResult) this._WifiList.get(i2)).level;
                    str = str.replace(":", "");
                    if (BDLocManager.this.o == null || !str.equals(BDLocManager.this.o)) {
                        if (i3 < i) {
                            stringBuffer.append("h");
                            stringBuffer.append(str);
                            stringBuffer.append("m");
                            stringBuffer.append(StrictMath.abs(obj2));
                            obj2 = i3 + 1;
                            obj3 = null;
                        } else {
                            obj2 = i3;
                            obj3 = obj4;
                        }
                        if (obj2 > i && obj5 != null) {
                            break;
                        }
                        obj4 = obj5;
                    } else {
                        BDLocManager.this.p = StrictMath.abs(obj2);
                        obj2 = i3;
                        obj3 = obj4;
                        int i4 = 1;
                    }
                }
                i2++;
                obj5 = obj4;
                obj4 = obj3;
                i3 = obj2;
            }
            obj3 = obj4;
            String str2 = a ? "h" + BDLocManager.this.o + "km" + BDLocManager.this.p : null;
            return obj3 == null ? str2 + stringBuffer.toString() : str2;
        }
    }

    private class a {
        public int a;
        public int b;
        public int c;
        public int d;
        public char e;

        private a() {
            this.a = -1;
            this.b = -1;
            this.c = -1;
            this.d = -1;
            this.e = 0;
        }

        /* access modifiers changed from: private */
        public boolean a() {
            return this.a > -1 && this.b > 0;
        }

        public String toString() {
            if (!a()) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append(this.e);
            stringBuffer.append("h");
            if (this.c != 460) {
                stringBuffer.append(this.c);
            }
            stringBuffer.append(String.format(Locale.CHINA, "h%xh%xh%x", new Object[]{Integer.valueOf(this.d), Integer.valueOf(this.a), Integer.valueOf(this.b)}));
            return stringBuffer.toString();
        }
    }

    public BDLocManager(Context context) {
        String deviceId;
        this.b = context.getApplicationContext();
        String packageName = this.b.getPackageName();
        try {
            this.c = (TelephonyManager) this.b.getSystemService("phone");
            deviceId = this.c.getDeviceId();
        } catch (Exception e) {
            deviceId = null;
        }
        this.q = "&" + packageName + "&" + deviceId;
        this.i = (WifiManager) this.b.getSystemService("wifi");
        try {
            Field declaredField = Class.forName("android.net.wifi.WifiManager").getDeclaredField("mService");
            if (declaredField != null) {
                declaredField.setAccessible(true);
                this.k = declaredField.get(this.i);
                this.l = this.k.getClass().getDeclaredMethod("startScan", new Class[]{Boolean.TYPE});
                if (this.l != null) {
                    this.l.setAccessible(true);
                }
            }
        } catch (Exception e2) {
        }
    }

    private String a(int i) {
        String aVar;
        String wifiList;
        if (i < 3) {
            i = 3;
        }
        try {
            a(this.c.getCellLocation());
            aVar = this.d.toString();
        } catch (Exception e) {
            aVar = null;
        }
        if (aVar == null) {
            aVar = "Z";
        }
        try {
            if (this.j == null || this.j.b()) {
                this.j = new WifiList(this.i.getScanResults());
            }
            wifiList = this.j.toString(i);
        } catch (Exception e2) {
            wifiList = null;
        }
        if (wifiList != null) {
            aVar = aVar + wifiList;
        }
        return aVar.equals("Z") ? null : a(aVar + "t" + System.currentTimeMillis() + this.q);
    }

    private static String a(String str) {
        int i = 0;
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        byte nextInt = (byte) new Random().nextInt(MotionEventCompat.ACTION_MASK);
        byte nextInt2 = (byte) new Random().nextInt(MotionEventCompat.ACTION_MASK);
        byte[] bArr = new byte[(bytes.length + 2)];
        int length = bytes.length;
        int i2 = 0;
        while (i < length) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) (bytes[i] ^ nextInt);
            i++;
            i2 = i3;
        }
        i = i2 + 1;
        bArr[i2] = nextInt;
        i2 = i + 1;
        bArr[i] = nextInt2;
        return a(bArr);
    }

    private static String a(byte[] bArr) {
        char[] cArr = new char[(((bArr.length + 2) / 3) * 4)];
        int i = 0;
        int i2 = 0;
        while (i2 < bArr.length) {
            Object obj;
            Object obj2;
            int i3 = (bArr[i2] & MotionEventCompat.ACTION_MASK) << 8;
            if (i2 + 1 < bArr.length) {
                i3 |= bArr[i2 + 1] & MotionEventCompat.ACTION_MASK;
                obj = 1;
            } else {
                obj = null;
            }
            i3 <<= 8;
            if (i2 + 2 < bArr.length) {
                i3 |= bArr[i2 + 2] & MotionEventCompat.ACTION_MASK;
                obj2 = 1;
            } else {
                obj2 = null;
            }
            cArr[i + 3] = r[obj2 != null ? 63 - (i3 & 63) : 64];
            int i4 = i3 >> 6;
            cArr[i + 2] = r[obj != null ? 63 - (i4 & 63) : 64];
            i3 = i4 >> 6;
            cArr[i + 1] = r[63 - (i3 & 63)];
            cArr[i + 0] = r[63 - ((i3 >> 6) & 63)];
            i2 += 3;
            i += 4;
        }
        return new String(cArr);
    }

    private void a(CellLocation cellLocation) {
        int i = 0;
        if (cellLocation != null && this.c != null) {
            a aVar = new a();
            String networkOperator = this.c.getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 0) {
                try {
                    if (networkOperator.length() >= 3) {
                        int intValue = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                        if (intValue < 0) {
                            intValue = this.d.c;
                        }
                        aVar.c = intValue;
                    }
                    String substring = networkOperator.substring(3);
                    if (substring != null) {
                        char[] toCharArray = substring.toCharArray();
                        while (i < toCharArray.length && Character.isDigit(toCharArray[i])) {
                            i++;
                        }
                    }
                    i = Integer.valueOf(substring.substring(0, i)).intValue();
                    if (i < 0) {
                        i = this.d.d;
                    }
                    aVar.d = i;
                } catch (Exception e) {
                }
            }
            if (cellLocation instanceof GsmCellLocation) {
                aVar.a = ((GsmCellLocation) cellLocation).getLac();
                aVar.b = ((GsmCellLocation) cellLocation).getCid();
                aVar.e = 'g';
            } else if (cellLocation instanceof CdmaCellLocation) {
                aVar.e = 'w';
                if (h == null) {
                    try {
                        h = Class.forName("android.telephony.cdma.CdmaCellLocation");
                        e = h.getMethod("getBaseStationId", new Class[0]);
                        f = h.getMethod("getNetworkId", new Class[0]);
                        g = h.getMethod("getSystemId", new Class[0]);
                    } catch (Exception e2) {
                        h = null;
                        return;
                    }
                }
                if (h != null && h.isInstance(cellLocation)) {
                    try {
                        i = ((Integer) g.invoke(cellLocation, new Object[0])).intValue();
                        if (i < 0) {
                            i = this.d.d;
                        }
                        aVar.d = i;
                        aVar.b = ((Integer) e.invoke(cellLocation, new Object[0])).intValue();
                        aVar.a = ((Integer) f.invoke(cellLocation, new Object[0])).intValue();
                    } catch (Exception e3) {
                        return;
                    }
                }
            }
            if (aVar.a()) {
                this.d = aVar;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean a() {
        String str = null;
        this.o = null;
        this.p = 0;
        WifiInfo connectionInfo = this.i.getConnectionInfo();
        if (connectionInfo == null) {
            return false;
        }
        try {
            String bssid = connectionInfo.getBSSID();
            if (bssid != null) {
                str = bssid.replace(":", "");
            }
            if (str.length() != 12) {
                return false;
            }
            this.o = new String(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getLocString() {
        try {
            return a(3);
        } catch (Exception e) {
            return null;
        }
    }

    public String getLocString(int i) {
        try {
            return a(i);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean startWifiScan() {
        if (this.i == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.n;
        if (currentTimeMillis <= 5000 && currentTimeMillis >= 0) {
            return false;
        }
        if (this.i.isWifiEnabled()) {
            if (this.l == null || this.k == null) {
                this.i.startScan();
            } else {
                try {
                    this.l.invoke(this.k, new Object[]{Boolean.valueOf(this.m)});
                } catch (Exception e) {
                    this.i.startScan();
                }
            }
            this.n = System.currentTimeMillis();
            return true;
        }
        this.n = 0;
        return false;
    }
}
