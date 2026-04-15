package downloader;

public interface DownloadListener {
    void onAddToDownloadQueue(DownloadRequest downloadRequest);

    void onCancle(DownloadRequest downloadRequest);

    void onFailed(DownloadRequest downloadRequest);

    void onProgress(DownloadRequest downloadRequest);

    void onStart(DownloadRequest downloadRequest);

    void onSuccess(DownloadRequest downloadRequest);
}
