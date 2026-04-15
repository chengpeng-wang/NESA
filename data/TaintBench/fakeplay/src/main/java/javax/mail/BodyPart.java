package javax.mail;

public abstract class BodyPart implements Part {
    protected Multipart parent;

    public Multipart getParent() {
        return this.parent;
    }

    /* access modifiers changed from: 0000 */
    public void setParent(Multipart parent) {
        this.parent = parent;
    }
}
