package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import org.w3c.dom.Node;

class VastAdXmlManager {
    private static final String INLINE = "InLine";
    private static final String SEQUENCE = "sequence";
    private static final String WRAPPER = "Wrapper";
    @NonNull
    private final Node mAdNode;

    VastAdXmlManager(@NonNull Node adNode) {
        Preconditions.checkNotNull(adNode);
        this.mAdNode = adNode;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public VastInLineXmlManager getInLineXmlManager() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mAdNode, INLINE);
        if (firstMatchingChildNode != null) {
            return new VastInLineXmlManager(firstMatchingChildNode);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public VastWrapperXmlManager getWrapperXmlManager() {
        Node firstMatchingChildNode = XmlUtils.getFirstMatchingChildNode(this.mAdNode, WRAPPER);
        if (firstMatchingChildNode != null) {
            return new VastWrapperXmlManager(firstMatchingChildNode);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getSequence() {
        return XmlUtils.getAttributeValue(this.mAdNode, SEQUENCE);
    }
}
