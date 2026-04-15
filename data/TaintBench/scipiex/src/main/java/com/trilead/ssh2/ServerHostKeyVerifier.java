package com.trilead.ssh2;

public interface ServerHostKeyVerifier {
    boolean verifyServerHostKey(String str, int i, String str2, byte[] bArr) throws Exception;
}
