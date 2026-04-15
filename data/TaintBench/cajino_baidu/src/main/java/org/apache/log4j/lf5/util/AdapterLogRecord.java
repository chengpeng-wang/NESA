package org.apache.log4j.lf5.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.LogRecord;

public class AdapterLogRecord extends LogRecord {
    private static PrintWriter pw = new PrintWriter(sw);
    private static LogLevel severeLevel = null;
    private static StringWriter sw = new StringWriter();

    public void setCategory(String category) {
        super.setCategory(category);
        super.setLocation(getLocationInfo(category));
    }

    public boolean isSevereLevel() {
        if (severeLevel == null) {
            return false;
        }
        return severeLevel.equals(getLevel());
    }

    public static void setSevereLevel(LogLevel level) {
        severeLevel = level;
    }

    public static LogLevel getSevereLevel() {
        return severeLevel;
    }

    /* access modifiers changed from: protected */
    public String getLocationInfo(String category) {
        return parseLine(stackTraceToString(new Throwable()), category);
    }

    /* access modifiers changed from: protected */
    public String stackTraceToString(Throwable t) {
        String s;
        synchronized (sw) {
            t.printStackTrace(pw);
            s = sw.toString();
            sw.getBuffer().setLength(0);
        }
        return s;
    }

    /* access modifiers changed from: protected */
    public String parseLine(String trace, String category) {
        int index = trace.indexOf(category);
        if (index == -1) {
            return null;
        }
        trace = trace.substring(index);
        return trace.substring(0, trace.indexOf(")") + 1);
    }
}
