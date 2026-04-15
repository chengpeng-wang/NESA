# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: xbot_android_samp.apk
- **packageName**: com.address.core
- **targetSdk**: undefined
- **minSdk**: undefined
- **maxSdk**: undefined
- **mainActivity**: com.address.core.MainActivity
# Behavior Information:
## Activities:
MainActivity starts the service RunSerivce.
BrowserActivity uses WebView to aallow malicious code (e.g. sending SMSs, calls, contacts, collecting device information etc.) which are implemented in Java to be called from JavaScript. The malicious functionalities are implemented in the class xAPI.
## BroadcastReceivers:
SMSHandler monitors incoming SMSs.
## Services:
RunService monitors running apps via the getRunningTasks() API in Android. It popups another interface on the top of certain running apps, i.e., it performs activity hijacking. The facked interface Lock uses WebView to download JavaScript code. 
# Detail Information:
## Activities: 9
	com.address.core.MainActivity
	com.address.core.AdminActivity
	com.address.core.activities.Browser
	com.address.core.activities.Inject
	com.address.core.activities.BrowserActivity
	com.address.core.activities.BankApp
	com.address.core.activities.GoogleCC
	com.address.core.lck.Lock
	com.address.core.activities.RunScript
## Services: 1
	com.address.core.RunService
## Receivers: 5
	com.address.core.AdminReceiver
	.sms.SentReceiver
	.sms.DeliveredReceiver
	com.address.core.OnBootHandler
	.SMSHandler
## Permissions: 16
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_FINE_LOCATION
	android.permission.INTERNET
	android.permission.GET_TASKS
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.READ_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
	android.permission.ACCESS_MOCK_LOCATION
	android.permission.ACCESS_LOCATION_EXTRA_COMMANDS
	android.permission.BIND_DEVICE_ADMIN
