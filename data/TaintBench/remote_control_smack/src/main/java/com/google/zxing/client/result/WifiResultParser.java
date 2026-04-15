package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class WifiResultParser extends ResultParser {
    public WifiParsedResult parse(Result result) {
        String rawText = result.getText();
        if (!rawText.startsWith("WIFI:")) {
            return null;
        }
        String ssid = ResultParser.matchSinglePrefixedField("S:", rawText, ';', false);
        if (ssid == null || ssid.length() == 0) {
            return null;
        }
        String pass = ResultParser.matchSinglePrefixedField("P:", rawText, ';', false);
        String type = ResultParser.matchSinglePrefixedField("T:", rawText, ';', false);
        if (type == null) {
            type = "nopass";
        }
        return new WifiParsedResult(type, ssid, pass);
    }
}
