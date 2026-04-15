package com.flurry.sdk;

import java.util.List;

public final class em {
    public String a;
    public List b;
    public List c;
    public long d;
    public String e;
    public boolean f;

    public final String toString() {
        return "\n { \n apiKey " + this.a + ",\n adReportedIds " + this.b + ",\n sdkAdLogs " + this.c + ",\n agentTimestamp " + this.d + ",\n agentVersion " + this.e + ",\n testDevice " + this.f + "\n } \n";
    }
}
