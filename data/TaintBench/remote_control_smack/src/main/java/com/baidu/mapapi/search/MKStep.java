package com.baidu.mapapi.search;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MKStep {
    private GeoPoint a;
    private String b;
    private int c;
    private String d;

    /* access modifiers changed from: 0000 */
    public int a() {
        return this.c;
    }

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.c = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(GeoPoint geoPoint) {
        this.a = geoPoint;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.b = str;
    }

    /* access modifiers changed from: 0000 */
    public String b() {
        return this.d;
    }

    /* access modifiers changed from: 0000 */
    public void b(String str) {
        this.d = str;
    }

    public String getContent() {
        return this.b;
    }

    public GeoPoint getPoint() {
        return this.a;
    }
}
