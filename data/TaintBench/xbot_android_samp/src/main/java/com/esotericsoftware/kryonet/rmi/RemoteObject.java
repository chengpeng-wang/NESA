package com.esotericsoftware.kryonet.rmi;

import com.esotericsoftware.kryonet.Connection;

public interface RemoteObject {
    void close();

    Connection getConnection();

    byte getLastResponseID();

    void setNonBlocking(boolean z);

    void setResponseTimeout(int i);

    void setTransmitExceptions(boolean z);

    void setTransmitReturnValue(boolean z);

    Object waitForLastResponse();

    Object waitForResponse(byte b);
}
