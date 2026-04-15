package com.mvlove;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App instance;

    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static final Context getAppContext() {
        return instance.getApplicationContext();
    }
}
