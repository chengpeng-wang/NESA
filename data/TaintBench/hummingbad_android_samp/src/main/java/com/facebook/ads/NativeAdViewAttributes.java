package com.facebook.ads;

import android.graphics.Color;
import android.graphics.Typeface;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.c;
import org.json.JSONObject;

public class NativeAdViewAttributes {
    private Typeface a = Typeface.DEFAULT;
    private int b = -1;
    private int c = -16777216;
    private int d = -11643291;
    private int e = 0;
    private int f = -12420889;
    private int g = -12420889;
    private boolean h = true;

    public NativeAdViewAttributes(JSONObject jSONObject) {
        int i = 0;
        try {
            int parseColor = jSONObject.getBoolean("background_transparent") ? 0 : Color.parseColor(jSONObject.getString("background_color"));
            int parseColor2 = Color.parseColor(jSONObject.getString("title_text_color"));
            int parseColor3 = Color.parseColor(jSONObject.getString("description_text_color"));
            int parseColor4 = jSONObject.getBoolean("button_transparent") ? 0 : Color.parseColor(jSONObject.getString("button_color"));
            if (!jSONObject.getBoolean("button_border_transparent")) {
                i = Color.parseColor(jSONObject.getString("button_border_color"));
            }
            int parseColor5 = Color.parseColor(jSONObject.getString("button_text_color"));
            Typeface create = Typeface.create(jSONObject.getString("android_typeface"), 0);
            this.b = parseColor;
            this.c = parseColor2;
            this.d = parseColor3;
            this.e = parseColor4;
            this.g = i;
            this.f = parseColor5;
            this.a = create;
        } catch (Exception e) {
            c.a(b.a(e, "Error retrieving native ui configuration data"));
        }
    }

    public boolean getAutoplay() {
        return this.h;
    }

    public int getBackgroundColor() {
        return this.b;
    }

    public int getButtonBorderColor() {
        return this.g;
    }

    public int getButtonColor() {
        return this.e;
    }

    public int getButtonTextColor() {
        return this.f;
    }

    public int getDescriptionTextColor() {
        return this.d;
    }

    public int getDescriptionTextSize() {
        return 10;
    }

    public int getTitleTextColor() {
        return this.c;
    }

    public int getTitleTextSize() {
        return 16;
    }

    public Typeface getTypeface() {
        return this.a;
    }

    public NativeAdViewAttributes setAutoplay(boolean z) {
        this.h = z;
        return this;
    }

    public NativeAdViewAttributes setBackgroundColor(int i) {
        this.b = i;
        return this;
    }

    public NativeAdViewAttributes setButtonBorderColor(int i) {
        this.g = i;
        return this;
    }

    public NativeAdViewAttributes setButtonColor(int i) {
        this.e = i;
        return this;
    }

    public NativeAdViewAttributes setButtonTextColor(int i) {
        this.f = i;
        return this;
    }

    public NativeAdViewAttributes setDescriptionTextColor(int i) {
        this.d = i;
        return this;
    }

    public NativeAdViewAttributes setTitleTextColor(int i) {
        this.c = i;
        return this;
    }

    public NativeAdViewAttributes setTypeface(Typeface typeface) {
        this.a = typeface;
        return this;
    }
}
