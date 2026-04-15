package com.mopub.common.event;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;

public class EventDispatcher {
    /* access modifiers changed from: private|final */
    public final Iterable<EventRecorder> mEventRecorders;
    private final Callback mHandlerCallback = new Callback() {
        public boolean handleMessage(Message msg) {
            if (msg.obj instanceof BaseEvent) {
                for (EventRecorder record : EventDispatcher.this.mEventRecorders) {
                    record.record((BaseEvent) msg.obj);
                }
            } else {
                MoPubLog.d("EventDispatcher received non-BaseEvent message type.");
            }
            return true;
        }
    };
    private final Looper mLooper;
    private final Handler mMessageHandler = new Handler(this.mLooper, this.mHandlerCallback);

    @VisibleForTesting
    EventDispatcher(Iterable<EventRecorder> recorders, Looper looper) {
        this.mEventRecorders = recorders;
        this.mLooper = looper;
    }

    public void dispatch(BaseEvent event) {
        Message.obtain(this.mMessageHandler, 0, event).sendToTarget();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public Iterable<EventRecorder> getEventRecorders() {
        return this.mEventRecorders;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public Callback getHandlerCallback() {
        return this.mHandlerCallback;
    }
}
