package com.cleanmaster.model;

import java.io.Serializable;
import java.util.List;

public class VastModel implements Serializable {
    private String adTitle;
    private String behindImgUrl;
    private String clickThrough;
    private List clickTrackings;
    private List closeReportUrls;
    private List completeReportUrls;
    private List createViewReportUrls;
    private String delivery;
    private String errorReportUrl;
    private List exitFullScreenReportUrls;
    private List firstQuartileReportUrls;
    private String frontImgUrl;
    private List fullScreenReportUrls;
    private String iconUrl;
    private int id;
    private List impressionReportUrls;
    private List midpointReportUrls;
    private List muteReportUrls;
    private List pauseReportUrls;
    private List resumeReportUrls;
    private List rewindReportUrls;
    private List startReportUrls;
    private List thirdQuartileReportUrls;
    private List unmuteReportUrls;
    private String videoFilePath;
    private int videoHeight;
    private String videoType;
    private String videoUrl;
    private int videoWidth;

    public VastModel(int id, String adTitle, String errorReportUrl, List impressionReportUrls, List createViewReportUrls, List startReportUrls, List firstQuartileReportUrls, List midpointReportUrls, List thirdQuartileReportUrls, List completeReportUrls, List closeReportUrls, List pauseReportUrls, List muteReportUrls, List unmuteReportUrls, List rewindReportUrls, List resumeReportUrls, List fullScreenReportUrls, List exitFullScreenReportUrls, String clickThrough, List clickTrackings, String delivery, int videoWidth, int videoHeight, String videoUrl, String videoType, String frontImgUrl, String behindImgUrl, String iconUrl, String videoFilePath) {
        this.id = id;
        this.adTitle = adTitle;
        this.errorReportUrl = errorReportUrl;
        this.impressionReportUrls = impressionReportUrls;
        this.createViewReportUrls = createViewReportUrls;
        this.startReportUrls = startReportUrls;
        this.firstQuartileReportUrls = firstQuartileReportUrls;
        this.midpointReportUrls = midpointReportUrls;
        this.thirdQuartileReportUrls = thirdQuartileReportUrls;
        this.completeReportUrls = completeReportUrls;
        this.closeReportUrls = closeReportUrls;
        this.pauseReportUrls = pauseReportUrls;
        this.muteReportUrls = muteReportUrls;
        this.unmuteReportUrls = unmuteReportUrls;
        this.rewindReportUrls = rewindReportUrls;
        this.resumeReportUrls = resumeReportUrls;
        this.fullScreenReportUrls = fullScreenReportUrls;
        this.exitFullScreenReportUrls = exitFullScreenReportUrls;
        this.clickThrough = clickThrough;
        this.clickTrackings = clickTrackings;
        this.delivery = delivery;
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.videoUrl = videoUrl;
        this.videoType = videoType;
        this.frontImgUrl = frontImgUrl;
        this.behindImgUrl = behindImgUrl;
        this.iconUrl = iconUrl;
        this.videoFilePath = videoFilePath;
    }

    public int getId() {
        return this.id;
    }

    public String getAdTitle() {
        return this.adTitle;
    }

    public String getErrorReportUrl() {
        return this.errorReportUrl;
    }

    public List getImpressionReportUrls() {
        return this.impressionReportUrls;
    }

    public List getCreateViewReportUrls() {
        return this.createViewReportUrls;
    }

    public List getStartReportUrls() {
        return this.startReportUrls;
    }

    public List getFirstQuartileReportUrls() {
        return this.firstQuartileReportUrls;
    }

    public List getMidpointReportUrls() {
        return this.midpointReportUrls;
    }

    public List getThirdQuartileReportUrls() {
        return this.thirdQuartileReportUrls;
    }

    public List getCompleteReportUrls() {
        return this.completeReportUrls;
    }

    public List getCloseReportUrls() {
        return this.closeReportUrls;
    }

    public List getPauseReportUrls() {
        return this.pauseReportUrls;
    }

    public List getMuteReportUrls() {
        return this.muteReportUrls;
    }

    public List getUnmuteReportUrls() {
        return this.unmuteReportUrls;
    }

    public List getRewindReportUrls() {
        return this.rewindReportUrls;
    }

    public List getResumeReportUrls() {
        return this.resumeReportUrls;
    }

    public String getClickThrough() {
        return this.clickThrough;
    }

    public List getClickTrackings() {
        return this.clickTrackings;
    }

    public String getDelivery() {
        return this.delivery;
    }

    public int getVideoWidth() {
        return this.videoWidth;
    }

