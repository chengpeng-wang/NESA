package org.apache.log4j;

import org.apache.http.protocol.HTTP;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;

public abstract class Layout implements OptionHandler {
    public static final String LINE_SEP = System.getProperty("line.separator");
    public static final int LINE_SEP_LEN = LINE_SEP.length();

    public abstract void activateOptions();

    public abstract String format(LoggingEvent loggingEvent);

    public abstract boolean ignoresThrowable();

    public String getContentType() {
        return HTTP.PLAIN_TEXT_TYPE;
    }

    public String getHeader() {
        return null;
    }

    public String getFooter() {
        return null;
    }
}
