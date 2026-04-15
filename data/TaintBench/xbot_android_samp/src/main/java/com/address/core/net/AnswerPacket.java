package com.address.core.net;

public class AnswerPacket extends Packet {
    public String result = "";

    public void init(String actionName) {
        this.action = actionName;
    }
}
