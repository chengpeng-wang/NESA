package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.ExploreByTouchHelper;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comjni.tools.ParcelItem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class ItemizedOverlay<Item extends OverlayItem> extends Overlay implements Comparator<Integer> {
    private ArrayList<OverlayItem> a;
    private ArrayList<Integer> b;
    private Drawable c;
    private boolean d;
    protected MapView mMapView;

    public ItemizedOverlay(Drawable drawable, MapView mapView) {
        this.mType = 27;
        this.c = drawable;
        this.a = new ArrayList();
        this.b = new ArrayList();
        this.mMapView = mapView;
        this.mLayerID = 0;
    }

    private void a(List<OverlayItem> list, boolean z) {
        int i = 0;
        if (this.mLayerID != 0) {
            Bundle bundle = new Bundle();
            bundle.clear();
            ArrayList arrayList = new ArrayList();
            bundle.putInt("itemaddr", this.mLayerID);
            bundle.putInt("bshow", 1);
            if (z) {
                bundle.putString("extparam", Item.UPDATE_ACTION);
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                OverlayItem overlayItem = (OverlayItem) list.get(i2);
                if (overlayItem.getMarker() != null || this.c != null) {
                    if (overlayItem.getMarker() == null) {
                        overlayItem.setMarker(this.c);
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    if (!z) {
                        overlayItem.a("" + currentTimeMillis + "_" + i2);
                    }
                    ParcelItem parcelItem = new ParcelItem();
                    Bitmap bitmap = ((BitmapDrawable) overlayItem.getMarker()).getBitmap();
                    Bundle bundle2 = new Bundle();
                    if (overlayItem.getPoint() != null) {
                        GeoPoint b = e.b(overlayItem.getPoint());
                        bundle2.putInt(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                        bundle2.putInt("y", b.getLatitudeE6());
                        bundle2.putInt("imgW", bitmap.getWidth());
                        bundle2.putInt("imgH", bitmap.getHeight());
                        bundle2.putInt("showLR", 1);
                        bundle2.putInt("iconwidth", 0);
                        bundle2.putInt("iconlayer", 1);
                        bundle2.putFloat("ax", overlayItem.getAnchorX());
                        bundle2.putFloat("ay", overlayItem.getAnchorY());
                        bundle2.putInt("bound", overlayItem.a());
                        bundle2.putString("popname", "" + overlayItem.c());
                        bundle2.putInt("imgindex", overlayItem.b());
                        if (z || !a(overlayItem)) {
                            ByteBuffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
                            bitmap.copyPixelsToBuffer(allocate);
                            bundle2.putByteArray("imgdata", allocate.array());
                        } else {
                            bundle2.putByteArray("imgdata", null);
                        }
                        parcelItem.setBundle(bundle2);
                        arrayList.add(parcelItem);
                        if (!z) {
                            this.a.add(overlayItem);
                        }
                    }
                }
            }
            if (arrayList.size() > 0) {
                ParcelItem[] parcelItemArr = new ParcelItem[arrayList.size()];
                while (i < arrayList.size()) {
                    parcelItemArr[i] = (ParcelItem) arrayList.get(i);
                    i++;
                }
                bundle.putParcelableArray("itemdatas", parcelItemArr);
                this.mMapView.getController().a.b().c(bundle);
            }
            this.d = true;
        } else if (!z) {
            this.a.addAll(list);
        }
    }

    private boolean a(OverlayItem overlayItem) {
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            OverlayItem overlayItem2 = (OverlayItem) it.next();
            if (overlayItem.b() == -1) {
                return false;
            }
            if (overlayItem2.b() != -1 && overlayItem.b() == overlayItem2.b()) {
                return true;
            }
        }
        return false;
    }

    private int b(boolean z) {
        if (this.a == null || this.a.size() == 0) {
            return 0;
        }
        int i = Integer.MAX_VALUE;
        Iterator it = this.a.iterator();
        int i2 = ExploreByTouchHelper.INVALID_ID;
        while (true) {
            int i3 = i;
            if (!it.hasNext()) {
                return i2 - i3;
            }
            GeoPoint point = ((OverlayItem) it.next()).getPoint();
            i = z ? point.getLatitudeE6() : point.getLongitudeE6();
            if (i > i2) {
                i2 = i;
            }
            if (i >= i3) {
                i = i3;
            }
        }
    }

    protected static void boundCenter(OverlayItem overlayItem) {
        if (overlayItem != null) {
            overlayItem.a(2);
            overlayItem.setAnchor(0.5f, 0.5f);
        }
    }

    protected static void boundCenterBottom(OverlayItem overlayItem) {
        if (overlayItem != null) {
            overlayItem.a(1);
            overlayItem.setAnchor(0.5f, 1.0f);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.mLayerID = this.mMapView.a("item");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not add new layer");
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(ArrayList<OverlayItem> arrayList) {
        int size = arrayList.size();
        if (this.b != null) {
            this.b.clear();
            this.b = null;
        }
        if (this.a != null) {
            this.a.clear();
            this.a = null;
        }
        this.a = new ArrayList(size);
        this.b = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            this.b.add(i, Integer.valueOf(i));
            OverlayItem overlayItem = (OverlayItem) arrayList.get(i);
            if (overlayItem.getMarker() == null) {
                overlayItem.setMarker(this.c);
            }
            this.a.add(i, overlayItem);
        }
        Collections.sort(this.b, this);
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z) {
        this.d = z;
    }

    public void addItem(OverlayItem overlayItem) {
        if (this.a != null && overlayItem != null) {
            List arrayList = new ArrayList();
            arrayList.add(overlayItem);
            addItem(arrayList);
        }
    }

    public void addItem(List<OverlayItem> list) {
        if (list != null) {
            a(list, false);
        }
    }

    /* access modifiers changed from: 0000 */
    public void b() {
        List arrayList = new ArrayList();
        arrayList.addAll(this.a);
        removeAll();
        addItem(arrayList);
    }

    /* access modifiers changed from: 0000 */
    public boolean c() {
        return this.d;
    }

    public int compare(Integer num, Integer num2) {
        GeoPoint point = ((OverlayItem) this.a.get(num.intValue())).getPoint();
        GeoPoint point2 = ((OverlayItem) this.a.get(num2.intValue())).getPoint();
        if (point.getLatitudeE6() > point2.getLatitudeE6()) {
            return -1;
        }
        if (point.getLatitudeE6() < point2.getLatitudeE6()) {
            return 1;
        }
        if (point.getLongitudeE6() < point2.getLongitudeE6()) {
            return -1;
        }
        return point.getLongitudeE6() == point2.getLongitudeE6() ? 0 : 1;
    }

    /* access modifiers changed from: protected */
    public Item createItem(int i) {
        return null;
    }

    /* access modifiers changed from: 0000 */
    public int d() {
        return this.mLayerID;
    }

    public ArrayList<OverlayItem> getAllItem() {
        return this.a;
    }

    public GeoPoint getCenter() {
        int indexToDraw = getIndexToDraw(0);
        return indexToDraw == -1 ? null : getItem(indexToDraw).getPoint();
    }

    /* access modifiers changed from: protected */
    public int getIndexToDraw(int i) {
        return (this.a == null || this.a.size() == 0) ? -1 : i;
    }

    public final OverlayItem getItem(int i) {
        return (this.a == null || this.a.size() <= i || i < 0) ? null : (OverlayItem) this.a.get(i);
    }

    public int getLatSpanE6() {
        return b(true);
    }

    public int getLonSpanE6() {
        return b(false);
    }

    /* access modifiers changed from: protected */
    public boolean hitTest(OverlayItem overlayItem, Drawable drawable, int i, int i2) {
        if (drawable == null) {
            return false;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Point toPixels = this.mMapView.getProjection().toPixels(overlayItem.getPoint(), null);
        Rect bounds = drawable.getBounds();
        bounds.left = toPixels.x - ((int) (overlayItem.getAnchorX() * ((float) width)));
        bounds.right = ((int) (((float) width) * (1.0f - overlayItem.getAnchorX()))) + toPixels.x;
        bounds.bottom = toPixels.y + ((int) ((1.0f - overlayItem.getAnchorY()) * ((float) height)));
        bounds.top = toPixels.y - ((int) (((float) height) * overlayItem.getAnchorY()));
        return bounds.contains(i, i2);
    }

    /* access modifiers changed from: protected */
    public boolean onTap(int i) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        return false;
    }

    public boolean removeAll() {
        this.mMapView.getController().a.b().c(this.mLayerID);
        this.a.clear();
        this.d = true;
        return true;
    }

    public boolean removeItem(OverlayItem overlayItem) {
        if (this.mLayerID == 0) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("itemaddr", this.mLayerID);
        if (overlayItem.c().equals("")) {
            return false;
        }
        bundle.putString("id", overlayItem.c());
        if (!this.mMapView.getController().a.b().d(bundle)) {
            return false;
        }
        this.a.remove(overlayItem);
        this.d = true;
        return true;
    }

    public int size() {
        return this.a == null ? 0 : this.a.size();
    }

    public boolean updateItem(OverlayItem overlayItem) {
        if (overlayItem == null) {
            return false;
        }
        if (overlayItem.c().equals("")) {
            return false;
        }
        boolean z;
        Iterator it = this.a.iterator();
        while (it.hasNext()) {
            if (overlayItem.c().equals(((OverlayItem) it.next()).c())) {
                z = true;
                break;
            }
        }
        z = false;
        if (!z) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(overlayItem);
        a(arrayList, true);
        return true;
    }
}
