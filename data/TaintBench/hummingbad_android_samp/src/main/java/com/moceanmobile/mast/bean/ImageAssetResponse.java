package com.moceanmobile.mast.bean;

import com.moceanmobile.mast.MASTNativeAd.Image;

public class ImageAssetResponse extends AssetResponse {
    public Image image;
    public ImageAssetTypes imageType;

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ImageAssetTypes getImageType() {
        return this.imageType;
    }

    public void setImageType(ImageAssetTypes imageType) {
        this.imageType = imageType;
    }
}
