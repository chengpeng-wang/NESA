package com.esotericsoftware.kryonet;

import com.esotericsoftware.kryo.Kryo;
import java.io.IOException;

public interface EndPoint extends Runnable {
    void addListener(Listener listener);

    void close();

    Kryo getKryo();

    Serialization getSerialization();

    Thread getUpdateThread();

    void removeListener(Listener listener);

    void run();

    void start();

    void stop();

    void update(int i) throws IOException;
}
