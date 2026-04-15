package com.baidu.mapapi.map;

import android.graphics.Color;
import android.os.Bundle;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smackx.GroupChatInvitation;
import vi.com.gdi.bgl.android.java.EnvDrawText;

public class TextOverlay extends Overlay {
    private ArrayList<TextItem> a;
    private MapView b;

    public TextOverlay(MapView mapView) {
        this.b = mapView;
        this.mType = 30;
        this.a = new ArrayList();
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        this.mLayerID = this.b.a("text");
        if (this.mLayerID == 0) {
            throw new RuntimeException("can not add text layer");
        }
    }

    public void addText(TextItem textItem) {
        if (textItem != null && textItem.pt != null && textItem.text != null && textItem.fontSize != 0 && textItem.fontColor != null) {
            if (this.mLayerID == 0) {
                this.a.add(textItem);
                return;
            }
            Bundle bundle = new Bundle();
            GeoPoint b = e.b(textItem.pt);
            int longitudeE6 = b.getLongitudeE6();
            int latitudeE6 = b.getLatitudeE6();
            bundle.putInt(GroupChatInvitation.ELEMENT_NAME, longitudeE6);
            bundle.putInt("y", latitudeE6);
            bundle.putInt("fsize", textItem.fontSize);
            bundle.putInt("bgcolor", textItem.bgColor == null ? Color.argb(0, 0, 0, 0) : Color.argb(textItem.bgColor.alpha, textItem.bgColor.blue, textItem.bgColor.green, textItem.bgColor.red));
            bundle.putInt("fcolor", Color.argb(textItem.fontColor.alpha, textItem.fontColor.blue, textItem.fontColor.green, textItem.fontColor.red));
            bundle.putString("str", textItem.text);
            textItem.a(System.currentTimeMillis() + "_" + size());
            bundle.putString("id", textItem.a());
            bundle.putInt("align", textItem.align);
            bundle.putInt("textaddr", this.mLayerID);
            if (textItem.typeface != null) {
                EnvDrawText.registFontCache(textItem.typeface.hashCode(), textItem.typeface);
                bundle.putInt("fstyle", textItem.typeface.hashCode());
            } else {
                bundle.putInt("fstyle", 0);
            }
            this.a.add(textItem);
            this.b.getController().a.b().h(bundle);
        }
    }

    /* access modifiers changed from: 0000 */
    public void b() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.a);
        removeAll();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            addText((TextItem) it.next());
        }
    }

    public List<TextItem> getAllText() {
        return this.a;
    }

    public TextItem getText(int i) {
        return (i >= size() || i < 0) ? null : (TextItem) this.a.get(i);
    }

    public boolean removeAll() {
        this.b.getController().a.b().c(this.mLayerID);
        this.b.getController().a.b().a(this.mLayerID, false);
        this.b.getController().a.b().a(this.mLayerID);
        this.a.clear();
        return true;
    }

    public boolean removeText(TextItem textItem) {
        Bundle bundle = new Bundle();
        bundle.putInt("textaddr", this.mLayerID);
        if (textItem.a().equals("")) {
            return false;
        }
        bundle.putString("id", textItem.a());
        if (!this.b.getController().a.b().i(bundle)) {
            return false;
        }
        if (textItem.typeface != null) {
            EnvDrawText.removeFontCache(textItem.typeface.hashCode());
        }
        this.a.remove(textItem);
        return true;
    }

    public int size() {
        return this.a.size();
    }
}
