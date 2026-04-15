package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class JoinerMacro extends FunctionCallImplementation {
    private static final String ARG0 = Key.ARG0.toString();
    private static final String DEFAULT_ITEM_SEPARATOR = "";
    private static final String DEFAULT_KEY_VALUE_SEPARATOR = "=";
    private static final String ESCAPE = Key.ESCAPE.toString();
    private static final String ID = FunctionType.JOINER.toString();
    private static final String ITEM_SEPARATOR = Key.ITEM_SEPARATOR.toString();
    private static final String KEY_VALUE_SEPARATOR = Key.KEY_VALUE_SEPARATOR.toString();

    private enum EscapeType {
        NONE,
        URL,
        BACKSLASH
    }

    public static String getFunctionId() {
        return ID;
    }

    public JoinerMacro() {
        super(ID, ARG0);
    }

    public boolean isCacheable() {
        return true;
    }

    public Value evaluate(Map<String, Value> parameters) {
        Value input = (Value) parameters.get(ARG0);
        if (input == null) {
            return Types.getDefaultValue();
        }
        Value itemSeparatorParameter = (Value) parameters.get(ITEM_SEPARATOR);
        String itemSeparator = itemSeparatorParameter != null ? Types.valueToString(itemSeparatorParameter) : DEFAULT_ITEM_SEPARATOR;
        Value keyValueSeparatorParameter = (Value) parameters.get(KEY_VALUE_SEPARATOR);
        String keyValueSeparator = keyValueSeparatorParameter != null ? Types.valueToString(keyValueSeparatorParameter) : DEFAULT_KEY_VALUE_SEPARATOR;
        EscapeType escapeType = EscapeType.NONE;
        Value escapeParameter = (Value) parameters.get(ESCAPE);
        Set<Character> charsToBackslashEscape = null;
        if (escapeParameter != null) {
            String escape = Types.valueToString(escapeParameter);
            if ("url".equals(escape)) {
                escapeType = EscapeType.URL;
            } else if ("backslash".equals(escape)) {
                escapeType = EscapeType.BACKSLASH;
                charsToBackslashEscape = new HashSet();
                addTo(charsToBackslashEscape, itemSeparator);
                addTo(charsToBackslashEscape, keyValueSeparator);
                charsToBackslashEscape.remove(Character.valueOf('\\'));
            } else {
                Log.e("Joiner: unsupported escape type: " + escape);
                return Types.getDefaultValue();
            }
        }
        StringBuilder sb = new StringBuilder();
        switch (input.getType()) {
            case LIST:
                boolean firstTime = true;
                for (Value itemVal : input.getListItemList()) {
                    if (!firstTime) {
                        sb.append(itemSeparator);
                    }
                    firstTime = false;
                    append(sb, Types.valueToString(itemVal), escapeType, charsToBackslashEscape);
                }
                break;
            case MAP:
                for (int i = 0; i < input.getMapKeyCount(); i++) {
                    if (i > 0) {
                        sb.append(itemSeparator);
                    }
                    String key = Types.valueToString(input.getMapKey(i));
                    String value = Types.valueToString(input.getMapValue(i));
                    append(sb, key, escapeType, charsToBackslashEscape);
                    sb.append(keyValueSeparator);
                    append(sb, value, escapeType, charsToBackslashEscape);
                }
                break;
            default:
                append(sb, Types.valueToString(input), escapeType, charsToBackslashEscape);
                break;
        }
        return Types.objectToValue(sb.toString());
    }

    private void addTo(Set<Character> set, String string) {
        for (int i = 0; i < string.length(); i++) {
            set.add(Character.valueOf(string.charAt(i)));
        }
    }

    private void append(StringBuilder sb, String s, EscapeType escapeType, Set<Character> charsToBackslashEscape) {
        sb.append(escape(s, escapeType, charsToBackslashEscape));
    }

    private String escape(String s, EscapeType escapeType, Set<Character> charsToBackslashEscape) {
        switch (escapeType) {
            case URL:
                try {
                    return ValueEscapeUtil.urlEncode(s);
                } catch (UnsupportedEncodingException e) {
                    Log.e("Joiner: unsupported encoding", e);
                    return s;
                }
            case BACKSLASH:
                s = s.replace("\\", "\\\\");
                for (Character c : charsToBackslashEscape) {
                    String charAsString = c.toString();
                    s = s.replace(charAsString, "\\" + charAsString);
                }
                return s;
            default:
                return s;
        }
    }
}
