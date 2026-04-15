package com.flurry.sdk;

import java.util.HashMap;
import java.util.Map;

public final class aq {
    private final Map a = new HashMap();

    public final synchronized ar a(String str) {
        ar arVar;
        as asVar = new as(str);
        arVar = (ar) this.a.get(asVar);
        if (arVar == null) {
            arVar = new ar();
            arVar.a = new gc(str);
            arVar.b = new am();
            this.a.put(asVar, arVar);
        }
        return arVar;
    }
}
