package com.baidu.mapapi.map;

import android.support.v4.internal.view.SupportMenu;
import com.baidu.platform.comapi.map.g;
import com.baidu.platform.comapi.map.h;
import com.baidu.platform.comapi.map.k;
import com.baidu.platform.comapi.map.l;
import java.util.ArrayList;
import java.util.Iterator;

public class MKOfflineMap {
    public static final int TYPE_DOWNLOAD_UPDATE = 0;
    public static final int TYPE_NEW_OFFLINE = 6;
    public static final int TYPE_VER_UPDATE = 4;
    private h a = null;
    /* access modifiers changed from: private */
    public a b = null;
    private a c = null;

    class a implements l {
        a() {
        }

        public void a(int i, int i2) {
            switch (i) {
                case 4:
                    ArrayList<MKOLUpdateElement> allUpdateInfo = MKOfflineMap.this.getAllUpdateInfo();
                    if (allUpdateInfo != null) {
                        for (MKOLUpdateElement mKOLUpdateElement : allUpdateInfo) {
                            if (mKOLUpdateElement.update) {
                                MKOfflineMap.this.b.a(new MKEvent(4, 0, mKOLUpdateElement.cityID));
                            }
                        }
                        return;
                    }
                    return;
                case 6:
                    MKOfflineMap.this.b.a(new MKEvent(6, 0, i2));
                    return;
                case 8:
                    MKOfflineMap.this.b.a(new MKEvent(0, 0, SupportMenu.USER_MASK & (i2 >> 16)));
                    return;
                default:
                    return;
            }
        }
    }

    public void destroy() {
        this.a.d(0);
        this.a.b(null);
        h.a();
    }

    public ArrayList<MKOLUpdateElement> getAllUpdateInfo() {
        ArrayList d = this.a.d();
        if (d == null) {
            return null;
        }
        ArrayList<MKOLUpdateElement> arrayList = new ArrayList();
        Iterator it = d.iterator();
        while (it.hasNext()) {
            arrayList.add(com.baidu.mapapi.utils.h.a(((k) it.next()).a()));
        }
        return arrayList;
    }

    public ArrayList<MKOLSearchRecord> getHotCityList() {
        ArrayList b = this.a.b();
        if (b == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList();
        Iterator it = b.iterator();
        while (it.hasNext()) {
            arrayList.add(com.baidu.mapapi.utils.h.a((g) it.next()));
        }
        return arrayList;
    }

    public ArrayList<MKOLSearchRecord> getOfflineCityList() {
        ArrayList c = this.a.c();
        if (c == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList();
        Iterator it = c.iterator();
        while (it.hasNext()) {
            arrayList.add(com.baidu.mapapi.utils.h.a((g) it.next()));
        }
        return arrayList;
    }

    public MKOLUpdateElement getUpdateInfo(int i) {
        k f = this.a.f(i);
        return f == null ? null : com.baidu.mapapi.utils.h.a(f.a());
    }

    public boolean init(MapController mapController, MKOfflineMapListener mKOfflineMapListener) {
        if (mapController == null) {
            return false;
        }
        this.a = h.a(mapController.a);
        if (this.a == null) {
            return false;
        }
        this.c = new a();
        this.a.a(this.c);
        this.b = new a(mKOfflineMapListener);
        this.c.a(4, 0);
        return true;
    }

    public boolean pause(int i) {
        return this.a.c(i);
    }

    public boolean remove(int i) {
        return this.a.e(i);
    }

    public int scan() {
        return scan(false);
    }

    public int scan(boolean z) {
        int i;
        int i2 = 0;
        ArrayList d = this.a.d();
        if (d != null) {
            i2 = d.size();
            i = i2;
        } else {
            i = 0;
        }
        this.a.a(z);
        ArrayList d2 = this.a.d();
        if (d2 != null) {
            i2 = d2.size();
        }
        return i2 - i;
    }

    public ArrayList<MKOLSearchRecord> searchCity(String str) {
        ArrayList a = this.a.a(str);
        if (a == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList();
        Iterator it = a.iterator();
        while (it.hasNext()) {
            arrayList.add(com.baidu.mapapi.utils.h.a((g) it.next()));
        }
        return arrayList;
    }

    public boolean start(int i) {
        if (this.a == null) {
            return false;
        }
        if (this.a.d() != null) {
            Iterator it = this.a.d().iterator();
            while (it.hasNext()) {
                k kVar = (k) it.next();
                if (kVar.a.a == i) {
                    return (kVar.a.j || kVar.a.l == 2 || kVar.a.l == 3) ? this.a.b(i) : false;
                }
            }
        }
        return this.a.a(i);
    }
}
