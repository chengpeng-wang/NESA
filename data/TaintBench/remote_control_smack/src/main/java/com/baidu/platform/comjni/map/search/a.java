package com.baidu.platform.comjni.map.search;

import android.os.Bundle;

public class a {
    private int a;
    private JNISearch b;

    public a() {
        this.a = 0;
        this.b = null;
        this.b = new JNISearch();
    }

    public int a() {
        this.a = this.b.Create();
        return this.a;
    }

    public String a(int i) {
        return this.b.GetSearchResult(this.a, i);
    }

    public boolean a(int i, int i2) {
        return this.b.ReverseGeocodeSearch(this.a, i, i2);
    }

    public boolean a(int i, int i2, String str, String str2) {
        return this.b.PoiRGCShareUrlSearch(this.a, i, i2, str, str2);
    }

    public boolean a(Bundle bundle) {
        return this.b.ForceSearchByCityName(this.a, bundle);
    }

    public boolean a(String str) {
        return this.b.POIDetailSearchPlace(this.a, str);
    }

    public boolean a(String str, String str2) {
        return this.b.BusLineDetailSearch(this.a, str, str2);
    }

    public int b() {
        return this.b.QueryInterface(this.a);
    }

    public boolean b(Bundle bundle) {
        return this.b.AreaSearch(this.a, bundle);
    }

    public boolean b(String str) {
        return this.b.PoiDetailShareUrlSearch(this.a, str);
    }

    public boolean b(String str, String str2) {
        return this.b.geocode(this.a, str, str2);
    }

    public int c() {
        return this.b.Release(this.a);
    }

    public boolean c(Bundle bundle) {
        return this.b.AreaMultiSearch(this.a, bundle);
    }

    public boolean d(Bundle bundle) {
        return this.b.RoutePlanByBus(this.a, bundle);
    }

    public boolean e(Bundle bundle) {
        return this.b.RoutePlanByCar(this.a, bundle);
    }

    public boolean f(Bundle bundle) {
        return this.b.RoutePlanByFoot(this.a, bundle);
    }

    public boolean g(Bundle bundle) {
        return this.b.SuggestionSearch(this.a, bundle);
    }

    public boolean h(Bundle bundle) {
        return this.b.MapBoundSearch(this.a, bundle);
    }

    public boolean i(Bundle bundle) {
        return this.b.GeoSearch(this.a, bundle);
    }

    public boolean j(Bundle bundle) {
        return this.b.GeoDetailSearch(this.a, bundle);
    }
}
