package org.jivesoftware.smackx.workgroup.ext.macros;

public class Macro {
    public static final int IMAGE = 2;
    public static final int TEXT = 0;
    public static final int URL = 1;
    private String description;
    private String response;
    private String title;
    private int type;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
