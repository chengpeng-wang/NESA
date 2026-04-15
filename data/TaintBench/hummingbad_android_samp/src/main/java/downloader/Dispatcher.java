package downloader;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import downloader.DownloadRequest.State;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.TextParseException;

class Dispatcher implements Runnable {
    private static final int CONNECT_TIMEOUT = 20000;
    private static final float DEFAULT_INTERVAL_DOWNLOAD_PERCENT = 0.02f;
    private static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private static final int RETRY_DURATION = 2000;
    private static final int SC_TEMPORARY_REDIRECT = 307;
    private static final int SOCKET_TIMEOUT = 20000;
    private Delivery mDelivery;
    private float mIntervalDownloadPercent = DEFAULT_INTERVAL_DOWNLOAD_PERCENT;
    private BlockingQueue<DownloadRequest> mQueue;
    private volatile boolean mQuit;
    private Thread mThread;

    public interface ErrorCode {
        public static final int FILE_IO_ERROR = 4;
        public static final int HTTP_ERROR = 1;
        public static final int INTERRUPTED_ERROR = 3;
        public static final int IO_ERROR = 5;
        public static final int PROTOCOL_ERROR = 2;
        public static final int UNKNOWN_ERROR = 0;
    }

    public float getIntervalDownloadPercent() {
        return this.mIntervalDownloadPercent;
    }

    public void setIntervalDownloadPercent(float intervalDownloadPercent) {
        if (intervalDownloadPercent > 1.0f || intervalDownloadPercent <= 0.0f) {
            throw new IllegalArgumentException("intervalDownloadPercent must be between 0 to 1");
        }
        this.mIntervalDownloadPercent = intervalDownloadPercent;
    }

    Dispatcher(BlockingQueue<DownloadRequest> queue, Delivery delivery) {
        this.mQueue = queue;
        this.mDelivery = delivery;
        this.mThread = new Thread(this);
        this.mThread.setName(Dispatcher.class.getSimpleName());
    }

