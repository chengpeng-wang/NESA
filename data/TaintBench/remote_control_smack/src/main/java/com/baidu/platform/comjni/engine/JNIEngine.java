package com.baidu.platform.comjni.engine;

import android.os.Bundle;

public class JNIEngine {
    public static native boolean GetFlaxLength(Bundle bundle);

    public static native boolean InitEngine(Bundle bundle);

    public static native void SetProxyInfo(String str, int i);

    public static native boolean StartSocketProc();

    public static native boolean UnInitEngine();

    public static native int initClass(Object obj, int i);
}
