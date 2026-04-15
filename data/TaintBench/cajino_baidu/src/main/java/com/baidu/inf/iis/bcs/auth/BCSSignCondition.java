package com.baidu.inf.iis.bcs.auth;

public class BCSSignCondition {
    private String ip = "";
    private Long size = Long.valueOf(0);
    private Long time = Long.valueOf(0);

    public String getIp() {
        return this.ip;
    }

    public Long getSize() {
        return this.size;
    }

    public Long getTime() {
        return this.time;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public void setSize(Long l) {
        this.size = l;
    }

    public void setTime(Long l) {
        this.time = l;
    }
}
