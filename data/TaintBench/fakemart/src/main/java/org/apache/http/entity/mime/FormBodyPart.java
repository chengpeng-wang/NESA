package org.apache.http.entity.mime;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.descriptor.ContentDescriptor;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Header;

@NotThreadSafe
public class FormBodyPart extends BodyPart {
    private final String name;

    public FormBodyPart(String name, ContentBody body) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else if (body == null) {
            throw new IllegalArgumentException("Body may not be null");
        } else {
            this.name = name;
            setHeader(new Header());
            setBody(body);
            generateContentDisp(body);
            generateContentType(body);
            generateTransferEncoding(body);
        }
    }

    public String getName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public void generateContentDisp(ContentBody body) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("form-data; name=\"");
        buffer.append(getName());
        buffer.append("\"");
        if (body.getFilename() != null) {
            buffer.append("; filename=\"");
            buffer.append(body.getFilename());
            buffer.append("\"");
        }
        addField("Content-Disposition", buffer.toString());
    }

    /* access modifiers changed from: protected */
    public void generateContentType(ContentDescriptor desc) {
        if (desc.getMimeType() != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(desc.getMimeType());
            if (desc.getCharset() != null) {
                buffer.append("; charset=");
                buffer.append(desc.getCharset());
            }
            addField("Content-Type", buffer.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void generateTransferEncoding(ContentDescriptor desc) {
        if (desc.getTransferEncoding() != null) {
            addField("Content-Transfer-Encoding", desc.getTransferEncoding());
        }
    }

    private void addField(String name, String value) {
        getHeader().addField(new MinimalField(name, value));
    }
}
