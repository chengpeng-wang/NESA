package com.google.analytics.tracking.android;

import java.util.Map;

abstract class TrackerHandler {
    public abstract void sendHit(Map<String, String> map);

    TrackerHandler() {
    }
}
