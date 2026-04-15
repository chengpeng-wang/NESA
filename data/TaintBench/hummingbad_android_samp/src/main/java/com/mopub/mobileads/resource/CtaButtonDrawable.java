package com.mopub.mobileads.resource;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;

public class CtaButtonDrawable extends BaseWidgetDrawable {
    @NonNull
    private final Paint mBackgroundPaint = new Paint();
    private final int mButtonCornerRadius;
    @NonNull
    private final RectF mButtonRect;
    private String mCtaText;
    @NonNull
    private final Paint mOutlinePaint;
    @NonNull
    private final Paint mTextPaint;
    @NonNull
    private final Rect mTextRect;

    public CtaButtonDrawable(@NonNull Context context) {
        int dipsToIntPixels = Dips.dipsToIntPixels(2.0f, context);
        float dipsToFloatPixels = Dips.dipsToFloatPixels(20.0f, context);
        this.mBackgroundPaint.setColor(-16777216);
        this.mBackgroundPaint.setAlpha(51);
        this.mBackgroundPaint.setStyle(CtaButton.BACKGROUND_STYLE);
        this.mBackgroundPaint.setAntiAlias(true);
        this.mOutlinePaint = new Paint();
        this.mOutlinePaint.setColor(-1);
        this.mOutlinePaint.setAlpha(51);
        this.mOutlinePaint.setStyle(CtaButton.OUTLINE_STYLE);
        this.mOutlinePaint.setStrokeWidth((float) dipsToIntPixels);
        this.mOutlinePaint.setAntiAlias(true);
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTextAlign(CtaButton.TEXT_ALIGN);
        this.mTextPaint.setTypeface(CtaButton.TEXT_TYPEFACE);
        this.mTextPaint.setTextSize(dipsToFloatPixels);
        this.mTextPaint.setAntiAlias(true);
        this.mTextRect = new Rect();
        this.mCtaText = CtaButton.DEFAULT_CTA_TEXT;
        this.mButtonRect = new RectF();
        this.mButtonCornerRadius = Dips.dipsToIntPixels(6.0f, context);
    }

    public void draw(Canvas canvas) {
        this.mButtonRect.set(getBounds());
        canvas.drawRoundRect(this.mButtonRect, (float) this.mButtonCornerRadius, (float) this.mButtonCornerRadius, this.mBackgroundPaint);
        canvas.drawRoundRect(this.mButtonRect, (float) this.mButtonCornerRadius, (float) this.mButtonCornerRadius, this.mOutlinePaint);
        drawTextWithinBounds(canvas, this.mTextPaint, this.mTextRect, this.mCtaText);
    }

    public void setCtaText(@NonNull String ctaText) {
        this.mCtaText = ctaText;
        invalidateSelf();
    }

    @Deprecated
    @VisibleForTesting
    public String getCtaText() {
        return this.mCtaText;
    }
}
