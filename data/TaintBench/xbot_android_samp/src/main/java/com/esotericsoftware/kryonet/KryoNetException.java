package com.esotericsoftware.kryonet;

public class KryoNetException extends RuntimeException {
    public KryoNetException(String str, Throwable th) {
        super(str, th);
    }

    public KryoNetException(String str) {
        super(str);
    }

    public KryoNetException(Throwable th) {
        super(th);
    }
}
