package com.google.analytics.tracking.android;

import android.content.Context;
import android.content.Intent;
import com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectedListener;
import com.google.analytics.tracking.android.AnalyticsGmsCoreClient.OnConnectionFailedListener;
import com.google.android.gms.analytics.internal.Command;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

class GAServiceProxy implements ServiceProxy, OnConnectedListener, OnConnectionFailedListener {
    private static final long FAILED_CONNECT_WAIT_TIME = 3000;
    private static final int MAX_TRIES = 2;
    private static final long RECONNECT_WAIT_TIME = 5000;
    private static final long SERVICE_CONNECTION_TIMEOUT = 300000;
    private volatile AnalyticsClient client;
    /* access modifiers changed from: private */
    public Clock clock;
    private volatile int connectTries;
    private final Context ctx;
    /* access modifiers changed from: private|volatile */
    public volatile Timer disconnectCheckTimer;
    private volatile Timer failedConnectTimer;
    private boolean forceLocalDispatch;
    private final GoogleAnalytics gaInstance;
    /* access modifiers changed from: private */
    public long idleTimeout;
    /* access modifiers changed from: private|volatile */
    public volatile long lastRequestTime;
    private boolean pendingClearHits;
    private boolean pendingDispatch;
    private boolean pendingServiceDisconnect;
    /* access modifiers changed from: private|final */
    public final Queue<HitParams> queue;
    private volatile Timer reConnectTimer;
    /* access modifiers changed from: private|volatile */
    public volatile ConnectState state;
    private AnalyticsStore store;
    private AnalyticsStore testStore;
    private final AnalyticsThread thread;

    private enum ConnectState {
        CONNECTING,
        CONNECTED_SERVICE,
        CONNECTED_LOCAL,
        BLOCKED,
        PENDING_CONNECTION,
        PENDING_DISCONNECT,
        DISCONNECTED
    }

    private class DisconnectCheckTask extends TimerTask {
        private DisconnectCheckTask() {
        }

        /* synthetic */ DisconnectCheckTask(GAServiceProxy x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (GAServiceProxy.this.state == ConnectState.CONNECTED_SERVICE && GAServiceProxy.this.queue.isEmpty() && GAServiceProxy.this.lastRequestTime + GAServiceProxy.this.idleTimeout < GAServiceProxy.this.clock.currentTimeMillis()) {
                Log.v("Disconnecting due to inactivity");
                GAServiceProxy.this.disconnectFromService();
                return;
            }
            GAServiceProxy.this.disconnectCheckTimer.schedule(new DisconnectCheckTask(), GAServiceProxy.this.idleTimeout);
        }
    }

    private class FailedConnectTask extends TimerTask {
        private FailedConnectTask() {
        }

        /* synthetic */ FailedConnectTask(GAServiceProxy x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            if (GAServiceProxy.this.state == ConnectState.CONNECTING) {
                GAServiceProxy.this.useStore();
            }
        }
    }

    private static class HitParams {
        private final List<Command> commands;
        private final long hitTimeInMilliseconds;
        private final String path;
        private final Map<String, String> wireFormatParams;

        public HitParams(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, List<Command> commands) {
            this.wireFormatParams = wireFormatParams;
            this.hitTimeInMilliseconds = hitTimeInMilliseconds;
            this.path = path;
            this.commands = commands;
        }

        public Map<String, String> getWireFormatParams() {
            return this.wireFormatParams;
        }

        public long getHitTimeInMilliseconds() {
            return this.hitTimeInMilliseconds;
        }

        public String getPath() {
            return this.path;
        }

        public List<Command> getCommands() {
            return this.commands;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("PATH: ");
            sb.append(this.path);
            if (this.wireFormatParams != null) {
                sb.append("  PARAMS: ");
                for (Entry<String, String> entry : this.wireFormatParams.entrySet()) {
                    sb.append((String) entry.getKey());
                    sb.append("=");
                    sb.append((String) entry.getValue());
                    sb.append(",  ");
                }
            }
            return sb.toString();
        }
    }

    private class ReconnectTask extends TimerTask {
        private ReconnectTask() {
        }

