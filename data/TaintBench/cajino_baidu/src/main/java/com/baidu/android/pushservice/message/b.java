package com.baidu.android.pushservice.message;

public class b {
    public int a;
    public int b;
    public byte[] c;
    public boolean d;
    private boolean e;

    public void a(boolean z) {
        this.e = z;
    }

    public boolean a() {
        return this.e;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("id : ");
        stringBuffer.append(this.a);
        stringBuffer.append(", version: ");
        stringBuffer.append(this.b);
        stringBuffer.append(", needReply: ");
        stringBuffer.append(this.d);
        return stringBuffer.toString();
    }
}
