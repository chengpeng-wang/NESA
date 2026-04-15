package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import org.w3c.dom.Node;

public class VastResourceXmlManager {
    public static final String CREATIVE_TYPE = "creativeType";
    public static final String HTML_RESOURCE = "HTMLResource";
    public static final String IFRAME_RESOURCE = "IFrameResource";
    public static final String STATIC_RESOURCE = "StaticResource";
    @NonNull
    private final Node mResourceNode;

    VastResourceXmlManager(@NonNull Node resourceNode) {
        Preconditions.checkNotNull(resourceNode);
        this.mResourceNode = resourceNode;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getStaticResource() {
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mResourceNode, STATIC_RESOURCE));
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getStaticResourceType() {
        String attributeValue = XmlUtils.getAttributeValue(XmlUtils.getFirstMatchingChildNode(this.mResourceNode, STATIC_RESOURCE), CREATIVE_TYPE);
        if (attributeValue != null) {
            return attributeValue.toLowerCase();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getIFrameResource() {
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mResourceNode, IFRAME_RESOURCE));
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getHTMLResource() {
        return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mResourceNode, HTML_RESOURCE));
    }
}
