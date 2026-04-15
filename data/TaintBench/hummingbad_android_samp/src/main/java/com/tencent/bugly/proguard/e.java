package com.tencent.bugly.proguard;

import java.nio.ByteBuffer;
import java.util.HashMap;

/* compiled from: BUGLY */
public class e extends d {
    static HashMap<String, byte[]> h = null;
    static HashMap<String, HashMap<String, byte[]>> i = null;
    protected f g = new f();
    private int j = 0;
    private int k = 0;

    public <T> void a(String str, T t) {
        if (str.startsWith(".")) {
            throw new IllegalArgumentException("put name can not startwith . , now is " + str);
        }
        super.a(str, (Object) t);
    }

    public void b() {
        super.b();
        this.g.a = (short) 3;
    }

    public byte[] a() {
        if (this.g.a != (short) 2) {
            if (this.g.e == null) {
                this.g.e = "";
            }
            if (this.g.f == null) {
                this.g.f = "";
            }
        } else if (this.g.e == null || this.g.e.equals("")) {
            throw new IllegalArgumentException("servantName can not is null");
        } else if (this.g.f == null || this.g.f.equals("")) {
            throw new IllegalArgumentException("funcName can not is null");
        }
        l lVar = new l(0);
        lVar.a(this.c);
        if (this.g.a == (short) 2 || this.g.a == (short) 1) {
            lVar.a(this.a, 0);
        } else {
            lVar.a(this.e, 0);
        }
        this.g.g = n.a(lVar.a());
        lVar = new l(0);
        lVar.a(this.c);
        this.g.a(lVar);
        byte[] a = n.a(lVar.a());
        int length = a.length;
        ByteBuffer allocate = ByteBuffer.allocate(length + 4);
        allocate.putInt(length + 4).put(a).flip();
        return allocate.array();
    }

    private void c() {
        k kVar = new k(this.g.g);
        kVar.a(this.c);
        if (h == null) {
            h = new HashMap();
            h.put("", new byte[0]);
        }
        this.e = kVar.a(h, 0, false);
    }

    private void d() {
        k kVar = new k(this.g.g);
        kVar.a(this.c);
        if (i == null) {
            i = new HashMap();
            HashMap hashMap = new HashMap();
            hashMap.put("", new byte[0]);
            i.put("", hashMap);
        }
        this.a = kVar.a(i, 0, false);
        this.b = new HashMap();
    }

    public void a(byte[] bArr) {
        if (bArr.length < 4) {
            throw new IllegalArgumentException("decode package must include size head");
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        allocate.put(bArr2).flip();
        this.j = allocate.getInt();
        try {
            k kVar = new k(bArr, 4);
            kVar.a(this.c);
            this.g.a(kVar);
            if (this.g.a == (short) 3) {
                c();
                return;
            }
            this.e = null;
            d();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void b(String str) {
        this.g.e = str;
    }

    public void c(String str) {
        this.g.f = str;
    }

    public void a(int i) {
        this.g.d = i;
    }
}
