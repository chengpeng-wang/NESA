package com.sun.mail.imap;

import com.sun.mail.iap.Literal;
import com.sun.mail.util.CRLFOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.mail.Message;
import javax.mail.MessagingException;

/* compiled from: IMAPFolder */
class MessageLiteral implements Literal {
    private byte[] buf;
    private Message msg;
    private int msgSize = -1;

    public MessageLiteral(Message msg, int maxsize) throws MessagingException, IOException {
        this.msg = msg;
        LengthCounter lc = new LengthCounter(maxsize);
        OutputStream os = new CRLFOutputStream(lc);
        msg.writeTo(os);
        os.flush();
        this.msgSize = lc.getSize();
        this.buf = lc.getBytes();
    }

    public int size() {
        return this.msgSize;
    }

    public void writeTo(OutputStream os) throws IOException {
        MessagingException mex;
        try {
            if (this.buf != null) {
                os.write(this.buf, 0, this.msgSize);
                return;
            }
            OutputStream os2 = new CRLFOutputStream(os);
            try {
                this.msg.writeTo(os2);
                os = os2;
            } catch (MessagingException e) {
                mex = e;
                os = os2;
                throw new IOException("MessagingException while appending message: " + mex);
            }
        } catch (MessagingException e2) {
            mex = e2;
            throw new IOException("MessagingException while appending message: " + mex);
        }
    }
}
