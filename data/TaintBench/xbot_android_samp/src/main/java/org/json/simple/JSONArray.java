package org.json.simple;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JSONArray extends ArrayList implements List, JSONAware, JSONStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    public static String toJSONString(List list) {
        if (list == null) {
            return "null";
        }
        Object obj = 1;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('[');
        for (Object next : list) {
            if (obj != null) {
                obj = null;
            } else {
                stringBuffer.append(',');
            }
            if (next == null) {
                stringBuffer.append("null");
            } else {
                stringBuffer.append(JSONValue.toJSONString(next));
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    public static void writeJSONString(List list, Writer writer) throws IOException {
        if (list == null) {
            writer.write("null");
            return;
        }
        Object obj = 1;
        writer.write(91);
        for (Object next : list) {
            if (obj != null) {
                obj = null;
            } else {
                writer.write(44);
            }
            if (next == null) {
                writer.write("null");
            } else {
                JSONValue.writeJSONString(next, writer);
            }
        }
        writer.write(93);
    }

    public String toJSONString() {
        return toJSONString(this);
    }

    public String toString() {
        return toJSONString();
    }

    public void writeJSONString(Writer writer) throws IOException {
        writeJSONString(this, writer);
    }
}
