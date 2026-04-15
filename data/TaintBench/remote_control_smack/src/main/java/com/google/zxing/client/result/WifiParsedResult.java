package com.google.zxing.client.result;

public final class WifiParsedResult extends ParsedResult {
    private final String networkEncryption;
    private final String password;
    private final String ssid;

    public WifiParsedResult(String networkEncryption, String ssid, String password) {
        super(ParsedResultType.WIFI);
        this.ssid = ssid;
        this.networkEncryption = networkEncryption;
        this.password = password;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getNetworkEncryption() {
        return this.networkEncryption;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(80);
        ParsedResult.maybeAppend(this.ssid, result);
        ParsedResult.maybeAppend(this.networkEncryption, result);
        ParsedResult.maybeAppend(this.password, result);
        return result.toString();
    }
}
