package ru.stels2;

import java.util.Vector;

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
        if (this.wildCardPatternString.startsWith("*")) {
            this.hasLeadingStar = true;
        }
        if (this.wildCardPatternString.endsWith("*") && this.wildCardPatternLength > 1) {
            this.hasTrailingStar = true;
        }
        Vector<String> temp = new Vector();
        int pos = 0;
        StringBuffer buf = new StringBuffer();
        while (pos < this.wildCardPatternLength) {
            int pos2 = pos + 1;
            char c = this.wildCardPatternString.charAt(pos);
            switch (c) {
                case '*':
                    if (buf.length() <= 0) {
                        break;
                    }
                    temp.addElement(buf.toString());
                    this.charBound += buf.length();
                    buf.setLength(0);
                    break;
                case '?':
                    buf.append(0);
                    break;
                default:
                    buf.append(c);
                    break;
            }
            pos = pos2;
        }
        if (buf.length() > 0) {
            temp.addElement(buf.toString());
            this.charBound += buf.length();
        }
        this.charSegments = new String[temp.size()];
        temp.copyInto(this.charSegments);
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
            if (length2 > 0) {
                stringStartIndex = stringStartIndex2 + 1;
                char textChar = textString.charAt(stringStartIndex2);
                patternStartIndex = patternStartIndex2 + 1;
                char patternChar = patternString.charAt(patternStartIndex2);
                if ((this.ignoreWildCards || patternChar != 0) && patternChar != textChar && textChar != patternChar && textChar != patternChar) {
                    return false;
                }
            } else {
                patternStartIndex = patternStartIndex2;
                stringStartIndex = stringStartIndex2;
                return true;
            }
        }
    }
}
