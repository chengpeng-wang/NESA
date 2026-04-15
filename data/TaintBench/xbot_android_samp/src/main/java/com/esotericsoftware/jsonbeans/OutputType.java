package com.esotericsoftware.jsonbeans;

import java.util.regex.Pattern;

public enum OutputType {
    json,
    javascript,
    minimal;
    
    private static Pattern javascriptPattern;
    private static Pattern minimalNamePattern;
    private static Pattern minimalValuePattern;

    static {
        javascriptPattern = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
        minimalValuePattern = Pattern.compile("[a-zA-Z_$][^:}\\], ]*");
        minimalNamePattern = Pattern.compile("[a-zA-Z0-9_$][^:}\\], ]*");
    }

    public String quoteValue(Object obj) {
        if (obj == null || (obj instanceof Number) || (obj instanceof Boolean)) {
            return String.valueOf(obj);
        }
        String replace = String.valueOf(obj).replace("\\", "\\\\");
        return (this != minimal || replace.equals("true") || replace.equals("false") || replace.equals("null") || !minimalValuePattern.matcher(replace).matches()) ? '\"' + replace.replace("\"", "\\\"") + '\"' : replace;
    }

    public String quoteName(String str) {
        String replace = str.replace("\\", "\\\\");
        switch (this) {
            case minimal:
                if (minimalNamePattern.matcher(replace).matches()) {
                    return replace;
                }
                return '\"' + replace.replace("\"", "\\\"") + '\"';
            case javascript:
                if (javascriptPattern.matcher(replace).matches()) {
                    return replace;
                }
                return '\"' + replace.replace("\"", "\\\"") + '\"';
            default:
                return '\"' + replace.replace("\"", "\\\"") + '\"';
        }
    }
}
