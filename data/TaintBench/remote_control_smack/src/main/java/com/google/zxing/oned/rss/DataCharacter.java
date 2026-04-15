package com.google.zxing.oned.rss;

public class DataCharacter {
    private final int checksumPortion;
    private final int value;

    public DataCharacter(int value, int checksumPortion) {
        this.value = value;
        this.checksumPortion = checksumPortion;
    }

    public int getValue() {
        return this.value;
    }

    public int getChecksumPortion() {
        return this.checksumPortion;
    }
}
