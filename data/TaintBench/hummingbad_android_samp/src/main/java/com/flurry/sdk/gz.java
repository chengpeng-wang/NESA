package com.flurry.sdk;

public class gz {
    private static final String a = gz.class.getSimpleName();

    public static void a(af afVar) {
        if (afVar != null) {
            iw.a(3, a, "Firing onFetched, adObject=" + afVar);
            d dVar = new d();
            dVar.a = afVar;
            dVar.b = e.kOnFetched;
            dVar.a();
        }
    }

    public static void a(af afVar, df dfVar) {
        if (afVar != null && dfVar != null) {
            iw.a(3, a, "Firing onFetchFailed, adObject=" + afVar + ", errorCode=" + dfVar);
            d dVar = new d();
            dVar.a = afVar;
            dVar.b = e.kOnFetchFailed;
            dVar.c = dfVar;
            dVar.a();
        }
    }
}
