package org.apache.log4j.pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;

public final class PatternParser {
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final char ESCAPE_CHAR = '%';
    private static final Map FILENAME_PATTERN_RULES;
    private static final int LITERAL_STATE = 0;
    private static final int MAX_STATE = 5;
    private static final int MIN_STATE = 4;
    private static final Map PATTERN_LAYOUT_RULES;
    static Class class$org$apache$log4j$pattern$ClassNamePatternConverter;
    static Class class$org$apache$log4j$pattern$DatePatternConverter;
    static Class class$org$apache$log4j$pattern$FileDatePatternConverter;
    static Class class$org$apache$log4j$pattern$FileLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$FullLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$IntegerPatternConverter;
    static Class class$org$apache$log4j$pattern$LevelPatternConverter;
    static Class class$org$apache$log4j$pattern$LineLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$LineSeparatorPatternConverter;
    static Class class$org$apache$log4j$pattern$LoggerPatternConverter;
    static Class class$org$apache$log4j$pattern$MessagePatternConverter;
    static Class class$org$apache$log4j$pattern$MethodLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$NDCPatternConverter;
    static Class class$org$apache$log4j$pattern$PropertiesPatternConverter;
    static Class class$org$apache$log4j$pattern$RelativeTimePatternConverter;
    static Class class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
    static Class class$org$apache$log4j$pattern$ThreadPatternConverter;
    static Class class$org$apache$log4j$pattern$ThrowableInformationPatternConverter;

    private static class ReadOnlyMap implements Map {
        private final Map map;

        public ReadOnlyMap(Map src) {
            this.map = src;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        public Set entrySet() {
            return this.map.entrySet();
        }

        public Object get(Object key) {
            return this.map.get(key);
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public Set keySet() {
            return this.map.keySet();
        }

        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.map.size();
        }

        public Collection values() {
            return this.map.values();
        }
    }

