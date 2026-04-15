package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class TaskBarView extends Activity {
    Context azz = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, CHECKUPD.class));
        finish();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Intent App = new Intent(this.azz, TaskBarView.class);
        App.addFlags(268435456);
        this.azz.startActivity(App);
    }
}
