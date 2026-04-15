package com.address.core;

import android.content.SharedPreferences.Editor;

public class Settings {
    public void set(String name, String value) {
        Editor edit = RunService.getService().getSharedPreferences("cmmn", 0).edit();
        edit.putString(name, value);
        edit.commit();
    }

    public String get(String name) {
        return RunService.getService().getSharedPreferences("cmmn", 0).getString(name, "");
    }
}
