package com.android.service.view;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import com.android.service.XmppService;
import com.xmpp.client.util.HttpClinentModle;
import com.xmpp.client.util.PushDialogModle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.json.JSONException;
import org.json.JSONObject;

public class MapDataBean {
    List<String> ID_list;
    List<String> beginString_list;
    private Context ctx;
    List<String> description_list;
    List<String> endString_list;
    List<String> eventLocation_list;
    private HttpClinentModle hcm = new HttpClinentModle(this.ctx);
    List<String> items_list;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);
    private String strAddress;
    private String strSelect_Name;
    private String strSelect_Pairnum;
    List<String> title_list;

    public MapDataBean(Context context, String Select_Name, String Select_Pairnum) {
        this.ctx = context;
        this.strSelect_Name = Select_Name;
        this.strSelect_Pairnum = Select_Pairnum;
    }

    public void UpdataJason(String strHostname, String type) {
        JsonParser(this.hcm.HttpClinentJason("http://" + strHostname + "/API/GetGPS.ashx?phonenum=" + this.strSelect_Pairnum), type);
    }

    public String UpdataJasonTime(String strHostname) {
        String urlAPI = "http://" + strHostname + "/API/GetGPS.ashx?phonenum=" + this.strSelect_Pairnum;
        System.out.println("urlAPI: " + urlAPI);
        String strJason = this.hcm.HttpClinentJason(urlAPI);
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                return json.getString("AddTime");
            }
            this.pdm.PushAlertSingleButton("伺服器訊息", json.getString("reason"));
            return null;
        } catch (JSONException e) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e)).toString());
            return null;
        } catch (Exception e2) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e2)).toString());
            return null;
        }
    }

    private void getGooglelocation2(String Lat, String Lon) {
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + Lat + "," + Lon + "&sensor=true&language=zh-TW")).getEntity()));
            if (json.getString("status").equalsIgnoreCase("OK")) {
                JSONObject jsonResults = json.getJSONArray("results").getJSONObject(0);
                Log.e("strAddress--------------->", jsonResults.toString());
                this.strAddress = jsonResults.getString("formatted_address");
                return;
            }
            new Builder(this.ctx).setTitle("訊息").setMessage("請使用百度地圖!!").setPositiveButton("確定", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } catch (ParseException e) {
            this.pdm.PushAlertSingleButton("系統出錯", Log.getStackTraceString(e));
        } catch (IOException e2) {
            this.pdm.PushAlertSingleButton("系統出錯", Log.getStackTraceString(e2));
        } catch (JSONException e3) {
            this.pdm.PushAlertSingleButton("系統出錯", Log.getStackTraceString(e3));
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    private boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        List<String> packageNames = new ArrayList();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                packageNames.add(((PackageInfo) packageInfos.get(i)).packageName);
            }
        }
        return packageNames.contains(packageName);
    }

    private void JsonParser(String strJason, String type) {
        System.out.println("==========[strJason]==========" + strJason);
        String smssub1 = null;
        String smssub2 = null;
        try {
            JSONObject json = new JSONObject(strJason);
            if (json.getString("success").equalsIgnoreCase("true")) {
                String strtime = json.getString("AddTime");
                StringTokenizer st2 = new StringTokenizer(json.getJSONArray(Data.ELEMENT_NAME).getJSONObject(0).getString("str"), XmppService.strCheckTag);
                int n = 0;
                while (st2.hasMoreElements()) {
                    if (n == 0) {
                        smssub1 = st2.nextToken();
                    } else if (n == 1) {
                        smssub2 = st2.nextToken();
                    } else {
                        st2.nextToken();
                    }
                    n++;
                }
                System.out.println("smssub1:" + smssub1);
                System.out.println("smssub2:" + smssub2);
                System.out.println("strSelect_Name" + this.strSelect_Name);
                if (type.equalsIgnoreCase("google")) {
                    if (this.strSelect_Name.equalsIgnoreCase("googlemap")) {
                        this.ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://maps.google.com/maps?q=" + smssub2 + "," + smssub1)));
                        ((Activity) this.ctx).finish();
                        return;
                    }
                    this.ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("google.streetview:cbll=" + smssub2 + "," + smssub1 + "&cbp=1,99.56,,1,-5.27&mz=21")));
                    ((Activity) this.ctx).finish();
                    return;
                } else if (type.equalsIgnoreCase("baidu")) {
                    if (isAvilible(this.ctx, "com.baidu.BaiduMap")) {
                        try {
                            this.ctx.startActivity(Intent.getIntent("intent://map/marker?location=" + smssub1 + "," + smssub2 + "&" + "title=他的位置" + "&content=&" + "&src=Monkey|MonkeyDIY#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"));
                            ((Activity) this.ctx).finish();
                            return;
                        } catch (URISyntaxException e) {
                            Log.e("intent", e.getMessage());
                            return;
                        }
                    }
                    this.ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.baidu.BaiduMap")));
                    ((Activity) this.ctx).finish();
                    return;
                } else {
                    return;
                }
            }
            this.pdm.PushAlertSingleButton("伺服器訊息", json.getString("reason"));
        } catch (JSONException e2) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e2)).toString());
        } catch (Exception e3) {
            this.pdm.PushAlertSingleButton("系統出錯", new StringBuilder(String.valueOf(strJason)).append(this.strSelect_Pairnum).append(":").append(Log.getStackTraceString(e3)).toString());
        }
    }
}
