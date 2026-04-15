package com.trilead.ssh2;

public interface InteractiveCallback {
    String[] replyToChallenge(String str, String str2, int i, String[] strArr, boolean[] zArr) throws Exception;
}
