# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: fakedaum.apk
- **packageName**: com.tmvlove
- **targetSdk**: 7
- **minSdk**: 7
- **maxSdk**: undefined
- **mainActivity**: com.mvlove.MainActivity
# Behavior Information:
## Activities:
- When the malware is executed, it downloads another copy of itself with the name "Data Provider".  Next, the user is asked to install the second copy of the malware.
## BroadcastReceivers:
- SmsReceiver monitors incoming SMS messsages.
## Services: 
- TaskService uploads SMS messages and device information to a remote server. 
# Detail Information:
## Activities: 1
	com.mvlove.MainActivity
## Services: 2
	com.mvlove.service.TaskService
	com.mvlove.service.PhoneService
## Receivers: 2
	com.mvlove.receiver.AutoRunReceiver
	com.mvlove.receiver.SmsReceiver
## Permissions: 15
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.WRITE_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.WRITE_CALL_LOG
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.INTERNET
	android.permission.READ_CALL_LOG
	android.permission.ACCESS_NETWORK_STATE
	android.permission.ACCESS_WIFI_STATE
	android.permission.READ_PHONE_STATE
## Sources: 97
	<org.apache.http.util.EntityUtils: java.lang.String getContentCharSet(org.apache.http.HttpEntity)>: 4
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 1
	<java.lang.reflect.Array: java.lang.Object get(java.lang.Object,int)>: 1
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 1
	<java.lang.reflect.Field: int getModifiers()>: 1
	<java.util.BitSet: boolean get(int)>: 1
	<java.lang.reflect.Array: int getLength(java.lang.Object)>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<java.util.logging.Logger: java.util.logging.Logger getLogger(java.lang.String)>: 1
	<android.telephony.SmsManager: java.util.ArrayList divideMessage(java.lang.String)>: 1
	<java.lang.reflect.Field: java.lang.reflect.Type getGenericType()>: 2
	<android.content.Intent: java.util.ArrayList getParcelableArrayListExtra(java.lang.String)>: 1
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 1
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 3
	<java.text.DateFormat: java.text.DateFormat getDateInstance(int)>: 1
	<java.net.InetAddress: java.lang.String getHostAddress()>: 2
	<java.lang.System: java.lang.String getProperty(java.lang.String,java.lang.String)>: 1
	<java.io.File: java.lang.String getName()>: 2
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<java.lang.String: byte[] getBytes()>: 2
	<android.view.View: int getId()>: 1
	<android.content.Intent: java.io.Serializable getSerializableExtra(java.lang.String)>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayOriginatingAddress()>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<java.lang.ThreadLocal: java.lang.Object get()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<org.apache.http.conn.scheme.PlainSocketFactory: org.apache.http.conn.scheme.PlainSocketFactory getSocketFactory()>: 1
	<java.nio.charset.Charset: java.lang.String name()>: 4
	<java.lang.Double: double parseDouble(java.lang.String)>: 6
	<java.lang.Number: short shortValue()>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<java.text.DateFormat: java.text.DateFormat getDateTimeInstance(int,int,java.util.Locale)>: 3
	<java.text.DateFormat: java.text.DateFormat getDateTimeInstance(int,int)>: 3
	<android.content.ContentUris: long parseId(android.net.Uri)>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 4
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 1
	<java.lang.Class: java.lang.reflect.Constructor getDeclaredConstructor(java.lang.Class[])>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.lang.reflect.Field: java.lang.String getName()>: 2
	<java.lang.Long: long parseLong(java.lang.String)>: 5
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 1
	<java.lang.Class: int getModifiers()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.database.Cursor: int getInt(int)>: 1
	<org.apache.http.util.ByteArrayBuffer: byte[] buffer()>: 1
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 5
	<java.util.Date: int getHours()>: 6
	<java.lang.Short: short parseShort(java.lang.String)>: 1
	<java.lang.Float: float parseFloat(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 7
	<java.util.Date: int getMinutes()>: 6
	<java.lang.Class: java.lang.reflect.TypeVariable[] getTypeParameters()>: 2
	<android.os.HandlerThread: android.os.Looper getLooper()>: 2
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 8
	<java.util.Date: long getTime()>: 17
	<java.lang.Class: java.lang.String getSimpleName()>: 18
	<java.net.InetAddress: java.net.InetAddress getByName(java.lang.String)>: 1
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 4
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<android.net.NetworkInfo: int getType()>: 3
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 3
	<java.io.FileInputStream: void <init>: 2
	<java.lang.Number: byte byteValue()>: 1
	<java.lang.Class: java.lang.reflect.Type[] getGenericInterfaces()>: 5
	<java.lang.Class: java.lang.reflect.Type getGenericSuperclass()>: 6
	<java.lang.Byte: byte parseByte(java.lang.String)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 8
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<java.lang.Class: java.lang.reflect.Field[] getDeclaredFields()>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<java.security.MessageDigest: byte[] digest()>: 1
	<java.lang.reflect.InvocationTargetException: java.lang.Throwable getTargetException()>: 1
	<java.lang.reflect.Field: java.lang.Class getType()>: 3
	<java.util.Calendar: int get(int)>: 6
	<java.lang.String: byte[] getBytes(java.lang.String)>: 1
	<java.text.DateFormat: java.text.DateFormat getDateInstance(int,java.util.Locale)>: 1
	<java.util.TimeZone: java.util.TimeZone getTimeZone(java.lang.String)>: 2
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 5
	<java.lang.Class: java.lang.String getName()>: 12
	<java.lang.Enum: java.lang.String name()>: 2
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 1
	<java.lang.Class: java.lang.Class getEnclosingClass()>: 2
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 4
	<android.database.Cursor: long getLong(int)>: 4
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 6
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.lang.reflect.Array: java.lang.Object newInstance(java.lang.Class,int)>: 2
	<android.widget.EditText: android.text.Editable getText()>: 1
	<java.lang.Class: java.lang.'annotation'.Annotation getAnnotation(java.lang.Class)>: 2
	<java.io.File: boolean delete()>: 1
	<org.apache.http.conn.ssl.SSLSocketFactory: org.apache.http.conn.ssl.SSLSocketFactory getSocketFactory()>: 1
	<android.telephony.TelephonyManager: java.lang.String getSimSerialNumber()>: 1
	<java.io.File: void <init>: 1
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 1
	<java.text.DateFormat: java.util.Date parse(java.lang.String)>: 8
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
## Sinks: 83
	<java.lang.Short: short parseShort(java.lang.String)>: 1
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 2
	<java.lang.Float: float parseFloat(java.lang.String)>: 2
	<android.util.Log: int println(int,java.lang.String,java.lang.String)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<android.webkit.WebSettings: void setDefaultZoom(android.webkit.WebSettings$ZoomDensity)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 8
	<java.io.FileOutputStream: void write(byte[],int,int)>: 2
	<android.webkit.WebSettings: void setCacheMode(int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 1
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.view.View: void setEnabled(boolean)>: 1
	<org.apache.http.params.HttpProtocolParams: void setHttpElementCharset(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 5
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 5
	<org.apache.http.conn.params.ConnManagerParams: void setMaxTotalConnections(org.apache.http.params.HttpParams,int)>: 1
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<java.lang.Byte: byte parseByte(java.lang.String)>: 1
	<java.io.Writer: java.io.Writer append(char)>: 1
	<java.util.BitSet: void set(int)>: 1
	<java.net.URL: void <init>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 1
	<java.io.OutputStream: void write(byte[])>: 1
	<android.webkit.WebSettings: void setBuiltInZoomControls(boolean)>: 1
	<org.apache.http.params.HttpProtocolParams: void setUseExpectContinue(org.apache.http.params.HttpParams,boolean)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 8
	<org.apache.http.params.HttpConnectionParams: void setTcpNoDelay(org.apache.http.params.HttpParams,boolean)>: 1
	<android.os.Bundle: void writeToParcel(android.os.Parcel,int)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<java.text.DateFormat: void setTimeZone(java.util.TimeZone)>: 2
	<android.util.Log: int i(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<org.apache.http.params.HttpProtocolParams: void setContentCharset(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<org.apache.http.params.HttpConnectionParams: void setSocketBufferSize(org.apache.http.params.HttpParams,int)>: 1
	<java.io.Writer: void write(java.lang.String)>: 19
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 1
	<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>: 1
	<org.apache.http.params.HttpProtocolParams: void setVersion(org.apache.http.params.HttpParams,org.apache.http.ProtocolVersion)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 13
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 6
	<android.content.ContentValues: void put(java.lang.String,java.lang.Integer)>: 2
	<java.lang.reflect.Array: void set(java.lang.Object,int,java.lang.Object)>: 1
	<org.apache.http.conn.params.ConnManagerParams: void setTimeout(org.apache.http.params.HttpParams,long)>: 1
	<java.lang.reflect.AccessibleObject: void setAccessible(java.lang.reflect.AccessibleObject[],boolean)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 6
	<org.apache.http.conn.params.ConnManagerParams: void setMaxConnectionsPerRoute(org.apache.http.params.HttpParams,org.apache.http.conn.params.ConnPerRoute)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.io.Serializable)>: 1
	<java.lang.StringBuilder: void setCharAt(int,char)>: 3
	<android.content.ContentUris: long parseId(android.net.Uri)>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<org.apache.http.conn.params.ConnRouteParams: void setDefaultProxy(org.apache.http.params.HttpParams,org.apache.http.HttpHost)>: 2
	<java.io.File: boolean delete()>: 1
	<java.lang.StringBuilder: void setLength(int)>: 2
	<android.content.Intent: android.content.Intent putParcelableArrayListExtra(java.lang.String,java.util.ArrayList)>: 1
	<android.webkit.WebSettings: void setSupportZoom(boolean)>: 1
	<android.webkit.WebView: void setWebViewClient(android.webkit.WebViewClient)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.util.logging.Logger: void log(java.util.logging.Level,java.lang.String,java.lang.Object)>: 2
	<java.lang.Long: long parseLong(java.lang.String)>: 5
	<org.apache.http.params.HttpProtocolParams: void setUserAgent(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<java.text.DateFormat: java.util.Date parse(java.lang.String)>: 8
	<java.lang.Integer: int parseInt(java.lang.String)>: 5
	<android.os.AsyncTask: void onPostExecute(java.lang.Object)>: 1
	<android.content.Intent: android.content.Intent setComponent(android.content.ComponentName)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 5
	<org.apache.http.conn.scheme.SchemeRegistry: org.apache.http.conn.scheme.Scheme register(org.apache.http.conn.scheme.Scheme)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.Long)>: 1
