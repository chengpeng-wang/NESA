package com.cleanmaster.util;

/* compiled from: SystemProperties */
public class u {
    public static String a(String str) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{str});
        } catch (Exception e) {
            return "";
        }
    }
}
