package com.esotericsoftware.kryonet;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonException;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.ByteBufferOutputStream;
import com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterTCP;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

public class JsonSerialization implements Serialization {
    private final ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream();
    private final ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream();
    private final Json json = new Json();
    private byte[] logBuffer = new byte[0];
    private boolean logging = true;
    private boolean prettyPrint = true;
    private final OutputStreamWriter writer = new OutputStreamWriter(this.byteBufferOutputStream);

    public JsonSerialization() {
        this.json.addClassTag("RegisterTCP", RegisterTCP.class);
        this.json.addClassTag("RegisterUDP", RegisterUDP.class);
        this.json.addClassTag("KeepAlive", KeepAlive.class);
        this.json.addClassTag("DiscoverHost", DiscoverHost.class);
        this.json.addClassTag("Ping", Ping.class);
        this.json.setWriter(this.writer);
    }

    public void setLogging(boolean z, boolean z2) {
        this.logging = z;
        this.prettyPrint = z2;
    }

    public void write(Connection connection, ByteBuffer byteBuffer, Object obj) {
        this.byteBufferOutputStream.setByteBuffer(byteBuffer);
        byteBuffer.position();
        try {
            this.json.writeValue(obj, Object.class, null);
            this.writer.flush();
        } catch (Exception e) {
            throw new JsonException("Error writing object: " + obj, e);
        }
    }

    public Object read(Connection connection, ByteBuffer byteBuffer) {
        this.byteBufferInputStream.setByteBuffer(byteBuffer);
        return this.json.fromJson(Object.class, this.byteBufferInputStream);
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
