package com.mopub.common.event;

import com.mopub.common.logging.MoPubLog;

class LogCatEventRecorder implements EventRecorder {
    LogCatEventRecorder() {
    }

    public void record(BaseEvent baseEvent) {
        MoPubLog.d(baseEvent.toString());
    }
}
