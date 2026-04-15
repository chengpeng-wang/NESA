package org.apache.log4j.helpers;

import org.apache.log4j.spi.LoggingEvent;

public abstract class PatternConverter {
    static String[] SPACES = new String[]{" ", "  ", "    ", "        ", "                ", "                                "};
    boolean leftAlign = false;
    int max = Integer.MAX_VALUE;
    int min = -1;
    public PatternConverter next;

    public abstract String convert(LoggingEvent loggingEvent);

    protected PatternConverter() {
    }

    protected PatternConverter(FormattingInfo fi) {
        this.min = fi.min;
        this.max = fi.max;
        this.leftAlign = fi.leftAlign;
    }

    public void format(StringBuffer sbuf, LoggingEvent e) {
        String s = convert(e);
        if (s != null) {
            int len = s.length();
            if (len > this.max) {
                sbuf.append(s.substring(len - this.max));
            } else if (len >= this.min) {
                sbuf.append(s);
            } else if (this.leftAlign) {
                sbuf.append(s);
                spacePad(sbuf, this.min - len);
            } else {
                spacePad(sbuf, this.min - len);
                sbuf.append(s);
            }
        } else if (this.min > 0) {
            spacePad(sbuf, this.min);
        }
    }

    public void spacePad(StringBuffer sbuf, int length) {
        while (length >= 32) {
            sbuf.append(SPACES[5]);
            length -= 32;
        }
        for (int i = 4; i >= 0; i--) {
            if (((1 << i) & length) != 0) {
                sbuf.append(SPACES[i]);
            }
        }
    }
}
