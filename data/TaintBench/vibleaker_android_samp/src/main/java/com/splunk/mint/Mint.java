package com.splunk.mint;

import android.content.Context;
import com.splunk.mint.Properties.RemoteSettingsProps;
import com.splunk.mint.network.MonitorRegistry;
import com.splunk.mint.network.NetLogManager;
import com.splunk.mint.network.http.MonitorableURLStreamHandlerFactory;
import com.splunk.mint.network.socket.MonitoringSocketFactory;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.Socket;
import java.net.SocketImplFactory;
import java.net.URL;
import java.util.HashMap;
import javax.net.ssl.SSLSocket;
import org.json.JSONObject;

public final class Mint {
    static boolean DEBUG = false;
    public static final String XSplunkMintSessionIdHeader = "X-Splunk-Mint-Session-id";
    public static final String XSplunkMintUuidHeader = "X-Splunk-Mint-uuid";
    private static boolean isSessionActive = false;
    static MintCallback mintCallback = null;
    static boolean netInitializedSuccessfully = false;
    private static boolean networkMonitoringEnabled = true;
    private static MonitorRegistry registry = new MonitorRegistry();
    static boolean triedToInitNet = false;

    public static void initAndStartSession(Context context, String apiKey) {
        initAndStartSession(context, apiKey, null);
    }

    private static void initAndStartSession(final Context context, String apiKey, String url) {
        if (context == null) {
            Logger.logWarning("Context is null!");
        } else if (apiKey == null || apiKey.length() < 8 || apiKey.length() > 14) {
            throw new IllegalArgumentException("Your Mint API Key is invalid!");
        } else {
            Properties.API_KEY = apiKey;
            Properties.TIMESTAMP = System.currentTimeMillis();
            Properties.initialize(context, url, apiKey);
            new LowPriorityThreadFactory().newThread(new Runnable() {
                public void run() {
                    Mint.installExceptionHandler();
                    if (!Mint.netInitializedSuccessfully) {
                        Mint.initializeNetworkMonitoring();
                    }
                    Mint.startSession(context);
                    Mint.flush();
                }
            }).start();
        }
    }

    /* access modifiers changed from: private|static|declared_synchronized */
    public static synchronized void initializeNetworkMonitoring() {
        synchronized (Mint.class) {
            if (networkMonitoringEnabled && !triedToInitNet && NetLogManager.deviceSupporsNetworkMonitoring() && RemoteSettingsProps.netMonitoringEnabled.booleanValue()) {
                Logger.logInfo("Initializing Network Monitoring");
                triedToInitNet = true;
                try {
                    URL.setURLStreamHandlerFactory(new MonitorableURLStreamHandlerFactory(registry));
                } catch (Throwable th) {
                    netInitializedSuccessfully = false;
                }
                try {
                    SocketImplFactory factory = new MonitoringSocketFactory(registry);
                    Socket.setSocketImplFactory(factory);
                    SSLSocket.setSocketImplFactory(factory);
                    netInitializedSuccessfully = true;
                } catch (Throwable th2) {
                    netInitializedSuccessfully = false;
                }
                if (netInitializedSuccessfully) {
                    Logger.logInfo("Network monitoring was initialized successfully!");
                }
            }
        }
        return;
    }

    public MonitorRegistry getRegistry() {
        if (registry == null) {
            registry = new MonitorRegistry();
        }
        return registry;
    }

