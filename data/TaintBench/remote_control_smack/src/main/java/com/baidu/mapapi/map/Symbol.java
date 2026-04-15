package com.baidu.mapapi.map;

public class Symbol {
    int a = 0;
    Color b = null;
    int c = 0;

    public class Color {
        public int alpha;
        public int blue;
        public int green;
        public int red;
    }

    public void setLineSymbol(Color color, int i) {
        this.b = color;
        this.a = i;
    }

    public void setPointSymbol(Color color) {
        this.b = color;
        this.c = 1;
    }

    public void setSurface(Color color, int i, int i2) {
        this.b = color;
        this.a = i2;
        this.c = i;
    }
}
