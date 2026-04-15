package com.googleprojects.mm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class JHDataManager {
    private static final String APP_REFERENCES = "jhgj_preferences";
    private SharedPreferences preferences = null;

    public JHDataManager(Context context) {
        this.preferences = context.getSharedPreferences(APP_REFERENCES, 0);
    }

    public boolean setEnabled(String enabled) {
        Editor edit = this.preferences.edit();
        edit.putString("enabled", enabled);
        return edit.commit();
    }

    public String getEnabled() {
        return this.preferences.getString("enabled", MMMailContentUtil.MM_MESSAGE_SUBJECT);
    }

    public boolean isEnabled() {
        return getEnabled().equalsIgnoreCase("1");
    }
}
