package org.apache.log4j.chainsaw;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

class LoggingReceiver extends Thread {
    private static final Logger LOG;
    static Class class$org$apache$log4j$chainsaw$LoggingReceiver;
    private MyTableModel mModel;
    private ServerSocket mSvrSock;

    private class Slurper implements Runnable {
        private final Socket mClient;
        private final LoggingReceiver this$0;

        Slurper(LoggingReceiver loggingReceiver, Socket aClient) {
            this.this$0 = loggingReceiver;
            this.mClient = aClient;
        }

        public void run() {
            LoggingReceiver.access$000().debug("Starting to get data");
            try {
                while (true) {
                    LoggingReceiver.access$100(this.this$0).addEvent(new EventDetails((LoggingEvent) new ObjectInputStream(this.mClient.getInputStream()).readObject()));
                }
            } catch (EOFException e) {
                LoggingReceiver.access$000().info("Reached EOF, closing connection");
                try {
                    this.mClient.close();
                } catch (IOException e2) {
                    LoggingReceiver.access$000().warn("Error closing connection", e2);
                }
            } catch (SocketException e3) {
                LoggingReceiver.access$000().info("Caught SocketException, closing connection");
                this.mClient.close();
            } catch (IOException e22) {
                LoggingReceiver.access$000().warn("Got IOException, closing connection", e22);
                this.mClient.close();
            } catch (ClassNotFoundException e4) {
                LoggingReceiver.access$000().warn("Got ClassNotFoundException, closing connection", e4);
                this.mClient.close();
            }
        }
    }

    static Logger access$000() {
        return LOG;
    }

    static MyTableModel access$100(LoggingReceiver x0) {
        return x0.mModel;
    }

    static {
        Class class$;
        if (class$org$apache$log4j$chainsaw$LoggingReceiver == null) {
            class$ = class$("org.apache.log4j.chainsaw.LoggingReceiver");
            class$org$apache$log4j$chainsaw$LoggingReceiver = class$;
        } else {
            class$ = class$org$apache$log4j$chainsaw$LoggingReceiver;
        }
        LOG = Logger.getLogger(class$);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    LoggingReceiver(MyTableModel aModel, int aPort) throws IOException {
        setDaemon(true);
        this.mModel = aModel;
        this.mSvrSock = new ServerSocket(aPort);
    }

    public void run() {
        LOG.info("Thread started");
        while (true) {
            try {
                LOG.debug("Waiting for a connection");
                Socket client = this.mSvrSock.accept();
                LOG.debug(new StringBuffer().append("Got a connection from ").append(client.getInetAddress().getHostName()).toString());
                Thread t = new Thread(new Slurper(this, client));
                t.setDaemon(true);
                t.start();
            } catch (IOException e) {
                LOG.error("Error in accepting connections, stopping.", e);
                return;
            }
        }
    }
}
