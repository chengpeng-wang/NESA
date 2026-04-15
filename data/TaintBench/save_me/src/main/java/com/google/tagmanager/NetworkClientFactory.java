package com.google.tagmanager;

import android.os.Build.VERSION;
import com.google.android.gms.common.util.VisibleForTesting;

class NetworkClientFactory {
    NetworkClientFactory() {
    }

    public NetworkClient createNetworkClient() {
        if (getSdkVersion() < 8) {
            return new HttpNetworkClient();
        }
        return new HttpUrlConnectionNetworkClient();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public int getSdkVersion() {
        return VERSION.SDK_INT;
    }
}
