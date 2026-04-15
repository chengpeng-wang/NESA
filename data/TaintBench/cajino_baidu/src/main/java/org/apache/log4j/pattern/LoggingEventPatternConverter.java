package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public abstract class LoggingEventPatternConverter extends PatternConverter {
    public abstract void format(LoggingEvent loggingEvent, StringBuffer stringBuffer);

    protected LoggingEventPatternConverter(String name, String style) {
        super(name, style);
    }

    public void format(Object obj, StringBuffer output) {
        if (obj instanceof LoggingEvent) {
            format((LoggingEvent) obj, output);
        }
    }

    public boolean handlesThrowable() {
        return false;
    }
}
