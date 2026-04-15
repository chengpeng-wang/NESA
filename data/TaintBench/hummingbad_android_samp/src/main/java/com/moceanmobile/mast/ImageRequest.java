package com.moceanmobile.mast;

public class ImageRequest {
    /* access modifiers changed from: private */
    public Handler handler = null;
    /* access modifiers changed from: private|final */
    public final int timeout;
    /* access modifiers changed from: private|final */
    public final String url;
    /* access modifiers changed from: private|final */
    public final boolean useGifDecoder;
    /* access modifiers changed from: private|final */
    public final String userAgent;

    public interface Handler {
        void imageFailed(ImageRequest imageRequest, Exception exception);

        void imageReceived(ImageRequest imageRequest, Object obj);
    }

    private class RequestProcessor implements Runnable {
        private RequestProcessor() {
        }

        /* synthetic */ RequestProcessor(ImageRequest imageRequest, RequestProcessor requestProcessor) {
            this();
        }

        /* JADX WARNING: Missing block: B:19:0x009b, code skipped:
            if (r1.read(r5) == 0) goto L_0x009d;
     */
        /* JADX WARNING: Missing block: B:27:0x00c2, code skipped:
            if (r1 == null) goto L_0x00c4;
     */
        /* JADX WARNING: Missing block: B:28:0x00c4, code skipped:
            r1 = null;
     */
        public void run() {
            /*
            r8 = this;
            r1 = 0;
            r2 = 0;
            r3 = 1;
            r0 = new java.net.URL;	 Catch:{ Exception -> 0x00b1 }
            r4 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r4 = r4.url;	 Catch:{ Exception -> 0x00b1 }
            r0.<init>(r4);	 Catch:{ Exception -> 0x00b1 }
            r0 = r0.openConnection();	 Catch:{ Exception -> 0x00b1 }
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00b1 }
            r4 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r4 = r4.timeout;	 Catch:{ Exception -> 0x00b1 }
            r4 = r4 * 1000;
            r0.setConnectTimeout(r4);	 Catch:{ Exception -> 0x00b1 }
            r4 = "User-Agent";
            r5 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r5 = r5.userAgent;	 Catch:{ Exception -> 0x00b1 }
            r0.setRequestProperty(r4, r5);	 Catch:{ Exception -> 0x00b1 }
            r4 = "Connection";
            r5 = "close";
            r0.setRequestProperty(r4, r5);	 Catch:{ Exception -> 0x00b1 }
            r4 = 1;
            r0.setDoInput(r4);	 Catch:{ Exception -> 0x00b1 }
            r4 = "GET";
            r0.setRequestMethod(r4);	 Catch:{ Exception -> 0x00b1 }
            r4 = r0.getResponseCode();	 Catch:{ Exception -> 0x00b1 }
            r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r4 == r5) goto L_0x0057;
        L_0x0042:
            r0 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r0 = r0.handler;	 Catch:{ Exception -> 0x00b1 }
            if (r0 == 0) goto L_0x0056;
        L_0x004a:
            r0 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r0 = r0.handler;	 Catch:{ Exception -> 0x00b1 }
            r1 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r2 = 0;
            r0.imageFailed(r1, r2);	 Catch:{ Exception -> 0x00b1 }
        L_0x0056:
            return;
        L_0x0057:
            r4 = r0.getInputStream();	 Catch:{ Exception -> 0x00b1 }
            r5 = new java.io.BufferedInputStream;	 Catch:{ Exception -> 0x00b1 }
            r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r5.<init>(r4, r6);	 Catch:{ Exception -> 0x00b1 }
            r4 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r5.mark(r4);	 Catch:{ Exception -> 0x00b1 }
            r4 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r4 = r4.useGifDecoder;	 Catch:{ Exception -> 0x00b1 }
            if (r4 == 0) goto L_0x0090;
        L_0x0071:
            r4 = 3;
            r4 = new byte[r4];	 Catch:{ Exception -> 0x00b1 }
            r5.read(r4);	 Catch:{ Exception -> 0x00b1 }
            r6 = 0;
            r6 = r4[r6];	 Catch:{ Exception -> 0x00b1 }
            r7 = 71;
            if (r6 != r7) goto L_0x008d;
        L_0x007e:
            r6 = 1;
            r6 = r4[r6];	 Catch:{ Exception -> 0x00b1 }
            r7 = 73;
            if (r6 != r7) goto L_0x008d;
        L_0x0085:
            r6 = 2;
            r4 = r4[r6];	 Catch:{ Exception -> 0x00b1 }
            r6 = 70;
            if (r4 != r6) goto L_0x008d;
        L_0x008c:
            r1 = r3;
        L_0x008d:
            r5.reset();	 Catch:{ Exception -> 0x00b1 }
        L_0x0090:
            if (r1 == 0) goto L_0x00be;
        L_0x0092:
            r1 = new com.moceanmobile.mast.GifDecoder;	 Catch:{ Exception -> 0x00b1 }
            r1.m1782init();	 Catch:{ Exception -> 0x00b1 }
            r3 = r1.read(r5);	 Catch:{ Exception -> 0x00b1 }
            if (r3 != 0) goto L_0x00c4;
        L_0x009d:
            if (r1 == 0) goto L_0x00c6;
        L_0x009f:
            r2 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r2 = r2.handler;	 Catch:{ Exception -> 0x00b1 }
            r3 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r2.imageReceived(r3, r1);	 Catch:{ Exception -> 0x00b1 }
        L_0x00aa:
            r5.close();	 Catch:{ Exception -> 0x00b1 }
            r0.disconnect();	 Catch:{ Exception -> 0x00b1 }
            goto L_0x0056;
        L_0x00b1:
            r0 = move-exception;
            r1 = com.moceanmobile.mast.ImageRequest.this;
            r1 = r1.handler;
            r2 = com.moceanmobile.mast.ImageRequest.this;
            r1.imageFailed(r2, r0);
            goto L_0x0056;
        L_0x00be:
            r1 = android.graphics.BitmapFactory.decodeStream(r5);	 Catch:{ Exception -> 0x00b1 }
            if (r1 != 0) goto L_0x009d;
        L_0x00c4:
            r1 = r2;
            goto L_0x009d;
        L_0x00c6:
            r1 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r1 = r1.handler;	 Catch:{ Exception -> 0x00b1 }
            r2 = com.moceanmobile.mast.ImageRequest.this;	 Catch:{ Exception -> 0x00b1 }
            r3 = 0;
            r1.imageFailed(r2, r3);	 Catch:{ Exception -> 0x00b1 }
            goto L_0x00aa;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.moceanmobile.mast.ImageRequest$RequestProcessor.run():void");
        }
    }

    public static ImageRequest create(int timeout, String url, String userAgent, boolean useGifDecoder, Handler handler) {
        if (handler == null) {
            return null;
        }
        ImageRequest imageRequest = new ImageRequest(timeout, url, userAgent, useGifDecoder, handler);
        imageRequest.start();
        return imageRequest;
    }

    public ImageRequest(int timeout, String url, String userAgent, boolean useGifDecoder, Handler handler) {
        this.timeout = timeout;
        this.url = url;
        this.userAgent = userAgent;
        this.useGifDecoder = useGifDecoder;
        this.handler = handler;
    }

    public void cancel() {
        this.handler = null;
    }

    private void start() {
        Background.getExecutor().execute(new RequestProcessor(this, null));
    }
}
