package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class URIResultParser extends ResultParser {
    public URIParsedResult parse(Result result) {
        String rawText = result.getText();
        if (rawText.startsWith("URL:")) {
            rawText = rawText.substring(4);
        }
        rawText = rawText.trim();
        if (isBasicallyValidURI(rawText)) {
            return new URIParsedResult(rawText, null);
        }
        return null;
    }

    static boolean isBasicallyValidURI(CharSequence uri) {
        if (uri == null) {
            return false;
        }
        int period = -1;
        int colon = -1;
        for (int i = uri.length() - 1; i >= 0; i--) {
            char c = uri.charAt(i);
            if (c <= ' ') {
                return false;
            }
            if (c == '.') {
                period = i;
            } else if (c == ':') {
                colon = i;
            }
        }
        if (period >= uri.length() - 2) {
            return false;
        }
        if (period <= 0 && colon <= 0) {
            return false;
        }
        if (colon >= 0) {
            if (period < 0 || period > colon) {
                if (!ResultParser.isSubstringOfAlphaNumeric(uri, 0, colon)) {
                    return false;
                }
            } else if (colon >= uri.length() - 2 || !ResultParser.isSubstringOfDigits(uri, colon + 1, 2)) {
                return false;
            }
        }
        return true;
    }
}
