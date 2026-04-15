# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: slocker_android_samp.apk
- **packageName**: com.android.locker
- **targetSdk**: undefined
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: MainActivity$mainActivity
# Behavior Information:
## Activities:
-  The malware does its best to be as intrusive as possible by blocking the victim’s normal device-use with the app. Using a Java TimerTask, which is set to run every 10 milliseconds, the application will kill any other running processes that the user interacts that are not the malware itself or the phone's settings application. 
## BroadcastReceivers:
-  BootReceiver resumes ScarePakage’s
takeover of your device immediately, shutting down all other processes that the user interacts with. 
## Services:
- BackgroundService uses an Android WakeLock to prevent the device from going to
sleep. 
# Detail Information:
## Activities: 3
	SenderActivity
	VirusSearcher
	MainActivity$mainActivity
## Services: 1
	BackgroundService
## Receivers: 2
	BootReceiver
	.MainActivity
## Permissions: 9
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.DISABLE_KEYGUARD
	android.permission.INTERNET
	android.permission.GET_TASKS
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 30
	<java.io.BufferedReader: java.lang.String readLine()>: 6
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 2
	<java.util.Locale: java.lang.String getISO3Country()>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String,java.lang.String)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 3
	<java.util.Date: int getYear()>: 2
	<java.util.Date: int getDate()>: 2
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 2
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 4
	<java.io.FileInputStream: void <init>: 1
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 2
	<android.widget.EditText: android.text.Editable getText()>: 11
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 1
	<java.io.File: java.io.File[] listFiles()>: 3
	<java.io.File: java.lang.String getName()>: 2
	<javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>: 1
	<android.app.KeyguardManager: android.app.KeyguardManager$KeyguardLock newKeyguardLock(java.lang.String)>: 1
	<java.util.Locale: java.util.Locale getDefault()>: 2
	<java.io.File: void <init>: 7
	<java.io.File: java.lang.String getAbsolutePath()>: 8
	<java.lang.String: byte[] getBytes()>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<java.util.Date: int getMonth()>: 2
	<android.view.KeyEvent: int getKeyCode()>: 6
	<android.content.ComponentName: java.lang.String getPackageName()>: 2
	<android.content.Context: java.lang.String getString(int)>: 3
	<java.util.Calendar: int get(int)>: 1
## Sinks: 26
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 1
	<android.widget.ProgressBar: void setMax(int)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<android.widget.Toast: void setGravity(int,int,int)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.view.Window: void setFlags(int,int)>: 3
	<java.lang.String: java.lang.String substring(int,int)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 3
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<org.apache.http.entity.AbstractHttpEntity: void setContentEncoding(java.lang.String)>: 4
	<java.security.SecureRandom: void setSeed(byte[])>: 1
	<org.apache.http.entity.AbstractHttpEntity: void setContentType(java.lang.String)>: 4
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<android.view.Window: void setType(int)>: 5
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 7
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 1
	<java.io.OutputStream: void write(byte[])>: 1
	<android.widget.ProgressBar: void setProgress(int)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 8
	<java.io.FileOutputStream: void <init>: 3
	<android.content.Context: void startActivity(android.content.Intent)>: 4
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 4
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 3
