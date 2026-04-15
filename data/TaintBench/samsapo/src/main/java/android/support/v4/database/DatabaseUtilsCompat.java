package android.support.v4.database;

import android.text.TextUtils;

public class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
    }

    public static String concatenateWhere(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        if (TextUtils.isEmpty(str3)) {
            return str4;
        }
        if (TextUtils.isEmpty(str4)) {
            return str3;
        }
        StringBuilder stringBuilder = r4;
        StringBuilder stringBuilder2 = new StringBuilder();
        return stringBuilder.append("(").append(str3).append(") AND (").append(str4).append(")").toString();
    }

    public static String[] appendSelectionArgs(String[] strArr, String[] strArr2) {
        Object obj = strArr;
        Object obj2 = strArr2;
        if (obj == null || obj.length == 0) {
            return obj2;
        }
        Object obj3 = new String[(obj.length + obj2.length)];
        System.arraycopy(obj, 0, obj3, 0, obj.length);
        System.arraycopy(obj2, 0, obj3, obj.length, obj2.length);
        return obj3;
    }
}
