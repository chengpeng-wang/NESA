# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: sms_google.apk
- **packageName**: com.google.elements
- **targetSdk**: 19
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.google.elements.MainActivity
# Behavior Information:
## Activities:
- The malware first prevents the user from uninstalling itself using the Device Administration API, steals identifying information such as the device ID and IMEI, registers as a GCM client and to the C&C endpoint, waits for commands, and starts sending SMSs to premium numbers.
## Services:
- GCMIntentService executes commands sent by C&C server. 
# Detail Information:
## Activities: 2
	com.google.elements.MainActivity
	com.google.elements.AdminActivity
## Services: 3
	com.google.elements.WorkService
	com.google.elements.GCMIntentService
	com.google.elements.AdminService
## Receivers: 4
	com.google.elements.DeviceAdmin
	com.google.android.gcm.GCMBroadcastReceiver
	com.google.elements.BootReceiver
	com.google.elements.SmsReceiver
## Permissions: 12
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.GET_ACCOUNTS
	android.permission.CALL_PHONE
	com.google.elements.permission.C2D_MESSAGE
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.RECEIVE_BOOT_COMPLETED
	com.google.android.c2dm.permission.RECEIVE
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 38
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 8
	<org.json.JSONObject: int getInt(java.lang.String)>: 3
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 14
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.util.Date: long getTime()>: 6
	<java.lang.Class: java.lang.String getName()>: 3
	<org.json.JSONObject: org.json.JSONObject getJSONObject(java.lang.String)>: 4
	<android.os.Environment: java.io.File getExternalStoragePublicDirectory(java.lang.String)>: 2
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 3
	<android.content.Intent: java.lang.String getAction()>: 2
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 3
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 5
	<android.widget.ProgressBar: int getProgress()>: 2
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 8
	<java.io.File: boolean delete()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkCountryIso()>: 5
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 31
	<android.graphics.Color: int parseColor(java.lang.String)>: 2
	<org.json.JSONObject: boolean getBoolean(java.lang.String)>: 21
	<java.io.File: void <init>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.util.Calendar: java.util.Calendar getInstance()>: 4
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayOriginatingAddress()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<com.google.elements.Utils: java.lang.String getDeviceId()>: 9
	<org.json.JSONObject: org.json.JSONArray getJSONArray(java.lang.String)>: 4
	<org.json.JSONArray: org.json.JSONObject getJSONObject(int)>: 2
	<java.util.Calendar: int get(int)>: 4
## Sinks: 36
	<android.widget.TextView: void setTextColor(int)>: 2
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 6
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setPackage(java.lang.String)>: 3
	<java.io.FileOutputStream: void write(byte[],int,int)>: 1
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 6
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 5
	<android.content.Intent: android.content.Intent setFlags(int)>: 11
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.content.Intent: android.content.Intent setClassName(android.content.Context,java.lang.String)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 9
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 1
	<java.net.HttpURLConnection: void connect()>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 4
	<java.io.File: boolean delete()>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 4
	<android.graphics.Color: int parseColor(java.lang.String)>: 2
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 20
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 20
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.content.Context: void startActivity(android.content.Intent)>: 3
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 3
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<android.content.SharedPreferences$Editor: boolean commit()>: 27
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 11
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 1
