package com.moceanmobile.mast;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageView extends android.widget.ImageView {
    /* access modifiers changed from: private */
    public GifDecoder gifDecoder = null;
    /* access modifiers changed from: private */
    public Worker gifWorker = null;

    private class Worker extends Thread {
        private Worker() {
        }

        /* synthetic */ Worker(ImageView imageView, Worker worker) {
            this();
        }

        public void run() {
            try {
                Object obj;
                int frameCount = ImageView.this.gifDecoder.getFrameCount();
                int loopCount = ImageView.this.gifDecoder.getLoopCount();
                if (loopCount == 0) {
                    obj = 1;
                } else {
                    obj = null;
                }
                while (ImageView.this.gifWorker == this) {
                    int i;
                    if (obj == null) {
                        i = loopCount - 1;
                        if (loopCount > 0) {
                            loopCount = i;
                        } else {
                            return;
                        }
                    }
                    for (int i2 = 0; i2 < frameCount; i2++) {
                        int i3;
                        i = ImageView.this.gifDecoder.getDelay(i2);
                        if (i < 0) {
                            i3 = 100;
                        } else {
                            i3 = i;
                        }
                        final Bitmap createBitmap = Bitmap.createBitmap(ImageView.this.gifDecoder.getFrame(i2), ImageView.this.gifDecoder.getWidth(), ImageView.this.gifDecoder.getHeight(), Config.ARGB_4444);
                        ((Activity) ImageView.this.getContext()).runOnUiThread(new Runnable() {
                            public void run() {
                                ImageView.this.setGifBitmap(createBitmap);
                            }
                        });
                        sleep((long) i3);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public ImageView(Context context) {
        super(context);
    }

    public void setImageDrawable(Drawable drawable) {
        resetGifState();
        super.setImageDrawable(drawable);
    }

    public void setImageGifDecoder(GifDecoder gifDecoder) {
        if (gifDecoder != null && gifDecoder.getFrameCount() == 0) {
            gifDecoder = null;
        }
        resetGifState();
        if (gifDecoder != null) {
            this.gifDecoder = gifDecoder;
            this.gifWorker = new Worker(this, null);
            this.gifWorker.start();
        }
    }

    public GifDecoder getImageGifDecoder() {
        return this.gifDecoder;
    }

    private void resetGifState() {
        if (this.gifWorker != null) {
            this.gifWorker.interrupt();
            this.gifWorker = null;
        }
        super.setImageDrawable(null);
    }

    /* access modifiers changed from: private */
    public void setGifBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap bitmap2;
            Drawable drawable = getDrawable();
            if (drawable instanceof BitmapDrawable) {
                bitmap2 = ((BitmapDrawable) drawable).getBitmap();
            } else {
                bitmap2 = null;
            }
            super.setImageDrawable(new BitmapDrawable(getContext().getResources(), bitmap));
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
        }
    }
}
