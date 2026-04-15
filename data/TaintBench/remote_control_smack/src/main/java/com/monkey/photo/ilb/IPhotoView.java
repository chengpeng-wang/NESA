package com.monkey.photo.ilb;

import android.graphics.RectF;
import android.view.View.OnLongClickListener;
import android.widget.ImageView.ScaleType;
import com.monkey.photo.ilb.PhotoViewAttacher.OnMatrixChangedListener;
import com.monkey.photo.ilb.PhotoViewAttacher.OnPhotoTapListener;
import com.monkey.photo.ilb.PhotoViewAttacher.OnViewTapListener;

public interface IPhotoView {
    boolean canZoom();

    RectF getDisplayRect();

    float getMaxScale();

    float getMidScale();

    float getMinScale();

    float getScale();

    ScaleType getScaleType();

    void setAllowParentInterceptOnEdge(boolean z);

    void setMaxScale(float f);

    void setMidScale(float f);

    void setMinScale(float f);

    void setOnLongClickListener(OnLongClickListener onLongClickListener);

    void setOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener);

    void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener);

    void setOnViewTapListener(OnViewTapListener onViewTapListener);

    void setScaleType(ScaleType scaleType);

    void setZoomable(boolean z);

    void zoomTo(float f, float f2, float f3);
}
