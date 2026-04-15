package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.baidu.platform.comapi.map.d;
import com.baidu.platform.comapi.map.n;
import com.baidu.platform.comjni.tools.ParcelItem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MyLocationOverlay extends Overlay {
    boolean a = true;
    n b = null;
    private LocationData c = null;
    private MapView d = null;
    private boolean e = false;
    private String f = null;
    private Drawable g;
    private List<Drawable> h;

    public MyLocationOverlay(MapView mapView) {
        this.d = mapView;
        this.mType = 7;
        this.h = new ArrayList();
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.b = new n(this.mType);
        this.mLayerID = this.d.a("location");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not create poi layer.");
        }
        this.d.a(this.mLayerID, this.b);
    }

    /* access modifiers changed from: 0000 */
    public d b() {
        return this.b;
    }

    /* access modifiers changed from: 0000 */
    public Bundle c() {
        if (this.g == null) {
            return null;
        }
        this.h.clear();
        this.h.add(this.g);
        Bundle bundle = new Bundle();
        bundle.clear();
        ArrayList arrayList = new ArrayList();
        for (Drawable drawable : this.h) {
            ParcelItem parcelItem = new ParcelItem();
            Bundle bundle2 = new Bundle();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteBuffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle2.putByteArray("imgdata", allocate.array());
            bundle2.putInt("imgindex", drawable.hashCode());
            bundle2.putInt("imgH", bitmap.getHeight());
            bundle2.putInt("imgW", bitmap.getWidth());
            parcelItem.setBundle(bundle2);
            arrayList.add(parcelItem);
        }
        if (arrayList.size() > 0) {
            ParcelItem[] parcelItemArr = new ParcelItem[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                parcelItemArr[i] = (ParcelItem) arrayList.get(i);
            }
            bundle.putParcelableArray("icondata", parcelItemArr);
        }
        this.a = false;
        return bundle;
    }

    /* access modifiers changed from: 0000 */
    public String d() {
        return this.f;
    }

    public void disableCompass() {
        this.e = false;
        setData(this.c);
    }

    /* access modifiers changed from: protected */
    public boolean dispatchTap() {
        return false;
    }

    public boolean enableCompass() {
        this.e = true;
        setData(this.c);
        return true;
    }

    public LocationData getMyLocation() {
        return this.c;
    }

    public boolean isCompassEnable() {
        return this.e;
    }

    public void setData(LocationData locationData) {
        if (locationData != null) {
            if (!this.e) {
                locationData.direction = -1.0f;
            }
            this.c = locationData;
            this.f = locationData.a();
        }
    }

    public void setMarker(Drawable drawable) {
        this.g = drawable;
    }
}
