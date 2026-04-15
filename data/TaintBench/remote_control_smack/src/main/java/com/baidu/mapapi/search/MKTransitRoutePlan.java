package com.baidu.mapapi.search;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;

public class MKTransitRoutePlan {
    private int a;
    private int b;
    private String c;
    private ArrayList<MKRoute> d;
    private ArrayList<MKLine> e;
    private GeoPoint f;
    private GeoPoint g;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.a = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(GeoPoint geoPoint) {
        this.f = geoPoint;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.c = str;
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKRoute> arrayList) {
        this.d = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void b(int i) {
        this.b = i;
    }

    /* access modifiers changed from: 0000 */
    public void b(GeoPoint geoPoint) {
        this.g = geoPoint;
    }

    public String getContent() {
        return this.c;
    }

    public int getDistance() {
        return this.a;
    }

    public GeoPoint getEnd() {
        return this.g;
    }

    public MKLine getLine(int i) {
        return (this.e == null || i < 0 || i > this.e.size() - 1) ? null : (MKLine) this.e.get(i);
    }

    public int getNumLines() {
        return this.e != null ? this.e.size() : 0;
    }

    public int getNumRoute() {
        return this.d != null ? this.d.size() : 0;
    }

    public MKRoute getRoute(int i) {
        return this.d != null ? (MKRoute) this.d.get(i) : null;
    }

    public GeoPoint getStart() {
        return this.f;
    }

    public int getTime() {
        return this.b;
    }

    public void setLine(ArrayList<MKLine> arrayList) {
        this.e = arrayList;
    }
}
