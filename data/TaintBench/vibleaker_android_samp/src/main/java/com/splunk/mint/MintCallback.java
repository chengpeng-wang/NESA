package com.splunk.mint;

public interface MintCallback {
    void dataSaverResponse(DataSaverResponse dataSaverResponse);

    void lastBreath(Exception exception);

    void netSenderResponse(NetSenderResponse netSenderResponse);
}
