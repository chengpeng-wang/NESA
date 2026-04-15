package downloader;

import downloader.DownloadRequest.State;

public class DownloadManager {
    public static DownloadManager sInstance;
    private DownloadQueue mQueue;

    private DownloadManager() {
    }

    public static DownloadManager instance() {
        if (sInstance == null) {
            sInstance = new DownloadManager();
        }
        return sInstance;
    }

    public void init(int poolSize) {
        this.mQueue = DownloadQueue.instance();
        this.mQueue.init(poolSize);
    }

    public void finit() {
        this.mQueue.finit();
    }

    public int add(DownloadRequest req) {
        return add(req, false);
    }

    public int add(DownloadRequest req, boolean refresh) {
        req.setDownloadQueue(this.mQueue);
        req.setBasePath(this.mQueue.getDownloadPath());
        if (refresh) {
            req.finish();
            if (req.getTempFile().exists()) {
                req.getTempFile().delete();
            }
            if (req.getDestFile().exists()) {
                req.getDestFile().delete();
            }
            this.mQueue.add(req);
            return 0;
        } else if (req.getDestFile().exists()) {
            DownloadListener listener = req.getListener();
            if (listener == null) {
                return 0;
            }
            req.setState(State.NotModify);
            long length = req.getDestFile().length();
            req.setDownloadedSize(length);
            req.setTotalSize(length);
            listener.onSuccess(req);
            return 0;
        } else if (this.mQueue.query(req.getUrl()) != null) {
            return -1;
        } else {
            this.mQueue.add(req);
            return 0;
        }
    }

    public void stop(DownloadRequest req) {
        req.finish();
    }
}
