package com.splunk.mint.network.socket;

import com.splunk.mint.Logger;
import com.splunk.mint.Properties;
import com.splunk.mint.network.Counter;
import com.splunk.mint.network.Metric;
import com.splunk.mint.network.MonitorRegistry;
import com.splunk.mint.network.NetLogManager;
import com.splunk.mint.network.io.InputStreamMonitor;
import com.splunk.mint.network.io.InputStreamMonitorKitKat;
import com.splunk.mint.network.io.OutputStreamMonitor;
import com.splunk.mint.network.util.Delegator;
import com.splunk.mint.network.util.ReflectionUtil;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.HttpHeaders;

public class MonitoringSocketImpl extends SocketImpl {
    public static final String ENCAPSULATED_SOCKET_IMPL = "java.net.PlainSocketImpl";
    private static final String[] HTTP_CLASSES = new String[]{"HttpClient", "URLConnection", "HttpsURLConnectionImpl", "HttpURLConnectionImpl"};
    private static final String[] SYSTEM_PACKAGES = new String[]{"android", "java", "org.apache", "splunk", "libcore"};
    private final Delegator delegator = new Delegator((Object) this, SocketImpl.class, ENCAPSULATED_SOCKET_IMPL);
    private final boolean http = ReflectionUtil.callingClassAnyOf(HTTP_CLASSES);
    private InputStreamMonitor mInputStreamMonitor = null;
    private InputStreamMonitorKitKat mInputStreamMonitorKitKat = null;
    private OutputStreamMonitor mOutputStreamMonitor = null;
    private final String method = ReflectionUtil.extractCallingMethod(SYSTEM_PACKAGES);
    private String name;
    private String protocol = "NA";
    private boolean readingDone = false;
    private final MonitorRegistry registry;
    private long startTime;

    public MonitoringSocketImpl(MonitorRegistry registry) {
        this.registry = registry;
    }

