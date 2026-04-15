package javax.mail.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import javax.activation.DataSource;
import javax.mail.MessageAware;
import javax.mail.MessageContext;
import javax.mail.MessagingException;

public class MimePartDataSource implements DataSource, MessageAware {
    private static boolean ignoreMultipartEncoding;
    private MessageContext context;
    protected MimePart part;

    static {
        ignoreMultipartEncoding = true;
        try {
            String s = System.getProperty("mail.mime.ignoremultipartencoding");
            boolean z = s == null || !s.equalsIgnoreCase("false");
            ignoreMultipartEncoding = z;
        } catch (SecurityException e) {
        }
    }

    public MimePartDataSource(MimePart part) {
        this.part = part;
    }

    public InputStream getInputStream() throws IOException {
        try {
            InputStream is;
            if (this.part instanceof MimeBodyPart) {
                is = ((MimeBodyPart) this.part).getContentStream();
            } else if (this.part instanceof MimeMessage) {
                is = ((MimeMessage) this.part).getContentStream();
            } else {
                throw new MessagingException("Unknown part");
            }
            String encoding = restrictEncoding(this.part.getEncoding(), this.part);
            if (encoding != null) {
                return MimeUtility.decode(is, encoding);
            }
            return is;
        } catch (MessagingException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static String restrictEncoding(String encoding, MimePart part) throws MessagingException {
        if (!ignoreMultipartEncoding || encoding == null) {
            return encoding;
        }
        if (encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit") || encoding.equalsIgnoreCase("binary")) {
            return encoding;
        }
        String type = part.getContentType();
        if (type == null) {
            return encoding;
        }
        try {
            ContentType cType = new ContentType(type);
            if (cType.match("multipart/*") || cType.match("message/*")) {
                return null;
            }
        } catch (ParseException e) {
        }
        return encoding;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException();
    }

    public String getContentType() {
        try {
            return this.part.getContentType();
        } catch (MessagingException e) {
            MessagingException mex = e;
            return "application/octet-stream";
        }
    }

    public String getName() {
        try {
            if (this.part instanceof MimeBodyPart) {
                return ((MimeBodyPart) this.part).getFileName();
            }
        } catch (MessagingException e) {
        }
        return "";
    }

    public synchronized MessageContext getMessageContext() {
        if (this.context == null) {
            this.context = new MessageContext(this.part);
        }
        return this.context;
    }
}
