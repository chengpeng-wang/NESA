package com.cmcm.adsdk.nativead;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.cmcm.adsdk.requestconfig.log.a;
import com.moceanmobile.mast.MASTNativeAdConstants;

public class PicksLoadingActivity extends Activity {
    private static final String TAG = PicksLoadingActivity.class.getSimpleName();
    public static final String TAG_CLOSE_DIALOG = "tag_close_dialog";
    public static boolean mNeedJumpGP = true;
    private ImageView mLoadingCircle;
    private TextView mLoadingTv;
    private TextView mTopGp;
    private ImageView mTopGpImage;

    public static void startLoadingDialog(Context context) {
        mNeedJumpGP = true;
        Intent intent = new Intent(context, PicksLoadingActivity.class);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void closeLoadingDialog(Context context) {
        Intent intent = new Intent(context, PicksLoadingActivity.class);
        intent.putExtra(TAG_CLOSE_DIALOG, true);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a.a(TAG, "oncreate");
        boolean booleanExtra = getIntent().getBooleanExtra(TAG_CLOSE_DIALOG, false);
        a.a(TAG, "isClose" + booleanExtra);
        if (booleanExtra) {
            finish();
            return;
        }
        try {
            setContentView(getResourceId(this, "activity_picks_loading", "layout", getPackageName()));
            a.a(TAG, "setContenView");
            initUI();
        } catch (Exception e) {
            a.d(TAG, "initUi error,the error message = " + e.getMessage());
        }
        a.a(TAG, "init");
        startAnim();
        a.a(TAG, "startAnim");
    }

    private int getResourceId(Context context, String name, String type, String packageName) {
        try {
            return context.getPackageManager().getResourcesForApplication(packageName).getIdentifier(name, type, packageName);
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
    }

    private void initUI() {
        this.mLoadingCircle = (ImageView) findViewById(getResourceId(this, "cmadsdk_loading_cicle", MASTNativeAdConstants.ID_STRING, getPackageName()));
        this.mLoadingTv = (TextView) findViewById(getResourceId(this, "cmadsdk_loading_tv", MASTNativeAdConstants.ID_STRING, getPackageName()));
        this.mTopGp = (TextView) findViewById(getResourceId(this, "cmadsdk_market_top_gp", MASTNativeAdConstants.ID_STRING, getPackageName()));
        this.mTopGpImage = (ImageView) findViewById(getResourceId(this, "cmadsdk_market_top_gp_image", MASTNativeAdConstants.ID_STRING, getPackageName()));
        int width = ((WindowManager) getSystemService("window")).getDefaultDisplay().getWidth();
        LayoutParams layoutParams = this.mTopGp.getLayoutParams();
        layoutParams.height = (int) (((double) width) * 0.13333333333333333d);
        layoutParams.width = width;
        this.mTopGp.setLayoutParams(layoutParams);
        layoutParams = this.mTopGpImage.getLayoutParams();
        int i = (int) (((double) width) * 0.13333333333333333d);
        layoutParams.height = i;
        layoutParams.width = i;
        this.mTopGpImage.setLayoutParams(layoutParams);
        width = (int) (0.06666666666666667d * ((double) width));
        this.mLoadingTv.setPadding(width, 0, width, 0);
        this.mLoadingTv.setText("");
    }

    private void startAnim() {
        this.mLoadingCircle.clearAnimation();
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(800);
        rotateAnimation.setRepeatMode(1);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        this.mLoadingCircle.startAnimation(rotateAnimation);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        a.a(TAG, "onNewIntent");
        if (intent.getBooleanExtra(TAG_CLOSE_DIALOG, false)) {
            finish();
        }
    }

    public void onBackPressed() {
        mNeedJumpGP = false;
        a.a(TAG, "onBackPressed");
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mLoadingCircle != null) {
            this.mLoadingCircle.clearAnimation();
        }
    }
}
