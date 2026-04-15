package com.baidu.mapapi.map;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.c;
import com.baidu.platform.comapi.map.d;
import com.baidu.platform.comapi.map.x;
import java.util.ArrayList;

public class PoiOverlay extends Overlay {
    private MapView a;
    private Context b;
    private ArrayList<MKPoiInfo> c;
    private String d;
    private d e;

    public PoiOverlay(Activity activity, MapView mapView) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.mType = 14;
        this.b = activity;
        this.c = new ArrayList();
        this.a = mapView;
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.e = new x(this.mType);
        this.mLayerID = this.a.a("default");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not create poi layer.");
        }
        this.a.a(this.mLayerID, this.e);
    }

    public void animateTo() {
        if (size() > 0) {
            this.a.getController().animateTo(((MKPoiInfo) this.c.get(0)).pt);
        }
    }

    /* access modifiers changed from: 0000 */
    public String b() {
        return this.d;
    }

    public d getInnerOverlay() {
        return this.e;
    }

    public MKPoiInfo getPoi(int i) {
        return (this.c != null && i >= 0 && i < this.c.size()) ? (MKPoiInfo) this.c.get(i) : null;
    }

    /* access modifiers changed from: protected */
    public boolean onTap(int i) {
        this.a.getController().animateTo(((MKPoiInfo) this.c.get(i)).pt);
        Toast.makeText(this.b, ((MKPoiInfo) this.c.get(i)).name, 1).show();
        return false;
    }

    public void setData(ArrayList<MKPoiInfo> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            this.c.clear();
            for (int i = 0; i < arrayList.size(); i++) {
                this.c.add(arrayList.get(i));
            }
            this.d = c.c(this.c);
        }
    }

    public int size() {
        return this.c == null ? 0 : this.c.size() <= 10 ? this.c.size() : 10;
    }
}