    public void run() {
        Thread.currentThread().setPriority(10);
        DownloadRequest request = null;
        while (!this.mQuit) {
            try {
                request = (DownloadRequest) this.mQueue.take();
                download(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (((request != null ? 1 : 0) & this.mQuit) != 0) {
                    request.finish();
                    if (request.getState() != State.Success) {
                        postInterruptedError(request);
                    }
                }
            }
        }
    }

    private void postInterruptedError(DownloadRequest request) {
        request.setState(State.Failed);
        this.mDelivery.postFailure(request, 3, "download thread may be interrupted");
    }

    private HttpURLConnection prepareConnection(DownloadRequest request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        connection.setInstanceFollowRedirects(false);
        connection.setUseCaches(false);
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        connection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        long range = request.getRange();
        if (range > 0) {
            connection.setRequestProperty("Range", "bytes=" + range + "-");
            request.setDownloadedSize(range);
        }
        return connection;
    }

    /* JADX WARNING: Failed to extract finally block: empty outs */
    /* JADX WARNING: Failed to extract finally block: empty outs */
    /* JADX WARNING: Failed to extract finally block: empty outs */
    /* JADX WARNING: Missing block: B:4:0x000c, code skipped:
            updateFailure(r8, r0, r1.getResponseMessage());
     */
    /* JADX WARNING: Missing block: B:5:0x0013, code skipped:
            if (r1 == null) goto L_?;
     */
    /* JADX WARNING: Missing block: B:6:0x0015, code skipped:
            r1.disconnect();
     */
    /* JADX WARNING: Missing block: B:9:0x001c, code skipped:
            transferData(r1, r8);
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            return;
     */
    private void download(downloader.DownloadRequest r8) {
        /*
        r7 = this;
        r1 = 0;
        r1 = r7.prepareConnection(r8);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        r0 = r1.getResponseCode();	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        switch(r0) {
            case 200: goto L_0x0019;
            case 206: goto L_0x001c;
            case 301: goto L_0x002f;
            case 302: goto L_0x002f;
            case 303: goto L_0x002f;
            case 307: goto L_0x002f;
            case 416: goto L_0x0083;
            default: goto L_0x000c;
        };	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
    L_0x000c:
        r5 = r1.getResponseMessage();	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        r7.updateFailure(r8, r0, r5);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
    L_0x0013:
        if (r1 == 0) goto L_0x0018;
    L_0x0015:
        r1.disconnect();
    L_0x0018:
        return;
    L_0x0019:
        r8.deleteTmpFile();	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
    L_0x001c:
        r7.transferData(r1, r8);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        goto L_0x0013;
    L_0x0020:
        r2 = move-exception;
        r5 = 2;
        r6 = r2.getMessage();	 Catch:{ all -> 0x0096 }
        r7.updateFailure(r8, r5, r6);	 Catch:{ all -> 0x0096 }
        if (r1 == 0) goto L_0x0018;
    L_0x002b:
        r1.disconnect();
        goto L_0x0018;
    L_0x002f:
        r5 = r8.getRedirectTimes();	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        if (r5 < 0) goto L_0x006e;
    L_0x0035:
        r5 = downloader.Utils.getLocation(r1);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        r8.setUrl(r5);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        r7.download(r8);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        goto L_0x0013;
    L_0x0040:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x0096 }
        r5 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r6 = r8.getUrl();	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r5.<init>(r6);	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r3 = r5.getHost();	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r4 = r7.getCnameForHost(r3);	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r5 = r8.getUrl();	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r5 = r5.replace(r3, r4);	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r8.setUrl(r5);	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r5 = 1;
        r6 = r2.getMessage();	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
        r7.updateFailure(r8, r5, r6);	 Catch:{ MalformedURLException -> 0x00a1, TextParseException -> 0x009f, Exception | MalformedURLException | TextParseException -> 0x009d }
    L_0x0068:
        if (r1 == 0) goto L_0x0018;
    L_0x006a:
        r1.disconnect();
        goto L_0x0018;
    L_0x006e:
        r5 = "redirect too many times";
        r7.updateFailure(r8, r0, r5);	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        goto L_0x0013;
    L_0x0074:
        r2 = move-exception;
        r5 = 1;
        r6 = r2.getMessage();	 Catch:{ all -> 0x0096 }
        r7.updateFailure(r8, r5, r6);	 Catch:{ all -> 0x0096 }
        if (r1 == 0) goto L_0x0018;
    L_0x007f:
        r1.disconnect();
        goto L_0x0018;
    L_0x0083:
        r8.deleteTmpFile();	 Catch:{ MalformedURLException -> 0x0020, UnknownHostException -> 0x0040, IOException -> 0x0074, Exception -> 0x0087 }
        goto L_0x000c;
    L_0x0087:
        r2 = move-exception;
        r5 = 0;
        r6 = r2.getMessage();	 Catch:{ all -> 0x0096 }
        r7.updateFailure(r8, r5, r6);	 Catch:{ all -> 0x0096 }
        if (r1 == 0) goto L_0x0018;
    L_0x0092:
        r1.disconnect();
        goto L_0x0018;
    L_0x0096:
        r5 = move-exception;
        if (r1 == 0) goto L_0x009c;
    L_0x0099:
        r1.disconnect();
    L_0x009c:
        throw r5;
    L_0x009d:
        r5 = move-exception;
        goto L_0x0068;
    L_0x009f:
        r5 = move-exception;
        goto L_0x0068;
    L_0x00a1:
        r5 = move-exception;
        goto L_0x0068;
        */
        throw new UnsupportedOperationException("Method not decompiled: downloader.Dispatcher.download(downloader.DownloadRequest):void");
    }

    private String getCnameForHost(String host) throws TextParseException {
        Lookup lookup = new Lookup(host, 5);
        lookup.run();
        if (lookup.getAnswers().length == 0) {
            return null;
        }
        String target = ((CNAMERecord) lookup.getAnswers()[0]).getTarget().toString();
        return target.substring(0, target.length() - 1);
    }

    private void updateFailure(DownloadRequest request, int code, String msg) {
        request.setState(State.Failed);
        if (code != 1 || request.getProgressiveRetryTimes() < 0 || request.getTotalFailedRetryTimes() < 0) {
            request.finish();
            this.mDelivery.postFailure(request, code, msg);
            return;
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Logger.d(e.getMessage());
            if (this.mQuit) {
                request.finish();
                return;
            }
        }
        Logger.w("retry download.");
        download(request);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:60:0x0190=Splitter:B:60:0x0190, B:31:0x00c4=Splitter:B:31:0x00c4, B:39:0x00fd=Splitter:B:39:0x00fd, B:49:0x013e=Splitter:B:49:0x013e} */
    private void transferData(java.net.HttpURLConnection r23, downloader.DownloadRequest r24) {
        /*
        r22 = this;
        r20 = r23.getContentLength();
        r0 = r20;
        r5 = (long) r0;
        r19 = r24.getTempFile();
        r14 = 0;
        r16 = 0;
        r17 = new java.io.RandomAccessFile;	 Catch:{ FileNotFoundException -> 0x01e1, SocketTimeoutException -> 0x01de, IOException -> 0x01db, Exception -> 0x01d9 }
        r20 = "rw";
        r0 = r17;
        r1 = r19;
        r2 = r20;
        r0.<init>(r1, r2);	 Catch:{ FileNotFoundException -> 0x01e1, SocketTimeoutException -> 0x01de, IOException -> 0x01db, Exception -> 0x01d9 }
        r7 = r19.length();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r5 = r5 + r7;
        r20 = 0;
        r20 = (r7 > r20 ? 1 : (r7 == r20 ? 0 : -1));
        if (r20 <= 0) goto L_0x002b;
    L_0x0026:
        r0 = r17;
        r0.seek(r7);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
    L_0x002b:
        r0 = r24;
        r0.setDownloadedSize(r7);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = r24;
        r0.setTotalSize(r5);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = r24.isRetry();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        if (r20 != 0) goto L_0x0042;
    L_0x003b:
        r0 = r22;
        r1 = r24;
        r0.updateStart(r1);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
    L_0x0042:
        r14 = r23.getInputStream();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        if (r14 == 0) goto L_0x0076;
    L_0x0048:
        r20 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r20;
        r4 = new byte[r0];	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r18 = 0;
        r20 = 0;
        r20 = (r5 > r20 ? 1 : (r5 == r20 ? 0 : -1));
        if (r20 == 0) goto L_0x0085;
    L_0x0056:
        r0 = (float) r5;	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = r0;
        r0 = r22;
        r0 = r0.mIntervalDownloadPercent;	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r21 = r0;
        r18 = r20 * r21;
    L_0x0061:
        r9 = 0;
        r13 = 0;
    L_0x0063:
        r20 = java.lang.Thread.currentThread();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = r20.isInterrupted();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        if (r20 != 0) goto L_0x0073;
    L_0x006d:
        r20 = r24.isCancled();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        if (r20 == 0) goto L_0x008e;
    L_0x0073:
        r24.finish();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
    L_0x0076:
        r0 = r22;
        r1 = r17;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        r16 = r17;
    L_0x0084:
        return;
    L_0x0085:
        r0 = r4.length;	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = r0;
        r0 = r20;
        r0 = (float) r0;	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r18 = r0;
        goto L_0x0061;
    L_0x008e:
        r0 = r22;
        r15 = r0.readFromInputStream(r4, r14);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = -1;
        r0 = r20;
        if (r15 != r0) goto L_0x0123;
    L_0x009a:
        r20 = r24.getTempFile();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r11 = r20.length();	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = r24;
        r0.setDownloadedSize(r11);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = r24;
        r0.setTotalSize(r11);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = r22;
        r1 = r24;
        r0.updateProgress(r1);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r20 != 0) goto L_0x00e9;
    L_0x00b7:
        r0 = r22;
        r1 = r24;
        r2 = r23;
        r0.updateSuccess(r1, r2);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        goto L_0x0076;
    L_0x00c1:
        r10 = move-exception;
        r16 = r17;
    L_0x00c4:
        r20 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        downloader.Logger.w(r20);	 Catch:{ all -> 0x01c7 }
        r20 = 4;
        r21 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r16;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        goto L_0x0084;
    L_0x00e9:
        r20 = 1;
        r21 = "file size error";
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        goto L_0x0076;
    L_0x00fa:
        r10 = move-exception;
        r16 = r17;
    L_0x00fd:
        r20 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        downloader.Logger.w(r20);	 Catch:{ all -> 0x01c7 }
        r20 = 1;
        r21 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r16;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        goto L_0x0084;
    L_0x0123:
        r20 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r20;
        if (r15 != r0) goto L_0x0164;
    L_0x012a:
        r20 = 1;
        r21 = "transfer error";
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        goto L_0x0076;
    L_0x013b:
        r10 = move-exception;
        r16 = r17;
    L_0x013e:
        r20 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        downloader.Logger.w(r20);	 Catch:{ all -> 0x01c7 }
        r20 = 4;
        r21 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r16;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        goto L_0x0084;
    L_0x0164:
        r20 = 3;
        r0 = r24;
        r1 = r20;
        r0.setRetryTimes(r1);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = (long) r15;	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r20 = r0;
        r7 = r7 + r20;
        r20 = 0;
        r0 = r17;
        r1 = r20;
        r0.write(r4, r1, r15);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r0 = r24;
        r0.setDownloadedSize(r7);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r9 = r9 + r15;
        if (r13 != 0) goto L_0x01b6;
    L_0x0183:
        r13 = 1;
        r0 = r22;
        r1 = r24;
        r0.updateProgress(r1);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        goto L_0x0063;
    L_0x018d:
        r10 = move-exception;
        r16 = r17;
    L_0x0190:
        r20 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        downloader.Logger.w(r20);	 Catch:{ all -> 0x01c7 }
        r20 = 0;
        r21 = r10.getMessage();	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r24;
        r2 = r20;
        r3 = r21;
        r0.updateFailure(r1, r2, r3);	 Catch:{ all -> 0x01c7 }
        r0 = r22;
        r1 = r16;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        goto L_0x0084;
    L_0x01b6:
        r0 = (float) r9;
        r20 = r0;
        r20 = (r20 > r18 ? 1 : (r20 == r18 ? 0 : -1));
        if (r20 < 0) goto L_0x0063;
    L_0x01bd:
        r0 = r22;
        r1 = r24;
        r0.updateProgress(r1);	 Catch:{ FileNotFoundException -> 0x00c1, SocketTimeoutException -> 0x00fa, IOException -> 0x013b, Exception -> 0x018d, all -> 0x01d5 }
        r9 = 0;
        goto L_0x0063;
    L_0x01c7:
        r20 = move-exception;
    L_0x01c8:
        r0 = r22;
        r1 = r16;
        r0.closeIO(r1);
        r0 = r22;
        r0.closeIO(r14);
        throw r20;
    L_0x01d5:
        r20 = move-exception;
        r16 = r17;
        goto L_0x01c8;
    L_0x01d9:
        r10 = move-exception;
        goto L_0x0190;
    L_0x01db:
        r10 = move-exception;
        goto L_0x013e;
    L_0x01de:
        r10 = move-exception;
        goto L_0x00fd;
    L_0x01e1:
        r10 = move-exception;
        goto L_0x00c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: downloader.Dispatcher.transferData(java.net.HttpURLConnection, downloader.DownloadRequest):void");
    }

    private void updateStart(DownloadRequest request) {
        if (!request.isCancled()) {
            request.setState(State.Running);
            this.mDelivery.postStart(request);
        }
    }

    private void updateSuccess(DownloadRequest request, HttpURLConnection connection) {
        request.setState(State.Success);
        request.finish();
        File temp = request.getTempFile();
        if (temp.exists()) {
            temp.renameTo(new File(request.getDestPath()));
        }
        this.mDelivery.postSuccess(request);
    }

    private void updateProgress(DownloadRequest request) {
        request.setState(State.Running);
        if (!request.isCancled()) {
            this.mDelivery.postProgress(request);
        }
    }

    private int readFromInputStream(byte[] buffer, InputStream in) {
        try {
            return in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            if ("unexpected end of stream".equals(e.getMessage())) {
                return -1;
            }
            return MoPubClientPositioning.NO_REPEAT;
        }
    }

    public void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        this.mQuit = false;
        this.mThread.start();
    }

    public void quit() {
        this.mQuit = true;
        this.mThread.interrupt();
    }
}