    static {
        Object class$;
        Map rules = new HashMap(17);
        String str = "c";
        if (class$org$apache$log4j$pattern$LoggerPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LoggerPatternConverter");
            class$org$apache$log4j$pattern$LoggerPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LoggerPatternConverter;
        }
        rules.put(str, class$);
        str = "logger";
        if (class$org$apache$log4j$pattern$LoggerPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LoggerPatternConverter");
            class$org$apache$log4j$pattern$LoggerPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LoggerPatternConverter;
        }
        rules.put(str, class$);
        str = "C";
        if (class$org$apache$log4j$pattern$ClassNamePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.ClassNamePatternConverter");
            class$org$apache$log4j$pattern$ClassNamePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$ClassNamePatternConverter;
        }
        rules.put(str, class$);
        str = "class";
        if (class$org$apache$log4j$pattern$ClassNamePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.ClassNamePatternConverter");
            class$org$apache$log4j$pattern$ClassNamePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$ClassNamePatternConverter;
        }
        rules.put(str, class$);
        str = "d";
        if (class$org$apache$log4j$pattern$DatePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.DatePatternConverter");
            class$org$apache$log4j$pattern$DatePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$DatePatternConverter;
        }
        rules.put(str, class$);
        str = "date";
        if (class$org$apache$log4j$pattern$DatePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.DatePatternConverter");
            class$org$apache$log4j$pattern$DatePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$DatePatternConverter;
        }
        rules.put(str, class$);
        str = "F";
        if (class$org$apache$log4j$pattern$FileLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.FileLocationPatternConverter");
            class$org$apache$log4j$pattern$FileLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$FileLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "file";
        if (class$org$apache$log4j$pattern$FileLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.FileLocationPatternConverter");
            class$org$apache$log4j$pattern$FileLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$FileLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "l";
        if (class$org$apache$log4j$pattern$FullLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.FullLocationPatternConverter");
            class$org$apache$log4j$pattern$FullLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$FullLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "L";
        if (class$org$apache$log4j$pattern$LineLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LineLocationPatternConverter");
            class$org$apache$log4j$pattern$LineLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LineLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "line";
        if (class$org$apache$log4j$pattern$LineLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LineLocationPatternConverter");
            class$org$apache$log4j$pattern$LineLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LineLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "m";
        if (class$org$apache$log4j$pattern$MessagePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.MessagePatternConverter");
            class$org$apache$log4j$pattern$MessagePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$MessagePatternConverter;
        }
        rules.put(str, class$);
        str = "message";
        if (class$org$apache$log4j$pattern$MessagePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.MessagePatternConverter");
            class$org$apache$log4j$pattern$MessagePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$MessagePatternConverter;
        }
        rules.put(str, class$);
        str = "n";
        if (class$org$apache$log4j$pattern$LineSeparatorPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LineSeparatorPatternConverter");
            class$org$apache$log4j$pattern$LineSeparatorPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LineSeparatorPatternConverter;
        }
        rules.put(str, class$);
        str = "M";
        if (class$org$apache$log4j$pattern$MethodLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.MethodLocationPatternConverter");
            class$org$apache$log4j$pattern$MethodLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$MethodLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "method";
        if (class$org$apache$log4j$pattern$MethodLocationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.MethodLocationPatternConverter");
            class$org$apache$log4j$pattern$MethodLocationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$MethodLocationPatternConverter;
        }
        rules.put(str, class$);
        str = "p";
        if (class$org$apache$log4j$pattern$LevelPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LevelPatternConverter");
            class$org$apache$log4j$pattern$LevelPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LevelPatternConverter;
        }
        rules.put(str, class$);
        str = "level";
        if (class$org$apache$log4j$pattern$LevelPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.LevelPatternConverter");
            class$org$apache$log4j$pattern$LevelPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$LevelPatternConverter;
        }
        rules.put(str, class$);
        str = "r";
        if (class$org$apache$log4j$pattern$RelativeTimePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.RelativeTimePatternConverter");
            class$org$apache$log4j$pattern$RelativeTimePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$RelativeTimePatternConverter;
        }
        rules.put(str, class$);
        str = "relative";
        if (class$org$apache$log4j$pattern$RelativeTimePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.RelativeTimePatternConverter");
            class$org$apache$log4j$pattern$RelativeTimePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$RelativeTimePatternConverter;
        }
        rules.put(str, class$);
        str = "t";
        if (class$org$apache$log4j$pattern$ThreadPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.ThreadPatternConverter");
            class$org$apache$log4j$pattern$ThreadPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$ThreadPatternConverter;
        }
        rules.put(str, class$);
        str = "thread";
        if (class$org$apache$log4j$pattern$ThreadPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.ThreadPatternConverter");
            class$org$apache$log4j$pattern$ThreadPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$ThreadPatternConverter;
        }
        rules.put(str, class$);
        str = "x";
        if (class$org$apache$log4j$pattern$NDCPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.NDCPatternConverter");
            class$org$apache$log4j$pattern$NDCPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$NDCPatternConverter;
        }
        rules.put(str, class$);
        str = "ndc";
        if (class$org$apache$log4j$pattern$NDCPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.NDCPatternConverter");
            class$org$apache$log4j$pattern$NDCPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$NDCPatternConverter;
        }
        rules.put(str, class$);
        str = "X";
        if (class$org$apache$log4j$pattern$PropertiesPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.PropertiesPatternConverter");
            class$org$apache$log4j$pattern$PropertiesPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$PropertiesPatternConverter;
        }
        rules.put(str, class$);
        str = "properties";
        if (class$org$apache$log4j$pattern$PropertiesPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.PropertiesPatternConverter");
            class$org$apache$log4j$pattern$PropertiesPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$PropertiesPatternConverter;
        }
        rules.put(str, class$);
        str = "sn";
        if (class$org$apache$log4j$pattern$SequenceNumberPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.SequenceNumberPatternConverter");
            class$org$apache$log4j$pattern$SequenceNumberPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
        }
        rules.put(str, class$);
        str = "sequenceNumber";
        if (class$org$apache$log4j$pattern$SequenceNumberPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.SequenceNumberPatternConverter");
            class$org$apache$log4j$pattern$SequenceNumberPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
        }
        rules.put(str, class$);
        str = "throwable";
        if (class$org$apache$log4j$pattern$ThrowableInformationPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.ThrowableInformationPatternConverter");
            class$org$apache$log4j$pattern$ThrowableInformationPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$ThrowableInformationPatternConverter;
        }
        rules.put(str, class$);
        PATTERN_LAYOUT_RULES = new ReadOnlyMap(rules);
        Map fnameRules = new HashMap(4);
        str = "d";
        if (class$org$apache$log4j$pattern$FileDatePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.FileDatePatternConverter");
            class$org$apache$log4j$pattern$FileDatePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$FileDatePatternConverter;
        }
        fnameRules.put(str, class$);
        str = "date";
        if (class$org$apache$log4j$pattern$FileDatePatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.FileDatePatternConverter");
            class$org$apache$log4j$pattern$FileDatePatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$FileDatePatternConverter;
        }
        fnameRules.put(str, class$);
        str = "i";
        if (class$org$apache$log4j$pattern$IntegerPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.IntegerPatternConverter");
            class$org$apache$log4j$pattern$IntegerPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$IntegerPatternConverter;
        }
        fnameRules.put(str, class$);
        str = "index";
        if (class$org$apache$log4j$pattern$IntegerPatternConverter == null) {
            class$ = class$("org.apache.log4j.pattern.IntegerPatternConverter");
            class$org$apache$log4j$pattern$IntegerPatternConverter = class$;
        } else {
            class$ = class$org$apache$log4j$pattern$IntegerPatternConverter;
        }
        fnameRules.put(str, class$);
        FILENAME_PATTERN_RULES = new ReadOnlyMap(fnameRules);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private PatternParser() {
    }

    public static Map getPatternLayoutRules() {
        return PATTERN_LAYOUT_RULES;
    }

    public static Map getFileNamePatternRules() {
        return FILENAME_PATTERN_RULES;
    }

    private static int extractConverter(char lastChar, String pattern, int i, StringBuffer convBuf, StringBuffer currentLiteral) {
        convBuf.setLength(0);
        if (!Character.isUnicodeIdentifierStart(lastChar)) {
            return i;
        }
        convBuf.append(lastChar);
        while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
            convBuf.append(pattern.charAt(i));
            currentLiteral.append(pattern.charAt(i));
            i++;
        }
        return i;
    }

    private static int extractOptions(String pattern, int i, List options) {
        while (i < pattern.length() && pattern.charAt(i) == '{') {
            int end = pattern.indexOf(125, i);
            if (end == -1) {
                break;
            }
            options.add(pattern.substring(i + 1, end));
            i = end + 1;
        }
        return i;
    }

    public static void parse(java.lang.String r13, java.util.List r14, java.util.List r15, java.util.Map r16, java.util.Map r17) {
        /*
        if (r13 != 0) goto L_0x000a;
    L_0x0002:
        r1 = new java.lang.NullPointerException;
        r5 = "pattern";
        r1.<init>(r5);
        throw r1;
    L_0x000a:
        r3 = new java.lang.StringBuffer;
        r1 = 32;
        r3.<init>(r1);
        r11 = r13.length();
        r12 = 0;
        r2 = 0;
        r4 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r10 = r2;
    L_0x001c:
        if (r10 >= r11) goto L_0x0175;
    L_0x001e:
        r2 = r10 + 1;
        r0 = r13.charAt(r10);
        switch(r12) {
            case 0: goto L_0x0029;
            case 1: goto L_0x006b;
            case 2: goto L_0x0027;
            case 3: goto L_0x00f4;
            case 4: goto L_0x00b3;
            case 5: goto L_0x013b;
            default: goto L_0x0027;
        };
    L_0x0027:
        r10 = r2;
        goto L_0x001c;
    L_0x0029:
        if (r2 != r11) goto L_0x0030;
    L_0x002b:
        r3.append(r0);
        r10 = r2;
        goto L_0x001c;
    L_0x0030:
        r1 = 37;
        if (r0 != r1) goto L_0x0067;
    L_0x0034:
        r1 = r13.charAt(r2);
        switch(r1) {
            case 37: goto L_0x0061;
            default: goto L_0x003b;
        };
    L_0x003b:
        r1 = r3.length();
        if (r1 == 0) goto L_0x0054;
    L_0x0041:
        r1 = new org.apache.log4j.pattern.LiteralPatternConverter;
        r5 = r3.toString();
        r1.m2118init(r5);
        r14.add(r1);
        r1 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r15.add(r1);
    L_0x0054:
        r1 = 0;
        r3.setLength(r1);
        r3.append(r0);
        r12 = 1;
        r4 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        goto L_0x0027;
    L_0x0061:
        r3.append(r0);
        r2 = r2 + 1;
        goto L_0x0027;
    L_0x0067:
        r3.append(r0);
        goto L_0x0027;
    L_0x006b:
        r3.append(r0);
        switch(r0) {
            case 45: goto L_0x008b;
            case 46: goto L_0x009b;
            default: goto L_0x0071;
        };
    L_0x0071:
        r1 = 48;
        if (r0 < r1) goto L_0x009d;
    L_0x0075:
        r1 = 57;
        if (r0 > r1) goto L_0x009d;
    L_0x0079:
        r9 = new org.apache.log4j.pattern.FormattingInfo;
        r1 = r4.isLeftAligned();
        r5 = r0 + -48;
        r6 = r4.getMaxLength();
        r9.m1194init(r1, r5, r6);
        r12 = 4;
        r4 = r9;
        goto L_0x0027;
    L_0x008b:
        r9 = new org.apache.log4j.pattern.FormattingInfo;
        r1 = 1;
        r5 = r4.getMinLength();
        r6 = r4.getMaxLength();
        r9.m1194init(r1, r5, r6);
        r4 = r9;
        goto L_0x0027;
    L_0x009b:
        r12 = 3;
        goto L_0x0027;
    L_0x009d:
        r1 = r13;
        r5 = r16;
        r6 = r17;
        r7 = r14;
        r8 = r15;
        r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8);
        r12 = 0;
        r4 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r1 = 0;
        r3.setLength(r1);
        goto L_0x0027;
    L_0x00b3:
        r3.append(r0);
        r1 = 48;
        if (r0 < r1) goto L_0x00d7;
    L_0x00ba:
        r1 = 57;
        if (r0 > r1) goto L_0x00d7;
    L_0x00be:
        r9 = new org.apache.log4j.pattern.FormattingInfo;
        r1 = r4.isLeftAligned();
        r5 = r4.getMinLength();
        r5 = r5 * 10;
        r6 = r0 + -48;
        r5 = r5 + r6;
        r6 = r4.getMaxLength();
        r9.m1194init(r1, r5, r6);
        r4 = r9;
        goto L_0x0027;
    L_0x00d7:
        r1 = 46;
        if (r0 != r1) goto L_0x00de;
    L_0x00db:
        r12 = 3;
        goto L_0x0027;
    L_0x00de:
        r1 = r13;
        r5 = r16;
        r6 = r17;
        r7 = r14;
        r8 = r15;
        r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8);
        r12 = 0;
        r4 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r1 = 0;
        r3.setLength(r1);
        goto L_0x0027;
    L_0x00f4:
        r3.append(r0);
        r1 = 48;
        if (r0 < r1) goto L_0x0112;
    L_0x00fb:
        r1 = 57;
        if (r0 > r1) goto L_0x0112;
    L_0x00ff:
        r9 = new org.apache.log4j.pattern.FormattingInfo;
        r1 = r4.isLeftAligned();
        r5 = r4.getMinLength();
        r6 = r0 + -48;
        r9.m1194init(r1, r5, r6);
        r12 = 5;
        r4 = r9;
        goto L_0x0027;
    L_0x0112:
        r1 = new java.lang.StringBuffer;
        r1.<init>();
        r5 = "Error occured in position ";
        r1 = r1.append(r5);
        r1 = r1.append(r2);
        r5 = ".\n Was expecting digit, instead got char \"";
        r1 = r1.append(r5);
        r1 = r1.append(r0);
        r5 = "\".";
        r1 = r1.append(r5);
        r1 = r1.toString();
        org.apache.log4j.helpers.LogLog.error(r1);
        r12 = 0;
        goto L_0x0027;
    L_0x013b:
        r3.append(r0);
        r1 = 48;
        if (r0 < r1) goto L_0x015f;
    L_0x0142:
        r1 = 57;
        if (r0 > r1) goto L_0x015f;
    L_0x0146:
        r9 = new org.apache.log4j.pattern.FormattingInfo;
        r1 = r4.isLeftAligned();
        r5 = r4.getMinLength();
        r6 = r4.getMaxLength();
        r6 = r6 * 10;
        r7 = r0 + -48;
        r6 = r6 + r7;
        r9.m1194init(r1, r5, r6);
        r4 = r9;
        goto L_0x0027;
    L_0x015f:
        r1 = r13;
        r5 = r16;
        r6 = r17;
        r7 = r14;
        r8 = r15;
        r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8);
        r12 = 0;
        r4 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r1 = 0;
        r3.setLength(r1);
        goto L_0x0027;
    L_0x0175:
        r1 = r3.length();
        if (r1 == 0) goto L_0x018e;
    L_0x017b:
        r1 = new org.apache.log4j.pattern.LiteralPatternConverter;
        r5 = r3.toString();
        r1.m2118init(r5);
        r14.add(r1);
        r1 = org.apache.log4j.pattern.FormattingInfo.getDefault();
        r15.add(r1);
    L_0x018e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.pattern.PatternParser.parse(java.lang.String, java.util.List, java.util.List, java.util.Map, java.util.Map):void");
    }

    private static PatternConverter createConverter(String converterId, StringBuffer currentLiteral, Map converterRegistry, Map rules, List options) {
        String converterName = converterId;
        int i = converterId.length();
        Class cls = null;
        while (i > 0 && cls == null) {
            Class cls2;
            converterName = converterName.substring(0, i);
            if (converterRegistry != null) {
                cls2 = converterRegistry.get(converterName);
            } else {
                cls2 = cls;
            }
            if (cls2 == null && rules != null) {
                cls2 = rules.get(converterName);
            }
            i--;
            cls = cls2;
        }
        if (cls == null) {
            LogLog.error(new StringBuffer().append("Unrecognized format specifier [").append(converterId).append("]").toString());
            return null;
        }
        Class converterClass;
        if (cls instanceof Class) {
            converterClass = cls;
        } else if (cls instanceof String) {
            try {
                converterClass = Loader.loadClass((String) cls);
            } catch (ClassNotFoundException ex) {
                LogLog.warn(new StringBuffer().append("Class for conversion pattern %").append(converterName).append(" not found").toString(), ex);
                return null;
            }
        } else {
            LogLog.warn(new StringBuffer().append("Bad map entry for conversion pattern %").append(converterName).append(".").toString());
            return null;
        }
        try {
            List list = options;
            Object newObj = converterClass.getMethod("newInstance", new Class[]{Class.forName("[Ljava.lang.String;")}).invoke(null, new Object[]{(String[]) list.toArray(new String[options.size()])});
            if (newObj instanceof PatternConverter) {
                currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                return (PatternConverter) newObj;
            }
            LogLog.warn(new StringBuffer().append("Class ").append(converterClass.getName()).append(" does not extend PatternConverter.").toString());
            return null;
        } catch (Exception ex2) {
            LogLog.error(new StringBuffer().append("Error creating converter for ").append(converterId).toString(), ex2);
            try {
                PatternConverter pc = (PatternConverter) converterClass.newInstance();
                currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                return pc;
            } catch (Exception ex22) {
                LogLog.error(new StringBuffer().append("Error creating converter for ").append(converterId).toString(), ex22);
            }
        }
    }

    private static int finalizeConverter(char c, String pattern, int i, StringBuffer currentLiteral, FormattingInfo formattingInfo, Map converterRegistry, Map rules, List patternConverters, List formattingInfos) {
        StringBuffer convBuf = new StringBuffer();
        i = extractConverter(c, pattern, i, convBuf, currentLiteral);
        String converterId = convBuf.toString();
        List options = new ArrayList();
        i = extractOptions(pattern, i, options);
        PatternConverter pc = createConverter(converterId, currentLiteral, converterRegistry, rules, options);
        if (pc == null) {
            StringBuffer msg;
            if (converterId == null || converterId.length() == 0) {
                msg = new StringBuffer("Empty conversion specifier starting at position ");
            } else {
                msg = new StringBuffer("Unrecognized conversion specifier [");
                msg.append(converterId);
                msg.append("] starting at position ");
            }
            msg.append(Integer.toString(i));
            msg.append(" in conversion pattern.");
            LogLog.error(msg.toString());
            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        } else {
            patternConverters.add(pc);
            formattingInfos.add(formattingInfo);
            if (currentLiteral.length() > 0) {
                patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
                formattingInfos.add(FormattingInfo.getDefault());
            }
        }
        currentLiteral.setLength(0);
        return i;
    }
}
