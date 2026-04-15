package com.baidu.location;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

class c {
    /* renamed from: byte */
    private static String f99byte = null;
    private static int c = 3;
    /* renamed from: case */
    private static Method f100case = null;
    /* renamed from: char */
    private static boolean f101char = false;
    private static Class d = null;
    /* renamed from: for */
    private static Method f102for = null;
    /* renamed from: goto */
    private static String f103goto = null;
    /* renamed from: long */
    private static Method f104long = null;
    /* access modifiers changed from: private|static */
    /* renamed from: void */
    public static long f105void = 3000;
    /* access modifiers changed from: private */
    public a a = new a();
    private boolean b = false;
    /* access modifiers changed from: private */
    /* renamed from: do */
    public Handler f106do = null;
    /* renamed from: else */
    private final String f107else = f.v;
    /* renamed from: if */
    private Context f108if = null;
    /* renamed from: int */
    private b f109int = null;
    /* access modifiers changed from: private */
    /* renamed from: new */
    public List f110new = null;
    /* access modifiers changed from: private */
    /* renamed from: try */
    public TelephonyManager f111try = null;

    public class a {
        /* renamed from: byte */
        public long f92byte;
        /* renamed from: do */
        public int f93do;
        /* renamed from: for */
        public int f94for;
        /* renamed from: if */
        public int f95if;
        /* renamed from: int */
        public int f96int;
        /* renamed from: new */
        public char f97new;
        /* renamed from: try */
        public int f98try;

        public a() {
            this.f94for = -1;
            this.f98try = -1;
            this.f93do = -1;
            this.f95if = -1;
            this.f92byte = 0;
            this.f96int = -1;
            this.f97new = 0;
            this.f92byte = System.currentTimeMillis();
        }

        public a(int i, int i2, int i3, int i4, char c) {
            this.f94for = -1;
            this.f98try = -1;
            this.f93do = -1;
            this.f95if = -1;
            this.f92byte = 0;
            this.f96int = -1;
            this.f97new = 0;
            this.f94for = i;
            this.f98try = i2;
            this.f93do = i3;
            this.f95if = i4;
            this.f97new = c;
            this.f92byte = System.currentTimeMillis() / 1000;
        }

        public String a() {
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append(this.f98try + 23);
            stringBuffer.append("H");
            stringBuffer.append(this.f94for + 45);
            stringBuffer.append("K");
            stringBuffer.append(this.f95if + 54);
            stringBuffer.append("Q");
            stringBuffer.append(this.f93do + 203);
            return stringBuffer.toString();
        }

        public boolean a(a aVar) {
            return this.f94for == aVar.f94for && this.f98try == aVar.f98try && this.f95if == aVar.f95if;
        }

        /* renamed from: do */
        public boolean m797do() {
            return System.currentTimeMillis() - this.f92byte < c.f105void;
        }

        /* renamed from: for */
        public boolean m798for() {
            return this.f94for > -1 && this.f98try > 0;
        }

        /* renamed from: if */
        public String m799if() {
            StringBuffer stringBuffer = new StringBuffer(64);
            stringBuffer.append(String.format("cell=%d|%d|%d|%d:%d", new Object[]{Integer.valueOf(this.f93do), Integer.valueOf(this.f95if), Integer.valueOf(this.f94for), Integer.valueOf(this.f98try), Integer.valueOf(this.f96int)}));
            return stringBuffer.toString();
        }

