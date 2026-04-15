package org.springframework.web.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

public abstract class UriUtils {
    private static final String HOST_PATTERN = "([^/?#:]*)";
    private static final String HTTP_PATTERN = "(http|https):";
    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(http|https):(//(([^@/]*)@)?([^/?#:]*)(:(\\d*))?)?([^?#]*)(\\?(.*))?");
    private static final String LAST_PATTERN = "(.*)";
    private static final String PATH_PATTERN = "([^?#]*)";
    private static final String PORT_PATTERN = "(\\d*)";
    private static final String QUERY_PATTERN = "([^#]*)";
    private static final String SCHEME_PATTERN = "([^:/?#]+):";
    private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@/]*)@)?([^/?#:]*)(:(\\d*))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
    private static final String USERINFO_PATTERN = "([^@/]*)";

    @Deprecated
    public static String encodeUri(String uri, String encoding) throws UnsupportedEncodingException {
        Assert.notNull(uri, "URI must not be null");
        Assert.hasLength(encoding, "Encoding must not be empty");
        Matcher matcher = URI_PATTERN.matcher(uri);
        if (matcher.matches()) {
            return encodeUriComponents(matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(6), matcher.group(8), matcher.group(9), matcher.group(11), matcher.group(13), encoding);
        }
        throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
    }

    @Deprecated
    public static String encodeHttpUrl(String httpUrl, String encoding) throws UnsupportedEncodingException {
        Assert.notNull(httpUrl, "HTTP URL must not be null");
        Assert.hasLength(encoding, "Encoding must not be empty");
        Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
        if (matcher.matches()) {
            return encodeUriComponents(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(5), matcher.group(7), matcher.group(8), matcher.group(10), null, encoding);
        }
        throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
    }

    @Deprecated
    public static String encodeUriComponents(String scheme, String authority, String userInfo, String host, String port, String path, String query, String fragment, String encoding) throws UnsupportedEncodingException {
        Assert.hasLength(encoding, "Encoding must not be empty");
        StringBuilder sb = new StringBuilder();
        if (scheme != null) {
            sb.append(encodeScheme(scheme, encoding));
            sb.append(':');
        }
        if (authority != null) {
            sb.append("//");
            if (userInfo != null) {
                sb.append(encodeUserInfo(userInfo, encoding));
                sb.append('@');
            }
            if (host != null) {
                sb.append(encodeHost(host, encoding));
            }
            if (port != null) {
                sb.append(':');
                sb.append(encodePort(port, encoding));
            }
        }
        sb.append(encodePath(path, encoding));
        if (query != null) {
            sb.append('?');
            sb.append(encodeQuery(query, encoding));
        }
        if (fragment != null) {
            sb.append('#');
            sb.append(encodeFragment(fragment, encoding));
        }
        return sb.toString();
    }

    public static String encodeScheme(String scheme, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(scheme, encoding, Type.SCHEME);
    }

    public static String encodeAuthority(String authority, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(authority, encoding, Type.AUTHORITY);
    }

    public static String encodeUserInfo(String userInfo, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(userInfo, encoding, Type.USER_INFO);
    }

    public static String encodeHost(String host, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(host, encoding, Type.HOST_IPV4);
    }

    public static String encodePort(String port, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(port, encoding, Type.PORT);
    }

    public static String encodePath(String path, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(path, encoding, Type.PATH);
    }

    public static String encodePathSegment(String segment, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(segment, encoding, Type.PATH_SEGMENT);
    }

    public static String encodeQuery(String query, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(query, encoding, Type.QUERY);
    }

    public static String encodeQueryParam(String queryParam, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(queryParam, encoding, Type.QUERY_PARAM);
    }

    public static String encodeFragment(String fragment, String encoding) throws UnsupportedEncodingException {
        return HierarchicalUriComponents.encodeUriComponent(fragment, encoding, Type.FRAGMENT);
    }

    public static String decode(String source, String encoding) throws UnsupportedEncodingException {
        Assert.notNull(source, "Source must not be null");
        Assert.hasLength(encoding, "Encoding must not be empty");
        int length = source.length();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        boolean changed = false;
        int i = 0;
        while (i < length) {
            int ch = source.charAt(i);
            if (ch != 37) {
                bos.write(ch);
            } else if (i + 2 < length) {
                char hex1 = source.charAt(i + 1);
                char hex2 = source.charAt(i + 2);
                int u = Character.digit(hex1, 16);
                int l = Character.digit(hex2, 16);
                if (u == -1 || l == -1) {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
                bos.write((char) ((u << 4) + l));
                i += 2;
                changed = true;
            } else {
                throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
            }
            i++;
        }
        return changed ? new String(bos.toByteArray(), encoding) : source;
    }
}
