package com.baidu.mapapi.search;

import java.util.ArrayList;

public class MKRoutePlan {
    private int a;
    private int b;
    private ArrayList<MKRoute> c;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.a = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKRoute> arrayList) {
        this.c = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void b(int i) {
        this.b = i;
    }

    public int getDistance() {
        return this.a;
    }

    public int getNumRoutes() {
        return this.c != null ? this.c.size() : 0;
    }

    public MKRoute getRoute(int i) {
        return (this.c == null || i < 0 || i > this.c.size() - 1) ? null : (MKRoute) this.c.get(i);
    }

    public int getTime() {
        return this.b;
    }
}
