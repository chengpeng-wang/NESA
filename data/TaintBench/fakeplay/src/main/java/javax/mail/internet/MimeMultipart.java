package javax.mail.internet;

import android.support.v4.view.MotionEventCompat;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.LineOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessageAware;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.MultipartDataSource;

public class MimeMultipart extends Multipart {
    private static boolean bmparse;
    private static boolean ignoreMissingBoundaryParameter;
    private static boolean ignoreMissingEndBoundary;
    private boolean complete;
    protected DataSource ds;
    protected boolean parsed;
    private String preamble;

    static {
        boolean z = false;
        ignoreMissingEndBoundary = true;
        ignoreMissingBoundaryParameter = true;
        bmparse = true;
        try {
            String s = System.getProperty("mail.mime.multipart.ignoremissingendboundary");
            boolean z2 = s == null || !s.equalsIgnoreCase("false");
            ignoreMissingEndBoundary = z2;
            s = System.getProperty("mail.mime.multipart.ignoremissingboundaryparameter");
            if (s == null || !s.equalsIgnoreCase("false")) {
                z2 = true;
            } else {
                z2 = false;
            }
            ignoreMissingBoundaryParameter = z2;
            s = System.getProperty("mail.mime.multipart.bmparse");
            if (s == null || !s.equalsIgnoreCase("false")) {
                z = true;
            }
            bmparse = z;
        } catch (SecurityException e) {
        }
    }

    public MimeMultipart() {
        this("mixed");
    }

    public MimeMultipart(String subtype) {
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        String boundary = UniqueValue.getUniqueBoundaryValue();
        ContentType cType = new ContentType("multipart", subtype, null);
        cType.setParameter("boundary", boundary);
        this.contentType = cType.toString();
    }

    public MimeMultipart(DataSource ds) throws MessagingException {
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        if (ds instanceof MessageAware) {
            setParent(((MessageAware) ds).getMessageContext().getPart());
        }
        if (ds instanceof MultipartDataSource) {
            setMultipartDataSource((MultipartDataSource) ds);
            return;
        }
        this.parsed = false;
        this.ds = ds;
        this.contentType = ds.getContentType();
    }

    public synchronized void setSubType(String subtype) throws MessagingException {
        ContentType cType = new ContentType(this.contentType);
        cType.setSubType(subtype);
        this.contentType = cType.toString();
    }

    public synchronized int getCount() throws MessagingException {
        parse();
        return super.getCount();
    }

    public synchronized BodyPart getBodyPart(int index) throws MessagingException {
        parse();
        return super.getBodyPart(index);
    }

    public synchronized BodyPart getBodyPart(String CID) throws MessagingException {
        BodyPart part;
        parse();
        int count = getCount();
        for (int i = 0; i < count; i++) {
            MimeBodyPart part2 = (MimeBodyPart) getBodyPart(i);
            String s = part2.getContentID();
            if (s != null && s.equals(CID)) {
                break;
            }
        }
        part2 = null;
        return part2;
    }

    public boolean removeBodyPart(BodyPart part) throws MessagingException {
        parse();
        return super.removeBodyPart(part);
    }

    public void removeBodyPart(int index) throws MessagingException {
        parse();
        super.removeBodyPart(index);
    }

    public synchronized void addBodyPart(BodyPart part) throws MessagingException {
        parse();
        super.addBodyPart(part);
    }

    public synchronized void addBodyPart(BodyPart part, int index) throws MessagingException {
        parse();
        super.addBodyPart(part, index);
    }

    public synchronized boolean isComplete() throws MessagingException {
        parse();
        return this.complete;
    }

    public synchronized String getPreamble() throws MessagingException {
        parse();
        return this.preamble;
    }

    public synchronized void setPreamble(String preamble) throws MessagingException {
        this.preamble = preamble;
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        for (int i = 0; i < this.parts.size(); i++) {
            ((MimeBodyPart) this.parts.elementAt(i)).updateHeaders();
        }
    }