        /* renamed from: int */
        public String m800int() {
            String str;
            try {
                List<NeighboringCellInfo> neighboringCellInfo = c.this.f111try.getNeighboringCellInfo();
                if (neighboringCellInfo == null || neighboringCellInfo.isEmpty()) {
                    str = null;
                    j.a(f.v, "Neighbour:" + str);
                    return str;
                }
                String str2 = "&nc=";
                int i = 0;
                for (NeighboringCellInfo neighboringCellInfo2 : neighboringCellInfo) {
                    if (i != 0) {
                        if (i >= 8) {
                            break;
                        }
                        str = neighboringCellInfo2.getLac() != this.f94for ? str2 + ";" + neighboringCellInfo2.getLac() + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi() : str2 + ";" + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi();
                    } else {
                        str = neighboringCellInfo2.getLac() != this.f94for ? str2 + neighboringCellInfo2.getLac() + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi() : str2 + "|" + neighboringCellInfo2.getCid() + "|" + neighboringCellInfo2.getRssi();
                    }
                    i++;
                    str2 = str;
                }
                str = str2;
                j.a(f.v, "Neighbour:" + str);
                return str;
            } catch (Exception e) {
                str = null;
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append("&nw=");
            stringBuffer.append(c.this.a.f97new);
            stringBuffer.append(String.format("&cl=%d|%d|%d|%d&cl_s=%d", new Object[]{Integer.valueOf(this.f93do), Integer.valueOf(this.f95if), Integer.valueOf(this.f94for), Integer.valueOf(this.f98try), Integer.valueOf(this.f96int)}));
            stringBuffer.append("&cl_t=");
            stringBuffer.append(this.f92byte);
            if (c.this.f110new != null && c.this.f110new.size() > 0) {
                int size = c.this.f110new.size();
                stringBuffer.append("&clt=");
                for (int i = 0; i < size; i++) {
                    a aVar = (a) c.this.f110new.get(i);
                    if (aVar.f93do != this.f93do) {
                        stringBuffer.append(aVar.f93do);
                    }
                    stringBuffer.append("|");
                    if (aVar.f95if != this.f95if) {
                        stringBuffer.append(aVar.f95if);
                    }
                    stringBuffer.append("|");
                    if (aVar.f94for != this.f94for) {
                        stringBuffer.append(aVar.f94for);
                    }
                    stringBuffer.append("|");
                    if (aVar.f98try != this.f98try) {
                        stringBuffer.append(aVar.f98try);
                    }
                    stringBuffer.append("|");
                    if (i != size - 1) {
                        stringBuffer.append(aVar.f92byte / 1000);
                    } else {
                        stringBuffer.append((System.currentTimeMillis() - aVar.f92byte) / 1000);
                    }
                    stringBuffer.append(";");
                }
            }
            return stringBuffer.toString();
        }
    }

    private class b extends PhoneStateListener {
        public void onCellLocationChanged(CellLocation cellLocation) {
            if (cellLocation != null) {
                try {
                    c.this.a(c.this.f111try.getCellLocation());
                } catch (Exception e) {
                }
            }
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (c.this.a != null) {
                if (c.this.a.f97new == 'g') {
                    c.this.a.f96int = signalStrength.getGsmSignalStrength();
                } else if (c.this.a.f97new == 'c') {
                    c.this.a.f96int = signalStrength.getCdmaDbm();
                }
                j.a("cell strength", "===== cell singal strength changed : " + c.this.a.f96int);
                if (c.this.f106do != null) {
                    c.this.f106do.obtainMessage(31).sendToTarget();
                }
            }
        }
    }

    public c(Context context, Handler handler) {
        this.f108if = context;
        this.f106do = handler;
    }

