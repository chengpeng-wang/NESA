package com.flurry.sdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.io.OutputStream;

public final class gp implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        return inputStream == null ? null : BitmapFactory.decodeStream(inputStream);
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        Bitmap bitmap = (Bitmap) obj;
        if (outputStream != null && bitmap != null) {
            throw new UnsupportedOperationException("Serialization for bitmaps is not yet implemented");
        }
    }
}
