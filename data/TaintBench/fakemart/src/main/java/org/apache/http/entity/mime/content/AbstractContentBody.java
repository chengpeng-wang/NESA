package org.apache.http.entity.mime.content;

import java.util.Collections;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.SingleBody;

@NotThreadSafe
public abstract class AbstractContentBody extends SingleBody implements ContentBody {
    private final String mediaType;
    private final String mimeType;
    private Entity parent = null;
    private final String subType;

    public AbstractContentBody(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        this.mimeType = mimeType;
        int i = mimeType.indexOf(47);
        if (i != -1) {
            this.mediaType = mimeType.substring(0, i);
            this.subType = mimeType.substring(i + 1);
            return;
        }
        this.mediaType = mimeType;
        this.subType = null;
    }

    public Entity getParent() {
        return this.parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public String getSubType() {
        return this.subType;
    }

    public Map<String, String> getContentTypeParameters() {
        return Collections.emptyMap();
    }

    public void dispose() {
    }
}
