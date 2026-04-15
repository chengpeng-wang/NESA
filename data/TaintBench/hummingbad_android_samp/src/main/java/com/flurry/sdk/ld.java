package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class ld implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        lt.a(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        String str = (String) obj;
        if (outputStream != null && str != null) {
            byte[] bytes = str.getBytes("utf-8");
            outputStream.write(bytes, 0, bytes.length);
        }
    }
}
