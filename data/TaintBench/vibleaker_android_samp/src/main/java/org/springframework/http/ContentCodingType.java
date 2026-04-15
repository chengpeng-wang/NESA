package org.springframework.http;

import android.support.v4.media.TransportMediator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;

public class ContentCodingType implements Comparable<ContentCodingType> {
    public static final ContentCodingType ALL = valueOf("*");
    public static final String ALL_VALUE = "*";
    public static final ContentCodingType GZIP = valueOf(GZIP_VALUE);
    public static final String GZIP_VALUE = "gzip";
    public static final ContentCodingType IDENTITY = valueOf(IDENTITY_VALUE);
    public static final String IDENTITY_VALUE = "identity";
    private static final String PARAM_QUALITY_FACTOR = "q";
    public static final Comparator<ContentCodingType> QUALITY_VALUE_COMPARATOR = new Comparator<ContentCodingType>() {
        public int compare(ContentCodingType codingType1, ContentCodingType codingType2) {
            int qualityComparison = Double.compare(codingType2.getQualityValue(), codingType1.getQualityValue());
            if (qualityComparison != 0) {
                return qualityComparison;
            }
            if (codingType1.isWildcardType() && !codingType2.isWildcardType()) {
                return 1;
            }
            if (codingType2.isWildcardType() && !codingType1.isWildcardType()) {
                return -1;
            }
            if (codingType1.getType().equals(codingType2.getType())) {
                return 0;
            }
            return 0;
        }
    };
    private static final BitSet TOKEN = new BitSet(128);
    private static final String WILDCARD_TYPE = "*";
    private final Map<String, String> parameters;
    private final String type;

    static {
        BitSet ctl = new BitSet(128);
        for (int i = 0; i <= 31; i++) {
            ctl.set(i);
        }
        ctl.set(TransportMediator.KEYCODE_MEDIA_PAUSE);
        BitSet separators = new BitSet(128);
        separators.set(40);
        separators.set(41);
        separators.set(60);
        separators.set(62);
        separators.set(64);
        separators.set(44);
        separators.set(59);
        separators.set(58);
        separators.set(92);
        separators.set(34);
        separators.set(47);
        separators.set(91);
        separators.set(93);
        separators.set(63);
        separators.set(61);
        separators.set(123);
        separators.set(125);
        separators.set(32);
        separators.set(9);
        TOKEN.set(0, 128);
        TOKEN.andNot(ctl);
        TOKEN.andNot(separators);
    }

    public ContentCodingType(String type) {
        this(type, Collections.emptyMap());
    }

    public ContentCodingType(String type, double qualityValue) {
        this(type, Collections.singletonMap(PARAM_QUALITY_FACTOR, Double.toString(qualityValue)));
    }

    public ContentCodingType(String type, Map<String, String> parameters) {
        Assert.hasLength(type, "'type' must not be empty");
        checkToken(type);
        this.type = type.toLowerCase(Locale.ENGLISH);
        if (CollectionUtils.isEmpty((Map) parameters)) {
            this.parameters = Collections.emptyMap();
            return;
        }
        Map<String, String> m = new LinkedCaseInsensitiveMap(parameters.size(), Locale.ENGLISH);
        for (Entry<String, String> entry : parameters.entrySet()) {
            String attribute = (String) entry.getKey();
            String value = (String) entry.getValue();
            checkParameters(attribute, value);
            m.put(attribute, unquote(value));
        }
        this.parameters = Collections.unmodifiableMap(m);
    }

