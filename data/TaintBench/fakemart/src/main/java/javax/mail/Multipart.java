package javax.mail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public abstract class Multipart {
    protected String contentType = "multipart/mixed";
    protected Part parent;
    protected Vector parts = new Vector();

    public abstract void writeTo(OutputStream outputStream) throws IOException, MessagingException;

    protected Multipart() {
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void setMultipartDataSource(MultipartDataSource mp) throws MessagingException {
        this.contentType = mp.getContentType();
        int count = mp.getCount();
        for (int i = 0; i < count; i++) {
            addBodyPart(mp.getBodyPart(i));
        }
    }

    public String getContentType() {
        return this.contentType;
    }

    public synchronized int getCount() throws MessagingException {
        int i;
        if (this.parts == null) {
            i = 0;
        } else {
            i = this.parts.size();
        }
        return i;
    }

    public synchronized BodyPart getBodyPart(int index) throws MessagingException {
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        return (BodyPart) this.parts.elementAt(index);
    }

    public synchronized boolean removeBodyPart(BodyPart part) throws MessagingException {
        boolean ret;
        if (this.parts == null) {
            throw new MessagingException("No such body part");
        }
        ret = this.parts.removeElement(part);
        part.setParent(null);
        return ret;
    }

    public synchronized void removeBodyPart(int index) throws MessagingException {
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        BodyPart part = (BodyPart) this.parts.elementAt(index);
        this.parts.removeElementAt(index);
        part.setParent(null);
    }

    public synchronized void addBodyPart(BodyPart part) throws MessagingException {
        if (this.parts == null) {
            this.parts = new Vector();
        }
        this.parts.addElement(part);
        part.setParent(this);
    }

    public synchronized void addBodyPart(BodyPart part, int index) throws MessagingException {
        if (this.parts == null) {
            this.parts = new Vector();
        }
        this.parts.insertElementAt(part, index);
        part.setParent(this);
    }

    public synchronized Part getParent() {
        return this.parent;
    }

    public synchronized void setParent(Part parent) {
        this.parent = parent;
    }
}
