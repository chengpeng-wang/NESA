package javax.mail.event;

public class ConnectionEvent extends MailEvent {
    public static final int CLOSED = 3;
    public static final int DISCONNECTED = 2;
    public static final int OPENED = 1;
    private static final long serialVersionUID = -1855480171284792957L;
    protected int type;

    public ConnectionEvent(Object source, int type) {
        super(source);
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void dispatch(Object listener) {
        if (this.type == 1) {
            ((ConnectionListener) listener).opened(this);
        } else if (this.type == 2) {
            ((ConnectionListener) listener).disconnected(this);
        } else if (this.type == 3) {
            ((ConnectionListener) listener).closed(this);
        }
    }
}
