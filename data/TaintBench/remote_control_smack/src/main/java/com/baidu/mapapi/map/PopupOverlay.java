package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.nio.ByteBuffer;
import org.jivesoftware.smackx.GroupChatInvitation;

public class PopupOverlay extends Overlay {
    private static int d = 0;
    PopupClickListener a = null;
    private MapView b = null;
    private MapController c = null;

    public PopupOverlay(MapView mapView, PopupClickListener popupClickListener) {
        this.b = mapView;
        this.c = this.b.getController();
        this.mType = 21;
        this.mLayerID = mapView.getController().a.a;
        this.a = popupClickListener;
    }

    private Bitmap a(Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3) {
        int i = 0;
        if (bitmap == null) {
            return null;
        }
        if (bitmap2 == null && bitmap3 == null) {
            return bitmap;
        }
        int width;
        int height;
        int width2;
        int width3 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        if (bitmap2 != null) {
            width = bitmap2.getWidth();
            height = bitmap2.getHeight();
        } else {
            height = 0;
            width = 0;
        }
        if (bitmap3 != null) {
            width2 = bitmap3.getWidth();
            i = bitmap3.getHeight();
        } else {
            width2 = 0;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width2 + (width3 + width), Math.max(Math.max(height2, height), i), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        if (bitmap2 != null) {
            canvas.drawBitmap(bitmap2, (float) width3, 0.0f, null);
        }
        if (bitmap3 != null) {
            canvas.drawBitmap(bitmap3, (float) (width3 + width), 0.0f, null);
        }
        canvas.save(31);
        canvas.restore();
        return createBitmap;
    }

    public void hidePop() {
        if (d != 0) {
            this.c.a.b().c(this.c.a.a);
            this.c.a.b().a(this.c.a.a, false);
            this.b.a.c().remove(this);
            d = 0;
        }
    }

    public void showPopup(Bitmap bitmap, GeoPoint geoPoint, int i) {
        if (geoPoint != null && bitmap != null) {
            this.c.a.b().c(this.c.a.a);
            Bundle bundle = new Bundle();
            bundle.putInt("layeraddr", ((Integer) this.c.a.e.get("popup")).intValue());
            bundle.putInt("bshow", 1);
            GeoPoint b = e.b(geoPoint);
            bundle.putInt(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
            bundle.putInt("y", b.getLatitudeE6());
            bundle.putInt("imgW", bitmap.getWidth());
            bundle.putInt("imgH", bitmap.getHeight());
            bundle.putInt("showLR", 1);
            bundle.putInt("icon0width", 0);
            bundle.putInt("icon1width", 0);
            bundle.putInt("iconlayer", 1);
            bundle.putInt("offset", i);
            bundle.putInt("popname", -1288857266);
            ByteBuffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
            bitmap.copyPixelsToBuffer(allocate);
            bundle.putByteArray("imgdata", allocate.array());
            this.c.a.b().b(bundle);
            this.c.a.b().a(this.c.a.a, true);
            this.c.a.b().a(this.c.a.a);
            if (d == 0) {
                this.b.a.c().add(this);
            }
            d++;
        }
    }

    public void showPopup(Bitmap[] bitmapArr, GeoPoint geoPoint, int i) {
        int i2 = 0;
        if (geoPoint != null && bitmapArr != null && bitmapArr.length != 0 && i >= 0) {
            int i3;
            Bitmap a;
            int width;
            switch (bitmapArr.length) {
                case 1:
                    i3 = 0;
                    a = a(bitmapArr[0], null, null);
                    break;
                case 2:
                    width = bitmapArr[0] != null ? bitmapArr[0].getWidth() : 0;
                    a = a(bitmapArr[0], bitmapArr[1], null);
                    i3 = width;
                    break;
                default:
                    if (bitmapArr.length <= 2) {
                        a = null;
                        i3 = 0;
                        break;
                    }
                    if (bitmapArr[0] != null) {
                        width = bitmapArr[0].getWidth();
                        if (bitmapArr[1] == null) {
                            showPopup(bitmapArr[0], geoPoint, i);
                            return;
                        }
                    }
                    width = 0;
                    i3 = bitmapArr[2] != null ? bitmapArr[2].getWidth() : 0;
                    a = a(bitmapArr[0], bitmapArr[1], bitmapArr[2]);
                    i2 = i3;
                    i3 = width;
                    break;
            }
            if (a != null) {
                this.c.a.b().c(this.c.a.a);
                Bundle bundle = new Bundle();
                bundle.putInt("layeraddr", ((Integer) this.c.a.e.get("popup")).intValue());
                bundle.putInt("bshow", 1);
                GeoPoint b = e.b(geoPoint);
                bundle.putInt(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                bundle.putInt("y", b.getLatitudeE6());
                bundle.putInt("imgW", a.getWidth());
                bundle.putInt("imgH", a.getHeight());
                bundle.putInt("showLR", 1);
                bundle.putInt("icon0width", i3);
                bundle.putInt("icon1width", i2);
                bundle.putInt("iconlayer", 1);
                bundle.putInt("offset", i);
                bundle.putInt("popname", -1288857266);
                ByteBuffer allocate = ByteBuffer.allocate((a.getWidth() * a.getHeight()) * 4);
                a.copyPixelsToBuffer(allocate);
                bundle.putByteArray("imgdata", allocate.array());
                this.c.a.b().b(bundle);
                this.c.a.b().a(this.c.a.a, true);
                this.c.a.b().a(this.c.a.a);
                if (d == 0) {
                    this.b.a.c().add(this);
                }
                d++;
            }
        }
    }
}
