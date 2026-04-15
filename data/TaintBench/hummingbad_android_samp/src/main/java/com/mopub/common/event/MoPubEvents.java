package com.mopub.common.event;

import android.os.HandlerThread;
import com.mopub.common.VisibleForTesting;
import java.util.ArrayList;

public class MoPubEvents {
    private static volatile EventDispatcher sEventDispatcher;

    public static void log(BaseEvent baseEvent) {
        getDispatcher().dispatch(baseEvent);
    }

    @VisibleForTesting
    public static void setEventDispatcher(EventDispatcher dispatcher) {
        sEventDispatcher = dispatcher;
    }

    @VisibleForTesting
    static EventDispatcher getDispatcher() {
        EventDispatcher eventDispatcher = sEventDispatcher;
        if (eventDispatcher == null) {
            synchronized (MoPubEvents.class) {
                eventDispatcher = sEventDispatcher;
                if (eventDispatcher == null) {
                    ArrayList arrayList = new ArrayList();
                    HandlerThread handlerThread = new HandlerThread("mopub_event_logging");
                    handlerThread.start();
                    arrayList.add(new ScribeEventRecorder(handlerThread.getLooper()));
                    eventDispatcher = new EventDispatcher(arrayList, handlerThread.getLooper());
                    sEventDispatcher = eventDispatcher;
                }
            }
        }
        return eventDispatcher;
    }
}
