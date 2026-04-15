package com.baidu.platform.comapi.map;

import android.os.Bundle;
import android.util.SparseArray;
import com.baidu.platform.comjni.map.basemap.BaseMapCallback;

public class w extends BaseMapCallback {
    SparseArray<d> a = new SparseArray();
    o b = null;

    public w(o oVar) {
        this.b = oVar;
    }

    public int ReqLayerData(Bundle bundle, int i, int i2) {
        d dVar = (d) this.a.get(i);
        if (dVar == null) {
            return 0;
        }
        bundle.putString("jsondata", dVar.b());
        Bundle c = dVar.c();
        if (c != null) {
            bundle.putBundle("param", c);
        }
        return dVar.e();
    }

    public void a(int i) {
        if (this.b != null) {
            this.b.b().c(i);
        }
        this.a.remove(i);
    }

    public void a(int i, d dVar) {
        this.a.put(i, dVar);
        dVar.a(i, this.b);
    }
}
