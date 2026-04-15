# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: proxy_samp.apk
- **packageName**: com.smart.studio.proxy
- **targetSdk**: 15
- **minSdk**: 9
- **maxSdk**: -1
- **mainActivity**: UNKNOWN
# Behavior Information:
## BroadcastReceivers:
- SMSReceiver monitors SMS messages.
## Services:
- ProxyService steals user personal data like messages, call log history, location, Wi-Fi (including SSID) and mobile data enabled/disabled status, IMEI number even your account user names. These data are stored to text file with malware information logs (time, current action, exceptions, server response code…) on primary external storage directory. Gathered information is then sent to remote server each 30 minutes
# Detail Information:
## Activities: 1
	.MainActivity
## Services: 1
	.ProxyService
## Receivers: 4
	.LocationReceiver
	.ProxyReceiver
	.SMSReceiver
	.AlarmReceiver
## Permissions: 14
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.DISABLE_KEYGUARD
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.GET_ACCOUNTS
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_COARSE_LOCATION
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.CHANGE_NETWORK_STATE
	android.permission.ACCESS_WIFI_STATE
	android.permission.READ_PHONE_STATE
## Sources: 49
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 1
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 4
	<android.telephony.SmsMessage: long getTimestampMillis()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 4
	<android.net.wifi.WifiManager: android.net.wifi.WifiInfo getConnectionInfo()>: 1
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>: 1
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 1
	<android.telephony.gsm.GsmCellLocation: int getLac()>: 1
	<android.accounts.AccountManager: android.accounts.Account[] getAccounts()>: 1
	<java.util.Calendar: long getTimeInMillis()>: 5
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 2
	<android.telephony.TelephonyManager: android.telephony.CellLocation getCellLocation()>: 1
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 9
	<android.app.KeyguardManager: android.app.KeyguardManager$KeyguardLock newKeyguardLock(java.lang.String)>: 1
	<android.accounts.AccountManager: android.accounts.AccountManager get(android.content.Context)>: 1
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 8
	<android.database.Cursor: java.lang.String getString(int)>: 6
	<java.util.Calendar: java.util.Calendar getInstance()>: 6
	<android.telephony.SmsMessage: java.lang.String getDisplayOriginatingAddress()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.telephony.gsm.GsmCellLocation: int getCid()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 4
	<android.net.NetworkInfo: java.lang.String getSubtypeName()>: 1
	<android.net.NetworkInfo: android.net.NetworkInfo$State getState()>: 3
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 1
	<java.lang.Class: java.lang.String getName()>: 2
	<android.os.Bundle: android.os.Bundle getBundle(java.lang.String)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 3
	<android.content.Intent: java.lang.String getAction()>: 9
	<java.util.Calendar: java.util.Date getTime()>: 1
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<android.database.Cursor: long getLong(int)>: 5
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 1
	<android.net.wifi.WifiInfo: java.lang.String getSSID()>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 3
	<java.io.File: boolean delete()>: 1
	<java.io.File: void <init>: 3
	<java.io.File: java.lang.String getAbsolutePath()>: 2
	<android.content.Intent: android.os.Parcelable getParcelableExtra(java.lang.String)>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 1
	<android.database.Cursor: int getInt(int)>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
## Sinks: 21
	<java.io.File: boolean delete()>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,int)>: 4
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,boolean)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Bundle)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 7
	<java.lang.StringBuffer: void setLength(int)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 4
	<java.io.BufferedWriter: void write(java.lang.String)>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 4
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 29
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 7
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 5
	<android.content.SharedPreferences$Editor: boolean commit()>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 2
