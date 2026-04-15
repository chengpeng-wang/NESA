package com.sun.mail.imap;

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
            } catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            } catch (ProtocolException e) {
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
        return this.bs.cParams.get("name");
    }

    public void setFileName(String filename) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:22:0x0050, code skipped:
            if (r2 != null) goto L_0x0098;
     */
    /* JADX WARNING: Missing block: B:24:0x0059, code skipped:
            throw new javax.mail.MessagingException("No content");
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            return r2;
     */
    public java.io.InputStream getContentStream() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r8 = -1;
        r2 = 0;
        r5 = r12.getPeek();
        r9 = r12.getMessageCacheLock();
        monitor-enter(r9);
        r3 = r12.getProtocol();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r12.checkExpunged();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r7 = r3.isREV1();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        if (r7 == 0) goto L_0x0033;
    L_0x0018:
        r7 = r12.getFetchBlockSize();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        if (r7 == r8) goto L_0x0033;
    L_0x001e:
        r7 = new com.sun.mail.imap.IMAPInputStream;	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r10 = "TEXT";
        r10 = r12.toSection(r10);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r11 = r12.bs;	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        if (r11 == 0) goto L_0x002e;
    L_0x002a:
        r8 = r12.bs;	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r8 = r8.size;	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
    L_0x002e:
        r7.m301init(r12, r10, r8, r5);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        monitor-exit(r9);	 Catch:{ all -> 0x0087 }
    L_0x0032:
        return r7;
    L_0x0033:
        r7 = r3.isREV1();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        if (r7 == 0) goto L_0x0069;
    L_0x0039:
        if (r5 == 0) goto L_0x005a;
    L_0x003b:
        r7 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r8 = "TEXT";
        r8 = r12.toSection(r8);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r0 = r3.peekBody(r7, r8);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
    L_0x0049:
        if (r0 == 0) goto L_0x004f;
    L_0x004b:
        r2 = r0.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
    L_0x004f:
        monitor-exit(r9);	 Catch:{ all -> 0x0087 }
        if (r2 != 0) goto L_0x0098;
    L_0x0052:
        r7 = new javax.mail.MessagingException;
        r8 = "No content";
        r7.m403init(r8);
        throw r7;
    L_0x005a:
        r7 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r8 = "TEXT";
        r8 = r12.toSection(r8);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r0 = r3.fetchBody(r7, r8);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        goto L_0x0049;
    L_0x0069:
        r7 = r12.getSequenceNumber();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        r8 = "TEXT";
        r6 = r3.fetchRFC822(r7, r8);	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        if (r6 == 0) goto L_0x004f;
    L_0x0075:
        r2 = r6.getByteArrayInputStream();	 Catch:{ ConnectionException -> 0x007a, ProtocolException -> 0x008a }
        goto L_0x004f;
    L_0x007a:
        r1 = move-exception;
        r7 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x0087 }
        r8 = r12.folder;	 Catch:{ all -> 0x0087 }
        r10 = r1.getMessage();	 Catch:{ all -> 0x0087 }
        r7.m692init(r8, r10);	 Catch:{ all -> 0x0087 }
        throw r7;	 Catch:{ all -> 0x0087 }
    L_0x0087:
        r7 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0087 }
        throw r7;
    L_0x008a:
        r4 = move-exception;
        r12.forceCheckExpunged();	 Catch:{ all -> 0x0087 }
        r7 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0087 }
        r8 = r4.getMessage();	 Catch:{ all -> 0x0087 }
        r7.m404init(r8, r4);	 Catch:{ all -> 0x0087 }
        throw r7;	 Catch:{ all -> 0x0087 }
    L_0x0098:
        r7 = r2;
        goto L_0x0032;
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
            } catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            } catch (ProtocolException pex) {
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
            } catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            } catch (ProtocolException pex) {
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
            } catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            } catch (ProtocolException pex) {
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

    /* JADX WARNING: Missing block: B:165:?, code skipped:
            return;
     */
    static void fetch(com.sun.mail.imap.IMAPFolder r33, javax.mail.Message[] r34, javax.mail.FetchProfile r35) throws javax.mail.MessagingException {
        /*
        r5 = new java.lang.StringBuffer;
        r5.<init>();
        r10 = 1;
        r3 = 0;
        r28 = javax.mail.FetchProfile.Item.ENVELOPE;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x001b;
    L_0x0013:
        r28 = EnvelopeCmd;
        r0 = r28;
        r5.append(r0);
        r10 = 0;
    L_0x001b:
        r28 = javax.mail.FetchProfile.Item.FLAGS;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x0031;
    L_0x0027:
        if (r10 == 0) goto L_0x00d6;
    L_0x0029:
        r28 = "FLAGS";
    L_0x002b:
        r0 = r28;
        r5.append(r0);
        r10 = 0;
    L_0x0031:
        r28 = javax.mail.FetchProfile.Item.CONTENT_INFO;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x0047;
    L_0x003d:
        if (r10 == 0) goto L_0x00da;
    L_0x003f:
        r28 = "BODYSTRUCTURE";
    L_0x0041:
        r0 = r28;
        r5.append(r0);
        r10 = 0;
    L_0x0047:
        r28 = javax.mail.UIDFolder.FetchProfileItem.UID;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x005d;
    L_0x0053:
        if (r10 == 0) goto L_0x00de;
    L_0x0055:
        r28 = "UID";
    L_0x0057:
        r0 = r28;
        r5.append(r0);
        r10 = 0;
    L_0x005d:
        r28 = com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x0080;
    L_0x0069:
        r3 = 1;
        r0 = r33;
        r0 = r0.protocol;
        r28 = r0;
        r28 = r28.isREV1();
        if (r28 == 0) goto L_0x00e5;
    L_0x0076:
        if (r10 == 0) goto L_0x00e2;
    L_0x0078:
        r28 = "BODY.PEEK[HEADER]";
    L_0x007a:
        r0 = r28;
        r5.append(r0);
    L_0x007f:
        r10 = 0;
    L_0x0080:
        r28 = com.sun.mail.imap.IMAPFolder.FetchProfileItem.SIZE;
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);
        if (r28 == 0) goto L_0x0096;
    L_0x008c:
        if (r10 == 0) goto L_0x00f2;
    L_0x008e:
        r28 = "RFC822.SIZE";
    L_0x0090:
        r0 = r28;
        r5.append(r0);
        r10 = 0;
    L_0x0096:
        r12 = 0;
        r12 = (java.lang.String[]) r12;
        if (r3 != 0) goto L_0x00be;
    L_0x009b:
        r12 = r35.getHeaderNames();
        r0 = r12.length;
        r28 = r0;
        if (r28 <= 0) goto L_0x00be;
    L_0x00a4:
        if (r10 != 0) goto L_0x00ad;
    L_0x00a6:
        r28 = " ";
        r0 = r28;
        r5.append(r0);
    L_0x00ad:
        r0 = r33;
        r0 = r0.protocol;
        r28 = r0;
        r0 = r28;
        r28 = craftHeaderCmd(r0, r12);
        r0 = r28;
        r5.append(r0);
    L_0x00be:
        r6 = new com.sun.mail.imap.IMAPMessage$1FetchProfileCondition;
        r0 = r35;
        r6.m635init(r0);
        r0 = r33;
        r0 = r0.messageCacheLock;
        r29 = r0;
        monitor-enter(r29);
        r0 = r34;
        r20 = com.sun.mail.imap.Utility.toMessageSet(r0, r6);	 Catch:{ all -> 0x0116 }
        if (r20 != 0) goto L_0x00f5;
    L_0x00d4:
        monitor-exit(r29);	 Catch:{ all -> 0x0116 }
    L_0x00d5:
        return;
    L_0x00d6:
        r28 = " FLAGS";
        goto L_0x002b;
    L_0x00da:
        r28 = " BODYSTRUCTURE";
        goto L_0x0041;
    L_0x00de:
        r28 = " UID";
        goto L_0x0057;
    L_0x00e2:
        r28 = " BODY.PEEK[HEADER]";
        goto L_0x007a;
    L_0x00e5:
        if (r10 == 0) goto L_0x00ef;
    L_0x00e7:
        r28 = "RFC822.HEADER";
    L_0x00e9:
        r0 = r28;
        r5.append(r0);
        goto L_0x007f;
    L_0x00ef:
        r28 = " RFC822.HEADER";
        goto L_0x00e9;
    L_0x00f2:
        r28 = " RFC822.SIZE";
        goto L_0x0090;
    L_0x00f5:
        r22 = 0;
        r22 = (com.sun.mail.iap.Response[]) r22;	 Catch:{ all -> 0x0116 }
        r27 = new java.util.Vector;	 Catch:{ all -> 0x0116 }
        r27.<init>();	 Catch:{ all -> 0x0116 }
        r0 = r33;
        r0 = r0.protocol;	 Catch:{ ConnectionException -> 0x0119, CommandFailedException -> 0x02f3, ProtocolException -> 0x012a }
        r28 = r0;
        r30 = r5.toString();	 Catch:{ ConnectionException -> 0x0119, CommandFailedException -> 0x02f3, ProtocolException -> 0x012a }
        r0 = r28;
        r1 = r20;
        r2 = r30;
        r22 = r0.fetch(r1, r2);	 Catch:{ ConnectionException -> 0x0119, CommandFailedException -> 0x02f3, ProtocolException -> 0x012a }
    L_0x0112:
        if (r22 != 0) goto L_0x013b;
    L_0x0114:
        monitor-exit(r29);	 Catch:{ all -> 0x0116 }
        goto L_0x00d5;
    L_0x0116:
        r28 = move-exception;
        monitor-exit(r29);	 Catch:{ all -> 0x0116 }
        throw r28;
    L_0x0119:
        r4 = move-exception;
        r28 = new javax.mail.FolderClosedException;	 Catch:{ all -> 0x0116 }
        r30 = r4.getMessage();	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r33;
        r2 = r30;
        r0.m692init(r1, r2);	 Catch:{ all -> 0x0116 }
        throw r28;	 Catch:{ all -> 0x0116 }
    L_0x012a:
        r21 = move-exception;
        r28 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0116 }
        r30 = r21.getMessage();	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r30;
        r2 = r21;
        r0.m404init(r1, r2);	 Catch:{ all -> 0x0116 }
        throw r28;	 Catch:{ all -> 0x0116 }
    L_0x013b:
        r15 = 0;
    L_0x013c:
        r0 = r22;
        r0 = r0.length;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        r0 = r28;
        if (r15 < r0) goto L_0x0162;
    L_0x0145:
        r24 = r27.size();	 Catch:{ all -> 0x0116 }
        if (r24 == 0) goto L_0x015f;
    L_0x014b:
        r0 = r24;
        r0 = new com.sun.mail.iap.Response[r0];	 Catch:{ all -> 0x0116 }
        r23 = r0;
        r0 = r27;
        r1 = r23;
        r0.copyInto(r1);	 Catch:{ all -> 0x0116 }
        r0 = r33;
        r1 = r23;
        r0.handleResponses(r1);	 Catch:{ all -> 0x0116 }
    L_0x015f:
        monitor-exit(r29);	 Catch:{ all -> 0x0116 }
        goto L_0x00d5;
    L_0x0162:
        r28 = r22[r15];	 Catch:{ all -> 0x0116 }
        if (r28 != 0) goto L_0x0169;
    L_0x0166:
        r15 = r15 + 1;
        goto L_0x013c;
    L_0x0169:
        r28 = r22[r15];	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r0 = r0 instanceof com.sun.mail.imap.protocol.FetchResponse;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 != 0) goto L_0x0179;
    L_0x0173:
        r28 = r22[r15];	 Catch:{ all -> 0x0116 }
        r27.addElement(r28);	 Catch:{ all -> 0x0116 }
        goto L_0x0166;
    L_0x0179:
        r9 = r22[r15];	 Catch:{ all -> 0x0116 }
        r9 = (com.sun.mail.imap.protocol.FetchResponse) r9;	 Catch:{ all -> 0x0116 }
        r28 = r9.getNumber();	 Catch:{ all -> 0x0116 }
        r0 = r33;
        r1 = r28;
        r19 = r0.getMessageBySeqNumber(r1);	 Catch:{ all -> 0x0116 }
        r7 = r9.getItemCount();	 Catch:{ all -> 0x0116 }
        r26 = 0;
        r17 = 0;
    L_0x0191:
        r0 = r17;
        if (r0 < r7) goto L_0x019d;
    L_0x0195:
        if (r26 == 0) goto L_0x0166;
    L_0x0197:
        r0 = r27;
        r0.addElement(r9);	 Catch:{ all -> 0x0116 }
        goto L_0x0166;
    L_0x019d:
        r0 = r17;
        r16 = r9.getItem(r0);	 Catch:{ all -> 0x0116 }
        r0 = r16;
        r0 = r0 instanceof javax.mail.Flags;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x01c7;
    L_0x01ab:
        r28 = javax.mail.FetchProfile.Item.FLAGS;	 Catch:{ all -> 0x0116 }
        r0 = r35;
        r1 = r28;
        r28 = r0.contains(r1);	 Catch:{ all -> 0x0116 }
        if (r28 == 0) goto L_0x01b9;
    L_0x01b7:
        if (r19 != 0) goto L_0x01be;
    L_0x01b9:
        r26 = 1;
    L_0x01bb:
        r17 = r17 + 1;
        goto L_0x0191;
    L_0x01be:
        r16 = (javax.mail.Flags) r16;	 Catch:{ all -> 0x0116 }
        r0 = r16;
        r1 = r19;
        r1.flags = r0;	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x01c7:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.ENVELOPE;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x01d8;
    L_0x01cf:
        r16 = (com.sun.mail.imap.protocol.ENVELOPE) r16;	 Catch:{ all -> 0x0116 }
        r0 = r16;
        r1 = r19;
        r1.envelope = r0;	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x01d8:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.INTERNALDATE;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x01ed;
    L_0x01e0:
        r16 = (com.sun.mail.imap.protocol.INTERNALDATE) r16;	 Catch:{ all -> 0x0116 }
        r28 = r16.getDate();	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r19;
        r1.receivedDate = r0;	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x01ed:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.RFC822SIZE;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x0204;
    L_0x01f5:
        r16 = (com.sun.mail.imap.protocol.RFC822SIZE) r16;	 Catch:{ all -> 0x0116 }
        r0 = r16;
        r0 = r0.size;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        r0 = r28;
        r1 = r19;
        r1.size = r0;	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x0204:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.BODYSTRUCTURE;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x0215;
    L_0x020c:
        r16 = (com.sun.mail.imap.protocol.BODYSTRUCTURE) r16;	 Catch:{ all -> 0x0116 }
        r0 = r16;
        r1 = r19;
        r1.bs = r0;	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x0215:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.UID;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x025e;
    L_0x021d:
        r0 = r16;
        r0 = (com.sun.mail.imap.protocol.UID) r0;	 Catch:{ all -> 0x0116 }
        r25 = r0;
        r0 = r25;
        r0 = r0.uid;	 Catch:{ all -> 0x0116 }
        r30 = r0;
        r0 = r30;
        r2 = r19;
        r2.uid = r0;	 Catch:{ all -> 0x0116 }
        r0 = r33;
        r0 = r0.uidTable;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 != 0) goto L_0x0242;
    L_0x0237:
        r28 = new java.util.Hashtable;	 Catch:{ all -> 0x0116 }
        r28.<init>();	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r33;
        r1.uidTable = r0;	 Catch:{ all -> 0x0116 }
    L_0x0242:
        r0 = r33;
        r0 = r0.uidTable;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        r30 = new java.lang.Long;	 Catch:{ all -> 0x0116 }
        r0 = r25;
        r0 = r0.uid;	 Catch:{ all -> 0x0116 }
        r31 = r0;
        r30.<init>(r31);	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r30;
        r2 = r19;
        r0.put(r1, r2);	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x025e:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.RFC822DATA;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 != 0) goto L_0x026e;
    L_0x0266:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.BODY;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x01bb;
    L_0x026e:
        r0 = r16;
        r0 = r0 instanceof com.sun.mail.imap.protocol.RFC822DATA;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x029f;
    L_0x0276:
        r16 = (com.sun.mail.imap.protocol.RFC822DATA) r16;	 Catch:{ all -> 0x0116 }
        r14 = r16.getByteArrayInputStream();	 Catch:{ all -> 0x0116 }
    L_0x027c:
        r11 = new javax.mail.internet.InternetHeaders;	 Catch:{ all -> 0x0116 }
        r11.m439init();	 Catch:{ all -> 0x0116 }
        r11.load(r14);	 Catch:{ all -> 0x0116 }
        r0 = r19;
        r0 = r0.headers;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        if (r28 == 0) goto L_0x028e;
    L_0x028c:
        if (r3 == 0) goto L_0x02a6;
    L_0x028e:
        r0 = r19;
        r0.headers = r11;	 Catch:{ all -> 0x0116 }
    L_0x0292:
        if (r3 == 0) goto L_0x02dc;
    L_0x0294:
        r28 = 1;
        r0 = r19;
        r1 = r28;
        r0.setHeadersLoaded(r1);	 Catch:{ all -> 0x0116 }
        goto L_0x01bb;
    L_0x029f:
        r16 = (com.sun.mail.imap.protocol.BODY) r16;	 Catch:{ all -> 0x0116 }
        r14 = r16.getByteArrayInputStream();	 Catch:{ all -> 0x0116 }
        goto L_0x027c;
    L_0x02a6:
        r8 = r11.getAllHeaders();	 Catch:{ all -> 0x0116 }
    L_0x02aa:
        r28 = r8.hasMoreElements();	 Catch:{ all -> 0x0116 }
        if (r28 == 0) goto L_0x0292;
    L_0x02b0:
        r13 = r8.nextElement();	 Catch:{ all -> 0x0116 }
        r13 = (javax.mail.Header) r13;	 Catch:{ all -> 0x0116 }
        r28 = r13.getName();	 Catch:{ all -> 0x0116 }
        r0 = r19;
        r1 = r28;
        r28 = r0.isHeaderLoaded(r1);	 Catch:{ all -> 0x0116 }
        if (r28 != 0) goto L_0x02aa;
    L_0x02c4:
        r0 = r19;
        r0 = r0.headers;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        r30 = r13.getName();	 Catch:{ all -> 0x0116 }
        r31 = r13.getValue();	 Catch:{ all -> 0x0116 }
        r0 = r28;
        r1 = r30;
        r2 = r31;
        r0.addHeader(r1, r2);	 Catch:{ all -> 0x0116 }
        goto L_0x02aa;
    L_0x02dc:
        r18 = 0;
    L_0x02de:
        r0 = r12.length;	 Catch:{ all -> 0x0116 }
        r28 = r0;
        r0 = r18;
        r1 = r28;
        if (r0 >= r1) goto L_0x01bb;
    L_0x02e7:
        r28 = r12[r18];	 Catch:{ all -> 0x0116 }
        r0 = r19;
        r1 = r28;
        r0.setHeaderLoaded(r1);	 Catch:{ all -> 0x0116 }
        r18 = r18 + 1;
        goto L_0x02de;
    L_0x02f3:
        r28 = move-exception;
        goto L_0x0112;
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
                } catch (ConnectionException cex) {
                    throw new FolderClosedException(this.folder, cex.getMessage());
                } catch (ProtocolException pex) {
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
                } catch (ConnectionException cex) {
                    throw new FolderClosedException(this.folder, cex.getMessage());
                } catch (ProtocolException pex) {
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
                } catch (ConnectionException cex) {
                    throw new FolderClosedException(this.folder, cex.getMessage());
                } catch (ProtocolException pex) {
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
                } catch (ConnectionException cex) {
                    throw new FolderClosedException(this.folder, cex.getMessage());
                } catch (ProtocolException pex) {
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
        return this.sectionId == null ? what : this.sectionId + "." + what;
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
