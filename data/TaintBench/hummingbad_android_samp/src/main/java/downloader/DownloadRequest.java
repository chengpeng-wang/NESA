package downloader;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadRequest implements Comparable<DownloadRequest> {
    private long downloadedSize;
    private boolean isRetry;
    private String mBasePath;
    private boolean mCancle;
    private Delivery mDelivery;
    private String mFileName;
    private DownloadListener mListener;
    private String mOriginalUrl;
    private Priority mPriority;
    private AtomicInteger mProgressiveRetryTimes;
    private DownloadQueue mQueue;
    private AtomicInteger mRedirectTimes;
    private State mState;
    private AtomicInteger mTotalRetryTimes;
    private String mUrl;
    private long totalSize;

    public enum Priority {
        Low,
        Normal,
        High
    }

    public enum State {
        Waiting,
        Running,
        Failed,
        Success,
        NotModify
    }

    public Delivery getDelivery() {
        return this.mDelivery;
    }

    public void setDelivery(Delivery delivery) {
        this.mDelivery = delivery;
    }

    public State getState() {
        return this.mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public Priority getPriority() {
        return this.mPriority;
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public DownloadListener getListener() {
        return this.mListener;
    }

    public void setListener(DownloadListener listener) {
        this.mListener = listener;
    }

    public DownloadRequest(String url) {
        this(url, null, null);
    }

    public DownloadRequest(String url, String name) {
        this(url, name, null);
    }

    public DownloadRequest(String url, DownloadListener listener) {
        this(url, null, listener);
    }

    public DownloadRequest(String url, String fileName, DownloadListener listener) {
        this.mProgressiveRetryTimes = new AtomicInteger(3);
        this.mTotalRetryTimes = new AtomicInteger(10);
        this.mRedirectTimes = new AtomicInteger(5);
        this.isRetry = false;
        this.mUrl = url;
        this.mOriginalUrl = url;
        this.mListener = listener;
        this.mPriority = Priority.Low;
        this.mState = State.Waiting;
        if (fileName == null) {
            fileName = Utils.md5(url) + ".apk";
        }
        this.mFileName = fileName;
    }

    public void cancle() {
        this.mCancle = true;
        this.mState = State.Waiting;
        this.mDelivery.postCancle(this);
    }

    public boolean isCancled() {
        return this.mCancle;
    }

    public void finish() {
        if (this.mQueue != null) {
            this.mQueue.finish(this);
        }
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getOriginalUrl() {
        return this.mOriginalUrl;
    }

    public void setDownloadQueue(DownloadQueue downloadQueue) {
        this.mQueue = downloadQueue;
    }

    public long getRange() {
        return getTempFile().length();
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public DownloadRequest setRetryTimes(int retryTimes) {
        this.mProgressiveRetryTimes = new AtomicInteger(retryTimes);
        return this;
    }

    public int getProgressiveRetryTimes() {
        this.isRetry = true;
        return this.mProgressiveRetryTimes.decrementAndGet();
    }

    public int getTotalFailedRetryTimes() {
        return this.mTotalRetryTimes.decrementAndGet();
    }

    public String getTempPath() {
        return this.mBasePath + File.separator + Utils.md5(getUrl()) + ".tmp";
    }

    public String getDestPath() {
        return this.mBasePath + File.separator + this.mFileName;
    }

    public File getTempFile() {
        return new File(getTempPath());
    }

    public File getDestFile() {
        return new File(getDestPath());
    }

    public void setBasePath(String dir) {
        this.mBasePath = dir;
    }

    public String getBasePath() {
        return this.mBasePath;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getDownloadedSize() {
        return this.downloadedSize;
    }

    public void setDownloadedSize(long downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public void deleteTmpFile() {
        if (getTempFile().exists()) {
            getTempFile().delete();
        }
    }

    public boolean isRetry() {
        return this.isRetry;
    }

    public int compareTo(DownloadRequest other) {
        if (this.mPriority.ordinal() == other.mPriority.ordinal()) {
            return 0;
        }
        return this.mPriority.ordinal() - other.mPriority.ordinal();
    }

    public int getRedirectTimes() {
        return this.mRedirectTimes.getAndDecrement();
    }
}
