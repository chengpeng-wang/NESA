package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamResource extends AbstractResource {
    private final String description;
    private final InputStream inputStream;
    private boolean read;

    public InputStreamResource(InputStream inputStream) {
        this(inputStream, "resource loaded through InputStream");
    }

    public InputStreamResource(InputStream inputStream, String description) {
        this.read = false;
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }
        this.inputStream = inputStream;
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public boolean exists() {
        return true;
    }

    public boolean isOpen() {
        return true;
    }

    public InputStream getInputStream() throws IOException, IllegalStateException {
        if (this.read) {
            throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
        }
        this.read = true;
        return this.inputStream;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof InputStreamResource) && ((InputStreamResource) obj).inputStream.equals(this.inputStream));
    }

    public int hashCode() {
        return this.inputStream.hashCode();
    }
}
