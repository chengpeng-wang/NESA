package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

final class ModifyFirstLetterNamingPolicy extends RecursiveFieldNamingPolicy {
    private final LetterModifier letterModifier;

    public enum LetterModifier {
        UPPER,
        LOWER
    }

    ModifyFirstLetterNamingPolicy(LetterModifier modifier) {
        this.letterModifier = (LetterModifier) C$Gson$Preconditions.checkNotNull(modifier);
    }

    /* access modifiers changed from: protected */
    public String translateName(String target, Type fieldType, Collection<Annotation> collection) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        int index = 0;
        char firstCharacter = target.charAt(0);
        while (index < target.length() - 1 && !Character.isLetter(firstCharacter)) {
            fieldNameBuilder.append(firstCharacter);
            index++;
            firstCharacter = target.charAt(index);
        }
        if (index == target.length()) {
            return fieldNameBuilder.toString();
        }
        boolean capitalizeFirstLetter = this.letterModifier == LetterModifier.UPPER;
        if (capitalizeFirstLetter && !Character.isUpperCase(firstCharacter)) {
            return fieldNameBuilder.append(modifyString(Character.toUpperCase(firstCharacter), target, index + 1)).toString();
        }
        if (capitalizeFirstLetter || !Character.isUpperCase(firstCharacter)) {
            return target;
        }
        return fieldNameBuilder.append(modifyString(Character.toLowerCase(firstCharacter), target, index + 1)).toString();
    }

    private String modifyString(char firstCharacter, String srcString, int indexOfSubstring) {
        return indexOfSubstring < srcString.length() ? firstCharacter + srcString.substring(indexOfSubstring) : String.valueOf(firstCharacter);
    }
}
