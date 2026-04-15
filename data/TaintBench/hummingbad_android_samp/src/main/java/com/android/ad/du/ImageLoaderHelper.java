package com.android.ad.du;

import android.content.Context;
import com.mopub.common.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;

public class ImageLoaderHelper {
    private static boolean mIsInited = false;
    private static boolean mShouldToolboxInitImageLoader = true;

    public static void disableToolboxInitImageLoader() {
        mShouldToolboxInitImageLoader = false;
    }

    private static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new Builder(context).diskCacheSize(Constants.TEN_MB).build());
    }

    public static ImageLoader getInstance(Context context) {
        if (mShouldToolboxInitImageLoader && !mIsInited) {
            synchronized (ImageLoaderHelper.class) {
                if (mShouldToolboxInitImageLoader && !mIsInited) {
                    initImageLoader(context);
                    mIsInited = true;
                }
            }
        }
        return ImageLoader.getInstance();
    }
}
