package com.flurry.sdk;

public final class bg {
    String a;
    cr b;
    long c;
    long d;
    bw e;
    long f;
    long g;

    private bg() {
    }

    /* synthetic */ bg(byte b) {
        this();
    }

    public bg(String str, cr crVar, long j) {
        this.a = str;
        this.b = crVar;
        this.c = System.currentTimeMillis();
        this.d = System.currentTimeMillis();
        this.e = bw.NONE;
        this.f = j;
        this.g = -1;
    }

    public final synchronized bw a() {
        return this.e;
    }

    public final synchronized void a(long j) {
        this.g = j;
    }

    public final synchronized void a(bw bwVar) {
        this.e = bwVar;
    }

    public final boolean b() {
        return this.f > 0 && System.currentTimeMillis() > this.f;
    }

    public final synchronized void c() {
        this.d = System.currentTimeMillis();
    }

    public final String toString() {
        return "url: " + this.a + ", type:" + this.b + ", creation:" + this.c + ", accessed:" + this.d + ", status: " + this.e + ", expiration: " + this.f + ", size: " + this.g;
    }
}
