package org.springframework.http;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class HttpHeaders implements MultiValueMap<String, String>, Serializable {
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String DATE = "Date";
    private static final String[] DATE_FORMATS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy"};
    public static final String ETAG = "ETag";
    public static final String EXPECT = "Expect";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    private static TimeZone GMT = TimeZone.getTimeZone("GMT");
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LINK = "Link";
    public static final String LOCATION = "Location";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
    public static final String TE = "TE";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final long serialVersionUID = -8578554704772377436L;
    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        Assert.notNull(headers, "'headers' must not be null");
        if (readOnly) {
            Map<String, List<String>> map = new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
            for (Entry<String, List<String>> entry : headers.entrySet()) {
                map.put(entry.getKey(), Collections.unmodifiableList((List) entry.getValue()));
            }
            this.headers = Collections.unmodifiableMap(map);
            return;
        }
        this.headers = headers;
    }

    public void setAccept(List<MediaType> acceptableMediaTypes) {
        set(ACCEPT, MediaType.toString(acceptableMediaTypes));
    }

    public List<MediaType> getAccept() {
        String value = getFirst(ACCEPT);
        List<MediaType> result = value != null ? MediaType.parseMediaTypes(value) : Collections.emptyList();
        if (result.size() != 1) {
            return result;
        }
        List<String> acceptHeader = get(ACCEPT);
        if (acceptHeader.size() > 1) {
            return MediaType.parseMediaTypes(StringUtils.collectionToCommaDelimitedString(acceptHeader));
        }
        return result;
    }

    public void setAcceptCharset(List<Charset> acceptableCharsets) {
        StringBuilder builder = new StringBuilder();
        Iterator<Charset> iterator = acceptableCharsets.iterator();
        while (iterator.hasNext()) {
            builder.append(((Charset) iterator.next()).name().toLowerCase(Locale.ENGLISH));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        set(ACCEPT_CHARSET, builder.toString());
    }

    public List<Charset> getAcceptCharset() {
        List<Charset> result = new ArrayList();
        String value = getFirst(ACCEPT_CHARSET);
        if (value != null) {
            for (String token : value.split(",\\s*")) {
                String charsetName;
                int paramIdx = token.indexOf(59);
                if (paramIdx == -1) {
                    charsetName = token;
                } else {
                    charsetName = token.substring(0, paramIdx);
                }
                if (!charsetName.equals(ContentCodingType.ALL_VALUE)) {
                    result.add(Charset.forName(charsetName));
                }
            }
        }
        return result;
    }

    public void setAcceptEncoding(List<ContentCodingType> acceptableEncodingTypes) {
        set(ACCEPT_ENCODING, ContentCodingType.toString(acceptableEncodingTypes));
    }

    public void setAcceptEncoding(ContentCodingType acceptableEncodingType) {
        setAcceptEncoding(Collections.singletonList(acceptableEncodingType));
    }

    public List<ContentCodingType> getAcceptEncoding() {
        String value = getFirst(ACCEPT_ENCODING);
        return value != null ? ContentCodingType.parseCodingTypes(value) : Collections.emptyList();
    }

    public void setAcceptLanguage(String acceptLanguage) {
        set(ACCEPT_LANGUAGE, acceptLanguage);
    }

    public String getAcceptLanguage() {
        return getFirst(ACCEPT_LANGUAGE);
    }

    public void setAllow(Set<HttpMethod> allowedMethods) {
        set(ALLOW, StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public Set<HttpMethod> getAllow() {
        String value = getFirst(ALLOW);
        if (StringUtils.isEmpty(value)) {
            return EnumSet.noneOf(HttpMethod.class);
        }
        List<HttpMethod> allowedMethod = new ArrayList(5);
        for (String token : value.split(",\\s*")) {
            allowedMethod.add(HttpMethod.valueOf(token));
        }
        return EnumSet.copyOf(allowedMethod);
    }

    public void setAuthorization(HttpAuthentication httpAuthentication) {
        set(AUTHORIZATION, httpAuthentication.getHeaderValue());
    }

    public String getAuthorization() {
        return getFirst(AUTHORIZATION);
    }

    public void setCacheControl(String cacheControl) {
        set(CACHE_CONTROL, cacheControl);
    }

    public String getCacheControl() {
        return getFirst(CACHE_CONTROL);
    }

    public void setConnection(String connection) {
        set(CONNECTION, connection);
    }

    public void setConnection(List<String> connection) {
        set(CONNECTION, toCommaDelimitedString(connection));
    }

    public List<String> getConnection() {
        return getFirstValueAsList(CONNECTION);
    }

    public void setContentDispositionFormData(String name, String filename) {
        Assert.notNull(name, "'name' must not be null");
        StringBuilder builder = new StringBuilder("form-data; name=\"");
        builder.append(name).append('\"');
        if (filename != null) {
            builder.append("; filename=\"");
            builder.append(filename).append('\"');
        }
        set(CONTENT_DISPOSITION, builder.toString());
    }

    public void setContentEncoding(List<ContentCodingType> contentEncodingTypes) {
        set(CONTENT_ENCODING, ContentCodingType.toString(contentEncodingTypes));
    }

    public void setContentEncoding(ContentCodingType contentEncodingType) {
        setContentEncoding(Collections.singletonList(contentEncodingType));
    }

    public List<ContentCodingType> getContentEncoding() {
        String value = getFirst(CONTENT_ENCODING);
        return value != null ? ContentCodingType.parseCodingTypes(value) : Collections.emptyList();
    }

    public void setContentLength(long contentLength) {
        set(CONTENT_LENGTH, Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = getFirst(CONTENT_LENGTH);
        return value != null ? Long.parseLong(value) : -1;
    }

    public void setContentType(MediaType mediaType) {
        boolean z;
        boolean z2 = true;
        if (mediaType.isWildcardType()) {
            z = false;
        } else {
            z = true;
        }
        Assert.isTrue(z, "'Content-Type' cannot contain wildcard type '*'");
        if (mediaType.isWildcardSubtype()) {
            z2 = false;
        }
        Assert.isTrue(z2, "'Content-Type' cannot contain wildcard subtype '*'");
        set(CONTENT_TYPE, mediaType.toString());
    }

    public MediaType getContentType() {
        String value = getFirst(CONTENT_TYPE);
        return StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
    }

    public void setDate(long date) {
        setDate(DATE, date);
    }

    public long getDate() {
        return getFirstDate(DATE);
    }

    public void setETag(String eTag) {
        if (eTag != null) {
            boolean z = eTag.startsWith("\"") || eTag.startsWith("W/");
            Assert.isTrue(z, "Invalid eTag, does not start with W/ or \"");
            Assert.isTrue(eTag.endsWith("\""), "Invalid eTag, does not end with \"");
        }
        set(ETAG, eTag);
    }

    public String getETag() {
        return getFirst(ETAG);
    }

    public void setExpires(long expires) {
        setDate(EXPIRES, expires);
    }

    public long getExpires() {
        try {
            return getFirstDate(EXPIRES);
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    public void setIfModifiedSince(long ifModifiedSince) {
        setDate(IF_MODIFIED_SINCE, ifModifiedSince);
    }

    @Deprecated
    public long getIfNotModifiedSince() {
        return getIfModifiedSince();
    }

    public long getIfModifiedSince() {
        return getFirstDate(IF_MODIFIED_SINCE);
    }

    public void setIfNoneMatch(String ifNoneMatch) {
        set(IF_NONE_MATCH, ifNoneMatch);
    }

    public void setIfNoneMatch(List<String> ifNoneMatchList) {
        set(IF_NONE_MATCH, toCommaDelimitedString(ifNoneMatchList));
    }

    /* access modifiers changed from: protected */
    public String toCommaDelimitedString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            builder.append((String) iterator.next());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public List<String> getIfNoneMatch() {
        return getFirstValueAsList(IF_NONE_MATCH);
    }

    /* access modifiers changed from: protected */
    public List<String> getFirstValueAsList(String header) {
        List<String> result = new ArrayList();
        String value = getFirst(header);
        if (value != null) {
            for (String token : value.split(",\\s*")) {
                result.add(token);
            }
        }
        return result;
    }

    public void setLastModified(long lastModified) {
        setDate(LAST_MODIFIED, lastModified);
    }

    public long getLastModified() {
        return getFirstDate(LAST_MODIFIED);
    }

    public void setLocation(URI location) {
        set(LOCATION, location.toASCIIString());
    }

    public URI getLocation() {
        String value = getFirst(LOCATION);
        return value != null ? URI.create(value) : null;
    }

    public void setOrigin(String origin) {
        set(ORIGIN, origin);
    }

    public String getOrigin() {
        return getFirst(ORIGIN);
    }

    public void setPragma(String pragma) {
        set(PRAGMA, pragma);
    }

    public String getPragma() {
        return getFirst(PRAGMA);
    }

    public void setUserAgent(String userAgent) {
        set(USER_AGENT, userAgent);
    }

    public String getUserAgent() {
        return getFirst(USER_AGENT);
    }

    public void setUpgrade(String upgrade) {
        set(UPGRADE, upgrade);
    }

    public String getUpgrade() {
        return getFirst(UPGRADE);
    }

    public long getFirstDate(String headerName) {
        String headerValue = getFirst(headerName);
        if (headerValue == null) {
            return -1;
        }
        String[] arr$ = DATE_FORMATS;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(arr$[i$], Locale.US);
            simpleDateFormat.setTimeZone(GMT);
            try {
                return simpleDateFormat.parse(headerValue).getTime();
            } catch (ParseException e) {
                i$++;
            }
        }
        throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
    }

    public void setDate(String headerName, long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
        dateFormat.setTimeZone(GMT);
        set(headerName, dateFormat.format(new Date(date)));
    }

    public String getFirst(String headerName) {
        List<String> headerValues = (List) this.headers.get(headerName);
        return headerValues != null ? (String) headerValues.get(0) : null;
    }

    public void add(String headerName, String headerValue) {
        List<String> headerValues = (List) this.headers.get(headerName);
        if (headerValues == null) {
            headerValues = new LinkedList();
            this.headers.put(headerName, headerValues);
        }
        headerValues.add(headerValue);
    }

    public void set(String headerName, String headerValue) {
        List<String> headerValues = new LinkedList();
        headerValues.add(headerValue);
        this.headers.put(headerName, headerValues);
    }

    public void setAll(Map<String, String> values) {
        for (Entry<String, String> entry : values.entrySet()) {
            set((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public Map<String, String> toSingleValueMap() {
        LinkedHashMap<String, String> singleValueMap = new LinkedHashMap(this.headers.size());
        for (Entry<String, List<String>> entry : this.headers.entrySet()) {
            singleValueMap.put(entry.getKey(), ((List) entry.getValue()).get(0));
        }
        return singleValueMap;
    }

    public int size() {
        return this.headers.size();
    }

    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    public List<String> get(Object key) {
        return (List) this.headers.get(key);
    }

    public List<String> put(String key, List<String> value) {
        return (List) this.headers.put(key, value);
    }

    public List<String> remove(Object key) {
        return (List) this.headers.remove(key);
    }

    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    public void clear() {
        this.headers.clear();
    }

    public Set<String> keySet() {
        return this.headers.keySet();
    }

    public Collection<List<String>> values() {
        return this.headers.values();
    }

    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpHeaders)) {
            return false;
        }
        return this.headers.equals(((HttpHeaders) other).headers);
    }

    public int hashCode() {
        return this.headers.hashCode();
    }

    public String toString() {
        return this.headers.toString();
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        return new HttpHeaders(headers, true);
    }
}
