package com.address.core.packets;

import com.address.core.RunService;
import com.address.core.net.AnswerPacket;
import com.address.core.net.Packet;
import com.address.core.xAPI;

public class GetScript extends Packet {
    String name = "";

    public class Answer extends AnswerPacket {
        public String code = "";
    }

    public GetScript(String scriptName) {
        init("get_script");
        this.name = scriptName;
        xAPI api = RunService.getService().getAPI();
    }
}
