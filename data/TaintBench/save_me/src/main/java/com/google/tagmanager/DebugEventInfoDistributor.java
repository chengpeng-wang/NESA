package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.EventInfo.EventType;

class DebugEventInfoDistributor implements EventInfoDistributor {
    private String containerId;
    private String containerVersion;
    private DebugInformationHandler handler;

    public DebugEventInfoDistributor(DebugInformationHandler handler, String containerVersion, String containerId) {
        this.handler = handler;
        this.containerVersion = containerVersion;
        this.containerId = containerId;
    }

    public EventInfoBuilder createMacroEvalutionEventInfo(String key) {
        return new DebugEventInfoBuilder(EventType.MACRO_REFERENCE, this.containerVersion, this.containerId, key, this.handler);
    }

    public EventInfoBuilder createDataLayerEventEvaluationEventInfo(String event) {
        return new DebugEventInfoBuilder(EventType.DATA_LAYER_EVENT, this.containerVersion, this.containerId, event, this.handler);
    }

    public boolean debugMode() {
        return true;
    }
}
