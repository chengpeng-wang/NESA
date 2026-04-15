package com.tencent.bugly.proguard;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: BUGLY */
public class ad {
    public static void a(Parcel parcel, Map<String, PlugInBean> map) {
        if (map == null || map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList arrayList = new ArrayList(size);
        ArrayList arrayList2 = new ArrayList(size);
        for (Entry entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("pluginKeys", arrayList);
        bundle.putParcelableArrayList("pluginValues", arrayList2);
        parcel.writeBundle(bundle);
    }

    public static Map<String, PlugInBean> a(Parcel parcel) {
        int i = 0;
        Bundle readBundle = parcel.readBundle();
        if (readBundle == null) {
            return null;
        }
        Map<String, PlugInBean> map;
        ArrayList stringArrayList = readBundle.getStringArrayList("pluginKeys");
        ArrayList parcelableArrayList = readBundle.getParcelableArrayList("pluginValues");
        if (stringArrayList == null || parcelableArrayList == null || stringArrayList.size() != parcelableArrayList.size()) {
            z.e("map plugin parcel error!", new Object[0]);
            map = null;
        } else {
            HashMap hashMap = new HashMap(stringArrayList.size());
            while (i < stringArrayList.size()) {
                hashMap.put(stringArrayList.get(i), PlugInBean.class.cast(parcelableArrayList.get(i)));
                i++;
            }
            map = hashMap;
        }
        return map;
    }

    public static void b(Parcel parcel, Map<String, String> map) {
        if (map == null || map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList arrayList = new ArrayList(size);
        ArrayList arrayList2 = new ArrayList(size);
        for (Entry entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("keys", arrayList);
        bundle.putStringArrayList("values", arrayList2);
        parcel.writeBundle(bundle);
    }

    public static Map<String, String> b(Parcel parcel) {
        int i = 0;
        Bundle readBundle = parcel.readBundle();
        if (readBundle == null) {
            return null;
        }
        Map<String, String> map;
        ArrayList stringArrayList = readBundle.getStringArrayList("keys");
        ArrayList stringArrayList2 = readBundle.getStringArrayList("values");
        if (stringArrayList == null || stringArrayList2 == null || stringArrayList.size() != stringArrayList2.size()) {
            z.e("map parcel error!", new Object[0]);
            map = null;
        } else {
            HashMap hashMap = new HashMap(stringArrayList.size());
            while (i < stringArrayList.size()) {
                hashMap.put(stringArrayList.get(i), stringArrayList2.get(i));
                i++;
            }
            map = hashMap;
        }
        return map;
    }

    public static byte[] a(Parcelable parcelable) {
        Parcel obtain = Parcel.obtain();
        parcelable.writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        return marshall;
    }

    public static Parcel a(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        return obtain;
    }

    public static <T> T a(byte[] bArr, Creator<T> creator) {
        T createFromParcel;
        Parcel a = a(bArr);
        try {
            createFromParcel = creator.createFromParcel(a);
            if (a != null) {
                a.recycle();
            }
        } catch (Throwable th) {
            if (a != null) {
                a.recycle();
            }
        }
        return createFromParcel;
    }
}
