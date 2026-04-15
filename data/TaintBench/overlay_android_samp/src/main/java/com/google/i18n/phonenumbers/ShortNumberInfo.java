package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneNumberDesc;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ShortNumberInfo {
    private static final ShortNumberInfo INSTANCE = new ShortNumberInfo(PhoneNumberUtil.getInstance());
    private static final Set<String> REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT = new HashSet();
    private static final Logger logger = Logger.getLogger(ShortNumberInfo.class.getName());
    private final PhoneNumberUtil phoneUtil;

    public enum ShortNumberCost {
        TOLL_FREE,
        STANDARD_RATE,
        PREMIUM_RATE,
        UNKNOWN_COST
    }

    static {
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("BR");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("CL");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("NI");
    }

    public static ShortNumberInfo getInstance() {
        return INSTANCE;
    }

    ShortNumberInfo(PhoneNumberUtil util) {
        this.phoneUtil = util;
    }

    public boolean isPossibleShortNumberForRegion(String shortNumber, String regionDialingFrom) {
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionDialingFrom);
        if (phoneMetadata == null) {
            return false;
        }
        return this.phoneUtil.isNumberPossibleForDesc(shortNumber, phoneMetadata.getGeneralDesc());
    }

    public boolean isPossibleShortNumber(PhoneNumber number) {
        List<String> regionCodes = this.phoneUtil.getRegionCodesForCountryCode(number.getCountryCode());
        String shortNumber = this.phoneUtil.getNationalSignificantNumber(number);
        for (String region : regionCodes) {
            if (this.phoneUtil.isNumberPossibleForDesc(shortNumber, MetadataManager.getShortNumberMetadataForRegion(region).getGeneralDesc())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidShortNumberForRegion(String shortNumber, String regionDialingFrom) {
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionDialingFrom);
        if (phoneMetadata == null) {
            return false;
        }
        PhoneNumberDesc generalDesc = phoneMetadata.getGeneralDesc();
        if (!generalDesc.hasNationalNumberPattern() || !this.phoneUtil.isNumberMatchingDesc(shortNumber, generalDesc)) {
            return false;
        }
        PhoneNumberDesc shortNumberDesc = phoneMetadata.getShortCode();
        if (shortNumberDesc.hasNationalNumberPattern()) {
            return this.phoneUtil.isNumberMatchingDesc(shortNumber, shortNumberDesc);
        }
        Logger logger = logger;
        Level level = Level.WARNING;
        String str = "No short code national number pattern found for region: ";
        String valueOf = String.valueOf(regionDialingFrom);
        logger.log(level, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        return false;
    }

    public boolean isValidShortNumber(PhoneNumber number) {
        List<String> regionCodes = this.phoneUtil.getRegionCodesForCountryCode(number.getCountryCode());
        String shortNumber = this.phoneUtil.getNationalSignificantNumber(number);
        String regionCode = getRegionCodeForShortNumberFromRegionList(number, regionCodes);
        if (regionCodes.size() <= 1 || regionCode == null) {
            return isValidShortNumberForRegion(shortNumber, regionCode);
        }
        return true;
    }

    public ShortNumberCost getExpectedCostForRegion(String shortNumber, String regionDialingFrom) {
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionDialingFrom);
        if (phoneMetadata == null) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (this.phoneUtil.isNumberMatchingDesc(shortNumber, phoneMetadata.getPremiumRate())) {
            return ShortNumberCost.PREMIUM_RATE;
        }
        if (this.phoneUtil.isNumberMatchingDesc(shortNumber, phoneMetadata.getStandardRate())) {
            return ShortNumberCost.STANDARD_RATE;
        }
        if (this.phoneUtil.isNumberMatchingDesc(shortNumber, phoneMetadata.getTollFree())) {
            return ShortNumberCost.TOLL_FREE;
        }
        if (isEmergencyNumber(shortNumber, regionDialingFrom)) {
            return ShortNumberCost.TOLL_FREE;
        }
        return ShortNumberCost.UNKNOWN_COST;
    }

    public ShortNumberCost getExpectedCost(PhoneNumber number) {
        List<String> regionCodes = this.phoneUtil.getRegionCodesForCountryCode(number.getCountryCode());
        if (regionCodes.size() == 0) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        String shortNumber = this.phoneUtil.getNationalSignificantNumber(number);
        if (regionCodes.size() == 1) {
            return getExpectedCostForRegion(shortNumber, (String) regionCodes.get(0));
        }
        ShortNumberCost cost = ShortNumberCost.TOLL_FREE;
        for (String regionCode : regionCodes) {
            ShortNumberCost costForRegion = getExpectedCostForRegion(shortNumber, regionCode);
            switch (costForRegion) {
                case PREMIUM_RATE:
                    return ShortNumberCost.PREMIUM_RATE;
                case UNKNOWN_COST:
                    cost = ShortNumberCost.UNKNOWN_COST;
                    break;
                case STANDARD_RATE:
                    if (cost == ShortNumberCost.UNKNOWN_COST) {
                        break;
                    }
                    cost = ShortNumberCost.STANDARD_RATE;
                    break;
                case TOLL_FREE:
                    break;
                default:
                    Logger logger = logger;
                    Level level = Level.SEVERE;
                    String valueOf = String.valueOf(String.valueOf(costForRegion));
                    logger.log(level, new StringBuilder(valueOf.length() + 30).append("Unrecognised cost for region: ").append(valueOf).toString());
                    break;
            }
        }
        return cost;
    }

    private String getRegionCodeForShortNumberFromRegionList(PhoneNumber number, List<String> regionCodes) {
        if (regionCodes.size() == 0) {
            return null;
        }
        if (regionCodes.size() == 1) {
            return (String) regionCodes.get(0);
        }
        String nationalNumber = this.phoneUtil.getNationalSignificantNumber(number);
        for (String regionCode : regionCodes) {
            PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionCode);
            if (phoneMetadata != null && this.phoneUtil.isNumberMatchingDesc(nationalNumber, phoneMetadata.getShortCode())) {
                return regionCode;
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Set<String> getSupportedRegions() {
        return Collections.unmodifiableSet(MetadataManager.getShortNumberMetadataSupportedRegions());
    }

    /* access modifiers changed from: 0000 */
    public String getExampleShortNumber(String regionCode) {
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionCode);
        if (phoneMetadata == null) {
            return "";
        }
        PhoneNumberDesc desc = phoneMetadata.getShortCode();
        if (desc.hasExampleNumber()) {
            return desc.getExampleNumber();
        }
        return "";
    }

    /* access modifiers changed from: 0000 */
    public String getExampleShortNumberForCost(String regionCode, ShortNumberCost cost) {
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionCode);
        if (phoneMetadata == null) {
            return "";
        }
        PhoneNumberDesc desc = null;
        switch (cost) {
            case PREMIUM_RATE:
                desc = phoneMetadata.getPremiumRate();
                break;
            case STANDARD_RATE:
                desc = phoneMetadata.getStandardRate();
                break;
            case TOLL_FREE:
                desc = phoneMetadata.getTollFree();
                break;
        }
        if (desc == null || !desc.hasExampleNumber()) {
            return "";
        }
        return desc.getExampleNumber();
    }

    public boolean connectsToEmergencyNumber(String number, String regionCode) {
        return matchesEmergencyNumberHelper(number, regionCode, true);
    }

    public boolean isEmergencyNumber(String number, String regionCode) {
        return matchesEmergencyNumberHelper(number, regionCode, false);
    }

    private boolean matchesEmergencyNumberHelper(String number, String regionCode, boolean allowPrefixMatch) {
        number = PhoneNumberUtil.extractPossibleNumber(number);
        if (PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(number).lookingAt()) {
            return false;
        }
        PhoneMetadata metadata = MetadataManager.getShortNumberMetadataForRegion(regionCode);
        if (metadata == null || !metadata.hasEmergency()) {
            return false;
        }
        Pattern emergencyNumberPattern = Pattern.compile(metadata.getEmergency().getNationalNumberPattern());
        String normalizedNumber = PhoneNumberUtil.normalizeDigitsOnly(number);
        return (!allowPrefixMatch || REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.contains(regionCode)) ? emergencyNumberPattern.matcher(normalizedNumber).matches() : emergencyNumberPattern.matcher(normalizedNumber).lookingAt();
    }

    public boolean isCarrierSpecific(PhoneNumber number) {
        String regionCode = getRegionCodeForShortNumberFromRegionList(number, this.phoneUtil.getRegionCodesForCountryCode(number.getCountryCode()));
        String nationalNumber = this.phoneUtil.getNationalSignificantNumber(number);
        PhoneMetadata phoneMetadata = MetadataManager.getShortNumberMetadataForRegion(regionCode);
        return phoneMetadata != null && this.phoneUtil.isNumberMatchingDesc(nationalNumber, phoneMetadata.getCarrierSpecific());
    }
}
