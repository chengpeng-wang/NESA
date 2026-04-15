package com.baidu.mapapi;

import android.os.Handler;
import android.os.Message;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.utils.PermissionCheck;

class a extends Handler {
    final /* synthetic */ BMapManager a;

    a(BMapManager bMapManager) {
        this.a = bMapManager;
    }

    public void handleMessage(Message message) {
        if (message.what == 2010) {
            switch (message.arg2) {
                case 0:
                case 2:
                    return;
                case 1:
                case 4:
                    PermissionCheck.check();
                    if (!this.a.h && this.a.a != null) {
                        this.a.h = true;
                        this.a.a.onGetNetworkState(2);
                        return;
                    }
                    return;
                default:
                    if (this.a.a != null) {
                        this.a.a.onGetPermissionState(MKEvent.ERROR_PERMISSION_DENIED);
                        return;
                    }
                    return;
            }
        } else if (message.what != 65289) {
            if (message.arg2 == 3 && this.a.a != null) {
                this.a.a.onGetNetworkState(3);
            }
            if ((message.arg2 == 2 || message.arg2 == 404 || message.arg2 == 5) && this.a.a != null) {
                this.a.a.onGetNetworkState(2);
            }
        }
    }
}
