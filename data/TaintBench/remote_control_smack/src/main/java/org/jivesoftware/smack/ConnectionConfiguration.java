package org.jivesoftware.smack;

import java.io.File;
import javax.net.SocketFactory;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.DNSUtil.HostAddress;

public class ConnectionConfiguration implements Cloneable {
    private boolean compressionEnabled = false;
    private boolean debuggerEnabled = XMPPConnection.DEBUG_ENABLED;
    private boolean expiredCertificatesCheckEnabled = false;
    private String host;
    private boolean notMatchingDomainCheckEnabled = false;
    private String password;
    private int port;
    private boolean reconnectionAllowed = true;
    private String resource;
    private boolean saslAuthenticationEnabled = true;
    private SecurityMode securityMode = SecurityMode.enabled;
    private boolean selfSignedCertificateEnabled = false;
    private boolean sendPresence;
    private String serviceName;
    private SocketFactory socketFactory;
    private String truststorePassword;
    private String truststorePath;
    private String truststoreType;
    private String username;
    private boolean verifyChainEnabled = false;
    private boolean verifyRootCAEnabled = false;

    public enum SecurityMode {
        required,
        enabled,
        disabled
    }

    public ConnectionConfiguration(String serviceName) {
        HostAddress address = DNSUtil.resolveXMPPDomain(serviceName);
        init(address.getHost(), address.getPort(), serviceName);
    }

    public ConnectionConfiguration(String host, int port, String serviceName) {
        init(host, port, serviceName);
    }

    public ConnectionConfiguration(String host, int port) {
        init(host, port, host);
    }

    private void init(String host, int port, String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
        String javaHome = System.getProperty("java.home");
        StringBuilder buffer = new StringBuilder();
        buffer.append(javaHome).append(File.separator).append("lib");
        buffer.append(File.separator).append("security");
        buffer.append(File.separator).append("cacerts");
        this.truststorePath = buffer.toString();
        this.truststoreType = "jks";
        this.truststorePassword = "changeit";
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public SecurityMode getSecurityMode() {
        return this.securityMode;
    }

    public void setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
    }

    public String getTruststorePath() {
        return this.truststorePath;
    }

    public void setTruststorePath(String truststorePath) {
        this.truststorePath = truststorePath;
    }

    public String getTruststoreType() {
        return this.truststoreType;
    }

    public void setTruststoreType(String truststoreType) {
        this.truststoreType = truststoreType;
    }

    public String getTruststorePassword() {
        return this.truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public boolean isVerifyChainEnabled() {
        return this.verifyChainEnabled;
    }

    public void setVerifyChainEnabled(boolean verifyChainEnabled) {
        this.verifyChainEnabled = verifyChainEnabled;
    }

    public boolean isVerifyRootCAEnabled() {
        return this.verifyRootCAEnabled;
    }

    public void setVerifyRootCAEnabled(boolean verifyRootCAEnabled) {
        this.verifyRootCAEnabled = verifyRootCAEnabled;
    }

    public boolean isSelfSignedCertificateEnabled() {
        return this.selfSignedCertificateEnabled;
    }

    public void setSelfSignedCertificateEnabled(boolean selfSignedCertificateEnabled) {
        this.selfSignedCertificateEnabled = selfSignedCertificateEnabled;
    }

    public boolean isExpiredCertificatesCheckEnabled() {
        return this.expiredCertificatesCheckEnabled;
    }

    public void setExpiredCertificatesCheckEnabled(boolean expiredCertificatesCheckEnabled) {
        this.expiredCertificatesCheckEnabled = expiredCertificatesCheckEnabled;
    }

    public boolean isNotMatchingDomainCheckEnabled() {
        return this.notMatchingDomainCheckEnabled;
    }

    public void setNotMatchingDomainCheckEnabled(boolean notMatchingDomainCheckEnabled) {
        this.notMatchingDomainCheckEnabled = notMatchingDomainCheckEnabled;
    }

    public boolean isCompressionEnabled() {
        return this.compressionEnabled;
    }

    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    public boolean isSASLAuthenticationEnabled() {
        return this.saslAuthenticationEnabled;
    }

    public void setSASLAuthenticationEnabled(boolean saslAuthenticationEnabled) {
        this.saslAuthenticationEnabled = saslAuthenticationEnabled;
    }

    public boolean isDebuggerEnabled() {
        return this.debuggerEnabled;
    }

    public void setDebuggerEnabled(boolean debuggerEnabled) {
        this.debuggerEnabled = debuggerEnabled;
    }

    public void setReconnectionAllowed(boolean isAllowed) {
        this.reconnectionAllowed = isAllowed;
    }

    public boolean isReconnectionAllowed() {
        return this.reconnectionAllowed;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    /* access modifiers changed from: 0000 */
    public String getUsername() {
        return this.username;
    }

    /* access modifiers changed from: 0000 */
    public String getPassword() {
        return this.password;
    }

    /* access modifiers changed from: 0000 */
    public String getResource() {
        return this.resource;
    }

    /* access modifiers changed from: 0000 */
    public boolean isSendPresence() {
        return this.sendPresence;
    }

    /* access modifiers changed from: 0000 */
    public void setLoginInfo(String username, String password, String resource, boolean sendPresence) {
        this.username = username;
        this.password = password;
        this.resource = resource;
        this.sendPresence = sendPresence;
    }
}
