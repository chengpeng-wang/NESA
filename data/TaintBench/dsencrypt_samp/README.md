# PROFILE
# Installation:
![ICON](icon.png)
# General Information:
- **fileName**: dsencrypt_samp.apk
- **packageName**: com.sdwiurse
- **targetSdk**: undefined
- **minSdk**: undefined
- **maxSdk**: undefined
- **mainActivity**: com.kbstar.kb.android.star.MainA
# Behavior Information:
- The malware app disguises itself as the Google Play store app, placing its similar icon close to the real Google Play store icon on the
homescreen. Once installed, the hacker uses a dynamic DNS server with the Gmail SSL protocol to collect text messages, signature
certificates and bank passwords from the Android devices. 
- The malicious code is encrypted in a file named "ds" as asset of the app. 
- Android users can’t remove the app once the device is infected because the “uninstall” function is disabled and the app continues to run as services in the back-end. These services can be killed manually but will restart once the Android phone is restarted.

# Detail Information:
## Activities: 11
	com.kbstar.kb.android.star.V_Dialog
	com.kbstar.kb.android.star.KB_Account_info
	com.kbstar.kb.android.star.BKMain
	com.kbstar.kb.android.star.KB_Account_Psw
	com.kbstar.kb.android.star.KB_Last
	com.kbstar.kb.android.star.MainActivity
	com.kbstar.kb.android.star.MainA
	com.kbstar.kb.android.star.OpenMain
	com.kbstar.kb.android.star.KB_Card_Psw
	com.kbstar.kb.android.star.KB_Cert_List
	com.kbstar.kb.android.star.KB_Cert_Psw
## Services: 6
	com.kbstar.kb.android.services.autoRunService
	com.kbstar.kb.android.services.ABK_SENDSMS
	com.kbstar.kb.android.services.uploadPhone
	com.kbstar.kb.android.services.UninstallerService
	com.kbstar.kb.android.services.SoftService
	com.kbstar.kb.android.services.uploadContentService
## Receivers: 3
	com.kbstar.kb.android.receiver.SystemReceiver
	com.kbstar.kb.android.receiver.openActivityReceiver
	com.kbstar.kb.android.star.MDAR
## Permissions: 14
	android.permission.RECEIVE_SMS
	android.permission.SEND_SMS
	android.permission.READ_CONTACTS
	android.permission.READ_SMS
	android.permission.CALL_PHONE
	android.permission.SYSTEM_ALERT_WINDOW
	android.permission.MOUNT_UNMOUNT_FILESYSTEMS
	android.permission.RECEIVE_BOOT_COMPLETED
	android.permission.WRITE_EXTERNAL_STORAGE
	android.permission.INTERNET
	android.permission.GET_TASKS
	android.permission.ACCESS_WIFI_STATE
	android.permission.READ_PHONE_STATE
	android.permission.MOUNT_FORMAT_FILESYSTEMS
## Sources: 18
	<java.io.File: boolean delete()>: 2
	<dalvik.system.DexFile: java.lang.Class loadClass(java.lang.String,java.lang.ClassLoader)>: 1
	<android.content.res.Resources: android.util.DisplayMetrics getDisplayMetrics()>: 2
	<java.lang.Package: java.lang.String getName()>: 1
	<javax.crypto.Cipher: byte[] doFinal(byte[])>: 1
	<java.lang.Class: java.lang.Package getPackage()>: 1
	<java.lang.Class: java.lang.reflect.Field getDeclaredField(java.lang.String)>: 4
	<java.io.File: void <init>: 3
	<java.lang.Class: java.lang.reflect.Method getDeclaredMethod(java.lang.String,java.lang.Class[])>: 1
	<java.lang.String: byte[] getBytes()>: 2
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<java.lang.reflect.Field: java.lang.Object get(java.lang.Object)>: 2
	<java.lang.Class: java.lang.reflect.Constructor getConstructor(java.lang.Class[])>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 4
	<android.content.res.Resources: android.content.res.Configuration getConfiguration()>: 2
	<javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>: 1
	<java.lang.Class: java.lang.reflect.Method getMethod(java.lang.String,java.lang.Class[])>: 3
## Sinks: 9
	<java.io.File: boolean delete()>: 2
	<java.lang.ProcessBuilder: java.lang.Process start()>: 1
	<java.lang.Integer: int parseInt(java.lang.String,int)>: 1
	<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>: 4
	<android.content.res.AssetManager: java.io.InputStream open(java.lang.String)>: 1
	<java.io.FileOutputStream: void <init>: 1
	<java.io.FileOutputStream: void write(byte[])>: 1
	<java.lang.Class: java.lang.Class forName(java.lang.String)>: 9
	<java.lang.reflect.Field: void set(java.lang.Object,java.lang.Object)>: 2
