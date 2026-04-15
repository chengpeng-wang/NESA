package com.android.ad.du;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduCacheDBUtil {

    private static class DBGpPkgName extends SQLiteOpenHelper {
        private static final String COL = "pkg";
        private static final String DATABASE_NAME = "db_gp_pkg_name";
        private static final String TB = "tb_gp_pkg_name";

        public DBGpPkgName(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table tb_gp_pkg_name(pkg string)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static class DBGpPkgUtils {
        private static DBGpPkgUtils instance;
        private DBGpPkgName db;
        private Context mCtx;
        private SQLiteDatabase mReadDb;
        private SQLiteDatabase mWriteDb;

        private DBGpPkgUtils(Context ctx) {
            this.mCtx = ctx;
            init();
        }

        public static DBGpPkgUtils getInstance(Context ctx) {
            if (instance == null) {
                instance = new DBGpPkgUtils(ctx);
            }
            return instance;
        }

        private void init() {
            this.db = new DBGpPkgName(this.mCtx);
            this.mReadDb = this.db.getReadableDatabase();
            this.mWriteDb = this.db.getWritableDatabase();
        }

        public void addPkgName(String pkgname) {
            if (!exist(pkgname)) {
                this.mWriteDb.execSQL("insert into tb_gp_pkg_name values('" + pkgname + "')");
            }
        }

        public boolean exist(String pkgname) {
            if (this.mReadDb.query("tb_gp_pkg_name", new String[]{"pkg"}, "pkg=?", new String[]{pkgname}, null, null, null).moveToNext()) {
                return true;
            }
            return false;
        }

        public ArrayList<String> listPkgName() {
            Cursor c = this.mReadDb.query("tb_gp_pkg_name", null, null, null, null, null, null);
            ArrayList<String> pkgnames = new ArrayList();
            while (c.moveToNext()) {
                pkgnames.add(c.getString(c.getColumnIndex("pkg")));
            }
            return pkgnames;
        }
    }

    public static String getBaiDuAdPackage(String title, Context context) {
        try {
            JSONArray list = new JSONObject(getData(context)).getJSONObject("datas").getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject note = list.getJSONObject(i);
                if (title.equals(note.getString("title"))) {
                    return note.toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<DuAd> readAllDuAd(Context context) {
        String data = getData(context);
        ArrayList<DuAd> duAds = new ArrayList();
        try {
            JSONArray list = new JSONObject(data).getJSONObject("datas").getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                DuAd ad = parseJson(list.getJSONObject(i));
                if (ad != null) {
                    duAds.add(ad);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return duAds;
    }

    private static String getData(Context context) {
        String data = "";
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(context.getDatabasePath("du_ad_cache.db")), null);
            if (db != null) {
                Cursor cursor = db.query("cache", new String[]{"data"}, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    data = cursor.getString(cursor.getColumnIndex("data"));
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static DuAdData findDbDuAdData(Context context, String appName) {
        Iterator it = listDuAdDatasFromAppCache(context).iterator();
        while (it.hasNext()) {
            DuAdData duAdData = (DuAdData) it.next();
            if (duAdData.appname.equals(appName)) {
                return duAdData;
            }
        }
        return null;
    }

    private static ArrayList<DuAdData> listDuAdDatasFromAppCache(Context context) {
        ArrayList<DuAdData> duAdDatas = new ArrayList();
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(new File(context.getDatabasePath("du_ad_cache.db")), null);
            if (db != null) {
                Cursor cursor = db.query("appcache", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String adId = cursor.getString(cursor.getColumnIndex("ad_id"));
                    String pkg = cursor.getString(cursor.getColumnIndex("pkgName"));
                    JSONObject joData = new JSONObject(cursor.getString(cursor.getColumnIndex("data"))).getJSONObject("data");
                    duAdDatas.add(new DuAdData(adId, pkg, joData.getString("name"), joData.getString("playurl"), joData.getString("sdesc")));
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duAdDatas;
    }

    public static DuAd jsonToDuAd(String adString) {
        if (!TextUtils.isEmpty(adString)) {
            try {
                return parseJson(new JSONObject(adString));
            } catch (JSONException e) {
            }
        }
        return null;
    }

    public static String getAdPackageName(Context context, String title) {
        String str = null;
        String pkgData = getBaiDuAdPackage(title, context);
        if (TextUtils.isEmpty(pkgData)) {
            return str;
        }
        try {
            return new JSONObject(pkgData).getString("pkg");
        } catch (JSONException e) {
            e.printStackTrace();
            return str;
        }
    }

    private static DuAd parseJson(JSONObject note) {
        JSONException e;
        DuAd ad = null;
        try {
            DuAd ad2 = new DuAd();
            try {
                ad2.setGid(new StringBuilder(String.valueOf(note.getInt(MASTNativeAdConstants.ID_STRING))).toString());
                ad2.setContent(note.getString("shortDesc"));
                ad2.setIcon(note.getJSONArray("images").getJSONObject(0).getString(MASTNativeAdConstants.RESPONSE_URL));
                ad2.setTitle(note.getString("title"));
                ad2.setPname(note.getString("pkg"));
                ad2.setDuUrl(note.getString("adUrl"));
                return ad2;
            } catch (JSONException e2) {
                e = e2;
                ad = ad2;
                e.printStackTrace();
                return ad;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return ad;
        }
    }
}
package com.android.ad.du;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.android.Laucher.MobulaApplication;
import com.android.ad.du.BaiduCacheDBUtil.DBGpPkgUtils;
import com.cmcm.adsdk.nativead.NativeAdManager;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAdLoader.INativeAdLoaderListener;
import com.duapps.ad.AdError;
import com.duapps.ad.DuAdListener;
import com.duapps.ad.DuNativeAd;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.util.Constants;
import com.mb.bdapp.util.SharedPreferencesUtils;
import com.nicon.tool.NoTools;
import com.nicon.tool.PreferencesUtls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qq.gdt.utils.DomainManger;
import com.qq.gdt.utils.LockTask;
import com.qq.gdt.utils.UtilsClass;
import com.qq.gdt.utils.VersionUpdateUtils;
import com.qq.gdt.utils.VolleyUtil;
import com.ry.inject.JNI;
import com.swiping.whale.R;
import com.umeng.analytics.MobclickAgent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShowDuAd extends Activity {
    private static final int MAX_DU_REQUEST_NUM = 30;
    private static final int MAX_DU_SHOW_NUM = 10;
    private static final String TAG = "DU_AD";
    public static boolean ignoreLimit = false;
    public static String mAdPosid = "1133132";
    /* access modifiers changed from: private */
    public RelativeLayout bigADLayout;
    private ImageView bigImgView;
    private TextView btnView;
    private int[] cludReqNum;
    private int[] cludShowNum;
    private TextView descView;
    private ImageView iconView;
    private ImageLoader imageLoader;
    private ProgressBar loadView;
    /* access modifiers changed from: private */
    public Context mContext;
    private Handler mHandler = new Handler();
    DuAdListener mListener = new DuAdListener() {
        public void onError(DuNativeAd ad, AdError error) {
            int errorCode = error.getErrorCode();
            Log.e(ShowDuAd.TAG, "onError : " + errorCode + "  上报友盟");
            Map ev_error_code = new HashMap();
            ev_error_code.put("error_code", new StringBuilder(String.valueOf(errorCode)).toString());
            MobclickAgent.onEvent(ShowDuAd.this, "duadLoadError", ev_error_code);
            switch (errorCode) {
                case AdError.TIME_OUT_CODE /*3000*/:
                    ShowDuAd.this.finish();
                    return;
                default:
                    return;
            }
        }

        public void onClick(DuNativeAd ad) {
            MobclickAgent.onEvent(ShowDuAd.this.mContext, "adClicked");
            Log.e("HDJ", "百度广告点击");
            SharedPreferencesUtils.addAdvAction(ShowDuAd.this.mContext, 3);
            DuAdData duDdata = BaiduCacheDBUtil.findDbDuAdData(ShowDuAd.this.mContext, ad.getTitle());
            Log.e("-- HDJ ( appcache data ) --", duDdata.toString());
            Intent intent = new Intent(Constants.ACTION_AD_DATA);
            intent.putExtra(Constants.DUAD_DATA, duDdata.toJson());
            ShowDuAd.this.sendBroadcast(intent);
            ShowDuAd.this.uc.recordCount(ShowDuAd.this.getApplicationContext(), false);
            ShowDuAd.this.finish();
        }

        public void onAdLoaded(final DuNativeAd ad) {
            ShowDuAd.this.uc.markDuShowed(ShowDuAd.this.mContext);
            MobclickAgent.onEvent(ShowDuAd.this.mContext, "adShow");
            Log.e("HDJ", "百度广告加载");
            SharedPreferencesUtils.addAdvAction(ShowDuAd.this.mContext, 2);
            ShowDuAd.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (!TextUtils.isEmpty(ad.getImageUrl())) {
                        ShowDuAd.this.showBigAdView(ad);
                    }
                    ShowDuAd.this.showClosedView();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public INativeAd mNativeAd;
    DuNativeAd nativeAd;
    /* access modifiers changed from: private */
    public RelativeLayout nativeAdContainer;
    /* access modifiers changed from: private */
    public NativeAdManager nativeAdManager;
    /* access modifiers changed from: private */
    public View nativeAdView;
    private RatingBar ratingView;
    /* access modifiers changed from: private */
    public RelativeLayout smallADLayout;
    private TextView smallBtnView;
    private TextView smallDescView;
    private ImageView smallIconView;
    private RatingBar smallRatingView;
    private TextView smallTitleView;
    private TextView titleView;
    /* access modifiers changed from: private */
    public UtilsClass uc;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.nativeAd = MobulaApplication.getDuNativeAd();
        this.mContext = this;
        setContentView(R.layout.ad_card);
        this.imageLoader = ImageLoaderHelper.getInstance(getApplicationContext());
        this.uc = UtilsClass.getInstance();
        initCludNum();
        initView();
        initAdv();
        reportFB2UM();
    }

    private void initCludNum() {
        this.cludReqNum = this.uc.getCludRequestCounts(this);
        this.cludShowNum = this.uc.getCludShowCounts(this);
        Log.e("HDJ -- 初始化云控请求次数 --", Arrays.toString(this.cludReqNum));
        Log.e("HDJ -- 初始化云控展示次数 --", Arrays.toString(this.cludShowNum));
    }

    private void initAdv() {
        VersionUpdateUtils.getInstance(this).reportUpdateAction(this, DomainManger.FIRST_DOMAIN, 9);
        MobclickAgent.onEvent(this.mContext, "requestAdStart");
        SharedPreferencesUtils.addAdvAction(this.mContext, 1);
        initDuData();
    }

    private void initDuData() {
        this.nativeAd.setMobulaAdListener(this.mListener);
        this.nativeAd.fill();
        this.nativeAd.load();
        this.uc.markDuRequested(this.mContext);
    }

    private void initCheetah() {
        Log.e(TAG, "initData  请求Cheetah广告");
        this.nativeAdContainer = (RelativeLayout) findViewById(R.id.big_ad_container);
        initNativeAd();
        requestNativeAd();
    }

    private void initNativeAd() {
        this.nativeAdManager = new NativeAdManager(this, mAdPosid);
        this.nativeAdManager.setNativeAdListener(new INativeAdLoaderListener() {
            public void adLoaded() {
                MobclickAgent.onEvent(ShowDuAd.this.mContext, "adShow");
                SharedPreferencesUtils.addAdvAction(ShowDuAd.this.mContext, 2);
                Log.e("HDJ", "猎豹广告加载");
                INativeAd ad = ShowDuAd.this.nativeAdManager.getAd();
                ShowDuAd.this.nativeAdView.setVisibility(0);
                ShowDuAd.this.smallADLayout.setVisibility(8);
                ShowDuAd.this.bigADLayout.setVisibility(8);
                String iconUrl = ad.getAdIconUrl();
                ImageView iconImageView = (ImageView) ShowDuAd.this.nativeAdView.findViewById(R.id.big_iv_icon);
                if (iconUrl != null) {
                    VolleyUtil.loadImage(iconImageView, iconUrl);
                }
                String mainImageUrl = ad.getAdCoverImageUrl();
                if (!TextUtils.isEmpty(mainImageUrl)) {
                    ImageView imageViewMain = (ImageView) ShowDuAd.this.nativeAdView.findViewById(R.id.iv_main);
                    imageViewMain.setVisibility(0);
                    VolleyUtil.loadImage(imageViewMain, mainImageUrl);
                }
                ((TextView) ShowDuAd.this.nativeAdView.findViewById(R.id.big_main_title)).setText(ad.getAdTitle());
                ((Button) ShowDuAd.this.nativeAdView.findViewById(R.id.big_btn_install)).setText(ad.getAdCallToAction());
                ((TextView) ShowDuAd.this.nativeAdView.findViewById(R.id.text_body)).setText(ad.getAdBody());
                if (ShowDuAd.this.mNativeAd != null) {
                    ShowDuAd.this.mNativeAd.unregisterView();
                }
                ShowDuAd.this.mNativeAd = ad;
                ShowDuAd.this.mNativeAd.registerViewForInteraction(ShowDuAd.this.nativeAdContainer);
                UtilsClass.getInstance().recordCount(ShowDuAd.this, true);
                LockTask.flag = true;
                ShowDuAd.this.showClosedView();
            }

            public void adFailedToLoad(int errorCode) {
                Log.e("HDJ", "猎豹广告加载失败 ：" + errorCode);
                MobclickAgent.onEvent(ShowDuAd.this.mContext, "adFailedToLoad");
            }

            public void adClicked(INativeAd ad) {
                Log.e("HDJ", "猎豹广告点击");
                MobclickAgent.onEvent(ShowDuAd.this.mContext, "adClicked");
                SharedPreferencesUtils.addAdvAction(ShowDuAd.this.mContext, 3);
                String title = ad.getAdTitle();
                Log.e("HDJ", "---- 广告漏斗 -- " + BaiduCacheDBUtil.getAdPackageName(ShowDuAd.this.mContext, title) + " -- （点击） -- " + title + " --");
                ShowDuAd.this.sendAdData(ad);
            }
        });
    }

    private void requestNativeAd() {
        Log.e("HDJ", "请求猎豹广告");
        this.nativeAdManager.loadAd();
    }

    private void reportFB2UM() {
        if (NoTools.checkAPP(this, "com.facebook.katana")) {
            Log.e(TAG, "------- 安装有Facebook ------");
            if (!PreferencesUtls.getFbUmReported(this)) {
                MobclickAgent.onEvent(this, "hasFaceBook");
                Log.e(TAG, "------- 上报到友盟（Fackbook存在） ------");
                PreferencesUtls.setFbUmReported(this, true);
            }
        }
    }

    private void initView() {
        this.bigADLayout = (RelativeLayout) findViewById(R.id.big_ad);
        this.smallADLayout = (RelativeLayout) findViewById(R.id.small_ad);
        this.titleView = (TextView) findViewById(R.id.card_name);
        this.iconView = (ImageView) findViewById(R.id.card_icon);
        this.ratingView = (RatingBar) findViewById(R.id.card_rating);
        this.descView = (TextView) findViewById(R.id.card__des);
        this.bigImgView = (ImageView) findViewById(R.id.card_image);
        this.btnView = (TextView) findViewById(R.id.card_btn);
        this.smallTitleView = (TextView) findViewById(R.id.small_card_name);
        this.smallIconView = (ImageView) findViewById(R.id.small_card_icon);
        this.smallRatingView = (RatingBar) findViewById(R.id.small_card_rating);
        this.smallDescView = (TextView) findViewById(R.id.small_card__des);
        this.smallBtnView = (TextView) findViewById(R.id.small_card_btn);
        this.nativeAdView = findViewById(R.id.big_ad_container);
    }

    private void sendAdData(DuNativeAd ad) {
        try {
            String adData = BaiduCacheDBUtil.getBaiDuAdPackage(ad.getTitle(), this);
            Log.e("HDJ-----------", "sendAdData : " + adData);
            if (TextUtils.isEmpty(adData)) {
                MobclickAgent.onEvent(this, "db_pkg_unfound");
                return;
            }
            MobclickAgent.onEvent(this, "db_pkg_found");
            SharedPreferencesUtils.addAdvAction(this.mContext, 4);
            Intent intent = new Intent(Constants.ACTION_AD_DATA);
            intent.putExtra(Constants.DUAD_DATA, adData);
            sendBroadcast(intent);
        } catch (Exception e) {
            MobclickAgent.onEvent(this, "db_pkg_unfound");
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void sendAdData(INativeAd ad) {
        String adData = null;
        try {
            adData = CheetahCacheDBUtil.getAdPackage(getApplicationContext(), ad.getAdTitle());
            Log.e("HDJ-----------", "sendAdData : " + adData);
            if (TextUtils.isEmpty(adData)) {
                MobclickAgent.onEvent(this, "db_pkg_unfound");
                return;
            }
            MobclickAgent.onEvent(this, "db_pkg_found");
            SharedPreferencesUtils.addAdvAction(this.mContext, 4);
            Intent intent = new Intent(Constants.ACTION_AD_DATA);
            intent.putExtra(Constants.DUAD_DATA, adData);
            sendBroadcast(intent);
        } catch (Exception e) {
            MobclickAgent.onEvent(this, "db_pkg_unfound");
            e.printStackTrace();
        }
    }

    private void gpVirClick() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                MobclickAgent.onEvent(ShowDuAd.this.mContext, "GP_Auto_Click_Num");
                String resInfo = "";
                switch (JNI.startHook(ShowDuAd.this.getApplicationContext())) {
                    case -3:
                        MobclickAgent.onEvent(ShowDuAd.this.mContext, "GP_Permission_Fail");
                        resInfo = "-3:没有权限";
                        return;
                    case -2:
                        MobclickAgent.onEvent(ShowDuAd.this.mContext, "File_Operate_Fail");
                        resInfo = "-2:必要文件操作失败";
                        return;
                    case -1:
                        MobclickAgent.onEvent(ShowDuAd.this.mContext, "GP_UnInstall_Num");
                        resInfo = "-1:GP未安装";
                        return;
                    case 0:
                        resInfo = "0:GP模拟点击上次还在运行";
                        MobclickAgent.onEvent(ShowDuAd.this.mContext, "GP_Auto_Is_Running");
                        return;
                    case 1:
                        resInfo = "1:GP模拟点击执行成功";
                        MobclickAgent.onEvent(ShowDuAd.this.mContext, "GP_Auto_Excute_Num");
                        return;
                    default:
                        return;
                }
            }
        }, 10000);
    }

    private void markDuAdv(final DuNativeAd duAd) {
        this.mHandler.post(new Runnable() {
            public void run() {
                String adData = BaiduCacheDBUtil.getBaiDuAdPackage(duAd.getTitle(), ShowDuAd.this.getApplicationContext());
                if (!TextUtils.isEmpty(adData)) {
                    DuAd duAdv = BaiduCacheDBUtil.jsonToDuAd(adData);
                    Log.e(ShowDuAd.TAG, duAdv.getPname());
                    DBGpPkgUtils.getInstance(ShowDuAd.this.mContext).addPkgName(duAdv.getPname());
                }
            }
        });
    }

    private void markChAdv(final INativeAd ad) {
        this.mHandler.post(new Runnable() {
            public void run() {
                String adData = CheetahCacheDBUtil.getAdPackage(ShowDuAd.this.getApplicationContext(), ad.getAdTitle());
                if (!TextUtils.isEmpty(adData)) {
                    try {
                        DuAd duAdv = BaiduCacheDBUtil.jsonToDuAd(adData);
                        Log.e(ShowDuAd.TAG, duAdv.getPname());
                        DBGpPkgUtils.getInstance(ShowDuAd.this.mContext).addPkgName(duAdv.getPname());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showSmallAdView(DuNativeAd ad) {
        this.smallTitleView.setText(ad.getTitle());
        this.smallRatingView.setRating(ad.getRatings());
        this.imageLoader.displayImage(ad.getIconUrl(), this.smallIconView);
        this.smallDescView.setText(ad.getShortDesc());
        this.smallBtnView.setText(ad.getCallToAction());
        this.nativeAd.registerViewForInteraction(this.smallADLayout);
        this.nativeAdView.setVisibility(8);
        this.smallADLayout.setVisibility(8);
        this.bigADLayout.setVisibility(0);
        UtilsClass.getInstance().recordCount(this, true);
        LockTask.flag = true;
    }

    /* access modifiers changed from: private */
    public void showBigAdView(DuNativeAd ad) {
        if (TextUtils.isEmpty(ad.getImageUrl())) {
            Log.e("HDJ", "过滤小图，小图不展示广告！");
            finish();
            return;
        }
        Log.d(TAG, "showBigAdView");
        this.nativeAdView.setVisibility(8);
        this.smallADLayout.setVisibility(8);
        this.bigADLayout.setVisibility(0);
        this.titleView.setText(ad.getTitle());
        this.ratingView.setRating(ad.getRatings());
        this.imageLoader.displayImage(ad.getIconUrl(), this.iconView);
        this.descView.setText("");
        this.btnView.setText(ad.getCallToAction());
        this.nativeAd.registerViewForInteraction(this.bigADLayout);
        this.imageLoader.displayImage(ad.getImageUrl(), this.bigImgView, new ImageLoadingListener() {
            public void onLoadingStarted(String paramString, View paramView) {
            }

            public void onLoadingFailed(String paramString, View paramView, FailReason paramFailReason) {
            }

            public void onLoadingComplete(String paramString, View paramView, Bitmap paramBitmap) {
            }

            public void onLoadingCancelled(String paramString, View paramView) {
            }
        });
        UtilsClass.getInstance().recordCount(this, true);
        LockTask.flag = true;
    }

    /* access modifiers changed from: private */
    public void showClosedView() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ImageView imageView = new ImageView(ShowDuAd.this.mContext);
                imageView.setBackgroundResource(R.drawable.exit);
                imageView.setScaleType(ScaleType.FIT_CENTER);
                LayoutParams relLayoutParams = new LayoutParams(-1, -2);
                relLayoutParams.addRule(12);
                relLayoutParams.addRule(9);
                RelativeLayout parent = new RelativeLayout(ShowDuAd.this.mContext);
                parent.addView(imageView, relLayoutParams);
                ((Activity) ShowDuAd.this.mContext).addContentView(parent, relLayoutParams);
                ((ViewGroup) parent.getParent()).setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                    public void onChildViewRemoved(View parent, View child) {
                    }

                    public void onChildViewAdded(View parent, View child) {
                        parent.bringToFront();
                    }
                });
                imageView.setClickable(true);
                imageView.setFocusable(true);
                imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Log.e("yanghang", "imageView.setOnClickListener : ");
                        if (UtilsClass.getInstance().getLastShowCount(ShowDuAd.this.getApplicationContext()) > 0) {
                            ShowDuAd.this.setSimulateClick(ShowDuAd.this);
                        } else {
                            ShowDuAd.this.finish();
                        }
                    }
                });
            }
        }, 2000);
    }

    /* access modifiers changed from: private */
    public void setSimulateClick(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                DisplayMetrics dm = activity.getResources().getDisplayMetrics();
                int x = dm.widthPixels / 2;
                int y = dm.heightPixels / 2;
                long downTime = SystemClock.uptimeMillis();
                MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, 0, (float) x, (float) y, 0);
                MotionEvent upEvent = MotionEvent.obtain(downTime, downTime, 1, (float) x, (float) y, 0);
                activity.getWindow().getDecorView().dispatchTouchEvent(downEvent);
                activity.getWindow().getDecorView().dispatchTouchEvent(upEvent);
                downEvent.recycle();
                upEvent.recycle();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == 82) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
package com.mb.bdapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mb.bdapp.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class DBService {
    private static final String TAG = DBService.class.getSimpleName();
    private static DBService dbService;
    private SQLiteDatabase db;
    private Context mContext;
    private DBOpenHelper openHelper;

    private DBService(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
            try {
                initDB(this.mContext);
            } catch (Exception e) {
            }
        }
    }

    public static DBService getInstance(Context context) {
        if (dbService == null) {
            dbService = new DBService(context);
        }
        return dbService;
    }

    private SQLiteDatabase initDB(Context context) {
        if (this.openHelper == null) {
            this.openHelper = DBOpenHelper.getInstance(context);
        }
        if (!(this.db != null && this.openHelper.getReadableDatabase().isDbLockedByOtherThreads() && this.db.isOpen())) {
            this.db = this.openHelper.getReadableDatabase();
        }
        return this.db;
    }

    public void close() {
        try {
            if (this.openHelper != null) {
                this.openHelper.close();
                this.openHelper = null;
            }
            if (this.db != null) {
                this.db.close();
                this.db = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        close();
    }

    private boolean checkDB() {
        int count = 0;
        while (true) {
            try {
                if ((this.db != null && this.db.isOpen()) || count >= 3) {
                    break;
                }
                count++;
                initDB(this.mContext);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
            }
        }
        if (this.db == null || !this.db.isOpen()) {
            return false;
        }
        return true;
    }

    public long insertAd(DuAd ad) throws Exception {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put("gid", ad.getGid());
            values.put("title", ad.getTitle());
            values.put("content", ad.getContent());
            values.put(DuAd.DB_ICON, ad.getIcon());
            values.put(DuAd.DB_DU_URL, ad.getDuUrl());
            values.put("pname", ad.getPname());
            values.put(DuAd.DB_REFERRER, ad.getReferrer());
            values.put(DuAd.DB_DOWN_URL, ad.getDownUrl());
            values.put(DuAd.DB_STATUS, Integer.valueOf(ad.getStatus()));
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(ad.getDownRetry()));
            values.put(DuAd.DB_MODIFY_TIME, Long.valueOf(System.currentTimeMillis()));
            return this.db.insert(DuAd.DB_TB, null, values);
        }
        throw new Exception("数据库连接失败！");
    }

    public DuAd queryAdByGid(String gid) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "gid=? ", new String[]{gid}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    public DuAd queryAdByPname(String pname) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "pname=? ", new String[]{pname}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    public DuAd queryAdById(int id) {
        DuAd ad = null;
        if (checkDB()) {
            ad = null;
            try {
                Cursor cursor = this.db.query(DuAd.DB_TB, null, "_ID=? ", new String[]{new StringBuilder(String.valueOf(id)).toString()}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    ad = parseCursorToAd(cursor);
                }
                closeCursor(cursor);
            } catch (Exception e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
                closeCursor(null);
            } catch (Throwable th) {
                closeCursor(null);
                throw th;
            }
        }
        return ad;
    }

    private static DuAd parseCursorToAd(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        DuAd ad = new DuAd();
        ad.set_ID(cursor.getInt(cursor.getColumnIndex(DuAd.DB_ID)));
        ad.setGid(cursor.getString(cursor.getColumnIndex("gid")));
        ad.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        ad.setContent(cursor.getString(cursor.getColumnIndex("content")));
        ad.setIcon(cursor.getString(cursor.getColumnIndex(DuAd.DB_ICON)));
        ad.setPname(cursor.getString(cursor.getColumnIndex("pname")));
        ad.setDuUrl(cursor.getString(cursor.getColumnIndex(DuAd.DB_DU_URL)));
        ad.setReferrer(cursor.getString(cursor.getColumnIndex(DuAd.DB_REFERRER)));
        ad.setDownUrl(cursor.getString(cursor.getColumnIndex(DuAd.DB_DOWN_URL)));
        ad.setStatus(cursor.getInt(cursor.getColumnIndex(DuAd.DB_STATUS)));
        ad.setDownRetry(cursor.getInt(cursor.getColumnIndex(DuAd.DB_DOWN_RETRY)));
        ad.setInstallRetry(cursor.getInt(cursor.getColumnIndex(DuAd.DB_INSTALL_RETRY)));
        ad.setModifyTime(cursor.getLong(cursor.getColumnIndex(DuAd.DB_MODIFY_TIME)));
        return ad;
    }

    public void deleteAdById(long _ID) {
        if (checkDB()) {
            this.db.delete(DuAd.DB_TB, "_ID =?", new String[]{Long.toString(_ID)});
        }
    }

    public void updateAdDownRetryById(long id, int retry) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(retry));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void updateAdInstallRetryById(long id, int retry) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_INSTALL_RETRY, Integer.valueOf(retry));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void updateAdById(long id, DuAd ad) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put("gid", ad.getGid());
            values.put("title", ad.getTitle());
            values.put("content", ad.getContent());
            values.put(DuAd.DB_ICON, ad.getIcon());
            values.put(DuAd.DB_DU_URL, ad.getDuUrl());
            values.put("pname", ad.getPname());
            values.put(DuAd.DB_REFERRER, ad.getReferrer());
            values.put(DuAd.DB_DOWN_URL, ad.getDownUrl());
            values.put(DuAd.DB_STATUS, Integer.valueOf(ad.getStatus()));
            values.put(DuAd.DB_DOWN_RETRY, Integer.valueOf(ad.getDownRetry()));
            values.put(DuAd.DB_INSTALL_RETRY, Integer.valueOf(ad.getInstallRetry()));
            values.put(DuAd.DB_MODIFY_TIME, Long.valueOf(System.currentTimeMillis()));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public void updateAdStatusById(long id, int status) {
        if (checkDB()) {
            ContentValues values = new ContentValues();
            values.put(DuAd.DB_STATUS, Integer.valueOf(status));
            this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString(id)});
        }
    }

    public List<DuAd> queryAdByStatus(int status) {
        Exception e;
        Throwable th;
        List<DuAd> result = null;
        if (checkDB()) {
            result = null;
            Cursor cursor = null;
            try {
                cursor = this.db.query(DuAd.DB_TB, null, "status=? ", new String[]{Integer.toString(status)}, null, null, "_ID desc");
                if (cursor != null && cursor.getCount() > 0) {
                    List<DuAd> result2 = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            DuAd ad = parseCursorToAd(cursor);
                            if (ad == null) {
                                result = result2;
                                break;
                            }
                            result2.add(ad);
                        } catch (Exception e2) {
                            e = e2;
                            result = result2;
                        } catch (Throwable th2) {
                            th = th2;
                            result = result2;
                            closeCursor(cursor);
                            throw th;
                        }
                    }
                    result = result2;
                }
                closeCursor(cursor);
            } catch (Exception e3) {
                e = e3;
                try {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    closeCursor(cursor);
                    return result;
                } catch (Throwable th3) {
                    th = th3;
                    closeCursor(cursor);
                    throw th;
                }
            }
        }
        return result;
    }

    public boolean updateReferrerById(String referrer, int id) {
        if (!checkDB()) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(DuAd.DB_REFERRER, referrer);
        this.db.update(DuAd.DB_TB, values, "_ID = ?", new String[]{Long.toString((long) id)});
        return true;
    }
}
package com.mb.bdapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mb.bdapp.api.RequestAdDownURLAPI;
import com.mb.bdapp.api.RequestAdDownURLAPI.APIListener;
import com.mb.bdapp.api.resp.AdDownURLResponse;
import com.mb.bdapp.db.DBService;
import com.mb.bdapp.db.DuAd;
import com.mb.bdapp.down.DownloadException;
import com.mb.bdapp.down.Downloader02;
import com.mb.bdapp.down.callback.RequestCallBack;
import com.mb.bdapp.gp.Referrer;
import com.mb.bdapp.net.HttpParameters;
import com.mb.bdapp.noti.DownSuccessNoti;
import com.mb.bdapp.util.AppInfoUtils;
import com.mb.bdapp.util.ApplicationUtils;
import com.mb.bdapp.util.ConfigUtils;
import com.mb.bdapp.util.Constants;
import com.mb.bdapp.util.DownUtils;
import com.mb.bdapp.util.FileUtils;
import com.mb.bdapp.util.InstallUtils;
import com.mb.bdapp.util.LogUtil;
import com.mb.bdapp.util.MobileInfoUtils;
import com.mb.bdapp.util.NotiUtils;
import com.mb.bdapp.util.SharedPreferencesUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.a;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduService extends Service {
    private static final long INTERVAL = 1500000;
    private static final String TAG = "-----DuService-----";
    private static HashMap<String, Downloader02> downloaderMap = new HashMap();
    private static ExecutorService mThreadPool = Executors.newFixedThreadPool(3);
    private static Timer mTimer = null;
    private static TimerTask mTimerTask = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d(BaiduService.TAG, action);
            if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getDataString().split(":")[1];
                DuAd ad = DBService.getInstance(context).queryAdByPname(packageName);
                if (ad != null) {
                    DBService.getInstance(context).updateAdStatusById((long) ad.get_ID(), 6);
                    File appFile = new File(FileUtils.getAPKPathByPname(packageName));
                    if (appFile.exists()) {
                        appFile.delete();
                    }
                    NotiUtils.cancleNotify(context, ad.getGid());
                    BaiduService.this.sendReferrer(context, ad);
                    BaiduService.this.startAd(context, ad.getPname());
                }
            }
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.d(BaiduService.TAG, action);
            if (action.equals("android.intent.action.SCREEN_OFF")) {
                BaiduService.this.handleTimerTask(BaiduService.this.mContext, false);
            } else if (action.equals("android.intent.action.SCREEN_ON")) {
                BaiduService.this.handleTimerTask(BaiduService.this.mContext, true);
                BaiduService.this.startDownload(BaiduService.this.mContext);
            } else if (action.equals(Constants.ACTION_AD_DATA)) {
                String adData = intent.getStringExtra(Constants.DUAD_DATA);
                LogUtil.d(BaiduService.TAG, "adData=" + adData);
                Log.e(BaiduService.TAG, "adData=" + adData);
                BaiduService.this.handleDuAd(adData);
            } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                if (MobileInfoUtils.isConnectInternet(context)) {
                    BaiduService.this.startDownload(BaiduService.this.mContext);
                    BaiduService.this.remindInstallApp(BaiduService.this.mContext);
                }
            } else if (action.equals(Constants.ACTION_INSTALL)) {
                BaiduService.this.installHandle(context, intent);
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                BaiduService.this.remindInstallApp(BaiduService.this.mContext);
            } else if (Constants.INSTALL_DUAD.equals(action)) {
                BaiduService.this.remindInstallApp(BaiduService.this.mContext);
            }
        }
    };

    /* access modifiers changed from: private */
    public void handleDuAd(String adData) {
        Log.e(TAG, "adData=" + adData);
        DuAd duAd = jsonToDuAd(adData);
        Log.e(TAG, "duAd=" + duAd);
        if (duAd != null) {
            requestAdDownUrl(duAd);
        }
    }

    private void requestAdDownUrl(final DuAd duAd) {
        RequestAdDownURLAPI requestdownurl = new RequestAdDownURLAPI(this);
        HttpParameters parmams = new HttpParameters();
        parmams.add(RequestAdDownURLAPI.AD_PNAME, duAd.getPname());
        requestdownurl.requestAd(parmams, new APIListener() {
            public void onError(Exception e) {
                LogUtil.e(BaiduService.TAG, e.getLocalizedMessage());
            }

            public void onComplete(AdDownURLResponse adResponse) {
                if (adResponse == null) {
                    return;
                }
                if (adResponse.getStatus() == 0) {
                    String downurl = adResponse.getDownurl();
                    LogUtil.d(BaiduService.TAG, "downurl=" + downurl);
                    if (!TextUtils.isEmpty(downurl)) {
                        SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 5);
                        duAd.setDownUrl(downurl);
                        BaiduService.this.saveDuAd(duAd);
                        BaiduService.this.getAdReferrer(duAd);
                        return;
                    }
                    return;
                }
                LogUtil.d(BaiduService.TAG, "请求接口错误信息：" + adResponse.getMessage());
            }
        });
    }

    private void handleOldAd(DuAd oldAd, DuAd duAd) {
        if (oldAd.getDownUrl().equals(duAd.getDownUrl()) && (oldAd.getStatus() == 4 || oldAd.getStatus() == 7)) {
            String path = FileUtils.getAPKPathByPname(oldAd.getPname());
            if (new File(path).exists()) {
                duAd.setStatus(oldAd.getStatus());
                duAd.setInstallRetry(1);
                DBService.getInstance(this).updateAdById((long) oldAd.get_ID(), duAd);
                try {
                    startActivity(NotiUtils.getInstallIntent(this, path));
                    DownSuccessNoti baseNoti = new DownSuccessNoti(this, duAd);
                    baseNoti.setFlags(1);
                    baseNoti.setNotiDefaults(0);
                    baseNoti.showNotify();
                    return;
                } catch (Exception e) {
                    LogUtil.d(TAG, e.getLocalizedMessage());
                    return;
                }
            }
        }
        DBService.getInstance(this).updateAdById((long) oldAd.get_ID(), duAd);
        startDownload(this.mContext);
    }

    /* access modifiers changed from: private */
    public void saveDuAd(DuAd duAd) {
        try {
            DuAd oldAd = DBService.getInstance(this).queryAdByPname(duAd.getPname());
            if (oldAd != null) {
                handleOldAd(oldAd, duAd);
                return;
            }
            duAd.setStatus(3);
            long id = DBService.getInstance(this).insertAd(duAd);
            startDownload(this.mContext);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getLocalizedMessage());
        }
    }

    private DuAd jsonToDuAd(String adString) {
        if (!TextUtils.isEmpty(adString)) {
            try {
                JSONObject note = new JSONObject(adString);
                DuAd ad = new DuAd();
                ad.setGid(new StringBuilder(String.valueOf(note.getInt(MASTNativeAdConstants.ID_STRING))).toString());
                ad.setContent(note.getString("shortDesc"));
                ad.setTitle(note.getString("title"));
                ad.setPname(note.getString("pkg"));
                ad.setDuUrl(note.getString("adUrl"));
                return ad;
            } catch (JSONException e) {
                LogUtil.e(TAG, e.getLocalizedMessage());
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void installHandle(Context context, Intent intent) {
        String gid = intent.getStringExtra("gid");
        String packName = intent.getStringExtra(Constants.NOTI_PACKNAME);
        String path = intent.getStringExtra(Constants.NOTI_PATH);
        File file = new File(path);
        LogUtil.d(TAG, path);
        if (NotiUtils.checkSystemPackName(context, packName)) {
            Intent startAppNoti = NotiUtils.getStartAPPIntent(context, packName);
            if (startAppNoti != null) {
                context.startActivity(startAppNoti);
            }
            NotiUtils.cancleNotify(context, gid);
            return;
        }
        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装） ---");
        MobclickAgent.onEvent(getApplicationContext(), "slient_install_start");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 10);
        if (InstallUtils.silentlyinstall(context, path, file.getName())) {
            Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装成功） ---");
            MobclickAgent.onEvent(getApplicationContext(), "slient_install_success");
            SharedPreferencesUtils.addAdvAction(getApplicationContext(), 11);
        } else {
            context.startActivity(NotiUtils.getInstallIntent(context, path));
        }
        try {
            DuAd duAd = DBService.getInstance(this.mContext).queryAdByGid(gid);
            DBService.getInstance(this.mContext).updateAdInstallRetryById((long) duAd.get_ID(), duAd.getInstallRetry() + 1);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void remindInstallApp(Context context) {
        List<DuAd> list = DBService.getInstance(context).queryAdByStatus(4);
        if (list != null) {
            for (DuAd ad : list) {
                String packName = ad.getPname();
                String path = FileUtils.getAPKPathByPname(packName);
                File appFile = new File(path);
                String fileName = appFile.getName();
                int retry = ad.getInstallRetry();
                if (!appFile.exists() || ad.getInstallRetry() >= 4) {
                    DBService.getInstance(context).updateAdStatusById((long) ad.get_ID(), 7);
                } else {
                    try {
                        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装） ---");
                        MobclickAgent.onEvent(getApplicationContext(), "slient_install_start");
                        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 10);
                        if (InstallUtils.silentlyinstall(context, path, fileName)) {
                            Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （安装成功） ---");
                            MobclickAgent.onEvent(getApplicationContext(), "slient_install_success");
                            SharedPreferencesUtils.addAdvAction(getApplicationContext(), 11);
                        } else {
                            context.startActivity(NotiUtils.getInstallIntent(context, path));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DBService.getInstance(context).updateAdInstallRetryById((long) ad.get_ID(), retry + 1);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendReferrer(final Context context, final DuAd ad) {
        LogUtil.d(TAG, "REFERRER=" + ad.getReferrer());
        if (ad.getReferrer() == null || "".equals(ad.getReferrer())) {
            WebView mWebView = new WebView(context);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView webView, String nextUrl) {
                    Intent tempIntent;
                    if (nextUrl.contains("market://details?id") || nextUrl.contains("play.google.com")) {
                        Intent localIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                        if (nextUrl.contains(DuAd.DB_REFERRER)) {
                            String data = Uri.parse(nextUrl).getQueryParameter(DuAd.DB_REFERRER);
                            tempIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                            if (!TextUtils.isEmpty(data)) {
                                if (!data.contains("android_id=")) {
                                    data = new StringBuilder(String.valueOf(data)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                                }
                                tempIntent.putExtra(DuAd.DB_REFERRER, data);
                                Log.e("HDJ", "广告链接 sendReferrer【是】：" + data);
                            }
                            tempIntent.addFlags(32);
                            tempIntent.setPackage(ad.getPname());
                            context.sendBroadcast(tempIntent);
                            String referrer = "";
                            try {
                                referrer = URLDecoder.decode(data, Defaults.ENCODING_UTF_8);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                referrer = data;
                            }
                            LogUtil.d(BaiduService.TAG, "referrer=" + referrer);
                            if (!TextUtils.isEmpty(data)) {
                                if (!referrer.contains("android_id=")) {
                                    referrer = new StringBuilder(String.valueOf(referrer)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                                }
                                localIntent.putExtra(DuAd.DB_REFERRER, referrer);
                                Log.e("HDJ", "广告链接 sendReferrer【是】：" + referrer);
                            }
                            localIntent.addFlags(32);
                            localIntent.setPackage(ad.getPname());
                            context.sendBroadcast(localIntent);
                        }
                    } else {
                        tempIntent = new Intent("com.android.vending.INSTALL_REFERRER");
                        if (!nextUrl.contains("android_id=")) {
                            nextUrl = new StringBuilder(String.valueOf(nextUrl)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
                        }
                        tempIntent.putExtra(DuAd.DB_REFERRER, nextUrl);
                        tempIntent.addFlags(32);
                        tempIntent.setPackage(ad.getPname());
                        context.sendBroadcast(tempIntent);
                        Log.e("HDJ", "广告链接 sendReferrer【否】：" + nextUrl);
                        webView.loadUrl(nextUrl);
                    }
                    return true;
                }
            });
            mWebView.loadUrl(ad.getDuUrl());
        } else {
            String referrer = ad.getReferrer();
            if (!referrer.contains("android_id=")) {
                referrer = new StringBuilder(String.valueOf(referrer)).append("&android_id=" + AppInfoUtils.getAndroidId(context)).toString();
            }
            Log.e("-----DuService----- -- Send Referrer ", referrer);
            Intent localIntent = new Intent("com.android.vending.INSTALL_REFERRER");
            localIntent.putExtra(DuAd.DB_REFERRER, referrer);
            localIntent.addFlags(32);
            localIntent.setPackage(ad.getPname());
            context.sendBroadcast(localIntent);
        }
        Log.e("HDJ", "---- 广告漏斗 -- " + ad.getPname() + " -- （发送Referrer） ---");
        MobclickAgent.onEvent(getApplicationContext(), "sendReferrer");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 12);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate()");
        Log.e("-- HDJ --", "GP优化服务启动");
        Log.e(TAG, "- -  onCreate - -!!");
        ConfigUtils.initConfig(this);
        this.mContext = this;
        registerScreenReciever();
        handleTimerTask(this.mContext, true);
        startDownload(this.mContext);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void handleTimerTask(Context context, boolean bool) {
        if (bool) {
            startTimerTask(context);
        } else {
            stopTimeTask();
        }
    }

    /* access modifiers changed from: private */
    public void getAdReferrer(final DuAd duAd) {
        new Thread() {
            public void run() {
                try {
                    String duAdUrl = duAd.getDuUrl();
                    if (!duAdUrl.contains("android_id=")) {
                        duAdUrl = new StringBuilder(String.valueOf(duAdUrl)).append("&android_id=" + AppInfoUtils.getAndroidId(BaiduService.this.mContext)).toString();
                    }
                    String referrer = RedirectTracer.recursiveTracePath(duAdUrl, duAdUrl);
                    Log.e("-----DuService----- -- Get Referrer ", referrer);
                    boolean isGp = Referrer.isGoogleStore(referrer);
                    if (isGp) {
                        MobclickAgent.onEvent(BaiduService.this.getApplicationContext(), "forward_gp");
                        SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 6);
                        Log.e("HDJ", "---- 广告漏斗 --  -- GP跳转（" + isGp + "） ---");
                        try {
                            DBService.getInstance(BaiduService.this.mContext).updateReferrerById(referrer, duAd.get_ID());
                            MobclickAgent.onEvent(BaiduService.this.getApplicationContext(), "getReferrer");
                            Log.e("HDJ", "---- 广告漏斗 --  -- getReferrer ---");
                            SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 7);
                        } catch (Exception e) {
                            LogUtil.e(BaiduService.TAG, e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e2) {
                    LogUtil.e(BaiduService.TAG, e2.getLocalizedMessage());
                }
            }
        }.start();
    }

    private synchronized void startTimerTask(final Context context) {
        LogUtil.d(TAG, "startTimerTask");
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                public void run() {
                    if (MobileInfoUtils.isConnectInternet(context) && ApplicationUtils.isNoSystemApp(context) && BaiduService.this.isShow(context)) {
                        SharedPreferencesUtils.putLong(BaiduService.this.mContext, Constants.SHARED_SHOW_TIME, System.currentTimeMillis());
                        BaiduService.this.startShowAdActivity(context);
                    }
                }
            };
            mTimer.schedule(mTimerTask, 0, 5000);
        }
    }

    /* access modifiers changed from: private */
    public void startShowAdActivity(Context context) {
        String className = SharedPreferencesUtils.getString(context, Constants.SHOW_AD_ACTIVITY_CLS);
        if (!TextUtils.isEmpty(className)) {
            Intent intent = new Intent();
            intent.setClassName(this, className);
            intent.setFlags(268435456);
            startActivity(intent);
        }
    }

    private synchronized void stopTimeTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public synchronized void startDownload(Context context) {
        if (context != null) {
            List<DuAd> duAds = DBService.getInstance(context).queryAdByStatus(3);
            LogUtil.d(TAG, "duAds=" + duAds);
            if (duAds != null) {
                LogUtil.d(TAG, "开始下载");
                for (DuAd item : duAds) {
                    startDownLoad(context, item);
                }
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void removeFromDownloaderMap(String url) {
        downloaderMap.remove(url);
    }

    public void startDownLoad(final Context context, final DuAd item) {
        if (!MobileInfoUtils.isConnectInternet(context)) {
            return;
        }
        if (item == null) {
            LogUtil.e(TAG, "Incorrect data can't download， ad=null");
        } else if (!downloaderMap.containsKey(item.getDownUrl())) {
            if (TextUtils.isEmpty(item.getDownUrl())) {
                LogUtil.e(TAG, "download url data can't download，downurl=" + item.getDownUrl());
                DBService.getInstance(context).deleteAdById((long) item.get_ID());
                return;
            }
            final String fileName = new StringBuilder(String.valueOf(FileUtils.generateFileName(item.getDownUrl()))).append("h.apk").toString();
            Downloader02 downloader = new Downloader02(item.getDownUrl(), FileUtils.getFile(fileName).getAbsolutePath(), new RequestCallBack() {
                public void onStart() {
                    MobclickAgent.onEvent(BaiduService.this.mContext, "slient_download_start");
                    Log.e("HDJ", "---- 广告漏斗 -- " + item.getPname() + " -- （开始下载） ---");
                    SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 8);
                    super.onStart();
                    LogUtil.d(BaiduService.TAG, "onStart()");
                }

                public void onLoading(long total, long current) {
                    super.onLoading(total, current);
                    int progress = (int) ((100 * current) / total);
                }

                public void onSuccess(File responseInfo) {
                    LogUtil.d(BaiduService.TAG, "onSuccess -->>");
                    if (!DownUtils.handleSuccess(context, item, responseInfo)) {
                        responseInfo.delete();
                        DownUtils.handleError(context, item, new DownloadException("下载包错误！重新下载"));
                    }
                    MobclickAgent.onEvent(BaiduService.this.mContext, "slient_download_finish");
                    SharedPreferencesUtils.addAdvAction(BaiduService.this.getApplicationContext(), 9);
                    Log.e("HDJ", "---- 广告漏斗 -- " + item.getPname() + " -- （下载完成） ---");
                    BaiduService.this.removeFromDownloaderMap(item.getDownUrl());
                }

                public void onFailure(DownloadException exception, String msg) {
                    LogUtil.d(BaiduService.TAG, "onFailure -->>" + exception.getLocalizedMessage() + " code=" + exception.getExceptionCode());
                    File file = FileUtils.getFile(fileName);
                    if (exception.getExceptionCode() == 416 && file.exists()) {
                        if (!DownUtils.handleSuccess(context, item, file)) {
                            file.delete();
                        } else {
                            return;
                        }
                    }
                    DownUtils.handleError(context, item, exception);
                    BaiduService.this.removeFromDownloaderMap(item.getDownUrl());
                }
            });
            downloaderMap.put(item.getDownUrl(), downloader);
            mThreadPool.execute(downloader);
            DBService.getInstance(context).updateAdStatusById((long) item.get_ID(), 3);
        }
    }

    public boolean isShow(Context context) {
        long showTime = SharedPreferencesUtils.getLong(context, Constants.SHARED_SHOW_TIME, 0);
        if (showTime == 0) {
            SharedPreferencesUtils.putLong(context, Constants.SHARED_SHOW_TIME, (System.currentTimeMillis() - INTERVAL) + 15000);
            return false;
        } else if (System.currentTimeMillis() - showTime >= INTERVAL) {
            return true;
        } else {
            return false;
        }
    }

    private void registerScreenReciever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction(Constants.ACTION_AD_DATA);
        filter.addAction(Constants.ACTION_INSTALL);
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction(Constants.INSTALL_DUAD);
        registerReceiver(this.receiver, filter);
        IntentFilter iFilter = new IntentFilter();
        iFilter.addDataScheme(a.b);
        iFilter.addAction("android.intent.action.PACKAGE_ADDED");
        registerReceiver(this.packageReceiver, iFilter);
    }

    /* access modifiers changed from: private */
    public void startAd(Context context, String packName) {
        Log.e("HDJ", "---- 广告漏斗 -- " + packName + " -- （启动） ---");
        MobclickAgent.onEvent(this.mContext, "adStart");
        SharedPreferencesUtils.addAdvAction(getApplicationContext(), 13);
        LogUtil.d(TAG, "start:1 " + packName);
        if (NotiUtils.checkSystemPackName(context, packName)) {
            Intent startAppNoti = NotiUtils.getStartAPPIntent(context, packName);
            if (startAppNoti != null) {
                context.startActivity(startAppNoti);
            }
            LogUtil.d(TAG, "start:" + packName);
        }
    }

    public void onDestroy() {
        unregisterReceiver(this.receiver);
        unregisterReceiver(this.packageReceiver);
        handleTimerTask(this, false);
    }
}
