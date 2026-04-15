package com.mobclick.android;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class i implements OnClickListener {
    i() {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
