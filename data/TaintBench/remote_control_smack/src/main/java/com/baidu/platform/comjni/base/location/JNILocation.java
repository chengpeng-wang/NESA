package com.baidu.platform.comjni.base.location;

import android.os.Bundle;

public class JNILocation {
    public native boolean CoordinateEncryptEx(int i, float f, float f2, Bundle bundle, String str);

    public native int Create();
}
