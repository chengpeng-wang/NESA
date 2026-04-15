package com.mb.bdapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import com.mb.bdapp.db.DBService;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.down.DownloadException;
import com.mb.bdapp.down.Downloader02;
import com.mb.bdapp.down.callback.RequestCallBack;
import com.mb.bdapp.noti.DowningNoti;
import java.io.File;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import org.apache.http.NoHttpResponseException;

public class DownUtils {
    public static final int NOTI_TYPE_DOWNING = 0;
    public static final int NOTI_TYPE_DOWN_ERROR = -1;
    /* access modifiers changed from: private|static|final */
    public static final String TAG = DownUtils.class.getSimpleName();
    /* access modifiers changed from: private|static */
    public static Downloader02 downloader = null;
    private static HashSet<Class<?>> exceptionWhiteList = new HashSet();

    static {
        exceptionWhiteList.add(NoHttpResponseException.class);
        exceptionWhiteList.add(UnknownHostException.class);
        exceptionWhiteList.add(SocketException.class);
        exceptionWhiteList.add(InterruptedIOException.class);
    }

    private static synchronized Downloader02 getDownloader() {
        Downloader02 downloader02;
        synchronized (DownUtils.class) {
            downloader02 = downloader;
        }
        return downloader02;
    }

    public static void startDownLoad(final Context context, final DuAd item) {
        if (!MobileInfoUtils.isConnectInternet(context) || getDownloader() != null) {
            return;
        }
        if (item == null) {
            LogUtil.e(TAG, "Incorrect data can't download， ad=null");
        } else if (TextUtils.isEmpty(item.getDownUrl())) {
            LogUtil.e(TAG, "download url data can't download，downurl=" + item.getDownUrl());
            DBService.getInstance(context).deleteAdById((long) item.get_ID());
        } else {
            final String fileName = new StringBuilder(String.valueOf(FileUtils.generateFileName(item.getDownUrl()))).append("h.apk").toString();
            downloader = new Downloader02(item.getDownUrl(), FileUtils.getFile(fileName).getAbsolutePath(), new RequestCallBack() {
                public void onStart() {
                    super.onStart();
                    LogUtil.d(DownUtils.TAG, "onStart()");
                }

                public void onLoading(long total, long current) {
                    super.onLoading(total, current);
                    DownUtils.showNotify(context, item, (int) ((100 * current) / total), 0);
                }

                public void onSuccess(File responseInfo) {
                    LogUtil.d(DownUtils.TAG, "onSuccess -->>");
                    DownUtils.downloader = null;
                    if (!DownUtils.handleSuccess(context, item, responseInfo)) {
                        responseInfo.delete();
                        DownUtils.handleError(context, item, new DownloadException("下载包错误！重新下载"));
                    }
                }

                public void onFailure(DownloadException exception, String msg) {
                    LogUtil.d(DownUtils.TAG, "onFailure -->>" + exception.getLocalizedMessage() + " code=" + exception.getExceptionCode());
                    DownUtils.downloader = null;
                    File file = FileUtils.getFile(fileName);
                    if (exception.getExceptionCode() == 416 && file.exists()) {
                        if (!DownUtils.handleSuccess(context, item, file)) {
                            file.delete();
                        } else {
                            return;
                        }
                    }
                    DownUtils.handleError(context, item, exception);
                }
            });
            new Thread(downloader).start();
            DBService.getInstance(context).updateAdStatusById((long) item.get_ID(), 3);
            item.getDownRetry();
        }
    }

    public static synchronized void cancelDownloader() {
        synchronized (DownUtils.class) {
            if (downloader != null) {
                downloader.cancel();
                downloader = null;
            }
        }
    }

    public static void showNotify(final Context context, final DuAd item, final int progress, final int type) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            public void run() {
                try {
                    new DowningNoti(context, item, progress, type).showNotify();
                } catch (Exception e) {
                    LogUtil.e(DownUtils.TAG, e.getLocalizedMessage());
                }
            }
        });
    }

    public static void handleError(Context context, DuAd item, DownloadException exception) {
        if (!exceptionWhiteList.contains(exception.getClass())) {
            int retry = item.getDownRetry() + 1;
            item.setDownRetry(retry);
            DBService.getInstance(context).updateAdDownRetryById((long) item.get_ID(), retry);
        }
        if (item.getDownRetry() > 5) {
            File file = FileUtils.getFile(new StringBuilder(String.valueOf(FileUtils.generateFileName(item.getDownUrl()))).append("h.apk").toString());
            if (file.exists()) {
                file.delete();
            }
            DBService.getInstance(context).deleteAdById((long) item.get_ID());
        }
    }

    public static boolean handleSuccess(Context context, DuAd item, File file) {
        if (!renameDownloadFile(context, item, file)) {
            return false;
        }
        item.setStatus(4);
        DBService.getInstance(context).updateAdStatusById((long) item.get_ID(), 4);
        sendBroadcast(context, item);
        return true;
    }

    private static void sendBroadcast(Context context, DuAd item) {
        Intent intent = new Intent();
        intent.setAction(Constants.INSTALL_DUAD);
        context.sendBroadcast(intent);
    }

    private static boolean renameDownloadFile(Context context, DuAd item, File from) {
        File to = FileUtils.getFile(item.getPname() + ".apk");
        if (to.exists()) {
            to.delete();
        }
        return from.renameTo(to);
    }
}
