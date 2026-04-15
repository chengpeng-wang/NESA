package android.support.v4.util;

public class DebugUtils {
    public DebugUtils() {
    }

    public static void buildShortClassTag(Object obj, StringBuilder stringBuilder) {
        Object obj2 = obj;
        StringBuilder stringBuilder2 = stringBuilder;
        StringBuilder append;
        if (obj2 == null) {
            append = stringBuilder2.append("null");
            return;
        }
        String simpleName = obj2.getClass().getSimpleName();
        if (simpleName == null || simpleName.length() <= 0) {
            simpleName = obj2.getClass().getName();
            int lastIndexOf = simpleName.lastIndexOf(46);
            if (lastIndexOf > 0) {
                simpleName = simpleName.substring(lastIndexOf + 1);
            }
        }
        append = stringBuilder2.append(simpleName);
        append = stringBuilder2.append('{');
        append = stringBuilder2.append(Integer.toHexString(System.identityHashCode(obj2)));
    }
}