        /* synthetic */ ReconnectTask(GAServiceProxy x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            GAServiceProxy.this.connectToService();
        }
    }

    @VisibleForTesting
    GAServiceProxy(Context ctx, AnalyticsThread thread, AnalyticsStore store, GoogleAnalytics gaInstance) {
        this.queue = new ConcurrentLinkedQueue();
        this.idleTimeout = SERVICE_CONNECTION_TIMEOUT;
        this.testStore = store;
        this.ctx = ctx;
        this.thread = thread;
        this.gaInstance = gaInstance;
        this.clock = new Clock() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.connectTries = 0;
        this.state = ConnectState.DISCONNECTED;
    }

    GAServiceProxy(Context ctx, AnalyticsThread thread) {
        this(ctx, thread, null, GoogleAnalytics.getInstance(ctx));
    }

    /* access modifiers changed from: 0000 */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void putHit(Map<String, String> wireFormatParams, long hitTimeInMilliseconds, String path, List<Command> commands) {
        Log.v("putHit called");
        this.queue.add(new HitParams(wireFormatParams, hitTimeInMilliseconds, path, commands));
        sendQueue();
    }

    public void dispatch() {
        switch (this.state) {
            case CONNECTED_LOCAL:
                dispatchToStore();
                return;
            case CONNECTED_SERVICE:
                return;
            default:
                this.pendingDispatch = true;
                return;
        }
    }

    public void clearHits() {
        Log.v("clearHits called");
        this.queue.clear();
        switch (this.state) {
            case CONNECTED_LOCAL:
                this.store.clearHits(0);
                this.pendingClearHits = false;
                return;
            case CONNECTED_SERVICE:
                this.client.clearHits();
                this.pendingClearHits = false;
                return;
            default:
                this.pendingClearHits = true;
                return;
        }
    }

    public synchronized void setForceLocalDispatch() {
        if (!this.forceLocalDispatch) {
            Log.v("setForceLocalDispatch called.");
            this.forceLocalDispatch = true;
            switch (this.state) {
                case CONNECTED_LOCAL:
                case PENDING_CONNECTION:
                case PENDING_DISCONNECT:
                case DISCONNECTED:
                    break;
                case CONNECTED_SERVICE:
                    disconnectFromService();
                    break;
                case CONNECTING:
                    this.pendingServiceDisconnect = true;
                    break;
                default:
                    break;
            }
        }
    }

