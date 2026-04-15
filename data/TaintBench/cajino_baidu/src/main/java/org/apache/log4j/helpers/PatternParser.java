package org.apache.log4j.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.util.LangUtils;
import org.apache.log4j.Layout;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class PatternParser {
    static final int CLASS_LOCATION_CONVERTER = 1002;
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final char ESCAPE_CHAR = '%';
    static final int FILE_LOCATION_CONVERTER = 1004;
    static final int FULL_LOCATION_CONVERTER = 1000;
    static final int LEVEL_CONVERTER = 2002;
    static final int LINE_LOCATION_CONVERTER = 1003;
    private static final int LITERAL_STATE = 0;
    private static final int MAX_STATE = 5;
    static final int MESSAGE_CONVERTER = 2004;
    static final int METHOD_LOCATION_CONVERTER = 1001;
    private static final int MIN_STATE = 4;
    static final int NDC_CONVERTER = 2003;
    static final int RELATIVE_TIME_CONVERTER = 2000;
    static final int THREAD_CONVERTER = 2001;
    static Class class$java$text$DateFormat;
    protected StringBuffer currentLiteral = new StringBuffer(32);
    protected FormattingInfo formattingInfo = new FormattingInfo();
    PatternConverter head;
    protected int i;
    protected String pattern;
    protected int patternLength;
    int state;
    PatternConverter tail;

    private static class BasicPatternConverter extends PatternConverter {
        int type;

        BasicPatternConverter(FormattingInfo formattingInfo, int type) {
            super(formattingInfo);
            this.type = type;
        }

        public String convert(LoggingEvent event) {
            switch (this.type) {
                case PatternParser.RELATIVE_TIME_CONVERTER /*2000*/:
                    return Long.toString(event.timeStamp - LoggingEvent.getStartTime());
                case PatternParser.THREAD_CONVERTER /*2001*/:
                    return event.getThreadName();
                case PatternParser.LEVEL_CONVERTER /*2002*/:
                    return event.getLevel().toString();
                case PatternParser.NDC_CONVERTER /*2003*/:
                    return event.getNDC();
                case PatternParser.MESSAGE_CONVERTER /*2004*/:
                    return event.getRenderedMessage();
                default:
                    return null;
            }
        }
    }

    private static class DatePatternConverter extends PatternConverter {
        private Date date = new Date();
        private DateFormat df;

        DatePatternConverter(FormattingInfo formattingInfo, DateFormat df) {
            super(formattingInfo);
            this.df = df;
        }

        public String convert(LoggingEvent event) {
            this.date.setTime(event.timeStamp);
            String converted = null;
            try {
                return this.df.format(this.date);
            } catch (Exception ex) {
                LogLog.error("Error occured while converting date.", ex);
                return converted;
            }
        }
    }

    private static class LiteralPatternConverter extends PatternConverter {
        private String literal;

        LiteralPatternConverter(String value) {
            this.literal = value;
        }

        public final void format(StringBuffer sbuf, LoggingEvent event) {
            sbuf.append(this.literal);
        }

        public String convert(LoggingEvent event) {
            return this.literal;
        }
    }

    private class LocationPatternConverter extends PatternConverter {
        private final PatternParser this$0;
        int type;

        LocationPatternConverter(PatternParser patternParser, FormattingInfo formattingInfo, int type) {
            super(formattingInfo);
            this.this$0 = patternParser;
            this.type = type;
        }

        public String convert(LoggingEvent event) {
            LocationInfo locationInfo = event.getLocationInformation();
            switch (this.type) {
                case PatternParser.FULL_LOCATION_CONVERTER /*1000*/:
                    return locationInfo.fullInfo;
                case PatternParser.METHOD_LOCATION_CONVERTER /*1001*/:
                    return locationInfo.getMethodName();
                case PatternParser.LINE_LOCATION_CONVERTER /*1003*/:
                    return locationInfo.getLineNumber();
                case PatternParser.FILE_LOCATION_CONVERTER /*1004*/:
                    return locationInfo.getFileName();
                default:
                    return null;
            }
        }
    }

    private static class MDCPatternConverter extends PatternConverter {
        private String key;

        MDCPatternConverter(FormattingInfo formattingInfo, String key) {
            super(formattingInfo);
            this.key = key;
        }

        public String convert(LoggingEvent event) {
            if (this.key == null) {
                StringBuffer buf = new StringBuffer("{");
                Map properties = event.getProperties();
                if (properties.size() > 0) {
                    Object[] keys = properties.keySet().toArray();
                    Arrays.sort(keys);
                    for (int i = 0; i < keys.length; i++) {
                        buf.append('{');
                        buf.append(keys[i]);
                        buf.append(',');
                        buf.append(properties.get(keys[i]));
                        buf.append('}');
                    }
                }
                buf.append('}');
                return buf.toString();
            }
            Object val = event.getMDC(this.key);
            if (val == null) {
                return null;
            }
            return val.toString();
        }
    }

    private static abstract class NamedPatternConverter extends PatternConverter {
        int precision;

        public abstract String getFullyQualifiedName(LoggingEvent loggingEvent);

        NamedPatternConverter(FormattingInfo formattingInfo, int precision) {
            super(formattingInfo);
            this.precision = precision;
        }

        public String convert(LoggingEvent event) {
            String n = getFullyQualifiedName(event);
            if (this.precision <= 0) {
                return n;
            }
            int len = n.length();
            int end = len - 1;
            for (int i = this.precision; i > 0; i--) {
                end = n.lastIndexOf(46, end - 1);
                if (end == -1) {
                    return n;
                }
            }
            return n.substring(end + 1, len);
        }
    }

    private class CategoryPatternConverter extends NamedPatternConverter {
        private final PatternParser this$0;

        CategoryPatternConverter(PatternParser patternParser, FormattingInfo formattingInfo, int precision) {
            super(formattingInfo, precision);
            this.this$0 = patternParser;
        }

        /* access modifiers changed from: 0000 */
        public String getFullyQualifiedName(LoggingEvent event) {
            return event.getLoggerName();
        }
    }

    private class ClassNamePatternConverter extends NamedPatternConverter {
        private final PatternParser this$0;

        ClassNamePatternConverter(PatternParser patternParser, FormattingInfo formattingInfo, int precision) {
            super(formattingInfo, precision);
            this.this$0 = patternParser;
        }

        /* access modifiers changed from: 0000 */
        public String getFullyQualifiedName(LoggingEvent event) {
            return event.getLocationInformation().getClassName();
        }
    }

    public PatternParser(String pattern) {
        this.pattern = pattern;
        this.patternLength = pattern.length();
        this.state = 0;
    }

    private void addToList(PatternConverter pc) {
        if (this.head == null) {
            this.tail = pc;
            this.head = pc;
            return;
        }
        this.tail.next = pc;
        this.tail = pc;
    }

    /* access modifiers changed from: protected */
    public String extractOption() {
        if (this.i < this.patternLength && this.pattern.charAt(this.i) == '{') {
            int end = this.pattern.indexOf(125, this.i);
            if (end > this.i) {
                String r = this.pattern.substring(this.i + 1, end);
                this.i = end + 1;
                return r;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int extractPrecisionOption() {
        String opt = extractOption();
        if (opt == null) {
            return 0;
        }
        try {
            int r = Integer.parseInt(opt);
            if (r > 0) {
                return r;
            }
            LogLog.error(new StringBuffer().append("Precision option (").append(opt).append(") isn't a positive integer.").toString());
            return 0;
        } catch (NumberFormatException e) {
            LogLog.error(new StringBuffer().append("Category option \"").append(opt).append("\" not a decimal integer.").toString(), e);
            return 0;
        }
    }

    public PatternConverter parse() {
        this.i = 0;
        while (this.i < this.patternLength) {
            String str = this.pattern;
            int i = this.i;
            this.i = i + 1;
            char c = str.charAt(i);
            switch (this.state) {
                case 0:
                    if (this.i != this.patternLength) {
                        if (c != ESCAPE_CHAR) {
                            this.currentLiteral.append(c);
                            break;
                        }
                        switch (this.pattern.charAt(this.i)) {
                            case LangUtils.HASH_OFFSET /*37*/:
                                this.currentLiteral.append(c);
                                this.i++;
                                break;
                            case 'n':
                                this.currentLiteral.append(Layout.LINE_SEP);
                                this.i++;
                                break;
                            default:
                                if (this.currentLiteral.length() != 0) {
                                    addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
                                }
                                this.currentLiteral.setLength(0);
                                this.currentLiteral.append(c);
                                this.state = 1;
                                this.formattingInfo.reset();
                                break;
                        }
                    }
                    this.currentLiteral.append(c);
                    break;
                case 1:
                    this.currentLiteral.append(c);
                    switch (c) {
                        case '-':
                            this.formattingInfo.leftAlign = true;
                            break;
                        case '.':
                            this.state = 3;
                            break;
                        default:
                            if (c >= '0' && c <= '9') {
                                this.formattingInfo.min = c - 48;
                                this.state = 4;
                                break;
                            }
                            finalizeConverter(c);
                            break;
                    }
                case 3:
                    this.currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        this.formattingInfo.max = c - 48;
                        this.state = 5;
                        break;
                    }
                    LogLog.error(new StringBuffer().append("Error occured in position ").append(this.i).append(".\n Was expecting digit, instead got char \"").append(c).append("\".").toString());
                    this.state = 0;
                    break;
                    break;
                case 4:
                    this.currentLiteral.append(c);
                    if (c < '0' || c > '9') {
                        if (c != '.') {
                            finalizeConverter(c);
                            break;
                        }
                        this.state = 3;
                        break;
                    }
                    this.formattingInfo.min = (this.formattingInfo.min * 10) + (c - 48);
                    break;
                case 5:
                    this.currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        this.formattingInfo.max = (this.formattingInfo.max * 10) + (c - 48);
                        break;
                    }
                    finalizeConverter(c);
                    this.state = 0;
                    break;
                    break;
                default:
                    break;
            }
        }
        if (this.currentLiteral.length() != 0) {
            addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
        }
        return this.head;
    }

    /* access modifiers changed from: protected */
    public void finalizeConverter(char c) {
        PatternConverter pc;
        Class class$;
        switch (c) {
            case 'C':
                pc = new ClassNamePatternConverter(this, this.formattingInfo, extractPrecisionOption());
                this.currentLiteral.setLength(0);
                break;
            case 'F':
                pc = new LocationPatternConverter(this, this.formattingInfo, FILE_LOCATION_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'L':
                pc = new LocationPatternConverter(this, this.formattingInfo, LINE_LOCATION_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'M':
                pc = new LocationPatternConverter(this, this.formattingInfo, METHOD_LOCATION_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case SyslogAppender.LOG_FTP /*88*/:
                pc = new MDCPatternConverter(this.formattingInfo, extractOption());
                this.currentLiteral.setLength(0);
                break;
            case 'c':
                pc = new CategoryPatternConverter(this, this.formattingInfo, extractPrecisionOption());
                this.currentLiteral.setLength(0);
                break;
            case HttpStatus.SC_CONTINUE /*100*/:
                DateFormat df;
                String dateFormatStr = AbsoluteTimeDateFormat.ISO8601_DATE_FORMAT;
                String dOpt = extractOption();
                if (dOpt != null) {
                    dateFormatStr = dOpt;
                }
                if (dateFormatStr.equalsIgnoreCase(AbsoluteTimeDateFormat.ISO8601_DATE_FORMAT)) {
                    df = new ISO8601DateFormat();
                } else if (dateFormatStr.equalsIgnoreCase(AbsoluteTimeDateFormat.ABS_TIME_DATE_FORMAT)) {
                    df = new AbsoluteTimeDateFormat();
                } else if (dateFormatStr.equalsIgnoreCase(AbsoluteTimeDateFormat.DATE_AND_TIME_DATE_FORMAT)) {
                    df = new DateTimeDateFormat();
                } else {
                    try {
                        df = new SimpleDateFormat(dateFormatStr);
                    } catch (IllegalArgumentException e) {
                        LogLog.error(new StringBuffer().append("Could not instantiate SimpleDateFormat with ").append(dateFormatStr).toString(), e);
                        String str = "org.apache.log4j.helpers.ISO8601DateFormat";
                        if (class$java$text$DateFormat == null) {
                            class$ = class$("java.text.DateFormat");
                            class$java$text$DateFormat = class$;
                        } else {
                            class$ = class$java$text$DateFormat;
                        }
                        df = (DateFormat) OptionConverter.instantiateByClassName(str, class$, null);
                    }
                }
                pc = new DatePatternConverter(this.formattingInfo, df);
                this.currentLiteral.setLength(0);
                break;
            case 'l':
                pc = new LocationPatternConverter(this, this.formattingInfo, FULL_LOCATION_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'm':
                pc = new BasicPatternConverter(this.formattingInfo, MESSAGE_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'p':
                pc = new BasicPatternConverter(this.formattingInfo, LEVEL_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'r':
                pc = new BasicPatternConverter(this.formattingInfo, RELATIVE_TIME_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 't':
                pc = new BasicPatternConverter(this.formattingInfo, THREAD_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            case 'x':
                pc = new BasicPatternConverter(this.formattingInfo, NDC_CONVERTER);
                this.currentLiteral.setLength(0);
                break;
            default:
                LogLog.error(new StringBuffer().append("Unexpected char [").append(c).append("] at position ").append(this.i).append(" in conversion patterrn.").toString());
                pc = new LiteralPatternConverter(this.currentLiteral.toString());
                this.currentLiteral.setLength(0);
                break;
        }
        addConverter(pc);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void addConverter(PatternConverter pc) {
        this.currentLiteral.setLength(0);
        addToList(pc);
        this.state = 0;
        this.formattingInfo.reset();
    }
}
