package downloader;

public class SimpleDownloadListener implements DownloadListener {
    public void onStart(DownloadRequest request) {
    }

    public void onFailed(DownloadRequest request) {
        onFinished(request);
    }

    public void onProgress(DownloadRequest request) {
    }

    public void onSuccess(DownloadRequest request) {
        onFinished(request);
    }

    public void onCancle(DownloadRequest request) {
        onFinished(request);
    }

    public void onFinished(DownloadRequest request) {
    }

    public void onAddToDownloadQueue(DownloadRequest request) {
    }
}
