package com.baidu.android.pushservice.richmedia;

import org.apache.http.client.methods.HttpGet;

public class p {
    public static n a(o oVar, String str) {
        n nVar = new n();
        nVar.a(oVar);
        switch (q.a[oVar.ordinal()]) {
            case 1:
                nVar.b(str);
                nVar.a(HttpGet.METHOD_NAME);
                return nVar;
            default:
                throw new IllegalArgumentException("illegal request type " + oVar);
        }
    }
}
