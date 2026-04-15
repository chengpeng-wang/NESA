package install.app;

public class WildCardStringFinder {
    private int charBound;
    private String[] charSegments;
    private boolean hasLeadingStar;
    private boolean hasTrailingStar;
    private boolean ignoreWildCards = false;
    private int wildCardPatternLength;
    private String wildCardPatternString;

    public boolean isStringMatching(String actualString, String wildCardString) {
        this.wildCardPatternString = wildCardString;
        this.wildCardPatternLength = wildCardString.length();
        setWildCards();
        return doesMatch(actualString, 0, actualString.length());
    }

    private void setWildCards() {
        /*
        r9 = this;
        r8 = 0;
        r7 = 1;
        r5 = r9.wildCardPatternString;
        r6 = "*";
        r5 = r5.startsWith(r6);
        if (r5 == 0) goto L_0x000e;
    L_0x000c:
        r9.hasLeadingStar = r7;
    L_0x000e:
        r5 = r9.wildCardPatternString;
        r6 = "*";
        r5 = r5.endsWith(r6);
        if (r5 == 0) goto L_0x001e;
    L_0x0018:
        r5 = r9.wildCardPatternLength;
        if (r5 <= r7) goto L_0x001e;
    L_0x001c:
        r9.hasTrailingStar = r7;
    L_0x001e:
        r4 = new java.util.Vector;
        r4.<init>();
        r2 = 0;
        r0 = new java.lang.StringBuffer;
        r0.<init>();
    L_0x0029:
        r5 = r9.wildCardPatternLength;
        if (r2 < r5) goto L_0x0051;
    L_0x002d:
        r5 = r0.length();
        if (r5 <= 0) goto L_0x0043;
    L_0x0033:
        r5 = r0.toString();
        r4.addElement(r5);
        r5 = r9.charBound;
        r6 = r0.length();
        r5 = r5 + r6;
        r9.charBound = r5;
    L_0x0043:
        r5 = r4.size();
        r5 = new java.lang.String[r5];
        r9.charSegments = r5;
        r5 = r9.charSegments;
        r4.copyInto(r5);
        return;
    L_0x0051:
        r5 = r9.wildCardPatternString;
        r3 = r2 + 1;
        r1 = r5.charAt(r2);
        switch(r1) {
            case 42: goto L_0x0061;
            case 63: goto L_0x007c;
            default: goto L_0x005c;
        };
    L_0x005c:
        r0.append(r1);
    L_0x005f:
        r2 = r3;
        goto L_0x0029;
    L_0x0061:
        r5 = r0.length();
        if (r5 <= 0) goto L_0x005f;
    L_0x0067:
        r5 = r0.toString();
        r4.addElement(r5);
        r5 = r9.charBound;
        r6 = r0.length();
        r5 = r5 + r6;
        r9.charBound = r5;
        r0.setLength(r8);
        r2 = r3;
        goto L_0x0029;
    L_0x007c:
        r0.append(r8);
        r2 = r3;
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: install.app.WildCardStringFinder.setWildCards():void");
    }

    private final boolean doesMatch(String text, int startPoint, int endPoint) {
        int textLength = text.length();
        if (startPoint > endPoint) {
            return false;
        }
        if (this.ignoreWildCards) {
            if (endPoint - startPoint == this.wildCardPatternLength) {
                if (this.wildCardPatternString.regionMatches(false, 0, text, startPoint, this.wildCardPatternLength)) {
                    return true;
                }
            }
            return false;
        }
        int charCount = this.charSegments.length;
        if (charCount == 0 && (this.hasLeadingStar || this.hasTrailingStar)) {
            return true;
        }
        if (startPoint == endPoint) {
            return this.wildCardPatternLength == 0;
        } else {
            if (this.wildCardPatternLength == 0) {
                return startPoint == endPoint;
            } else {
                if (startPoint < 0) {
                    startPoint = 0;
                }
                if (endPoint > textLength) {
                    endPoint = textLength;
                }
                int currPosition = startPoint;
                if (endPoint - this.charBound < 0) {
                    return false;
                }
                int i = 0;
                String currString = this.charSegments[0];
                int currStringLength = currString.length();
                if (!this.hasLeadingStar) {
                    if (!isExpressionMatching(text, startPoint, currString, 0, currStringLength)) {
                        return false;
                    }
                    i = 0 + 1;
                    currPosition += currStringLength;
                }
                if (this.charSegments.length == 1 && !this.hasLeadingStar && !this.hasTrailingStar) {
                    return currPosition == endPoint;
                } else {
                    while (i < charCount) {
                        currString = this.charSegments[i];
                        int k = currString.indexOf(0);
                        int currentMatch = getTextPosition(text, currPosition, endPoint, currString);
                        if (k < 0 && currentMatch < 0) {
                            return false;
                        }
                        currPosition = currentMatch + currString.length();
                        i++;
                    }
                    if (this.hasTrailingStar || currPosition == endPoint) {
                        return i == charCount;
                    } else {
                        int clen = currString.length();
                        return isExpressionMatching(text, endPoint - clen, currString, 0, clen);
                    }
                }
            }
        }
    }

    private final int getTextPosition(String textString, int start, int end, String posString) {
        int max = end - posString.length();
        int i = textString.indexOf(posString, start);
        if (posString.equals(".")) {
        }
        if (i == -1 || i > max) {
            return -1;
        }
        return i;
    }

    private boolean isExpressionMatching(String textString, int stringStartIndex, String patternString, int patternStartIndex, int length) {
        while (true) {
            int length2 = length;
            int patternStartIndex2 = patternStartIndex;
            int stringStartIndex2 = stringStartIndex;
            length = length2 - 1;
            if (length2 <= 0) {
                patternStartIndex = patternStartIndex2;
                stringStartIndex = stringStartIndex2;
                return true;
            }
            stringStartIndex = stringStartIndex2 + 1;
            char textChar = textString.charAt(stringStartIndex2);
            patternStartIndex = patternStartIndex2 + 1;
            char patternChar = patternString.charAt(patternStartIndex2);
            if ((this.ignoreWildCards || patternChar != 0) && patternChar != textChar && textChar != patternChar && textChar != patternChar) {
                return false;
            }
        }
    }
}
