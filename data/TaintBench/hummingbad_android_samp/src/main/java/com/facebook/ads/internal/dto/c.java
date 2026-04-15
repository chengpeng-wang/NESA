package com.facebook.ads.internal.dto;

import java.util.ArrayList;
import java.util.List;

public class c {
    private List<a> a = new ArrayList();
    private int b = 0;
    private d c;

    public c(d dVar) {
        this.c = dVar;
    }

    public d a() {
        return this.c;
    }

    public void a(a aVar) {
        this.a.add(aVar);
    }

    public int b() {
        return this.a.size();
    }

    public a c() {
        if (this.b >= this.a.size()) {
            return null;
        }
        this.b++;
        return (a) this.a.get(this.b - 1);
    }
}
