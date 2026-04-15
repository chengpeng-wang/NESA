package com.baidu.mapapi.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class j {
    private Bitmap a;

    public j(byte[] bArr) {
        try {
            this.a = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public Bitmap a() {
        return this.a;
    }
}
