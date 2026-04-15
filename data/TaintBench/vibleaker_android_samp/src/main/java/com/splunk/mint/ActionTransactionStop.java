package com.splunk.mint;

import android.content.Context;
import com.splunk.mint.TransactionsDatabase.Container;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

class ActionTransactionStop extends ActionTransaction implements InterfaceDataType {
    protected long duration = 0;
    protected String reason = "";
    protected EnumTransactionStatus status = EnumTransactionStatus.FAIL;

    private ActionTransactionStop(String name, EnumTransactionStatus status, String reason, HashMap<String, Object> customData) {
        super(name, EnumActionType.trstop, customData);
        this.status = status;
        this.reason = reason;
        if (reason == null || reason.length() == 0) {
            this.reason = "NA";
        }
        Container container = Properties.transactionsDatabase.getStartedTransactionContainer(name);
        if (container != null) {
            this.transaction_id = container.transid;
            long timestampStart = container.timestamp.longValue();
            if (timestampStart != -1) {
                this.duration = this.timestampMilis.longValue() - timestampStart;
            }
        } else {
            this.transaction_id = null;
        }
        Properties.transactionsDatabase.closeStartedTransaction(name);
    }

    protected static final ActionTransactionStop createTransactionStop(String name, HashMap<String, Object> customData) {
        return new ActionTransactionStop(name, EnumTransactionStatus.SUCCESS, null, customData);
    }

    protected static final ActionTransactionStop createTransactionCancel(String name, String reason, HashMap<String, Object> customData) {
        return new ActionTransactionStop(name, EnumTransactionStatus.CANCEL, reason, customData);
    }

    protected static final ActionTransactionStop createTransactionFail(String name, String errorHash, HashMap<String, Object> customData) {
        return new ActionTransactionStop(name, EnumTransactionStatus.FAIL, errorHash, customData);
    }

    public String toJsonLine() {
        if (this.transaction_id == null) {
            return null;
        }
        JSONObject json = getBasicDataFixtureJson();
        try {
            json.put("tr_name", this.name);
            json.put("status", this.status.toString());
            json.put("reason", this.reason);
            json.put("transaction_id", this.transaction_id);
            json.put("tr_duration", String.valueOf(this.duration));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Properties.transactions.contains(this.transaction_id)) {
            Properties.transactions.remove(this.transaction_id);
        }
        return json.toString() + Properties.getSeparator(EnumActionType.trstop);
    }

    public void send(NetSender netSender, boolean saveOnFail) {
        String trData = toJsonLine();
        if (trData != null) {
            netSender.send(trData, saveOnFail);
        }
    }

    public void save(DataSaver dataSaver) {
        String trData = toJsonLine();
        if (trData != null) {
            new DataSaver().save(trData);
        }
    }

    public void send(Context ctx, NetSender netSender, boolean saveOnFail) {
        String trData = toJsonLine();
        if (trData != null) {
            netSender.send(trData, saveOnFail);
        }
    }
}
