package com.google.android.gms.ads.internal.formats;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class zza {
    private static final int zzvA = Color.rgb(204, 204, 204);
    static final int zzvB = zzvA;
    static final int zzvC = zzvz;
    private static final int zzvz = Color.rgb(12, 174, 206);
    private final int mTextColor;
    private final String zzvD;
    private final Drawable zzvE;
    private final int zzvF;
    private final int zzvG;

    public zza(String str, Drawable drawable, Integer num, Integer num2, Integer num3) {
        this.zzvD = str;
        this.zzvE = drawable;
        this.zzvF = num != null ? num.intValue() : zzvB;
        this.mTextColor = num2 != null ? num2.intValue() : zzvC;
        this.zzvG = num3 != null ? num3.intValue() : 12;
    }

    public int getBackgroundColor() {
        return this.zzvF;
    }

    public Drawable getIcon() {
        return this.zzvE;
    }

    public String getText() {
        return this.zzvD;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public int getTextSize() {
        return this.zzvG;
    }
}
