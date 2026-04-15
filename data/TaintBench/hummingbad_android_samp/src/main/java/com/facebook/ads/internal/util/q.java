package com.facebook.ads.internal.util;

import android.graphics.Bitmap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class q {
    static final int a;
    static final ExecutorService b;
    private static volatile boolean c = true;
    private final Bitmap d;
    private Bitmap e;
    private final j f = new n();

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        a = availableProcessors;
        b = Executors.newFixedThreadPool(availableProcessors);
    }

    public q(Bitmap bitmap) {
        this.d = bitmap;
    }

    public Bitmap a() {
        return this.e;
    }

    public Bitmap a(int i) {
        this.e = this.f.a(this.d, (float) i);
        return this.e;
    }
}
