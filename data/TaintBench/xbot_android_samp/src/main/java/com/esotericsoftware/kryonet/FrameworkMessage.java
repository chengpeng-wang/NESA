package com.esotericsoftware.kryonet;

public interface FrameworkMessage {
    public static final KeepAlive keepAlive = new KeepAlive();

    public static class DiscoverHost implements FrameworkMessage {
    }

    public static class KeepAlive implements FrameworkMessage {
    }

    public static class Ping implements FrameworkMessage {
        public int id;
        public boolean isReply;
    }

    public static class RegisterTCP implements FrameworkMessage {
        public int connectionID;
    }

    public static class RegisterUDP implements FrameworkMessage {
        public int connectionID;
    }
}
