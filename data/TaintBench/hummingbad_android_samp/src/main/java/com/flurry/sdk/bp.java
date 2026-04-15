package com.flurry.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public final class bp {
    final br a;
    final boolean[] b;
    boolean c;
    final /* synthetic */ bm d;

    private bp(bm bmVar, br brVar) {
        this.d = bmVar;
        this.a = brVar;
        this.b = brVar.c ? null : new boolean[bmVar.h];
    }

    /* synthetic */ bp(bm bmVar, br brVar, byte b) {
        this(bmVar, brVar);
    }

    public final OutputStream a() {
        OutputStream a;
        synchronized (this.d) {
            if (this.a.d != this) {
                throw new IllegalStateException();
            }
            OutputStream fileOutputStream;
            if (!this.a.c) {
                this.b[0] = true;
            }
            File b = this.a.b(0);
            try {
                fileOutputStream = new FileOutputStream(b);
            } catch (FileNotFoundException e) {
                this.d.b.mkdirs();
                try {
                    fileOutputStream = new FileOutputStream(b);
                } catch (FileNotFoundException e2) {
                    a = bm.p;
                }
            }
            a = new bq(this, fileOutputStream, (byte) 0);
        }
        return a;
    }

    public final void b() {
        this.d.a(this, false);
    }
}
