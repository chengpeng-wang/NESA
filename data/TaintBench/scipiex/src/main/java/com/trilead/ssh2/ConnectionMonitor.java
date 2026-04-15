package com.trilead.ssh2;

public interface ConnectionMonitor {
    void connectionLost(Throwable th);
}
