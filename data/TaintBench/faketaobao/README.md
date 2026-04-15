# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: faketaobao.apk
- **packageName**: com.tao.bao
- **targetSdk**: 17
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: .SpashActivity
# Behavior Information:
## Activities:
- The malware generally arrives within a repackaged .apk file from a legitimate application. The package name, publisher, and other details willvary and may be taken directly from the original application.
It appears to be the legitimate "taobao" application for Android and has the same login activity as the legitimate version. It sends the following information to a specific phone number: Taobao user name, Taobao password, Alipay user name and Alipay password.
# Detail Information:
## Activities: 3
	.LocationVerify
	.MainActivity
	.SpashActivity
## Permissions: 3
	android.permission.INTERNET
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 12
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 2
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<android.app.AlertDialog$Builder: android.app.AlertDialog show()>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 2
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 1
	<java.io.File: void <init>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.io.File: java.lang.String getPath()>: 1
	<android.widget.EditText: android.text.Editable getText()>: 4
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
## Sinks: 19
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<org.apache.http.client.methods.HttpEntityEnclosingRequestBase: void setEntity(org.apache.http.HttpEntity)>: 1
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 3
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.io.FileOutputStream: void write(byte[])>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 1
	<android.content.Intent: android.content.Intent setFlags(int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 4
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<android.content.Intent: android.content.Intent setComponent(android.content.ComponentName)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 10
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1

