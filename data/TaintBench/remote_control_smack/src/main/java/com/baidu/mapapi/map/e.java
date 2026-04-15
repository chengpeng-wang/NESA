package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.baidu.mapapi.utils.b;
import com.baidu.platform.comapi.map.a;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class e implements a {
    final /* synthetic */ MapView a;

    e(MapView mapView) {
        this.a = mapView;
    }

    public void a(boolean z) {
        if (z) {
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(new FileInputStream(b.h() + "/BaiduMapSDK/capture.png"));
                if (this.a.o != null && com.baidu.platform.comapi.a.a) {
                    this.a.o.onGetCurrentMap(decodeStream);
                }
            } catch (FileNotFoundException e) {
                this.a.o.onGetCurrentMap(null);
            }
        } else if (this.a.o != null && com.baidu.platform.comapi.a.a) {
            this.a.o.onGetCurrentMap(null);
        }
    }
}
