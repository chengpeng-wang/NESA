package com.baidu.location;

import android.location.Location;
import com.baidu.location.c.a;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

class k {
    private static double a = 0.1d;
    private static int b = 64;
    /* renamed from: byte */
    private static Location f211byte = null;
    private static File c = null;
    /* renamed from: case */
    private static int f212case = 5;
    /* renamed from: char */
    private static int f213char = 1024;
    private static double d = 100.0d;
    /* renamed from: do */
    private static Location f214do = null;
    private static double e = 0.0d;
    /* renamed from: else */
    private static String f215else = f.v;
    private static ArrayList f = new ArrayList();
    /* renamed from: for */
    private static c f216for = null;
    private static int g = 256;
    /* renamed from: goto */
    private static Location f217goto = null;
    private static String h = (f.aa + "/yo.dat");
    private static int i = 32;
    /* renamed from: if */
    private static int f218if = 512;
    /* renamed from: int */
    private static int f219int = 128;
    private static int j = 1024;
    /* renamed from: long */
    private static double f220long = 30.0d;
    /* renamed from: new */
    private static int f221new = 0;
    /* renamed from: try */
    private static ArrayList f222try = new ArrayList();
    /* renamed from: void */
    private static ArrayList f223void = new ArrayList();

    k() {
    }

