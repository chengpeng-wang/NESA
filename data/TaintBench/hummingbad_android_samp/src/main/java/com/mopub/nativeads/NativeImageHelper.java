package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.Networking;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.mopub.volley.toolbox.ImageLoader.ImageContainer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NativeImageHelper {

    public interface ImageListener {
        void onImagesCached();

        void onImagesFailedToCache(NativeErrorCode nativeErrorCode);
    }

    public static void preCacheImages(@NonNull Context context, @NonNull List<String> imageUrls, @NonNull final ImageListener imageListener) {
        ImageLoader imageLoader = Networking.getImageLoader(context);
        final AtomicInteger atomicInteger = new AtomicInteger(imageUrls.size());
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AnonymousClass1 anonymousClass1 = new com.mopub.volley.toolbox.ImageLoader.ImageListener() {
            public final void onResponse(ImageContainer imageContainer, boolean isImmediate) {
                if (imageContainer.getBitmap() != null && atomicInteger.decrementAndGet() == 0 && !atomicBoolean.get()) {
                    imageListener.onImagesCached();
                }
            }

            public final void onErrorResponse(VolleyError volleyError) {
                MoPubLog.d("Failed to download a native ads image:", volleyError);
                boolean andSet = atomicBoolean.getAndSet(true);
                atomicInteger.decrementAndGet();
                if (!andSet) {
                    imageListener.onImagesFailedToCache(NativeErrorCode.IMAGE_DOWNLOAD_FAILURE);
                }
            }
        };
        for (String str : imageUrls) {
            if (TextUtils.isEmpty(str)) {
                atomicBoolean.set(true);
                imageListener.onImagesFailedToCache(NativeErrorCode.IMAGE_DOWNLOAD_FAILURE);
                return;
            }
            imageLoader.get(str, anonymousClass1);
        }
    }

    public static void loadImageView(@Nullable String url, @Nullable final ImageView imageView) {
        if (!NoThrow.checkNotNull(imageView, "Cannot load image into null ImageView")) {
            return;
        }
        if (NoThrow.checkNotNull(url, "Cannot load image with null url")) {
            Networking.getImageLoader(imageView.getContext()).get(url, new com.mopub.volley.toolbox.ImageLoader.ImageListener() {
                public final void onResponse(ImageContainer imageContainer, boolean isImmediate) {
                    if (!isImmediate) {
                        MoPubLog.d("Image was not loaded immediately into your ad view. You should call preCacheImages as part of your custom event loading process.");
                    }
                    imageView.setImageBitmap(imageContainer.getBitmap());
                }

                public final void onErrorResponse(VolleyError volleyError) {
                    MoPubLog.d("Failed to load image.", volleyError);
                    imageView.setImageDrawable(null);
                }
            });
        } else {
            imageView.setImageDrawable(null);
        }
    }
}
