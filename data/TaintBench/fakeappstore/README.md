# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: fakeappstore.apk
- **packageName**: com.google.games.stores
- **targetSdk**: 8
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.google.games.stores.MainActivity
# Behavior Information:
## BroadcastReceivers:
- MessageReceiver monitors incoming SMS messages. 
## Services:
- ContactsServercie spams contacts with SMS messages. 
- MessageService uploads SMS messages. 
# Detail Information:
## Activities: 3
	com.google.games.stores.MainActivity
	com.google.games.stores.site.BKMain
	com.google.games.stores.site.Factory
## Services: 3
	com.google.games.stores.service.MessageService
	com.google.games.stores.service.Notifications
	com.google.games.stores.service.ContactsService
## Receivers: 3
	com.google.games.stores.recevier.MessageReceiver
	com.google.games.stores.recevier.ActiveRecevier
	com.google.games.stores.recevier.BootRecevier
## Permissions: 9
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.INTERNET
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
	com.android.launcher.permission.INSTALL_SHORTCUT
## Sources: 44
	<javax.crypto.SecretKeyFactory: javax.crypto.SecretKeyFactory getInstance(java.lang.String)>: 2
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 6
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 4
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 2
	<android.util.Xml: org.xmlpull.v1.XmlPullParser newPullParser()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 5
	<android.app.Activity: android.view.View getCurrentFocus()>: 1
	<android.view.View: android.os.IBinder getWindowToken()>: 1
	<android.content.res.Resources: java.lang.String getString(int)>: 13
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<android.content.ComponentName: java.lang.String getClassName()>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 4
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.content.Intent: java.lang.String getAction()>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 1
	<android.media.RingtoneManager: android.net.Uri getDefaultUri(int)>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<java.io.FileInputStream: void <init>: 1
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 2
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 5
	<android.os.Environment: java.lang.String getExternalStorageState()>: 4
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 3
	<android.content.Intent: int getIntExtra(java.lang.String,int)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 6
	<java.io.File: java.lang.String getAbsolutePath()>: 3
	<java.io.File: void <init>: 5
	<java.lang.String: byte[] getBytes()>: 3
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.view.View: int getId()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.content.Intent: java.io.Serializable getSerializableExtra(java.lang.String)>: 1
	<android.app.Activity: java.lang.Object getSystemService(java.lang.String)>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<android.content.Intent: java.lang.String getDataString()>: 1
	<org.json.JSONArray: java.lang.Object get(int)>: 2
	<android.media.RingtoneManager: android.media.Ringtone getRingtone(android.content.Context,android.net.Uri)>: 1
## Sinks: 31
	<android.app.ProgressDialog: void setMax(int)>: 1
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 7
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 5
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 4
	<java.io.FileOutputStream: void write(byte[],int,int)>: 2
	<java.lang.String: java.lang.String substring(int,int)>: 3
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 12
	<android.content.Intent: android.content.Intent setFlags(int)>: 11
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 9
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.io.Serializable)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 14
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 5
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.io.FileOutputStream: void <init>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 3
	<android.app.ProgressDialog: void setProgress(int)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<android.content.SharedPreferences$Editor: boolean commit()>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 5
	<android.app.ProgressDialog: void setProgressStyle(int)>: 1
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
