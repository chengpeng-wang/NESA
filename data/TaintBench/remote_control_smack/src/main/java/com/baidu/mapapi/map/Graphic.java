package com.baidu.mapapi.map;

public class Graphic {
    private Geometry a = null;
    private Symbol b = null;
    private long c = 0;

    public Graphic(Geometry geometry, Symbol symbol) {
        this.a = geometry;
        this.b = symbol;
    }

    /* access modifiers changed from: 0000 */
    public void a(long j) {
        this.c = j;
    }

    public Geometry getGeometry() {
        return this.a;
    }

    public long getID() {
        return this.c;
    }

    public Symbol getSymbol() {
        return this.b;
    }
}
