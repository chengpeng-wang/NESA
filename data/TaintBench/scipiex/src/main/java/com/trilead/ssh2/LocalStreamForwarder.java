package com.trilead.ssh2;

import com.trilead.ssh2.channel.Channel;
import com.trilead.ssh2.channel.ChannelManager;
import com.trilead.ssh2.channel.LocalAcceptThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalStreamForwarder {
    ChannelManager cm;
    Channel cn;
    String host_to_connect;
    LocalAcceptThread lat;
    int port_to_connect;

    LocalStreamForwarder(ChannelManager cm, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.cn = cm.openDirectTCPIPChannel(host_to_connect, port_to_connect, "127.0.0.1", 0);
    }

    public InputStream getInputStream() throws IOException {
        return this.cn.getStdoutStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.cn.getStdinStream();
    }

    public void close() throws IOException {
        this.cm.closeChannel(this.cn, "Closed due to user request.", true);
    }
}
