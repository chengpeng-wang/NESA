package com.flurry.sdk;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Method;

public final class lr {
    public static int a(int i) {
        return Math.round(((float) i) / a().density);
    }

    @SuppressLint({"NewApi"})
    public static DisplayMetrics a() {
        Display defaultDisplay = ((WindowManager) hz.a.b.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics;
        if (VERSION.SDK_INT >= 17) {
            displayMetrics = new DisplayMetrics();
            defaultDisplay.getRealMetrics(displayMetrics);
            return displayMetrics;
        }
        if (VERSION.SDK_INT >= 14) {
            try {
                displayMetrics = new DisplayMetrics();
                Display.class.getMethod("getRealMetrics", new Class[0]).invoke(defaultDisplay, new Object[]{displayMetrics});
                return displayMetrics;
            } catch (Exception e) {
            }
        }
        defaultDisplay = ((WindowManager) hz.a.b.getSystemService("window")).getDefaultDisplay();
        displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int b() {
        return a(e().x);
    }

    public static int c() {
        return a(e().y);
    }

    public static int d() {
        Point e = e();
        return e.x == e.y ? 3 : e.x < e.y ? 1 : 2;
    }

    @SuppressLint({"NewApi"})
    private static Point e() {
        Display defaultDisplay = ((WindowManager) hz.a.b.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        } else if (VERSION.SDK_INT >= 14) {
            try {
                Method method = Display.class.getMethod("getRawHeight", new Class[0]);
                point.x = ((Integer) Display.class.getMethod("getRawWidth", new Class[0]).invoke(defaultDisplay, new Object[0])).intValue();
                point.y = ((Integer) method.invoke(defaultDisplay, new Object[0])).intValue();
            } catch (Throwable th) {
                defaultDisplay.getSize(point);
            }
        } else if (VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
        } else {
            point.x = defaultDisplay.getWidth();
            point.y = defaultDisplay.getHeight();
        }
        return point;
    }
}
