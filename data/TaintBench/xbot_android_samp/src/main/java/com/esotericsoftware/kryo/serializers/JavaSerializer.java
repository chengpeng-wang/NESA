package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializer extends Serializer {
    private Output lastOutput;
    private ObjectOutputStream objectStream;

    public void write(Kryo kryo, Output output, Object obj) {
        try {
            if (output != this.lastOutput) {
                this.objectStream = new ObjectOutputStream(output);
                this.lastOutput = output;
            } else {
                this.objectStream.reset();
            }
            this.objectStream.writeObject(obj);
            this.objectStream.flush();
        } catch (Exception e) {
            throw new KryoException("Error during Java serialization.", e);
        }
    }

    public Object read(Kryo kryo, Input input, Class cls) {
        try {
            return new ObjectInputStream(input).readObject();
        } catch (Exception e) {
            throw new KryoException("Error during Java deserialization.", e);
        }
    }
}
