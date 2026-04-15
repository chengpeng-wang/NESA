package com.google.android.apps.analytics;

import android.os.Handler;

interface Dispatcher {
    public static final int MSG_DISPATCHED_FINISHED = 13651479;
    public static final int MSG_EVENT_DISPATCHED = 6178583;

    void dispatchEvents(Event[] eventArr);

    void init(Handler handler, String str);

    void stop();
}
