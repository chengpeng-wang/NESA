package com.sun.mail.pop3;

import com.googleprojects.mm.MMMailContentUtil;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Vector;
import javax.mail.FetchProfile;
import javax.mail.FetchProfile.Item;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.MethodNotSupportedException;
import javax.mail.UIDFolder.FetchProfileItem;

public class POP3Folder extends Folder {
    private boolean doneUidl = false;
    private boolean exists = false;
    private Vector message_cache;
    private String name;
    private boolean opened = false;
    private Protocol port;
    private int size;
    private int total;

    POP3Folder(POP3Store store, String name) {
        super(store);
        this.name = name;
        if (name.equalsIgnoreCase("INBOX")) {
            this.exists = true;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.name;
    }

    public Folder getParent() {
        return new DefaultFolder((POP3Store) this.store);
    }

    public boolean exists() {
        return this.exists;
    }

    public Folder[] list(String pattern) throws MessagingException {
        throw new MessagingException("not a directory");
    }

    public char getSeparator() {
        return 0;
    }

    public int getType() {
        return 1;
    }

    public boolean create(int type) throws MessagingException {
        return false;
    }

    public boolean hasNewMessages() throws MessagingException {
        return false;
    }

    public Folder getFolder(String name) throws MessagingException {
        throw new MessagingException("not a directory");
    }

    public boolean delete(boolean recurse) throws MessagingException {
        throw new MethodNotSupportedException("delete");
    }

    public boolean renameTo(Folder f) throws MessagingException {
        throw new MethodNotSupportedException("renameTo");
    }

    public synchronized void open(int mode) throws MessagingException {
        checkClosed();
        if (this.exists) {
            try {
                this.port = ((POP3Store) this.store).getPort(this);
                Status s = this.port.stat();
                this.total = s.total;
                this.size = s.size;
                this.mode = mode;
                this.opened = true;
                this.message_cache = new Vector(this.total);
                this.message_cache.setSize(this.total);
                this.doneUidl = false;
                notifyConnectionListeners(1);
            } catch (IOException e) {
                this.port = null;
                ((POP3Store) this.store).closePort(this);
            } catch (IOException ioex) {
                if (this.port != null) {
                    this.port.quit();
                }
                this.port = null;
                ((POP3Store) this.store).closePort(this);
            } catch (Throwable th) {
                Throwable th2 = th;
                this.port = null;
                ((POP3Store) this.store).closePort(this);
            }
        } else {
            throw new FolderNotFoundException((Folder) this, "folder is not INBOX");
        }
        return;
        throw new MessagingException("Open failed", ioex);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x007a A:{Splitter:B:3:0x0004, ExcHandler: all (r3_1 'th' java.lang.Throwable)} */
    /* JADX WARNING: Missing block: B:38:0x007a, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:39:0x007b, code skipped:
            r4 = r3;
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r5.port = null;
            ((com.sun.mail.pop3.POP3Store) r5.store).closePort(r5);
            r5.message_cache = null;
            r5.opened = false;
            notifyConnectionListeners(3);
     */
    public synchronized void close(boolean r6) throws javax.mail.MessagingException {
        /*
        r5 = this;
        monitor-enter(r5);
        r5.checkOpen();	 Catch:{ all -> 0x0077 }
        r3 = r5.store;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3 = (com.sun.mail.pop3.POP3Store) r3;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3 = r3.rsetBeforeQuit;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        if (r3 == 0) goto L_0x0011;
    L_0x000c:
        r3 = r5.port;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3.rset();	 Catch:{ IOException -> 0x0061, all -> 0x007a }
    L_0x0011:
        if (r6 == 0) goto L_0x0021;
    L_0x0013:
        r3 = r5.mode;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r4 = 2;
        if (r3 != r4) goto L_0x0021;
    L_0x0018:
        r0 = 0;
    L_0x0019:
        r3 = r5.message_cache;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3 = r3.size();	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        if (r0 < r3) goto L_0x003c;
    L_0x0021:
        r3 = r5.port;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3.quit();	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3 = 0;
        r5.port = r3;	 Catch:{ all -> 0x0077 }
        r3 = r5.store;	 Catch:{ all -> 0x0077 }
        r3 = (com.sun.mail.pop3.POP3Store) r3;	 Catch:{ all -> 0x0077 }
        r3.closePort(r5);	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.message_cache = r3;	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.opened = r3;	 Catch:{ all -> 0x0077 }
        r3 = 3;
        r5.notifyConnectionListeners(r3);	 Catch:{ all -> 0x0077 }
    L_0x003a:
        monitor-exit(r5);
        return;
    L_0x003c:
        r3 = r5.message_cache;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r2 = r3.elementAt(r0);	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r2 = (com.sun.mail.pop3.POP3Message) r2;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        if (r2 == 0) goto L_0x0055;
    L_0x0046:
        r3 = javax.mail.Flags.Flag.DELETED;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r3 = r2.isSet(r3);	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        if (r3 == 0) goto L_0x0055;
    L_0x004e:
        r3 = r5.port;	 Catch:{ IOException -> 0x0058, all -> 0x007a }
        r4 = r0 + 1;
        r3.dele(r4);	 Catch:{ IOException -> 0x0058, all -> 0x007a }
    L_0x0055:
        r0 = r0 + 1;
        goto L_0x0019;
    L_0x0058:
        r1 = move-exception;
        r3 = new javax.mail.MessagingException;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        r4 = "Exception deleting messages during close";
        r3.m404init(r4, r1);	 Catch:{ IOException -> 0x0061, all -> 0x007a }
        throw r3;	 Catch:{ IOException -> 0x0061, all -> 0x007a }
    L_0x0061:
        r3 = move-exception;
        r3 = 0;
        r5.port = r3;	 Catch:{ all -> 0x0077 }
        r3 = r5.store;	 Catch:{ all -> 0x0077 }
        r3 = (com.sun.mail.pop3.POP3Store) r3;	 Catch:{ all -> 0x0077 }
        r3.closePort(r5);	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.message_cache = r3;	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.opened = r3;	 Catch:{ all -> 0x0077 }
        r3 = 3;
        r5.notifyConnectionListeners(r3);	 Catch:{ all -> 0x0077 }
        goto L_0x003a;
    L_0x0077:
        r3 = move-exception;
        monitor-exit(r5);
        throw r3;
    L_0x007a:
        r3 = move-exception;
        r4 = r3;
        r3 = 0;
        r5.port = r3;	 Catch:{ all -> 0x0077 }
        r3 = r5.store;	 Catch:{ all -> 0x0077 }
        r3 = (com.sun.mail.pop3.POP3Store) r3;	 Catch:{ all -> 0x0077 }
        r3.closePort(r5);	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.message_cache = r3;	 Catch:{ all -> 0x0077 }
        r3 = 0;
        r5.opened = r3;	 Catch:{ all -> 0x0077 }
        r3 = 3;
        r5.notifyConnectionListeners(r3);	 Catch:{ all -> 0x0077 }
        throw r4;	 Catch:{ all -> 0x0077 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.POP3Folder.close(boolean):void");
    }

    public boolean isOpen() {
        if (!this.opened) {
            return false;
        }
        if (this.store.isConnected()) {
            return true;
        }
        try {
            close(false);
            return false;
        } catch (MessagingException e) {
            return false;
        }
    }

    public Flags getPermanentFlags() {
        return new Flags();
    }

    public synchronized int getMessageCount() throws MessagingException {
        int i;
        if (this.opened) {
            checkReadable();
            i = this.total;
        } else {
            i = -1;
        }
        return i;
    }

    public synchronized Message getMessage(int msgno) throws MessagingException {
        POP3Message m;
        checkOpen();
        m = (POP3Message) this.message_cache.elementAt(msgno - 1);
        if (m == null) {
            m = createMessage(this, msgno);
            this.message_cache.setElementAt(m, msgno - 1);
        }
        return m;
    }

    /* access modifiers changed from: protected */
    public POP3Message createMessage(Folder f, int msgno) throws MessagingException {
        POP3Message m = null;
        Constructor cons = ((POP3Store) this.store).messageConstructor;
        if (cons != null) {
            try {
                m = (POP3Message) cons.newInstance(new Object[]{this, new Integer(msgno)});
            } catch (Exception e) {
            }
        }
        if (m == null) {
            return new POP3Message(this, msgno);
        }
        return m;
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
        throw new MethodNotSupportedException("Append not supported");
    }

    public Message[] expunge() throws MessagingException {
        throw new MethodNotSupportedException("Expunge not supported");
    }

    public synchronized void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
        int i;
        checkReadable();
        if (!this.doneUidl && fp.contains(FetchProfileItem.UID)) {
            String[] uids = new String[this.message_cache.size()];
            try {
                if (this.port.uidl(uids)) {
                    for (i = 0; i < uids.length; i++) {
                        if (uids[i] != null) {
                            ((POP3Message) getMessage(i + 1)).uid = uids[i];
                        }
                    }
                    this.doneUidl = true;
                }
            } catch (EOFException eex) {
                close(false);
                throw new FolderClosedException(this, eex.toString());
            } catch (IOException ex) {
                throw new MessagingException("error getting UIDL", ex);
            }
        }
        if (fp.contains(Item.ENVELOPE)) {
            for (i = 0; i < msgs.length; i++) {
                try {
                    POP3Message msg = msgs[i];
                    msg.getHeader(MMMailContentUtil.MM_MESSAGE_SUBJECT);
                    msg.getSize();
                } catch (MessageRemovedException e) {
                }
            }
        }
    }

    public synchronized String getUID(Message msg) throws MessagingException {
        POP3Message m;
        checkOpen();
        m = (POP3Message) msg;
        try {
            if (m.uid == "UNKNOWN") {
                m.uid = this.port.uidl(m.getMessageNumber());
            }
        } catch (EOFException eex) {
            close(false);
            throw new FolderClosedException(this, eex.toString());
        } catch (IOException ex) {
            throw new MessagingException("error getting UIDL", ex);
        }
        return m.uid;
    }

    public synchronized int getSize() throws MessagingException {
        checkOpen();
        return this.size;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x005b A:{SYNTHETIC, Splitter:B:37:0x005b} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0060 A:{SYNTHETIC, Splitter:B:40:0x0060} */
    public synchronized int[] getSizes() throws javax.mail.MessagingException {
        /*
        r10 = this;
        monitor-enter(r10);
        r10.checkOpen();	 Catch:{ all -> 0x0064 }
        r8 = r10.total;	 Catch:{ all -> 0x0064 }
        r6 = new int[r8];	 Catch:{ all -> 0x0064 }
        r0 = 0;
        r2 = 0;
        r8 = r10.port;	 Catch:{ IOException -> 0x004a, all -> 0x0058 }
        r0 = r8.list();	 Catch:{ IOException -> 0x004a, all -> 0x0058 }
        r3 = new com.sun.mail.util.LineInputStream;	 Catch:{ IOException -> 0x004a, all -> 0x0058 }
        r3.m346init(r0);	 Catch:{ IOException -> 0x004a, all -> 0x0058 }
    L_0x0015:
        r1 = r3.readLine();	 Catch:{ IOException -> 0x0075, all -> 0x0072 }
        if (r1 != 0) goto L_0x0028;
    L_0x001b:
        if (r3 == 0) goto L_0x0020;
    L_0x001d:
        r3.close();	 Catch:{ IOException -> 0x0070 }
    L_0x0020:
        if (r0 == 0) goto L_0x0078;
    L_0x0022:
        r0.close();	 Catch:{ IOException -> 0x0067 }
        r2 = r3;
    L_0x0026:
        monitor-exit(r10);
        return r6;
    L_0x0028:
        r7 = new java.util.StringTokenizer;	 Catch:{ Exception -> 0x0048 }
        r7.<init>(r1);	 Catch:{ Exception -> 0x0048 }
        r8 = r7.nextToken();	 Catch:{ Exception -> 0x0048 }
        r4 = java.lang.Integer.parseInt(r8);	 Catch:{ Exception -> 0x0048 }
        r8 = r7.nextToken();	 Catch:{ Exception -> 0x0048 }
        r5 = java.lang.Integer.parseInt(r8);	 Catch:{ Exception -> 0x0048 }
        if (r4 <= 0) goto L_0x0015;
    L_0x003f:
        r8 = r10.total;	 Catch:{ Exception -> 0x0048 }
        if (r4 > r8) goto L_0x0015;
    L_0x0043:
        r8 = r4 + -1;
        r6[r8] = r5;	 Catch:{ Exception -> 0x0048 }
        goto L_0x0015;
    L_0x0048:
        r8 = move-exception;
        goto L_0x0015;
    L_0x004a:
        r8 = move-exception;
    L_0x004b:
        if (r2 == 0) goto L_0x0050;
    L_0x004d:
        r2.close();	 Catch:{ IOException -> 0x006a }
    L_0x0050:
        if (r0 == 0) goto L_0x0026;
    L_0x0052:
        r0.close();	 Catch:{ IOException -> 0x0056 }
        goto L_0x0026;
    L_0x0056:
        r8 = move-exception;
        goto L_0x0026;
    L_0x0058:
        r8 = move-exception;
    L_0x0059:
        if (r2 == 0) goto L_0x005e;
    L_0x005b:
        r2.close();	 Catch:{ IOException -> 0x006c }
    L_0x005e:
        if (r0 == 0) goto L_0x0063;
    L_0x0060:
        r0.close();	 Catch:{ IOException -> 0x006e }
    L_0x0063:
        throw r8;	 Catch:{ all -> 0x0064 }
    L_0x0064:
        r8 = move-exception;
        monitor-exit(r10);
        throw r8;
    L_0x0067:
        r8 = move-exception;
        r2 = r3;
        goto L_0x0026;
    L_0x006a:
        r8 = move-exception;
        goto L_0x0050;
    L_0x006c:
        r9 = move-exception;
        goto L_0x005e;
    L_0x006e:
        r9 = move-exception;
        goto L_0x0063;
    L_0x0070:
        r8 = move-exception;
        goto L_0x0020;
    L_0x0072:
        r8 = move-exception;
        r2 = r3;
        goto L_0x0059;
    L_0x0075:
        r8 = move-exception;
        r2 = r3;
        goto L_0x004b;
    L_0x0078:
        r2 = r3;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.pop3.POP3Folder.getSizes():int[]");
    }

    public synchronized InputStream listCommand() throws MessagingException, IOException {
        checkOpen();
        return this.port.list();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        close(false);
    }

    /* access modifiers changed from: 0000 */
    public void checkOpen() throws IllegalStateException {
        if (!this.opened) {
            throw new IllegalStateException("Folder is not Open");
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkClosed() throws IllegalStateException {
        if (this.opened) {
            throw new IllegalStateException("Folder is Open");
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkReadable() throws IllegalStateException {
        if (!this.opened || (this.mode != 1 && this.mode != 2)) {
            throw new IllegalStateException("Folder is not Readable");
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkWritable() throws IllegalStateException {
        if (!this.opened || this.mode != 2) {
            throw new IllegalStateException("Folder is not Writable");
        }
    }

    /* access modifiers changed from: 0000 */
    public Protocol getProtocol() throws MessagingException {
        checkOpen();
        return this.port;
    }

    /* access modifiers changed from: protected */
    public void notifyMessageChangedListeners(int type, Message m) {
        super.notifyMessageChangedListeners(type, m);
    }
}
