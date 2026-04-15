package com.baidu.platform.comjni.engine;

import android.os.Bundle;

public class AppEngine {
    public static boolean GetFlaxLength(Bundle bundle) {
        return JNIEngine.GetFlaxLength(bundle);
    }

    public static boolean InitEngine(Bundle bundle) {
        JNIEngine.initClass(new Bundle(), 0);
        return JNIEngine.InitEngine(bundle);
    }

    public static void SetProxyInfo(String str, int i) {
        JNIEngine.SetProxyInfo(str, i);
    }

    public static boolean StartSocketProc() {
        return JNIEngine.StartSocketProc();
    }

    public static boolean UnInitEngine() {
        return JNIEngine.UnInitEngine();
    }

    public static void despatchMessage(int i, int i2, int i3) {
        a.a(i, i2, i3);
    }
}
