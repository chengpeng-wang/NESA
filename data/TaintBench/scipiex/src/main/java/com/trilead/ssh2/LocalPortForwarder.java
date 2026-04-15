package com.trilead.ssh2;

import com.trilead.ssh2.channel.ChannelManager;
import com.trilead.ssh2.channel.LocalAcceptThread;
import java.io.IOException;
import java.net.InetSocketAddress;

public class LocalPortForwarder {
    ChannelManager cm;
    String host_to_connect;
    LocalAcceptThread lat;
    int port_to_connect;

    LocalPortForwarder(ChannelManager cm, int local_port, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.lat = new LocalAcceptThread(cm, local_port, host_to_connect, port_to_connect);
        this.lat.setDaemon(true);
        this.lat.start();
    }

    LocalPortForwarder(ChannelManager cm, InetSocketAddress addr, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.lat = new LocalAcceptThread(cm, addr, host_to_connect, port_to_connect);
        this.lat.setDaemon(true);
        this.lat.start();
    }

    public void close() throws IOException {
        this.lat.stopWorking();
    }
}
