# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: save_me.apk
- **packageName**: com.savemebeta
- **targetSdk**: 19
- **minSdk**: 14
- **maxSdk**: undefined
- **mainActivity**: com.savemebeta.SplashScreen
# Behavior Information:
## Activities:
- The activities pretend to protect user's data, then steals it.
## BroadcastReceivers:
- BootStartUpReceiver initiates the backend service, connecting to the command and control server, to which it exfiltrates this personal information along with additional data it surreptitiously collects from the device. 
## Services:
- CO sends contacts on the device to the URL http://topemarketing.com/android/googlefinal/sendcontacts.php.
- CHECKUP sends device information (MAC, carrier, country) to a remote server. 
- RC allows making calls controlled by the C&C server. 

# Detail Information:
## Activities: 9
	com.savemebeta.SOSsm
	com.savemebeta.thanks2
	com.savemebeta.addcontact2
	com.savemebeta.Scan
	com.savemebeta.pack
	com.savemebeta.addcontact
	com.savemebeta.Analyse
	com.savemebeta.thanks
	com.savemebeta.SplashScreen
## Services: 7
	com.savemebeta.CO
	com.savemebeta.RC
	com.savemebeta.restart
	com.savemebeta.CHECKUPD
	com.savemebeta.SCHKMS
	com.savemebeta.GTSTSR
	com.savemebeta.restartSCHK
## Receivers: 1
	com.savemebeta.BootStartUpReciever
## Permissions: 25
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.BLUETOOTH_ADMIN
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_NETWORK_STATE
	android.permission.ACCESS_WIFI_STATE
	android.permission.WRITE_CONTACTS
	com.android.launcher.permission.INSTALL_SHORTCUT
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.WRITE_CALL_LOG
	android.permission.VIBRATE
	android.permission.RESTART_PACKAGES
	android.permission.KILL_BACKGROUND_PROCESSES
	android.permission.CHANGE_WIFI_STATE
	android.permission.INTERNET
	android.permission.READ_CALL_LOG
	android.permission.GET_TASKS
	android.permission.BLUETOOTH
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_SETTINGS
## Sources: 93
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 7
	<android.net.Uri: java.lang.String getQueryParameter(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 12
	<android.database.sqlite.SQLiteOpenHelper: android.database.sqlite.SQLiteDatabase getWritableDatabase()>: 4
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 3
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 10
	<android.net.wifi.WifiManager: android.net.wifi.WifiInfo getConnectionInfo()>: 6
	<java.lang.Class: java.lang.String getSimpleName()>: 4
	<java.net.URL: java.lang.String getPath()>: 1
	<java.net.URL: java.lang.String getHost()>: 2
	<java.lang.StackTraceElement: java.lang.String getClassName()>: 2
	<java.util.Locale: java.lang.String getDisplayCountry()>: 3
	<java.lang.Class: java.lang.ClassLoader getClassLoader()>: 1
	<java.io.FileInputStream: void <init>: 2
	<android.os.Parcel: java.util.ArrayList createTypedArrayList(android.os.Parcelable$Creator)>: 1
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 7
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 1
	<android.database.sqlite.SQLiteDatabase: java.lang.String getPath()>: 2
	<android.widget.ProgressBar: int getMax()>: 2
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>: 6
	<java.net.URL: java.lang.String getProtocol()>: 2
	<java.net.HttpURLConnection: java.lang.String getResponseMessage()>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 46
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 3
	<java.util.Locale: java.util.Locale getDefault()>: 8
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<java.util.Calendar: java.util.Calendar getInstance()>: 4
	<java.lang.String: byte[] getBytes()>: 7
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 24
	<android.util.LruCache: java.lang.Object get(java.lang.Object)>: 1
	<android.view.View: int getId()>: 2
	<android.content.res.AssetFileDescriptor: long getDeclaredLength()>: 1
	<java.lang.Class: java.lang.Class[] getClasses()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 7
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 2
	<android.database.CursorWindow: int getNumRows()>: 2
	<java.net.URL: java.io.InputStream openStream()>: 1
	<java.security.MessageDigest: byte[] digest()>: 1
	<android.net.wifi.WifiInfo: java.lang.String getMacAddress()>: 6
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[])>: 6
	<android.view.Display: void getMetrics(android.util.DisplayMetrics)>: 1
	<java.net.URISyntaxException: java.lang.String getMessage()>: 1
	<android.content.Intent: android.net.Uri getData()>: 1
	<java.util.Locale: java.lang.String getDisplayLanguage()>: 2
	<java.nio.ByteBuffer: java.nio.ByteBuffer get(byte[])>: 2
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<java.lang.Thread: java.lang.String getName()>: 1
	<java.lang.String: byte[] getBytes(java.lang.String)>: 15
	<java.io.BufferedReader: java.lang.String readLine()>: 3
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 7
	<java.lang.StackTraceElement: int getLineNumber()>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 5
	<java.lang.Class: java.lang.String getName()>: 6
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 2
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 4
	<java.net.HttpURLConnection: int getResponseCode()>: 2
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 2
	<android.content.Intent: java.lang.String getAction()>: 4
	<java.util.Calendar: java.util.Date getTime()>: 4
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 4
	<android.database.Cursor: long getLong(int)>: 11
	<java.util.Locale: java.lang.String getCountry()>: 5
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 7
	<java.lang.Thread: java.lang.Thread$UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()>: 1
	<java.nio.ByteBuffer: java.nio.ByteBuffer get(byte[],int,int)>: 6
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 3
	<java.net.URLDecoder: java.lang.String decode(java.lang.String,java.lang.String)>: 2
	<java.net.URL: int getPort()>: 2
	<java.lang.Throwable: java.lang.StackTraceElement[] getStackTrace()>: 1
	<android.widget.EditText: android.text.Editable getText()>: 11
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 11
	<java.io.File: boolean delete()>: 3
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 1
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 1
	<java.util.Locale: java.lang.String getLanguage()>: 9
	<java.lang.Class: java.lang.String getCanonicalName()>: 1
	<android.os.Bundle: boolean getBoolean(java.lang.String)>: 2
	<android.content.res.Resources: int getIdentifier(java.lang.String,java.lang.String,java.lang.String)>: 1
	<java.io.File: void <init>: 5
	<java.util.concurrent.locks.ReentrantLock: int getHoldCount()>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 3
	<java.lang.Throwable: java.lang.Throwable getCause()>: 3
	<java.lang.StackTraceElement: java.lang.String getMethodName()>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 4
	<org.json.JSONObject: java.lang.Object get(java.lang.String)>: 1
	<android.database.Cursor: int getInt(int)>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
	<android.content.res.Resources: android.content.res.Configuration getConfiguration()>: 3
	<java.net.URLConnection: int getContentLength()>: 1
	<android.content.Context: java.lang.String getString(int)>: 4
	<java.lang.reflect.InvocationTargetException: java.lang.Throwable getCause()>: 3
