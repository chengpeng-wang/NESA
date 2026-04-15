package com.cmcm.baseapi;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.cmcm.baseapi.ISharedConfig.OnSharedConfigChangeListener;
import com.cmcm.baseapi.utils.Assure;
import java.util.Set;

public class SharedPreferencesAdapter implements ISharedConfig {
    OnSharedConfigChangeListener mConfigListener;
    final SharedPreferences mPreference;
    OnSharedPreferenceChangeListener mPreferencesListener = new OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (SharedPreferencesAdapter.this.mConfigListener != null) {
                SharedPreferencesAdapter.this.mConfigListener.onSharedConfigChanged(SharedPreferencesAdapter.this);
            }
        }
    };

    SharedPreferencesAdapter(SharedPreferences preferences) {
        Assure.checkNotNull(preferences);
        this.mPreference = preferences;
    }

    public void registerOnSharedConfigChangeListener(OnSharedConfigChangeListener listener) {
        Assure.checkNotNull(listener);
        this.mConfigListener = listener;
        this.mPreference.registerOnSharedPreferenceChangeListener(this.mPreferencesListener);
    }

    public void unregisterOnSharedConfigChangeListener(OnSharedConfigChangeListener listener) {
        Assure.checkEqual(this.mConfigListener, (Object) listener);
        this.mConfigListener = null;
        this.mPreference.unregisterOnSharedPreferenceChangeListener(this.mPreferencesListener);
    }

    public boolean contains(String key) {
        return this.mPreference.contains(key);
    }

    public String getString(String key, String defValue) {
        return this.mPreference.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return this.mPreference.getStringSet(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return this.mPreference.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return this.mPreference.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return this.mPreference.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return this.mPreference.getBoolean(key, defValue);
    }
}
