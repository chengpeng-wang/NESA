package org.apache.http.entity.mime;

import org.apache.http.annotation.Immutable;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

@Immutable
public class MinimalField implements Field {
    private final String name;
    private ByteSequence raw = null;
    private final String value;

    MinimalField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.value;
    }

    public ByteSequence getRaw() {
        if (this.raw == null) {
            this.raw = ContentUtil.encode(toString());
        }
        return this.raw;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name);
        buffer.append(": ");
        buffer.append(this.value);
        return buffer.toString();
    }
}
