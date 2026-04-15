package com.baidu.mapapi.map;

import android.graphics.Typeface;
import com.baidu.mapapi.map.Symbol.Color;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class TextItem {
    public static final int ALIGN_BOTTOM = 2;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_TOP = 1;
    private String a;
    public int align;
    public Color bgColor;
    public Color fontColor;
    public int fontSize;
    public GeoPoint pt;
    public String text;
    public Typeface typeface;

    public TextItem() {
        this.fontSize = 12;
        this.align = 0;
        this.fontSize = 0;
    }

    /* access modifiers changed from: 0000 */
    public String a() {
        return this.a;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.a = str;
    }
}
