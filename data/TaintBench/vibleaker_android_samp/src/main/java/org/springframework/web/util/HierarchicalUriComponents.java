package org.springframework.web.util;

import android.support.v4.media.TransportMediator;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents.UriTemplateVariables;

final class HierarchicalUriComponents extends UriComponents {
    static final PathComponent NULL_PATH_COMPONENT = new PathComponent() {
        public String getPath() {
            return null;
        }

        public List<String> getPathSegments() {
            return Collections.emptyList();
        }

        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            return this;
        }

        public void verify() {
        }

        public PathComponent expand(UriTemplateVariables uriVariables) {
            return this;
        }

        public boolean equals(Object obj) {
            return this == obj;
        }

        public int hashCode() {
            return 42;
        }
    };
    private static final char PATH_DELIMITER = '/';
    private final boolean encoded;
    private final String host;
    private final PathComponent path;
    private final String port;
    private final MultiValueMap<String, String> queryParams;
    private final String userInfo;

    interface PathComponent extends Serializable {
        PathComponent encode(String str) throws UnsupportedEncodingException;

        PathComponent expand(UriTemplateVariables uriTemplateVariables);

        String getPath();

        List<String> getPathSegments();

        void verify();
    }

    static final class FullPathComponent implements PathComponent {
        private final String path;

        public FullPathComponent(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }

        public List<String> getPathSegments() {
            return Collections.unmodifiableList(Arrays.asList(StringUtils.tokenizeToStringArray(this.path, new String(new char[]{HierarchicalUriComponents.PATH_DELIMITER}))));
        }

        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            return new FullPathComponent(HierarchicalUriComponents.encodeUriComponent(getPath(), encoding, Type.PATH));
        }

        public void verify() {
            HierarchicalUriComponents.verifyUriComponent(this.path, Type.PATH);
        }

        public PathComponent expand(UriTemplateVariables uriVariables) {
            return new FullPathComponent(UriComponents.expandUriComponent(getPath(), uriVariables));
        }

        public boolean equals(Object obj) {
            return this == obj || ((obj instanceof FullPathComponent) && getPath().equals(((FullPathComponent) obj).getPath()));
        }

        public int hashCode() {
            return getPath().hashCode();
        }
    }

    static final class PathComponentComposite implements PathComponent {
        private final List<PathComponent> pathComponents;

        public PathComponentComposite(List<PathComponent> pathComponents) {
            this.pathComponents = pathComponents;
        }

        public String getPath() {
            StringBuilder pathBuilder = new StringBuilder();
            for (PathComponent pathComponent : this.pathComponents) {
                pathBuilder.append(pathComponent.getPath());
            }
            return pathBuilder.toString();
        }

        public List<String> getPathSegments() {
            List<String> result = new ArrayList();
            for (PathComponent pathComponent : this.pathComponents) {
                result.addAll(pathComponent.getPathSegments());
            }
            return result;
        }

        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            List<PathComponent> encodedComponents = new ArrayList(this.pathComponents.size());
            for (PathComponent pathComponent : this.pathComponents) {
                encodedComponents.add(pathComponent.encode(encoding));
            }
            return new PathComponentComposite(encodedComponents);
        }

        public void verify() {
            for (PathComponent pathComponent : this.pathComponents) {
                pathComponent.verify();
            }
        }

        public PathComponent expand(UriTemplateVariables uriVariables) {
            List<PathComponent> expandedComponents = new ArrayList(this.pathComponents.size());
            for (PathComponent pathComponent : this.pathComponents) {
                expandedComponents.add(pathComponent.expand(uriVariables));
            }
            return new PathComponentComposite(expandedComponents);
        }
    }

    static final class PathSegmentComponent implements PathComponent {
        private final List<String> pathSegments;

        public PathSegmentComponent(List<String> pathSegments) {
            this.pathSegments = Collections.unmodifiableList(new ArrayList(pathSegments));
        }

        public String getPath() {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(HierarchicalUriComponents.PATH_DELIMITER);
            Iterator<String> iterator = this.pathSegments.iterator();
            while (iterator.hasNext()) {
                pathBuilder.append((String) iterator.next());
                if (iterator.hasNext()) {
                    pathBuilder.append(HierarchicalUriComponents.PATH_DELIMITER);
                }
            }
            return pathBuilder.toString();
        }

        public List<String> getPathSegments() {
            return this.pathSegments;
        }

        public PathComponent encode(String encoding) throws UnsupportedEncodingException {
            List<String> pathSegments = getPathSegments();
            List<String> encodedPathSegments = new ArrayList(pathSegments.size());
            for (String pathSegment : pathSegments) {
                encodedPathSegments.add(HierarchicalUriComponents.encodeUriComponent(pathSegment, encoding, Type.PATH_SEGMENT));
            }
            return new PathSegmentComponent(encodedPathSegments);
        }

        public void verify() {
            for (String pathSegment : getPathSegments()) {
                HierarchicalUriComponents.verifyUriComponent(pathSegment, Type.PATH_SEGMENT);
            }
        }

        public PathComponent expand(UriTemplateVariables uriVariables) {
            List<String> pathSegments = getPathSegments();
            List<String> expandedPathSegments = new ArrayList(pathSegments.size());
            for (String pathSegment : pathSegments) {
                expandedPathSegments.add(UriComponents.expandUriComponent(pathSegment, uriVariables));
            }
            return new PathSegmentComponent(expandedPathSegments);
        }

        public boolean equals(Object obj) {
            return this == obj || ((obj instanceof PathSegmentComponent) && getPathSegments().equals(((PathSegmentComponent) obj).getPathSegments()));
        }

        public int hashCode() {
            return getPathSegments().hashCode();
        }
    }

    enum Type {
        SCHEME {
            public boolean isAllowed(int c) {
                return isAlpha(c) || isDigit(c) || 43 == c || 45 == c || 46 == c;
            }
        },
        AUTHORITY {
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c;
            }
        },
        USER_INFO {
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 58 == c;
            }
        },
        HOST_IPV4 {
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c);
            }
        },
        HOST_IPV6 {
            public boolean isAllowed(int c) {
                return isUnreserved(c) || isSubDelimiter(c) || 91 == c || 93 == c || 58 == c;
            }
        },
        PORT {
            public boolean isAllowed(int c) {
                return isDigit(c);
            }
        },
        PATH {
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c;
            }
        },
        PATH_SEGMENT {
            public boolean isAllowed(int c) {
                return isPchar(c);
            }
        },
        QUERY {
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c || 63 == c;
            }
        },
        QUERY_PARAM {
            public boolean isAllowed(int c) {
                if (61 == c || 43 == c || 38 == c) {
                    return false;
                }
                if (isPchar(c) || 47 == c || 63 == c) {
                    return true;
                }
                return false;
            }
        },
        FRAGMENT {
            public boolean isAllowed(int c) {
                return isPchar(c) || 47 == c || 63 == c;
            }
        };

        public abstract boolean isAllowed(int i);

        /* access modifiers changed from: protected */
        public boolean isAlpha(int c) {
            return (c >= 97 && c <= 122) || (c >= 65 && c <= 90);
        }

        /* access modifiers changed from: protected */
        public boolean isDigit(int c) {
            return c >= 48 && c <= 57;
        }

        /* access modifiers changed from: protected */
        public boolean isGenericDelimiter(int c) {
            return 58 == c || 47 == c || 63 == c || 35 == c || 91 == c || 93 == c || 64 == c;
        }

        /* access modifiers changed from: protected */
        public boolean isSubDelimiter(int c) {
            return 33 == c || 36 == c || 38 == c || 39 == c || 40 == c || 41 == c || 42 == c || 43 == c || 44 == c || 59 == c || 61 == c;
        }

        /* access modifiers changed from: protected */
        public boolean isReserved(char c) {
            return isGenericDelimiter(c) || isReserved(c);
        }

        /* access modifiers changed from: protected */
        public boolean isUnreserved(int c) {
            return isAlpha(c) || isDigit(c) || 45 == c || 46 == c || 95 == c || TransportMediator.KEYCODE_MEDIA_PLAY == c;
        }

        /* access modifiers changed from: protected */
        public boolean isPchar(int c) {
            return isUnreserved(c) || isSubDelimiter(c) || 58 == c || 64 == c;
        }
    }

    HierarchicalUriComponents(String scheme, String userInfo, String host, String port, PathComponent path, MultiValueMap<String, String> queryParams, String fragment, boolean encoded, boolean verify) {
        super(scheme, fragment);
        this.userInfo = userInfo;
        this.host = host;
        this.port = port;
        if (path == null) {
            path = NULL_PATH_COMPONENT;
        }
        this.path = path;
        if (queryParams == null) {
            queryParams = new LinkedMultiValueMap(0);
        }
        this.queryParams = CollectionUtils.unmodifiableMultiValueMap(queryParams);
        this.encoded = encoded;
        if (verify) {
            verify();
        }
    }

    public String getSchemeSpecificPart() {
        return null;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        if (this.port == null) {
            return -1;
        }
        if (!this.port.contains("{")) {
            return Integer.parseInt(this.port);
        }
        throw new IllegalStateException("The port contains a URI variable but has not been expanded yet: " + this.port);
    }

    public String getPath() {
        return this.path.getPath();
    }

    public List<String> getPathSegments() {
        return this.path.getPathSegments();
    }

    public String getQuery() {
        if (this.queryParams.isEmpty()) {
            return null;
        }
        StringBuilder queryBuilder = new StringBuilder();
        for (Entry<String, List<String>> entry : this.queryParams.entrySet()) {
            String name = (String) entry.getKey();
            Collection values = (List) entry.getValue();
            if (CollectionUtils.isEmpty(values)) {
                if (queryBuilder.length() != 0) {
                    queryBuilder.append('&');
                }
                queryBuilder.append(name);
            } else {
                for (Object value : values) {
                    if (queryBuilder.length() != 0) {
                        queryBuilder.append('&');
                    }
                    queryBuilder.append(name);
                    if (value != null) {
                        queryBuilder.append('=');
                        queryBuilder.append(value.toString());
                    }
                }
            }
        }
        return queryBuilder.toString();
    }

    public MultiValueMap<String, String> getQueryParams() {
        return this.queryParams;
    }

    public HierarchicalUriComponents encode(String encoding) throws UnsupportedEncodingException {
        Assert.hasLength(encoding, "Encoding must not be empty");
        if (this.encoded) {
            return this;
        }
        String encodedScheme = encodeUriComponent(getScheme(), encoding, Type.SCHEME);
        String encodedUserInfo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
        String encodedHost = encodeUriComponent(this.host, encoding, getHostType());
        PathComponent encodedPath = this.path.encode(encoding);
        MultiValueMap<String, String> encodedQueryParams = new LinkedMultiValueMap(this.queryParams.size());
        for (Entry<String, List<String>> entry : this.queryParams.entrySet()) {
            String encodedName = encodeUriComponent((String) entry.getKey(), encoding, Type.QUERY_PARAM);
            List<String> encodedValues = new ArrayList(((List) entry.getValue()).size());
            for (String value : (List) entry.getValue()) {
                encodedValues.add(encodeUriComponent(value, encoding, Type.QUERY_PARAM));
            }
            encodedQueryParams.put(encodedName, encodedValues);
        }
        return new HierarchicalUriComponents(encodedScheme, encodedUserInfo, encodedHost, this.port, encodedPath, encodedQueryParams, encodeUriComponent(getFragment(), encoding, Type.FRAGMENT), true, false);
    }

    static String encodeUriComponent(String source, String encoding, Type type) throws UnsupportedEncodingException {
        if (source == null) {
            return null;
        }
        Assert.hasLength(encoding, "Encoding must not be empty");
        return new String(encodeBytes(source.getBytes(encoding), type), "US-ASCII");
    }

    private static byte[] encodeBytes(byte[] source, Type type) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(type, "Type must not be null");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
        for (byte b : source) {
            byte b2;
            if (b2 < (byte) 0) {
                b2 = (byte) (b2 + 256);
            }
            if (type.isAllowed(b2)) {
                bos.write(b2);
            } else {
                bos.write(37);
                char hex1 = Character.toUpperCase(Character.forDigit((b2 >> 4) & 15, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b2 & 15, 16));
                bos.write(hex1);
                bos.write(hex2);
            }
        }
        return bos.toByteArray();
    }

    private Type getHostType() {
        return (this.host == null || !this.host.startsWith("[")) ? Type.HOST_IPV4 : Type.HOST_IPV6;
    }

    private void verify() {
        if (this.encoded) {
            verifyUriComponent(getScheme(), Type.SCHEME);
            verifyUriComponent(this.userInfo, Type.USER_INFO);
            verifyUriComponent(this.host, getHostType());
            this.path.verify();
            for (Entry<String, List<String>> entry : this.queryParams.entrySet()) {
                verifyUriComponent((String) entry.getKey(), Type.QUERY_PARAM);
                for (String value : (List) entry.getValue()) {
                    verifyUriComponent(value, Type.QUERY_PARAM);
                }
            }
            verifyUriComponent(getFragment(), Type.FRAGMENT);
        }
    }

    /* access modifiers changed from: private|static */
    public static void verifyUriComponent(String source, Type type) {
        if (source != null) {
            int length = source.length();
            int i = 0;
            while (i < length) {
                char ch = source.charAt(i);
                if (ch == '%') {
                    if (i + 2 < length) {
                        char hex1 = source.charAt(i + 1);
                        char hex2 = source.charAt(i + 2);
                        int u = Character.digit(hex1, 16);
                        int l = Character.digit(hex2, 16);
                        if (u == -1 || l == -1) {
                            throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                        }
                        i += 2;
                    } else {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                } else if (!type.isAllowed(ch)) {
                    throw new IllegalArgumentException("Invalid character '" + ch + "' for " + type.name() + " in \"" + source + "\"");
                }
                i++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public HierarchicalUriComponents expandInternal(UriTemplateVariables uriVariables) {
        Assert.state(!this.encoded, "Cannot expand an already encoded UriComponents object");
        String expandedScheme = UriComponents.expandUriComponent(getScheme(), uriVariables);
        String expandedUserInfo = UriComponents.expandUriComponent(this.userInfo, uriVariables);
        String expandedHost = UriComponents.expandUriComponent(this.host, uriVariables);
        String expandedPort = UriComponents.expandUriComponent(this.port, uriVariables);
        PathComponent expandedPath = this.path.expand(uriVariables);
        MultiValueMap<String, String> expandedQueryParams = new LinkedMultiValueMap(this.queryParams.size());
        for (Entry<String, List<String>> entry : this.queryParams.entrySet()) {
            String expandedName = UriComponents.expandUriComponent((String) entry.getKey(), uriVariables);
            List<String> expandedValues = new ArrayList(((List) entry.getValue()).size());
            for (String value : (List) entry.getValue()) {
                expandedValues.add(UriComponents.expandUriComponent(value, uriVariables));
            }
            expandedQueryParams.put(expandedName, expandedValues);
        }
        return new HierarchicalUriComponents(expandedScheme, expandedUserInfo, expandedHost, expandedPort, expandedPath, expandedQueryParams, UriComponents.expandUriComponent(getFragment(), uriVariables), false, false);
    }

    public UriComponents normalize() {
        return new HierarchicalUriComponents(getScheme(), this.userInfo, this.host, this.port, new FullPathComponent(StringUtils.cleanPath(getPath())), this.queryParams, getFragment(), this.encoded, false);
    }

    public String toUriString() {
        StringBuilder uriBuilder = new StringBuilder();
        if (getScheme() != null) {
            uriBuilder.append(getScheme());
            uriBuilder.append(':');
        }
        if (!(this.userInfo == null && this.host == null)) {
            uriBuilder.append("//");
            if (this.userInfo != null) {
                uriBuilder.append(this.userInfo);
                uriBuilder.append('@');
            }
            if (this.host != null) {
                uriBuilder.append(this.host);
            }
            if (getPort() != -1) {
                uriBuilder.append(':');
                uriBuilder.append(this.port);
            }
        }
        String path = getPath();
        if (StringUtils.hasLength(path)) {
            if (!(uriBuilder.length() == 0 || path.charAt(0) == PATH_DELIMITER)) {
                uriBuilder.append(PATH_DELIMITER);
            }
            uriBuilder.append(path);
        }
        String query = getQuery();
        if (query != null) {
            uriBuilder.append('?');
            uriBuilder.append(query);
        }
        if (getFragment() != null) {
            uriBuilder.append('#');
            uriBuilder.append(getFragment());
        }
        return uriBuilder.toString();
    }

    public URI toUri() {
        try {
            if (this.encoded) {
                return new URI(toString());
            }
            String path = getPath();
            if (!(!StringUtils.hasLength(path) || path.charAt(0) == PATH_DELIMITER || (getScheme() == null && getUserInfo() == null && getHost() == null && getPort() == -1))) {
                path = PATH_DELIMITER + path;
            }
            return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), getFragment());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HierarchicalUriComponents)) {
            return false;
        }
        HierarchicalUriComponents other = (HierarchicalUriComponents) obj;
        if (ObjectUtils.nullSafeEquals(getScheme(), other.getScheme()) && ObjectUtils.nullSafeEquals(getUserInfo(), other.getUserInfo()) && ObjectUtils.nullSafeEquals(getHost(), other.getHost()) && getPort() == other.getPort() && this.path.equals(other.path) && this.queryParams.equals(other.queryParams) && ObjectUtils.nullSafeEquals(getFragment(), other.getFragment())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((ObjectUtils.nullSafeHashCode(getScheme()) * 31) + ObjectUtils.nullSafeHashCode(this.userInfo)) * 31) + ObjectUtils.nullSafeHashCode(this.host)) * 31) + ObjectUtils.nullSafeHashCode(this.port)) * 31) + this.path.hashCode()) * 31) + this.queryParams.hashCode()) * 31) + ObjectUtils.nullSafeHashCode(getFragment());
    }
}
