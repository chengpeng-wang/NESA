package com.trilead.ssh2.channel;

import com.trilead.ssh2.log.Logger;
import java.io.IOException;
import java.net.Socket;

public class RemoteAcceptThread extends Thread {
    private static final Logger log = Logger.getLogger(RemoteAcceptThread.class);
    Channel c;
    String remoteConnectedAddress;
    int remoteConnectedPort;
    String remoteOriginatorAddress;
    int remoteOriginatorPort;
    Socket s;
    String targetAddress;
    int targetPort;

    public RemoteAcceptThread(Channel c, String remoteConnectedAddress, int remoteConnectedPort, String remoteOriginatorAddress, int remoteOriginatorPort, String targetAddress, int targetPort) {
        this.c = c;
        this.remoteConnectedAddress = remoteConnectedAddress;
        this.remoteConnectedPort = remoteConnectedPort;
        this.remoteOriginatorAddress = remoteOriginatorAddress;
        this.remoteOriginatorPort = remoteOriginatorPort;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
        if (log.isEnabled()) {
            log.log(20, "RemoteAcceptThread: " + remoteConnectedAddress + "/" + remoteConnectedPort + ", R: " + remoteOriginatorAddress + "/" + remoteOriginatorPort);
        }
    }

    public void run() {
        try {
            this.c.cm.sendOpenConfirmation(this.c);
            this.s = new Socket(this.targetAddress, this.targetPort);
            StreamForwarder r2l = new StreamForwarder(this.c, null, null, this.c.getStdoutStream(), this.s.getOutputStream(), "RemoteToLocal");
            StreamForwarder l2r = new StreamForwarder(this.c, null, null, this.s.getInputStream(), this.c.getStdinStream(), "LocalToRemote");
            r2l.setDaemon(true);
            r2l.start();
            l2r.run();
            while (r2l.isAlive()) {
                try {
                    r2l.join();
                } catch (InterruptedException e) {
                }
            }
            this.c.cm.closeChannel(this.c, "EOF on both streams reached.", true);
            this.s.close();
        } catch (IOException e2) {
            log.log(50, "IOException in proxy code: " + e2.getMessage());
            try {
                this.c.cm.closeChannel(this.c, "IOException in proxy code (" + e2.getMessage() + ")", true);
            } catch (IOException e3) {
            }
            try {
                if (this.s != null) {
                    this.s.close();
                }
            } catch (IOException e4) {
            }
        }
    }
}
