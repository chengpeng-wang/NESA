package com.baidu.platform.comjni.base.userdatacollect;

import android.os.Bundle;

public class JNIUserdataCollect {
    public native void AppendRecord(int i, String str, String str2);

    public native int Create();

    public native boolean CreateUDC(int i, String str, Bundle bundle);

    public native int Release(int i);

    public native void Save(int i);
}
