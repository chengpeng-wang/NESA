package com.splunk.mint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Pattern;

class StacktraceHash {
    private static String guidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    private static String hexRegex = "0[xX][0-9a-fA-F]+";

    StacktraceHash() {
    }

    public static final HashMap<String, String> manipulateStacktrace(String packageName, String stacktrace) {
        if (packageName == null || stacktrace == null) {
            return null;
        }
        String klass;
        HashMap<String, String> map = new HashMap();
        String library = null;
        String[] stackArray = stacktrace.split("\n\t");
        if (stackArray.length == 1) {
            stackArray = stacktrace.split("\n");
        }
        String message = removeFirstDate(stackArray[0]);
        try {
            klass = message.substring(0, message.indexOf(":"));
        } catch (Exception e) {
            klass = message;
        }
        map.put("message", message.replaceAll("\n", " ").replaceAll("Caused by:", ""));
        map.put("klass", klass);
        if (packageName.contains(".")) {
            String[] packageParts = packageName.split("\\.");
            if (packageParts[0].length() > 3) {
                packageName = packageParts[0];
            } else {
                packageName = packageParts[1];
            }
        }
        StringBuilder stringForHash = new StringBuilder();
        for (String line : stackArray) {
            if (line.indexOf(packageName) != -1 && line.indexOf(packageName) <= 20) {
                stringForHash.append(line);
                stringForHash.append("\n");
            }
        }
        if (stringForHash.length() == 0) {
            for (String line2 : stackArray) {
                if (!(line2.length() <= 10 || !line2.trim().startsWith("at ") || line2.contains("...") || line2.contains(".java.") || line2.substring(0, 10).contains("android") || line2.contains("org.apache") || line2.contains("org.acra") || line2.contains("dalvik") || line2.contains(".acra."))) {
                    stringForHash.append(line2);
                    stringForHash.append("\n");
                }
            }
        }
        if (stringForHash.length() == 0) {
            for (String line22 : stackArray) {
                if (line22.length() > 10 && line22.trim().startsWith("at ") && (line22.contains(".java") || line22.contains("Unknown"))) {
                    stringForHash.append(line22);
                    stringForHash.append("\n");
                }
            }
        }
        String firstline = stringForHash.toString().split("\n")[0];
        if (!firstline.contains(packageName)) {
            library = firstline.split("\\.")[1];
        }
        map.put("library", library);
        String where = firstline;
        if (where.contains("(")) {
            where = where.substring(where.indexOf("(") + 1, where.indexOf(")"));
        } else {
            where = "Unknown";
        }
        map.put("where", where);
        String stringToHash = stringForHash.toString().replaceAll("@\\w+", "").replaceAll(hexRegex, "").replaceAll(guidRegex, "").replaceAll("$\\w+", "");
        if (!firstline.contains(".java:")) {
            stringToHash = stringToHash.replace(firstline, firstline.replaceAll("\\d+", ""));
        }
        map.put("errorHash", md5(stringToHash));
        return map;
    }

    public static String getMD5ForJavascriptError(String stacktrace) {
        return md5(stacktrace.replaceAll("@\\w+", "").replaceAll(hexRegex, "").replaceAll(guidRegex, "").replaceAll("$\\w+", ""));
    }

    private static String md5(String stringToHash) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(stringToHash.getBytes());
            String hashtext = new BigInteger(1, m.digest()).toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String removeFirstDate(String message) {
        if (!message.contains("\n")) {
            return message;
        }
        String[] parts = message.split("\\n");
        if (Pattern.compile("[\\d]{2}:[\\d]{2}:[\\d]{2}").matcher(message).find()) {
            return parts[1];
        }
        return message;
    }
}
