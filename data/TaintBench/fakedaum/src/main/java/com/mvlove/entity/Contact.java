package com.mvlove.entity;

import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = -3025755556867054916L;
    private String name;
    private String number;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
