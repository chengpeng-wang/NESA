package org.jsoup.nodes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;

public class Entities {
    private static final Map<String, Character> base = loadEntities("entities-base.properties");
    /* access modifiers changed from: private|static|final */
    public static final Map<Character, String> baseByVal = toCharacterKey(base);
    private static final Map<String, Character> full = loadEntities("entities-full.properties");
    /* access modifiers changed from: private|static|final */
    public static final Map<Character, String> fullByVal = toCharacterKey(full);
    private static final Pattern strictUnescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);");
    private static final Pattern unescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);?");
    private static final Object[][] xhtmlArray;
    /* access modifiers changed from: private|static|final */
    public static final Map<Character, String> xhtmlByVal = new HashMap();

    public enum EscapeMode {
        xhtml(Entities.xhtmlByVal),
        base(Entities.baseByVal),
        extended(Entities.fullByVal);
        
        private Map<Character, String> map;

        private EscapeMode(Map<Character, String> map) {
            this.map = map;
        }

        public Map<Character, String> getMap() {
            return this.map;
        }
    }

    static {
        Object[][] objArr = new Object[5][];
        objArr[0] = new Object[]{"quot", Integer.valueOf(34)};
        objArr[1] = new Object[]{"amp", Integer.valueOf(38)};
        objArr[2] = new Object[]{"apos", Integer.valueOf(39)};
        objArr[3] = new Object[]{"lt", Integer.valueOf(60)};
        objArr[4] = new Object[]{"gt", Integer.valueOf(62)};
        xhtmlArray = objArr;
        for (Object[] entity : xhtmlArray) {
            xhtmlByVal.put(Character.valueOf((char) ((Integer) entity[1]).intValue()), (String) entity[0]);
        }
    }

    private Entities() {
    }

    public static boolean isNamedEntity(String name) {
        return full.containsKey(name);
    }

    public static boolean isBaseNamedEntity(String name) {
        return base.containsKey(name);
    }

    public static Character getCharacterByName(String name) {
        return (Character) full.get(name);
    }

    static String escape(String string, OutputSettings out) {
        return escape(string, out.encoder(), out.escapeMode());
    }

    static String escape(String string, CharsetEncoder encoder, EscapeMode escapeMode) {
        StringBuilder accum = new StringBuilder(string.length() * 2);
        Map<Character, String> map = escapeMode.getMap();
        int length = string.length();
        int offset = 0;
        while (offset < length) {
            int codePoint = string.codePointAt(offset);
            if (codePoint < 65536) {
                char c = (char) codePoint;
                if (map.containsKey(Character.valueOf(c))) {
                    accum.append('&').append((String) map.get(Character.valueOf(c))).append(';');
                } else if (encoder.canEncode(c)) {
                    accum.append(c);
                } else {
                    accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
                }
            } else {
                String c2 = new String(Character.toChars(codePoint));
                if (encoder.canEncode(c2)) {
                    accum.append(c2);
                } else {
                    accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
                }
            }
            offset += Character.charCount(codePoint);
        }
        return accum.toString();
    }

    static String unescape(String string) {
        return unescape(string, false);
    }

    static String unescape(String string, boolean strict) {
        return Parser.unescapeEntities(string, strict);
    }

    private static Map<String, Character> loadEntities(String filename) {
        Properties properties = new Properties();
        Map<String, Character> entities = new HashMap();
        try {
            InputStream in = Entities.class.getResourceAsStream(filename);
            properties.load(in);
            in.close();
            for (Entry entry : properties.entrySet()) {
                entities.put((String) entry.getKey(), Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16)));
            }
            return entities;
        } catch (IOException e) {
            throw new MissingResourceException("Error loading entities resource: " + e.getMessage(), "Entities", filename);
        }
    }

    private static Map<Character, String> toCharacterKey(Map<String, Character> inMap) {
        Map<Character, String> outMap = new HashMap();
        for (Entry<String, Character> entry : inMap.entrySet()) {
            Character character = (Character) entry.getValue();
            String name = (String) entry.getKey();
            if (!outMap.containsKey(character)) {
                outMap.put(character, name);
            } else if (name.toLowerCase().equals(name)) {
                outMap.put(character, name);
            }
        }
        return outMap;
    }
}
