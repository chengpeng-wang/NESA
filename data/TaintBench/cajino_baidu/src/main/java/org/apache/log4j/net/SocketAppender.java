package org.apache.log4j.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class SocketAppender extends AppenderSkeleton {
    public static final int DEFAULT_PORT = 4560;
    static final int DEFAULT_RECONNECTION_DELAY = 30000;
    private static final int RESET_FREQUENCY = 1;
    public static final String ZONE = "_log4j_obj_tcpconnect_appender.local.";
    InetAddress address;
    private boolean advertiseViaMulticastDNS;
    private String application;
    private Connector connector;
    int counter = 0;
    boolean locationInfo = false;
    ObjectOutputStream oos;
    int port = DEFAULT_PORT;
    int reconnectionDelay = 30000;
    String remoteHost;
    private ZeroConfSupport zeroConf;

    class Connector extends Thread {
        boolean interrupted = false;
        private final SocketAppender this$0;

        Connector(SocketAppender socketAppender) {
            this.this$0 = socketAppender;
        }

        public void run() {
            while (!this.interrupted) {
                try {
                    Thread.sleep((long) this.this$0.reconnectionDelay);
                    LogLog.debug(new StringBuffer().append("Attempting connection to ").append(this.this$0.address.getHostName()).toString());
                    Socket socket = new Socket(this.this$0.address, this.this$0.port);
                    synchronized (this) {
                        this.this$0.oos = new ObjectOutputStream(socket.getOutputStream());
                        SocketAppender.access$002(this.this$0, null);
                        LogLog.debug("Connection established. Exiting connector thread.");
                    }
                    return;
                } catch (InterruptedException e) {
                    LogLog.debug("Connector interrupted. Leaving loop.");
                    return;
                } catch (ConnectException e2) {
                    LogLog.debug(new StringBuffer().append("Remote host ").append(this.this$0.address.getHostName()).append(" refused connection.").toString());
                } catch (IOException e3) {
                    if (e3 instanceof InterruptedIOException) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.debug(new StringBuffer().append("Could not connect to ").append(this.this$0.address.getHostName()).append(". Exception is ").append(e3).toString());
                }
            }
        }
    }

    static Connector access$002(SocketAppender x0, Connector x1) {
        x0.connector = x1;
        return x1;
    }

    public SocketAppender(InetAddress address, int port) {
        this.address = address;
        this.remoteHost = address.getHostName();
        this.port = port;
        connect(address, port);
    }

    public SocketAppender(String host, int port) {
        this.port = port;
        this.address = getAddressByName(host);
        this.remoteHost = host;
        connect(this.address, port);
    }

    public void activateOptions() {
        if (this.advertiseViaMulticastDNS) {
            this.zeroConf = new ZeroConfSupport(ZONE, this.port, getName());
            this.zeroConf.advertise();
        }
        connect(this.address, this.port);
    }

    public synchronized void close() {
        if (!this.closed) {
            this.closed = true;
            if (this.advertiseViaMulticastDNS) {
                this.zeroConf.unadvertise();
            }
            cleanUp();
        }
    }

    public void cleanUp() {
        if (this.oos != null) {
            try {
                this.oos.close();
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("Could not close oos.", e);
            }
            this.oos = null;
        }
        if (this.connector != null) {
            this.connector.interrupted = true;
            this.connector = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void connect(InetAddress address, int port) {
        if (this.address != null) {
            try {
                cleanUp();
                this.oos = new ObjectOutputStream(new Socket(address, port).getOutputStream());
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                String msg = new StringBuffer().append("Could not connect to remote log4j server at [").append(address.getHostName()).append("].").toString();
                if (this.reconnectionDelay > 0) {
                    msg = new StringBuffer().append(msg).append(" We will try again later.").toString();
                    fireConnector();
                } else {
                    msg = new StringBuffer().append(msg).append(" We are not retrying.").toString();
                    this.errorHandler.error(msg, e, 0);
                }
                LogLog.error(msg);
            }
        }
    }

    public void append(LoggingEvent event) {
        if (event != null) {
            if (this.address == null) {
                this.errorHandler.error(new StringBuffer().append("No remote host is set for SocketAppender named \"").append(this.name).append("\".").toString());
            } else if (this.oos != null) {
                try {
                    if (this.locationInfo) {
                        event.getLocationInformation();
                    }
                    if (this.application != null) {
                        event.setProperty("application", this.application);
                    }
                    event.getNDC();
                    event.getThreadName();
                    event.getMDCCopy();
                    event.getRenderedMessage();
                    event.getThrowableStrRep();
                    this.oos.writeObject(event);
                    this.oos.flush();
                    int i = this.counter + 1;
                    this.counter = i;
                    if (i >= 1) {
                        this.counter = 0;
                        this.oos.reset();
                    }
                } catch (IOException e) {
                    if (e instanceof InterruptedIOException) {
                        Thread.currentThread().interrupt();
                    }
                    this.oos = null;
                    LogLog.warn(new StringBuffer().append("Detected problem with connection: ").append(e).toString());
                    if (this.reconnectionDelay > 0) {
                        fireConnector();
                    } else {
                        this.errorHandler.error("Detected problem with connection, not reconnecting.", e, 0);
                    }
                }
            }
        }
    }

    public void setAdvertiseViaMulticastDNS(boolean advertiseViaMulticastDNS) {
        this.advertiseViaMulticastDNS = advertiseViaMulticastDNS;
    }

    public boolean isAdvertiseViaMulticastDNS() {
        return this.advertiseViaMulticastDNS;
    }

    /* access modifiers changed from: 0000 */
    public void fireConnector() {
        if (this.connector == null) {
            LogLog.debug("Starting a new connector thread.");
            this.connector = new Connector(this);
            this.connector.setDaemon(true);
            this.connector.setPriority(1);
            this.connector.start();
        }
    }

    static InetAddress getAddressByName(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (Exception e) {
            if ((e instanceof InterruptedIOException) || (e instanceof InterruptedException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error(new StringBuffer().append("Could not find address of [").append(host).append("].").toString(), e);
            return null;
        }
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setRemoteHost(String host) {
        this.address = getAddressByName(host);
        this.remoteHost = host;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setApplication(String lapp) {
        this.application = lapp;
    }

    public String getApplication() {
        return this.application;
    }

    public void setReconnectionDelay(int delay) {
        this.reconnectionDelay = delay;
    }

    public int getReconnectionDelay() {
        return this.reconnectionDelay;
    }
}
