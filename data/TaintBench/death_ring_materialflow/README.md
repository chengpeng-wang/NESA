# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: death_ring_materialflow.apk
- **packageName**: com.android.Materialflow
- **targetSdk**: undefined
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: UNKNOWN
# Behavior Information:
## BroadcastReceivers:
- SmsReceiver monitors incoming SMS messages and sends faked messages for fishing. 
- AdsInstallReceiver install APKs silently.
# Detail Information:
## Activities: 2
	com.qc.access.Warming
	com.qc.access.TestJarActivity
## Services: 5
	com.qc.access.MotionService
	com.qc.access.BaseSiteService
	com.qc.access.LocalOsService
	com.qc.access.MainOsService
	com.qc.access.BaseOsService
## Receivers: 8
	com.qc.access.BootReceiver
	com.qc.access.ApkUninstallReceiver
	com.qc.access.QCAlarmReceiver
	com.qc.access.ShutdownReceiver
	com.qc.access.OutGoingCallReceiver
	com.qc.access.AdsInstallReceiver
	com.qc.access.DateChangedReceiver
	com.qc.access.SmsReceiver
## Permissions: 34
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.MODIFY_PHONE_STATE
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.INSTALL_PACKAGES
	android.permission.CLEAR_APP_USER_DATA
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_MEDIA_STORAGE
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WRITE_SECURE_SETTINGS
	android.permission.PROCESS_OUTGOING_CALLS
	android.permission.WAKE_LOCK
	android.permission.ACCESS_WIFI_STATE
	android.permission.DELETE_PACKAGES
	com.android.launcher.permission.INSTALL_SHORTCUT
	android.permission.WRITE_APN_SETTINGS
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.CLEAR_APP_CACHE
	android.permission.WRITE_SMS
	android.permission.DISABLE_KEYGUARD
	android.permission.GET_PACKAGE_SIZE
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.RESTART_PACKAGES
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.CHANGE_WIFI_STATE
	android.permission.INTERNET
	android.permission.INJECT_EVENTS
	android.permission.FORCE_STOP_PACKAGES
	android.permission.GET_TASKS
	android.permission.CHANGE_NETWORK_STATE
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_SETTINGS
## Sources: 101
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 2
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 5
	<android.net.wifi.WifiManager: android.net.wifi.WifiInfo getConnectionInfo()>: 2
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 2
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 3
	<android.content.res.Resources: android.content.res.AssetManager getAssets()>: 4
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 6
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 10
	<android.os.StatFs: int getBlockSize()>: 1
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 4
	<android.os.StatFs: int getAvailableBlocks()>: 1
	<android.app.KeyguardManager: android.app.KeyguardManager$KeyguardLock newKeyguardLock(java.lang.String)>: 1
	<java.lang.String: byte[] getBytes()>: 10
	<java.util.Calendar: java.util.Calendar getInstance()>: 1
	<java.io.File: java.lang.String getPath()>: 1
	<android.provider.Settings$System: int getInt(android.content.ContentResolver,java.lang.String,int)>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<org.json.JSONObject: long getLong(java.lang.String)>: 5
	<android.app.Activity: android.view.WindowManager getWindowManager()>: 3
	<android.telephony.gsm.SmsMessage: java.lang.String getDisplayOriginatingAddress()>: 1
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[])>: 2
	<android.net.wifi.WifiInfo: java.lang.String getMacAddress()>: 3
	<android.view.Display: void getMetrics(android.util.DisplayMetrics)>: 2
	<android.telephony.TelephonyManager: java.lang.String getSimOperator()>: 2
	<android.view.Display: int getWidth()>: 2
	<java.io.BufferedReader: java.lang.String readLine()>: 7
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 6
	<java.lang.Double: double parseDouble(java.lang.String)>: 2
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 8
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.content.Intent: java.lang.String getAction()>: 27
	<android.net.NetworkInfo: java.lang.String getExtraInfo()>: 2
	<android.telephony.gsm.SmsMessage: android.telephony.gsm.SmsMessage createFromPdu(byte[])>: 1
	<java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String)>: 1
	<java.lang.Thread: java.lang.Thread$UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()>: 1
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 4
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 46
	<android.telephony.TelephonyManager: int getDataState()>: 1
	<android.app.ActivityManager: java.util.List getRunningAppProcesses()>: 3
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 3
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 1
	<android.os.Environment: java.lang.String getExternalStorageState()>: 10
	<android.content.res.Resources: int getIdentifier(java.lang.String,java.lang.String,java.lang.String)>: 6
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>: 2
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 7
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 2
	<java.lang.Throwable: java.lang.Throwable getCause()>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<java.lang.reflect.Field: java.lang.String getName()>: 1
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 3
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.database.Cursor: int getInt(int)>: 5
	<android.content.ComponentName: java.lang.String getPackageName()>: 3
	<android.content.Context: java.lang.String getString(int)>: 1
	<android.telephony.gsm.SmsMessage: java.lang.String getDisplayMessageBody()>: 1
	<org.json.JSONObject: int getInt(java.lang.String)>: 18
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 10
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 15
	<java.lang.Class: java.lang.String getSimpleName()>: 1
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 1
	<org.json.JSONObject: org.json.JSONObject getJSONObject(java.lang.String)>: 1
	<android.content.ComponentName: java.lang.String getClassName()>: 2
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 3
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 2
	<java.io.FileInputStream: void <init>: 1
	<javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>: 1
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 28
	<android.database.Cursor: java.lang.String getString(int)>: 20
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 4
	<java.lang.Class: java.lang.reflect.Field[] getDeclaredFields()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<android.content.Intent: java.lang.String getDataString()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSubscriberId()>: 5
	<java.util.Hashtable: java.lang.Object get(java.lang.Object)>: 1
	<android.content.res.Resources: android.graphics.drawable.Drawable getDrawable(int)>: 1
	<java.util.Calendar: int get(int)>: 1
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 1
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 4
	<android.net.NetworkInfo: android.net.NetworkInfo$State getState()>: 11
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 6
	<java.lang.Class: java.lang.String getName()>: 6
	<android.content.pm.ResolveInfo: java.lang.CharSequence loadLabel(android.content.pm.PackageManager)>: 1
	<android.location.LocationManager: java.util.List getProviders(boolean)>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 16
	<android.telephony.gsm.SmsManager: android.telephony.gsm.SmsManager getDefault()>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 11
	<android.provider.Settings$System: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 1
	<java.io.File: boolean delete()>: 42
	<java.io.LineNumberReader: java.lang.String readLine()>: 4
	<android.media.AudioManager: int getRingerMode()>: 1
	<android.content.Intent: int getIntExtra(java.lang.String,int)>: 2
	<java.io.File: void <init>: 32
	<java.io.File: java.lang.String getAbsolutePath()>: 7
	<android.net.ConnectivityManager: android.net.NetworkInfo[] getAllNetworkInfo()>: 4
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 3
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
	<org.json.JSONObject: org.json.JSONArray getJSONArray(java.lang.String)>: 5
	<android.view.Display: int getHeight()>: 2
