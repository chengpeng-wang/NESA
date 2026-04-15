package android.support.v4.text;

import android.support.v4.widget.ViewDragHelper;
import java.nio.CharBuffer;
import java.util.Locale;

public class TextDirectionHeuristicsCompat {
    public static final TextDirectionHeuristicCompat ANYRTL_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
    public static final TextDirectionHeuristicCompat LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    public static final TextDirectionHeuristicCompat LTR;
    public static final TextDirectionHeuristicCompat RTL;
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;

    private static class AnyStrong implements TextDirectionAlgorithm {
        public static final AnyStrong INSTANCE_LTR;
        public static final AnyStrong INSTANCE_RTL;
        private final boolean mLookForRtl;

        public int checkRtl(CharSequence charSequence, int i, int i2) {
            CharSequence charSequence2 = charSequence;
            int i3 = i;
            Object obj = null;
            int i4 = i3 + i2;
            for (int i5 = i3; i5 < i4; i5++) {
                int obj2;
                switch (TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(charSequence2.charAt(i5)))) {
                    case 0:
                        if (!this.mLookForRtl) {
                            obj2 = 1;
                            break;
                        }
                        return 0;
                    case 1:
                        if (this.mLookForRtl) {
                            obj2 = 1;
                            break;
                        }
                        return 1;
                    default:
                        break;
                }
            }
            if (obj2 == null) {
                return 2;
            }
            return this.mLookForRtl ? 1 : 0;
        }

        private AnyStrong(boolean z) {
            this.mLookForRtl = z;
        }

        static {
            AnyStrong anyStrong = r3;
            AnyStrong anyStrong2 = new AnyStrong(true);
            INSTANCE_RTL = anyStrong;
            anyStrong = r3;
            anyStrong2 = new AnyStrong(false);
            INSTANCE_LTR = anyStrong;
        }
    }

    private static class FirstStrong implements TextDirectionAlgorithm {
        public static final FirstStrong INSTANCE;

        public int checkRtl(CharSequence charSequence, int i, int i2) {
            CharSequence charSequence2 = charSequence;
            int i3 = i;
            int i4 = 2;
            int i5 = i3 + i2;
            for (int i6 = i3; i6 < i5 && i4 == 2; i6++) {
                i4 = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(charSequence2.charAt(i6)));
            }
            return i4;
        }

        private FirstStrong() {
        }

        static {
            FirstStrong firstStrong = r2;
            FirstStrong firstStrong2 = new FirstStrong();
            INSTANCE = firstStrong;
        }
    }

    private interface TextDirectionAlgorithm {
        int checkRtl(CharSequence charSequence, int i, int i2);
    }

    private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
        private final TextDirectionAlgorithm mAlgorithm;

        public abstract boolean defaultIsRtl();

        public TextDirectionHeuristicImpl(TextDirectionAlgorithm textDirectionAlgorithm) {
            this.mAlgorithm = textDirectionAlgorithm;
        }

        public boolean isRtl(char[] cArr, int i, int i2) {
            return isRtl(CharBuffer.wrap(cArr), i, i2);
        }

        public boolean isRtl(CharSequence charSequence, int i, int i2) {
            CharSequence charSequence2 = charSequence;
            int i3 = i;
            int i4 = i2;
            if (charSequence2 == null || i3 < 0 || i4 < 0 || charSequence2.length() - i4 < i3) {
                IllegalArgumentException illegalArgumentException = r8;
                IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException();
                throw illegalArgumentException;
            } else if (this.mAlgorithm == null) {
                return defaultIsRtl();
            } else {
                return doCheck(charSequence2, i3, i4);
            }
        }

        private boolean doCheck(CharSequence charSequence, int i, int i2) {
            switch (this.mAlgorithm.checkRtl(charSequence, i, i2)) {
                case 0:
                    return true;
                case 1:
                    return false;
                default:
                    return defaultIsRtl();
            }
        }
    }

    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        /* synthetic */ TextDirectionHeuristicInternal(TextDirectionAlgorithm textDirectionAlgorithm, boolean z, AnonymousClass1 anonymousClass1) {
            AnonymousClass1 anonymousClass12 = anonymousClass1;
            this(textDirectionAlgorithm, z);
        }

        private TextDirectionHeuristicInternal(TextDirectionAlgorithm textDirectionAlgorithm, boolean z) {
            boolean z2 = z;
            super(textDirectionAlgorithm);
            this.mDefaultIsRtl = z2;
        }

        /* access modifiers changed from: protected */
        public boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl {
        public static final TextDirectionHeuristicLocale INSTANCE;

        public TextDirectionHeuristicLocale() {
            super(null);
        }

        /* access modifiers changed from: protected */
        public boolean defaultIsRtl() {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        }

        static {
            TextDirectionHeuristicLocale textDirectionHeuristicLocale = r2;
            TextDirectionHeuristicLocale textDirectionHeuristicLocale2 = new TextDirectionHeuristicLocale();
            INSTANCE = textDirectionHeuristicLocale;
        }
    }

    public TextDirectionHeuristicsCompat() {
    }

    static {
        TextDirectionHeuristicInternal textDirectionHeuristicInternal = r5;
        TextDirectionHeuristicInternal textDirectionHeuristicInternal2 = new TextDirectionHeuristicInternal(null, false, null);
        LTR = textDirectionHeuristicInternal;
        textDirectionHeuristicInternal = r5;
        textDirectionHeuristicInternal2 = new TextDirectionHeuristicInternal(null, true, null);
        RTL = textDirectionHeuristicInternal;
        textDirectionHeuristicInternal = r5;
        textDirectionHeuristicInternal2 = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false, null);
        FIRSTSTRONG_LTR = textDirectionHeuristicInternal;
        textDirectionHeuristicInternal = r5;
        textDirectionHeuristicInternal2 = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true, null);
        FIRSTSTRONG_RTL = textDirectionHeuristicInternal;
        textDirectionHeuristicInternal = r5;
        textDirectionHeuristicInternal2 = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false, null);
        ANYRTL_LTR = textDirectionHeuristicInternal;
    }

    /* access modifiers changed from: private|static */
    public static int isRtlText(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
            case 2:
                return 0;
            default:
                return 2;
        }
    }

    /* access modifiers changed from: private|static */
    public static int isRtlTextOrFormat(int i) {
        switch (i) {
            case 0:
            case 14:
            case ViewDragHelper.EDGE_ALL /*15*/:
                return 1;
            case 1:
            case 2:
            case 16:
            case 17:
                return 0;
            default:
                return 2;
        }
    }
}
