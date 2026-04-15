package com.baidu.location;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class h {
    private static int a = 100;
    /* renamed from: do */
    private static String f175do = (f.aa + "/juz.dat");
    /* renamed from: for */
    private static float f176for = 299.0f;
    /* renamed from: if */
    private static String f177if = f.v;
    /* renamed from: int */
    private static ArrayList f178int = null;
    /* renamed from: new */
    private static int f179new = 64;
    /* renamed from: try */
    private static long f180try = 64;

    private static class a {
        /* access modifiers changed from: private */
        public int a = 0;
        /* access modifiers changed from: private */
        /* renamed from: do */
        public int f169do = 0;
        /* access modifiers changed from: private */
        /* renamed from: for */
        public float f170for = 0.0f;
        /* access modifiers changed from: private */
        /* renamed from: if */
        public int f171if = 0;
        /* access modifiers changed from: private */
        /* renamed from: int */
        public double f172int = 0.0d;
        /* access modifiers changed from: private */
        /* renamed from: new */
        public double f173new = 0.0d;
        /* access modifiers changed from: private */
        /* renamed from: try */
        public int f174try = 0;

        public a(int i, int i2, int i3, int i4, double d, double d2, float f) {
            this.f169do = i;
            this.f174try = i2;
            this.f171if = i3;
            this.a = i4;
            this.f173new = d;
            this.f172int = d2;
            this.f170for = f;
        }

        public boolean a(int i, int i2, int i3) {
            return this.a == i && this.f169do == i2 && this.f174try == i3;
        }
    }

    h() {
    }

    public static String a(int i, int i2, int i3) {
        if (m951if(i, i2, i3) == null) {
            return null;
        }
        return String.format("{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"65\"},\"content\":{\"point\":{\"x\":" + "\"%f\",\"y\":\"%f\"},\"radius\":\"%d\"}}", new Object[]{Double.valueOf(r0.f173new), Double.valueOf(r0.f172int), Integer.valueOf((int) r0.f170for)});
    }

    private static void a() {
        File file = new File(f175do);
        try {
            if (file.exists()) {
                if (f178int != null) {
                    f178int.clear();
                    f178int = null;
                }
                f178int = new ArrayList();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(0);
                int readInt = randomAccessFile.readInt();
                j.a(f177if, "size of loc cache is " + readInt);
                for (int i = 0; i < readInt; i++) {
                    randomAccessFile.seek(f180try + ((long) (f179new * i)));
                    float readFloat = randomAccessFile.readFloat();
                    int readInt2 = randomAccessFile.readInt();
                    double readDouble = randomAccessFile.readDouble();
                    f178int.add(new a(randomAccessFile.readInt(), randomAccessFile.readInt(), readInt2, randomAccessFile.readInt(), readDouble, randomAccessFile.readDouble(), readFloat));
                }
                randomAccessFile.close();
                return;
            }
            j.a(f177if, "locCache file does not exists...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(com.baidu.location.c.a aVar, double d, double d2, float f) {
        if (aVar != null) {
            float f2 = f < f176for ? f176for : f;
            a aVar2 = m951if(aVar.f95if, aVar.f94for, aVar.f98try);
            if (aVar2 == null) {
                if (f178int == null) {
                    f178int = new ArrayList();
                }
                f178int.add(new a(aVar.f94for, aVar.f98try, aVar.f93do, aVar.f95if, d, d2, f2));
                if (f178int.size() > a) {
                    f178int.remove(0);
                }
                j.a(f177if, "locCache add new cell info into loc cache ...");
                return;
            }
            aVar2.f173new = d;
            aVar2.f172int = d2;
            aVar2.f170for = f2;
            j.a(f177if, "locCache update loc cache ...");
        }
    }

    /* renamed from: do */
    private static void m950do() {
        if (f178int != null) {
            File file = new File(f175do);
            try {
                if (!file.exists()) {
                    File file2 = new File(f.aa);
                    if (!file2.exists()) {
                        j.a(f177if, "locCache make dirs " + file2.mkdirs());
                    }
                    if (file.createNewFile()) {
                        j.a(f177if, "locCache create loc cache file success ...");
                    } else {
                        j.a(f177if, "locCache create loc cache file failure ...");
                        return;
                    }
                }
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if (randomAccessFile.length() < 1) {
                    randomAccessFile.writeInt(0);
                }
                int size = f178int.size() - 1;
                int i = 0;
                while (size >= 0) {
                    a aVar = (a) f178int.get(size);
                    if (aVar != null) {
                        randomAccessFile.seek(f180try + ((long) (f179new * (size % a))));
                        randomAccessFile.writeFloat(aVar.f170for);
                        randomAccessFile.writeInt(aVar.f171if);
                        randomAccessFile.writeDouble(aVar.f173new);
                        randomAccessFile.writeInt(aVar.a);
                        randomAccessFile.writeDouble(aVar.f172int);
                        randomAccessFile.writeInt(aVar.f169do);
                        randomAccessFile.writeInt(aVar.f174try);
                        j.a(f177if, "add a new cell loc into file ...");
                    }
                    size--;
                    i++;
                }
                randomAccessFile.seek(0);
                randomAccessFile.writeInt(i);
                randomAccessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: if */
    private static a m951if(int i, int i2, int i3) {
        try {
            if (f178int == null || f178int.size() < 1) {
                a();
            }
            if (f178int == null || f178int.size() < 1) {
                return null;
            }
            for (int size = f178int.size() - 1; size >= 0; size--) {
                a aVar = (a) f178int.get(size);
                if (aVar != null && aVar.a(i, i2, i3)) {
                    return aVar;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: if */
    public static void m952if() {
        m950do();
    }
}
