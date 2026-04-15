package com.moceanmobile.mast.bean;

public abstract class AssetRequest {
    public int assetId;
    public boolean isRequired = false;

    public int getAssetId() {
        return this.assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
}
