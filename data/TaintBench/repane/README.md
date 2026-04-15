# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: repane.apk
- **packageName**: com.vr.installer.scanner
- **targetSdk**: 8
- **minSdk**: 8
- **maxSdk**: undefined
- **mainActivity**: .MainActivity
# Behavior Information:
## Activities:
- When the malware is executed, it asks the user to install additional libraries.Once the user clicks Install, two additional malicious applications are installed on the compromised device.
# Detail Information:
## Activities: 1
	.MainActivity
## Permissions: 2
	android.permission.READ_PHONE_STATE
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 11
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<java.io.File: boolean delete()>: 1
	<java.security.MessageDigest: byte[] digest(byte[])>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>: 1
	<java.io.File: void <init>: 2
	<java.lang.Class: java.lang.String getName()>: 3
	<java.lang.String: byte[] getBytes()>: 1
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.io.File: java.lang.String getPath()>: 2
	<java.util.ArrayList: java.lang.Object get(int)>: 5
## Sinks: 17
	<java.io.File: boolean delete()>: 1
	<android.widget.TextView: void setText(java.lang.CharSequence)>: 4
	<android.widget.ProgressBar: void setProgress(int)>: 1
	<android.widget.TextView: void setText(int)>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.io.FileOutputStream: void write(byte[],int,int)>: 2
	<android.widget.Toast: void setGravity(int,int,int)>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<android.content.Context: void startActivity(android.content.Intent)>: 2
	<android.content.Intent: android.content.Intent setFlags(int)>: 2
	<android.widget.Toast: android.widget.Toast makeText(android.content.Context,java.lang.CharSequence,int)>: 1
	<java.lang.Integer: int parseInt(java.lang.String)>: 2
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.content.Intent: android.content.Intent setDataAndType(android.net.Uri,java.lang.String)>: 1
	<android.view.View: void setVisibility(int)>: 4
	<android.util.Log: int e(java.lang.String,java.lang.String)>: 3
