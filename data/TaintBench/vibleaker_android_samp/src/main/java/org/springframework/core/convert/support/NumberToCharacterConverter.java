package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

final class NumberToCharacterConverter implements Converter<Number, Character> {
    NumberToCharacterConverter() {
    }

    public Character convert(Number source) {
        return Character.valueOf((char) source.shortValue());
    }
}
