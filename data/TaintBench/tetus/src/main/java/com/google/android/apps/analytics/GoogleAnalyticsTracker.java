package com.google.android.apps.analytics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GoogleAnalyticsTracker {
    static final boolean DEBUG = false;
    private static final GoogleAnalyticsTracker INSTANCE = new GoogleAnalyticsTracker();
    public static final String TRACKER_TAG = "googleanalytics";
    public static final String VERSION = "1.0";
    private String accountId;
    private ConnectivityManager connetivityManager;
    private int dispatchPeriod;
    private Runnable dispatchRunner = new Runnable() {
        public void run() {
            GoogleAnalyticsTracker.this.dispatch();
        }
    };
    private Dispatcher dispatcher;
    private boolean dispatcherIsBusy;
    private EventStore eventStore;
    private int eventsBeingDispatched;
    private int eventsDispatched;
    private Handler handler;
    private Context parent;
    private boolean powerSaveMode;

    private class DispatcherMessageHandler extends Handler {
        public DispatcherMessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == Dispatcher.MSG_DISPATCHED_FINISHED) {
                GoogleAnalyticsTracker.this.dispatchFinished();
            } else if (message.what == Dispatcher.MSG_EVENT_DISPATCHED) {
                GoogleAnalyticsTracker.this.eventDispatched(((Long) message.obj).longValue());
            }
        }
    }

    private GoogleAnalyticsTracker() {
    }

    private void cancelPendingDispathes() {
        this.handler.removeCallbacks(this.dispatchRunner);
    }

    private void createEvent(String str, String str2, String str3, String str4, int i) {
        this.eventStore.putEvent(new Event(this.eventStore.getStoreId(), str, str2, str3, str4, i, this.parent.getResources().getDisplayMetrics().widthPixels, this.parent.getResources().getDisplayMetrics().heightPixels));
        resetPowerSaveMode();
    }

    public static GoogleAnalyticsTracker getInstance() {
        return INSTANCE;
    }

    private void maybeScheduleNextDispatch() {
        if (this.dispatchPeriod >= 0 && this.handler.postDelayed(this.dispatchRunner, (long) (this.dispatchPeriod * 1000))) {
        }
    }

    private void resetPowerSaveMode() {
        if (this.powerSaveMode) {
            this.powerSaveMode = DEBUG;
            maybeScheduleNextDispatch();
        }
    }

    public boolean dispatch() {
        if (this.dispatcherIsBusy) {
            maybeScheduleNextDispatch();
            return DEBUG;
        }
        NetworkInfo activeNetworkInfo = this.connetivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            maybeScheduleNextDispatch();
            return DEBUG;
        }
        this.eventsBeingDispatched = this.eventStore.getNumStoredEvents();
        if (this.eventsBeingDispatched != 0) {
            this.eventsDispatched = 0;
            this.dispatcher.dispatchEvents(this.eventStore.peekEvents());
            this.dispatcherIsBusy = true;
            maybeScheduleNextDispatch();
            return true;
        }
        this.powerSaveMode = true;
        return DEBUG;
    }

    /* access modifiers changed from: 0000 */
    public void dispatchFinished() {
        int i = this.eventsBeingDispatched - this.eventsDispatched;
        if (i != 0) {
            Log.w(TRACKER_TAG, "Dispatcher thinks it finished, but there were " + i + " failed events");
        }
        this.eventsBeingDispatched = 0;
        this.dispatcherIsBusy = DEBUG;
    }

    /* access modifiers changed from: 0000 */
    public void eventDispatched(long j) {
        this.eventStore.deleteEvent(j);
        this.eventsDispatched++;
    }

    public void setDispatchPeriod(int i) {
        int i2 = this.dispatchPeriod;
        this.dispatchPeriod = i;
        if (i2 <= 0) {
            maybeScheduleNextDispatch();
        } else if (i2 > 0) {
            cancelPendingDispathes();
            maybeScheduleNextDispatch();
        }
    }

    public void start(String str, int i, Context context) {
        start(str, i, context, this.eventStore == null ? new PersistentEventStore(context) : this.eventStore, this.dispatcher == null ? new NetworkDispatcher() : this.dispatcher);
    }

    /* access modifiers changed from: 0000 */
    public void start(String str, int i, Context context, EventStore eventStore, Dispatcher dispatcher) {
        this.accountId = str;
        this.parent = context;
        this.eventStore = eventStore;
        this.eventStore.startNewVisit();
        this.dispatcher = dispatcher;
        this.dispatcher.init(new DispatcherMessageHandler(this.parent.getMainLooper()), this.eventStore.getReferrer());
        this.eventsBeingDispatched = 0;
        this.dispatcherIsBusy = DEBUG;
        if (this.connetivityManager == null) {
            this.connetivityManager = (ConnectivityManager) this.parent.getSystemService("connectivity");
        }
        if (this.handler == null) {
            this.handler = new Handler(context.getMainLooper());
        } else {
            cancelPendingDispathes();
        }
        setDispatchPeriod(i);
    }

    public void start(String str, Context context) {
        start(str, -1, context);
    }

    public void stop() {
        this.dispatcher.stop();
        cancelPendingDispathes();
    }

    public void trackEvent(String str, String str2, String str3, int i) {
        createEvent(this.accountId, str, str2, str3, i);
    }

    public void trackPageView(String str) {
        createEvent(this.accountId, "__##GOOGLEPAGEVIEW##__", str, null, -1);
    }
}
