package com.splunk.mint;

import android.content.Context;

interface InterfaceDataType {
    void save(DataSaver dataSaver);

    void send(Context context, NetSender netSender, boolean z);

    void send(NetSender netSender, boolean z);

    String toJsonLine();
}
