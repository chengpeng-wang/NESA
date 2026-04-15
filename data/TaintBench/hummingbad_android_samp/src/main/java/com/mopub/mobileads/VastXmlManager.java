package com.mopub.mobileads;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import com.mopub.common.util.DeviceUtils.ForceOrientation;
import com.mopub.mobileads.util.XmlUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class VastXmlManager {
    private static final String AD = "Ad";
    private static final String CUSTOM_CLOSE_ICON = "MoPubCloseIcon";
    private static final String CUSTOM_CTA_TEXT = "MoPubCtaText";
    private static final String CUSTOM_FORCE_ORIENTATION = "MoPubForceOrientation";
    private static final String CUSTOM_SKIP_TEXT = "MoPubSkipText";
    private static final String ERROR = "Error";
    private static final int MAX_CTA_TEXT_LENGTH = 15;
    private static final int MAX_SKIP_TEXT_LENGTH = 8;
    private static final String MP_IMPRESSION_TRACKER = "MP_TRACKING_URL";
    private static final String ROOT_TAG = "MPMoVideoXMLDocRoot";
    private static final String ROOT_TAG_CLOSE = "</MPMoVideoXMLDocRoot>";
    private static final String ROOT_TAG_OPEN = "<MPMoVideoXMLDocRoot>";
    @Nullable
    private Document mVastDoc;

    VastXmlManager() {
    }

    /* access modifiers changed from: 0000 */
    public void parseVastXml(@NonNull String xmlString) throws ParserConfigurationException, IOException, SAXException {
        Preconditions.checkNotNull(xmlString, "xmlString cannot be null");
        String stringBuilder = new StringBuilder(ROOT_TAG_OPEN).append(xmlString.replaceFirst("<\\?.*\\?>", "")).append(ROOT_TAG_CLOSE).toString();
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setCoalescing(true);
        this.mVastDoc = newInstance.newDocumentBuilder().parse(new InputSource(new StringReader(stringBuilder)));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastAdXmlManager> getAdXmlManagers() {
        ArrayList arrayList = new ArrayList();
        if (this.mVastDoc == null) {
            return arrayList;
        }
        NodeList elementsByTagName = this.mVastDoc.getElementsByTagName(AD);
        for (int i = 0; i < elementsByTagName.getLength(); i++) {
            arrayList.add(new VastAdXmlManager(elementsByTagName.item(i)));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public VastTracker getErrorTracker() {
        if (this.mVastDoc == null) {
            return null;
        }
        String firstMatchingStringData = XmlUtils.getFirstMatchingStringData(this.mVastDoc, ERROR);
        if (TextUtils.isEmpty(firstMatchingStringData)) {
            return null;
        }
        return new VastTracker(firstMatchingStringData);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List<VastTracker> getMoPubImpressionTrackers() {
        List<String> stringDataAsList = XmlUtils.getStringDataAsList(this.mVastDoc, MP_IMPRESSION_TRACKER);
        ArrayList arrayList = new ArrayList(stringDataAsList.size());
        for (String vastTracker : stringDataAsList) {
            arrayList.add(new VastTracker(vastTracker));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getCustomCtaText() {
        String firstMatchingStringData = XmlUtils.getFirstMatchingStringData(this.mVastDoc, CUSTOM_CTA_TEXT);
        return (firstMatchingStringData == null || firstMatchingStringData.length() > 15) ? null : firstMatchingStringData;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getCustomSkipText() {
        String firstMatchingStringData = XmlUtils.getFirstMatchingStringData(this.mVastDoc, CUSTOM_SKIP_TEXT);
        return (firstMatchingStringData == null || firstMatchingStringData.length() > 8) ? null : firstMatchingStringData;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getCustomCloseIconUrl() {
        return XmlUtils.getFirstMatchingStringData(this.mVastDoc, CUSTOM_CLOSE_ICON);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public ForceOrientation getCustomForceOrientation() {
        return ForceOrientation.getForceOrientation(XmlUtils.getFirstMatchingStringData(this.mVastDoc, CUSTOM_FORCE_ORIENTATION));
    }
}
