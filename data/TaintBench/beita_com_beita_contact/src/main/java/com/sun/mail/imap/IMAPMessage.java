package com.sun.mail.imap;

import com.beita.contact.ContactColumn;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.Utility.Condition;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.ENVELOPE;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.INTERNALDATE;
import com.sun.mail.imap.protocol.RFC822DATA;
import com.sun.mail.imap.protocol.RFC822SIZE;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.FetchProfile.Item;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.IllegalWriteException;
import javax.mail.Message.RecipientType;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.UIDFolder.FetchProfileItem;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class IMAPMessage extends MimeMessage {
    private static String EnvelopeCmd = "ENVELOPE INTERNALDATE RFC822.SIZE";
    protected BODYSTRUCTURE bs;
    private String description;
    protected ENVELOPE envelope;
    private boolean headersLoaded = false;
    private Hashtable loadedHeaders;
    private boolean peek;
    private Date receivedDate;
    protected String sectionId;
    private int seqnum;
    /* access modifiers changed from: private */
    public int size = -1;
    private String subject;
    private String type;
    private long uid = -1;

    /* renamed from: com.sun.mail.imap.IMAPMessage$1FetchProfileCondition */
    class AnonymousClass1FetchProfileCondition implements Condition {
        private String[] hdrs = null;
        private boolean needBodyStructure = false;
        private boolean needEnvelope = false;
        private boolean needFlags = false;
        private boolean needHeaders = false;
        private boolean needSize = false;
        private boolean needUID = false;

        public AnonymousClass1FetchProfileCondition(FetchProfile fp) {
            if (fp.contains(Item.ENVELOPE)) {
                this.needEnvelope = true;
            }
            if (fp.contains(Item.FLAGS)) {
                this.needFlags = true;
            }
            if (fp.contains(Item.CONTENT_INFO)) {
                this.needBodyStructure = true;
            }
            if (fp.contains(FetchProfileItem.UID)) {
                this.needUID = true;
            }
            if (fp.contains(IMAPFolder.FetchProfileItem.HEADERS)) {
                this.needHeaders = true;
            }
            if (fp.contains(IMAPFolder.FetchProfileItem.SIZE)) {
                this.needSize = true;
            }
            this.hdrs = fp.getHeaderNames();
        }

        public boolean test(IMAPMessage m) {
            if (this.needEnvelope && m._getEnvelope() == null) {
                return true;
            }
            if (this.needFlags && m._getFlags() == null) {
                return true;
            }
            if (this.needBodyStructure && m._getBodyStructure() == null) {
                return true;
            }
            if (this.needUID && m.getUID() == -1) {
                return true;
            }
            if (this.needHeaders && !m.areHeadersLoaded()) {
                return true;
            }
            if (this.needSize && m.size == -1) {
                return true;
            }
            for (String access$5 : this.hdrs) {
                if (!m.isHeaderLoaded(access$5)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected IMAPMessage(IMAPFolder folder, int msgnum, int seqnum) {
        super((Folder) folder, msgnum);
        this.seqnum = seqnum;
        this.flags = null;
    }

    protected IMAPMessage(Session session) {
        super(session);
    }

    /* access modifiers changed from: protected */
    public IMAPProtocol getProtocol() throws ProtocolException, FolderClosedException {
        ((IMAPFolder) this.folder).waitIfIdle();
        IMAPProtocol p = ((IMAPFolder) this.folder).protocol;
        if (p != null) {
            return p;
        }
        throw new FolderClosedException(this.folder);
    }

    /* access modifiers changed from: protected */
    public boolean isREV1() throws FolderClosedException {
        IMAPProtocol p = ((IMAPFolder) this.folder).protocol;
        if (p != null) {
            return p.isREV1();
        }
        throw new FolderClosedException(this.folder);
    }

    /* access modifiers changed from: protected */
    public Object getMessageCacheLock() {
        return ((IMAPFolder) this.folder).messageCacheLock;
    }

    /* access modifiers changed from: protected */
    public int getSequenceNumber() {
        return this.seqnum;
    }

    /* access modifiers changed from: protected */
    public void setSequenceNumber(int seqnum) {
        this.seqnum = seqnum;
    }

    /* access modifiers changed from: protected */
    public void setMessageNumber(int msgnum) {
        super.setMessageNumber(msgnum);
    }

    /* access modifiers changed from: protected */
    public long getUID() {
        return this.uid;
    }

    /* access modifiers changed from: protected */
    public void setUID(long uid) {
        this.uid = uid;
    }

    /* access modifiers changed from: protected */
    public void setExpunged(boolean set) {
        super.setExpunged(set);
        this.seqnum = -1;
    }

    /* access modifiers changed from: protected */
    public void checkExpunged() throws MessageRemovedException {
        if (this.expunged) {
            throw new MessageRemovedException();
        }
    }

    /* access modifiers changed from: protected */
    public void forceCheckExpunged() throws MessageRemovedException, FolderClosedException {
        synchronized (getMessageCacheLock()) {
            try {
                getProtocol().noop();
            } catch (ConnectionException e) {
                throw new FolderClosedException(this.folder, e.getMessage());
            } catch (ProtocolException e2) {
            }
        }
        if (this.expunged) {
            throw new MessageRemovedException();
        }
    }

    /* access modifiers changed from: protected */
    public int getFetchBlockSize() {
        return ((IMAPStore) this.folder.getStore()).getFetchBlockSize();
    }

    public Address[] getFrom() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        return aaclone(this.envelope.from);
    }

    public void setFrom(Address address) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addFrom(Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address getSender() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        if (this.envelope.sender != null) {
            return this.envelope.sender[0];
        }
        return null;
    }

    public void setSender(Address address) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address[] getRecipients(RecipientType type) throws MessagingException {
        checkExpunged();
        loadEnvelope();
        if (type == RecipientType.TO) {
            return aaclone(this.envelope.to);
        }
        if (type == RecipientType.CC) {
            return aaclone(this.envelope.cc);
        }
        if (type == RecipientType.BCC) {
            return aaclone(this.envelope.bcc);
        }
        return super.getRecipients(type);
    }

    public void setRecipients(RecipientType type, Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addRecipients(RecipientType type, Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address[] getReplyTo() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        return aaclone(this.envelope.replyTo);
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getSubject() throws MessagingException {
        checkExpunged();
        if (this.subject != null) {
            return this.subject;
        }
        loadEnvelope();
        if (this.envelope.subject == null) {
            return null;
        }
        try {
            this.subject = MimeUtility.decodeText(this.envelope.subject);
        } catch (UnsupportedEncodingException e) {
            UnsupportedEncodingException ex = e;
            this.subject = this.envelope.subject;
        }
        return this.subject;
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Date getSentDate() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        if (this.envelope.date == null) {
            return null;
        }
        return new Date(this.envelope.date.getTime());
    }

    public void setSentDate(Date d) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Date getReceivedDate() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        if (this.receivedDate == null) {
            return null;
        }
        return new Date(this.receivedDate.getTime());
    }

    public int getSize() throws MessagingException {
        checkExpunged();
        if (this.size == -1) {
            loadEnvelope();
        }
        return this.size;
    }

    public int getLineCount() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        return this.bs.lines;
    }

    public String[] getContentLanguage() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        if (this.bs.language != null) {
            return (String[]) this.bs.language.clone();
        }
        return null;
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getInReplyTo() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        return this.envelope.inReplyTo;
    }

    public String getContentType() throws MessagingException {
        checkExpunged();
        if (this.type == null) {
            loadBODYSTRUCTURE();
            this.type = new ContentType(this.bs.type, this.bs.subtype, this.bs.cParams).toString();
        }
        return this.type;
    }

    public String getDisposition() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        return this.bs.disposition;
    }

    public void setDisposition(String disposition) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getEncoding() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        return this.bs.encoding;
    }

    public String getContentID() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        return this.bs.id;
    }

    public void setContentID(String cid) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getContentMD5() throws MessagingException {
        checkExpunged();
        loadBODYSTRUCTURE();
        return this.bs.md5;
    }

    public void setContentMD5(String md5) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getDescription() throws MessagingException {
        checkExpunged();
        if (this.description != null) {
            return this.description;
        }
        loadBODYSTRUCTURE();
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
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getMessageID() throws MessagingException {
        checkExpunged();
        loadEnvelope();
        return this.envelope.messageId;
    }

    public String getFileName() throws MessagingException {
        checkExpunged();
        String filename = null;
        loadBODYSTRUCTURE();
        if (this.bs.dParams != null) {
            filename = this.bs.dParams.get("filename");
        }
        if (filename != null || this.bs.cParams == null) {
            return filename;
        }
        return this.bs.cParams.get(ContactColumn.NAME);
    }

    public void setFileName(String filename) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:24:0x0053, code skipped:
            if (r2 != null) goto L_0x009d;
     */
    /* JADX WARNING: Missing block: B:26:0x005c, code skipped:
            throw new javax.mail.MessagingException("No content");
     */
    /* JADX WARNING: Missing block: B:47:?, code skipped:
            return r2;
     */
    public java.io.InputStream getContentStream() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r11 = -1;
        r2 = 0;
        r5 = r12.getPeek();
        r7 = r12.getMessageCacheLock();
        monitor-enter(r7);
        r3 = r12.getProtocol();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r12.checkExpunged();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r8 = r3.isREV1();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        if (r8 == 0) goto L_0x0036;
    L_0x0018:
        r8 = r12.getFetchBlockSize();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        if (r8 == r11) goto L_0x0036;
    L_0x001e:
        r8 = new com.sun.mail.imap.IMAPInputStream;	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r9 = "TEXT";
        r9 = r12.toSection(r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r10 = r12.bs;	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        if (r10 == 0) goto L_0x0034;
    L_0x002a:
        r10 = r12.bs;	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r10 = r10.size;	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
    L_0x002e:
        r8.m221init(r12, r9, r10, r5);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        monitor-exit(r7);	 Catch:{ all -> 0x008b }
        r7 = r8;
    L_0x0033:
        return r7;
    L_0x0034:
        r10 = r11;
        goto L_0x002e;
    L_0x0036:
        r8 = r3.isREV1();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        if (r8 == 0) goto L_0x006c;
    L_0x003c:
        if (r5 == 0) goto L_0x005d;
    L_0x003e:
        r8 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r9 = "TEXT";
        r9 = r12.toSection(r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r0 = r3.peekBody(r8, r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
    L_0x004c:
        if (r0 == 0) goto L_0x0052;
    L_0x004e:
        r2 = r0.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
    L_0x0052:
        monitor-exit(r7);	 Catch:{ all -> 0x008b }
        if (r2 != 0) goto L_0x009d;
    L_0x0055:
        r7 = new javax.mail.MessagingException;
        r8 = "No content";
        r7.m313init(r8);
        throw r7;
    L_0x005d:
        r8 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r9 = "TEXT";
        r9 = r12.toSection(r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r0 = r3.fetchBody(r8, r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        goto L_0x004c;
    L_0x006c:
        r8 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        r9 = "TEXT";
        r6 = r3.fetchRFC822(r8, r9);	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        if (r6 == 0) goto L_0x0052;
    L_0x0078:
        r2 = r6.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x007d, ProtocolException -> 0x008e }
        goto L_0x0052;
    L_0x007d:
        r8 = move-exception;
        r1 = r8;
        r8 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x008b }
        r9 = r12.folder;	 Catch:{ all -> 0x008b }
        r10 = r1.getMessage();	 Catch:{ all -> 0x008b }
        r8.m423init(r9, r10);	 Catch:{ all -> 0x008b }
        throw r8;	 Catch:{ all -> 0x008b }
    L_0x008b:
        r8 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x008b }
        throw r8;
    L_0x008e:
        r8 = move-exception;
        r4 = r8;
        r12.forceCheckExpunged();	 Catch:{ all -> 0x008b }
        r8 = new javax.mail.MessagingException;	 Catch:{ all -> 0x008b }
        r9 = r4.getMessage();	 Catch:{ all -> 0x008b }
        r8.m314init(r9, r4);	 Catch:{ all -> 0x008b }
        throw r8;	 Catch:{ all -> 0x008b }
    L_0x009d:
        r7 = r2;
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPMessage.getContentStream():java.io.InputStream");
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        checkExpunged();
        if (this.dh == null) {
            loadBODYSTRUCTURE();
            if (this.type == null) {
                this.type = new ContentType(this.bs.type, this.bs.subtype, this.bs.cParams).toString();
            }
            if (this.bs.isMulti()) {
                this.dh = new DataHandler(new IMAPMultipartDataSource(this, this.bs.bodies, this.sectionId, this));
            } else if (this.bs.isNested() && isREV1()) {
                this.dh = new DataHandler(new IMAPNestedMessage(this, this.bs.bodies[0], this.bs.envelope, this.sectionId == null ? "1" : this.sectionId + ".1"), this.type);
            }
        }
        return super.getDataHandler();
    }

    public void setDataHandler(DataHandler content) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        InputStream is = null;
        boolean pk = getPeek();
        synchronized (getMessageCacheLock()) {
            try {
                IMAPProtocol p = getProtocol();
                checkExpunged();
                if (p.isREV1()) {
                    BODY b;
                    if (pk) {
                        b = p.peekBody(getSequenceNumber(), this.sectionId);
                    } else {
                        b = p.fetchBody(getSequenceNumber(), this.sectionId);
                    }
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(getSequenceNumber(), null);
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this.folder, e.getMessage());
            } catch (ProtocolException e2) {
                ProtocolException pex = e2;
                forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            throw new MessagingException("No content");
        }
        byte[] bytes = new byte[1024];
        while (true) {
            int count = is.read(bytes);
            if (count != -1) {
                os.write(bytes, 0, count);
            } else {
                return;
            }
        }
    }

    public String[] getHeader(String name) throws MessagingException {
        checkExpunged();
        if (isHeaderLoaded(name)) {
            return this.headers.getHeader(name);
        }
        InputStream is = null;
        synchronized (getMessageCacheLock()) {
            try {
                IMAPProtocol p = getProtocol();
                checkExpunged();
                if (p.isREV1()) {
                    BODY b = p.peekBody(getSequenceNumber(), toSection("HEADER.FIELDS (" + name + ")"));
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(getSequenceNumber(), "HEADER.LINES (" + name + ")");
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            } catch (ConnectionException e) {
                throw new FolderClosedException(this.folder, e.getMessage());
            } catch (ProtocolException e2) {
                ProtocolException pex = e2;
                forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            return null;
        }
        if (this.headers == null) {
            this.headers = new InternetHeaders();
        }
        this.headers.load(is);
        setHeaderLoaded(name);
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        checkExpunged();
        if (getHeader(name) == null) {
            return null;
        }
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void removeHeader(String name) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        checkExpunged();
        loadHeaders();
        return super.getNonMatchingHeaderLines(names);
    }

    public synchronized Flags getFlags() throws MessagingException {
        checkExpunged();
        loadFlags();
        return super.getFlags();
    }

    public synchronized boolean isSet(Flag flag) throws MessagingException {
        checkExpunged();
        loadFlags();
        return super.isSet(flag);
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        synchronized (getMessageCacheLock()) {
            try {
                IMAPProtocol p = getProtocol();
                checkExpunged();
                p.storeFlags(getSequenceNumber(), flag, set);
            } catch (ConnectionException e) {
                throw new FolderClosedException(this.folder, e.getMessage());
            } catch (ProtocolException e2) {
                ProtocolException pex = e2;
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    public synchronized void setPeek(boolean peek) {
        this.peek = peek;
    }

    public synchronized boolean getPeek() {
        return this.peek;
    }

    public synchronized void invalidateHeaders() {
        this.headersLoaded = false;
        this.loadedHeaders = null;
        this.envelope = null;
        this.bs = null;
        this.receivedDate = null;
        this.size = -1;
        this.type = null;
        this.subject = null;
        this.description = null;
    }

    /* JADX WARNING: Missing block: B:168:?, code skipped:
            return;
     */
    static void fetch(com.sun.mail.imap.IMAPFolder r18, javax.mail.Message[] r19, javax.mail.FetchProfile r20) throws javax.mail.MessagingException {
        /*
        r4 = new java.lang.StringBuffer;
        r4.<init>();
        r5 = 1;
        r3 = 0;
        r6 = javax.mail.FetchProfile.Item.ENVELOPE;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x0018;
    L_0x0012:
        r5 = EnvelopeCmd;
        r4.append(r5);
        r5 = 0;
    L_0x0018:
        r6 = javax.mail.FetchProfile.Item.FLAGS;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x002b;
    L_0x0023:
        if (r5 == 0) goto L_0x00bb;
    L_0x0025:
        r5 = "FLAGS";
    L_0x0027:
        r4.append(r5);
        r5 = 0;
    L_0x002b:
        r6 = javax.mail.FetchProfile.Item.CONTENT_INFO;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x003e;
    L_0x0036:
        if (r5 == 0) goto L_0x00bf;
    L_0x0038:
        r5 = "BODYSTRUCTURE";
    L_0x003a:
        r4.append(r5);
        r5 = 0;
    L_0x003e:
        r6 = javax.mail.UIDFolder.FetchProfileItem.UID;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x0051;
    L_0x0049:
        if (r5 == 0) goto L_0x00c3;
    L_0x004b:
        r5 = "UID";
    L_0x004d:
        r4.append(r5);
        r5 = 0;
    L_0x0051:
        r6 = com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x0070;
    L_0x005c:
        r3 = 1;
        r0 = r18;
        r0 = r0.protocol;
        r6 = r0;
        r6 = r6.isREV1();
        if (r6 == 0) goto L_0x00c9;
    L_0x0068:
        if (r5 == 0) goto L_0x00c6;
    L_0x006a:
        r5 = "BODY.PEEK[HEADER]";
    L_0x006c:
        r4.append(r5);
    L_0x006f:
        r5 = 0;
    L_0x0070:
        r6 = com.sun.mail.imap.IMAPFolder.FetchProfileItem.SIZE;
        r0 = r20;
        r1 = r6;
        r6 = r0.contains(r1);
        if (r6 == 0) goto L_0x0083;
    L_0x007b:
        if (r5 == 0) goto L_0x00d4;
    L_0x007d:
        r5 = "RFC822.SIZE";
    L_0x007f:
        r4.append(r5);
        r5 = 0;
    L_0x0083:
        r6 = 0;
        r6 = (java.lang.String[]) r6;
        if (r3 != 0) goto L_0x00a2;
    L_0x0088:
        r6 = r20.getHeaderNames();
        r7 = r6.length;
        if (r7 <= 0) goto L_0x00a2;
    L_0x008f:
        if (r5 != 0) goto L_0x0096;
    L_0x0091:
        r5 = " ";
        r4.append(r5);
    L_0x0096:
        r0 = r18;
        r0 = r0.protocol;
        r5 = r0;
        r5 = craftHeaderCmd(r5, r6);
        r4.append(r5);
    L_0x00a2:
        r5 = new com.sun.mail.imap.IMAPMessage$1FetchProfileCondition;
        r0 = r5;
        r1 = r20;
        r0.m222init(r1);
        r0 = r18;
        r0 = r0.messageCacheLock;
        r14 = r0;
        monitor-enter(r14);
        r0 = r19;
        r1 = r5;
        r19 = com.sun.mail.imap.Utility.toMessageSet(r0, r1);	 Catch:{ all -> 0x00f6 }
        if (r19 != 0) goto L_0x00d7;
    L_0x00b9:
        monitor-exit(r14);	 Catch:{ all -> 0x00f6 }
    L_0x00ba:
        return;
    L_0x00bb:
        r5 = " FLAGS";
        goto L_0x0027;
    L_0x00bf:
        r5 = " BODYSTRUCTURE";
        goto L_0x003a;
    L_0x00c3:
        r5 = " UID";
        goto L_0x004d;
    L_0x00c6:
        r5 = " BODY.PEEK[HEADER]";
        goto L_0x006c;
    L_0x00c9:
        if (r5 == 0) goto L_0x00d1;
    L_0x00cb:
        r5 = "RFC822.HEADER";
    L_0x00cd:
        r4.append(r5);
        goto L_0x006f;
    L_0x00d1:
        r5 = " RFC822.HEADER";
        goto L_0x00cd;
    L_0x00d4:
        r5 = " RFC822.SIZE";
        goto L_0x007f;
    L_0x00d7:
        r5 = 0;
        r5 = (com.sun.mail.iap.Response[]) r5;	 Catch:{ all -> 0x00f6 }
        r13 = new java.util.Vector;	 Catch:{ all -> 0x00f6 }
        r13.<init>();	 Catch:{ all -> 0x00f6 }
        r0 = r18;
        r0 = r0.protocol;	 Catch:{ ConnectionException -> 0x00f9, CommandFailedException -> 0x010a, ProtocolException -> 0x010d }
        r7 = r0;
        r4 = r4.toString();	 Catch:{ ConnectionException -> 0x00f9, CommandFailedException -> 0x010a, ProtocolException -> 0x010d }
        r0 = r7;
        r1 = r19;
        r2 = r4;
        r19 = r0.fetch(r1, r2);	 Catch:{ ConnectionException -> 0x00f9, CommandFailedException -> 0x010a, ProtocolException -> 0x010d }
        r11 = r19;
    L_0x00f2:
        if (r11 != 0) goto L_0x011e;
    L_0x00f4:
        monitor-exit(r14);	 Catch:{ all -> 0x00f6 }
        goto L_0x00ba;
    L_0x00f6:
        r18 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00f6 }
        throw r18;
    L_0x00f9:
        r19 = move-exception;
        r20 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x00f6 }
        r19 = r19.getMessage();	 Catch:{ all -> 0x00f6 }
        r0 = r20;
        r1 = r18;
        r2 = r19;
        r0.m423init(r1, r2);	 Catch:{ all -> 0x00f6 }
        throw r20;	 Catch:{ all -> 0x00f6 }
    L_0x010a:
        r19 = move-exception;
        r11 = r5;
        goto L_0x00f2;
    L_0x010d:
        r18 = move-exception;
        r19 = new javax.mail.MessagingException;	 Catch:{ all -> 0x00f6 }
        r20 = r18.getMessage();	 Catch:{ all -> 0x00f6 }
        r0 = r19;
        r1 = r20;
        r2 = r18;
        r0.m314init(r1, r2);	 Catch:{ all -> 0x00f6 }
        throw r19;	 Catch:{ all -> 0x00f6 }
    L_0x011e:
        r19 = 0;
        r8 = r19;
    L_0x0122:
        r0 = r11;
        r0 = r0.length;	 Catch:{ all -> 0x00f6 }
        r19 = r0;
        r0 = r8;
        r1 = r19;
        if (r0 < r1) goto L_0x0143;
    L_0x012b:
        r19 = r13.size();	 Catch:{ all -> 0x00f6 }
        if (r19 == 0) goto L_0x0140;
    L_0x0131:
        r0 = r19;
        r0 = new com.sun.mail.iap.Response[r0];	 Catch:{ all -> 0x00f6 }
        r19 = r0;
        r0 = r13;
        r1 = r19;
        r0.copyInto(r1);	 Catch:{ all -> 0x00f6 }
        r18.handleResponses(r19);	 Catch:{ all -> 0x00f6 }
    L_0x0140:
        monitor-exit(r14);	 Catch:{ all -> 0x00f6 }
        goto L_0x00ba;
    L_0x0143:
        r19 = r11[r8];	 Catch:{ all -> 0x00f6 }
        if (r19 != 0) goto L_0x014c;
    L_0x0147:
        r19 = r8 + 1;
        r8 = r19;
        goto L_0x0122;
    L_0x014c:
        r19 = r11[r8];	 Catch:{ all -> 0x00f6 }
        r0 = r19;
        r0 = r0 instanceof com.sun.mail.imap.protocol.FetchResponse;	 Catch:{ all -> 0x00f6 }
        r19 = r0;
        if (r19 != 0) goto L_0x015f;
    L_0x0156:
        r19 = r11[r8];	 Catch:{ all -> 0x00f6 }
        r0 = r13;
        r1 = r19;
        r0.addElement(r1);	 Catch:{ all -> 0x00f6 }
        goto L_0x0147;
    L_0x015f:
        r5 = r11[r8];	 Catch:{ all -> 0x00f6 }
        r5 = (com.sun.mail.imap.protocol.FetchResponse) r5;	 Catch:{ all -> 0x00f6 }
        r19 = r5.getNumber();	 Catch:{ all -> 0x00f6 }
        r10 = r18.getMessageBySeqNumber(r19);	 Catch:{ all -> 0x00f6 }
        r19 = r5.getItemCount();	 Catch:{ all -> 0x00f6 }
        r7 = 0;
        r4 = 0;
        r9 = r4;
        r12 = r7;
    L_0x0173:
        r0 = r9;
        r1 = r19;
        if (r0 < r1) goto L_0x017e;
    L_0x0178:
        if (r12 == 0) goto L_0x0147;
    L_0x017a:
        r13.addElement(r5);	 Catch:{ all -> 0x00f6 }
        goto L_0x0147;
    L_0x017e:
        r4 = r5.getItem(r9);	 Catch:{ all -> 0x00f6 }
        r7 = r4 instanceof javax.mail.Flags;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01a0;
    L_0x0186:
        r7 = javax.mail.FetchProfile.Item.FLAGS;	 Catch:{ all -> 0x00f6 }
        r0 = r20;
        r1 = r7;
        r7 = r0.contains(r1);	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x0193;
    L_0x0191:
        if (r10 != 0) goto L_0x019a;
    L_0x0193:
        r4 = 1;
        r7 = r4;
    L_0x0195:
        r4 = r9 + 1;
        r9 = r4;
        r12 = r7;
        goto L_0x0173;
    L_0x019a:
        r4 = (javax.mail.Flags) r4;	 Catch:{ all -> 0x00f6 }
        r10.flags = r4;	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01a0:
        r7 = r4 instanceof com.sun.mail.imap.protocol.ENVELOPE;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01aa;
    L_0x01a4:
        r4 = (com.sun.mail.imap.protocol.ENVELOPE) r4;	 Catch:{ all -> 0x00f6 }
        r10.envelope = r4;	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01aa:
        r7 = r4 instanceof com.sun.mail.imap.protocol.INTERNALDATE;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01b8;
    L_0x01ae:
        r4 = (com.sun.mail.imap.protocol.INTERNALDATE) r4;	 Catch:{ all -> 0x00f6 }
        r4 = r4.getDate();	 Catch:{ all -> 0x00f6 }
        r10.receivedDate = r4;	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01b8:
        r7 = r4 instanceof com.sun.mail.imap.protocol.RFC822SIZE;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01c4;
    L_0x01bc:
        r4 = (com.sun.mail.imap.protocol.RFC822SIZE) r4;	 Catch:{ all -> 0x00f6 }
        r4 = r4.size;	 Catch:{ all -> 0x00f6 }
        r10.size = r4;	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01c4:
        r7 = r4 instanceof com.sun.mail.imap.protocol.BODYSTRUCTURE;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01ce;
    L_0x01c8:
        r4 = (com.sun.mail.imap.protocol.BODYSTRUCTURE) r4;	 Catch:{ all -> 0x00f6 }
        r10.bs = r4;	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01ce:
        r7 = r4 instanceof com.sun.mail.imap.protocol.UID;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x01fd;
    L_0x01d2:
        r4 = (com.sun.mail.imap.protocol.UID) r4;	 Catch:{ all -> 0x00f6 }
        r15 = r4.uid;	 Catch:{ all -> 0x00f6 }
        r10.uid = r15;	 Catch:{ all -> 0x00f6 }
        r0 = r18;
        r0 = r0.uidTable;	 Catch:{ all -> 0x00f6 }
        r7 = r0;
        if (r7 != 0) goto L_0x01e9;
    L_0x01df:
        r7 = new java.util.Hashtable;	 Catch:{ all -> 0x00f6 }
        r7.<init>();	 Catch:{ all -> 0x00f6 }
        r0 = r7;
        r1 = r18;
        r1.uidTable = r0;	 Catch:{ all -> 0x00f6 }
    L_0x01e9:
        r0 = r18;
        r0 = r0.uidTable;	 Catch:{ all -> 0x00f6 }
        r7 = r0;
        r15 = new java.lang.Long;	 Catch:{ all -> 0x00f6 }
        r0 = r4;
        r0 = r0.uid;	 Catch:{ all -> 0x00f6 }
        r16 = r0;
        r15.<init>(r16);	 Catch:{ all -> 0x00f6 }
        r7.put(r15, r10);	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x01fd:
        r7 = r4 instanceof com.sun.mail.imap.protocol.RFC822DATA;	 Catch:{ all -> 0x00f6 }
        if (r7 != 0) goto L_0x0205;
    L_0x0201:
        r7 = r4 instanceof com.sun.mail.imap.protocol.BODY;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x0261;
    L_0x0205:
        r7 = r4 instanceof com.sun.mail.imap.protocol.RFC822DATA;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x0229;
    L_0x0209:
        r4 = (com.sun.mail.imap.protocol.RFC822DATA) r4;	 Catch:{ all -> 0x00f6 }
        r4 = r4.getByteArrayInputStream();	 Catch:{ all -> 0x00f6 }
        r7 = r4;
    L_0x0210:
        r4 = new javax.mail.internet.InternetHeaders;	 Catch:{ all -> 0x00f6 }
        r4.m499init();	 Catch:{ all -> 0x00f6 }
        r4.load(r7);	 Catch:{ all -> 0x00f6 }
        r7 = r10.headers;	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x021e;
    L_0x021c:
        if (r3 == 0) goto L_0x0231;
    L_0x021e:
        r10.headers = r4;	 Catch:{ all -> 0x00f6 }
    L_0x0220:
        if (r3 == 0) goto L_0x025d;
    L_0x0222:
        r4 = 1;
        r10.setHeadersLoaded(r4);	 Catch:{ all -> 0x00f6 }
        r7 = r12;
        goto L_0x0195;
    L_0x0229:
        r4 = (com.sun.mail.imap.protocol.BODY) r4;	 Catch:{ all -> 0x00f6 }
        r4 = r4.getByteArrayInputStream();	 Catch:{ all -> 0x00f6 }
        r7 = r4;
        goto L_0x0210;
    L_0x0231:
        r4 = r4.getAllHeaders();	 Catch:{ all -> 0x00f6 }
    L_0x0235:
        r7 = r4.hasMoreElements();	 Catch:{ all -> 0x00f6 }
        if (r7 == 0) goto L_0x0220;
    L_0x023b:
        r7 = r4.nextElement();	 Catch:{ all -> 0x00f6 }
        r7 = (javax.mail.Header) r7;	 Catch:{ all -> 0x00f6 }
        r15 = r7.getName();	 Catch:{ all -> 0x00f6 }
        r15 = r10.isHeaderLoaded(r15);	 Catch:{ all -> 0x00f6 }
        if (r15 != 0) goto L_0x0235;
    L_0x024b:
        r15 = r10.headers;	 Catch:{ all -> 0x00f6 }
        r16 = r7.getName();	 Catch:{ all -> 0x00f6 }
        r7 = r7.getValue();	 Catch:{ all -> 0x00f6 }
        r0 = r15;
        r1 = r16;
        r2 = r7;
        r0.addHeader(r1, r2);	 Catch:{ all -> 0x00f6 }
        goto L_0x0235;
    L_0x025d:
        r4 = 0;
    L_0x025e:
        r7 = r6.length;	 Catch:{ all -> 0x00f6 }
        if (r4 < r7) goto L_0x0264;
    L_0x0261:
        r7 = r12;
        goto L_0x0195;
    L_0x0264:
        r7 = r6[r4];	 Catch:{ all -> 0x00f6 }
        r10.setHeaderLoaded(r7);	 Catch:{ all -> 0x00f6 }
        r4 = r4 + 1;
        goto L_0x025e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPMessage.fetch(com.sun.mail.imap.IMAPFolder, javax.mail.Message[], javax.mail.FetchProfile):void");
    }

    private synchronized void loadEnvelope() throws MessagingException {
        if (this.envelope == null) {
            Response[] r = null;
            synchronized (getMessageCacheLock()) {
                try {
                    IMAPProtocol p = getProtocol();
                    checkExpunged();
                    int seqnum = getSequenceNumber();
                    r = p.fetch(seqnum, EnvelopeCmd);
                    int i = 0;
                    while (i < r.length) {
                        if (r[i] != null && (r[i] instanceof FetchResponse) && ((FetchResponse) r[i]).getNumber() == seqnum) {
                            FetchResponse f = r[i];
                            int count = f.getItemCount();
                            for (int j = 0; j < count; j++) {
                                com.sun.mail.imap.protocol.Item item = f.getItem(j);
                                if (item instanceof ENVELOPE) {
                                    this.envelope = (ENVELOPE) item;
                                } else if (item instanceof INTERNALDATE) {
                                    this.receivedDate = ((INTERNALDATE) item).getDate();
                                } else if (item instanceof RFC822SIZE) {
                                    this.size = ((RFC822SIZE) item).size;
                                }
                            }
                        }
                        i++;
                    }
                    p.notifyResponseHandlers(r);
                    p.handleResult(r[r.length - 1]);
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this.folder, e.getMessage());
                } catch (ProtocolException e2) {
                    ProtocolException pex = e2;
                    forceCheckExpunged();
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
            if (this.envelope == null) {
                throw new MessagingException("Failed to load IMAP envelope");
            }
        }
    }

    private static String craftHeaderCmd(IMAPProtocol p, String[] hdrs) {
        StringBuffer sb;
        if (p.isREV1()) {
            sb = new StringBuffer("BODY.PEEK[HEADER.FIELDS (");
        } else {
            sb = new StringBuffer("RFC822.HEADER.LINES (");
        }
        for (int i = 0; i < hdrs.length; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(hdrs[i]);
        }
        if (p.isREV1()) {
            sb.append(")]");
        } else {
            sb.append(")");
        }
        return sb.toString();
    }

    private synchronized void loadBODYSTRUCTURE() throws MessagingException {
        if (this.bs == null) {
            synchronized (getMessageCacheLock()) {
                try {
                    IMAPProtocol p = getProtocol();
                    checkExpunged();
                    this.bs = p.fetchBodyStructure(getSequenceNumber());
                    if (this.bs == null) {
                        forceCheckExpunged();
                        throw new MessagingException("Unable to load BODYSTRUCTURE");
                    }
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this.folder, e.getMessage());
                } catch (ProtocolException e2) {
                    ProtocolException pex = e2;
                    forceCheckExpunged();
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
        }
    }

    private synchronized void loadHeaders() throws MessagingException {
        if (!this.headersLoaded) {
            InputStream is = null;
            synchronized (getMessageCacheLock()) {
                try {
                    IMAPProtocol p = getProtocol();
                    checkExpunged();
                    if (p.isREV1()) {
                        BODY b = p.peekBody(getSequenceNumber(), toSection("HEADER"));
                        if (b != null) {
                            is = b.getByteArrayInputStream();
                        }
                    } else {
                        RFC822DATA rd = p.fetchRFC822(getSequenceNumber(), "HEADER");
                        if (rd != null) {
                            is = rd.getByteArrayInputStream();
                        }
                    }
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this.folder, e.getMessage());
                } catch (ProtocolException e2) {
                    ProtocolException pex = e2;
                    forceCheckExpunged();
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
            if (is == null) {
                throw new MessagingException("Cannot load header");
            }
            this.headers = new InternetHeaders(is);
            this.headersLoaded = true;
        }
    }

    private synchronized void loadFlags() throws MessagingException {
        if (this.flags == null) {
            synchronized (getMessageCacheLock()) {
                try {
                    IMAPProtocol p = getProtocol();
                    checkExpunged();
                    this.flags = p.fetchFlags(getSequenceNumber());
                } catch (ConnectionException e) {
                    throw new FolderClosedException(this.folder, e.getMessage());
                } catch (ProtocolException e2) {
                    ProtocolException pex = e2;
                    forceCheckExpunged();
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized boolean areHeadersLoaded() {
        return this.headersLoaded;
    }

    private synchronized void setHeadersLoaded(boolean loaded) {
        this.headersLoaded = loaded;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized boolean isHeaderLoaded(String name) {
        boolean z;
        if (this.headersLoaded) {
            z = true;
        } else if (this.loadedHeaders != null) {
            z = this.loadedHeaders.containsKey(name.toUpperCase(Locale.ENGLISH));
        } else {
            z = false;
        }
        return z;
    }

    private synchronized void setHeaderLoaded(String name) {
        if (this.loadedHeaders == null) {
            this.loadedHeaders = new Hashtable(1);
        }
        this.loadedHeaders.put(name.toUpperCase(Locale.ENGLISH), name);
    }

    private String toSection(String what) {
        if (this.sectionId == null) {
            return what;
        }
        return this.sectionId + "." + what;
    }

    private InternetAddress[] aaclone(InternetAddress[] aa) {
        if (aa == null) {
            return null;
        }
        return (InternetAddress[]) aa.clone();
    }

    /* access modifiers changed from: private */
    public Flags _getFlags() {
        return this.flags;
    }

    /* access modifiers changed from: private */
    public ENVELOPE _getEnvelope() {
        return this.envelope;
    }

    /* access modifiers changed from: private */
    public BODYSTRUCTURE _getBodyStructure() {
        return this.bs;
    }

    /* access modifiers changed from: 0000 */
    public void _setFlags(Flags flags) {
        this.flags = flags;
    }

    /* access modifiers changed from: 0000 */
    public Session _getSession() {
        return this.session;
    }
}
