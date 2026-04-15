# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: samsapo.apk
- **packageName**: com.android.tools.system
- **targetSdk**: 17
- **minSdk**: 5
- **maxSdk**: 10
- **mainActivity**: .SplashScreen
# Behavior Information:
## Activities:
- when running on an Android device, it will send an SMS message
with text “Это твои фото?” (which is Russian for “Is this your photo?”) and a link
to the malicious APK package to all of the user’s contacts. It also uploads SMS messages and phone numbers to a remote server. 
## BroadcastReceivers:
- PhoneReceiver blocks phone calls from user's contact. 
- SmsReceiver monitors incoming SMS messages and send them to a URL http://oopsspoo.ru/index.php 

# Detail Information:
## Activities: 1
	.SplashScreen
## Services: 1
	.EternalService
## Receivers: 4
	.SmsReceiver
	.EternalService$Alarm
	.PhoneReceiver
	.OnBootReceiver
## Permissions: 16
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.WRITE_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.MODIFY_PHONE_STATE
	android.permission.WRITE_CALL_LOG
	android.permission.INSTALL_PACKAGES
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_CALL_LOG
	android.permission.INTERNET
	android.permission.READ_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
	android.permission.DELETE_PACKAGES
## Sources: 35
	<java.io.BufferedReader: java.lang.String readLine()>: 4
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 3
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 4
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 1
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 1
	<java.lang.Class: java.lang.String getName()>: 3
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 4
	<android.content.ComponentName: java.lang.String getClassName()>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<android.content.Intent: java.lang.String getAction()>: 1
	<android.telephony.gsm.SmsMessage: android.telephony.gsm.SmsMessage createFromPdu(byte[])>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 2
	<java.lang.Throwable: java.lang.String getMessage()>: 5
	<android.telephony.gsm.SmsManager: android.telephony.gsm.SmsManager getDefault()>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 1
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 1
	<java.util.ArrayList: java.lang.Object get(int)>: 4
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 5
	<java.io.File: boolean delete()>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 5
	<java.io.File: java.lang.String getAbsolutePath()>: 2
	<java.io.File: void <init>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.net.ConnectivityManager: android.net.NetworkInfo[] getAllNetworkInfo()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<android.database.Cursor: int getInt(int)>: 2
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 4
	<android.telephony.gsm.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.telephony.gsm.SmsMessage: java.lang.String getMessageBody()>: 1
## Sinks: 30
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 1
	<android.widget.ProgressBar: void setMax(int)>: 1
	<android.content.Intent: android.content.Intent setPackage(java.lang.String)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 4
	<java.io.FileOutputStream: void write(byte[],int,int)>: 4
	<android.content.Intent: android.content.Intent setFlags(int)>: 1
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 2
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<java.net.HttpURLConnection: void connect()>: 2
	<java.io.File: boolean delete()>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<java.net.URL: void <init>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 2
	<android.widget.ProgressBar: void setProgress(int)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String[])>: 1
	<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<java.io.FileOutputStream: void <init>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 1
	<com.android.tools.system.MyPostRequest: android.os.AsyncTask execute(java.lang.Object[])>: 2
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 3
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.os.AsyncTask: void onPostExecute(java.lang.Object)>: 5
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 6
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 7
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 2