    public static void startSession(final Context context) {
        if (context == null) {
            Logger.logWarning("Context is null!");
            return;
        }
        if (!isSessionActive) {
            isSessionActive = true;
            Properties.initialize(context, null, null);
        }
        new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                if (Utils.shouldSendPing(context)) {
                    ActionEvent.createPing().send(context, new NetSender(), true);
                }
            }
        }).start();
    }

    public static void closeSession(Context context) {
        if (Properties.isPluginInitialized() && isSessionActive) {
            isSessionActive = false;
            ActionEvent.createGnip().save(new DataSaver());
            Utils.clearLastPingSentTime(context);
        }
    }

    public static void flush() {
        if (Properties.isPluginInitialized()) {
            new DataFlusher().send();
        }
    }

    public static void setUserOptOut(boolean optedOut) {
        Properties.USER_OPTEDOUT = optedOut;
    }

    public static void disableNetworkMonitoring() {
        networkMonitoringEnabled = false;
    }

    public static final void setMintCallback(MintCallback mintCallback) {
        mintCallback = mintCallback;
    }

    public static void leaveBreadcrumb(String breadcrumb) {
        if (Properties.isPluginInitialized() && breadcrumb != null) {
            Properties.breadcrumbs.addToList(breadcrumb);
        }
    }

    public static void logView(String view, HashMap<String, Object> extraData) {
        if (Properties.isPluginInitialized() && view != null) {
            ActionView.logView(view, extraData);
        }
    }

    public static void logEvent(String eventName) {
        if (Properties.isPluginInitialized() && eventName != null) {
            ActionEvent.createEvent(eventName).save(new DataSaver());
        }
    }

    public static void logEvent(String eventName, MintLogLevel logLevel) {
        if (Properties.isPluginInitialized() && eventName != null) {
            ActionEvent.createEvent(eventName, logLevel, null).save(new DataSaver());
        }
    }

    public static void logEvent(String eventName, MintLogLevel logLevel, HashMap<String, Object> customData) {
        if (Properties.isPluginInitialized() && eventName != null) {
            ActionEvent.createEvent(eventName, logLevel, customData).save(new DataSaver());
        }
    }

    public static void logEvent(String eventName, MintLogLevel logLevel, String keyName, String keyValue) {
        if (Properties.isPluginInitialized()) {
            HashMap<String, Object> customData = new HashMap(1);
            customData.put(keyName, keyValue);
            logEvent(eventName, logLevel, customData);
        }
    }

    public static void setUserIdentifier(String userIdentifier) {
        Properties.userIdentifier = userIdentifier;
    }

    public static void enableDebug() {
        DEBUG = true;
    }

    public static void setFlushOnlyOverWiFi(boolean enabled) {
        Properties.flushOnlyOverWiFi = enabled;
    }

    public static void transactionStart(String name) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStart.createTransactionStart(name, null).save(new DataSaver());
        }
    }

    public static void transactionStart(String name, HashMap<String, Object> customData) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStart.createTransactionStart(name, customData).save(new DataSaver());
        }
    }

    public static void transactionStart(String name, String keyName, String keyValue) {
        if (Properties.isPluginInitialized()) {
            HashMap<String, Object> customData = new HashMap(1);
            customData.put(keyName, keyValue);
            transactionStart(keyName, customData);
        }
    }

    public static void transactionStop(String name) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStop.createTransactionStop(name, null).save(new DataSaver());
        }
    }

    public static void transactionStop(String name, HashMap<String, Object> customData) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStop.createTransactionStop(name, customData).save(new DataSaver());
        }
    }

    public static void transactionStop(String name, String keyName, String keyValue) {
        if (Properties.isPluginInitialized() && name != null) {
            HashMap<String, Object> customData = new HashMap(1);
            customData.put(keyName, keyValue);
            transactionStop(keyName, customData);
        }
    }

    public static void transactionCancel(String name, String reason) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStop.createTransactionCancel(name, reason, null).save(new DataSaver());
        }
    }

    public static void transactionCancel(String name, String reason, HashMap<String, Object> customData) {
        if (Properties.isPluginInitialized() && name != null) {
            ActionTransactionStop.createTransactionCancel(name, reason, customData).save(new DataSaver());
        }
    }

    public static void transactionCancel(String name, String reason, String keyName, String keyValue) {
        if (Properties.isPluginInitialized() && name != null) {
            HashMap<String, Object> customData = new HashMap(1);
            customData.put(keyName, keyValue);
            transactionCancel(keyName, reason, customData);
        }
    }

    public static HashMap<String, Object> getExtraData() {
        if (Properties.extraData == null) {
            Properties.extraData = new ExtraData();
        }
        return Properties.extraData.getExtraData();
    }

    public static void addExtraData(String key, String value) {
        if (Properties.extraData == null) {
            Properties.extraData = new ExtraData();
        }
        if (key != null) {
            Object value2;
            if (value2 == null) {
                value2 = "null";
            }
            Properties.extraData.addExtraData(key, value2);
        }
    }

    public static void addExtraDataMap(HashMap<String, Object> extras) {
        if (Properties.extraData == null) {
            Properties.extraData = new ExtraData();
        }
        if (extras != null) {
            Properties.extraData.addExtraDataMap(extras);
        }
    }

    public static void removeExtraData(String key) {
        if (Properties.extraData == null) {
            Properties.extraData = new ExtraData();
        }
        if (key != null) {
            Properties.extraData.removeKey(key);
        }
    }

    public static void clearExtraData() {
        if (Properties.extraData == null) {
            Properties.extraData = new ExtraData();
        }
        Properties.extraData.clearData();
    }

    public static int getTotalCrashesNum() {
        return CrashInfo.getTotalCrashesNum();
    }

    public static void clearTotalCrashesNum() {
        if (Properties.isPluginInitialized()) {
            new CrashInfo().clearCrashCounter();
        }
    }

    public static String getLastCrashID() {
        return CrashInfo.getLastCrashID();
    }

    public static void logExceptionMap(HashMap<String, Object> customData, Exception exception) {
        if (Properties.isPluginInitialized()) {
            Writer stacktrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stacktrace));
            new ActionError(EnumActionType.error, stacktrace.toString(), EnumExceptionType.HANDLED, customData).save(new DataSaver());
        }
    }

    public static void logException(Exception ex) {
        logExceptionMap(new HashMap(0), ex);
    }

    public static void logExceptionMessage(String key, String value, Exception exception) {
        HashMap<String, Object> extraData = new HashMap(1);
        if (!(key == null || value == null)) {
            extraData.put(key, value);
        }
        logExceptionMap(extraData, exception);
    }

    public static void xamarinException(Exception exception, boolean handled, HashMap<String, Object> customData) {
        Writer stacktrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stacktrace));
        EnumExceptionType isHandled = EnumExceptionType.UNHANDLED;
        if (handled) {
            isHandled = EnumExceptionType.HANDLED;
        }
        new ActionError(EnumActionType.error, stacktrace.toString().replaceFirst("\n", "\n\t"), isHandled, customData).save(new DataSaver());
    }

    public static void enableLogging(boolean enable) {
        Properties.SEND_LOG = enable;
    }

    public static void setLogging(int lines) {
        if (lines > 0) {
            Properties.SEND_LOG = true;
            Properties.LOG_LINES = lines;
        }
    }

    public static void setLogging(String filter) {
        if (filter != null) {
            Properties.SEND_LOG = true;
            Properties.LOG_FILTER = filter;
        }
    }

    public static void setLogging(int lines, String filter) {
        if (filter != null && lines > 0) {
            Properties.SEND_LOG = true;
            Properties.LOG_LINES = lines;
            Properties.LOG_FILTER = filter;
        }
    }

    public static JSONObject getDevSettings() {
        return RemoteSettingsProps.devSettings;
    }

    public static void addURLToBlackList(String url) {
        if (url != null) {
            Properties.excludedUrls.addValue(url);
        }
    }

    /* access modifiers changed from: private|static */
    public static void installExceptionHandler() {
        Logger.logInfo("Registering the exception handler");
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(currentHandler instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler));
        }
    }

    public static final String getSessionId() {
        if (ActionEvent.savedSessionID != null || ActionEvent.savedSessionID.length() > 0) {
            return ActionEvent.savedSessionID;
        }
        return "NA";
    }

    public static final String getMintUUID() {
        if (Properties.UID != null || Properties.UID.length() > 0) {
            return Properties.UID;
        }
        return "NA";
    }
}