    public synchronized void writeTo(OutputStream os) throws IOException, MessagingException {
        parse();
        String boundary = "--" + new ContentType(this.contentType).getParameter("boundary");
        LineOutputStream los = new LineOutputStream(os);
        if (this.preamble != null) {
            byte[] pb = ASCIIUtility.getBytes(this.preamble);
            los.write(pb);
            if (!(pb.length <= 0 || pb[pb.length - 1] == (byte) 13 || pb[pb.length - 1] == (byte) 10)) {
                los.writeln();
            }
        }
        for (int i = 0; i < this.parts.size(); i++) {
            los.writeln(boundary);
            ((MimeBodyPart) this.parts.elementAt(i)).writeTo(os);
            los.writeln();
        }
        los.writeln(new StringBuilder(String.valueOf(boundary)).append("--").toString());
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void parse() throws MessagingException {
        if (!this.parsed) {
            if (bmparse) {
                parsebm();
            } else {
                SharedInputStream sin = null;
                long start = 0;
                long end = 0;
                try {
                    InputStream in = this.ds.getInputStream();
                    if (!((in instanceof ByteArrayInputStream) || (in instanceof BufferedInputStream) || (in instanceof SharedInputStream))) {
                        in = new BufferedInputStream(in);
                    }
                    if (in instanceof SharedInputStream) {
                        sin = (SharedInputStream) in;
                    }
                    String boundary = null;
                    String bp = new ContentType(this.contentType).getParameter("boundary");
                    if (bp != null) {
                        boundary = "--" + bp;
                    } else if (!ignoreMissingBoundaryParameter) {
                        throw new MessagingException("Missing boundary parameter");
                    }
                    try {
                        String line;
                        int i;
                        LineInputStream lineInputStream = new LineInputStream(in);
                        StringBuffer preamblesb = null;
                        String lineSeparator = null;
                        while (true) {
                            line = lineInputStream.readLine();
                            if (line == null) {
                                break;
                            }
                            i = line.length() - 1;
                            while (i >= 0) {
                                char c = line.charAt(i);
                                if (c != ' ' && c != 9) {
                                    break;
                                }
                                i--;
                            }
                            line = line.substring(0, i + 1);
                            if (boundary == null) {
                                if (line.startsWith("--")) {
                                    boundary = line;
                                    break;
                                }
                            } else if (line.equals(boundary)) {
                                break;
                            }
                            if (line.length() > 0) {
                                if (lineSeparator == null) {
                                    try {
                                        lineSeparator = System.getProperty("line.separator", "\n");
                                    } catch (SecurityException e) {
                                        lineSeparator = "\n";
                                    }
                                }
                                if (preamblesb == null) {
                                    StringBuffer stringBuffer = new StringBuffer(line.length() + 2);
                                }
                                preamblesb.append(line).append(lineSeparator);
                            }
                        }
                        if (line == null) {
                            throw new MessagingException("Missing start boundary");
                        }
                        if (preamblesb != null) {
                            this.preamble = preamblesb.toString();
                        }
                        byte[] bndbytes = ASCIIUtility.getBytes(boundary);
                        int bl = bndbytes.length;
                        boolean done = false;
                        while (!done) {
                            InternetHeaders headers = null;
                            if (sin != null) {
                                start = sin.getPosition();
                                do {
                                    line = lineInputStream.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                } while (line.length() > 0);
                                if (line == null) {
                                    if (ignoreMissingEndBoundary) {
                                        this.complete = false;
                                    } else {
                                        throw new MessagingException("missing multipart end boundary");
                                    }
                                }
                            }
                            headers = createInternetHeaders(in);
                            if (in.markSupported()) {
                                BodyPart part;
                                ByteArrayOutputStream buf = null;
                                if (sin == null) {
                                    buf = new ByteArrayOutputStream();
                                } else {
                                    end = sin.getPosition();
                                }
                                boolean bol = true;
                                int eol1 = -1;
                                int eol2 = -1;
                                while (true) {
                                    if (bol) {
                                        in.mark((bl + 4) + IMAPStore.RESPONSE);
                                        i = 0;
                                        while (i < bl && in.read() == (bndbytes[i] & MotionEventCompat.ACTION_MASK)) {
                                            i++;
                                        }
                                        if (i == bl) {
                                            int b2 = in.read();
                                            if (b2 == 45 && in.read() == 45) {
                                                this.complete = true;
                                                done = true;
                                                break;
                                            }
                                            while (true) {
                                                if (b2 != 32 && b2 != 9) {
                                                    break;
                                                }
                                                b2 = in.read();
                                            }
                                            if (b2 == 10) {
                                                break;
                                            } else if (b2 == 13) {
                                                in.mark(1);
                                                if (in.read() != 10) {
                                                    in.reset();
                                                }
                                            }
                                        }
                                        in.reset();
                                        if (!(buf == null || eol1 == -1)) {
                                            buf.write(eol1);
                                            if (eol2 != -1) {
                                                buf.write(eol2);
                                            }
                                            eol2 = -1;
                                            eol1 = -1;
                                        }
                                    }
                                    int b = in.read();
                                    if (b < 0) {
                                        if (ignoreMissingEndBoundary) {
                                            this.complete = false;
                                            done = true;
                                        } else {
                                            throw new MessagingException("missing multipart end boundary");
                                        }
                                    } else if (b == 13 || b == 10) {
                                        bol = true;
                                        if (sin != null) {
                                            end = sin.getPosition() - 1;
                                        }
                                        eol1 = b;
                                        if (b == 13) {
                                            in.mark(1);
                                            b = in.read();
                                            if (b == 10) {
                                                eol2 = b;
                                            } else {
                                                in.reset();
                                            }
                                        }
                                    } else {
                                        bol = false;
                                        if (buf != null) {
                                            buf.write(b);
                                        }
                                    }
                                }
                                if (sin != null) {
                                    part = createMimeBodyPart(sin.newStream(start, end));
                                } else {
                                    part = createMimeBodyPart(headers, buf.toByteArray());
                                }
                                super.addBodyPart(part);
                            } else {
                                throw new MessagingException("Stream doesn't support mark");
                            }
                        }
                        try {
                            in.close();
                        } catch (IOException e2) {
                        }
                        this.parsed = true;
                    } catch (IOException ioex) {
                        throw new MessagingException("IO Error", ioex);
                    } catch (Throwable th) {
                        try {
                            in.close();
                        } catch (IOException e3) {
                        }
                    }
                } catch (Exception ex) {
                    throw new MessagingException("No inputstream from datasource", ex);
                }
            }
        }
    }

    private synchronized void parsebm() throws MessagingException {
        if (!this.parsed) {
            SharedInputStream sin = null;
            long start = 0;
            long end = 0;
            try {
                InputStream in = this.ds.getInputStream();
                if (!((in instanceof ByteArrayInputStream) || (in instanceof BufferedInputStream) || (in instanceof SharedInputStream))) {
                    in = new BufferedInputStream(in);
                }
                if (in instanceof SharedInputStream) {
                    sin = (SharedInputStream) in;
                }
                String boundary = null;
                String bp = new ContentType(this.contentType).getParameter("boundary");
                if (bp != null) {
                    boundary = "--" + bp;
                } else if (!ignoreMissingBoundaryParameter) {
                    throw new MessagingException("Missing boundary parameter");
                }
                try {
                    String line;
                    int i;
                    LineInputStream lineInputStream = new LineInputStream(in);
                    StringBuffer preamblesb = null;
                    String lineSeparator = null;
                    while (true) {
                        line = lineInputStream.readLine();
                        if (line == null) {
                            break;
                        }
                        i = line.length() - 1;
                        while (i >= 0) {
                            char c = line.charAt(i);
                            if (c != ' ' && c != 9) {
                                break;
                            }
                            i--;
                        }
                        line = line.substring(0, i + 1);
                        if (boundary == null) {
                            if (line.startsWith("--")) {
                                boundary = line;
                                break;
                            }
                        } else if (line.equals(boundary)) {
                            break;
                        }
                        if (line.length() > 0) {
                            if (lineSeparator == null) {
                                try {
                                    lineSeparator = System.getProperty("line.separator", "\n");
                                } catch (SecurityException e) {
                                    lineSeparator = "\n";
                                }
                            }
                            if (preamblesb == null) {
                                StringBuffer stringBuffer = new StringBuffer(line.length() + 2);
                            }
                            preamblesb.append(line).append(lineSeparator);
                        }
                    }
                    if (line == null) {
                        throw new MessagingException("Missing start boundary");
                    }
                    if (preamblesb != null) {
                        this.preamble = preamblesb.toString();
                    }
                    byte[] bndbytes = ASCIIUtility.getBytes(boundary);
                    int bl = bndbytes.length;
                    int[] bcs = new int[256];
                    for (i = 0; i < bl; i++) {
                        bcs[bndbytes[i]] = i + 1;
                    }
                    int[] gss = new int[bl];
                    for (i = bl; i > 0; i--) {
                        int j = bl - 1;
                        while (j >= i) {
                            if (bndbytes[j] != bndbytes[j - i]) {
                                break;
                            }
                            gss[j - 1] = i;
                            j--;
                        }
                        while (j > 0) {
                            j--;
                            gss[j] = i;
                        }
                    }
                    gss[bl - 1] = 1;
                    boolean done = false;
                    while (!done) {
                        InternetHeaders headers = null;
                        if (sin != null) {
                            start = sin.getPosition();
                            do {
                                line = lineInputStream.readLine();
                                if (line == null) {
                                    break;
                                }
                            } while (line.length() > 0);
                            if (line == null) {
                                if (ignoreMissingEndBoundary) {
                                    this.complete = false;
                                } else {
                                    throw new MessagingException("missing multipart end boundary");
                                }
                            }
                        }
                        headers = createInternetHeaders(in);
                        if (in.markSupported()) {
                            int eolLen;
                            int inSize;
                            BodyPart part;
                            ByteArrayOutputStream buf = null;
                            if (sin == null) {
                                buf = new ByteArrayOutputStream();
                            } else {
                                end = sin.getPosition();
                            }
                            byte[] inbuf = new byte[bl];
                            byte[] previnbuf = new byte[bl];
                            int prevSize = 0;
                            boolean first = true;
                            while (true) {
                                in.mark((bl + 4) + IMAPStore.RESPONSE);
                                eolLen = 0;
                                inSize = readFully(in, inbuf, 0, bl);
                                if (inSize >= bl) {
                                    i = bl - 1;
                                    while (i >= 0 && inbuf[i] == bndbytes[i]) {
                                        i--;
                                    }
                                    if (i < 0) {
                                        eolLen = 0;
                                        if (!first) {
                                            int b = previnbuf[prevSize - 1];
                                            if (b == 13 || b == 10) {
                                                eolLen = 1;
                                                if (b == 10 && prevSize >= 2 && previnbuf[prevSize - 2] == 13) {
                                                    eolLen = 2;
                                                }
                                            }
                                        }
                                        if (first || eolLen > 0) {
                                            if (sin != null) {
                                                end = (sin.getPosition() - ((long) bl)) - ((long) eolLen);
                                            }
                                            int b2 = in.read();
                                            if (b2 == 45 && in.read() == 45) {
                                                this.complete = true;
                                                done = true;
                                                break;
                                            }
                                            while (true) {
                                                if (b2 != 32 && b2 != 9) {
                                                    break;
                                                }
                                                b2 = in.read();
                                            }
                                            if (b2 == 10) {
                                                break;
                                            } else if (b2 == 13) {
                                                in.mark(1);
                                                if (in.read() != 10) {
                                                    in.reset();
                                                }
                                            }
                                        }
                                        i = 0;
                                    }
                                    int skip = Math.max((i + 1) - bcs[inbuf[i] & 127], gss[i]);
                                    if (skip < 2) {
                                        if (sin == null && prevSize > 1) {
                                            buf.write(previnbuf, 0, prevSize - 1);
                                        }
                                        in.reset();
                                        skipFully(in, 1);
                                        if (prevSize >= 1) {
                                            previnbuf[0] = previnbuf[prevSize - 1];
                                            previnbuf[1] = inbuf[0];
                                            prevSize = 2;
                                        } else {
                                            previnbuf[0] = inbuf[0];
                                            prevSize = 1;
                                        }
                                    } else {
                                        if (prevSize > 0 && sin == null) {
                                            buf.write(previnbuf, 0, prevSize);
                                        }
                                        prevSize = skip;
                                        in.reset();
                                        skipFully(in, (long) prevSize);
                                        byte[] tmp = inbuf;
                                        inbuf = previnbuf;
                                        previnbuf = tmp;
                                    }
                                    first = false;
                                } else if (ignoreMissingEndBoundary) {
                                    if (sin != null) {
                                        end = sin.getPosition();
                                    }
                                    this.complete = false;
                                    done = true;
                                } else {
                                    throw new MessagingException("missing multipart end boundary");
                                }
                            }
                            if (sin != null) {
                                part = createMimeBodyPart(sin.newStream(start, end));
                            } else {
                                if (prevSize - eolLen > 0) {
                                    buf.write(previnbuf, 0, prevSize - eolLen);
                                }
                                if (!this.complete && inSize > 0) {
                                    buf.write(inbuf, 0, inSize);
                                }
                                part = createMimeBodyPart(headers, buf.toByteArray());
                            }
                            super.addBodyPart(part);
                        } else {
                            throw new MessagingException("Stream doesn't support mark");
                        }
                    }
                    try {
                        in.close();
                    } catch (IOException e2) {
                    }
                    this.parsed = true;
                } catch (IOException ioex) {
                    throw new MessagingException("IO Error", ioex);
                } catch (Throwable th) {
                    try {
                        in.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Exception ex) {
                throw new MessagingException("No inputstream from datasource", ex);
            }
        }
        return;
    }

    private static int readFully(InputStream in, byte[] buf, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int total = 0;
        while (len > 0) {
            int bsize = in.read(buf, off, len);
            if (bsize <= 0) {
                break;
            }
            off += bsize;
            total += bsize;
            len -= bsize;
        }
        if (total <= 0) {
            return -1;
        }
        return total;
    }

    private void skipFully(InputStream in, long offset) throws IOException {
        while (offset > 0) {
            long cur = in.skip(offset);
            if (cur <= 0) {
                throw new EOFException("can't skip");
            }
            offset -= cur;
        }
    }

    /* access modifiers changed from: protected */
    public InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    /* access modifiers changed from: protected */
    public MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] content) throws MessagingException {
        return new MimeBodyPart(headers, content);
    }

    /* access modifiers changed from: protected */
    public MimeBodyPart createMimeBodyPart(InputStream is) throws MessagingException {
        return new MimeBodyPart(is);
    }
}
