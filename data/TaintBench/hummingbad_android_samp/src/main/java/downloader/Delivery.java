package downloader;

public class Delivery {
    public void postFailure(DownloadRequest request, int code, String msg) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onFailed(request);
        }
    }

    public void postProgress(DownloadRequest request) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onProgress(request);
        }
    }

    public void postSuccess(DownloadRequest request) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onSuccess(request);
        }
    }

    public void postStart(DownloadRequest request) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onStart(request);
        }
    }

    public void postCancle(DownloadRequest request) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onCancle(request);
        }
    }

    public void postAddToDownloadQueue(DownloadRequest request) {
        DownloadListener listener = request.getListener();
        if (listener != null) {
            listener.onAddToDownloadQueue(request);
        }
    }
}
