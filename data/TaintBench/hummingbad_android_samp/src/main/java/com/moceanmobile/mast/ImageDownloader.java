package com.moceanmobile.mast;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public final class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private ImageDownloaderListener listener = null;

    private interface ImageDownloaderListener {
        void onError();

        void onSuccess(Bitmap bitmap);
    }

    public ImageDownloader(ImageDownloaderListener listener) {
        this.listener = listener;
    }

    public static void loadImage(final ImageView imageView, String url) {
        if (imageView != null) {
            imageView.setImageBitmap(null);
            new ImageDownloader(new ImageDownloaderListener() {
                public void onSuccess(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }

                public void onError() {
                }
            }).execute(new String[]{url});
        }
    }

    /* access modifiers changed from: protected|final */
    public final void onPostExecute(Bitmap bitmap) {
        if (this.listener == null) {
            return;
        }
        if (bitmap != null) {
            this.listener.onSuccess(bitmap);
        } else {
            this.listener.onError();
        }
    }

    /* access modifiers changed from: protected|final|varargs */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0046 A:{SYNTHETIC, Splitter:B:19:0x0046} */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004b A:{SYNTHETIC, Splitter:B:22:0x004b} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070 A:{SYNTHETIC, Splitter:B:42:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0075 A:{SYNTHETIC, Splitter:B:45:0x0075} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0057 A:{SYNTHETIC, Splitter:B:29:0x0057} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005c A:{SYNTHETIC, Splitter:B:32:0x005c} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070 A:{SYNTHETIC, Splitter:B:42:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0075 A:{SYNTHETIC, Splitter:B:45:0x0075} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0057 A:{SYNTHETIC, Splitter:B:29:0x0057} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x005c A:{SYNTHETIC, Splitter:B:32:0x005c} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0070 A:{SYNTHETIC, Splitter:B:42:0x0070} */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0075 A:{SYNTHETIC, Splitter:B:45:0x0075} */
    public final android.graphics.Bitmap doInBackground(java.lang.String... r6) {
        /*
        r5 = this;
        r1 = 0;
        if (r6 == 0) goto L_0x00b0;
    L_0x0003:
        r0 = r6.length;	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        if (r0 <= 0) goto L_0x00b0;
    L_0x0006:
        r0 = 0;
        r0 = r6[r0];	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        if (r0 == 0) goto L_0x00b0;
    L_0x000b:
        r2 = new java.net.URL;	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        r2.<init>(r0);	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        r0 = r2.openConnection();	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x004f, all -> 0x006c }
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r0.setConnectTimeout(r2);	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r2 = "Connection";
        r3 = "close";
        r0.setRequestProperty(r2, r3);	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r2 = 1;
        r0.setDoInput(r2);	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r2 = "GET";
        r0.setRequestMethod(r2);	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r2 = r0.getResponseCode();	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 != r3) goto L_0x00ab;
    L_0x0033:
        r3 = r0.getInputStream();	 Catch:{ Exception -> 0x009a, all -> 0x008d }
        r2 = r5.isCancelled();	 Catch:{ Exception -> 0x00a0, all -> 0x0092 }
        if (r2 != 0) goto L_0x00a7;
    L_0x003d:
        r1 = android.graphics.BitmapFactory.decodeStream(r3);	 Catch:{ Exception -> 0x00a0, all -> 0x0092 }
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0044:
        if (r3 == 0) goto L_0x0049;
    L_0x0046:
        r3.close();	 Catch:{ Exception -> 0x0083 }
    L_0x0049:
        if (r1 == 0) goto L_0x004e;
    L_0x004b:
        r1.disconnect();	 Catch:{ Exception -> 0x0088 }
    L_0x004e:
        return r0;
    L_0x004f:
        r0 = move-exception;
        r2 = r1;
        r3 = r1;
    L_0x0052:
        r0.printStackTrace();	 Catch:{ all -> 0x0097 }
        if (r3 == 0) goto L_0x005a;
    L_0x0057:
        r3.close();	 Catch:{ Exception -> 0x0061 }
    L_0x005a:
        if (r2 == 0) goto L_0x00a5;
    L_0x005c:
        r2.disconnect();	 Catch:{ Exception -> 0x0066 }
        r0 = r1;
        goto L_0x004e;
    L_0x0061:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x005a;
    L_0x0066:
        r0 = move-exception;
        r0.printStackTrace();
        r0 = r1;
        goto L_0x004e;
    L_0x006c:
        r0 = move-exception;
        r3 = r1;
    L_0x006e:
        if (r3 == 0) goto L_0x0073;
    L_0x0070:
        r3.close();	 Catch:{ Exception -> 0x0079 }
    L_0x0073:
        if (r1 == 0) goto L_0x0078;
    L_0x0075:
        r1.disconnect();	 Catch:{ Exception -> 0x007e }
    L_0x0078:
        throw r0;
    L_0x0079:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0073;
    L_0x007e:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0078;
    L_0x0083:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0049;
    L_0x0088:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x004e;
    L_0x008d:
        r2 = move-exception;
        r3 = r1;
        r1 = r0;
        r0 = r2;
        goto L_0x006e;
    L_0x0092:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x006e;
    L_0x0097:
        r0 = move-exception;
        r1 = r2;
        goto L_0x006e;
    L_0x009a:
        r2 = move-exception;
        r3 = r1;
        r4 = r0;
        r0 = r2;
        r2 = r4;
        goto L_0x0052;
    L_0x00a0:
        r2 = move-exception;
        r4 = r2;
        r2 = r0;
        r0 = r4;
        goto L_0x0052;
    L_0x00a5:
        r0 = r1;
        goto L_0x004e;
    L_0x00a7:
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x0044;
    L_0x00ab:
        r3 = r1;
        r4 = r0;
        r0 = r1;
        r1 = r4;
        goto L_0x0044;
    L_0x00b0:
        r0 = r1;
        r3 = r1;
        goto L_0x0044;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moceanmobile.mast.ImageDownloader.doInBackground(java.lang.String[]):android.graphics.Bitmap");
    }
}
