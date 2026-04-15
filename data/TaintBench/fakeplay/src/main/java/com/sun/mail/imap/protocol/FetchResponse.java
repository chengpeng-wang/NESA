package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import java.io.IOException;
import java.util.Vector;

public class FetchResponse extends IMAPResponse {
    private static final char[] HEADER = new char[]{'.', 'H', 'E', 'A', 'D', 'E', 'R'};
    private static final char[] TEXT = new char[]{'.', 'T', 'E', 'X', 'T'};
    private Item[] items;

    public FetchResponse(Protocol p) throws IOException, ProtocolException {
        super(p);
        parse();
    }

    public FetchResponse(IMAPResponse r) throws IOException, ProtocolException {
        super(r);
        parse();
    }

    public int getItemCount() {
        return this.items.length;
    }

    public Item getItem(int index) {
        return this.items[index];
    }

    public Item getItem(Class c) {
        for (int i = 0; i < this.items.length; i++) {
            if (c.isInstance(this.items[i])) {
                return this.items[i];
            }
        }
        return null;
    }

    public static Item getItem(Response[] r, int msgno, Class c) {
        if (r == null) {
            return null;
        }
        int i = 0;
        while (i < r.length) {
            if (r[i] != null && (r[i] instanceof FetchResponse) && ((FetchResponse) r[i]).getNumber() == msgno) {
                FetchResponse f = r[i];
                for (int j = 0; j < f.items.length; j++) {
                    if (c.isInstance(f.items[j])) {
                        return f.items[j];
                    }
                }
                continue;
            }
            i++;
        }
        return null;
    }

    private void parse() throws ParsingException {
        skipSpaces();
        if (this.buffer[this.index] != (byte) 40) {
            throw new ParsingException("error in FETCH parsing, missing '(' at index " + this.index);
        }
        Vector v = new Vector();
        Item i = null;
        do {
            this.index++;
            if (this.index >= this.size) {
                throw new ParsingException("error in FETCH parsing, ran off end of buffer, size " + this.size);
            }
            switch (this.buffer[this.index]) {
                case (byte) 66:
                    if (match(BODY.name)) {
                        if (this.buffer[this.index + 4] != (byte) 91) {
                            if (match(BODYSTRUCTURE.name)) {
                                this.index += BODYSTRUCTURE.name.length;
                            } else {
                                this.index += BODY.name.length;
                            }
                            i = new BODYSTRUCTURE(this);
                            break;
                        }
                        this.index += BODY.name.length;
                        i = new BODY(this);
                        break;
                    }
                    break;
                case (byte) 69:
                    if (match(ENVELOPE.name)) {
                        this.index += ENVELOPE.name.length;
                        i = new ENVELOPE(this);
                        break;
                    }
                    break;
                case (byte) 70:
                    if (match(FLAGS.name)) {
                        this.index += FLAGS.name.length;
                        i = new FLAGS(this);
                        break;
                    }
                    break;
                case (byte) 73:
                    if (match(INTERNALDATE.name)) {
                        this.index += INTERNALDATE.name.length;
                        i = new INTERNALDATE(this);
                        break;
                    }
                    break;
                case (byte) 82:
                    if (!match(RFC822SIZE.name)) {
                        if (match(RFC822DATA.name)) {
                            this.index += RFC822DATA.name.length;
                            if (match(HEADER)) {
                                this.index += HEADER.length;
                            } else if (match(TEXT)) {
                                this.index += TEXT.length;
                            }
                            i = new RFC822DATA(this);
                            break;
                        }
                    }
                    this.index += RFC822SIZE.name.length;
                    i = new RFC822SIZE(this);
                    break;
                    break;
                case (byte) 85:
                    if (match(UID.name)) {
                        this.index += UID.name.length;
                        i = new UID(this);
                        break;
                    }
                    break;
            }
            if (i != null) {
                v.addElement(i);
            }
        } while (this.buffer[this.index] != (byte) 41);
        this.index++;
        this.items = new Item[v.size()];
        v.copyInto(this.items);
    }

    private boolean match(char[] itemName) {
        int j;
        int i;
        int len = itemName.length;
        int j2 = this.index;
        int i2 = 0;
        while (i2 < len) {
            j = j2 + 1;
            i = i2 + 1;
            if (Character.toUpperCase((char) this.buffer[j2]) != itemName[i2]) {
                return false;
            }
            j2 = j;
            i2 = i;
        }
        j = j2;
        i = i2;
        return true;
    }
}
