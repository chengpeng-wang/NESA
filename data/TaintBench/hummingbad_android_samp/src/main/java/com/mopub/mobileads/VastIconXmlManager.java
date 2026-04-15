package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Strings;
import com.mopub.mobileads.util.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

public class VastIconXmlManager {
    public static final String DURATION = "duration";
    public static final String HEIGHT = "height";
    public static final String ICON_CLICKS = "IconClicks";
    public static final String ICON_CLICK_THROUGH = "IconClickThrough";
    public static final String ICON_CLICK_TRACKING = "IconClickTracking";
    public static final String ICON_VIEW_TRACKING = "IconViewTracking";
    public static final String OFFSET = "offset";
    public static final String WIDTH = "width";
    @NonNull
    private final Node mIconNode;
    @NonNull
    private final VastResourceXmlManager mResourceXmlManager;

    VastIconXmlManager(@NonNull Node iconNode) {
        Preconditions.checkNotNull(iconNode);
        this.mIconNode = iconNode;
        this.mResourceXmlManager = new VastResourceXmlManager(iconNode);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getWidth() {
        return XmlUtils.getAttributeValueAsInt(this.mIconNode, WIDTH);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getHeight() {
        return XmlUtils.getAttributeValueAsInt(this.mIconNode, HEIGHT);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getOffsetMS() {
        Integer num = null;
        try {
            return Strings.parseAbsoluteOffset(r1);
        } catch (NumberFormatException e) {
            MoPubLog.d(String.format("Invalid VAST icon offset format: %s:", new Object[]{XmlUtils.getAttributeValue(this.mIconNode, OFFSET)}));
            return num;
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getDurationMS() {
        Integer num = null;
        try {
            return Strings.parseAbsoluteOffset(r1);
        } catch (NumberFormatException e) {
            MoPubLog.d(String.format("Invalid VAST icon duration format: %s:", new Object[]{XmlUtils.getAttributeValue(this.mIconNode, DURATION)}));
            return num;
        }
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public VastResourceXmlManager getResourceXmlManager() {
        return this.mResourceXmlManager;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getClickTrackingUris() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mIconNode, ICON_CLICKS);
        ArrayList arrayList = new ArrayList();
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, ICON_CLICK_TRACKING)) {
            String nodeValue = XmlUtils.getNodeValue(firstMatchingChildNode2);
            if (nodeValue != null) {
                arrayList.add(new VastTracker(nodeValue));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getClickThroughUri() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mIconNode, ICON_CLICKS);
        if (firstMatchingChildNode == null) {
            return null;
        }
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(firstMatchingChildNode, ICON_CLICK_THROUGH));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getViewTrackingUris() {
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(this.mIconNode, ICON_VIEW_TRACKING);
        ArrayList arrayList = new ArrayList();
        for (Node nodeValue : matchingChildNodes) {
            String nodeValue2 = XmlUtils.getNodeValue(nodeValue);
            if (nodeValue2 != null) {
                arrayList.add(new VastTracker(nodeValue2));
            }
        }
        return arrayList;
    }
}
