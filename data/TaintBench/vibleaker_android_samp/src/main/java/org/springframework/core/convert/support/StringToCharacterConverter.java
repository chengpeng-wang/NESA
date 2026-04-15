package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

final class StringToCharacterConverter implements Converter<String, Character> {
    StringToCharacterConverter() {
    }

    public Character convert(String source) {
        if (source.length() == 0) {
            return null;
        }
        if (source.length() <= 1) {
            return Character.valueOf(source.charAt(0));
        }
        throw new IllegalArgumentException("Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source.length());
    }
}
