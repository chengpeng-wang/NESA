package com.baidu.platform.comjni.map.commonmemcache;

import android.os.Bundle;

public class JNICommonMemCache {
    public native int Create();

    public native void Init(int i, Bundle bundle);

    public native int Release(int i);
}
