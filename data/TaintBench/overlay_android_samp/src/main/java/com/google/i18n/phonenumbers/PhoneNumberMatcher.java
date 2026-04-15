package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.PhoneNumberUtil.Leniency;
import com.google.i18n.phonenumbers.PhoneNumberUtil.MatchType;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonemetadata.NumberFormat;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import java.lang.Character.UnicodeBlock;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PhoneNumberMatcher implements Iterator<PhoneNumberMatch> {
    private static final Pattern[] INNER_MATCHES = new Pattern[]{Pattern.compile("/+(.*)"), Pattern.compile("(\\([^(]*)"), Pattern.compile("(?:\\p{Z}-|-\\p{Z})\\p{Z}*(.+)"), Pattern.compile("[‒-―－]\\p{Z}*(.+)"), Pattern.compile("\\.+\\p{Z}*([^.]+)"), Pattern.compile("\\p{Z}+(\\P{Z}+)")};
    private static final Pattern LEAD_CLASS;
    private static final Pattern MATCHING_BRACKETS;
    private static final Pattern PATTERN;
    private static final Pattern PUB_PAGES = Pattern.compile("\\d{1,5}-+\\d{1,5}\\s{0,4}\\(\\d{1,4}");
    private static final Pattern SLASH_SEPARATED_DATES = Pattern.compile("(?:(?:[0-3]?\\d/[01]?\\d)|(?:[01]?\\d/[0-3]?\\d))/(?:[12]\\d)?\\d{2}");
    private static final Pattern TIME_STAMPS = Pattern.compile("[12]\\d{3}[-/]?[01]\\d[-/]?[0-3]\\d +[0-2]\\d$");
    private static final Pattern TIME_STAMPS_SUFFIX = Pattern.compile(":[0-5]\\d");
    private PhoneNumberMatch lastMatch = null;
    private final Leniency leniency;
    private long maxTries;
    private final PhoneNumberUtil phoneUtil;
    private final String preferredRegion;
    private int searchIndex = 0;
    private State state = State.NOT_READY;
    private final CharSequence text;

    interface NumberGroupingChecker {
        boolean checkGroups(PhoneNumberUtil phoneNumberUtil, PhoneNumber phoneNumber, StringBuilder stringBuilder, String[] strArr);
    }

    private enum State {
        NOT_READY,
        READY,
        DONE
    }

    static {
        String openingParens = "(\\[（［";
        String closingParens = ")\\]）］";
        String valueOf = String.valueOf(String.valueOf(openingParens));
        String valueOf2 = String.valueOf(String.valueOf(closingParens));
        String nonParens = new StringBuilder((valueOf.length() + 3) + valueOf2.length()).append("[^").append(valueOf).append(valueOf2).append("]").toString();
        String bracketPairLimit = limit(0, 3);
        valueOf = String.valueOf(String.valueOf(openingParens));
        valueOf2 = String.valueOf(String.valueOf(nonParens));
        String valueOf3 = String.valueOf(String.valueOf(closingParens));
        String valueOf4 = String.valueOf(String.valueOf(nonParens));
        String valueOf5 = String.valueOf(String.valueOf(openingParens));
        String valueOf6 = String.valueOf(String.valueOf(nonParens));
        String valueOf7 = String.valueOf(String.valueOf(closingParens));
        String valueOf8 = String.valueOf(String.valueOf(bracketPairLimit));
        String valueOf9 = String.valueOf(String.valueOf(nonParens));
        MATCHING_BRACKETS = Pattern.compile(new StringBuilder(((((((((valueOf.length() + 26) + valueOf2.length()) + valueOf3.length()) + valueOf4.length()) + valueOf5.length()) + valueOf6.length()) + valueOf7.length()) + valueOf8.length()) + valueOf9.length()).append("(?:[").append(valueOf).append("])?").append("(?:").append(valueOf2).append("+").append("[").append(valueOf3).append("])?").append(valueOf4).append("+").append("(?:[").append(valueOf5).append("]").append(valueOf6).append("+[").append(valueOf7).append("])").append(valueOf8).append(valueOf9).append("*").toString());
        String leadLimit = limit(0, 2);
        String punctuationLimit = limit(0, 4);
        String blockLimit = limit(0, 20);
        valueOf = String.valueOf("[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～]");
        valueOf2 = String.valueOf(punctuationLimit);
        String punctuation = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        valueOf = "\\p{Nd}";
        valueOf2 = String.valueOf(limit(1, 20));
        String digitSequence = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        valueOf = String.valueOf(openingParens);
        valueOf2 = String.valueOf("+＋");
        valueOf = String.valueOf(String.valueOf(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)));
        String leadClass = new StringBuilder(valueOf.length() + 2).append("[").append(valueOf).append("]").toString();
        LEAD_CLASS = Pattern.compile(leadClass);
        valueOf = String.valueOf(String.valueOf(leadClass));
        valueOf2 = String.valueOf(String.valueOf(punctuation));
        valueOf3 = String.valueOf(String.valueOf(leadLimit));
        valueOf4 = String.valueOf(String.valueOf(digitSequence));
        valueOf5 = String.valueOf(String.valueOf(punctuation));
        valueOf6 = String.valueOf(String.valueOf(digitSequence));
        valueOf7 = String.valueOf(String.valueOf(blockLimit));
        valueOf8 = String.valueOf(String.valueOf(PhoneNumberUtil.EXTN_PATTERNS_FOR_MATCHING));
        PATTERN = Pattern.compile(new StringBuilder((((((((valueOf.length() + 13) + valueOf2.length()) + valueOf3.length()) + valueOf4.length()) + valueOf5.length()) + valueOf6.length()) + valueOf7.length()) + valueOf8.length()).append("(?:").append(valueOf).append(valueOf2).append(")").append(valueOf3).append(valueOf4).append("(?:").append(valueOf5).append(valueOf6).append(")").append(valueOf7).append("(?:").append(valueOf8).append(")?").toString(), 66);
    }

    private static String limit(int lower, int upper) {
        if (lower >= 0 && upper > 0 && upper >= lower) {
            return "{" + lower + "," + upper + "}";
        }
        throw new IllegalArgumentException();
    }

    PhoneNumberMatcher(PhoneNumberUtil util, CharSequence text, String country, Leniency leniency, long maxTries) {
        if (util == null || leniency == null) {
            throw new NullPointerException();
        } else if (maxTries < 0) {
            throw new IllegalArgumentException();
        } else {
            this.phoneUtil = util;
            if (text == null) {
                text = "";
            }
            this.text = text;
            this.preferredRegion = country;
            this.leniency = leniency;
            this.maxTries = maxTries;
        }
    }

    private PhoneNumberMatch find(int index) {
        Matcher matcher = PATTERN.matcher(this.text);
        while (this.maxTries > 0 && matcher.find(index)) {
            int start = matcher.start();
            CharSequence candidate = trimAfterFirstMatch(PhoneNumberUtil.SECOND_NUMBER_START_PATTERN, this.text.subSequence(start, matcher.end()));
            PhoneNumberMatch match = extractMatch(candidate, start);
            if (match != null) {
                return match;
            }
            index = start + candidate.length();
            this.maxTries--;
        }
        return null;
    }

    private static CharSequence trimAfterFirstMatch(Pattern pattern, CharSequence candidate) {
        Matcher trailingCharsMatcher = pattern.matcher(candidate);
        if (trailingCharsMatcher.find()) {
            return candidate.subSequence(0, trailingCharsMatcher.start());
        }
        return candidate;
    }

    static boolean isLatinLetter(char letter) {
        if (!Character.isLetter(letter) && Character.getType(letter) != 6) {
            return false;
        }
        UnicodeBlock block = UnicodeBlock.of(letter);
        if (block.equals(UnicodeBlock.BASIC_LATIN) || block.equals(UnicodeBlock.LATIN_1_SUPPLEMENT) || block.equals(UnicodeBlock.LATIN_EXTENDED_A) || block.equals(UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) || block.equals(UnicodeBlock.LATIN_EXTENDED_B) || block.equals(UnicodeBlock.COMBINING_DIACRITICAL_MARKS)) {
            return true;
        }
        return false;
    }

    private static boolean isInvalidPunctuationSymbol(char character) {
        return character == '%' || Character.getType(character) == 26;
    }

    private PhoneNumberMatch extractMatch(CharSequence candidate, int offset) {
        if (SLASH_SEPARATED_DATES.matcher(candidate).find()) {
            return null;
        }
        if (TIME_STAMPS.matcher(candidate).find()) {
            if (TIME_STAMPS_SUFFIX.matcher(this.text.toString().substring(candidate.length() + offset)).lookingAt()) {
                return null;
            }
        }
        String rawString = candidate.toString();
        PhoneNumberMatch match = parseAndVerify(rawString, offset);
        return match == null ? extractInnerMatch(rawString, offset) : match;
    }

    private PhoneNumberMatch extractInnerMatch(String candidate, int offset) {
        for (Pattern possibleInnerMatch : INNER_MATCHES) {
            Matcher groupMatcher = possibleInnerMatch.matcher(candidate);
            boolean isFirstMatch = true;
            while (groupMatcher.find() && this.maxTries > 0) {
                PhoneNumberMatch match;
                if (isFirstMatch) {
                    match = parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, candidate.substring(0, groupMatcher.start())).toString(), offset);
                    if (match != null) {
                        return match;
                    }
                    this.maxTries--;
                    isFirstMatch = false;
                }
                match = parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, groupMatcher.group(1)).toString(), groupMatcher.start(1) + offset);
                if (match != null) {
                    return match;
                }
                this.maxTries--;
            }
        }
        return null;
    }

    private PhoneNumberMatch parseAndVerify(String candidate, int offset) {
        try {
            if (!MATCHING_BRACKETS.matcher(candidate).matches() || PUB_PAGES.matcher(candidate).find()) {
                return null;
            }
            if (this.leniency.compareTo(Leniency.VALID) >= 0) {
                if (offset > 0 && !LEAD_CLASS.matcher(candidate).lookingAt()) {
                    char previousChar = this.text.charAt(offset - 1);
                    if (isInvalidPunctuationSymbol(previousChar) || isLatinLetter(previousChar)) {
                        return null;
                    }
                }
                int lastCharIndex = offset + candidate.length();
                if (lastCharIndex < this.text.length()) {
                    char nextChar = this.text.charAt(lastCharIndex);
                    if (isInvalidPunctuationSymbol(nextChar) || isLatinLetter(nextChar)) {
                        return null;
                    }
                }
            }
            PhoneNumber number = this.phoneUtil.parseAndKeepRawInput(candidate, this.preferredRegion);
            if (this.phoneUtil.getRegionCodeForCountryCode(number.getCountryCode()).equals("IL") && this.phoneUtil.getNationalSignificantNumber(number).length() == 4) {
                if (offset == 0) {
                    return null;
                }
                if (offset > 0 && this.text.charAt(offset - 1) != '*') {
                    return null;
                }
            }
            if (!this.leniency.verify(number, candidate, this.phoneUtil)) {
                return null;
            }
            number.clearCountryCodeSource();
            number.clearRawInput();
            number.clearPreferredDomesticCarrierCode();
            return new PhoneNumberMatch(offset, candidate, number);
        } catch (NumberParseException e) {
            return null;
        }
    }

    static boolean allNumberGroupsRemainGrouped(PhoneNumberUtil util, PhoneNumber number, StringBuilder normalizedCandidate, String[] formattedNumberGroups) {
        int fromIndex = 0;
        if (number.getCountryCodeSource() != CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            String countryCode = Integer.toString(number.getCountryCode());
            fromIndex = normalizedCandidate.indexOf(countryCode) + countryCode.length();
        }
        int i = 0;
        while (i < formattedNumberGroups.length) {
            fromIndex = normalizedCandidate.indexOf(formattedNumberGroups[i], fromIndex);
            if (fromIndex < 0) {
                return false;
            }
            fromIndex += formattedNumberGroups[i].length();
            if (i != 0 || fromIndex >= normalizedCandidate.length() || util.getNddPrefixForRegion(util.getRegionCodeForCountryCode(number.getCountryCode()), true) == null || !Character.isDigit(normalizedCandidate.charAt(fromIndex))) {
                i++;
            } else {
                return normalizedCandidate.substring(fromIndex - formattedNumberGroups[i].length()).startsWith(util.getNationalSignificantNumber(number));
            }
        }
        return normalizedCandidate.substring(fromIndex).contains(number.getExtension());
    }

    static boolean allNumberGroupsAreExactlyPresent(PhoneNumberUtil util, PhoneNumber number, StringBuilder normalizedCandidate, String[] formattedNumberGroups) {
        int candidateNumberGroupIndex;
        String[] candidateGroups = PhoneNumberUtil.NON_DIGITS_PATTERN.split(normalizedCandidate.toString());
        if (number.hasExtension()) {
            candidateNumberGroupIndex = candidateGroups.length - 2;
        } else {
            candidateNumberGroupIndex = candidateGroups.length - 1;
        }
        if (candidateGroups.length == 1 || candidateGroups[candidateNumberGroupIndex].contains(util.getNationalSignificantNumber(number))) {
            return true;
        }
        int formattedNumberGroupIndex = formattedNumberGroups.length - 1;
        while (formattedNumberGroupIndex > 0 && candidateNumberGroupIndex >= 0) {
            if (!candidateGroups[candidateNumberGroupIndex].equals(formattedNumberGroups[formattedNumberGroupIndex])) {
                return false;
            }
            formattedNumberGroupIndex--;
            candidateNumberGroupIndex--;
        }
        if (candidateNumberGroupIndex < 0 || !candidateGroups[candidateNumberGroupIndex].endsWith(formattedNumberGroups[0])) {
            return false;
        }
        return true;
    }

    private static String[] getNationalNumberGroups(PhoneNumberUtil util, PhoneNumber number, NumberFormat formattingPattern) {
        if (formattingPattern != null) {
            return util.formatNsnUsingPattern(util.getNationalSignificantNumber(number), formattingPattern, PhoneNumberFormat.RFC3966).split("-");
        }
        String rfc3966Format = util.format(number, PhoneNumberFormat.RFC3966);
        int endIndex = rfc3966Format.indexOf(59);
        if (endIndex < 0) {
            endIndex = rfc3966Format.length();
        }
        return rfc3966Format.substring(rfc3966Format.indexOf(45) + 1, endIndex).split("-");
    }

    static boolean checkNumberGroupingIsValid(PhoneNumber number, String candidate, PhoneNumberUtil util, NumberGroupingChecker checker) {
        StringBuilder normalizedCandidate = PhoneNumberUtil.normalizeDigits(candidate, true);
        if (checker.checkGroups(util, number, normalizedCandidate, getNationalNumberGroups(util, number, null))) {
            return true;
        }
        PhoneMetadata alternateFormats = MetadataManager.getAlternateFormatsForCountry(number.getCountryCode());
        if (alternateFormats != null) {
            for (NumberFormat alternateFormat : alternateFormats.numberFormats()) {
                if (checker.checkGroups(util, number, normalizedCandidate, getNationalNumberGroups(util, number, alternateFormat))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean containsMoreThanOneSlashInNationalNumber(PhoneNumber number, String candidate) {
        int firstSlashInBodyIndex = candidate.indexOf(47);
        if (firstSlashInBodyIndex < 0) {
            return false;
        }
        int secondSlashInBodyIndex = candidate.indexOf(47, firstSlashInBodyIndex + 1);
        if (secondSlashInBodyIndex < 0) {
            return false;
        }
        boolean candidateHasCountryCode;
        if (number.getCountryCodeSource() == CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN || number.getCountryCodeSource() == CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN) {
            candidateHasCountryCode = true;
        } else {
            candidateHasCountryCode = false;
        }
        if (candidateHasCountryCode && PhoneNumberUtil.normalizeDigitsOnly(candidate.substring(0, firstSlashInBodyIndex)).equals(Integer.toString(number.getCountryCode()))) {
            return candidate.substring(secondSlashInBodyIndex + 1).contains("/");
        }
        return true;
    }

    static boolean containsOnlyValidXChars(PhoneNumber number, String candidate, PhoneNumberUtil util) {
        int index = 0;
        while (index < candidate.length() - 1) {
            char charAtIndex = candidate.charAt(index);
            if (charAtIndex == 'x' || charAtIndex == 'X') {
                char charAtNextIndex = candidate.charAt(index + 1);
                if (charAtNextIndex == 'x' || charAtNextIndex == 'X') {
                    index++;
                    if (util.isNumberMatch(number, candidate.substring(index)) != MatchType.NSN_MATCH) {
                        return false;
                    }
                } else if (!PhoneNumberUtil.normalizeDigitsOnly(candidate.substring(index)).equals(number.getExtension())) {
                    return false;
                }
            }
            index++;
        }
        return true;
    }

    static boolean isNationalPrefixPresentIfRequired(PhoneNumber number, PhoneNumberUtil util) {
        if (number.getCountryCodeSource() != CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            return true;
        }
        PhoneMetadata metadata = util.getMetadataForRegion(util.getRegionCodeForCountryCode(number.getCountryCode()));
        if (metadata == null) {
            return true;
        }
        NumberFormat formatRule = util.chooseFormattingPatternForNumber(metadata.numberFormats(), util.getNationalSignificantNumber(number));
        if (formatRule == null || formatRule.getNationalPrefixFormattingRule().length() <= 0 || formatRule.isNationalPrefixOptionalWhenFormatting() || PhoneNumberUtil.formattingRuleHasFirstGroupOnly(formatRule.getNationalPrefixFormattingRule())) {
            return true;
        }
        return util.maybeStripNationalPrefixAndCarrierCode(new StringBuilder(PhoneNumberUtil.normalizeDigitsOnly(number.getRawInput())), metadata, null);
    }

    public boolean hasNext() {
        if (this.state == State.NOT_READY) {
            this.lastMatch = find(this.searchIndex);
            if (this.lastMatch == null) {
                this.state = State.DONE;
            } else {
                this.searchIndex = this.lastMatch.end();
                this.state = State.READY;
            }
        }
        return this.state == State.READY;
    }

    public PhoneNumberMatch next() {
        if (hasNext()) {
            PhoneNumberMatch result = this.lastMatch;
            this.lastMatch = null;
            this.state = State.NOT_READY;
            return result;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
