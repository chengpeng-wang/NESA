package com.xmpp.client.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class PushDialogModle {
    /* access modifiers changed from: private */
    public Context ctx;

    public PushDialogModle(Context context) {
        this.ctx = context;
    }

    public void PushAlertSingleButton(String title, String mess) {
        System.out.println("======title==========" + title);
        System.out.println("======mess==========" + mess);
    }

    public void showDialog(String title, String mess) {
        new Builder(this.ctx).setTitle(title).setMessage(mess).setPositiveButton("確定", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) PushDialogModle.this.ctx).finish();
            }
        }).show();
    }
}
