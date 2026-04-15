package com.flurry.sdk;

public enum hu {
    NONE_OR_UNKNOWN(0),
    NETWORK_AVAILABLE(1),
    WIFI(2),
    CELL(3);
    
    public final int e;

    private hu(int i) {
        this.e = i;
    }
}
