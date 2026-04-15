package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mvlove.entity.RemoteSmsState;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;

public final class TypeAdapters {
    public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() {
        public BitSet read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            BitSet bitset = new BitSet();
            reader.beginArray();
            int i = 0;
            JsonToken tokenType = reader.peek();
            while (tokenType != JsonToken.END_ARRAY) {
                boolean set;
                switch (AnonymousClass29.$SwitchMap$com$google$gson$stream$JsonToken[tokenType.ordinal()]) {
                    case RemoteSmsState.STATUS_SEND /*1*/:
                        if (reader.nextInt() == 0) {
                            set = false;
                            break;
                        }
                        set = true;
                        break;
                    case RemoteSmsState.STATUS_DILIVERED /*2*/:
                        set = reader.nextBoolean();
                        break;
                    case RemoteSmsState.STATUS_FAILED /*3*/:
                        String stringValue = reader.nextString();
                        try {
                            if (Integer.parseInt(stringValue) == 0) {
                                set = false;
                                break;
                            }
                            set = true;
                            break;
                        } catch (NumberFormatException e) {
                            throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
                        }
                    default:
                        throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
                }
                if (set) {
                    bitset.set(i);
                }
                i++;
                tokenType = reader.peek();
            }
            reader.endArray();
            return bitset;
        }

