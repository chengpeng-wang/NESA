package com.baidu.mapapi.search;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;

public class MKLine {
    public static final int LINE_TYPE_BUS = 0;
    public static final int LINE_TYPE_SUBWAY = 1;
    ArrayList<GeoPoint> a;
    private int b;
    private int c;
    private int d;
    private int e;
    private String f;
    private String g;
    private String h;
    private MKPoiInfo i;
    private MKPoiInfo j;
    private ArrayList<GeoPoint> k;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.b = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(MKPoiInfo mKPoiInfo) {
        this.i = mKPoiInfo;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] toCharArray = str.toCharArray();
        Object obj = null;
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '<') {
                obj = 1;
            } else if (toCharArray[i] == '>') {
                obj = null;
            } else if (obj == null) {
                stringBuilder.append(toCharArray[i]);
            }
        }
        this.h = stringBuilder.toString();
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<GeoPoint> arrayList) {
        this.k = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void b(int i) {
        this.c = i;
    }

    /* access modifiers changed from: 0000 */
    public void b(MKPoiInfo mKPoiInfo) {
        this.j = mKPoiInfo;
    }

    /* access modifiers changed from: 0000 */
    public void b(String str) {
        this.f = str;
    }

    /* access modifiers changed from: 0000 */
    public void c(int i) {
        this.d = i;
    }

    /* access modifiers changed from: 0000 */
    public void d(int i) {
        this.e = i;
    }

    public int getDistance() {
        return this.c;
    }

    public MKPoiInfo getGetOffStop() {
        return this.j;
    }

    public MKPoiInfo getGetOnStop() {
        return this.i;
    }

    public int getNumViaStops() {
        return this.b;
    }

    public ArrayList<GeoPoint> getPoints() {
        return this.k;
    }

    public int getTime() {
        return this.d;
    }

    public String getTip() {
        return this.h;
    }

    public String getTitle() {
        return this.f;
    }

    public int getType() {
        return this.e;
    }

    public String getUid() {
        return this.g;
    }
}
