package com.cleanmaster.ui.app.market;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.cleanmaster.data.filter.b;
import com.cleanmaster.model.VastModel;
import com.cleanmaster.model.a;
import com.cmcm.adsdk.requestconfig.data.ConfigResponseHeader.Colums;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.AdType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ad extends a implements Serializable {
    public static final int APP_CAN_UPDATE = 2;
    public static final int APP_INSTALLED = 1;
    public static final int APP_NOT_INSTALLED = 0;
    public static final int APP_TAG_NOT_SHOW = 0;
    public static final int APP_TAG_SHOW_HOT = 2;
    public static final int APP_TAG_SHOW_NEW = 1;
    private static final int BOLD_MASK = 1;
    private static final int COLLECTION_AD_TYPE = 100;
    public static final int COLOR_BLACK = 2;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_GREY = 1;
    public static final int COLOR_LIGHT_GREY = 3;
    public static final int COLOR_LIGHT_RED = 5;
    private static final int COLOR_MASK = 30;
    private static final int DESC_MASK = 992;
    private static final int DETAIL_MASK = 31744;
    public static final int MT_TYPE_OPEN_BROWSER = 256;
    public static final int MT_TYPE_OPEN_DEEPLINK = 512;
    public static final int MT_TYPE_OPEN_DOWNLOAD = 8;
    public static final int MT_TYPE_OPEN_INTERNAL = 64;
    private static final int OPERATION_AD_TYPE = 0;
    public static final int RECOMMEND_SHOW_TYPE_HIGH = 1003;
    public static final int RECOMMEND_SHOW_TYPE_LOW = 1005;
    public static final int RECOMMEND_TYPE_THREE_APPS = 13;
    private static final int SELF_ADV_AD_TYPE = 3;
    private static final String SHOW_TOP_FAKE_PKGNAME = "x_showtop";
    public static final int SHOW_TYPE_BANNER_GALLERY = 1018;
    public static final int SHOW_TYPE_BANNER_ICON_LIST = 1013;
    public static final int SHOW_TYPE_BANNER_SINGLE_APP = 1012;
    public static final int SHOW_TYPE_BANNER_TEXT = 1014;
    public static final int SHOW_TYPE_BAO_CARD = 1002;
    public static final int SHOW_TYPE_BAO_CARD_NEW = 50000;
    public static final int SHOW_TYPE_BIG_ICON = 1004;
    public static final int SHOW_TYPE_BIG_PICKS = 1000;
    public static final int SHOW_TYPE_CONTENT_CHEETAH_JUMPTO = 10000;
    public static final int SHOW_TYPE_EIGHT_ICON = 1015;
    public static final int SHOW_TYPE_FACEBOOK = 1016;
    public static final int SHOW_TYPE_GAMEBOX_3_ICON = 1024;
    public static final int SHOW_TYPE_GAMEBOX_BAO_CARD = 1022;
    public static final int SHOW_TYPE_GAME_BIG_CARD = 1010;
    public static final int SHOW_TYPE_GDT = 1026;
    public static final int SHOW_TYPE_GENERNAL_PICKS = 0;
    public static final int SHOW_TYPE_GP_TOP = 1025;
    public static final int SHOW_TYPE_HAVE_PIC_BIG_CARD = 50000;
    public static final int SHOW_TYPE_HTML5_CARD = 1023;
    public static final int SHOW_TYPE_ICON_LIST = 1006;
    public static final int SHOW_TYPE_NEWS_SMALL_PIC = 70003;
    public static final int SHOW_TYPE_NEWS_THREE_PIC = 70002;
    public static final int SHOW_TYPE_NO_PIC_SMALL_CARD = 50001;
    public static final int SHOW_TYPE_RESULTPAGE_BIGCARD = 1016;
    public static final int SHOW_TYPE_SPECIAL_RECOMMEND = 1001;
    public static final int SHOW_TYPE_TEXT = 1009;
    public static final int SHOW_TYPE_TEXT_WITH_ICON = 1011;
    public static final int SHOW_TYPE_THREE_ICON = 1008;
    public static final int SHOW_TYPE_TWO_PIC = 1007;
    public static final int SHOW_TYPE_VAST = 50003;
    private static final int TITLE_MASK = 31;
    public static final int VERSION = 40;
    b _conditionsjson = null;
    private int app_action_type = -1;
    private int app_show_type = -1;
    private int app_tag;
    private String background;
    private String button_txt;
    private String conditionsjson = "";
    private String context_code;
    private String des;
    private String desc;
    private String detail_id;
    private String detail_pg;
    private String download_num;
    private String genre;
    private String html;
    public int installedStatus;
    public boolean isNativeAd = false;
    private boolean isShowGuess = false;
    private int mAdStatus = 0;
    private String mClickTrackingUrl;
    private long mCreateTime;
    private String mDeepLink;
    private String mEditorExt = "";
    private String mEditorHeadLogo = "";
    private String mEditorName = "";
    private String mEditorRecommend = "";
    private String mExtPicks;
    private String mExtension;
    private int mFont = 0;
    private String mImprTrackingUrl = "";
    private boolean mIsShowed = false;
    private int mLikes = -1;
    private int mLoadClick = 0;
    private String mPosName = "";
    private String mPosid;
    private int mPriority;
    private ArrayList mSugApps;
    private String mThirdImpUrl;
    private long mTimeStamp = 0;
    private int mt_type;
    private String name_space;
    private String pic_url;
    private String pkg;
    private String pkg_size;
    private String pkg_url;
    private double price;
    private double rating;
    private int res_type;
    private String reviewers;
    private int showRating;
    private int showedPos = 0;
    private int subject = 0;
    private String sug_app;
    private int sug_type;
    private String title;
    private String trigger_src;
    private VastModel vastModel;
    public int versionCode;
    private int x_posision = -1;

    public boolean isShowGuess() {
        return this.isShowGuess;
    }

    public void setShowGuess(boolean isShowGuess) {
        this.isShowGuess = isShowGuess;
    }

    public String toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("title", getTitle());
            jSONObject.put("desc", getDesc());
            jSONObject.put("pic_url", getPicUrl());
            jSONObject.put("pkg", getPkg());
            jSONObject.put("pkg_url", getPkgUrl());
            jSONObject.put("des", getDes());
            jSONObject.put("app_tag", getAppTag());
            jSONObject.put("detail_id", getDetailId());
            jSONObject.put("download_num", getDownloadNum());
            jSONObject.put("subject", getSubject());
            jSONObject.put("rating", getRating());
            jSONObject.put("versioncode", getVersionCode());
            jSONObject.put("price", getPrice());
            jSONObject.put("reviewers", getReviewers());
            jSONObject.put("genre", getGenre());
            jSONObject.put("pkg_size", getpkg_size());
            jSONObject.put("sug_type", getSugType());
            jSONObject.put("res_type", getResType());
            jSONObject.put(Colums.REQUEST_MT_TYPE_COLUMN, getMtType());
            jSONObject.put("app_show_type", getAppShowType());
            jSONObject.put("app_action_type", getAppActionType());
            jSONObject.put("background", getBackground());
            jSONObject.put("context_code", getContextCode());
            jSONObject.put("name_space", getNameSpace());
            jSONObject.put("trigger_src", getNameSpace());
            jSONObject.put("sug_app", getSugApp());
            jSONObject.put("button_txt", getButtonTxt());
            jSONObject.put("pos_name=", getPosName());
            jSONObject.put("timestamp=", getTimeStamp());
            jSONObject.put("editor_head_logo=", getEditorHeadLogo());
            jSONObject.put("editor_name=", getEditorName());
            jSONObject.put("editor_recommend=", getEditorRecommend());
            jSONObject.put("editor_ext=", getEditorExt());
            jSONObject.put("detail_pg=", getDetail_pg());
            jSONObject.put("impr_tracking_url", getImprTrackingUrl());
            jSONObject.put("likes", getLikes());
            jSONObject.put("conditionsjson", getConditionsJson());
            jSONObject.put("is_ad", this.mAdStatus);
            jSONObject.put("font", getFont());
            jSONObject.put("load_clk", getLoadClk());
            jSONObject.put(AdType.HTML, getHtml());
            jSONObject.put("extension", getExtension());
            jSONObject.put("deeplink", getDeepLink());
            jSONObject.put("priority", getPriority());
            jSONObject.put("click_tracking_url", getClickTrackingUrl());
            jSONObject.put("third_imp_url", getThirdImpUrl());
            jSONObject.put("create_time", getCreateTime());
            jSONObject.put("posid", getPosid());
            jSONObject.put("is_show", isShowed() ? 1 : 0);
            jSONObject.put("ext_pics", getExtPick());
        } catch (Exception e) {
        }
        return jSONObject.toString();
    }

    public boolean isPriority() {
        return getPriority() == 1;
    }

    public String getClickTrackingUrl() {
        return this.mClickTrackingUrl;
    }

    public void setClickTrackingUrl(String clickTrackingUrl) {
        this.mClickTrackingUrl = clickTrackingUrl;
    }

    public String getThirdImpUrl() {
        return this.mThirdImpUrl;
    }

    public void setThirdImpUrl(String thirdImpUrl) {
        this.mThirdImpUrl = thirdImpUrl;
    }

    public String getPosid() {
        return this.mPosid;
    }

    public void setPosid(String posid) {
        this.mPosid = posid;
    }

    public String getExtPick() {
        return this.mExtPicks;
    }

    public void setExtPicks(String extPicks) {
        this.mExtPicks = extPicks;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public String getDeepLink() {
        return this.mDeepLink;
    }

    public void setDeepLink(String deeplink) {
        this.mDeepLink = deeplink;
    }

    public int getAdStatus() {
        return this.mAdStatus;
    }

    public void setAdStatus(int adStatus) {
        this.mAdStatus = adStatus;
    }

    public String getConditionsJson() {
        return this.conditionsjson;
    }

    public Ad createAdFromPckName(String packageName) {
        this.pkg = packageName;
        return this;
    }

    public Ad setConditionsJson(String conditionsjson) {
        this.conditionsjson = conditionsjson;
        return this;
    }

    public a toBuinessDataItem() {
        a aVar = new a(getPkg(), getSugType(), getResType(), getDes());
        aVar.a(this.x_posision);
        return aVar;
    }

    public a toBuinessDataItem(int duration, int playtime, int event) {
        a aVar = new a(getPkg(), getSugType(), getResType(), getDes(), duration, playtime, event);
        aVar.a(this.x_posision);
        return aVar;
    }

    public String getImprTrackingUrl() {
        return this.mImprTrackingUrl;
    }

    public void setImprTrackingUrl(String imprTrackingUrl) {
        this.mImprTrackingUrl = imprTrackingUrl;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public boolean isInstalled() {
        return 1 == this.installedStatus;
    }

    public boolean isSubjectAd() {
        return getSubject() > 0;
    }

    public boolean isOperationOrCollectionAd() {
        if (this.res_type == 0 || this.res_type == 100 || this.res_type == 3 || this.mAdStatus != 0) {
            return true;
        }
        return false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetail_pg() {
        return this.detail_pg;
    }

    public void setDetail_pg(String detail_pg) {
        this.detail_pg = detail_pg;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setPicUrl(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPicUrl() {
        return this.pic_url;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPkg() {
        return this.pkg;
    }

    public void setPkgUrl(String pkg_url) {
        this.pkg_url = pkg_url;
    }

    public String getPkgUrl() {
        return this.pkg_url;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public void setAppTag(int app_tag) {
        this.app_tag = app_tag;
    }

    public int getAppTag() {
        return this.app_tag;
    }

    public void setDetailId(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getDetailId() {
        return this.detail_id;
    }

    public void setDownloadNum(String download_num) {
        this.download_num = download_num;
    }

    public String getDownloadNum() {
        return this.download_num;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getSubject() {
        return this.subject;
    }

    public int getShowType() {
        return this.showRating;
    }

    public void setShowType(int showType) {
        this.showRating = showType;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double p) {
        this.price = p;
    }

    public void setReviewers(String rv) {
        this.reviewers = rv;
    }

    public String getReviewers() {
        return this.reviewers;
    }

    public boolean isShowRating() {
        return this.showRating == 1;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setgenre(String sg) {
        this.genre = sg;
    }

    public void setPkgSize(String ps) {
        this.pkg_size = ps;
    }

    public String getpkg_size() {
        return this.pkg_size;
    }

    public void setSugType(int st) {
        this.sug_type = st;
    }

    public int getSugType() {
        return this.sug_type;
    }

    public void setResType(int rt) {
        this.res_type = rt;
    }

    public int getResType() {
        return this.res_type;
    }

    public double getRating() {
        return this.rating;
    }

    public int getLikes() {
        return this.mLikes;
    }

    public void setLikes(int likes) {
        this.mLikes = likes;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setMtType(int mt_type) {
        this.mt_type = mt_type;
    }

    public int getMtType() {
        return this.mt_type;
    }

    public int getAppShowType() {
        return this.app_show_type;
    }

    public int getAppActionType() {
        return this.app_action_type;
    }

    public void setAppShowType(int app_show_type) {
        this.app_show_type = app_show_type;
    }

    public void setAppActionType(int app_action_type) {
        this.app_action_type = app_action_type;
    }

    public boolean isOpenInternal() {
        return this.mt_type == 64;
    }

    public boolean isOpenBrowser() {
        return this.mt_type == 256;
    }

    public boolean isDeepLink() {
        return this.mt_type == 512;
    }

    public boolean isAvailAble() {
        long b = b.b(getPosid());
        if (b == 0) {
            b = 3600000;
        }
        return System.currentTimeMillis() - getCreateTime() < b;
    }

    public void setShowed(boolean isShow) {
        this.mIsShowed = isShow;
    }

    public boolean isShowed() {
        return this.mIsShowed;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackground() {
        return this.background;
    }

    public void setContextCode(String context_code) {
        this.context_code = context_code;
    }

    public String getContextCode() {
        return this.context_code;
    }

    public void setNameSpace(String name_space) {
        this.name_space = name_space;
    }

    public String getNameSpace() {
        return this.name_space;
    }

    public String getPosName() {
        return this.mPosName;
    }

    public void setPosName(String posname) {
        this.mPosName = posname;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(long timestamp) {
        this.mTimeStamp = timestamp;
    }

    public String getEditorHeadLogo() {
        return this.mEditorHeadLogo;
    }

    public void setEditorHeadLogo(String editorheadlogo) {
        this.mEditorHeadLogo = editorheadlogo;
    }

    public String getEditorName() {
        return this.mEditorName;
    }

    public void setEditorName(String editorname) {
        this.mEditorName = editorname;
    }

    public String getEditorRecommend() {
        return this.mEditorRecommend;
    }

    public void setEditorRecommend(String editorrecommend) {
        this.mEditorRecommend = editorrecommend;
    }

    public String getEditorExt() {
        return this.mEditorExt;
    }

    public void setEditorExt(String editorext) {
        this.mEditorExt = editorext;
    }

    public void setTriggerSrc(String trigger_src) {
        this.trigger_src = trigger_src;
    }

    public String getSugApp() {
        return notnull(this.sug_app);
    }

    public void setSugApp(String sug_app) {
        this.sug_app = sug_app;
    }

    public String getTriggerSrc() {
        return this.trigger_src;
    }

    public int getFont() {
        return this.mFont;
    }

    public void setFont(int font) {
        this.mFont = font;
    }

    private int getTitleFont() {
        return getFont() & 31;
    }

    private int getDescFont() {
        return (getFont() & DESC_MASK) >> 5;
    }

    private int getDetailFont() {
        return (getFont() & DETAIL_MASK) >> 10;
    }

    public boolean isTitleBold() {
        return (getTitleFont() & 1) == 1;
    }

    public boolean isDescBold() {
        return (getDescFont() & 1) == 1;
    }

    public boolean isDetailBold() {
        return (getDetailFont() & 1) == 1;
    }

    public int getTitleColor() {
        return (getTitleFont() & 30) >> 1;
    }

    public int getDescColor() {
        return (getDescFont() & 30) >> 1;
    }

    public int getDetailColor() {
        return (getDetailFont() & 30) >> 1;
    }

    public int getLoadClk() {
        return this.mLoadClick;
    }

    public void setLoadClick(int loadClick) {
        this.mLoadClick = loadClick;
    }

    public boolean isPreloadUrl() {
        return getLoadClk() > 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ " + getClass().getSimpleName() + " ]").append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("        title=").append(getTitle()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("         desc=").append(getDesc()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      pic_url=").append(getPicUrl()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("          pkg=").append(getPkg()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      pkg_url=").append(getPkgUrl()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("          des=").append(getDes()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      app_tag=").append(getAppTag()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("    detail_id=").append(getDetailId()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" download_num=").append(getDownloadNum()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      subject=").append(getSubject()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("       rating=").append(getRating()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("  versionCode=").append(this.versionCode).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("  show_rating=").append(getShowType()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      mt_type=").append(getMtType()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("    app_action_type=").append(getAppActionType()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      app_show_type=").append(getAppShowType()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("         background=").append(getBackground()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("       context_code=").append(getContextCode()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("         name_space=").append(getNameSpace()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("        trigger_src=").append(getTriggerSrc()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("            sug_app=").append(getSugApp()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("            button_txt=").append(getButtonTxt()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      pos_name=").append(getPosName()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      timestamp=").append(getTimeStamp()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      editor_head_logo=").append(getEditorHeadLogo()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      editor_name=").append(getEditorName()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      editor_recommend=").append(getEditorRecommend()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("      editor_ext=").append(getEditorExt()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     impr_tracking_url=").append(getImprTrackingUrl()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     detail_pg=").append(getDetail_pg()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     likes=").append(getLikes()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     conditionsjson=").append(getConditionsJson()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     font=").append(getFont()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     load_clk=").append(getLoadClk()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     html=").append(getHtml()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     extension=").append(getExtension()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     deeplink=").append(getDeepLink()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     priority=").append(getPriority()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     clickTrackUrl=").append(getClickTrackingUrl()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     thirdImpUrl=").append(getThirdImpUrl()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     createTime=").append(getCreateTime()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     posid=").append(getPosid()).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     showed=").append(isShowed() ? 1 : 0).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append("     ext_picks=").append(getExtPick()).append(MASTNativeAdConstants.NEWLINE);
        return stringBuilder.toString();
    }

    public Ad fromJSONObject(JSONObject o) {
        boolean z = true;
        if (o != null) {
            try {
                int i;
                if (!o.isNull("title")) {
                    setTitle(o.getString("title"));
                }
                if (!o.isNull("desc")) {
                    setDesc(o.getString("desc"));
                }
                if (!o.isNull("pic_url")) {
                    setPicUrl(o.getString("pic_url"));
                }
                if (!o.isNull("pkg")) {
                    setPkg(o.getString("pkg"));
                }
                if (!o.isNull("pkg_url")) {
                    setPkgUrl(o.getString("pkg_url"));
                }
                if (!o.isNull("des")) {
                    setDes(o.getString("des"));
                }
                if (!o.isNull("app_tag")) {
                    setAppTag(o.getInt("app_tag"));
                }
                if (!o.isNull("detail_id")) {
                    setDetailId(o.getString("detail_id"));
                }
                if (!o.isNull("download_num")) {
                    setDownloadNum(o.getString("download_num"));
                }
                if (!o.isNull("subject")) {
                    setSubject(o.getInt("subject"));
                }
                if (!o.isNull("rating")) {
                    setRating(o.getDouble("rating"));
                }
                if (!o.isNull("versioncode")) {
                    this.versionCode = o.getInt("versioncode");
                }
                if (!o.isNull("price")) {
                    setPrice(o.getDouble("price"));
                }
                if (!o.isNull("reviewers")) {
                    setReviewers(o.getString("reviewers"));
                }
                if (!o.isNull("genre")) {
                    setgenre(o.getString("genre"));
                }
                if (!o.isNull("pkg_size")) {
                    setPkgSize(o.getString("pkg_size"));
                }
                if (!o.isNull("sug_type")) {
                    setSugType(o.getInt("sug_type"));
                }
                if (!o.isNull("res_type")) {
                    setResType(o.getInt("res_type"));
                }
                if (!o.isNull(Colums.REQUEST_MT_TYPE_COLUMN)) {
                    setMtType(o.getInt(Colums.REQUEST_MT_TYPE_COLUMN));
                }
                if (!o.isNull("app_show_type")) {
                    setAppShowType(o.optInt("app_show_type"));
                }
                if (!o.isNull("app_action_type")) {
                    setAppActionType(o.optInt("app_action_type"));
                }
                if (!o.isNull("background")) {
                    setBackground(o.optString("background"));
                }
                if (!o.isNull("context_code")) {
                    setContextCode(o.optString("context_code"));
                }
                if (!o.isNull("name_space")) {
                    setNameSpace(o.optString("name_space"));
                }
                if (!o.isNull("trigger_src")) {
                    setTriggerSrc(o.optString("trigger_src"));
                }
                if (!o.isNull("sug_app")) {
                    setSugApp(o.optString("sug_app"));
                }
                if (!o.isNull("button_txt")) {
                    setButtonTxt(o.optString("button_txt"));
                }
                if (!o.isNull("detail_pg")) {
                    setDetail_pg(o.optString("detail_pg"));
                }
                if (!o.isNull("pos_name")) {
                    setPosName(o.optString("pos_name", ""));
                }
                if (!o.isNull("timestamp")) {
                    setTimeStamp(o.optLong("timestamp", 0));
                }
                if (!o.isNull("editor_head_logo")) {
                    setEditorHeadLogo(o.optString("editor_head_logo", ""));
                }
                if (!o.isNull("editor_name")) {
                    setEditorName(o.optString("editor_name", ""));
                }
                if (!o.isNull("editor_recommend")) {
                    setEditorRecommend(o.optString("editor_recommend", ""));
                }
                if (!o.isNull("editor_ext")) {
                    setEditorExt(o.optString("editor_ext", ""));
                }
                if (!o.isNull("impr_tracking_url")) {
                    setImprTrackingUrl(o.optString("impr_tracking_url", ""));
                }
                if (!o.isNull("likes")) {
                    setLikes(o.optInt("likes", -1));
                }
                if (!o.isNull("conditionsjson")) {
                    setConditionsJson(o.optString("conditionsjson", ""));
                }
                if (o.isNull("is_ad")) {
                    i = 0;
                } else {
                    i = o.optInt("is_ad", 0);
                }
                setAdStatus(i);
                if (!o.isNull("font")) {
                    setFont(o.optInt("font", 0));
                }
                if (!o.isNull("load_clk")) {
                    setLoadClick(o.optInt("load_clk", 0));
                }
                if (!o.isNull(AdType.HTML)) {
                    setHtml(o.optString(AdType.HTML, ""));
                }
                if (!o.isNull("extension")) {
                    setExtension(o.optString("extension", ""));
                }
                if (!o.isNull("deeplink")) {
                    setDeepLink(o.optString("deeplink", ""));
                }
                if (!o.isNull("priority")) {
                    setPriority(o.optInt("priority", 0));
                }
                if (!o.isNull("click_tracking_url")) {
                    setClickTrackingUrl(o.optString("click_tracking_url", ""));
                }
                if (!o.isNull("third_imp_url")) {
                    setThirdImpUrl(o.optString("third_imp_url", ""));
                }
                if (!o.isNull("create_time")) {
                    setCreateTime(o.optLong("create_time", 0));
                }
                if (!o.isNull("posid")) {
                    setPosid(o.optString("posid", ""));
                }
                if (!o.isNull("is_show")) {
                    if (o.optInt("is_show", 0) != 1) {
                        z = false;
                    }
                    setShowed(z);
                }
                if (!o.isNull("ext_pics")) {
                    setExtPicks(o.optString("ext_pics", ""));
                }
            } catch (JSONException e) {
            }
        }
        return this;
    }

    public static String notnull(String s) {
        return s == null ? "" : s;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("title", notnull(getTitle()));
            contentValues.put("pic_url", notnull(getPicUrl()));
            contentValues.put("pkg", notnull(getPkg()));
            contentValues.put("pkg_url", notnull(getPkgUrl()));
            contentValues.put("app_tag", Integer.valueOf(getAppTag()));
            contentValues.put("desc", notnull(getDesc()));
            contentValues.put("des", notnull(getDes()));
            contentValues.put("detail_id", notnull(getDetailId()));
            contentValues.put("download_num", notnull(getDownloadNum()));
            contentValues.put("subject", Integer.valueOf(getSubject()));
            contentValues.put("price", Double.valueOf(getPrice()));
            contentValues.put("reviewers", notnull(getReviewers()));
            contentValues.put("genre", notnull(getGenre()));
            contentValues.put("rating", Double.valueOf(getRating()));
            contentValues.put("pkg_size", notnull(getpkg_size()));
            contentValues.put("sug_type", Integer.valueOf(getSugType()));
            contentValues.put("res_type", Integer.valueOf(getResType()));
            contentValues.put(Colums.REQUEST_MT_TYPE_COLUMN, Integer.valueOf(getMtType()));
            contentValues.put("app_action_type", Integer.valueOf(getAppActionType()));
            contentValues.put("app_show_type", Integer.valueOf(getAppShowType()));
            contentValues.put("background", getBackground());
            contentValues.put("context_code", getContextCode());
            contentValues.put("name_space", getNameSpace());
            contentValues.put("trigger_src", notnull(getTriggerSrc()));
            contentValues.put("sug_app", getSugApp());
            contentValues.put("button_txt", getButtonTxt());
            contentValues.put("pos_name", notnull(getPosName()));
            contentValues.put("editor_head_logo", notnull(getEditorHeadLogo()));
            contentValues.put("editor_name", notnull(getEditorName()));
            contentValues.put("editor_recommend", notnull(getEditorRecommend()));
            contentValues.put("timestamp", Long.valueOf(getTimeStamp()));
            contentValues.put("editor_ext", getEditorExt());
            contentValues.put("impr_tracking_url", getImprTrackingUrl());
            contentValues.put("detail_pg", notnull(getDetail_pg()));
            contentValues.put("likes", Integer.valueOf(getLikes()));
            contentValues.put("conditionsjson", getConditionsJson());
            contentValues.put("is_ad", Integer.valueOf(getAdStatus()));
            contentValues.put("font", Integer.valueOf(getFont()));
            contentValues.put("load_clk", Integer.valueOf(getLoadClk()));
            contentValues.put(AdType.HTML, getHtml());
            contentValues.put("extension", getExtension());
            contentValues.put("deeplink", getDeepLink());
            contentValues.put("priority", Integer.valueOf(getPriority()));
            contentValues.put("click_tracking_url", getClickTrackingUrl());
            contentValues.put("third_imp_url", getThirdImpUrl());
            setCreateTime(System.currentTimeMillis());
            contentValues.put("create_time", Long.valueOf(getCreateTime()));
            contentValues.put("posid", getPosid());
            contentValues.put("is_show", Integer.valueOf(isShowed() ? 1 : 0));
            contentValues.put("ext_pics", getExtPick());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    public ContentValues toContentValues(String posid) {
        setPosid(posid);
        return toContentValues();
    }

    public Ad fromCursor(Cursor cursor) {
        boolean z = true;
        setTitle(STRING(cursor, "title", ""));
        setPicUrl(STRING(cursor, "pic_url", ""));
        setPkg(STRING(cursor, "pkg", ""));
        setPkgUrl(STRING(cursor, "pkg_url", ""));
        setAppTag(cursor.getInt(cursor.getColumnIndex("app_tag")));
        setDes(STRING(cursor, "des", ""));
        setDesc(STRING(cursor, "desc", ""));
        setDetailId(STRING(cursor, "detail_id", ""));
        setDownloadNum(STRING(cursor, "download_num", ""));
        setSubject(cursor.getInt(cursor.getColumnIndex("subject")));
        setRating(cursor.getDouble(cursor.getColumnIndex("rating")));
        setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
        setReviewers(cursor.getString(cursor.getColumnIndex("reviewers")));
        setgenre(cursor.getString(cursor.getColumnIndex("genre")));
        setPkgSize(cursor.getString(cursor.getColumnIndex("pkg_size")));
        setSugType(cursor.getInt(cursor.getColumnIndex("sug_type")));
        setResType(cursor.getInt(cursor.getColumnIndex("res_type")));
        setMtType(cursor.getInt(cursor.getColumnIndex(Colums.REQUEST_MT_TYPE_COLUMN)));
        setAppShowType(cursor.getInt(cursor.getColumnIndex("app_show_type")));
        setAppActionType(cursor.getInt(cursor.getColumnIndex("app_action_type")));
        setBackground(cursor.getString(cursor.getColumnIndex("background")));
        setContextCode(cursor.getString(cursor.getColumnIndex("context_code")));
        setNameSpace(cursor.getString(cursor.getColumnIndex("name_space")));
        setTriggerSrc(STRING(cursor, "trigger_src", ""));
        setSugApp(STRING(cursor, "sug_app", ""));
        setButtonTxt(cursor.getString(cursor.getColumnIndex("button_txt")));
        setDetail_pg(cursor.getString(cursor.getColumnIndex("detail_pg")));
        setPosName(STRING(cursor, "pos_name", ""));
        setTimeStamp(LONG(cursor, "timestamp", 0));
        setEditorHeadLogo(STRING(cursor, "editor_head_logo", ""));
        setEditorName(STRING(cursor, "editor_name", ""));
        setEditorRecommend(STRING(cursor, "editor_recommend", ""));
        setEditorExt(STRING(cursor, "editor_ext", ""));
        setImprTrackingUrl(STRING(cursor, "impr_tracking_url", ""));
        setLikes(cursor.getInt(cursor.getColumnIndex("likes")));
        setConditionsJson(STRING(cursor, "conditionsjson", ""));
        setAdStatus(cursor.getInt(cursor.getColumnIndex("is_ad")));
        setFont(cursor.getInt(cursor.getColumnIndex("font")));
        setLoadClick(cursor.getInt(cursor.getColumnIndex("load_clk")));
        setHtml(cursor.getString(cursor.getColumnIndex(AdType.HTML)));
        setExtension(cursor.getString(cursor.getColumnIndex("extension")));
        setDeepLink(cursor.getString(cursor.getColumnIndex("deeplink")));
        setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
        setClickTrackingUrl(cursor.getString(cursor.getColumnIndex("click_tracking_url")));
        setThirdImpUrl(cursor.getString(cursor.getColumnIndex("third_imp_url")));
        setCreateTime(cursor.getLong(cursor.getColumnIndex("create_time")));
        setPosid(cursor.getString(cursor.getColumnIndex("posid")));
        if (cursor.getInt(cursor.getColumnIndex("is_show")) != 1) {
            z = false;
        }
        setShowed(z);
        setExtPicks(cursor.getString(cursor.getColumnIndex("ext_pics")));
        return this;
    }

    public static String STRING(Cursor cursor, String name, String fail) {
        try {
            return cursor.getString(cursor.getColumnIndexOrThrow(name));
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static long LONG(Cursor cursor, String name, long fail) {
        try {
            return cursor.getLong(cursor.getColumnIndexOrThrow(name));
        } catch (RuntimeException e) {
            return fail;
        }
    }

    public boolean isForOpen_Uninstall() {
        return isSubjectAd() || isOpenInternal() || isInstalled();
    }

    public boolean isForOpen() {
        return isSubjectAd() || isOpenInternal() || isInstalled() || !(getAppShowType() == 0 || getAppShowType() == SHOW_TYPE_BIG_ICON || getAppShowType() == 1002 || getAppShowType() == SHOW_TYPE_GAMEBOX_BAO_CARD || getAppShowType() == 50000);
    }

    public static void createTable(SQLiteDatabase db, String tableName) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id" + " INTEGER PRIMARY KEY," + "title TEXT" + ",desc" + " TEXT," + "pic_url TEXT" + ",pkg" + " TEXT," + "pkg_url TEXT" + ",des" + " TEXT," + "app_tag INTEGER" + ",detail_id" + " TEXT," + "download_num TEXT" + ",rating" + " DOUBLE," + "show_rating TEXT" + ",price" + " DOUBLE," + "reviewers TEXT" + ",genre" + " TEXT," + "pkg_size TEXT" + ",sug_type" + " INTEGER," + "res_type INTEGER" + ",subject" + " INTEGER," + "app_action_type INTEGER" + ",app_show_type" + " INTEGER," + "mt_type INTEGER" + ",background" + " TEXT," + "context_code TEXT" + ",name_space" + " TEXT," + "trigger_src TEXT" + ",button_txt" + " TEXT," + "sug_app TEXT" + ",pos_name" + " TEXT," + "timestamp INTEGER" + ",editor_head_logo" + " TEXT," + "editor_name TEXT" + ",editor_ext" + " TEXT," + "detail_pg TEXT" + ",editor_recommend" + " TEXT," + "impr_tracking_url TEXT" + ",likes" + " INTEGER," + "conditionsjson TEXT" + ",is_ad" + " INTEGER," + "font INTEGER" + ",load_clk" + " INTEGER," + "html TEXT" + ",extension" + " TEXT," + "deeplink TEXT" + ",priority" + " INTEGER," + "click_tracking_url TEXT" + ",third_imp_url" + " TEXT," + "create_time INTEGER" + ",posid" + " TEXT," + "is_show INTEGER" + ",ext_pics" + " TEXT);");
    }

    public int getActionForMaidian() {
        if (isOpenInternal()) {
            return 6;
        }
        switch (this.installedStatus) {
            case 0:
                return 2;
            case 1:
                return 4;
            case 2:
                return 3;
            default:
                return 0;
        }
    }

    public void setPosision(int posision) {
        this.x_posision = posision;
    }

    public int getPosision() {
        return this.x_posision;
    }

    public boolean isNotInstalled() {
        return this.installedStatus == 0;
    }

    public boolean canUpdate() {
        return this.installedStatus == 2;
    }

    public static List getTopAds(Map datasSet) {
        return datasSet.containsKey(SHOW_TOP_FAKE_PKGNAME) ? (List) datasSet.get(SHOW_TOP_FAKE_PKGNAME) : null;
    }

    public List getTriggerSrcAsList() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.trigger_src)) {
            arrayList.add(SHOW_TOP_FAKE_PKGNAME);
        } else {
            String[] split = this.trigger_src.split(",");
            if (split != null) {
                for (Object add : split) {
                    arrayList.add(add);
                }
            }
        }
        return arrayList;
    }

    public void setSugApps(ArrayList ads) {
        this.mSugApps = ads;
    }

    public ArrayList getSugApps() {
        if (this.mSugApps != null) {
            return this.mSugApps;
        }
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(this.sug_app)) {
            try {
                JSONArray jSONArray = new JSONArray(this.sug_app);
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    Ad ad = new Ad();
                    ad.fromJSONObject(jSONObject);
                    if (ad != null) {
                        arrayList.add(ad);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.mSugApps = arrayList;
        return arrayList;
    }

    public boolean hasDetail() {
        return !TextUtils.isEmpty(getDetail_pg());
    }

    public String getButtonTxt() {
        return this.button_txt;
    }

    public void setButtonTxt(String button_txt) {
        this.button_txt = button_txt;
    }

    public int getShowedPos() {
        return this.showedPos;
    }

    public void setShowedPos(int showedPos) {
        this.showedPos = showedPos;
    }

    public String getExtension() {
        return this.mExtension;
    }

    public void setExtension(String strExtension) {
        this.mExtension = strExtension;
    }

    public static Ad createAd(String packageName) {
        Ad ad = new Ad();
        ad.setDes("");
        ad.setSugType(-1);
        return ad.createAdFromPckName(packageName);
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public boolean match(com.cleanmaster.ui.app.market.data.filter.a env) {
        if (TextUtils.isEmpty(this.conditionsjson)) {
            return true;
        }
        if (this._conditionsjson == null) {
            this._conditionsjson = com.cleanmaster.ui.app.market.data.filter.b.a(this.conditionsjson);
        }
        if (this._conditionsjson != null) {
            return this._conditionsjson.a(env);
        }
        return true;
    }

    public void setVastModel(VastModel vastModel) {
        this.vastModel = vastModel;
    }

    public VastModel getVastModel() {
        return this.vastModel;
    }

    public List getExtPics() {
        ArrayList arrayList = new ArrayList();
        String background = getBackground();
        if (!TextUtils.isEmpty(background)) {
            arrayList.add(background);
        }
        background = getExtPick();
        if (!TextUtils.isEmpty(background)) {
            try {
                JSONArray jSONArray = new JSONArray(background);
                for (int i = 0; i < jSONArray.length(); i++) {
                    Object obj = jSONArray.get(i);
                    if (obj != null) {
                        arrayList.add(obj.toString());
                    }
                }
            } catch (Exception e) {
            }
        }
        return arrayList;
    }

    public boolean isMtTypeAvail() {
        return this.mt_type == 256 || this.mt_type == 512 || this.mt_type == 8 || this.mt_type == 64;
    }
}
