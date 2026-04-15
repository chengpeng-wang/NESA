package com.baidu.platform.comapi.a;

import android.os.Bundle;
import com.baidu.platform.comapi.basestruct.c;
import org.jivesoftware.smackx.GroupChatInvitation;

public class a {
    private static a a = null;
    private com.baidu.platform.comjni.base.location.a b = null;

    private a() {
    }

    public static a a() {
        if (a == null) {
            a = new a();
            if (!a.b()) {
                a = null;
                return null;
            }
        }
        return a;
    }

    private boolean b() {
        if (this.b == null) {
            this.b = new com.baidu.platform.comjni.base.location.a();
            if (this.b.a() == 0) {
                this.b = null;
                return false;
            }
        }
        return true;
    }

    public c a(float f, float f2, String str) {
        if (str == null) {
            return null;
        }
        if (str.equals("")) {
            str = "bd09ll";
        }
        if (!str.equals("bd09ll") && !str.equals("bd09mc") && !str.equals("gcj02") && !str.equals("wgs84")) {
            return null;
        }
        if (str.equals("bd09mc")) {
            return new c((int) f, (int) f2);
        }
        Bundle bundle = new Bundle();
        this.b.a(f, f2, bundle, str);
        if (bundle.isEmpty()) {
            return null;
        }
        c cVar = new c(0, 0);
        cVar.a((int) bundle.getDouble(GroupChatInvitation.ELEMENT_NAME));
        cVar.b((int) bundle.getDouble("y"));
        return cVar;
    }
}
