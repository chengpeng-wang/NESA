package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Node;

class VastCompanionAdXmlManager {
    private static final String COMPANION_CLICK_THROUGH = "CompanionClickThrough";
    private static final String COMPANION_CLICK_TRACKING = "CompanionClickTracking";
    private static final String CREATIVE_VIEW = "creativeView";
    private static final String EVENT = "event";
    private static final String HEIGHT = "height";
    private static final String TRACKING_EVENTS = "TrackingEvents";
    private static final String VIDEO_TRACKER = "Tracking";
    private static final String WIDTH = "width";
    @NonNull
    private final Node mCompanionNode;
    @NonNull
    private final VastResourceXmlManager mResourceXmlManager;

    VastCompanionAdXmlManager(@NonNull Node companionNode) {
        Preconditions.checkNotNull(companionNode, "companionNode cannot be null");
        this.mCompanionNode = companionNode;
        this.mResourceXmlManager = new VastResourceXmlManager(companionNode);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getWidth() {
        return XmlUtils.getAttributeValueAsInt(this.mCompanionNode, "width");
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getHeight() {
        return XmlUtils.getAttributeValueAsInt(this.mCompanionNode, "height");
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public VastResourceXmlManager getResourceXmlManager() {
        return this.mResourceXmlManager;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getClickThroughUrl() {
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, COMPANION_CLICK_THROUGH));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getClickTrackers() {
        ArrayList arrayList = new ArrayList();
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(this.mCompanionNode, COMPANION_CLICK_TRACKING);
        if (matchingChildNodes == null) {
            return arrayList;
        }
        for (Node nodeValue : matchingChildNodes) {
            String nodeValue2 = XmlUtils.getNodeValue(nodeValue);
            if (!TextUtils.isEmpty(nodeValue2)) {
                arrayList.add(new VastTracker(nodeValue2));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getCompanionCreativeViewTrackers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, TRACKING_EVENTS);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, VIDEO_TRACKER, EVENT, Collections.singletonList(CREATIVE_VIEW))) {
            arrayList.add(new VastTracker(XmlUtils.getNodeValue(firstMatchingChildNode2)));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasResources() {
        return (TextUtils.isEmpty(this.mResourceXmlManager.getStaticResource()) && TextUtils.isEmpty(this.mResourceXmlManager.getHTMLResource()) && TextUtils.isEmpty(this.mResourceXmlManager.getIFrameResource())) ? false : true;
    }
}
