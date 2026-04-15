package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.server.AdPlacementType;

public enum g {
    ANBANNER(h.class, f.AN, AdPlacementType.BANNER),
    ANINTERSTITIAL(i.class, f.AN, AdPlacementType.INTERSTITIAL),
    ANNATIVE(j.class, f.AN, AdPlacementType.NATIVE);
    
    public Class<?> d;
    public String e;
    public f f;
    public AdPlacementType g;

    private g(Class<?> cls, f fVar, AdPlacementType adPlacementType) {
        this.d = cls;
        this.f = fVar;
        this.g = adPlacementType;
    }
}
