# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: smsstealer_kysn_assassincreed_android_samp.apk
- **packageName**: com.dsifakf.aoakmnq
- **targetSdk**: undefined
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.dsifakf.aoakmnq.MainActivity
# Behavior Information:
## Activities:
- MainActivity installs a pirated version of the Assassins Creed game that functions normally, making end user oblivious to the
malicious activities it performs in background.
## BroadcastReceivers:
- Reciever monitors incoming SMS messages.
- GetAll sends collected data to via a HTTP Post request.
- CheckUpdate sends multi-part text messages.
# Detail Information:
## Activities: 1
	com.dsifakf.aoakmnq.MainActivity
## Services: 1
	com.dsifakf.aoakmnq.Repeat
## Receivers: 6
	com.dsifakf.aoakmnq.GetAccs
	com.dsifakf.aoakmnq.Auto
	com.dsifakf.aoakmnq.GetAll
	com.dsifakf.aoakmnq.CheckUpdate
	com.dsifakf.aoakmnq.Receiver
	com.dsifakf.aoakmnq.Call
## Permissions: 13
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.GET_ACCOUNTS
	android.permission.READ_SMS
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.PROCESS_OUTGOING_CALLS
	android.permission.READ_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 33
	<java.io.BufferedReader: java.lang.String readLine()>: 3
	<java.lang.String: byte[] getBytes(java.lang.String)>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 1
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<android.content.res.Resources: java.lang.String getString(int)>: 1
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 2
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<android.telephony.gsm.SmsManager: java.util.ArrayList divideMessage(java.lang.String)>: 1
	<android.content.Intent: java.lang.String getAction()>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<android.accounts.AccountManager: android.accounts.Account[] getAccounts()>: 1
	<android.telephony.gsm.SmsManager: android.telephony.gsm.SmsManager getDefault()>: 2
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.net.URLDecoder: java.lang.String decode(java.lang.String,java.lang.String)>: 1
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 4
	<android.preference.PreferenceManager: android.content.SharedPreferences getDefaultSharedPreferences(android.content.Context)>: 4
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 4
	<android.accounts.AccountManager: android.accounts.AccountManager get(android.content.Context)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 3
	<java.io.File: void <init>: 1
	<java.lang.String: byte[] getBytes()>: 3
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getSubscriberId()>: 5
## Sinks: 18
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<android.os.AsyncTask: android.os.AsyncTask execute(java.lang.Object[])>: 2
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 1
	<android.os.AsyncTask: void onPostExecute(java.lang.Object)>: 6
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<android.telephony.gsm.SmsManager: void sendMultipartTextMessage(java.lang.String,java.lang.String,java.util.ArrayList,java.util.ArrayList,java.util.ArrayList)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 2

