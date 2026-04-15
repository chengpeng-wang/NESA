package com.sun.mail.imap;

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
        return this.bs.cParams.get("name");
    }

    public void setFileName(String filename) throws MessagingException {
        throw new IllegalWriteException("IMAPBodyPart is read-only");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:18:0x004c, code skipped:
            if (r2 != null) goto L_0x007c;
     */
    /* JADX WARNING: Missing block: B:20:0x0055, code skipped:
            throw new javax.mail.MessagingException("No content");
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            return r2;
     */
    public java.io.InputStream getContentStream() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r2 = 0;
        r7 = r12.message;
        r5 = r7.getPeek();
        r7 = r12.message;
        r8 = r7.getMessageCacheLock();
        monitor-enter(r8);
        r7 = r12.message;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r3 = r7.getProtocol();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r7 = r12.message;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r7.checkExpunged();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r7 = r3.isREV1();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        if (r7 == 0) goto L_0x0037;
    L_0x001f:
        r7 = r12.message;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r7 = r7.getFetchBlockSize();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r9 = -1;
        if (r7 == r9) goto L_0x0037;
    L_0x0028:
        r7 = new com.sun.mail.imap.IMAPInputStream;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r9 = r12.message;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r10 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r11 = r12.bs;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r11 = r11.size;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r7.m301init(r9, r10, r11, r5);	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        monitor-exit(r8);	 Catch:{ all -> 0x006e }
    L_0x0036:
        return r7;
    L_0x0037:
        r7 = r12.message;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r6 = r7.getSequenceNumber();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        if (r5 == 0) goto L_0x0056;
    L_0x003f:
        r7 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r0 = r3.peekBody(r6, r7);	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
    L_0x0045:
        if (r0 == 0) goto L_0x004b;
    L_0x0047:
        r2 = r0.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
    L_0x004b:
        monitor-exit(r8);	 Catch:{ all -> 0x006e }
        if (r2 != 0) goto L_0x007c;
    L_0x004e:
        r7 = new javax.mail.MessagingException;
        r8 = "No content";
        r7.m403init(r8);
        throw r7;
    L_0x0056:
        r7 = r12.sectionId;	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        r0 = r3.fetchBody(r6, r7);	 Catch:{ ConnectionException -> 0x005d, ProtocolException -> 0x0071 }
        goto L_0x0045;
    L_0x005d:
        r1 = move-exception;
        r7 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x006e }
        r9 = r12.message;	 Catch:{ all -> 0x006e }
        r9 = r9.getFolder();	 Catch:{ all -> 0x006e }
        r10 = r1.getMessage();	 Catch:{ all -> 0x006e }
        r7.m692init(r9, r10);	 Catch:{ all -> 0x006e }
        throw r7;	 Catch:{ all -> 0x006e }
    L_0x006e:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x006e }
        throw r7;
    L_0x0071:
        r4 = move-exception;
        r7 = new javax.mail.MessagingException;	 Catch:{ all -> 0x006e }
        r9 = r4.getMessage();	 Catch:{ all -> 0x006e }
        r7.m404init(r9, r4);	 Catch:{ all -> 0x006e }
        throw r7;	 Catch:{ all -> 0x006e }
    L_0x007c:
        r7 = r2;
        goto L_0x0036;
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
                } catch (ConnectionException cex) {
                    throw new FolderClosedException(this.message.getFolder(), cex.getMessage());
                } catch (ProtocolException pex) {
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
            this.headersLoaded = true;
        }
    }
}
