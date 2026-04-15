package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.EventInfo;

interface DebugInformationHandler {
    void receiveEventInfo(EventInfo eventInfo);
}
