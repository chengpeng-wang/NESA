# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: the_interview_movieshow.apk
- **packageName**: com.movieshow.down
- **targetSdk**: 15
- **minSdk**: 10
- **maxSdk**: undefined
- **mainActivity**: com.movieshow.down.Badaccents
# Behavior Information:
## Activities:
- Badaccents claims to download a copy of the movie “The Interview” but instead installs a twostage banking Trojan onto victims’ devices.
# Detail Information:
## Activities: 1
	com.movieshow.down.Badaccents
## Permissions: 3
	android.permission.INTERNET
	android.permission.INSTALL_PACKAGES
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 6
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 3
	<java.net.URL: java.io.InputStream openStream()>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<java.net.URLConnection: int getContentLength()>: 1
	<java.io.File: void <init>: 2
	<java.io.File: java.lang.String getPath()>: 3
## Sinks: 16
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 1
	<android.app.ProgressDialog: void setMax(int)>: 1
	<java.lang.String: boolean startsWith(java.lang.String)>: 1
	<android.app.ProgressDialog: void setIndeterminate(boolean)>: 1
	<java.net.URL: void <init>: 1
	<java.net.URL: java.net.URLConnection openConnection()>: 1
	<java.io.FileOutputStream: void <init>: 1
	<android.app.ProgressDialog: void setProgress(int)>: 1
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 3
	<java.lang.Integer: int parseInt(java.lang.String)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.app.ProgressDialog: void setProgressStyle(int)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<com.movieshow.down.Badaccents: void startActivity(android.content.Intent)>: 1
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 1
