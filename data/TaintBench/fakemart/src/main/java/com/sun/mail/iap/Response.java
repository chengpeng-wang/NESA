package com.sun.mail.iap;

import com.googleprojects.mm.MMMailContentUtil;
import com.sun.mail.util.ASCIIUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

public class Response {
    public static final int BAD = 12;
    public static final int BYE = 16;
    public static final int CONTINUATION = 1;
    public static final int NO = 8;
    public static final int OK = 4;
    public static final int SYNTHETIC = 32;
    public static final int TAGGED = 2;
    public static final int TAG_MASK = 3;
    public static final int TYPE_MASK = 28;
    public static final int UNTAGGED = 3;
    private static final int increment = 100;
    protected byte[] buffer = null;
    protected int index;
    protected int pindex;
    protected int size;
    protected String tag = null;
    protected int type = 0;

    public Response(String s) {
        this.buffer = ASCIIUtility.getBytes(s);
        this.size = this.buffer.length;
        parse();
    }

    public Response(Protocol p) throws IOException, ProtocolException {
        ByteArray response = p.getInputStream().readResponse(p.getResponseBuffer());
        this.buffer = response.getBytes();
        this.size = response.getCount() - 2;
        parse();
    }

    public Response(Response r) {
        this.index = r.index;
        this.size = r.size;
        this.buffer = r.buffer;
        this.type = r.type;
        this.tag = r.tag;
    }

    public static Response byeResponse(Exception ex) {
        Response r = new Response(("* BYE JavaMail Exception: " + ex.toString()).replace(13, ' ').replace(10, ' '));
        r.type |= 32;
        return r;
    }

    private void parse() {
        this.index = 0;
        if (this.buffer[this.index] == (byte) 43) {
            this.type |= 1;
            this.index++;
            return;
        }
        if (this.buffer[this.index] == (byte) 42) {
            this.type |= 3;
            this.index++;
        } else {
            this.type |= 2;
            this.tag = readAtom();
        }
        int mark = this.index;
        String s = readAtom();
        if (s == null) {
            s = MMMailContentUtil.MM_MESSAGE_SUBJECT;
        }
        if (s.equalsIgnoreCase("OK")) {
            this.type |= 4;
        } else if (s.equalsIgnoreCase("NO")) {
            this.type |= 8;
        } else if (s.equalsIgnoreCase("BAD")) {
            this.type |= 12;
        } else if (s.equalsIgnoreCase("BYE")) {
            this.type |= 16;
        } else {
            this.index = mark;
        }
        this.pindex = this.index;
    }

    public void skipSpaces() {
        while (this.index < this.size && this.buffer[this.index] == (byte) 32) {
            this.index++;
        }
    }

    public void skipToken() {
        while (this.index < this.size && this.buffer[this.index] != (byte) 32) {
            this.index++;
        }
    }

    public void skip(int count) {
        this.index += count;
    }

    public byte peekByte() {
        if (this.index < this.size) {
            return this.buffer[this.index];
        }
        return (byte) 0;
    }

    public byte readByte() {
        if (this.index >= this.size) {
            return (byte) 0;
        }
        byte[] bArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        return bArr[i];
    }

    public String readAtom() {
        return readAtom(0);
    }

    public String readAtom(char delim) {
        skipSpaces();
        if (this.index >= this.size) {
            return null;
        }
        int start = this.index;
        while (this.index < this.size) {
            char b = this.buffer[this.index];
            if (b <= ' ' || b == '(' || b == ')' || b == '%' || b == '*' || b == '\"' || b == '\\' || b == 127 || (delim != 0 && b == delim)) {
                break;
            }
            this.index++;
        }
        return ASCIIUtility.toString(this.buffer, start, this.index);
    }

    public String readString(char delim) {
        skipSpaces();
        if (this.index >= this.size) {
            return null;
        }
        int start = this.index;
        while (this.index < this.size && this.buffer[this.index] != delim) {
            this.index++;
        }
        return ASCIIUtility.toString(this.buffer, start, this.index);
    }

