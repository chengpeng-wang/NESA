package com.baidu.mapapi.map;

import android.graphics.drawable.Drawable;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OverlayItem {
    public static final int ALIGN_BOTTON = 2;
    public static final int ALIGN_TOP = 3;
    public static final int ALING_CENTER = 1;
    private int a = 2;
    private Drawable b = null;
    private String c = "";
    private float d = 0.5f;
    private float e = 1.0f;
    protected GeoPoint mPoint;
    protected String mSnippet;
    protected String mTitle;

    public OverlayItem(GeoPoint geoPoint, String str, String str2) {
        this.mPoint = geoPoint;
        this.mTitle = str;
        this.mSnippet = str2;
    }

    /* access modifiers changed from: 0000 */
    public int a() {
        return this.a;
    }

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.a = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.c = str;
    }

    /* access modifiers changed from: 0000 */
    public int b() {
        return getMarker() == null ? -1 : getMarker().hashCode();
    }

    /* access modifiers changed from: 0000 */
    public String c() {
        return this.c;
    }

    public float getAnchorX() {
        return this.d;
    }

    public float getAnchorY() {
        return this.e;
    }

    public final Drawable getMarker() {
        return this.b;
    }

    public GeoPoint getPoint() {
        return this.mPoint;
    }

    public String getSnippet() {
        return this.mSnippet;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setAnchor(float f, float f2) {
        if (((double) f) <= 1.0d && ((double) f) >= 0.0d && ((double) f2) <= 1.0d && ((double) f2) >= 0.0d) {
            this.d = f;
            this.e = f2;
        }
    }

    public void setAnchor(int i) {
        switch (i) {
            case 1:
                setAnchor(0.5f, 0.5f);
                return;
            case 2:
                setAnchor(0.5f, 1.0f);
                return;
            case 3:
                setAnchor(0.5f, 0.0f);
                return;
            default:
                return;
        }
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.mPoint = geoPoint;
    }

    public void setMarker(Drawable drawable) {
        this.b = drawable;
    }

    public void setSnippet(String str) {
        this.mSnippet = str;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }
}
