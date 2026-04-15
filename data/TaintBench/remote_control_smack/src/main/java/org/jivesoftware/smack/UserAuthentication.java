package org.jivesoftware.smack;

interface UserAuthentication {
    String authenticate(String str, String str2, String str3) throws XMPPException;

    String authenticateAnonymously() throws XMPPException;
}
