package com.baidu.inf.iis.bcs.http;

import org.apache.http.HttpStatus;
import org.apache.log4j.Priority;

public class ClientConfiguration {
    private int connectionTimeout = Priority.FATAL_INT;
    private int maxConnections = HttpStatus.SC_OK;
    private int maxConnectionsPerRoute = 100;
    private int maxErrorRetry = 3;
    private Protocol protocol = Protocol.HTTP;
    private String proxyDomain = null;
    private String proxyHost = null;
    private String proxyPassword = null;
    private int proxyPort = -1;
    private String proxyUsername = null;
    private String proxyWorkstation = null;
    private int socketReceiveBufferSizeHint = 0;
    private int socketSendBufferSizeHint = 0;
    private int socketTimeout = Priority.FATAL_INT;
    private String userAgent = "baidu-bcs-java-sdk/1";

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return this.maxConnectionsPerRoute;
    }

    public int getMaxErrorRetry() {
        return this.maxErrorRetry;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public String getProxyDomain() {
        return this.proxyDomain;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public String getProxyWorkstation() {
        return this.proxyWorkstation;
    }

    public int[] getSocketBufferSizeHints() {
        return new int[]{this.socketSendBufferSizeHint, this.socketReceiveBufferSizeHint};
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String str) {
        this.userAgent = str;
    }

    public void setConnectionTimeout(int i) {
        this.connectionTimeout = i;
    }

    public void setMaxConnections(int i) {
        this.maxConnections = i;
    }

    public void setMaxConnectionsPerRoute(int i) {
        this.maxConnectionsPerRoute = i;
    }

    public void setMaxErrorRetry(int i) {
        this.maxErrorRetry = i;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void setProxyDomain(String str) {
        this.proxyDomain = str;
    }

    public void setProxyHost(String str) {
        this.proxyHost = str;
    }

    public void setProxyPassword(String str) {
        this.proxyPassword = str;
    }

    public void setProxyPort(int i) {
        this.proxyPort = i;
    }

    public void setProxyUsername(String str) {
        this.proxyUsername = str;
    }

    public void setProxyWorkstation(String str) {
        this.proxyWorkstation = str;
    }

    public void setSocketBufferSizeHints(int i, int i2) {
        this.socketSendBufferSizeHint = i;
        this.socketReceiveBufferSizeHint = i2;
    }

    public void setSocketTimeout(int i) {
        this.socketTimeout = i;
    }

    public ClientConfiguration withConnectionTimeout(int i) {
        setConnectionTimeout(i);
        return this;
    }

    public ClientConfiguration withMaxConnections(int i) {
        setMaxConnections(i);
        return this;
    }

    public ClientConfiguration withMaxConnectionsPerRoute(int i) {
        setMaxConnectionsPerRoute(i);
        return this;
    }

    public ClientConfiguration withMaxErrorRetry(int i) {
        setMaxErrorRetry(i);
        return this;
    }

    public ClientConfiguration withProtocol(Protocol protocol) {
        setProtocol(protocol);
        return this;
    }

    public ClientConfiguration withProxyDomain(String str) {
        setProxyDomain(str);
        return this;
    }

    public ClientConfiguration withProxyHost(String str) {
        setProxyHost(str);
        return this;
    }

    public ClientConfiguration withProxyPassword(String str) {
        setProxyPassword(str);
        return this;
    }

    public ClientConfiguration withProxyPort(int i) {
        setProxyPort(i);
        return this;
    }

    public ClientConfiguration withProxyUsername(String str) {
        setProxyUsername(str);
        return this;
    }

    public ClientConfiguration withProxyWorkstation(String str) {
        setProxyWorkstation(str);
        return this;
    }

    public ClientConfiguration withSocketBufferSizeHints(int i, int i2) {
        setSocketBufferSizeHints(i, i2);
        return this;
    }

    public ClientConfiguration withSocketTimeout(int i) {
        setSocketTimeout(i);
        return this;
    }
}
