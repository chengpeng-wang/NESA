package org.springframework.http;

import org.springframework.util.Base64Utils;

public class HttpBasicAuthentication extends HttpAuthentication {
    private final String password;
    private final String username;

    public HttpBasicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getHeaderValue() {
        byte[] bytes = String.format("%s:%s", new Object[]{this.username, this.password}).getBytes();
        return String.format("Basic %s", new Object[]{Base64Utils.encodeToString(bytes)});
    }

    public String toString() {
        try {
            return String.format("Authorization: %s", new Object[]{getHeaderValue()});
        } catch (RuntimeException e) {
            return null;
        }
    }
}
