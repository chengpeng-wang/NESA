package com.facebook.ads.internal.thirdparty.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public interface p {
    OutputStream a(HttpURLConnection httpURLConnection);

    HttpURLConnection a(String str);

    void a(OutputStream outputStream, byte[] bArr);

    void a(HttpURLConnection httpURLConnection, j jVar, String str);

    boolean a(m mVar);

    byte[] a(InputStream inputStream);

    InputStream b(HttpURLConnection httpURLConnection);
}
