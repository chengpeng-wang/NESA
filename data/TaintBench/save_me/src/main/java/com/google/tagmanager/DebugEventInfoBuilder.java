package com.google.tagmanager;

import com.google.analytics.containertag.proto.MutableDebug.EventInfo;
import com.google.analytics.containertag.proto.MutableDebug.EventInfo.EventType;
import com.google.android.gms.common.util.VisibleForTesting;

class DebugEventInfoBuilder implements EventInfoBuilder {
    private DebugDataLayerEventEvaluationInfoBuilder dataLayerEventBuilder;
    @VisibleForTesting
    EventInfo eventInfoBuilder = EventInfo.newMessage();
    private DebugInformationHandler handler;
    private DebugMacroEvaluationInfoBuilder macroBuilder;

    public DebugEventInfoBuilder(EventType eventType, String containerVersion, String containerId, String key, DebugInformationHandler handler) {
        this.eventInfoBuilder.setEventType(eventType);
        this.eventInfoBuilder.setContainerVersion(containerVersion);
        this.eventInfoBuilder.setContainerId(containerId);
        this.eventInfoBuilder.setKey(key);
        this.handler = handler;
        if (eventType.equals(EventType.DATA_LAYER_EVENT)) {
            this.dataLayerEventBuilder = new DebugDataLayerEventEvaluationInfoBuilder(this.eventInfoBuilder.getMutableDataLayerEventResult());
        } else {
            this.macroBuilder = new DebugMacroEvaluationInfoBuilder(this.eventInfoBuilder.getMutableMacroResult());
        }
    }

    public MacroEvaluationInfoBuilder createMacroEvaluationInfoBuilder() {
        return this.macroBuilder;
    }

    public DataLayerEventEvaluationInfoBuilder createDataLayerEventEvaluationInfoBuilder() {
        return this.dataLayerEventBuilder;
    }

    public void processEventInfo() {
        this.handler.receiveEventInfo(this.eventInfoBuilder);
    }
}
