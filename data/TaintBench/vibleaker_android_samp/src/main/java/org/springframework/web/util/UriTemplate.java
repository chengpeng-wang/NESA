package org.springframework.web.util;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class UriTemplate implements Serializable {
    private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
    /* access modifiers changed from: private|static|final */
    public static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
    private final Pattern matchPattern;
    private final UriComponents uriComponents;
    private final String uriTemplate;
    private final List<String> variableNames;

    private static class Parser {
        private final StringBuilder patternBuilder;
        private final List<String> variableNames;

        private Parser(String uriTemplate) {
            this.variableNames = new LinkedList();
            this.patternBuilder = new StringBuilder();
            Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
            Matcher matcher = UriTemplate.NAMES_PATTERN.matcher(uriTemplate);
            int end = 0;
            while (matcher.find()) {
                this.patternBuilder.append(quote(uriTemplate, end, matcher.start()));
                String match = matcher.group(1);
                int colonIdx = match.indexOf(58);
                if (colonIdx == -1) {
                    this.patternBuilder.append(UriTemplate.DEFAULT_VARIABLE_PATTERN);
                    this.variableNames.add(match);
                } else if (colonIdx + 1 == match.length()) {
                    throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + match + "\"");
                } else {
                    String variablePattern = match.substring(colonIdx + 1, match.length());
                    this.patternBuilder.append('(');
                    this.patternBuilder.append(variablePattern);
                    this.patternBuilder.append(')');
                    this.variableNames.add(match.substring(0, colonIdx));
                }
                end = matcher.end();
            }
            this.patternBuilder.append(quote(uriTemplate, end, uriTemplate.length()));
            int lastIdx = this.patternBuilder.length() - 1;
            if (lastIdx >= 0 && this.patternBuilder.charAt(lastIdx) == '/') {
                this.patternBuilder.deleteCharAt(lastIdx);
            }
        }

        private String quote(String fullPath, int start, int end) {
            if (start == end) {
                return "";
            }
            return Pattern.quote(fullPath.substring(start, end));
        }

        /* access modifiers changed from: private */
        public List<String> getVariableNames() {
            return Collections.unmodifiableList(this.variableNames);
        }

        /* access modifiers changed from: private */
        public Pattern getMatchPattern() {
            return Pattern.compile(this.patternBuilder.toString());
        }
    }

    public UriTemplate(String uriTemplate) {
        Parser parser = new Parser(uriTemplate);
        this.uriTemplate = uriTemplate;
        this.variableNames = parser.getVariableNames();
        this.matchPattern = parser.getMatchPattern();
        this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();
    }

    public List<String> getVariableNames() {
        return this.variableNames;
    }

    public URI expand(Map<String, ?> uriVariables) {
        return this.uriComponents.expand((Map) uriVariables).encode().toUri();
    }

    public URI expand(Object... uriVariableValues) {
        return this.uriComponents.expand(uriVariableValues).encode().toUri();
    }

    public boolean matches(String uri) {
        if (uri == null) {
            return false;
        }
        return this.matchPattern.matcher(uri).matches();
    }

    public Map<String, String> match(String uri) {
        Assert.notNull(uri, "'uri' must not be null");
        Map<String, String> result = new LinkedHashMap(this.variableNames.size());
        Matcher matcher = this.matchPattern.matcher(uri);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.put((String) this.variableNames.get(i - 1), matcher.group(i));
            }
        }
        return result;
    }

    public String toString() {
        return this.uriTemplate;
    }
}
