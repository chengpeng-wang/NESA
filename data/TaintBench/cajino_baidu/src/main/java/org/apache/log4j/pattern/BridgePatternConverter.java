package org.apache.log4j.pattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public final class BridgePatternConverter extends PatternConverter {
    private boolean handlesExceptions = false;
    private LoggingEventPatternConverter[] patternConverters;
    private FormattingInfo[] patternFields;

    public BridgePatternConverter(String pattern) {
        List converters = new ArrayList();
        List fields = new ArrayList();
        PatternParser.parse(pattern, converters, fields, null, PatternParser.getPatternLayoutRules());
        this.patternConverters = new LoggingEventPatternConverter[converters.size()];
        this.patternFields = new FormattingInfo[converters.size()];
        int i = 0;
        Iterator fieldIter = fields.iterator();
        for (Object converter : converters) {
            if (converter instanceof LoggingEventPatternConverter) {
                this.patternConverters[i] = (LoggingEventPatternConverter) converter;
                this.handlesExceptions |= this.patternConverters[i].handlesThrowable();
            } else {
                this.patternConverters[i] = new LiteralPatternConverter("");
            }
            if (fieldIter.hasNext()) {
                this.patternFields[i] = (FormattingInfo) fieldIter.next();
            } else {
                this.patternFields[i] = FormattingInfo.getDefault();
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public String convert(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        format(sbuf, event);
        return sbuf.toString();
    }

    public void format(StringBuffer sbuf, LoggingEvent e) {
        for (int i = 0; i < this.patternConverters.length; i++) {
            int startField = sbuf.length();
            this.patternConverters[i].format(e, sbuf);
            this.patternFields[i].format(startField, sbuf);
        }
    }

    public boolean ignoresThrowable() {
        return !this.handlesExceptions;
    }
}
