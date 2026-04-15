# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: phospy.apk
- **packageName**: com.android.lulaoshi
- **targetSdk**: 8
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: com.labado.lulaoshi.password
# Behavior Information:
## Services:
- myService uploads device information and images from external storage to a remote server. 
# Detail Information:
## Activities: 2
	com.labado.lulaoshi.AsyncListImage
	com.labado.lulaoshi.password
## Services: 1
	com.labado.lulaoshi.myService
## Receivers: 1
	com.labado.lulaoshi.myReceiver
## Permissions: 6
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.ACCESS_WIFI_STATE
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.READ_PHONE_STATE
## Sources: 43
	<java.lang.Thread: java.lang.String getName()>: 2
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 2
	<java.nio.charset.Charset: java.nio.CharBuffer decode(java.nio.ByteBuffer)>: 5
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<java.nio.charset.Charset: java.lang.String name()>: 2
	<java.lang.Class: java.lang.String getSimpleName()>: 1
	<java.net.URL: java.lang.String getPath()>: 2
	<java.net.URL: java.lang.String getAuthority()>: 1
	<java.util.Properties: void load(java.io.InputStream)>: 1
	<java.net.HttpURLConnection: int getResponseCode()>: 3
	<android.view.View: java.lang.Object getTag()>: 2
	<java.net.URL: java.lang.Object getContent()>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<java.net.Socket: java.io.OutputStream getOutputStream()>: 1
	<java.net.HttpURLConnection: java.lang.String getRequestMethod()>: 1
	<java.io.FileInputStream: void <init>: 2
	<java.nio.CharBuffer: java.lang.String toString()>: 5
	<java.util.ArrayList: java.lang.Object get(int)>: 3
	<android.widget.EditText: android.text.Editable getText()>: 1
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 2
	<java.io.File: java.io.File[] listFiles()>: 1
	<java.util.LinkedHashMap: java.lang.Object get(java.lang.Object)>: 2
	<android.widget.TextView: java.lang.CharSequence getText()>: 4
	<java.io.File: java.lang.String getName()>: 1
	<java.net.URL: java.lang.String getProtocol()>: 2
	<java.net.HttpURLConnection: java.io.InputStream getErrorStream()>: 2
	<java.net.HttpURLConnection: java.lang.String getResponseMessage()>: 1
	<java.lang.Class: java.io.InputStream getResourceAsStream(java.lang.String)>: 1
	<java.io.File: java.lang.String getAbsolutePath()>: 4
	<java.io.File: void <init>: 1
	<java.util.HashMap: java.lang.Object get(java.lang.Object)>: 1
	<java.io.File: java.lang.String getPath()>: 1
	<java.io.DataInputStream: int read(byte[])>: 1
	<android.app.Activity: android.view.LayoutInflater getLayoutInflater()>: 1
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<java.net.URL: java.lang.String getQuery()>: 2
	<android.net.ConnectivityManager: android.net.NetworkInfo[] getAllNetworkInfo()>: 2
	<android.net.NetworkInfo: java.lang.String getTypeName()>: 2
	<java.lang.Integer: int parseInt(java.lang.String)>: 4
	<android.graphics.drawable.Drawable: android.graphics.drawable.Drawable createFromStream(java.io.InputStream,java.lang.String)>: 3
	<java.util.LinkedList: java.lang.Object get(int)>: 9
	<android.net.wifi.WifiManager: int getWifiState()>: 2
## Sinks: 37
	<java.lang.String: boolean startsWith(java.lang.String)>: 15
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 1
	<android.os.Handler: boolean sendMessage(android.os.Message)>: 1
	<android.view.Window: void setFlags(int,int)>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 11
	<android.os.StrictMode: void setVmPolicy(android.os.StrictMode$VmPolicy)>: 1
	<java.io.OutputStreamWriter: void <init>: 1
	<android.os.Handler: boolean sendEmptyMessage(int)>: 3
	<android.content.Intent: android.content.Intent setFlags(int)>: 1
	<android.view.View: void setTag(java.lang.Object)>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 9
	<java.io.DataOutputStream: void flush()>: 3
	<java.io.DataOutputStream: void write(byte[],int,int)>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<android.app.Activity: void startActivity(android.content.Intent)>: 1
	<android.widget.ImageView: void setImageResource(int)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<java.io.OutputStreamWriter: void write(java.lang.String)>: 2
	<android.widget.ListView: void setAdapter(android.widget.ListAdapter)>: 1
	<java.net.HttpURLConnection: void connect()>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 10
	<java.net.URL: void <init>: 7
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<java.io.DataOutputStream: void writeUTF(java.lang.String)>: 2
	<java.net.HttpURLConnection: java.io.InputStream getInputStream()>: 2
	<android.widget.ImageView: void setImageDrawable(android.graphics.drawable.Drawable)>: 3
	<java.net.HttpURLConnection: void setInstanceFollowRedirects(boolean)>: 1
	<android.os.StrictMode: void setThreadPolicy(android.os.StrictMode$ThreadPolicy)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 4
	<android.app.Activity: void onCreate(android.os.Bundle)>: 2
	<android.content.SharedPreferences$Editor: boolean commit()>: 3
	<android.widget.ArrayAdapter: void setDropDownViewResource(int)>: 1
	<java.net.HttpURLConnection: java.io.OutputStream getOutputStream()>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 5
	<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>: 1
	<java.net.HttpURLConnection: void setRequestMethod(java.lang.String)>: 1
