package com.cmcm.adsdk.requestconfig.request;

import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.adsdk.requestconfig.request.RequestTask.ResultListener;
import java.util.HashSet;
import java.util.Set;

public final class RequestAction implements ResultListener {
    private static RequestAction b;
    public Set<RequestListener> a = new HashSet();

    public interface RequestListener {
        void onFailed(String str);

        void onSuccess(String str);
    }

    private RequestAction() {
    }

    public static synchronized RequestAction a() {
        RequestAction requestAction;
        synchronized (RequestAction.class) {
            if (b == null) {
                b = new RequestAction();
            }
            requestAction = b;
        }
        return requestAction;
    }

    public final void a(String str, String str2, RequestListener requestListener) {
        if (this.a != null) {
            synchronized (this.a) {
                this.a.add(requestListener);
            }
            new RequestTask(str, str2, this).execute(new Void[0]);
        }
    }

    public final void result(byte[] result) {
        synchronized (this.a) {
            if (this.a == null) {
                a.b("RequestAction", "mListener == null...");
            }
            if (result != null) {
                for (RequestListener requestListener : this.a) {
                    if (requestListener != null) {
                        requestListener.onSuccess(new String(result));
                        a.b("RequestAction", "onSuccess...");
                    }
                }
            } else {
                for (RequestListener requestListener2 : this.a) {
                    if (requestListener2 != null) {
                        requestListener2.onFailed(null);
                        a.b("RequestAction", "failed...");
                    }
                }
            }
        }
    }
}
