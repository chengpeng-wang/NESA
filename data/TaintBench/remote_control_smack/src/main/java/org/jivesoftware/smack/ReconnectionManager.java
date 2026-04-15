package org.jivesoftware.smack;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MKEvent;
import org.jivesoftware.smack.packet.StreamError;

public class ReconnectionManager implements ConnectionListener {
    /* access modifiers changed from: private */
    public XMPPConnection connection;
    boolean done;
    private Thread reconnectionThread;
    private int secondBetweenReconnection;

    /* synthetic */ ReconnectionManager(XMPPConnection x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {
            public void connectionCreated(XMPPConnection connection) {
                connection.addConnectionListener(new ReconnectionManager(connection, null));
            }
        });
    }

    private ReconnectionManager(XMPPConnection connection) {
        this.secondBetweenReconnection = MKEvent.ERROR_PERMISSION_DENIED;
        this.done = false;
        this.connection = connection;
    }

    /* access modifiers changed from: private */
    public boolean isReconnectionAllowed() {
        return (this.done || this.connection.isConnected() || !this.connection.getConfiguration().isReconnectionAllowed() || this.connection.packetReader == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public int getSecondBetweenReconnection() {
        return this.secondBetweenReconnection;
    }

    /* access modifiers changed from: protected */
    public void setSecondBetweenReconnection(int secondBetweenReconnection) {
        this.secondBetweenReconnection = secondBetweenReconnection;
    }

    /* access modifiers changed from: protected */
    public void reconnect() {
        if (isReconnectionAllowed()) {
            this.reconnectionThread = new Thread() {
                private int attempts = 0;
                private int firstReconnectionPeriod = 7;
                private int firstReconnectionTime = 10;
                private int lastReconnectionTime = ReconnectionManager.this.getSecondBetweenReconnection();
                private int notificationPeriod = LocationClientOption.MIN_SCAN_SPAN;
                private int remainingSeconds = 0;
                private int secondReconnectionPeriod = (this.firstReconnectionPeriod + 10);
                private int secondReconnectionTime = 60;

                private int timeDelay() {
                    if (this.attempts > this.secondReconnectionPeriod) {
                        return this.lastReconnectionTime;
                    }
                    if (this.attempts > this.firstReconnectionPeriod) {
                        return this.secondReconnectionTime;
                    }
                    return this.firstReconnectionTime;
                }

                public void run() {
                    while (ReconnectionManager.this.isReconnectionAllowed()) {
                        this.remainingSeconds = timeDelay();
                        while (ReconnectionManager.this.isReconnectionAllowed() && this.remainingSeconds > 0) {
                            try {
                                Thread.sleep((long) this.notificationPeriod);
                                this.remainingSeconds--;
                                ReconnectionManager.this.notifyAttemptToReconnectIn(this.remainingSeconds);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                                ReconnectionManager.this.notifyReconnectionFailed(e1);
                            }
                        }
                        try {
                            if (ReconnectionManager.this.isReconnectionAllowed()) {
                                ReconnectionManager.this.connection.connect();
                            }
                        } catch (XMPPException e) {
                            ReconnectionManager.this.notifyReconnectionFailed(e);
                        }
                    }
                }
            };
            this.reconnectionThread.setName("Smack Reconnection Manager");
            this.reconnectionThread.setDaemon(true);
            this.reconnectionThread.start();
        }
    }

    /* access modifiers changed from: protected */
    public void notifyReconnectionFailed(Exception exception) {
        if (isReconnectionAllowed()) {
            for (ConnectionListener listener : this.connection.packetReader.connectionListeners) {
                listener.reconnectionFailed(exception);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyAttemptToReconnectIn(int seconds) {
        if (isReconnectionAllowed()) {
            for (ConnectionListener listener : this.connection.packetReader.connectionListeners) {
                listener.reconnectingIn(seconds);
            }
        }
    }

    public void connectionClosed() {
        this.done = true;
    }

    public void connectionClosedOnError(Exception e) {
        this.done = false;
        if (e instanceof XMPPException) {
            StreamError error = ((XMPPException) e).getStreamError();
            if (error != null) {
                if ("conflict".equals(error.getCode())) {
                    return;
                }
            }
        }
        if (isReconnectionAllowed()) {
            reconnect();
        }
    }

    public void reconnectingIn(int seconds) {
    }

    public void reconnectionFailed(Exception e) {
    }

    public void reconnectionSuccessful() {
    }
}
