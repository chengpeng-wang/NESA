package org.springframework.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ByteArrayResource extends AbstractResource {
    private final byte[] byteArray;
    private final String description;

    public ByteArrayResource(byte[] byteArray) {
        this(byteArray, "resource loaded from byte array");
    }

    public ByteArrayResource(byte[] byteArray, String description) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Byte array must not be null");
        }
        this.byteArray = byteArray;
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public final byte[] getByteArray() {
        return this.byteArray;
    }

    public boolean exists() {
        return true;
    }

    public long contentLength() {
        return (long) this.byteArray.length;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.byteArray);
    }

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof ByteArrayResource) && Arrays.equals(((ByteArrayResource) obj).byteArray, this.byteArray));
    }

    public int hashCode() {
        return (byte[].class.hashCode() * 29) * this.byteArray.length;
    }
}
