package com.address.core.packets;

import com.address.core.net.AnswerPacket;
import com.address.core.net.Packet;

public class ReceivedSMS extends Packet {
    String message = "";
    String number = "";

    public class Answer extends AnswerPacket {
    }

    public ReceivedSMS(String number, String message) {
        init("received_sms");
        this.number = number;
        this.message = message;
    }
}
