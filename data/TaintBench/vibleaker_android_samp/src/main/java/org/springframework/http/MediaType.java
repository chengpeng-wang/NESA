package org.springframework.http;

import android.support.v4.media.TransportMediator;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
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
import org.springframework.util.comparator.CompoundComparator;

public class MediaType implements Comparable<MediaType> {
    public static final MediaType ALL = valueOf(ALL_VALUE);
    public static final String ALL_VALUE = "*/*";
    public static final MediaType APPLICATION_ATOM_XML = valueOf(APPLICATION_ATOM_XML_VALUE);
    public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";
    public static final MediaType APPLICATION_FORM_URLENCODED = valueOf(APPLICATION_FORM_URLENCODED_VALUE);
    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
    public static final MediaType APPLICATION_JSON = valueOf(APPLICATION_JSON_VALUE);
    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final MediaType APPLICATION_OCTET_STREAM = valueOf(APPLICATION_OCTET_STREAM_VALUE);
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
    public static final MediaType APPLICATION_RSS_XML = valueOf(APPLICATION_RSS_XML_VALUE);
    public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml";
    public static final MediaType APPLICATION_WILDCARD_XML = valueOf(APPLICATION_WILDCARD_XML_VALUE);
    public static final String APPLICATION_WILDCARD_XML_VALUE = "application/*+xml";
    public static final MediaType APPLICATION_XHTML_XML = valueOf(APPLICATION_XHTML_XML_VALUE);
    public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
    public static final MediaType APPLICATION_XML = valueOf(APPLICATION_XML_VALUE);
    public static final String APPLICATION_XML_VALUE = "application/xml";
    public static final MediaType IMAGE_GIF = valueOf(IMAGE_GIF_VALUE);
    public static final String IMAGE_GIF_VALUE = "image/gif";
    public static final MediaType IMAGE_JPEG = valueOf(IMAGE_JPEG_VALUE);
    public static final String IMAGE_JPEG_VALUE = "image/jpeg";
    public static final MediaType IMAGE_PNG = valueOf(IMAGE_PNG_VALUE);
    public static final String IMAGE_PNG_VALUE = "image/png";
    public static final MediaType MULTIPART_FORM_DATA = valueOf(MULTIPART_FORM_DATA_VALUE);
    public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
    private static final String PARAM_CHARSET = "charset";
    private static final String PARAM_QUALITY_FACTOR = "q";
    public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR = new Comparator<MediaType>() {
        public int compare(MediaType mediaType1, MediaType mediaType2) {
            int qualityComparison = Double.compare(mediaType2.getQualityValue(), mediaType1.getQualityValue());
            if (qualityComparison != 0) {
                return qualityComparison;
            }
            if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) {
                return 1;
            }
            if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) {
                return -1;
            }
            if (!mediaType1.getType().equals(mediaType2.getType())) {
                return 0;
            }
            if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) {
                return 1;
            }
            if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) {
                return -1;
            }
            if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
                return 0;
            }
            int paramsSize1 = mediaType1.parameters.size();
            int paramsSize2 = mediaType2.parameters.size();
            if (paramsSize2 >= paramsSize1) {
                return paramsSize2 == paramsSize1 ? 0 : 1;
            } else {
                return -1;
            }
        }
    };
    public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = new Comparator<MediaType>() {
        public int compare(MediaType mediaType1, MediaType mediaType2) {
            if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) {
                return 1;
            }
            if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) {
                return -1;
            }
            if (!mediaType1.getType().equals(mediaType2.getType())) {
                return 0;
            }
            if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) {
                return 1;
            }
            if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) {
                return -1;
            }
            if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
                return 0;
            }
            int qualityComparison = Double.compare(mediaType2.getQualityValue(), mediaType1.getQualityValue());
            if (qualityComparison != 0) {
                return qualityComparison;
            }
            int paramsSize1 = mediaType1.parameters.size();
            int paramsSize2 = mediaType2.parameters.size();
            if (paramsSize2 >= paramsSize1) {
                return paramsSize2 == paramsSize1 ? 0 : 1;
            } else {
                return -1;
            }
        }
    };
    public static final MediaType TEXT_HTML = valueOf(TEXT_HTML_VALUE);
    public static final String TEXT_HTML_VALUE = "text/html";
    public static final MediaType TEXT_PLAIN = valueOf(TEXT_PLAIN_VALUE);
    public static final String TEXT_PLAIN_VALUE = "text/plain";
    public static final MediaType TEXT_XML = valueOf(TEXT_XML_VALUE);
    public static final String TEXT_XML_VALUE = "text/xml";
    private static final BitSet TOKEN = new BitSet(128);
    private static final String WILDCARD_TYPE = "*";
    /* access modifiers changed from: private|final */
    public final Map<String, String> parameters;
    private final String subtype;
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

    public MediaType(String type) {
        this(type, "*");
    }

    public MediaType(String type, String subtype) {
        this(type, subtype, Collections.emptyMap());
    }

    public MediaType(String type, String subtype, Charset charset) {
        this(type, subtype, Collections.singletonMap(PARAM_CHARSET, charset.name()));
    }

    public MediaType(String type, String subtype, double qualityValue) {
        this(type, subtype, Collections.singletonMap(PARAM_QUALITY_FACTOR, Double.toString(qualityValue)));
    }

    public MediaType(MediaType other, Map<String, String> parameters) {
        this(other.getType(), other.getSubtype(), (Map) parameters);
    }

    public MediaType(String type, String subtype, Map<String, String> parameters) {
        Assert.hasLength(type, "type must not be empty");
        Assert.hasLength(subtype, "subtype must not be empty");
        checkToken(type);
        checkToken(subtype);
        this.type = type.toLowerCase(Locale.ENGLISH);
        this.subtype = subtype.toLowerCase(Locale.ENGLISH);
        if (CollectionUtils.isEmpty((Map) parameters)) {
            this.parameters = Collections.emptyMap();
            return;
        }
        Map<String, String> m = new LinkedCaseInsensitiveMap(parameters.size(), Locale.ENGLISH);
        for (Entry<String, String> entry : parameters.entrySet()) {
            String attribute = (String) entry.getKey();
            String value = (String) entry.getValue();
            checkParameters(attribute, value);
            m.put(attribute, value);
        }
        this.parameters = Collections.unmodifiableMap(m);
    }

    private void checkToken(String token) {
        int i = 0;
        while (i < token.length()) {
            char ch = token.charAt(i);
            if (TOKEN.get(ch)) {
                i++;
            } else {
                throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
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
        } else if (PARAM_CHARSET.equals(attribute)) {
            Charset.forName(unquote(value));
        } else if (!isQuotedString(value)) {
            checkToken(value);
        }
    }

    private boolean isQuotedString(String s) {
        if (s.length() < 2) {
            return false;
        }
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            return true;
        }
        return false;
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

    public String getSubtype() {
        return this.subtype;
    }

    public boolean isWildcardSubtype() {
        return "*".equals(this.subtype) || this.subtype.startsWith("*+");
    }

    public boolean isConcrete() {
        return (isWildcardType() || isWildcardSubtype()) ? false : true;
    }

    public Charset getCharSet() {
        String charSet = getParameter(PARAM_CHARSET);
        return charSet != null ? Charset.forName(unquote(charSet)) : null;
    }

    public double getQualityValue() {
        String qualityFactory = getParameter(PARAM_QUALITY_FACTOR);
        return qualityFactory != null ? Double.parseDouble(unquote(qualityFactory)) : 1.0d;
    }

    public String getParameter(String name) {
        return (String) this.parameters.get(name);
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public boolean includes(MediaType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType()) {
            return true;
        }
        if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.subtype.equals(other.subtype)) {
            return true;
        }
        if (!isWildcardSubtype()) {
            return false;
        }
        int thisPlusIdx = this.subtype.indexOf(43);
        if (thisPlusIdx == -1) {
            return true;
        }
        int otherPlusIdx = other.subtype.indexOf(43);
        if (otherPlusIdx == -1) {
            return false;
        }
        String thisSubtypeNoSuffix = this.subtype.substring(0, thisPlusIdx);
        if (this.subtype.substring(thisPlusIdx + 1).equals(other.subtype.substring(otherPlusIdx + 1)) && "*".equals(thisSubtypeNoSuffix)) {
            return true;
        }
        return false;
    }

    public boolean isCompatibleWith(MediaType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType() || other.isWildcardType()) {
            return true;
        }
        if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.subtype.equals(other.subtype)) {
            return true;
        }
        if (!isWildcardSubtype() && !other.isWildcardSubtype()) {
            return false;
        }
        int thisPlusIdx = this.subtype.indexOf(43);
        int otherPlusIdx = other.subtype.indexOf(43);
        if (thisPlusIdx == -1 && otherPlusIdx == -1) {
            return true;
        }
        if (thisPlusIdx == -1 || otherPlusIdx == -1) {
            return false;
        }
        String thisSubtypeNoSuffix = this.subtype.substring(0, thisPlusIdx);
        String otherSubtypeNoSuffix = other.subtype.substring(0, otherPlusIdx);
        if (!this.subtype.substring(thisPlusIdx + 1).equals(other.subtype.substring(otherPlusIdx + 1))) {
            return false;
        }
        if ("*".equals(thisSubtypeNoSuffix) || "*".equals(otherSubtypeNoSuffix)) {
            return true;
        }
        return false;
    }

    public MediaType copyQualityValue(MediaType mediaType) {
        if (!mediaType.parameters.containsKey(PARAM_QUALITY_FACTOR)) {
            return this;
        }
        Map params = new LinkedHashMap(this.parameters);
        params.put(PARAM_QUALITY_FACTOR, mediaType.parameters.get(PARAM_QUALITY_FACTOR));
        return new MediaType(this, params);
    }

    public MediaType removeQualityValue() {
        if (!this.parameters.containsKey(PARAM_QUALITY_FACTOR)) {
            return this;
        }
        Map params = new LinkedHashMap(this.parameters);
        params.remove(PARAM_QUALITY_FACTOR);
        return new MediaType(this, params);
    }

    public int compareTo(MediaType other) {
        int comp = this.type.compareToIgnoreCase(other.type);
        if (comp != 0) {
            return comp;
        }
        comp = this.subtype.compareToIgnoreCase(other.subtype);
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
        if (!(other instanceof MediaType)) {
            return false;
        }
        MediaType otherType = (MediaType) other;
        if (this.type.equalsIgnoreCase(otherType.type) && this.subtype.equalsIgnoreCase(otherType.subtype) && this.parameters.equals(otherType.parameters)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((this.type.hashCode() * 31) + this.subtype.hashCode()) * 31) + this.parameters.hashCode();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        appendTo(builder);
        return builder.toString();
    }

    private void appendTo(StringBuilder builder) {
        builder.append(this.type);
        builder.append('/');
        builder.append(this.subtype);
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

    public static MediaType valueOf(String value) {
        return parseMediaType(value);
    }

    public static MediaType parseMediaType(String mediaType) {
        Assert.hasLength(mediaType, "'mediaType' must not be empty");
        String[] parts = StringUtils.tokenizeToStringArray(mediaType, ";");
        String fullType = parts[0].trim();
        if ("*".equals(fullType)) {
            fullType = ALL_VALUE;
        }
        int subIndex = fullType.indexOf(47);
        if (subIndex == -1) {
            throw new InvalidMediaTypeException(mediaType, "does not contain '/'");
        } else if (subIndex == fullType.length() - 1) {
            throw new InvalidMediaTypeException(mediaType, "does not contain subtype after '/'");
        } else {
            String type = fullType.substring(0, subIndex);
            String subtype = fullType.substring(subIndex + 1, fullType.length());
            if (!"*".equals(type) || "*".equals(subtype)) {
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
                try {
                    return new MediaType(type, subtype, parameters);
                } catch (UnsupportedCharsetException ex) {
                    throw new InvalidMediaTypeException(mediaType, "unsupported charset '" + ex.getCharsetName() + "'");
                } catch (IllegalArgumentException ex2) {
                    throw new InvalidMediaTypeException(mediaType, ex2.getMessage());
                }
            }
            throw new InvalidMediaTypeException(mediaType, "wildcard type is legal only in '*/*' (all media types)");
        }
    }

    public static List<MediaType> parseMediaTypes(String mediaTypes) {
        if (!StringUtils.hasLength(mediaTypes)) {
            return Collections.emptyList();
        }
        String[] tokens = mediaTypes.split(",\\s*");
        List<MediaType> result = new ArrayList(tokens.length);
        for (String token : tokens) {
            result.add(parseMediaType(token));
        }
        return result;
    }

    public static String toString(Collection<MediaType> mediaTypes) {
        StringBuilder builder = new StringBuilder();
        Iterator<MediaType> iterator = mediaTypes.iterator();
        while (iterator.hasNext()) {
            ((MediaType) iterator.next()).appendTo(builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static void sortBySpecificity(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            Collections.sort(mediaTypes, SPECIFICITY_COMPARATOR);
        }
    }

    public static void sortByQualityValue(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            Collections.sort(mediaTypes, QUALITY_VALUE_COMPARATOR);
        }
    }

    public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            Collections.sort(mediaTypes, new CompoundComparator(SPECIFICITY_COMPARATOR, QUALITY_VALUE_COMPARATOR));
        }
    }
}
