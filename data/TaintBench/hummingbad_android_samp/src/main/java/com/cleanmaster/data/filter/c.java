package com.cleanmaster.data.filter;

/* compiled from: Or */
public class c implements b {
    b a;
    b b;

    public c(b bVar, b bVar2) {
        this.a = bVar;
        this.b = bVar2;
    }

    public boolean a(Object obj) {
        return this.a.a(obj) || this.b.a(obj);
    }

    public String toString() {
        return String.format("(OR %s %s)", new Object[]{this.a.toString(), this.b.toString()});
    }
}
