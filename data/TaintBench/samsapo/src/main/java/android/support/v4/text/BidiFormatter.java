package android.support.v4.text;

import android.support.v4.widget.ViewDragHelper;
import java.util.Locale;

public final class BidiFormatter {
    private static final int DEFAULT_FLAGS = 2;
    /* access modifiers changed from: private|static|final */
    public static final BidiFormatter DEFAULT_LTR_INSTANCE;
    /* access modifiers changed from: private|static|final */
    public static final BidiFormatter DEFAULT_RTL_INSTANCE;
    /* access modifiers changed from: private|static */
    public static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '‪';
    private static final char LRM = '‎';
    private static final String LRM_STRING = Character.toString(LRM);
    private static final char PDF = '‬';
    private static final char RLE = '‫';
    private static final char RLM = '‏';
    private static final String RLM_STRING = Character.toString(RLM);
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;

    public static final class Builder {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;

        public Builder() {
            initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
        }

        public Builder(boolean z) {
            initialize(z);
        }

        public Builder(Locale locale) {
            initialize(BidiFormatter.isRtlLocale(locale));
        }

        private void initialize(boolean z) {
            this.mIsRtlContext = z;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }

        public Builder stereoReset(boolean z) {
            if (z) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            return this;
        }

        public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
            this.mTextDirectionHeuristicCompat = textDirectionHeuristicCompat;
            return this;
        }

