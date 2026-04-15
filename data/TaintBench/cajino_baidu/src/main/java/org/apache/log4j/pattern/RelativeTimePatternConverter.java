package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

public class RelativeTimePatternConverter extends LoggingEventPatternConverter {
    private CachedTimestamp lastTimestamp = new CachedTimestamp(0, "");

    private static final class CachedTimestamp {
        private final String formatted;
        private final long timestamp;

        public CachedTimestamp(long timestamp, String formatted) {
            this.timestamp = timestamp;
            this.formatted = formatted;
        }

        public boolean format(long newTimestamp, StringBuffer toAppendTo) {
            if (newTimestamp != this.timestamp) {
                return false;
            }
            toAppendTo.append(this.formatted);
            return true;
        }
    }

    public RelativeTimePatternConverter() {
        super("Time", "time");
    }

    public static RelativeTimePatternConverter newInstance(String[] options) {
        return new RelativeTimePatternConverter();
    }

    public void format(LoggingEvent event, StringBuffer toAppendTo) {
        long timestamp = event.timeStamp;
        if (!this.lastTimestamp.format(timestamp, toAppendTo)) {
            String formatted = Long.toString(timestamp - LoggingEvent.getStartTime());
            toAppendTo.append(formatted);
            this.lastTimestamp = new CachedTimestamp(timestamp, formatted);
        }
    }
}
