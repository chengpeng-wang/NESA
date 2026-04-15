package com.google.zxing.client.result;

public final class CalendarParsedResult extends ParsedResult {
    private final String attendee;
    private final String description;
    private final String end;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String start;
    private final String summary;

    public CalendarParsedResult(String summary, String start, String end, String location, String attendee, String description) {
        this(summary, start, end, location, attendee, description, Double.NaN, Double.NaN);
    }

    public CalendarParsedResult(String summary, String start, String end, String location, String attendee, String description, double latitude, double longitude) {
        super(ParsedResultType.CALENDAR);
        validateDate(start);
        this.summary = summary;
        this.start = start;
        if (end != null) {
            validateDate(end);
            this.end = end;
        } else {
            this.end = null;
        }
        this.location = location;
        this.attendee = attendee;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public String getLocation() {
        return this.location;
    }

    public String getAttendee() {
        return this.attendee;
    }

    public String getDescription() {
        return this.description;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(100);
        ParsedResult.maybeAppend(this.summary, result);
        ParsedResult.maybeAppend(this.start, result);
        ParsedResult.maybeAppend(this.end, result);
        ParsedResult.maybeAppend(this.location, result);
        ParsedResult.maybeAppend(this.attendee, result);
        ParsedResult.maybeAppend(this.description, result);
        return result.toString();
    }

    private static void validateDate(CharSequence date) {
        if (date != null) {
            int length = date.length();
            if (length == 8 || length == 15 || length == 16) {
                int i = 0;
                while (i < 8) {
                    if (Character.isDigit(date.charAt(i))) {
                        i++;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                if (length <= 8) {
                    return;
                }
                if (date.charAt(8) != 'T') {
                    throw new IllegalArgumentException();
                }
                i = 9;
                while (i < 15) {
                    if (Character.isDigit(date.charAt(i))) {
                        i++;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                if (length == 16 && date.charAt(15) != 'Z') {
                    throw new IllegalArgumentException();
                }
                return;
            }
            throw new IllegalArgumentException();
        }
    }
}
