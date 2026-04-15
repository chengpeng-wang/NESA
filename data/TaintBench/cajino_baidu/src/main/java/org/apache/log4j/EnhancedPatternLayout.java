package org.apache.log4j;

import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.pattern.BridgePatternConverter;
import org.apache.log4j.pattern.BridgePatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class EnhancedPatternLayout extends Layout {
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
    public static final String PATTERN_RULE_REGISTRY = "PATTERN_RULE_REGISTRY";
    public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %x - %m%n";
    protected final int BUF_SIZE;
    protected final int MAX_CAPACITY;
    private String conversionPattern;
    private boolean handlesExceptions;
    private PatternConverter head;

    public EnhancedPatternLayout() {
        this("%m%n");
    }

    public EnhancedPatternLayout(String pattern) {
        this.BUF_SIZE = 256;
        this.MAX_CAPACITY = 1024;
        this.conversionPattern = pattern;
        if (pattern == null) {
            pattern = "%m%n";
        }
        this.head = createPatternParser(pattern).parse();
        if (this.head instanceof BridgePatternConverter) {
            boolean z;
            if (((BridgePatternConverter) this.head).ignoresThrowable()) {
                z = false;
            } else {
                z = true;
            }
            this.handlesExceptions = z;
            return;
        }
        this.handlesExceptions = false;
    }

    public void setConversionPattern(String conversionPattern) {
        this.conversionPattern = OptionConverter.convertSpecialChars(conversionPattern);
        this.head = createPatternParser(this.conversionPattern).parse();
        if (this.head instanceof BridgePatternConverter) {
            boolean z;
            if (((BridgePatternConverter) this.head).ignoresThrowable()) {
                z = false;
            } else {
                z = true;
            }
            this.handlesExceptions = z;
            return;
        }
        this.handlesExceptions = false;
    }

    public String getConversionPattern() {
        return this.conversionPattern;
    }

    /* access modifiers changed from: protected */
    public PatternParser createPatternParser(String pattern) {
        return new BridgePatternParser(pattern);
    }

    public void activateOptions() {
    }

    public String format(LoggingEvent event) {
        StringBuffer buf = new StringBuffer();
        for (PatternConverter c = this.head; c != null; c = c.next) {
            c.format(buf, event);
        }
        return buf.toString();
    }

    public boolean ignoresThrowable() {
        return !this.handlesExceptions;
    }
}
