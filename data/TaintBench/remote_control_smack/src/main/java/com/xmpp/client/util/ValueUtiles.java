package com.xmpp.client.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ValueUtiles extends Activity {
    public static int ControlCheck;
    private static boolean booldebug = false;
    public static String strStoreFolder = "/MonkeyData/";
    public String role = "";
    public SharedPreferences rolesp = null;

    public static String UserVersion(String hostname, String mainnum, String psw) {
        String TAG_success = "success";
        String TAG_errmsg = "errmsg";
        String strVersion = "";
        try {
            JSONObject json = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + hostname + ":8080/MonkeyDIYWeb/user_verification?phoneNum=" + mainnum + "&StartPSW=" + psw)).getEntity()));
            String strSuccess = json.getString("success");
            return json.getString("releasetype");
        } catch (JSONException e) {
            e.printStackTrace();
            return strVersion;
        } catch (IOException e2) {
            e2.printStackTrace();
            return strVersion;
        } catch (Exception e3) {
            e3.printStackTrace();
            return strVersion;
        }
    }

    public static Boolean UploadFileForServer(String strFullPath, String strurl) {
        boolean boolResult;
        Exception e;
        System.out.println("--------UploadFileForServer-------" + strurl);
        String httpUrl = strurl;
        byte[] buf = new byte[1024];
        try {
            File fileUploadObj = new File(strFullPath);
            if (fileUploadObj.exists()) {
                InputStream in = new FileInputStream(fileUploadObj);
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(httpUrl).openConnection();
                    con.setConnectTimeout(20000);
                    con.setReadTimeout(12000);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream osw = con.getOutputStream();
                    while (in.read(buf) != -1) {
                        osw.write(buf);
                    }
                    osw.flush();
                    osw.close();
                    in.close();
                    StringBuilder buffer = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StringEncodings.UTF8));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        buffer.append(line);
                    }
                    Log.e("context:", buffer.toString().trim());
                    boolResult = true;
                } catch (Exception e2) {
                    e = e2;
                    InputStream inputStream = in;
                    e.printStackTrace();
                    boolResult = false;
                    return Boolean.valueOf(boolResult);
                }
            }
            boolResult = false;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            boolResult = false;
            return Boolean.valueOf(boolResult);
        }
        return Boolean.valueOf(boolResult);
    }

    public static String getDataPath(Context contextObj) {
        String strDataPath = "";
        try {
            strDataPath = contextObj.getFilesDir() + strStoreFolder;
            System.out.println("-------strDataPath--------" + strDataPath);
            File path = new File(strDataPath);
            if (!path.exists()) {
                path.mkdir();
            }
            if (strDataPath == null || strDataPath.length() <= 0) {
                return strDataPath;
            }
            new AppPreferences(contextObj).saveDataPath(strDataPath);
            return strDataPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showlog(String LogText) {
        if (booldebug) {
            try {
                FileWriter fw = new FileWriter(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/LogText.txt").toString(), true);
                fw.append(new StringBuilder(String.valueOf(LogText)).append("\r\n").toString());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
