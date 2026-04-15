package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Strings;
import com.mopub.mobileads.util.XmlUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Node;

class VastLinearXmlManager {
    private static final String CLICK_THROUGH = "ClickThrough";
    private static final String CLICK_TRACKER = "ClickTracking";
    private static final String CLOSE = "close";
    private static final String CLOSE_LINEAR = "closeLinear";
    private static final String COMPLETE = "complete";
    private static final String CREATIVE_VIEW = "creativeView";
    private static final int CREATIVE_VIEW_TRACKER_THRESHOLD = 0;
    private static final String EVENT = "event";
    private static final float FIRST_QUARTER_MARKER = 0.25f;
    private static final String FIRST_QUARTILE = "firstQuartile";
    public static final String ICON = "Icon";
    public static final String ICONS = "Icons";
    private static final String MEDIA_FILE = "MediaFile";
    private static final String MEDIA_FILES = "MediaFiles";
    private static final String MIDPOINT = "midpoint";
    private static final float MID_POINT_MARKER = 0.5f;
    private static final String OFFSET = "offset";
    private static final String PAUSE = "pause";
    private static final String PROGRESS = "progress";
    private static final String RESUME = "resume";
    private static final String SKIP = "skip";
    private static final String SKIP_OFFSET = "skipoffset";
    private static final String START = "start";
    private static final int START_TRACKER_THRESHOLD = 2000;
    private static final float THIRD_QUARTER_MARKER = 0.75f;
    private static final String THIRD_QUARTILE = "thirdQuartile";
    private static final String TRACKING_EVENTS = "TrackingEvents";
    private static final String VIDEO_CLICKS = "VideoClicks";
    private static final String VIDEO_TRACKER = "Tracking";
    @NonNull
    private final Node mLinearNode;

