# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: remote_control_smack.apk
- **packageName**: com.android.service
- **targetSdk**: undefined
- **minSdk**: 9
- **maxSdk**: undefined
- **mainActivity**: com.android.service.ActivityLogin
# Behavior Information:
## BroadcastReceivers:
- AlarmReceiver monitors GPS location information.
- SMSReceive monitors incoming SMS messages.
- PhoneBroadcastReceiver monitors phone calls.
## Services:
- XmppService receives commands from remote server and executes malicious functions such as uploading files, SMS messages, contact information, sound recording, GPS location and other information. 
- RecordingService records audio and uploads to remote server. 
# Detail Information:
## Activities: 11
	com.android.service.view.SMSViewActivity
	com.android.service.view.CallRecordViewActivity
	com.android.service.view.MapBaidu
	com.android.service.view.PhotoRecord
	com.android.service.view.MapGoogle
	com.android.service.view.CalendarViewActivity
	com.android.service.MainControlActivity
	com.android.service.ActivityLogin
	com.android.service.view.ContactsViewActivity
	com.android.service.view.AudioRecordView
	com.android.service.view.MapSelectListActivity
## Services: 4
	com.android.service.RecordingService
	com.baidu.location.f
	com.android.service.XmppService
	com.android.service.RecorderService
## Receivers: 5
	com.android.service.AlarmReceiver
	com.android.service.SMSReceive
	com.android.service.BootReceiver
	com.android.service.GetPackageBroadcast
	com.android.service.PhoneBroadcastReceiver
## Permissions: 38
	android.permission.READ_LOGS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.GET_ACCOUNTS
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.READ_CALENDAR
	android.permission.RECEIVE_BOOT_COMPLETED
	com.android.browser.permission.READ_HISTORY_BOOKMARKS
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_FINE_LOCATION
	android.permission.ACCESS_LOCATION
	android.permission.ACCESS_COARSE_LOCATION
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WAKE_LOCK
	android.permission.PROCESS_OUTGOING_CALLS
	android.permission.ACCESS_WIFI_STATE
	android.permission.WRITE_CONTACTS
	android.permission.SEND_SMS
	android.permission.RECEIVE_SMS
	android.permission.DISABLE_KEYGUARD
	android.permission.WRITE_SMS
	android.permission.VIBRATE
	com.android.email.permission.ACCESS_PROVIDER
	android.permission.RECORD_VIDEO
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.RECORD_AUDIO
	android.permission.BROADCAST_STICKY
	android.permission.RESTART_PACKAGES
	android.permission.CHANGE_WIFI_STATE
	com.android.launcher.action.INSTALL_SHORTCUT
	android.permission.INTERNET
	android.permission.CHANGE_NETWORK_STATE
	android.permission.MODIFY_AUDIO_SETTINGS
	android.permission.CAMERA
	android.permission.BLUETOOTH
	android.permission.WRITE_SETTINGS
	android.permission.READ_PHONE_STATE
