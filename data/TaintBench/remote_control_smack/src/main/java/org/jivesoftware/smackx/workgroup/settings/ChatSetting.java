package org.jivesoftware.smackx.workgroup.settings;

public class ChatSetting {
    private String key;
    private int type;
    private String value;

    public ChatSetting(String key, String value, int type) {
        setKey(key);
        setValue(value);
        setType(type);
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
