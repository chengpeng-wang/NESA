package com.baidu.mapapi.search;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

final class f extends Handler {
    f() {
    }

    public void handleMessage(Message message) {
        j jVar;
        switch (message.what) {
            case 1:
                jVar = (j) message.obj;
                if (PlaceCaterActivity.c != null) {
                    PlaceCaterActivity.c.setImageBitmap(jVar.a());
                    return;
                }
                return;
            case 2:
                jVar = (j) message.obj;
                if (PlaceCaterActivity.q != null) {
                    ImageView imageView = (ImageView) PlaceCaterActivity.q.get(Integer.valueOf(message.arg1));
                    if (imageView != null) {
                        imageView.setImageBitmap(jVar.a());
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }
}
