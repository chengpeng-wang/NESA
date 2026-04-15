package com.esotericsoftware.kryonet;

import java.nio.ByteBuffer;

public interface Serialization {
    int getLengthLength();

    Object read(Connection connection, ByteBuffer byteBuffer);

    int readLength(ByteBuffer byteBuffer);

    void write(Connection connection, ByteBuffer byteBuffer, Object obj);

    void writeLength(ByteBuffer byteBuffer, int i);
}
