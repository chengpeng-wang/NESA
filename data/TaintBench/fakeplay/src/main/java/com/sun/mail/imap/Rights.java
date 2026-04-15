package com.sun.mail.imap;

import java.util.Vector;

public class Rights implements Cloneable {
    private boolean[] rights;

    public static final class Right {
        public static final Right ADMINISTER = getInstance('a');
        public static final Right CREATE = getInstance('c');
        public static final Right DELETE = getInstance('d');
        public static final Right INSERT = getInstance('i');
        public static final Right KEEP_SEEN = getInstance('s');
        public static final Right LOOKUP = getInstance('l');
        public static final Right POST = getInstance('p');
        public static final Right READ = getInstance('r');
        public static final Right WRITE = getInstance('w');
        private static Right[] cache = new Right[128];
        char right;

        private Right(char right) {
            if (right >= 128) {
                throw new IllegalArgumentException("Right must be ASCII");
            }
            this.right = right;
        }

        public static synchronized Right getInstance(char right) {
            Right right2;
            synchronized (Right.class) {
                if (right >= 128) {
                    throw new IllegalArgumentException("Right must be ASCII");
                }
                if (cache[right] == null) {
                    cache[right] = new Right(right);
                }
                right2 = cache[right];
            }
            return right2;
        }

        public String toString() {
            return String.valueOf(this.right);
        }
    }

    public Rights() {
        this.rights = new boolean[128];
    }

    public Rights(Rights rights) {
        this.rights = new boolean[128];
        System.arraycopy(rights.rights, 0, this.rights, 0, this.rights.length);
    }

    public Rights(String rights) {
        this.rights = new boolean[128];
        for (int i = 0; i < rights.length(); i++) {
            add(Right.getInstance(rights.charAt(i)));
        }
    }

    public Rights(Right right) {
        this.rights = new boolean[128];
        this.rights[right.right] = true;
    }

    public void add(Right right) {
        this.rights[right.right] = true;
    }

    public void add(Rights rights) {
        for (int i = 0; i < rights.rights.length; i++) {
            if (rights.rights[i]) {
                this.rights[i] = true;
            }
        }
    }

    public void remove(Right right) {
        this.rights[right.right] = false;
    }

    public void remove(Rights rights) {
        for (int i = 0; i < rights.rights.length; i++) {
            if (rights.rights[i]) {
                this.rights[i] = false;
            }
        }
    }

    public boolean contains(Right right) {
        return this.rights[right.right];
    }

    public boolean contains(Rights rights) {
        int i = 0;
        while (i < rights.rights.length) {
            if (rights.rights[i] && !this.rights[i]) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Rights)) {
            return false;
        }
        Rights rights = (Rights) obj;
        for (int i = 0; i < rights.rights.length; i++) {
            if (rights.rights[i] != this.rights[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (boolean z : this.rights) {
            if (z) {
                hash++;
            }
        }
        return hash;
    }

    public Right[] getRights() {
        Vector v = new Vector();
        for (int i = 0; i < this.rights.length; i++) {
            if (this.rights[i]) {
                v.addElement(Right.getInstance((char) i));
            }
        }
        Right[] rights = new Right[v.size()];
        v.copyInto(rights);
        return rights;
    }

    public Object clone() {
        Rights r = null;
        try {
            r = (Rights) super.clone();
            r.rights = new boolean[128];
            System.arraycopy(this.rights, 0, r.rights, 0, this.rights.length);
            return r;
        } catch (CloneNotSupportedException e) {
            return r;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.rights.length; i++) {
            if (this.rights[i]) {
                sb.append((char) i);
            }
        }
        return sb.toString();
    }
}
