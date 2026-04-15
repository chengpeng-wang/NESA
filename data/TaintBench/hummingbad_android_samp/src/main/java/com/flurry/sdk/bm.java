package com.flurry.sdk;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class bm implements Closeable {
    private static final Pattern a = Pattern.compile("[a-z0-9_-]{1,64}");
    /* access modifiers changed from: private|static|final */
    public static final OutputStream p = new bo();
    /* access modifiers changed from: private|final */
    public final File b;
    private final File c;
    private final File d;
    private final File e;
    private final int f;
    private long g;
    /* access modifiers changed from: private|final */
    public final int h;
    private long i = 0;
    /* access modifiers changed from: private */
    public Writer j;
    private final LinkedHashMap k = new LinkedHashMap(0, 0.75f, true);
    /* access modifiers changed from: private */
    public int l;
    private long m = 0;
    private final ThreadPoolExecutor n = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable o = new bn(this);

    private bm(File file, long j) {
        this.b = file;
        this.f = 1;
        this.c = new File(file, "journal");
        this.d = new File(file, "journal.tmp");
        this.e = new File(file, "journal.bkp");
        this.h = 1;
        this.g = j;
    }

    public static bm a(File file, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            File file3 = new File(file, "journal");
            if (file3.exists()) {
                file2.delete();
            } else {
                a(file2, file3, false);
            }
        }
        bm bmVar = new bm(file, j);
        if (bmVar.c.exists()) {
            try {
                bmVar.b();
                bmVar.c();
                bmVar.j = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bmVar.c, true), bv.a));
                return bmVar;
            } catch (IOException e) {
                System.out.println("DiskLruCache " + file + " is corrupt: " + e.getMessage() + ", removing");
                bmVar.close();
                bv.a(bmVar.b);
            }
        }
        file.mkdirs();
        bmVar = new bm(file, j);
        bmVar.d();
        return bmVar;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(bp bpVar, boolean z) {
        int i = 0;
        synchronized (this) {
            br brVar = bpVar.a;
            if (brVar.d != bpVar) {
                throw new IllegalStateException();
            }
            if (z) {
                if (!brVar.c) {
                    int i2 = 0;
                    while (i2 < this.h) {
                        if (!bpVar.b[i2]) {
                            bpVar.b();
                            throw new IllegalStateException("Newly created entry didn't create value for index " + i2);
                        } else if (!brVar.b(i2).exists()) {
                            bpVar.b();
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
            while (i < this.h) {
                File b = brVar.b(i);
                if (!z) {
                    a(b);
                } else if (b.exists()) {
                    File a = brVar.a(i);
                    b.renameTo(a);
                    long j = brVar.b[i];
                    long length = a.length();
                    brVar.b[i] = length;
                    this.i = (this.i - j) + length;
                }
                i++;
            }
            this.l++;
            brVar.d = null;
            if ((brVar.c | z) != 0) {
                brVar.c = true;
                this.j.write("CLEAN " + brVar.a + brVar.a() + 10);
                if (z) {
                    long j2 = this.m;
                    this.m = 1 + j2;
                    brVar.e = j2;
                }
            } else {
                this.k.remove(brVar.a);
                this.j.write("REMOVE " + brVar.a + 10);
            }
            this.j.flush();
            if (this.i > this.g || e()) {
                this.n.submit(this.o);
            }
        }
    }

    private static void a(File file) {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void a(File file, File file2, boolean z) {
        if (z) {
            a(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f3 A:{Catch:{ EOFException -> 0x00b3, all -> 0x008c }} */
    private void b() {
        /*
        r10 = this;
        r9 = 5;
        r0 = 0;
        r8 = -1;
        r3 = new com.flurry.sdk.bt;
        r1 = new java.io.FileInputStream;
        r2 = r10.c;
        r1.<init>(r2);
        r3.m997init(r1);
        r1 = r3.a();	 Catch:{ all -> 0x008c }
        r2 = r3.a();	 Catch:{ all -> 0x008c }
        r4 = r3.a();	 Catch:{ all -> 0x008c }
        r5 = r3.a();	 Catch:{ all -> 0x008c }
        r6 = r3.a();	 Catch:{ all -> 0x008c }
        r7 = "libcore.io.DiskLruCache";
        r7 = r7.equals(r1);	 Catch:{ all -> 0x008c }
        if (r7 == 0) goto L_0x0053;
    L_0x002b:
        r7 = "1";
        r7 = r7.equals(r2);	 Catch:{ all -> 0x008c }
        if (r7 == 0) goto L_0x0053;
    L_0x0033:
        r7 = r10.f;	 Catch:{ all -> 0x008c }
        r7 = java.lang.Integer.toString(r7);	 Catch:{ all -> 0x008c }
        r4 = r7.equals(r4);	 Catch:{ all -> 0x008c }
        if (r4 == 0) goto L_0x0053;
    L_0x003f:
        r4 = r10.h;	 Catch:{ all -> 0x008c }
        r4 = java.lang.Integer.toString(r4);	 Catch:{ all -> 0x008c }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x008c }
        if (r4 == 0) goto L_0x0053;
    L_0x004b:
        r4 = "";
        r4 = r4.equals(r6);	 Catch:{ all -> 0x008c }
        if (r4 != 0) goto L_0x0091;
    L_0x0053:
        r0 = new java.io.IOException;	 Catch:{ all -> 0x008c }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008c }
        r7 = "unexpected journal header: [";
        r4.<init>(r7);	 Catch:{ all -> 0x008c }
        r1 = r4.append(r1);	 Catch:{ all -> 0x008c }
        r4 = ", ";
        r1 = r1.append(r4);	 Catch:{ all -> 0x008c }
        r1 = r1.append(r2);	 Catch:{ all -> 0x008c }
        r2 = ", ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x008c }
        r1 = r1.append(r5);	 Catch:{ all -> 0x008c }
        r2 = ", ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x008c }
        r1 = r1.append(r6);	 Catch:{ all -> 0x008c }
        r2 = "]";
        r1 = r1.append(r2);	 Catch:{ all -> 0x008c }
        r1 = r1.toString();	 Catch:{ all -> 0x008c }
        r0.<init>(r1);	 Catch:{ all -> 0x008c }
        throw r0;	 Catch:{ all -> 0x008c }
    L_0x008c:
        r0 = move-exception;
        com.flurry.sdk.bv.a(r3);
        throw r0;
    L_0x0091:
        r1 = r0;
    L_0x0092:
        r4 = r3.a();	 Catch:{ EOFException -> 0x00b3 }
        r0 = 32;
        r5 = r4.indexOf(r0);	 Catch:{ EOFException -> 0x00b3 }
        if (r5 != r8) goto L_0x00c2;
    L_0x009e:
        r0 = new java.io.IOException;	 Catch:{ EOFException -> 0x00b3 }
        r2 = new java.lang.StringBuilder;	 Catch:{ EOFException -> 0x00b3 }
        r5 = "unexpected journal line: ";
        r2.<init>(r5);	 Catch:{ EOFException -> 0x00b3 }
        r2 = r2.append(r4);	 Catch:{ EOFException -> 0x00b3 }
        r2 = r2.toString();	 Catch:{ EOFException -> 0x00b3 }
        r0.<init>(r2);	 Catch:{ EOFException -> 0x00b3 }
        throw r0;	 Catch:{ EOFException -> 0x00b3 }
    L_0x00b3:
        r0 = move-exception;
        r0 = r10.k;	 Catch:{ all -> 0x008c }
        r0 = r0.size();	 Catch:{ all -> 0x008c }
        r0 = r1 - r0;
        r10.l = r0;	 Catch:{ all -> 0x008c }
        com.flurry.sdk.bv.a(r3);
        return;
    L_0x00c2:
        r0 = r5 + 1;
        r2 = 32;
        r6 = r4.indexOf(r2, r0);	 Catch:{ EOFException -> 0x00b3 }
        if (r6 != r8) goto L_0x00e4;
    L_0x00cc:
        r0 = r4.substring(r0);	 Catch:{ EOFException -> 0x00b3 }
        r2 = 6;
        if (r5 != r2) goto L_0x0157;
    L_0x00d3:
        r2 = "REMOVE";
        r2 = r4.startsWith(r2);	 Catch:{ EOFException -> 0x00b3 }
        if (r2 == 0) goto L_0x0157;
    L_0x00db:
        r2 = r10.k;	 Catch:{ EOFException -> 0x00b3 }
        r2.remove(r0);	 Catch:{ EOFException -> 0x00b3 }
    L_0x00e0:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0092;
    L_0x00e4:
        r0 = r4.substring(r0, r6);	 Catch:{ EOFException -> 0x00b3 }
        r2 = r0;
    L_0x00e9:
        r0 = r10.k;	 Catch:{ EOFException -> 0x00b3 }
        r0 = r0.get(r2);	 Catch:{ EOFException -> 0x00b3 }
        r0 = (com.flurry.sdk.br) r0;	 Catch:{ EOFException -> 0x00b3 }
        if (r0 != 0) goto L_0x00fe;
    L_0x00f3:
        r0 = new com.flurry.sdk.br;	 Catch:{ EOFException -> 0x00b3 }
        r7 = 0;
        r0.m994init(r10, r2, r7);	 Catch:{ EOFException -> 0x00b3 }
        r7 = r10.k;	 Catch:{ EOFException -> 0x00b3 }
        r7.put(r2, r0);	 Catch:{ EOFException -> 0x00b3 }
    L_0x00fe:
        if (r6 == r8) goto L_0x0120;
    L_0x0100:
        if (r5 != r9) goto L_0x0120;
    L_0x0102:
        r2 = "CLEAN";
        r2 = r4.startsWith(r2);	 Catch:{ EOFException -> 0x00b3 }
        if (r2 == 0) goto L_0x0120;
    L_0x010a:
        r2 = r6 + 1;
        r2 = r4.substring(r2);	 Catch:{ EOFException -> 0x00b3 }
        r4 = " ";
        r2 = r2.split(r4);	 Catch:{ EOFException -> 0x00b3 }
        r4 = 1;
        r0.c = r4;	 Catch:{ EOFException -> 0x00b3 }
        r4 = 0;
        r0.d = r4;	 Catch:{ EOFException -> 0x00b3 }
        r0.a(r2);	 Catch:{ EOFException -> 0x00b3 }
        goto L_0x00e0;
    L_0x0120:
        if (r6 != r8) goto L_0x0135;
    L_0x0122:
        if (r5 != r9) goto L_0x0135;
    L_0x0124:
        r2 = "DIRTY";
        r2 = r4.startsWith(r2);	 Catch:{ EOFException -> 0x00b3 }
        if (r2 == 0) goto L_0x0135;
    L_0x012c:
        r2 = new com.flurry.sdk.bp;	 Catch:{ EOFException -> 0x00b3 }
        r4 = 0;
        r2.m990init(r10, r0, r4);	 Catch:{ EOFException -> 0x00b3 }
        r0.d = r2;	 Catch:{ EOFException -> 0x00b3 }
        goto L_0x00e0;
    L_0x0135:
        if (r6 != r8) goto L_0x0142;
    L_0x0137:
        r0 = 4;
        if (r5 != r0) goto L_0x0142;
    L_0x013a:
        r0 = "READ";
        r0 = r4.startsWith(r0);	 Catch:{ EOFException -> 0x00b3 }
        if (r0 != 0) goto L_0x00e0;
    L_0x0142:
        r0 = new java.io.IOException;	 Catch:{ EOFException -> 0x00b3 }
        r2 = new java.lang.StringBuilder;	 Catch:{ EOFException -> 0x00b3 }
        r5 = "unexpected journal line: ";
        r2.<init>(r5);	 Catch:{ EOFException -> 0x00b3 }
        r2 = r2.append(r4);	 Catch:{ EOFException -> 0x00b3 }
        r2 = r2.toString();	 Catch:{ EOFException -> 0x00b3 }
        r0.<init>(r2);	 Catch:{ EOFException -> 0x00b3 }
        throw r0;	 Catch:{ EOFException -> 0x00b3 }
    L_0x0157:
        r2 = r0;
        goto L_0x00e9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.bm.b():void");
    }

    private void c() {
        a(this.d);
        Iterator it = this.k.values().iterator();
        while (it.hasNext()) {
            br brVar = (br) it.next();
            int i;
            if (brVar.d == null) {
                for (i = 0; i < this.h; i++) {
                    this.i += brVar.b[i];
                }
            } else {
                brVar.d = null;
                for (i = 0; i < this.h; i++) {
                    a(brVar.a(i));
                    a(brVar.b(i));
                }
                it.remove();
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void d() {
        if (this.j != null) {
            this.j.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d), bv.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write("1");
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(Integer.toString(this.f));
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(Integer.toString(this.h));
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            bufferedWriter.write(MASTNativeAdConstants.NEWLINE);
            for (br brVar : this.k.values()) {
                if (brVar.d != null) {
                    bufferedWriter.write("DIRTY " + brVar.a + 10);
                } else {
                    bufferedWriter.write("CLEAN " + brVar.a + brVar.a() + 10);
                }
            }
            bufferedWriter.close();
            if (this.c.exists()) {
                a(this.c, this.e, true);
            }
            a(this.d, this.c, false);
            this.e.delete();
            this.j = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.c, true), bv.a));
        } catch (Throwable th) {
            bufferedWriter.close();
        }
    }

    private static void d(String str) {
        if (!a.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + str + "\"");
        }
    }

    /* access modifiers changed from: private */
    public boolean e() {
        return this.l >= 2000 && this.l >= this.k.size();
    }

    private void f() {
        if (this.j == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        while (this.i > this.g) {
            c((String) ((Entry) this.k.entrySet().iterator().next()).getKey());
        }
    }

    public final synchronized bs a(String str) {
        bs bsVar;
        f();
        d(str);
        br brVar = (br) this.k.get(str);
        if (brVar == null) {
            bsVar = null;
        } else if (brVar.c) {
            InputStream[] inputStreamArr = new InputStream[this.h];
            int i = 0;
            while (i < this.h) {
                try {
                    inputStreamArr[i] = new FileInputStream(brVar.a(i));
                    i++;
                } catch (FileNotFoundException e) {
                    int i2 = 0;
                    while (i2 < this.h && inputStreamArr[i2] != null) {
                        bv.a(inputStreamArr[i2]);
                        i2++;
                    }
                    bsVar = null;
                }
            }
            this.l++;
            this.j.append("READ ").append(str).append(10);
            if (e()) {
                this.n.submit(this.o);
            }
            bsVar = new bs(this, inputStreamArr, (byte) 0);
        } else {
            bsVar = null;
        }
        return bsVar;
    }

    /* access modifiers changed from: final|declared_synchronized */
    public final synchronized bp b(String str) {
        bp bpVar;
        f();
        d(str);
        br brVar = (br) this.k.get(str);
        if (-1 == -1 || (brVar != null && brVar.e == -1)) {
            br brVar2;
            if (brVar == null) {
                brVar = new br(this, str, (byte) 0);
                this.k.put(str, brVar);
                brVar2 = brVar;
            } else if (brVar.d != null) {
                bpVar = null;
            } else {
                brVar2 = brVar;
            }
            bpVar = new bp(this, brVar2, (byte) 0);
            brVar2.d = bpVar;
            this.j.write("DIRTY " + str + 10);
            this.j.flush();
        } else {
            bpVar = null;
        }
        return bpVar;
    }

    public final synchronized boolean c(String str) {
        boolean z;
        int i = 0;
        synchronized (this) {
            f();
            d(str);
            br brVar = (br) this.k.get(str);
            if (brVar == null || brVar.d != null) {
                z = false;
            } else {
                while (i < this.h) {
                    File a = brVar.a(i);
                    if (!a.exists() || a.delete()) {
                        this.i -= brVar.b[i];
                        brVar.b[i] = 0;
                        i++;
                    } else {
                        throw new IOException("failed to delete " + a);
                    }
                }
                this.l++;
                this.j.append("REMOVE ").append(str).append(10);
                this.k.remove(str);
                if (e()) {
                    this.n.submit(this.o);
                }
                z = true;
            }
        }
        return z;
    }

    public final synchronized void close() {
        if (this.j != null) {
            Iterator it = new ArrayList(this.k.values()).iterator();
            while (it.hasNext()) {
                br brVar = (br) it.next();
                if (brVar.d != null) {
                    brVar.d.b();
                }
            }
            g();
            this.j.close();
            this.j = null;
        }
    }
}
