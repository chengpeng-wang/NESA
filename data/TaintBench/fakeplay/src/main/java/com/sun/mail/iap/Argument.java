package com.sun.mail.iap;

import android.support.v4.view.MotionEventCompat;
import com.sun.mail.util.ASCIIUtility;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class Argument {
    protected Vector items = new Vector(1);

    public void append(Argument arg) {
        this.items.ensureCapacity(this.items.size() + arg.items.size());
        for (int i = 0; i < arg.items.size(); i++) {
            this.items.addElement(arg.items.elementAt(i));
        }
    }

    public void writeString(String s) {
        this.items.addElement(new AString(ASCIIUtility.getBytes(s)));
    }

    public void writeString(String s, String charset) throws UnsupportedEncodingException {
        if (charset == null) {
            writeString(s);
        } else {
            this.items.addElement(new AString(s.getBytes(charset)));
        }
    }

    public void writeBytes(byte[] b) {
        this.items.addElement(b);
    }

    public void writeBytes(ByteArrayOutputStream b) {
        this.items.addElement(b);
    }

    public void writeBytes(Literal b) {
        this.items.addElement(b);
    }

    public void writeAtom(String s) {
        this.items.addElement(new Atom(s));
    }

    public void writeNumber(int i) {
        this.items.addElement(new Integer(i));
    }

    public void writeNumber(long i) {
        this.items.addElement(new Long(i));
    }

    public void writeArgument(Argument c) {
        this.items.addElement(c);
    }

    public void write(Protocol protocol) throws IOException, ProtocolException {
        int size = this.items != null ? this.items.size() : 0;
        DataOutputStream os = (DataOutputStream) protocol.getOutputStream();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                os.write(32);
            }
            Object o = this.items.elementAt(i);
            if (o instanceof Atom) {
                os.writeBytes(((Atom) o).string);
            } else if (o instanceof Number) {
                os.writeBytes(((Number) o).toString());
            } else if (o instanceof AString) {
                astring(((AString) o).bytes, protocol);
            } else if (o instanceof byte[]) {
                literal((byte[]) o, protocol);
            } else if (o instanceof ByteArrayOutputStream) {
                literal((ByteArrayOutputStream) o, protocol);
            } else if (o instanceof Literal) {
                literal((Literal) o, protocol);
            } else if (o instanceof Argument) {
                os.write(40);
                ((Argument) o).write(protocol);
                os.write(41);
            }
        }
    }

    private void astring(byte[] bytes, Protocol protocol) throws IOException, ProtocolException {
        DataOutputStream os = (DataOutputStream) protocol.getOutputStream();
        if (len > 1024) {
            literal(bytes, protocol);
            return;
        }
        boolean quote = len == 0;
        boolean escape = false;
        for (byte b : bytes) {
            if (b == (byte) 0 || b == (byte) 13 || b == (byte) 10 || (b & MotionEventCompat.ACTION_MASK) > 127) {
                literal(bytes, protocol);
                return;
            }
            if (b == (byte) 42 || b == (byte) 37 || b == (byte) 40 || b == (byte) 41 || b == (byte) 123 || b == (byte) 34 || b == (byte) 92 || (b & MotionEventCompat.ACTION_MASK) <= 32) {
                quote = true;
                if (b == (byte) 34 || b == (byte) 92) {
                    escape = true;
                }
            }
        }
        if (quote) {
            os.write(34);
        }
        if (escape) {
            for (byte b2 : bytes) {
                if (b2 == (byte) 34 || b2 == (byte) 92) {
                    os.write(92);
                }
                os.write(b2);
            }
        } else {
            os.write(bytes);
        }
        if (quote) {
            os.write(34);
        }
    }

    private void literal(byte[] b, Protocol protocol) throws IOException, ProtocolException {
        startLiteral(protocol, b.length).write(b);
    }

    private void literal(ByteArrayOutputStream b, Protocol protocol) throws IOException, ProtocolException {
        b.writeTo(startLiteral(protocol, b.size()));
    }

    private void literal(Literal b, Protocol protocol) throws IOException, ProtocolException {
        b.writeTo(startLiteral(protocol, b.size()));
    }

    private OutputStream startLiteral(Protocol protocol, int size) throws IOException, ProtocolException {
        DataOutputStream os = (DataOutputStream) protocol.getOutputStream();
        boolean nonSync = protocol.supportsNonSyncLiterals();
        os.write(123);
        os.writeBytes(Integer.toString(size));
        if (nonSync) {
            os.writeBytes("+}\r\n");
        } else {
            os.writeBytes("}\r\n");
        }
        os.flush();
        if (!nonSync) {
            Response r;
            do {
                r = protocol.readResponse();
                if (r.isContinuation()) {
                }
            } while (!r.isTagged());
            throw new LiteralException(r);
        }
        return os;
    }
}