## Sources: 220
	<java.lang.Class: java.lang.reflect.Field[] getFields()>: 1
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 13
	<java.security.PrivilegedActionException: java.lang.Throwable getCause()>: 2
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>: 1
	<java.net.Socket: java.net.SocketAddress getLocalSocketAddress()>: 1
	<java.util.LinkedList: java.lang.Object getFirst()>: 9
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 2
	<javax.net.ssl.SSLEngineResult: javax.net.ssl.SSLEngineResult$HandshakeStatus getHandshakeStatus()>: 3
	<java.lang.reflect.Array: int getLength(java.lang.Object)>: 8
	<java.util.BitSet: boolean get(int)>: 5
	<java.lang.StackTraceElement: java.lang.String getClassName()>: 1
	<java.util.Calendar: java.util.TimeZone getTimeZone()>: 1
	<java.nio.charset.CharsetDecoder: java.nio.CharBuffer decode(java.nio.ByteBuffer)>: 1
	<java.lang.Class: java.lang.reflect.Method[] getDeclaredMethods()>: 5
	<java.util.Calendar: int getFirstDayOfWeek()>: 1
	<java.lang.reflect.Field: java.lang.reflect.Type getGenericType()>: 4
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 21
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 4
	<java.net.InetAddress: java.lang.String getHostAddress()>: 2
	<java.text.DateFormat: java.text.DateFormat getDateInstance(int)>: 2
	<java.io.File: java.lang.String getName()>: 1
	<org.apache.http.message.BasicLineParser: org.apache.http.StatusLine parseStatusLine(java.lang.String,org.apache.http.message.LineParser)>: 1
	<java.lang.String: byte[] getBytes()>: 6
	<java.lang.Thread: long getId()>: 1
	<javax.net.ssl.SSLEngineResult: javax.net.ssl.SSLEngineResult$Status getStatus()>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<java.net.DatagramPacket: java.net.InetAddress getAddress()>: 2
	<java.net.URLConnection: java.util.Map getHeaderFields()>: 1
	<java.net.URISyntaxException: java.lang.String getMessage()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 2
	<java.io.File: java.lang.String getParent()>: 3
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<javax.net.ssl.SSLEngine: javax.net.ssl.SSLEngineResult unwrap(java.nio.ByteBuffer,java.nio.ByteBuffer)>: 1
	<java.lang.reflect.Field: java.lang.Class getDeclaringClass()>: 3
	<java.lang.reflect.Constructor: int getModifiers()>: 1
	<java.lang.reflect.Field: java.lang.'annotation'.Annotation getAnnotation(java.lang.Class)>: 12
	<java.lang.Class: java.security.ProtectionDomain getProtectionDomain()>: 4
	<java.text.DateFormat: java.text.DateFormat getDateTimeInstance(int,int)>: 4
	<java.util.GregorianCalendar: java.util.Date getGregorianChange()>: 1
	<java.lang.Throwable: java.lang.String getMessage()>: 2
	<java.lang.Thread: java.lang.Thread$UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()>: 1
	<java.math.BigInteger: byte[] toByteArray()>: 7
	<java.lang.Throwable: java.lang.StackTraceElement[] getStackTrace()>: 1
	<java.util.LinkedHashMap: java.lang.Object get(java.lang.Object)>: 1
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 3
	<java.lang.Throwable: java.lang.Throwable getCause()>: 3
	<java.io.DataInputStream: int read(byte[])>: 2
	<java.lang.StackTraceElement: java.lang.String getMethodName()>: 3
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<java.net.URI: java.lang.String getScheme()>: 6
	<javax.net.SocketFactory: javax.net.SocketFactory getDefault()>: 2
	<java.lang.reflect.InvocationTargetException: java.lang.Throwable getCause()>: 1
	<java.util.ResourceBundle: java.util.ResourceBundle getBundle(java.lang.String,java.util.Locale)>: 4
	<java.lang.Float: float parseFloat(java.lang.String)>: 3
	<java.lang.reflect.Proxy: java.lang.reflect.InvocationHandler getInvocationHandler(java.lang.Object)>: 1
	<java.lang.Class: java.lang.reflect.TypeVariable[] getTypeParameters()>: 2
	<java.util.Calendar: int getMinimalDaysInFirstWeek()>: 1
	<java.util.Date: long getTime()>: 10
	<java.util.EnumMap: java.lang.Object get(java.lang.Object)>: 2
	<java.nio.channels.SelectionKey: boolean isReadable()>: 4
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 6
	<android.content.ComponentName: java.lang.String getClassName()>: 1
	<java.util.TimeZone: java.lang.String getID()>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 14
	<java.lang.Class: java.lang.ClassLoader getClassLoader()>: 11
	<java.util.Currency: java.util.Currency getInstance(java.lang.String)>: 2
	<java.lang.Number: byte byteValue()>: 1
	<java.lang.ClassLoader: java.lang.Class defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)>: 1
	<java.lang.ref.SoftReference: java.lang.Object get()>: 2
	<java.lang.String: void getChars(int,int,char[],int)>: 6
	<java.lang.Class: java.lang.reflect.Type getGenericSuperclass()>: 6
	<java.lang.Byte: byte parseByte(java.lang.String)>: 1
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 1
	<android.database.Cursor: java.lang.String getString(int)>: 4
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<java.lang.Class: java.lang.reflect.Field[] getDeclaredFields()>: 5
	<java.lang.reflect.Method: java.lang.Class getDeclaringClass()>: 14
	<java.io.ObjectStreamClass: java.lang.String getName()>: 2
	<java.lang.reflect.InvocationTargetException: java.lang.Throwable getTargetException()>: 4
	<java.lang.ClassLoader: java.lang.Class loadClass(java.lang.String,boolean)>: 2
	<java.io.ObjectInputStream: java.lang.Object readObject()>: 21
	<java.util.Calendar: int get(int)>: 6
	<java.lang.System: java.lang.String getProperty(java.lang.String)>: 11
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 13
	<java.lang.Enum: java.lang.String name()>: 1
	<java.lang.reflect.Method: java.lang.Class[] getParameterTypes()>: 22
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 1
	<java.security.ProtectionDomain: java.security.CodeSource getCodeSource()>: 2
	<java.util.concurrent.ConcurrentHashMap: java.lang.Object get(java.lang.Object)>: 1
	<java.net.URI: java.lang.String getPath()>: 3
	<java.lang.Class: java.lang.Class getEnclosingClass()>: 3
	<java.nio.channels.SelectionKey: java.lang.Object attachment()>: 4
	<java.net.Socket: java.io.OutputStream getOutputStream()>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 33
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<java.lang.reflect.Array: java.lang.Object newInstance(java.lang.Class,int)>: 10
	<android.telephony.TelephonyManager: java.lang.String getSimCountryIso()>: 1
	<java.nio.ByteBuffer: java.nio.ByteBuffer get(byte[],int,int)>: 3
	<java.lang.reflect.Method: java.lang.String getName()>: 31
	<java.lang.Class: java.lang.'annotation'.Annotation getAnnotation(java.lang.Class)>: 4
	<android.telephony.TelephonyManager: java.lang.String getNetworkCountryIso()>: 1
	<android.content.Intent: int getIntExtra(java.lang.String,int)>: 2
	<java.io.File: java.lang.String getAbsolutePath()>: 3
	<java.net.URI: java.lang.String getQuery()>: 3
	<java.lang.Integer: int parseInt(java.lang.String)>: 12
	<java.net.URLConnection: int getContentLength()>: 2
	<android.net.wifi.WifiManager: android.net.wifi.WifiManager$WifiLock createWifiLock(int,java.lang.String)>: 1
	<java.util.ResourceBundle: java.lang.String getString(java.lang.String)>: 2
	<java.lang.ClassLoader: java.lang.Class loadClass(java.lang.String)>: 4
	<java.lang.StackTraceElement: java.lang.String getFileName()>: 2
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 4
	<java.util.concurrent.atomic.AtomicInteger: int get()>: 1
	<java.lang.reflect.Array: java.lang.Object get(java.lang.Object,int)>: 3
	<java.net.URLConnection: long getHeaderFieldDate(java.lang.String,long)>: 3
	<java.lang.reflect.Field: int getModifiers()>: 8
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<org.apache.http.message.BasicNameValuePair: java.lang.String getName()>: 1
	<org.apache.http.message.BasicNameValuePair: java.lang.String getValue()>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 2
	<java.util.logging.Logger: java.util.logging.Logger getLogger(java.lang.String)>: 7
	<java.nio.CharBuffer: java.lang.String toString()>: 1
	<java.util.Calendar: long getTimeInMillis()>: 1
	<java.lang.Class: java.io.InputStream getResourceAsStream(java.lang.String)>: 1
	<java.lang.reflect.Method: int getModifiers()>: 16
	<java.util.Locale: java.util.Locale getDefault()>: 4
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 3
	<java.net.URI: java.lang.String getHost()>: 6
	<java.lang.Class: java.lang.Object[] getEnumConstants()>: 3
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<java.lang.ThreadLocal: java.lang.Object get()>: 3
	<java.net.URL: java.io.InputStream openStream()>: 1
	<java.net.InetSocketAddress: int getPort()>: 2
	<java.net.NetworkInterface: java.util.Enumeration getInetAddresses()>: 1
	<java.lang.String: byte[] getBytes(java.nio.charset.Charset)>: 1
	<java.text.Collator: java.text.Collator getInstance(java.util.Locale)>: 1
	<java.io.ObjectStreamClass: long getSerialVersionUID()>: 1
	<java.util.LinkedList: java.lang.Object getLast()>: 1
	<java.lang.StackTraceElement: int getLineNumber()>: 3
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 4
	<java.lang.Double: double parseDouble(java.lang.String)>: 16
	<java.lang.Number: short shortValue()>: 1
	<java.lang.Class: java.lang.reflect.Constructor[] getConstructors()>: 4
	<java.net.InetAddress: java.net.InetAddress getByAddress(byte[])>: 2
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<java.net.URLConnection: java.lang.String getContentType()>: 2
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 2
	<java.text.DateFormat: java.text.DateFormat getDateTimeInstance(int,int,java.util.Locale)>: 3
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<java.net.Socket: java.io.InputStream getInputStream()>: 1
	<android.app.admin.DevicePolicyManager: boolean isAdminActive(android.content.ComponentName)>: 1
	<java.net.URI: int getPort()>: 3
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 1
	<java.lang.reflect.Constructor: java.lang.Class[] getParameterTypes()>: 7
	<java.net.URLConnection: int getHeaderFieldInt(java.lang.String,int)>: 1
	<java.util.Calendar: java.lang.Object clone()>: 1
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLSocketFactory getSocketFactory()>: 1
	<java.lang.reflect.Proxy: java.lang.Class getProxyClass(java.lang.ClassLoader,java.lang.Class[])>: 1
	<java.lang.Class: java.lang.reflect.Constructor getDeclaredConstructor(java.lang.Class[])>: 4
	<org.xml.sax.SAXException: java.lang.String getMessage()>: 1
	<java.util.Calendar: java.util.Calendar getInstance(java.util.TimeZone)>: 1
	<java.text.DateFormat: java.text.DateFormat getTimeInstance(int)>: 1
	<java.lang.reflect.Field: java.lang.String getName()>: 48
	<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>: 2
	<java.lang.Long: long parseLong(java.lang.String)>: 10
	<java.lang.Class: int getModifiers()>: 19
	<java.util.concurrent.atomic.AtomicBoolean: boolean get()>: 1
	<java.lang.String: void getBytes(int,int,byte[],int)>: 3
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 8
	<java.lang.Class: java.net.URL getResource(java.lang.String)>: 1
	<java.net.URLConnection: long getDate()>: 1
	<java.lang.Short: short parseShort(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 2
	<java.lang.System: java.lang.SecurityManager getSecurityManager()>: 1
	<android.os.HandlerThread: android.os.Looper getLooper()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.util.TimeZone: java.util.TimeZone getDefault()>: 1
	<java.lang.Class: java.lang.String getSimpleName()>: 32
	<java.net.InetAddress: java.net.InetAddress getByName(java.lang.String)>: 4
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<java.net.URLConnection: long getLastModified()>: 2
	<java.lang.reflect.Constructor: java.lang.Class getDeclaringClass()>: 1
	<java.security.MessageDigest: byte[] digest(byte[])>: 3
	<java.lang.Thread: java.lang.ClassLoader getContextClassLoader()>: 2
	<java.lang.ClassLoader: java.io.InputStream getSystemResourceAsStream(java.lang.String)>: 2
	<java.io.FileInputStream: void <init>: 5
	<java.util.ArrayList: java.lang.Object get(int)>: 25
	<java.lang.Class: java.lang.reflect.Type[] getGenericInterfaces()>: 5
	<java.lang.Character: int getType(char)>: 2
	<java.net.URLConnection: java.lang.String getHeaderField(java.lang.String)>: 3
	<android.telephony.TelephonyManager: java.lang.String getSimOperatorName()>: 1
	<java.lang.reflect.Method: java.lang.Class getReturnType()>: 23
	<android.webkit.WebView: void loadData(java.lang.String,java.lang.String,java.lang.String)>: 2
	<java.lang.reflect.Method: java.lang.'annotation'.Annotation getAnnotation(java.lang.Class)>: 4
	<java.net.Socket: java.net.SocketAddress getRemoteSocketAddress()>: 2
	<android.content.Intent: android.os.Bundle getExtras()>: 1
	<org.xml.sax.SAXParseException: int getLineNumber()>: 1
	<java.net.URLConnection: java.io.InputStream getInputStream()>: 3
	<java.lang.reflect.Field: java.lang.Class getType()>: 22
	<java.net.NetworkInterface: java.util.Enumeration getNetworkInterfaces()>: 1
	<java.net.ServerSocket: int getLocalPort()>: 1
	<java.nio.ByteBuffer: java.nio.ByteBuffer get(byte[])>: 3
	<java.lang.String: byte[] getBytes(java.lang.String)>: 7
	<java.text.DateFormat: java.text.DateFormat getDateInstance(int,java.util.Locale)>: 1
	<java.util.TimeZone: java.util.TimeZone getTimeZone(java.lang.String)>: 3
	<javax.net.ssl.SSLEngine: javax.net.ssl.SSLEngineResult wrap(java.nio.ByteBuffer,java.nio.ByteBuffer)>: 1
	<java.lang.Class: java.lang.String getName()>: 190
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<java.lang.Character: int getType(int)>: 2
	<android.telephony.TelephonyManager: java.lang.String getSimSerialNumber()>: 1
	<java.net.InetAddress: byte[] getAddress()>: 1
	<java.io.File: void <init>: 10
	<org.apache.http.message.BasicLineParser: org.apache.http.Header parseHeader(java.lang.String,org.apache.http.message.LineParser)>: 1
	<java.lang.Class: java.lang.reflect.Method[] getMethods()>: 6
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<java.util.Currency: java.lang.String getCurrencyCode()>: 2
	<java.text.DateFormat: java.util.Date parse(java.lang.String)>: 8
	<java.lang.Class: java.lang.reflect.Constructor getConstructor(java.lang.Class[])>: 18
	<java.lang.Class: java.lang.reflect.Constructor[] getDeclaredConstructors()>: 2
## Sinks: 100
	<android.app.ActivityManager: java.util.List getRunningTasks(int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 62
	<java.lang.ThreadLocal: void set(java.lang.Object)>: 5
	<android.os.Handler: boolean sendEmptyMessageDelayed(int,long)>: 11
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 2
	<java.net.URLConnection: void setIfModifiedSince(long)>: 1
	<android.webkit.WebView: void addJavascriptInterface(java.lang.Object,java.lang.String)>: 8
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 3
	<java.io.PrintStream: void print(java.lang.String)>: 3
	<java.net.URL: java.net.URLConnection openConnection()>: 3
	<java.util.BitSet: void set(int)>: 6
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 2
	<android.webkit.WebSettings: void setBuiltInZoomControls(boolean)>: 2
	<android.util.Log: int d(java.lang.String,java.lang.String,java.lang.Throwable)>: 3
	<java.lang.Math: double log(double)>: 12
	<android.app.Activity: void onCreate(android.os.Bundle)>: 6
	<java.net.Socket: void setSoTimeout(int)>: 1
	<java.util.concurrent.ConcurrentHashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 1
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 16
	<java.util.GregorianCalendar: void setGregorianChange(java.util.Date)>: 1
	<java.net.DatagramSocket: void setSoTimeout(int)>: 2
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 5
	<java.util.Calendar: void setLenient(boolean)>: 1
	<java.nio.ByteBuffer: java.nio.ByteBuffer put(byte[],int,int)>: 4
	<java.lang.StringBuilder: void setCharAt(int,char)>: 1
	<java.io.PrintWriter: void print(java.lang.String)>: 12
	<java.io.ObjectOutputStream: void writeByte(int)>: 1
	<java.text.Collator: void setDecomposition(int)>: 1
	<java.io.ObjectOutputStream: void writeInt(int)>: 7
	<java.util.Calendar: void setTimeInMillis(long)>: 1
	<java.util.zip.GZIPOutputStream: void write(byte[],int,int)>: 1
	<java.io.DataOutputStream: void writeUTF(java.lang.String)>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 10
	<java.util.Calendar: void setMinimalDaysInFirstWeek(int)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 1
	<java.util.EnumMap: java.lang.Object put(java.lang.Enum,java.lang.Object)>: 2
	<android.webkit.WebSettings: void setUseWideViewPort(boolean)>: 2
	<java.lang.Short: short parseShort(java.lang.String)>: 2
	<java.lang.Float: float parseFloat(java.lang.String)>: 3
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.io.OutputStreamWriter: void <init>: 2
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 1
	<java.net.DatagramSocket: void bind(java.net.SocketAddress)>: 2
	<javax.xml.parsers.DocumentBuilderFactory: void setNamespaceAware(boolean)>: 2
	<javax.xml.parsers.DocumentBuilderFactory: void setIgnoringComments(boolean)>: 2
	<java.util.HashSet: boolean add(java.lang.Object)>: 4
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 2
	<java.io.ObjectOutputStream: void writeShort(int)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<java.lang.String: java.lang.String replace(char,char)>: 18
	<java.net.Socket: void setTcpNoDelay(boolean)>: 2
	<java.lang.Byte: byte parseByte(java.lang.String)>: 1
	<java.io.Writer: java.io.Writer append(char)>: 1
	<java.net.URL: void <init>: 3
	<java.io.OutputStream: void write(byte[])>: 6
	<java.io.ObjectOutputStream: void writeBoolean(boolean)>: 9
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 2
	<java.io.DataOutputStream: void writeByte(int)>: 7
	<java.net.URLConnection: java.io.InputStream getInputStream()>: 3
	<java.text.DateFormat: void setTimeZone(java.util.TimeZone)>: 2
	<java.util.logging.Logger: void log(java.util.logging.Level,java.lang.String,java.lang.Throwable)>: 7
	<org.mozilla.javascript.Function: java.lang.Object call(org.mozilla.javascript.Context,org.mozilla.javascript.Scriptable,org.mozilla.javascript.Scriptable,java.lang.Object[])>: 17
	<java.io.Writer: void write(java.lang.String)>: 31
	<java.util.Calendar: void setFirstDayOfWeek(int)>: 1
	<java.text.Collator: void setStrength(int)>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 30
	<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>: 6
	<java.lang.Thread: void setName(java.lang.String)>: 1
	<java.net.DatagramSocket: void send(java.net.DatagramPacket)>: 2
	<java.lang.String: boolean startsWith(java.lang.String)>: 41
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<java.net.Socket: void setKeepAlive(boolean)>: 1
	<java.lang.reflect.Array: void set(java.lang.Object,int,java.lang.Object)>: 6
	<java.lang.reflect.AccessibleObject: void setAccessible(java.lang.reflect.AccessibleObject[],boolean)>: 1
	<java.lang.System: java.lang.String setProperty(java.lang.String,java.lang.String)>: 1
	<java.io.ObjectOutputStream: void writeObject(java.lang.Object)>: 21
	<java.io.PrintWriter: void println(java.lang.String)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 33
	<android.net.http.AndroidHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.webkit.WebView: void setWebChromeClient(android.webkit.WebChromeClient)>: 2
	<java.lang.StringBuilder: void setLength(int)>: 25
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 5
	<android.webkit.WebView: void setWebViewClient(android.webkit.WebViewClient)>: 2
	<java.io.FileOutputStream: void <init>: 6
	<java.io.FileOutputStream: void write(byte[])>: 1
	<java.net.ServerSocket: void bind(java.net.SocketAddress)>: 2
	<android.webkit.WebSettings: void setAllowFileAccess(boolean)>: 2
	<java.util.ArrayList: java.lang.Object set(int,java.lang.Object)>: 2
	<java.lang.reflect.AccessibleObject: void setAccessible(boolean)>: 1
	<java.net.ServerSocket: void setReceiveBufferSize(int)>: 1
	<java.text.DateFormat: java.util.Date parse(java.lang.String)>: 8
	<android.app.Activity: void setTitle(java.lang.CharSequence)>: 4
	<java.lang.Integer: int parseInt(java.lang.String)>: 12
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 2
	<java.lang.Thread: void setDaemon(boolean)>: 1
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 7
