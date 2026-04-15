package com.address.core.net;

import com.address.core.Log;
import com.address.core.Network;
import com.address.core.RunService;
import com.google.gson.Gson;

public class Packet {
    public String action;
    private Class clazz;
    public String deviceID;

    public Packet() {
        this.action = "";
        this.deviceID = "";
        this.clazz = null;
        this.deviceID = RunService.getService().getDeviceID();
    }

    public void init(String actionName) {
        this.action = actionName;
    }

    public String getJSON() {
        return new Gson().toJson((Object) this);
    }

    public static Object getObject(String json, Class c) {
        return new Gson().fromJson(json, c);
    }

    public static Object get(Packet o, Class c) {
        String get = Network.postBase64(o);
        Log.write("Packet.get: " + get);
        return getObject(get, c);
    }
}
