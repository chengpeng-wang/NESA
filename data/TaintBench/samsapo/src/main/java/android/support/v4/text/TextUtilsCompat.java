package android.support.v4.text;

import java.util.Locale;

public class TextUtilsCompat {
    private static String ARAB_SCRIPT_SUBTAG = "Arab";
    private static String HEBR_SCRIPT_SUBTAG = "Hebr";
    public static final Locale ROOT;

    public TextUtilsCompat() {
    }

    public static String htmlEncode(String str) {
        String str2 = str;
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = stringBuilder;
        for (int i = 0; i < str2.length(); i++) {
            char charAt = str2.charAt(i);
            switch (charAt) {
                case '\"':
                    stringBuilder = stringBuilder3.append("&quot;");
                    break;
                case '&':
                    stringBuilder = stringBuilder3.append("&amp;");
                    break;
                case '\'':
                    stringBuilder = stringBuilder3.append("&#39;");
                    break;
                case '<':
                    stringBuilder = stringBuilder3.append("&lt;");
                    break;
                case '>':
                    stringBuilder = stringBuilder3.append("&gt;");
                    break;
                default:
                    stringBuilder = stringBuilder3.append(charAt);
                    break;
            }
        }
        return stringBuilder3.toString();
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        Locale locale2 = locale;
        if (!(locale2 == null || locale2.equals(ROOT))) {
            String script = ICUCompat.getScript(ICUCompat.addLikelySubtags(locale2.toString()));
            if (script == null) {
                return getLayoutDirectionFromFirstChar(locale2);
            }
            if (script.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || script.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) {
                return 1;
            }
        }
        return 0;
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        Locale locale2 = locale;
        switch (Character.getDirectionality(locale2.getDisplayName(locale2).charAt(0))) {
            case (byte) 1:
            case (byte) 2:
                return 1;
            default:
                return 0;
        }
    }

    static {
        Locale locale = r4;
        Locale locale2 = new Locale("", "");
        ROOT = locale;
    }
}
