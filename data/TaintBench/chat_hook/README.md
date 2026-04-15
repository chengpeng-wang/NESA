# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: chat_hook.apk
- **packageName**: com.example.windseeker
- **targetSdk**: 15
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.example.chathook.MainActivity
# Behavior Information:
## Activities:
- The malware controls the QQ and WeChat processes. After gaining control it runs malicious code in libcall.so in the target process. 
# Detail Information:
## Activities: 1
	com.example.chathook.MainActivity
## Services: 1
	com.example.chathook.ProcessMonitor
## Receivers: 1
	com.example.chathook.HookReceiver
## Permissions: 7
	android.permission.INTERNET
	android.permission.GET_TASKS
	android.permission.ACCESS_NETWORK_STATE
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.WRITE_SETTINGS
	android.permission.READ_PHONE_STATE
## Sources: 22
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<java.lang.String: byte[] getBytes(java.lang.String)>: 2
	<java.io.BufferedReader: java.lang.String readLine()>: 6
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 4
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 5
	<android.content.Intent: boolean getBooleanExtra(java.lang.String,boolean)>: 3
	<java.io.File: void <init>: 19
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 1
	<java.io.File: java.lang.String getAbsolutePath()>: 9
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 4
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 2
	<android.app.ActivityManager: java.util.List getRecentTasks(int,int)>: 1
	<android.content.Intent: java.lang.String getAction()>: 2
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<android.content.Intent: android.content.ComponentName getComponent()>: 1
	<android.content.res.Resources: android.content.res.AssetManager getAssets()>: 4
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.content.ComponentName: java.lang.String getPackageName()>: 3
	<android.telephony.SmsManager: java.util.ArrayList divideMessage(java.lang.String)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 2
	<android.widget.EditText: android.text.Editable getText()>: 1
## Sinks: 21
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 2
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 4
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 2
	<java.io.FileOutputStream: void <init>: 4
	<java.io.FileOutputStream: void write(byte[])>: 4
	<java.lang.String: java.lang.String substring(int,int)>: 2
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 74
	<android.view.View: void setOnClickListener(android.view.View$OnClickListener)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 5
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<java.io.DataOutputStream: void flush()>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 2
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 3
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 2
	<android.telephony.SmsManager: void sendMultipartTextMessage(java.lang.String,java.lang.String,java.util.ArrayList,java.util.ArrayList,java.util.ArrayList)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 2

