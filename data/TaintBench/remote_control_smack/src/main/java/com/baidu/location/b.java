package com.baidu.location;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class b {
    private static final int E = 5;
    /* access modifiers changed from: private|static */
    public static int G = 0;
    /* access modifiers changed from: private|static */
    public static long H = 0;
    /* access modifiers changed from: private|static */
    public static long J = 0;
    private static final int K = 5;
    private static String L = "Temp_in.dat";
    /* access modifiers changed from: private|static */
    public static int b = 0;
    /* access modifiers changed from: private|static */
    /* renamed from: byte */
    public static long f79byte = 0;
    /* access modifiers changed from: private|static */
    public static int c = 0;
    /* access modifiers changed from: private|static */
    /* renamed from: case */
    public static int f80case = 0;
    /* access modifiers changed from: private|static */
    /* renamed from: char */
    public static boolean f81char = true;
    /* renamed from: else */
    private static final double f82else = 1.0E-5d;
    /* renamed from: goto */
    private static final int f83goto = 3000;
    private static final int h = 1024;
    private static final int i = 1000;
    /* renamed from: if */
    private static final String f84if = "baidu_location_service";
    /* access modifiers changed from: private|static */
    public static int j = 0;
    /* access modifiers changed from: private|static */
    public static int k = 0;
    private static final int l = 12;
    private static final int n = 1;
    /* access modifiers changed from: private|static */
    public static String p = null;
    private static final int q = 3;
    private static final int t = 100;
    /* renamed from: void */
    private static final int f85void = 3600;
    /* access modifiers changed from: private|static */
    public static StringBuffer w = null;
    private static final int x = 5;
    /* access modifiers changed from: private|static */
    public static File y = new File(f.aa, L);
    private static final int z = 750;
    /* access modifiers changed from: private */
    public boolean A = false;
    private String B = null;
    /* access modifiers changed from: private */
    public long C = 0;
    private Location D;
    private Handler F = null;
    private final int I = 400;
    /* access modifiers changed from: private */
    public GpsStatus a;
    /* access modifiers changed from: private */
    public long d = 0;
    /* access modifiers changed from: private */
    /* renamed from: do */
    public LocationManager f86do = null;
    /* access modifiers changed from: private */
    public boolean e = false;
    private Context f;
    /* renamed from: for */
    private d f87for = null;
    /* access modifiers changed from: private */
    public String g = null;
    /* access modifiers changed from: private */
    /* renamed from: int */
    public boolean f88int = false;
    /* access modifiers changed from: private */
    /* renamed from: long */
    public long f89long = 0;
    /* access modifiers changed from: private */
    public String m = null;
    /* renamed from: new */
    private a f90new = null;
    private final long o = 1000;
    private boolean r = false;
    /* access modifiers changed from: private */
    public List s = new ArrayList();
    /* access modifiers changed from: private */
    /* renamed from: try */
    public String f91try = null;
    private boolean u = true;
    private b v = null;

    private class a implements NmeaListener, Listener {
        private a() {
        }

        public void onGpsStatusChanged(int i) {
            if (b.this.f86do != null) {
                switch (i) {
                    case 2:
                        b.this.a(null);
                        b.this.a(false);
                        b.k = 0;
                        return;
                    case 4:
                        j.a("baidu_location_service", "gps status change");
                        if (b.this.a == null) {
                            b.this.a = b.this.f86do.getGpsStatus(null);
                        } else {
                            b.this.f86do.getGpsStatus(b.this.a);
                        }
                        int i2 = 0;
                        for (GpsSatellite usedInFix : b.this.a.getSatellites()) {
                            i2 = usedInFix.usedInFix() ? i2 + 1 : i2;
                        }
                        j.a("baidu_location_service", "gps nunmber in count:" + i2);
                        if (b.k >= 3 && i2 < 3) {
                            b.this.d = System.currentTimeMillis();
                        }
                        if (i2 < 3) {
                            b.this.a(false);
                        }
                        if (b.k <= 3 && i2 > 3) {
                            b.this.a(true);
                        }
                        b.k = i2;
                        return;
                    default:
                        return;
                }
            }
        }

        public void onNmeaReceived(long j, String str) {
            if (str != null && !str.equals("")) {
                if (System.currentTimeMillis() - b.this.C > 400 && b.this.e && b.this.s.size() > 0) {
                    try {
                        c cVar = new c(b.this.s, b.this.g, b.this.f91try, b.this.m);
                        if (cVar.m746if()) {
                            j.f210void = cVar.c();
                            if (j.f210void > 0) {
                                b.p = String.format("&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&nmea=%.1f|%.1f&ll_t=%d&g_tp=%d", new Object[]{Double.valueOf(cVar.d()), Double.valueOf(cVar.l()), Double.valueOf(cVar.m740case()), Double.valueOf(cVar.j()), Integer.valueOf(0), Integer.valueOf(cVar.m745goto()), Double.valueOf(cVar.m750try()), Double.valueOf(cVar.a()), Double.valueOf(cVar.b()), Long.valueOf(r6 / 1000), Integer.valueOf(j.f210void)});
                            }
                        } else {
                            j.f210void = 0;
                            j.a("baidu_location_service", "nmea invalid");
                        }
                    } catch (Exception e) {
                        j.f210void = 0;
                    }
                    b.this.s.clear();
                    b.this.g = b.this.f91try = b.this.m = null;
                    b.this.e = false;
                }
                if (str.startsWith(c.f67if)) {
                    b.this.e = true;
                    b.this.g = str.trim();
                } else if (str.startsWith(c.g)) {
                    b.this.s.add(str.trim());
                } else if (str.startsWith(c.f68int)) {
                    b.this.f91try = str.trim();
                } else if (str.startsWith(c.f66for)) {
                    b.this.m = str.trim();
                }
                b.this.C = System.currentTimeMillis();
            }
        }
    }

    private class b implements LocationListener {
        private b() {
        }

        public void onLocationChanged(Location location) {
            b.this.a(location);
            b.this.f88int = false;
            if (b.this.A) {
                b.this.a(true);
            }
        }

        public void onProviderDisabled(String str) {
            b.this.a(null);
            b.this.a(false);
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                    b.this.a(null);
                    b.this.a(false);
                    return;
                case 1:
                    b.this.f89long = System.currentTimeMillis();
                    b.this.f88int = true;
                    b.this.a(false);
                    return;
                case 2:
                    b.this.f88int = false;
                    return;
                default:
                    return;
            }
        }
    }

    public class c {
        /* renamed from: for */
        public static final String f66for = "$GPGSA";
        public static final String g = "$GPGSV";
        /* renamed from: if */
        public static final String f67if = "$GPGGA";
        /* renamed from: int */
        public static final String f68int = "$GPRMC";
        private double a = 500.0d;
        private double b = 0.0d;
        /* renamed from: byte */
        private double f69byte = 500.0d;
        private char c = 'N';
        /* renamed from: case */
        private double f70case = 0.0d;
        /* renamed from: char */
        private double f71char = 0.0d;
        private String d = "";
        /* renamed from: do */
        public int f72do = 0;
        private List e = null;
        /* renamed from: else */
        private double f73else = 500.0d;
        private boolean f = true;
        /* renamed from: goto */
        private String f74goto = "";
        private int h = 0;
        private boolean i = false;
        private double j = 0.0d;
        private String k = "";
        /* renamed from: long */
        private boolean f75long = false;
        private int m = 0;
        private int n = 0;
        /* renamed from: new */
        private List f76new = null;
        private double o = 0.0d;
        private String p = "";
        private String q = "";
        private boolean r = false;
        /* renamed from: try */
        private int f77try = 1;
        /* renamed from: void */
        private boolean f78void = false;

        public class a {
            private int a = 0;
            /* renamed from: do */
            private int f62do = 0;
            /* renamed from: if */
            private int f64if = 0;
            /* renamed from: int */
            private int f65int = 0;

            public a(int i, int i2, int i3, int i4) {
                this.f65int = i;
                this.a = i2;
                this.f64if = i3;
                this.f62do = i4;
            }

            public int a() {
                return this.a;
            }

            /* renamed from: do */
            public int m734do() {
                return this.f62do;
            }

            /* renamed from: for */
            public int m735for() {
                return this.f65int;
            }

            /* renamed from: if */
            public int m736if() {
                return this.f64if;
            }
        }

        public c(List list, String str, String str2, String str3) {
            this.f76new = list;
            this.d = str;
            this.q = str2;
            this.p = str3;
            this.e = new ArrayList();
            m751void();
        }

        private boolean a(String str) {
            if (str == null || str.length() < 4) {
                return false;
            }
            int i = 0;
            for (int i2 = 1; i2 < str.length() - 3; i2++) {
                i ^= str.charAt(i2);
            }
            return Integer.toHexString(i).equalsIgnoreCase(str.substring(str.length() + -2, str.length()));
        }

        private double[] a(double d, double d2) {
            double d3 = 0.0d;
            if (d2 != 0.0d) {
                d3 = Math.toDegrees(Math.atan(d / d2));
            } else if (d > 0.0d) {
                d3 = 90.0d;
            } else if (d < 0.0d) {
                d3 = 270.0d;
            }
            return new double[]{Math.sqrt((d * d) + (d2 * d2)), d3};
        }

        private double[] a(List list) {
            if (list == null || list.size() <= 0) {
                return null;
            }
            double[] dArr = m738if((double) (90 - ((a) list.get(0)).m736if()), (double) ((a) list.get(0)).a());
            if (list.size() > 1) {
                for (int i = 1; i < list.size(); i++) {
                    double[] dArr2 = m738if((double) (90 - ((a) list.get(i)).m736if()), (double) ((a) list.get(i)).a());
                    dArr[0] = (dArr[0] + dArr2[0]) / 2.0d;
                    dArr[1] = (dArr[1] + dArr2[1]) / 2.0d;
                }
            }
            return dArr;
        }

        /* renamed from: if */
        private double[] m738if(double d, double d2) {
            return new double[]{Math.sin(Math.toRadians(d2)) * d, Math.cos(Math.toRadians(d2)) * d};
        }

        public double a() {
            return this.f70case;
        }

        public int a(boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            if (!this.f) {
                return 0;
            }
            int i;
            int i2;
            if (z && this.r) {
                this.f72do = 1;
                if (this.m >= j.a) {
                    return 1;
                }
                if (this.m <= j.t) {
                    return 4;
                }
            }
            if (z2 && this.i) {
                this.f72do = 2;
                if (this.f70case <= ((double) j.f199case)) {
                    return 1;
                }
                if (this.f70case >= ((double) j.A)) {
                    return 4;
                }
            }
            if (z3 && this.i) {
                this.f72do = 3;
                if (this.b <= ((double) j.f200char)) {
                    return 1;
                }
                if (this.b >= ((double) j.C)) {
                    return 4;
                }
            }
            if (z4 && this.f78void) {
                this.f72do = 4;
                i = 0;
                Iterator it = this.e.iterator();
                while (true) {
                    i2 = i;
                    if (!it.hasNext()) {
                        break;
                    }
                    i = ((a) it.next()).m734do() >= j.f206int ? i2 + 1 : i2;
                }
                if (i2 >= j.f205if) {
                    return 1;
                }
                if (i2 <= j.u) {
                    return 4;
                }
            }
            if (z5 && this.f78void) {
                int i3;
                this.f72do = 5;
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (i = 0; i < 10; i++) {
                    arrayList.add(new ArrayList());
                }
                i = 0;
                Iterator it2 = this.e.iterator();
                while (true) {
                    i3 = i;
                    if (!it2.hasNext()) {
                        break;
                    }
                    a aVar = (a) it2.next();
                    if (aVar.m734do() >= 10 && aVar.m736if() >= 1) {
                        ((List) arrayList.get((aVar.m734do() - 10) / 5)).add(aVar);
                        i3++;
                    }
                    i = i3;
                }
                if (i3 < 4) {
                    return 4;
                }
                double[] a;
                i = 0;
                while (true) {
                    i2 = i;
                    if (i2 >= arrayList.size()) {
                        break;
                    }
                    if (((List) arrayList.get(i2)).size() != 0) {
                        a = a((List) arrayList.get(i2));
                        if (a != null) {
                            arrayList2.add(a);
                            arrayList3.add(Integer.valueOf(i2));
                        }
                    }
                    i = i2 + 1;
                }
                if (arrayList2 == null || arrayList2.size() <= 0) {
                    return 4;
                }
                a = (double[]) arrayList2.get(0);
                a[0] = a[0] * ((double) ((Integer) arrayList3.get(0)).intValue());
                a[1] = a[1] * ((double) ((Integer) arrayList3.get(0)).intValue());
                if (arrayList2.size() > 1) {
                    i2 = 1;
                    while (true) {
                        int i4 = i2;
                        if (i4 >= arrayList2.size()) {
                            break;
                        }
                        double[] dArr = (double[]) arrayList2.get(i4);
                        dArr[0] = dArr[0] * ((double) ((Integer) arrayList3.get(i4)).intValue());
                        dArr[1] = dArr[1] * ((double) ((Integer) arrayList3.get(i4)).intValue());
                        a[0] = (a[0] + dArr[0]) / 2.0d;
                        a[1] = (a[1] + dArr[1]) / 2.0d;
                        i2 = i4 + 1;
                    }
                }
                a = a(a[0], a[1]);
                if (a[0] <= ((double) j.k)) {
                    return 1;
                }
                if (a[0] >= ((double) j.K)) {
                    return 4;
                }
            }
            this.f72do = 0;
            return 3;
        }

        public double b() {
            return this.b;
        }

        /* renamed from: byte */
        public String m739byte() {
            return this.k;
        }

        public int c() {
            return a(true, true, true, true, true);
        }

        /* renamed from: case */
        public double m740case() {
            return this.j;
        }

        /* renamed from: char */
        public boolean m741char() {
            return this.r;
        }

        public double d() {
            return this.f69byte;
        }

        /* renamed from: do */
        public List m742do() {
            return this.e;
        }

        public String e() {
            return this.p;
        }

        /* renamed from: else */
        public List m743else() {
            return this.f76new;
        }

        public int f() {
            return this.f77try;
        }

        /* renamed from: for */
        public double m744for() {
            return this.f71char;
        }

        public int g() {
            return this.h;
        }

        /* renamed from: goto */
        public int m745goto() {
            return this.m;
        }

        public boolean h() {
            return this.i;
        }

        public boolean i() {
            return this.f75long;
        }

        /* renamed from: if */
        public boolean m746if() {
            return this.f;
        }

        /* renamed from: int */
        public String m747int() {
            return this.f74goto;
        }

        public double j() {
            return this.a;
        }

        public String k() {
            return this.q;
        }

        public double l() {
            return this.f73else;
        }

        /* renamed from: long */
        public boolean m748long() {
            return this.f78void;
        }

        public int m() {
            return this.n;
        }

        public String n() {
            return this.d;
        }

        /* renamed from: new */
        public char m749new() {
            return this.c;
        }

        /* renamed from: try */
        public double m750try() {
            return this.o;
        }

        /* renamed from: void */
        public void m751void() {
            String substring;
            int i;
            int i2;
            String[] split;
            if (a(this.d)) {
                substring = this.d.substring(0, this.d.length() - 3);
                i = 0;
                for (i2 = 0; i2 < substring.length(); i2++) {
                    if (substring.charAt(i2) == ',') {
                        i++;
                    }
                }
                split = substring.split(",", i + 1);
                if (!(split[1].equals("") || split[2].equals("") || split[4].equals("") || split[6].equals("") || split[7].equals("") || split[9].equals(""))) {
                    i2 = 1;
                    i = 1;
                    if (split[3].equals("S")) {
                        i2 = -1;
                    }
                    if (split[5].equals("W")) {
                        i = -1;
                    }
                    this.f73else = ((double) i2) * (((double) Integer.valueOf(split[2].substring(0, 2)).intValue()) + (Double.valueOf(split[2].substring(2, split[2].length())).doubleValue() / 60.0d));
                    this.f69byte = ((double) i) * (((double) Integer.valueOf(split[4].substring(0, 3)).intValue()) + (Double.valueOf(split[4].substring(3, split[4].length())).doubleValue() / 60.0d));
                    this.o = Double.valueOf(split[9]).doubleValue();
                    this.h = Integer.valueOf(split[6]).intValue();
                    this.m = Integer.valueOf(split[7]).intValue();
                    this.r = true;
                }
            }
            if (a(this.q)) {
                substring = this.q.substring(0, this.q.length() - 3);
                i = 0;
                for (i2 = 0; i2 < substring.length(); i2++) {
                    if (substring.charAt(i2) == ',') {
                        i++;
                    }
                }
                split = substring.split(",", i + 1);
                if (!(split[9].equals("") || split[2].equals(""))) {
                    this.c = Character.valueOf(split[2].charAt(0)).charValue();
                    this.j = split[7].equals("") ? 0.0d : 1.852d * Double.valueOf(split[7]).doubleValue();
                    double doubleValue = (split[8].equals("") || split[8].equalsIgnoreCase("nan")) ? 500.0d : Double.valueOf(split[8]).doubleValue();
                    this.a = doubleValue;
                    this.f75long = true;
                }
            }
            if (a(this.p)) {
                substring = this.p.substring(0, this.p.length() - 3);
                i = 0;
                for (i2 = 0; i2 < substring.length(); i2++) {
                    if (substring.charAt(i2) == ',') {
                        i++;
                    }
                }
                String[] split2 = substring.split(",", i + 1);
                if (!(split2[2].equals("") || split2[split2.length - 3].equals("") || split2[split2.length - 2].equals("") || split2[split2.length - 1].equals(""))) {
                    this.f77try = Integer.valueOf(split2[2]).intValue();
                    this.b = Double.valueOf(split2[split2.length - 3]).doubleValue();
                    this.f70case = Double.valueOf(split2[split2.length - 2]).doubleValue();
                    this.f71char = Double.valueOf(split2[split2.length - 1]).doubleValue();
                    this.i = true;
                }
            }
            if (this.f76new == null || this.f76new.size() <= 0) {
                this.f78void = false;
            } else {
                this.f78void = Integer.valueOf(((String) this.f76new.get(0)).split(",")[1]).intValue() == this.f76new.size();
                if (this.f78void) {
                    for (String str : this.f76new) {
                        String str2;
                        if (!a(str2)) {
                            this.f78void = false;
                            break;
                        }
                        str2 = str2.split(",", 5)[4];
                        substring = str2.substring(0, str2.length() - 3);
                        i = 0;
                        for (i2 = 0; i2 < substring.length(); i2++) {
                            if (substring.charAt(i2) == ',') {
                                i++;
                            }
                        }
                        String[] split3 = substring.split(",", i + 1);
                        i2 = 0;
                        while (true) {
                            int i3 = i2;
                            if (i3 < split3.length) {
                                if (!(split3[i3].equals("") || split3[i3 + 1].equals("") || split3[i3 + 2].equals(""))) {
                                    this.n++;
                                    this.e.add(new a(Integer.valueOf(split3[i3]).intValue(), Integer.valueOf(split3[i3 + 2]).intValue(), Integer.valueOf(split3[i3 + 1]).intValue(), split3[i3 + 3].equals("") ? 0 : Integer.valueOf(split3[i3 + 3]).intValue()));
                                }
                                i2 = i3 + 4;
                            }
                        }
                    }
                }
            }
            boolean z = this.r && this.i;
            this.f = z;
        }
    }

    public static class d {
        private String a = null;

        public d(String str) {
            if (str == null) {
                str = "";
            } else if (str.length() > 100) {
                str = str.substring(0, 100);
            }
            this.a = str;
        }

        private boolean a(String str) {
            if (str == null || !str.startsWith("&nr")) {
                return false;
            }
            if (!b.y.exists() && !b.d()) {
                return false;
            }
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(b.y, "rw");
                randomAccessFile.seek(0);
                int readInt = randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                int readInt3 = randomAccessFile.readInt();
                if (b.m781if(readInt, readInt2, readInt3)) {
                    if (j.G) {
                        if (readInt == j.l) {
                            if (str.equals(a(readInt3 == 1 ? j.l : readInt3 - 1))) {
                                randomAccessFile.close();
                                return false;
                            }
                        } else if (readInt3 > 1 && str.equals(a(readInt3 - 1))) {
                            randomAccessFile.close();
                            return false;
                        }
                    }
                    randomAccessFile.seek(((long) (((readInt3 - 1) * 1024) + b.l)) + 0);
                    if (str.length() > b.z) {
                        return false;
                    }
                    String str2 = Jni.m673if(str);
                    int length = str2.length();
                    if (length > 1020) {
                        return false;
                    }
                    randomAccessFile.writeInt(length);
                    randomAccessFile.writeBytes(str2);
                    int i;
                    if (readInt == 0) {
                        randomAccessFile.seek(0);
                        randomAccessFile.writeInt(1);
                        randomAccessFile.writeInt(1);
                        randomAccessFile.writeInt(2);
                    } else if (readInt < j.l - 1) {
                        randomAccessFile.seek(0);
                        randomAccessFile.writeInt(readInt + 1);
                        randomAccessFile.seek(8);
                        randomAccessFile.writeInt(readInt + 2);
                    } else if (readInt == j.l - 1) {
                        randomAccessFile.seek(0);
                        randomAccessFile.writeInt(j.l);
                        if (readInt2 == 0 || readInt2 == 1) {
                            randomAccessFile.writeInt(2);
                        }
                        randomAccessFile.seek(8);
                        randomAccessFile.writeInt(1);
                    } else if (readInt3 == readInt2) {
                        readInt = readInt3 == j.l ? 1 : readInt3 + 1;
                        i = readInt == j.l ? 1 : readInt + 1;
                        randomAccessFile.seek(4);
                        randomAccessFile.writeInt(i);
                        randomAccessFile.writeInt(readInt);
                    } else {
                        readInt = readInt3 == j.l ? 1 : readInt3 + 1;
                        if (readInt == readInt2) {
                            i = readInt == j.l ? 1 : readInt + 1;
                            randomAccessFile.seek(4);
                            randomAccessFile.writeInt(i);
                        }
                        randomAccessFile.seek(8);
                        randomAccessFile.writeInt(readInt);
                    }
                    randomAccessFile.close();
                    return true;
                }
                randomAccessFile.close();
                b.d();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        public int a() {
            if (!b.y.exists()) {
                return 0;
            }
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(b.y, "rw");
                randomAccessFile.seek(0);
                int readInt = randomAccessFile.readInt();
                int readInt2 = randomAccessFile.readInt();
                int readInt3 = randomAccessFile.readInt();
                if (b.m781if(readInt3, readInt2, readInt3)) {
                    return readInt;
                }
                randomAccessFile.close();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public String a(int i) {
            if (!b.y.exists()) {
                return null;
            }
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(b.y, "rw");
                randomAccessFile.seek(0);
                int readInt = randomAccessFile.readInt();
                if (!b.m781if(readInt, randomAccessFile.readInt(), randomAccessFile.readInt())) {
                    randomAccessFile.close();
                    b.d();
                    return null;
                } else if (i == 0 || i == readInt + 1) {
                    return null;
                } else {
                    long j = (12 + 0) + ((long) ((i - 1) * 1024));
                    randomAccessFile.seek(j);
                    int readInt2 = randomAccessFile.readInt();
                    byte[] bArr = new byte[readInt2];
                    randomAccessFile.seek(j + 4);
                    for (readInt = 0; readInt < readInt2; readInt++) {
                        bArr[readInt] = randomAccessFile.readByte();
                    }
                    randomAccessFile.close();
                    return new String(bArr);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void a(boolean z) {
            j.m = z;
        }

        public boolean a(Location location) {
            return a(location, j.i, j.g);
        }

        public boolean a(Location location, int i, int i2) {
            if (location == null || !j.m) {
                return false;
            }
            if (j.i < 5) {
                j.i = 5;
            } else if (j.i > 1000) {
                j.i = 1000;
            }
            if (j.g < 5) {
                j.g = 5;
            } else if (j.g > b.f85void) {
                j.g = b.f85void;
            }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            long time = location.getTime() / 1000;
            if (b.f81char) {
                b.f80case = 1;
                b.w = new StringBuffer("");
                b.w.append(String.format("&nr=%s&traj=%d,%.5f,%.5f|", new Object[]{this.a, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)}));
                b.c = b.w.length();
                b.f79byte = time;
                b.H = (long) Math.floor((longitude * 100000.0d) + 0.5d);
                b.J = (long) Math.floor((latitude * 100000.0d) + 0.5d);
                b.f81char = false;
                return true;
            }
            float[] fArr = new float[1];
            Location.distanceBetween(latitude, longitude, b.f82else * ((double) b.J), b.f82else * ((double) b.H), fArr);
            long m = time - b.f79byte;
            if (fArr[0] < ((float) j.i) && m < ((long) j.g)) {
                return false;
            }
            if (b.w == null) {
                b.m758byte();
                b.c = 0;
                b.w = new StringBuffer("");
                b.w.append(String.format("&nr=%s&traj=%d,%.5f,%.5f|", new Object[]{this.a, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)}));
                b.c = b.w.length();
                b.f79byte = time;
                b.H = (long) Math.floor((longitude * 100000.0d) + 0.5d);
                b.J = (long) Math.floor((latitude * 100000.0d) + 0.5d);
            } else {
                long floor = (long) Math.floor((longitude * 100000.0d) + 0.5d);
                long floor2 = (long) Math.floor((latitude * 100000.0d) + 0.5d);
                b.j = (int) (time - b.f79byte);
                b.G = (int) (floor - b.H);
                b.b = (int) (floor2 - b.J);
                b.w.append(String.format("%d,%d,%d|", new Object[]{Integer.valueOf(b.j), Integer.valueOf(b.G), Integer.valueOf(b.b)}));
                b.c = b.w.length();
                b.f79byte = time;
                b.H = floor;
                b.J = floor2;
            }
            if (b.c + 15 > b.z) {
                a(b.w.toString());
                b.w = null;
            }
            if (b.f80case >= j.l) {
                j.m = false;
            }
            return true;
        }

        /* renamed from: do */
        public void m753do() {
            if (b.w != null && b.w.length() >= 100) {
                a(b.w.toString());
            }
            b.m789void();
        }

        /* renamed from: for */
        public boolean m754for() {
            if (b.y.exists()) {
                b.y.delete();
            }
            b.m789void();
            return !b.y.exists();
        }

        /* renamed from: if */
        public boolean m755if() {
            return j.m;
        }
    }

    public b(Context context, Handler handler) {
        this.f = context;
        this.F = handler;
        try {
            if (j.f != null) {
                this.f87for = new d(j.f);
            } else {
                this.f87for = new d("NULL");
            }
        } catch (Exception e) {
            this.f87for = null;
        }
    }

    private void a(double d, double d2, float f) {
        int i = 0;
        j.a("baidu_location_service", "check...gps ...");
        if (d >= 73.146973d && d <= 135.252686d && d2 <= 54.258807d && d2 >= 14.604847d && f <= 18.0f) {
            j.a("baidu_location_service", "check...gps2 ...");
            int i2 = (int) ((d - j.s) * 1000.0d);
            int i3 = (int) ((j.f198byte - d2) * 1000.0d);
            j.a("baidu_location_service", "check...gps ..." + i2 + i3);
            if (i2 <= 0 || i2 >= 50 || i3 <= 0 || i3 >= 50) {
                String str = String.format("&ll=%.5f|%.5f", new Object[]{Double.valueOf(d), Double.valueOf(d2)}) + "&im=" + j.f;
                j.O = d;
                j.c = d2;
                g.a(str, true);
            } else {
                j.a("baidu_location_service", "check...gps ..." + i2 + i3);
                i2 += i3 * 50;
                i3 = i2 >> 2;
                i2 &= 3;
                if (j.e) {
                    i = (j.o[i3] >> (i2 * 2)) & 3;
                    j.a("baidu_location_service", "check gps scacity..." + i);
                }
            }
        }
        if (j.f207long != i) {
            j.f207long = i;
            try {
                if (j.f207long == 3) {
                    this.f86do.removeUpdates(this.v);
                    this.f86do.requestLocationUpdates("gps", 1000, 1.0f, this.v);
                    return;
                }
                this.f86do.removeUpdates(this.v);
                this.f86do.requestLocationUpdates("gps", 1000, 5.0f, this.v);
            } catch (Exception e) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Location location) {
        j.a("baidu_location_service", "set new gpsLocation ...");
        this.D = location;
        if (this.D == null) {
            this.B = null;
        } else {
            this.D.setTime(System.currentTimeMillis());
            this.B = String.format("&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_n=%d&ll_t=%d", new Object[]{Double.valueOf(this.D.getLongitude()), Double.valueOf(this.D.getLatitude()), Float.valueOf((float) (((double) this.D.getSpeed()) * 3.6d)), Float.valueOf(this.D.getBearing()), Integer.valueOf(k), Long.valueOf(r0)});
            a(this.D.getLongitude(), this.D.getLatitude(), r5);
        }
        if (this.f87for != null) {
            try {
                this.f87for.a(this.D);
            } catch (Exception e) {
            }
        }
        this.F.obtainMessage(51).sendToTarget();
    }

    /* access modifiers changed from: private */
    public void a(boolean z) {
        this.A = z;
        if ((!z || m791for()) && j.b != z) {
            j.b = z;
            if (j.I) {
                this.F.obtainMessage(53).sendToTarget();
            }
        }
    }

    public static boolean a(Location location, Location location2, boolean z) {
        if (location == location2) {
            return false;
        }
        if (location == null || location2 == null) {
            return true;
        }
        float speed = location2.getSpeed();
        if (z && j.f207long == 3 && speed < 5.0f) {
            return true;
        }
        float distanceTo = location2.distanceTo(location);
        return speed > j.Q ? distanceTo > j.H : speed > j.T ? distanceTo > j.d : distanceTo > 5.0f;
    }

    /* renamed from: byte */
    static /* synthetic */ int m758byte() {
        int i = f80case + 1;
        f80case = i;
        return i;
    }

    /* access modifiers changed from: private|static */
    public static boolean d() {
        if (y.exists()) {
            y.delete();
        }
        if (!y.getParentFile().exists()) {
            y.getParentFile().mkdirs();
        }
        try {
            y.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(y, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(1);
            randomAccessFile.close();
            m789void();
            return y.exists();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String e() {
        j.a("baidu_location_service", "GPS readline...");
        if (y == null) {
            return null;
        }
        if (!y.exists()) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(y, "rw");
            randomAccessFile.seek(0);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            if (m781if(readInt, readInt2, readInt3)) {
                j.a("baidu_location_service", "GPS readline1...");
                if (readInt2 == 0 || readInt2 == readInt3) {
                    return null;
                }
                j.a("baidu_location_service", "GPS readline2...");
                long j = 0 + ((long) (((readInt2 - 1) * 1024) + l));
                randomAccessFile.seek(j);
                int readInt4 = randomAccessFile.readInt();
                byte[] bArr = new byte[readInt4];
                randomAccessFile.seek(j + 4);
                for (readInt3 = 0; readInt3 < readInt4; readInt3++) {
                    bArr[readInt3] = randomAccessFile.readByte();
                }
                String str = new String(bArr);
                readInt3 = readInt < j.l ? readInt2 + 1 : readInt2 == j.l ? 1 : readInt2 + 1;
                randomAccessFile.seek(4);
                randomAccessFile.writeInt(readInt3);
                randomAccessFile.close();
                return str;
            }
            randomAccessFile.close();
            d();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /* renamed from: if */
    public static String m777if(Location location) {
        if (location == null) {
            return null;
        }
        float speed = (float) (((double) location.getSpeed()) * 3.6d);
        int accuracy = (int) (location.hasAccuracy() ? location.getAccuracy() : -1.0f);
        double altitude = location.hasAltitude() ? location.getAltitude() : 555.0d;
        return String.format("&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d&g_tp=0", new Object[]{Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(location.getBearing()), Integer.valueOf(accuracy), Integer.valueOf(k), Double.valueOf(altitude), Long.valueOf(location.getTime() / 1000)});
    }

    /* access modifiers changed from: private|static */
    /* renamed from: if */
    public static boolean m781if(int i, int i2, int i3) {
        return (i < 0 || i > j.l) ? false : (i2 < 0 || i2 > i + 1) ? false : i3 >= 1 && i3 <= i + 1 && i3 <= j.l;
    }

    public static String k() {
        return p;
    }

    /* access modifiers changed from: private|static */
    /* renamed from: void */
    public static void m789void() {
        f81char = true;
        w = null;
        f80case = 0;
        c = 0;
        f79byte = 0;
        H = 0;
        J = 0;
        j = 0;
        G = 0;
        b = 0;
    }

    /* renamed from: do */
    public String m790do() {
        if (this.D != null) {
            String str = "{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"61\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%d\",\"d\":\"%f\"," + "\"s\":\"%f\",\"n\":\"%d\"}}";
            int accuracy = (int) (this.D.hasAccuracy() ? this.D.getAccuracy() : 10.0f);
            float speed = (float) (((double) this.D.getSpeed()) * 3.6d);
            double[] dArr = Jni.m674if(this.D.getLongitude(), this.D.getLatitude(), "gps2gcj");
            if (dArr[0] <= 0.0d && dArr[1] <= 0.0d) {
                dArr[0] = this.D.getLongitude();
                dArr[1] = this.D.getLatitude();
            }
            String format = String.format(str, new Object[]{Double.valueOf(dArr[0]), Double.valueOf(dArr[1]), Integer.valueOf(accuracy), Float.valueOf(this.D.getBearing()), Float.valueOf(speed), Integer.valueOf(k)});
            j.a("baidu_location_service", "wgs84: " + this.D.getLongitude() + " " + this.D.getLatitude() + " gcj02: " + dArr[0] + " " + dArr[1]);
            return format;
        }
        j.a("baidu_location_service", "gps man getGpsJson but gpslocation is null");
        return null;
    }

    /* renamed from: for */
    public boolean m791for() {
        if (!m793new()) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        return (!this.f88int || currentTimeMillis - this.f89long <= 3000) ? k >= 3 ? true : currentTimeMillis - this.d < 3000 : false;
    }

    public void i() {
        if (!this.r) {
            try {
                this.f86do = (LocationManager) this.f.getSystemService("location");
                this.v = new b();
                this.f90new = new a();
                this.f86do.requestLocationUpdates("gps", 1000, 5.0f, this.v);
                this.f86do.addGpsStatusListener(this.f90new);
                this.f86do.addNmeaListener(this.f90new);
                this.r = true;
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: int */
    public Location m792int() {
        return this.D;
    }

    public void j() {
        if (this.r) {
            if (this.f86do != null) {
                try {
                    if (this.v != null) {
                        this.f86do.removeUpdates(this.v);
                    }
                    if (this.f90new != null) {
                        this.f86do.removeGpsStatusListener(this.f90new);
                        this.f86do.removeNmeaListener(this.f90new);
                    }
                    if (this.f87for != null) {
                        this.f87for.m753do();
                    }
                } catch (Exception e) {
                }
            }
            this.v = null;
            this.f90new = null;
            this.f86do = null;
            this.r = false;
            a(false);
        }
    }

    /* renamed from: new */
    public boolean m793new() {
        return (this.D == null || this.D.getLatitude() == 0.0d || this.D.getLongitude() == 0.0d) ? false : true;
    }

    /* renamed from: try */
    public String m794try() {
        return this.B;
    }
}
