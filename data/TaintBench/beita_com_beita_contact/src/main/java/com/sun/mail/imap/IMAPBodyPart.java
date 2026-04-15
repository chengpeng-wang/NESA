package com.sun.mail.imap;

import com.beita.contact.ContactColumn;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.IMAPProtocol;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.FolderClosedException;
import javax.mail.IllegalWriteException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

public class IMAPBodyPart extends MimeBodyPart {
    private BODYSTRUCTURE bs;
    private String description;
    private boolean headersLoaded = false;
    private IMAPMessage message;
    private String sectionId;
    private String type;

    protected IMAPBodyPart(BODYSTRUCTURE bs, String sid, IMAPMessage message) {
        this.bs = bs;
        this.sectionId = sid;
        this.message = message;
        this.type = new ContentType(bs.type, bs.subtype, bs.cParams).toString();
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() {
    }

    public int getSize() throws MessagingException {
        return this.bs.size;
    }

    public int getLineCount() throws MessagingException {
        return this.bs.lines;
    }

    public String getContentType() throws MessagingException {
        return this.type;
    }

    public String getDisposition() throws MessagingException {
        return this.bs.disposition;
    }

    public void setDisposition(String disposition) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getEncoding() throws MessagingException {
        return this.bs.encoding;
    }

    public String getContentID() throws MessagingException {
        return this.bs.id;
    }

    public String getContentMD5() throws MessagingException {
        return this.bs.md5;
    }

    public void setContentMD5(String md5) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getDescription() throws MessagingException {
        if (this.description != null) {
            return this.description;
        }
        if (this.bs.description == null) {
            return null;
        }
        try {
            this.description = MimeUtility.decodeText(this.bs.description);
        } catch (UnsupportedEncodingException e) {
            UnsupportedEncodingException ex = e;
            this.description = this.bs.description;
        }
        return this.description;
    }

    public void setDescription(String description, String charset) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String getFileName() throws MessagingException {
        String filename = null;
        if (this.bs.dParams != null) {
            filename = this.bs.dParams.get("filename");
        }
        if (filename != null || this.bs.cParams == null) {
            return filename;
        }
        return this.bs.cParams.get(ContactColumn.NAME);
    }

    public void setFileName(String filename) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:19:0x004d, code skipped:
            if (r2 != null) goto L_0x007f;
     */
    /* JADX WARNING: Missing block: B:21:0x0056, code skipped:
            throw new javax.mail.MessagingException("No content");
     */
    /* JADX WARNING: Missing block: B:39:?, code skipped:
            return r2;
     */
    public java.io.InputStream getContentStream() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r2 = 0;
        r7 = r12.message;
        r5 = r7.getPeek();
        r7 = r12.message;
        r7 = r7.getMessageCacheLock();
        monitor-enter(r7);
        r8 = r12.message;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r3 = r8.getProtocol();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r8 = r12.message;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r8.checkExpunged();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r8 = r3.isREV1();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        if (r8 == 0) goto L_0x0038;
    L_0x001f:
        r8 = r12.message;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r8 = r8.getFetchBlockSize();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r9 = -1;
        if (r8 == r9) goto L_0x0038;
    L_0x0028:
        r8 = new com.sun.mail.imap.IMAPInputStream;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r9 = r12.message;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r10 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r11 = r12.bs;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r11 = r11.size;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r8.m221init(r9, r10, r11, r5);	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        monitor-exit(r7);	 Catch:{ all -> 0x0070 }
        r7 = r8;
    L_0x0037:
        return r7;
    L_0x0038:
        r8 = r12.message;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r6 = r8.getSequenceNumber();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        if (r5 == 0) goto L_0x0057;
    L_0x0040:
        r8 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r0 = r3.peekBody(r6, r8);	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
    L_0x0046:
        if (r0 == 0) goto L_0x004c;
    L_0x0048:
        r2 = r0.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
    L_0x004c:
        monitor-exit(r7);	 Catch:{ all -> 0x0070 }
        if (r2 != 0) goto L_0x007f;
    L_0x004f:
        r7 = new javax.mail.MessagingException;
        r8 = "No content";
        r7.m313init(r8);
        throw r7;
    L_0x0057:
        r8 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        r0 = r3.fetchBody(r6, r8);	 Catch:{ ConnectionException -> 0x005e, ProtocolException -> 0x0073 }
        goto L_0x0046;
    L_0x005e:
        r8 = move-exception;
        r1 = r8;
        r8 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x0070 }
        r9 = r12.message;	 Catch:{ all -> 0x0070 }
        r9 = r9.getFolder();	 Catch:{ all -> 0x0070 }
        r10 = r1.getMessage();	 Catch:{ all -> 0x0070 }
        r8.m423init(r9, r10);	 Catch:{ all -> 0x0070 }
        throw r8;	 Catch:{ all -> 0x0070 }
    L_0x0070:
        r8 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0070 }
        throw r8;
    L_0x0073:
        r8 = move-exception;
        r4 = r8;
        r8 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0070 }
        r9 = r4.getMessage();	 Catch:{ all -> 0x0070 }
        r8.m314init(r9, r4);	 Catch:{ all -> 0x0070 }
        throw r8;	 Catch:{ all -> 0x0070 }
    L_0x007f:
        r7 = r2;
        goto L_0x0037;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPBodyPart.getContentStream():java.io.InputStream");
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            if (this.bs.isMulti()) {
                this.dh = new DataHandler(new IMAPMultipartDataSource(this, this.bs.bodies, this.sectionId, this.message));
            } else if (this.bs.isNested() && this.message.isREV1()) {
                this.dh = new DataHandler(new IMAPNestedMessage(this.message, this.bs.bodies[0], this.bs.envelope, this.sectionId), this.type);
            }
        }
        return super.getDataHandler();
    }

    public void setDataHandler(DataHandler content) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void setContent(Object o, String type) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void setContent(Multipart mp) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public String[] getHeader(String name) throws MessagingException {
        loadHeaders();
        return super.getHeader(name);
    }

    public void setHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void addHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public void removeHeader(String name) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
        loadHeaders();
        return super.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        loadHeaders();
        return super.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        loadHeaders();
        return super.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        loadHeaders();
        return super.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        loadHeaders();
        return super.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        loadHeaders();
        return super.getNonMatchingHeaderLines(names);
    }

    private synchronized void loadHeaders() throws MessagingException {
        if (!this.headersLoaded) {
            if (this.headers == null) {
                this.headers = new InternetHeaders();
            }
            synchronized (this.message.getMessageCacheLock()) {
                try {
                    IMAPProtocol p = this.message.getProtocol();
                    this.message.checkExpunged();
                    if (p.isREV1()) {
                        BODY b = p.peekBody(this.message.getSequenceNumber(), this.sectionId + ".MIME");
                        if (b == null) {
                            throw new MessagingException("Failed to fetch headers");
                        }
                        ByteArrayInputStream bis = b.getByteArrayInputStream();
                        if (bis == null) {
                            throw new MessagingException("Failed to fetch headers");
                        }
                        this.headers.load(bis);
                    } else {
                        this.headers.addHeader("Content-Type", this.type);
                        this.headers.addHeader("Content-Transfer-Encoding", this.bs.encoding);
                        if (this.bs.description != null) {
                            this.headers.addHeader("Content-Description", this.bs.description);
                        }
                        if (this.bs.id != null) {
                            this.headers.addHeader("Content-ID", this.bs.id);
                        }
                        if (this.bs.md5 != null) {
                            this.headers.addHeader("Content-MD5", this.bs.md5);
                        }
                    }
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this.message.getFolder(), e.getMessage());
                } catch (ProtocolException e2) {
                    ProtocolException pex = e2;
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
            this.headersLoaded = true;
        }
    }
}
