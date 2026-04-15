package org.apache.log4j.helpers;

import java.io.IOException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class SyslogWriter extends Writer {
    static String syslogHost;
    final int SYSLOG_PORT = 514;
    private InetAddress address;
    private DatagramSocket ds;
    private final int port;

    public SyslogWriter(String syslogHost) {
        syslogHost = syslogHost;
        if (syslogHost == null) {
            throw new NullPointerException("syslogHost");
        }
        String host = syslogHost;
        int urlPort = -1;
        if (host.indexOf("[") != -1 || host.indexOf(58) == host.lastIndexOf(58)) {
            try {
                URL url = new URL(new StringBuffer().append("http://").append(host).toString());
                if (url.getHost() != null) {
                    host = url.getHost();
                    if (host.startsWith("[") && host.charAt(host.length() - 1) == ']') {
                        host = host.substring(1, host.length() - 1);
                    }
                    urlPort = url.getPort();
                }
            } catch (MalformedURLException e) {
                LogLog.error("Malformed URL: will attempt to interpret as InetAddress.", e);
            }
        }
        if (urlPort == -1) {
            urlPort = 514;
        }
        this.port = urlPort;
        try {
            this.address = InetAddress.getByName(host);
        } catch (UnknownHostException e2) {
            LogLog.error(new StringBuffer().append("Could not find ").append(host).append(". All logging will FAIL.").toString(), e2);
        }
        try {
            this.ds = new DatagramSocket();
        } catch (SocketException e3) {
            e3.printStackTrace();
            LogLog.error(new StringBuffer().append("Could not instantiate DatagramSocket to ").append(host).append(". All logging will FAIL.").toString(), e3);
        }
    }

    public void write(char[] buf, int off, int len) throws IOException {
        write(new String(buf, off, len));
    }

    public void write(String string) throws IOException {
        if (this.ds != null && this.address != null) {
            byte[] bytes = string.getBytes();
            int bytesLength = bytes.length;
            if (bytesLength >= 1024) {
                bytesLength = 1024;
            }
            this.ds.send(new DatagramPacket(bytes, bytesLength, this.address, this.port));
        }
    }

    public void flush() {
    }

    public void close() {
        if (this.ds != null) {
            this.ds.close();
        }
    }
}
