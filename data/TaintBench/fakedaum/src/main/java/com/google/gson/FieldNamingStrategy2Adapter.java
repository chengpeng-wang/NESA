package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;

final class FieldNamingStrategy2Adapter implements FieldNamingStrategy2 {
    private final FieldNamingStrategy adaptee;

    FieldNamingStrategy2Adapter(FieldNamingStrategy adaptee) {
        this.adaptee = (FieldNamingStrategy) C$Gson$Preconditions.checkNotNull(adaptee);
    }

    public String translateName(FieldAttributes f) {
        return this.adaptee.translateName(f.getFieldObject());
    }
}
