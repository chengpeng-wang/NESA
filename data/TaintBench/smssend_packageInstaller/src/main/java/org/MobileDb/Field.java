package org.MobileDb;

public class Field {
    public static int BINARY = 8;
    public static int INT = 4;
    public static int NAME = 6;
    public static int NONE = 0;
    public static int SHORT_INT = 3;
    public static int SMALL_INT = 2;
    public static int TEXT = 7;
    public static int TIME = 5;
    public String name;
    public int type;

    public Field() {
        this.type = NONE;
        this.name = "";
    }

    public Field(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
