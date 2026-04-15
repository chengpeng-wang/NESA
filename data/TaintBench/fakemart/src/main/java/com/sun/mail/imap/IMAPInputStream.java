package com.sun.mail.imap;

import android.support.v4.view.MotionEventCompat;
import com.sun.mail.iap.ByteArray;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.MessagingException;

public class IMAPInputStream extends InputStream {
    private static final int slop = 64;
    private int blksize;
    private byte[] buf;
    private int bufcount;
    private int bufpos;
    private int max;
    private IMAPMessage msg;
    private boolean peek;
    private int pos = 0;
    private ByteArray readbuf;
    private String section;

    public IMAPInputStream(IMAPMessage msg, String section, int max, boolean peek) {
        this.msg = msg;
        this.section = section;
        this.max = max;
        this.peek = peek;
        this.blksize = msg.getFetchBlockSize();
    }

    private void forceCheckExpunged() throws MessageRemovedIOException, FolderClosedIOException {
        synchronized (this.msg.getMessageCacheLock()) {
            try {
                this.msg.getProtocol().noop();
            } catch (ConnectionException cex) {
                throw new FolderClosedIOException(this.msg.getFolder(), cex.getMessage());
            } catch (FolderClosedException fex) {
                throw new FolderClosedIOException(fex.getFolder(), fex.getMessage());
            } catch (ProtocolException e) {
            }
        }
        if (this.msg.isExpunged()) {
            throw new MessageRemovedIOException();
        }
    }

    private void fill() throws IOException {
        if (this.max == -1 || this.pos < this.max) {
            ByteArray ba;
            if (this.readbuf == null) {
                this.readbuf = new ByteArray(this.blksize + 64);
            }
            synchronized (this.msg.getMessageCacheLock()) {
                try {
                    IMAPProtocol p = this.msg.getProtocol();
                    if (this.msg.isExpunged()) {
                        throw new MessageRemovedIOException("No content for expunged message");
                    }
                    BODY b;
                    int seqnum = this.msg.getSequenceNumber();
                    int cnt = this.blksize;
                    if (this.max != -1 && this.pos + this.blksize > this.max) {
                        cnt = this.max - this.pos;
                    }
                    if (this.peek) {
                        b = p.peekBody(seqnum, this.section, this.pos, cnt, this.readbuf);
                    } else {
                        b = p.fetchBody(seqnum, this.section, this.pos, cnt, this.readbuf);
                    }
                    if (b != null) {
                        ba = b.getByteArray();
                        if (ba != null) {
                        }
                    }
                    forceCheckExpunged();
                    throw new IOException("No content");
                } catch (ProtocolException pex) {
                    forceCheckExpunged();
                    throw new IOException(pex.getMessage());
                } catch (FolderClosedException fex) {
                    throw new FolderClosedIOException(fex.getFolder(), fex.getMessage());
                }
            }
            if (this.pos == 0) {
                checkSeen();
            }
            this.buf = ba.getBytes();
            this.bufpos = ba.getStart();
            int n = ba.getCount();
            this.bufcount = this.bufpos + n;
            this.pos += n;
            return;
        }
        if (this.pos == 0) {
            checkSeen();
        }
        this.readbuf = null;
    }

    public synchronized int read() throws IOException {
        int i;
        if (this.bufpos >= this.bufcount) {
            fill();
            if (this.bufpos >= this.bufcount) {
                i = -1;
            }
        }
        byte[] bArr = this.buf;
        int i2 = this.bufpos;
        this.bufpos = i2 + 1;
        i = bArr[i2] & MotionEventCompat.ACTION_MASK;
        return i;
    }

    public synchronized int read(byte[] b, int off, int len) throws IOException {
        int cnt;
        int avail = this.bufcount - this.bufpos;
        if (avail <= 0) {
            fill();
            avail = this.bufcount - this.bufpos;
            if (avail <= 0) {
                cnt = -1;
            }
        }
        if (avail < len) {
            cnt = avail;
        } else {
            cnt = len;
        }
        System.arraycopy(this.buf, this.bufpos, b, off, cnt);
        this.bufpos += cnt;
        return cnt;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public synchronized int available() throws IOException {
        return this.bufcount - this.bufpos;
    }

    private void checkSeen() {
        if (!this.peek) {
            try {
                Folder f = this.msg.getFolder();
                if (f != null && f.getMode() != 1 && !this.msg.isSet(Flag.SEEN)) {
                    this.msg.setFlag(Flag.SEEN, true);
                }
            } catch (MessagingException e) {
            }
        }
    }
}
