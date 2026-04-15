package com.savemebeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 5000;
    Context azz = this;
    Boolean conx = Boolean.valueOf(false);
    Context ctx2 = this;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTitle("Search Me");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ConnectivityManager con_manager = (ConnectivityManager) SplashScreen.this.ctx2.getSystemService("connectivity");
                if (con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected()) {
                    SplashScreen.this.conx = Boolean.valueOf(true);
                } else {
                    SplashScreen.this.conx = Boolean.valueOf(false);
                }
                if (SplashScreen.this.conx.booleanValue()) {
                    SplashScreen.this.startService(new Intent(SplashScreen.this, CHECKUPD.class));
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, thanks.class));
                    SplashScreen.this.addShortcut();
                    return;
                }
                SplashScreen.this.startService(new Intent(SplashScreen.this, CHECKUPD.class));
                Toast.makeText(SplashScreen.this, "ERROR CONNEXION , CHECK YOUR NETWORK !!", 1).show();
                SplashScreen.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }

    /* access modifiers changed from: private */
    public void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), SplashScreen.class);
        shortcutIntent.setAction("android.intent.action.MAIN");
        Intent addIntent = new Intent();
        addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        addIntent.putExtra("android.intent.extra.shortcut.NAME", "Save Me");
        addIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.savemelg));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}
