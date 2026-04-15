package com.cmcm.baseapi;

import android.util.Log;

public class DummyLogger implements ILogger {
    private String mTagName;

    DummyLogger(String tagName) {
        this.mTagName = tagName;
    }

    public void v(String message) {
        Log.v(this.mTagName, message);
    }

    public void d(String message) {
        Log.d(this.mTagName, message);
    }

    public void i(String message) {
        Log.i(this.mTagName, message);
    }

    public void w(String message) {
        Log.w(this.mTagName, message);
    }

    public void e(String message) {
        Log.e(this.mTagName, message);
    }

    public void v(String message, Throwable throwable) {
        Log.v(this.mTagName, message, throwable);
    }

    public void d(String message, Throwable throwable) {
        Log.d(this.mTagName, message, throwable);
    }

    public void i(String message, Throwable throwable) {
        Log.i(this.mTagName, message, throwable);
    }

    public void w(String message, Throwable throwable) {
        Log.w(this.mTagName, message, throwable);
    }

    public void e(String message, Throwable throwable) {
        Log.e(this.mTagName, message, throwable);
    }
}
