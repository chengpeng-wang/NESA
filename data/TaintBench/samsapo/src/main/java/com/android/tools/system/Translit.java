package com.android.tools.system;

public class Translit {
    private static final char CODER_CHAR = '#';
    private static final char START_CHAR = 'Ё';
    private static final String[] charTable = new String[88];

    static {
        charTable[15] = "A";
        charTable[16] = "B";
        charTable[17] = "V";
        charTable[18] = "G";
        charTable[19] = "D";
        charTable[20] = "E";
        charTable[0] = "YO";
        charTable[21] = "ZH";
        charTable[22] = "Z";
        charTable[23] = "I";
        charTable[24] = "YI";
        charTable[25] = "K";
        charTable[26] = "L";
        charTable[27] = "M";
        charTable[28] = "N";
        charTable[29] = "O";
        charTable[30] = "P";
        charTable[31] = "R";
        charTable[32] = "S";
        charTable[33] = "T";
        charTable[34] = "U";
        charTable[35] = "F";
        charTable[36] = "H";
        charTable[37] = "C";
        charTable[38] = "CH";
        charTable[39] = "SH";
        charTable[40] = "SCH";
        charTable[41] = "''";
        charTable[42] = "Y";
        charTable[43] = "'";
        charTable[44] = "YE";
        charTable[45] = "YU";
        charTable[46] = "YA";
        for (int i = 0; i < charTable.length; i++) {
            String str = r10;
            char[] cArr = new char[1];
            char[] cArr2 = cArr;
            cArr[0] = (char) (((char) i) + 1025);
            String str2 = new String(cArr2);
            char charAt = str.toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[charAt - 1025] = charTable[i].toLowerCase();
            }
        }
    }

    public static String toTranslit(String str) {
        String str2 = str;
        char[] toCharArray = str2.toCharArray();
        StringBuilder stringBuilder = r12;
        StringBuilder stringBuilder2 = new StringBuilder(str2.length());
        StringBuilder stringBuilder3 = stringBuilder;
        char[] cArr = toCharArray;
        for (char c : cArr) {
            int i = c - 1025;
            if (i < 0 || i >= charTable.length) {
                stringBuilder = stringBuilder3.append(c);
            } else {
                String str3 = charTable[i];
                stringBuilder = stringBuilder3.append(str3 == null ? String.valueOf(c) : str3);
            }
        }
        return stringBuilder3.toString();
    }

    public static String toTraslitCoded(String str) {
        String str2 = str;
        char[] toCharArray = str2.toCharArray();
        StringBuilder stringBuilder = r12;
        StringBuilder stringBuilder2 = new StringBuilder(str2.length());
        StringBuilder stringBuilder3 = stringBuilder;
        char[] cArr = toCharArray;
        for (char c : cArr) {
            stringBuilder = stringBuilder3.append(CODER_CHAR);
            int i = c - 1025;
            if (i < 0 || i >= charTable.length) {
                stringBuilder = stringBuilder3.append(c);
            } else {
                String str3 = charTable[i];
                stringBuilder = stringBuilder3.append(str3 == null ? String.valueOf(c) : str3);
            }
        }
        return stringBuilder3.toString();
    }

    public Translit() {
    }
}
