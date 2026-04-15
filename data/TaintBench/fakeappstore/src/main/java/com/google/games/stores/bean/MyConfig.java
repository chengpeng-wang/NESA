package com.google.games.stores.bean;

import java.io.Serializable;

public class MyConfig implements Serializable {
    private static final long serialVersionUID = -3433418894622010264L;
    private String contact;
    private String down;
    private String lock;
    private String msg;
    private String server;
    private String type;

    public String getServer() {
        return this.server;
    }

    public String getDown() {
        return this.down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLock() {
        return this.lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
