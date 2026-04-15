package org.apache.james.mime4j.message;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.james.mime4j.field.ContentDispositionField;
import org.apache.james.mime4j.field.ContentTransferEncodingField;
import org.apache.james.mime4j.field.ContentTypeField;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.MimeUtil;

public abstract class Entity implements Disposable {
    private Body body = null;
    private Header header = null;
    private Entity parent = null;

    protected Entity() {
    }

    protected Entity(Entity other) {
        if (other.header != null) {
            this.header = new Header(other.header);
        }
        if (other.body != null) {
            setBody(BodyCopier.copy(other.body));
        }
    }

    public Entity getParent() {
        return this.parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        if (this.body != null) {
            throw new IllegalStateException("body already set");
        }
        this.body = body;
        body.setParent(this);
    }

    public Body removeBody() {
        if (this.body == null) {
            return null;
        }
        Body body = this.body;
        this.body = null;
        body.setParent(null);
        return body;
    }

    public void setMessage(Message message) {
        setBody(message, ContentTypeField.TYPE_MESSAGE_RFC822, null);
    }

    public void setMultipart(Multipart multipart) {
        setBody(multipart, ContentTypeField.TYPE_MULTIPART_PREFIX + multipart.getSubType(), Collections.singletonMap(ContentTypeField.PARAM_BOUNDARY, MimeUtil.createUniqueBoundary()));
    }

    public void setMultipart(Multipart multipart, Map<String, String> parameters) {
        String mimeType = ContentTypeField.TYPE_MULTIPART_PREFIX + multipart.getSubType();
        if (!parameters.containsKey(ContentTypeField.PARAM_BOUNDARY)) {
            Map<String, String> parameters2 = new HashMap(parameters);
            parameters2.put(ContentTypeField.PARAM_BOUNDARY, MimeUtil.createUniqueBoundary());
            parameters = parameters2;
        }
        setBody(multipart, mimeType, parameters);
    }

    public void setText(TextBody textBody) {
        setText(textBody, "plain");
    }

    public void setText(TextBody textBody, String subtype) {
        String mimeType = "text/" + subtype;
        Map<String, String> parameters = null;
        String mimeCharset = textBody.getMimeCharset();
        if (!(mimeCharset == null || mimeCharset.equalsIgnoreCase("us-ascii"))) {
            parameters = Collections.singletonMap(ContentTypeField.PARAM_CHARSET, mimeCharset);
        }
        setBody(textBody, mimeType, parameters);
    }

    public void setBody(Body body, String mimeType) {
        setBody(body, mimeType, null);
    }

    public void setBody(Body body, String mimeType, Map<String, String> parameters) {
        setBody(body);
        obtainHeader().setField(Fields.contentType(mimeType, parameters));
    }

    public String getMimeType() {
        return ContentTypeField.getMimeType((ContentTypeField) getHeader().getField("Content-Type"), getParent() != null ? (ContentTypeField) getParent().getHeader().getField("Content-Type") : null);
    }

    public String getCharset() {
        return ContentTypeField.getCharset((ContentTypeField) getHeader().getField("Content-Type"));
    }

    public String getContentTransferEncoding() {
        return ContentTransferEncodingField.getEncoding((ContentTransferEncodingField) getHeader().getField("Content-Transfer-Encoding"));
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        obtainHeader().setField(Fields.contentTransferEncoding(contentTransferEncoding));
    }

    public String getDispositionType() {
        ContentDispositionField field = (ContentDispositionField) obtainField("Content-Disposition");
        if (field == null) {
            return null;
        }
        return field.getDispositionType();
    }

    public void setContentDisposition(String dispositionType) {
        obtainHeader().setField(Fields.contentDisposition(dispositionType, null, -1, null, null, null));
    }

    public void setContentDisposition(String dispositionType, String filename) {
        obtainHeader().setField(Fields.contentDisposition(dispositionType, filename, -1, null, null, null));
    }

    public void setContentDisposition(String dispositionType, String filename, long size) {
        obtainHeader().setField(Fields.contentDisposition(dispositionType, filename, size, null, null, null));
    }

    public void setContentDisposition(String dispositionType, String filename, long size, Date creationDate, Date modificationDate, Date readDate) {
        obtainHeader().setField(Fields.contentDisposition(dispositionType, filename, size, creationDate, modificationDate, readDate));
    }

    public String getFilename() {
        ContentDispositionField field = (ContentDispositionField) obtainField("Content-Disposition");
        if (field == null) {
            return null;
        }
        return field.getFilename();
    }

    public void setFilename(String filename) {
        Header header = obtainHeader();
        ContentDispositionField field = (ContentDispositionField) header.getField("Content-Disposition");
        if (field != null) {
            String dispositionType = field.getDispositionType();
            Map parameters = new HashMap(field.getParameters());
            if (filename == null) {
                parameters.remove("filename");
            } else {
                parameters.put("filename", filename);
            }
            header.setField(Fields.contentDisposition(dispositionType, parameters));
        } else if (filename != null) {
            header.setField(Fields.contentDisposition(ContentDispositionField.DISPOSITION_TYPE_ATTACHMENT, filename, -1, null, null, null));
        }
    }

    public boolean isMimeType(String type) {
        return getMimeType().equalsIgnoreCase(type);
    }

    public boolean isMultipart() {
        ContentTypeField f = (ContentTypeField) getHeader().getField("Content-Type");
        return (f == null || f.getBoundary() == null || !getMimeType().startsWith(ContentTypeField.TYPE_MULTIPART_PREFIX)) ? false : true;
    }

    public void dispose() {
        if (this.body != null) {
            this.body.dispose();
        }
    }

    /* access modifiers changed from: 0000 */
    public Header obtainHeader() {
        if (this.header == null) {
            this.header = new Header();
        }
        return this.header;
    }

    /* access modifiers changed from: 0000 */
    public <F extends Field> F obtainField(String fieldName) {
        Header header = getHeader();
        if (header == null) {
            return null;
        }
        return header.getField(fieldName);
    }
}
