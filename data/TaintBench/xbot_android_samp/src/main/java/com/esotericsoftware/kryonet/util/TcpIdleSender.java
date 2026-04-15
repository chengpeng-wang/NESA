package com.esotericsoftware.kryonet.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class TcpIdleSender extends Listener {
    boolean started;

    public abstract Object next();

    public void idle(Connection connection) {
        if (!this.started) {
            this.started = true;
            start();
        }
        Object next = next();
        if (next == null) {
            connection.removeListener(this);
        } else {
            connection.sendTCP(next);
        }
    }

    /* access modifiers changed from: protected */
    public void start() {
    }
}
