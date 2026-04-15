# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: chulia.apk
- **packageName**: com.google.services
- **targetSdk**: 15
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: .turntest
# Behavior Information:
## Services:
- AlarmService monitors incoming SMS messages.
- PhoneService sends collected data to a remote server.
# Detail Information:
## Activities: 1
	.turntest
## Services: 2
	.AlarmService
	com.google.services.PhoneService
## Receivers: 1
	.ScreenReceiver
## Permissions: 10
	android.permission.RECEIVE_SMS
	android.permission.ACCESS_FINE_LOCATION
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	READ_PHONE_STATE
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 37
	<java.lang.String: byte[] getBytes(java.lang.String)>: 5
	<android.location.Location: double getLatitude()>: 1
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 4
	<android.location.LocationManager: android.location.Location getLastKnownLocation(java.lang.String)>: 1
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 4
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<java.util.Date: long getTime()>: 1
	<android.view.MotionEvent: int getAction()>: 6
	<android.telephony.SmsMessage: java.lang.String getOriginatingAddress()>: 1
	<android.content.ComponentName: java.lang.String getClassName()>: 1
	<android.content.Intent: java.lang.String getAction()>: 2
	<android.view.MotionEvent: float getY()>: 3
	<android.telephony.SmsMessage: java.lang.String getMessageBody()>: 1
	<android.content.res.Resources: android.content.res.AssetManager getAssets()>: 1
	<android.location.LocationManager: java.lang.String getBestProvider(android.location.Criteria,boolean)>: 1
	<android.database.Cursor: long getLong(int)>: 1
	<android.telephony.SmsMessage: android.telephony.SmsMessage createFromPdu(byte[])>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<java.io.FileInputStream: void <init>: 3
	<android.content.Context: java.lang.Object getSystemService(java.lang.String)>: 2
	<android.widget.Scroller: int getCurrY()>: 1
	<android.os.Bundle: java.lang.String getString(java.lang.String)>: 6
	<android.database.Cursor: java.lang.String getString(int)>: 12
	<java.io.RandomAccessFile: java.nio.channels.FileChannel getChannel()>: 1
	<java.io.File: void <init>: 2
	<java.lang.String: byte[] getBytes()>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<android.net.ConnectivityManager: android.net.NetworkInfo getActiveNetworkInfo()>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<java.util.Vector: java.lang.Object get(int)>: 1
	<android.content.Intent: android.os.Bundle getExtras()>: 2
	<android.os.Bundle: java.lang.Object get(java.lang.String)>: 4
	<android.view.MotionEvent: float getX()>: 3
	<android.database.Cursor: int getInt(int)>: 2
	<android.graphics.Bitmap: android.graphics.Bitmap createBitmap(int,int,android.graphics.Bitmap$Config)>: 2
	<android.location.Location: double getLongitude()>: 1
	<android.widget.Scroller: int getCurrX()>: 1
## Sinks: 31
	<android.content.Intent: android.content.Intent setAction(java.lang.String)>: 5
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 2
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 2
	<java.lang.String: java.lang.String substring(int,int)>: 2
	<android.view.Window: void setFlags(int,int)>: 1
	<android.location.Criteria: void setBearingRequired(boolean)>: 1
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 28
	<android.graphics.ColorMatrix: void set(float[])>: 1
	<android.location.LocationManager: void requestLocationUpdates(java.lang.String,long,float,android.location.LocationListener)>: 1
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<android.app.ActivityManager: java.util.List getRunningServices(int)>: 1
	<android.location.Criteria: void setAccuracy(int)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 1
	<android.graphics.drawable.GradientDrawable: void setGradientType(int)>: 8
	<android.graphics.Paint: void setStyle(android.graphics.Paint$Style)>: 1
	<java.io.OutputStream: void write(byte[])>: 1
	<android.content.IntentFilter: void setPriority(int)>: 3
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<android.location.Criteria: void setAltitudeRequired(boolean)>: 1
	<android.graphics.Paint: android.graphics.ColorFilter setColorFilter(android.graphics.ColorFilter)>: 2
	<android.graphics.Paint: void setTextAlign(android.graphics.Paint$Align)>: 1
	<java.io.FileOutputStream: void <init>: 3
	<android.location.Criteria: void setPowerRequirement(int)>: 1
	<java.io.RandomAccessFile: void write(byte[],int,int)>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<android.location.Criteria: void setCostAllowed(boolean)>: 1
	<android.os.Bundle: void putString(java.lang.String,java.lang.String)>: 5
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.SharedPreferences$Editor: boolean commit()>: 1
	<android.content.Intent: android.content.Intent putExtras(android.os.Bundle)>: 5
	<android.graphics.Matrix: void setValues(float[])>: 1