    public static String a(boolean z) {
        StringBuffer stringBuffer = new StringBuffer(256);
        stringBuffer.append("&sdk=");
        stringBuffer.append(3.1f);
        if (!j.f209try && !j.v && z && j.j.equals("all")) {
            stringBuffer.append("&addr=all");
        }
        if (z) {
            if (j.v) {
                stringBuffer.append("&coor=wgs84");
            } else {
                stringBuffer.append("&coor=gcj02");
            }
        }
        if (f103goto == null) {
            stringBuffer.append("&im=");
            stringBuffer.append(f99byte);
        } else {
            stringBuffer.append("&cu=");
            stringBuffer.append(f103goto);
        }
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&resid=");
        if (j.f209try) {
            stringBuffer.append("13");
        } else if (j.v) {
            stringBuffer.append("11");
        } else {
            stringBuffer.append("12");
        }
        stringBuffer.append("&os=A");
        stringBuffer.append(VERSION.SDK);
        if (z) {
            stringBuffer.append("&sv=");
            String str = VERSION.RELEASE;
            if (str != null && str.length() > 5) {
                str = str.substring(0, 5);
            }
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: private */
    public void a(CellLocation cellLocation) {
        if (cellLocation != null && this.f111try != null) {
            int intValue;
            if (!f101char) {
                f99byte = this.f111try.getDeviceId();
                f101char = m807if();
            }
            j.a(f.v, "set cell info..");
            a aVar = new a();
            aVar.f92byte = System.currentTimeMillis();
            String networkOperator = this.f111try.getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 0) {
                try {
                    if (networkOperator.length() >= 3) {
                        intValue = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                        if (intValue < 0) {
                            intValue = this.a.f93do;
                        }
                        aVar.f93do = intValue;
                    }
                    networkOperator = networkOperator.substring(3);
                    if (networkOperator != null) {
                        char[] toCharArray = networkOperator.toCharArray();
                        intValue = 0;
                        while (intValue < toCharArray.length && Character.isDigit(toCharArray[intValue])) {
                            intValue++;
                        }
                    } else {
                        intValue = 0;
                    }
                    intValue = Integer.valueOf(networkOperator.substring(0, intValue)).intValue();
                    if (intValue < 0) {
                        intValue = this.a.f95if;
                    }
                    aVar.f95if = intValue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (cellLocation instanceof GsmCellLocation) {
                aVar.f94for = ((GsmCellLocation) cellLocation).getLac();
                aVar.f98try = ((GsmCellLocation) cellLocation).getCid();
                aVar.f97new = 'g';
            } else if (cellLocation instanceof CdmaCellLocation) {
                aVar.f97new = 'c';
                if (Integer.parseInt(VERSION.SDK) >= 5) {
                    if (d == null) {
                        try {
                            d = Class.forName("android.telephony.cdma.CdmaCellLocation");
                            f104long = d.getMethod("getBaseStationId", new Class[0]);
                            f100case = d.getMethod("getNetworkId", new Class[0]);
                            f102for = d.getMethod("getSystemId", new Class[0]);
                        } catch (Exception e2) {
                            d = null;
                            e2.printStackTrace();
                            return;
                        }
                    }
                    if (d != null && d.isInstance(cellLocation)) {
                        try {
                            intValue = ((Integer) f102for.invoke(cellLocation, new Object[0])).intValue();
                            if (intValue < 0) {
                                intValue = this.a.f95if;
                            }
                            aVar.f95if = intValue;
                            aVar.f98try = ((Integer) f104long.invoke(cellLocation, new Object[0])).intValue();
                            aVar.f94for = ((Integer) f100case.invoke(cellLocation, new Object[0])).intValue();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                            return;
                        }
                    }
                }
                return;
            }
            if (!aVar.m798for()) {
                return;
            }
            if (this.a == null || !this.a.a(aVar)) {
                this.a = aVar;
                this.f106do.obtainMessage(31).sendToTarget();
                if (aVar.m798for()) {
                    if (this.f110new == null) {
                        this.f110new = new LinkedList();
                    }
                    intValue = this.f110new.size();
                    a aVar2 = intValue == 0 ? null : (a) this.f110new.get(intValue - 1);
                    if (aVar2 == null || aVar2.f98try != this.a.f98try || aVar2.f94for != this.a.f94for) {
                        if (aVar2 != null) {
                            aVar2.f92byte = this.a.f92byte - aVar2.f92byte;
                        }
                        this.f110new.add(this.a);
                        if (this.f110new.size() > c) {
                            this.f110new.remove(0);
                        }
                    }
                } else if (this.f110new != null) {
                    this.f110new.clear();
                }
            }
        }
    }

    /* renamed from: if */
    private boolean m807if() {
        if (f99byte == null || f99byte.length() < 10) {
            return false;
        }
        try {
            char[] toCharArray = f99byte.toCharArray();
            int i = 0;
            while (i < 10) {
                if (toCharArray[i] > '9' || toCharArray[i] < '0') {
                    return false;
                }
                i++;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public a a() {
        if (!((this.a != null && this.a.m797do() && this.a.m798for()) || this.f111try == null)) {
            try {
                a(this.f111try.getCellLocation());
            } catch (Exception e) {
            }
        }
        return this.a;
    }

    /* renamed from: byte */
    public void m809byte() {
        if (this.b) {
            if (!(this.f109int == null || this.f111try == null)) {
                this.f111try.listen(this.f109int, 0);
            }
            this.f109int = null;
            this.f111try = null;
            this.f110new.clear();
            this.f110new = null;
            j.a(f.v, "cell manager stop ...");
            this.b = false;
        }
    }

    /* renamed from: do */
    public void m810do() {
        if (!this.b) {
            this.f111try = (TelephonyManager) this.f108if.getSystemService("phone");
            this.f110new = new LinkedList();
            this.f109int = new b();
            if (this.f111try != null && this.f109int != null) {
                try {
                    this.f111try.listen(this.f109int, 272);
                    f99byte = this.f111try.getDeviceId();
                    j.f = f99byte + "|" + Build.MODEL;
                } catch (Exception e) {
                }
                try {
                    f103goto = com.baidu.location.j.a.m963if(this.f108if);
                    j.a(f.v, "CUID:" + f103goto);
                } catch (Exception e2) {
                    f103goto = null;
                }
                try {
                    if (f103goto != null) {
                        j.f = "v3.1|" + f103goto + "|" + Build.MODEL;
                    }
                    j.a(f.v, "CUID:" + j.f);
                } catch (Exception e3) {
                }
                f101char = m807if();
                j.m976if(f.v, "i:" + f99byte);
                j.a(f.v, "cell manager start...");
                this.b = true;
            }
        }
    }

    /* renamed from: for */
    public String m811for() {
        if (this.f111try == null) {
            this.f111try = (TelephonyManager) this.f108if.getSystemService("phone");
        }
        try {
            a(this.f111try.getCellLocation());
        } catch (Exception e) {
        }
        return this.a.toString();
    }

    /* renamed from: int */
    public String m812int() {
        return f99byte;
    }

    /* renamed from: new */
    public int m813new() {
        return this.f111try.getNetworkType();
    }
}
