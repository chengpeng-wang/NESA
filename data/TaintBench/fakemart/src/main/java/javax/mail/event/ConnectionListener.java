package javax.mail.event;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {
    void closed(ConnectionEvent connectionEvent);

    void disconnected(ConnectionEvent connectionEvent);

    void opened(ConnectionEvent connectionEvent);
}
