package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

public class DefaultSerializers {

    public static class BigDecimalSerializer extends Serializer<BigDecimal> {
        private BigIntegerSerializer bigIntegerSerializer = new BigIntegerSerializer();

        public BigDecimalSerializer() {
            setAcceptsNull(true);
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, BigDecimal bigDecimal) {
            if (bigDecimal == null) {
                output.writeByte((byte) 0);
                return;
            }
            this.bigIntegerSerializer.write(kryo, output, bigDecimal.unscaledValue());
            output.writeInt(bigDecimal.scale(), false);
        }

        public BigDecimal read(Kryo kryo, Input input, Class<BigDecimal> cls) {
            BigInteger read = this.bigIntegerSerializer.read(kryo, input, null);
            if (read == null) {
                return null;
            }
            return new BigDecimal(read, input.readInt(false));
        }
    }

    public static class BigIntegerSerializer extends Serializer<BigInteger> {
        public BigIntegerSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, BigInteger bigInteger) {
            if (bigInteger == null) {
                output.writeByte((byte) 0);
                return;
            }
            byte[] toByteArray = bigInteger.toByteArray();
            output.writeInt(toByteArray.length + 1, true);
            output.writeBytes(toByteArray);
        }

        public BigInteger read(Kryo kryo, Input input, Class<BigInteger> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            return new BigInteger(input.readBytes(readInt - 1));
        }
    }

    public static class BooleanSerializer extends Serializer<Boolean> {
        public BooleanSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Boolean bool) {
            output.writeBoolean(bool.booleanValue());
        }

        public Boolean read(Kryo kryo, Input input, Class<Boolean> cls) {
            return Boolean.valueOf(input.readBoolean());
        }
    }

    public static class ByteSerializer extends Serializer<Byte> {
        public ByteSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Byte b) {
            output.writeByte(b.byteValue());
        }

        public Byte read(Kryo kryo, Input input, Class<Byte> cls) {
            return Byte.valueOf(input.readByte());
        }
    }

    public static class CalendarSerializer extends Serializer<Calendar> {
        private static final long DEFAULT_GREGORIAN_CUTOVER = -12219292800000L;
        TimeZoneSerializer timeZoneSerializer = new TimeZoneSerializer();

        public void write(Kryo kryo, Output output, Calendar calendar) {
            this.timeZoneSerializer.write(kryo, output, calendar.getTimeZone());
            output.writeLong(calendar.getTimeInMillis(), true);
            output.writeBoolean(calendar.isLenient());
            output.writeInt(calendar.getFirstDayOfWeek(), true);
            output.writeInt(calendar.getMinimalDaysInFirstWeek(), true);
            if (calendar instanceof GregorianCalendar) {
                output.writeLong(((GregorianCalendar) calendar).getGregorianChange().getTime(), false);
            } else {
                output.writeLong(DEFAULT_GREGORIAN_CUTOVER, false);
            }
        }

        public Calendar read(Kryo kryo, Input input, Class<Calendar> cls) {
            Calendar instance = Calendar.getInstance(this.timeZoneSerializer.read(kryo, input, TimeZone.class));
            instance.setTimeInMillis(input.readLong(true));
            instance.setLenient(input.readBoolean());
            instance.setFirstDayOfWeek(input.readInt(true));
            instance.setMinimalDaysInFirstWeek(input.readInt(true));
            long readLong = input.readLong(false);
            if (readLong != DEFAULT_GREGORIAN_CUTOVER && (instance instanceof GregorianCalendar)) {
                ((GregorianCalendar) instance).setGregorianChange(new Date(readLong));
            }
            return instance;
        }

        public Calendar copy(Kryo kryo, Calendar calendar) {
            return (Calendar) calendar.clone();
        }
    }

    public static class CharSerializer extends Serializer<Character> {
        public CharSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Character ch) {
            output.writeChar(ch.charValue());
        }

        public Character read(Kryo kryo, Input input, Class<Character> cls) {
            return Character.valueOf(input.readChar());
        }
    }

    public static class ClassSerializer extends Serializer<Class> {
        public ClassSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, Class cls) {
            kryo.writeClass(output, cls);
        }

        public Class read(Kryo kryo, Input input, Class<Class> cls) {
            return kryo.readClass(input).getType();
        }
    }

    public static class CollectionsEmptyListSerializer extends Serializer {
        public CollectionsEmptyListSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object obj) {
        }

        public Object read(Kryo kryo, Input input, Class cls) {
            return Collections.EMPTY_LIST;
        }
    }

    public static class CollectionsEmptyMapSerializer extends Serializer {
        public CollectionsEmptyMapSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object obj) {
        }

        public Object read(Kryo kryo, Input input, Class cls) {
            return Collections.EMPTY_MAP;
        }
    }

    public static class CollectionsEmptySetSerializer extends Serializer {
        public CollectionsEmptySetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Object obj) {
        }

        public Object read(Kryo kryo, Input input, Class cls) {
            return Collections.EMPTY_SET;
        }
    }

    public static class CollectionsSingletonListSerializer extends Serializer<List> {
        public CollectionsSingletonListSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, List list) {
            kryo.writeClassAndObject(output, list.get(0));
        }

        public List read(Kryo kryo, Input input, Class cls) {
            return Collections.singletonList(kryo.readClassAndObject(input));
        }
    }

    public static class CollectionsSingletonMapSerializer extends Serializer<Map> {
        public CollectionsSingletonMapSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Map map) {
            Entry entry = (Entry) map.entrySet().iterator().next();
            kryo.writeClassAndObject(output, entry.getKey());
            kryo.writeClassAndObject(output, entry.getValue());
        }

        public Map read(Kryo kryo, Input input, Class cls) {
            return Collections.singletonMap(kryo.readClassAndObject(input), kryo.readClassAndObject(input));
        }
    }

    public static class CollectionsSingletonSetSerializer extends Serializer<Set> {
        public CollectionsSingletonSetSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Set set) {
            kryo.writeClassAndObject(output, set.iterator().next());
        }

        public Set read(Kryo kryo, Input input, Class cls) {
            return Collections.singleton(kryo.readClassAndObject(input));
        }
    }

    public static class CurrencySerializer extends Serializer<Currency> {
        public CurrencySerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, Currency currency) {
            output.writeString(currency == null ? null : currency.getCurrencyCode());
        }

        public Currency read(Kryo kryo, Input input, Class<Currency> cls) {
            String readString = input.readString();
            if (readString == null) {
                return null;
            }
            return Currency.getInstance(readString);
        }
    }

    public static class DateSerializer extends Serializer<Date> {
        public void write(Kryo kryo, Output output, Date date) {
            output.writeLong(date.getTime(), true);
        }

        public Date read(Kryo kryo, Input input, Class<Date> cls) {
            return new Date(input.readLong(true));
        }

        public Date copy(Kryo kryo, Date date) {
            return new Date(date.getTime());
        }
    }

    public static class DoubleSerializer extends Serializer<Double> {
        public DoubleSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Double d) {
            output.writeDouble(d.doubleValue());
        }

        public Double read(Kryo kryo, Input input, Class<Double> cls) {
            return Double.valueOf(input.readDouble());
        }
    }

    public static class EnumSerializer extends Serializer<Enum> {
        private Object[] enumConstants;

        public EnumSerializer(Class<? extends Enum> cls) {
            setImmutable(true);
            setAcceptsNull(true);
            this.enumConstants = cls.getEnumConstants();
            if (this.enumConstants == null) {
                throw new IllegalArgumentException("The type must be an enum: " + cls);
            }
        }

        public void write(Kryo kryo, Output output, Enum enumR) {
            if (enumR == null) {
                output.writeByte((byte) 0);
            } else {
                output.writeInt(enumR.ordinal() + 1, true);
            }
        }

        public Enum read(Kryo kryo, Input input, Class<Enum> cls) {
            int readInt = input.readInt(true);
            if (readInt == 0) {
                return null;
            }
            readInt--;
            if (readInt >= 0 && readInt <= this.enumConstants.length - 1) {
                return (Enum) this.enumConstants[readInt];
            }
            throw new KryoException("Invalid ordinal for enum \"" + cls.getName() + "\": " + readInt);
        }
    }

    public static class EnumSetSerializer extends Serializer<EnumSet> {
        public void write(Kryo kryo, Output output, EnumSet enumSet) {
            if (enumSet.isEmpty()) {
                throw new KryoException("An empty EnumSet cannot be serialized.");
            }
            Serializer serializer = kryo.writeClass(output, enumSet.iterator().next().getClass()).getSerializer();
            output.writeInt(enumSet.size(), true);
            Iterator it = enumSet.iterator();
            while (it.hasNext()) {
                serializer.write(kryo, output, it.next());
            }
        }

        public EnumSet read(Kryo kryo, Input input, Class<EnumSet> cls) {
            Registration readClass = kryo.readClass(input);
            EnumSet noneOf = EnumSet.noneOf(readClass.getType());
            Serializer serializer = readClass.getSerializer();
            int readInt = input.readInt(true);
            for (int i = 0; i < readInt; i++) {
                noneOf.add(serializer.read(kryo, input, null));
            }
            return noneOf;
        }

        public EnumSet copy(Kryo kryo, EnumSet enumSet) {
            return EnumSet.copyOf(enumSet);
        }
    }

    public static class FloatSerializer extends Serializer<Float> {
        public FloatSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Float f) {
            output.writeFloat(f.floatValue());
        }

        public Float read(Kryo kryo, Input input, Class<Float> cls) {
            return Float.valueOf(input.readFloat());
        }
    }

    public static class IntSerializer extends Serializer<Integer> {
        public IntSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Integer num) {
            output.writeInt(num.intValue(), false);
        }

        public Integer read(Kryo kryo, Input input, Class<Integer> cls) {
            return Integer.valueOf(input.readInt(false));
        }
    }

    public static class KryoSerializableSerializer extends Serializer<KryoSerializable> {
        public void write(Kryo kryo, Output output, KryoSerializable kryoSerializable) {
            kryoSerializable.write(kryo, output);
        }

        public KryoSerializable read(Kryo kryo, Input input, Class<KryoSerializable> cls) {
            KryoSerializable kryoSerializable = (KryoSerializable) kryo.newInstance(cls);
            kryo.reference(kryoSerializable);
            kryoSerializable.read(kryo, input);
            return kryoSerializable;
        }
    }

    public static class LongSerializer extends Serializer<Long> {
        public LongSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Long l) {
            output.writeLong(l.longValue(), false);
        }

        public Long read(Kryo kryo, Input input, Class<Long> cls) {
            return Long.valueOf(input.readLong(false));
        }
    }

    public static class ShortSerializer extends Serializer<Short> {
        public ShortSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, Short sh) {
            output.writeShort(sh.shortValue());
        }

        public Short read(Kryo kryo, Input input, Class<Short> cls) {
            return Short.valueOf(input.readShort());
        }
    }

    public static class StringBufferSerializer extends Serializer<StringBuffer> {
        public StringBufferSerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, StringBuffer stringBuffer) {
            output.writeString((CharSequence) stringBuffer);
        }

        public StringBuffer read(Kryo kryo, Input input, Class<StringBuffer> cls) {
            String readString = input.readString();
            if (readString == null) {
                return null;
            }
            return new StringBuffer(readString);
        }

        public StringBuffer copy(Kryo kryo, StringBuffer stringBuffer) {
            return new StringBuffer(stringBuffer);
        }
    }

    public static class StringBuilderSerializer extends Serializer<StringBuilder> {
        public StringBuilderSerializer() {
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, StringBuilder stringBuilder) {
            output.writeString((CharSequence) stringBuilder);
        }

        public StringBuilder read(Kryo kryo, Input input, Class<StringBuilder> cls) {
            return input.readStringBuilder();
        }

        public StringBuilder copy(Kryo kryo, StringBuilder stringBuilder) {
            return new StringBuilder(stringBuilder);
        }
    }

    public static class StringSerializer extends Serializer<String> {
        public StringSerializer() {
            setImmutable(true);
            setAcceptsNull(true);
        }

        public void write(Kryo kryo, Output output, String str) {
            output.writeString(str);
        }

        public String read(Kryo kryo, Input input, Class<String> cls) {
            return input.readString();
        }
    }

    public static class TimeZoneSerializer extends Serializer<TimeZone> {
        public TimeZoneSerializer() {
            setImmutable(true);
        }

        public void write(Kryo kryo, Output output, TimeZone timeZone) {
            output.writeString(timeZone.getID());
        }

        public TimeZone read(Kryo kryo, Input input, Class<TimeZone> cls) {
            return TimeZone.getTimeZone(input.readString());
        }
    }

    public static class TreeMapSerializer extends MapSerializer {
        public void write(Kryo kryo, Output output, Map map) {
            TreeMap treeMap = (TreeMap) map;
            boolean references = kryo.setReferences(false);
            kryo.writeClassAndObject(output, treeMap.comparator());
            kryo.setReferences(references);
            super.write(kryo, output, map);
        }

        /* access modifiers changed from: protected */
        public Map create(Kryo kryo, Input input, Class<Map> cls) {
            Comparator comparator = (Comparator) kryo.readClassAndObject(input);
            kryo.setReferences(kryo.setReferences(false));
            return new TreeMap(comparator);
        }

        /* access modifiers changed from: protected */
        public Map createCopy(Kryo kryo, Map map) {
            return new TreeMap(((TreeMap) map).comparator());
        }
    }
}
