package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;

public class JNIBaseMap {
    public static native int MapProc(int i, int i2, int i3, int i4);

    public native int AddGeometryData(int i, Bundle bundle);

    public native void AddItemData(int i, Bundle bundle);

    public native int AddLayer(int i, int i2, int i3, String str);

    public native void AddLogoData(int i, Bundle bundle);

    public native void AddPopupData(int i, Bundle bundle);

    public native int AddTextData(int i, Bundle bundle);

    public native void ClearLayer(int i, int i2);

    public native int Create();

    public native String GeoPtToScrPoint(int i, int i2, int i3);

    public native Bundle GetMapStatus(int i);

    public native String GetNearlyObjID(int i, int i2, int i3, int i4, int i5);

    public native boolean Init(int i, String str, String str2, String str3, String str4, String str5, String str6, int i2, int i3, int i4, int i5, int i6, int i7);

    public native void MoveToScrPoint(int i, int i2, int i3);

    public native String OnHotcityGet(int i);

    public native void OnPause(int i);

    public native boolean OnRecordAdd(int i, int i2);

    public native String OnRecordGetAll(int i);

    public native String OnRecordGetAt(int i, int i2);

    public native boolean OnRecordImport(int i, boolean z);

    public native boolean OnRecordRemove(int i, int i2, boolean z);

    public native boolean OnRecordStart(int i, int i2, boolean z, int i3);

    public native boolean OnRecordSuspend(int i, int i2, boolean z, int i3);

    public native void OnResume(int i);

    public native String OnSchcityGet(int i, String str);

    public native void PostStatInfo(int i);

    public native int Release(int i);

    public native boolean RemoveGeometryData(int i, Bundle bundle);

    public native boolean RemoveItemData(int i, Bundle bundle);

    public native int RemoveLayer(int i, int i2);

    public native boolean RemoveTextData(int i, Bundle bundle);

    public native void ResetImageRes(int i);

    public native void SaveScreenToLocal(int i, String str);

    public native String ScrPtToGeoPoint(int i, int i2, int i3);

    public native int SetCallback(int i, BaseMapCallback baseMapCallback);

    public native void SetLayersClickable(int i, int i2, boolean z);

    public native void SetMapStatus(int i, Bundle bundle);

    public native void ShowLayers(int i, int i2, boolean z);

    public native void ShowSatelliteMap(int i, boolean z);

    public native void ShowTrafficMap(int i, boolean z);

    public native void UpdateLayers(int i, int i2);
}
