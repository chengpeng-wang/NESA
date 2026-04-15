package com.mopub.common.event;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.network.ScribeBackoffPolicy;
import com.mopub.network.ScribeRequest;
import com.mopub.network.ScribeRequest.Listener;
import com.mopub.network.ScribeRequest.ScribeRequestFactory;
import com.mopub.network.ScribeRequestManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ScribeEventRecorder implements EventRecorder {
    private static final int EVENT_COUNT_SEND_THRESHHOLD = 100;
    private static final int POLLING_PERIOD_MS = 120000;
    private static final int QUEUE_MAX_SIZE = 500;
    private static final String SCRIBE_URL = "https://analytics.mopub.com/i/jot/exchange_client_event";
    @NonNull
    private final Queue<BaseEvent> mEventQueue;
    @NonNull
    private final EventSampler mEventSampler;
    /* access modifiers changed from: private|final */
    @NonNull
    public final EventSerializer mEventSerializer;
    @NonNull
    private final Handler mPollHandler;
    @NonNull
    private final PollingRunnable mPollingRunnable;
    @NonNull
    private final ScribeRequestManager mScribeRequestManager;

    class PollingRunnable implements Runnable {
        PollingRunnable() {
        }

        public void run() {
            ScribeEventRecorder.this.sendEvents();
            ScribeEventRecorder.this.scheduleNextPoll();
        }
    }

    ScribeEventRecorder(@NonNull Looper looper) {
        this(new EventSampler(), new LinkedList(), new EventSerializer(), new ScribeRequestManager(looper), new Handler(looper));
    }

    @VisibleForTesting
    ScribeEventRecorder(@NonNull EventSampler eventSampler, @NonNull Queue<BaseEvent> eventQueue, @NonNull EventSerializer eventSerializer, @NonNull ScribeRequestManager scribeRequestManager, @NonNull Handler handler) {
        this.mEventSampler = eventSampler;
        this.mEventQueue = eventQueue;
        this.mEventSerializer = eventSerializer;
        this.mScribeRequestManager = scribeRequestManager;
        this.mPollHandler = handler;
        this.mPollingRunnable = new PollingRunnable();
    }

    public void record(@NonNull BaseEvent baseEvent) {
        if (!this.mEventSampler.sample(baseEvent)) {
            return;
        }
        if (this.mEventQueue.size() >= 500) {
            MoPubLog.d("EventQueue is at max capacity. Event \"" + baseEvent.getName() + "\" is being dropped.");
            return;
        }
        this.mEventQueue.add(baseEvent);
        if (this.mEventQueue.size() >= 100) {
            sendEvents();
        }
        scheduleNextPoll();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void sendEvents() {
        if (!this.mScribeRequestManager.isAtCapacity()) {
            final List dequeueEvents = dequeueEvents();
            if (!dequeueEvents.isEmpty()) {
                this.mScribeRequestManager.makeRequest(new ScribeRequestFactory() {
                    public ScribeRequest createRequest(Listener listener) {
                        return new ScribeRequest(ScribeEventRecorder.SCRIBE_URL, dequeueEvents, ScribeEventRecorder.this.mEventSerializer, listener);
                    }
                }, new ScribeBackoffPolicy());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @VisibleForTesting
    public List<BaseEvent> dequeueEvents() {
        ArrayList arrayList = new ArrayList();
        while (this.mEventQueue.peek() != null && arrayList.size() < 100) {
            arrayList.add(this.mEventQueue.poll());
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void scheduleNextPoll() {
        if (!this.mPollHandler.hasMessages(0) && !this.mEventQueue.isEmpty()) {
            this.mPollHandler.postDelayed(this.mPollingRunnable, 120000);
        }
    }
}
