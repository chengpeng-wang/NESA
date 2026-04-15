package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.InputChunked;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.OutputChunked;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import com.esotericsoftware.kryo.util.ObjectMap;
import org.objectweb.asm.Opcodes;

public class CompatibleFieldSerializer<T> extends FieldSerializer<T> {
    public CompatibleFieldSerializer(Kryo kryo, Class cls) {
        super(kryo, cls);
    }

    public void write(Kryo kryo, Output output, T t) {
        int i = 0;
        CachedField[] fields = getFields();
        ObjectMap graphContext = kryo.getGraphContext();
        if (!graphContext.containsKey(this)) {
            graphContext.put(this, null);
            output.writeInt(fields.length, true);
            for (CachedField cachedField : fields) {
                output.writeString(cachedField.field.getName());
            }
        }
        OutputChunked outputChunked = new OutputChunked(output, Opcodes.ACC_ABSTRACT);
        int length = fields.length;
        while (i < length) {
            fields[i].write(outputChunked, t);
            outputChunked.endChunks();
            i++;
        }
    }

    public T read(Kryo kryo, Input input, Class<T> cls) {
        int i;
        int i2 = 0;
        Object newInstance = kryo.newInstance(cls);
        kryo.reference(newInstance);
        ObjectMap graphContext = kryo.getGraphContext();
        CachedField[] cachedFieldArr = (CachedField[]) graphContext.get(this);
        if (cachedFieldArr == null) {
            int readInt = input.readInt(true);
            String[] strArr = new String[readInt];
            for (int i3 = 0; i3 < readInt; i3++) {
                strArr[i3] = input.readString();
            }
            cachedFieldArr = new CachedField[readInt];
            CachedField[] fields = getFields();
            int length = strArr.length;
            for (i = 0; i < length; i++) {
                Object obj = strArr[i];
                int length2 = fields.length;
                for (readInt = 0; readInt < length2; readInt++) {
                    if (fields[readInt].field.getName().equals(obj)) {
                        cachedFieldArr[i] = fields[readInt];
                        break;
                    }
                }
            }
            graphContext.put(this, cachedFieldArr);
        }
        InputChunked inputChunked = new InputChunked(input, Opcodes.ACC_ABSTRACT);
        i = cachedFieldArr.length;
        while (i2 < i) {
            CachedField cachedField = cachedFieldArr[i2];
            if (cachedField == null) {
                inputChunked.nextChunks();
            } else {
                cachedField.read(inputChunked, newInstance);
                inputChunked.nextChunks();
            }
            i2++;
        }
        return newInstance;
    }
}