    VastLinearXmlManager(@NonNull Node linearNode) {
        Preconditions.checkNotNull(linearNode);
        this.mLinearNode = linearNode;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastFractionalProgressTracker> getFractionalProgressTrackers() {
        ArrayList arrayList = new ArrayList();
        addQuartileTrackerWithFraction(arrayList, getVideoTrackersByAttribute(FIRST_QUARTILE), FIRST_QUARTER_MARKER);
        addQuartileTrackerWithFraction(arrayList, getVideoTrackersByAttribute(MIDPOINT), MID_POINT_MARKER);
        addQuartileTrackerWithFraction(arrayList, getVideoTrackersByAttribute(THIRD_QUARTILE), THIRD_QUARTER_MARKER);
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, TRACKING_EVENTS);
        if (firstMatchingChildNode != null) {
            for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, VIDEO_TRACKER, EVENT, Collections.singletonList(PROGRESS))) {
                String attributeValue = XmlUtils.getAttributeValue(firstMatchingChildNode2, "offset");
                if (attributeValue != null) {
                    attributeValue = attributeValue.trim();
                    if (Strings.isPercentageTracker(attributeValue)) {
                        try {
                            arrayList.add(new VastFractionalProgressTracker(XmlUtils.getNodeValue(firstMatchingChildNode2), Float.parseFloat(attributeValue.replace("%", "")) / 100.0f));
                        } catch (NumberFormatException e) {
                            MoPubLog.d(String.format("Failed to parse VAST progress tracker %s", new Object[]{attributeValue}));
                        }
                    }
                }
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastAbsoluteProgressTracker> getAbsoluteProgressTrackers() {
        String nodeValue;
        ArrayList arrayList = new ArrayList();
        for (String nodeValue2 : getVideoTrackersByAttribute(START)) {
            arrayList.add(new VastAbsoluteProgressTracker(nodeValue2, 2000));
        }
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, TRACKING_EVENTS);
        if (firstMatchingChildNode != null) {
            for (Node node : XmlUtils.getMatchingChildNodes(firstMatchingChildNode, VIDEO_TRACKER, EVENT, Collections.singletonList(PROGRESS))) {
                String attributeValue = XmlUtils.getAttributeValue(node, "offset");
                if (attributeValue != null) {
                    attributeValue = attributeValue.trim();
                    if (Strings.isAbsoluteTracker(attributeValue)) {
                        nodeValue2 = XmlUtils.getNodeValue(node);
                        try {
                            Integer parseAbsoluteOffset = Strings.parseAbsoluteOffset(attributeValue);
                            if (parseAbsoluteOffset != null) {
                                arrayList.add(new VastAbsoluteProgressTracker(nodeValue2, parseAbsoluteOffset.intValue()));
                            }
                        } catch (NumberFormatException e) {
                            MoPubLog.d(String.format("Failed to parse VAST progress tracker %s", new Object[]{attributeValue}));
                        }
                    }
                }
            }
            for (Node node2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode, VIDEO_TRACKER, EVENT, Collections.singletonList(CREATIVE_VIEW))) {
                arrayList.add(new VastAbsoluteProgressTracker(XmlUtils.getNodeValue(node2), 0));
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getVideoCompleteTrackers() {
        return getVideoTrackersByAttributeAsVastTrackers(COMPLETE);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getPauseTrackers() {
        List<String> videoTrackersByAttribute = getVideoTrackersByAttribute(PAUSE);
        ArrayList arrayList = new ArrayList();
        for (String vastTracker : videoTrackersByAttribute) {
            arrayList.add(new VastTracker(vastTracker, true));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getResumeTrackers() {
        List<String> videoTrackersByAttribute = getVideoTrackersByAttribute(RESUME);
        ArrayList arrayList = new ArrayList();
        for (String vastTracker : videoTrackersByAttribute) {
            arrayList.add(new VastTracker(vastTracker, true));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getVideoCloseTrackers() {
        List videoTrackersByAttributeAsVastTrackers = getVideoTrackersByAttributeAsVastTrackers("close");
        videoTrackersByAttributeAsVastTrackers.addAll(getVideoTrackersByAttributeAsVastTrackers(CLOSE_LINEAR));
        return videoTrackersByAttributeAsVastTrackers;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getVideoSkipTrackers() {
        return getVideoTrackersByAttributeAsVastTrackers(SKIP);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getClickThroughUrl() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, VIDEO_CLICKS);
        if (firstMatchingChildNode == null) {
            return null;
        }
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(firstMatchingChildNode, CLICK_THROUGH));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getClickTrackers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, VIDEO_CLICKS);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, CLICK_TRACKER)) {
            String nodeValue = XmlUtils.getNodeValue(firstMatchingChildNode2);
            if (nodeValue != null) {
                arrayList.add(new VastTracker(nodeValue));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getSkipOffset() {
        String attributeValue = XmlUtils.getAttributeValue(this.mLinearNode, SKIP_OFFSET);
        if (attributeValue == null || attributeValue.trim().isEmpty()) {
            return null;
        }
        return attributeValue.trim();
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastMediaXmlManager> getMediaXmlManagers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, MEDIA_FILES);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, MEDIA_FILE)) {
            arrayList.add(new VastMediaXmlManager(firstMatchingChildNode2));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastIconXmlManager> getIconXmlManagers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, ICONS);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, ICON)) {
            arrayList.add(new VastIconXmlManager(firstMatchingChildNode2));
        }
        return arrayList;
    }

    @NonNull
    private List<VastTracker> getVideoTrackersByAttributeAsVastTrackers(@NonNull String attributeValue) {
        List<String> videoTrackersByAttribute = getVideoTrackersByAttribute(attributeValue);
        ArrayList arrayList = new ArrayList(videoTrackersByAttribute.size());
        for (String vastTracker : videoTrackersByAttribute) {
            arrayList.add(new VastTracker(vastTracker));
        }
        return arrayList;
    }

    @NonNull
    private List<String> getVideoTrackersByAttribute(@NonNull String attributeValue) {
        Preconditions.checkNotNull(attributeValue);
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mLinearNode, TRACKING_EVENTS);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, VIDEO_TRACKER, EVENT, Collections.singletonList(attributeValue))) {
            String nodeValue = XmlUtils.getNodeValue(firstMatchingChildNode2);
            if (nodeValue != null) {
                arrayList.add(nodeValue);
            }
        }
        return arrayList;
    }

    private void addQuartileTrackerWithFraction(@NonNull List<VastFractionalProgressTracker> trackers, @NonNull List<String> urls, float fraction) {
        Preconditions.checkNotNull(trackers, "trackers cannot be null");
        Preconditions.checkNotNull(urls, "urls cannot be null");
        for (String vastFractionalProgressTracker : urls) {
            trackers.add(new VastFractionalProgressTracker(vastFractionalProgressTracker, fraction));
        }
    }
}
