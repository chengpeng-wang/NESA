package com.mobclick.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.feedback.b.d;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.microedition.khronos.opengles.GL10;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobclickAgent {
    public static String GPU_RENDERER = "";
    public static String GPU_VENDER = "";
    /* access modifiers changed from: private|static|final */
    public static final MobclickAgent a = new MobclickAgent();
    private static int b = 1;
    private static UmengUpdateListener e = null;
    private static JSONObject f = null;
    public static boolean mUpdateOnlyWifi = true;
    public static boolean updateAutoPopup = true;
    private Context c;
    private final Handler d;

    private MobclickAgent() {
        HandlerThread handlerThread = new HandlerThread(UmengConstants.LOG_TAG);
        handlerThread.start();
        this.d = new Handler(handlerThread.getLooper());
    }

    private static String a(Context context) {
        String str = "";
        String packageName;
        try {
            packageName = context.getPackageName();
            ArrayList arrayList = new ArrayList();
            arrayList.add("logcat");
            arrayList.add("-d");
            arrayList.add("-v");
            arrayList.add("raw");
            arrayList.add("-s");
            arrayList.add("AndroidRuntime:E");
            arrayList.add("-p");
            arrayList.add(packageName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[]) arrayList.toArray(new String[arrayList.size()])).getInputStream()), 1024);
            Object obj = null;
            String str2 = "";
            String readLine = bufferedReader.readLine();
            Object obj2 = null;
            while (readLine != null) {
                if (readLine.indexOf("thread attach failed") < 0) {
                    str2 = new StringBuilder(String.valueOf(str2)).append(readLine).append(10).toString();
                }
                if (obj2 == null && readLine.toLowerCase().indexOf("exception") >= 0) {
                    obj2 = 1;
                }
                Object obj3 = (obj != null || readLine.indexOf(packageName) < 0) ? obj : 1;
                obj = obj3;
                readLine = bufferedReader.readLine();
            }
            if (!(str2.length() <= 0 || obj2 == null || obj == null)) {
                str = str2;
            }
            try {
                Runtime.getRuntime().exec("logcat -c");
                return str;
            } catch (Exception e) {
                Log.e(UmengConstants.LOG_TAG, "Failed to clear log");
                e.printStackTrace();
                return str;
            }
        } catch (Exception e2) {
            Exception exception = e2;
            packageName = str;
            Exception exception2 = exception;
            Log.e(UmengConstants.LOG_TAG, "Failed to catch error log");
            exception2.printStackTrace();
            return packageName;
        }
    }

    private String a(Context context, SharedPreferences sharedPreferences) {
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        Editor edit = sharedPreferences.edit();
        edit.putLong("start_millis", valueOf.longValue());
        edit.putLong("end_millis", -1);
        edit.commit();
        return sharedPreferences.getString("session_id", null);
    }

    private String a(Context context, String str, SharedPreferences sharedPreferences) {
        c(context, sharedPreferences);
        long currentTimeMillis = System.currentTimeMillis();
        String stringBuilder = new StringBuilder(String.valueOf(str)).append(String.valueOf(currentTimeMillis)).toString();
        Editor edit = sharedPreferences.edit();
        edit.putString(UmengConstants.AtomKey_AppKey, str);
        edit.putString("session_id", stringBuilder);
        edit.putLong("start_millis", currentTimeMillis);
        edit.putLong("end_millis", -1);
        edit.putLong("duration", 0);
        edit.putString("activities", "");
        edit.commit();
        b(context, sharedPreferences);
        return stringBuilder;
    }

    private static String a(Context context, JSONObject jSONObject, String str, boolean z) {
        Log.i(UmengConstants.LOG_TAG, jSONObject.toString());
        HttpPost httpPost = new HttpPost(str);
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, d.b);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        try {
            String a = m.a(context);
            if (a != null) {
                Log.i("TAG", "Proxy IP:" + a);
                defaultHttpClient.getParams().setParameter("http.route.default-proxy", new HttpHost(a, 80));
            }
            a = jSONObject.toString();
            if (!UmengConstants.COMPRESS_DATA || z) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair(UmengConstants.AtomKey_Content, a));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            } else {
                byte[] b = l.b("content=" + a);
                httpPost.addHeader("Content-Encoding", "deflate");
                httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(b), (long) l.b));
            }
            Editor edit = g(context).edit();
            Date date = new Date();
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            long time = new Date().getTime() - date.getTime();
            if (execute.getStatusLine().getStatusCode() == 200) {
                Log.i(UmengConstants.LOG_TAG, "Sent message to " + str);
                edit.putLong("req_time", time);
                edit.commit();
                HttpEntity entity = execute.getEntity();
                return entity != null ? a(entity.getContent()) : null;
            } else {
                edit.putLong("req_time", -1);
                return null;
            }
        } catch (ClientProtocolException e) {
            Log.i(UmengConstants.LOG_TAG, "ClientProtocolException,Failed to send message.", e);
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            Log.i(UmengConstants.LOG_TAG, "IOException,Failed to send message.", e2);
            e2.printStackTrace();
            return null;
        }
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(new StringBuilder(String.valueOf(readLine)).append("\n").toString());
            } catch (IOException e) {
                stringBuilder = UmengConstants.LOG_TAG;
                Log.e(stringBuilder, "Caught IOException in convertStreamToString()", e);
                e.printStackTrace();
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    Log.e(UmengConstants.LOG_TAG, "Caught IOException in convertStreamToString()", e2);
                    e2.printStackTrace();
                    return null;
                }
            }
        }
        return stringBuilder.toString();
    }

    private static void a(Context context, int i) {
        if (i < 0 || i > 5) {
            Log.e(UmengConstants.LOG_TAG, "Illegal value of report policy");
            return;
        }
        SharedPreferences l = l(context);
        if (!l.contains("umeng_report_policy")) {
            synchronized (UmengConstants.saveOnlineConfigMutex) {
                l.edit().putInt("umeng_report_policy", i).commit();
            }
        }
    }

    private void a(Context context, SharedPreferences sharedPreferences, String str, String str2, int i) {
        String string = sharedPreferences.getString("session_id", "");
        String b = b();
        Object obj = b.split(" ")[0];
        Object obj2 = b.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "event");
            jSONObject.put("session_id", string);
            jSONObject.put("date", obj);
            jSONObject.put("time", obj2);
            jSONObject.put("tag", str);
            jSONObject.put("label", str2);
            jSONObject.put("acc", i);
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private synchronized void a(Context context, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "update");
            jSONObject.put(UmengConstants.AtomKey_AppKey, str);
            int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String packageName = context.getPackageName();
            jSONObject.put("version_code", i);
            jSONObject.put("package", packageName);
            jSONObject.put(UmengConstants.AtomKey_SDK_Version, UmengConstants.SDK_VERSION);
            jSONObject.put("idmd5", l.c(context));
            jSONObject.put("channel", l.g(context));
            this.d.post(new k(this, context, jSONObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(Context context, String str, String str2) {
        this.c = context;
        SharedPreferences i = i(context);
        if (i != null) {
            if (a(i)) {
                Log.i(UmengConstants.LOG_TAG, "Start new session: " + a(context, str, i));
            } else {
                Log.i(UmengConstants.LOG_TAG, "Extend current session: " + a(context, i));
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(Context context, String str, String str2, String str3, int i) {
        SharedPreferences i2 = i(context);
        if (i2 != null) {
            a(context, i2, str2, str3, i);
        }
    }

    /* access modifiers changed from: private */
    public void a(Context context, JSONObject jSONObject) {
        if (a("update", context)) {
            String str;
            String str2 = null;
            for (String str22 : UmengConstants.UPDATE_URL_LIST) {
                str22 = a(context, jSONObject, str22, true);
                Log.i(UmengConstants.LOG_TAG, "return message from " + str22);
                if (str22 != null) {
                    str = str22;
                    break;
                }
            }
            str = str22;
            if (str != null) {
                d(context, str);
            } else if (e != null) {
                e.onUpdateReturned(UpdateStatus.Timeout);
            }
        } else if (e != null) {
            e.onUpdateReturned(UpdateStatus.No);
        }
    }

    private boolean a(SharedPreferences sharedPreferences) {
        return UmengConstants.testMode || System.currentTimeMillis() - sharedPreferences.getLong("end_millis", -1) > UmengConstants.kContinueSessionMillis;
    }

    private static boolean a(String str, Context context) {
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0 && !l.h(context)) {
            return false;
        }
        if (str == "update" || str == UmengConstants.FeedbackPreName || str == "online_config") {
            return true;
        }
        b = m(context);
        if (b == 3) {
            if (str == "flush") {
                return true;
            }
        } else if (str == UmengConstants.Atom_State_Error) {
            return true;
        } else {
            if (b == 1 && str == "launch") {
                return true;
            }
            if (b == 2 && str == "terminate") {
                return true;
            }
            if (b == 0) {
                return true;
            }
            if (b == 4) {
                String string = h(context).getString(l.b(), "false");
                Log.i(UmengConstants.LOG_TAG, "Log has been sent today: " + string + ";type:" + str);
                return !string.equals("true") && str.equals("launch");
            } else if (b == 5) {
                return l.f(context)[0].equals("Wi-Fi");
            }
        }
        return false;
    }

    private static AlertDialog b(Context context, File file) {
        Builder builder = new Builder(context);
        builder.setTitle(context.getString(l.a(context, "string", "UMUpdateTitle"))).setMessage(context.getString(l.a(context, "string", "UMDialog_InstallAPK"))).setCancelable(false).setPositiveButton(context.getString(l.a(context, "string", "UMUpdateNow")), new f(context, file)).setNegativeButton(context.getString(l.a(context, "string", "UMNotNow")), new g());
        AlertDialog create = builder.create();
        create.setCancelable(true);
        return create;
    }

    private static AlertDialog b(Context context, JSONObject jSONObject) {
        try {
            String string = jSONObject.has("version") ? jSONObject.getString("version") : "";
            String string2 = jSONObject.has("update_log") ? jSONObject.getString("update_log") : "";
            String string3 = jSONObject.has("path") ? jSONObject.getString("path") : "";
            Object obj = "";
            if (!l.f(context)[0].equals("Wi-Fi")) {
                obj = context.getString(l.a(context, "string", "UMGprsCondition")) + "\n";
            }
            Builder builder = new Builder(context);
            builder.setTitle(context.getString(l.a(context, "string", "UMUpdateTitle"))).setMessage(new StringBuilder(String.valueOf(obj)).append(context.getString(l.a(context, "string", "UMNewVersion"))).append(string).append("\n").append(string2).toString()).setCancelable(false).setPositiveButton(context.getString(l.a(context, "string", "UMUpdateNow")), new h(context, string3, string)).setNegativeButton(context.getString(l.a(context, "string", "UMNotNow")), new i());
            AlertDialog create = builder.create();
            create.setCancelable(true);
            return create;
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, "Fail to create update dialog box.", e);
            e.printStackTrace();
            return null;
        }
    }

    private static String b() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void b(Context context) {
        if (this.c != context) {
            Log.e(UmengConstants.LOG_TAG, "onPause() called without context from corresponding onResume()");
        } else {
            this.c = context;
            SharedPreferences i = i(context);
            if (i != null) {
                long j = i.getLong("start_millis", -1);
                if (j == -1) {
                    Log.e(UmengConstants.LOG_TAG, "onEndSession called before onStartSession");
                } else {
                    long currentTimeMillis = System.currentTimeMillis();
                    j = currentTimeMillis - j;
                    long j2 = i.getLong("duration", 0);
                    Editor edit = i.edit();
                    if (UmengConstants.ACTIVITY_DURATION_OPEN) {
                        Object string = i.getString("activities", "");
                        String name = context.getClass().getName();
                        if (!"".equals(string)) {
                            string = new StringBuilder(String.valueOf(string)).append(";").toString();
                        }
                        String stringBuilder = new StringBuilder(String.valueOf(string)).append("[").append(name).append(",").append(j / 1000).append("]").toString();
                        edit.remove("activities");
                        edit.putString("activities", stringBuilder);
                    }
                    edit.putLong("start_millis", -1);
                    edit.putLong("end_millis", currentTimeMillis);
                    edit.putLong("duration", j + j2);
                    edit.commit();
                }
            }
        }
    }

    private void b(Context context, SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("session_id", null);
        if (string == null) {
            Log.e(UmengConstants.LOG_TAG, "Missing session_id, ignore message");
            return;
        }
        String b = b();
        Object obj = b.split(" ")[0];
        Object obj2 = b.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "launch");
            jSONObject.put("session_id", string);
            jSONObject.put("date", obj);
            jSONObject.put("time", obj2);
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void b(Context context, String str) {
        String a = a(context);
        if (a != "" && a.length() <= 10240) {
            c(context, a);
        }
    }

    private synchronized void c(Context context) {
        d(context);
    }

    private void c(Context context, SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("session_id", null);
        if (string == null) {
            Log.w(UmengConstants.LOG_TAG, "Missing session_id, ignore message");
            return;
        }
        Long valueOf = Long.valueOf(sharedPreferences.getLong("duration", -1));
        if (valueOf.longValue() <= 0) {
            valueOf = Long.valueOf(0);
        }
        String b = b();
        Object obj = b.split(" ")[0];
        Object obj2 = b.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "terminate");
            jSONObject.put("session_id", string);
            jSONObject.put("date", obj);
            jSONObject.put("time", obj2);
            jSONObject.put("duration", String.valueOf(valueOf.longValue() / 1000));
            if (UmengConstants.ACTIVITY_DURATION_OPEN) {
                String[] split = sharedPreferences.getString("activities", "").split(";");
                JSONArray jSONArray = new JSONArray();
                for (String jSONArray2 : split) {
                    jSONArray.put(new JSONArray(jSONArray2));
                }
                jSONObject.put("activities", jSONArray);
            }
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private|static */
    public static void c(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void c(Context context, String str) {
        String b = b();
        Object obj = b.split(" ")[0];
        Object obj2 = b.split(" ")[1];
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, UmengConstants.Atom_State_Error);
            jSONObject.put("context", str);
            jSONObject.put("date", obj);
            jSONObject.put("time", obj2);
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void c(Context context, JSONObject jSONObject) {
        SharedPreferences g = g(context);
        JSONObject e = l.e(context);
        long j = g.getLong("req_time", 0);
        if (j != 0) {
            try {
                e.put("req_time", j);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        g.edit().putString("header", e.toString()).commit();
        JSONObject e3 = e(context);
        JSONObject jSONObject2 = new JSONObject();
        try {
            String string = jSONObject.getString(UmengConstants.AtomKey_Type);
            if (string != null) {
                if (string != "flush") {
                    jSONObject.remove(UmengConstants.AtomKey_Type);
                    JSONArray jSONArray;
                    if (e3 == null) {
                        e3 = new JSONObject();
                        jSONArray = new JSONArray();
                        jSONArray.put(jSONObject);
                        e3.put(string, jSONArray);
                    } else if (e3.isNull(string)) {
                        jSONArray = new JSONArray();
                        jSONArray.put(jSONObject);
                        e3.put(string, jSONArray);
                    } else {
                        e3.getJSONArray(string).put(jSONObject);
                    }
                }
                if (e3 == null) {
                    Log.w(UmengConstants.LOG_TAG, "No cache message to flush");
                    return;
                }
                jSONObject2.put("header", e);
                jSONObject2.put("body", e3);
                if (a(string, context)) {
                    String str;
                    string = null;
                    for (String string2 : UmengConstants.APPLOG_URL_LIST) {
                        string2 = a(context, jSONObject2, string2, false);
                        Log.i(UmengConstants.LOG_TAG, "return message from " + string2);
                        if (string2 != null) {
                            str = string2;
                            break;
                        }
                    }
                    str = string2;
                    if (str != null) {
                        Log.i(UmengConstants.LOG_TAG, "send message succeed, clear cache");
                        f(context);
                        if (b == 4) {
                            Editor edit = h(context).edit();
                            edit.putString(l.b(), "true");
                            edit.commit();
                            return;
                        }
                        return;
                    }
                }
                d(context, e3);
            }
        } catch (JSONException e4) {
            Log.e(UmengConstants.LOG_TAG, "Fail to construct json message.");
            e4.printStackTrace();
            f(context);
        }
    }

    private void d(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "flush");
            this.d.post(new k(this, context, jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void d(Context context, String str) {
        try {
            f = new JSONObject(str);
            if (f.getString("update").equals("Yes")) {
                if (e != null) {
                    e.onUpdateReturned(UpdateStatus.Yes);
                }
                if (updateAutoPopup) {
                    showUpdateDialog(context);
                }
            } else if (e != null) {
                e.onUpdateReturned(UpdateStatus.No);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void d(Context context, JSONObject jSONObject) {
        try {
            FileOutputStream openFileOutput = context.openFileOutput(k(context), 0);
            openFileOutput.write(jSONObject.toString().getBytes());
            openFileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static File e(Context context, JSONObject jSONObject) {
        try {
            String string = jSONObject.has("path") ? jSONObject.getString("path") : "";
            String string2 = jSONObject.has("version") ? jSONObject.getString("version") : "";
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(string).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            int contentLength = httpURLConnection.getContentLength();
            httpURLConnection.disconnect();
            if (Environment.getExternalStorageState().equals("mounted")) {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                string = externalStorageDirectory.getParent() + "/" + externalStorageDirectory.getName() + "/download";
            } else {
                string = context.getFilesDir().getAbsolutePath();
            }
            File file = new File(string, a.a(context.getPackageName(), string2, contentLength));
            if (file.exists() && file.length() == ((long) contentLength)) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject e(Context context) {
        try {
            FileInputStream openFileInput = context.openFileInput(k(context));
            String str = "";
            byte[] bArr = new byte[16384];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                str = new StringBuilder(String.valueOf(str)).append(new String(bArr, 0, read)).toString();
            }
            if (str.length() == 0) {
                return null;
            }
            try {
                return new JSONObject(str);
            } catch (JSONException e) {
                openFileInput.close();
                f(context);
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e2) {
            return null;
        } catch (IOException e3) {
            return null;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    private static void e(android.content.Context r6, java.lang.String r7) {
        /*
        r0 = l(r6);
        r1 = com.mobclick.android.UmengConstants.saveOnlineConfigMutex;
        monitor-enter(r1);
        r2 = new org.json.JSONObject;	 Catch:{ Exception -> 0x006f }
        r2.<init>(r7);	 Catch:{ Exception -> 0x006f }
        r3 = "last_config_time";
        r3 = r2.has(r3);	 Catch:{ Exception -> 0x007c }
        if (r3 == 0) goto L_0x0027;
    L_0x0014:
        r3 = r0.edit();	 Catch:{ Exception -> 0x007c }
        r4 = "umeng_last_config_time";
        r5 = "last_config_time";
        r5 = r2.getString(r5);	 Catch:{ Exception -> 0x007c }
        r3 = r3.putString(r4, r5);	 Catch:{ Exception -> 0x007c }
        r3.commit();	 Catch:{ Exception -> 0x007c }
    L_0x0027:
        r3 = "report_policy";
        r3 = r2.has(r3);	 Catch:{ Exception -> 0x0081 }
        if (r3 == 0) goto L_0x0042;
    L_0x002f:
        r3 = r0.edit();	 Catch:{ Exception -> 0x0081 }
        r4 = "umeng_report_policy";
        r5 = "report_policy";
        r5 = r2.getInt(r5);	 Catch:{ Exception -> 0x0081 }
        r3 = r3.putInt(r4, r5);	 Catch:{ Exception -> 0x0081 }
        r3.commit();	 Catch:{ Exception -> 0x0081 }
    L_0x0042:
        r3 = "online_params";
        r3 = r2.has(r3);	 Catch:{ Exception -> 0x0094 }
        if (r3 == 0) goto L_0x006d;
    L_0x004a:
        r3 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0094 }
        r4 = "online_params";
        r2 = r2.getString(r4);	 Catch:{ Exception -> 0x0094 }
        r3.<init>(r2);	 Catch:{ Exception -> 0x0094 }
        r2 = r3.keys();	 Catch:{ Exception -> 0x0094 }
        r0 = r0.edit();	 Catch:{ Exception -> 0x0094 }
    L_0x005d:
        r4 = r2.hasNext();	 Catch:{ Exception -> 0x0094 }
        if (r4 != 0) goto L_0x0086;
    L_0x0063:
        r0.commit();	 Catch:{ Exception -> 0x0094 }
        r0 = "MobclickAgent";
        r2 = "get online setting params";
        android.util.Log.i(r0, r2);	 Catch:{ Exception -> 0x0094 }
    L_0x006d:
        monitor-exit(r1);	 Catch:{ all -> 0x0079 }
    L_0x006e:
        return;
    L_0x006f:
        r0 = move-exception;
        r0 = "MobclickAgent";
        r2 = "not json string";
        android.util.Log.i(r0, r2);	 Catch:{ all -> 0x0079 }
        monitor-exit(r1);	 Catch:{ all -> 0x0079 }
        goto L_0x006e;
    L_0x0079:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0079 }
        throw r0;
    L_0x007c:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0079 }
        goto L_0x0027;
    L_0x0081:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ all -> 0x0079 }
        goto L_0x0042;
    L_0x0086:
        r6 = r2.next();	 Catch:{ Exception -> 0x0094 }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x0094 }
        r4 = r3.getString(r6);	 Catch:{ Exception -> 0x0094 }
        r0.putString(r6, r4);	 Catch:{ Exception -> 0x0094 }
        goto L_0x005d;
    L_0x0094:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0079 }
        goto L_0x006d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.MobclickAgent.e(android.content.Context, java.lang.String):void");
    }

    public static void enterPage(Context context, String str) {
        onEvent(context, "_PAGE_", str);
    }

    private static void f(Context context) {
        context.deleteFile(j(context));
        context.deleteFile(k(context));
    }

    private synchronized void f(Context context, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UmengConstants.AtomKey_Type, "online_config");
            jSONObject.put(UmengConstants.AtomKey_AppKey, str);
            int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            String packageName = context.getPackageName();
            jSONObject.put("version_code", i);
            jSONObject.put("package", packageName);
            jSONObject.put(UmengConstants.AtomKey_SDK_Version, UmengConstants.SDK_VERSION);
            jSONObject.put("idmd5", l.c(context));
            jSONObject.put("channel", l.g(context));
            jSONObject.put("report_policy", m(context));
            jSONObject.put("last_config_time", n(context));
            this.d.post(new k(this, context, jSONObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /* access modifiers changed from: private */
    public void f(Context context, JSONObject jSONObject) {
        if (a("online_config", context)) {
            String a = a(context, jSONObject, UmengConstants.CONFIG_URL, true);
            Log.i(UmengConstants.LOG_TAG, "return message from " + a);
            if (a == null) {
                a = a(context, jSONObject, UmengConstants.CONFIG_URL_BACK, true);
            }
            if (a != null) {
                e(context, a);
            }
        }
    }

    public static void flush(Context context) {
        if (context == null) {
            try {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
            } catch (Exception e) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.flush(). ");
                e.printStackTrace();
                return;
            }
        }
        a.c(context);
    }

    private static SharedPreferences g(Context context) {
        return context.getSharedPreferences("mobclick_agent_header_" + context.getPackageName(), 0);
    }

    public static String getConfigParams(Context context, String str) {
        return l(context).getString(str, "");
    }

    public static JSONObject getUpdateInfo() {
        return f;
    }

    private static SharedPreferences h(Context context) {
        return context.getSharedPreferences("mobclick_agent_update_" + context.getPackageName(), 0);
    }

    private static SharedPreferences i(Context context) {
        return context.getSharedPreferences("mobclick_agent_state_" + context.getPackageName(), 0);
    }

    private static String j(Context context) {
        return "mobclick_agent_header_" + context.getPackageName();
    }

    private static String k(Context context) {
        return "mobclick_agent_cached_" + context.getPackageName();
    }

    private static SharedPreferences l(Context context) {
        return context.getSharedPreferences("mobclick_agent_online_setting_" + context.getPackageName(), 0);
    }

    private static int m(Context context) {
        return l(context).getInt("umeng_report_policy", b);
    }

    private static String n(Context context) {
        return l(context).getString("umeng_last_config_time", "");
    }

    public static void onError(Context context) {
        try {
            String b = l.b(context);
            if (b == null || b.length() == 0) {
                Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey");
            } else if (context == null) {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
            } else {
                new j(context, b, 2).start();
            }
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onError()");
            e.printStackTrace();
        }
    }

    public static void onEvent(Context context, String str) {
        onEvent(context, str, 1);
    }

    public static void onEvent(Context context, String str, int i) {
        onEvent(context, str, str, i);
    }

    public static void onEvent(Context context, String str, String str2) {
        onEvent(context, str, str2, 1);
    }

    public static void onEvent(Context context, String str, String str2, int i) {
        try {
            String b = l.b(context);
            if (b == null || b.length() == 0) {
                Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey");
            } else if (context == null) {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
            } else {
                if (str != null) {
                    if (str != "") {
                        if (str2 == null || str2 == "") {
                            Log.e(UmengConstants.LOG_TAG, "label is null or empty");
                            return;
                        } else if (i <= 0) {
                            Log.e(UmengConstants.LOG_TAG, "Illegal value of acc");
                            return;
                        } else {
                            new j(context, b, str, str2, i, 3).start();
                            return;
                        }
                    }
                }
                Log.e(UmengConstants.LOG_TAG, "tag is null or empty");
            }
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onEvent(). ");
            e.printStackTrace();
        }
    }

    public static void onPause(Context context) {
        if (context == null) {
            try {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
                return;
            } catch (Exception e) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onRause(). ");
                e.printStackTrace();
                return;
            }
        }
        new j(context, 0).start();
    }

    public static void onResume(Context context) {
        onResume(context, l.b(context), l.g(context));
    }

    public static void onResume(Context context, String str, String str2) {
        if (context == null) {
            try {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
            } catch (Exception e) {
                Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onResume(). ");
                e.printStackTrace();
            }
        } else if (str == null || str.length() == 0) {
            Log.e(UmengConstants.LOG_TAG, "unexpected empty appkey");
        } else {
            new j(context, str, str2, 1).start();
        }
    }

    public static void openActivityDurationTrack(boolean z) {
        UmengConstants.ACTIVITY_DURATION_OPEN = z;
    }

    public static void setDefaultReportPolicy(Context context, int i) {
        if (i < 0 || i > 5) {
            Log.e(UmengConstants.LOG_TAG, "Illegal value of report policy");
            return;
        }
        b = i;
        a(context, i);
    }

    public static void setOpenGLContext(GL10 gl10) {
        if (gl10 != null) {
            String[] a = l.a(gl10);
            if (a.length == 2) {
                GPU_VENDER = a[0];
                GPU_RENDERER = a[1];
            }
        }
    }

    public static void setUpdateListener(UmengUpdateListener umengUpdateListener) {
        e = umengUpdateListener;
    }

    public static void setUpdateOnlyWifi(boolean z) {
        mUpdateOnlyWifi = z;
    }

    public static void showUpdateDialog(Context context) {
        if (f != null && f.has("update") && f.optString("update").toLowerCase().equals("yes")) {
            File e = e(context, f);
            if (e == null || !UmengConstants.enableCacheInUpdate) {
                b(context, f).show();
            } else {
                b(context, e).show();
            }
        }
    }

    public static void update(Context context) {
        try {
            if (a.b()) {
                l.a(context);
            } else if (!mUpdateOnlyWifi || l.f(context)[0].equals("Wi-Fi")) {
                if (context == null) {
                    if (e != null) {
                        e.onUpdateReturned(UpdateStatus.No);
                    }
                    Log.e(UmengConstants.LOG_TAG, "unexpected null context");
                    return;
                }
                a.a(context, l.b(context));
            } else if (e != null) {
                e.onUpdateReturned(UpdateStatus.NoneWifi);
            }
        } catch (Exception e) {
            Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.update(). " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateOnlineConfig(Context context) {
        if (context == null) {
            try {
                Log.e(UmengConstants.LOG_TAG, "unexpected null context");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        a.f(context, l.b(context));
    }
}