    private static int a(int i, int i2, int i3, long j) {
        if (i < 0 || i > 256 || i2 > 2048 || i3 > 1024 || j > 5242880) {
            return -1;
        }
        j.a(f215else, "upload manager start to init cache ...");
        try {
            if (c == null) {
                c = new File(h);
                if (!c.exists()) {
                    File file = new File(f.aa);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    if (c.createNewFile()) {
                        j.a(f215else, "upload manager create file success");
                    } else {
                        j.a(f215else, "upload manager create file error...");
                        c = null;
                        return -2;
                    }
                }
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            randomAccessFile.seek((long) i);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(i3);
            randomAccessFile.writeInt(i2);
            randomAccessFile.writeLong(j);
            randomAccessFile.close();
            j.a(f215else, "cache inited ...");
            return 0;
        } catch (Exception e) {
            return -3;
        }
    }

    private static int a(List list, int i) {
        if (list == null || i > 256 || i < 0) {
            return -1;
        }
        try {
            if (c == null) {
                c = new File(h);
                if (!c.exists()) {
                    j.a(f215else, "upload man readfile does not exist...");
                    c = null;
                    return -2;
                }
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            if (randomAccessFile.length() < 1) {
                randomAccessFile.close();
                return -3;
            }
            randomAccessFile.seek((long) i);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            int readInt4 = randomAccessFile.readInt();
            long readLong = randomAccessFile.readLong();
            if (!a(readInt, readInt2, readInt3, readInt4, readLong) || readInt2 < 1) {
                randomAccessFile.close();
                return -4;
            }
            byte[] bArr = new byte[j];
            int i2 = readInt2;
            readInt2 = i;
            while (readInt2 > 0 && i2 > 0) {
                randomAccessFile.seek(((long) ((((readInt + i2) - 1) % readInt3) * readInt4)) + readLong);
                int readInt5 = randomAccessFile.readInt();
                if (readInt5 > 0 && readInt5 < readInt4) {
                    randomAccessFile.read(bArr, 0, readInt5);
                    if (bArr[readInt5 - 1] == (byte) 0) {
                        list.add(new String(bArr, 0, readInt5 - 1));
                    }
                }
                readInt2--;
                i2--;
            }
            randomAccessFile.seek((long) i);
            randomAccessFile.writeInt(readInt);
            randomAccessFile.writeInt(i2);
            randomAccessFile.writeInt(readInt3);
            randomAccessFile.writeInt(readInt4);
            randomAccessFile.writeLong(readLong);
            randomAccessFile.close();
            return i - readInt2;
        } catch (Exception e) {
            return -5;
        }
    }

    public static String a() {
        String str = null;
        if (f223void == null || f223void.size() < 1) {
            a(f223void, f221new);
        }
        if (f223void != null && f223void.size() >= 1) {
            str = (String) f223void.get(0);
            f223void.remove(0);
            j.a(f215else, "upload manager get upload data from q1 ...");
        }
        if (str == null) {
            if (f222try == null || f222try.size() < 1) {
                a(f222try, b);
            }
            if (f222try != null && f222try.size() >= 1) {
                str = (String) f222try.get(0);
                f222try.remove(0);
                j.a(f215else, "upload manager get upload data from q2 ...");
            }
        }
        if (str == null) {
            if (f == null || f.size() < 1) {
                a(f, f219int);
            }
            if (f != null && f.size() >= 1) {
                str = (String) f.get(0);
                f.remove(0);
                j.a(f215else, "upload manager get upload data from q3 ...");
            }
        }
        j.a(f215else, "upload manager get upload data : " + str);
        return str;
    }

    public static void a(double d, double d2, double d3, double d4) {
        if (d <= 0.0d) {
            d = e;
        }
        e = d;
        a = d2;
        if (d3 <= 20.0d) {
            d3 = f220long;
        }
        f220long = d3;
        d = d4;
    }

    public static void a(a aVar, c cVar, Location location, String str) {
        c cVar2 = null;
        j.a(f215else, "upload manager insert2UploadQueue...");
        if (j.f207long == 3 && !a(location, cVar) && !a(location, false)) {
            return;
        }
        String a;
        if (aVar != null && aVar.m797do()) {
            if (!a(location, cVar)) {
                cVar = null;
            }
            a = j.a(aVar, cVar, location, str, 1);
            if (a != null) {
                m983if(Jni.m673if(a));
                f211byte = location;
                f217goto = location;
                if (cVar != null) {
                    f216for = cVar;
                }
            }
        } else if (cVar != null && cVar.m828if() && a(location, cVar)) {
            a aVar2;
            if (a(location)) {
                aVar2 = aVar;
            }
            a = j.a(aVar2, cVar, location, str, 2);
            if (a != null) {
                a = Jni.m673if(a);
                j.a(f215else, "upload size:" + a.length());
                a(a);
                f214do = location;
                f217goto = location;
                if (cVar != null) {
                    f216for = cVar;
                }
            }
        } else {
            if (!a(location)) {
                aVar = null;
            }
            if (a(location, cVar)) {
                cVar2 = cVar;
            }
            if (aVar != null || cVar2 != null) {
                String a2 = j.a(aVar, cVar2, location, str, 3);
                if (a2 != null) {
                    m980do(Jni.m673if(a2));
                    f217goto = location;
                    if (cVar2 != null) {
                        f216for = cVar2;
                    }
                }
            }
        }
    }

    public static void a(a aVar, c cVar, String str, double d, double d2, String str2) {
        String str3 = String.format("&manll=%.5f|%.5f&manaddr=%s", new Object[]{Double.valueOf(d), Double.valueOf(d2), str2}) + j.a(aVar, cVar, null, str, 1);
        if (str3 != null) {
            m983if(Jni.m673if(str3));
        }
    }

    private static void a(String str) {
        if (f222try != null) {
            j.a(f215else, "insert2WifiQueue...");
            if (f222try.size() <= i) {
                f222try.add(str);
            }
            if (f222try.size() >= i && m981if(f222try, b) < -1) {
                a(b, j, f218if, (long) (g + (j * f218if)));
                m981if(f222try, b);
            }
        }
    }

    private static boolean a(int i, int i2, int i3, int i4, long j) {
        return i >= 0 && i < i3 && i2 >= 0 && i2 <= i3 && i3 >= 0 && i3 <= 1024 && i4 >= 128 && i4 <= 1024;
    }

    private static boolean a(Location location) {
        if (location == null) {
            return false;
        }
        if (f211byte == null || f217goto == null) {
            f211byte = location;
            return true;
        }
        double distanceTo = (double) location.distanceTo(f211byte);
        return ((double) location.distanceTo(f217goto)) > ((distanceTo * ((double) j.P)) + ((((double) j.S) * distanceTo) * distanceTo)) + ((double) j.N);
    }

    private static boolean a(Location location, c cVar) {
        if (location == null || cVar == null || cVar.f113do == null || cVar.f113do.isEmpty() || cVar.m825do(f216for)) {
            return false;
        }
        if (f214do != null) {
            return true;
        }
        f214do = location;
        return true;
    }

    public static boolean a(Location location, boolean z) {
        return b.a(f217goto, location, z);
    }

    /* renamed from: do */
    private static void m980do(String str) {
        if (f != null) {
            j.a(f215else, "insert2GpsQueue...");
            if (f.size() <= i) {
                f.add(str);
            }
            if (f.size() >= i && m981if(f, f219int) < -1) {
                a(f219int, j, f213char, (long) (g + ((j * f218if) * 2)));
                m981if(f, f219int);
            }
        }
    }

    /* renamed from: if */
    private static int m981if(List list, int i) {
        int i2 = 0;
        if (list == null || list.size() < 1 || i > 256 || i < 0) {
            return -1;
        }
        try {
            if (c == null) {
                c = new File(h);
                if (!c.exists()) {
                    j.a(f215else, "upload man write file does not exist...");
                    c = null;
                    return -2;
                }
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            if (randomAccessFile.length() < 1) {
                randomAccessFile.close();
                return -3;
            }
            randomAccessFile.seek((long) i);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            int readInt4 = randomAccessFile.readInt();
            long readLong = randomAccessFile.readLong();
            if (a(readInt, readInt2, readInt3, readInt4, readLong)) {
                for (int size = list.size(); size > f212case; size--) {
                    randomAccessFile.seek(((long) ((((readInt + readInt2) + i2) % readInt3) * readInt4)) + readLong);
                    byte[] bytes = (((String) list.get(0)) + 0).getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes, 0, bytes.length);
                    list.remove(0);
                    i2++;
                }
                readInt2 += i2;
                if (readInt2 > readInt3) {
                    readInt2 = (readInt + (readInt2 - readInt3)) % readInt3;
                    readInt = readInt3;
                } else {
                    int i3 = readInt2;
                    readInt2 = readInt;
                    readInt = i3;
                }
                randomAccessFile.seek((long) i);
                randomAccessFile.writeInt(readInt2);
                randomAccessFile.writeInt(readInt);
                randomAccessFile.writeInt(readInt3);
                randomAccessFile.writeInt(readInt4);
                randomAccessFile.writeLong(readLong);
                randomAccessFile.close();
                return i2;
            }
            randomAccessFile.close();
            return -4;
        } catch (IOException e) {
            return -5;
        }
    }

    /* renamed from: if */
    public static void m982if() {
        j.a(f215else, "upload manager flush...");
        f212case = 0;
        if (m981if(f223void, f221new) < -1) {
            a(f221new, j, f218if, (long) g);
            m981if(f223void, f221new);
        }
        if (m981if(f222try, b) < -1) {
            a(b, j, f218if, (long) (g + (j * f218if)));
            m981if(f222try, b);
        }
        if (m981if(f, f219int) < -1) {
            a(f219int, j, f213char, (long) (g + ((j * f218if) * 2)));
            m981if(f, f219int);
        }
        f212case = 5;
    }

    /* renamed from: if */
    private static void m983if(String str) {
        if (f223void != null) {
            j.a(f215else, "insert2CellQueue...");
            if (f223void.size() <= i) {
                f223void.add(str);
            }
            if (f223void.size() >= i && m981if(f223void, f221new) < -1) {
                a(f221new, j, f218if, (long) g);
                m981if(f223void, f221new);
            }
        }
    }
}
