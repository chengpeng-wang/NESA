package com.sun.activation.registries;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.googleprojects.mm.MMMailContentUtil;

public class MailcapTokenizer {
    public static final int EOI_TOKEN = 5;
    public static final int EQUALS_TOKEN = 61;
    public static final int SEMICOLON_TOKEN = 59;
    public static final int SLASH_TOKEN = 47;
    public static final int START_TOKEN = 1;
    public static final int STRING_TOKEN = 2;
    public static final int UNKNOWN_TOKEN = 0;
    private char autoquoteChar;
    private int currentToken;
    private String currentTokenValue;
    private String data;
    private int dataIndex = 0;
    private int dataLength;
    private boolean isAutoquoting;

    public MailcapTokenizer(String inputString) {
        this.data = inputString;
        this.dataLength = inputString.length();
        this.currentToken = 1;
        this.currentTokenValue = MMMailContentUtil.MM_MESSAGE_SUBJECT;
        this.isAutoquoting = false;
        this.autoquoteChar = ';';
    }

    public void setIsAutoquoting(boolean value) {
        this.isAutoquoting = value;
    }

    public int getCurrentToken() {
        return this.currentToken;
    }

    public static String nameForToken(int token) {
        String name = "really unknown";
        switch (token) {
            case 0:
                return "unknown";
            case 1:
                return "start";
            case 2:
                return "string";
            case 5:
                return "EOI";
            case SLASH_TOKEN /*47*/:
                return "'/'";
            case SEMICOLON_TOKEN /*59*/:
                return "';'";
            case EQUALS_TOKEN /*61*/:
                return "'='";
            default:
                return name;
        }
    }

    public String getCurrentTokenValue() {
        return this.currentTokenValue;
    }

    public int nextToken() {
        if (this.dataIndex < this.dataLength) {
            while (this.dataIndex < this.dataLength && isWhiteSpaceChar(this.data.charAt(this.dataIndex))) {
                this.dataIndex++;
            }
            if (this.dataIndex < this.dataLength) {
                char c = this.data.charAt(this.dataIndex);
                if (this.isAutoquoting) {
                    if (c == ';' || c == '=') {
                        this.currentToken = c;
                        this.currentTokenValue = new Character(c).toString();
                        this.dataIndex++;
                    } else {
                        processAutoquoteToken();
                    }
                } else if (isStringTokenChar(c)) {
                    processStringToken();
                } else if (c == '/' || c == ';' || c == '=') {
                    this.currentToken = c;
                    this.currentTokenValue = new Character(c).toString();
                    this.dataIndex++;
                } else {
                    this.currentToken = 0;
                    this.currentTokenValue = new Character(c).toString();
                    this.dataIndex++;
                }
            } else {
                this.currentToken = 5;
                this.currentTokenValue = null;
            }
        } else {
            this.currentToken = 5;
            this.currentTokenValue = null;
        }
        return this.currentToken;
    }

    private void processStringToken() {
        int initialIndex = this.dataIndex;
        while (this.dataIndex < this.dataLength && isStringTokenChar(this.data.charAt(this.dataIndex))) {
            this.dataIndex++;
        }
        this.currentToken = 2;
        this.currentTokenValue = this.data.substring(initialIndex, this.dataIndex);
    }

    private void processAutoquoteToken() {
        int initialIndex = this.dataIndex;
        boolean foundTerminator = false;
        while (this.dataIndex < this.dataLength && !foundTerminator) {
            if (this.data.charAt(this.dataIndex) != this.autoquoteChar) {
                this.dataIndex++;
            } else {
                foundTerminator = true;
            }
        }
        this.currentToken = 2;
        this.currentTokenValue = fixEscapeSequences(this.data.substring(initialIndex, this.dataIndex));
    }

    private static boolean isSpecialChar(char c) {
        switch (c) {
            case '\"':
            case '(':
            case ')':
            case ',':
            case SLASH_TOKEN /*47*/:
            case ':':
            case SEMICOLON_TOKEN /*59*/:
            case '<':
            case EQUALS_TOKEN /*61*/:
            case '>':
            case '?':
            case AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS /*64*/:
            case '[':
            case '\\':
            case ']':
                return true;
            default:
                return false;
        }
    }

    private static boolean isControlChar(char c) {
        return Character.isISOControl(c);
    }

    private static boolean isWhiteSpaceChar(char c) {
        return Character.isWhitespace(c);
    }

    private static boolean isStringTokenChar(char c) {
        return (isSpecialChar(c) || isControlChar(c) || isWhiteSpaceChar(c)) ? false : true;
    }

    private static String fixEscapeSequences(String inputString) {
        int inputLength = inputString.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(inputLength);
        int i = 0;
        while (i < inputLength) {
            char currentChar = inputString.charAt(i);
            if (currentChar != '\\') {
                buffer.append(currentChar);
            } else if (i < inputLength - 1) {
                buffer.append(inputString.charAt(i + 1));
                i++;
            } else {
                buffer.append(currentChar);
            }
            i++;
        }
        return buffer.toString();
    }
}
