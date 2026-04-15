package downloader;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

public class DownloadQueue {
    private static DownloadQueue sInstance;
    private Set<DownloadRequest> mAllRequest = new HashSet();
    private Delivery mDelivery = new Delivery();
    private Dispatcher[] mDispatchers;
    private String mPath;
    private PriorityBlockingQueue<DownloadRequest> mWaitingQueue = new PriorityBlockingQueue(20);

    public static DownloadQueue instance() {
        if (sInstance == null) {
            sInstance = new DownloadQueue();
        }
        return sInstance;
    }

    private DownloadQueue() {
    }

    public DownloadQueue init(int poolSize) {
        quit();
        if (this.mDispatchers == null) {
            this.mDispatchers = new Dispatcher[poolSize];
            for (int i = 0; i < this.mDispatchers.length; i++) {
                this.mDispatchers[i] = new Dispatcher(this.mWaitingQueue, this.mDelivery);
                this.mDispatchers[i].setIntervalDownloadPercent(0.02f);
                this.mDispatchers[i].start();
            }
            return this;
        }
        throw new RuntimeException("download queue already init");
    }

    private void quit() {
        if (this.mDispatchers != null) {
            synchronized (this.mDispatchers) {
                for (Dispatcher dispatcher : this.mDispatchers) {
                    if (dispatcher != null) {
                        dispatcher.quit();
                    }
                }
            }
        }
    }

    public int add(DownloadRequest request) {
        if (this.mDispatchers == null) {
            throw new RuntimeException("download queue have't init.");
        }
        int ret = -1;
        if (query(request.getUrl()) == null) {
            synchronized (this.mAllRequest) {
                this.mAllRequest.add(request);
                ret = 1;
            }
            request.setDownloadQueue(this);
            request.setDelivery(this.mDelivery);
            this.mWaitingQueue.add(request);
            this.mDelivery.postAddToDownloadQueue(request);
        }
        return ret;
    }

    public boolean cancle(String url) {
        synchronized (this.mAllRequest) {
            for (DownloadRequest req : this.mAllRequest) {
                if (url.equals(req.getUrl())) {
                    req.cancle();
                    return true;
                }
            }
            return false;
        }
    }

    public void cancleAll() {
        synchronized (this.mAllRequest) {
            for (DownloadRequest req : this.mAllRequest) {
                req.cancle();
            }
        }
        this.mAllRequest.clear();
    }

    public DownloadRequest query(String url) {
        DownloadRequest request = null;
        synchronized (this.mAllRequest) {
            for (DownloadRequest req : this.mAllRequest) {
                if (url.equals(req.getUrl())) {
                    request = req;
                    break;
                }
            }
        }
        return request;
    }

    public void finit() {
        cancleAll();
        if (this.mWaitingQueue != null) {
            this.mWaitingQueue.clear();
            this.mWaitingQueue = null;
        }
        if (this.mDispatchers != null) {
            quit();
            for (int i = 0; i < this.mDispatchers.length; i++) {
                this.mDispatchers[i] = null;
            }
            this.mDispatchers = null;
        }
    }

    public int getActiveRequestCounts() {
        return this.mAllRequest.size();
    }

    public Set<DownloadRequest> getActiveRequest() {
        return this.mAllRequest;
    }

    public void finish(DownloadRequest request) {
        synchronized (this.mAllRequest) {
            this.mAllRequest.remove(request);
        }
    }

    public void setDownloadPath(String path) {
        this.mPath = path;
    }

    public String getDownloadPath() {
        return this.mPath;
    }
}
