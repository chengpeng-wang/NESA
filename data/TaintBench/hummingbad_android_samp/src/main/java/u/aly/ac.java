package u.aly;

/* compiled from: SDKType */
public enum ac implements aD {
    ANDROID(0),
    IOS(1),
    WINDOWS_PHONE(2),
    WINDOWS_RT(3);
    
    private final int e;

    private ac(int i) {
        this.e = i;
    }

    public int a() {
        return this.e;
    }

    public static ac a(int i) {
        switch (i) {
            case 0:
                return ANDROID;
            case 1:
                return IOS;
            case 2:
                return WINDOWS_PHONE;
            case 3:
                return WINDOWS_RT;
            default:
                return null;
        }
    }
}
