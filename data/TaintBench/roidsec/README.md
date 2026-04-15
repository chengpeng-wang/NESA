# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: roidsec.apk
- **packageName**: cn.phoneSync
- **targetSdk**: 7
- **minSdk**: 7
- **maxSdk**: undefined
- **mainActivity**: UNKNOWN
# Behavior Information:
## Services:
- PhoneSyncService collects all kinds of information on the device such as call log, contacts, installed apps, GPS location, SMS messages, etc and sends them to a remote server.  
# Detail Information:
## Services: 1
	.PhoneSyncService
## Receivers: 1
	.BootBroadcastReceiver
## Permissions: 17
	android.permission.SEND_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.RECORD_AUDIO
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.ACCESS_FINE_LOCATION
	android.permission.ACCESS_COARSE_LOCATION
	android.permission.CHANGE_WIFI_STATE
	android.permission.INTERNET
	android.permission.WAKE_LOCK
	android.permission.WRITE_SECURE_SETTINGS
	android.permission.ACCESS_WIFI_STATE
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_SETTINGS
## Sources: 40
	<android.telephony.SmsManager: android.telephony.SmsManager getDefault()>: 1
	<android.os.Environment: java.io.File getDataDirectory()>: 2
	<java.lang.String: byte[] getBytes(java.lang.String)>: 18
	<java.io.BufferedReader: java.lang.String readLine()>: 1
	<android.location.Location: double getLatitude()>: 1
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 6
	<android.location.LocationManager: android.location.Location getLastKnownLocation(java.lang.String)>: 1
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<java.net.InetAddress: java.net.InetAddress getByName(java.lang.String)>: 2
	<android.os.StatFs: int getBlockCount()>: 2
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 1
	<android.app.PendingIntent: android.app.PendingIntent getBroadcast(android.content.Context,int,android.content.Intent,int)>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 4
	<android.telephony.TelephonyManager: java.lang.String getDeviceId()>: 1
	<java.net.Socket: java.io.OutputStream getOutputStream()>: 32
	<java.net.Socket: java.io.InputStream getInputStream()>: 2
	<android.location.LocationManager: java.lang.String getBestProvider(android.location.Criteria,boolean)>: 1
	<android.net.wifi.WifiManager: java.util.List getScanResults()>: 1
	<android.telephony.SmsManager: java.util.ArrayList divideMessage(java.lang.String)>: 1
	<java.io.File: boolean delete()>: 3
	<android.os.StatFs: int getBlockSize()>: 2
	<java.io.File: java.io.File[] listFiles()>: 3
	<android.os.Environment: java.lang.String getExternalStorageState()>: 2
	<java.io.File: java.lang.String getName()>: 3
	<android.content.pm.PackageManager: java.util.List getInstalledPackages(int)>: 2
	<android.os.StatFs: int getAvailableBlocks()>: 2
	<android.database.Cursor: java.lang.String getString(int)>: 13
	<java.io.File: void <init>: 5
	<java.io.File: java.lang.String getAbsolutePath()>: 1
	<java.lang.String: byte[] getBytes()>: 19
	<android.location.LocationManager: boolean isProviderEnabled(java.lang.String)>: 3
	<java.io.File: java.lang.String getPath()>: 6
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<android.telephony.TelephonyManager: java.lang.String getLine1Number()>: 1
	<android.os.PowerManager: android.os.PowerManager$WakeLock newWakeLock(int,java.lang.String)>: 1
	<android.telephony.TelephonyManager: java.lang.String getNetworkOperatorName()>: 1
	<android.database.Cursor: int getInt(int)>: 1
	<android.location.Location: double getLongitude()>: 1
	<android.net.wifi.WifiManager: int getWifiState()>: 1
## Sinks: 22
	<java.io.File: boolean delete()>: 3
	<android.media.MediaRecorder: void setOutputFile(java.lang.String)>: 1
	<android.telephony.SmsManager: void sendTextMessage(java.lang.String,java.lang.String,java.lang.String,android.app.PendingIntent,android.app.PendingIntent)>: 1
	<android.net.Uri: android.net.Uri parse(java.lang.String)>: 3
	<java.io.OutputStream: void write(byte[])>: 37
	<android.location.Criteria: void setAltitudeRequired(boolean)>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.io.FileOutputStream: void write(byte[])>: 1
	<android.content.Intent: android.content.Intent setClass(android.content.Context,java.lang.Class)>: 1
	<android.location.Criteria: void setBearingRequired(boolean)>: 1
	<android.location.Criteria: void setPowerRequirement(int)>: 1
	<java.lang.Long: long parseLong(java.lang.String)>: 2
	<android.util.Log: int i(java.lang.String,java.lang.String)>: 5
	<java.lang.ProcessBuilder: java.lang.Process start()>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 1
	<android.location.Criteria: void setCostAllowed(boolean)>: 1
	<android.net.wifi.WifiManager: boolean setWifiEnabled(boolean)>: 2
	<android.content.Context: android.content.ComponentName startService(android.content.Intent)>: 1
	<android.location.Criteria: void setAccuracy(int)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 7
	<java.io.OutputStream: void write(byte[],int,int)>: 3
