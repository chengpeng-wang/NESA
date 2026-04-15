package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.List;

public final class VEventResultParser extends ResultParser {
    public CalendarParsedResult parse(Result result) {
        String rawText = result.getText();
        if (rawText == null) {
            return null;
        }
        if (rawText.indexOf("BEGIN:VEVENT") < 0) {
            return null;
        }
        String summary = matchSingleVCardPrefixedField("SUMMARY", rawText, true);
        String start = matchSingleVCardPrefixedField("DTSTART", rawText, true);
        if (start == null) {
            return null;
        }
        double latitude;
        double longitude;
        String end = matchSingleVCardPrefixedField("DTEND", rawText, true);
        String location = matchSingleVCardPrefixedField("LOCATION", rawText, true);
        String description = matchSingleVCardPrefixedField("DESCRIPTION", rawText, true);
        String geoString = matchSingleVCardPrefixedField("GEO", rawText, true);
        if (geoString == null) {
            latitude = Double.NaN;
            longitude = Double.NaN;
        } else {
            int semicolon = geoString.indexOf(59);
            try {
                latitude = Double.parseDouble(geoString.substring(0, semicolon));
                longitude = Double.parseDouble(geoString.substring(semicolon + 1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        try {
            return new CalendarParsedResult(summary, start, end, location, null, description, latitude, longitude);
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }

    private static String matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
        List<String> values = VCardResultParser.matchSingleVCardPrefixedField(prefix, rawText, trim);
        return (values == null || values.isEmpty()) ? null : (String) values.get(0);
    }
}