        private static BidiFormatter getDefaultInstanceFromContext(boolean z) {
            return z ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
        }

        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            BidiFormatter bidiFormatter = r7;
            BidiFormatter bidiFormatter2 = new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat, null);
            return bidiFormatter;
        }
    }

    private static class DirectionalityEstimator {
        private static final byte[] DIR_TYPE_CACHE = new byte[DIR_TYPE_CACHE_SIZE];
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final String text;

        static {
            for (int i = 0; i < DIR_TYPE_CACHE_SIZE; i++) {
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        DirectionalityEstimator(String str, boolean z) {
            String str2 = str;
            boolean z2 = z;
            this.text = str2;
            this.isHtml = z2;
            this.length = str2.length();
        }

        /* access modifiers changed from: 0000 */
        public int getEntryDir() {
            this.charIndex = 0;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (this.charIndex < this.length && i3 == 0) {
                switch (dirTypeForward()) {
                    case (byte) 0:
                        if (i != 0) {
                            i3 = i;
                            break;
                        }
                        return -1;
                    case (byte) 1:
                    case (byte) 2:
                        if (i != 0) {
                            i3 = i;
                            break;
                        }
                        return 1;
                    case (byte) 9:
                        break;
                    case (byte) 14:
                    case ViewDragHelper.EDGE_ALL /*15*/:
                        i++;
                        i2 = -1;
                        break;
                    case (byte) 16:
                    case (byte) 17:
                        i++;
                        i2 = 1;
                        break;
                    case (byte) 18:
                        i--;
                        i2 = 0;
                        break;
                    default:
                        i3 = i;
                        break;
                }
            }
            if (i3 == 0) {
                return 0;
            }
            if (i2 != 0) {
                return i2;
            }
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case (byte) 14:
                    case ViewDragHelper.EDGE_ALL /*15*/:
                        if (i3 != i) {
                            i--;
                            break;
                        }
                        return -1;
                    case (byte) 16:
                    case (byte) 17:
                        if (i3 != i) {
                            i--;
                            break;
                        }
                        return 1;
                    case (byte) 18:
                        i++;
                        break;
                    default:
                        break;
                }
            }
            return 0;
        }

        /* access modifiers changed from: 0000 */
        public int getExitDir() {
            this.charIndex = this.length;
            int i = 0;
            int i2 = 0;
            while (this.charIndex > 0) {
                switch (dirTypeBackward()) {
                    case (byte) 0:
                        if (i != 0) {
                            if (i2 != 0) {
                                break;
                            }
                            i2 = i;
                            break;
                        }
                        return -1;
                    case (byte) 1:
                    case (byte) 2:
                        if (i != 0) {
                            if (i2 != 0) {
                                break;
                            }
                            i2 = i;
                            break;
                        }
                        return 1;
                    case (byte) 9:
                        break;
                    case (byte) 14:
                    case ViewDragHelper.EDGE_ALL /*15*/:
                        if (i2 != i) {
                            i--;
                            break;
                        }
                        return -1;
                    case (byte) 16:
                    case (byte) 17:
                        if (i2 != i) {
                            i--;
                            break;
                        }
                        return 1;
                    case (byte) 18:
                        i++;
                        break;
                    default:
                        if (i2 != 0) {
                            break;
                        }
                        i2 = i;
                        break;
                }
            }
            return 0;
        }

        private static byte getCachedDirectionality(char c) {
            char c2 = c;
            return c2 < DIR_TYPE_CACHE_SIZE ? DIR_TYPE_CACHE[c2] : Character.getDirectionality(c2);
        }

        /* access modifiers changed from: 0000 */
        public byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            if (Character.isHighSurrogate(this.lastChar)) {
                int codePointAt = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePointAt);
                return Character.getDirectionality(codePointAt);
            }
            this.charIndex++;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                if (this.lastChar == '<') {
                    cachedDirectionality = skipTagForward();
                } else if (this.lastChar == '&') {
                    cachedDirectionality = skipEntityForward();
                }
            }
            return cachedDirectionality;
        }

        /* access modifiers changed from: 0000 */
        public byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            if (Character.isLowSurrogate(this.lastChar)) {
                int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.charIndex--;
            byte cachedDirectionality = getCachedDirectionality(this.lastChar);
            if (this.isHtml) {
                if (this.lastChar == '>') {
                    cachedDirectionality = skipTagBackward();
                } else if (this.lastChar == ';') {
                    cachedDirectionality = skipEntityBackward();
                }
            }
            return cachedDirectionality;
        }

        private byte skipTagForward() {
            int i = this.charIndex;
            while (this.charIndex < this.length) {
                String str = this.text;
                int i2 = this.charIndex;
                int i3 = i2;
                this.charIndex = i2 + 1;
                this.lastChar = str.charAt(i3);
                if (this.lastChar == '>') {
                    return (byte) 12;
                }
                if (this.lastChar == '\"' || this.lastChar == '\'') {
                    char c = this.lastChar;
                    while (this.charIndex < this.length) {
                        str = this.text;
                        i2 = this.charIndex;
                        i3 = i2;
                        this.charIndex = i2 + 1;
                        char charAt = str.charAt(i3);
                        char c2 = charAt;
                        this.lastChar = charAt;
                        if (c2 == c) {
                            break;
                        }
                    }
                }
            }
            this.charIndex = i;
            this.lastChar = '<';
            return (byte) 13;
        }

        private byte skipTagBackward() {
            int i = this.charIndex;
            while (this.charIndex > 0) {
                String str = this.text;
                int i2 = this.charIndex - 1;
                int i3 = i2;
                this.charIndex = i2;
                this.lastChar = str.charAt(i3);
                if (this.lastChar == '<') {
                    return (byte) 12;
                }
                if (this.lastChar == '>') {
                    break;
                } else if (this.lastChar == '\"' || this.lastChar == '\'') {
                    char c = this.lastChar;
                    while (this.charIndex > 0) {
                        str = this.text;
                        i2 = this.charIndex - 1;
                        i3 = i2;
                        this.charIndex = i2;
                        char charAt = str.charAt(i3);
                        char c2 = charAt;
                        this.lastChar = charAt;
                        if (c2 == c) {
                            break;
                        }
                    }
                }
            }
            this.charIndex = i;
            this.lastChar = '>';
            return (byte) 13;
        }

        private byte skipEntityForward() {
            while (this.charIndex < this.length) {
                String str = this.text;
                int i = this.charIndex;
                int i2 = i;
                this.charIndex = i + 1;
                char charAt = str.charAt(i2);
                char c = charAt;
                this.lastChar = charAt;
                if (c == ';') {
                    break;
                }
            }
            return (byte) 12;
        }

        private byte skipEntityBackward() {
            int i = this.charIndex;
            while (this.charIndex > 0) {
                String str = this.text;
                int i2 = this.charIndex - 1;
                int i3 = i2;
                this.charIndex = i2;
                this.lastChar = str.charAt(i3);
                if (this.lastChar != '&') {
                    if (this.lastChar == ';') {
                        break;
                    }
                }
                return (byte) 12;
            }
            this.charIndex = i;
            this.lastChar = ';';
            return (byte) 13;
        }
    }

    /* synthetic */ BidiFormatter(boolean z, int i, TextDirectionHeuristicCompat textDirectionHeuristicCompat, AnonymousClass1 anonymousClass1) {
        AnonymousClass1 anonymousClass12 = anonymousClass1;
        this(z, i, textDirectionHeuristicCompat);
    }

    static {
        BidiFormatter bidiFormatter = r5;
        BidiFormatter bidiFormatter2 = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_LTR_INSTANCE = bidiFormatter;
        bidiFormatter = r5;
        bidiFormatter2 = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = bidiFormatter;
    }

    public static BidiFormatter getInstance() {
        Builder builder = r2;
        Builder builder2 = new Builder();
        return builder.build();
    }

    public static BidiFormatter getInstance(boolean z) {
        Builder builder = r4;
        Builder builder2 = new Builder(z);
        return builder.build();
    }

    public static BidiFormatter getInstance(Locale locale) {
        Builder builder = r4;
        Builder builder2 = new Builder(locale);
        return builder.build();
    }

    private BidiFormatter(boolean z, int i, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        int i2 = i;
        TextDirectionHeuristicCompat textDirectionHeuristicCompat2 = textDirectionHeuristicCompat;
        this.mIsRtlContext = z;
        this.mFlags = i2;
        this.mDefaultTextDirectionHeuristicCompat = textDirectionHeuristicCompat2;
    }

    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }

    public boolean getStereoReset() {
        return (this.mFlags & 2) != 0;
    }

    private String markAfter(String str, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        String str2 = str;
        boolean isRtl = textDirectionHeuristicCompat.isRtl((CharSequence) str2, 0, str2.length());
        if (!this.mIsRtlContext && (isRtl || getExitDir(str2) == 1)) {
            return LRM_STRING;
        }
        if (!this.mIsRtlContext || (isRtl && getExitDir(str2) != -1)) {
            return EMPTY_STRING;
        }
        return RLM_STRING;
    }

    private String markBefore(String str, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        String str2 = str;
        boolean isRtl = textDirectionHeuristicCompat.isRtl((CharSequence) str2, 0, str2.length());
        if (!this.mIsRtlContext && (isRtl || getEntryDir(str2) == 1)) {
            return LRM_STRING;
        }
        if (!this.mIsRtlContext || (isRtl && getEntryDir(str2) != -1)) {
            return EMPTY_STRING;
        }
        return RLM_STRING;
    }

    public boolean isRtl(String str) {
        String str2 = str;
        return this.mDefaultTextDirectionHeuristicCompat.isRtl((CharSequence) str2, 0, str2.length());
    }

    public String unicodeWrap(String str, TextDirectionHeuristicCompat textDirectionHeuristicCompat, boolean z) {
        String str2 = str;
        boolean z2 = z;
        boolean isRtl = textDirectionHeuristicCompat.isRtl((CharSequence) str2, 0, str2.length());
        StringBuilder stringBuilder = r10;
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = stringBuilder;
        if (getStereoReset() && z2) {
            stringBuilder = stringBuilder3.append(markBefore(str2, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        if (isRtl != this.mIsRtlContext) {
            stringBuilder = stringBuilder3.append(isRtl ? RLE : LRE);
            stringBuilder = stringBuilder3.append(str2);
            stringBuilder = stringBuilder3.append(PDF);
        } else {
            stringBuilder = stringBuilder3.append(str2);
        }
        if (z2) {
            stringBuilder = stringBuilder3.append(markAfter(str2, isRtl ? TextDirectionHeuristicsCompat.RTL : TextDirectionHeuristicsCompat.LTR));
        }
        return stringBuilder3.toString();
    }

    public String unicodeWrap(String str, TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return unicodeWrap(str, textDirectionHeuristicCompat, true);
    }

    public String unicodeWrap(String str, boolean z) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, z);
    }

    public String unicodeWrap(String str) {
        return unicodeWrap(str, this.mDefaultTextDirectionHeuristicCompat, true);
    }

    /* access modifiers changed from: private|static */
    public static boolean isRtlLocale(Locale locale) {
        return TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1;
    }

    private static int getExitDir(String str) {
        DirectionalityEstimator directionalityEstimator = r5;
        DirectionalityEstimator directionalityEstimator2 = new DirectionalityEstimator(str, false);
        return directionalityEstimator.getExitDir();
    }

    private static int getEntryDir(String str) {
        DirectionalityEstimator directionalityEstimator = r5;
        DirectionalityEstimator directionalityEstimator2 = new DirectionalityEstimator(str, false);
        return directionalityEstimator.getEntryDir();
    }
}