## Sinks: 74
	<android.app.Dialog: void setContentView(int)>: 1
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 5
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 10
	<java.lang.String: java.lang.String substring(int,int)>: 2
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 2
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 2
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 7
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 28
	<android.content.ContentProviderOperation$Builder: android.content.ContentProviderOperation$Builder withValue(java.lang.String,java.lang.Object)>: 6
	<android.widget.ProgressBar: void setVisibility(int)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 3
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 23
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 2
	<java.io.OutputStream: void write(byte[],int,int)>: 6
	<java.net.URL: void <init>: 7
	<java.net.URL: java.net.URLConnection openConnection()>: 3
	<android.widget.ProgressBar: void setProgress(int)>: 2
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 5
	<java.io.OutputStream: void write(byte[])>: 4
	<java.io.File: boolean setWritable(boolean,boolean)>: 4
	<android.content.Context: void startActivity(android.content.Intent)>: 2
	<android.util.Log: int d(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 25
	<android.app.Dialog: void setTitle(java.lang.CharSequence)>: 1
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 6
	<android.app.Activity: void onCreate(android.os.Bundle)>: 11
	<android.util.Log: int i(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 5
	<android.content.ContentProviderOperation$Builder: android.content.ContentProviderOperation$Builder withValueBackReference(java.lang.String,int)>: 2
	<java.util.concurrent.ConcurrentHashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 7
	<java.util.TreeSet: boolean add(java.lang.Object)>: 2
	<android.util.Log: int v(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 7
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 2
	<java.io.File: boolean setReadable(boolean,boolean)>: 4
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 2
	<java.lang.Double: double parseDouble(java.lang.String)>: 5
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.Integer)>: 5
	<java.lang.Thread: void setDefaultUncaughtExceptionHandler(java.lang.Thread$UncaughtExceptionHandler)>: 2
	<android.view.Window: void setFlags(int,int)>: 4
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,boolean)>: 2
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 26
	<java.io.DataOutputStream: void write(byte[],int,int)>: 2
	<java.io.DataOutputStream: void flush()>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 7
	<java.nio.ByteBuffer: java.nio.ByteBuffer put(byte[],int,int)>: 2
	<java.net.HttpURLConnection: void connect()>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<java.io.File: boolean delete()>: 3
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 10
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 3
	<java.io.PrintStream: void println(java.lang.Object)>: 2
	<java.io.FileOutputStream: void <init>: 3
	<java.io.FileOutputStream: void write(byte[])>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 5
	<java.lang.Long: long parseLong(java.lang.String)>: 4
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
	<android.content.Intent: android.content.Intent setComponent(android.content.ComponentName)>: 1
	<android.os.Handler: boolean sendMessageDelayed(android.os.Message,long)>: 8
	<android.content.SharedPreferences$Editor: boolean commit()>: 1
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 2
	<android.content.Intent: android.content.Intent putExtras(android.os.Bundle)>: 6
	<android.content.ContentValues: void put(java.lang.String,java.lang.Long)>: 5
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 12
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 2
