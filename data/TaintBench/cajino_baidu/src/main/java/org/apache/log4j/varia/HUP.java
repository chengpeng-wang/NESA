package org.apache.log4j.varia;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.helpers.LogLog;

/* compiled from: ExternallyRolledFileAppender */
class HUP extends Thread {
    ExternallyRolledFileAppender er;
    int port;

    HUP(ExternallyRolledFileAppender er, int port) {
        this.er = er;
        this.port = port;
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                while (true) {
                    Socket socket = new ServerSocket(this.port).accept();
                    LogLog.debug(new StringBuffer().append("Connected to client at ").append(socket.getInetAddress()).toString());
                    new Thread(new HUPNode(socket, this.er), "ExternallyRolledFileAppender-HUP").start();
                }
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (RuntimeException e3) {
                e3.printStackTrace();
            }
        }
    }
}
