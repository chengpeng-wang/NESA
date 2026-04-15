package org.apache.log4j.pattern;

public abstract class NamePatternConverter extends LoggingEventPatternConverter {
    private final NameAbbreviator abbreviator;

    protected NamePatternConverter(String name, String style, String[] options) {
        super(name, style);
        if (options == null || options.length <= 0) {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        } else {
            this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
        }
    }

    /* access modifiers changed from: protected|final */
    public final void abbreviate(int nameStart, StringBuffer buf) {
        this.abbreviator.abbreviate(nameStart, buf);
    }
}
