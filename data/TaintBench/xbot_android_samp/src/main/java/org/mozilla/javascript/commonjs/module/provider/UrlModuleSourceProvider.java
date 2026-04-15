package org.mozilla.javascript.commonjs.module.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

public class UrlModuleSourceProvider extends ModuleSourceProviderBase {
    private static final long serialVersionUID = 1;
    private final Iterable<URI> fallbackUris;
    private final Iterable<URI> privilegedUris;
    private final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator;
    private final UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider;

    private static class URLValidator implements Serializable {
        private static final long serialVersionUID = 1;
        private final String entityTags;
        private long expiry;
        private final long lastModified;
        private final URI uri;

        public URLValidator(URI uri, URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
            this.uri = uri;
            this.lastModified = urlConnection.getLastModified();
            this.entityTags = getEntityTags(urlConnection);
            this.expiry = calculateExpiry(urlConnection, request_time, urlConnectionExpiryCalculator);
        }

        /* access modifiers changed from: 0000 */
        public boolean updateValidator(URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) throws IOException {
            boolean isResourceChanged = isResourceChanged(urlConnection);
            if (!isResourceChanged) {
                this.expiry = calculateExpiry(urlConnection, request_time, urlConnectionExpiryCalculator);
            }
            return isResourceChanged;
        }

        private boolean isResourceChanged(URLConnection urlConnection) throws IOException {
            if (urlConnection instanceof HttpURLConnection) {
                if (((HttpURLConnection) urlConnection).getResponseCode() == 304) {
                    return true;
                }
                return false;
            } else if (this.lastModified != urlConnection.getLastModified()) {
                return false;
            } else {
                return true;
            }
        }

        private long calculateExpiry(URLConnection urlConnection, long request_time, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
            if ("no-cache".equals(urlConnection.getHeaderField("Pragma"))) {
                return 0;
            }
            String cacheControl = urlConnection.getHeaderField("Cache-Control");
            if (cacheControl != null) {
                if (cacheControl.indexOf("no-cache") != -1) {
                    return 0;
                }
                int max_age = getMaxAge(cacheControl);
                if (-1 != max_age) {
                    long response_time = System.currentTimeMillis();
                    return (((long) max_age) * 1000) + (response_time - (Math.max(Math.max(0, response_time - urlConnection.getDate()), ((long) urlConnection.getHeaderFieldInt("Age", 0)) * 1000) + (response_time - request_time)));
                }
            }
            long explicitExpiry = urlConnection.getHeaderFieldDate("Expires", -1);
            if (explicitExpiry != -1) {
                return explicitExpiry;
            }
            long j;
            if (urlConnectionExpiryCalculator == null) {
                j = 0;
            } else {
                j = urlConnectionExpiryCalculator.calculateExpiry(urlConnection);
            }
            return j;
        }

        private int getMaxAge(String cacheControl) {
            int i = -1;
            int maxAgeIndex = cacheControl.indexOf("max-age");
            if (maxAgeIndex == i) {
                return i;
            }
            int eq = cacheControl.indexOf(61, maxAgeIndex + 7);
            if (eq == i) {
                return i;
            }
            String strAge;
            int comma = cacheControl.indexOf(44, eq + 1);
            if (comma == i) {
                strAge = cacheControl.substring(eq + 1);
            } else {
                strAge = cacheControl.substring(eq + 1, comma);
            }
            try {
                return Integer.parseInt(strAge);
            } catch (NumberFormatException e) {
                return i;
            }
        }

        private String getEntityTags(URLConnection urlConnection) {
            List<String> etags = (List) urlConnection.getHeaderFields().get("ETag");
            if (etags == null || etags.isEmpty()) {
                return null;
            }
            StringBuilder b = new StringBuilder();
            Iterator<String> it = etags.iterator();
            b.append((String) it.next());
            while (it.hasNext()) {
                b.append(", ").append((String) it.next());
            }
            return b.toString();
        }

        /* access modifiers changed from: 0000 */
        public boolean appliesTo(URI uri) {
            return this.uri.equals(uri);
        }

