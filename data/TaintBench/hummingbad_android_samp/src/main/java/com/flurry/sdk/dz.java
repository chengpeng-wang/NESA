package com.flurry.sdk;

public final class dz {
    public String a;
    public int b;
    public int c;
    public int d;

    public final String toString() {
        return "\n { \nsdkAssetUrl " + this.a + ",\n cacheSizeMb " + this.b + ",\n maxAssetSizeKb " + this.c + ",\n maxBitRateKbps " + this.d + "\n } \n";
    }
}
