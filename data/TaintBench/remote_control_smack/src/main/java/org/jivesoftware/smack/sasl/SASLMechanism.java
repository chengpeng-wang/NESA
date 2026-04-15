package org.jivesoftware.smack.sasl;

import java.io.IOException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.util.StringUtils;

public abstract class SASLMechanism {
    private SASLAuthentication saslAuthentication;

    public abstract String getAuthenticationText(String str, String str2, String str3);

    public abstract String getChallengeResponse(byte[] bArr);

    public abstract String getName();

    public SASLMechanism(SASLAuthentication saslAuthentication) {
        this.saslAuthentication = saslAuthentication;
    }

    public void authenticate(String username, String host, String password) throws IOException {
        StringBuilder stanza = new StringBuilder();
        stanza.append("<auth mechanism=\"").append(getName());
        stanza.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        String authenticationText = getAuthenticationText(username, host, password);
        if (authenticationText != null) {
            stanza.append(StringUtils.encodeBase64(authenticationText));
        }
        stanza.append("</auth>");
        getSASLAuthentication().send(stanza.toString());
    }

    public void challengeReceived(String challenge) throws IOException {
        StringBuilder stanza = new StringBuilder();
        stanza.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        String authenticationText = getChallengeResponse(StringUtils.decodeBase64(challenge));
        if (authenticationText != null) {
            stanza.append(StringUtils.encodeBase64(authenticationText));
        }
        stanza.append("</response>");
        getSASLAuthentication().send(stanza.toString());
    }

    /* access modifiers changed from: protected */
    public SASLAuthentication getSASLAuthentication() {
        return this.saslAuthentication;
    }
}
