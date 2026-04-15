package com.moceanmobile.mast.bean;

public enum DataAssetTypes {
    sponsored(1),
    desc(2),
    rating(3),
    likes(4),
    downloads(5),
    price(6),
    saleprice(7),
    phone(8),
    address(9),
    desc2(10),
    displayurl(11),
    ctatext(12),
    OTHER(501);
    
    private int type;

    private DataAssetTypes(int type) {
        this.type = type;
    }

    public final int getTypeId() {
        return this.type;
    }
}
