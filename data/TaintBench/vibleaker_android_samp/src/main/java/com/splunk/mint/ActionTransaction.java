package com.splunk.mint;

import android.content.Context;
import java.util.HashMap;

abstract class ActionTransaction extends BaseDTO implements InterfaceDataType {
    protected String name = "";
    protected String transaction_id = "";

    protected ActionTransaction(String name, EnumActionType transactionType, HashMap<String, Object> customData) {
        super(transactionType, customData);
        this.name = name;
    }

    public String toJsonLine() {
        return "" + Properties.getSeparator(EnumActionType.trstart);
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }

    public void save(DataSaver dataSaver) {
        new DataSaver().save(toJsonLine());
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        netSender.send(toJsonLine(), saveOnFail);
    }
}
