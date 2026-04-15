package com.flurry.sdk;

import java.util.List;

public final class el {
    public long a;
    public String b;
    public List c;

    public final String toString() {
        return "\n { \n sessionId " + this.a + ",\n adLogGUID " + this.b + ",\n sdkAdEvents " + this.c + "\n } \n";
    }
}