## Sinks: 80
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 3
	<java.io.FileWriter: void write(java.lang.String)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 15
	<android.content.Intent: android.content.Intent setPackage(java.lang.String)>: 3
	<java.io.FileOutputStream: void write(byte[],int,int)>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 3
	<java.lang.String: java.lang.String substring(int,int)>: 7
	<java.io.OutputStreamWriter: void <init>: 2
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 2
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 1
	<android.os.Handler: boolean sendEmptyMessage(int)>: 1
	<android.os.Handler: boolean sendEmptyMessageDelayed(int,long)>: 6
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.widget.RemoteViews: void setImageViewResource(int,int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 4
	<android.widget.RemoteViews: void setImageViewBitmap(int,android.graphics.Bitmap)>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 7
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 13
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 4
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 4
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<java.net.URL: void <init>: 3
	<java.net.URL: java.net.URLConnection openConnection()>: 3
	<android.media.AudioManager: void setVibrateSetting(int,int)>: 4
	<java.io.OutputStream: void write(byte[])>: 6
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 14
	<java.lang.Thread: void setPriority(int)>: 4
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 3
	<android.os.Parcel: void writeFloatArray(float[])>: 2
	<android.widget.ImageView: void setImageBitmap(android.graphics.Bitmap)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 10
	<java.lang.String: boolean startsWith(java.lang.String)>: 16
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 1
	<android.os.Parcel: void writeIntArray(int[])>: 2
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 12
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 2
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.Integer)>: 1
	<java.lang.Thread: void setDefaultUncaughtExceptionHandler(java.lang.Thread$UncaughtExceptionHandler)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,boolean)>: 2
	<java.lang.ProcessBuilder: java.lang.Process start()>: 6
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 1
	<android.content.Intent: android.content.Intent setFlags(int)>: 4
	<android.media.AudioManager: void setRingerMode(int)>: 2
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 1
	<java.io.DataOutputStream: void flush()>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 11
	<java.security.SecureRandom: void setSeed(byte[])>: 1
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 4
	<java.net.HttpURLConnection: void connect()>: 4
	<android.util.Log: int w(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<java.io.File: boolean delete()>: 42
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 4
	<android.widget.RemoteViews: void setTextViewText(int,java.lang.CharSequence)>: 3
	<android.content.IntentFilter: void setPriority(int)>: 2
	<android.telephony.gsm.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 2
	<java.io.FileOutputStream: void <init>: 6
	<java.io.FileOutputStream: void write(byte[])>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<android.widget.ImageView: void setImageDrawable(android.graphics.drawable.Drawable)>: 1
	<java.lang.ProcessBuilder: java.lang.ProcessBuilder command(java.lang.String[])>: 1
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 1
	<android.content.Intent: android.content.Intent setClassName(java.lang.String,java.lang.String)>: 1
	<android.net.wifi.WifiManager: boolean setWifiEnabled(boolean)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
	<android.os.AsyncTask: void onPostExecute(java.lang.Object)>: 1
	<android.content.Intent: android.content.Intent setComponent(android.content.ComponentName)>: 4
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 2
	<android.app.Activity: boolean onKeyDown(int,android.view.KeyEvent)>: 2
	<android.content.SharedPreferences$Editor: boolean commit()>: 5
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 10
