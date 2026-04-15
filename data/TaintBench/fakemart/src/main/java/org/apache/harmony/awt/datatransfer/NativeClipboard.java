package org.apache.harmony.awt.datatransfer;

import java.awt.datatransfer.Clipboard;

public abstract class NativeClipboard extends Clipboard {
    protected static final int OPS_TIMEOUT = 10000;

    public NativeClipboard(String name) {
        super(name);
    }

    public void onShutdown() {
    }

    public void onRestart() {
    }
}
