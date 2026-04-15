# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: fakeplay.apk
- **packageName**: com.googleprojects.mmsp
- **targetSdk**: 8
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.googleprojects.mm.SPMainActivity
# Behavior Information:
## Activities:
- The malware runs in the background, gathering SMS activity and periodically send it to a proxy email address.
It monitors incoming SMS messages and sends them to the following proxy email addresses.
## Services:
- SOMailPoolService sends collected data in email.  
# Detail Information:
## Activities: 1
	com.googleprojects.mm.SPMainActivity
## Services: 3
	com.googleprojects.mm.SOMailPoolService
	GCMIntentService
	com.googleprojects.mm.JHService
## Receivers: 3
	com.googleprojects.mm.GJBootReceiver
	com.google.android.gcm.GCMBroadcastReceiver
	com.googleprojects.mm.DevAdReceiver
## Permissions: 12
	android.permission.RECEIVE_SMS
	android.permission.READ_SMS
	android.permission.GET_ACCOUNTS
	android.permission.CHANGE_WIFI_STATE
	android.permission.INTERNET
	android.permission.WAKE_LOCK
	android.permission.ACCESS_WIFI_STATE
	android.permission.RECEIVE_MMS
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.READ_PHONE_STATE
	com.google.android.c2dm.permission.RECEIVE
	com.googleprojects.mmsp.permission.C2D_MESSAGE
## Sources: 43
	<java.lang.System: java.lang.String getProperty(java.lang.String)>: 1
	<java.lang.Float: float parseFloat(java.lang.String)>: 2
	<java.lang.String: byte[] getBytes(java.lang.String)>: 3
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 6
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 2
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 2
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 5
	<java.lang.Class: java.lang.String getName()>: 9
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 3
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 3
	<android.content.Intent: java.lang.String getAction()>: 3
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 4
	<java.lang.Thread: java.lang.ClassLoader getContextClassLoader()>: 1
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 1
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 3
	<java.lang.ClassLoader: java.lang.ClassLoader getSystemClassLoader()>: 3
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 4
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 3
	<java.util.ResourceBundle: java.util.ResourceBundle getBundle(java.lang.String,java.util.Locale,java.lang.ClassLoader)>: 2
	<java.util.Calendar: long getTimeInMillis()>: 4
	<java.lang.String: void getChars(int,int,char[],int)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 7
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getService(android.content.Context,int,android.content.Intent,int)>: 4
	<java.util.Locale: java.util.Locale getDefault()>: 1
	<java.lang.String: byte[] getBytes()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimOperatorName()>: 10
	<java.util.Calendar: java.util.Calendar getInstance()>: 2
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 5
	<android.content.Intent: android.os.Bundle getExtras()>: 3
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 2
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 5
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<android.database.Cursor: int getInt(int)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<java.util.ResourceBundle: java.lang.String getString(java.lang.String)>: 2
	<java.util.Hashtable: java.lang.Object get(java.lang.Object)>: 3
	<java.lang.ClassLoader: java.lang.Class loadClass(java.lang.String)>: 6
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimOperator()>: 10
	<android.net.wifi.WifiManager: int getWifiState()>: 3
	<java.io.ObjectInputStream: java.lang.Object readObject()>: 1
## Sinks: 34
	<java.lang.Float: float parseFloat(java.lang.String)>: 2
	<java.lang.String: boolean startsWith(java.lang.String)>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 5
	<android.content.Intent: android.content.Intent setPackage(java.lang.String)>: 3
	<android.webkit.WebSettings: void setAppCacheEnabled(boolean)>: 1
	<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>: 3
	<java.lang.String: java.lang.String substring(int,int)>: 10
	<android.webkit.WebSettings: void setCacheMode(int)>: 1
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 3
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 6
	<java.io.ObjectOutputStream: void writeObject(java.lang.Object)>: 1
	<android.content.Intent: android.content.Intent setClassName(android.content.Context,java.lang.String)>: 1
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 4
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 3
	<android.content.UriMatcher: void addURI(java.lang.String,java.lang.String,int)>: 4
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 13
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 3
	<android.webkit.WebSettings: void setRenderPriority(android.webkit.WebSettings$RenderPriority)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 3
	<java.net.URL: void <init>: 2
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 1
	<android.webkit.WebSettings: void setBuiltInZoomControls(boolean)>: 1
	<android.content.IntentFilter: void setPriority(int)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 20
	<android.webkit.WebSettings: void setAllowFileAccess(boolean)>: 1
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 4
	<android.content.ContentResolver: void notifyChange(android.net.Uri,android.database.ContentObserver)>: 6
	<android.net.wifi.WifiManager: boolean setWifiEnabled(boolean)>: 6
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 5
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 11
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 6

