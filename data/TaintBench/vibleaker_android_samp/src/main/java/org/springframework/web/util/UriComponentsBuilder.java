package org.springframework.web.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class UriComponentsBuilder {
    private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";
    private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";
    private static final String HOST_PATTERN = "(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)";
    private static final String HTTP_PATTERN = "(?i)(http|https):";
    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?(.*))?");
    private static final String LAST_PATTERN = "(.*)";
    private static final String PATH_PATTERN = "([^?#]*)";
    private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";
    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
    private static final String QUERY_PATTERN = "([^#]*)";
    private static final String SCHEME_PATTERN = "([^:/?#]+):";
    private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
    private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";
    private String fragment;
    private String host;
    private CompositePathComponentBuilder pathBuilder = new CompositePathComponentBuilder();
    private String port;
    private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
    private String scheme;
    private String ssp;
    private String userInfo;

    private interface PathComponentBuilder {
        PathComponent build();
    }

    private static class CompositePathComponentBuilder implements PathComponentBuilder {
        private final LinkedList<PathComponentBuilder> componentBuilders = new LinkedList();

        public CompositePathComponentBuilder(String path) {
            addPath(path);
        }

        public void addPathSegments(String... pathSegments) {
            if (!ObjectUtils.isEmpty(pathSegments)) {
                PathSegmentComponentBuilder psBuilder = (PathSegmentComponentBuilder) getLastBuilder(PathSegmentComponentBuilder.class);
                FullPathComponentBuilder fpBuilder = (FullPathComponentBuilder) getLastBuilder(FullPathComponentBuilder.class);
                if (psBuilder == null) {
                    psBuilder = new PathSegmentComponentBuilder();
                    this.componentBuilders.add(psBuilder);
                    if (fpBuilder != null) {
                        fpBuilder.removeTrailingSlash();
                    }
                }
                psBuilder.append(pathSegments);
            }
        }

        public void addPath(String path) {
            if (StringUtils.hasText(path)) {
                FullPathComponentBuilder fpBuilder = (FullPathComponentBuilder) getLastBuilder(FullPathComponentBuilder.class);
                if (!(((PathSegmentComponentBuilder) getLastBuilder(PathSegmentComponentBuilder.class)) == null || path.startsWith("/"))) {
                    path = "/" + path;
                }
                if (fpBuilder == null) {
                    fpBuilder = new FullPathComponentBuilder();
                    this.componentBuilders.add(fpBuilder);
                }
                fpBuilder.append(path);
            }
        }

        private <T> T getLastBuilder(Class<T> builderClass) {
            if (!this.componentBuilders.isEmpty()) {
                PathComponentBuilder last = (PathComponentBuilder) this.componentBuilders.getLast();
                if (builderClass.isInstance(last)) {
                    return last;
                }
            }
            return null;
        }

        public PathComponent build() {
            List<PathComponent> components = new ArrayList(this.componentBuilders.size());
            Iterator i$ = this.componentBuilders.iterator();
            while (i$.hasNext()) {
                PathComponent pathComponent = ((PathComponentBuilder) i$.next()).build();
                if (pathComponent != null) {
                    components.add(pathComponent);
                }
            }
            if (components.isEmpty()) {
                return HierarchicalUriComponents.NULL_PATH_COMPONENT;
            }
            if (components.size() == 1) {
                return (PathComponent) components.get(0);
            }
            return new PathComponentComposite(components);
        }
    }

    private static class FullPathComponentBuilder implements PathComponentBuilder {
        private final StringBuilder path;

        private FullPathComponentBuilder() {
            this.path = new StringBuilder();
        }

        public void append(String path) {
            this.path.append(path);
        }

        public PathComponent build() {
            if (this.path.length() == 0) {
                return null;
            }
            String path = this.path.toString();
            while (true) {
                int index = path.indexOf("//");
                if (index == -1) {
                    return new FullPathComponent(path);
                }
                path = path.substring(0, index) + path.substring(index + 1);
            }
        }

        public void removeTrailingSlash() {
            int index = this.path.length() - 1;
            if (this.path.charAt(index) == '/') {
                this.path.deleteCharAt(index);
            }
        }
    }

    private static class PathSegmentComponentBuilder implements PathComponentBuilder {
        private final List<String> pathSegments;

        private PathSegmentComponentBuilder() {
            this.pathSegments = new LinkedList();
        }

        public void append(String... pathSegments) {
            for (String pathSegment : pathSegments) {
                if (StringUtils.hasText(pathSegment)) {
                    this.pathSegments.add(pathSegment);
                }
            }
        }

        public PathComponent build() {
            return this.pathSegments.isEmpty() ? null : new PathSegmentComponent(this.pathSegments);
        }
    }

    protected UriComponentsBuilder() {
    }

    public static UriComponentsBuilder newInstance() {
        return new UriComponentsBuilder();
    }

    public static UriComponentsBuilder fromPath(String path) {
        UriComponentsBuilder builder = new UriComponentsBuilder();
        builder.path(path);
        return builder;
    }

    public static UriComponentsBuilder fromUri(URI uri) {
        UriComponentsBuilder builder = new UriComponentsBuilder();
        builder.uri(uri);
        return builder;
    }

    public static UriComponentsBuilder fromUriString(String uri) {
        Assert.hasLength(uri, "'uri' must not be empty");
        Matcher matcher = URI_PATTERN.matcher(uri);
        if (matcher.matches()) {
            UriComponentsBuilder builder = new UriComponentsBuilder();
            String scheme = matcher.group(2);
            String userInfo = matcher.group(5);
            String host = matcher.group(6);
            String port = matcher.group(8);
            String path = matcher.group(9);
            String query = matcher.group(11);
            String fragment = matcher.group(13);
            boolean opaque = false;
            if (StringUtils.hasLength(scheme) && !uri.substring(scheme.length()).startsWith(":/")) {
                opaque = true;
            }
            builder.scheme(scheme);
            if (opaque) {
                String ssp = uri.substring(scheme.length()).substring(1);
                if (StringUtils.hasLength(fragment)) {
                    ssp = ssp.substring(0, ssp.length() - (fragment.length() + 1));
                }
                builder.schemeSpecificPart(ssp);
            } else {
                builder.userInfo(userInfo);
                builder.host(host);
                if (StringUtils.hasLength(port)) {
                    builder.port(port);
                }
                builder.path(path);
                builder.query(query);
            }
            if (StringUtils.hasText(fragment)) {
                builder.fragment(fragment);
            }
            return builder;
        }
        throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
    }

    public static UriComponentsBuilder fromHttpUrl(String httpUrl) {
        Assert.notNull(httpUrl, "'httpUrl' must not be null");
        Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
        if (matcher.matches()) {
            UriComponentsBuilder builder = new UriComponentsBuilder();
            String scheme = matcher.group(1);
            builder.scheme(scheme != null ? scheme.toLowerCase() : null);
            builder.userInfo(matcher.group(4));
            String host = matcher.group(5);
            if (!StringUtils.hasLength(scheme) || StringUtils.hasLength(host)) {
                builder.host(host);
                String port = matcher.group(7);
                if (StringUtils.hasLength(port)) {
                    builder.port(port);
                }
                builder.path(matcher.group(8));
                builder.query(matcher.group(10));
                return builder;
            }
            throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
        }
        throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
    }

    public UriComponents build() {
        return build(false);
    }

    public UriComponents build(boolean encoded) {
        if (this.ssp != null) {
            return new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
        }
        return new HierarchicalUriComponents(this.scheme, this.userInfo, this.host, this.port, this.pathBuilder.build(), this.queryParams, this.fragment, encoded, true);
    }

    public UriComponents buildAndExpand(Map<String, ?> uriVariables) {
        return build(false).expand((Map) uriVariables);
    }

    public UriComponents buildAndExpand(Object... uriVariableValues) {
        return build(false).expand(uriVariableValues);
    }

    public String toUriString() {
        return build(false).encode().toUriString();
    }

    public UriComponentsBuilder uri(URI uri) {
        Assert.notNull(uri, "'uri' must not be null");
        this.scheme = uri.getScheme();
        if (uri.isOpaque()) {
            this.ssp = uri.getRawSchemeSpecificPart();
            resetHierarchicalComponents();
        } else {
            if (uri.getRawUserInfo() != null) {
                this.userInfo = uri.getRawUserInfo();
            }
            if (uri.getHost() != null) {
                this.host = uri.getHost();
            }
            if (uri.getPort() != -1) {
                this.port = String.valueOf(uri.getPort());
            }
            if (StringUtils.hasLength(uri.getRawPath())) {
                this.pathBuilder = new CompositePathComponentBuilder(uri.getRawPath());
            }
            if (StringUtils.hasLength(uri.getRawQuery())) {
                this.queryParams.clear();
                query(uri.getRawQuery());
            }
            resetSchemeSpecificPart();
        }
        if (uri.getRawFragment() != null) {
            this.fragment = uri.getRawFragment();
        }
        return this;
    }

    private void resetHierarchicalComponents() {
        this.userInfo = null;
        this.host = null;
        this.port = null;
        this.pathBuilder = new CompositePathComponentBuilder();
        this.queryParams.clear();
    }

    private void resetSchemeSpecificPart() {
        this.ssp = null;
    }

    public UriComponentsBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public UriComponentsBuilder uriComponents(UriComponents uriComponents) {
        Assert.notNull(uriComponents, "'uriComponents' must not be null");
        this.scheme = uriComponents.getScheme();
        if (uriComponents instanceof OpaqueUriComponents) {
            this.ssp = uriComponents.getSchemeSpecificPart();
            resetHierarchicalComponents();
        } else {
            if (uriComponents.getUserInfo() != null) {
                this.userInfo = uriComponents.getUserInfo();
            }
            if (uriComponents.getHost() != null) {
                this.host = uriComponents.getHost();
            }
            if (uriComponents.getPort() != -1) {
                this.port = String.valueOf(uriComponents.getPort());
            }
            if (StringUtils.hasLength(uriComponents.getPath())) {
                List<String> segments = uriComponents.getPathSegments();
                if (segments.isEmpty()) {
                    this.pathBuilder.addPath(uriComponents.getPath());
                } else {
                    this.pathBuilder.addPathSegments((String[]) segments.toArray(new String[segments.size()]));
                }
            }
            if (!uriComponents.getQueryParams().isEmpty()) {
                this.queryParams.clear();
                this.queryParams.putAll(uriComponents.getQueryParams());
            }
            resetSchemeSpecificPart();
        }
        if (uriComponents.getFragment() != null) {
            this.fragment = uriComponents.getFragment();
        }
        return this;
    }

    public UriComponentsBuilder schemeSpecificPart(String ssp) {
        this.ssp = ssp;
        resetHierarchicalComponents();
        return this;
    }

    public UriComponentsBuilder userInfo(String userInfo) {
        this.userInfo = userInfo;
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder host(String host) {
        this.host = host;
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder port(int port) {
        Assert.isTrue(port >= -1, "'port' must not be < -1");
        this.port = String.valueOf(port);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder port(String port) {
        this.port = port;
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder path(String path) {
        this.pathBuilder.addPath(path);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder replacePath(String path) {
        this.pathBuilder = new CompositePathComponentBuilder(path);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder pathSegment(String... pathSegments) throws IllegalArgumentException {
        Assert.notNull(pathSegments, "'segments' must not be null");
        this.pathBuilder.addPathSegments(pathSegments);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder query(String query) {
        if (query != null) {
            Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
            while (matcher.find()) {
                String name = matcher.group(1);
                String eq = matcher.group(2);
                String value = matcher.group(3);
                Object[] objArr = new Object[1];
                if (value == null) {
                    value = StringUtils.hasLength(eq) ? "" : null;
                }
                objArr[0] = value;
                queryParam(name, objArr);
            }
        } else {
            this.queryParams.clear();
        }
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder replaceQuery(String query) {
        this.queryParams.clear();
        query(query);
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder queryParam(String name, Object... values) {
        Assert.notNull(name, "'name' must not be null");
        if (ObjectUtils.isEmpty(values)) {
            this.queryParams.add(name, null);
        } else {
            for (Object value : values) {
                String valueAsString;
                if (value != null) {
                    valueAsString = value.toString();
                } else {
                    valueAsString = null;
                }
                this.queryParams.add(name, valueAsString);
            }
        }
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder queryParams(MultiValueMap<String, String> params) {
        Assert.notNull(params, "'params' must not be null");
        this.queryParams.putAll(params);
        return this;
    }

    public UriComponentsBuilder replaceQueryParam(String name, Object... values) {
        Assert.notNull(name, "'name' must not be null");
        this.queryParams.remove(name);
        if (!ObjectUtils.isEmpty(values)) {
            queryParam(name, values);
        }
        resetSchemeSpecificPart();
        return this;
    }

    public UriComponentsBuilder fragment(String fragment) {
        if (fragment != null) {
            Assert.hasLength(fragment, "'fragment' must not be empty");
            this.fragment = fragment;
        } else {
            this.fragment = null;
        }
        return this;
    }
}
