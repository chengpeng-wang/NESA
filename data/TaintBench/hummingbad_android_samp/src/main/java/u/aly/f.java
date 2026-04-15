package u.aly;

import android.content.Context;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/* compiled from: ImprintHandler */
public class f {
    private static final String a = ".imprint";
    private static final byte[] b = "pbl0".getBytes();
    private T c = null;
    private Context d;

    public f(Context context) {
        this.d = context;
    }

    public String a(T t) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : new TreeMap(t.d()).entrySet()) {
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(((U) entry.getValue()).c());
            stringBuilder.append(((U) entry.getValue()).f());
            stringBuilder.append(((U) entry.getValue()).j());
        }
        stringBuilder.append(t.b);
        return av.a(stringBuilder.toString()).toLowerCase(Locale.US);
    }

    private boolean c(T t) {
        if (!t.k().equals(a(t))) {
            return false;
        }
        for (U u : t.d().values()) {
            byte[] b = c.b(u.j());
            byte[] a = a(u);
            for (int i = 0; i < 4; i++) {
                if (b[i] != a[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public byte[] a(U u) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(null);
        allocate.putLong(u.f());
        byte[] array = allocate.array();
        byte[] bArr = b;
        byte[] bArr2 = new byte[4];
        for (int i = 0; i < 4; i++) {
            bArr2[i] = (byte) (array[i] ^ bArr[i]);
        }
        return bArr2;
    }

    public void b(T t) {
        if (t != null && c(t)) {
            synchronized (this) {
                T t2 = this.c;
                if (t2 != null) {
                    t = a(t2, t);
                }
                this.c = t;
            }
        }
    }

    private T a(T t, T t2) {
        if (t2 != null) {
            Map d = t.d();
            for (Entry entry : t2.d().entrySet()) {
                if (((U) entry.getValue()).e()) {
                    d.put((String) entry.getKey(), (U) entry.getValue());
                } else {
                    d.remove(entry.getKey());
                }
            }
            t.a(t2.h());
            t.a(a(t));
        }
        return t;
    }

    public synchronized T a() {
        return this.c;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026 A:{SYNTHETIC, Splitter:B:8:0x0026} */
    public void b() {
        /*
        r4 = this;
        r2 = 0;
        r0 = new java.io.File;
        r1 = r4.d;
        r1 = r1.getFilesDir();
        r3 = ".imprint";
        r0.<init>(r1, r3);
        r0 = r0.exists();
        if (r0 != 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r0 = r4.d;	 Catch:{ Exception -> 0x003b, all -> 0x0044 }
        r1 = ".imprint";
        r1 = r0.openFileInput(r1);	 Catch:{ Exception -> 0x003b, all -> 0x0044 }
        r2 = u.aly.av.b(r1);	 Catch:{ Exception -> 0x004c }
        u.aly.av.c(r1);
    L_0x0024:
        if (r2 == 0) goto L_0x0014;
    L_0x0026:
        r0 = new u.aly.T;	 Catch:{ Exception -> 0x0036 }
        r0.m4313init();	 Catch:{ Exception -> 0x0036 }
        r1 = new u.aly.aC;	 Catch:{ Exception -> 0x0036 }
        r1.m2881init();	 Catch:{ Exception -> 0x0036 }
        r1.a(r0, r2);	 Catch:{ Exception -> 0x0036 }
        r4.c = r0;	 Catch:{ Exception -> 0x0036 }
        goto L_0x0014;
    L_0x0036:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0014;
    L_0x003b:
        r0 = move-exception;
        r1 = r2;
    L_0x003d:
        r0.printStackTrace();	 Catch:{ all -> 0x0049 }
        u.aly.av.c(r1);
        goto L_0x0024;
    L_0x0044:
        r0 = move-exception;
    L_0x0045:
        u.aly.av.c(r2);
        throw r0;
    L_0x0049:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0045;
    L_0x004c:
        r0 = move-exception;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.f.b():void");
    }

    public void c() {
        if (this.c != null) {
            try {
                av.a(new File(this.d.getFilesDir(), a), new aI().a(this.c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean d() {
        return new File(this.d.getFilesDir(), a).delete();
    }
}
