package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.ContentUtil;

class RawField implements Field {
    private String body;
    private int colonIdx;
    private String name;
    private final ByteSequence raw;

    public RawField(ByteSequence raw, int colonIdx) {
        this.raw = raw;
        this.colonIdx = colonIdx;
    }

    public String getName() {
        if (this.name == null) {
            this.name = parseName();
        }
        return this.name;
    }

    public String getBody() {
        if (this.body == null) {
            this.body = parseBody();
        }
        return this.body;
    }

    public ByteSequence getRaw() {
        return this.raw;
    }

    public String toString() {
        return getName() + ':' + getBody();
    }

    private String parseName() {
        return ContentUtil.decode(this.raw, 0, this.colonIdx);
    }

    private String parseBody() {
        int offset = this.colonIdx + 1;
        return ContentUtil.decode(this.raw, offset, this.raw.length() - offset);
    }
}
