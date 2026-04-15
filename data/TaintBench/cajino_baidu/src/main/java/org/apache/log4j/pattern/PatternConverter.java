package org.apache.log4j.pattern;

public abstract class PatternConverter {
    private final String name;
    private final String style;

    public abstract void format(Object obj, StringBuffer stringBuffer);

    protected PatternConverter(String name, String style) {
        this.name = name;
        this.style = style;
    }

    public final String getName() {
        return this.name;
    }

    public String getStyleClass(Object e) {
        return this.style;
    }
}
