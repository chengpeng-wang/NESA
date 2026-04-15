package com.trilead.ssh2.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalAcceptThread extends Thread implements IChannelWorkerThread {
    ChannelManager cm;
    String host_to_connect;
    int port_to_connect;
    final ServerSocket ss;

    public LocalAcceptThread(ChannelManager cm, int local_port, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.ss = new ServerSocket(local_port);
    }

    public LocalAcceptThread(ChannelManager cm, InetSocketAddress localAddress, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.ss = new ServerSocket();
        this.ss.bind(localAddress);
    }

    public void run() {
        IOException e;
        try {
            this.cm.registerThread(this);
            while (true) {
                try {
                    Socket s = this.ss.accept();
                    try {
                        Channel cn = this.cm.openDirectTCPIPChannel(this.host_to_connect, this.port_to_connect, s.getInetAddress().getHostAddress(), s.getPort());
                        StreamForwarder r2l;
                        try {
                            r2l = new StreamForwarder(cn, null, null, cn.stdoutStream, s.getOutputStream(), "RemoteToLocal");
                            try {
                                StreamForwarder l2r = new StreamForwarder(cn, r2l, s, s.getInputStream(), cn.stdinStream, "LocalToRemote");
                                r2l.setDaemon(true);
                                l2r.setDaemon(true);
                                r2l.start();
                                l2r.start();
                            } catch (IOException e2) {
                                e = e2;
                                try {
                                    cn.cm.closeChannel(cn, "Weird error during creation of StreamForwarder (" + e.getMessage() + ")", true);
                                } catch (IOException e3) {
                                }
                            }
                        } catch (IOException e4) {
                            e = e4;
                            r2l = null;
                            cn.cm.closeChannel(cn, "Weird error during creation of StreamForwarder (" + e.getMessage() + ")", true);
                        }
                    } catch (IOException e5) {
                        try {
                            s.close();
                        } catch (IOException e6) {
                        }
                    }
                } catch (IOException e7) {
                    stopWorking();
                    return;
                }
            }
        } catch (IOException e8) {
            stopWorking();
        }
    }

    public void stopWorking() {
        try {
            this.ss.close();
        } catch (IOException e) {
        }
    }
}
