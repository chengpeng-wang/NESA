package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class DeflateSerializer extends Serializer {
    private int compressionLevel = 4;
    private boolean noHeaders = true;
    private final Serializer serializer;

    public DeflateSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void write(Kryo kryo, Output output, Object obj) {
        OutputStream deflaterOutputStream = new DeflaterOutputStream(output, new Deflater(this.compressionLevel, this.noHeaders));
        Output output2 = new Output(deflaterOutputStream, 256);
        kryo.writeObject(output2, obj, this.serializer);
        output2.flush();
        try {
            deflaterOutputStream.finish();
        } catch (IOException e) {
            throw new KryoException(e);
        }
    }

    public Object read(Kryo kryo, Input input, Class cls) {
        return kryo.readObject(new Input(new InflaterInputStream(input, new Inflater(this.noHeaders)), 256), cls, this.serializer);
    }

    public void setNoHeaders(boolean z) {
        this.noHeaders = z;
    }

    public void setCompressionLevel(int i) {
        this.compressionLevel = i;
    }

    public Object copy(Kryo kryo, Object obj) {
        return this.serializer.copy(kryo, obj);
    }
}
