# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: tetus.apk
- **packageName**: com.droidmojo.awesomejokes
- **targetSdk**: undefined
- **minSdk**: 4
- **maxSdk**: undefined
- **mainActivity**: .Main
# Behavior Information:
## Activities:
- When the malware is executed, it registers an SMS observer to record SMS messages and send
them to a C&C server. It may delete some SMS messages from the device and register an SMS receiver to send SMS messages without the user's consent. It may also collect other data on the device.
# Detail Information:
## Activities: 3
	shared.library.us.Splash
	shared.library.us.Marketing
	.Main
## Receivers: 1
	shared.library.us.MarketReciever
## Permissions: 4
	android.permission.SEND_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.READ_PHONE_STATE
## Sources: 45
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<java.io.BufferedReader: java.lang.String readLine()>: 8
	<android.graphics.Bitmap: int getHeight()>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 2
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 7
	<android.media.MediaPlayer: android.media.MediaPlayer create(android.content.Context,android.net.Uri)>: 1
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 1
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 4
	<android.content.Intent: java.lang.String getAction()>: 6
	<android.graphics.Bitmap: int getWidth()>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 4
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>: 1
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<java.util.Locale: java.lang.String getCountry()>: 4
	<android.database.Cursor: long getLong(int)>: 6
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(android.graphics.Bitmap,int,int,int,int,android.graphics.Matrix,boolean)>: 1
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 2
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 3
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 2
	<java.util.Locale: java.lang.String getLanguage()>: 4
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 2
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 7
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>: 1
	<java.util.Locale: java.util.Locale getDefault()>: 3
	<android.app.PendingIntent: android.app.PendingIntent getService(android.content.Context,int,android.content.Intent,int)>: 1
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 13
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 4
	<android.webkit.WebView: void loadData(java.lang.String,java.lang.String,java.lang.String)>: 1
	<org.apache.http.HttpHost: java.lang.String getHostName()>: 2
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<org.apache.http.HttpHost: int getPort()>: 1
	<android.database.Cursor: int getInt(int)>: 11
	<android.content.Context: java.lang.String getString(int)>: 4
	<java.util.LinkedList: java.lang.Object get(int)>: 1
## Sinks: 43
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 2
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 2
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 4
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 7
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.Integer)>: 9
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<java.io.OutputStreamWriter: void <init>: 1
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 3
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 8
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 19
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 1
	<android.media.MediaPlayer: void setOnPreparedListener(android.media.MediaPlayer$OnPreparedListener)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 3
	<java.io.OutputStreamWriter: void write(java.lang.String)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 5
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 1
	<java.net.HttpURLConnection: void connect()>: 4
	<android.util.Log: int w(java.lang.String,java.lang.String,java.lang.Throwable)>: 3
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 1
	<android.widget.ImageView: void setVisibility(int)>: 3
	<java.net.URL: void <init>: 4
	<java.net.URL: java.net.URLConnection openConnection()>: 4
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 4
	<android.content.Context: void startActivity(android.content.Intent)>: 2
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 1
	<java.net.HttpURLConnection: void setInstanceFollowRedirects(boolean)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<android.app.Activity: boolean onKeyDown(int,android.view.KeyEvent)>: 6
	<android.media.MediaPlayer: void setOnCompletionListener(android.media.MediaPlayer$OnCompletionListener)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 1
	<android.content.ContentValues: void put(java.lang.String,java.lang.Long)>: 8
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 1
	<android.webkit.WebSettings: void setUserAgentString(java.lang.String)>: 1
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 1
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 4
