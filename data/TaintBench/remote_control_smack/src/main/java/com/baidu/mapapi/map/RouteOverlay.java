package com.baidu.mapapi.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKStep;
import com.baidu.mapapi.search.c;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.y;
import com.baidu.platform.comjni.tools.ParcelItem;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RouteOverlay extends ItemizedOverlay {
    private ArrayList<a> a;
    private MapView b;
    private Context c;
    private String d;
    private y e;
    private Drawable f;
    private Drawable g;
    public ArrayList<MKRoute> mRoute;

    private class a {
        public String a;
        public GeoPoint b;
        public int c;

        private a() {
        }
    }

    public RouteOverlay(Activity activity, MapView mapView) {
        super(null, mapView);
        this.mRoute = null;
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.mType = 12;
        this.c = activity;
        this.b = mapView;
        this.a = new ArrayList();
        this.mRoute = new ArrayList();
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
        this.e = new y(this.mType);
        this.mLayerID = this.b.a("default");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not create route layer.");
        }
        this.b.a(this.mLayerID, this.e);
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
        return this.d;
    }

    /* access modifiers changed from: 0000 */
    public Bundle f() {
        if (this.f == null && this.g == null) {
            return null;
        }
        ParcelItem parcelItem;
        Bundle bundle;
        Bitmap bitmap;
        ByteBuffer allocate;
        Bundle bundle2 = new Bundle();
        bundle2.clear();
        ArrayList arrayList = new ArrayList();
        if (this.f != null) {
            parcelItem = new ParcelItem();
            bundle = new Bundle();
            bitmap = ((BitmapDrawable) this.f).getBitmap();
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

    public y getInnerOverlay() {
        return this.e;
    }

    public Drawable getStMarker() {
        return this.f;
    }

    /* access modifiers changed from: protected */
    public boolean onTap(int i) {
        OverlayItem item = getItem(i);
        this.b.getController().animateTo(item.mPoint);
        if (item.mTitle != null) {
            Toast.makeText(this.c, item.mTitle, 1).show();
        }
        super.onTap(i);
        return true;
    }

    public void setData(MKRoute mKRoute) {
        int i = 3;
        int i2 = 0;
        if (mKRoute != null && mKRoute.getStart() != null && mKRoute.getEnd() != null) {
            this.mRoute.add(mKRoute);
            if (mKRoute.getRouteType() != 1) {
                i = mKRoute.getRouteType() == 2 ? 2 : mKRoute.getRouteType() == 3 ? 4 : 0;
            }
            GeoPoint start = mKRoute.getStart();
            if (start != null) {
                a aVar = new a();
                aVar.b = start;
                aVar.c = 0;
                if (i == 4) {
                    aVar.a = mKRoute.getStep(0).getContent();
                }
                this.a.add(aVar);
            }
            int numSteps = mKRoute.getNumSteps();
            if (numSteps != 0) {
                while (i2 < numSteps) {
                    MKStep step = mKRoute.getStep(i2);
                    a aVar2 = new a();
                    aVar2.b = step.getPoint();
                    aVar2.a = step.getContent();
                    aVar2.c = i;
                    this.a.add(aVar2);
                    i2++;
                }
            }
            GeoPoint end = mKRoute.getEnd();
            if (end != null) {
                a aVar3 = new a();
                aVar3.b = end;
                aVar3.c = 1;
                this.a.add(aVar3);
            }
            g();
            this.d = c.b(this.mRoute);
        }
    }

    public void setEnMarker(Drawable drawable) {
        this.g = drawable;
    }

    public void setStMarker(Drawable drawable) {
        this.f = drawable;
    }

    public int size() {
        return this.a == null ? 0 : this.a.size();
    }
}
