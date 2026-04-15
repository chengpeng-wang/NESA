package com.baidu.mapapi.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import com.baidu.mapapi.search.MKLine;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKTransitRoutePlan;
import com.baidu.mapapi.search.c;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.A;
import com.baidu.platform.comjni.tools.ParcelItem;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TransitOverlay extends ItemizedOverlay {
    private ArrayList<a> a;
    private MapView b;
    private Context c;
    private int d;
    private String e;
    private A f;
    private Drawable g;
    private Drawable h;
    public ArrayList<MKTransitRoutePlan> mPlan;

    private class a {
        public String a;
        public GeoPoint b;
        public int c;

        private a() {
        }
    }

    public TransitOverlay(Activity activity, MapView mapView) {
        super(null, mapView);
        this.mPlan = null;
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = 1;
        this.e = null;
        this.f = null;
        this.mType = 28;
        this.c = activity;
        this.b = mapView;
        this.a = new ArrayList();
        this.mPlan = new ArrayList();
    }

    private void g() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size(); i++) {
            arrayList.add(createItem(i));
        }
        super.a(arrayList);
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.f = new A(12);
        this.mLayerID = this.b.a("default");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not create transit layer.");
        }
        this.b.a(this.mLayerID, this.f);
    }

    public void animateTo() {
        if (size() > 0) {
            OverlayItem item = getItem(0);
            if (item != null) {
                this.b.getController().animateTo(item.mPoint);
            }
        }
    }

    /* access modifiers changed from: protected */
    public OverlayItem createItem(int i) {
        a aVar = (a) this.a.get(i);
        return new OverlayItem(aVar.b, aVar.a, null);
    }

    /* access modifiers changed from: 0000 */
    public String e() {
        return this.e;
    }

    /* access modifiers changed from: 0000 */
    public Bundle f() {
        if (this.h == null && this.g == null) {
            return null;
        }
        ParcelItem parcelItem;
        Bundle bundle;
        Bitmap bitmap;
        ByteBuffer allocate;
        Bundle bundle2 = new Bundle();
        bundle2.clear();
        ArrayList arrayList = new ArrayList();
        if (this.h != null) {
            parcelItem = new ParcelItem();
            bundle = new Bundle();
            bitmap = ((BitmapDrawable) this.h).getBitmap();
            allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle.putByteArray("imgdata", allocate.array());
            bundle.putInt("type", 1);
            bundle.putInt("imgH", bitmap.getHeight());
            bundle.putInt("imgW", bitmap.getWidth());
            parcelItem.setBundle(bundle);
            arrayList.add(parcelItem);
        }
        if (this.g != null) {
            parcelItem = new ParcelItem();
            bundle = new Bundle();
            bitmap = ((BitmapDrawable) this.g).getBitmap();
            allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle.putByteArray("imgdata", allocate.array());
            bundle.putInt("type", 2);
            bundle.putInt("imgH", bitmap.getHeight());
            bundle.putInt("imgW", bitmap.getWidth());
            parcelItem.setBundle(bundle);
            arrayList.add(parcelItem);
        }
        if (arrayList.size() > 0) {
            ParcelItem[] parcelItemArr = new ParcelItem[arrayList.size()];
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= arrayList.size()) {
                    break;
                }
                parcelItemArr[i2] = (ParcelItem) arrayList.get(i2);
                i = i2 + 1;
            }
            bundle2.putParcelableArray("icondata", parcelItemArr);
        }
        return bundle2;
    }

    public Drawable getEnMarker() {
        return this.g;
    }

    public A getInnerOverlay() {
        return this.f;
    }

    public Drawable getStMarker() {
        return this.h;
    }

    /* access modifiers changed from: protected */
    public boolean onTap(int i) {
        OverlayItem item = getItem(i);
        this.b.getController().animateTo(item.mPoint);
        if (!(item == null || item.mTitle == null)) {
            Toast.makeText(this.c, item.mTitle, 1).show();
        }
        super.onTap(i);
        return true;
    }

    public void setData(MKTransitRoutePlan mKTransitRoutePlan) {
        if (mKTransitRoutePlan != null) {
            int numLines = mKTransitRoutePlan.getNumLines();
            int numRoute = mKTransitRoutePlan.getNumRoute();
            if (numLines != 0 || numRoute != 0) {
                this.mPlan.add(mKTransitRoutePlan);
                GeoPoint start = mKTransitRoutePlan.getStart();
                if (start != null) {
                    a aVar = new a();
                    aVar.b = start;
                    aVar.c = 0;
                    this.a.add(aVar);
                }
                for (int i = 0; i < numLines; i++) {
                    MKLine line = mKTransitRoutePlan.getLine(i);
                    a aVar2 = new a();
                    MKPoiInfo getOnStop = line.getGetOnStop();
                    aVar2.b = getOnStop.pt;
                    aVar2.a = "在" + getOnStop.name + "上车，" + "乘坐" + line.getTitle() + "经过" + String.valueOf(line.getNumViaStops()) + "站";
                    if (i == 0 && this.a.size() > 0) {
                        ((a) this.a.get(this.a.size() - 1)).a = aVar2.a;
                    }
                    if (line.getType() == 0) {
                        aVar2.c = 2;
                    } else {
                        aVar2.c = 4;
                    }
                    this.a.add(aVar2);
                    MKLine line2 = mKTransitRoutePlan.getLine(i);
                    a aVar3 = new a();
                    getOnStop = line.getGetOffStop();
                    aVar3.b = getOnStop.pt;
                    aVar3.a = "在" + getOnStop.name + "下车";
                    for (int i2 = 0; i2 < numRoute; i2++) {
                        MKRoute route = mKTransitRoutePlan.getRoute(i2);
                        if (route.getIndex() == i) {
                            aVar3.a += "," + route.getTip();
                            break;
                        }
                    }
                    if (line2.getType() == 0) {
                        aVar3.c = 2;
                    } else {
                        aVar3.c = 4;
                    }
                    this.a.add(aVar3);
                }
                start = mKTransitRoutePlan.getEnd();
                if (start != null) {
                    a aVar4 = new a();
                    aVar4.b = start;
                    aVar4.c = 1;
                    this.a.add(aVar4);
                }
                g();
                this.e = c.a(this.mPlan);
            }
        }
    }

    public void setEnMarker(Drawable drawable) {
        this.g = drawable;
    }

    public void setStMarker(Drawable drawable) {
        this.h = drawable;
    }

    public int size() {
        return this.a == null ? 0 : this.a.size();
    }
}
