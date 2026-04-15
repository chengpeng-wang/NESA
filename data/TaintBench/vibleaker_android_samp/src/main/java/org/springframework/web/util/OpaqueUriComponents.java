package org.springframework.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponents.UriTemplateVariables;

final class OpaqueUriComponents extends UriComponents {
    private static final MultiValueMap<String, String> QUERY_PARAMS_NONE = new LinkedMultiValueMap(0);
    private final String ssp;

    OpaqueUriComponents(String scheme, String schemeSpecificPart, String fragment) {
        super(scheme, fragment);
        this.ssp = schemeSpecificPart;
    }

    public String getSchemeSpecificPart() {
        return this.ssp;
    }

    public String getUserInfo() {
        return null;
    }

    public String getHost() {
        return null;
    }

    public int getPort() {
        return -1;
    }

    public String getPath() {
        return null;
    }

    public List<String> getPathSegments() {
        return Collections.emptyList();
    }

    public String getQuery() {
        return null;
    }

    public MultiValueMap<String, String> getQueryParams() {
        return QUERY_PARAMS_NONE;
    }

    public UriComponents encode(String encoding) throws UnsupportedEncodingException {
        return this;
    }

    /* access modifiers changed from: protected */
    public UriComponents expandInternal(UriTemplateVariables uriVariables) {
        return new OpaqueUriComponents(UriComponents.expandUriComponent(getScheme(), uriVariables), UriComponents.expandUriComponent(getSchemeSpecificPart(), uriVariables), UriComponents.expandUriComponent(getFragment(), uriVariables));
    }

    public UriComponents normalize() {
        return this;
    }

    public String toUriString() {
        StringBuilder uriBuilder = new StringBuilder();
        if (getScheme() != null) {
            uriBuilder.append(getScheme());
            uriBuilder.append(':');
        }
        if (this.ssp != null) {
            uriBuilder.append(this.ssp);
        }
        if (getFragment() != null) {
            uriBuilder.append('#');
            uriBuilder.append(getFragment());
        }
        return uriBuilder.toString();
    }

    public URI toUri() {
        try {
            return new URI(getScheme(), this.ssp, getFragment());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OpaqueUriComponents)) {
            return false;
        }
        OpaqueUriComponents other = (OpaqueUriComponents) obj;
        if (ObjectUtils.nullSafeEquals(getScheme(), other.getScheme()) && ObjectUtils.nullSafeEquals(this.ssp, other.ssp) && ObjectUtils.nullSafeEquals(getFragment(), other.getFragment())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((ObjectUtils.nullSafeHashCode(getScheme()) * 31) + ObjectUtils.nullSafeHashCode(this.ssp)) * 31) + ObjectUtils.nullSafeHashCode(getFragment());
    }
}
