package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SASLAuthentication;

public class SASLPlainMechanism extends SASLMechanism {
    public SASLPlainMechanism(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
    }

    /* access modifiers changed from: protected */
    public String getName() {
        return "PLAIN";
    }

    /* access modifiers changed from: protected */
    public String getAuthenticationText(String username, String host, String password) {
        StringBuilder text = new StringBuilder();
        text.append(0);
        text.append(username);
        text.append(0);
        text.append(password);
        return text.toString();
    }

    /* access modifiers changed from: protected */
    public String getChallengeResponse(byte[] bytes) {
        return null;
    }
}
