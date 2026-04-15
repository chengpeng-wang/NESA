package com.duapps.ad;

import android.text.TextUtils;

public class AdError {
    public static final AdError INTERNAL_ERROR = new AdError(2001, "Internal Error");
    public static final int INTERNAL_ERROR_CODE = 2001;
    public static final AdError LOAD_TOO_FREQUENTLY = new AdError(1002, "Ad was re-loaded too frequently");
    public static final int LOAD_TOO_FREQUENTLY_ERROR_CODE = 1002;
    public static final AdError MISSING_PROPERTIES = new AdError(2002, "Native ad failed to load due to missing properties");
    public static final AdError NETWORK_ERROR = new AdError(1000, "Network Error");
    public static final int NETWORK_ERROR_CODE = 1000;
    public static final AdError NO_FILL = new AdError(1001, "No Fill");
    public static final int NO_FILL_ERROR_CODE = 1001;
    public static final AdError SERVER_ERROR = new AdError(2000, "Server Error");
    public static final int SERVER_ERROR_CODE = 2000;
    public static final int TIME_OUT_CODE = 3000;
    public static final AdError TIME_OUT_ERROR = new AdError(TIME_OUT_CODE, "Time Out");
    public static final AdError UNKNOW_ERROR = new AdError(UNKNOW_ERROR_CODE, "unknow error");
    public static final int UNKNOW_ERROR_CODE = 3001;
    private final int errorCode;
    private final String errorMessage;

    public AdError(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            str = "unknown error";
        }
        this.errorCode = i;
        this.errorMessage = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
