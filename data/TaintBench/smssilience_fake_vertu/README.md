# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: smssilience_fake_vertu.apk
- **packageName**: com.vertu.jp
- **targetSdk**: undefined
- **minSdk**: 9
- **maxSdk**: undefined
- **mainActivity**: com.vertu.jp.MainActivity
# Behavior Information:
## BroadcastReceivers:
- catchsms2 monitors incoming SMS messages and sends them to a remote server. 
# Detail Information:
## Activities: 1
	com.vertu.jp.MainActivity
## Receivers: 1
	com.vertu.jp.catchsms2
## Permissions: 5
	android.permission.RECEIVE_SMS
	android.permission.READ_SMS
	android.permission.INTERNET
	android.permission.READ_PHONE_STATE
	android.permission.DELETE_PACKAGES
## Sources: 11
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 3
	<android.app.AlertDialog$Builder: android.app.AlertDialog show()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 4
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 5
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 3
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 2
## Sinks: 13
	<java.net.URL: void <init>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<android.view.Window: void setFlags(int,int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 3
	<java.io.OutputStreamWriter: void <init>: 2
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<java.io.PrintWriter: void write(java.lang.String)>: 2
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 2
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 2
