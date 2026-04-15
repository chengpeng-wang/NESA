package fm.xtube.core;

public class Server {
    public static String DEVICE_ID;
    public static String RIGGED_TIME;

    public static void setDeviceId(String deviceId) {
        DEVICE_ID = deviceId;
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static void setRiggedTime(String time) {
        RIGGED_TIME = time;
    }

    public static String getRiggedTime() {
        return RIGGED_TIME;
    }
}