## Sources: 278
	<android.view.ScaleGestureDetector: float getFocusX()>: 1
	<android.os.Message: android.os.Handler getTarget()>: 2
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 18
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>: 2
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 3
	<java.util.BitSet: boolean get(int)>: 4
	<java.net.InetAddress: java.lang.String getHostName()>: 1
	<android.view.KeyEvent: int getAction()>: 4
	<javax.xml.parsers.DocumentBuilder: org.w3c.dom.Document parse(java.io.InputStream)>: 1
	<java.util.Calendar: java.util.TimeZone getTimeZone()>: 3
	<android.os.Messenger: android.os.IBinder getBinder()>: 1
	<android.telephony.gsm.GsmCellLocation: int getLac()>: 2
	<android.view.View: int getMeasuredHeight()>: 1
	<android.app.ActivityManager: void getMemoryInfo(android.app.ActivityManager$MemoryInfo)>: 1
	<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(android.graphics.Bitmap,int,int,int,int,android.graphics.Matrix,boolean)>: 2
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 9
	<java.net.InetAddress: java.lang.String getHostAddress()>: 1
	<android.view.ViewConfiguration: int getScaledMinimumFlingVelocity()>: 1
	<android.os.StatFs: int getBlockSize()>: 5
	<android.location.Location: float getBearing()>: 3
	<java.lang.ClassLoader: java.util.Enumeration getResources(java.lang.String)>: 2
	<java.io.File: java.lang.String getName()>: 7
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 1
	<android.os.StatFs: int getAvailableBlocks()>: 3
	<org.apache.commons.codec.binary.Base64: byte[] discardWhitespace(byte[])>: 1
	<android.content.Intent: android.content.Intent parseUri(java.lang.String,int)>: 1
	<android.view.ViewGroup: android.view.ViewGroup$LayoutParams generateDefaultLayoutParams()>: 1
	<javax.xml.transform.TransformerException: java.lang.Throwable getException()>: 2
	<java.lang.String: byte[] getBytes()>: 23
	<java.util.Calendar: java.util.Calendar getInstance()>: 6
	<android.os.AsyncTask: java.lang.Object get(long,java.util.concurrent.TimeUnit)>: 1
	<java.io.File: java.lang.String getPath()>: 7
	<android.view.View: int getId()>: 1
	<android.location.Location: float getAccuracy()>: 2
	<android.app.Activity: android.view.WindowManager getWindowManager()>: 5
	<android.telephony.gsm.GsmCellLocation: int getCid()>: 2
	<android.net.wifi.WifiInfo: java.lang.String getMacAddress()>: 1
	<android.database.sqlite.SQLiteDatabase: android.database.Cursor rawQuery(java.lang.String,java.lang.String[])>: 1
	<java.net.URISyntaxException: java.lang.String getMessage()>: 1
	<android.view.View: android.view.ViewGroup$LayoutParams getLayoutParams()>: 4
	<java.net.Socket: java.net.InetAddress getInetAddress()>: 1
	<java.io.File: java.lang.String getParent()>: 1
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 3
	<java.lang.reflect.Field: java.lang.Class getDeclaringClass()>: 1
	<android.os.Bundle: java.lang.String[] getStringArray(java.lang.String)>: 3
	<java.util.AbstractMap: java.lang.Object clone()>: 1
	<java.util.zip.ZipInputStream: java.util.zip.ZipEntry getNextEntry()>: 3
	<android.content.Intent: java.lang.String getAction()>: 6
	<java.net.URL: java.lang.Object getContent()>: 2
	<android.view.MotionEvent: float getY(int)>: 4
	<android.net.NetworkInfo: java.lang.String getExtraInfo()>: 4
	<java.net.URLDecoder: java.lang.String decode(java.lang.String,java.lang.String)>: 1
	<java.lang.ClassLoader: java.net.URL getResource(java.lang.String)>: 10
	<java.util.concurrent.FutureTask: java.lang.Object get(long,java.util.concurrent.TimeUnit)>: 1
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 1
	<android.widget.OverScroller: int getCurrX()>: 1
	<android.os.Bundle: int getInt(java.lang.String)>: 27
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.view.ScaleGestureDetector: float getScaleFactor()>: 1
	<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(int,int,android.graphics.Bitmap$Config)>: 3
	<android.database.Cursor: int getInt(int)>: 4
	<android.view.KeyEvent: int getKeyCode()>: 2
	<android.app.Dialog: android.view.Window getWindow()>: 1
	<java.lang.Float: float parseFloat(java.lang.String)>: 20
	<android.telephony.NeighboringCellInfo: int getCid()>: 4
	<android.location.LocationManager: android.location.Location getLastKnownLocation(java.lang.String)>: 8
	<java.net.InetAddress: java.net.InetAddress getLocalHost()>: 1
	<android.view.MotionEvent: int getPointerCount()>: 1
	<java.util.Date: long getTime()>: 5
	<android.view.VelocityTracker: float getYVelocity()>: 1
	<org.json.JSONObject: org.json.JSONObject getJSONObject(java.lang.String)>: 15
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 1
	<android.telephony.TelephonyManager: java.util.List getNeighboringCellInfo()>: 1
	<android.content.ComponentName: java.lang.String getClassName()>: 1
	<java.util.TimeZone: java.lang.String getID()>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 6
	<android.net.NetworkInfo: int getType()>: 6
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 4
	<android.graphics.drawable.BitmapDrawable: android.graphics.Bitmap getBitmap()>: 7
	<java.lang.Class: java.lang.ClassLoader getClassLoader()>: 2
	<android.location.Address: java.lang.String getAdminArea()>: 2
	<android.location.Location: float getSpeed()>: 5
	<android.widget.Scroller: int getCurrY()>: 1
	<java.lang.ref.SoftReference: java.lang.Object get()>: 1
	<java.lang.String: void getChars(int,int,char[],int)>: 1
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 88
	<android.view.ScaleGestureDetector: float getFocusY()>: 1
	<android.view.ViewConfiguration: int getMinimumFlingVelocity()>: 2
	<android.database.Cursor: java.lang.String getString(int)>: 20
	<org.apache.commons.codec.binary.Base64: byte[] discardNonBase64(byte[])>: 1
	<android.os.Bundle: boolean getBoolean(java.lang.String,boolean)>: 3
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 22
	<android.os.Bundle: float getFloat(java.lang.String)>: 3
	<java.lang.Class: java.lang.reflect.Field[] getDeclaredFields()>: 1
	<org.apache.commons.codec.binary.Base64: byte[] decode(byte[])>: 3
	<android.util.Log: java.lang.String getStackTraceString(java.lang.Throwable)>: 33
	<android.telephony.TelephonyManager: java.lang.String getSubscriberId()>: 3
	<android.telephony.TelephonyManager: int getSimState()>: 2
	<java.io.RandomAccessFile: boolean readBoolean()>: 3
	<android.os.Environment: java.io.File getRootDirectory()>: 2
	<android.view.View: int getMeasuredWidth()>: 1
	<android.content.ContentResolver: java.io.InputStream openInputStream(android.net.Uri)>: 3
	<java.io.ObjectInputStream: java.lang.Object readObject()>: 8
	<android.view.View: android.view.ViewParent getParent()>: 2
	<java.util.Calendar: int get(int)>: 18
	<java.lang.System: java.lang.String getProperty(java.lang.String)>: 4
	<android.graphics.Bitmap: int getHeight()>: 24
	<android.view.VelocityTracker: float getYVelocity(int)>: 2
	<java.lang.Boolean: boolean getBoolean(java.lang.String)>: 1
	<java.util.TimeZone: int getOffset(long)>: 3
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 7
	<android.graphics.drawable.Drawable: android.graphics.Rect getBounds()>: 1
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream,android.graphics.Rect,android.graphics.BitmapFactory$Options)>: 4
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 14
	<android.graphics.Bitmap: int getWidth()>: 30
	<android.view.ViewConfiguration: int getScaledTouchSlop()>: 1
	<java.net.Socket: java.io.OutputStream getOutputStream()>: 6
	<android.graphics.Paint: android.graphics.Paint$FontMetrics getFontMetrics()>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 17
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.os.Bundle: long getLong(java.lang.String)>: 6
	<java.security.KeyStore: void load(java.io.InputStream,char[])>: 2
	<android.widget.EditText: android.text.Editable getText()>: 2
	<android.provider.Settings$System: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 2
	<java.io.File: java.io.File[] listFiles()>: 1
	<java.util.Locale: java.lang.String getLanguage()>: 1
	<android.view.VelocityTracker: float getXVelocity(int)>: 2
	<android.content.Intent: int getIntExtra(java.lang.String,int)>: 2
	<java.io.File: java.lang.String getAbsolutePath()>: 19
	<java.text.NumberFormat: java.lang.String format(long)>: 4
	<android.telephony.SmsMessage: java.lang.String getDisplayMessageBody()>: 1
	<android.os.Bundle: int getInt(java.lang.String,int)>: 4
	<android.view.MotionEvent: float getX()>: 21
	<java.lang.Integer: int parseInt(java.lang.String)>: 59
	<java.security.KeyStore: java.security.KeyStore getInstance(java.lang.String)>: 2
	<java.net.URLConnection: int getContentLength()>: 1
	<java.util.zip.ZipEntry: java.lang.String getName()>: 5
	<android.location.Location: double getLongitude()>: 20
	<org.json.JSONObject: org.json.JSONArray getJSONArray(java.lang.String)>: 9
	<org.json.JSONArray: org.json.JSONObject getJSONObject(int)>: 17
	<android.view.Display: int getHeight()>: 2
	<android.net.wifi.WifiManager: android.net.wifi.WifiInfo getConnectionInfo()>: 2
	<android.webkit.MimeTypeMap: java.lang.String getFileExtensionFromUrl(java.lang.String)>: 1
	<java.lang.reflect.Field: int getModifiers()>: 1
	<java.net.URL: java.lang.String getPath()>: 1
	<android.os.Message: android.os.Bundle getData()>: 24
	<android.view.MotionEvent: int getPointerId(int)>: 5
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 5
	<android.location.Geocoder: java.util.List getFromLocation(double,double,int)>: 2
	<java.net.Socket: int getPort()>: 1
	<java.io.Reader: long skip(long)>: 1
	<java.security.KeyStore: java.lang.String getDefaultType()>: 1
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 5
	<java.util.Calendar: long getTimeInMillis()>: 4
	<java.security.cert.CertificateFactory: java.security.cert.CertificateFactory getInstance(java.lang.String)>: 1
	<android.media.ExifInterface: int getAttributeInt(java.lang.String,int)>: 1
	<java.util.Locale: java.util.Locale getDefault()>: 1
	<java.io.RandomAccessFile: int readInt()>: 53
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 7
	<android.graphics.drawable.Drawable: int getIntrinsicHeight()>: 2
	<org.json.JSONObject: long getLong(java.lang.String)>: 2
	<java.net.URL: java.io.InputStream openStream()>: 3
	<java.security.cert.X509Certificate: java.util.Collection getSubjectAlternativeNames()>: 1
	<android.view.Display: void getMetrics(android.util.DisplayMetrics)>: 5
	<java.lang.Long: long parseLong(java.lang.String,int)>: 2
	<android.location.Location: double getAltitude()>: 1
	<android.widget.Scroller: int getCurrX()>: 1
	<android.view.Display: int getWidth()>: 2
	<android.os.Environment: java.io.File getDataDirectory()>: 1
	<android.content.Intent: android.content.Intent getIntent(java.lang.String)>: 1
	<java.lang.ref.Reference: java.lang.Object get()>: 2
	<java.util.concurrent.atomic.AtomicInteger: int getAndIncrement()>: 2
	<java.lang.Double: double parseDouble(java.lang.String)>: 20
	<android.telephony.NeighboringCellInfo: int getLac()>: 4
	<android.view.MotionEvent: int getAction()>: 28
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 3
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 6
	<android.os.Bundle: android.os.Bundle getBundle(java.lang.String)>: 4
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 3
	<android.provider.Settings$Secure: java.lang.String getString(android.content.ContentResolver,java.lang.String)>: 2
	<java.net.HttpURLConnection: int getResponseCode()>: 2
	<android.location.GpsStatus: java.lang.Iterable getSatellites()>: 1
	<android.view.MotionEvent: float getY()>: 18
	<android.view.ViewConfiguration: int getMaximumFlingVelocity()>: 1
	<android.os.Bundle: float getFloat(java.lang.String,float)>: 1
	<java.util.Calendar: java.util.Date getTime()>: 2
	<android.provider.Settings$System: int getInt(android.content.ContentResolver,java.lang.String)>: 3
	<java.net.Socket: java.io.InputStream getInputStream()>: 6
	<java.util.zip.ZipFile: java.io.InputStream getInputStream(java.util.zip.ZipEntry)>: 1
	<android.webkit.MimeTypeMap: android.webkit.MimeTypeMap getSingleton()>: 1
	<android.content.ContentProviderClient: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 49
	<android.widget.ImageView: android.graphics.drawable.Drawable getDrawable()>: 7
	<android.graphics.drawable.Drawable: int getIntrinsicWidth()>: 2
	<android.os.Environment: java.lang.String getExternalStorageState()>: 5
	<javax.net.ssl.SSLContext: javax.net.ssl.SSLSocketFactory getSocketFactory()>: 3
	<android.os.Bundle: boolean getBoolean(java.lang.String)>: 4
	<android.graphics.BitmapFactory: android.graphics.Bitmap decodeStream(java.io.InputStream)>: 4
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 7
	<java.lang.Long: long parseLong(java.lang.String)>: 3
	<java.io.File: java.io.File getParentFile()>: 4
	<android.widget.ImageView: android.widget.ImageView$ScaleType getScaleType()>: 1
	<android.graphics.Matrix: void getValues(float[])>: 1
	<org.json.JSONArray: java.lang.Object get(int)>: 3
	<android.content.Context: java.lang.String getString(int)>: 8
	<android.net.Proxy: java.lang.String getDefaultHost()>: 11
	<android.content.pm.Signature: byte[] toByteArray()>: 2
	<org.apache.commons.codec.binary.Base64: byte[] encode(byte[])>: 1
	<org.json.JSONObject: int getInt(java.lang.String)>: 14
	<android.location.Location: double getLatitude()>: 18
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 8
	<android.os.HandlerThread: android.os.Looper getLooper()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 25
	<java.util.TimeZone: java.util.TimeZone getDefault()>: 1
	<android.graphics.Bitmap: void getPixels(int[],int,int,int,int,int,int)>: 1
	<java.lang.Class: java.lang.String getSimpleName()>: 2
	<android.os.Bundle: android.os.Parcelable[] getParcelableArray(java.lang.String)>: 4
	<android.net.Proxy: int getDefaultPort()>: 1
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 1
	<android.app.AlertDialog$Builder: android.app.AlertDialog show()>: 20
	<android.view.MotionEvent: long getDownTime()>: 2
	<android.telephony.SignalStrength: int getCdmaDbm()>: 1
	<java.security.KeyStore: java.lang.String getCertificateAlias(java.security.cert.Certificate)>: 1
	<java.security.MessageDigest: byte[] digest(byte[])>: 3
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperator()>: 1
	<java.lang.Thread: java.lang.ClassLoader getContextClassLoader()>: 12
	<java.io.RandomAccessFile: byte readByte()>: 2
	<org.json.JSONArray: org.json.JSONArray put(int,java.lang.Object)>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<java.io.FileInputStream: void <init>: 13
	<java.util.ArrayList: java.lang.Object get(int)>: 108
	<java.text.DateFormat: java.text.DateFormat getDateTimeInstance()>: 1
	<android.telephony.TelephonyManager: android.telephony.CellLocation getCellLocation()>: 7
	<java.io.RandomAccessFile: double readDouble()>: 4
	<android.widget.TextView: java.lang.CharSequence getText()>: 1
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 45
	<android.content.Intent: android.os.Bundle getExtras()>: 4
	<java.security.MessageDigest: byte[] digest()>: 4
	<android.view.MotionEvent: float getX(int)>: 4
	<java.util.Hashtable: java.lang.Object get(java.lang.Object)>: 1
	<java.net.ServerSocket: int getLocalPort()>: 1
	<android.view.MotionEvent: long getEventTime()>: 6
	<java.lang.String: byte[] getBytes(java.lang.String)>: 18
	<java.lang.reflect.Array: java.lang.Object newInstance(java.lang.Class,int[])>: 4
	<android.net.NetworkInfo: android.net.NetworkInfo$State getState()>: 2
	<android.widget.OverScroller: int getCurrY()>: 1
	<java.util.TimeZone: java.util.TimeZone getTimeZone(java.lang.String)>: 8
	<android.location.Address: java.lang.String getAddressLine(int)>: 3
	<java.lang.Class: java.lang.String getName()>: 35
	<android.telephony.SignalStrength: int getGsmSignalStrength()>: 1
	<android.os.StatFs: int getBlockCount()>: 2
	<android.location.Location: long getTime()>: 2
	<android.net.wifi.WifiInfo: java.lang.String getBSSID()>: 2
	<android.view.View: java.lang.Object getTag()>: 2
	<android.location.LocationManager: android.location.GpsStatus getGpsStatus(android.location.GpsStatus)>: 2
	<android.database.Cursor: long getLong(int)>: 3
	<android.net.wifi.WifiManager: java.util.List getScanResults()>: 4
	<java.io.RandomAccessFile: float readFloat()>: 1
	<android.view.ViewGroup: android.view.View getChildAt(int)>: 2
	<android.view.ViewConfiguration: android.view.ViewConfiguration get(android.content.Context)>: 1
	<java.io.File: boolean delete()>: 7
	<java.io.RandomAccessFile: long readLong()>: 5
	<android.os.Bundle: double getDouble(java.lang.String)>: 20
	<java.io.File: void <init>: 67
	<android.location.LocationManager: boolean isProviderEnabled(java.lang.String)>: 10
	<android.webkit.MimeTypeMap: java.lang.String getMimeTypeFromExtension(java.lang.String)>: 1
	<android.location.Address: int getMaxAddressLineIndex()>: 3
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 6
	<java.lang.Class: java.lang.reflect.Constructor getConstructor(java.lang.Class[])>: 4
	<android.telephony.TelephonyManager: int getNetworkType()>: 2
	<android.util.SparseArray: java.lang.Object get(int)>: 8
	<android.telephony.NeighboringCellInfo: int getRssi()>: 4
	<android.media.ExifInterface: java.lang.String getAttribute(java.lang.String)>: 1
	<android.view.VelocityTracker: float getXVelocity()>: 1
