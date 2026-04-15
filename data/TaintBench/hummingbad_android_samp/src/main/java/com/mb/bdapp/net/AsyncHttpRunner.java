package com.mb.bdapp.net;

import com.mb.bdapp.util.LogUtil;

public class AsyncHttpRunner {
    protected static final String TAG = AsyncHttpRunner.class.getSimpleName();

    public static void request(final String url, final HttpParameters params, final String httpMethod, final RequestListener listener) {
        new Thread() {
            public void run() {
                try {
                    String resp = HttpManager.openUrl(url, httpMethod, params);
                    LogUtil.d(AsyncHttpRunner.TAG, "resp=" + resp);
                    if (listener != null) {
                        listener.onComplete(resp);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }.start();
    }
}
