package com.google.tagmanager;

import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value.Escaping;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value.Type;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

class ValueEscapeUtil {
    private ValueEscapeUtil() {
    }

    static ObjectAndStatic<Value> applyEscapings(ObjectAndStatic<Value> value, List<Escaping> escapings) {
        ObjectAndStatic<Value> escapedValue = value;
        for (Escaping escaping : escapings) {
            escapedValue = applyEscaping(escapedValue, escaping);
        }
        return escapedValue;
    }

    static String urlEncode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8").replaceAll("\\+", "%20");
    }

    private static ObjectAndStatic<Value> applyEscaping(ObjectAndStatic<Value> value, Escaping escaping) {
        if (isValidStringType((Value) value.getObject())) {
            switch (escaping) {
                case ESCAPE_URI:
                    return escapeUri(value);
                default:
                    Log.e("Unsupported Value Escaping: " + escaping);
                    return value;
            }
        }
        Log.e("Escaping can only be applied to strings.");
        return value;
    }

    private static ObjectAndStatic<Value> escapeUri(ObjectAndStatic<Value> value) {
        try {
            return new ObjectAndStatic(Types.objectToValue(urlEncode(((Value) value.getObject()).getString())), value.isStatic());
        } catch (UnsupportedEncodingException e) {
            Log.e("Escape URI: unsupported encoding", e);
            return value;
        }
    }

    private static boolean isValidStringType(Value value) {
        return value.hasType() && value.getType().equals(Type.STRING) && value.hasString();
    }
}