    public String[] readStringList() {
        skipSpaces();
        if (this.buffer[this.index] != (byte) 40) {
            return null;
        }
        this.index++;
        Vector v = new Vector();
        byte[] bArr;
        int i;
        do {
            v.addElement(readString());
            bArr = this.buffer;
            i = this.index;
            this.index = i + 1;
        } while (bArr[i] != (byte) 41);
        int size = v.size();
        if (size <= 0) {
            return null;
        }
        String[] s = new String[size];
        v.copyInto(s);
        return s;
    }

    public int readNumber() {
        skipSpaces();
        int start = this.index;
        while (this.index < this.size && Character.isDigit((char) this.buffer[this.index])) {
            this.index++;
        }
        if (this.index > start) {
            try {
                return ASCIIUtility.parseInt(this.buffer, start, this.index);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    public long readLong() {
        skipSpaces();
        int start = this.index;
        while (this.index < this.size && Character.isDigit((char) this.buffer[this.index])) {
            this.index++;
        }
        if (this.index > start) {
            try {
                return ASCIIUtility.parseLong(this.buffer, start, this.index);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    public String readString() {
        return (String) parseString(false, true);
    }

    public ByteArrayInputStream readBytes() {
        ByteArray ba = readByteArray();
        if (ba != null) {
            return ba.toByteArrayInputStream();
        }
        return null;
    }

    public ByteArray readByteArray() {
        if (!isContinuation()) {
            return (ByteArray) parseString(false, false);
        }
        skipSpaces();
        return new ByteArray(this.buffer, this.index, this.size - this.index);
    }

    public String readAtomString() {
        return (String) parseString(true, true);
    }

    private Object parseString(boolean parseAtoms, boolean returnString) {
        skipSpaces();
        byte b = this.buffer[this.index];
        int start;
        if (b == (byte) 34) {
            this.index++;
            start = this.index;
            int copyto = this.index;
            while (true) {
                b = this.buffer[this.index];
                if (b == (byte) 34) {
                    break;
                }
                if (b == (byte) 92) {
                    this.index++;
                }
                if (this.index != copyto) {
                    this.buffer[copyto] = this.buffer[this.index];
                }
                copyto++;
                this.index++;
            }
            this.index++;
            if (returnString) {
                return ASCIIUtility.toString(this.buffer, start, copyto);
            }
            return new ByteArray(this.buffer, start, copyto - start);
        } else if (b == (byte) 123) {
            start = this.index + 1;
            this.index = start;
            while (this.buffer[this.index] != (byte) 125) {
                this.index++;
            }
            try {
                int count = ASCIIUtility.parseInt(this.buffer, start, this.index);
                start = this.index + 3;
                this.index = start + count;
                if (returnString) {
                    return ASCIIUtility.toString(this.buffer, start, start + count);
                }
                return new ByteArray(this.buffer, start, count);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (parseAtoms) {
            start = this.index;
            Object s = readAtom();
            if (returnString) {
                return s;
            }
            return new ByteArray(this.buffer, start, this.index);
        } else if (b != (byte) 78 && b != (byte) 110) {
            return null;
        } else {
            this.index += 3;
            return null;
        }
    }

    public int getType() {
        return this.type;
    }

    public boolean isContinuation() {
        return (this.type & 3) == 1;
    }

    public boolean isTagged() {
        return (this.type & 3) == 2;
    }

    public boolean isUnTagged() {
        return (this.type & 3) == 3;
    }

    public boolean isOK() {
        return (this.type & 28) == 4;
    }

    public boolean isNO() {
        return (this.type & 28) == 8;
    }

    public boolean isBAD() {
        return (this.type & 28) == 12;
    }

    public boolean isBYE() {
        return (this.type & 28) == 16;
    }

    public boolean isSynthetic() {
        return (this.type & 32) == 32;
    }

    public String getTag() {
        return this.tag;
    }

    public String getRest() {
        skipSpaces();
        return ASCIIUtility.toString(this.buffer, this.index, this.size);
    }

    public void reset() {
        this.index = this.pindex;
    }

    public String toString() {
        return ASCIIUtility.toString(this.buffer, 0, this.size);
    }
}
