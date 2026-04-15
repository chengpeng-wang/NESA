# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: backflash.apk
- **packageName**: com.adobe.flash
- **targetSdk**: 17
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.adobe.flashplayer_.FlashARM
# Behavior Information:
## Activities:
- Once executed, the malwere requests Device Administrator privileges.
If there is an attempt to disable Device Administrator privileges by accessing the device settings, the Trojan will restart device settings every 10
milliseconds. Next, it opens a back door on the device and connects to the following command-and-control (C&C) server.
## Services:
- AdobeFlashCore gathers data on the device and sends it to a remote server. 
# Detail Information:
## Activities: 4
	com.adobe.packages.BK
	com.adobe.packages.ZLocker
	com.adobe.flashplayer.Certificate
	com.adobe.flashplayer_.FlashARM
## Services: 4
	com.adobe.flashplayer_.AdobeZCore
	com.adobe.flashplayer_.AdobeFlashCore
	com.adobe.flashplayer_.AdobeUtil
	com.adobe.flashplayer_.MacrosStat
## Receivers: 7
	com.adobe.flashplayer_.FlashURL
	com.adobe.flashplayer_.FlashW
	com.adobe.flashplayer_.ADOBEcoreZa
	com.adobe.flashplayer_.SystemSWF
	com.adobe.flashplayer_.FlashZ
	com.adobe.flashplayer_.adobeCore
	com.adobe.flashplayer_.FlashY
## Permissions: 27
	android.permission.READ_CONTACTS
	android.permission.READ_LOGS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.BROADCAST_PACKAGE_INSTALL
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	com.android.browser.permission.READ_HISTORY_BOOKMARKS
	android.permission.ACCESS_NETWORK_STATE
	android.permission.PROCESS_OUTGOING_CALLS
	android.permission.WAKE_LOCK
	android.permission.ACCESS_WIFI_STATE
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.DISABLE_KEYGUARD
	android.permission.BROADCAST_PACKAGE_REPLACED
	android.permission.RECORD_AUDIO
	android.permission.BROADCAST_PACKAGE_ADDED
	android.permission.RESTART_PACKAGES
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.CHANGE_WIFI_STATE
	android.permission.INTERNET
	android.permission.GET_TASKS
	android.permission.CHANGE_NETWORK_STATE
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_SETTINGS
## Sources: 53
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 15
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 11
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 6
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.content.ComponentName: java.lang.String getClassName()>: 3
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 4
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 1
	<android.telephony.SmsManager: java.util.ArrayList divideMessage(java.lang.String)>: 5
	<java.io.FileInputStream: void <init>: 1
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 1
	<android.os.AsyncTask: java.lang.Object get()>: 4
	<android.database.Cursor: java.lang.String getString(int)>: 26
	<java.util.Calendar: java.util.Calendar getInstance()>: 3
	<java.lang.String: byte[] getBytes()>: 1
	<android.webkit.WebSettings: void setLoadWithOverviewMode(boolean)>: 1
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 10
	<java.io.File: java.lang.String getPath()>: 2
	<android.webkit.WebView: void loadData(java.lang.String,java.lang.String,java.lang.String)>: 3
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 5
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<android.content.Intent: android.net.Uri getData()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 11
	<java.io.BufferedReader: java.lang.String readLine()>: 26
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 1
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 11
	<java.lang.Class: java.lang.String getName()>: 2
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 3
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 4
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<android.content.Intent: java.lang.String getAction()>: 3
	<java.util.Calendar: java.util.Date getTime()>: 3
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<android.database.Cursor: long getLong(int)>: 6
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 1
	<android.app.ActivityManager: android.os.Debug$MemoryInfo[] getProcessMemoryInfo(int[])>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 2
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 29
	<android.app.ActivityManager: java.util.List getRunningAppProcesses()>: 1
	<java.io.File: java.io.File[] listFiles()>: 1
	<android.os.Environment: java.lang.String getExternalStorageState()>: 2
	<java.io.DataInputStream: java.lang.String readLine()>: 2
	<java.io.File: void <init>: 3
	<java.io.File: java.lang.String getAbsolutePath()>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 3
## Sinks: 35
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 6
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.view.Window: void setFlags(int,int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<java.io.OutputStreamWriter: void <init>: 17
	<android.content.Intent: android.content.Intent setFlags(int)>: 14
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 1
	<java.io.DataOutputStream: void write(byte[],int,int)>: 2
	<java.io.DataOutputStream: void flush()>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 1
	<java.io.OutputStreamWriter: void write(java.lang.String)>: 17
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 2
	<android.view.View: void setVisibility(int)>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 2
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 1
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 5
	<android.webkit.WebSettings: void setBuiltInZoomControls(boolean)>: 1
	<android.webkit.WebView: void setWebViewClient(android.webkit.WebViewClient)>: 1
	<java.util.Calendar: void setTimeInMillis(long)>: 3
	<android.webkit.WebSettings: void setLoadWithOverviewMode(boolean)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.content.Context: void startActivity(android.content.Intent)>: 3
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.net.wifi.WifiManager: boolean setWifiEnabled(boolean)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 4
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<android.telephony.SmsManager: void sendMultipartTextMessage(java.lang.String,java.lang.String,java.util.ArrayList,java.util.ArrayList,java.util.ArrayList)>: 5
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 2
