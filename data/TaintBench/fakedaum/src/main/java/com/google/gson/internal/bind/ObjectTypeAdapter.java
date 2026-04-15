package com.google.gson.internal.bind;

import com.google.gson.internal.bind.TypeAdapter.Factory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mvlove.entity.RemoteSmsState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final Factory FACTORY = new Factory() {
        public <T> TypeAdapter<T> create(MiniGson context, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return new ObjectTypeAdapter(context, null);
            }
            return null;
        }
    };
    private final MiniGson miniGson;

    /* renamed from: com.google.gson.internal.bind.ObjectTypeAdapter$2 */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];

        static {
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* synthetic */ ObjectTypeAdapter(MiniGson x0, AnonymousClass1 x1) {
        this(x0);
    }

    private ObjectTypeAdapter(MiniGson miniGson) {
        this.miniGson = miniGson;
    }

    public Object read(JsonReader reader) throws IOException {
        switch (AnonymousClass2.$SwitchMap$com$google$gson$stream$JsonToken[reader.peek().ordinal()]) {
            case RemoteSmsState.STATUS_SEND /*1*/:
                List<Object> list = new ArrayList();
                reader.beginArray();
                while (reader.hasNext()) {
                    list.add(read(reader));
                }
                reader.endArray();
                return list;
            case RemoteSmsState.STATUS_DILIVERED /*2*/:
                Map<String, Object> map = new LinkedHashMap();
                reader.beginObject();
                while (reader.hasNext()) {
                    map.put(reader.nextName(), read(reader));
                }
                reader.endObject();
                return map;
            case RemoteSmsState.STATUS_FAILED /*3*/:
                return reader.nextString();
            case 4:
                return Double.valueOf(reader.nextDouble());
            case 5:
                return Boolean.valueOf(reader.nextBoolean());
            case 6:
                reader.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public void write(JsonWriter writer, Object value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        TypeAdapter<Object> typeAdapter = this.miniGson.getAdapter(value.getClass());
        if (typeAdapter instanceof ObjectTypeAdapter) {
            writer.beginObject();
            writer.endObject();
            return;
        }
        typeAdapter.write(writer, value);
    }
}