    public int getVideoHeight() {
        return this.videoHeight;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public String getVideoType() {
        return this.videoType;
    }

    public String getVideoFilePath() {
        return this.videoFilePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public void setErrorReportUrl(String errorReportUrl) {
        this.errorReportUrl = errorReportUrl;
    }

    public void setImpressionReportUrls(List impressionReportUrls) {
        this.impressionReportUrls = impressionReportUrls;
    }

    public void setCreateViewReportUrls(List createViewReportUrls) {
        this.createViewReportUrls = createViewReportUrls;
    }

    public void setStartReportUrls(List startReportUrls) {
        this.startReportUrls = startReportUrls;
    }

    public void setFirstQuartileReportUrls(List firstQuartileReportUrls) {
        this.firstQuartileReportUrls = firstQuartileReportUrls;
    }

    public void setMidpointReportUrls(List midpointReportUrls) {
        this.midpointReportUrls = midpointReportUrls;
    }

    public void setThirdQuartileReportUrls(List thirdQuartileReportUrls) {
        this.thirdQuartileReportUrls = thirdQuartileReportUrls;
    }

    public void setCompleteReportUrls(List completeReportUrls) {
        this.completeReportUrls = completeReportUrls;
    }

    public void setCloseReportUrls(List closeReportUrls) {
        this.closeReportUrls = closeReportUrls;
    }

    public void setPauseReportUrls(List pauseReportUrls) {
        this.pauseReportUrls = pauseReportUrls;
    }

    public void setMuteReportUrls(List muteReportUrls) {
        this.muteReportUrls = muteReportUrls;
    }

    public void setUnmuteReportUrls(List unmuteReportUrls) {
        this.unmuteReportUrls = unmuteReportUrls;
    }

    public void setRewindReportUrls(List rewindReportUrls) {
        this.rewindReportUrls = rewindReportUrls;
    }

    public void setResumeReportUrls(List resumeReportUrls) {
        this.resumeReportUrls = resumeReportUrls;
    }

    public void setClickThrough(String clickThrough) {
        this.clickThrough = clickThrough;
    }

    public void setClickTrackings(List clickTrackings) {
        this.clickTrackings = clickTrackings;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public void setVideoFilePath(String videoFilePath) {
        this.videoFilePath = videoFilePath;
    }

    public String getFrontImgUrl() {
        return this.frontImgUrl;
    }

    public void setFrontImgUrl(String frontImgUrl) {
        this.frontImgUrl = frontImgUrl;
    }

    public String getBehindImgUrl() {
        return this.behindImgUrl;
    }

    public void setBehindImgUrl(String behindImgUrl) {
        this.behindImgUrl = behindImgUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List getFullScreenReportUrls() {
        return this.fullScreenReportUrls;
    }

    public void setFullScreenReportUrls(List fullScreenReportUrls) {
        this.fullScreenReportUrls = fullScreenReportUrls;
    }

    public List getExitFullScreenReportUrls() {
        return this.exitFullScreenReportUrls;
    }

    public void setExitFullScreenReportUrls(List exitFullScreenReportUrls) {
        this.exitFullScreenReportUrls = exitFullScreenReportUrls;
    }

    public String toString() {
        return "VastModel{id=" + this.id + ", adTitle='" + this.adTitle + '\'' + ", errorReportUrl='" + this.errorReportUrl + '\'' + ", impressionReportUrls=" + this.impressionReportUrls + ", createViewReportUrls=" + this.createViewReportUrls + ", startReportUrls=" + this.startReportUrls + ", firstQuartileReportUrls=" + this.firstQuartileReportUrls + ", midpointReportUrls=" + this.midpointReportUrls + ", thirdQuartileReportUrls=" + this.thirdQuartileReportUrls + ", completeReportUrls=" + this.completeReportUrls + ", closeReportUrls=" + this.closeReportUrls + ", pauseReportUrls=" + this.pauseReportUrls + ", muteReportUrls=" + this.muteReportUrls + ", unmuteReportUrls=" + this.unmuteReportUrls + ", rewindReportUrls=" + this.rewindReportUrls + ", resumeReportUrls=" + this.resumeReportUrls + ", fullScreenReportUrls=" + this.fullScreenReportUrls + ", exitFullScreenReportUrls=" + this.exitFullScreenReportUrls + ", clickThrough='" + this.clickThrough + '\'' + ", clickTrackings=" + this.clickTrackings + ", delivery='" + this.delivery + '\'' + ", videoWidth=" + this.videoWidth + ", videoHeight=" + this.videoHeight + ", videoUrl='" + this.videoUrl + '\'' + ", videoType='" + this.videoType + '\'' + ", frontImgUrl='" + this.frontImgUrl + '\'' + ", behindImgUrl='" + this.behindImgUrl + '\'' + ", iconUrl='" + this.iconUrl + '\'' + ", videoFilePath='" + this.videoFilePath + '\'' + '}';
    }
}
