package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import org.w3c.dom.Node;

class VastMediaXmlManager {
    private static final String DELIVERY = "delivery";
    private static final String HEIGHT = "height";
    private static final String VIDEO_TYPE = "type";
    private static final String WIDTH = "width";
    @NonNull
    private final Node mMediaNode;

    VastMediaXmlManager(@NonNull Node mediaNode) {
        Preconditions.checkNotNull(mediaNode, "mediaNode cannot be null");
        this.mMediaNode = mediaNode;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getDelivery() {
        return XmlUtils.getAttributeValue(this.mMediaNode, DELIVERY);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getWidth() {
        return XmlUtils.getAttributeValueAsInt(this.mMediaNode, "width");
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Integer getHeight() {
        return XmlUtils.getAttributeValueAsInt(this.mMediaNode, "height");
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getType() {
        return XmlUtils.getAttributeValue(this.mMediaNode, "type");
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getMediaUrl() {
        return XmlUtils.getNodeValue(this.mMediaNode);
    }
}