        /* access modifiers changed from: 0000 */
        public void applyConditionals(URLConnection urlConnection) {
            if (this.lastModified != 0) {
                urlConnection.setIfModifiedSince(this.lastModified);
            }
            if (this.entityTags != null && this.entityTags.length() > 0) {
                urlConnection.addRequestProperty("If-None-Match", this.entityTags);
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean entityNeedsRevalidation() {
            return System.currentTimeMillis() > this.expiry;
        }
    }

    public UrlModuleSourceProvider(Iterable<URI> privilegedUris, Iterable<URI> fallbackUris) {
        this(privilegedUris, fallbackUris, new DefaultUrlConnectionExpiryCalculator(), null);
    }

    public UrlModuleSourceProvider(Iterable<URI> privilegedUris, Iterable<URI> fallbackUris, UrlConnectionExpiryCalculator urlConnectionExpiryCalculator, UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider) {
        this.privilegedUris = privilegedUris;
        this.fallbackUris = fallbackUris;
        this.urlConnectionExpiryCalculator = urlConnectionExpiryCalculator;
        this.urlConnectionSecurityDomainProvider = urlConnectionSecurityDomainProvider;
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromPrivilegedLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
        return loadFromPathList(moduleId, validator, this.privilegedUris);
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromFallbackLocations(String moduleId, Object validator) throws IOException, URISyntaxException {
        return loadFromPathList(moduleId, validator, this.fallbackUris);
    }

    private ModuleSource loadFromPathList(String moduleId, Object validator, Iterable<URI> paths) throws IOException, URISyntaxException {
        if (paths == null) {
            return null;
        }
        for (URI path : paths) {
            ModuleSource moduleSource = loadFromUri(path.resolve(moduleId), path, validator);
            if (moduleSource != null) {
                return moduleSource;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromUri(URI uri, URI base, Object validator) throws IOException, URISyntaxException {
        ModuleSource source = loadFromActualUri(new URI(uri + ".js"), base, validator);
        return source != null ? source : loadFromActualUri(uri, base, validator);
    }

    /* access modifiers changed from: protected */
    public ModuleSource loadFromActualUri(URI uri, URI base, Object validator) throws IOException {
        URLValidator applicableValidator;
        URL url = new URL(base == null ? null : base.toURL(), uri.toString());
        long request_time = System.currentTimeMillis();
        URLConnection urlConnection = openUrlConnection(url);
        if (validator instanceof URLValidator) {
            URLValidator uriValidator = (URLValidator) validator;
            applicableValidator = uriValidator.appliesTo(uri) ? uriValidator : null;
        } else {
            applicableValidator = null;
        }
        if (applicableValidator != null) {
            applicableValidator.applyConditionals(urlConnection);
        }
        try {
            urlConnection.connect();
            if (applicableValidator == null || !applicableValidator.updateValidator(urlConnection, request_time, this.urlConnectionExpiryCalculator)) {
                return new ModuleSource(getReader(urlConnection), getSecurityDomain(urlConnection), uri, base, new URLValidator(uri, urlConnection, request_time, this.urlConnectionExpiryCalculator));
            }
            close(urlConnection);
            return NOT_MODIFIED;
        } catch (FileNotFoundException e) {
            return null;
        } catch (RuntimeException e2) {
            close(urlConnection);
            throw e2;
        } catch (IOException e3) {
            close(urlConnection);
            throw e3;
        }
    }

    private static Reader getReader(URLConnection urlConnection) throws IOException {
        return new InputStreamReader(urlConnection.getInputStream(), getCharacterEncoding(urlConnection));
    }

    private static String getCharacterEncoding(URLConnection urlConnection) {
        ParsedContentType pct = new ParsedContentType(urlConnection.getContentType());
        String encoding = pct.getEncoding();
        if (encoding != null) {
            return encoding;
        }
        String contentType = pct.getContentType();
        if (contentType == null || !contentType.startsWith("text/")) {
            return "utf-8";
        }
        return "8859_1";
    }

    private Object getSecurityDomain(URLConnection urlConnection) {
        if (this.urlConnectionSecurityDomainProvider == null) {
            return null;
        }
        return this.urlConnectionSecurityDomainProvider.getSecurityDomain(urlConnection);
    }

    private void close(URLConnection urlConnection) {
        try {
            urlConnection.getInputStream().close();
        } catch (IOException e) {
            onFailedClosingUrlConnection(urlConnection, e);
        }
    }

    /* access modifiers changed from: protected */
    public void onFailedClosingUrlConnection(URLConnection urlConnection, IOException cause) {
    }

    /* access modifiers changed from: protected */
    public URLConnection openUrlConnection(URL url) throws IOException {
        return url.openConnection();
    }

    /* access modifiers changed from: protected */
    public boolean entityNeedsRevalidation(Object validator) {
        return !(validator instanceof URLValidator) || ((URLValidator) validator).entityNeedsRevalidation();
    }
}
