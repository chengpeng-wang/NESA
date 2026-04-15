# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: overlay_android_samp.apk
- **packageName**: exts.whats
- **targetSdk**: 21
- **minSdk**: 13
- **maxSdk**: undefined
- **mainActivity**: .Main
# Behavior Information:
## Activities:
- The activity Cards steals credit card information which will be later sent by the SendService. 
## BroadcastReceivers:
- MessageReceiver monitors incoming SMS messages which will be later sent by the SendService. 
## Services:
- SendService sends collected information to a remote server.
# Detail Information:
## Activities: 4
	.activities.CvcPopup
	.DevAdminDisabler
	.activities.Cards
	.Main
## Services: 2
	.MainService
	.SendService
## Receivers: 3
	.DevAdminReceiver
	.MessageReceiver
	.Starter
## Permissions: 10
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.READ_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.GET_TASKS
	android.permission.WAKE_LOCK
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.READ_PHONE_STATE
## Sources: 55
	<android.view.View: int getWidth()>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 5
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<android.content.res.Resources: int getInteger(int)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.view.View: void getLocationOnScreen(int[])>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<android.telephony.TelephonyManager: int getPhoneType()>: 3
	<java.util.logging.Logger: java.util.logging.Logger getLogger(java.lang.String)>: 3
	<android.view.animation.AnimationUtils: android.view.animation.Animation loadAnimation(android.content.Context,int)>: 5
	<android.view.MotionEvent: int getActionMasked()>: 5
	<java.lang.Character: int getType(char)>: 2
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 2
	<java.lang.Class: java.io.InputStream getResourceAsStream(java.lang.String)>: 4
	<android.text.method.DigitsKeyListener: android.text.method.DigitsKeyListener getInstance(java.lang.String)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 3
	<android.content.Intent: int getFlags()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.view.View: int getHeight()>: 1
	<android.telephony.TelephonyManager: int getSimState()>: 1
	<java.lang.Character: int getNumericValue(char)>: 3
	<android.telephony.TelephonyManager: java.lang.String getSimOperator()>: 1
	<android.view.View: android.os.IBinder getWindowToken()>: 2
	<java.lang.Class: java.lang.String getName()>: 3
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 3
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 2
	<exts.whats.activities.Cards: android.view.View findViewById(int)>: 23
	<java.lang.reflect.Field: int getInt(java.lang.Object)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 1
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 1
	<android.content.Intent: java.lang.String getAction()>: 3
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 2
	<android.content.res.Resources: int getColor(int)>: 1
	<java.util.Locale: java.lang.String getCountry()>: 1
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 2
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.content.res.Resources: java.lang.String[] getStringArray(int)>: 3
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 1
	<android.widget.EditText: android.text.Editable getText()>: 14
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 11
	<android.app.ActivityManager: java.util.List getRunningAppProcesses()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkCountryIso()>: 1
	<java.util.LinkedHashMap: java.lang.Object get(java.lang.Object)>: 1
	<android.os.Bundle: boolean getBoolean(java.lang.String)>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 4
	<android.content.res.Resources: android.content.res.Configuration getConfiguration()>: 1
	<android.content.ComponentName: java.lang.String getPackageName()>: 1
	<android.content.Context: java.lang.String getString(int)>: 1
	<android.net.wifi.WifiManager: android.net.wifi.WifiManager$WifiLock createWifiLock(int,java.lang.String)>: 1
	<android.os.Looper: android.os.Looper getMainLooper()>: 3
## Sinks: 35
	<java.lang.String: boolean startsWith(java.lang.String)>: 7
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 11
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 3
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,boolean)>: 2
	<java.lang.String: java.lang.String substring(int,int)>: 18
	<android.view.animation.Animation: void setAnimationListener(android.view.animation.Animation$AnimationListener)>: 2
	<android.media.AudioManager: void setRingerMode(int)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 2
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 2
	<android.widget.ImageView: void setImageResource(int)>: 6
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<android.view.View: void setVisibility(int)>: 4
	<java.lang.StringBuilder: void setLength(int)>: 11
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 6
	<android.widget.ImageView: void setVisibility(int)>: 2
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,boolean)>: 2
	<java.util.regex.Matcher: java.lang.String replaceFirst(java.lang.String)>: 8
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 5
	<java.lang.StringBuilder: java.lang.StringBuilder replace(int,int,java.lang.String)>: 4
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 34
	<java.util.logging.Logger: void log(java.util.logging.Level,java.lang.String)>: 28
	<android.content.Context: void startActivity(android.content.Intent)>: 2
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 4
	<android.app.Activity: void onCreate(android.os.Bundle)>: 4
	<android.content.SharedPreferences$Editor: boolean commit()>: 2
	<android.app.Activity: boolean onKeyDown(int,android.view.KeyEvent)>: 2
	<java.util.logging.Logger: void log(java.util.logging.Level,java.lang.String,java.lang.Throwable)>: 6
	<java.util.regex.Matcher: java.lang.String replaceAll(java.lang.String)>: 10
	<exts.whats.activities.Cards: android.content.ComponentName startService(android.content.Intent)>: 1
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 58

