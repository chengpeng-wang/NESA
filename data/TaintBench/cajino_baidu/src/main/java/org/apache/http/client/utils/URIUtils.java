package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Stack;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.log4j.spi.LocationInfo;

@Immutable
public class URIUtils {
    public static URI createURI(String scheme, String host, int port, String path, String query, String fragment) throws URISyntaxException {
        StringBuilder buffer = new StringBuilder();
        if (host != null) {
            if (scheme != null) {
                buffer.append(scheme);
                buffer.append("://");
            }
            buffer.append(host);
            if (port > 0) {
                buffer.append(':');
                buffer.append(port);
            }
        }
        if (path == null || !path.startsWith("/")) {
            buffer.append('/');
        }
        if (path != null) {
            buffer.append(path);
        }
        if (query != null) {
            buffer.append('?');
            buffer.append(query);
        }
        if (fragment != null) {
            buffer.append('#');
            buffer.append(fragment);
        }
        return new URI(buffer.toString());
    }

    public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
        if (uri == null) {
            throw new IllegalArgumentException("URI may nor be null");
        } else if (target != null) {
            return createURI(target.getSchemeName(), target.getHostName(), target.getPort(), normalizePath(uri.getRawPath()), uri.getRawQuery(), dropFragment ? null : uri.getRawFragment());
        } else {
            return createURI(null, null, -1, normalizePath(uri.getRawPath()), uri.getRawQuery(), dropFragment ? null : uri.getRawFragment());
        }
    }

    private static String normalizePath(String path) {
        if (path == null) {
            return null;
        }
        int n = 0;
        while (n < path.length() && path.charAt(n) == '/') {
            n++;
        }
        if (n > 1) {
            return path.substring(n - 1);
        }
        return path;
    }

    public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
        return rewriteURI(uri, target, false);
    }

    public static URI resolve(URI baseURI, String reference) {
        return resolve(baseURI, URI.create(reference));
    }

    public static URI resolve(URI baseURI, URI reference) {
        if (baseURI == null) {
            throw new IllegalArgumentException("Base URI may nor be null");
        } else if (reference == null) {
            throw new IllegalArgumentException("Reference URI may nor be null");
        } else {
            String s = reference.toString();
            if (s.startsWith(LocationInfo.NA)) {
                return resolveReferenceStartingWithQueryString(baseURI, reference);
            }
            boolean emptyReference;
            if (s.length() == 0) {
                emptyReference = true;
            } else {
                emptyReference = false;
            }
            if (emptyReference) {
                reference = URI.create("#");
            }
            URI resolved = baseURI.resolve(reference);
            if (emptyReference) {
                String resolvedString = resolved.toString();
                resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf(35)));
            }
            return removeDotSegments(resolved);
        }
    }

    private static URI resolveReferenceStartingWithQueryString(URI baseURI, URI reference) {
        String baseUri = baseURI.toString();
        if (baseUri.indexOf(63) > -1) {
            baseUri = baseUri.substring(0, baseUri.indexOf(63));
        }
        return URI.create(baseUri + reference.toString());
    }

    private static URI removeDotSegments(URI uri) {
        String path = uri.getPath();
        if (path == null || path.indexOf("/.") == -1) {
            return uri;
        }
        String[] inputSegments = path.split("/");
        Stack<String> outputSegments = new Stack();
        int i = 0;
        while (i < inputSegments.length) {
            if (!(inputSegments[i].length() == 0 || ".".equals(inputSegments[i]))) {
                if (!"..".equals(inputSegments[i])) {
                    outputSegments.push(inputSegments[i]);
                } else if (!outputSegments.isEmpty()) {
                    outputSegments.pop();
                }
            }
            i++;
        }
        StringBuilder outputBuffer = new StringBuilder();
        Iterator i$ = outputSegments.iterator();
        while (i$.hasNext()) {
            outputBuffer.append('/').append((String) i$.next());
        }
        try {
            return new URI(uri.getScheme(), uri.getAuthority(), outputBuffer.toString(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static HttpHost extractHost(URI uri) {
        if (uri == null) {
            return null;
        }
        if (!uri.isAbsolute()) {
            return null;
        }
        int port = uri.getPort();
        String host = uri.getHost();
        if (host == null) {
            host = uri.getAuthority();
            if (host != null) {
                int at = host.indexOf(64);
                if (at >= 0) {
                    if (host.length() > at + 1) {
                        host = host.substring(at + 1);
                    } else {
                        host = null;
                    }
                }
                if (host != null) {
                    int colon = host.indexOf(58);
                    if (colon >= 0) {
                        if (colon + 1 < host.length()) {
                            port = Integer.parseInt(host.substring(colon + 1));
                        }
                        host = host.substring(0, colon);
                    }
                }
            }
        }
        String scheme = uri.getScheme();
        if (host != null) {
            return new HttpHost(host, port, scheme);
        }
        return null;
    }

    private URIUtils() {
    }
}
