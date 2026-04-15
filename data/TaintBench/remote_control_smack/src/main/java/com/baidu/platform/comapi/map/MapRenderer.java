package com.baidu.platform.comapi.map;

import android.opengl.GLSurfaceView.Renderer;
import com.baidu.platform.comapi.d.c;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MapRenderer implements Renderer {
    public static int a;
    public static int b;
    public static int c = 0;
    public static int d = 2048;
    public static int e = 14336;
    public static int f = 8192;
    private int g = 0;

    public MapRenderer(int i) {
        this.g = i;
    }

    private static native void nativeInit();

    private static native void nativeRender(int i);

    private static native void nativeResize(int i, int i2, int i3, int i4, int i5);

    public void onDrawFrame(GL10 gl10) {
        if (c <= 1) {
            nativeResize(a, b, d, e, f);
            c++;
        }
        nativeRender(this.g);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        nativeResize(i, i2, d, e, f);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        nativeInit();
        String glGetString = gl10.glGetString(7938);
        if (glGetString.equalsIgnoreCase("OpenGL ES-CM 1.0") || glGetString.equalsIgnoreCase("OpenGL ES 1.0-CM")) {
            d = 256;
            e = 1152;
            f = 640;
        }
        String glGetString2 = gl10.glGetString(7937);
        if (c.s() == null || !c.s().equals(glGetString) || c.r() == null || !c.r().equals(glGetString2)) {
            c.a(glGetString, glGetString2);
        }
    }
}
