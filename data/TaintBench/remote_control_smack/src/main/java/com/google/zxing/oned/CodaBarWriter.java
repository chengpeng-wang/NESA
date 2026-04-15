package com.google.zxing.oned;

public class CodaBarWriter extends OneDimensionalCodeWriter {
    public CodaBarWriter() {
        super(20);
    }

    public byte[] encode(String contents) {
        if (!CodaBarReader.arrayContains(new char[]{'A', 'B', 'C', 'D'}, Character.toUpperCase(contents.charAt(0)))) {
            throw new IllegalArgumentException("Codabar should start with one of the following: 'A', 'B', 'C' or 'D'");
        } else if (CodaBarReader.arrayContains(new char[]{'T', 'N', '*', 'E'}, Character.toUpperCase(contents.charAt(contents.length() - 1)))) {
            int resultLength = 20;
            char[] charsWhichAreTenLengthEachAfterDecoded = new char[]{'/', ':', '+', '.'};
            int i = 1;
            while (i < contents.length() - 1) {
                if (Character.isDigit(contents.charAt(i)) || contents.charAt(i) == '-' || contents.charAt(i) == '$') {
                    resultLength += 9;
                } else if (CodaBarReader.arrayContains(charsWhichAreTenLengthEachAfterDecoded, contents.charAt(i))) {
                    resultLength += 10;
                } else {
                    throw new IllegalArgumentException("Cannot encode : '" + contents.charAt(i) + '\'');
                }
                i++;
            }
            byte[] result = new byte[(resultLength + (contents.length() - 1))];
            int position = 0;
            for (int index = 0; index < contents.length(); index++) {
                char c = Character.toUpperCase(contents.charAt(index));
                if (index == contents.length() - 1) {
                    if (c == '*') {
                        c = 'C';
                    } else if (c == 'E') {
                        c = 'D';
                    }
                }
                int code = 0;
                for (i = 0; i < CodaBarReader.ALPHABET.length; i++) {
                    if (c == CodaBarReader.ALPHABET[i]) {
                        code = CodaBarReader.CHARACTER_ENCODINGS[i];
                        break;
                    }
                }
                byte color = (byte) 1;
                int counter = 0;
                int bit = 0;
                while (bit < 7) {
                    result[position] = color;
                    position++;
                    if (((code >> (6 - bit)) & 1) == 0 || counter == 1) {
                        color = (byte) (color ^ 1);
                        bit++;
                        counter = 0;
                    } else {
                        counter++;
                    }
                }
                if (index < contents.length() - 1) {
                    result[position] = (byte) 0;
                    position++;
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException("Codabar should end with one of the following: 'T', 'N', '*' or 'E'");
        }
    }
}
