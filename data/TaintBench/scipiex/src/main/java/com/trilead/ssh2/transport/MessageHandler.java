package com.trilead.ssh2.transport;

import java.io.IOException;

public interface MessageHandler {
    void handleMessage(byte[] bArr, int i) throws IOException;
}
