package com.flurry.sdk;

public enum jj {
    kUnknown,
    kGet,
    kPost,
    kPut,
    kDelete,
    kHead;

    public final String toString() {
        switch (ji.a[ordinal()]) {
            case 1:
                return "POST";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
            case 4:
                return "HEAD";
            case 5:
                return "GET";
            default:
                return null;
        }
    }
}
