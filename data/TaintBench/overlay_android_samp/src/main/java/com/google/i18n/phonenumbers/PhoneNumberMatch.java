package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.util.Arrays;

public final class PhoneNumberMatch {
    private final PhoneNumber number;
    private final String rawString;
    private final int start;

    PhoneNumberMatch(int start, String rawString, PhoneNumber number) {
        if (start < 0) {
            throw new IllegalArgumentException("Start index must be >= 0.");
        } else if (rawString == null || number == null) {
            throw new NullPointerException();
        } else {
            this.start = start;
            this.rawString = rawString;
            this.number = number;
        }
    }

    public PhoneNumber number() {
        return this.number;
    }

    public int start() {
        return this.start;
    }

    public int end() {
        return this.start + this.rawString.length();
    }

    public String rawString() {
        return this.rawString;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.start), this.rawString, this.number});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PhoneNumberMatch)) {
            return false;
        }
        PhoneNumberMatch other = (PhoneNumberMatch) obj;
        if (this.rawString.equals(other.rawString) && this.start == other.start && this.number.equals(other.number)) {
            return true;
        }
        return false;
    }

    public String toString() {
        int start = start();
        int end = end();
        String valueOf = String.valueOf(String.valueOf(this.rawString));
        return new StringBuilder(valueOf.length() + 43).append("PhoneNumberMatch [").append(start).append(",").append(end).append(") ").append(valueOf).toString();
    }
}