## Sinks: 157
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,int)>: 44
	<android.widget.ZoomControls: void setOnZoomOutClickListener(android.view.View$OnClickListener)>: 1
	<java.lang.StringBuffer: void setCharAt(int,char)>: 1
	<javax.xml.parsers.DocumentBuilder: org.w3c.dom.Document parse(java.io.InputStream)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 133
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 5
	<java.io.PrintStream: void println(int)>: 1
	<android.os.Bundle: void putParcelableArray(java.lang.String,android.os.Parcelable[])>: 8
	<android.os.Bundle: void putDouble(java.lang.String,double)>: 16
	<android.graphics.RectF: void set(float,float,float,float)>: 1
	<android.content.Intent: android.content.Intent setType(java.lang.String)>: 1
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 1
	<android.widget.ImageView: void setImageMatrix(android.graphics.Matrix)>: 1
	<android.widget.ListView: void setAdapter(android.widget.ListAdapter)>: 5
	<android.os.Bundle: void putBundle(java.lang.String,android.os.Bundle)>: 21
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 5
	<java.io.PrintStream: void print(java.lang.String)>: 2
	<java.net.URL: java.net.URLConnection openConnection()>: 6
	<java.util.BitSet: void set(int)>: 45
	<android.content.Intent: android.content.Intent parseUri(java.lang.String,int)>: 1
	<android.widget.ImageView: void setScaleType(android.widget.ImageView$ScaleType)>: 2
	<android.graphics.Paint: android.graphics.Typeface setTypeface(android.graphics.Typeface)>: 1
	<java.io.RandomAccessFile: void write(byte[],int,int)>: 5
	<java.lang.Math: double log(double)>: 12
	<java.io.FileWriter: java.io.Writer append(java.lang.CharSequence)>: 11
	<java.util.concurrent.Executors: java.util.concurrent.ExecutorService newSingleThreadExecutor(java.util.concurrent.ThreadFactory)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 12
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 102
	<android.widget.ImageView: void setImageBitmap(android.graphics.Bitmap)>: 6
	<android.location.LocationManager: boolean addGpsStatusListener(android.location.GpsStatus$Listener)>: 1
	<java.lang.Long: long parseLong(java.lang.String,int)>: 2
	<java.util.regex.Matcher: java.lang.String replaceAll(java.lang.String)>: 3
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 46
	<android.widget.ZoomControls: void setOnZoomInClickListener(android.view.View$OnClickListener)>: 1
	<java.lang.Double: double parseDouble(java.lang.String)>: 20
	<org.apache.http.conn.ssl.SSLSocketFactory: void setHostnameVerifier(org.apache.http.conn.ssl.X509HostnameVerifier)>: 1
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 6
	<java.lang.Thread: void setDefaultUncaughtExceptionHandler(java.lang.Thread$UncaughtExceptionHandler)>: 1
	<android.graphics.Paint: void setStrokeCap(android.graphics.Paint$Cap)>: 3
	<android.os.Messenger: void send(android.os.Message)>: 10
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 45
	<android.content.Intent: android.content.Intent setFlags(int)>: 11
	<android.app.AlarmManager: void setRepeating(int,long,long,android.app.PendingIntent)>: 2
	<android.os.Bundle: void putByteArray(java.lang.String,byte[])>: 11
	<android.app.AlarmManager: void set(int,long,android.app.PendingIntent)>: 1
	<java.lang.StringBuilder: void setCharAt(int,char)>: 3
	<java.net.HttpURLConnection: void connect()>: 2
	<android.media.MediaRecorder: void setOutputFile(java.lang.String)>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 54
	<android.content.ContentValues: void put(java.lang.String,java.lang.Float)>: 1
	<android.os.Bundle: void putInt(java.lang.String,int)>: 231
	<android.graphics.Paint: void setStyle(android.graphics.Paint$Style)>: 6
	<android.support.v4.view.ViewPager: void setCurrentItem(int)>: 3
	<java.util.Calendar: void setTime(java.util.Date)>: 1
	<android.os.Bundle: void putFloat(java.lang.String,float)>: 12
	<java.io.ObjectOutputStream: void writeInt(int)>: 5
	<java.util.Calendar: void setTimeInMillis(long)>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 7
	<android.util.Log: int v(java.lang.String,java.lang.String)>: 3
	<java.util.zip.GZIPOutputStream: void write(byte[],int,int)>: 1
	<java.io.PrintStream: void println(java.lang.Object)>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 3
	<android.media.MediaRecorder: void setOnInfoListener(android.media.MediaRecorder$OnInfoListener)>: 1
	<android.os.Bundle: void putLong(java.lang.String,long)>: 4
	<java.lang.Long: long parseLong(java.lang.String)>: 3
	<android.media.ExifInterface: void setAttribute(java.lang.String,java.lang.String)>: 1
	<android.view.GestureDetector: void setOnDoubleTapListener(android.view.GestureDetector$OnDoubleTapListener)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 19
	<org.apache.http.conn.scheme.SchemeRegistry: org.apache.http.conn.scheme.Scheme register(org.apache.http.conn.scheme.Scheme)>: 1
	<android.app.ProgressDialog: void setProgressStyle(int)>: 6
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
	<java.lang.Float: float parseFloat(java.lang.String)>: 20
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 1
	<java.lang.StringBuffer: void setLength(int)>: 1
	<java.io.RandomAccessFile: void writeFloat(float)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 25
	<java.io.FileOutputStream: void write(byte[],int,int)>: 6
	<java.io.OutputStreamWriter: void <init>: 3
	<java.net.ServerSocket: void setSoTimeout(int)>: 1
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 1
	<java.lang.Boolean: boolean parseBoolean(java.lang.String)>: 1
	<android.os.Handler: boolean sendEmptyMessage(int)>: 4
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 2
	<java.io.RandomAccessFile: void writeDouble(double)>: 4
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 23
	<android.location.LocationManager: void requestLocationUpdates(java.lang.String,long,float,android.location.LocationListener)>: 11
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 7
	<android.app.Activity: void startActivity(android.content.Intent)>: 2
	<java.lang.String: java.lang.String replace(char,char)>: 2
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 4
	<android.graphics.Matrix: boolean setRectToRect(android.graphics.RectF,android.graphics.RectF,android.graphics.Matrix$ScaleToFit)>: 4
	<android.view.Window: void setType(int)>: 15
	<android.widget.ImageView: void setImageURI(android.net.Uri)>: 1
	<java.net.URL: void <init>: 7
	<android.location.LocationManager: boolean addNmeaListener(android.location.GpsStatus$NmeaListener)>: 1
	<java.io.OutputStream: void write(byte[])>: 9
	<java.io.ObjectOutputStream: void writeBoolean(boolean)>: 1
	<android.widget.TextView: void setTypeface(android.graphics.Typeface,int)>: 1
	<org.apache.http.params.HttpProtocolParams: void setUseExpectContinue(org.apache.http.params.HttpParams,boolean)>: 2
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 59
	<android.content.Context: void startActivity(android.content.Intent)>: 17
	<android.content.ContentValues: void put(java.lang.String,java.lang.Double)>: 2
	<android.os.Bundle: void putBoolean(java.lang.String,boolean)>: 6
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 23
	<java.io.Writer: void write(java.lang.String)>: 10
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 19
	<org.apache.http.client.params.HttpClientParams: void setCookiePolicy(org.apache.http.params.HttpParams,java.lang.String)>: 1
	<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>: 1
	<java.io.Writer: void write(char[])>: 1
	<android.widget.TextView: void setTextColor(int)>: 17
	<java.lang.Thread: void setName(java.lang.String)>: 4
	<android.database.sqlite.SQLiteDatabase: android.database.sqlite.SQLiteDatabase openOrCreateDatabase(java.io.File,android.database.sqlite.SQLiteDatabase$CursorFactory)>: 3
	<java.lang.String: boolean startsWith(java.lang.String)>: 69
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 3
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<android.graphics.Matrix: void set(android.graphics.Matrix)>: 1
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 24
	<java.util.zip.ZipOutputStream: void write(byte[],int,int)>: 2
	<android.view.Window: void setFlags(int,int)>: 1
	<android.widget.TextView: void setTextSize(float)>: 14
	<java.lang.ProcessBuilder: java.lang.Process start()>: 1
	<java.lang.System: java.lang.String setProperty(java.lang.String,java.lang.String)>: 3
	<java.io.ObjectOutputStream: void writeObject(java.lang.Object)>: 9
	<java.io.PrintWriter: void println(java.lang.String)>: 1
	<java.io.ObjectOutputStream: void writeFloat(float)>: 2
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 17
	<android.widget.ImageView: void setImageResource(int)>: 34
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,double)>: 2
	<java.io.File: boolean delete()>: 7
	<java.io.RandomAccessFile: void write(byte[])>: 2
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 6
	<java.lang.StringBuilder: void setLength(int)>: 11
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,boolean)>: 1
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 7
	<android.widget.ZoomControls: void setIsZoomInEnabled(boolean)>: 1
	<android.os.Bundle: void putIntArray(java.lang.String,int[])>: 12
	<java.io.FileOutputStream: void <init>: 14
	<java.io.FileOutputStream: void write(byte[])>: 6
	<android.location.Location: void setTime(long)>: 1
	<android.widget.ImageView: void setImageDrawable(android.graphics.drawable.Drawable)>: 1
	<android.os.Message: void setData(android.os.Bundle)>: 4
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 1
	<java.net.HttpURLConnection: void setInstanceFollowRedirects(boolean)>: 1
	<android.app.ProgressDialog: void setProgress(int)>: 2
	<android.widget.LinearLayout: void setOrientation(int)>: 10
	<android.net.wifi.WifiManager: boolean setWifiEnabled(boolean)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 59
	<android.widget.ZoomControls: void setIsZoomOutEnabled(boolean)>: 1
	<android.widget.TextView: void setPadding(int,int,int,int)>: 15
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<java.lang.Thread: void setDaemon(boolean)>: 5
	<android.graphics.Paint: void setStrokeJoin(android.graphics.Paint$Join)>: 3
	<android.location.LocationManager: void removeUpdates(android.location.LocationListener)>: 5
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 1

