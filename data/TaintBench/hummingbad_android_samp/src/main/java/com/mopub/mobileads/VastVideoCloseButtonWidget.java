package com.mopub.mobileads;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils.TruncateAt;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.CloseButtonDrawable;
import com.mopub.mobileads.resource.DrawableConstants.CloseButton;
import com.mopub.network.Networking;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.mopub.volley.toolbox.ImageLoader.ImageContainer;
import com.mopub.volley.toolbox.ImageLoader.ImageListener;

public class VastVideoCloseButtonWidget extends RelativeLayout {
    @NonNull
    private CloseButtonDrawable mCloseButtonDrawable = new CloseButtonDrawable();
    private final int mEdgePadding;
    @NonNull
    private final ImageLoader mImageLoader;
    private final int mImagePadding;
    /* access modifiers changed from: private */
    @NonNull
    public ImageView mImageView;
    private final int mTextRightMargin;
    @NonNull
    private TextView mTextView;
    private final int mWidgetHeight;

    public VastVideoCloseButtonWidget(@NonNull Context context) {
        super(context);
        setId((int) Utils.generateUniqueId());
        this.mEdgePadding = Dips.dipsToIntPixels(16.0f, context);
        this.mImagePadding = Dips.dipsToIntPixels(5.0f, context);
        this.mWidgetHeight = Dips.dipsToIntPixels(46.0f, context);
        this.mTextRightMargin = Dips.dipsToIntPixels(7.0f, context);
        this.mImageLoader = Networking.getImageLoader(context);
        createImageView();
        createTextView();
        LayoutParams layoutParams = new LayoutParams(-2, this.mWidgetHeight);
        layoutParams.addRule(11);
        setLayoutParams(layoutParams);
    }

    private void createImageView() {
        this.mImageView = new ImageView(getContext());
        this.mImageView.setId((int) Utils.generateUniqueId());
        LayoutParams layoutParams = new LayoutParams(this.mWidgetHeight, this.mWidgetHeight);
        layoutParams.addRule(11);
        this.mImageView.setImageDrawable(this.mCloseButtonDrawable);
        this.mImageView.setPadding(this.mImagePadding, this.mImagePadding + this.mEdgePadding, this.mImagePadding + this.mEdgePadding, this.mImagePadding);
        addView(this.mImageView, layoutParams);
    }

    private void createTextView() {
        this.mTextView = new TextView(getContext());
        this.mTextView.setSingleLine();
        this.mTextView.setEllipsize(TruncateAt.END);
        this.mTextView.setTextColor(-1);
        this.mTextView.setTextSize(20.0f);
        this.mTextView.setTypeface(CloseButton.TEXT_TYPEFACE);
        this.mTextView.setText("");
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(0, this.mImageView.getId());
        this.mTextView.setPadding(0, this.mEdgePadding, 0, 0);
        layoutParams.setMargins(0, 0, this.mTextRightMargin, 0);
        addView(this.mTextView, layoutParams);
    }

    /* access modifiers changed from: 0000 */
    public void updateCloseButtonText(@Nullable String text) {
        if (this.mTextView != null) {
            this.mTextView.setText(text);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateCloseButtonIcon(@NonNull final String imageUrl) {
        this.mImageLoader.get(imageUrl, new ImageListener() {
            public void onResponse(ImageContainer imageContainer, boolean isImmediate) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap != null) {
                    VastVideoCloseButtonWidget.this.mImageView.setImageBitmap(bitmap);
                    return;
                }
                MoPubLog.d(String.format("%s returned null bitmap", new Object[]{imageUrl}));
            }

            public void onErrorResponse(VolleyError volleyError) {
                MoPubLog.d("Failed to load image.", volleyError);
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void setOnTouchListenerToContent(@Nullable OnTouchListener onTouchListener) {
        this.mImageView.setOnTouchListener(onTouchListener);
        this.mTextView.setOnTouchListener(onTouchListener);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public ImageView getImageView() {
        return this.mImageView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setImageView(ImageView imageView) {
        this.mImageView = imageView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public TextView getTextView() {
        return this.mTextView;
    }
}
