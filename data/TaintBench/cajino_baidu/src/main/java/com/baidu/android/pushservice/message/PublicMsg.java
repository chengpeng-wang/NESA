package com.baidu.android.pushservice.message;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.a;
import com.baidu.android.pushservice.b.b;
import com.baidu.android.pushservice.b.j;
import com.baidu.android.pushservice.b.s;
import com.baidu.android.pushservice.d;
import com.baidu.android.pushservice.util.m;
import com.baidu.android.pushservice.y;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicMsg implements Parcelable {
    public static final Creator CREATOR = new h();
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public int g = 0;
    public String h;
    public int i = 0;
    public int j = 0;
    public int k = 0;
    public int l = 0;
    public int m = 7;
    public String n;
    public String o;
    public boolean p = true;

    PublicMsg() {
    }

    PublicMsg(Parcel parcel) {
        this.a = parcel.readString();
        this.b = parcel.readString();
        this.c = parcel.readString();
        this.d = parcel.readString();
        this.e = parcel.readString();
        this.f = parcel.readString();
        this.g = parcel.readInt();
        this.j = parcel.readInt();
        this.m = parcel.readInt();
        this.k = parcel.readInt();
        this.l = parcel.readInt();
        this.n = parcel.readString();
        this.h = parcel.readString();
    }

    private String a(Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo != null) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }

    private void a(Context context, d dVar, j jVar, b bVar) {
        if (dVar != null) {
            bVar.d(dVar.a);
            bVar.c(m.b(dVar.c));
            bVar.b(dVar.c);
            b a = m.a(bVar, context, dVar.a);
            s.a(context, jVar);
            s.a(context, a);
        }
    }

    private void a(Intent intent) {
        if (this.n != null) {
            try {
                JSONObject jSONObject = new JSONObject(this.n);
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    intent.putExtra(str, jSONObject.getString(str));
                }
                intent.putExtra(PushConstants.EXTRA_EXTRA, this.n);
            } catch (JSONException e) {
                Log.w("PublicMsg", "Custom content to JSONObject exception::" + e.getMessage());
            }
        }
    }

    private void c(Context context, String str, String str2) {
        try {
            Intent parseUri = this.h != null ? Intent.parseUri(this.h, 0) : new Intent();
            String a = a(context, str);
            if (a != null) {
                parseUri.setClassName(str, a);
                parseUri.setFlags(parseUri.getFlags() | 268435456);
                parseUri.putExtra(PushConstants.EXTRA_OPENTYPE, 1);
                parseUri.putExtra(PushConstants.EXTRA_MSGID, str2);
                context.startActivity(parseUri);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context, String str, int i) {
        String c = y.a().c();
        String d = y.a().d();
        if (!TextUtils.isEmpty(c) && !TextUtils.isEmpty(d)) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("PublicMsg", "Send Linkhit, msgId = " + str + ", resultCode = " + i);
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(PushConstants.EXTRA_MSGID, str);
                jSONObject.put("result_code", i);
            } catch (JSONException e) {
                if (com.baidu.android.pushservice.b.a()) {
                    Log.d("PublicMsg", e.getMessage());
                }
            }
            Thread thread = new Thread(new g(this, context, c, d, jSONObject.toString()));
            thread.setName("PushService-linkhit");
            thread.start();
        } else if (com.baidu.android.pushservice.b.a()) {
            Log.e("PublicMsg", "Fail Send Public msg result. Token invalid!");
        }
    }

    public void a(Context context, String str, String str2) {
        int i = 1;
        int i2 = 0;
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("PublicMsg", "=== Handle msg: " + toString());
        }
        if ("com.baidu.pushservice.action.publicmsg.DELETE_V2".equals(str)) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("PublicMsg", "Public msg deleted by user, title = " + this.c);
            }
            a(context, str2, 2);
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            int i3 = packageManager.getPackageInfo(this.f, 0).versionCode;
            if (i3 >= this.g) {
                Intent parseUri = Intent.parseUri(this.h, 0);
                parseUri.setPackage(this.f);
                if (packageManager.queryBroadcastReceivers(parseUri, 0).size() > 0) {
                    if (com.baidu.android.pushservice.b.a()) {
                        Log.d("PublicMsg", "Intent broadcasted to app! ===> " + parseUri.toURI());
                    }
                    context.sendBroadcast(parseUri);
                } else if (packageManager.queryIntentActivities(parseUri, 0).size() > 0) {
                    if (com.baidu.android.pushservice.b.a()) {
                        Log.d("PublicMsg", "Intent sent to actvity! ===> " + parseUri.toURI());
                    }
                    parseUri.addFlags(268435456);
                    context.startActivity(parseUri);
                } else {
                    if (com.baidu.android.pushservice.b.a()) {
                        Log.d("PublicMsg", "No app component can deal, so start " + this.f + " launcher activity!");
                    }
                    i = 0;
                }
                i2 = i;
            } else if (com.baidu.android.pushservice.b.a()) {
                Log.d("PublicMsg", "Version code is too low! ===> app ver: " + i3 + ", request ver:" + this.g);
            }
        } catch (NameNotFoundException e) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", "package not exist \r\n" + e.getMessage());
            }
        } catch (URISyntaxException e2) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", "uri to intent fail \r\n" + e2.getMessage());
            }
        } catch (Exception e3) {
            Log.e("PublicMsg", "parse customize action error\r\n" + e3.getMessage());
        }
        if (i2 == 0) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.e));
            intent.addFlags(268435456);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e4) {
                if (com.baidu.android.pushservice.b.a()) {
                    Log.e("PublicMsg", ">>> Url cann't be deal! \r\n" + e4.getMessage());
                }
            }
        }
        a(context, str2, i2);
    }

    public void a(Context context, String str, String str2, String str3) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("PublicMsg", "=== Handle private notification: " + str);
        }
        if ("com.baidu.android.pushservice.action.privatenotification.DELETE".equals(str)) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.d("PublicMsg", "private notification deleted by user, title = " + this.c);
            }
            j jVar = new j();
            jVar.c("010202");
            jVar.a(str2);
            jVar.a(System.currentTimeMillis());
            jVar.d(com.baidu.android.pushservice.b.m.d(context));
            jVar.c(5);
            jVar.e(str3);
            d b = a.a(context).b(str3);
            b bVar = new b(str3);
            bVar.c(m.b(b.c));
            bVar.b(b.c);
            bVar.d(b.a);
            a(context, b, jVar, bVar);
            return;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.f, 0);
            int i = packageInfo.versionCode;
            if (i >= this.g) {
                Intent intent = new Intent(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK);
                intent.setPackage(this.f);
                intent.putExtra(PushConstants.EXTRA_NOTIFICATION_TITLE, this.c);
                intent.putExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT, this.d);
                a(intent);
                context.sendBroadcast(intent);
                j jVar2 = new j();
                jVar2.c("010201");
                jVar2.a(str2);
                jVar2.a(System.currentTimeMillis());
                jVar2.d(com.baidu.android.pushservice.b.m.d(context));
                s.a(context, jVar2);
                if (this.k != 1 || this.e == null) {
                    if (this.k != 2) {
                        return;
                    }
                    if (this.h != null) {
                        Intent parseUri = Intent.parseUri(this.h, 0);
                        parseUri.setPackage(this.f);
                        if (packageManager.queryBroadcastReceivers(parseUri, 0).size() > 0) {
                            if (com.baidu.android.pushservice.b.a()) {
                                Log.d("PublicMsg", "Intent broadcasted to app! ===> " + parseUri.toURI());
                            }
                            context.sendBroadcast(parseUri);
                            return;
                        } else if (packageManager.queryIntentActivities(parseUri, 0).size() > 0) {
                            if (com.baidu.android.pushservice.b.a()) {
                                Log.d("PublicMsg", "Intent sent to actvity! ===> " + parseUri.toURI());
                            }
                            parseUri.addFlags(268435456);
                            parseUri.putExtra(PushConstants.EXTRA_OPENTYPE, 1);
                            parseUri.putExtra(PushConstants.EXTRA_MSGID, str2);
                            context.startActivity(parseUri);
                            return;
                        } else {
                            return;
                        }
                    }
                    c(context, this.f, str2);
                } else if (this.l == 1) {
                    Builder builder = new Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage(packageInfo.applicationInfo.loadLabel(packageManager) + "请求访问:" + this.e + " ?");
                    builder.setPositiveButton("允许", new e(this, context));
                    builder.setNeutralButton("拒绝", new f(this));
                    AlertDialog create = builder.create();
                    create.getWindow().setType(2003);
                    create.setCanceledOnTouchOutside(false);
                    create.show();
                } else {
                    Intent intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    intent2.setData(Uri.parse(this.e));
                    intent2.addFlags(268435456);
                    context.startActivity(intent2);
                }
            } else if (com.baidu.android.pushservice.b.a()) {
                Log.d("PublicMsg", "Version code is too low! ===> app ver: " + i + ", request ver:" + this.g);
            }
        } catch (NameNotFoundException e) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", "package not exist \r\n" + e.getMessage());
            }
        } catch (URISyntaxException e2) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", "uri to intent fail \r\n" + e2.getMessage());
            }
        }
    }

    public void b(Context context, String str, String str2) {
        if (com.baidu.android.pushservice.b.a()) {
            Log.d("PublicMsg", "=== Handle rich media notification: " + str + " title = " + this.c);
        }
        if (!"com.baidu.android.pushservice.action.media.DELETE".equals(str)) {
            Intent intent = new Intent("com.baidu.android.pushservice.action.media.CLICK");
            intent.setPackage(this.f);
            intent.putExtra("public_msg", this);
            context.sendBroadcast(intent);
        } else if ("com.baidu.android.pushservice.action.media.DELETE".equals(str)) {
            j jVar = new j();
            jVar.c("010402");
            jVar.a(this.a);
            jVar.a(System.currentTimeMillis());
            jVar.a(0);
            jVar.d(com.baidu.android.pushservice.b.m.d(context));
            jVar.e(str2);
            d b = a.a(context).b(str2);
            b bVar = new b(str2);
            bVar.c(m.b(b.c));
            bVar.b(b.c);
            bVar.d(b.a);
            a(context, b, jVar, bVar);
        }
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "\r\n mMsgId = " + this.a + "\r\n mAppId = " + this.b + "\r\n mTitle = " + this.c + "\r\n mDescription = " + this.d + "\r\n mUrl = " + this.e + "\r\n mNetType = " + this.i + "\r\n mSupportAppname = " + this.o + "\r\n mIsSupportApp = " + this.p + "\r\n mPkgName = " + this.f + "\r\n mPlgVercode = " + this.g + "\r\n mNotificationBuilder = " + this.j + "\r\n mNotificationBasicStyle = " + this.m + "\r\n mOpenType = " + this.k + "\r\n mUserConfirm = " + this.l + "\r\n mCustomContent = " + this.n + "\r\n mIntent = " + this.h;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.e);
        parcel.writeString(this.f);
        parcel.writeInt(this.g);
        parcel.writeInt(this.j);
        parcel.writeInt(this.m);
        parcel.writeInt(this.k);
        parcel.writeInt(this.l);
        parcel.writeString(this.n);
        parcel.writeString(this.h);
    }
}
