package org.jivesoftware.smack;

public interface ConnectionListener {
    void connectionClosed();

    void connectionClosedOnError(Exception exception);

    void reconnectingIn(int i);

    void reconnectionFailed(Exception exception);

    void reconnectionSuccessful();
}
