package exts.whats.billing;

import android.text.TextUtils;
import exts.whats.utils.Lists;
import java.util.ArrayList;
import java.util.Arrays;

public enum CreditCardType {
    JCB(5, 3, new String[]{"3528-3589"}, new int[]{4, 4, 4, 4}),
    DISCOVER(4, 3, new String[]{"6011", "650"}, new int[]{4, 4, 4, 4}),
    AMEX(3, 4, new String[]{"34", "37"}, new int[]{4, 6, 5}),
    MC(2, 3, new String[]{"51-55"}, new int[]{4, 4, 4, 4}),
    VISA(1, 3, new String[]{"4"}, new int[]{4, 4, 4, 4});
    
    public final int cvcLength;
    public final int[] groupLengths;
    public final int length;
    public final String[] numberPrefixRanges;
    public final int protobufType;

    private CreditCardType(int paramInt1, int paramInt2, String[] paramArrayOfString, int[] paramArrayOfInt) {
        this.protobufType = paramInt1;
        this.length = arraySum(paramArrayOfInt);
        this.cvcLength = paramInt2;
        this.numberPrefixRanges = paramArrayOfString;
        this.groupLengths = paramArrayOfInt;
    }

    private static int arraySum(int[] paramArrayOfInt) {
        int i = 0;
        for (int i2 : paramArrayOfInt) {
            i += i2;
        }
        return i;
    }

    public static int getMaxCvcLength() {
        int i = Integer.MIN_VALUE;
        for (CreditCardType creditCardType : values()) {
            i = Math.max(i, creditCardType.cvcLength);
        }
        return i;
    }

    public static CreditCardType getTypeForNumber(String paramString) {
        for (CreditCardType localCreditCardType : values()) {
            if (localCreditCardType.isValidNumber(paramString)) {
                return localCreditCardType;
            }
        }
        return null;
    }

    public static CreditCardType getTypeForPrefix(String paramString) {
        for (CreditCardType localCreditCardType : values()) {
            if (localCreditCardType.isValidPrefix(paramString)) {
                return localCreditCardType;
            }
        }
        return null;
    }

    public static String normalizeNumber(String paramString) {
        return paramString.replace(" ", "");
    }

    public String concealNumber(String paramString) {
        int i = Math.min(paramString.length(), this.length - 4);
        char[] arrayOfChar = new char[i];
        Arrays.fill(arrayOfChar, 65533);
        String str = new String(arrayOfChar);
        if (i < paramString.length()) {
            str = new StringBuilder(String.valueOf(str)).append(paramString.substring(i)).toString();
        }
        return formatNumber(str);
    }

    public String formatNumber(String paramString) {
        int i = paramString.length();
        int j = 0;
        ArrayList localArrayList = Lists.newArrayList();
        int k = 0;
        while (k < this.groupLengths.length && this.groupLengths[k] + j <= i) {
            localArrayList.add(paramString.substring(j, this.groupLengths[k] + j));
            j += this.groupLengths[k];
            k++;
        }
        StringBuilder localStringBuilder = new StringBuilder(TextUtils.join(" ", localArrayList));
        if (j < i && localArrayList.size() < this.groupLengths.length) {
            if (localArrayList.size() > 0) {
                localStringBuilder.append(' ');
            }
            localStringBuilder.append(paramString.substring(j, i));
        }
        return localStringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public boolean hasValidChecksum(String paramString) {
        int i = 0;
        if (!TextUtils.isEmpty(paramString)) {
            int j = 0;
            int k = 0;
            for (int m = paramString.length() - 1; m >= 0; m--) {
                int i1 = Integer.parseInt(String.valueOf(paramString.charAt(m)));
                int i2 = i1 + (k * i1);
                j += (int) (((double) i2) + Math.floor((double) (i2 / 10)));
                k = 1 - k;
            }
            i = 0;
            if (j % 10 == 0) {
                i = 1;
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    public boolean hasValidLength(String paramString) {
        return paramString.length() == this.length;
    }

    public boolean isValidNumber(String paramString) {
        return hasValidLength(paramString) && hasValidChecksum(paramString) && isValidPrefix(paramString);
    }

    public boolean isValidPrefix(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return false;
        }
        for (String range : this.numberPrefixRanges) {
            String[] ranges = range.split("-", 2);
            if (ranges.length == 2) {
                if (paramString.length() > ranges[0].length()) {
                    paramString = paramString.substring(0, ranges[0].length());
                }
                for (int i = 0; i < paramString.length(); i++) {
                    int realValue = Character.getNumericValue(paramString.charAt(i));
                    int minValue = Character.getNumericValue(ranges[0].charAt(i));
                    int maxValue = Character.getNumericValue(ranges[1].charAt(i));
                    if (realValue < minValue || realValue > maxValue) {
                        return false;
                    }
                }
                return true;
            }
            if (paramString.length() <= range.length()) {
                if (range.startsWith(paramString)) {
                    return true;
                }
            } else if (paramString.startsWith(range)) {
                return true;
            }
        }
        return false;
    }

    public String limitLength(String paramString) {
        return paramString.substring(0, Math.min(this.length, paramString.length()));
    }
}
