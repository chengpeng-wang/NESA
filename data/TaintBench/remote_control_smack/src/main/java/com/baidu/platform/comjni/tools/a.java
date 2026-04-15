package com.baidu.platform.comjni.tools;

import android.os.Bundle;
import com.baidu.platform.comapi.basestruct.c;
import java.util.ArrayList;

public class a {
    public static double a(c cVar, c cVar2) {
        Bundle bundle = new Bundle();
        bundle.putDouble("x1", (double) cVar.a);
        bundle.putDouble("y1", (double) cVar.b);
        bundle.putDouble("x2", (double) cVar2.a);
        bundle.putDouble("y2", (double) cVar2.b);
        JNITools.GetDistanceByMC(bundle);
        return bundle.getDouble("distance");
    }

    public static com.baidu.platform.comapi.basestruct.a a(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        Bundle bundle;
        Bundle bundle2 = new Bundle();
        bundle2.putString("strkey", str);
        JNITools.TransGeoStr2ComplexPt(bundle2);
        com.baidu.platform.comapi.basestruct.a aVar = new com.baidu.platform.comapi.basestruct.a();
        Bundle bundle3 = bundle2.getBundle("map_bound");
        if (bundle3 != null) {
            bundle = bundle3.getBundle("ll");
            if (bundle != null) {
                aVar.b = new c((int) bundle.getDouble("ptx"), (int) bundle.getDouble("pty"));
            }
            bundle3 = bundle3.getBundle("ru");
            if (bundle3 != null) {
                aVar.c = new c((int) bundle3.getDouble("ptx"), (int) bundle3.getDouble("pty"));
            }
        }
        ParcelItem[] parcelItemArr = (ParcelItem[]) bundle2.getParcelableArray("poly_line");
        for (ParcelItem bundle4 : parcelItemArr) {
            if (aVar.d == null) {
                aVar.d = new ArrayList();
            }
            bundle = bundle4.getBundle();
            if (bundle != null) {
                ParcelItem[] parcelItemArr2 = (ParcelItem[]) bundle.getParcelableArray("point_array");
                ArrayList arrayList = new ArrayList();
                for (ParcelItem bundle5 : parcelItemArr2) {
                    Bundle bundle6 = bundle5.getBundle();
                    if (bundle6 != null) {
                        arrayList.add(new c((int) bundle6.getDouble("ptx"), (int) bundle6.getDouble("pty")));
                    }
                }
                arrayList.trimToSize();
                aVar.d.add(arrayList);
            }
        }
        aVar.d.trimToSize();
        aVar.a = (int) bundle2.getDouble("type");
        return aVar;
    }
}
