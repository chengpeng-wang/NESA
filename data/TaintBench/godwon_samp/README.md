# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: godwon_samp.apk
- **packageName**: android.sms.core
- **targetSdk**: 17
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: .MainActivity
# Behavior Information:
## BroadcastReceivers:
- BootReciever minitors incoming SMS messages and sends them to a URL http://www.gogledown.com/vipboss/saves.php.
## Services:
- GoogleService sends phone number to a URL http://www.gogledown.com/vipboss/saves.php.  
# Detail Information:
## Activities: 1
	.MainActivity
## Services: 1
	.GoogleService
## Receivers: 1
	.BootReceiver
## Permissions: 6
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.READ_PHONE_STATE
## Sources: 10
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 2
	<android.content.Intent: java.io.Serializable getSerializableExtra(java.lang.String)>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<android.content.Intent: java.lang.String getAction()>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 2
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 1
## Sinks: 8
	<org.apache.http.client.methods.HttpEntityEnclosingRequestBase: void setEntity(org.apache.http.HttpEntity)>: 1
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 10
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
