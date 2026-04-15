package javax.mail.event;

import java.util.EventObject;

public abstract class MailEvent extends EventObject {
    private static final long serialVersionUID = 1846275636325456631L;

    public abstract void dispatch(Object obj);

    public MailEvent(Object source) {
        super(source);
    }
}
