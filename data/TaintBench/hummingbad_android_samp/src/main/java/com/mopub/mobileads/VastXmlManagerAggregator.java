package com.mopub.mobileads;

import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.network.Networking;
import com.mopub.network.TrackingRequest;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VastXmlManagerAggregator extends AsyncTask<String, Void, VastVideoConfig> {
    private static final double AREA_WEIGHT = 30.0d;
    private static final double ASPECT_RATIO_WEIGHT = 70.0d;
    static final int MAX_TIMES_TO_FOLLOW_VAST_REDIRECT = 10;
    private static final int MINIMUM_COMPANION_AD_HEIGHT = 250;
    private static final int MINIMUM_COMPANION_AD_WIDTH = 300;
    private static final String MOPUB = "MoPub";
    private static final List<String> VIDEO_MIME_TYPES = Arrays.asList(new String[]{"video/mp4", "video/3gpp"});
    @NonNull
    private final Context mContext;
    private final int mScreenAreaDp;
    private final double mScreenAspectRatio;
    private int mTimesFollowedVastRedirect;
    @NonNull
    private final WeakReference<VastXmlManagerAggregatorListener> mVastXmlManagerAggregatorListener;

    enum CompanionOrientation {
        LANDSCAPE,
        PORTRAIT
    }

    interface VastXmlManagerAggregatorListener {
        void onAggregationComplete(@Nullable VastVideoConfig vastVideoConfig);
    }

    VastXmlManagerAggregator(@NonNull VastXmlManagerAggregatorListener vastXmlManagerAggregatorListener, double screenAspectRatio, int screenAreaDp, @NonNull Context context) {
        Preconditions.checkNotNull(vastXmlManagerAggregatorListener);
        Preconditions.checkNotNull(context);
        this.mVastXmlManagerAggregatorListener = new WeakReference(vastXmlManagerAggregatorListener);
        this.mScreenAspectRatio = screenAspectRatio;
        this.mScreenAreaDp = screenAreaDp;
        this.mContext = context.getApplicationContext();
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        Networking.getUserAgent(this.mContext);
    }

    /* access modifiers changed from: protected|varargs */
    public VastVideoConfig doInBackground(@Nullable String... strings) {
        VastVideoConfig vastVideoConfig = null;
        if (strings == null || strings.length == 0 || strings[0] == null) {
            return vastVideoConfig;
        }
        try {
            return evaluateVastXmlManager(strings[0], new ArrayList());
        } catch (Exception e) {
            MoPubLog.d("Unable to generate VastVideoConfig.", e);
            return vastVideoConfig;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(@Nullable VastVideoConfig vastVideoConfig) {
        VastXmlManagerAggregatorListener vastXmlManagerAggregatorListener = (VastXmlManagerAggregatorListener) this.mVastXmlManagerAggregatorListener.get();
        if (vastXmlManagerAggregatorListener != null) {
            vastXmlManagerAggregatorListener.onAggregationComplete(vastVideoConfig);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        VastXmlManagerAggregatorListener vastXmlManagerAggregatorListener = (VastXmlManagerAggregatorListener) this.mVastXmlManagerAggregatorListener.get();
        if (vastXmlManagerAggregatorListener != null) {
            vastXmlManagerAggregatorListener.onAggregationComplete(null);
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public VastVideoConfig evaluateVastXmlManager(@NonNull String vastXml, @NonNull List<VastTracker> errorTrackers) {
        Preconditions.checkNotNull(vastXml, "vastXml cannot be null");
        Preconditions.checkNotNull(errorTrackers, "errorTrackers cannot be null");
        VastXmlManager vastXmlManager = new VastXmlManager();
        try {
            vastXmlManager.parseVastXml(vastXml);
            List<VastAdXmlManager> adXmlManagers = vastXmlManager.getAdXmlManagers();
            if (fireErrorTrackerIfNoAds(adXmlManagers, vastXmlManager, this.mContext)) {
                return null;
            }
            for (VastAdXmlManager vastAdXmlManager : adXmlManagers) {
                if (isValidSequenceNumber(vastAdXmlManager.getSequence())) {
                    VastVideoConfig evaluateInLineXmlManager;
                    VastInLineXmlManager inLineXmlManager = vastAdXmlManager.getInLineXmlManager();
                    if (inLineXmlManager != null) {
                        evaluateInLineXmlManager = evaluateInLineXmlManager(inLineXmlManager, errorTrackers);
                        if (evaluateInLineXmlManager != null) {
                            populateMoPubCustomElements(vastXmlManager, evaluateInLineXmlManager);
                            return evaluateInLineXmlManager;
                        }
                    }
                    VastWrapperXmlManager wrapperXmlManager = vastAdXmlManager.getWrapperXmlManager();
                    if (wrapperXmlManager != null) {
                        ArrayList arrayList = new ArrayList(errorTrackers);
                        arrayList.addAll(wrapperXmlManager.getErrorTrackers());
                        String evaluateWrapperRedirect = evaluateWrapperRedirect(wrapperXmlManager, arrayList);
                        if (evaluateWrapperRedirect != null) {
                            evaluateInLineXmlManager = evaluateVastXmlManager(evaluateWrapperRedirect, arrayList);
                            if (evaluateInLineXmlManager != null) {
                                evaluateInLineXmlManager.addImpressionTrackers(wrapperXmlManager.getImpressionTrackers());
                                for (VastLinearXmlManager populateLinearTrackersAndIcon : wrapperXmlManager.getLinearXmlManagers()) {
                                    populateLinearTrackersAndIcon(populateLinearTrackersAndIcon, evaluateInLineXmlManager);
                                }
                                populateVideoViewabilityTracker(wrapperXmlManager, evaluateInLineXmlManager);
                                if (evaluateInLineXmlManager.hasCompanionAd()) {
                                    VastCompanionAdConfig vastCompanionAd = evaluateInLineXmlManager.getVastCompanionAd(2);
                                    VastCompanionAdConfig vastCompanionAd2 = evaluateInLineXmlManager.getVastCompanionAd(1);
                                    if (!(vastCompanionAd == null || vastCompanionAd2 == null)) {
                                        for (VastCompanionAdXmlManager vastCompanionAdXmlManager : wrapperXmlManager.getCompanionAdXmlManagers()) {
                                            if (!vastCompanionAdXmlManager.hasResources()) {
                                                vastCompanionAd.addClickTrackers(vastCompanionAdXmlManager.getClickTrackers());
                                                vastCompanionAd.addCreativeViewTrackers(vastCompanionAdXmlManager.getCompanionCreativeViewTrackers());
                                                vastCompanionAd2.addClickTrackers(vastCompanionAdXmlManager.getClickTrackers());
                                                vastCompanionAd2.addCreativeViewTrackers(vastCompanionAdXmlManager.getCompanionCreativeViewTrackers());
                                            }
                                        }
                                    }
                                } else {
                                    evaluateInLineXmlManager.setVastCompanionAd(getBestCompanionAd(wrapperXmlManager.getCompanionAdXmlManagers(), CompanionOrientation.LANDSCAPE), getBestCompanionAd(wrapperXmlManager.getCompanionAdXmlManagers(), CompanionOrientation.PORTRAIT));
                                }
                                populateMoPubCustomElements(vastXmlManager, evaluateInLineXmlManager);
                                return evaluateInLineXmlManager;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            MoPubLog.d("Failed to parse VAST XML", e);
            TrackingRequest.makeVastTrackingHttpRequest(errorTrackers, VastErrorCode.XML_PARSING_ERROR, null, null, this.mContext);
            return null;
        }
    }

    @Nullable
    private VastVideoConfig evaluateInLineXmlManager(@NonNull VastInLineXmlManager vastInLineXmlManager, @NonNull List<VastTracker> errorTrackers) {
        Preconditions.checkNotNull(vastInLineXmlManager);
        Preconditions.checkNotNull(errorTrackers);
        for (VastLinearXmlManager vastLinearXmlManager : vastInLineXmlManager.getLinearXmlManagers()) {
            String bestMediaFileUrl = getBestMediaFileUrl(vastLinearXmlManager.getMediaXmlManagers());
            if (bestMediaFileUrl != null) {
                VastVideoConfig vastVideoConfig = new VastVideoConfig();
                vastVideoConfig.addImpressionTrackers(vastInLineXmlManager.getImpressionTrackers());
                populateLinearTrackersAndIcon(vastLinearXmlManager, vastVideoConfig);
                vastVideoConfig.setClickThroughUrl(vastLinearXmlManager.getClickThroughUrl());
                vastVideoConfig.setNetworkMediaFileUrl(bestMediaFileUrl);
                vastVideoConfig.setVastCompanionAd(getBestCompanionAd(vastInLineXmlManager.getCompanionAdXmlManagers(), CompanionOrientation.LANDSCAPE), getBestCompanionAd(vastInLineXmlManager.getCompanionAdXmlManagers(), CompanionOrientation.PORTRAIT));
                errorTrackers.addAll(vastInLineXmlManager.getErrorTrackers());
                vastVideoConfig.addErrorTrackers(errorTrackers);
                populateVideoViewabilityTracker(vastInLineXmlManager, vastVideoConfig);
                return vastVideoConfig;
            }
        }
        return null;
    }

    private void populateVideoViewabilityTracker(@NonNull VastBaseInLineWrapperXmlManager vastInLineXmlManager, @NonNull VastVideoConfig vastVideoConfig) {
        Preconditions.checkNotNull(vastInLineXmlManager);
        Preconditions.checkNotNull(vastVideoConfig);
        if (vastVideoConfig.getVideoViewabilityTracker() == null) {
            VastExtensionParentXmlManager vastExtensionParentXmlManager = vastInLineXmlManager.getVastExtensionParentXmlManager();
            if (vastExtensionParentXmlManager != null) {
                for (VastExtensionXmlManager vastExtensionXmlManager : vastExtensionParentXmlManager.getVastExtensionXmlManagers()) {
                    if (MOPUB.equals(vastExtensionXmlManager.getType())) {
                        vastVideoConfig.setVideoViewabilityTracker(vastExtensionXmlManager.getVideoViewabilityTracker());
                        return;
                    }
                }
            }
        }
    }

    @Nullable
    private String evaluateWrapperRedirect(@NonNull VastWrapperXmlManager vastWrapperXmlManager, @NonNull List<VastTracker> wrapperErrorTrackers) {
        String str = null;
        String vastAdTagURI = vastWrapperXmlManager.getVastAdTagURI();
        if (vastAdTagURI == null) {
            return str;
        }
        try {
            return followVastRedirect(vastAdTagURI);
        } catch (Exception e) {
            MoPubLog.d("Failed to follow VAST redirect", e);
            if (wrapperErrorTrackers.isEmpty()) {
                return str;
            }
            TrackingRequest.makeVastTrackingHttpRequest(wrapperErrorTrackers, VastErrorCode.WRAPPER_TIMEOUT, str, str, this.mContext);
            return str;
        }
    }

    private void populateLinearTrackersAndIcon(@NonNull VastLinearXmlManager linearXmlManager, @NonNull VastVideoConfig vastVideoConfig) {
        Preconditions.checkNotNull(linearXmlManager, "linearXmlManager cannot be null");
        Preconditions.checkNotNull(vastVideoConfig, "vastVideoConfig cannot be null");
        vastVideoConfig.addAbsoluteTrackers(linearXmlManager.getAbsoluteProgressTrackers());
        vastVideoConfig.addFractionalTrackers(linearXmlManager.getFractionalProgressTrackers());
        vastVideoConfig.addPauseTrackers(linearXmlManager.getPauseTrackers());
        vastVideoConfig.addResumeTrackers(linearXmlManager.getResumeTrackers());
        vastVideoConfig.addCompleteTrackers(linearXmlManager.getVideoCompleteTrackers());
        vastVideoConfig.addCloseTrackers(linearXmlManager.getVideoCloseTrackers());
        vastVideoConfig.addSkipTrackers(linearXmlManager.getVideoSkipTrackers());
        vastVideoConfig.addClickTrackers(linearXmlManager.getClickTrackers());
        if (vastVideoConfig.getSkipOffsetString() == null) {
            vastVideoConfig.setSkipOffset(linearXmlManager.getSkipOffset());
        }
        if (vastVideoConfig.getVastIconConfig() == null) {
            vastVideoConfig.setVastIconConfig(getBestIcon(linearXmlManager.getIconXmlManagers()));
        }
    }

    private void populateMoPubCustomElements(@NonNull VastXmlManager xmlManager, @NonNull VastVideoConfig vastVideoConfig) {
        Preconditions.checkNotNull(xmlManager, "xmlManager cannot be null");
        Preconditions.checkNotNull(vastVideoConfig, "vastVideoConfig cannot be null");
        vastVideoConfig.addImpressionTrackers(xmlManager.getMoPubImpressionTrackers());
        if (vastVideoConfig.getCustomCtaText() == null) {
            vastVideoConfig.setCustomCtaText(xmlManager.getCustomCtaText());
        }
        if (vastVideoConfig.getCustomSkipText() == null) {
            vastVideoConfig.setCustomSkipText(xmlManager.getCustomSkipText());
        }
        if (vastVideoConfig.getCustomCloseIconUrl() == null) {
            vastVideoConfig.setCustomCloseIconUrl(xmlManager.getCustomCloseIconUrl());
        }
        if (!vastVideoConfig.isCustomForceOrientationSet()) {
            vastVideoConfig.setCustomForceOrientation(xmlManager.getCustomForceOrientation());
        }
    }

    private boolean fireErrorTrackerIfNoAds(@NonNull List<VastAdXmlManager> vastAdXmlManagers, @NonNull VastXmlManager xmlManager, @NonNull Context context) {
        if (!vastAdXmlManagers.isEmpty() || xmlManager.getErrorTracker() == null) {
            return false;
        }
        TrackingRequest.makeVastTrackingHttpRequest(Collections.singletonList(xmlManager.getErrorTracker()), this.mTimesFollowedVastRedirect > 0 ? VastErrorCode.NO_ADS_VAST_RESPONSE : VastErrorCode.UNDEFINED_ERROR, null, null, context);
        return true;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public String getBestMediaFileUrl(@NonNull List<VastMediaXmlManager> managers) {
        Preconditions.checkNotNull(managers, "managers cannot be null");
        double d = Double.POSITIVE_INFINITY;
        String str = null;
        Iterator it = new ArrayList(managers).iterator();
        while (it.hasNext()) {
            VastMediaXmlManager vastMediaXmlManager = (VastMediaXmlManager) it.next();
            String type = vastMediaXmlManager.getType();
            String mediaUrl = vastMediaXmlManager.getMediaUrl();
            if (!VIDEO_MIME_TYPES.contains(type) || mediaUrl == null) {
                it.remove();
            } else {
                Integer width = vastMediaXmlManager.getWidth();
                Integer height = vastMediaXmlManager.getHeight();
                if (width != null && width.intValue() > 0 && height != null && height.intValue() > 0) {
                    String str2;
                    double d2;
                    double calculateFitness = calculateFitness(width.intValue(), height.intValue());
                    if (calculateFitness < d) {
                        str2 = mediaUrl;
                        d2 = calculateFitness;
                    } else {
                        str2 = str;
                        d2 = d;
                    }
                    d = d2;
                    str = str2;
                }
            }
        }
        return str;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public VastCompanionAdConfig getBestCompanionAd(@NonNull List<VastCompanionAdXmlManager> managers, @NonNull CompanionOrientation orientation) {
        VastResource fromVastResourceXmlManager;
        Preconditions.checkNotNull(managers, "managers cannot be null");
        Preconditions.checkNotNull(orientation, "orientation cannot be null");
        ArrayList<VastCompanionAdXmlManager> arrayList = new ArrayList(managers);
        double d = Double.POSITIVE_INFINITY;
        VastCompanionAdXmlManager vastCompanionAdXmlManager = null;
        VastResource vastResource = null;
        Point point = null;
        Type[] values = Type.values();
        int length = values.length;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                break;
            }
            Type type = values[i2];
            for (VastCompanionAdXmlManager vastCompanionAdXmlManager2 : arrayList) {
                Integer width = vastCompanionAdXmlManager2.getWidth();
                Integer height = vastCompanionAdXmlManager2.getHeight();
                if (width != null && width.intValue() >= 300 && height != null && height.intValue() >= 250) {
                    Point scaledDimensions = getScaledDimensions(width.intValue(), height.intValue());
                    fromVastResourceXmlManager = VastResource.fromVastResourceXmlManager(vastCompanionAdXmlManager2.getResourceXmlManager(), type, scaledDimensions.x, scaledDimensions.y);
                    if (fromVastResourceXmlManager != null) {
                        double calculateFitness;
                        VastCompanionAdXmlManager vastCompanionAdXmlManager3;
                        Point point2;
                        VastResource vastResource2;
                        double d2;
                        if (CompanionOrientation.PORTRAIT == orientation) {
                            calculateFitness = calculateFitness(height.intValue(), width.intValue());
                        } else {
                            calculateFitness = calculateFitness(width.intValue(), height.intValue());
                        }
                        if (calculateFitness < d) {
                            vastCompanionAdXmlManager3 = vastCompanionAdXmlManager2;
                            point2 = scaledDimensions;
                            vastResource2 = fromVastResourceXmlManager;
                            d2 = calculateFitness;
                        } else {
                            point2 = point;
                            vastResource2 = vastResource;
                            vastCompanionAdXmlManager3 = vastCompanionAdXmlManager;
                            d2 = d;
                        }
                        vastCompanionAdXmlManager = vastCompanionAdXmlManager3;
                        d = d2;
                        point = point2;
                        vastResource = vastResource2;
                    }
                }
            }
            if (vastCompanionAdXmlManager != null) {
                break;
            }
            i = i2 + 1;
        }
        fromVastResourceXmlManager = vastResource;
        VastCompanionAdXmlManager vastCompanionAdXmlManager4 = vastCompanionAdXmlManager;
        if (vastCompanionAdXmlManager4 != null) {
            return new VastCompanionAdConfig(point.x, point.y, fromVastResourceXmlManager, vastCompanionAdXmlManager4.getClickThroughUrl(), vastCompanionAdXmlManager4.getClickTrackers(), vastCompanionAdXmlManager4.getCompanionCreativeViewTrackers());
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @VisibleForTesting
    public Point getScaledDimensions(int widthDp, int heightDp) {
        Point point = new Point(widthDp, heightDp);
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        int max = Math.max(width, height);
        width = Math.min(width, height);
        int dipsToIntPixels = Dips.dipsToIntPixels((float) widthDp, this.mContext);
        int dipsToIntPixels2 = Dips.dipsToIntPixels((float) heightDp, this.mContext);
        if (dipsToIntPixels <= max && dipsToIntPixels2 <= width) {
            return point;
        }
        float f = ((float) dipsToIntPixels) / ((float) max);
        float f2 = ((float) dipsToIntPixels2) / ((float) width);
        Point point2 = new Point();
        if (f >= f2) {
            point2.x = max - 16;
            point2.y = ((int) (((float) dipsToIntPixels2) / f)) - 16;
        } else {
            point2.x = ((int) (((float) dipsToIntPixels) / f2)) - 16;
            point2.y = width - 16;
        }
        if (point2.x < 0 || point2.y < 0) {
            return point;
        }
        point2.x = Dips.pixelsToIntDips((float) point2.x, this.mContext);
        point2.y = Dips.pixelsToIntDips((float) point2.y, this.mContext);
        return point2;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public VastIconConfig getBestIcon(@NonNull List<VastIconXmlManager> managers) {
        Preconditions.checkNotNull(managers, "managers cannot be null");
        ArrayList<VastIconXmlManager> arrayList = new ArrayList(managers);
        for (Type type : Type.values()) {
            for (VastIconXmlManager vastIconXmlManager : arrayList) {
                Integer width = vastIconXmlManager.getWidth();
                Integer height = vastIconXmlManager.getHeight();
                if (width != null && width.intValue() > 0 && width.intValue() <= 300 && height != null && height.intValue() > 0 && height.intValue() <= 300) {
                    VastResource fromVastResourceXmlManager = VastResource.fromVastResourceXmlManager(vastIconXmlManager.getResourceXmlManager(), type, width.intValue(), height.intValue());
                    if (fromVastResourceXmlManager != null) {
                        return new VastIconConfig(vastIconXmlManager.getWidth().intValue(), vastIconXmlManager.getHeight().intValue(), vastIconXmlManager.getOffsetMS(), vastIconXmlManager.getDurationMS(), fromVastResourceXmlManager, vastIconXmlManager.getClickTrackingUris(), vastIconXmlManager.getClickThroughUri(), vastIconXmlManager.getViewTrackingUris());
                    }
                }
            }
        }
        return null;
    }

    private double calculateFitness(int widthDp, int heightDp) {
        return (Math.abs(Math.log((((double) widthDp) / ((double) heightDp)) / this.mScreenAspectRatio)) * ASPECT_RATIO_WEIGHT) + (Math.abs(Math.log(((double) (widthDp * heightDp)) / ((double) this.mScreenAreaDp))) * AREA_WEIGHT);
    }

    static boolean isValidSequenceNumber(@Nullable String sequence) {
        if (TextUtils.isEmpty(sequence)) {
            return true;
        }
        try {
            if (Integer.parseInt(sequence) >= 2) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0034  */
    @android.support.annotation.Nullable
    private java.lang.String followVastRedirect(@android.support.annotation.NonNull java.lang.String r6) throws java.io.IOException {
        /*
        r5 = this;
        r0 = 0;
        com.mopub.common.Preconditions.checkNotNull(r6);
        r1 = r5.mTimesFollowedVastRedirect;
        r2 = 10;
        if (r1 >= r2) goto L_0x0029;
    L_0x000a:
        r1 = r5.mTimesFollowedVastRedirect;
        r1 = r1 + 1;
        r5.mTimesFollowedVastRedirect = r1;
        r2 = com.mopub.common.MoPubHttpUrlConnection.getHttpUrlConnection(r6);	 Catch:{ all -> 0x002a }
        r1 = new java.io.BufferedInputStream;	 Catch:{ all -> 0x0038 }
        r3 = r2.getInputStream();	 Catch:{ all -> 0x0038 }
        r1.<init>(r3);	 Catch:{ all -> 0x0038 }
        r0 = com.mopub.common.util.Strings.fromStream(r1);	 Catch:{ all -> 0x003d }
        com.mopub.common.util.Streams.closeStream(r1);
        if (r2 == 0) goto L_0x0029;
    L_0x0026:
        r2.disconnect();
    L_0x0029:
        return r0;
    L_0x002a:
        r1 = move-exception;
        r2 = r0;
        r4 = r0;
        r0 = r1;
        r1 = r4;
    L_0x002f:
        com.mopub.common.util.Streams.closeStream(r1);
        if (r2 == 0) goto L_0x0037;
    L_0x0034:
        r2.disconnect();
    L_0x0037:
        throw r0;
    L_0x0038:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
        goto L_0x002f;
    L_0x003d:
        r0 = move-exception;
        goto L_0x002f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.VastXmlManagerAggregator.followVastRedirect(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setTimesFollowedVastRedirect(int timesFollowedVastRedirect) {
        this.mTimesFollowedVastRedirect = timesFollowedVastRedirect;
    }
}
