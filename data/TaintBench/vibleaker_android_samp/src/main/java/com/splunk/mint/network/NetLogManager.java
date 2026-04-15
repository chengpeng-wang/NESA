package com.splunk.mint.network;

import com.splunk.mint.ActionNetwork;
import com.splunk.mint.Logger;
import com.splunk.mint.Properties;
import com.splunk.mint.network.io.OutputStreamMonitor;
import com.splunk.mint.network.socket.MonitoringSocketImpl;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

public class NetLogManager {
    private static final long CONNECTION_TIMEOUT = 60000;
    private static volatile HashMap<String, StartConnectionInfo> connectionsMap = new HashMap();
    private static NetLogManager mNetLogManager = null;

    class StartConnectionInfo {
        public String protocol;
        public Long startTime;
        public int statusCode;
        public String url;

        StartConnectionInfo() {
        }
    }

    public synchronized void startNetworkCall(String netCallID, String url, long startTime, String protocol) {
        if (netCallID != null) {
            netCallID = cleanUrl(netCallID);
            if (!checkIfURLisExcluded(url)) {
                StartConnectionInfo mStartConnectionInfo = new StartConnectionInfo();
                mStartConnectionInfo.startTime = Long.valueOf(startTime);
                mStartConnectionInfo.url = url;
                mStartConnectionInfo.protocol = protocol;
                connectionsMap.put(netCallID, mStartConnectionInfo);
                removeOldEntries();
            }
        }
    }

    public synchronized void endNetworkCall(MonitorRegistry registry, String netCallID, long stopTime, int statusCode) {
        if (netCallID != null) {
            netCallID = cleanUrl(netCallID);
            if (connectionsMap != null && connectionsMap.containsKey(netCallID)) {
                StartConnectionInfo mSCI = (StartConnectionInfo) connectionsMap.get(netCallID);
                if (mSCI != null) {
                    connectionsMap.remove(netCallID);
                    Long bytesOut = Long.valueOf(0);
                    long bytesIn = 0;
                    Iterator i$ = registry.getMetricsForName(netCallID).iterator();
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
                    ActionNetwork.logNetwork(mSCI.url, mSCI.startTime.longValue(), stopTime, mSCI.protocol, statusCode, bytesOut.longValue(), bytesIn, null, null);
                }
            }
        }
    }

    public synchronized void cancelNetworkCall(MonitorRegistry registry, String netCallID, long stopTime, String protocol, String exception) {
        if (netCallID != null) {
            netCallID = cleanUrl(netCallID);
            if (connectionsMap != null && connectionsMap.containsKey(netCallID)) {
                StartConnectionInfo mSCI = (StartConnectionInfo) connectionsMap.get(netCallID);
                if (mSCI != null) {
                    connectionsMap.remove(netCallID);
                    ActionNetwork.logNetwork(mSCI.url, mSCI.startTime.longValue(), stopTime, protocol, 0, 0, 0, exception, null);
                }
            }
        }
    }

    public synchronized void logNetworkRequest(String url, String protocol, long startT, long endT, int statusCode, long requestLength, long responseLength, String exception, HashMap<String, Object> customData) {
        if (!checkIfURLisExcluded(url)) {
            ActionNetwork.logNetwork(url, startT, endT, protocol, statusCode, requestLength, responseLength, exception, customData);
        }
    }

    private synchronized void removeOldEntries() {
        Iterator<Entry<String, StartConnectionInfo>> it = connectionsMap.entrySet().iterator();
        while (it.hasNext()) {
            if (System.currentTimeMillis() - ((StartConnectionInfo) ((Entry) it.next()).getValue()).startTime.longValue() > CONNECTION_TIMEOUT) {
                it.remove();
            }
        }
    }

    public static synchronized NetLogManager getInstance() {
        NetLogManager netLogManager;
        synchronized (NetLogManager.class) {
            if (mNetLogManager == null) {
                mNetLogManager = new NetLogManager();
            }
            netLogManager = mNetLogManager;
        }
        return netLogManager;
    }

    private boolean checkIfURLisExcluded(String url) {
        if (url == null) {
            return true;
        }
        if (Properties.excludedUrls != null) {
            String clearUrl = cleanUrl(url);
            Iterator i$ = Properties.excludedUrls.iterator();
            while (i$.hasNext()) {
                if (clearUrl.contains((String) i$.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String cleanUrl(String url) {
        if (url != null) {
            return url.toLowerCase().replaceAll("https://", "").replaceAll("http://", "").replaceAll("www.", "");
        }
        return url;
    }

    public static boolean deviceSupporsNetworkMonitoring() {
        try {
            Class implCl = Class.forName(MonitoringSocketImpl.ENCAPSULATED_SOCKET_IMPL);
            if (implCl == null) {
                return false;
            }
            Constructor delegateConstructor = implCl.getDeclaredConstructor(new Class[0]);
            if (delegateConstructor == null) {
                return false;
            }
            delegateConstructor.newInstance(new Object[0]);
            Logger.logInfo("Device supports Network Monitoring");
            return true;
        } catch (RuntimeException e) {
            Logger.logInfo("deviceSupporsNetworkMonitoring: " + e.getMessage());
            return false;
        } catch (Exception e2) {
            Logger.logInfo("deviceSupporsNetworkMonitoring: " + e2.getMessage());
            return false;
        }
    }

    public static final int getStatusCodeFromURLConnection(URLConnection mURLConnection) {
        int statusCode = 0;
        if (mURLConnection == null) {
            return statusCode;
        }
        if (mURLConnection instanceof HttpURLConnection) {
            try {
                return ((HttpURLConnection) mURLConnection).getResponseCode();
            } catch (Exception e) {
                return statusCode;
            }
        } else if (!(mURLConnection instanceof HttpsURLConnection)) {
            return statusCode;
        } else {
            try {
                return ((HttpsURLConnection) mURLConnection).getResponseCode();
            } catch (Exception e2) {
                return statusCode;
            }
        }
    }
}
