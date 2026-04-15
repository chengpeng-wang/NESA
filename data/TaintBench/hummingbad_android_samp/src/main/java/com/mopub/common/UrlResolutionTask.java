package com.mopub.common;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.util.AsyncTasks;
import java.io.IOException;

@VisibleForTesting
public class UrlResolutionTask extends AsyncTask<String, Void, String> {
    private static final int REDIRECT_LIMIT = 10;
    @NonNull
    private final UrlResolutionListener mListener;

    interface UrlResolutionListener {
        void onFailure(@NonNull String str, @Nullable Throwable th);

        void onSuccess(@NonNull String str);
    }

    public static void getResolvedUrl(@NonNull String urlString, @NonNull UrlResolutionListener listener) {
        try {
            AsyncTasks.safeExecuteOnExecutor(new UrlResolutionTask(listener), urlString);
        } catch (Exception e) {
            listener.onFailure("Failed to resolve url", e);
        }
    }

    UrlResolutionTask(@NonNull UrlResolutionListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected|varargs */
    @Nullable
    public String doInBackground(@Nullable String... urls) {
        if (urls == null || urls.length == 0) {
            return null;
        }
        try {
            int i = 0;
            String str = urls[0];
            String str2 = null;
            while (str != null && i < 10) {
                if (!UrlAction.OPEN_IN_APP_BROWSER.shouldTryHandlingUrl(Uri.parse(str))) {
                    return str;
                }
                i++;
                str2 = str;
                str = getRedirectLocation(str);
            }
            return str2;
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033  */
    @android.support.annotation.Nullable
    private java.lang.String getRedirectLocation(@android.support.annotation.NonNull java.lang.String r6) throws java.io.IOException {
        /*
        r5 = this;
        r1 = 0;
        r0 = new java.net.URL;
        r0.<init>(r6);
        r0 = r0.openConnection();	 Catch:{ all -> 0x0030 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ all -> 0x0030 }
        r2 = 0;
        r0.setInstanceFollowRedirects(r2);	 Catch:{ all -> 0x0037 }
        r2 = r0.getResponseCode();	 Catch:{ all -> 0x0037 }
        r3 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r2 < r3) goto L_0x0029;
    L_0x0018:
        r3 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r2 >= r3) goto L_0x0029;
    L_0x001c:
        r1 = "Location";
        r1 = r0.getHeaderField(r1);	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0027;
    L_0x0024:
        r0.disconnect();
    L_0x0027:
        r0 = r1;
    L_0x0028:
        return r0;
    L_0x0029:
        if (r0 == 0) goto L_0x002e;
    L_0x002b:
        r0.disconnect();
    L_0x002e:
        r0 = r1;
        goto L_0x0028;
    L_0x0030:
        r0 = move-exception;
    L_0x0031:
        if (r1 == 0) goto L_0x0036;
    L_0x0033:
        r1.disconnect();
    L_0x0036:
        throw r0;
    L_0x0037:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.common.UrlResolutionTask.getRedirectLocation(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(@Nullable String resolvedUrl) {
        super.onPostExecute(resolvedUrl);
        if (isCancelled() || resolvedUrl == null) {
            onCancelled();
        } else {
            this.mListener.onSuccess(resolvedUrl);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        super.onCancelled();
        this.mListener.onFailure("Task for resolving url was cancelled", null);
    }
}
