package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class BlowfishSerializer extends Serializer {
    private static SecretKeySpec keySpec;
    private final Serializer serializer;

    public BlowfishSerializer(Serializer serializer, byte[] bArr) {
        this.serializer = serializer;
        keySpec = new SecretKeySpec(bArr, "Blowfish");
    }

    public void write(Kryo kryo, Output output, Object obj) {
        CipherOutputStream cipherOutputStream = new CipherOutputStream(output, getCipher(1));
        AnonymousClass1 anonymousClass1 = new Output(cipherOutputStream, 256) {
            public void close() throws KryoException {
            }
        };
        kryo.writeObject(anonymousClass1, obj, this.serializer);
        anonymousClass1.flush();
        try {
            cipherOutputStream.close();
        } catch (IOException e) {
            throw new KryoException(e);
        }
    }

    public Object read(Kryo kryo, Input input, Class cls) {
        return kryo.readObject(new Input(new CipherInputStream(input, getCipher(2)), 256), cls, this.serializer);
    }

    public Object copy(Kryo kryo, Object obj) {
        return this.serializer.copy(kryo, obj);
    }

    private static Cipher getCipher(int i) {
        try {
            Cipher instance = Cipher.getInstance("Blowfish");
            instance.init(i, keySpec);
            return instance;
        } catch (Exception e) {
            throw new KryoException(e);
        }
    }
}
