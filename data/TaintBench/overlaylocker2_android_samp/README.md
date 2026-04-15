# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: overlaylocker2_android_samp.apk
- **packageName**: brandmangroupe.miui.updater
- **targetSdk**: undefined
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: brandmangroupe.miui.updater.SampleOverlayShowActivity
# Behavior Information:
## BroadcastReceivers:
- IncominingCall monitors the incoming calls.  
- IncomingSms monitors the incoming SMSs.
## Services:
- GlobalCode uses WebView to allow malicious code (e.g. sending SMSs, making calls, collecting device information etc.) which are implemented in Java to be called from JavaScript. The malicious functionalities are implemented in the classes  MeSetting, MeSystem, MeFile, MePackage, MeContent and MeAction. It also sends collected data as GET parameters
in a HTTP request.

# Detail Information:
## Activities: 3
	brandmangroupe.miui.updater.MasterPage
	brandmangroupe.miui.updater.SampleOverlayShowActivity
	brandmangroupe.miui.updater.SampleOverlayHideActivity
## Services: 3
	brandmangroupe.miui.updater.MasterInterceptor
	brandmangroupe.miui.updater.OverlayService
	brandmangroupe.miui.updater.GlobalCode
## Receivers: 7
	brandmangroupe.miui.updater.MasterTimer
	brandmangroupe.miui.updater.IncomingSms
	brandmangroupe.miui.updater.SampleOverlayShowActivity$MyAdmin
	brandmangroupe.miui.updater.PowerConnectionReceiver
	brandmangroupe.miui.updater.MasterBoot
	brandmangroupe.miui.updater.IncomingCall
	brandmangroupe.miui.updater.NetworkChangeReceiver
## Permissions: 24
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_LOGS
	android.permission.READ_SMS
	android.permission.READ_SYNC_SETTINGS
	android.permission.CALL_PHONE
	android.permission.VIBRATE
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.READ_CALENDAR
	android.permission.READ_PROFILE
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	com.android.browser.permission.READ_HISTORY_BOOKMARKS
	com.android.alarm.permission.SET_ALARM
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.RESTART_PACKAGES
	android.permission.INTERNET
	android.permission.READ_CALL_LOG
	android.permission.ACCESS_NETWORK_STATE
	android.permission.GET_TASKS
	android.permission.READ_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 60
	<android.view.View: int getWidth()>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 1
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 7
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 2
	<android.view.View: void getLocationOnScreen(int[])>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 5
	<android.net.NetworkInfo: int getType()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>: 1
	<android.content.res.Resources: android.content.res.AssetManager getAssets()>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<java.io.FileInputStream: void <init>: 2
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 6
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 4
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 2
	<java.io.File: java.lang.String getName()>: 5
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 1
	<java.net.HttpURLConnection: java.lang.String getResponseMessage()>: 1
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 11
	<android.database.Cursor: java.lang.String getString(int)>: 2
	<java.util.Calendar: java.util.Calendar getInstance()>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<android.webkit.WebView: void loadData(java.lang.String,java.lang.String,java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 3
	<android.view.View: int getHeight()>: 1
	<java.net.URL: java.io.InputStream openStream()>: 1
	<android.telephony.TelephonyManager: int getSimState()>: 1
	<android.os.Environment: java.io.File getRootDirectory()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<android.os.Environment: java.io.File getDataDirectory()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 4
	<java.lang.Class: java.lang.String getName()>: 2
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 2
	<java.io.File: long getUsableSpace()>: 3
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 2
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 4
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 10
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 1
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 21
	<android.app.ActivityManager: java.util.List getRunningAppProcesses()>: 4
	<java.io.File: java.io.File[] listFiles()>: 2
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 1
	<android.os.Environment: java.lang.String getExternalStorageState()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimSerialNumber()>: 4
	<java.io.File: void <init>: 7
	<java.io.File: java.lang.String getAbsolutePath()>: 3
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.database.Cursor: int getInt(int)>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<android.content.ComponentName: java.lang.String getPackageName()>: 2
	<java.net.URLConnection: int getContentLength()>: 1
	<android.os.Environment: java.io.File getDownloadCacheDirectory()>: 1
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 3
## Sinks: 48
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,int)>: 2
	<android.webkit.WebSettings: void setSavePassword(boolean)>: 3
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 1
	<java.io.FileWriter: void write(java.lang.String)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 7
	<android.webkit.WebSettings: void setSaveFormData(boolean)>: 3
	<java.io.FileOutputStream: void write(byte[],int,int)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 2
	<java.lang.String: java.lang.String substring(int,int)>: 7
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 2
	<android.webkit.WebView: void addJavascriptInterface(java.lang.Object,java.lang.String)>: 18
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 2
	<java.io.DataOutputStream: void write(byte[],int,int)>: 2
	<java.io.DataOutputStream: void flush()>: 1
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 10
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 9
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,double)>: 2
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,long)>: 6
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<android.webkit.WebView: void setLayoutParams(android.view.ViewGroup$LayoutParams)>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,boolean)>: 14
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 30
	<android.webkit.WebView: void setBackgroundColor(int)>: 2
	<java.net.URL: void <init>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 2
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 6
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 5
	<java.util.Calendar: void setTimeInMillis(long)>: 2
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 62
	<android.webkit.WebSettings: void setAllowFileAccess(boolean)>: 3
	<java.io.FileOutputStream: void <init>: 1
	<android.content.Context: void startActivity(android.content.Intent)>: 3
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 2
	<android.content.SharedPreferences$Editor: boolean commit()>: 4
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 12
	<android.webkit.WebSettings: void setUserAgentString(java.lang.String)>: 3
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 8
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1

