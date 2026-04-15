package com.baidu.mapapi.utils;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.g;
import com.baidu.platform.comapi.map.j;
import java.util.ArrayList;
import java.util.Iterator;

public class h {
    public static MKOLSearchRecord a(g gVar) {
        if (gVar == null) {
            return null;
        }
        int i;
        MKOLSearchRecord mKOLSearchRecord = new MKOLSearchRecord();
        mKOLSearchRecord.cityID = gVar.a;
        mKOLSearchRecord.cityName = gVar.b;
        mKOLSearchRecord.cityType = gVar.d;
        int i2 = 0;
        if (gVar.a() != null) {
            ArrayList arrayList = new ArrayList();
            Iterator it = gVar.a().iterator();
            while (true) {
                i = i2;
                if (!it.hasNext()) {
                    break;
                }
                g gVar2 = (g) it.next();
                arrayList.add(a(gVar2));
                i2 = gVar2.c + i;
                mKOLSearchRecord.childCities = arrayList;
            }
        } else {
            i = 0;
        }
        if (mKOLSearchRecord.cityType == 1) {
            mKOLSearchRecord.size = i;
        } else {
            mKOLSearchRecord.size = gVar.c;
        }
        return mKOLSearchRecord;
    }

    public static MKOLUpdateElement a(j jVar) {
        if (jVar == null) {
            return null;
        }
        MKOLUpdateElement mKOLUpdateElement = new MKOLUpdateElement();
        mKOLUpdateElement.cityID = jVar.a;
        mKOLUpdateElement.cityName = jVar.b;
        if (jVar.g != null) {
            mKOLUpdateElement.geoPt = e.a(new GeoPoint(jVar.g.b(), jVar.g.a()));
        }
        mKOLUpdateElement.level = jVar.e;
        mKOLUpdateElement.ratio = jVar.i;
        mKOLUpdateElement.serversize = jVar.h;
        if (jVar.i == 100) {
            mKOLUpdateElement.size = jVar.h;
        } else {
            mKOLUpdateElement.size = (jVar.h * jVar.i) / 100;
        }
        mKOLUpdateElement.status = jVar.l;
        mKOLUpdateElement.update = jVar.j;
        return mKOLUpdateElement;
    }
}
