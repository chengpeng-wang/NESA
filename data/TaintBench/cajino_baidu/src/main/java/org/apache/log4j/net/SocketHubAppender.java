package org.apache.log4j.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class SocketHubAppender extends AppenderSkeleton {
    static final int DEFAULT_PORT = 4560;
    public static final String ZONE = "_log4j_obj_tcpaccept_appender.local.";
    private boolean advertiseViaMulticastDNS;
    private String application;
    private CyclicBuffer buffer = null;
    private boolean locationInfo = false;
    private Vector oosList = new Vector();
    private int port = 4560;
    private ServerMonitor serverMonitor = null;
    private ZeroConfSupport zeroConf;

    private class ServerMonitor implements Runnable {
        private boolean keepRunning = true;
        private Thread monitorThread = new Thread(this);
        private Vector oosList;
        private int port;
        private final SocketHubAppender this$0;

        public ServerMonitor(SocketHubAppender socketHubAppender, int _port, Vector _oosList) {
            this.this$0 = socketHubAppender;
            this.port = _port;
            this.oosList = _oosList;
            this.monitorThread.setDaemon(true);
            this.monitorThread.setName(new StringBuffer().append("SocketHubAppender-Monitor-").append(this.port).toString());
            this.monitorThread.start();
        }

        public synchronized void stopMonitor() {
            if (this.keepRunning) {
                LogLog.debug("server monitor thread shutting down");
                this.keepRunning = false;
                try {
                    this.monitorThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                this.monitorThread = null;
                LogLog.debug("server monitor thread shut down");
            }
            return;
        }

        private void sendCachedEvents(ObjectOutputStream stream) throws IOException {
            if (SocketHubAppender.access$000(this.this$0) != null) {
                for (int i = 0; i < SocketHubAppender.access$000(this.this$0).length(); i++) {
                    stream.writeObject(SocketHubAppender.access$000(this.this$0).get(i));
                }
                stream.flush();
                stream.reset();
            }
        }

        public void run() {
            try {
                ServerSocket serverSocket = this.this$0.createServerSocket(this.port);
                serverSocket.setSoTimeout(1000);
                try {
                    serverSocket.setSoTimeout(1000);
                    while (this.keepRunning) {
                        try {
                            Socket socket = null;
                            try {
                                socket = serverSocket.accept();
                            } catch (InterruptedIOException e) {
                            } catch (SocketException e2) {
                                LogLog.error("exception accepting socket, shutting down server socket.", e2);
                                this.keepRunning = false;
                            } catch (IOException e3) {
                                LogLog.error("exception accepting socket.", e3);
                            }
                            if (socket != null) {
                                InetAddress remoteAddress = socket.getInetAddress();
                                LogLog.debug(new StringBuffer().append("accepting connection from ").append(remoteAddress.getHostName()).append(" (").append(remoteAddress.getHostAddress()).append(")").toString());
                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                if (SocketHubAppender.access$000(this.this$0) != null && SocketHubAppender.access$000(this.this$0).length() > 0) {
                                    sendCachedEvents(oos);
                                }
                                this.oosList.addElement(oos);
                            }
                        } catch (IOException e32) {
                            if (e32 instanceof InterruptedIOException) {
                                Thread.currentThread().interrupt();
                            }
                            LogLog.error("exception creating output stream on socket.", e32);
                        } catch (Throwable th) {
                            try {
                                serverSocket.close();
                            } catch (InterruptedIOException e4) {
                                Thread.currentThread().interrupt();
                            } catch (IOException e5) {
                            }
                        }
                    }
                    try {
                        serverSocket.close();
                    } catch (InterruptedIOException e6) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e7) {
                    }
                } catch (SocketException e22) {
                    LogLog.error("exception setting timeout, shutting down server socket.", e22);
                    try {
                        serverSocket.close();
                    } catch (InterruptedIOException e8) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e9) {
                    }
                }
            } catch (Exception e10) {
                if ((e10 instanceof InterruptedIOException) || (e10 instanceof InterruptedException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("exception setting timeout, shutting down server socket.", e10);
                this.keepRunning = false;
            }
        }
    }

    static CyclicBuffer access$000(SocketHubAppender x0) {
        return x0.buffer;
    }

    public SocketHubAppender(int _port) {
        this.port = _port;
        startServer();
    }

    public void activateOptions() {
        if (this.advertiseViaMulticastDNS) {
            this.zeroConf = new ZeroConfSupport(ZONE, this.port, getName());
            this.zeroConf.advertise();
        }
        startServer();
    }

    public synchronized void close() {
        if (!this.closed) {
            LogLog.debug(new StringBuffer().append("closing SocketHubAppender ").append(getName()).toString());
            this.closed = true;
            if (this.advertiseViaMulticastDNS) {
                this.zeroConf.unadvertise();
            }
            cleanUp();
            LogLog.debug(new StringBuffer().append("SocketHubAppender ").append(getName()).append(" closed").toString());
        }
    }

    public void cleanUp() {
        LogLog.debug("stopping ServerSocket");
        this.serverMonitor.stopMonitor();
        this.serverMonitor = null;
        LogLog.debug("closing client connections");
        while (this.oosList.size() != 0) {
            ObjectOutputStream oos = (ObjectOutputStream) this.oosList.elementAt(0);
            if (oos != null) {
                try {
                    oos.close();
                } catch (InterruptedIOException e) {
                    Thread.currentThread().interrupt();
                    LogLog.error("could not close oos.", e);
                } catch (IOException e2) {
                    LogLog.error("could not close oos.", e2);
                }
                this.oosList.removeElementAt(0);
            }
        }
    }

    public void append(LoggingEvent event) {
        if (event != null) {
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
            if (this.buffer != null) {
                this.buffer.add(event);
            }
        }
        if (event != null && this.oosList.size() != 0) {
            int streamCount = 0;
            while (streamCount < this.oosList.size()) {
                ObjectOutputStream oos = null;
                try {
                    oos = (ObjectOutputStream) this.oosList.elementAt(streamCount);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                if (oos != null) {
                    try {
                        oos.writeObject(event);
                        oos.flush();
                        oos.reset();
                    } catch (IOException e2) {
                        if (e2 instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        this.oosList.removeElementAt(streamCount);
                        LogLog.debug("dropped connection");
                        streamCount--;
                    }
                    streamCount++;
                } else {
                    return;
                }
            }
        }
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setPort(int _port) {
        this.port = _port;
    }

    public void setApplication(String lapp) {
        this.application = lapp;
    }

    public String getApplication() {
        return this.application;
    }

    public int getPort() {
        return this.port;
    }

    public void setBufferSize(int _bufferSize) {
        this.buffer = new CyclicBuffer(_bufferSize);
    }

    public int getBufferSize() {
        if (this.buffer == null) {
            return 0;
        }
        return this.buffer.getMaxSize();
    }

    public void setLocationInfo(boolean _locationInfo) {
        this.locationInfo = _locationInfo;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setAdvertiseViaMulticastDNS(boolean advertiseViaMulticastDNS) {
        this.advertiseViaMulticastDNS = advertiseViaMulticastDNS;
    }

    public boolean isAdvertiseViaMulticastDNS() {
        return this.advertiseViaMulticastDNS;
    }

    private void startServer() {
        this.serverMonitor = new ServerMonitor(this, this.port, this.oosList);
    }

    /* access modifiers changed from: protected */
    public ServerSocket createServerSocket(int socketPort) throws IOException {
        return new ServerSocket(socketPort);
    }
}