    private void checkToken(String s) {
        int i = 0;
        while (i < s.length()) {
            char ch = s.charAt(i);
            if (TOKEN.get(ch)) {
                i++;
            } else {
                throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + s + "\"");
            }
        }
    }

    private void checkParameters(String attribute, String value) {
        Assert.hasLength(attribute, "parameter attribute must not be empty");
        Assert.hasLength(value, "parameter value must not be empty");
        checkToken(attribute);
        if (PARAM_QUALITY_FACTOR.equals(attribute)) {
            value = unquote(value);
            double d = Double.parseDouble(value);
            boolean z = d >= 0.0d && d <= 1.0d;
            Assert.isTrue(z, "Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
        } else if (!isQuotedString(value)) {
            checkToken(value);
        }
    }

    private boolean isQuotedString(String s) {
        return s.length() > 1 && s.startsWith("\"") && s.endsWith("\"");
    }

    private String unquote(String s) {
        if (s == null) {
            return null;
        }
        return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
    }

    public String getType() {
        return this.type;
    }

    public boolean isWildcardType() {
        return "*".equals(this.type);
    }

    public double getQualityValue() {
        String qualityFactory = getParameter(PARAM_QUALITY_FACTOR);
        return qualityFactory != null ? Double.parseDouble(qualityFactory) : 1.0d;
    }

    public String getParameter(String name) {
        return (String) this.parameters.get(name);
    }

    public boolean includes(ContentCodingType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType()) {
            return true;
        }
        if (this.type.equals(other.type)) {
            return true;
        }
        return false;
    }

    public boolean isCompatibleWith(ContentCodingType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType() || other.isWildcardType()) {
            return true;
        }
        if (this.type.equals(other.type)) {
            return true;
        }
        return false;
    }

    public int compareTo(ContentCodingType other) {
        int comp = this.type.compareToIgnoreCase(other.type);
        if (comp != 0) {
            return comp;
        }
        comp = this.parameters.size() - other.parameters.size();
        if (comp != 0) {
            return comp;
        }
        TreeSet<String> thisAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        thisAttributes.addAll(this.parameters.keySet());
        TreeSet<String> otherAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        otherAttributes.addAll(other.parameters.keySet());
        Iterator<String> thisAttributesIterator = thisAttributes.iterator();
        Iterator<String> otherAttributesIterator = otherAttributes.iterator();
        while (thisAttributesIterator.hasNext()) {
            String thisAttribute = (String) thisAttributesIterator.next();
            String otherAttribute = (String) otherAttributesIterator.next();
            comp = thisAttribute.compareToIgnoreCase(otherAttribute);
            if (comp != 0) {
                return comp;
            }
            String thisValue = (String) this.parameters.get(thisAttribute);
            String otherValue = (String) other.parameters.get(otherAttribute);
            if (otherValue == null) {
                otherValue = "";
            }
            comp = thisValue.compareTo(otherValue);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentCodingType)) {
            return false;
        }
        ContentCodingType otherType = (ContentCodingType) other;
        if (this.type.equalsIgnoreCase(otherType.type) && this.parameters.equals(otherType.parameters)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.type.hashCode() * 31) + this.parameters.hashCode();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        appendTo(builder);
        return builder.toString();
    }

    private void appendTo(StringBuilder builder) {
        builder.append(this.type);
        appendTo(this.parameters, builder);
    }

    private void appendTo(Map<String, String> map, StringBuilder builder) {
        for (Entry<String, String> entry : map.entrySet()) {
            builder.append(';');
            builder.append((String) entry.getKey());
            builder.append('=');
            builder.append((String) entry.getValue());
        }
    }

    public static ContentCodingType valueOf(String value) {
        return parseCodingType(value);
    }

    public static ContentCodingType parseCodingType(String codingType) {
        Assert.hasLength(codingType, "'codingType' must not be empty");
        String[] parts = StringUtils.tokenizeToStringArray(codingType, ";");
        String type = parts[0].trim();
        Map parameters = null;
        if (parts.length > 1) {
            parameters = new LinkedHashMap(parts.length - 1);
            for (int i = 1; i < parts.length; i++) {
                String parameter = parts[i];
                int eqIndex = parameter.indexOf(61);
                if (eqIndex != -1) {
                    parameters.put(parameter.substring(0, eqIndex), parameter.substring(eqIndex + 1, parameter.length()));
                }
            }
        }
        return new ContentCodingType(type, parameters);
    }

    public static List<ContentCodingType> parseCodingTypes(String codingTypes) {
        if (!StringUtils.hasLength(codingTypes)) {
            return Collections.emptyList();
        }
        String[] tokens = codingTypes.split(",");
        List<ContentCodingType> result = new ArrayList(tokens.length);
        for (String token : tokens) {
            result.add(parseCodingType(token));
        }
        return result;
    }

    public static String toString(Collection<ContentCodingType> codingTypes) {
        StringBuilder builder = new StringBuilder();
        Iterator<ContentCodingType> iterator = codingTypes.iterator();
        while (iterator.hasNext()) {
            ((ContentCodingType) iterator.next()).appendTo(builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static void sortByQualityValue(List<ContentCodingType> codingTypes) {
        Assert.notNull(codingTypes, "'codingTypes' must not be null");
        if (codingTypes.size() > 1) {
            Collections.sort(codingTypes, QUALITY_VALUE_COMPARATOR);
        }
    }
}