    private Timer cancelTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
        }
        return null;
    }

    private void clearAllTimers() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
    }

    public void createService() {
        if (this.client == null) {
            this.client = new AnalyticsGmsCoreClient(this.ctx, this, this);
            connectToService();
        }
    }

    /* access modifiers changed from: 0000 */
    public void createService(AnalyticsClient client) {
        if (this.client == null) {
            this.client = client;
            connectToService();
        }
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /* access modifiers changed from: private|declared_synchronized */
    /* JADX WARNING: Missing block: B:32:0x00cc, code skipped:
            r7.lastRequestTime = r7.clock.currentTimeMillis();
     */
    public synchronized void sendQueue() {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0072 }
        r1 = r7.thread;	 Catch:{ all -> 0x0072 }
        r1 = r1.getThread();	 Catch:{ all -> 0x0072 }
        r0 = r0.equals(r1);	 Catch:{ all -> 0x0072 }
        if (r0 != 0) goto L_0x0021;
    L_0x0011:
        r0 = r7.thread;	 Catch:{ all -> 0x0072 }
        r0 = r0.getQueue();	 Catch:{ all -> 0x0072 }
        r1 = new com.google.analytics.tracking.android.GAServiceProxy$2;	 Catch:{ all -> 0x0072 }
        r1.m600init();	 Catch:{ all -> 0x0072 }
        r0.add(r1);	 Catch:{ all -> 0x0072 }
    L_0x001f:
        monitor-exit(r7);
        return;
    L_0x0021:
        r0 = r7.pendingClearHits;	 Catch:{ all -> 0x0072 }
        if (r0 == 0) goto L_0x0028;
    L_0x0025:
        r7.clearHits();	 Catch:{ all -> 0x0072 }
    L_0x0028:
        r0 = com.google.analytics.tracking.android.GAServiceProxy.AnonymousClass3.$SwitchMap$com$google$analytics$tracking$android$GAServiceProxy$ConnectState;	 Catch:{ all -> 0x0072 }
        r1 = r7.state;	 Catch:{ all -> 0x0072 }
        r1 = r1.ordinal();	 Catch:{ all -> 0x0072 }
        r0 = r0[r1];	 Catch:{ all -> 0x0072 }
        switch(r0) {
            case 1: goto L_0x0036;
            case 2: goto L_0x007d;
            case 3: goto L_0x0035;
            case 4: goto L_0x0035;
            case 5: goto L_0x0035;
            case 6: goto L_0x00d6;
            default: goto L_0x0035;
        };	 Catch:{ all -> 0x0072 }
    L_0x0035:
        goto L_0x001f;
    L_0x0036:
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0072 }
        if (r0 != 0) goto L_0x0075;
    L_0x003e:
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r6 = r0.poll();	 Catch:{ all -> 0x0072 }
        r6 = (com.google.analytics.tracking.android.GAServiceProxy.HitParams) r6;	 Catch:{ all -> 0x0072 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r0.<init>();	 Catch:{ all -> 0x0072 }
        r1 = "Sending hit to store  ";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0072 }
        r0 = r0.append(r6);	 Catch:{ all -> 0x0072 }
        r0 = r0.toString();	 Catch:{ all -> 0x0072 }
        com.google.analytics.tracking.android.Log.v(r0);	 Catch:{ all -> 0x0072 }
        r0 = r7.store;	 Catch:{ all -> 0x0072 }
        r1 = r6.getWireFormatParams();	 Catch:{ all -> 0x0072 }
        r2 = r6.getHitTimeInMilliseconds();	 Catch:{ all -> 0x0072 }
        r4 = r6.getPath();	 Catch:{ all -> 0x0072 }
        r5 = r6.getCommands();	 Catch:{ all -> 0x0072 }
        r0.putHit(r1, r2, r4, r5);	 Catch:{ all -> 0x0072 }
        goto L_0x0036;
    L_0x0072:
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
    L_0x0075:
        r0 = r7.pendingDispatch;	 Catch:{ all -> 0x0072 }
        if (r0 == 0) goto L_0x001f;
    L_0x0079:
        r7.dispatchToStore();	 Catch:{ all -> 0x0072 }
        goto L_0x001f;
    L_0x007d:
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0072 }
        if (r0 != 0) goto L_0x00cc;
    L_0x0085:
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r6 = r0.peek();	 Catch:{ all -> 0x0072 }
        r6 = (com.google.analytics.tracking.android.GAServiceProxy.HitParams) r6;	 Catch:{ all -> 0x0072 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r0.<init>();	 Catch:{ all -> 0x0072 }
        r1 = "Sending hit to service   ";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0072 }
        r0 = r0.append(r6);	 Catch:{ all -> 0x0072 }
        r0 = r0.toString();	 Catch:{ all -> 0x0072 }
        com.google.analytics.tracking.android.Log.v(r0);	 Catch:{ all -> 0x0072 }
        r0 = r7.gaInstance;	 Catch:{ all -> 0x0072 }
        r0 = r0.isDryRunEnabled();	 Catch:{ all -> 0x0072 }
        if (r0 != 0) goto L_0x00c6;
    L_0x00ab:
        r0 = r7.client;	 Catch:{ all -> 0x0072 }
        r1 = r6.getWireFormatParams();	 Catch:{ all -> 0x0072 }
        r2 = r6.getHitTimeInMilliseconds();	 Catch:{ all -> 0x0072 }
        r4 = r6.getPath();	 Catch:{ all -> 0x0072 }
        r5 = r6.getCommands();	 Catch:{ all -> 0x0072 }
        r0.sendHit(r1, r2, r4, r5);	 Catch:{ all -> 0x0072 }
    L_0x00c0:
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r0.poll();	 Catch:{ all -> 0x0072 }
        goto L_0x007d;
    L_0x00c6:
        r0 = "Dry run enabled. Hit not actually sent to service.";
        com.google.analytics.tracking.android.Log.v(r0);	 Catch:{ all -> 0x0072 }
        goto L_0x00c0;
    L_0x00cc:
        r0 = r7.clock;	 Catch:{ all -> 0x0072 }
        r0 = r0.currentTimeMillis();	 Catch:{ all -> 0x0072 }
        r7.lastRequestTime = r0;	 Catch:{ all -> 0x0072 }
        goto L_0x001f;
    L_0x00d6:
        r0 = "Need to reconnect";
        com.google.analytics.tracking.android.Log.v(r0);	 Catch:{ all -> 0x0072 }
        r0 = r7.queue;	 Catch:{ all -> 0x0072 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0072 }
        if (r0 != 0) goto L_0x001f;
    L_0x00e3:
        r7.connectToService();	 Catch:{ all -> 0x0072 }
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.analytics.tracking.android.GAServiceProxy.sendQueue():void");
    }

    private void dispatchToStore() {
        this.store.dispatch();
        this.pendingDispatch = false;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void useStore() {
        if (this.state != ConnectState.CONNECTED_LOCAL) {
            clearAllTimers();
            Log.v("falling back to local store");
            if (this.testStore != null) {
                this.store = this.testStore;
            } else {
                GAServiceManager instance = GAServiceManager.getInstance();
                instance.initialize(this.ctx, this.thread);
                this.store = instance.getStore();
            }
            this.state = ConnectState.CONNECTED_LOCAL;
            sendQueue();
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void connectToService() {
        if (this.forceLocalDispatch || this.client == null || this.state == ConnectState.CONNECTED_LOCAL) {
            Log.w("client not initialized.");
            useStore();
        } else {
            try {
                this.connectTries++;
                cancelTimer(this.failedConnectTimer);
                this.state = ConnectState.CONNECTING;
                this.failedConnectTimer = new Timer("Failed Connect");
                this.failedConnectTimer.schedule(new FailedConnectTask(this, null), FAILED_CONNECT_WAIT_TIME);
                Log.v("connecting to Analytics service");
                this.client.connect();
            } catch (SecurityException e) {
                Log.w("security exception on connectToService");
                useStore();
            }
        }
        return;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void disconnectFromService() {
        if (this.client != null && this.state == ConnectState.CONNECTED_SERVICE) {
            this.state = ConnectState.PENDING_DISCONNECT;
            this.client.disconnect();
        }
    }

    public synchronized void onConnected() {
        this.failedConnectTimer = cancelTimer(this.failedConnectTimer);
        this.connectTries = 0;
        Log.v("Connected to service");
        this.state = ConnectState.CONNECTED_SERVICE;
        if (this.pendingServiceDisconnect) {
            disconnectFromService();
            this.pendingServiceDisconnect = false;
        } else {
            sendQueue();
            this.disconnectCheckTimer = cancelTimer(this.disconnectCheckTimer);
            this.disconnectCheckTimer = new Timer("disconnect check");
            this.disconnectCheckTimer.schedule(new DisconnectCheckTask(this, null), this.idleTimeout);
        }
    }

    public synchronized void onDisconnected() {
        if (this.state == ConnectState.PENDING_DISCONNECT) {
            Log.v("Disconnected from service");
            clearAllTimers();
            this.state = ConnectState.DISCONNECTED;
        } else {
            Log.v("Unexpected disconnect.");
            this.state = ConnectState.PENDING_CONNECTION;
            if (this.connectTries < 2) {
                fireReconnectAttempt();
            } else {
                useStore();
            }
        }
    }

    public synchronized void onConnectionFailed(int errorCode, Intent resolution) {
        this.state = ConnectState.PENDING_CONNECTION;
        if (this.connectTries < 2) {
            Log.w("Service unavailable (code=" + errorCode + "), will retry.");
            fireReconnectAttempt();
        } else {
            Log.w("Service unavailable (code=" + errorCode + "), using local store.");
            useStore();
        }
    }

    private void fireReconnectAttempt() {
        this.reConnectTimer = cancelTimer(this.reConnectTimer);
        this.reConnectTimer = new Timer("Service Reconnect");
        this.reConnectTimer.schedule(new ReconnectTask(this, null), RECONNECT_WAIT_TIME);
    }
}
