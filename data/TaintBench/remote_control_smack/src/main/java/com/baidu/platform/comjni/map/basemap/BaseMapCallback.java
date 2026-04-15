package com.baidu.platform.comjni.map.basemap;

import android.os.Bundle;

public class BaseMapCallback {
    private BaseMapCallback a = null;

    public int ReqLayerData(Bundle bundle, int i, int i2) {
        return this.a == null ? 0 : this.a.ReqLayerData(bundle, i, i2);
    }

    public boolean SetMapCallback(BaseMapCallback baseMapCallback) {
        if (baseMapCallback == null) {
            return false;
        }
        this.a = baseMapCallback;
        return true;
    }
}
