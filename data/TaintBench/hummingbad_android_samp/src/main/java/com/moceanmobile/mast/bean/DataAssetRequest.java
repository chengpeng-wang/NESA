package com.moceanmobile.mast.bean;

public class DataAssetRequest extends AssetRequest {
    public DataAssetTypes dataAssetType = null;
    public int length;

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DataAssetTypes getDataAssetType() {
        return this.dataAssetType;
    }

    public void setDataAssetType(DataAssetTypes dataAssetType) {
        this.dataAssetType = dataAssetType;
    }
}
