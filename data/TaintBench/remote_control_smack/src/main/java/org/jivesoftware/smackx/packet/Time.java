package org.jivesoftware.smackx.packet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;

public class Time extends IQ {
    private static DateFormat displayFormat = DateFormat.getDateTimeInstance();
    private static SimpleDateFormat utcFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    private String display = null;
    private String tz = null;
    private String utc = null;

    public Time(Calendar cal) {
        TimeZone timeZone = cal.getTimeZone();
        this.tz = cal.getTimeZone().getID();
        this.display = displayFormat.format(cal.getTime());
        this.utc = utcFormat.format(new Date(cal.getTimeInMillis() - ((long) timeZone.getOffset(cal.getTimeInMillis()))));
    }

    public Date getTime() {
        if (this.utc == null) {
            return null;
        }
        Date date = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(utcFormat.parse(this.utc).getTime() + ((long) cal.getTimeZone().getOffset(cal.getTimeInMillis()))));
            return cal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    public void setTime(Date time) {
        this.utc = utcFormat.format(new Date(time.getTime() - ((long) TimeZone.getDefault().getOffset(time.getTime()))));
    }

    public String getUtc() {
        return this.utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getTz() {
        return this.tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:time\">");
        if (this.utc != null) {
            buf.append("<utc>").append(this.utc).append("</utc>");
        }
        if (this.tz != null) {
            buf.append("<tz>").append(this.tz).append("</tz>");
        }
        if (this.display != null) {
            buf.append("<display>").append(this.display).append("</display>");
        }
        buf.append("</query>");
        return buf.toString();
    }
}
