# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: smssend_packageInstaller.apk
- **packageName**: install.app
- **targetSdk**: 15
- **minSdk**: 4
- **maxSdk**: unfined
- **mainActivity**: .MainActivity
# Behavior Information:
## Activities:
- MainActivity collects device information such as IMEI, phone number etc. 
## BroadcastReceivers:
- MainReceiver minitors SMS messages. When a certain SMS is received, it sends another SMS.
# Detail Information:
## Activities: 1
	.MainActivity
## Services: 2
	.MainService
	ru.beta.MainService
## Receivers: 2
	ru.beta.MainReceiver
	.MainReceiver
## Permissions: 19
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.DISABLE_KEYGUARD
	android.permission.READ_CONTACTS
	android.permission.READ_LOGS
	android.permission.CALL_PHONE
	android.permission.CALL_PRIVILEGED
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.INSTALL_PACKAGES
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.RESTART_PACKAGES
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.GET_TASKS
	android.permission.READ_PHONE_STATE
	android.permission.DELETE_PACKAGES
	com.android.launcher.permission.INSTALL_SHORTCUT
## Sources: 53
	<org.json.JSONObject: int getInt(java.lang.String)>: 10
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<org.json.JSONArray: java.lang.String getString(int)>: 4
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 6
	<org.json.JSONObject: org.json.JSONObject getJSONObject(java.lang.String)>: 4
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 3
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 4
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 2
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 41
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 1
	<org.json.JSONObject: boolean getBoolean(java.lang.String)>: 9
	<java.lang.Class: java.io.InputStream getResourceAsStream(java.lang.String)>: 5
	<android.database.Cursor: java.lang.String getString(int)>: 1
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<java.lang.String: byte[] getBytes()>: 2
	<java.lang.Class: java.lang.Class[] getClasses()>: 3
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 3
	<android.content.Intent: android.os.Bundle getExtras()>: 9
	<org.json.JSONObject: long getLong(java.lang.String)>: 2
	<java.security.MessageDigest: byte[] digest()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSubscriberId()>: 3
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 3
	<java.lang.String: byte[] getBytes(java.lang.String)>: 3
	<java.io.BufferedReader: java.lang.String readLine()>: 5
	<org.apache.http.conn.scheme.PlainSocketFactory: org.apache.http.conn.scheme.PlainSocketFactory getSocketFactory()>: 1
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 2
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 2
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 5
	<java.net.HttpURLConnection: int getResponseCode()>: 6
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 1
	<android.content.Intent: java.lang.String getAction()>: 2
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 2
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 2
	<java.security.KeyStore: void load(java.io.InputStream,char[])>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 13
	<java.lang.Class: java.lang.String getCanonicalName()>: 3
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLSocketFactory getSocketFactory()>: 2
	<org.apache.http.params.HttpConnectionParams: int getSoTimeout(org.apache.http.params.HttpParams)>: 1
	<java.io.File: void <init>: 3
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>: 4
	<java.lang.Long: long parseLong(java.lang.String)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 1
	<java.util.Vector: java.lang.Object get(int)>: 22
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 10
	<java.security.KeyStore: java.security.KeyStore getInstance(java.lang.String)>: 1
	<org.apache.http.params.HttpConnectionParams: int getConnectionTimeout(org.apache.http.params.HttpParams)>: 1
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 1
	<org.json.JSONObject: org.json.JSONArray getJSONArray(java.lang.String)>: 11
	<org.json.JSONArray: org.json.JSONObject getJSONObject(int)>: 11
## Sinks: 51
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,int)>: 10
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 2
	<java.lang.StringBuffer: void setLength(int)>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 6
	<java.io.FileOutputStream: void write(byte[],int,int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 4
	<android.webkit.WebView: void setScrollBarStyle(int)>: 1
	<java.util.Vector: java.lang.Object set(int,java.lang.Object)>: 1
	<android.webkit.WebView: void addJavascriptInterface(java.lang.Object,java.lang.String)>: 1
	<org.apache.http.params.HttpProtocolParams: void setHttpElementCharset(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 4
	<java.io.DataOutputStream: void write(byte[])>: 3
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<android.webkit.WebView: void setLayoutParams(android.view.ViewGroup$LayoutParams)>: 1
	<java.net.URL: void <init>: 5
	<java.net.URL: java.net.URLConnection openConnection()>: 7
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 1
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 27
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 31
	<android.content.Context: void startActivity(android.content.Intent)>: 8
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<org.apache.http.params.HttpProtocolParams: void setContentCharset(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 3
	<java.lang.String: boolean startsWith(java.lang.String)>: 6
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 1
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 2
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 5
	<org.json.JSONObject: java.lang.String toString(int)>: 3
	<org.apache.http.conn.ssl.SSLSocketFactory: void setHostnameVerifier(org.apache.http.conn.ssl.X509HostnameVerifier)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,boolean)>: 1
	<android.content.Intent: android.content.Intent setFlags(int)>: 2
	<java.io.DataOutputStream: void flush()>: 2
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 4
	<java.net.HttpURLConnection: void connect()>: 3
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,long)>: 5
	<android.webkit.WebView: void setWebChromeClient(android.webkit.WebChromeClient)>: 1
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 2
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 11
	<android.webkit.WebView: void setWebViewClient(android.webkit.WebViewClient)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<java.io.FileOutputStream: void <init>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 4
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 2
	<android.content.Intent: android.content.Intent putExtras(android.os.Bundle)>: 4
	<org.apache.http.conn.scheme.SchemeRegistry: org.apache.http.conn.scheme.Scheme register(org.apache.http.conn.scheme.Scheme)>: 2
	<android.app.ProgressDialog: void setProgressStyle(int)>: 2
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 3

