package org.springframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public interface Resource extends InputStreamSource {
    long contentLength() throws IOException;

    Resource createRelative(String str) throws IOException;

    boolean exists();

    String getDescription();

    File getFile() throws IOException;

    String getFilename();

    URI getURI() throws IOException;

    URL getURL() throws IOException;

    boolean isOpen();

    boolean isReadable();

    long lastModified() throws IOException;
}
