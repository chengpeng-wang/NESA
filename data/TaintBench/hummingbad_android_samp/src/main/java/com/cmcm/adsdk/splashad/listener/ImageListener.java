package com.cmcm.adsdk.splashad.listener;

import android.graphics.Bitmap;

public interface ImageListener {
    void onFailed(String str);

    void onSuccessed(Bitmap bitmap);
}
