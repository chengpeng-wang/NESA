package com.splunk.mint.network;

import java.io.Serializable;

public abstract class Metric<T extends Serializable> {
    private final String name;

    public abstract T getValue();

    public Metric(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
