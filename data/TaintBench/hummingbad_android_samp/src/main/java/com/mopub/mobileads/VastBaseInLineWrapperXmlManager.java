package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

abstract class VastBaseInLineWrapperXmlManager {
    private static final String COMPANION = "Companion";
    private static final String COMPANION_ADS = "CompanionAds";
    private static final String CREATIVE = "Creative";
    private static final String CREATIVES = "Creatives";
    private static final String ERROR = "Error";
    private static final String EXTENSIONS = "Extensions";
    private static final String IMPRESSION_TRACKER = "Impression";
    private static final String LINEAR = "Linear";
    @NonNull
    protected final Node mNode;

    VastBaseInLineWrapperXmlManager(@NonNull Node node) {
        Preconditions.checkNotNull(node);
        this.mNode = node;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getImpressionTrackers() {
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(this.mNode, IMPRESSION_TRACKER);
        ArrayList arrayList = new ArrayList();
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
    public List<VastTracker> getErrorTrackers() {
        ArrayList arrayList = new ArrayList();
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(this.mNode, ERROR);
        if (matchingChildNodes == null) {
            return arrayList;
        }
        for (Node nodeValue : matchingChildNodes) {
            String nodeValue2 = XmlUtils.getNodeValue(nodeValue);
            if (!TextUtils.isEmpty(nodeValue2)) {
                arrayList.add(new VastTracker(nodeValue2, true));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastLinearXmlManager> getLinearXmlManagers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mNode, CREATIVES);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(firstMatchingChildNode, CREATIVE);
        if (matchingChildNodes == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : matchingChildNodes) {
            firstMatchingChildNode2 = XmlUtils.getFirstMatchingChildNode(firstMatchingChildNode2, LINEAR);
            if (firstMatchingChildNode2 != null) {
                arrayList.add(new VastLinearXmlManager(firstMatchingChildNode2));
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastCompanionAdXmlManager> getCompanionAdXmlManagers() {
        ArrayList arrayList = new ArrayList();
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mNode, CREATIVES);
        if (firstMatchingChildNode == null) {
            return arrayList;
        }
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(firstMatchingChildNode, CREATIVE);
        if (matchingChildNodes == null) {
            return arrayList;
        }
        for (Node firstMatchingChildNode2 : matchingChildNodes) {
            firstMatchingChildNode2 = XmlUtils.getFirstMatchingChildNode(firstMatchingChildNode2, COMPANION_ADS);
            if (firstMatchingChildNode2 != null) {
                matchingChildNodes = XmlUtils.getMatchingChildNodes(firstMatchingChildNode2, COMPANION);
                if (matchingChildNodes != null) {
                    for (Node firstMatchingChildNode22 : matchingChildNodes) {
                        arrayList.add(new VastCompanionAdXmlManager(firstMatchingChildNode22));
                    }
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public VastExtensionParentXmlManager getVastExtensionParentXmlManager() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mNode, EXTENSIONS);
        if (firstMatchingChildNode == null) {
            return null;
        }
        return new VastExtensionParentXmlManager(firstMatchingChildNode);
    }
}
