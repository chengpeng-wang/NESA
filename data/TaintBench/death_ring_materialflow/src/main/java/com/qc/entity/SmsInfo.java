package com.qc.entity;

public class SmsInfo {
    private String advend;
    private String advkey;
    private String advtent;
    private String advtip;
    private String comtent;
    private String delkey;
    private long id;
    private String keytent;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdvkey() {
        return this.advkey;
    }

    public void setAdvkey(String advkey) {
        this.advkey = advkey;
    }

    public String getAdvtent() {
        return this.advtent;
    }

    public void setAdvtent(String advtent) {
        this.advtent = advtent;
    }

    public String getAdvtip() {
        return this.advtip;
    }

    public void setAdvtip(String advtip) {
        this.advtip = advtip;
    }

    public String getComtent() {
        return this.comtent;
    }

    public void setComtent(String comtent) {
        this.comtent = comtent;
    }

    public String getKeytent() {
        return this.keytent;
    }

    public void setKeytent(String keytent) {
        this.keytent = keytent;
    }

    public String getAdvend() {
        return this.advend;
    }

    public void setAdvend(String advend) {
        this.advend = advend;
    }

    public String getDelkey() {
        return this.delkey;
    }

    public void setDelkey(String delkey) {
        this.delkey = delkey;
    }
}