        public void write(JsonWriter writer, BitSet src) throws IOException {
            if (src == null) {
                writer.nullValue();
                return;
            }
            writer.beginArray();
            for (int i = 0; i < src.length(); i++) {
                writer.value((long) (src.get(i) ? 1 : 0));
            }
            writer.endArray();
        }
    };
    public static final Factory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        public Boolean read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else if (reader.peek() == JsonToken.STRING) {
                return Boolean.valueOf(Boolean.parseBoolean(reader.nextString()));
            } else {
                return Boolean.valueOf(reader.nextBoolean());
            }
        }

        public void write(JsonWriter writer, Boolean value) throws IOException {
            if (value == null) {
                writer.nullValue();
            } else {
                writer.value(value.booleanValue());
            }
        }
    };
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
        public Boolean read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return Boolean.valueOf(reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, Boolean value) throws IOException {
            writer.value(value == null ? "null" : value.toString());
        }
    };
    public static final Factory BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
    public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                return Byte.valueOf((byte) reader.nextInt());
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
    public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>() {
        private static final String DAY_OF_MONTH = "dayOfMonth";
        private static final String HOUR_OF_DAY = "hourOfDay";
        private static final String MINUTE = "minute";
        private static final String MONTH = "month";
        private static final String SECOND = "second";
        private static final String YEAR = "year";

        public Calendar read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            reader.beginObject();
            int year = 0;
            int month = 0;
            int dayOfMonth = 0;
            int hourOfDay = 0;
            int minute = 0;
            int second = 0;
            while (reader.peek() != JsonToken.END_OBJECT) {
                String name = reader.nextName();
                int value = reader.nextInt();
                if (YEAR.equals(name)) {
                    year = value;
                } else if (MONTH.equals(name)) {
                    month = value;
                } else if (DAY_OF_MONTH.equals(name)) {
                    dayOfMonth = value;
                } else if (HOUR_OF_DAY.equals(name)) {
                    hourOfDay = value;
                } else if (MINUTE.equals(name)) {
                    minute = value;
                } else if (SECOND.equals(name)) {
                    second = value;
                }
            }
            reader.endObject();
            return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
        }

        public void write(JsonWriter writer, Calendar value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.beginObject();
            writer.name(YEAR);
            writer.value((long) value.get(1));
            writer.name(MONTH);
            writer.value((long) value.get(2));
            writer.name(DAY_OF_MONTH);
            writer.value((long) value.get(5));
            writer.name(HOUR_OF_DAY);
            writer.value((long) value.get(11));
            writer.name(MINUTE);
            writer.value((long) value.get(12));
            writer.name(SECOND);
            writer.value((long) value.get(13));
            writer.endObject();
        }
    };
    public static final Factory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
        public Character read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return Character.valueOf(reader.nextString().charAt(0));
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, Character value) throws IOException {
            writer.value(value == null ? null : String.valueOf(value));
        }
    };
    public static final Factory CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return Double.valueOf(reader.nextDouble());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory DOUBLE_FACTORY = newFactory(Double.TYPE, Double.class, DOUBLE);
    public static final Factory ENUM_FACTORY = newEnumTypeHierarchyFactory(Enum.class);
    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return Float.valueOf((float) reader.nextDouble());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory FLOAT_FACTORY = newFactory(Float.TYPE, Float.class, FLOAT);
    public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>() {
        public InetAddress read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return InetAddress.getByName(reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, InetAddress value) throws IOException {
            writer.value(value == null ? null : value.getHostAddress());
        }
    };
    public static final Factory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                return Integer.valueOf(reader.nextInt());
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
    public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() {
        public JsonElement read(JsonReader reader) throws IOException {
            switch (AnonymousClass29.$SwitchMap$com$google$gson$stream$JsonToken[reader.peek().ordinal()]) {
                case RemoteSmsState.STATUS_SEND /*1*/:
                    return new JsonPrimitive(new LazilyParsedNumber(reader.nextString()));
                case RemoteSmsState.STATUS_DILIVERED /*2*/:
                    return new JsonPrimitive(Boolean.valueOf(reader.nextBoolean()));
                case RemoteSmsState.STATUS_FAILED /*3*/:
                    return new JsonPrimitive(reader.nextString());
                case 4:
                    reader.nextNull();
                    return JsonNull.INSTANCE;
                case 5:
                    JsonElement array = new JsonArray();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        array.add(read(reader));
                    }
                    reader.endArray();
                    return array;
                case 6:
                    JsonElement object = new JsonObject();
                    reader.beginObject();
                    while (reader.hasNext()) {
                        object.add(reader.nextName(), read(reader));
                    }
                    reader.endObject();
                    return object;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public void write(JsonWriter writer, JsonElement value) throws IOException {
            Iterator i$;
            if (value == null || value.isJsonNull()) {
                writer.nullValue();
            } else if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    writer.value(primitive.getAsNumber());
                } else if (primitive.isBoolean()) {
                    writer.value(primitive.getAsBoolean());
                } else {
                    writer.value(primitive.getAsString());
                }
            } else if (value.isJsonArray()) {
                writer.beginArray();
                i$ = value.getAsJsonArray().iterator();
                while (i$.hasNext()) {
                    write(writer, (JsonElement) i$.next());
                }
                writer.endArray();
            } else if (value.isJsonObject()) {
                writer.beginObject();
                for (Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
                    writer.name((String) e.getKey());
                    write(writer, (JsonElement) e.getValue());
                }
                writer.endObject();
            } else {
                throw new IllegalArgumentException("Couldn't write " + value.getClass());
            }
        }
    };
    public static final Factory JSON_ELEMENT_FACTORY = newFactory(JsonElement.class, JSON_ELEMENT);
    public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>() {
        public Locale read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            StringTokenizer tokenizer = new StringTokenizer(reader.nextString(), "_");
            String language = null;
            String country = null;
            String variant = null;
            if (tokenizer.hasMoreElements()) {
                language = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                country = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                variant = tokenizer.nextToken();
            }
            if (country == null && variant == null) {
                return new Locale(language);
            }
            if (variant == null) {
                return new Locale(language, country);
            }
            return new Locale(language, country, variant);
        }

        public void write(JsonWriter writer, Locale value) throws IOException {
            writer.value(value == null ? null : value.toString());
        }
    };
    public static final Factory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                return Long.valueOf(reader.nextLong());
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory LONG_FACTORY = newFactory(Long.TYPE, Long.class, LONG);
    public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            JsonToken jsonToken = reader.peek();
            switch (AnonymousClass29.$SwitchMap$com$google$gson$stream$JsonToken[jsonToken.ordinal()]) {
                case RemoteSmsState.STATUS_SEND /*1*/:
                    return new LazilyParsedNumber(reader.nextString());
                case 4:
                    reader.nextNull();
                    return null;
                default:
                    throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
            }
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory NUMBER_FACTORY = newFactory(Number.class, NUMBER);
    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
        public Number read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                return Short.valueOf((short) reader.nextInt());
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        public void write(JsonWriter writer, Number value) throws IOException {
            writer.value(value);
        }
    };
    public static final Factory SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) throws IOException {
            JsonToken peek = reader.peek();
            if (peek == JsonToken.NULL) {
                reader.nextNull();
                return null;
            } else if (peek == JsonToken.BOOLEAN) {
                return Boolean.toString(reader.nextBoolean());
            } else {
                return reader.nextString();
            }
        }

        public void write(JsonWriter writer, String value) throws IOException {
            writer.value(value);
        }
    };
    public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>() {
        public StringBuffer read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return new StringBuffer(reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, StringBuffer value) throws IOException {
            writer.value(value == null ? null : value.toString());
        }
    };
    public static final Factory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
    public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>() {
        public StringBuilder read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return new StringBuilder(reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, StringBuilder value) throws IOException {
            writer.value(value == null ? null : value.toString());
        }
    };
    public static final Factory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
    public static final Factory STRING_FACTORY = newFactory(String.class, STRING);
    public static final Factory TIMESTAMP_FACTORY = new Factory() {
        public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
            if (typeToken.getRawType() != Timestamp.class) {
                return null;
            }
            final TypeAdapter<Date> dateTypeAdapter = context.getAdapter(Date.class);
            return new TypeAdapter<Timestamp>() {
                public Timestamp read(JsonReader reader) throws IOException {
                    Date date = (Date) dateTypeAdapter.read(reader);
                    return date != null ? new Timestamp(date.getTime()) : null;
                }

                public void write(JsonWriter writer, Timestamp value) throws IOException {
                    dateTypeAdapter.write(writer, (Object) value);
                }
            };
        }
    };
    public static final TypeAdapter<URI> URI = new TypeAdapter<URI>() {
        public URI read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            try {
                String nextString = reader.nextString();
                if ("null".equals(nextString)) {
                    return null;
                }
                return new URI(nextString);
            } catch (URISyntaxException e) {
                throw new JsonIOException(e);
            }
        }

        public void write(JsonWriter writer, URI value) throws IOException {
            writer.value(value == null ? null : value.toASCIIString());
        }
    };
    public static final Factory URI_FACTORY = newFactory(URI.class, URI);
    public static final TypeAdapter<URL> URL = new TypeAdapter<URL>() {
        public URL read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String nextString = reader.nextString();
            if ("null".equals(nextString)) {
                return null;
            }
            return new URL(nextString);
        }

        public void write(JsonWriter writer, URL value) throws IOException {
            writer.value(value == null ? null : value.toExternalForm());
        }
    };
    public static final Factory URL_FACTORY = newFactory(URL.class, URL);
    public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>() {
        public UUID read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return UUID.fromString(reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, UUID value) throws IOException {
            writer.value(value == null ? null : value.toString());
        }
    };
    public static final Factory UUID_FACTORY = newFactory(UUID.class, UUID);

    /* renamed from: com.google.gson.internal.bind.TypeAdapters$29 */
    static /* synthetic */ class AnonymousClass29 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];

        static {
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_ARRAY.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Class<T> classOfT;

        public EnumTypeAdapter(Class<T> classOfT) {
            this.classOfT = classOfT;
        }

        public T read(JsonReader reader) throws IOException {
            if (reader.peek() != JsonToken.NULL) {
                return Enum.valueOf(this.classOfT, reader.nextString());
            }
            reader.nextNull();
            return null;
        }

        public void write(JsonWriter writer, T value) throws IOException {
            writer.value(value == null ? null : value.name());
        }
    }

    private TypeAdapters() {
    }

    public static <TT> Factory newEnumTypeHierarchyFactory(final Class<TT> clazz) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return clazz.isAssignableFrom(rawType) ? new EnumTypeAdapter(rawType) : null;
            }
        };
    }

    public static <TT> Factory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                return typeToken.equals(type) ? typeAdapter : null;
            }
        };
    }

    public static <TT> Factory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                return typeToken.getRawType() == type ? typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> Factory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == unboxed || rawType == boxed) ? typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> Factory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == base || rawType == sub) ? typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> Factory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new Factory() {
            public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> typeToken) {
                return clazz.isAssignableFrom(typeToken.getRawType()) ? typeAdapter : null;
            }

            public String toString() {
                return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }
}