    /* access modifiers changed from: protected */
    public void create(boolean isStreaming) throws IOException {
        try {
            this.delegator.invoke(Boolean.valueOf(isStreaming));
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), this.name);
        }
    }

    /* access modifiers changed from: protected */
    public void bind(InetAddress address, int port) throws IOException {
        this.name = address.getHostName();
        try {
            this.delegator.invoke(address, Integer.valueOf(port));
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), this.name);
        }
        this.startTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public void accept(SocketImpl newSocket) throws IOException {
        try {
            this.delegator.invoke(newSocket);
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), this.name);
        }
    }

    /* access modifiers changed from: protected */
    public int available() throws IOException {
        int i = 0;
        try {
            return ((Integer) this.delegator.invoke(new Object[0])).intValue();
        } catch (Exception e) {
            return i;
        }
    }

    /* access modifiers changed from: protected */
    public void connect(InetAddress address, int port) throws IOException {
        this.name = address.getHostName();
        try {
            this.delegator.delegateTo("connect", InetAddress.class, Integer.TYPE).invoke(address, Integer.valueOf(port));
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), this.name);
        }
        this.startTime = System.currentTimeMillis();
        setProtocolFromPort(port);
    }

    /* access modifiers changed from: protected */
    public void connect(SocketAddress remoteAddr, int timeout) throws IOException {
        if (remoteAddr instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) remoteAddr;
            this.name = addr.getHostName();
            setProtocolFromPort(addr.getPort());
        } else {
            this.name = remoteAddr.toString();
        }
        try {
            this.delegator.invoke(remoteAddr, Integer.valueOf(timeout));
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), this.name);
        }
        this.startTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public void connect(String host, int port) throws IOException {
        this.name = host;
        try {
            this.delegator.invoke(host, Integer.valueOf(port));
        } catch (Exception e) {
            createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), null, null, System.currentTimeMillis(), e.getMessage(), host);
        }
        this.startTime = System.currentTimeMillis();
        setProtocolFromPort(port);
    }

    /* access modifiers changed from: protected */
    public void close() throws IOException {
        try {
            this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            Logger.logError("Error closing socket impl: " + e.getMessage());
        }
    }

    public void readingDone() {
        if (!this.readingDone) {
            this.readingDone = true;
            if (Properties.isKitKat) {
                if (this.mOutputStreamMonitor != null && this.mInputStreamMonitorKitKat != null) {
                    createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), this.mOutputStreamMonitor.getHeaders(), this.mInputStreamMonitorKitKat.getHeaders(), System.currentTimeMillis(), null, null);
                }
            } else if (this.mOutputStreamMonitor != null && this.mInputStreamMonitor != null) {
                createActionEventFromCollectedStats(this.registry.getMetricsForName(this.name), this.mOutputStreamMonitor.getHeaders(), this.mInputStreamMonitor.getHeaders(), System.currentTimeMillis(), null, null);
            }
        }
    }

    private void createActionEventFromCollectedStats(ArrayList<Metric<?>> metrics, HashMap<String, List<String>> outputHeaders, HashMap<String, List<String>> inputHeaders, long endTime, String exception, String exceptionUrl) {
        String url = "";
        if (outputHeaders != null) {
            try {
                url = (String) ((List) outputHeaders.get(HttpHeaders.HOST)).get(0);
            } catch (Exception e) {
            }
            try {
                url = url + ((String) ((List) outputHeaders.get("splk-host2")).get(0));
            } catch (Exception e2) {
            }
        }
        if (exception != null) {
            url = exceptionUrl;
        }
        int statuscode = 0;
        if (inputHeaders != null) {
            try {
                statuscode = Integer.valueOf((String) ((List) inputHeaders.get("splk-statuscode")).get(0)).intValue();
            } catch (Exception e3) {
            }
        }
        Long bytesOut = Long.valueOf(0);
        long bytesIn = 0;
        Iterator i$ = metrics.iterator();
        while (i$.hasNext()) {
            Metric<?> m = (Metric) i$.next();
            if (m instanceof Counter) {
                if (((Counter) m).getName().endsWith(OutputStreamMonitor.OUT_POSTFIX)) {
                    bytesOut = (Long) m.getValue();
                } else if (((Counter) m).getName().endsWith("-bytes-in")) {
                    bytesIn = ((Long) m.getValue()).longValue();
                }
            }
        }
        try {
            bytesIn = Long.valueOf((String) ((List) inputHeaders.get(HttpHeaders.CONTENT_LENGTH)).get(0)).longValue();
        } catch (Exception e4) {
            Logger.logInfo("Could not read the Content-Length HTTP header value");
        }
        NetLogManager.getInstance().logNetworkRequest(url, this.protocol, this.startTime, endTime, statuscode, bytesOut.longValue(), bytesIn, exception, null);
    }

    /* access modifiers changed from: protected */
    public InputStream getInputStream() throws IOException {
        InputStream stream = null;
        try {
            stream = (InputStream) this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stream == null) {
            return null;
        }
        if (Properties.isKitKat) {
            if (this.mInputStreamMonitorKitKat == null) {
                this.mInputStreamMonitorKitKat = new InputStreamMonitorKitKat(this.name, this.registry, stream, this);
            }
            return this.mInputStreamMonitorKitKat;
        }
        if (this.mInputStreamMonitor == null) {
            this.mInputStreamMonitor = new InputStreamMonitor(this.name, this.registry, stream, this);
        }
        return this.mInputStreamMonitor;
    }

    public Object getOption(int optID) throws SocketException {
        try {
            return this.delegator.invoke(Integer.valueOf(optID));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public OutputStream getOutputStream() throws IOException {
        OutputStream out = null;
        try {
            out = (OutputStream) this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (out == null) {
            return null;
        }
        if (this.mOutputStreamMonitor == null) {
            this.mOutputStreamMonitor = new OutputStreamMonitor(this.name, this.registry, out);
        }
        return this.mOutputStreamMonitor;
    }

    /* access modifiers changed from: protected */
    public void listen(int backlog) throws IOException {
        try {
            this.delegator.invoke(Integer.valueOf(backlog));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void sendUrgentData(int value) throws IOException {
        try {
            this.delegator.invoke(Integer.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOption(int optID, Object val) throws SocketException {
        try {
            this.delegator.invoke(Integer.valueOf(optID), val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public FileDescriptor getFileDescriptor() {
        try {
            return (FileDescriptor) this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public InetAddress getInetAddress() {
        try {
            return (InetAddress) this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public int getLocalPort() {
        try {
            return ((Integer) this.delegator.invoke(new Object[0])).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public int getPort() {
        try {
            return ((Integer) this.delegator.invoke(new Object[0])).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        try {
            this.delegator.invoke(Integer.valueOf(connectionTime), Integer.valueOf(latency), Integer.valueOf(bandwidth));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void shutdownInput() throws IOException {
        if (Properties.isKitKat) {
            if (this.mInputStreamMonitorKitKat != null) {
                this.mInputStreamMonitorKitKat.close();
            }
        } else if (this.mInputStreamMonitor != null) {
            this.mInputStreamMonitor.close();
        }
        try {
            this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void shutdownOutput() throws IOException {
        if (this.mOutputStreamMonitor != null) {
            this.mOutputStreamMonitor.close();
        }
        try {
            this.delegator.invoke(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public boolean supportsUrgentData() {
        boolean z = false;
        try {
            return ((Boolean) this.delegator.invoke(new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return z;
        }
    }

    public String getMethod() {
        return this.method;
    }

    public boolean isHttp() {
        return this.http;
    }

    private void setProtocolFromPort(int port) {
        if (port == 80) {
            this.protocol = "HTTP";
        } else if (port == 443) {
            this.protocol = "HTTPS";
        }
    }
}
