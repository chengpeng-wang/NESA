package com.cleanmaster.gaid;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GooglePlayServiceConnection implements ServiceConnection {
    boolean CQ = false;
    private final BlockingQueue CR = new LinkedBlockingQueue();

    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            this.CR.put(service);
        } catch (InterruptedException e) {
        }
    }

    public void onServiceDisconnected(ComponentName name) {
    }

    public IBinder getConnectedBinder() {
        if (this.CQ) {
            throw new IllegalStateException();
        }
        this.CQ = true;
        return (IBinder) this.CR.take();
    }
}
