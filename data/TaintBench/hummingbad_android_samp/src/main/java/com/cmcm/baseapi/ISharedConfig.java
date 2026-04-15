package com.cmcm.baseapi;

import java.util.Set;

public interface ISharedConfig {

    public interface OnSharedConfigChangeListener {
        void onSharedConfigChanged(ISharedConfig iSharedConfig);
    }

    boolean contains(String str);

    boolean getBoolean(String str, boolean z);

    float getFloat(String str, float f);

    int getInt(String str, int i);

    long getLong(String str, long j);

    String getString(String str, String str2);

    Set<String> getStringSet(String str, Set<String> set);

    void registerOnSharedConfigChangeListener(OnSharedConfigChangeListener onSharedConfigChangeListener);

    void unregisterOnSharedConfigChangeListener(OnSharedConfigChangeListener onSharedConfigChangeListener);
}
