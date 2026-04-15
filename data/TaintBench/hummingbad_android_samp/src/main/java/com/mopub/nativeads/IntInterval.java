package com.mopub.nativeads;

import android.support.annotation.Nullable;

public class IntInterval implements Comparable<IntInterval> {
    private int length;
    private int start;

    public IntInterval(int start, int length) {
        this.start = start;
        this.length = length;
    }

    public int getStart() {
        return this.start;
    }

    public int getLength() {
        return this.length;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean equals(int start, int length) {
        return this.start == start && this.length == length;
    }

    public String toString() {
        return "{start : " + this.start + ", length : " + this.length + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntInterval)) {
            return false;
        }
        IntInterval intInterval = (IntInterval) o;
        if (this.start == intInterval.start && this.length == intInterval.length) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.start + 899) * 31) + this.length;
    }

    public int compareTo(@Nullable IntInterval another) {
        if (this.start == another.start) {
            return this.length - another.length;
        }
        return this.start - another.start;
    }
}
