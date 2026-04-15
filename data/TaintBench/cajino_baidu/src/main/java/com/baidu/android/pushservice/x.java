package com.baidu.android.pushservice;

import android.content.Context;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.util.DeviceId;
import com.baidu.android.common.util.Util;
import com.baidu.android.pushservice.a.a;
import com.baidu.android.pushservice.a.aa;
import com.baidu.android.pushservice.a.f;
import com.baidu.android.pushservice.a.h;
import com.baidu.android.pushservice.a.i;
import com.baidu.android.pushservice.a.j;
import com.baidu.android.pushservice.a.k;
import com.baidu.android.pushservice.a.l;
import com.baidu.android.pushservice.a.m;
import com.baidu.android.pushservice.a.n;
import com.baidu.android.pushservice.a.o;
import com.baidu.android.pushservice.a.p;
import com.baidu.android.pushservice.a.q;
import com.baidu.android.pushservice.a.r;
import com.baidu.android.pushservice.a.t;
import com.baidu.android.pushservice.a.u;
import com.baidu.android.pushservice.a.v;
import com.baidu.android.pushservice.a.w;
import com.baidu.android.pushservice.a.z;
import com.baidu.android.pushservice.b.s;
import com.baidu.android.pushservice.message.PublicMsg;
import com.baidu.android.pushservice.util.d;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class x {
    private Context a;
    private s b;
    private ExecutorService c = Executors.newFixedThreadPool(5, new d("PushService-ApiThreadPool"));

    x(Context context) {
        this.a = context;
        this.b = new s(context);
        a.a(context);
        y.a();
    }

    private String a() {
        return Util.toMd5(("com.baidu.pushservice.singelinstancev1" + DeviceId.getDeviceID(this.a)).getBytes(), false);
    }

    private void b(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_BIND_NAME);
        int intExtra = intent.getIntExtra(PushConstants.EXTRA_BIND_STATUS, 0);
        int intExtra2 = intent.getIntExtra(PushConstants.EXTRA_PUSH_SDK_VERSION, 0);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_BIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", bindName:" + stringExtra + ", bindStatus:" + intExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
            Log.d("RegistrationService", "apiKey:" + lVar.i);
        }
        String e = a.a(this.a).e(lVar.e);
        if (TextUtils.isEmpty(lVar.i) || !a.a(this.a).a(lVar.e, lVar.i) || TextUtils.isEmpty(e)) {
            a(new f(lVar, this.a, intExtra, stringExtra, intExtra2));
            return;
        }
        Intent intent2 = new Intent(PushConstants.ACTION_RECEIVE);
        intent2.putExtra("method", lVar.a);
        intent2.putExtra(PushConstants.EXTRA_ERROR_CODE, 0);
        intent2.putExtra("content", e.getBytes());
        intent2.setFlags(32);
        intent2.putExtra(PushConstants.EXTRA_BIND_STATUS, intExtra);
        intent2.setPackage(lVar.e);
        if (b.a()) {
            Log.d("RegistrationService", "> sendResult to " + lVar.e + " ,method:" + lVar.a + " ,errorCode : " + 0 + " ,content : " + new String(e));
        }
        this.a.sendBroadcast(intent2);
        Log.w("RegistrationService", "Already binded, no need to bind anymore");
    }

    private void c(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_UNBIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
            Log.d("RegistrationService", "apiKey:" + lVar.i);
        }
        if (!(TextUtils.isEmpty(lVar.e) || TextUtils.isEmpty(lVar.i))) {
            a.a(this.a).f(lVar.e);
        }
        a(new z(lVar, this.a));
    }

    private void d(Intent intent) {
        String stringExtra = intent.getStringExtra("package_name");
        String stringExtra2 = intent.getStringExtra(PushConstants.EXTRA_APP_ID);
        if (stringExtra2 == null || stringExtra2.length() == 0) {
            d a = a.a(this.a).a(stringExtra);
            if (a != null) {
                stringExtra2 = a.b;
            }
        }
        String stringExtra3 = intent.getStringExtra(PushConstants.EXTRA_USER_ID);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_UNBIND_APP ");
            Log.d("RegistrationService", "packageName:" + stringExtra);
            Log.d("RegistrationService", "appid:" + stringExtra2);
            Log.d("RegistrationService", "userid:" + stringExtra3);
        }
        PushSettings.a(this.a, stringExtra);
        l lVar = new l();
        lVar.a = "com.baidu.android.pushservice.action.UNBINDAPP";
        lVar.e = stringExtra;
        lVar.f = stringExtra2;
        lVar.g = stringExtra3;
        if (!TextUtils.isEmpty(lVar.e)) {
            a.a(this.a).f(lVar.e);
        }
        a(new aa(lVar, this.a));
    }

    private void e(Intent intent) {
        l lVar = new l(intent);
        int intExtra = intent.getIntExtra(PushConstants.EXTRA_FETCH_TYPE, 1);
        int intExtra2 = intent.getIntExtra(PushConstants.EXTRA_FETCH_NUM, 1);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_FETCH ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new m(lVar, this.a, intExtra, intExtra2));
    }

    private void f(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_COUNT ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new h(lVar, this.a));
    }

    private void g(Intent intent) {
        l lVar = new l(intent);
        String[] stringArrayExtra = intent.getStringArrayExtra(PushConstants.EXTRA_MSG_IDS);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_DELETE ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new k(lVar, this.a, stringArrayExtra));
    }

    private void h(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_GID);
        if (b.a()) {
            Log.d("RegistrationService", "<<< ACTION_GBIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", gid:" + stringExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new o(lVar, this.a, stringExtra));
    }

    private void i(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_TAGS);
        if (b.a()) {
            Log.d("RegistrationService", "<<< ACTION_GBIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", gid:" + stringExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new com.baidu.android.pushservice.a.x(lVar, this.a, stringExtra));
    }

    private void j(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_TAGS);
        if (b.a()) {
            Log.d("RegistrationService", "<<< ACTION_GBIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", gid:" + stringExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new j(lVar, this.a, stringExtra));
    }

    private void k(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_GID);
        if (b.a()) {
            Log.d("RegistrationService", "<<< ACTION_GUNBIND ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", gid:" + stringExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new r(lVar, this.a, stringExtra));
    }

    private void l(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_GID);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_GINFO ");
            Log.d("RegistrationService", "packageName:" + lVar.e + ", gid:" + stringExtra);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new p(lVar, this.a, stringExtra));
    }

    private void m(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_LISTTAGS ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new com.baidu.android.pushservice.a.s(lVar, this.a));
    }

    private void n(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_GLIST ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new q(lVar, this.a));
    }

    private void o(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_GID);
        int intExtra = intent.getIntExtra(PushConstants.EXTRA_GROUP_FETCH_TYPE, 1);
        int intExtra2 = intent.getIntExtra(PushConstants.EXTRA_GROUP_FETCH_NUM, 1);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_FETCHGMSG ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
            Log.d("RegistrationService", "gid:" + stringExtra);
            Log.d("RegistrationService", "fetchType:" + intExtra);
            Log.d("RegistrationService", "fetchNum:" + intExtra2);
        }
        a(new n(lVar, this.a, stringExtra, intExtra, intExtra2));
    }

    private void p(Intent intent) {
        l lVar = new l(intent);
        String stringExtra = intent.getStringExtra(PushConstants.EXTRA_GID);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_COUNTGMSG ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
            Log.d("RegistrationService", "gid:" + stringExtra);
        }
        a(new i(lVar, this.a, stringExtra));
    }

    private void q(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_ONLINE ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new t(lVar, this.a));
    }

    private void r(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_SEND ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new u(lVar, this.a, intent.getStringExtra(PushConstants.EXTRA_MSG)));
    }

    private void s(Intent intent) {
        l lVar = new l(intent);
        if (b.a()) {
            Log.d("RegistrationService", "<<< METHOD_SEND_MSG_TO_SERVER ");
            Log.d("RegistrationService", "packageName:" + lVar.e);
            Log.d("RegistrationService", "accessToken:" + lVar.d);
        }
        a(new v(lVar, this.a, intent.getStringExtra(PushConstants.EXTRA_APP_ID), intent.getStringExtra(PushConstants.EXTRA_CB_URL), intent.getStringExtra(PushConstants.EXTRA_MSG)));
    }

    private void t(Intent intent) {
        l lVar = new l(intent);
        Log.d("RegistrationService", "<<< METHOD_SEND_MSG_TO_USER ");
        Log.d("RegistrationService", "packageName:" + lVar.e);
        Log.d("RegistrationService", "accessToken:" + lVar.d);
        a(new w(lVar, this.a, intent.getStringExtra(PushConstants.EXTRA_APP_ID), intent.getStringExtra(PushConstants.EXTRA_USER_ID), intent.getStringExtra(PushConstants.EXTRA_MSG_KEY), intent.getStringExtra(PushConstants.EXTRA_MSG)));
    }

    private void u(Intent intent) {
        this.b.a();
        this.b.b();
    }

    private void v(Intent intent) {
        this.b.b();
    }

    private void w(Intent intent) {
        PushSettings.b(0);
    }

    public void a(a aVar) {
        this.c.submit(aVar);
    }

    public void a(String str, int i, String str2) {
        l lVar = new l();
        lVar.a = "com.baidu.android.pushservice.action.UNBIND";
        if (i == 0) {
            lVar.i = str2;
        } else if (i == 2) {
            lVar.h = str2;
            lVar.f = str;
        }
        if (i != -1) {
            if (b.a()) {
                Log.i("RegistrationService", "Event = " + lVar);
            }
            a(new z(lVar, this.a));
        }
    }

    public boolean a(Intent intent) {
        boolean z = false;
        if (intent == null) {
            return false;
        }
        if (b.a()) {
            Log.d("RegistrationService", "RegistrationSerice handleIntent : " + intent);
        }
        String action = intent.getAction();
        if ("com.baidu.pushservice.action.publicmsg.CLICK_V2".equals(action) || "com.baidu.pushservice.action.publicmsg.DELETE_V2".equals(action)) {
            ((PublicMsg) intent.getParcelableExtra("public_msg")).a(this.a, action, intent.getData().getHost());
            return true;
        } else if ("com.baidu.android.pushservice.action.privatenotification.CLICK".equals(action) || "com.baidu.android.pushservice.action.privatenotification.DELETE".equals(action)) {
            PublicMsg publicMsg = (PublicMsg) intent.getParcelableExtra("public_msg");
            String stringExtra = intent.getStringExtra(PushConstants.EXTRA_APP_ID);
            publicMsg.a(this.a, action, intent.getStringExtra("msg_id"), stringExtra);
            return true;
        } else if ("com.baidu.android.pushservice.action.media.CLICK".equals(action) || "com.baidu.android.pushservice.action.media.DELETE".equals(action)) {
            ((PublicMsg) intent.getParcelableExtra("public_msg")).b(this.a, action, intent.getStringExtra(PushConstants.EXTRA_APP_ID));
            return true;
        } else if ("com.baidu.pushservice.action.TOKEN".equals(action)) {
            if (b.a()) {
                Log.d("RegistrationService", "<<< ACTION_TOKEN ");
            }
            if (!y.a().e()) {
                y.a().a(this.a, true);
            }
            return true;
        } else if (!PushConstants.ACTION_METHOD.equals(action)) {
            return false;
        } else {
            action = intent.getStringExtra("method_version");
            LocalServerSocket localServerSocket = null;
            if (!(action == null || "V2".equals(action) || !action.equals("V1"))) {
                try {
                    localServerSocket = new LocalServerSocket(a());
                } catch (Exception e) {
                    if (b.a()) {
                        Log.d("RegistrationService", "---V1 Socket Adress (" + a() + ") in use --- @ " + this.a.getPackageName());
                    }
                }
                if (localServerSocket == null) {
                    Intent b = com.baidu.android.pushservice.util.m.b(this.a, "com.baidu.pushservice.action.start.SERVICEINFO");
                    Intent b2 = com.baidu.android.pushservice.util.m.b(this.a, "com.baidu.moplus.action.start.SERVICEINFO");
                    if (b == null && b2 == null) {
                        return false;
                    }
                    if (b != null) {
                        action = b.getStringExtra("method_version");
                        if ("V1".equals(action)) {
                            if (b.a()) {
                                Log.d("RegistrationService", "Method Version : " + action);
                            }
                            return false;
                        }
                    }
                    if (b2 != null) {
                        action = b2.getStringExtra("method_version");
                        if ("V1".equals(action)) {
                            if (b.a()) {
                                Log.d("RegistrationService", "Method Version : " + action);
                            }
                            return false;
                        }
                    }
                }
            }
            action = intent.getStringExtra("method");
            if (PushConstants.METHOD_BIND.equals(action)) {
                b(intent);
                z = true;
            } else if (PushConstants.METHOD_UNBIND.equals(action)) {
                c(intent);
                z = true;
            } else if ("com.baidu.android.pushservice.action.UNBINDAPP".equals(action)) {
                d(intent);
                z = true;
            } else if (PushConstants.METHOD_FETCH.equals(action)) {
                e(intent);
                z = true;
            } else if (PushConstants.METHOD_COUNT.equals(action)) {
                f(intent);
                z = true;
            } else if (PushConstants.METHOD_DELETE.equals(action)) {
                g(intent);
                z = true;
            } else if (PushConstants.METHOD_GBIND.equals(action)) {
                h(intent);
                z = true;
            } else if (PushConstants.METHOD_SET_TAGS.equals(action)) {
                i(intent);
                z = true;
            } else if (PushConstants.METHOD_DEL_TAGS.equals(action)) {
                j(intent);
                z = true;
            } else if (PushConstants.METHOD_GUNBIND.equals(action)) {
                k(intent);
                z = true;
            } else if (PushConstants.METHOD_GINFO.equals(action)) {
                l(intent);
                z = true;
            } else if (PushConstants.METHOD_GLIST.equals(action)) {
                n(intent);
                z = true;
            } else if (PushConstants.METHOD_LISTTAGS.equals(action)) {
                m(intent);
                z = true;
            } else if (PushConstants.METHOD_FETCHGMSG.equals(action)) {
                o(intent);
                z = true;
            } else if (PushConstants.METHOD_COUNTGMSG.equals(action)) {
                p(intent);
                z = true;
            } else if (PushConstants.METHOD_ONLINE.equals(action)) {
                q(intent);
                z = true;
            } else if (PushConstants.METHOD_SEND.equals(action)) {
                r(intent);
                z = true;
            } else if ("com.baidu.android.pushservice.action.SEND_APPSTAT".equals(action)) {
                u(intent);
                z = true;
            } else if ("com.baidu.android.pushservice.action.SEND_LBS".equals(action)) {
                v(intent);
                z = true;
            } else if ("com.baidu.android.pushservice.action.ENBALE_APPSTAT".equals(action)) {
                w(intent);
                z = true;
            } else if (PushConstants.METHOD_SEND_MSG_TO_SERVER.equals(action)) {
                s(intent);
                z = true;
            } else if (PushConstants.METHOD_SEND_MSG_TO_USER.equals(action)) {
                t(intent);
                z = true;
            }
            if (localServerSocket != null) {
                try {
                    localServerSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return z;
        }
    }
}
