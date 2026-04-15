package com.baidu.platform.comapi.map;

import android.os.Message;

class m {
    private l a = null;

    m() {
    }

    public void a() {
    }

    /* access modifiers changed from: 0000 */
    public void a(Message message) {
        if (message.what == 65289) {
            switch (message.arg1) {
                case -1:
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 12:
                case 101:
                case 102:
                    if (this.a != null) {
                        this.a.a(message.arg1, message.arg2);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(l lVar) {
        this.a = lVar;
    }

    /* access modifiers changed from: 0000 */
    public void b(l lVar) {
        this.a = null;
    }
}
