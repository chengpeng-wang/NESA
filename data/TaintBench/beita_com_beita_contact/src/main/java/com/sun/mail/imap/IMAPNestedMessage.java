package com.sun.mail.imap;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.ENVELOPE;
import com.sun.mail.imap.protocol.IMAPProtocol;
import javax.mail.Flags;
import javax.mail.FolderClosedException;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.MethodNotSupportedException;

public class IMAPNestedMessage extends IMAPMessage {
    private IMAPMessage msg;

    IMAPNestedMessage(IMAPMessage m, BODYSTRUCTURE b, ENVELOPE e, String sid) {
        super(m._getSession());
        this.msg = m;
        this.bs = b;
        this.envelope = e;
        this.sectionId = sid;
    }

    /* access modifiers changed from: protected */
    public IMAPProtocol getProtocol() throws ProtocolException, FolderClosedException {
        return this.msg.getProtocol();
    }

    /* access modifiers changed from: protected */
    public boolean isREV1() throws FolderClosedException {
        return this.msg.isREV1();
    }

    /* access modifiers changed from: protected */
    public Object getMessageCacheLock() {
        return this.msg.getMessageCacheLock();
    }

    /* access modifiers changed from: protected */
    public int getSequenceNumber() {
        return this.msg.getSequenceNumber();
    }

    /* access modifiers changed from: protected */
    public void checkExpunged() throws MessageRemovedException {
        this.msg.checkExpunged();
    }

    public boolean isExpunged() {
        return this.msg.isExpunged();
    }

    /* access modifiers changed from: protected */
    public int getFetchBlockSize() {
        return this.msg.getFetchBlockSize();
    }

    public int getSize() throws MessagingException {
        return this.bs.size;
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        throw new MethodNotSupportedException("Cannot set flags on this nested message");
    }
}
