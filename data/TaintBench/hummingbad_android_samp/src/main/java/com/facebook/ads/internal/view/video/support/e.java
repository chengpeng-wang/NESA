package com.facebook.ads.internal.view.video.support;

import android.net.Uri;
import android.view.View;

interface e {
    void a(View view, Uri uri);

    int getCurrentPosition();

    void pause();

    void setFrameVideoViewListener(b bVar);

    void start();
}
