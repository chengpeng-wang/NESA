package install.app;

import java.util.Vector;

class SmsOperator {
    public Vector<String> codes;
    public int id;
    public int maxSmsCost;
    public int maxSmsCount;
    public String name;
    public int repeat;
    public Vector<SmsItem> sms;
    public long time;

    public SmsOperator() {
        this.repeat = 0;
        this.time = 0;
        this.id = 0;
        this.name = "";
        this.codes = new Vector();
        this.sms = new Vector();
        this.maxSmsCount = 0;
        this.maxSmsCost = 0;
    }

    public SmsOperator(Integer id) {
        this.id = id.intValue();
        this.name = "";
        this.codes = new Vector();
        this.sms = new Vector();
    }

    public String toString() {
        return "";
    }
}
