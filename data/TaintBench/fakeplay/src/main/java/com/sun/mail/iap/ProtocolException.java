package com.sun.mail.iap;

public class ProtocolException extends Exception {
    private static final long serialVersionUID = -4360500807971797439L;
    protected transient Response response = null;

    public ProtocolException(String s) {
        super(s);
    }

    public ProtocolException(Response r) {
        super(r.toString());
        this.response = r;
    }

    public Response getResponse() {
        return this.response;
    }
}
