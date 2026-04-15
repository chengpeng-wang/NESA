# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: jollyserv.apk
- **packageName**: fm.xtube
- **targetSdk**: undefined
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: fm.xtube.CheckAgeActivity
# Behavior Information:
## Activities:
- The malware sends device information to a remote server. It contains code which sends SMS messages and installed apps to a remote server. However, it is not clear how the code is executed. 
# Detail Information:
## Activities: 7
	.ListMoviesActivity
	.VideoActivity
	.ListHDMoviesActivity
	.HdActivity
	fm.xtube.CheckAgeActivity
	MainActivity
	.PayActivity
## Services: 1
	sx.jolly.core.JollyService
## Receivers: 2
	sx.jolly.receivers.RebootReceiver
	sx.jolly.receivers.SMSReceiver
## Permissions: 10
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.WRITE_SMS
	android.permission.READ_LOGS
	android.permission.READ_SMS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 200
	<java.lang.Class: java.lang.reflect.Field[] getFields()>: 7
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 9
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>: 1
	<java.net.URL: java.lang.String getHost()>: 1
	<java.lang.StackTraceElement: java.lang.String getClassName()>: 2
	<javax.xml.parsers.DocumentBuilder: org.w3c.dom.Document parse(java.io.InputStream)>: 1
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 15
	<android.webkit.WebView: void loadUrl(java.lang.String)>: 1
	<android.os.StatFs: int getBlockSize()>: 2
	<java.io.File: java.lang.String getName()>: 3
	<android.os.StatFs: int getAvailableBlocks()>: 1
	<android.accounts.AccountManager: android.accounts.AccountManagerFuture getAuthToken(android.accounts.Account,java.lang.String,android.os.Bundle,android.app.Activity,android.accounts.AccountManagerCallback,android.os.Handler)>: 1
	<java.util.Calendar: java.util.Calendar getInstance()>: 1
	<java.lang.String: byte[] getBytes()>: 16
	<java.io.File: java.lang.String getPath()>: 2
	<java.lang.Thread: long getId()>: 3
	<android.view.View: int getId()>: 2
	<android.location.Location: float getAccuracy()>: 1
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[])>: 3
	<android.widget.ExpandableListView: long getExpandableListPosition(int)>: 1
	<android.view.View: android.view.ViewGroup$LayoutParams getLayoutParams()>: 2
	<android.widget.AdapterView: int getFirstVisiblePosition()>: 1
	<android.view.View: android.content.res.Resources getResources()>: 1
	<android.app.ProgressDialog: int getMax()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 18
	<java.lang.Thread: int getPriority()>: 1
	<java.lang.reflect.Field: int getInt(java.lang.Object)>: 28
	<java.lang.Thread: java.lang.ThreadGroup getThreadGroup()>: 2
	<android.content.Intent: java.lang.String getAction()>: 3
	<android.webkit.CookieSyncManager: android.webkit.CookieSyncManager createInstance(android.content.Context)>: 2
	<java.net.URL: java.lang.Object getContent()>: 1
	<java.lang.Throwable: java.lang.String getMessage()>: 3
	<java.lang.Thread: java.lang.Thread$UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()>: 2
	<java.net.URLDecoder: java.lang.String decode(java.lang.String,java.lang.String)>: 1
	<java.lang.Throwable: java.lang.StackTraceElement[] getStackTrace()>: 1
	<android.preference.PreferenceManager: android.content.SharedPreferences getDefaultSharedPreferences(android.content.Context)>: 16
	<android.view.View: java.lang.Object getTag(int)>: 10
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 3
	<java.lang.Class: java.lang.String getCanonicalName()>: 1
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeFileDescriptor(java.io.FileDescriptor,android.graphics.Rect,android.graphics.BitmapFactory$Options)>: 1
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 4
	<java.lang.Throwable: java.lang.Throwable getCause()>: 4
	<java.lang.StackTraceElement: java.lang.String getMethodName()>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<java.net.URL: java.lang.String getQuery()>: 1
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 3
	<java.lang.ThreadGroup: java.lang.String getName()>: 1
	<android.database.Cursor: int getInt(int)>: 2
	<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(int,int,android.graphics.Bitmap$Config)>: 3
	<android.webkit.WebView: void stopLoading()>: 1
	<java.util.WeakHashMap: java.lang.Object get(java.lang.Object)>: 1
	<android.os.Looper: android.os.Looper getMainLooper()>: 4
	<android.location.LocationManager: android.location.Location getLastKnownLocation(java.lang.String)>: 2
	<java.util.Date: long getTime()>: 2
	<android.accounts.AccountManager: java.lang.String blockingGetAuthToken(android.accounts.Account,java.lang.String,boolean)>: 1
	<java.util.EnumMap: java.lang.Object get(java.lang.Object)>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 3
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 10
	<java.lang.Class: java.lang.ClassLoader getClassLoader()>: 1
	<android.os.Parcel: java.util.ArrayList createTypedArrayList(android.os.Parcelable$Creator)>: 1
	<android.database.sqlite.SQLiteDatabase: java.lang.String getPath()>: 1
	<android.widget.ProgressBar: int getMax()>: 1
	<java.lang.ref.SoftReference: java.lang.Object get()>: 1
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 11
	<android.database.Cursor: java.lang.String getString(int)>: 5
	<java.io.FileInputStream: java.nio.channels.FileChannel getChannel()>: 1
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 2
	<android.webkit.CookieManager: android.webkit.CookieManager getInstance()>: 2
	<android.util.Log: java.lang.String getStackTraceString(java.lang.Throwable)>: 4
	<android.telephony.TelephonyManager: int getSimState()>: 1
	<java.io.ObjectInputStream: java.lang.Object readObject()>: 8
	<android.view.View: android.view.ViewParent getParent()>: 2
	<java.util.Calendar: int get(int)>: 1
	<android.graphics.Bitmap: int getHeight()>: 10
	<android.view.Display: float getRefreshRate()>: 1
	<java.lang.reflect.Method: java.lang.Class[] getParameterTypes()>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<android.graphics.Bitmap: int getWidth()>: 10
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 19
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.widget.EditText: android.text.Editable getText()>: 6
	<java.lang.reflect.Method: java.lang.String getName()>: 5
	<android.provider.Settings$System: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 1
	<java.lang.Class: java.lang.'annotation'.Annotation getAnnotation(java.lang.Class)>: 2
	<java.io.File: java.io.File[] listFiles()>: 3
	<java.util.Locale: java.lang.String getLanguage()>: 3
	<java.net.HttpURLConnection: java.io.InputStream getErrorStream()>: 1
	<android.app.Dialog: android.content.Context getContext()>: 2
	<android.widget.ExpandableListView: long getPackedPositionForChild(int,int)>: 1
	<java.io.File: java.lang.String getAbsolutePath()>: 6
	<org.apache.http.HttpHost: java.lang.String getHostName()>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 8
	<android.location.Location: double getLongitude()>: 2
	<android.app.Activity: android.view.Window getWindow()>: 1
	<org.json.JSONArray: org.json.JSONObject getJSONObject(int)>: 6
	<android.view.Display: int getHeight()>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 9
	<android.database.sqlite.SQLiteOpenHelper: android.database.sqlite.SQLiteDatabase getWritableDatabase()>: 2
	<android.util.Xml: org.xmlpull.v1.XmlPullParser newPullParser()>: 1
	<java.lang.reflect.Field: int getModifiers()>: 7
	<java.lang.ClassLoader: java.io.InputStream getResourceAsStream(java.lang.String)>: 1
	<java.net.URL: java.lang.String getPath()>: 1
	<org.apache.http.message.BasicNameValuePair: java.lang.String getValue()>: 8
	<java.net.URL: java.lang.String getRef()>: 1
	<android.view.animation.AnimationUtils: android.view.animation.Animation loadAnimation(android.content.Context,int)>: 2
	<android.location.Location: java.lang.String getProvider()>: 3
	<java.io.FileInputStream: java.io.FileDescriptor getFD()>: 1
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String,java.lang.String)>: 2
	<android.accounts.AccountManager: android.accounts.AccountManager get(android.content.Context)>: 1
	<android.widget.AdapterView: java.lang.Object getSelectedItem()>: 1
	<java.util.Locale: java.util.Locale getDefault()>: 4
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.view.View: android.content.Context getContext()>: 1
	<android.view.Display: void getMetrics(android.util.DisplayMetrics)>: 1
	<android.os.Environment: java.io.File getDataDirectory()>: 2
	<android.view.Display: int getWidth()>: 1
	<org.apache.http.conn.scheme.PlainSocketFactory: org.apache.http.conn.scheme.PlainSocketFactory getSocketFactory()>: 2
	<org.apache.http.impl.client.AbstractHttpClient: org.apache.http.client.HttpRequestRetryHandler getHttpRequestRetryHandler()>: 1
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 4
	<java.lang.ref.Reference: java.lang.Object get()>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 4
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 4
	<android.content.res.Resources: java.lang.String getString(int)>: 7
	<android.view.Display: int getPixelFormat()>: 1
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 1
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 5
	<java.net.URL: int getPort()>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 15
	<android.app.ActivityManager: java.util.List getRunningAppProcesses()>: 1
	<android.os.Environment: java.lang.String getExternalStorageState()>: 1
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLSocketFactory getSocketFactory()>: 2
	<android.graphics.Bitmap: int getRowBytes()>: 2
	<android.os.Bundle: boolean getBoolean(java.lang.String)>: 1
	<android.content.res.Resources: int getIdentifier(java.lang.String,java.lang.String,java.lang.String)>: 1
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>: 1
	<android.app.Activity: android.view.LayoutInflater getLayoutInflater()>: 1
	<java.lang.reflect.Field: java.lang.String getName()>: 29
	<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>: 8
	<java.lang.Long: long parseLong(java.lang.String)>: 3
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 1
	<java.io.File: java.io.File getParentFile()>: 1
	<android.content.res.Resources: android.content.res.Configuration getConfiguration()>: 1
	<android.content.Context: java.lang.String getString(int)>: 4
	<android.webkit.WebView: android.webkit.WebSettings getSettings()>: 4
	<android.location.Location: double getLatitude()>: 2
	<android.net.Uri: java.lang.String getQueryParameter(java.lang.String)>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<java.io.FileOutputStream: java.nio.channels.FileChannel getChannel()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 12
	<java.lang.Class: java.lang.String getSimpleName()>: 5
	<android.view.View: int getVisibility()>: 3
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<android.widget.ExpandableListView: int getPackedPositionChild(long)>: 1
	<android.app.Activity: android.content.ComponentName getComponentName()>: 1
	<java.io.FileInputStream: void <init>: 10
	<java.util.ArrayList: java.lang.Object get(int)>: 8
	<android.widget.TextView: java.lang.CharSequence getText()>: 1
	<android.accounts.AccountManager: android.accounts.Account[] getAccountsByType(java.lang.String)>: 2
	<java.net.URL: java.lang.String getProtocol()>: 1
	<java.net.HttpURLConnection: java.lang.String getResponseMessage()>: 1
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 10
	<android.telephony.TelephonyManager: java.lang.String getSimOperatorName()>: 1
	<java.lang.Integer: java.lang.Integer getInteger(java.lang.String)>: 1
	<android.webkit.WebView: void loadData(java.lang.String,java.lang.String,java.lang.String)>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 6
	<org.apache.http.HttpHost: int getPort()>: 2
	<android.database.CursorWindow: int getNumRows()>: 1
	<java.security.MessageDigest: byte[] digest()>: 1
	<android.widget.AdapterView: int getSelectedItemPosition()>: 1
	<android.content.Context: java.lang.CharSequence getText(int)>: 3
	<android.content.Context: java.lang.String getString(int,java.lang.Object[])>: 1
	<java.net.HttpURLConnection: java.lang.String getContentEncoding()>: 1
	<java.lang.reflect.Field: java.lang.Class getType()>: 3
	<java.lang.Thread: java.lang.String getName()>: 2
	<java.lang.String: byte[] getBytes(java.lang.String)>: 3
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor query(java.lang.String,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 1
	<android.webkit.WebView: void loadDataWithBaseURL(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)>: 1
	<java.lang.Class: java.lang.String getName()>: 3
	<android.os.StatFs: int getBlockCount()>: 1
	<android.location.Location: long getTime()>: 5
	<android.view.View: java.lang.Object getTag()>: 5
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 6
	<android.database.Cursor: long getLong(int)>: 4
	<java.util.Locale: java.lang.String getCountry()>: 2
	<android.view.ViewGroup: android.view.View getChildAt(int)>: 1
	<java.io.File: boolean delete()>: 6
	<org.apache.http.conn.ssl.SSLSocketFactory: org.apache.http.conn.ssl.SSLSocketFactory getSocketFactory()>: 3
	<org.apache.http.params.HttpConnectionParams: int getSoTimeout(org.apache.http.params.HttpParams)>: 2
	<android.widget.ExpandableListView: android.widget.ExpandableListAdapter getExpandableListAdapter()>: 2
	<java.io.File: void <init>: 13
	<android.location.LocationManager: boolean isProviderEnabled(java.lang.String)>: 2
	<java.lang.Class: java.lang.reflect.Method[] getMethods()>: 1
	<android.os.Looper: java.lang.Thread getThread()>: 1
	<android.util.SparseArray: java.lang.Object get(int)>: 12
	<java.lang.Class: java.lang.reflect.Constructor getConstructor(java.lang.Class[])>: 1
	<org.apache.http.params.HttpConnectionParams: int getConnectionTimeout(org.apache.http.params.HttpParams)>: 1
	<android.widget.ExpandableListView: int getPackedPositionGroup(long)>: 1
## Sinks: 159
	<android.view.View: void setOnLongClickListener(android.view.View$OnLongClickListener)>: 1
	<android.widget.Gallery: void setCallbackDuringFling(boolean)>: 1
	<android.app.Activity: void setProgressBarVisibility(boolean)>: 4
	<org.apache.http.impl.cookie.BasicClientCookie: void setPath(java.lang.String)>: 1
	<javax.xml.parsers.DocumentBuilder: org.w3c.dom.Document parse(java.io.InputStream)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 3
	<android.widget.AdapterView: void setOnItemClickListener(android.widget.AdapterView$OnItemClickListener)>: 1
	<android.view.View: void setTag(java.lang.Object)>: 3
	<android.view.View: void setEnabled(boolean)>: 1
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 6
	<android.content.Intent: android.content.Intent setType(java.lang.String)>: 1
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 3
	<java.io.DataOutputStream: void write(byte[])>: 1
	<android.widget.ListView: void setAdapter(android.widget.ListAdapter)>: 2
	<android.widget.ImageView: void setImageMatrix(android.graphics.Matrix)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 4
	<org.apache.http.conn.params.ConnManagerParams: void setMaxTotalConnections(org.apache.http.params.HttpParams,int)>: 1
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<android.app.Activity: void setProgressBarIndeterminateVisibility(boolean)>: 4
	<android.webkit.WebView: void setBackgroundColor(int)>: 3
	<android.graphics.drawable.TransitionDrawable: void setCrossFadeEnabled(boolean)>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<android.webkit.WebSettings: void setJavaScriptEnabled(boolean)>: 3
	<android.widget.ProgressBar: void setProgress(int)>: 5
	<android.webkit.WebSettings: void setBuiltInZoomControls(boolean)>: 1
	<android.widget.ImageView: void setScaleType(android.widget.ImageView$ScaleType)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String,java.lang.Throwable)>: 2
	<android.widget.RatingBar: void setRating(float)>: 1
	<org.apache.http.params.HttpConnectionParams: void setTcpNoDelay(org.apache.http.params.HttpParams,boolean)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 8
	<android.widget.ImageView: void setImageBitmap(android.graphics.Bitmap)>: 8
	<android.util.Log: int i(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<org.apache.http.params.HttpConnectionParams: void setSocketBufferSize(org.apache.http.params.HttpParams,int)>: 3
	<android.widget.MediaController: void setAnchorView(android.view.View)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 10
	<java.util.concurrent.ConcurrentHashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 4
	<org.apache.http.params.HttpProtocolParams: void setVersion(org.apache.http.params.HttpParams,org.apache.http.ProtocolVersion)>: 1
	<android.app.ProgressDialog: void setMax(int)>: 2
	<java.util.TreeSet: boolean add(java.lang.Object)>: 2
	<android.widget.ExpandableListView: void setAdapter(android.widget.ExpandableListAdapter)>: 1
	<android.widget.AbsListView: void setOnScrollListener(android.widget.AbsListView$OnScrollListener)>: 2
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 5
	<android.os.Bundle: void putAll(android.os.Bundle)>: 1
	<android.widget.VideoView: void setOnPreparedListener(android.media.MediaPlayer$OnPreparedListener)>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 4
	<org.apache.http.impl.client.AbstractHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest,org.apache.http.protocol.HttpContext)>: 1
	<java.lang.Thread: void setDefaultUncaughtExceptionHandler(java.lang.Thread$UncaughtExceptionHandler)>: 3
	<org.apache.http.conn.params.ConnManagerParams: void setTimeout(org.apache.http.params.HttpParams,long)>: 2
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 28
	<android.content.Intent: android.content.Intent setFlags(int)>: 2
	<android.widget.GridView: void setAdapter(android.widget.ListAdapter)>: 2
	<android.accounts.AccountManager: void invalidateAuthToken(java.lang.String,java.lang.String)>: 1
	<java.io.OutputStreamWriter: void write(java.lang.String)>: 1
	<java.net.HttpURLConnection: void connect()>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String,java.lang.Throwable)>: 14
	<android.view.View: void setLayoutParams(android.view.ViewGroup$LayoutParams)>: 2
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 11
	<org.apache.http.impl.cookie.BasicClientCookie: void setDomain(java.lang.String)>: 1
	<android.webkit.WebSettings: void setSupportZoom(boolean)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String[])>: 1
	<java.io.ObjectOutputStream: void writeInt(int)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 5
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<java.lang.Long: long parseLong(java.lang.String)>: 3
	<android.content.Intent: android.content.Intent setClassName(java.lang.String,java.lang.String)>: 2
	<android.widget.CompoundButton: void setChecked(boolean)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 15
	<org.apache.http.conn.scheme.SchemeRegistry: org.apache.http.conn.scheme.Scheme register(org.apache.http.conn.scheme.Scheme)>: 8
	<android.content.ContentValues: void put(java.lang.String,java.lang.Long)>: 2
	<android.widget.VideoView: void setVideoURI(android.net.Uri)>: 1
	<android.app.Activity: void startActivityForResult(android.content.Intent,int)>: 2
	<java.util.concurrent.ThreadPoolExecutor: java.util.concurrent.Future submit(java.lang.Runnable)>: 1
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 15
	<android.widget.ProgressBar: void setMax(int)>: 4
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 12
	<java.io.File: boolean setLastModified(long)>: 1
	<android.view.View: void setBackgroundColor(int)>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.io.OutputStreamWriter: void <init>: 1
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 1
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 2
	<android.view.View: void setBackgroundResource(int)>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 11
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 15
	<android.view.View: void setBackgroundDrawable(android.graphics.drawable.Drawable)>: 1
	<java.util.HashSet: boolean add(java.lang.Object)>: 5
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 3
	<android.widget.ProgressBar: void setVisibility(int)>: 3
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 2
	<android.app.Activity: void startActivity(android.content.Intent)>: 1
	<java.lang.String: java.lang.String replace(char,char)>: 1
	<android.widget.TextView: void setTypeface(android.graphics.Typeface)>: 1
	<android.view.View: void setClickable(boolean)>: 1
	<org.apache.http.client.methods.HttpEntityEnclosingRequestBase: void setEntity(org.apache.http.HttpEntity)>: 5
	<android.app.Activity: void setProgress(int)>: 5
	<java.net.URL: void <init>: 8
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 10
	<java.io.OutputStream: void write(byte[])>: 2
	<java.io.ObjectOutputStream: void writeBoolean(boolean)>: 1
	<android.widget.TextView: void setText(int)>: 1
	<java.io.File: boolean setWritable(boolean,boolean)>: 2
	<android.content.Context: void startActivity(android.content.Intent)>: 2
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<android.util.Log: int w(java.lang.String,java.lang.Throwable)>: 1
	<android.widget.VideoView: void setOnErrorListener(android.media.MediaPlayer$OnErrorListener)>: 1
	<android.view.View: void setTag(int,java.lang.Object)>: 19
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 6
	<android.widget.TextView: void setTextColor(int)>: 1
	<android.util.Log: int v(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 42
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<java.io.File: boolean setReadable(boolean,boolean)>: 2
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 2
	<android.webkit.WebView: void setPictureListener(android.webkit.WebView$PictureListener)>: 2
	<android.widget.Toast: void setGravity(int,int,int)>: 1
	<android.view.animation.Animation: void setInterpolator(android.view.animation.Interpolator)>: 1
	<android.app.AlertDialog: void setMessage(java.lang.CharSequence)>: 1
	<android.view.Window: void setFlags(int,int)>: 2
	<android.location.LocationManager: void requestLocationUpdates(java.lang.String,long,float,android.location.LocationListener,android.os.Looper)>: 2
	<android.widget.TextView: void setTextSize(float)>: 1
	<android.view.animation.Animation: void setAnimationListener(android.view.animation.Animation$AnimationListener)>: 1
	<android.view.View: void setOnClickListener(android.view.View$OnClickListener)>: 1
	<java.io.ObjectOutputStream: void writeObject(java.lang.Object)>: 8
	<android.widget.VideoView: void setMediaController(android.widget.MediaController)>: 1
	<android.graphics.Matrix: void setScale(float,float)>: 1
	<java.io.DataOutputStream: void flush()>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 19
	<org.apache.http.conn.params.ConnManagerParams: void setMaxConnectionsPerRoute(org.apache.http.params.HttpParams,org.apache.http.conn.params.ConnPerRoute)>: 2
	<android.widget.ImageView: void setImageResource(int)>: 2
	<org.apache.http.impl.cookie.BasicClientCookie: void setVersion(int)>: 1
	<android.widget.AdapterView: void setOnItemSelectedListener(android.widget.AdapterView$OnItemSelectedListener)>: 1
	<android.view.View: void setVisibility(int)>: 13
	<java.io.File: boolean delete()>: 6
	<java.lang.StringBuilder: void setLength(int)>: 1
	<android.view.Window: void setFormat(int)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 14
	<org.apache.http.impl.cookie.BasicClientCookie: void setExpiryDate(java.util.Date)>: 1
	<org.apache.http.impl.cookie.BasicClientCookie: void setComment(java.lang.String)>: 1
	<org.apache.http.impl.cookie.BasicClientCookie: void setSecure(boolean)>: 1
	<android.webkit.WebView: void setWebViewClient(android.webkit.WebViewClient)>: 4
	<android.view.animation.Animation: void setStartTime(long)>: 1
	<java.io.FileOutputStream: void write(byte[])>: 3
	<java.io.FileOutputStream: void <init>: 4
	<android.widget.ImageView: void setImageDrawable(android.graphics.drawable.Drawable)>: 7
	<android.view.animation.Animation: void setDuration(long)>: 1
	<org.apache.http.params.HttpProtocolParams: void setUserAgent(org.apache.http.params.HttpParams,java.lang.String)>: 2
	<java.net.HttpURLConnection: void setInstanceFollowRedirects(boolean)>: 1
	<android.app.ProgressDialog: void setProgress(int)>: 3
	<android.widget.LinearLayout: void setOrientation(int)>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 8
	<android.graphics.Paint: android.graphics.Xfermode setXfermode(android.graphics.Xfermode)>: 1
	<android.widget.TextView: void setPadding(int,int,int,int)>: 2
	<android.os.Handler: boolean sendMessageDelayed(android.os.Message,long)>: 4
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<android.location.LocationManager: void removeUpdates(android.location.LocationListener)>: 3
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 11

