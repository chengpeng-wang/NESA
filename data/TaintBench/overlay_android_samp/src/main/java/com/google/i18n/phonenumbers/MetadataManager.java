package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadataCollection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class MetadataManager {
    private static final String ALTERNATE_FORMATS_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberAlternateFormatsProto";
    private static final Logger LOGGER = Logger.getLogger(MetadataManager.class.getName());
    private static final String SHORT_NUMBER_METADATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/ShortNumberMetadataProto";
    private static final Map<Integer, PhoneMetadata> callingCodeToAlternateFormatsMap = Collections.synchronizedMap(new HashMap());
    private static final Set<Integer> countryCodeSet = AlternateFormatsCountryCodeSet.getCountryCodeSet();
    private static final Set<String> regionCodeSet = ShortNumbersRegionCodeSet.getRegionCodeSet();
    private static final Map<String, PhoneMetadata> regionCodeToShortNumberMetadataMap = Collections.synchronizedMap(new HashMap());

    private MetadataManager() {
    }

    private static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.toString());
            }
        }
    }

    private static void loadAlternateFormatsMetadataFromFile(int countryCallingCode) {
        IOException e;
        Throwable th;
        String valueOf = String.valueOf(String.valueOf("/com/google/i18n/phonenumbers/data/PhoneNumberAlternateFormatsProto_"));
        ObjectInputStream in = null;
        try {
            ObjectInputStream in2 = new ObjectInputStream(PhoneNumberMatcher.class.getResourceAsStream(new StringBuilder(valueOf.length() + 11).append(valueOf).append(countryCallingCode).toString()));
            try {
                PhoneMetadataCollection alternateFormats = new PhoneMetadataCollection();
                alternateFormats.readExternal(in2);
                for (PhoneMetadata metadata : alternateFormats.getMetadataList()) {
                    callingCodeToAlternateFormatsMap.put(Integer.valueOf(metadata.getCountryCode()), metadata);
                }
                close(in2);
                in = in2;
            } catch (IOException e2) {
                e = e2;
                in = in2;
                try {
                    LOGGER.log(Level.WARNING, e.toString());
                    close(in);
                } catch (Throwable th2) {
                    th = th2;
                    close(in);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                close(in);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            LOGGER.log(Level.WARNING, e.toString());
            close(in);
        }
    }

    static PhoneMetadata getAlternateFormatsForCountry(int countryCallingCode) {
        if (!countryCodeSet.contains(Integer.valueOf(countryCallingCode))) {
            return null;
        }
        synchronized (callingCodeToAlternateFormatsMap) {
            if (!callingCodeToAlternateFormatsMap.containsKey(Integer.valueOf(countryCallingCode))) {
                loadAlternateFormatsMetadataFromFile(countryCallingCode);
            }
        }
        return (PhoneMetadata) callingCodeToAlternateFormatsMap.get(Integer.valueOf(countryCallingCode));
    }

    private static void loadShortNumberMetadataFromFile(String regionCode) {
        IOException e;
        Throwable th;
        Class cls = PhoneNumberMatcher.class;
        String valueOf = String.valueOf("/com/google/i18n/phonenumbers/data/ShortNumberMetadataProto_");
        String valueOf2 = String.valueOf(regionCode);
        if (valueOf2.length() != 0) {
            valueOf2 = valueOf.concat(valueOf2);
        } else {
            valueOf2 = new String(valueOf);
        }
        ObjectInputStream in = null;
        try {
            ObjectInputStream in2 = new ObjectInputStream(cls.getResourceAsStream(valueOf2));
            try {
                PhoneMetadataCollection shortNumberMetadata = new PhoneMetadataCollection();
                shortNumberMetadata.readExternal(in2);
                for (PhoneMetadata metadata : shortNumberMetadata.getMetadataList()) {
                    regionCodeToShortNumberMetadataMap.put(regionCode, metadata);
                }
                close(in2);
                in = in2;
            } catch (IOException e2) {
                e = e2;
                in = in2;
                try {
                    LOGGER.log(Level.WARNING, e.toString());
                    close(in);
                } catch (Throwable th2) {
                    th = th2;
                    close(in);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                close(in);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            LOGGER.log(Level.WARNING, e.toString());
            close(in);
        }
    }

    static Set<String> getShortNumberMetadataSupportedRegions() {
        return regionCodeSet;
    }

    static PhoneMetadata getShortNumberMetadataForRegion(String regionCode) {
        if (!regionCodeSet.contains(regionCode)) {
            return null;
        }
        synchronized (regionCodeToShortNumberMetadataMap) {
            if (!regionCodeToShortNumberMetadataMap.containsKey(regionCode)) {
                loadShortNumberMetadataFromFile(regionCode);
            }
        }
        return (PhoneMetadata) regionCodeToShortNumberMetadataMap.get(regionCode);
    }
}
