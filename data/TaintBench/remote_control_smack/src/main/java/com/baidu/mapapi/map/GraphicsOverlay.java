package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import org.jivesoftware.smackx.GroupChatInvitation;

public class GraphicsOverlay extends Overlay {
    private Bundle a;
    private MapView b;
    private ArrayList<Graphic> c;
    private boolean d;

    public GraphicsOverlay(MapView mapView) {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = false;
        this.mType = 29;
        this.a = new Bundle();
        this.b = mapView;
        this.c = new ArrayList();
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.mLayerID = this.b.a("geometry");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not add geometry layer");
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z) {
        this.d = z;
    }

    /* access modifiers changed from: 0000 */
    public int b() {
        return this.mLayerID;
    }

    /* access modifiers changed from: 0000 */
    public void c() {
        this.b.getController().a.b().c(b());
        for (int i = 0; i < this.c.size(); i++) {
            Graphic graphic = (Graphic) this.c.get(i);
            ArrayList arrayList = graphic.getGeometry().b;
            if (!(arrayList == null || arrayList.size() == 0)) {
                GeoPoint b;
                int[] iArr;
                int[] iArr2;
                if (graphic.getGeometry().a == 2 || graphic.getGeometry().a == 5) {
                    int size = arrayList.size();
                    if (size >= 2) {
                        int[] iArr3 = new int[size];
                        int[] iArr4 = new int[size];
                        for (int i2 = 0; i2 < size; i2++) {
                            if (arrayList.get(i2) != null) {
                                b = e.b((GeoPoint) arrayList.get(i2));
                                iArr3[i2] = b.getLongitudeE6();
                                iArr4[i2] = b.getLatitudeE6();
                            }
                        }
                        this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr3);
                        this.a.putIntArray("y", iArr4);
                    }
                } else if (graphic.getGeometry().a == 3) {
                    if (!(arrayList.size() < 2 || arrayList.get(0) == null || arrayList.get(1) == null)) {
                        iArr = new int[5];
                        iArr2 = new int[5];
                        b = e.b((GeoPoint) arrayList.get(0));
                        iArr[0] = b.getLongitudeE6();
                        iArr2[0] = b.getLatitudeE6();
                        b = e.b(new GeoPoint(((GeoPoint) arrayList.get(0)).getLatitudeE6(), ((GeoPoint) arrayList.get(1)).getLongitudeE6()));
                        iArr[1] = b.getLongitudeE6();
                        iArr2[1] = b.getLatitudeE6();
                        b = e.b((GeoPoint) arrayList.get(1));
                        iArr[2] = b.getLongitudeE6();
                        iArr2[2] = b.getLatitudeE6();
                        b = e.b(new GeoPoint(((GeoPoint) arrayList.get(1)).getLatitudeE6(), ((GeoPoint) arrayList.get(0)).getLongitudeE6()));
                        iArr[3] = b.getLongitudeE6();
                        iArr2[3] = b.getLatitudeE6();
                        iArr[4] = iArr[0];
                        iArr2[4] = iArr2[0];
                        this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr);
                        this.a.putIntArray("y", iArr2);
                    }
                } else if ((graphic.getGeometry().a == 4 || graphic.getGeometry().a == 1) && arrayList.get(0) != null) {
                    iArr = new int[1];
                    iArr2 = new int[1];
                    b = e.b((GeoPoint) arrayList.get(0));
                    iArr[0] = b.getLongitudeE6();
                    iArr2[0] = b.getLatitudeE6();
                    this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr);
                    this.a.putIntArray("y", iArr2);
                }
                this.a.putInt("linewidth", graphic.getSymbol().a);
                this.a.putFloat("red", ((float) graphic.getSymbol().b.red) / 255.0f);
                this.a.putFloat("green", ((float) graphic.getSymbol().b.green) / 255.0f);
                this.a.putFloat("blue", ((float) graphic.getSymbol().b.blue) / 255.0f);
                this.a.putFloat("alpha", ((float) graphic.getSymbol().b.alpha) / 255.0f);
                if (graphic.getGeometry().a == 5) {
                    this.a.putInt("type", 2);
                } else {
                    this.a.putInt("type", graphic.getGeometry().a);
                }
                if (graphic.getGeometry().a == 5) {
                    this.a.putInt("status", 1);
                } else if (graphic.getGeometry().a == 2) {
                    this.a.putInt("status", 0);
                } else {
                    this.a.putInt("status", graphic.getSymbol().c);
                }
                if (graphic.getGeometry().a == 4 || graphic.getGeometry().a == 1) {
                    this.a.putInt("level", graphic.getGeometry().c);
                } else {
                    this.a.putInt("level", (int) this.b.getController().a.l());
                }
                this.a.putInt("geometryaddr", b());
                long currentTimeMillis = System.currentTimeMillis();
                this.a.putString("id", String.valueOf(currentTimeMillis));
                this.b.getController().a.b().f(this.a);
                graphic.a(currentTimeMillis);
            }
        }
        this.d = true;
    }

    /* access modifiers changed from: 0000 */
    public boolean d() {
        return this.d;
    }

    public ArrayList<Graphic> getAllGraphics() {
        return this.c;
    }

    public void removeAll() {
        this.b.getController().a.b().c(b());
        this.c.clear();
        this.d = true;
    }

    public void removeGraphic(long j) {
        Bundle bundle = new Bundle();
        bundle.putInt("geometryaddr", b());
        bundle.putString("id", String.valueOf(j));
        this.b.getController().a.b().g(bundle);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.c.size()) {
                if (j == ((Graphic) this.c.get(i2)).getID()) {
                    this.c.remove(i2);
                }
                i = i2 + 1;
            } else {
                this.d = true;
                return;
            }
        }
    }

    public long setData(Graphic graphic) {
        this.a.clear();
        if (graphic == null || graphic.getGeometry() == null || graphic.getSymbol() == null) {
            return 0;
        }
        Geometry geometry = new Geometry();
        geometry.a = graphic.getGeometry().a;
        geometry.b = graphic.getGeometry().b;
        geometry.c = graphic.getGeometry().c;
        Symbol symbol = new Symbol();
        symbol.b = graphic.getSymbol().b;
        symbol.a = graphic.getSymbol().a;
        symbol.c = graphic.getSymbol().c;
        Graphic graphic2 = new Graphic(geometry, symbol);
        if (b() == 0) {
            this.c.add(graphic2);
            return 0;
        }
        ArrayList arrayList = graphic.getGeometry().b;
        if (arrayList == null || arrayList.size() == 0) {
            return 0;
        }
        int i = graphic.getSymbol().a;
        if (graphic.getSymbol().b == null) {
            return 0;
        }
        float f = ((float) graphic.getSymbol().b.red) / 255.0f;
        float f2 = ((float) graphic.getSymbol().b.green) / 255.0f;
        float f3 = ((float) graphic.getSymbol().b.blue) / 255.0f;
        float f4 = ((float) graphic.getSymbol().b.alpha) / 255.0f;
        int i2 = graphic.getSymbol().c;
        int i3 = graphic.getGeometry().c;
        if (f < 0.0f || f > 255.0f || f2 < 0.0f || f2 > 255.0f || f3 < 0.0f || f3 > 255.0f || f4 < 0.0f || f4 > 255.0f) {
            return 0;
        }
        GeoPoint b;
        int[] iArr;
        int[] iArr2;
        if (graphic.getGeometry().a == 2 || graphic.getGeometry().a == 5) {
            int size = arrayList.size();
            if (size < 2) {
                return 0;
            }
            if (i <= 0 && graphic.getGeometry().a == 2) {
                return 0;
            }
            int[] iArr3 = new int[size];
            int[] iArr4 = new int[size];
            for (int i4 = 0; i4 < size; i4++) {
                if (arrayList.get(i4) == null) {
                    return 0;
                }
                b = e.b((GeoPoint) arrayList.get(i4));
                iArr3[i4] = b.getLongitudeE6();
                iArr4[i4] = b.getLatitudeE6();
            }
            this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr3);
            this.a.putIntArray("y", iArr4);
        } else if (graphic.getGeometry().a == 3) {
            if (arrayList.size() < 2) {
                return 0;
            }
            if (arrayList.get(0) == null || arrayList.get(1) == null) {
                return 0;
            }
            if (i <= 0 || (i2 != 0 && i2 != 1)) {
                return 0;
            }
            iArr = new int[5];
            iArr2 = new int[5];
            b = e.b((GeoPoint) arrayList.get(0));
            iArr[0] = b.getLongitudeE6();
            iArr2[0] = b.getLatitudeE6();
            b = e.b(new GeoPoint(((GeoPoint) arrayList.get(0)).getLatitudeE6(), ((GeoPoint) arrayList.get(1)).getLongitudeE6()));
            iArr[1] = b.getLongitudeE6();
            iArr2[1] = b.getLatitudeE6();
            b = e.b((GeoPoint) arrayList.get(1));
            iArr[2] = b.getLongitudeE6();
            iArr2[2] = b.getLatitudeE6();
            b = e.b(new GeoPoint(((GeoPoint) arrayList.get(1)).getLatitudeE6(), ((GeoPoint) arrayList.get(0)).getLongitudeE6()));
            iArr[3] = b.getLongitudeE6();
            iArr2[3] = b.getLatitudeE6();
            iArr[4] = iArr[0];
            iArr2[4] = iArr2[0];
            this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr);
            this.a.putIntArray("y", iArr2);
        } else if (graphic.getGeometry().a != 4 && graphic.getGeometry().a != 1) {
            return 0;
        } else {
            if (arrayList.get(0) == null) {
                return 0;
            }
            if (i2 != 0 && i2 != 1) {
                return 0;
            }
            if (i3 <= 0) {
                return 0;
            }
            iArr2 = new int[1];
            iArr = new int[1];
            b = e.b((GeoPoint) arrayList.get(0));
            iArr2[0] = b.getLongitudeE6();
            iArr[0] = b.getLatitudeE6();
            this.a.putIntArray(GroupChatInvitation.ELEMENT_NAME, iArr2);
            this.a.putIntArray("y", iArr);
        }
        this.a.putInt("linewidth", i);
        this.a.putFloat("red", f);
        this.a.putFloat("green", f2);
        this.a.putFloat("blue", f3);
        this.a.putFloat("alpha", f4);
        if (graphic.getGeometry().a == 5) {
            this.a.putInt("type", 2);
        } else {
            this.a.putInt("type", graphic.getGeometry().a);
        }
        if (graphic.getGeometry().a == 5) {
            this.a.putInt("status", 1);
        } else if (graphic.getGeometry().a == 2) {
            this.a.putInt("status", 0);
        } else {
            this.a.putInt("status", graphic.getSymbol().c);
        }
        if (graphic.getGeometry().a == 4 || graphic.getGeometry().a == 1) {
            this.a.putInt("level", i3);
        } else {
            this.a.putInt("level", (int) this.b.getController().a.l());
        }
        this.a.putInt("geometryaddr", b());
        long currentTimeMillis = System.currentTimeMillis();
        this.a.putString("id", String.valueOf(currentTimeMillis));
        this.b.getController().a.b().f(this.a);
        graphic.a(currentTimeMillis);
        graphic2.a(currentTimeMillis);
        this.c.add(graphic2);
        this.d = true;
        return currentTimeMillis;
    }
}
