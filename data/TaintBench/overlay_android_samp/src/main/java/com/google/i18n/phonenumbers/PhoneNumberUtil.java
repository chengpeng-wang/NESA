package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.NumberParseException.ErrorType;
import com.google.i18n.phonenumbers.Phonemetadata.NumberFormat;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadataCollection;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneNumberDesc;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtil {
    private static final Map<Character, Character> ALL_PLUS_NUMBER_GROUPING_SYMBOLS;
    private static final Map<Character, Character> ALPHA_MAPPINGS;
    private static final Map<Character, Character> ALPHA_PHONE_MAPPINGS;
    private static final Pattern CAPTURING_DIGIT_PATTERN = Pattern.compile("(\\p{Nd})");
    private static final String CAPTURING_EXTN_DIGITS = "(\\p{Nd}{1,7})";
    private static final Pattern CC_PATTERN = Pattern.compile("\\$CC");
    private static final String COLOMBIA_MOBILE_TO_FIXED_LINE_PREFIX = "3";
    private static final String DEFAULT_EXTN_PREFIX = " ext. ";
    static final MetadataLoader DEFAULT_METADATA_LOADER = new MetadataLoader() {
        public InputStream loadMetadata(String metadataFileName) {
            return PhoneNumberUtil.class.getResourceAsStream(metadataFileName);
        }
    };
    private static final Map<Character, Character> DIALLABLE_CHAR_MAPPINGS;
    private static final String DIGITS = "\\p{Nd}";
    private static final Pattern EXTN_PATTERN;
    static final String EXTN_PATTERNS_FOR_MATCHING;
    private static final String EXTN_PATTERNS_FOR_PARSING;
    private static final Pattern FG_PATTERN = Pattern.compile("\\$FG");
    private static final Pattern FIRST_GROUP_ONLY_PREFIX_PATTERN = Pattern.compile("\\(?\\$1\\)?");
    private static final Pattern FIRST_GROUP_PATTERN = Pattern.compile("(\\$\\d)");
    private static final int MAX_INPUT_STRING_LENGTH = 250;
    static final int MAX_LENGTH_COUNTRY_CODE = 3;
    static final int MAX_LENGTH_FOR_NSN = 17;
    private static final String META_DATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberMetadataProto";
    private static final int MIN_LENGTH_FOR_NSN = 2;
    private static final Map<Integer, String> MOBILE_TOKEN_MAPPINGS;
    private static final int NANPA_COUNTRY_CODE = 1;
    static final Pattern NON_DIGITS_PATTERN = Pattern.compile("(\\D+)");
    private static final Pattern NP_PATTERN = Pattern.compile("\\$NP");
    static final String PLUS_CHARS = "+＋";
    static final Pattern PLUS_CHARS_PATTERN = Pattern.compile("[+＋]+");
    static final char PLUS_SIGN = '+';
    static final int REGEX_FLAGS = 66;
    public static final String REGION_CODE_FOR_NON_GEO_ENTITY = "001";
    private static final String RFC3966_EXTN_PREFIX = ";ext=";
    private static final String RFC3966_ISDN_SUBADDRESS = ";isub=";
    private static final String RFC3966_PHONE_CONTEXT = ";phone-context=";
    private static final String RFC3966_PREFIX = "tel:";
    private static final String SECOND_NUMBER_START = "[\\\\/] *x";
    static final Pattern SECOND_NUMBER_START_PATTERN = Pattern.compile(SECOND_NUMBER_START);
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile("[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～]+");
    private static final char STAR_SIGN = '*';
    private static final Pattern UNIQUE_INTERNATIONAL_PREFIX = Pattern.compile("[\\d]+(?:[~⁓∼～][\\d]+)?");
    private static final String UNKNOWN_REGION = "ZZ";
    private static final String UNWANTED_END_CHARS = "[[\\P{N}&&\\P{L}]&&[^#]]+$";
    static final Pattern UNWANTED_END_CHAR_PATTERN = Pattern.compile(UNWANTED_END_CHARS);
    private static final String VALID_ALPHA;
    private static final Pattern VALID_ALPHA_PHONE_PATTERN = Pattern.compile("(?:.*?[A-Za-z]){3}.*");
    private static final String VALID_PHONE_NUMBER;
    private static final Pattern VALID_PHONE_NUMBER_PATTERN;
    static final String VALID_PUNCTUATION = "-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～";
    private static final String VALID_START_CHAR = "[+＋\\p{Nd}]";
    private static final Pattern VALID_START_CHAR_PATTERN = Pattern.compile(VALID_START_CHAR);
    private static PhoneNumberUtil instance = null;
    private static final Logger logger = Logger.getLogger(PhoneNumberUtil.class.getName());
    private final Map<Integer, List<String>> countryCallingCodeToRegionCodeMap;
    private final Map<Integer, PhoneMetadata> countryCodeToNonGeographicalMetadataMap = Collections.synchronizedMap(new HashMap());
    private final Set<Integer> countryCodesForNonGeographicalRegion = new HashSet();
    private final String currentFilePrefix;
    private final MetadataLoader metadataLoader;
    private final Set<String> nanpaRegions = new HashSet(35);
    private final RegexCache regexCache = new RegexCache(100);
    private final Map<String, PhoneMetadata> regionToMetadataMap = Collections.synchronizedMap(new HashMap());
    private final Set<String> supportedRegions = new HashSet(320);

    /* renamed from: com.google.i18n.phonenumbers.PhoneNumberUtil$3 */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat = new int[PhoneNumberFormat.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType = new int[PhoneNumberType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource = new int[CountryCodeSource.values().length];

        static {
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.PREMIUM_RATE.ordinal()] = PhoneNumberUtil.NANPA_COUNTRY_CODE;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.TOLL_FREE.ordinal()] = PhoneNumberUtil.MIN_LENGTH_FOR_NSN;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.MOBILE.ordinal()] = PhoneNumberUtil.MAX_LENGTH_COUNTRY_CODE;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.FIXED_LINE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.FIXED_LINE_OR_MOBILE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.SHARED_COST.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.VOIP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.PERSONAL_NUMBER.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.PAGER.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.UAN.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[PhoneNumberType.VOICEMAIL.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[PhoneNumberFormat.E164.ordinal()] = PhoneNumberUtil.NANPA_COUNTRY_CODE;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[PhoneNumberFormat.INTERNATIONAL.ordinal()] = PhoneNumberUtil.MIN_LENGTH_FOR_NSN;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[PhoneNumberFormat.RFC3966.ordinal()] = PhoneNumberUtil.MAX_LENGTH_COUNTRY_CODE;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[PhoneNumberFormat.NATIONAL.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN.ordinal()] = PhoneNumberUtil.NANPA_COUNTRY_CODE;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[CountryCodeSource.FROM_NUMBER_WITH_IDD.ordinal()] = PhoneNumberUtil.MIN_LENGTH_FOR_NSN;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN.ordinal()] = PhoneNumberUtil.MAX_LENGTH_COUNTRY_CODE;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[CountryCodeSource.FROM_DEFAULT_COUNTRY.ordinal()] = 4;
            } catch (NoSuchFieldError e19) {
            }
        }
    }

    public enum Leniency {
        POSSIBLE {
            /* access modifiers changed from: 0000 */
            public boolean verify(PhoneNumber number, String candidate, PhoneNumberUtil util) {
                return util.isPossibleNumber(number);
            }
        },
        VALID {
            /* access modifiers changed from: 0000 */
            public boolean verify(PhoneNumber number, String candidate, PhoneNumberUtil util) {
                if (util.isValidNumber(number) && PhoneNumberMatcher.containsOnlyValidXChars(number, candidate, util)) {
                    return PhoneNumberMatcher.isNationalPrefixPresentIfRequired(number, util);
                }
                return false;
            }
        },
        STRICT_GROUPING {
            /* access modifiers changed from: 0000 */
            public boolean verify(PhoneNumber number, String candidate, PhoneNumberUtil util) {
                if (util.isValidNumber(number) && PhoneNumberMatcher.containsOnlyValidXChars(number, candidate, util) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(number, candidate) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(number, util)) {
                    return PhoneNumberMatcher.checkNumberGroupingIsValid(number, candidate, util, new NumberGroupingChecker() {
                        public boolean checkGroups(PhoneNumberUtil util, PhoneNumber number, StringBuilder normalizedCandidate, String[] expectedNumberGroups) {
                            return PhoneNumberMatcher.allNumberGroupsRemainGrouped(util, number, normalizedCandidate, expectedNumberGroups);
                        }
                    });
                }
                return false;
            }
        },
        EXACT_GROUPING {
            /* access modifiers changed from: 0000 */
            public boolean verify(PhoneNumber number, String candidate, PhoneNumberUtil util) {
                if (util.isValidNumber(number) && PhoneNumberMatcher.containsOnlyValidXChars(number, candidate, util) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(number, candidate) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(number, util)) {
                    return PhoneNumberMatcher.checkNumberGroupingIsValid(number, candidate, util, new NumberGroupingChecker() {
                        public boolean checkGroups(PhoneNumberUtil util, PhoneNumber number, StringBuilder normalizedCandidate, String[] expectedNumberGroups) {
                            return PhoneNumberMatcher.allNumberGroupsAreExactlyPresent(util, number, normalizedCandidate, expectedNumberGroups);
                        }
                    });
                }
                return false;
            }
        };

        public abstract boolean verify(PhoneNumber phoneNumber, String str, PhoneNumberUtil phoneNumberUtil);
    }

    public enum MatchType {
        NOT_A_NUMBER,
        NO_MATCH,
        SHORT_NSN_MATCH,
        NSN_MATCH,
        EXACT_MATCH
    }

    public enum PhoneNumberFormat {
        E164,
        INTERNATIONAL,
        NATIONAL,
        RFC3966
    }

    public enum PhoneNumberType {
        FIXED_LINE,
        MOBILE,
        FIXED_LINE_OR_MOBILE,
        TOLL_FREE,
        PREMIUM_RATE,
        SHARED_COST,
        VOIP,
        PERSONAL_NUMBER,
        PAGER,
        UAN,
        VOICEMAIL,
        UNKNOWN
    }

    public enum ValidationResult {
        IS_POSSIBLE,
        INVALID_COUNTRY_CODE,
        TOO_SHORT,
        TOO_LONG
    }

    static {
        HashMap<Integer, String> mobileTokenMap = new HashMap();
        mobileTokenMap.put(Integer.valueOf(52), "1");
        mobileTokenMap.put(Integer.valueOf(54), "9");
        MOBILE_TOKEN_MAPPINGS = Collections.unmodifiableMap(mobileTokenMap);
        HashMap<Character, Character> asciiDigitMappings = new HashMap();
        asciiDigitMappings.put(Character.valueOf('0'), Character.valueOf('0'));
        asciiDigitMappings.put(Character.valueOf('1'), Character.valueOf('1'));
        asciiDigitMappings.put(Character.valueOf('2'), Character.valueOf('2'));
        asciiDigitMappings.put(Character.valueOf('3'), Character.valueOf('3'));
        asciiDigitMappings.put(Character.valueOf('4'), Character.valueOf('4'));
        asciiDigitMappings.put(Character.valueOf('5'), Character.valueOf('5'));
        asciiDigitMappings.put(Character.valueOf('6'), Character.valueOf('6'));
        asciiDigitMappings.put(Character.valueOf('7'), Character.valueOf('7'));
        asciiDigitMappings.put(Character.valueOf('8'), Character.valueOf('8'));
        asciiDigitMappings.put(Character.valueOf('9'), Character.valueOf('9'));
        HashMap<Character, Character> alphaMap = new HashMap(40);
        alphaMap.put(Character.valueOf('A'), Character.valueOf('2'));
        alphaMap.put(Character.valueOf('B'), Character.valueOf('2'));
        alphaMap.put(Character.valueOf('C'), Character.valueOf('2'));
        alphaMap.put(Character.valueOf('D'), Character.valueOf('3'));
        alphaMap.put(Character.valueOf('E'), Character.valueOf('3'));
        alphaMap.put(Character.valueOf('F'), Character.valueOf('3'));
        alphaMap.put(Character.valueOf('G'), Character.valueOf('4'));
        alphaMap.put(Character.valueOf('H'), Character.valueOf('4'));
        alphaMap.put(Character.valueOf('I'), Character.valueOf('4'));
        alphaMap.put(Character.valueOf('J'), Character.valueOf('5'));
        alphaMap.put(Character.valueOf('K'), Character.valueOf('5'));
        alphaMap.put(Character.valueOf('L'), Character.valueOf('5'));
        alphaMap.put(Character.valueOf('M'), Character.valueOf('6'));
        alphaMap.put(Character.valueOf('N'), Character.valueOf('6'));
        alphaMap.put(Character.valueOf('O'), Character.valueOf('6'));
        alphaMap.put(Character.valueOf('P'), Character.valueOf('7'));
        alphaMap.put(Character.valueOf('Q'), Character.valueOf('7'));
        alphaMap.put(Character.valueOf('R'), Character.valueOf('7'));
        alphaMap.put(Character.valueOf('S'), Character.valueOf('7'));
        alphaMap.put(Character.valueOf('T'), Character.valueOf('8'));
        alphaMap.put(Character.valueOf('U'), Character.valueOf('8'));
        alphaMap.put(Character.valueOf('V'), Character.valueOf('8'));
        alphaMap.put(Character.valueOf('W'), Character.valueOf('9'));
        alphaMap.put(Character.valueOf('X'), Character.valueOf('9'));
        alphaMap.put(Character.valueOf('Y'), Character.valueOf('9'));
        alphaMap.put(Character.valueOf('Z'), Character.valueOf('9'));
        ALPHA_MAPPINGS = Collections.unmodifiableMap(alphaMap);
        HashMap<Character, Character> combinedMap = new HashMap(100);
        combinedMap.putAll(ALPHA_MAPPINGS);
        combinedMap.putAll(asciiDigitMappings);
        ALPHA_PHONE_MAPPINGS = Collections.unmodifiableMap(combinedMap);
        HashMap<Character, Character> diallableCharMap = new HashMap();
        diallableCharMap.putAll(asciiDigitMappings);
        diallableCharMap.put(Character.valueOf(PLUS_SIGN), Character.valueOf(PLUS_SIGN));
        diallableCharMap.put(Character.valueOf(STAR_SIGN), Character.valueOf(STAR_SIGN));
        DIALLABLE_CHAR_MAPPINGS = Collections.unmodifiableMap(diallableCharMap);
        HashMap<Character, Character> allPlusNumberGroupings = new HashMap();
        for (Character charValue : ALPHA_MAPPINGS.keySet()) {
            char c = charValue.charValue();
            allPlusNumberGroupings.put(Character.valueOf(Character.toLowerCase(c)), Character.valueOf(c));
            allPlusNumberGroupings.put(Character.valueOf(c), Character.valueOf(c));
        }
        allPlusNumberGroupings.putAll(asciiDigitMappings);
        allPlusNumberGroupings.put(Character.valueOf('-'), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(65293), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8208), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8209), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8210), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8211), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8212), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8213), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf(8722), Character.valueOf('-'));
        allPlusNumberGroupings.put(Character.valueOf('/'), Character.valueOf('/'));
        allPlusNumberGroupings.put(Character.valueOf(65295), Character.valueOf('/'));
        allPlusNumberGroupings.put(Character.valueOf(' '), Character.valueOf(' '));
        allPlusNumberGroupings.put(Character.valueOf(12288), Character.valueOf(' '));
        allPlusNumberGroupings.put(Character.valueOf(8288), Character.valueOf(' '));
        allPlusNumberGroupings.put(Character.valueOf('.'), Character.valueOf('.'));
        allPlusNumberGroupings.put(Character.valueOf(65294), Character.valueOf('.'));
        ALL_PLUS_NUMBER_GROUPING_SYMBOLS = Collections.unmodifiableMap(allPlusNumberGroupings);
        String valueOf = String.valueOf(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).replaceAll("[, \\[\\]]", ""));
        String valueOf2 = String.valueOf(Arrays.toString(ALPHA_MAPPINGS.keySet().toArray()).toLowerCase().replaceAll("[, \\[\\]]", ""));
        if (valueOf2.length() != 0) {
            valueOf2 = valueOf.concat(valueOf2);
        } else {
            valueOf2 = new String(valueOf);
        }
        VALID_ALPHA = valueOf2;
        valueOf2 = String.valueOf(String.valueOf("\\p{Nd}{2}|[+＋]*+(?:[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～*]*\\p{Nd}){3,}[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～*"));
        valueOf = String.valueOf(String.valueOf(VALID_ALPHA));
        String valueOf3 = String.valueOf(String.valueOf(DIGITS));
        VALID_PHONE_NUMBER = new StringBuilder(((valueOf2.length() + MIN_LENGTH_FOR_NSN) + valueOf.length()) + valueOf3.length()).append(valueOf2).append(valueOf).append(valueOf3).append("]*").toString();
        String singleExtnSymbolsForMatching = "xｘ#＃~～";
        valueOf2 = ",";
        valueOf = String.valueOf(singleExtnSymbolsForMatching);
        EXTN_PATTERNS_FOR_PARSING = createExtnPattern(valueOf.length() != 0 ? valueOf2.concat(valueOf) : new String(valueOf2));
        EXTN_PATTERNS_FOR_MATCHING = createExtnPattern(singleExtnSymbolsForMatching);
        valueOf2 = String.valueOf(String.valueOf(EXTN_PATTERNS_FOR_PARSING));
        EXTN_PATTERN = Pattern.compile(new StringBuilder(valueOf2.length() + 5).append("(?:").append(valueOf2).append(")$").toString(), REGEX_FLAGS);
        valueOf2 = String.valueOf(String.valueOf(VALID_PHONE_NUMBER));
        valueOf = String.valueOf(String.valueOf(EXTN_PATTERNS_FOR_PARSING));
        VALID_PHONE_NUMBER_PATTERN = Pattern.compile(new StringBuilder((valueOf2.length() + 5) + valueOf.length()).append(valueOf2).append("(?:").append(valueOf).append(")?").toString(), REGEX_FLAGS);
    }

    private static String createExtnPattern(String singleExtnSymbols) {
        String valueOf = String.valueOf(String.valueOf(";ext=(\\p{Nd}{1,7})|[  \\t,]*(?:e?xt(?:ensi(?:ó?|ó))?n?|ｅ?ｘｔｎ?|["));
        String valueOf2 = String.valueOf(String.valueOf(singleExtnSymbols));
        String valueOf3 = String.valueOf(String.valueOf(CAPTURING_EXTN_DIGITS));
        String valueOf4 = String.valueOf(String.valueOf(DIGITS));
        return new StringBuilder((((valueOf.length() + 48) + valueOf2.length()) + valueOf3.length()) + valueOf4.length()).append(valueOf).append(valueOf2).append("]|int|anexo|ｉｎｔ)").append("[:\\.．]?[  \\t,-]*").append(valueOf3).append("#?|").append("[- ]+(").append(valueOf4).append("{1,5})#").toString();
    }

    PhoneNumberUtil(String filePrefix, MetadataLoader metadataLoader, Map<Integer, List<String>> countryCallingCodeToRegionCodeMap) {
        this.currentFilePrefix = filePrefix;
        this.metadataLoader = metadataLoader;
        this.countryCallingCodeToRegionCodeMap = countryCallingCodeToRegionCodeMap;
        for (Entry<Integer, List<String>> entry : countryCallingCodeToRegionCodeMap.entrySet()) {
            List<String> regionCodes = (List) entry.getValue();
            if (regionCodes.size() == NANPA_COUNTRY_CODE && REGION_CODE_FOR_NON_GEO_ENTITY.equals(regionCodes.get(0))) {
                this.countryCodesForNonGeographicalRegion.add(entry.getKey());
            } else {
                this.supportedRegions.addAll(regionCodes);
            }
        }
        if (this.supportedRegions.remove(REGION_CODE_FOR_NON_GEO_ENTITY)) {
            logger.log(Level.WARNING, "invalid metadata (country calling code was mapped to the non-geo entity as well as specific region(s))");
        }
        this.nanpaRegions.addAll((Collection) countryCallingCodeToRegionCodeMap.get(Integer.valueOf(NANPA_COUNTRY_CODE)));
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x013f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ec  */
    public void loadMetadataFromFile(java.lang.String r16, java.lang.String r17, int r18, com.google.i18n.phonenumbers.MetadataLoader r19) {
        /*
        r15 = this;
        r10 = "001";
        r0 = r17;
        r5 = r10.equals(r0);
        r10 = java.lang.String.valueOf(r16);
        r11 = java.lang.String.valueOf(r10);
        if (r5 == 0) goto L_0x0075;
    L_0x0012:
        r10 = java.lang.String.valueOf(r18);
    L_0x0016:
        r10 = java.lang.String.valueOf(r10);
        r10 = java.lang.String.valueOf(r10);
        r12 = new java.lang.StringBuilder;
        r13 = r11.length();
        r13 = r13 + 1;
        r14 = r10.length();
        r13 = r13 + r14;
        r12.<init>(r13);
        r11 = r12.append(r11);
        r12 = "_";
        r11 = r11.append(r12);
        r10 = r11.append(r10);
        r2 = r10.toString();
        r0 = r19;
        r9 = r0.loadMetadata(r2);
        if (r9 != 0) goto L_0x0084;
    L_0x0048:
        r11 = logger;
        r12 = java.util.logging.Level.SEVERE;
        r13 = "missing metadata: ";
        r10 = java.lang.String.valueOf(r2);
        r14 = r10.length();
        if (r14 == 0) goto L_0x0078;
    L_0x0058:
        r10 = r13.concat(r10);
    L_0x005c:
        r11.log(r12, r10);
        r11 = new java.lang.IllegalStateException;
        r12 = "missing metadata: ";
        r10 = java.lang.String.valueOf(r2);
        r13 = r10.length();
        if (r13 == 0) goto L_0x007e;
    L_0x006d:
        r10 = r12.concat(r10);
    L_0x0071:
        r11.<init>(r10);
        throw r11;
    L_0x0075:
        r10 = r17;
        goto L_0x0016;
    L_0x0078:
        r10 = new java.lang.String;
        r10.<init>(r13);
        goto L_0x005c;
    L_0x007e:
        r10 = new java.lang.String;
        r10.<init>(r12);
        goto L_0x0071;
    L_0x0084:
        r3 = 0;
        r4 = new java.io.ObjectInputStream;	 Catch:{ IOException -> 0x014b }
        r4.<init>(r9);	 Catch:{ IOException -> 0x014b }
        r7 = loadMetadataAndCloseInput(r4);	 Catch:{ IOException -> 0x00c5 }
        r8 = r7.getMetadataList();	 Catch:{ IOException -> 0x00c5 }
        r10 = r8.isEmpty();	 Catch:{ IOException -> 0x00c5 }
        if (r10 == 0) goto L_0x0100;
    L_0x0098:
        r11 = logger;	 Catch:{ IOException -> 0x00c5 }
        r12 = java.util.logging.Level.SEVERE;	 Catch:{ IOException -> 0x00c5 }
        r13 = "empty metadata: ";
        r10 = java.lang.String.valueOf(r2);	 Catch:{ IOException -> 0x00c5 }
        r14 = r10.length();	 Catch:{ IOException -> 0x00c5 }
        if (r14 == 0) goto L_0x00f4;
    L_0x00a8:
        r10 = r13.concat(r10);	 Catch:{ IOException -> 0x00c5 }
    L_0x00ac:
        r11.log(r12, r10);	 Catch:{ IOException -> 0x00c5 }
        r11 = new java.lang.IllegalStateException;	 Catch:{ IOException -> 0x00c5 }
        r12 = "empty metadata: ";
        r10 = java.lang.String.valueOf(r2);	 Catch:{ IOException -> 0x00c5 }
        r13 = r10.length();	 Catch:{ IOException -> 0x00c5 }
        if (r13 == 0) goto L_0x00fa;
    L_0x00bd:
        r10 = r12.concat(r10);	 Catch:{ IOException -> 0x00c5 }
    L_0x00c1:
        r11.<init>(r10);	 Catch:{ IOException -> 0x00c5 }
        throw r11;	 Catch:{ IOException -> 0x00c5 }
    L_0x00c5:
        r1 = move-exception;
        r3 = r4;
    L_0x00c7:
        r11 = logger;
        r12 = java.util.logging.Level.SEVERE;
        r13 = "cannot load/parse metadata: ";
        r10 = java.lang.String.valueOf(r2);
        r14 = r10.length();
        if (r14 == 0) goto L_0x013f;
    L_0x00d7:
        r10 = r13.concat(r10);
    L_0x00db:
        r11.log(r12, r10, r1);
        r11 = new java.lang.RuntimeException;
        r12 = "cannot load/parse metadata: ";
        r10 = java.lang.String.valueOf(r2);
        r13 = r10.length();
        if (r13 == 0) goto L_0x0145;
    L_0x00ec:
        r10 = r12.concat(r10);
    L_0x00f0:
        r11.<init>(r10, r1);
        throw r11;
    L_0x00f4:
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x00c5 }
        r10.<init>(r13);	 Catch:{ IOException -> 0x00c5 }
        goto L_0x00ac;
    L_0x00fa:
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x00c5 }
        r10.<init>(r12);	 Catch:{ IOException -> 0x00c5 }
        goto L_0x00c1;
    L_0x0100:
        r10 = r8.size();	 Catch:{ IOException -> 0x00c5 }
        r11 = 1;
        if (r10 <= r11) goto L_0x011e;
    L_0x0107:
        r11 = logger;	 Catch:{ IOException -> 0x00c5 }
        r12 = java.util.logging.Level.WARNING;	 Catch:{ IOException -> 0x00c5 }
        r13 = "invalid metadata (too many entries): ";
        r10 = java.lang.String.valueOf(r2);	 Catch:{ IOException -> 0x00c5 }
        r14 = r10.length();	 Catch:{ IOException -> 0x00c5 }
        if (r14 == 0) goto L_0x0131;
    L_0x0117:
        r10 = r13.concat(r10);	 Catch:{ IOException -> 0x00c5 }
    L_0x011b:
        r11.log(r12, r10);	 Catch:{ IOException -> 0x00c5 }
    L_0x011e:
        r10 = 0;
        r6 = r8.get(r10);	 Catch:{ IOException -> 0x00c5 }
        r6 = (com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata) r6;	 Catch:{ IOException -> 0x00c5 }
        if (r5 == 0) goto L_0x0137;
    L_0x0127:
        r10 = r15.countryCodeToNonGeographicalMetadataMap;	 Catch:{ IOException -> 0x00c5 }
        r11 = java.lang.Integer.valueOf(r18);	 Catch:{ IOException -> 0x00c5 }
        r10.put(r11, r6);	 Catch:{ IOException -> 0x00c5 }
    L_0x0130:
        return;
    L_0x0131:
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x00c5 }
        r10.<init>(r13);	 Catch:{ IOException -> 0x00c5 }
        goto L_0x011b;
    L_0x0137:
        r10 = r15.regionToMetadataMap;	 Catch:{ IOException -> 0x00c5 }
        r0 = r17;
        r10.put(r0, r6);	 Catch:{ IOException -> 0x00c5 }
        goto L_0x0130;
    L_0x013f:
        r10 = new java.lang.String;
        r10.<init>(r13);
        goto L_0x00db;
    L_0x0145:
        r10 = new java.lang.String;
        r10.<init>(r12);
        goto L_0x00f0;
    L_0x014b:
        r1 = move-exception;
        goto L_0x00c7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.i18n.phonenumbers.PhoneNumberUtil.loadMetadataFromFile(java.lang.String, java.lang.String, int, com.google.i18n.phonenumbers.MetadataLoader):void");
    }

    private static PhoneMetadataCollection loadMetadataAndCloseInput(ObjectInputStream source) {
        PhoneMetadataCollection metadataCollection = new PhoneMetadataCollection();
        try {
            metadataCollection.readExternal(source);
            try {
                source.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", e);
            } catch (Throwable th) {
            }
        } catch (IOException e2) {
            logger.log(Level.WARNING, "error reading input (ignored)", e2);
            try {
                source.close();
            } catch (IOException e22) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", e22);
            } catch (Throwable th2) {
            }
        } catch (Throwable th3) {
            try {
                source.close();
            } catch (IOException e222) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", e222);
            } catch (Throwable th4) {
            }
        }
        return metadataCollection;
    }

    static String extractPossibleNumber(String number) {
        Matcher m = VALID_START_CHAR_PATTERN.matcher(number);
        if (!m.find()) {
            return "";
        }
        number = number.substring(m.start());
        Matcher trailingCharsMatcher = UNWANTED_END_CHAR_PATTERN.matcher(number);
        if (trailingCharsMatcher.find()) {
            number = number.substring(0, trailingCharsMatcher.start());
            Logger logger = logger;
            Level level = Level.FINER;
            String str = "Stripped trailing characters: ";
            String valueOf = String.valueOf(number);
            logger.log(level, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
        Matcher secondNumber = SECOND_NUMBER_START_PATTERN.matcher(number);
        if (secondNumber.find()) {
            return number.substring(0, secondNumber.start());
        }
        return number;
    }

    static boolean isViablePhoneNumber(String number) {
        if (number.length() < MIN_LENGTH_FOR_NSN) {
            return false;
        }
        return VALID_PHONE_NUMBER_PATTERN.matcher(number).matches();
    }

    static String normalize(String number) {
        if (VALID_ALPHA_PHONE_PATTERN.matcher(number).matches()) {
            return normalizeHelper(number, ALPHA_PHONE_MAPPINGS, true);
        }
        return normalizeDigitsOnly(number);
    }

    static void normalize(StringBuilder number) {
        number.replace(0, number.length(), normalize(number.toString()));
    }

    public static String normalizeDigitsOnly(String number) {
        return normalizeDigits(number, false).toString();
    }

    static StringBuilder normalizeDigits(String number, boolean keepNonDigits) {
        StringBuilder normalizedDigits = new StringBuilder(number.length());
        char[] arr$ = number.toCharArray();
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$ += NANPA_COUNTRY_CODE) {
            char c = arr$[i$];
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                normalizedDigits.append(digit);
            } else if (keepNonDigits) {
                normalizedDigits.append(c);
            }
        }
        return normalizedDigits;
    }

    static String normalizeDiallableCharsOnly(String number) {
        return normalizeHelper(number, DIALLABLE_CHAR_MAPPINGS, true);
    }

    public static String convertAlphaCharactersInNumber(String number) {
        return normalizeHelper(number, ALPHA_PHONE_MAPPINGS, false);
    }

    public int getLengthOfGeographicalAreaCode(PhoneNumber number) {
        PhoneMetadata metadata = getMetadataForRegion(getRegionCodeForNumber(number));
        if (metadata == null) {
            return 0;
        }
        if ((metadata.hasNationalPrefix() || number.isItalianLeadingZero()) && isNumberGeographical(number)) {
            return getLengthOfNationalDestinationCode(number);
        }
        return 0;
    }

    public int getLengthOfNationalDestinationCode(PhoneNumber number) {
        PhoneNumber copiedProto;
        if (number.hasExtension()) {
            copiedProto = new PhoneNumber();
            copiedProto.mergeFrom(number);
            copiedProto.clearExtension();
        } else {
            copiedProto = number;
        }
        String[] numberGroups = NON_DIGITS_PATTERN.split(format(copiedProto, PhoneNumberFormat.INTERNATIONAL));
        if (numberGroups.length <= MAX_LENGTH_COUNTRY_CODE) {
            return 0;
        }
        if (getNumberType(number) != PhoneNumberType.MOBILE || getCountryMobileToken(number.getCountryCode()).equals("")) {
            return numberGroups[MIN_LENGTH_FOR_NSN].length();
        }
        return numberGroups[MIN_LENGTH_FOR_NSN].length() + numberGroups[MAX_LENGTH_COUNTRY_CODE].length();
    }

    public static String getCountryMobileToken(int countryCallingCode) {
        if (MOBILE_TOKEN_MAPPINGS.containsKey(Integer.valueOf(countryCallingCode))) {
            return (String) MOBILE_TOKEN_MAPPINGS.get(Integer.valueOf(countryCallingCode));
        }
        return "";
    }

    private static String normalizeHelper(String number, Map<Character, Character> normalizationReplacements, boolean removeNonMatches) {
        StringBuilder normalizedNumber = new StringBuilder(number.length());
        for (int i = 0; i < number.length(); i += NANPA_COUNTRY_CODE) {
            char character = number.charAt(i);
            Character newDigit = (Character) normalizationReplacements.get(Character.valueOf(Character.toUpperCase(character)));
            if (newDigit != null) {
                normalizedNumber.append(newDigit);
            } else if (!removeNonMatches) {
                normalizedNumber.append(character);
            }
        }
        return normalizedNumber.toString();
    }

    static synchronized void setInstance(PhoneNumberUtil util) {
        synchronized (PhoneNumberUtil.class) {
            instance = util;
        }
    }

    public Set<String> getSupportedRegions() {
        return Collections.unmodifiableSet(this.supportedRegions);
    }

    public Set<Integer> getSupportedGlobalNetworkCallingCodes() {
        return Collections.unmodifiableSet(this.countryCodesForNonGeographicalRegion);
    }

    public static synchronized PhoneNumberUtil getInstance() {
        PhoneNumberUtil phoneNumberUtil;
        synchronized (PhoneNumberUtil.class) {
            if (instance == null) {
                setInstance(createInstance(DEFAULT_METADATA_LOADER));
            }
            phoneNumberUtil = instance;
        }
        return phoneNumberUtil;
    }

    public static PhoneNumberUtil createInstance(MetadataLoader metadataLoader) {
        if (metadataLoader != null) {
            return new PhoneNumberUtil(META_DATA_FILE_PREFIX, metadataLoader, CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap());
        }
        throw new IllegalArgumentException("metadataLoader could not be null.");
    }

    static boolean formattingRuleHasFirstGroupOnly(String nationalPrefixFormattingRule) {
        return nationalPrefixFormattingRule.length() == 0 || FIRST_GROUP_ONLY_PREFIX_PATTERN.matcher(nationalPrefixFormattingRule).matches();
    }

    /* access modifiers changed from: 0000 */
    public boolean isNumberGeographical(PhoneNumber phoneNumber) {
        PhoneNumberType numberType = getNumberType(phoneNumber);
        return numberType == PhoneNumberType.FIXED_LINE || numberType == PhoneNumberType.FIXED_LINE_OR_MOBILE;
    }

    private boolean isValidRegionCode(String regionCode) {
        return regionCode != null && this.supportedRegions.contains(regionCode);
    }

    private boolean hasValidCountryCallingCode(int countryCallingCode) {
        return this.countryCallingCodeToRegionCodeMap.containsKey(Integer.valueOf(countryCallingCode));
    }

    public String format(PhoneNumber number, PhoneNumberFormat numberFormat) {
        if (number.getNationalNumber() == 0 && number.hasRawInput()) {
            String rawInput = number.getRawInput();
            if (rawInput.length() > 0) {
                return rawInput;
            }
        }
        StringBuilder formattedNumber = new StringBuilder(20);
        format(number, numberFormat, formattedNumber);
        return formattedNumber.toString();
    }

    public void format(PhoneNumber number, PhoneNumberFormat numberFormat, StringBuilder formattedNumber) {
        formattedNumber.setLength(0);
        int countryCallingCode = number.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(number);
        if (numberFormat == PhoneNumberFormat.E164) {
            formattedNumber.append(nationalSignificantNumber);
            prefixNumberWithCountryCallingCode(countryCallingCode, PhoneNumberFormat.E164, formattedNumber);
        } else if (hasValidCountryCallingCode(countryCallingCode)) {
            PhoneMetadata metadata = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
            formattedNumber.append(formatNsn(nationalSignificantNumber, metadata, numberFormat));
            maybeAppendFormattedExtension(number, metadata, numberFormat, formattedNumber);
            prefixNumberWithCountryCallingCode(countryCallingCode, numberFormat, formattedNumber);
        } else {
            formattedNumber.append(nationalSignificantNumber);
        }
    }

    public String formatByPattern(PhoneNumber number, PhoneNumberFormat numberFormat, List<NumberFormat> userDefinedFormats) {
        int countryCallingCode = number.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(number);
        if (!hasValidCountryCallingCode(countryCallingCode)) {
            return nationalSignificantNumber;
        }
        PhoneMetadata metadata = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
        StringBuilder formattedNumber = new StringBuilder(20);
        NumberFormat formattingPattern = chooseFormattingPatternForNumber(userDefinedFormats, nationalSignificantNumber);
        if (formattingPattern == null) {
            formattedNumber.append(nationalSignificantNumber);
        } else {
            NumberFormat numFormatCopy = new NumberFormat();
            numFormatCopy.mergeFrom(formattingPattern);
            String nationalPrefixFormattingRule = formattingPattern.getNationalPrefixFormattingRule();
            if (nationalPrefixFormattingRule.length() > 0) {
                String nationalPrefix = metadata.getNationalPrefix();
                if (nationalPrefix.length() > 0) {
                    numFormatCopy.setNationalPrefixFormattingRule(FG_PATTERN.matcher(NP_PATTERN.matcher(nationalPrefixFormattingRule).replaceFirst(nationalPrefix)).replaceFirst("\\$1"));
                } else {
                    numFormatCopy.clearNationalPrefixFormattingRule();
                }
            }
            formattedNumber.append(formatNsnUsingPattern(nationalSignificantNumber, numFormatCopy, numberFormat));
        }
        maybeAppendFormattedExtension(number, metadata, numberFormat, formattedNumber);
        prefixNumberWithCountryCallingCode(countryCallingCode, numberFormat, formattedNumber);
        return formattedNumber.toString();
    }

    public String formatNationalNumberWithCarrierCode(PhoneNumber number, String carrierCode) {
        int countryCallingCode = number.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(number);
        if (!hasValidCountryCallingCode(countryCallingCode)) {
            return nationalSignificantNumber;
        }
        PhoneMetadata metadata = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
        StringBuilder formattedNumber = new StringBuilder(20);
        formattedNumber.append(formatNsn(nationalSignificantNumber, metadata, PhoneNumberFormat.NATIONAL, carrierCode));
        maybeAppendFormattedExtension(number, metadata, PhoneNumberFormat.NATIONAL, formattedNumber);
        prefixNumberWithCountryCallingCode(countryCallingCode, PhoneNumberFormat.NATIONAL, formattedNumber);
        return formattedNumber.toString();
    }

    private PhoneMetadata getMetadataForRegionOrCallingCode(int countryCallingCode, String regionCode) {
        return REGION_CODE_FOR_NON_GEO_ENTITY.equals(regionCode) ? getMetadataForNonGeographicalRegion(countryCallingCode) : getMetadataForRegion(regionCode);
    }

    public String formatNationalNumberWithPreferredCarrierCode(PhoneNumber number, String fallbackCarrierCode) {
        if (number.hasPreferredDomesticCarrierCode()) {
            fallbackCarrierCode = number.getPreferredDomesticCarrierCode();
        }
        return formatNationalNumberWithCarrierCode(number, fallbackCarrierCode);
    }

    public String formatNumberForMobileDialing(PhoneNumber number, String regionCallingFrom, boolean withFormatting) {
        int countryCallingCode = number.getCountryCode();
        if (hasValidCountryCallingCode(countryCallingCode)) {
            String formattedNumber = "";
            PhoneNumber numberNoExt = new PhoneNumber().mergeFrom(number).clearExtension();
            String regionCode = getRegionCodeForCountryCode(countryCallingCode);
            PhoneNumberType numberType = getNumberType(numberNoExt);
            boolean isValidNumber = numberType != PhoneNumberType.UNKNOWN;
            if (regionCallingFrom.equals(regionCode)) {
                boolean isFixedLineOrMobile = numberType == PhoneNumberType.FIXED_LINE || numberType == PhoneNumberType.MOBILE || numberType == PhoneNumberType.FIXED_LINE_OR_MOBILE;
                if (regionCode.equals("CO") && numberType == PhoneNumberType.FIXED_LINE) {
                    formattedNumber = formatNationalNumberWithCarrierCode(numberNoExt, COLOMBIA_MOBILE_TO_FIXED_LINE_PREFIX);
                } else if (regionCode.equals("BR") && isFixedLineOrMobile) {
                    formattedNumber = numberNoExt.hasPreferredDomesticCarrierCode() ? formatNationalNumberWithPreferredCarrierCode(numberNoExt, "") : "";
                } else if (isValidNumber && regionCode.equals("HU")) {
                    String valueOf = String.valueOf(String.valueOf(getNddPrefixForRegion(regionCode, true)));
                    String valueOf2 = String.valueOf(String.valueOf(format(numberNoExt, PhoneNumberFormat.NATIONAL)));
                    formattedNumber = new StringBuilder((valueOf.length() + NANPA_COUNTRY_CODE) + valueOf2.length()).append(valueOf).append(" ").append(valueOf2).toString();
                } else if (countryCallingCode == NANPA_COUNTRY_CODE) {
                    formattedNumber = (!canBeInternationallyDialled(numberNoExt) || isShorterThanPossibleNormalNumber(getMetadataForRegion(regionCallingFrom), getNationalSignificantNumber(numberNoExt))) ? format(numberNoExt, PhoneNumberFormat.NATIONAL) : format(numberNoExt, PhoneNumberFormat.INTERNATIONAL);
                } else {
                    formattedNumber = ((regionCode.equals(REGION_CODE_FOR_NON_GEO_ENTITY) || ((regionCode.equals("MX") || regionCode.equals("CL")) && isFixedLineOrMobile)) && canBeInternationallyDialled(numberNoExt)) ? format(numberNoExt, PhoneNumberFormat.INTERNATIONAL) : format(numberNoExt, PhoneNumberFormat.NATIONAL);
                }
            } else if (isValidNumber && canBeInternationallyDialled(numberNoExt)) {
                return withFormatting ? format(numberNoExt, PhoneNumberFormat.INTERNATIONAL) : format(numberNoExt, PhoneNumberFormat.E164);
            }
            if (!withFormatting) {
                formattedNumber = normalizeDiallableCharsOnly(formattedNumber);
            }
            return formattedNumber;
        } else if (number.hasRawInput()) {
            return number.getRawInput();
        } else {
            return "";
        }
    }

    public String formatOutOfCountryCallingNumber(PhoneNumber number, String regionCallingFrom) {
        if (isValidRegionCode(regionCallingFrom)) {
            int countryCallingCode = number.getCountryCode();
            String nationalSignificantNumber = getNationalSignificantNumber(number);
            if (!hasValidCountryCallingCode(countryCallingCode)) {
                return nationalSignificantNumber;
            }
            if (countryCallingCode == NANPA_COUNTRY_CODE) {
                if (isNANPACountry(regionCallingFrom)) {
                    String valueOf = String.valueOf(String.valueOf(format(number, PhoneNumberFormat.NATIONAL)));
                    return new StringBuilder(valueOf.length() + 12).append(countryCallingCode).append(" ").append(valueOf).toString();
                }
            } else if (countryCallingCode == getCountryCodeForValidRegion(regionCallingFrom)) {
                return format(number, PhoneNumberFormat.NATIONAL);
            }
            PhoneMetadata metadataForRegionCallingFrom = getMetadataForRegion(regionCallingFrom);
            String internationalPrefix = metadataForRegionCallingFrom.getInternationalPrefix();
            String internationalPrefixForFormatting = "";
            if (UNIQUE_INTERNATIONAL_PREFIX.matcher(internationalPrefix).matches()) {
                internationalPrefixForFormatting = internationalPrefix;
            } else if (metadataForRegionCallingFrom.hasPreferredInternationalPrefix()) {
                internationalPrefixForFormatting = metadataForRegionCallingFrom.getPreferredInternationalPrefix();
            }
            PhoneMetadata metadataForRegion = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
            StringBuilder formattedNumber = new StringBuilder(formatNsn(nationalSignificantNumber, metadataForRegion, PhoneNumberFormat.INTERNATIONAL));
            maybeAppendFormattedExtension(number, metadataForRegion, PhoneNumberFormat.INTERNATIONAL, formattedNumber);
            if (internationalPrefixForFormatting.length() > 0) {
                formattedNumber.insert(0, " ").insert(0, countryCallingCode).insert(0, " ").insert(0, internationalPrefixForFormatting);
            } else {
                prefixNumberWithCountryCallingCode(countryCallingCode, PhoneNumberFormat.INTERNATIONAL, formattedNumber);
            }
            return formattedNumber.toString();
        }
        Logger logger = logger;
        Level level = Level.WARNING;
        String valueOf2 = String.valueOf(String.valueOf(regionCallingFrom));
        logger.log(level, new StringBuilder(valueOf2.length() + 79).append("Trying to format number from invalid region ").append(valueOf2).append(". International formatting applied.").toString());
        return format(number, PhoneNumberFormat.INTERNATIONAL);
    }

    public String formatInOriginalFormat(PhoneNumber number, String regionCallingFrom) {
        if (number.hasRawInput() && (hasUnexpectedItalianLeadingZero(number) || !hasFormattingPatternForNumber(number))) {
            return number.getRawInput();
        }
        if (!number.hasCountryCodeSource()) {
            return format(number, PhoneNumberFormat.NATIONAL);
        }
        String formattedNumber;
        switch (AnonymousClass3.$SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[number.getCountryCodeSource().ordinal()]) {
            case NANPA_COUNTRY_CODE /*1*/:
                formattedNumber = format(number, PhoneNumberFormat.INTERNATIONAL);
                break;
            case MIN_LENGTH_FOR_NSN /*2*/:
                formattedNumber = formatOutOfCountryCallingNumber(number, regionCallingFrom);
                break;
            case MAX_LENGTH_COUNTRY_CODE /*3*/:
                formattedNumber = format(number, PhoneNumberFormat.INTERNATIONAL).substring(NANPA_COUNTRY_CODE);
                break;
            default:
                String regionCode = getRegionCodeForCountryCode(number.getCountryCode());
                String nationalPrefix = getNddPrefixForRegion(regionCode, true);
                String nationalFormat = format(number, PhoneNumberFormat.NATIONAL);
                if (nationalPrefix != null && nationalPrefix.length() != 0) {
                    if (!rawInputContainsNationalPrefix(number.getRawInput(), nationalPrefix, regionCode)) {
                        PhoneMetadata metadata = getMetadataForRegion(regionCode);
                        NumberFormat formatRule = chooseFormattingPatternForNumber(metadata.numberFormats(), getNationalSignificantNumber(number));
                        if (formatRule != null) {
                            String candidateNationalPrefixRule = formatRule.getNationalPrefixFormattingRule();
                            int indexOfFirstGroup = candidateNationalPrefixRule.indexOf("$1");
                            if (indexOfFirstGroup > 0) {
                                if (normalizeDigitsOnly(candidateNationalPrefixRule.substring(0, indexOfFirstGroup)).length() != 0) {
                                    NumberFormat numFormatCopy = new NumberFormat();
                                    numFormatCopy.mergeFrom(formatRule);
                                    numFormatCopy.clearNationalPrefixFormattingRule();
                                    List<NumberFormat> numberFormats = new ArrayList(NANPA_COUNTRY_CODE);
                                    numberFormats.add(numFormatCopy);
                                    formattedNumber = formatByPattern(number, PhoneNumberFormat.NATIONAL, numberFormats);
                                    break;
                                }
                                formattedNumber = nationalFormat;
                                break;
                            }
                            formattedNumber = nationalFormat;
                            break;
                        }
                        formattedNumber = nationalFormat;
                        break;
                    }
                    formattedNumber = nationalFormat;
                    break;
                }
                formattedNumber = nationalFormat;
                break;
        }
        String rawInput = number.getRawInput();
        if (formattedNumber == null || rawInput.length() <= 0 || normalizeDiallableCharsOnly(formattedNumber).equals(normalizeDiallableCharsOnly(rawInput))) {
            return formattedNumber;
        }
        return rawInput;
    }

    private boolean rawInputContainsNationalPrefix(String rawInput, String nationalPrefix, String regionCode) {
        boolean z = false;
        String normalizedNationalNumber = normalizeDigitsOnly(rawInput);
        if (!normalizedNationalNumber.startsWith(nationalPrefix)) {
            return z;
        }
        try {
            return isValidNumber(parse(normalizedNationalNumber.substring(nationalPrefix.length()), regionCode));
        } catch (NumberParseException e) {
            return z;
        }
    }

    private boolean hasUnexpectedItalianLeadingZero(PhoneNumber number) {
        return number.isItalianLeadingZero() && !isLeadingZeroPossible(number.getCountryCode());
    }

    private boolean hasFormattingPatternForNumber(PhoneNumber number) {
        int countryCallingCode = number.getCountryCode();
        PhoneMetadata metadata = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
        if (metadata == null) {
            return false;
        }
        if (chooseFormattingPatternForNumber(metadata.numberFormats(), getNationalSignificantNumber(number)) != null) {
            return true;
        }
        return false;
    }

    public String formatOutOfCountryKeepingAlphaChars(PhoneNumber number, String regionCallingFrom) {
        String rawInput = number.getRawInput();
        if (rawInput.length() == 0) {
            return formatOutOfCountryCallingNumber(number, regionCallingFrom);
        }
        int countryCode = number.getCountryCode();
        if (!hasValidCountryCallingCode(countryCode)) {
            return rawInput;
        }
        rawInput = normalizeHelper(rawInput, ALL_PLUS_NUMBER_GROUPING_SYMBOLS, true);
        String nationalNumber = getNationalSignificantNumber(number);
        if (nationalNumber.length() > MAX_LENGTH_COUNTRY_CODE) {
            int firstNationalNumberDigit = rawInput.indexOf(nationalNumber.substring(0, MAX_LENGTH_COUNTRY_CODE));
            if (firstNationalNumberDigit != -1) {
                rawInput = rawInput.substring(firstNationalNumberDigit);
            }
        }
        PhoneMetadata metadataForRegionCallingFrom = getMetadataForRegion(regionCallingFrom);
        if (countryCode == NANPA_COUNTRY_CODE) {
            if (isNANPACountry(regionCallingFrom)) {
                String valueOf = String.valueOf(String.valueOf(rawInput));
                return new StringBuilder(valueOf.length() + 12).append(countryCode).append(" ").append(valueOf).toString();
            }
        } else if (metadataForRegionCallingFrom != null && countryCode == getCountryCodeForValidRegion(regionCallingFrom)) {
            NumberFormat formattingPattern = chooseFormattingPatternForNumber(metadataForRegionCallingFrom.numberFormats(), nationalNumber);
            if (formattingPattern == null) {
                return rawInput;
            }
            NumberFormat newFormat = new NumberFormat();
            newFormat.mergeFrom(formattingPattern);
            newFormat.setPattern("(\\d+)(.*)");
            newFormat.setFormat("$1$2");
            return formatNsnUsingPattern(rawInput, newFormat, PhoneNumberFormat.NATIONAL);
        }
        String internationalPrefixForFormatting = "";
        if (metadataForRegionCallingFrom != null) {
            String internationalPrefix = metadataForRegionCallingFrom.getInternationalPrefix();
            internationalPrefixForFormatting = UNIQUE_INTERNATIONAL_PREFIX.matcher(internationalPrefix).matches() ? internationalPrefix : metadataForRegionCallingFrom.getPreferredInternationalPrefix();
        }
        StringBuilder formattedNumber = new StringBuilder(rawInput);
        maybeAppendFormattedExtension(number, getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode)), PhoneNumberFormat.INTERNATIONAL, formattedNumber);
        if (internationalPrefixForFormatting.length() > 0) {
            formattedNumber.insert(0, " ").insert(0, countryCode).insert(0, " ").insert(0, internationalPrefixForFormatting);
        } else {
            Logger logger = logger;
            Level level = Level.WARNING;
            String valueOf2 = String.valueOf(String.valueOf(regionCallingFrom));
            logger.log(level, new StringBuilder(valueOf2.length() + 79).append("Trying to format number from invalid region ").append(valueOf2).append(". International formatting applied.").toString());
            prefixNumberWithCountryCallingCode(countryCode, PhoneNumberFormat.INTERNATIONAL, formattedNumber);
        }
        return formattedNumber.toString();
    }

    public String getNationalSignificantNumber(PhoneNumber number) {
        StringBuilder nationalNumber = new StringBuilder();
        if (number.isItalianLeadingZero()) {
            char[] zeros = new char[number.getNumberOfLeadingZeros()];
            Arrays.fill(zeros, '0');
            nationalNumber.append(new String(zeros));
        }
        nationalNumber.append(number.getNationalNumber());
        return nationalNumber.toString();
    }

    private void prefixNumberWithCountryCallingCode(int countryCallingCode, PhoneNumberFormat numberFormat, StringBuilder formattedNumber) {
        switch (AnonymousClass3.$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberFormat[numberFormat.ordinal()]) {
            case NANPA_COUNTRY_CODE /*1*/:
                formattedNumber.insert(0, countryCallingCode).insert(0, PLUS_SIGN);
                return;
            case MIN_LENGTH_FOR_NSN /*2*/:
                formattedNumber.insert(0, " ").insert(0, countryCallingCode).insert(0, PLUS_SIGN);
                return;
            case MAX_LENGTH_COUNTRY_CODE /*3*/:
                formattedNumber.insert(0, "-").insert(0, countryCallingCode).insert(0, PLUS_SIGN).insert(0, RFC3966_PREFIX);
                return;
            default:
                return;
        }
    }

    private String formatNsn(String number, PhoneMetadata metadata, PhoneNumberFormat numberFormat) {
        return formatNsn(number, metadata, numberFormat, null);
    }

    private String formatNsn(String number, PhoneMetadata metadata, PhoneNumberFormat numberFormat, String carrierCode) {
        List<NumberFormat> availableFormats = (metadata.intlNumberFormats().size() == 0 || numberFormat == PhoneNumberFormat.NATIONAL) ? metadata.numberFormats() : metadata.intlNumberFormats();
        NumberFormat formattingPattern = chooseFormattingPatternForNumber(availableFormats, number);
        return formattingPattern == null ? number : formatNsnUsingPattern(number, formattingPattern, numberFormat, carrierCode);
    }

    /* access modifiers changed from: 0000 */
    public NumberFormat chooseFormattingPatternForNumber(List<NumberFormat> availableFormats, String nationalNumber) {
        for (NumberFormat numFormat : availableFormats) {
            int size = numFormat.leadingDigitsPatternSize();
            if ((size == 0 || this.regexCache.getPatternForRegex(numFormat.getLeadingDigitsPattern(size - 1)).matcher(nationalNumber).lookingAt()) && this.regexCache.getPatternForRegex(numFormat.getPattern()).matcher(nationalNumber).matches()) {
                return numFormat;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public String formatNsnUsingPattern(String nationalNumber, NumberFormat formattingPattern, PhoneNumberFormat numberFormat) {
        return formatNsnUsingPattern(nationalNumber, formattingPattern, numberFormat, null);
    }

    private String formatNsnUsingPattern(String nationalNumber, NumberFormat formattingPattern, PhoneNumberFormat numberFormat, String carrierCode) {
        String numberFormatRule = formattingPattern.getFormat();
        Matcher m = this.regexCache.getPatternForRegex(formattingPattern.getPattern()).matcher(nationalNumber);
        String formattedNationalNumber = "";
        if (numberFormat != PhoneNumberFormat.NATIONAL || carrierCode == null || carrierCode.length() <= 0 || formattingPattern.getDomesticCarrierCodeFormattingRule().length() <= 0) {
            String nationalPrefixFormattingRule = formattingPattern.getNationalPrefixFormattingRule();
            if (numberFormat != PhoneNumberFormat.NATIONAL || nationalPrefixFormattingRule == null || nationalPrefixFormattingRule.length() <= 0) {
                formattedNationalNumber = m.replaceAll(numberFormatRule);
            } else {
                formattedNationalNumber = m.replaceAll(FIRST_GROUP_PATTERN.matcher(numberFormatRule).replaceFirst(nationalPrefixFormattingRule));
            }
        } else {
            formattedNationalNumber = m.replaceAll(FIRST_GROUP_PATTERN.matcher(numberFormatRule).replaceFirst(CC_PATTERN.matcher(formattingPattern.getDomesticCarrierCodeFormattingRule()).replaceFirst(carrierCode)));
        }
        if (numberFormat != PhoneNumberFormat.RFC3966) {
            return formattedNationalNumber;
        }
        Matcher matcher = SEPARATOR_PATTERN.matcher(formattedNationalNumber);
        if (matcher.lookingAt()) {
            formattedNationalNumber = matcher.replaceFirst("");
        }
        return matcher.reset(formattedNationalNumber).replaceAll("-");
    }

    public PhoneNumber getExampleNumber(String regionCode) {
        return getExampleNumberForType(regionCode, PhoneNumberType.FIXED_LINE);
    }

    public PhoneNumber getExampleNumberForType(String regionCode, PhoneNumberType type) {
        if (isValidRegionCode(regionCode)) {
            PhoneNumberDesc desc = getNumberDescByType(getMetadataForRegion(regionCode), type);
            try {
                if (desc.hasExampleNumber()) {
                    return parse(desc.getExampleNumber(), regionCode);
                }
            } catch (NumberParseException e) {
                logger.log(Level.SEVERE, e.toString());
            }
            return null;
        }
        Logger logger = logger;
        Level level = Level.WARNING;
        String str = "Invalid or unknown region code provided: ";
        String valueOf = String.valueOf(regionCode);
        if (valueOf.length() != 0) {
            valueOf = str.concat(valueOf);
        } else {
            valueOf = new String(str);
        }
        logger.log(level, valueOf);
        return null;
    }

    public PhoneNumber getExampleNumberForNonGeoEntity(int countryCallingCode) {
        PhoneMetadata metadata = getMetadataForNonGeographicalRegion(countryCallingCode);
        if (metadata != null) {
            PhoneNumberDesc desc = metadata.getGeneralDesc();
            try {
                if (desc.hasExampleNumber()) {
                    String valueOf = String.valueOf(String.valueOf(desc.getExampleNumber()));
                    return parse(new StringBuilder(valueOf.length() + 12).append("+").append(countryCallingCode).append(valueOf).toString(), UNKNOWN_REGION);
                }
            } catch (NumberParseException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        } else {
            logger.log(Level.WARNING, "Invalid or unknown country calling code provided: " + countryCallingCode);
        }
        return null;
    }

    private void maybeAppendFormattedExtension(PhoneNumber number, PhoneMetadata metadata, PhoneNumberFormat numberFormat, StringBuilder formattedNumber) {
        if (number.hasExtension() && number.getExtension().length() > 0) {
            if (numberFormat == PhoneNumberFormat.RFC3966) {
                formattedNumber.append(RFC3966_EXTN_PREFIX).append(number.getExtension());
            } else if (metadata.hasPreferredExtnPrefix()) {
                formattedNumber.append(metadata.getPreferredExtnPrefix()).append(number.getExtension());
            } else {
                formattedNumber.append(DEFAULT_EXTN_PREFIX).append(number.getExtension());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public PhoneNumberDesc getNumberDescByType(PhoneMetadata metadata, PhoneNumberType type) {
        switch (AnonymousClass3.$SwitchMap$com$google$i18n$phonenumbers$PhoneNumberUtil$PhoneNumberType[type.ordinal()]) {
            case NANPA_COUNTRY_CODE /*1*/:
                return metadata.getPremiumRate();
            case MIN_LENGTH_FOR_NSN /*2*/:
                return metadata.getTollFree();
            case MAX_LENGTH_COUNTRY_CODE /*3*/:
                return metadata.getMobile();
            case 4:
            case 5:
                return metadata.getFixedLine();
            case 6:
                return metadata.getSharedCost();
            case 7:
                return metadata.getVoip();
            case 8:
                return metadata.getPersonalNumber();
            case 9:
                return metadata.getPager();
            case 10:
                return metadata.getUan();
            case 11:
                return metadata.getVoicemail();
            default:
                return metadata.getGeneralDesc();
        }
    }

    public PhoneNumberType getNumberType(PhoneNumber number) {
        PhoneMetadata metadata = getMetadataForRegionOrCallingCode(number.getCountryCode(), getRegionCodeForNumber(number));
        if (metadata == null) {
            return PhoneNumberType.UNKNOWN;
        }
        return getNumberTypeHelper(getNationalSignificantNumber(number), metadata);
    }

    private PhoneNumberType getNumberTypeHelper(String nationalNumber, PhoneMetadata metadata) {
        PhoneNumberDesc generalNumberDesc = metadata.getGeneralDesc();
        if (!generalNumberDesc.hasNationalNumberPattern() || !isNumberMatchingDesc(nationalNumber, generalNumberDesc)) {
            return PhoneNumberType.UNKNOWN;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getPremiumRate())) {
            return PhoneNumberType.PREMIUM_RATE;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getTollFree())) {
            return PhoneNumberType.TOLL_FREE;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getSharedCost())) {
            return PhoneNumberType.SHARED_COST;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getVoip())) {
            return PhoneNumberType.VOIP;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getPersonalNumber())) {
            return PhoneNumberType.PERSONAL_NUMBER;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getPager())) {
            return PhoneNumberType.PAGER;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getUan())) {
            return PhoneNumberType.UAN;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getVoicemail())) {
            return PhoneNumberType.VOICEMAIL;
        }
        if (isNumberMatchingDesc(nationalNumber, metadata.getFixedLine())) {
            if (metadata.isSameMobileAndFixedLinePattern()) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            if (isNumberMatchingDesc(nationalNumber, metadata.getMobile())) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            return PhoneNumberType.FIXED_LINE;
        } else if (metadata.isSameMobileAndFixedLinePattern() || !isNumberMatchingDesc(nationalNumber, metadata.getMobile())) {
            return PhoneNumberType.UNKNOWN;
        } else {
            return PhoneNumberType.MOBILE;
        }
    }

    /* access modifiers changed from: 0000 */
    public PhoneMetadata getMetadataForRegion(String regionCode) {
        if (!isValidRegionCode(regionCode)) {
            return null;
        }
        synchronized (this.regionToMetadataMap) {
            if (!this.regionToMetadataMap.containsKey(regionCode)) {
                loadMetadataFromFile(this.currentFilePrefix, regionCode, 0, this.metadataLoader);
            }
        }
        return (PhoneMetadata) this.regionToMetadataMap.get(regionCode);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Missing block: B:20:?, code skipped:
            return (com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata) r4.countryCodeToNonGeographicalMetadataMap.get(java.lang.Integer.valueOf(r5));
     */
    public com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int r5) {
        /*
        r4 = this;
        r1 = r4.countryCodeToNonGeographicalMetadataMap;
        monitor-enter(r1);
        r0 = r4.countryCallingCodeToRegionCodeMap;	 Catch:{ all -> 0x0035 }
        r2 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0035 }
        r0 = r0.containsKey(r2);	 Catch:{ all -> 0x0035 }
        if (r0 != 0) goto L_0x0012;
    L_0x000f:
        r0 = 0;
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = r4.countryCodeToNonGeographicalMetadataMap;	 Catch:{ all -> 0x0035 }
        r2 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0035 }
        r0 = r0.containsKey(r2);	 Catch:{ all -> 0x0035 }
        if (r0 != 0) goto L_0x0027;
    L_0x001e:
        r0 = r4.currentFilePrefix;	 Catch:{ all -> 0x0035 }
        r2 = "001";
        r3 = r4.metadataLoader;	 Catch:{ all -> 0x0035 }
        r4.loadMetadataFromFile(r0, r2, r5, r3);	 Catch:{ all -> 0x0035 }
    L_0x0027:
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
        r0 = r4.countryCodeToNonGeographicalMetadataMap;
        r1 = java.lang.Integer.valueOf(r5);
        r0 = r0.get(r1);
        r0 = (com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata) r0;
        goto L_0x0011;
    L_0x0035:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0035 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.i18n.phonenumbers.PhoneNumberUtil.getMetadataForNonGeographicalRegion(int):com.google.i18n.phonenumbers.Phonemetadata$PhoneMetadata");
    }

    /* access modifiers changed from: 0000 */
    public boolean isNumberPossibleForDesc(String nationalNumber, PhoneNumberDesc numberDesc) {
        return this.regexCache.getPatternForRegex(numberDesc.getPossibleNumberPattern()).matcher(nationalNumber).matches();
    }

    /* access modifiers changed from: 0000 */
    public boolean isNumberMatchingDesc(String nationalNumber, PhoneNumberDesc numberDesc) {
        return isNumberPossibleForDesc(nationalNumber, numberDesc) && this.regexCache.getPatternForRegex(numberDesc.getNationalNumberPattern()).matcher(nationalNumber).matches();
    }

    public boolean isValidNumber(PhoneNumber number) {
        return isValidNumberForRegion(number, getRegionCodeForNumber(number));
    }

    public boolean isValidNumberForRegion(PhoneNumber number, String regionCode) {
        int countryCode = number.getCountryCode();
        PhoneMetadata metadata = getMetadataForRegionOrCallingCode(countryCode, regionCode);
        if (metadata == null || (!REGION_CODE_FOR_NON_GEO_ENTITY.equals(regionCode) && countryCode != getCountryCodeForValidRegion(regionCode))) {
            return false;
        }
        PhoneNumberDesc generalNumDesc = metadata.getGeneralDesc();
        String nationalSignificantNumber = getNationalSignificantNumber(number);
        if (!generalNumDesc.hasNationalNumberPattern()) {
            int numberLength = nationalSignificantNumber.length();
            if (numberLength <= MIN_LENGTH_FOR_NSN || numberLength > MAX_LENGTH_FOR_NSN) {
                return false;
            }
            return true;
        } else if (getNumberTypeHelper(nationalSignificantNumber, metadata) == PhoneNumberType.UNKNOWN) {
            return false;
        } else {
            return true;
        }
    }

    public String getRegionCodeForNumber(PhoneNumber number) {
        int countryCode = number.getCountryCode();
        List<String> regions = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(countryCode));
        if (regions == null) {
            String numberString = getNationalSignificantNumber(number);
            Logger logger = logger;
            Level level = Level.WARNING;
            String valueOf = String.valueOf(String.valueOf(numberString));
            logger.log(level, new StringBuilder(valueOf.length() + 54).append("Missing/invalid country_code (").append(countryCode).append(") for number ").append(valueOf).toString());
            return null;
        } else if (regions.size() == NANPA_COUNTRY_CODE) {
            return (String) regions.get(0);
        } else {
            return getRegionCodeForNumberFromRegionList(number, regions);
        }
    }

    private String getRegionCodeForNumberFromRegionList(PhoneNumber number, List<String> regionCodes) {
        String nationalNumber = getNationalSignificantNumber(number);
        for (String regionCode : regionCodes) {
            PhoneMetadata metadata = getMetadataForRegion(regionCode);
            if (metadata.hasLeadingDigits()) {
                if (this.regexCache.getPatternForRegex(metadata.getLeadingDigits()).matcher(nationalNumber).lookingAt()) {
                    return regionCode;
                }
            } else if (getNumberTypeHelper(nationalNumber, metadata) != PhoneNumberType.UNKNOWN) {
                return regionCode;
            }
        }
        return null;
    }

    public String getRegionCodeForCountryCode(int countryCallingCode) {
        List<String> regionCodes = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(countryCallingCode));
        return regionCodes == null ? UNKNOWN_REGION : (String) regionCodes.get(0);
    }

    public List<String> getRegionCodesForCountryCode(int countryCallingCode) {
        List list = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(countryCallingCode));
        if (list == null) {
            list = new ArrayList(0);
        }
        return Collections.unmodifiableList(list);
    }

    public int getCountryCodeForRegion(String regionCode) {
        if (isValidRegionCode(regionCode)) {
            return getCountryCodeForValidRegion(regionCode);
        }
        Logger logger = logger;
        Level level = Level.WARNING;
        if (regionCode == null) {
            regionCode = "null";
        }
        String valueOf = String.valueOf(String.valueOf(regionCode));
        logger.log(level, new StringBuilder(valueOf.length() + 43).append("Invalid or missing region code (").append(valueOf).append(") provided.").toString());
        return 0;
    }

    private int getCountryCodeForValidRegion(String regionCode) {
        PhoneMetadata metadata = getMetadataForRegion(regionCode);
        if (metadata != null) {
            return metadata.getCountryCode();
        }
        String str = "Invalid region code: ";
        String valueOf = String.valueOf(regionCode);
        throw new IllegalArgumentException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }

    public String getNddPrefixForRegion(String regionCode, boolean stripNonDigits) {
        PhoneMetadata metadata = getMetadataForRegion(regionCode);
        if (metadata == null) {
            Logger logger = logger;
            Level level = Level.WARNING;
            if (regionCode == null) {
                regionCode = "null";
            }
            String valueOf = String.valueOf(String.valueOf(regionCode));
            logger.log(level, new StringBuilder(valueOf.length() + 43).append("Invalid or missing region code (").append(valueOf).append(") provided.").toString());
            return null;
        }
        String nationalPrefix = metadata.getNationalPrefix();
        if (nationalPrefix.length() == 0) {
            return null;
        }
        if (stripNonDigits) {
            return nationalPrefix.replace("~", "");
        }
        return nationalPrefix;
    }

    public boolean isNANPACountry(String regionCode) {
        return this.nanpaRegions.contains(regionCode);
    }

    /* access modifiers changed from: 0000 */
    public boolean isLeadingZeroPossible(int countryCallingCode) {
        PhoneMetadata mainMetadataForCallingCode = getMetadataForRegionOrCallingCode(countryCallingCode, getRegionCodeForCountryCode(countryCallingCode));
        if (mainMetadataForCallingCode == null) {
            return false;
        }
        return mainMetadataForCallingCode.isLeadingZeroPossible();
    }

    public boolean isAlphaNumber(String number) {
        if (!isViablePhoneNumber(number)) {
            return false;
        }
        StringBuilder strippedNumber = new StringBuilder(number);
        maybeStripExtension(strippedNumber);
        return VALID_ALPHA_PHONE_PATTERN.matcher(strippedNumber).matches();
    }

    public boolean isPossibleNumber(PhoneNumber number) {
        return isPossibleNumberWithReason(number) == ValidationResult.IS_POSSIBLE;
    }

    private ValidationResult testNumberLengthAgainstPattern(Pattern numberPattern, String number) {
        Matcher numberMatcher = numberPattern.matcher(number);
        if (numberMatcher.matches()) {
            return ValidationResult.IS_POSSIBLE;
        }
        if (numberMatcher.lookingAt()) {
            return ValidationResult.TOO_LONG;
        }
        return ValidationResult.TOO_SHORT;
    }

    private boolean isShorterThanPossibleNormalNumber(PhoneMetadata regionMetadata, String number) {
        return testNumberLengthAgainstPattern(this.regexCache.getPatternForRegex(regionMetadata.getGeneralDesc().getPossibleNumberPattern()), number) == ValidationResult.TOO_SHORT;
    }

    public ValidationResult isPossibleNumberWithReason(PhoneNumber number) {
        String nationalNumber = getNationalSignificantNumber(number);
        int countryCode = number.getCountryCode();
        if (!hasValidCountryCallingCode(countryCode)) {
            return ValidationResult.INVALID_COUNTRY_CODE;
        }
        PhoneNumberDesc generalNumDesc = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode)).getGeneralDesc();
        if (generalNumDesc.hasNationalNumberPattern()) {
            return testNumberLengthAgainstPattern(this.regexCache.getPatternForRegex(generalNumDesc.getPossibleNumberPattern()), nationalNumber);
        }
        logger.log(Level.FINER, "Checking if number is possible with incomplete metadata.");
        int numberLength = nationalNumber.length();
        if (numberLength < MIN_LENGTH_FOR_NSN) {
            return ValidationResult.TOO_SHORT;
        }
        if (numberLength > MAX_LENGTH_FOR_NSN) {
            return ValidationResult.TOO_LONG;
        }
        return ValidationResult.IS_POSSIBLE;
    }

    public boolean isPossibleNumber(String number, String regionDialingFrom) {
        try {
            return isPossibleNumber(parse(number, regionDialingFrom));
        } catch (NumberParseException e) {
            return false;
        }
    }

    public boolean truncateTooLongNumber(PhoneNumber number) {
        if (isValidNumber(number)) {
            return true;
        }
        PhoneNumber numberCopy = new PhoneNumber();
        numberCopy.mergeFrom(number);
        long nationalNumber = number.getNationalNumber();
        do {
            nationalNumber /= 10;
            numberCopy.setNationalNumber(nationalNumber);
            if (isPossibleNumberWithReason(numberCopy) == ValidationResult.TOO_SHORT || nationalNumber == 0) {
                return false;
            }
        } while (!isValidNumber(numberCopy));
        number.setNationalNumber(nationalNumber);
        return true;
    }

    public AsYouTypeFormatter getAsYouTypeFormatter(String regionCode) {
        return new AsYouTypeFormatter(regionCode);
    }

    /* access modifiers changed from: 0000 */
    public int extractCountryCode(StringBuilder fullNumber, StringBuilder nationalNumber) {
        if (fullNumber.length() == 0 || fullNumber.charAt(0) == '0') {
            return 0;
        }
        int numberLength = fullNumber.length();
        int i = NANPA_COUNTRY_CODE;
        while (i <= MAX_LENGTH_COUNTRY_CODE && i <= numberLength) {
            int potentialCountryCode = Integer.parseInt(fullNumber.substring(0, i));
            if (this.countryCallingCodeToRegionCodeMap.containsKey(Integer.valueOf(potentialCountryCode))) {
                nationalNumber.append(fullNumber.substring(i));
                return potentialCountryCode;
            }
            i += NANPA_COUNTRY_CODE;
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int maybeExtractCountryCode(String number, PhoneMetadata defaultRegionMetadata, StringBuilder nationalNumber, boolean keepRawInput, PhoneNumber phoneNumber) throws NumberParseException {
        if (number.length() == 0) {
            return 0;
        }
        StringBuilder fullNumber = new StringBuilder(number);
        String possibleCountryIddPrefix = "NonMatch";
        if (defaultRegionMetadata != null) {
            possibleCountryIddPrefix = defaultRegionMetadata.getInternationalPrefix();
        }
        CountryCodeSource countryCodeSource = maybeStripInternationalPrefixAndNormalize(fullNumber, possibleCountryIddPrefix);
        if (keepRawInput) {
            phoneNumber.setCountryCodeSource(countryCodeSource);
        }
        if (countryCodeSource == CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            if (defaultRegionMetadata != null) {
                int defaultCountryCode = defaultRegionMetadata.getCountryCode();
                String defaultCountryCodeString = String.valueOf(defaultCountryCode);
                String normalizedNumber = fullNumber.toString();
                if (normalizedNumber.startsWith(defaultCountryCodeString)) {
                    StringBuilder potentialNationalNumber = new StringBuilder(normalizedNumber.substring(defaultCountryCodeString.length()));
                    PhoneNumberDesc generalDesc = defaultRegionMetadata.getGeneralDesc();
                    Pattern validNumberPattern = this.regexCache.getPatternForRegex(generalDesc.getNationalNumberPattern());
                    maybeStripNationalPrefixAndCarrierCode(potentialNationalNumber, defaultRegionMetadata, null);
                    Pattern possibleNumberPattern = this.regexCache.getPatternForRegex(generalDesc.getPossibleNumberPattern());
                    if ((!validNumberPattern.matcher(fullNumber).matches() && validNumberPattern.matcher(potentialNationalNumber).matches()) || testNumberLengthAgainstPattern(possibleNumberPattern, fullNumber.toString()) == ValidationResult.TOO_LONG) {
                        nationalNumber.append(potentialNationalNumber);
                        if (keepRawInput) {
                            phoneNumber.setCountryCodeSource(CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN);
                        }
                        phoneNumber.setCountryCode(defaultCountryCode);
                        return defaultCountryCode;
                    }
                }
            }
            phoneNumber.setCountryCode(0);
            return 0;
        } else if (fullNumber.length() <= MIN_LENGTH_FOR_NSN) {
            throw new NumberParseException(ErrorType.TOO_SHORT_AFTER_IDD, "Phone number had an IDD, but after this was not long enough to be a viable phone number.");
        } else {
            int potentialCountryCode = extractCountryCode(fullNumber, nationalNumber);
            if (potentialCountryCode != 0) {
                phoneNumber.setCountryCode(potentialCountryCode);
                return potentialCountryCode;
            }
            throw new NumberParseException(ErrorType.INVALID_COUNTRY_CODE, "Country calling code supplied was not recognised.");
        }
    }

    private boolean parsePrefixAsIdd(Pattern iddPattern, StringBuilder number) {
        Matcher m = iddPattern.matcher(number);
        if (!m.lookingAt()) {
            return false;
        }
        int matchEnd = m.end();
        Matcher digitMatcher = CAPTURING_DIGIT_PATTERN.matcher(number.substring(matchEnd));
        if (digitMatcher.find() && normalizeDigitsOnly(digitMatcher.group(NANPA_COUNTRY_CODE)).equals("0")) {
            return false;
        }
        number.delete(0, matchEnd);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public CountryCodeSource maybeStripInternationalPrefixAndNormalize(StringBuilder number, String possibleIddPrefix) {
        if (number.length() == 0) {
            return CountryCodeSource.FROM_DEFAULT_COUNTRY;
        }
        Matcher m = PLUS_CHARS_PATTERN.matcher(number);
        if (m.lookingAt()) {
            number.delete(0, m.end());
            normalize(number);
            return CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN;
        }
        Pattern iddPattern = this.regexCache.getPatternForRegex(possibleIddPrefix);
        normalize(number);
        return parsePrefixAsIdd(iddPattern, number) ? CountryCodeSource.FROM_NUMBER_WITH_IDD : CountryCodeSource.FROM_DEFAULT_COUNTRY;
    }

    /* access modifiers changed from: 0000 */
    public boolean maybeStripNationalPrefixAndCarrierCode(StringBuilder number, PhoneMetadata metadata, StringBuilder carrierCode) {
        int numberLength = number.length();
        String possibleNationalPrefix = metadata.getNationalPrefixForParsing();
        if (numberLength == 0 || possibleNationalPrefix.length() == 0) {
            return false;
        }
        Matcher prefixMatcher = this.regexCache.getPatternForRegex(possibleNationalPrefix).matcher(number);
        if (!prefixMatcher.lookingAt()) {
            return false;
        }
        Pattern nationalNumberRule = this.regexCache.getPatternForRegex(metadata.getGeneralDesc().getNationalNumberPattern());
        boolean isViableOriginalNumber = nationalNumberRule.matcher(number).matches();
        int numOfGroups = prefixMatcher.groupCount();
        String transformRule = metadata.getNationalPrefixTransformRule();
        if (transformRule != null && transformRule.length() != 0 && prefixMatcher.group(numOfGroups) != null) {
            StringBuilder transformedNumber = new StringBuilder(number);
            transformedNumber.replace(0, numberLength, prefixMatcher.replaceFirst(transformRule));
            if (isViableOriginalNumber && !nationalNumberRule.matcher(transformedNumber.toString()).matches()) {
                return false;
            }
            if (carrierCode != null && numOfGroups > NANPA_COUNTRY_CODE) {
                carrierCode.append(prefixMatcher.group(NANPA_COUNTRY_CODE));
            }
            number.replace(0, number.length(), transformedNumber.toString());
            return true;
        } else if (isViableOriginalNumber && !nationalNumberRule.matcher(number.substring(prefixMatcher.end())).matches()) {
            return false;
        } else {
            if (!(carrierCode == null || numOfGroups <= 0 || prefixMatcher.group(numOfGroups) == null)) {
                carrierCode.append(prefixMatcher.group(NANPA_COUNTRY_CODE));
            }
            number.delete(0, prefixMatcher.end());
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public String maybeStripExtension(StringBuilder number) {
        Matcher m = EXTN_PATTERN.matcher(number);
        if (m.find() && isViablePhoneNumber(number.substring(0, m.start()))) {
            int length = m.groupCount();
            for (int i = NANPA_COUNTRY_CODE; i <= length; i += NANPA_COUNTRY_CODE) {
                if (m.group(i) != null) {
                    String extension = m.group(i);
                    number.delete(m.start(), number.length());
                    return extension;
                }
            }
        }
        return "";
    }

    private boolean checkRegionForParsing(String numberToParse, String defaultRegion) {
        if (isValidRegionCode(defaultRegion) || (numberToParse != null && numberToParse.length() != 0 && PLUS_CHARS_PATTERN.matcher(numberToParse).lookingAt())) {
            return true;
        }
        return false;
    }

    public PhoneNumber parse(String numberToParse, String defaultRegion) throws NumberParseException {
        PhoneNumber phoneNumber = new PhoneNumber();
        parse(numberToParse, defaultRegion, phoneNumber);
        return phoneNumber;
    }

    public void parse(String numberToParse, String defaultRegion, PhoneNumber phoneNumber) throws NumberParseException {
        parseHelper(numberToParse, defaultRegion, false, true, phoneNumber);
    }

    public PhoneNumber parseAndKeepRawInput(String numberToParse, String defaultRegion) throws NumberParseException {
        PhoneNumber phoneNumber = new PhoneNumber();
        parseAndKeepRawInput(numberToParse, defaultRegion, phoneNumber);
        return phoneNumber;
    }

    public void parseAndKeepRawInput(String numberToParse, String defaultRegion, PhoneNumber phoneNumber) throws NumberParseException {
        parseHelper(numberToParse, defaultRegion, true, true, phoneNumber);
    }

    public Iterable<PhoneNumberMatch> findNumbers(CharSequence text, String defaultRegion) {
        return findNumbers(text, defaultRegion, Leniency.VALID, Long.MAX_VALUE);
    }

    public Iterable<PhoneNumberMatch> findNumbers(CharSequence text, String defaultRegion, Leniency leniency, long maxTries) {
        final CharSequence charSequence = text;
        final String str = defaultRegion;
        final Leniency leniency2 = leniency;
        final long j = maxTries;
        return new Iterable<PhoneNumberMatch>() {
            public Iterator<PhoneNumberMatch> iterator() {
                return new PhoneNumberMatcher(PhoneNumberUtil.this, charSequence, str, leniency2, j);
            }
        };
    }

    static void setItalianLeadingZerosForPhoneNumber(String nationalNumber, PhoneNumber phoneNumber) {
        if (nationalNumber.length() > NANPA_COUNTRY_CODE && nationalNumber.charAt(0) == '0') {
            phoneNumber.setItalianLeadingZero(true);
            int numberOfLeadingZeros = NANPA_COUNTRY_CODE;
            while (numberOfLeadingZeros < nationalNumber.length() - 1 && nationalNumber.charAt(numberOfLeadingZeros) == '0') {
                numberOfLeadingZeros += NANPA_COUNTRY_CODE;
            }
            if (numberOfLeadingZeros != NANPA_COUNTRY_CODE) {
                phoneNumber.setNumberOfLeadingZeros(numberOfLeadingZeros);
            }
        }
    }

    private void parseHelper(String numberToParse, String defaultRegion, boolean keepRawInput, boolean checkRegion, PhoneNumber phoneNumber) throws NumberParseException {
        int countryCode;
        if (numberToParse == null) {
            throw new NumberParseException(ErrorType.NOT_A_NUMBER, "The phone number supplied was null.");
        } else if (numberToParse.length() > MAX_INPUT_STRING_LENGTH) {
            throw new NumberParseException(ErrorType.TOO_LONG, "The string supplied was too long to parse.");
        } else {
            StringBuilder nationalNumber = new StringBuilder();
            buildNationalNumberForParsing(numberToParse, nationalNumber);
            if (isViablePhoneNumber(nationalNumber.toString())) {
                if (checkRegion) {
                    if (!checkRegionForParsing(nationalNumber.toString(), defaultRegion)) {
                        throw new NumberParseException(ErrorType.INVALID_COUNTRY_CODE, "Missing or invalid default region.");
                    }
                }
                if (keepRawInput) {
                    phoneNumber.setRawInput(numberToParse);
                }
                String extension = maybeStripExtension(nationalNumber);
                if (extension.length() > 0) {
                    phoneNumber.setExtension(extension);
                }
                PhoneMetadata regionMetadata = getMetadataForRegion(defaultRegion);
                StringBuilder normalizedNationalNumber = new StringBuilder();
                try {
                    countryCode = maybeExtractCountryCode(nationalNumber.toString(), regionMetadata, normalizedNationalNumber, keepRawInput, phoneNumber);
                } catch (NumberParseException e) {
                    Matcher matcher = PLUS_CHARS_PATTERN.matcher(nationalNumber.toString());
                    if (e.getErrorType() == ErrorType.INVALID_COUNTRY_CODE && matcher.lookingAt()) {
                        countryCode = maybeExtractCountryCode(nationalNumber.substring(matcher.end()), regionMetadata, normalizedNationalNumber, keepRawInput, phoneNumber);
                        if (countryCode == 0) {
                            throw new NumberParseException(ErrorType.INVALID_COUNTRY_CODE, "Could not interpret numbers after plus-sign.");
                        }
                    }
                    throw new NumberParseException(e.getErrorType(), e.getMessage());
                }
                if (countryCode != 0) {
                    String phoneNumberRegion = getRegionCodeForCountryCode(countryCode);
                    if (!phoneNumberRegion.equals(defaultRegion)) {
                        regionMetadata = getMetadataForRegionOrCallingCode(countryCode, phoneNumberRegion);
                    }
                } else {
                    normalize(nationalNumber);
                    normalizedNationalNumber.append(nationalNumber);
                    if (defaultRegion != null) {
                        phoneNumber.setCountryCode(regionMetadata.getCountryCode());
                    } else if (keepRawInput) {
                        phoneNumber.clearCountryCodeSource();
                    }
                }
                if (normalizedNationalNumber.length() < MIN_LENGTH_FOR_NSN) {
                    throw new NumberParseException(ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
                }
                if (regionMetadata != null) {
                    StringBuilder carrierCode = new StringBuilder();
                    StringBuilder stringBuilder = new StringBuilder(normalizedNationalNumber);
                    maybeStripNationalPrefixAndCarrierCode(stringBuilder, regionMetadata, carrierCode);
                    if (!isShorterThanPossibleNormalNumber(regionMetadata, stringBuilder.toString())) {
                        normalizedNationalNumber = stringBuilder;
                        if (keepRawInput) {
                            phoneNumber.setPreferredDomesticCarrierCode(carrierCode.toString());
                        }
                    }
                }
                int lengthOfNationalNumber = normalizedNationalNumber.length();
                if (lengthOfNationalNumber < MIN_LENGTH_FOR_NSN) {
                    throw new NumberParseException(ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
                } else if (lengthOfNationalNumber > MAX_LENGTH_FOR_NSN) {
                    throw new NumberParseException(ErrorType.TOO_LONG, "The string supplied is too long to be a phone number.");
                } else {
                    setItalianLeadingZerosForPhoneNumber(normalizedNationalNumber.toString(), phoneNumber);
                    phoneNumber.setNationalNumber(Long.parseLong(normalizedNationalNumber.toString()));
                    return;
                }
            }
            throw new NumberParseException(ErrorType.NOT_A_NUMBER, "The string supplied did not seem to be a phone number.");
        }
    }

    private void buildNationalNumberForParsing(String numberToParse, StringBuilder nationalNumber) {
        int indexOfPhoneContext = numberToParse.indexOf(RFC3966_PHONE_CONTEXT);
        if (indexOfPhoneContext > 0) {
            int phoneContextStart = indexOfPhoneContext + RFC3966_PHONE_CONTEXT.length();
            if (numberToParse.charAt(phoneContextStart) == PLUS_SIGN) {
                int phoneContextEnd = numberToParse.indexOf(59, phoneContextStart);
                if (phoneContextEnd > 0) {
                    nationalNumber.append(numberToParse.substring(phoneContextStart, phoneContextEnd));
                } else {
                    nationalNumber.append(numberToParse.substring(phoneContextStart));
                }
            }
            int indexOfRfc3966Prefix = numberToParse.indexOf(RFC3966_PREFIX);
            nationalNumber.append(numberToParse.substring(indexOfRfc3966Prefix >= 0 ? indexOfRfc3966Prefix + RFC3966_PREFIX.length() : 0, indexOfPhoneContext));
        } else {
            nationalNumber.append(extractPossibleNumber(numberToParse));
        }
        int indexOfIsdn = nationalNumber.indexOf(RFC3966_ISDN_SUBADDRESS);
        if (indexOfIsdn > 0) {
            nationalNumber.delete(indexOfIsdn, nationalNumber.length());
        }
    }

    public MatchType isNumberMatch(PhoneNumber firstNumberIn, PhoneNumber secondNumberIn) {
        PhoneNumber firstNumber = new PhoneNumber();
        firstNumber.mergeFrom(firstNumberIn);
        PhoneNumber secondNumber = new PhoneNumber();
        secondNumber.mergeFrom(secondNumberIn);
        firstNumber.clearRawInput();
        firstNumber.clearCountryCodeSource();
        firstNumber.clearPreferredDomesticCarrierCode();
        secondNumber.clearRawInput();
        secondNumber.clearCountryCodeSource();
        secondNumber.clearPreferredDomesticCarrierCode();
        if (firstNumber.hasExtension() && firstNumber.getExtension().length() == 0) {
            firstNumber.clearExtension();
        }
        if (secondNumber.hasExtension() && secondNumber.getExtension().length() == 0) {
            secondNumber.clearExtension();
        }
        if (firstNumber.hasExtension() && secondNumber.hasExtension() && !firstNumber.getExtension().equals(secondNumber.getExtension())) {
            return MatchType.NO_MATCH;
        }
        int firstNumberCountryCode = firstNumber.getCountryCode();
        int secondNumberCountryCode = secondNumber.getCountryCode();
        if (firstNumberCountryCode == 0 || secondNumberCountryCode == 0) {
            firstNumber.setCountryCode(secondNumberCountryCode);
            if (firstNumber.exactlySameAs(secondNumber)) {
                return MatchType.NSN_MATCH;
            }
            if (isNationalNumberSuffixOfTheOther(firstNumber, secondNumber)) {
                return MatchType.SHORT_NSN_MATCH;
            }
            return MatchType.NO_MATCH;
        } else if (firstNumber.exactlySameAs(secondNumber)) {
            return MatchType.EXACT_MATCH;
        } else {
            if (firstNumberCountryCode == secondNumberCountryCode && isNationalNumberSuffixOfTheOther(firstNumber, secondNumber)) {
                return MatchType.SHORT_NSN_MATCH;
            }
            return MatchType.NO_MATCH;
        }
    }

    private boolean isNationalNumberSuffixOfTheOther(PhoneNumber firstNumber, PhoneNumber secondNumber) {
        String firstNumberNationalNumber = String.valueOf(firstNumber.getNationalNumber());
        String secondNumberNationalNumber = String.valueOf(secondNumber.getNationalNumber());
        return firstNumberNationalNumber.endsWith(secondNumberNationalNumber) || secondNumberNationalNumber.endsWith(firstNumberNationalNumber);
    }

    public MatchType isNumberMatch(String firstNumber, String secondNumber) {
        try {
            return isNumberMatch(parse(firstNumber, UNKNOWN_REGION), secondNumber);
        } catch (NumberParseException e) {
            if (e.getErrorType() == ErrorType.INVALID_COUNTRY_CODE) {
                try {
                    return isNumberMatch(parse(secondNumber, UNKNOWN_REGION), firstNumber);
                } catch (NumberParseException e2) {
                    if (e2.getErrorType() == ErrorType.INVALID_COUNTRY_CODE) {
                        try {
                            PhoneNumber firstNumberProto = new PhoneNumber();
                            PhoneNumber secondNumberProto = new PhoneNumber();
                            parseHelper(firstNumber, null, false, false, firstNumberProto);
                            parseHelper(secondNumber, null, false, false, secondNumberProto);
                            return isNumberMatch(firstNumberProto, secondNumberProto);
                        } catch (NumberParseException e3) {
                        }
                    }
                }
            }
            return MatchType.NOT_A_NUMBER;
        }
    }

    public MatchType isNumberMatch(PhoneNumber firstNumber, String secondNumber) {
        try {
            return isNumberMatch(firstNumber, parse(secondNumber, UNKNOWN_REGION));
        } catch (NumberParseException e) {
            if (e.getErrorType() == ErrorType.INVALID_COUNTRY_CODE) {
                String firstNumberRegion = getRegionCodeForCountryCode(firstNumber.getCountryCode());
                try {
                    if (firstNumberRegion.equals(UNKNOWN_REGION)) {
                        PhoneNumber secondNumberProto = new PhoneNumber();
                        parseHelper(secondNumber, null, false, false, secondNumberProto);
                        return isNumberMatch(firstNumber, secondNumberProto);
                    }
                    MatchType match = isNumberMatch(firstNumber, parse(secondNumber, firstNumberRegion));
                    if (match == MatchType.EXACT_MATCH) {
                        return MatchType.NSN_MATCH;
                    }
                    return match;
                } catch (NumberParseException e2) {
                    return MatchType.NOT_A_NUMBER;
                }
            }
            return MatchType.NOT_A_NUMBER;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean canBeInternationallyDialled(PhoneNumber number) {
        PhoneMetadata metadata = getMetadataForRegion(getRegionCodeForNumber(number));
        if (metadata != null && isNumberMatchingDesc(getNationalSignificantNumber(number), metadata.getNoInternationalDialling())) {
            return false;
        }
        return true;
    }

    public boolean isMobileNumberPortableRegion(String regionCode) {
        PhoneMetadata metadata = getMetadataForRegion(regionCode);
        if (metadata != null) {
            return metadata.isMobileNumberPortableRegion();
        }
        Logger logger = logger;
        Level level = Level.WARNING;
        String str = "Invalid or unknown region code provided: ";
        String valueOf = String.valueOf(regionCode);
        logger.log(level, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        return false;
    }
}
