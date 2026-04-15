package com.moceanmobile.mast.bean;

public class DataAssetResponse extends AssetResponse {
    public DataAssetTypes dataAssetType = null;
    public String value;

    public DataAssetTypes getDataAssetType() {
        return this.dataAssetType;
    }

    public void setDataAssetType(DataAssetTypes dataAssetType) {
        this.dataAssetType = dataAssetType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
