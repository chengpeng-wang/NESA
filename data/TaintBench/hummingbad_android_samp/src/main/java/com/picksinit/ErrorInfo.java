package com.picksinit;

public class ErrorInfo {
    public static final int ERROR_CODE_AD_DATA_IS_NULL = 401;
    public static final int ERROR_CODE_NO_FILL = 400;
    public static final int ERROR_CODE_NO_NETWORK = 300;
    public static final int ERROR_CODE_PARAMS = 500;
    private int code;
    private String info;
    private int posid;

    public ErrorInfo(int code, String info, int posid) {
        this.code = code;
        this.info = info;
        this.posid = posid;
    }

    public String toString() {
        return "ErrorInfo{code=" + this.code + ", info='" + this.info + '\'' + ", posid=" + this.posid + '}';
    }
}
