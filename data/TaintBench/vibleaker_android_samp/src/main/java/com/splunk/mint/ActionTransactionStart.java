package com.splunk.mint;

import android.content.Context;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

class ActionTransactionStart extends ActionTransaction implements InterfaceDataType {
    private ActionTransactionStart(String name, HashMap<String, Object> customData) {
        super(name, EnumActionType.trstart, customData);
        this.transaction_id = Utils.getRandomSessionNumber();
        if (!Properties.transactions.contains(this.transaction_id)) {
            Properties.transactions.add(this.transaction_id);
        }
    }

    public static ActionTransactionStart createTransactionStart(String name, HashMap<String, Object> customData) {
        ActionTransactionStart mTransactionStart = new ActionTransactionStart(name, customData);
        Properties.transactionsDatabase.addStartedTransaction(mTransactionStart);
        return mTransactionStart;
    }

    public String toJsonLine() {
        JSONObject json = getBasicDataFixtureJson();
        try {
            json.put("tr_name", this.name);
            json.put("transaction_id", this.transaction_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString() + Properties.getSeparator(EnumActionType.trstart);
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
