package com.baidu.android.pushservice.b;

import android.text.TextUtils;

public class f {
    public static int a = -1;
    public static int b = 0;
    public static int c = 1;
    public static int d = 2;
    public static String e = "01";
    public static String f = "02";
    public static String g = "03";
    private int h;
    private String i;
    private long j;
    private String k;
    private int l;
    private String m = "0";

    public f(f fVar) {
        this.i = fVar.i;
        this.j = fVar.j;
        this.l = fVar.l;
        this.k = fVar.k;
    }

    public f(String str, long j, String str2, int i, String str3) {
        this.i = str;
        this.j = j;
        this.k = str2;
        this.l = i;
        this.m = str3;
    }

    public void a(int i) {
        this.l = i;
    }

    public void a(long j) {
        this.j = j;
    }

    public int c() {
        return this.h;
    }

    public void c(String str) {
        this.i = str;
    }

    public String d() {
        return this.i;
    }

    public void d(String str) {
        this.k = str;
    }

    public long e() {
        return this.j;
    }

    public void e(String str) {
        this.m = str;
    }

    public String f() {
        return this.k;
    }

    public int g() {
        return this.l;
    }

    public String h() {
        return this.m;
    }

    public int i() {
        return TextUtils.isEmpty(this.i) ? a : this.i.startsWith(e) ? b : this.i.startsWith(f) ? c : this.i.startsWith(g) ? d : a;
    }
}
