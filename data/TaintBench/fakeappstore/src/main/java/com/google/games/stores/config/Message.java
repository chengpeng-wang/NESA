package com.google.games.stores.config;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 7286774204269353932L;
    private String address;
    private String content;
    private String inout;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInout() {
        return this.inout;
    }

    public void setInout(String inout) {
        this.inout = inout;
    }
}
