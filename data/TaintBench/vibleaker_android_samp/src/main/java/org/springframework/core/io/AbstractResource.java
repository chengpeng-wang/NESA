package org.springframework.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.core.NestedIOException;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

public abstract class AbstractResource implements Resource {
    public boolean exists() {
        try {
            return getFile().exists();
        } catch (IOException e) {
            getInputStream().close();
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public boolean isReadable() {
        return true;
    }

    public boolean isOpen() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
    }

    public URI getURI() throws IOException {
        URL url = getURL();
        try {
            return ResourceUtils.toURI(url);
        } catch (URISyntaxException ex) {
            throw new NestedIOException("Invalid URI [" + url + "]", ex);
        }
    }

    public File getFile() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
    }

    public long contentLength() throws IOException {
        InputStream is = getInputStream();
        Assert.state(is != null, "resource input stream must not be null");
        long size = 0;
        try {
            byte[] buf = new byte[255];
            while (true) {
                int read = is.read(buf);
                if (read == -1) {
                    break;
                }
                size += (long) read;
            }
            return size;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public long lastModified() throws IOException {
        long lastModified = getFileForLastModifiedCheck().lastModified();
        if (lastModified != 0) {
            return lastModified;
        }
        throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for resolving its last-modified timestamp");
    }

    /* access modifiers changed from: protected */
    public File getFileForLastModifiedCheck() throws IOException {
        return getFile();
    }

    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
    }

    public String getFilename() {
        return null;
    }

    public String toString() {
        return getDescription();
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Resource) && ((Resource) obj).getDescription().equals(getDescription()));
    }

    public int hashCode() {
        return getDescription().hashCode();
    }
}
