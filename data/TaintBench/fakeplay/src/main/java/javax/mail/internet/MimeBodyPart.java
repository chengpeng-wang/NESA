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
