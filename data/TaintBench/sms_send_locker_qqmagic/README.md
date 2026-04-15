# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: sms_send_locker_qqmagic.apk
- **packageName**: qqkj.qqmagic
- **targetSdk**: 21
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: .MainActivity
# Behavior Information:
## BroadcastReceivers:
- Fr monitors incoming SMSs and sends SMSs to encrypted phone numbers.
# Detail Information:
## Activities: 1
	.MainActivity
## Services: 2
	b
	s
## Receivers: 2
	r
	Fr
## Permissions: 6
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.RECEIVE_BOOT_COMPLETED
## Sources: 20
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 2
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 4
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 1
	<java.lang.String: byte[] getBytes()>: 6
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 2
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 20
	<android.telephony.SmsMessage: java.lang.String getDisplayOriginatingAddress()>: 6
	<android.content.Intent: java.lang.String getAction()>: 2
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 4
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 4
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.telephony.gsm.SmsManager: android.telephony.gsm.SmsManager getDefault()>: 4
	<java.lang.Throwable: java.lang.String getMessage()>: 6
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 2
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 4
	<android.widget.EditText: android.text.Editable getText()>: 2
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 2
## Sinks: 14
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 10
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 2
	<android.content.Intent: android.content.Intent setPackage(java.lang.String)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String[])>: 1
	<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 6
	<java.lang.String: java.lang.String substring(int,int)>: 8
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 4
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<android.content.SharedPreferences$Editor: boolean commit()>: 4
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 2
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 6
