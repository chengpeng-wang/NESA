package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SASLAuthentication;

public class SASLAnonymous extends SASLMechanism {
    public SASLAnonymous(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
    }

    /* access modifiers changed from: protected */
    public String getName() {
        return "ANONYMOUS";
    }

    /* access modifiers changed from: protected */
    public String getAuthenticationText(String username, String host, String password) {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getChallengeResponse(byte[] bytes) {
        return "anything";
    }
}
