package org.jivesoftware.smack.sasl;

import java.io.IOException;
import java.util.HashMap;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.util.Base64;

public class SASLGSSAPIMechanism extends SASLMechanism {
    private static final String[] mechanisms = new String[]{"GSSAPI"};
    private static final String protocol = "xmpp";
    private SaslClient sc;

    public SASLGSSAPIMechanism(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
        System.setProperty("java.security.krb5.debug", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty("java.security.auth.login.config", "gss.conf");
    }

    /* access modifiers changed from: protected */
    public String getName() {
        return "GSSAPI";
    }

    public void authenticate(String username, String host, String password) throws IOException {
        StringBuffer stanza = new StringBuffer();
        String str = username;
        this.sc = Sasl.createSaslClient(mechanisms, str, protocol, host, new HashMap(), null);
        stanza.append("<auth mechanism=\"").append(getName());
        stanza.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        if (this.sc.hasInitialResponse()) {
            String authenticationText = Base64.encodeBytes(this.sc.evaluateChallenge(new byte[0]), 8);
            if (!(authenticationText == null || authenticationText.equals(""))) {
                stanza.append(authenticationText);
            }
        }
        stanza.append("</auth>");
        getSASLAuthentication().send(stanza.toString());
    }

    /* access modifiers changed from: protected */
    public String getAuthenticationText(String username, String host, String password) {
        return null;
    }

    public void challengeReceived(String challenge) throws IOException {
        byte[] response;
        StringBuffer stanza = new StringBuffer();
        if (challenge != null) {
            response = this.sc.evaluateChallenge(Base64.decode(challenge));
        } else {
            response = this.sc.evaluateChallenge(null);
        }
        String authenticationText = Base64.encodeBytes(response, 8);
        if (authenticationText.equals("")) {
            authenticationText = "=";
        }
        stanza.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        stanza.append(authenticationText);
        stanza.append("</response>");
        getSASLAuthentication().send(stanza.toString());
    }

    /* access modifiers changed from: protected */
    public String getChallengeResponse(byte[] bytes) {
        return null;
    }
}
