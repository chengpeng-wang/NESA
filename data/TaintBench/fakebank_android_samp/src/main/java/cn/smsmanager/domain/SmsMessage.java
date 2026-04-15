package cn.smsmanager.domain;

public class SmsMessage {
    private int _id;
    private String address;
    private String body;
    private String date;
    private int read;
    private String sim_no;
    private int thread_id;
    private int type;

    public String toString() {
        return "sim_no=" + this.sim_no + ",_id=" + this._id + ",thread_id=" + this.thread_id + ",address=" + this.address + ",date=" + this.date + ",body=" + this.body + ",read=" + this.read + ",type=" + this.type;
    }

    public String getSim_no() {
        return this.sim_no;
    }

    public void setSim_no(String sim_no) {
        this.sim_no = sim_no;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getThread_id() {
        return this.thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRead() {
        return this.read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
