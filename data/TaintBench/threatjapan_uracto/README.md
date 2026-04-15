# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: threatjapan_uracto.apk
- **packageName**: solution.newsandroid
- **targetSdk**: 15
- **minSdk**: 7
- **maxSdk**: undefined
- **mainActivity**: .MainviewActivity
# Behavior Information:
## Activities:
- The malware collects emails and sends them to a remote server. 
# Detail Information:
## Activities: 1
	.MainviewActivity
## Permissions: 4
	android.permission.READ_CONTACTS
	android.permission.INTERNET
	android.permission.ACCESS_NETWORK_STATE
	android.permission.WRITE_EXTERNAL_STORAGE
## Sources: 16
	<java.lang.String: byte[] getBytes(java.lang.String)>: 1
	<java.io.BufferedReader: java.lang.String readLine()>: 2
	<java.io.File: java.lang.String getName()>: 2
	<android.content.ContentResolver: android.database.Cursor query(android.net.Uri,java.lang.String[],java.lang.String,java.lang.String[],java.lang.String)>: 1
	<java.io.ByteArrayOutputStream: byte[] toByteArray()>: 1
	<java.nio.charset.Charset: java.lang.String name()>: 4
	<android.database.Cursor: java.lang.String getString(int)>: 6
	<java.io.File: java.lang.String getAbsolutePath()>: 1
	<java.io.File: void <init>: 2
	<java.io.File: java.lang.String getPath()>: 1
	<android.os.Environment: java.io.File getExternalStorageDirectory()>: 2
	<android.app.AlertDialog$Builder: android.app.AlertDialog show()>: 1
	<java.io.File: java.io.File getParentFile()>: 1
	<org.apache.http.HttpEntity: java.io.InputStream getContent()>: 1
	<org.apache.http.util.ByteArrayBuffer: byte[] buffer()>: 1
	<java.io.FileInputStream: void <init>: 2
## Sinks: 14
	<android.app.ProgressDialog: void setMessage(java.lang.CharSequence)>: 1
	<org.apache.http.params.HttpConnectionParams: void setConnectionTimeout(org.apache.http.params.HttpParams,int)>: 1
	<java.io.OutputStream: void write(byte[])>: 1
	<org.apache.http.impl.client.DefaultHttpClient: org.apache.http.HttpResponse execute(org.apache.http.client.methods.HttpUriRequest)>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.lang.String: java.lang.String substring(int,int)>: 1
	<java.io.OutputStreamWriter: void <init>: 1
	<java.io.PrintWriter: java.io.PrintWriter append(java.lang.CharSequence)>: 1
	<android.os.Handler: boolean sendEmptyMessage(int)>: 1
	<android.util.Log: int d(java.lang.String,java.lang.String)>: 11
	<org.apache.http.params.HttpConnectionParams: void setSoTimeout(org.apache.http.params.HttpParams,int)>: 1
	<android.app.Activity: void onCreate(android.os.Bundle)>: 1
	<android.app.ProgressDialog: void setProgressStyle(int)>: 1
	<java.io.OutputStream: void write(byte[],int,int)>: 4

