package org.objectweb.asm;

class Handler {
    Label a;
    Label b;
    Label c;
    String d;
    int e;
    Handler f;

    Handler() {
    }

    static Handler a(Handler handler, Label label, Label label2) {
        if (handler == null) {
            return null;
        }
        handler.f = a(handler.f, label, label2);
        int i = handler.a.c;
        int i2 = handler.b.c;
        int i3 = label.c;
        int i4 = label2 == null ? Integer.MAX_VALUE : label2.c;
        if (i3 >= i2 || i4 <= i) {
            return handler;
        }
        if (i3 <= i) {
            if (i4 >= i2) {
                return handler.f;
            }
            handler.a = label2;
            return handler;
        } else if (i4 >= i2) {
            handler.b = label;
            return handler;
        } else {
            Handler handler2 = new Handler();
            handler2.a = label2;
            handler2.b = handler.b;
            handler2.c = handler.c;
            handler2.d = handler.d;
            handler2.e = handler.e;
            handler2.f = handler.f;
            handler.b = label;
            handler.f = handler2;
            return handler;
        }
    }
}
