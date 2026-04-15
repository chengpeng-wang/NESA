package com.mopub.mobileads;

import android.support.annotation.NonNull;
import com.mopub.common.Preconditions;
import com.mopub.mobileads.util.XmlUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

public class VastExtensionParentXmlManager {
    private static final String EXTENSION = "Extension";
    @NonNull
    private final Node mVastExtensionParentNode;

    VastExtensionParentXmlManager(@NonNull Node vastExtensionParentNode) {
        Preconditions.checkNotNull(vastExtensionParentNode);
        this.mVastExtensionParentNode = vastExtensionParentNode;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastExtensionXmlManager> getVastExtensionXmlManagers() {
        ArrayList arrayList = new ArrayList();
        List<Node> matchingChildNodes = XmlUtils.getMatchingChildNodes(this.mVastExtensionParentNode, EXTENSION);
        if (matchingChildNodes == null) {
            return arrayList;
        }
        for (Node vastExtensionXmlManager : matchingChildNodes) {
            arrayList.add(new VastExtensionXmlManager(vastExtensionXmlManager));
        }
        return arrayList;
    }
}
