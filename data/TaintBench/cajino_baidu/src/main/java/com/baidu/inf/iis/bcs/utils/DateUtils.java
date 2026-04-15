package com.baidu.inf.iis.bcs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtils {
    protected final SimpleDateFormat alternateIo8601DateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    protected final SimpleDateFormat iso8601DateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    protected final SimpleDateFormat rfc822DateParser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public DateUtils() {
        this.iso8601DateParser.setTimeZone(new SimpleTimeZone(0, "GMT"));
        this.rfc822DateParser.setTimeZone(new SimpleTimeZone(0, "GMT"));
        this.alternateIo8601DateParser.setTimeZone(new SimpleTimeZone(0, "GMT"));
    }

    public String formatIso8601Date(Date date) {
        String format;
        synchronized (this.iso8601DateParser) {
            format = this.iso8601DateParser.format(date);
        }
        return format;
    }

    public String formatRfc822Date(Date date) {
        String format;
        synchronized (this.rfc822DateParser) {
            format = this.rfc822DateParser.format(date);
        }
        return format;
    }

    public Date parseIso8601Date(String str) throws ParseException {
        Date parse;
        try {
            synchronized (this.iso8601DateParser) {
                parse = this.iso8601DateParser.parse(str);
            }
        } catch (ParseException e) {
            synchronized (this.alternateIo8601DateParser) {
                parse = this.alternateIo8601DateParser.parse(str);
            }
        }
        return parse;
    }

    public Date parseRfc822Date(String str) throws ParseException {
        Date parse;
        synchronized (this.rfc822DateParser) {
            parse = this.rfc822DateParser.parse(str);
        }
        return parse;
    }
}
