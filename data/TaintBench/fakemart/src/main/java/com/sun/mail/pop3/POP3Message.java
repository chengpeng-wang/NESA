package com.sun.mail.pop3;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.IllegalWriteException;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.SharedInputStream;

public class POP3Message extends MimeMessage {
    static final String UNKNOWN = "UNKNOWN";
    private POP3Folder folder;
    private int hdrSize = -1;
    private int msgSize = -1;
    String uid = UNKNOWN;

    public POP3Message(Folder folder, int msgno) throws MessagingException {
        super(folder, msgno);
        this.folder = (POP3Folder) folder;
    }

    public void setFlags(Flags newFlags, boolean set) throws MessagingException {
        Flags oldFlags = (Flags) this.flags.clone();
        super.setFlags(newFlags, set);
        if (!this.flags.equals(oldFlags)) {
            this.folder.notifyMessageChangedListeners(1, this);
        }
    }

    public int getSize() throws MessagingException {
        try {
            int i;
            synchronized (this) {
                if (this.msgSize >= 0) {
                    i = this.msgSize;
                } else {
                    if (this.msgSize < 0) {
                        if (this.headers == null) {
                            loadHeaders();
                        }
                        if (this.contentStream != null) {
                            this.msgSize = this.contentStream.available();
                        } else {
                            this.msgSize = this.folder.getProtocol().list(this.msgnum) - this.hdrSize;
                        }
                    }
                    i = this.msgSize;
                }
            }
            return i;
        } catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        } catch (IOException ex) {
            throw new MessagingException("error getting size", ex);
        }
    }

    /* access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        try {
            synchronized (this) {
                if (this.contentStream == null) {
                    int i;
                    Protocol protocol = this.folder.getProtocol();
                    int i2 = this.msgnum;
                    if (this.msgSize > 0) {
                        i = this.msgSize + this.hdrSize;
                    } else {
                        i = 0;
                    }
                    InputStream rawcontent = protocol.retr(i2, i);
                    if (rawcontent == null) {
                        this.expunged = true;
                        throw new MessageRemovedException();
                    }
                    if (this.headers == null || ((POP3Store) this.folder.getStore()).forgetTopHeaders) {
                        this.headers = new InternetHeaders(rawcontent);
                        this.hdrSize = (int) ((SharedInputStream) rawcontent).getPosition();
                    } else {
                        int len;
                        do {
                            len = 0;
                            while (true) {
                                int c1 = rawcontent.read();
                                if (c1 < 0 || c1 == 10) {
                                    break;
                                } else if (c1 != 13) {
                                    len++;
                                } else if (rawcontent.available() > 0) {
                                    rawcontent.mark(1);
                                    if (rawcontent.read() != 10) {
                                        rawcontent.reset();
                                    }
                                }
                            }
                            if (rawcontent.available() != 0) {
                                break;
                            }
                            break;
                        } while (len != 0);
                        this.hdrSize = (int) ((SharedInputStream) rawcontent).getPosition();
                    }
                    this.contentStream = ((SharedInputStream) rawcontent).newStream((long) this.hdrSize, -1);
                }
            }
            return super.getContentStream();
        } catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        } catch (IOException ex) {
            throw new MessagingException("error fetching POP3 content", ex);
        }
    }

    public synchronized void invalidate(boolean invalidateHeaders) {
        this.content = null;
        this.contentStream = null;
        this.msgSize = -1;
        if (invalidateHeaders) {
            this.headers = null;
            this.hdrSize = -1;
        }
    }

    public InputStream top(int n) throws MessagingException {
        try {
            InputStream top;
            synchronized (this) {
                top = this.folder.getProtocol().top(this.msgnum, n);
            }
            return top;
        } catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        } catch (IOException ex) {
            throw new MessagingException("error getting size", ex);
        }
    }

    public String[] getHeader(String name) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public void addHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public void removeHeader(String name) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        if (this.headers == null) {
            loadHeaders();
        }
        return this.headers.getNonMatchingHeaderLines(names);
    }

    public void saveChanges() throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    /* JADX WARNING: Missing block: B:27:?, code skipped:
            return;
     */
    private void loadHeaders() throws javax.mail.MessagingException {
        /*
        r8 = this;
        r7 = 0;
        monitor-enter(r8);	 Catch:{ EOFException -> 0x0030, IOException -> 0x0050 }
        r4 = r8.headers;	 Catch:{ all -> 0x002d }
        if (r4 == 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r8);	 Catch:{ all -> 0x002d }
    L_0x0007:
        return;
    L_0x0008:
        r3 = 0;
        r4 = r8.folder;	 Catch:{ all -> 0x002d }
        r4 = r4.getStore();	 Catch:{ all -> 0x002d }
        r4 = (com.sun.mail.pop3.POP3Store) r4;	 Catch:{ all -> 0x002d }
        r4 = r4.disableTop;	 Catch:{ all -> 0x002d }
        if (r4 != 0) goto L_0x0024;
    L_0x0015:
        r4 = r8.folder;	 Catch:{ all -> 0x002d }
        r4 = r4.getProtocol();	 Catch:{ all -> 0x002d }
        r5 = r8.msgnum;	 Catch:{ all -> 0x002d }
        r6 = 0;
        r3 = r4.top(r5, r6);	 Catch:{ all -> 0x002d }
        if (r3 != 0) goto L_0x0042;
    L_0x0024:
        r0 = r8.getContentStream();	 Catch:{ all -> 0x002d }
        r0.close();	 Catch:{ all -> 0x002d }
    L_0x002b:
        monitor-exit(r8);	 Catch:{ all -> 0x002d }
        goto L_0x0007;
    L_0x002d:
        r4 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x002d }
        throw r4;	 Catch:{ EOFException -> 0x0030, IOException -> 0x0050 }
    L_0x0030:
        r1 = move-exception;
        r4 = r8.folder;
        r4.close(r7);
        r4 = new javax.mail.FolderClosedException;
        r5 = r8.folder;
        r6 = r1.toString();
        r4.m692init(r5, r6);
        throw r4;
    L_0x0042:
        r4 = r3.available();	 Catch:{ all -> 0x002d }
        r8.hdrSize = r4;	 Catch:{ all -> 0x002d }
        r4 = new javax.mail.internet.InternetHeaders;	 Catch:{ all -> 0x002d }
        r4.m440init(r3);	 Catch:{ all -> 0x002d }
        r8.headers = r4;	 Catch:{ all -> 0x002d }
        goto L_0x002b;
    L_0x0050:
        r2 = move-exception;
        r4 = new javax.mail.MessagingException;
        r5 = "error loading POP3 headers";
        r4.m404init(r5, r2);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.POP3Message.loadHeaders():void");
    }
}
