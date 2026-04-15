package com.mopub.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.volley.RequestQueue;
import com.mopub.volley.toolbox.ImageLoader;
import com.mopub.volley.toolbox.ImageLoader.ImageCache;
import com.mopub.volley.toolbox.ImageLoader.ImageContainer;
import com.mopub.volley.toolbox.ImageLoader.ImageListener;

public class MaxWidthImageLoader extends ImageLoader {
    private final int mMaxImageWidth;

    @TargetApi(13)
    public MaxWidthImageLoader(RequestQueue queue, Context context, ImageCache imageCache) {
        super(queue, imageCache);
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (VERSION.SDK_INT < 13) {
            point.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        } else {
            defaultDisplay.getSize(point);
        }
        this.mMaxImageWidth = Math.min(point.x, point.y);
    }

    public ImageContainer get(String requestUrl, ImageListener listener) {
        return super.get(requestUrl, listener, this.mMaxImageWidth, 0);
    }
}
