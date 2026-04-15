package org.apache.log4j.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class TelnetAppender extends AppenderSkeleton {
    private int port = 23;
    private SocketHandler sh;

    protected class SocketHandler extends Thread {
        private int MAX_CONNECTIONS = 20;
        private Vector connections = new Vector();
        private ServerSocket serverSocket;
        private final TelnetAppender this$0;
        private Vector writers = new Vector();

        public void finalize() {
            close();
        }

        public void close() {
            synchronized (this) {
                Enumeration e = this.connections.elements();
                while (e.hasMoreElements()) {
                    try {
                        ((Socket) e.nextElement()).close();
                    } catch (InterruptedIOException e2) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e3) {
                    } catch (RuntimeException e4) {
                    }
                }
            }
            try {
                this.serverSocket.close();
            } catch (InterruptedIOException e5) {
                Thread.currentThread().interrupt();
            } catch (IOException | RuntimeException e6) {
            }
        }

        public synchronized void send(String message) {
            Iterator ce = this.connections.iterator();
            Iterator e = this.writers.iterator();
            while (e.hasNext()) {
                ce.next();
                PrintWriter writer = (PrintWriter) e.next();
                writer.print(message);
                if (writer.checkError()) {
                    ce.remove();
                    e.remove();
                }
            }
        }

        public void run() {
            while (!this.serverSocket.isClosed()) {
                try {
                    Socket newClient = this.serverSocket.accept();
                    PrintWriter pw = new PrintWriter(newClient.getOutputStream());
                    if (this.connections.size() < this.MAX_CONNECTIONS) {
                        synchronized (this) {
                            this.connections.addElement(newClient);
                            this.writers.addElement(pw);
                            pw.print(new StringBuffer().append("TelnetAppender v1.0 (").append(this.connections.size()).append(" active connections)\r\n\r\n").toString());
                            pw.flush();
                        }
                    } else {
                        pw.print("Too many connections.\r\n");
                        pw.flush();
                        newClient.close();
                    }
                } catch (Exception e) {
                    if ((e instanceof InterruptedIOException) || (e instanceof InterruptedException)) {
                        Thread.currentThread().interrupt();
                    }
                    if (!this.serverSocket.isClosed()) {
                        LogLog.error("Encountered error while in SocketHandler loop.", e);
                    }
                }
            }
            try {
                this.serverSocket.close();
            } catch (InterruptedIOException e2) {
                Thread.currentThread().interrupt();
            } catch (IOException e3) {
            }
        }

        public SocketHandler(TelnetAppender telnetAppender, int port) throws IOException {
            this.this$0 = telnetAppender;
            this.serverSocket = new ServerSocket(port);
            setName(new StringBuffer().append("TelnetAppender-").append(getName()).append("-").append(port).toString());
        }
    }

    public boolean requiresLayout() {
        return true;
    }

    public void activateOptions() {
        try {
            this.sh = new SocketHandler(this, this.port);
            this.sh.start();
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (RuntimeException e3) {
            e3.printStackTrace();
        }
        super.activateOptions();
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void close() {
        if (this.sh != null) {
            this.sh.close();
            try {
                this.sh.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void append(LoggingEvent event) {
        if (this.sh != null) {
            this.sh.send(this.layout.format(event));
            if (this.layout.ignoresThrowable()) {
                String[] s = event.getThrowableStrRep();
                if (s != null) {
                    StringBuffer buf = new StringBuffer();
                    for (String append : s) {
                        buf.append(append);
                        buf.append("\r\n");
                    }
                    this.sh.send(buf.toString());
                }
            }
        }
    }
}
