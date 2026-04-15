# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: beita_com_beita_contact.apk
- **packageName**: com.beita.contact
- **targetSdk**: undefined
- **minSdk**: 5
- **maxSdk**: undefined
- **mainActivity**: .MyContacts
# Behavior Information:
## Activities:
- When the malware executes, it disguises itself as a contact management tool.
It then gathers information from the user's list of contacts.
It saves the contacts information in a plain text file called contact_backup.txt. The file is stored in the root path of the device's SD card. Next, it uploads the file to a URL http#://192.168.2.105:8080/upload_file_service/UploadServlet.
# Detail Information:
## Activities: 3
	.ContactEditor
	.MyContacts
	com.beita.contact.ContactView
## Permissions: 4
	android.permission.READ_CONTACTS
	android.permission.INTERNET
	android.permission.WRITE_CONTACTS
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 82
	<org.json.JSONObject: int getInt(java.lang.String)>: 1
	<android.content.Intent: java.lang.String getStringExtra(java.lang.String)>: 5
	<android.location.Location: double getLatitude()>: 3
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 4
	<org.json.JSONArray: java.lang.String getString(int)>: 2
	<android.location.LocationManager: android.location.Location getLastKnownLocation(java.lang.String)>: 2
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 2
	<android.os.HandlerThread: android.os.Looper getLooper()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<java.util.Date: long getTime()>: 5
	<android.net.wifi.WifiManager: android.net.wifi.WifiInfo getConnectionInfo()>: 1
	<java.lang.Class: java.lang.String getSimpleName()>: 4
	<java.text.DateFormat: java.lang.String format(java.util.Date)>: 1
	<java.util.Calendar: java.util.TimeZone getTimeZone()>: 1
	<android.os.Message: android.os.Bundle getData()>: 1
	<android.app.AlertDialog$Builder: android.app.AlertDialog show()>: 1
	<android.net.NetworkInfo: int getType()>: 2
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<java.lang.Thread: java.lang.ClassLoader getContextClassLoader()>: 1
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 2
	<org.json.JSONArray: org.json.JSONArray put(int,java.lang.Object)>: 1
	<java.io.FileInputStream: void <init>: 1
	<java.lang.ClassLoader: java.lang.ClassLoader getSystemClassLoader()>: 3
	<java.util.ResourceBundle: java.util.ResourceBundle getBundle(java.lang.String,java.util.Locale,java.lang.ClassLoader)>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo getNetworkInfo(int)>: 8
	<java.lang.String: void getChars(int,int,char[],int)>: 1
	<java.io.File: java.lang.String getName()>: 2
	<org.json.JSONObject: java.lang.String getString(java.lang.String)>: 16
	<android.database.Cursor: java.lang.String getString(int)>: 19
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 2
	<java.util.Locale: java.util.Locale getDefault()>: 1
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<java.lang.String: byte[] getBytes()>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 2
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 3
	<java.security.MessageDigest: byte[] digest()>: 1
	<org.json.JSONArray: org.json.JSONArray getJSONArray(int)>: 2
	<android.net.wifi.WifiInfo: java.lang.String getMacAddress()>: 1
	<android.view.Display: void getMetrics(android.util.DisplayMetrics)>: 1
	<java.util.Hashtable: java.lang.Object get(java.lang.Object)>: 3
	<android.content.Intent: android.net.Uri getData()>: 11
	<java.io.ObjectInputStream: java.lang.Object readObject()>: 1
	<java.lang.System: java.lang.String getProperty(java.lang.String)>: 1
	<java.io.File: java.lang.String getParent()>: 2
	<java.io.BufferedReader: java.lang.String readLine()>: 7
	<java.lang.String: byte[] getBytes(java.lang.String)>: 4
	<android.net.NetworkInfo: java.lang.String getSubtypeName()>: 1
	<android.net.NetworkInfo: android.net.NetworkInfo$State getState()>: 4
	<java.lang.Runtime: java.lang.Runtime getRuntime()>: 5
	<java.lang.Double: double parseDouble(java.lang.String)>: 2
	<java.util.Calendar: java.util.Calendar getInstance(java.util.Locale)>: 1
	<java.lang.Class: java.lang.String getName()>: 7
	<android.content.Context: java.io.FileInputStream openFileInput(java.lang.String)>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 4
	<android.content.Intent: java.lang.String getAction()>: 2
	<android.view.View: java.lang.Object getTag()>: 5
	<android.net.NetworkInfo: java.lang.String getExtraInfo()>: 2
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 2
	<java.util.Locale: java.lang.String getCountry()>: 1
	<android.content.res.Resources: java.lang.String[] getStringArray(int)>: 2
	<android.widget.EditText: android.text.Editable getText()>: 22
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 12
	<java.io.File: boolean delete()>: 2
	<android.content.ContentResolver: android.net.Uri insert(android.net.Uri,android.content.ContentValues)>: 1
	<android.os.Environment: java.lang.String getExternalStorageState()>: 4
	<android.view.KeyEvent: int getRepeatCount()>: 1
	<java.io.File: void <init>: 5
	<java.io.File: java.lang.String getAbsolutePath()>: 3
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<java.lang.reflect.Field: java.lang.String getName()>: 2
	<java.lang.Class: java.lang.reflect.Field getField(java.lang.String)>: 2
	<android.app.PendingIntent: android.app.PendingIntent getActivity(android.content.Context,int,android.content.Intent,int)>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 1
	<android.database.sqlite.SQLiteQueryBuilder: android.database.Cursor query(android.database.sqlite.SQLiteDatabase,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String,java.lang.String,java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.content.res.Resources: android.content.res.Configuration getConfiguration()>: 2
	<android.content.Context: java.lang.String getString(int)>: 23
	<java.util.ResourceBundle: java.lang.String getString(java.lang.String)>: 2
	<java.lang.ClassLoader: java.lang.Class loadClass(java.lang.String)>: 6
	<android.location.Location: double getLongitude()>: 3
	<org.json.JSONObject: org.json.JSONArray getJSONArray(java.lang.String)>: 2
	<org.json.JSONArray: org.json.JSONObject getJSONObject(int)>: 6
## Sinks: 70
	<android.content.Intent: android.content.Intent setData(android.net.Uri)>: 1
	<javax.mail.Transport: void sendMessage(javax.mail.Message,javax.mail.Address[])>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,int)>: 14
	<android.util.Log: int e(java.lang.String,java.lang.String,java.lang.Throwable)>: 11
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<java.io.BufferedWriter: void write(java.lang.String)>: 1
	<java.io.FileOutputStream: void write(byte[],int,int)>: 2
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 8
	<android.database.sqlite.SQLiteDatabase: long insert(java.lang.String,java.lang.String,android.content.ContentValues)>: 1
	<java.io.OutputStreamWriter: void <init>: 1
	<android.util.Log: int w(java.lang.String,java.lang.String)>: 11
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 30
	<android.os.Handler: boolean sendEmptyMessage(int)>: 1
	<android.view.View: void setTag(java.lang.Object)>: 2
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 3
	<android.widget.RemoteViews: void setImageViewResource(int,int)>: 2
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 8
	<android.widget.LinearLayout: void setGravity(int)>: 2
	<android.content.UriMatcher: void addURI(java.lang.String,java.lang.String,int)>: 2
	<android.content.ContentValues: void put(java.lang.String,java.lang.String)>: 24
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 2
	<android.app.Notification: void setLatestEventInfo(android.content.Context,java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent)>: 1
	<java.net.URL: void <init>: 5
	<java.net.URL: java.net.URLConnection openConnection()>: 3
	<android.database.sqlite.SQLiteQueryBuilder: void setTables(java.lang.String)>: 1
	<java.io.DataOutputStream: void writeBytes(java.lang.String)>: 5
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,java.lang.Object)>: 86
	<android.content.Context: void startActivity(android.content.Intent)>: 9
	<org.apache.http.client.HttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 3
	<android.app.Activity: void onCreate(android.os.Bundle)>: 3
	<java.util.zip.Deflater: void setInput(byte[])>: 1
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 1
	<android.util.Log: int i(java.lang.String,java.lang.String,java.lang.Throwable)>: 3
	<android.widget.ArrayAdapter: void setDropDownViewResource(int)>: 2
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 72
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 7
	<android.widget.TextView: void setTextColor(int)>: 3
	<android.app.NotificationManager: void notify(int,android.app.Notification)>: 3
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 4
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 3
	<java.lang.Double: double parseDouble(java.lang.String)>: 2
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 1
	<org.apache.http.conn.params.ConnManagerParams: void setTimeout(org.apache.http.params.HttpParams,long)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 6
	<java.io.ObjectOutputStream: void writeObject(java.lang.Object)>: 1
	<java.io.DataOutputStream: void write(byte[],int,int)>: 2
	<java.io.DataOutputStream: void flush()>: 1
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,double)>: 3
	<java.net.HttpURLConnection: void connect()>: 2
	<android.view.View: void setVisibility(int)>: 4
	<org.json.JSONObject: org.json.JSONObject put(java.lang.String,long)>: 1
	<java.io.File: boolean delete()>: 2
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 21
	<android.widget.ImageView: void setVisibility(int)>: 2
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.lang.String)>: 13
	<android.widget.RemoteViews: void setTextViewText(int,java.lang.CharSequence)>: 5
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<java.io.FileOutputStream: void <init>: 2
	<java.io.FileOutputStream: void write(byte[])>: 1
	<android.os.Message: void setData(android.os.Bundle)>: 1
	<android.database.sqlite.SQLiteDatabase: int update(java.lang.String,android.content.ContentValues,java.lang.String,java.lang.String[])>: 2
	<android.content.ContentResolver: void notifyChange(android.net.Uri,android.database.ContentObserver)>: 3
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.os.AsyncTask: void onPostExecute(java.lang.Object)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 22
	<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>: 10
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<android.widget.RemoteViews: void setProgressBar(int,int,int,boolean)>: 2
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 3
