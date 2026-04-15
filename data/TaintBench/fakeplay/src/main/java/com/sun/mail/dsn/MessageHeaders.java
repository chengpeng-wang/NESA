package com.sun.mail.dsn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

public class MessageHeaders extends MimeMessage {
    public MessageHeaders() throws MessagingException {
        super(null);
        this.content = new byte[0];
    }

    public MessageHeaders(InputStream is) throws MessagingException {
        super(null, is);
        this.content = new byte[0];
    }

    public MessageHeaders(InternetHeaders headers) throws MessagingException {
        super(null);
        this.headers = headers;
        this.content = new byte[0];
    }

    public int getSize() {
        return 0;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.content);
    }

    /* access modifiers changed from: protected */
    public InputStream getContentStream() {
        return new ByteArrayInputStream(this.content);
    }

    public void setDataHandler(DataHandler dh) throws MessagingException {
        throw new MessagingException("Can't set content for MessageHeaders");
    }
}
