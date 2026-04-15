package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;

public class a {
    private int a;
    private JNIBaseMap b;
    private BaseMapCallback c;

    public a() {
        this.a = 0;
        this.b = null;
        this.c = null;
        this.b = new JNIBaseMap();
        this.c = new BaseMapCallback();
    }

    public static int b(int i, int i2, int i3, int i4) {
        return JNIBaseMap.MapProc(i, i2, i3, i4);
    }

    public int a(int i, int i2, String str) {
        return this.b.AddLayer(this.a, i, i2, str);
    }

    public String a(int i, int i2) {
        return this.b.ScrPtToGeoPoint(this.a, i, i2);
    }

    public String a(int i, int i2, int i3, int i4) {
        return this.b.GetNearlyObjID(this.a, i, i2, i3, i4);
    }

    public void a(int i) {
        this.b.UpdateLayers(this.a, i);
    }

    public void a(int i, boolean z) {
        this.b.ShowLayers(this.a, i, z);
    }

    public void a(Bundle bundle) {
        this.b.SetMapStatus(this.a, bundle);
    }

    public void a(String str) {
        this.b.SaveScreenToLocal(this.a, str);
    }

    public void a(boolean z) {
        this.b.ShowSatelliteMap(this.a, z);
    }

    public boolean a() {
        this.a = this.b.Create();
        this.b.SetCallback(this.a, this.c);
        return true;
    }

    public boolean a(int i, boolean z, int i2) {
        return this.b.OnRecordStart(this.a, i, z, i2);
    }

    public boolean a(BaseMapCallback baseMapCallback) {
        return baseMapCallback == null ? false : this.c.SetMapCallback(baseMapCallback);
    }

    public boolean a(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, int i3, int i4, int i5, int i6) {
        return this.b.Init(this.a, str, str2, str3, str4, str5, str6, i, i2, i3, i4, i5, i6);
    }

    public int b(int i) {
        return this.b.RemoveLayer(this.a, i);
    }

    public String b(int i, int i2) {
        return this.b.GeoPtToScrPoint(this.a, i, i2);
    }

    public String b(String str) {
        return this.b.OnSchcityGet(this.a, str);
    }

    public void b(int i, boolean z) {
        this.b.SetLayersClickable(this.a, i, z);
    }

    public void b(Bundle bundle) {
        this.b.AddPopupData(this.a, bundle);
    }

    public void b(boolean z) {
        this.b.ShowTrafficMap(this.a, z);
    }

    public boolean b() {
        this.b.Release(this.a);
        return true;
    }

    public boolean b(int i, boolean z, int i2) {
        return this.b.OnRecordSuspend(this.a, i, z, i2);
    }

    public int c() {
        return this.a;
    }

    public void c(int i) {
        this.b.ClearLayer(this.a, i);
    }

    public void c(int i, int i2) {
        this.b.MoveToScrPoint(this.a, i, i2);
    }

    public void c(Bundle bundle) {
        this.b.AddItemData(this.a, bundle);
    }

    public boolean c(int i, boolean z) {
        return this.b.OnRecordRemove(this.a, i, z);
    }

    public boolean c(boolean z) {
        return this.b.OnRecordImport(this.a, z);
    }

    public void d() {
        this.b.OnPause(this.a);
    }

    public boolean d(int i) {
        return this.b.OnRecordAdd(this.a, i);
    }

    public boolean d(Bundle bundle) {
        return this.b.RemoveItemData(this.a, bundle);
    }

    public String e(int i) {
        return this.b.OnRecordGetAt(this.a, i);
    }

    public void e() {
        this.b.OnResume(this.a);
    }

    public void e(Bundle bundle) {
        this.b.AddLogoData(this.a, bundle);
    }

    public int f(Bundle bundle) {
        return this.b.AddGeometryData(this.a, bundle);
    }

    public void f() {
        this.b.ResetImageRes(this.a);
    }

    public Bundle g() {
        return this.b.GetMapStatus(this.a);
    }

    public boolean g(Bundle bundle) {
        return this.b.RemoveGeometryData(this.a, bundle);
    }

    public int h(Bundle bundle) {
        return this.b.AddTextData(this.a, bundle);
    }

    public String h() {
        return this.b.OnRecordGetAll(this.a);
    }

    public String i() {
        return this.b.OnHotcityGet(this.a);
    }

    public boolean i(Bundle bundle) {
        return this.b.RemoveTextData(this.a, bundle);
    }

    public void j() {
        this.b.PostStatInfo(this.a);
    }
}
