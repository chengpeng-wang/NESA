package org.springframework.web.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

public abstract class UriComponents implements Serializable {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private final String fragment;
    private final String scheme;

    public interface UriTemplateVariables {
        public static final Object SKIP_VALUE = UriTemplateVariables.class;

        Object getValue(String str);
    }

    private static class MapTemplateVariables implements UriTemplateVariables {
        private final Map<String, ?> uriVariables;

        public MapTemplateVariables(Map<String, ?> uriVariables) {
            this.uriVariables = uriVariables;
        }

        public Object getValue(String name) {
            if (this.uriVariables.containsKey(name)) {
                return this.uriVariables.get(name);
            }
            throw new IllegalArgumentException("Map has no value for '" + name + "'");
        }
    }

    private static class VarArgsTemplateVariables implements UriTemplateVariables {
        private final Iterator<Object> valueIterator;

        public VarArgsTemplateVariables(Object... uriVariableValues) {
            this.valueIterator = Arrays.asList(uriVariableValues).iterator();
        }

        public Object getValue(String name) {
            if (this.valueIterator.hasNext()) {
                return this.valueIterator.next();
            }
            throw new IllegalArgumentException("Not enough variable values available to expand '" + name + "'");
        }
    }

    public abstract UriComponents encode(String str) throws UnsupportedEncodingException;

    public abstract UriComponents expandInternal(UriTemplateVariables uriTemplateVariables);

    public abstract String getHost();

    public abstract String getPath();

    public abstract List<String> getPathSegments();

    public abstract int getPort();

    public abstract String getQuery();

    public abstract MultiValueMap<String, String> getQueryParams();

    public abstract String getSchemeSpecificPart();

    public abstract String getUserInfo();

    public abstract UriComponents normalize();

    public abstract URI toUri();

    public abstract String toUriString();

    protected UriComponents(String scheme, String fragment) {
        this.scheme = scheme;
        this.fragment = fragment;
    }

    public final String getScheme() {
        return this.scheme;
    }

    public final String getFragment() {
        return this.fragment;
    }

    public final UriComponents encode() {
        try {
            return encode(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public final UriComponents expand(Map<String, ?> uriVariables) {
        Assert.notNull(uriVariables, "'uriVariables' must not be null");
        return expandInternal(new MapTemplateVariables(uriVariables));
    }

    public final UriComponents expand(Object... uriVariableValues) {
        Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
        return expandInternal(new VarArgsTemplateVariables(uriVariableValues));
    }

    public final UriComponents expand(UriTemplateVariables uriVariables) {
        Assert.notNull(uriVariables, "'uriVariables' must not be null");
        return expandInternal(uriVariables);
    }

    public final String toString() {
        return toUriString();
    }

    static String expandUriComponent(String source, UriTemplateVariables uriVariables) {
        if (source == null) {
            return null;
        }
        if (source.indexOf(123) == -1) {
            return source;
        }
        Matcher matcher = NAMES_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            Object variableValue = uriVariables.getValue(getVariableName(matcher.group(1)));
            if (!UriTemplateVariables.SKIP_VALUE.equals(variableValue)) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(getVariableValueAsString(variableValue)));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getVariableName(String match) {
        int colonIdx = match.indexOf(58);
        return colonIdx != -1 ? match.substring(0, colonIdx) : match;
    }

    private static String getVariableValueAsString(Object variableValue) {
        return variableValue != null ? variableValue.toString() : "";
    }
}
