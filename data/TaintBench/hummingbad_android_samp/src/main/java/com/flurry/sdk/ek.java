package com.flurry.sdk;

import java.util.Map;

public final class ek {
    public String a;
    public Map b;
    public long c;

    public final String toString() {
        return "\n { \n type " + this.a + ",\n params " + this.b + ",\n timeOffset " + this.c + "\n } \n";
    }
}
