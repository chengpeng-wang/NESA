package org.springframework.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public abstract class ObjectUtils {
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
    private static final String ARRAY_END = "}";
    private static final String ARRAY_START = "{";
    private static final String EMPTY_ARRAY = "{}";
    private static final String EMPTY_STRING = "";
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;
    private static final String NULL_STRING = "null";

    public static boolean isCheckedException(Throwable ex) {
        return ((ex instanceof RuntimeException) || (ex instanceof Error)) ? false : true;
    }

    public static boolean isCompatibleWithThrowsClause(Throwable ex, Class<?>... declaredExceptions) {
        if (!isCheckedException(ex)) {
            return true;
        }
        if (declaredExceptions != null) {
            for (Class<?> declaredException : declaredExceptions) {
                if (declaredException.isInstance(ex)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean containsElement(Object[] array, Object element) {
        if (array == null) {
            return false;
        }
        for (Object arrayEle : array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsConstant(Enum<?>[] enumValues, String constant) {
        return containsConstant(enumValues, constant, false);
    }

    public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
        Enum<?>[] arr$ = enumValues;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            Enum<?> candidate = arr$[i$];
            if (caseSensitive) {
                if (!candidate.toString().equals(constant)) {
                    i$++;
                }
            } else if (!candidate.toString().equalsIgnoreCase(constant)) {
                i$++;
            }
            return true;
        }
        return false;
    }

    public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] enumValues, String constant) {
        for (E candidate : enumValues) {
            if (candidate.toString().equalsIgnoreCase(constant)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(String.format("constant [%s] does not exist in enum type %s", new Object[]{constant, enumValues.getClass().getComponentType().getName()}));
    }

    public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (obj != null) {
            compType = obj.getClass();
        }
        Object[] newArr = (Object[]) ((Object[]) Array.newInstance(compType, array != null ? array.length + 1 : 1));
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }

    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        if (source == null) {
            return new Object[0];
        }
        if (source.getClass().isArray()) {
            int length = Array.getLength(source);
            if (length == 0) {
                return new Object[0];
            }
            Object[] newArray = (Object[]) Array.newInstance(Array.get(source, 0).getClass(), length);
            for (int i = 0; i < length; i++) {
                newArray[i] = Array.get(source, i);
            }
            return newArray;
        }
        throw new IllegalArgumentException("Source is not an array: " + source);
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            if ((o1 instanceof Object[]) && (o2 instanceof Object[])) {
                return Arrays.equals((Object[]) o1, (Object[]) o2);
            }
            if ((o1 instanceof boolean[]) && (o2 instanceof boolean[])) {
                return Arrays.equals((boolean[]) o1, (boolean[]) o2);
            }
            if ((o1 instanceof byte[]) && (o2 instanceof byte[])) {
                return Arrays.equals((byte[]) o1, (byte[]) o2);
            }
            if ((o1 instanceof char[]) && (o2 instanceof char[])) {
                return Arrays.equals((char[]) o1, (char[]) o2);
            }
            if ((o1 instanceof double[]) && (o2 instanceof double[])) {
                return Arrays.equals((double[]) o1, (double[]) o2);
            }
            if ((o1 instanceof float[]) && (o2 instanceof float[])) {
                return Arrays.equals((float[]) o1, (float[]) o2);
            }
            if ((o1 instanceof int[]) && (o2 instanceof int[])) {
                return Arrays.equals((int[]) o1, (int[]) o2);
            }
            if ((o1 instanceof long[]) && (o2 instanceof long[])) {
                return Arrays.equals((long[]) o1, (long[]) o2);
            }
            if ((o1 instanceof short[]) && (o2 instanceof short[])) {
                return Arrays.equals((short[]) o1, (short[]) o2);
            }
        }
        return false;
    }

    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return nullSafeHashCode((Object[]) obj);
            }
            if (obj instanceof boolean[]) {
                return nullSafeHashCode((boolean[]) obj);
            }
            if (obj instanceof byte[]) {
                return nullSafeHashCode((byte[]) obj);
            }
            if (obj instanceof char[]) {
                return nullSafeHashCode((char[]) obj);
            }
            if (obj instanceof double[]) {
                return nullSafeHashCode((double[]) obj);
            }
            if (obj instanceof float[]) {
                return nullSafeHashCode((float[]) obj);
            }
            if (obj instanceof int[]) {
                return nullSafeHashCode((int[]) obj);
            }
            if (obj instanceof long[]) {
                return nullSafeHashCode((long[]) obj);
            }
            if (obj instanceof short[]) {
                return nullSafeHashCode((short[]) obj);
            }
        }
        return obj.hashCode();
    }

    public static int nullSafeHashCode(Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (Object element : array) {
            hash = (hash * 31) + nullSafeHashCode(element);
        }
        return hash;
    }

    public static int nullSafeHashCode(boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (boolean element : array) {
            hash = (hash * 31) + hashCode(element);
        }
        return hash;
    }

    public static int nullSafeHashCode(byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (byte element : array) {
            hash = (hash * 31) + element;
        }
        return hash;
    }

    public static int nullSafeHashCode(char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (char element : array) {
            hash = (hash * 31) + element;
        }
        return hash;
    }

    public static int nullSafeHashCode(double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (double element : array) {
            hash = (hash * 31) + hashCode(element);
        }
        return hash;
    }

    public static int nullSafeHashCode(float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (float element : array) {
            hash = (hash * 31) + hashCode(element);
        }
        return hash;
    }

    public static int nullSafeHashCode(int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (int element : array) {
            hash = (hash * 31) + element;
        }
        return hash;
    }

    public static int nullSafeHashCode(long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (long element : array) {
            hash = (hash * 31) + hashCode(element);
        }
        return hash;
    }

    public static int nullSafeHashCode(short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (short element : array) {
            hash = (hash * 31) + element;
        }
        return hash;
    }

    public static int hashCode(boolean bool) {
        return bool ? 1231 : 1237;
    }

    public static int hashCode(double dbl) {
        return hashCode(Double.doubleToLongBits(dbl));
    }

    public static int hashCode(float flt) {
        return Float.floatToIntBits(flt);
    }

    public static int hashCode(long lng) {
        return (int) ((lng >>> 32) ^ lng);
    }

    public static String identityToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static String getDisplayString(Object obj) {
        if (obj == null) {
            return "";
        }
        return nullSafeToString(obj);
    }

    public static String nullSafeClassName(Object obj) {
        return obj != null ? obj.getClass().getName() : NULL_STRING;
    }

    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        if (str == null) {
            str = "";
        }
        return str;
    }

    public static String nullSafeToString(Object[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(String.valueOf(array[i]));
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(boolean[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(byte[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(char[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append("'").append(array[i]).append("'");
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(double[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(float[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(int[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(long[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }

    public static String nullSafeToString(short[] array) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(ARRAY_START);
            } else {
                sb.append(ARRAY_ELEMENT_SEPARATOR);
            }
            sb.append(array[i]);
        }
        sb.append(ARRAY_END);
        return sb.toString();
    }
}
