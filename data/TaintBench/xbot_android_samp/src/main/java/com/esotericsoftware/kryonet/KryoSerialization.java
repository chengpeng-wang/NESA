package com.esotericsoftware.kryonet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.ByteBufferOutputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterTCP;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP;
import java.nio.ByteBuffer;
import org.objectweb.asm.Opcodes;

public class KryoSerialization implements Serialization {
    private final ByteBufferInputStream byteBufferInputStream;
    private final ByteBufferOutputStream byteBufferOutputStream;
    private final Input input;
    private final Kryo kryo;
    private final Output output;

    public KryoSerialization() {
        this(new Kryo());
        this.kryo.setReferences(false);
        this.kryo.setRegistrationRequired(true);
    }

    public KryoSerialization(Kryo kryo) {
        this.byteBufferInputStream = new ByteBufferInputStream();
        this.byteBufferOutputStream = new ByteBufferOutputStream();
        this.kryo = kryo;
        kryo.register(RegisterTCP.class);
        kryo.register(RegisterUDP.class);
        kryo.register(KeepAlive.class);
        kryo.register(DiscoverHost.class);
        kryo.register(Ping.class);
        this.input = new Input(this.byteBufferInputStream, Opcodes.ACC_INTERFACE);
        this.output = new Output(this.byteBufferOutputStream, (int) Opcodes.ACC_INTERFACE);
    }

    public Kryo getKryo() {
        return this.kryo;
    }

    public synchronized void write(Connection connection, ByteBuffer byteBuffer, Object obj) {
        this.byteBufferOutputStream.setByteBuffer(byteBuffer);
        this.kryo.getContext().put("connection", connection);
        this.kryo.writeClassAndObject(this.output, obj);
        this.output.flush();
    }

    public synchronized Object read(Connection connection, ByteBuffer byteBuffer) {
        this.byteBufferInputStream.setByteBuffer(byteBuffer);
        this.input.setInputStream(this.byteBufferInputStream);
        this.kryo.getContext().put("connection", connection);
        return this.kryo.readClassAndObject(this.input);
    }

    public void writeLength(ByteBuffer byteBuffer, int i) {
        byteBuffer.putInt(i);
    }

    public int readLength(ByteBuffer byteBuffer) {
        return byteBuffer.getInt();
    }

    public int getLengthLength() {
        return 4;
    }
}
