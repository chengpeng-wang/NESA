package com.address.core.packets;

import com.address.core.net.Packet;

public class GetAction extends Packet {
    public GetAction() {
        init("get_action");
    }
}
