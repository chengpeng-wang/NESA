package com.sun.mail.iap;

public class ConnectionException extends ProtocolException {
    private static final long serialVersionUID = 5749739604257464727L;
    private transient Protocol p;

    public ConnectionException(String s) {
        super(s);
    }

    public ConnectionException(Protocol p, Response r) {
        super(r);
        this.p = p;
    }

    public Protocol getProtocol() {
        return this.p;
    }
}
