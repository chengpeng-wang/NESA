package com.moceanmobile.mast.bean;

public enum ImageAssetTypes {
    icon(1),
    logo(2),
    main(3);
    
    private int type;

    private ImageAssetTypes(int type) {
        this.type = type;
    }

    public final int getTypeId() {
        return this.type;
    }
}
