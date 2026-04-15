package com.mobclick.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

class b extends Handler {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void handleMessage(Message message) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse("file://" + this.a.c + "/" + this.a.d), "application/vnd.android.package-archive");
            this.a.a.startActivity(intent);
        } catch (Exception e) {
            Log.e("can not install", e.getMessage());
            this.a.j = false;
        }
    }
}
