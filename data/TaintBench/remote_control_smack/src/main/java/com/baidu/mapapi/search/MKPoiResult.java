package com.baidu.mapapi.search;

import java.util.ArrayList;

public class MKPoiResult {
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int d = 0;
    private ArrayList<MKPoiResult> e;
    private ArrayList<MKPoiInfo> f;
    private ArrayList<MKCityListInfo> g;

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.b = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<MKPoiInfo> arrayList) {
        this.f = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void b(int i) {
        this.a = i;
    }

    /* access modifiers changed from: 0000 */
    public void b(ArrayList<MKPoiResult> arrayList) {
        this.e = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void c(int i) {
        this.c = i;
    }

    /* access modifiers changed from: 0000 */
    public void c(ArrayList<MKCityListInfo> arrayList) {
        this.g = arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void d(int i) {
        this.d = i;
    }

    public ArrayList<MKPoiInfo> getAllPoi() {
        return this.f;
    }

    public MKCityListInfo getCityListInfo(int i) {
        return (this.g == null || i < 0 || i > this.g.size() - 1) ? null : (MKCityListInfo) this.g.get(i);
    }

    public int getCityListNum() {
        return this.g != null ? this.g.size() : 0;
    }

    public int getCurrentNumPois() {
        return this.b;
    }

    public ArrayList<MKPoiResult> getMultiPoiResult() {
        return this.e;
    }

    public int getNumPages() {
        return this.c;
    }

    public int getNumPois() {
        return this.a;
    }

    public int getPageIndex() {
        return this.d;
    }

    public MKPoiInfo getPoi(int i) {
        return (this.f == null || i < 0 || i > this.f.size() - 1) ? null : (MKPoiInfo) this.f.get(i);
    }
}
