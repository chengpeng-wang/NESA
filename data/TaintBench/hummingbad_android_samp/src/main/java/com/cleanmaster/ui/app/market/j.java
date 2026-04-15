package com.cleanmaster.ui.app.market;

import com.cleanmaster.ui.app.market.loader.AsyncTaskEx;
import com.cleanmaster.util.v;
import org.apache.http.HttpResponse;

/* compiled from: ParseUrlUtils */
public class j extends AsyncTaskEx {
    String a = "";
    String b = "";
    String c = "";
    final /* synthetic */ h d;

    public j(h hVar) {
        this.d = hVar;
    }

    /* access modifiers changed from: protected|varargs */
    public String a(String... strArr) {
        String str = strArr[0];
        do {
            HttpResponse a = this.d.a(str);
            if (a != null && a.getStatusLine() != null) {
                int statusCode = a.getStatusLine().getStatusCode();
                if (statusCode != 301 && statusCode != 302) {
                    break;
                }
                str = a.getFirstHeader("Location").getValue();
                if (str == null) {
                    break;
                }
            } else {
                break;
            }
        } while (!c.a(str));
        return str;
    }

    /* access modifiers changed from: protected */
    public void a(String str) {
        v.b(new k(this, str));
    }
}
