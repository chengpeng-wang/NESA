package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import java.util.Vector;

public class Namespaces {
    public Namespace[] otherUsers;
    public Namespace[] personal;
    public Namespace[] shared;

    public static class Namespace {
        public char delimiter;
        public String prefix;

        public Namespace(Response r) throws ProtocolException {
            if (r.readByte() != (byte) 40) {
                throw new ProtocolException("Missing '(' at start of Namespace");
            }
            this.prefix = BASE64MailboxDecoder.decode(r.readString());
            r.skipSpaces();
            if (r.peekByte() == (byte) 34) {
                r.readByte();
                this.delimiter = (char) r.readByte();
                if (this.delimiter == '\\') {
                    this.delimiter = (char) r.readByte();
                }
                if (r.readByte() != (byte) 34) {
                    throw new ProtocolException("Missing '\"' at end of QUOTED_CHAR");
                }
            }
            String s = r.readAtom();
            if (s == null) {
                throw new ProtocolException("Expected NIL, got null");
            } else if (s.equalsIgnoreCase("NIL")) {
                this.delimiter = 0;
            } else {
                throw new ProtocolException("Expected NIL, got " + s);
            }
            if (r.peekByte() != (byte) 41) {
                r.skipSpaces();
                r.readString();
                r.skipSpaces();
                r.readStringList();
            }
            if (r.readByte() != (byte) 41) {
                throw new ProtocolException("Missing ')' at end of Namespace");
            }
        }
    }

    public Namespaces(Response r) throws ProtocolException {
        this.personal = getNamespaces(r);
        this.otherUsers = getNamespaces(r);
        this.shared = getNamespaces(r);
    }

    private Namespace[] getNamespaces(Response r) throws ProtocolException {
        r.skipSpaces();
        if (r.peekByte() == (byte) 40) {
            Vector v = new Vector();
            r.readByte();
            do {
                v.addElement(new Namespace(r));
            } while (r.peekByte() != (byte) 41);
            r.readByte();
            Namespace[] nsa = new Namespace[v.size()];
            v.copyInto(nsa);
            return nsa;
        }
        String s = r.readAtom();
        if (s == null) {
            throw new ProtocolException("Expected NIL, got null");
        } else if (s.equalsIgnoreCase("NIL")) {
            return null;
        } else {
            throw new ProtocolException("Expected NIL, got " + s);
        }
    }
}
