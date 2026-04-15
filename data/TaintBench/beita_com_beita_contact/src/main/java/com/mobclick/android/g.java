package com.mobclick.android;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class g implements OnClickListener {
    g() {
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
