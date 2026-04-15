package com.moceanmobile.mast.bean;

public class ImageAssetRequest extends AssetRequest {
    public int height;
    public ImageAssetTypes imageType = null;
    public int width;

    public ImageAssetTypes getImageType() {
        return this.imageType;
    }

    public void setImageType(ImageAssetTypes imageType) {
        this.imageType = imageType;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
