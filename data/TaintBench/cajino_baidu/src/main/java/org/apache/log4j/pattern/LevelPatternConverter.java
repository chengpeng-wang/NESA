package org.apache.log4j.pattern;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

public final class LevelPatternConverter extends LoggingEventPatternConverter {
    private static final LevelPatternConverter INSTANCE = new LevelPatternConverter();
    private static final int TRACE_INT = 5000;

    private LevelPatternConverter() {
        super("Level", "level");
    }

    public static LevelPatternConverter newInstance(String[] options) {
        return INSTANCE;
    }

    public void format(LoggingEvent event, StringBuffer output) {
        output.append(event.getLevel().toString());
    }

    public String getStyleClass(Object e) {
        if (!(e instanceof LoggingEvent)) {
            return "level";
        }
        switch (((LoggingEvent) e).getLevel().toInt()) {
            case 5000:
                return "level trace";
            case Priority.DEBUG_INT /*10000*/:
                return "level debug";
            case Priority.INFO_INT /*20000*/:
                return "level info";
            case Priority.WARN_INT /*30000*/:
                return "level warn";
            case Priority.ERROR_INT /*40000*/:
                return "level error";
            case Priority.FATAL_INT /*50000*/:
                return "level fatal";
            default:
                return new StringBuffer().append("level ").append(((LoggingEvent) e).getLevel().toString()).toString();
        }
    }
}
