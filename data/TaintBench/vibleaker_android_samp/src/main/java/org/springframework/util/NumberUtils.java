package org.springframework.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public abstract class NumberUtils {
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
        Assert.notNull(number, "Number must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        if (targetClass.isInstance(number)) {
            return number;
        }
        long value;
        if (targetClass.equals(Byte.class)) {
            value = number.longValue();
            if (value < -128 || value > 127) {
                raiseOverflowException(number, targetClass);
            }
            return new Byte(number.byteValue());
        } else if (targetClass.equals(Short.class)) {
            value = number.longValue();
            if (value < -32768 || value > 32767) {
                raiseOverflowException(number, targetClass);
            }
            return new Short(number.shortValue());
        } else if (targetClass.equals(Integer.class)) {
            value = number.longValue();
            if (value < -2147483648L || value > 2147483647L) {
                raiseOverflowException(number, targetClass);
            }
            return new Integer(number.intValue());
        } else if (targetClass.equals(Long.class)) {
            return new Long(number.longValue());
        } else {
            if (targetClass.equals(BigInteger.class)) {
                if (number instanceof BigDecimal) {
                    return ((BigDecimal) number).toBigInteger();
                }
                return BigInteger.valueOf(number.longValue());
            } else if (targetClass.equals(Float.class)) {
                return new Float(number.floatValue());
            } else {
                if (targetClass.equals(Double.class)) {
                    return new Double(number.doubleValue());
                }
                if (targetClass.equals(BigDecimal.class)) {
                    return new BigDecimal(number.toString());
                }
                throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to unknown target class [" + targetClass.getName() + "]");
            }
        }
    }

    private static void raiseOverflowException(Number number, Class targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        String trimmed = StringUtils.trimAllWhitespace(text);
        if (targetClass.equals(Byte.class)) {
            if (isHexNumber(trimmed)) {
                return Byte.decode(trimmed);
            }
            return Byte.valueOf(trimmed);
        } else if (targetClass.equals(Short.class)) {
            return isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed);
        } else {
            if (targetClass.equals(Integer.class)) {
                return isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed);
            } else {
                if (targetClass.equals(Long.class)) {
                    return isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed);
                } else {
                    if (targetClass.equals(BigInteger.class)) {
                        return isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed);
                    } else {
                        if (targetClass.equals(Float.class)) {
                            return Float.valueOf(trimmed);
                        }
                        if (targetClass.equals(Double.class)) {
                            return Double.valueOf(trimmed);
                        }
                        if (targetClass.equals(BigDecimal.class) || targetClass.equals(Number.class)) {
                            return new BigDecimal(trimmed);
                        }
                        throw new IllegalArgumentException("Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
                    }
                }
            }
        }
    }

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass, NumberFormat numberFormat) {
        if (numberFormat == null) {
            return parseNumber(text, targetClass);
        }
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        DecimalFormat decimalFormat = null;
        boolean resetBigDecimal = false;
        if (numberFormat instanceof DecimalFormat) {
            decimalFormat = (DecimalFormat) numberFormat;
            if (BigDecimal.class.equals(targetClass) && !decimalFormat.isParseBigDecimal()) {
                decimalFormat.setParseBigDecimal(true);
                resetBigDecimal = true;
            }
        }
        try {
            T convertNumberToTargetClass = convertNumberToTargetClass(numberFormat.parse(StringUtils.trimAllWhitespace(text)), targetClass);
            if (!resetBigDecimal) {
                return convertNumberToTargetClass;
            }
            decimalFormat.setParseBigDecimal(false);
            return convertNumberToTargetClass;
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
        } catch (Throwable th) {
            if (resetBigDecimal) {
                decimalFormat.setParseBigDecimal(false);
            }
        }
    }

    private static boolean isHexNumber(String value) {
        int index;
        if (value.startsWith("-")) {
            index = 1;
        } else {
            index = 0;
        }
        if (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index)) {
            return true;
        }
        return false;
    }

    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        if (value.startsWith("-")) {
            negative = true;
            index = 0 + 1;
        }
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (value.startsWith("0", index) && value.length() > index + 1) {
            index++;
            radix = 8;
        }
        BigInteger result = new BigInteger(value.substring(index), radix);
        return negative ? result.negate() : result;
    }
}
